package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.LoginRequestDTO;
import com.lms.lms_backend.dto.LoginResponseDTO;
import com.lms.lms_backend.dto.RegisterRequestDTO;
import com.lms.lms_backend.dto.UserResponseDTO;
import com.lms.lms_backend.entity.RefreshToken;
import com.lms.lms_backend.repository.RefreshTokenRepository;
import com.lms.lms_backend.security.JwtUtil;
import com.lms.lms_backend.service.RefreshTokenService;
import com.lms.lms_backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;


    public AuthController(UserService userService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
//        return ResponseEntity.ok(userService.login(request));
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {

        LoginResponseDTO loginResponse = userService.login(request);

        // 2. create Refresh Token in DB
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());

        // 3. create HttpOnly Cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false) // 🔥 In Production (HTTPS) should "true" --> this set to "true" when you deploy in secured "HTTPS" environment, now we this "false" because now we are in development "HTTP" environment
                .path("/api/auth")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Lax") // blocking CSRF attacks
                .build();

        // 4. put cookie to Header
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // only send Access Token to frontend (Refresh Token is put in Cookie)
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        // 1. get cookies from Request
        Cookie[] cookies = request.getCookies();
        String refreshTokenString = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenString = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshTokenString == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token is missing!");
        }

        // 2. check whether Token is valid or not?
        return refreshTokenRepository.findByToken(refreshTokenString)
                .map(refreshTokenService::verifyExpiration) // check Expire?
                .map(RefreshToken::getUser) // get User
                .map(user -> {

                    String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());


                    LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                            .token(newAccessToken)
                            .role(user.getRole().name())
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .message("Login successful")
                            .build();

                    return ResponseEntity.ok(responseDTO);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String refreshTokenString = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenString = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshTokenString != null) {
            // 1. Delete token from DB
            refreshTokenRepository.findByToken(refreshTokenString)
                    .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUser().getId()));
        }

        // 2. Clear Cookie from browser
        ResponseCookie cleanCookie = ResponseCookie.from("refreshToken", "") // set value to null
                .httpOnly(true)
                .secure(false) // 🔥 In Production (HTTPS) should "true" --> this set to "true" when you deploy in secured "HTTPS" environment, now we this "false" because now we are in development "HTTP" environment
                .path("/api/auth/")
                .maxAge(0) // immediately expire the cookie in browser
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cleanCookie.toString());

        return ResponseEntity.ok("Successfully logged out!");
    }


}

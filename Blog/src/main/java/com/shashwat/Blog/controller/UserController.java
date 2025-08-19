package com.shashwat.Blog.controller;

import com.shashwat.Blog.model.CustomUserDetails;
import com.shashwat.Blog.model.User;
import com.shashwat.Blog.model.UserDto;
import com.shashwat.Blog.service.UserService;
import com.shashwat.Blog.security.JwtUtil;

import com.shashwat.Blog.util.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // for redux persistent after refresh
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(new UserDto(userDetails.getId(), userDetails.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // ✅ Signup endpoint
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody User user) {

        System.out.println("inside user controller");
        return userService.signUp(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody User user, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                User dbUser= userService.findUserByEmail(user.getEmail());
                if(dbUser==null){
                    return new ResponseEntity<>(new ApiResponse(false,"User not found",null),HttpStatus.BAD_REQUEST);
                }

                String token = jwtService.generateToken(dbUser.getId(),dbUser.getEmail());
                System.out.println(dbUser.getId());

                // Set token in HttpOnly cookie
                ResponseCookie cookie = ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(false) // set to true in production with HTTPS
                        .path("/")
                        .maxAge(24 * 60 * 60) // 1 day
                        .sameSite("Strict")
                        .build();

                response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                UserDto dto=new UserDto(dbUser.getId(), dbUser.getEmail());
                return new ResponseEntity<>(new ApiResponse(true,"Login success",dto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false,"Invalid credentials",null),HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false,"Something went wrong",null),HttpStatus.BAD_REQUEST);
        }
    }


    // UserController.java
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        // Create an expired cookie with same name to clear it
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) // set to true in production
                .path("/")
                .maxAge(0) // immediately expire
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(
                new ApiResponse(true, "Logout successful", null),
                HttpStatus.OK
        );
    }


}

/*
 * Author: Hỏi Dân IT - @hoidanit 
 *
 * This source code is developed for the course
 * "Java Spring Siêu Tốc - Tự Học Java Spring Từ Số 0 Dành Cho Beginners từ A tới Z".
 * It is intended for educational purposes only.
 * Unauthorized distribution, reproduction, or modification is strictly prohibited.
 *
 * Copyright (c) 2025 Hỏi Dân IT. All Rights Reserved.
 */

package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.springsieutoc.config.JwtConfig;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.DTO.LoginRequestDTO;
import vn.hoidanit.springsieutoc.model.DTO.LoginResponseDTO;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final JwtConfig jwtConfig;
	private final DaoAuthenticationProvider authenticationManager;

	@Value("${hoidanit.secret:default-value}")
	private String name;

	@PostMapping("/auth/login")
	public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestDTO dto) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				dto.getUsername(), dto.getPassword());

		Authentication authentication = authenticationManager.authenticate(authToken);

		String accessToken = this.jwtConfig.createAccessToken(authentication);

		LoginResponseDTO res = new LoginResponseDTO(); // tạo để gửi ra phản hồi
		res.setAccessToken(accessToken);
		res.setUser(new LoginResponseDTO.UserLogin(
				authentication.getName(), "Sai"
		));

		return ApiResponse.success(res);
	}

	@GetMapping("/hoidanit/1")
	public ResponseEntity<String> demoAPI() {
//		return new ResponseEntity<String>("Hello Vuong", HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.CREATED).body("Hello update");
	}
}

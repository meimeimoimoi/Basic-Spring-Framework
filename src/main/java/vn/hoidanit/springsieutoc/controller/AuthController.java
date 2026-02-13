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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.springsieutoc.config.JwtService;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.DTO.ExchangeTokenResponse;
import vn.hoidanit.springsieutoc.model.DTO.LoginRequestDTO;
import vn.hoidanit.springsieutoc.model.DTO.LoginResponseDTO;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;


	@PostMapping("/auth/login")
	public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestDTO dto) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				dto.getUsername(), dto.getPassword());

		Authentication authentication = authenticationManager.authenticate(authToken);

		User currentUser = this.userService.findUserByEmail(authentication.getName());

		String accessToken = this.jwtService.createAccessToken(authentication, currentUser.getId());

		String refreshToken = this.jwtService.createRefreshToken(currentUser);

		LoginResponseDTO res = new LoginResponseDTO(); // tạo để gửi ra phản hồi
		res.setAccessToken(accessToken);
		res.setUser(new LoginResponseDTO.UserLogin(
				currentUser.getId(),
				authentication.getName(),
				this.jwtService.getScope(authentication)
		));

		res.setRefreshToken(refreshToken);
		return ApiResponse.success(res);
	}

	@PostMapping("/auth/refresh")
	public ResponseEntity<?> postRefreshToken(@RequestParam("token") String refreshToken){
		ExchangeTokenResponse res = this.jwtService.hanldeExchangeToken(refreshToken);
		return ApiResponse.success(res);
	}
}

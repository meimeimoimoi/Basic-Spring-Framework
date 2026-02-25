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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.springsieutoc.config.JwtService;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.DTO.ExchangeTokenResponse;
import vn.hoidanit.springsieutoc.model.DTO.LoginRequestDTO;
import vn.hoidanit.springsieutoc.model.DTO.LoginResponseDTO;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.service.RefreshTokenService;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final RefreshTokenService refreshTokenService;

	@Value("${hoidanit.jwt.refesh-token-validity-in-seconds}")
	private Long refreshTokenExpiration;

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

		//set cookies
		ResponseCookie resCookies = ResponseCookie
				.from("refreshToken", refreshToken)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(refreshTokenExpiration)
				.build();

		ApiResponse<LoginResponseDTO> finalData = new ApiResponse<>(
				HttpStatus.OK, "", res, ""
		);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString())
				.body(finalData);
	}

	@PostMapping("/auth/refresh")
	public ResponseEntity<?> postRefreshToken(@RequestParam("token") String refreshToken){
		ExchangeTokenResponse res = this.jwtService.hanldeExchangeToken(refreshToken);
		return ApiResponse.success(res);
	}

	@PostMapping("/auth/refresh-cookie")
	public ResponseEntity<?> postRefreshTokenWithCookie(
			@CookieValue(required = false) String refreshToken){

		ExchangeTokenResponse res = this.jwtService.hanldeExchangeToken(refreshToken);

		ResponseCookie resCookies = ResponseCookie
				.from("refreshToken", res.getRefreshToken())
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(refreshTokenExpiration)
				.build();

		ApiResponse<ExchangeTokenResponse> finalData = new ApiResponse<>(
				HttpStatus.OK, "", res, ""
		);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString())
				.body(finalData);
	}

	@GetMapping("/auth/account")
	public ResponseEntity<?> getAccount(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Jwt jwt = (Jwt) auth.getPrincipal();

		String userId = jwt.getClaimAsString("id");
		String userName = jwt.getSubject();
		String userRole = jwt.getClaimAsString("scope");

		LoginResponseDTO.UserLogin res = new LoginResponseDTO.UserLogin();
		res.setId(Integer.valueOf(userId));
		res.setUsername(userName);
		res.setRole(userRole);

		return ApiResponse.success(res);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<?> postLogout(@AuthenticationPrincipal Jwt jwt,
										@CookieValue(required = false) String refreshToken){
		String userId = jwt.getClaimAsString("id");
		String userName = jwt.getSubject();

		RefreshToken currentRefreshToken = this.refreshTokenService.findByToken(refreshToken);

		this.refreshTokenService.deteleById(currentRefreshToken.getId());

		ResponseCookie deleteCookie = ResponseCookie
				.from("refreshToken", null)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.build();

		ApiResponse<String> finalData = new ApiResponse<>(
				HttpStatus.OK, "", "ok", ""
		);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
				.body(finalData);
	}

}

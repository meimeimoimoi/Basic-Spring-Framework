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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.springsieutoc.config.JwtConfig;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
public class HomeController {

	private final UserService userService;
	private final JwtConfig jwtConfig;

	@Value("${hoidanit.secret:default-value}")
	private String name;

	@GetMapping("/")
	public ResponseEntity<?> index() {
		return ApiResponse.success(this.jwtConfig.createAccessToken());
	}

	@GetMapping("/hoidanit/1")
	public ResponseEntity<String> demoAPI() {
//		return new ResponseEntity<String>("Hello Vuong", HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.CREATED).body("Hello update");
	}
}

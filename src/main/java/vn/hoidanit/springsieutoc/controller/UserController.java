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

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.DTO.UserRequestDTO;
import vn.hoidanit.springsieutoc.model.DTO.UserResponseDTO;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/users")
	public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody User inputUser) {
		UserResponseDTO userInDB = this.userService.createUser(inputUser);
		return ApiResponse.created(userInDB);
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers(
            @RequestParam (required = false) String role) {
		List<UserResponseDTO> users = null;
        if(role != null)
            users = this.userService.fecthUserWithRole(role);
        else
            users = this.userService.fetchUsers();

		return ApiResponse.success(users);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByID(@PathVariable int id) {
		UserResponseDTO user = this.userService.findUserById(id);
		return ApiResponse.success(user);
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<ApiResponse<String>> updateUserById(@PathVariable int id, @RequestBody UserRequestDTO inputUser) {
		this.userService.updateUser(id, inputUser);
		return ApiResponse.success("Update success.");
	}
    // push

	@DeleteMapping("/user/{id}")
	public ResponseEntity<ApiResponse<String>> deleteUserById(@PathVariable int id) {
		this.userService.deleteUserById(id);
		return ApiResponse.success("Delete success.");
	}

}

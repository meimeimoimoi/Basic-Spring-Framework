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

package vn.hoidanit.springsieutoc.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.DTO.RoleResponseDTO;
import vn.hoidanit.springsieutoc.model.DTO.UserRequestDTO;
import vn.hoidanit.springsieutoc.model.DTO.UserResponseDTO;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.repository.RoleRepository;
import vn.hoidanit.springsieutoc.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // chi can goi interface
    private final RoleRepository roleRepository;

	public List<UserResponseDTO> fetchUsers() {
		// Logic fetch user/kết nối DB thực tế sẽ ở đây
		List<UserResponseDTO> userList =
                this.userRepository.findAll().stream().map(
                        user -> UserResponseDTO.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .address(user.getAddress())
                                .role(new RoleResponseDTO(user.getRole().getId(), user.getRole().getName()))
                        .build())
                        .collect(Collectors.toList());
		// select * from users
		return userList;
	}

    public List<UserResponseDTO> fecthUserWithRole(String roleName){
        List<UserResponseDTO> userList = this.userRepository.findByRole_Name(roleName).stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .role(new RoleResponseDTO(user.getRole().getId(), user.getRole().getName())).build())
                .collect(Collectors.toList());
        return userList;
    }

	public UserResponseDTO createUser(User user) {
		//check mail
        if(this.userRepository.existsByEmail(user.getEmail())){
            throw new ResourceAlreadyExistException("Email: "+user.getEmail()+" already exists");
        }
        //check role
        Long userId = user.getRole().getId();
        String roleName = user.getRole().getName();
        Role roleInDb = this.roleRepository.findByIdOrName(userId, roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not exists."));

        // hashpassword
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(roleInDb);
        return convertUserToDTO(this.userRepository.save(user));
	}

	public UserResponseDTO findUserById(int id) {

		User userInDb = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user not found with id = " + id));

        return convertUserToDTO(userInDb);
	}

	public void updateUser(int id, UserRequestDTO inputUser) {
        User userInDb = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id = " + id));

        if(inputUser.getRole() != null){
            //update role
            userInDb.setRole(inputUser.getRole());
        }

        userInDb.setName(inputUser.getName());
        userInDb.setAddress(inputUser.getAddress());

        this.userRepository.save(userInDb);
	}

	public void deleteUserById(int id) {
		this.userRepository.deleteById(id);
	}

    public UserResponseDTO convertUserToDTO(User user){
        RoleResponseDTO userRole = new RoleResponseDTO(user.getRole().getId(), user.getRole().getName());

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .role(userRole)
                .build();
    }

    public User findUserByEmail(String email){
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User không tìm thấy."));
    }
}

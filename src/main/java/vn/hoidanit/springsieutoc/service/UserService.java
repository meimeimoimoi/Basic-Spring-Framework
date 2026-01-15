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

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistException;
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

	public List<User> fetchUsers() {
		// Logic fetch user/kết nối DB thực tế sẽ ở đây
		List<User> userList = this.userRepository.findAll();
		// select * from users
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

	public User findUserById(int id) {
		return this.userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("user not found with id = " + id));

	}

	public void updateUser(User inputUser) {
		User currentUserInDB = this.findUserById(inputUser.getId());
		if (currentUserInDB != null) {
			currentUserInDB.setName(inputUser.getName());
			currentUserInDB.setEmail(inputUser.getEmail());
			currentUserInDB.setAddress(inputUser.getAddress());

			this.userRepository.save(currentUserInDB);
		}
	}

	public void deleteUserById(int id) {
		this.userRepository.deleteById(id);
	}

    public UserResponseDTO convertUserToDTO(User user){
//        UserResponseDTO userResponseDTO = new UserResponseDTO();
//
//        userResponseDTO.setId(user.getId());
//        userResponseDTO.setEmail(user.getEmail());
//
//        return userResponseDTO;
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }

}

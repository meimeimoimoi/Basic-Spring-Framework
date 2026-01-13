package vn.hoidanit.springsieutoc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.service.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/api/role")
    public ResponseEntity<ApiResponse<Role>> createARole(@RequestBody Role role){
        Role newRole = this.roleService.createRole(role);
        return ApiResponse.created(newRole);
    }

    @GetMapping("/api/roles")
    public ResponseEntity<ApiResponse<List<Role>>> getAllRoles(){
        List<Role> roles = this.roleService.fetchRoles();
        return ApiResponse.success(roles);
    }

    @GetMapping("/api/role/{id}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Long id){
        Role role = this.roleService.fetchRoleById(id);
        return ApiResponse.success(role);
    }

    @PutMapping("/api/role/{id}")
    public ResponseEntity<ApiResponse<String>> updateRole(@PathVariable Long id, @RequestBody Role role){
        role.setId(id);
        this.roleService.updateRole(role);
        return ApiResponse.success("Update success.");
    }

    @DeleteMapping("/api/role/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRole(@PathVariable Long id){
        this.roleService.deleteRole(id);
        return ApiResponse.success("Delete success.");
    }

}

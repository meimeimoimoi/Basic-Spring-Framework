package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role createRole(Role role){
        if(this.roleRepository.existsByName(role.getName())){
            throw new ResourceAlreadyExistException("Role with name "+ role.getName()+ " already exists");
        }
        return this.roleRepository.save(role);
    }

    public List<Role> fetchRoles(){
        return this.roleRepository.findAll();
    }

    public Role fetchRoleById(Long id){
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id "+ id + " not found"));
    }

    public Role updateRole(Role role){
        Role roleInDb = this.roleRepository.findById(role.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role with id "+ role.getId() + " not found"));

        if(roleInDb.getName().equals(role.getName())){
            roleInDb.setDescription(role.getDescription());
        } else {
            if(this.roleRepository.existsByNameAndIdNot(role.getName(), role.getId())){
                throw new ResourceAlreadyExistException("Role with id "+ role.getId() + " not found");
            }
            roleInDb.setName(role.getName());
        }
        return this.roleRepository.save(roleInDb);
    }

    public void deleteRole(Long id){
        this.roleRepository.delete(fetchRoleById(id));
    }
}

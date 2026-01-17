package vn.hoidanit.springsieutoc.model.DTO;

import lombok.*;
import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Role;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private int id;

    private String name;

    private String email;

    private String address;

    private RoleResponseDTO role;

}

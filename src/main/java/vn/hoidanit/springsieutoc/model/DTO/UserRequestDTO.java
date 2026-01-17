package vn.hoidanit.springsieutoc.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.springsieutoc.model.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "name không được để trống")
    private String name;

    @NotBlank(message = "address không được để trống")
    private String address;

    private Role role;
}

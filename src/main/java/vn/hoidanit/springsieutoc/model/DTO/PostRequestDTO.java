package vn.hoidanit.springsieutoc.model.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    @NotBlank(message = "title không được để trống")
    private String title;

    @NotBlank(message = "content không được để trống")
    private String content;

    @NotNull(message = "tag không được để trống")
    @Valid
    private List<InputTag> tags;

    @NotNull(message = "user không được để trống")
    @Valid
    private InputUser user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InputTag{

        @NotNull(message = "tag.id không được để trống")
        private Long id;

        @NotBlank(message = "tag.name không được để trống")
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InputUser{

        @NotNull(message = "user.id không được để trống")
        private int id;

        @NotBlank(message = "user.name không được để trống")
        private String name;
    }
}

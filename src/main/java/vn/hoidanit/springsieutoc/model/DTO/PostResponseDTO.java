package vn.hoidanit.springsieutoc.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private Long id;

    private String title;

    private String content;

    private List<OutputTag> tag;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OutputTag{
        private Long id;

        private String name;
    }

}

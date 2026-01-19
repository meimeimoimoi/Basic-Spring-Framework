package vn.hoidanit.springsieutoc.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;

    private String content;

    private boolean isApproved;

    private Instant createdAt;

    private Instant updatedAt;

    private int userId;

    private Long postId;
}

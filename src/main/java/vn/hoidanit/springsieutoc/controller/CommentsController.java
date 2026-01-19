package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.DTO.CommentResponseDTO;
import vn.hoidanit.springsieutoc.service.CommentsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;

    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<Comment>> createComment(@Valid @RequestBody Comment comment){
        Comment savedComment = this.commentsService.createComment(comment);
        return ApiResponse.created(savedComment);
    }

    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getAllComments(){
        List<CommentResponseDTO> comments = this.commentsService.fetchComments();
        return ApiResponse.success(comments);
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> getCommet(@PathVariable Long id){
        CommentResponseDTO dto = this.commentsService.fetchCommentById(id);
        return ApiResponse.success(dto);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> putComment(@PathVariable Long id,
                                                                      @Valid @RequestBody Comment comment){
        CommentResponseDTO commentResponseDTO = this.commentsService.updateComments(id, comment);
        return ApiResponse.success(commentResponseDTO);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long id){
        this.commentsService.deleteComment(id);
        return ApiResponse.success("ok");
    }
}

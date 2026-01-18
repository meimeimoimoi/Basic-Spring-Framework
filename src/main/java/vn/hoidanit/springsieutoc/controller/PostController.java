package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.DTO.PostRequestDTO;
import vn.hoidanit.springsieutoc.model.DTO.PostResponseDTO;
import vn.hoidanit.springsieutoc.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(@Valid @RequestBody PostRequestDTO dto){
        PostResponseDTO post = this.postService.createPost(dto);
        return ApiResponse.created(post);
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getAllPost(){
        List<PostResponseDTO> posts = this.postService.fectchPosts();
        return ApiResponse.success(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPost(@PathVariable Long id){
        PostResponseDTO post = this.postService.fecthPostById(id);
        return ApiResponse.success(post);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequestDTO dto){
        PostResponseDTO post = this.postService.updatePostById(id, dto);
        return ApiResponse.success(post);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long id){
        this.postService.deletePostById(id);
        return ApiResponse.success("delete success.");
    }
}

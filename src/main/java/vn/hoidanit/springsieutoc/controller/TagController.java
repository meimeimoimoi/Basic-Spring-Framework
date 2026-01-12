package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping("/api/tags")
    public ResponseEntity<ApiResponse<Tag>> createTag(@Valid @RequestBody Tag tag){
        Tag tagInDb = this.tagService.Create(tag);
        return ApiResponse.created(tagInDb);
    }

    @GetMapping("/api/tags")
    public ResponseEntity<ApiResponse<List<Tag>>> getAllTag(){
        List<Tag> tags = this.tagService.fetchTags();
        return ApiResponse.success(tags);
    }

    @GetMapping("/api/tag/{id}")
    public ResponseEntity<ApiResponse<Tag>> getTagByID(@PathVariable Long id){
        Tag tag = this.tagService.findTagById(id);
        return ApiResponse.success(tag);
    }
    @PutMapping("/api/tag/{id}")
    public ResponseEntity<ApiResponse<String>> updateTag(@PathVariable Long id, @RequestBody Tag inputTag){
        inputTag.setId(id);
        this.tagService.updateTag(inputTag);
        return ApiResponse.success("Update success.");
    }

    @DeleteMapping("/api/tag/{id}")
        public ResponseEntity<ApiResponse<String>> deleteTag(@PathVariable Long id){
            this.tagService.deleteTag(id);
            return ApiResponse.success("Delete Success.");
        }
    }


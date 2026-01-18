package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.DTO.PostRequestDTO;
import vn.hoidanit.springsieutoc.model.DTO.PostResponseDTO;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post converDTOtoPost(PostRequestDTO dto){
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        List<Tag> tags = dto.getTags().stream().map(inputTag -> {
            return new Tag(inputTag.getId(), inputTag.getName(), null);
        }).collect(Collectors.toList());

        post.setTags(tags);

        User user = new User();
        user.setId(dto.getUser().getId());

        post.setUser(user);

        return post;
    }

    public PostResponseDTO convertPostToDTO(Post post){
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());

        List<PostResponseDTO.OutputTag> tags = post.getTags().stream().map(tag -> {
            PostResponseDTO.OutputTag t = new PostResponseDTO.OutputTag();
            t.setId(tag.getId());
            t.setName(tag.getName());
            return t;
        }).collect(Collectors.toList());

        dto.setTag(tags);
        return dto;
    }

    public PostResponseDTO createPost(PostRequestDTO postDTO){
        Post post = this.postRepository.save(converDTOtoPost(postDTO));

        return convertPostToDTO(post);
    }

    public List<PostResponseDTO> fectchPosts(){
        return this.postRepository.findAll().stream().map(post -> {
            return convertPostToDTO(post);
        }).collect(Collectors.toList());
    }

    public PostResponseDTO updatePostById(Long id, PostRequestDTO postDTO){
        Post postInDB = this.postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post không tồn tại."));

        postInDB.setTitle(postDTO.getTitle());
        postInDB.setContent(postDTO.getContent());
        if(postDTO.getTags().size() > 0){
            List<Tag> tags = postDTO.getTags().stream().map(inputTag -> {
                return new Tag(inputTag.getId(), inputTag.getName(), null);
            }).collect(Collectors.toList());
            postInDB.setTags(tags);
        }
        this.postRepository.save(postInDB);

        return convertPostToDTO(postInDB);
    }

    public PostResponseDTO fecthPostById(Long id){
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post không tồn tại."));
        return convertPostToDTO(post);
    }

    public void deletePostById(Long id){
        this.postRepository.deleteById(id);
    }
}


package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.DTO.CommentResponseDTO;
import vn.hoidanit.springsieutoc.repository.CommentsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;

    public CommentResponseDTO convertCommentToDTO(Comment comment){
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser().getId());
        dto.setPostId(comment.getPost().getId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setApproved(comment.isApproved());
        return dto;
    }

    public Comment createComment(Comment inputComment){
        return this.commentsRepository.save(inputComment);
    }

    public List<CommentResponseDTO> fetchComments(){
        List<CommentResponseDTO> comments = this.commentsRepository.findAll().stream().map(comment -> {
            return convertCommentToDTO(comment);
        }).collect(Collectors.toList());
        return comments;
    }

    public CommentResponseDTO fetchCommentById(Long id){
        Comment comment = this.commentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy comment."));
        return convertCommentToDTO(comment);
    }

    public CommentResponseDTO updateComments(Long id, Comment inputComment){
        Comment comment = this.commentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy comment."));
        comment.setApproved(inputComment.isApproved());

        return convertCommentToDTO(this.commentsRepository.save(comment));
    }

    public void deleteComment(Long id){
        this.commentsRepository.deleteById(id);
    }
}

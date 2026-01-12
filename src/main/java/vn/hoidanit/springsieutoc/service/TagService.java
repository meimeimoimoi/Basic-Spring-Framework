package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag Create(Tag tag){
        if(this.tagRepository.existsByName(tag.getName())){
            throw new ResourceAlreadyExistException("Tag name "+ tag.getName() + " is already exist.");
        }
        return this.tagRepository.save(tag);
    }

    public List<Tag> fetchTags(){
        return this.tagRepository.findAll();
    }

    public Tag findTagById(Long id){
        return this.tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id "+ id +" not found"));
    }

    public void updateTag(Tag inputTag){
        Tag currentTag = this.findTagById(inputTag.getId());
        if(this.tagRepository.existsByName(inputTag.getName())){
            throw new ResourceAlreadyExistException("Tag name "+ inputTag.getName() + " is already exist.");
        }
        if(currentTag != null){
            currentTag.setName(inputTag.getName());
            this.tagRepository.save(currentTag);
        }
    }

    public void deleteTag(Long id){
        this.tagRepository.deleteById(id);
    }

}

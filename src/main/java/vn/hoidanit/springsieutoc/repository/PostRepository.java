package vn.hoidanit.springsieutoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    List<Post> findByTagsContains(Tag tag);
}

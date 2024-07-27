package ct553.backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public ArrayList<Post> findAllPosts() {
        return (ArrayList<Post>) this.postRepository.findAll();
    }

    public Post findPostById(Long id) {
        return this.postRepository.findById(id).orElse(null);
    }

    @Transactional
    public Post addPost(Post post) {
        return this.postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, Post post) {
        Post existingPost = findPostById(id);
        if (existingPost != null) {
            // Update fields based on your requirements
            existingPost.setTitle(post.getTitle());
            existingPost.setType(post.getType());
            existingPost.setAuthor(post.getAuthor());
            existingPost.setContent(post.getContent());
            existingPost.setImageData(post.getImageData());
            existingPost.setPublishAt(post.getPublishAt());
            existingPost.setEmployee(post.getEmployee());

            this.postRepository.save(existingPost);
            return existingPost;
        }
        return null;
    }

    @Transactional
    public boolean deletePost(Long id) {
        Post resultFindPost = findPostById(id);
        if (resultFindPost != null) {
            this.postRepository.delete(resultFindPost);
            return true;
        } else {
            return false;
        }
    }
}

package ct553.backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/post")
public class PostResource {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<?> getAllPost() {
        return new ResponseEntity<>(this.postService.findAllPosts(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        Post post = this.postService.findPostById(id);
        if (post == null) {
            return new ResponseEntity<>("This post is not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable Long id) {
        Post post = this.postService.findPostById(id);
        if (post == null) {
            return new ResponseEntity<>("This post is not exist", HttpStatus.NOT_FOUND);
        }

        this.postService.deletePost(id);
        return new ResponseEntity<>("A post with id=" + id + " is deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePostById(@PathVariable Long id, @RequestBody Post post) {
        if (this.postService.updatePost(id, post) != null) {
            return new ResponseEntity<>("A post with id=" + id + " is updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("The post with id=" + post.getId() + " fail updated. Try again!",
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public String addPost(@RequestBody Post post) {
        post = postService.addPost(post);
        if (post == null) {
            return "Call add post function \n" + "Add post failed";
        }
        return "Call add post function \n" + post.toString();
    }

}

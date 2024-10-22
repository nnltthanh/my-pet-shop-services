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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable Long id) {
        Post post = this.postService.findPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePostById(@PathVariable Long id, @RequestBody Post post) {
        if (this.postService.updatePost(id, post) != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public void addPost(@RequestBody Post post) {
        postService.addPost(post);
    }

}

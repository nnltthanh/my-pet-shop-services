package ct553.backend.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.auth.GroupName;
import ct553.backend.auth.RoleName;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    @Autowired
    UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return this.userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/login")
    public UserDTO login(JwtAuthenticationToken auth) {
        return this.userService.login(auth);
    }

    @GetMapping("/groups/{groupName}")
    public List<UserDTO> login(@PathVariable String groupName) {
        return this.userService.getUsersInGroup(GroupName.groupNameIdMap.get(groupName));
    }

    // @GetMapping("/account/{account}")
    // public ResponseEntity<?> getUserById(@PathVariable String account) {
    //     UserDTO user = userService.findByAccount(account);
    //     if (user == null) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }

    //     return new ResponseEntity<>(user, HttpStatus.OK);
    // }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @RolesAllowed({RoleName.ADMIN})
    public ResponseEntity<?> addUser(@RequestPart(value = "user") UserDTO user,
                        @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        UserDTO isExistedUser = this.userService.findByAccount(user.getAccount());
        if (isExistedUser == null) {
            this.userService.add(user, avatar);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({RoleName.ADMIN})
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        UserDTO userDTO = this.userService.findById(id);
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "basic-info/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.CUSTOMER, RoleName.ADMIN})
    public ResponseEntity<?> update(@PathVariable Long id, 
                @RequestPart(value = "user") UserDTO userDTO,
                @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        UserDTO user = this.userService.update(id, userDTO, avatar);
        if (user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{id}/fields/{field}")
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.CUSTOMER, RoleName.ADMIN})
    public ResponseEntity<?> updatePartially(@PathVariable Long id, @PathVariable String field, @RequestBody(required = false) String value) {
        UserDTO user = this.userService.updatePartially(id, field, value);
        if (user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}

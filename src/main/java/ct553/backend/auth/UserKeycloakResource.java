package ct553.backend.auth;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/user-keycloak")
@Slf4j
@EnableMethodSecurity
public class UserKeycloakResource {
    
    @Autowired
    UserKeycloakSerivce userKeycloakSerivce;

    // @PostMapping({ "", "/" })
    // public ResponseEntity<UserRepresentation> add(@RequestBody UserDTO userDTO) {
    //     log.info("Start create new user");
    //     log.info("{}", userDTO);
    //     return new ResponseEntity<UserRepresentation>(this.userKeycloakSerivce.createUser(userDTO), HttpStatus.OK);
    // }

    @GetMapping("/{account}")
    public UserRepresentation getMethodName(@PathVariable String account) {
        return this.userKeycloakSerivce.findByAccount(account).get();
    }
    
    

}

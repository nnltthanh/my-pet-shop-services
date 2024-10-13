package ct553.backend.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ct553.backend.auth.RoleName;
import ct553.backend.auth.UserKeycloakSerivce;
import ct553.backend.imagedata.ImageData;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserKeycloakSerivce userKeycloakSerivce;

    public List<UserDTO> findAll() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(UserDTO::from).map(this::mapUserGroups).toList();
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            UserDTO userDto = UserDTO.from(user);
            userDto.setGroups(null);
        }
        return UserDTO.from(user);
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.findByAccount(auth.getName());
    }

    public User findByAccount(String account) {
        return this.userRepository.findUserByAccount(account).orElse(null);
    }

    @Transactional
    public void add(UserDTO user) {
        if (user.getId() == null || this.findById(user.getId()) == null) {
            this.userKeycloakSerivce.createUser(user);
            this.userRepository.save(User.from(user));
        }
    }

    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Transactional
    public User update(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            // existingUser.setLocked(!existingUser.isLocked()); // TODO
            this.userRepository.save(existingUser);
            return existingUser;
        }
        return null;
    }

    public User updateAvatar(Long id, ImageData avatar) {
        User existUser = this.userRepository.findById(id).orElse(null);
        if (existUser != null) {
            existUser.setAvatar(avatar);
            this.userRepository.save(existUser);
            return existUser;
        }
        return null;
    }

    public UserDTO mapUserGroups(UserDTO userDTO) {
        Optional<UserRepresentation> userRepresentation = this.userKeycloakSerivce.findByAccount(userDTO.getAccount());
        if (userRepresentation.isPresent()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            List<String> groups = new ArrayList<>();
            auth.getAuthorities().stream().forEach(authority -> {
                if (RoleName.roles().indexOf(authority.getAuthority()) > -1) {
                    groups.add(authority.getAuthority());
                }
            });
            userDTO.setGroups(groups);
        }
        return userDTO;
    }
}

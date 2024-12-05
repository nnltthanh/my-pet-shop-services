package ct553.backend.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.CloudinaryService;
import ct553.backend.auth.RoleName;
import ct553.backend.auth.UserKeycloakSerivce;
import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserKeycloakSerivce userKeycloakSerivce;

    // @Autowired
    // CustomerService customerService;

    // @Autowired
    // EmployeeService employeeService;

    @Autowired
    CloudinaryService cloudinaryService;

    public List<UserDTO> findAll() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(UserDTO::from)
                // .map(this::mapUserGroups)
                .toList();
    }

    @Transactional
    public UserDTO login(JwtAuthenticationToken auth) {
        User user = new User();
        if (auth == null) {
            return null;
        }
        
        user.setAccount(auth.getToken().getClaimAsString(StandardClaimNames.PREFERRED_USERNAME));
        user.setEmail(auth.getToken().getClaimAsString(StandardClaimNames.EMAIL));
        user.setName(auth.getToken().getClaimAsString(StandardClaimNames.NAME));

        Optional<User> optionalUser = this.userRepository.findByAccount(user.getAccount());
        User savedUser = null;
        if (optionalUser.isPresent()) {
            savedUser = optionalUser.get();
        } else {
            savedUser = this.userRepository.save(user);
        }

        UserDTO userDTO = UserDTO.from(savedUser);
        log.info("userDTO {} ", userDTO);
        this.mapUserGroups(userDTO);
        return userDTO;
    }

    @Transactional
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            UserDTO userDto = UserDTO.from(user);
            // userDto.setGroups(null);
        }
        return UserDTO.from(user);
    }

    public User findByIdCore(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDTO getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.findByAccount(auth.getName());
    }

    public UserDTO findByAccount(String account) {
        return UserDTO.from(this.userRepository.findByAccount(account).orElse(null));
    }

    public List<UserDTO> getUsersInGroup(String groupName) {
        return this.userKeycloakSerivce.findAllByGroup(groupName)
                .stream()
                .map(u -> findByAccount(u.getUsername()))
                .toList();
    }

    // @Transactional
    // public void add(UserDTO user, MultipartFile avatar) throws IOException {
    //     if (user.getId() == null || this.findById(user.getId()) == null) {
    //         // this.userKeycloakSerivce.createUser(user);
    //         User beSavedUser = User.from(user);
    //         if (avatar != null) {
    //             String imageUrl = this.cloudinaryService.uploadFile(avatar);
    //             ImageData imageData = new ImageData(null, imageUrl, ImageDataType.AVATAR);
    //             beSavedUser.setAvatar(imageData);
    //         }

    //         if (user.getGroups().indexOf("Khách hàng") != -1) {
    //             this.customerService.add(new Customer(beSavedUser));
    //         } else {
    //             this.employeeService.add(new Employee(beSavedUser));
    //         }
    //     }
    // }

    @Transactional
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO userDTO, MultipartFile avatar) throws IOException {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setDob(userDTO.getDob());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setName(userDTO.getName());
            existingUser.setPhone(userDTO.getPhone());
            this.userRepository.save(existingUser);

            if (avatar != null) {
                String imageUrl = this.cloudinaryService.uploadFile(avatar);
                ImageData imageData = new ImageData(null, imageUrl, ImageDataType.AVATAR);
                existingUser.setAvatar(imageData);
            }

            return UserDTO.from(existingUser);
        }
        return null;
    }

    @Transactional
    public UserDTO updatePartially(Long id, String field, String value) {
        User user = this.userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        switch (field) {
            case User.Fields.account:
                user.setAccount(value);
                break;
            case User.Fields.name:
                user.setAccount(value);
                break;
            case User.Fields.email:
                user.setAccount(value);
                break;
            case User.Fields.phone:
                user.setAccount(value);
                break;
            case User.Fields.validTo:
                if (StringUtils.isNotEmpty(value)) {
                    System.out.println("blocked");
                    user.setValidTo(new Date()); // blocked
                } else {
                    System.out.println("unblocked");
                    user.setValidTo(null); // unblocked
                }
            default:
                break;
        }

        return UserDTO.from(this.userRepository.save(user));

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

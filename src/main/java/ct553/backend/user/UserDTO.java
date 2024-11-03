package ct553.backend.user;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String account;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String avatarUrl;
    private Date dob;
    private boolean isBlocked;
    private String blockedByUser;
    private List<String> groups;

    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .account(user.getAccount())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .dob(user.getDob())
                .isBlocked(user.getValidTo() == null ? false : true)
                .avatarUrl(user.getAvatar() == null ? null : user.getAvatar().getImageUrls())
                .build();
    }

    // public static UserDTO from(Customer user) {
    //     if (user == null) {
    //         return null;
    //     }
    //     return UserDTO.builder()
    //             .id(user.getId())
    //             .account(user.getAccount())
    //             .name(user.getName())
    //             .phone(user.getPhone())
    //             .email(user.getEmail())
    //             .dob(user.getDob())
    //             .password(user.getPassword())
    //             .groups(user.getGroups())
    //             .isBlocked(user.getValidTo() == null ? false : true)
    //             .avatarUrl(user.getAvatar() == null ? null : user.getAvatar().getImageUrls())
    //             .build(); 
    // }
}

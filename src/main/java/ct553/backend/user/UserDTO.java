package ct553.backend.user;

import java.util.Date;
import java.util.List;

import ct553.backend.customer.Customer;
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
    private Date dob;
    private List<String> groups;

    public static UserDTO from(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .account(user.getAccount())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .dob(user.getDob())
                .build();
    }

    public static UserDTO from(Customer user) {
        return UserDTO.builder()
                .id(user.getId())
                .account(user.getAccount())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .dob(user.getDob())
                .password(user.getPassword())
                .groups(user.getGroups())
                .build();
    }
}

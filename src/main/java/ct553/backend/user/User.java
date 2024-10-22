package ct553.backend.user;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import ct553.backend.imagedata.ImageData;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(length = 50, name = "account", unique = true, nullable = false)
        private String account;

        // @Column(columnDefinition = "TEXT")
        // private String password;

        @Column
        private String name;

        @Column(length = 12)
        private String phone;

        @Column(length = 80)
        private String email;

        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "avatar")
        private ImageData avatar;

        @Temporal(value = TemporalType.DATE)
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date dob;

        @Column(name = "created_at")
        @Temporal(value = TemporalType.TIMESTAMP)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        @CreatedDate
        private Date createdAt;

        @Column(name = "updated_at")
        @Temporal(value = TemporalType.TIMESTAMP)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        @UpdateTimestamp
        private Date updatedAt;

        // @Transient
        // public String getUserType() {
        //         return this.getClass().getAnnotation(DiscriminatorValue.class).value();
        // }

        public User(Long id) {
                this.id = id;
        }

        public static User from(UserDTO userDto) {
                return User.builder()
                        .id(userDto.getId())
                        .account(userDto.getAccount())
                        .name(userDto.getName())
                        .phone(userDto.getPhone())
                        .email(userDto.getEmail())
                        .dob(userDto.getDob())
                        .build();
        }

}

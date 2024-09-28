package ct553.backend.pet.healthrecord;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ct553.backend.pet.entity.PetCustomer;
import ct553.backend.product.entity.PetProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@Entity
@Data
@Table(name = "health_record")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Float weight;

    @Column
    private Float age;

    @Column
    @JsonProperty("petLength")
    private Float length;

    @Column
    private String vaccination;

    @Column
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime createdAt;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "pet_customer_id")
    private PetCustomer petCustomer;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private PetProduct petProduct;

    public boolean equals(HealthRecord healthRecord) {
        return this.age == healthRecord.getAge() &&
                this.weight == healthRecord.getWeight() &&
                this.length == healthRecord.getLength() &&
                StringUtils.equals(vaccination, healthRecord.getVaccination());
    }

}
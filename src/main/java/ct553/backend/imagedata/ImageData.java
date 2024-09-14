package ct553.backend.imagedata;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@Table(name = "image_data")
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String imageUrls;

    @Enumerated(EnumType.STRING)
    private ImageDataType type;

    public ImageData(String path, ImageDataType type) {
        this.imageUrls = path;
        this.type = type;
    }

}

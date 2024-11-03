package ct553.backend.pet.boundary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.CloudinaryService;
import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.pet.control.PetCustomerRepository;
import ct553.backend.pet.entity.PetCategory;
import ct553.backend.pet.entity.PetCustomer;
import ct553.backend.pet.healthrecord.HealthRecord;
import ct553.backend.product.pet.PetProduct;
import ct553.backend.user.User;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class PetCustomerService {

    @Autowired
    PetCustomerRepository petCustomerRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    PetCategoryService petCategoryService;

    public void addFromPetProduct(PetProduct petProduct, User customer) {
        PetCustomer petCustomer = PetCustomer.from(petProduct, customer);
        this.mapPetCategory(petCustomer);
        List<HealthRecord> healthRecords = petProduct.getHealthRecord();

        List<HealthRecord> petCustomerHealthRecords = new ArrayList<>();

        healthRecords.forEach(record -> {
            record.setPetCustomer(petCustomer);
            petCustomerHealthRecords.add(record);
        });
        petCustomer.setHealthRecord(petCustomerHealthRecords);
        this.petCustomerRepository.save(petCustomer);

    }

    public PetCustomer add(PetCustomer petCustomer, MultipartFile multipartFile) throws IOException {
        this.mapPetCategory(petCustomer);
        
        for (HealthRecord healthRecord : petCustomer.getHealthRecord()) {
            healthRecord.setPetCustomer(petCustomer);
        }
        
        if (Objects.nonNull(multipartFile)) {
            String imageUrl = this.cloudinaryService.uploadFile(multipartFile);
            ImageData imageData = new ImageData(null, imageUrl, ImageDataType.PET_CUSTOMER);
            petCustomer.setImageData(imageData);
        }
        return this.petCustomerRepository.save(petCustomer);
    }

    public PetCustomer update(Long id, PetCustomer petCustomer, MultipartFile multipartFile) throws IOException {
        if (Objects.nonNull(multipartFile)) {
            String imageUrl = this.cloudinaryService.uploadFile(multipartFile);
            ImageData imageData = new ImageData(null, imageUrl, ImageDataType.PET_CUSTOMER);
            petCustomer.setImageData(imageData);
        }
        PetCustomer petCustomerDB = this.petCustomerRepository.findById(id).orElse(null);
        this.mapPetCategory(petCustomerDB);
        petCustomerDB.setColor(petCustomer.getColor());
        petCustomerDB.setDateOfBirth(petCustomer.getDateOfBirth());
        petCustomerDB.setImageData(petCustomer.getImageData());
        petCustomerDB.setName(petCustomer.getName());
        return this.petCustomerRepository.save(petCustomerDB);
    }

    public List<PetCustomer> findAllByCustomer(Long customerId) {
        return this.petCustomerRepository.findAllByCustomer_Id(customerId);
    }

    public PetCustomer findById(Long id) {
        PetCustomer p = petCustomerRepository.findById(id).orElse(null);
        if (p == null) {
            return null;
        }
        if (p.getHealthRecord() != null && !p.getHealthRecord().isEmpty()) {
                HealthRecord latestHealthRecord = p.getHealthRecord().stream()
                        .sorted(Comparator.comparing(HealthRecord::getCreatedAt).reversed()).toList().get(0);
                p.setLatestHealthRecord(latestHealthRecord);
            }
        return p;
    }

    private void mapPetCategory(PetCustomer pet) {
        if (Optional.ofNullable(pet).isEmpty()) {
            throw new IllegalArgumentException("Not found pet product");
        }
        Optional<PetCategory> optionalPetCategory = Optional.ofNullable(pet.getCategory());
        optionalPetCategory.ifPresentOrElse(
                category -> {
                    PetCategory categoryDB = petCategoryService.findByNameAndBreed(category.getName(),
                            category.getBreed());
                    if (categoryDB == null) {
                        pet.setCategory(this.petCategoryService.add(category));
                    } else {
                        pet.setCategory(categoryDB);
                    }
                },
                () -> new IllegalArgumentException("Not found pet category"));
    }

}

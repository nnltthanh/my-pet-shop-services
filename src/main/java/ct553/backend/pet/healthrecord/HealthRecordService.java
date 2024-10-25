package ct553.backend.pet.healthrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ct553.backend.pet.entity.PetCustomer;
import ct553.backend.product.entity.PetProduct;

public class HealthRecordService {

    @Autowired
    HealthRecordRepository healthRecordRepository;

    public void save(HealthRecord healthRecord) {
        this.healthRecordRepository.save(healthRecord);
    }

    public List<HealthRecord> findAllByPetProduct(Long petProductId) {
        return this.healthRecordRepository.findAllByPetProduct_Id(petProductId);
    }

    public void addHealthRecordForPurchase(PetCustomer customer, PetProduct product) {
    HealthRecord healthRecord = new HealthRecord();
    
    // Set the associations
    healthRecord.setPetCustomer(customer);
    healthRecord.setPetProduct(product);
    
    // Add the HealthRecord to PetCustomer and PetProduct
    customer.getHealthRecord().add(healthRecord);
    product.getHealthRecord().add(healthRecord);
}

}
package ct553.backend.pet.healthrecord;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    List<HealthRecord> findAllByPetProduct_Id(Long petProductId);

}
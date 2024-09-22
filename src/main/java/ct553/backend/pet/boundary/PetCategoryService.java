package ct553.backend.pet.boundary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.pet.control.PetCategoryRepository;
import ct553.backend.pet.entity.PetBreed;
import ct553.backend.pet.entity.PetCategory;

@Service
public class PetCategoryService {

    @Autowired
    private PetCategoryRepository petCategoryRepository;

    public ArrayList<PetCategory> findAll() {
        return (ArrayList<PetCategory>) petCategoryRepository.findAll();
    }

    public PetCategory findById(Long id) {
        return petCategoryRepository.findById(id).orElse(null);
    }

    public PetCategory findByNameAndBreed(String name, PetBreed petBreed) {
        return petCategoryRepository.findByNameIgnoreCaseAndBreed(name, petBreed).orElse(null);
    }

    public PetCategory add(PetCategory pet) {
        return this.petCategoryRepository.save(pet);
    }

    public void deleteById(Long id) {
        this.petCategoryRepository.deleteById(id);
    }

}

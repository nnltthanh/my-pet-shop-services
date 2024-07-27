package ct553.backend.pet.boundary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import ct553.backend.pet.control.PetCategoryRepository;
import ct553.backend.pet.entity.PetCategory;

public class PetCategoryService {

    @Autowired
    private PetCategoryRepository petCategoryRepository;

    public ArrayList<PetCategory> findAll() {
        return (ArrayList<PetCategory>) petCategoryRepository.findAll();
    }

    public PetCategory findById(Long id) {
        return petCategoryRepository.findById(id).orElse(null);
    }

    public void add(PetCategory pet) {
        this.petCategoryRepository.save(pet);
    }

    public void deleteById(Long id) {
        this.petCategoryRepository.deleteById(id);
    }

}

package ct553.backend.product.boundary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.product.control.AccessorySubCategoryRepository;
import ct553.backend.product.entity.AccessorySubCategory;


@Service
public class AccessorySubCategoryService {

    @Autowired
    private AccessorySubCategoryRepository accessoryCategoryRepository;

    public ArrayList<AccessorySubCategory> findAll() {
        return (ArrayList<AccessorySubCategory>) accessoryCategoryRepository.findAll();
    }

    public AccessorySubCategory findById(Long id) {
        return accessoryCategoryRepository.findById(id).orElse(null);
    }

    public void add(AccessorySubCategory accessory) {
        this.accessoryCategoryRepository.save(accessory);
    }

    public void deleteById(Long id) {
        this.accessoryCategoryRepository.deleteById(id);
    }

}

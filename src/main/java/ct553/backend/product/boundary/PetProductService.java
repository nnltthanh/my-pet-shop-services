package ct553.backend.product.boundary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.CloudinaryServiceImp;
import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.pet.boundary.PetCategoryService;
import ct553.backend.pet.entity.PetCategory;
import ct553.backend.product.control.PetProductRepository;
import ct553.backend.product.entity.PetProduct;
import ct553.backend.product.entity.ProductSortingCriteria;

@Service
public class PetProductService {

    @Autowired
    private PetProductRepository petRepository;

    @Autowired
    private PetCategoryService petCategoryService;

    @Autowired
    private CloudinaryServiceImp cloudinaryService;

    public ArrayList<PetProduct> findAllBy(ProductSortingCriteria sortingCriteria, Pageable pageable) {

        return new ArrayList<>(
                petRepository
                        .findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                buildSortCriteria(sortingCriteria)))
                        .stream().map(pet -> {
                            this.mapPetCategory(pet);
                            return pet;
                        })
                        .toList());
    }

    public PetProduct findById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    public PetProduct add(PetProduct pet, MultipartFile multipartFile) throws IOException {
        if (Objects.nonNull(pet)) {
            pet.setEngName(deAccent(pet.getName()));
            this.mapPetCategory(pet);
            if (Objects.nonNull(multipartFile)) {
                String imageUrl = this.cloudinaryService.uploadFile(multipartFile);
                ImageData imageData = new ImageData(null, imageUrl, ImageDataType.PRODUCT);
                // imageData = this.imageDataService.addImageData(imageData);
                pet.setImageData(imageData);
            }
        }
        return this.petRepository.save(pet);
    }

    public void deleteById(Long id) {
        this.petRepository.deleteById(id);
    }

    private Sort buildSortCriteria(ProductSortingCriteria sortingCriteria) {
        if (Objects.isNull(sortingCriteria) || sortingCriteria.isEmptySearchCriteria()) {
            return Sort.by(Direction.DESC, "updatedAt");
        }
        if (sortingCriteria.getUpdatedAt() != null) {
            return Sort.by(Sort.Order.by("updatedAt").with(sortingCriteria.getUpdatedAt()));
        }

        if (sortingCriteria.getPrice() != null) {
            return Sort.by(Sort.Order.by("price").with(sortingCriteria.getPrice()));
        }

        if (sortingCriteria.getPrice() != null) {
            return Sort.by(Sort.Order.by("rating").with(sortingCriteria.getRating()));
        }
        return Sort.unsorted();
    }

    private void mapPetCategory(PetProduct pet) {
        if (Optional.ofNullable(pet).isEmpty()) {
            throw new IllegalArgumentException("Not found pet product");
        }
        Optional<PetCategory> optionalPetCategory = Optional.ofNullable(pet.getCategory());
        optionalPetCategory.ifPresentOrElse(
                category -> {
                    category = petCategoryService.findById(category.getId());
                    pet.setCategory(category);
                },
                () -> new IllegalArgumentException("Not found pet category"));
    }

    private String deAccent(String text) {
        text = text.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        text = text.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        text = text.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        text = text.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        text = text.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        text = text.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        text = text.replaceAll("đ", "d");

        text = text.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        text = text.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        text = text.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        text = text.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        text = text.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        text = text.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        text = text.replaceAll("Đ", "D");
        return text;
    }

}

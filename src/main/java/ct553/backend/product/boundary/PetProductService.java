package ct553.backend.product.boundary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.CloudinaryServiceImp;
import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.pet.boundary.PetCategoryService;
import ct553.backend.pet.entity.PetCategory;
import ct553.backend.product.control.PetProductRepository;
import ct553.backend.product.entity.PetProduct;
import ct553.backend.product.entity.PetProductOverviewResponse;
import ct553.backend.product.entity.ProductSearchingCriteria;
import ct553.backend.product.entity.ProductSortingCriteria;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PetProductService {

    @Autowired
    private PetProductRepository petRepository;

    @Autowired
    private PetCategoryService petCategoryService;

    @Autowired
    private CloudinaryServiceImp cloudinaryService;

    public PetProductOverviewResponse findPetProductOverviewResponseBy(ProductSortingCriteria sortingCriteria,
            ProductSearchingCriteria searchingCriteria, Pageable pageable) {
        Page<PetProduct> products = petRepository.findAllBy(
                searchingCriteria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), buildSortCriteria(sortingCriteria)));
        List<PetProduct> data = new ArrayList<>(products.stream().toList());

        return PetProductOverviewResponse.from(products.getTotalElements(), data);
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

    private void mapPetCategory(PetProduct pet) {
        if (Optional.ofNullable(pet).isEmpty()) {
            throw new IllegalArgumentException("Not found pet product");
        }
        Optional<PetCategory> optionalPetCategory = Optional.ofNullable(pet.getCategory());
        optionalPetCategory.ifPresentOrElse(
                category -> {
                    PetCategory categoryDB = petCategoryService.findByNameAndBreed(category.getName(), category.getBreed());
                    if (categoryDB == null) {
                        pet.setCategory(this.petCategoryService.add(category));
                    } else {
                        pet.setCategory(categoryDB);
                    }
                    System.out.println(pet.getCategory());
                    
                },
                () -> new IllegalArgumentException("Not found pet category"));
    }

    public PetProduct updateProduct(Long id, PetProduct productUpdateInfo, MultipartFile multipartFile) throws IOException {
        PetProduct existingProduct = this.petRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(
                productUpdateInfo.getName() != null ? productUpdateInfo.getName() : existingProduct.getName());
        existingProduct.setEngName(deAccent(existingProduct.getName()));
        existingProduct.setPrice(productUpdateInfo.getPrice() != null ? productUpdateInfo.getPrice()
                        : existingProduct.getPrice());
        existingProduct.setDescription(productUpdateInfo.getDescription() != null ? productUpdateInfo.getDescription()
                : existingProduct.getDescription());
        existingProduct.setCategory(productUpdateInfo.getCategory() != null ? productUpdateInfo.getCategory()
                : existingProduct.getCategory());
        this.mapPetCategory(existingProduct);

        if (Objects.nonNull(multipartFile)) {
            String imageUrl = this.cloudinaryService.uploadFile(multipartFile);
            ImageData imageData = new ImageData(null, imageUrl, ImageDataType.PRODUCT);
            // imageData = this.imageDataService.addImageData(imageData);
            existingProduct.setImageData(imageData);
        }

        this.petRepository.save(existingProduct);
        return existingProduct;
    }

    private Sort buildSortCriteria(ProductSortingCriteria sortingCriteria) {
        if (Objects.isNull(sortingCriteria) || sortingCriteria.isEmptySortingCriteria()) {
            return Sort.by(Direction.DESC, "updatedAt");
        }
        List<Sort.Order> orders = new ArrayList<>();
        sortingCriteria.getAsc().stream().forEach(value -> orders.add(Sort.Order.by(value).with(Direction.ASC)));
        sortingCriteria.getDesc().stream().forEach(value -> orders.add(Sort.Order.by(value).with(Direction.DESC)));
        return CollectionUtils.isEmpty(orders) ? Sort.unsorted() : Sort.by(orders);
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

package ct553.backend.product.service;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.CloudinaryServiceImp;
import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.pet.boundary.PetCategoryService;
import ct553.backend.pet.healthrecord.HealthRecord;
import ct553.backend.product.productdetail.ProductDetail;
import ct553.backend.product.productdetail.ProductDetailService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ServiceProductService {

    @Autowired
    private ServiceProductRepository serviceProductRepository;

    @Autowired
    private PetCategoryService petCategoryService;

    @Autowired
    private CloudinaryServiceImp cloudinaryService;

    @Autowired
    ProductDetailService productDetailService;

    // public ServiceProductOverviewResponse findServiceProductOverviewResponseBy(ProductSortingCriteria sortingCriteria,
    //         ProductSearchingCriteria searchingCriteria, Pageable pageable) {
    //     Page<ServiceProduct> products = petRepository.findAllBy(
    //             searchingCriteria,
    //             PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), buildSortCriteria(sortingCriteria)));
    //     List<ServiceProduct> data = new ArrayList<>(products.stream().map(p -> {
    //         if (p.getHealthRecord() != null && !p.getHealthRecord().isEmpty()) {
    //             HealthRecord latestHealthRecord = p.getHealthRecord().stream()
    //                     .sorted(Comparator.comparing(HealthRecord::getCreatedAt).reversed()).toList().get(0);
    //             p.setLatestHealthRecord(latestHealthRecord);
    //         }
    //         return p;
    //     }).toList());

    //     return ServiceProductOverviewResponse.from(products.getTotalElements(), data);
    // }

    public ServiceProduct findById(Long id) {
        ServiceProduct product = serviceProductRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        product.setProductDetails(this.productDetailService.getAllProductDetails(id));
        return product;
    }

    public List<ServiceProduct> findAllByType(ServiceProductType type) {
        return serviceProductRepository.findAllByType(type);
    }

    public List<ServiceProduct> findAll() {
        return serviceProductRepository.findAll();
    }

    @Transactional
    public ServiceProduct add(ServiceProduct service, List<ProductDetail> productDetails, MultipartFile multipartFile) throws IOException {
        if (Objects.nonNull(service)) {
            service.setEngName(deAccent(service.getName()));
            // this.mapPetCategory(pet);
            if (Objects.nonNull(multipartFile)) {
                String imageUrl = this.cloudinaryService.uploadFile(multipartFile);
                ImageData imageData = new ImageData(null, imageUrl, ImageDataType.PRODUCT);
                service.setImageData(imageData);
            }
        }
        var serviceDB = this.serviceProductRepository.save(service);
        productDetails.forEach(detail -> {
            detail.setProduct(serviceDB);
            this.productDetailService.add(detail, null);
        });
        return serviceDB;
    }

    void deleteById(Long id) {
        Optional<ServiceProduct> serviceProduct = this.serviceProductRepository.findById(id);
        if (serviceProduct.isPresent()) {
            ServiceProduct productDB = serviceProduct.get();
            productDB.setValidTo(new Date());
            this.serviceProductRepository.save(productDB);
        }
    }

    public ServiceProduct update(Long id, ServiceProduct productUpdateInfo, List<ProductDetail> productDetails, MultipartFile multipartFile)
            throws IOException {
        ServiceProduct existingProduct = this.serviceProductRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(
                productUpdateInfo.getName() != null ? productUpdateInfo.getName() : existingProduct.getName());
        existingProduct.setEngName(deAccent(existingProduct.getName()));
        existingProduct.setPrice(productUpdateInfo.getPrice());
        existingProduct.setDescription(productUpdateInfo.getDescription());

        if (Objects.nonNull(multipartFile)) {
            String imageUrl = this.cloudinaryService.uploadFile(multipartFile);
            ImageData imageData = new ImageData(null, imageUrl, ImageDataType.PRODUCT);
            existingProduct.setImageData(imageData);
        }

        var serviceDB = this.serviceProductRepository.save(existingProduct);
        productDetails.forEach(detail -> {
            detail.setProduct(serviceDB);
            this.productDetailService.add(detail, null);
        });

        return existingProduct;
    }

    // private Sort buildSortCriteria(ProductSortingCriteria sortingCriteria) {
    //     if (Objects.isNull(sortingCriteria) || sortingCriteria.isEmptySortingCriteria()) {
    //         return Sort.by(Direction.DESC, "updatedAt");
    //     }
    //     List<Sort.Order> orders = new ArrayList<>();

    //     sortingCriteria.getAsc().stream().filter(value -> !value.equalsIgnoreCase("inventoryStatus")).forEach(value -> orders.add(Sort.Order.by(value).with(Direction.ASC)));
    //     sortingCriteria.getDesc().stream().filter(value -> !value.equalsIgnoreCase("inventoryStatus")).forEach(value -> orders.add(Sort.Order.by(value).with(Direction.DESC)));
    //     return CollectionUtils.isEmpty(orders) ? Sort.unsorted() : Sort.by(orders);
    // }

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

package ct553.backend.product.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.imagedata.ImageData;
import ct553.backend.product.control.ProductDetailRepository;
import ct553.backend.product.entity.InventoryStatus;
import ct553.backend.product.entity.Product;
import ct553.backend.product.entity.ProductDetail;
import ct553.backend.product.entity.ProductDetailDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductDetailService {
    
    @Autowired
    private ProductDetailRepository productDetailRepository;

    public void add(ProductDetail productDetail, ImageData imageData) {
        if (productDetail.getProduct() == null) {
            throw new IllegalArgumentException("Product can not be null in Product Detail");
        }
        Product product = productDetail.getProduct();

        if (product != null) {
            productDetail.setProduct(product);
            productDetail.setImageData(imageData);
            if (productDetail.getInventoryStatus() == null) {
                if (productDetail.getQuantity() > 0) {
                    productDetail.setInventoryStatus(InventoryStatus.ON_HAND);
                }
                else if (productDetail.getQuantity() == 0) {
                    productDetail.setInventoryStatus(InventoryStatus.INCOMING);
                }
            }
            this.productDetailRepository.save(productDetail);
        }
    }

    public ProductDetail updateProductDetail(Long id, ProductDetail productDetailUpdateInfo) {
        ProductDetail existingProductDetail = this.productDetailRepository.findById(id).orElse(null);

        if (Objects.nonNull(existingProductDetail)) {
            existingProductDetail
                    .setQuantity(productDetailUpdateInfo.getQuantity() > 0 ? productDetailUpdateInfo.getQuantity()
                            : existingProductDetail.getQuantity());
            existingProductDetail.setSold(productDetailUpdateInfo.getSold() >= 0 ? productDetailUpdateInfo.getSold()
                    : existingProductDetail.getSold());
            existingProductDetail.setImageData(
                    productDetailUpdateInfo.getImageData() != null ? productDetailUpdateInfo.getImageData()
                            : existingProductDetail.getImageData());
            if (productDetailUpdateInfo.getInventoryStatus().equals(existingProductDetail.getInventoryStatus())) {
                existingProductDetail
                        .setInventoryStatus(existingProductDetail.getSold() == existingProductDetail.getQuantity()
                                && existingProductDetail.getInventoryStatus().equals(InventoryStatus.ON_HAND)
                                        ? InventoryStatus.SOLD_OUT
                                        : existingProductDetail.getInventoryStatus());
            }
            this.productDetailRepository.save(existingProductDetail);
            return existingProductDetail;
        } else {
            return null;
        }
    }

    public ProductDetail updateProductDetail(ProductDetail productDetail) {
        return this.productDetailRepository.save(productDetail);
    }

    ArrayList<ProductDetail> getListProductDetails() {
        return (ArrayList<ProductDetail>) this.productDetailRepository.findAll();
    }

    List<ProductDetailDTO> getAllProductDetails(Long productId) {
        return this.productDetailRepository.findByProduct_Id(productId)
                                                .stream()
                                                .map(ProductDetailDTO::from)
                                                .toList();
    }

    public ProductDetail findProductDetailById(Long id) {
        return this.productDetailRepository.findById(id).orElse(null);
    }

    
    void deleteProductDetailById(Long id) {
        this.productDetailRepository.deleteById(id);
    }

    ProductDetail updateProductDetailImageData(Long id, ImageData imageData) {
        ProductDetail detail = this.findProductDetailById(id);
        detail.setImageData(imageData);
        return this.productDetailRepository.save(detail);
    }

}

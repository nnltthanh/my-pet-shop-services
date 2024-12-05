package ct553.backend.cart.boundary;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.cart.control.CartDetailRepository;
import ct553.backend.cart.control.CartRepository;
import ct553.backend.cart.entity.Cart;
import ct553.backend.cart.entity.CartDetail;
import ct553.backend.product.ProductService;
import ct553.backend.product.productdetail.ProductDetail;
import ct553.backend.product.productdetail.ProductDetailService;
import ct553.backend.user.UserService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    UserService customerService;

    public CartDetail addProductDetailToCart(Long customerId, CartDetail cartDetail) {
        
        Long productDetailId = cartDetail.getProductDetail().getId();

        Cart cart = this.findCartByCustomerId(customerId);

        ProductDetail productDetail = this.productDetailService.findProductDetailById(productDetailId);

        // handle for exist cart detail
        CartDetail cartDetailDB = this.cartDetailRepository.findByProductDetailId(productDetailId).orElse(null);

        int newQuantity = cartDetail.getQuantity();

        if (cartDetailDB != null) {
            newQuantity += cartDetailDB.getQuantity();

            if (newQuantity > productDetail.getQuantity()) {
                newQuantity = productDetail.getQuantity();
            }
        }

        int remainingQuantity = productDetail.getQuantity() - newQuantity;

        if (remainingQuantity >= 0) {
            cartDetail.setQuantity(newQuantity);
            if (cartDetailDB != null) { // make cart detail push on top when add to cart
                this.cartDetailRepository.delete(cartDetailDB);
            }

            this.productDetailService.add(productDetail, null);

            BigDecimal total = BigDecimal.valueOf(cartDetail.getQuantity())
                    .multiply(productDetail.getPrice());

            cartDetail.setProductDetail(productDetail);
            cartDetail.setTotal(total);

            cart.addCartDetail(cartDetail);

            this.cartRepository.save(cart);

            return this.cartDetailRepository.save(cartDetail);
        }

        return null;
    }

    public ArrayList<CartDetail> getAllCartDetails(Long customerId) {
        Cart cart = this.findCartByCustomerId(customerId);

        if (cart.getCartDetails() != null && cart.getCartDetails().size() > 0) {
            for (int i = 0; i < cart.getCartDetails().size(); i++) {
                CartDetail cd = cart.getCartDetails().get(i);

                if (cd.getQuantity() > cd.getProductDetail().getQuantity() - cd.getProductDetail().getSold()) {
                    cd.setQuantity(cd.getProductDetail().getQuantity() - cd.getProductDetail().getSold());
                    if (cd.getQuantity() == 0) {
                        this.deleteCartDetail(cd.getId());
                        cart.getCartDetails().removeIf((cd2) -> cd2.getId().equals(cd.getId()));
                    }
                    else {
                        CartDetail updatedCartDetail = this.updateCartDetail(cd);
                        cart.getCartDetails().set(i, updatedCartDetail);
                    }
                }
            }
        }

        return new ArrayList<>(cart.getCartDetails());
    }

    private Cart findCartByCustomerId(Long customerId) {
        Cart cart = this.cartRepository.findByCustomer_Id(customerId).orElse(new Cart());

        if (cart.getCustomer() == null) {
            cart.setCustomer(this.customerService.findByIdCore(customerId));
            this.cartRepository.saveAndFlush(cart);
        }
        
        return cart;
    }

    public CartDetail findCartDetailById(Long id) {
        return this.cartDetailRepository.findById(id).orElse(null);
    }

    public void deleteCartDetail(Long id) {
        this.cartDetailRepository.deleteById(id);
    }

    public CartDetail updateCartDetail(CartDetail cartDetail) {
        CartDetail cartDetailDB = this.cartDetailRepository.findById(cartDetail.getId()).get();
        
        cartDetailDB.setQuantity(cartDetail.getQuantity());
        cartDetailDB.setTotal(cartDetail.getTotal());

        return this.cartDetailRepository.saveAndFlush(cartDetailDB);
    }

}

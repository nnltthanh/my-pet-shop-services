package ct553.backend.address;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public ArrayList<Address> findAll() {
        return (ArrayList<Address>) addressRepository.findAll();
    }

    public Address findById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public ArrayList<Address> findByCustomerId(Long id) {
        return addressRepository.findByCustomer_Id(id);
    }

    
    public Address findDefaultAddress(Long id) {
        return this.findByCustomerId(id)
                    .stream()
                    .filter(a -> a.getIsDefault() == true)
                    .findFirst().orElse(null);
    }

    public void add(Address address) {
        this.addressRepository.save(address);
    }

    public void deleteById(Long id) {
        this.addressRepository.deleteById(id);
    }

    public Address updateAddress(Long id, Address address) {
        Address existingAddress = findById(address.getId());

        if (existingAddress != null) {
            existingAddress.setBelongsTo(address.getBelongsTo());
            existingAddress.setPhone(address.getPhone());
            existingAddress.setAddress(address.getAddress());
            existingAddress.setCityId(address.getCityId());
            existingAddress.setDistrictId(address.getDistrictId());
            existingAddress.setWardId(address.getWardId());
            existingAddress.setCustomer(address.getCustomer());

            if (!address.getIsDefault()) {
                existingAddress.setIsDefault(address.getIsDefault());
            }

            if (address.getIsDefault() && this.findDefaultAddress(id) != null) {
                Address defaultAddress = this.findDefaultAddress(id);
                defaultAddress.setIsDefault(false);
                this.addressRepository.save(defaultAddress);

                existingAddress.setIsDefault(address.getIsDefault());
            }

            this.addressRepository.save(existingAddress);
            return existingAddress;
        }
        return null;
    }
}

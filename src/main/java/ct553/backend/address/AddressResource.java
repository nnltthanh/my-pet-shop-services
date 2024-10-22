package ct553.backend.address;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/addresses")
public class AddressResource {

    @Autowired
    AddressService addressService;

    @GetMapping
    public ArrayList<Address> getAllAddresses() {
        return this.addressService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Long id) {
        Address address = addressService.findById(id);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}/default")
    public Address getDefaultAddress(@PathVariable Long id) {
        return addressService.findDefaultAddress(id);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getAddressByCustomerId(@PathVariable Long id) {
        List<Address> addresses = addressService.findByCustomerId(id);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody Address address) {
        this.addressService.add(address);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long id) {
        Address address = addressService.findById(id);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.addressService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/customer/{id}")
    public void updateAddressByCustomerId(@PathVariable Long id, @RequestBody Address address) {
        this.addressService.updateAddress(id, address);
    }
}

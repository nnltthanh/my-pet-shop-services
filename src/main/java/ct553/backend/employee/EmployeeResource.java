package ct553.backend.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/staffs")
public class EmployeeResource {

    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public ArrayList<Employee> getAllStaffs() {
        return this.employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Long id) {
        Employee staff = employeeService.findById(id);
        if (staff == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(staff, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<?> addStaff(@RequestBody Employee staff) {
        // Check if customer exist or not?
        Employee isExistedStaff = employeeService.findById(staff.getId());
        if (isExistedStaff == null) {
        this.employeeService.add(staff);
        return new ResponseEntity<>(staff, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaffById(@PathVariable Long id) {
        Employee staff = employeeService.findById(id);
        if (staff == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.employeeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

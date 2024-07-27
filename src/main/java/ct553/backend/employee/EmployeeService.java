package ct553.backend.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public ArrayList<Employee> findAll() {
        return (ArrayList<Employee>) employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void add(Employee manager) {
        this.employeeRepository.save(manager);
    }


    public void deleteById(Long id) {
        this.employeeRepository.deleteById(id);
    }
}

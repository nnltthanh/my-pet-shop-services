package ct553.backend.privilege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public List<PrivilegeDTO> findAll() {
        List<Privilege> privileges = this.privilegeRepository.findAll();
        return privileges.stream().map(PrivilegeDTO::new).toList();
    }

    public PrivilegeDTO findById(Long id) {
        // Privilege privilege = privilegeRepository.findById(id).orElse(null);
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        // if (privilege != null) { // need to check TODO
        //     Hibernate.initialize(privilege); // Eagerly fetch roles
        //     return modelMapper.map(privilege, PrivilegeDTO.class);
        // }
        return null;
    }

    public Privilege findByName(String name) {
        return this.privilegeRepository.findByName(name).orElse(null);
    }

    public void add(Privilege role) {
        if (this.findById(role.getId()) == null) {
            this.privilegeRepository.save(role);
        }
    }

    public void deleteById(Long id) {
        this.privilegeRepository.deleteById(id);
    }
}

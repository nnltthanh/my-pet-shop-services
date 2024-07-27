package ct553.backend.pet.boundary;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.pet.control.PetRepository;
import ct553.backend.pet.entity.Pet;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public ArrayList<Pet> findAll() {
        return (ArrayList<Pet>) petRepository.findAll();
    }

    public Pet findById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    public void add(Pet pet) {
        if (Objects.nonNull(pet)) {
            pet.setEngName(deAccent(pet.getName()));
        }
        this.petRepository.save(pet);
    }

    public void deleteById(Long id) {
        this.petRepository.deleteById(id);
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

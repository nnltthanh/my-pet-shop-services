package ct553.backend.imagedata;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ImageDataService {

    @Autowired
    ImageDataRepository imageDataRepository;

    public ArrayList<ImageData> findAll() {
        return (ArrayList<ImageData>) this.imageDataRepository.findAll();
    }

    public ImageData findById(Long id) {
        return this.imageDataRepository.findById(id).orElse(null);
    }

    public ImageData addImageData(ImageData data) {
        return this.imageDataRepository.save(data);
    }

    public ImageData update(Long id, ImageData data) {
        ImageData existingImageData = findById(id);
        if (existingImageData != null) {
            // Update fields based on your requirements
            existingImageData.setPath(data.getPath());
            this.imageDataRepository.save(existingImageData);
            return existingImageData;
        }
        return null;
    }

    public boolean delete(Long id) {
        ImageData resultFindImageData = findById(id);
        if (resultFindImageData!= null) {
            this.imageDataRepository.delete(resultFindImageData);
            return true;
        } else {
            return false;
        }
    }
}

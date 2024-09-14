package ct553.backend.imagedata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.CloudinaryService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ImageDataService {

    @Autowired
    ImageDataRepository imageDataRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    public ImageData buildImageData(List<MultipartFile> files, ImageDataType type) throws IOException {
        StringBuilder imageUrls = new StringBuilder();

        for (MultipartFile multipartFile : files) {
            String imageUrl = this.cloudinaryService.uploadFile(multipartFile);
            imageUrls.append(imageUrl);
            if (multipartFile != files.get(files.size() - 1))
                imageUrls.append(", ");
        }

        return new ImageData(imageUrls.toString(), type);
        // data = this.imageDataService.addImageData(data);
    }

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
            existingImageData.setImageUrls(data.getImageUrls());
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

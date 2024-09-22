package ct553.backend.importer;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploaderService {
    
    public void uploadFile(MultipartFile file);

}

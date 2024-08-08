package by.intexsoft.diplom.person.service;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DropBoxService {

        private final DbxClientV2 dbxClientV2;

        public String uploadFile(MultipartFile file) {
            try {
                String dropboxPath = "/free-party/" + file.getOriginalFilename();
                ByteArrayInputStream fileInputStream = new ByteArrayInputStream(file.getBytes());
                FileMetadata metadata = dbxClientV2.files().uploadBuilder(dropboxPath)
                        .uploadAndFinish(fileInputStream);
                return metadata.getPathLower();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload file to Dropbox", e);
            } catch (UploadErrorException e) {
                e.printStackTrace();
                throw new RuntimeException("Dropbox API upload error", e);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("General Dropbox API error", e);
            }
        }
}
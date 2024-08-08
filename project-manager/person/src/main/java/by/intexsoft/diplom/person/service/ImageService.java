package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.person.exception.InvalidFileSizeException;
import by.intexsoft.diplom.person.exception.InvalidFileTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service for saving and manipulating images
 * @author Chaplygin Mihail
 */
@Service
@RequiredArgsConstructor
public class ImageService {

        private final DropBoxService dropBoxService;
        private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

        /**
         * validate file, saving images into dropbox and setting name into List for party creating
         * @param files - MultipartFile which will be saved into dropbox
         * @return - return set of files' names for returning to front and saving into db
         */
        public List<String> saveImagesToDropBox(List<MultipartFile> files) {
            List<String> filesName = new ArrayList<>();
            for (MultipartFile file : files) {
               isFileValid(file);
                System.out.println("fdx");
                filesName.add(file.getOriginalFilename());
                dropBoxService.uploadFile(file);
            }
            return filesName;
        }

        private void isFileValid(MultipartFile file){
            isFileSizeValid(file);
            isFileTypeValid(file);
        }

        private void isFileSizeValid(MultipartFile file){
            if(file.getSize()>MAX_FILE_SIZE){
                throw new InvalidFileSizeException("file must be max 5mb");
            }
        }

        private void isFileTypeValid(MultipartFile file) {
            if (!Objects.equals(file.getContentType(), "image/jpeg") &&
                    !Objects.equals(file.getContentType(), "image/png") &&
                    !Objects.equals(file.getContentType(), "image/jpg")) {
                throw new InvalidFileTypeException("Invalid file format. Only JPEG and PNG files are allowed.");
            }
        }
}
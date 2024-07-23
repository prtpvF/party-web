package by.intexsoft.diplom.person.controller;

import by.intexsoft.diplom.person.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

        private final ImageService imageService;

        @PostMapping("/image")
        public List<String> saveImagesIntoDropbox(@ModelAttribute List<MultipartFile> files) {
            return imageService.saveImagesToDropBox(files);
        }
}

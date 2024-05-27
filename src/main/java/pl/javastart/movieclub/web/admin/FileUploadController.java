package pl.javastart.movieclub.web.admin;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import pl.javastart.movieclub.storage.FileStorageService;

import java.net.URI;

@Controller
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(path = "/admin/images", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> saveFile(@RequestParam MultipartFile file) {
        String fileName = fileStorageService.saveImage(file);
        URI createdUri = UriComponentsBuilder
                .fromUriString("/img/")
                .path("{fileName}")
                .buildAndExpand(fileName)
                .toUri();
        return ResponseEntity.created(createdUri).body(new FileResponse(fileName));
    }

    private record FileResponse(String fileName){}
}

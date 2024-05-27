package pl.javastart.movieclub.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    FileStorageService fileStorageService;

    @BeforeEach
    void init() {
        fileStorageService = new FileStorageService(tempDir.toAbsolutePath().toString());
    }

    @Test
    void shouldSaveTextFileWithRandomName() {
        String randomFilename = UUID.randomUUID() + ".txt";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                randomFilename,
                randomFilename,
                MediaType.TEXT_PLAIN_VALUE,
                "content to test".getBytes()
        );
        String savedFile = fileStorageService.saveFile(mockMultipartFile);
        Path savedFilePath = tempDir.resolve("files").resolve(savedFile);
        assertTrue(Files.exists(savedFilePath));
    }

    @Test
    void shouldSaveImage() throws IOException {
        Path testFilePath = Path.of(FileStorageServiceTest.class.getResource("chucky.jpeg").getPath());
        byte[] content = Files.readAllBytes(testFilePath);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "chucky.jpeg",
                "chucky.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                content
        );
        String savedFile = fileStorageService.saveImage(mockMultipartFile);
        Path savedFilePath = tempDir.resolve("img").resolve(savedFile);
        assertTrue(Files.exists(savedFilePath));
    }

    @Test
    void shouldSaveMultipleImagesWithSameName() throws IOException {
        Path testFilePath = Path.of(FileStorageServiceTest.class.getResource("chucky.jpeg").getPath());
        byte[] content = Files.readAllBytes(testFilePath);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "chucky.jpeg",
                "chucky.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                content
        );
        String savedFile1 = fileStorageService.saveImage(mockMultipartFile);
        String savedFile2 = fileStorageService.saveImage(mockMultipartFile);
        Path savedFilePath1 = tempDir.resolve("img").resolve(savedFile1);
        Path savedFilePath2 = tempDir.resolve("img").resolve(savedFile1);
        assertTrue(Files.exists(savedFilePath1));
        assertTrue(Files.exists(savedFilePath2));
        assertNotEquals(savedFile1, savedFile2);
    }
}
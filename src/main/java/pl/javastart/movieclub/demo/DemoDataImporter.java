package pl.javastart.movieclub.demo;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Imports the data for demonstration purposes
 */

@Service
@Profile("demo")
@DependsOn("fileStorageService")
class DemoDataImporter {
    private final Logger logger = LoggerFactory.getLogger(DemoDataImporter.class);
    private final String imageStorageLocation;

    public DemoDataImporter(@Value("${app.storage.location}") String storageLocation) {
        this.imageStorageLocation = storageLocation + "/img/";
    }

    @PostConstruct
    void importPosters() throws IOException, URISyntaxException {
        Path demoDirectory = Path.of(DemoDataImporter.class.getResource("/demo/").toURI().getPath());
        logger.info("Importing data from %s to %s".formatted(
                demoDirectory,
                imageStorageLocation
        ));
        try (Stream<Path> demoFiles = Files.walk(demoDirectory)) {
            demoFiles
                    .filter(Files::isRegularFile)
                    .forEach(
                            file -> {
                                try {
                                    Files.copy(
                                            file,
                                            Path.of(imageStorageLocation).resolve(file.getFileName()),
                                            StandardCopyOption.REPLACE_EXISTING
                                    );
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            }
                    );
        }
        logger.info("Import completed");
    }
}

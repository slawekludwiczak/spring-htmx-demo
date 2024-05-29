package pl.javastart.movieclub.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.javastart.movieclub.domain.movie.MovieRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Imports the data for demonstration purposes
 */

@Service
@Profile("demo")
@DependsOn("fileStorageService")
class DemoDataImporter {
    private final String imageStorageLocation;

    public DemoDataImporter(@Value("${app.storage.location}") String storageLocation,
                            MovieRepository movieRepository) {
        this.imageStorageLocation = storageLocation + "/img/";
    }

    @PostConstruct
    void importPosters() throws IOException, URISyntaxException {
        Path demoDirectory = Path.of(DemoDataImporter.class.getResource("/demo/").toURI().getPath());

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
    }
}

package tic;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceLoader {

  private ResourceLoader() {}

  public static String getFilePath(String resourcePath) throws URISyntaxException {
    Path path = getPath(resourcePath);

    return path.toString();
  }

  public static String readString(String resourcePath) throws IOException, URISyntaxException {
    return new String(readAllBytes(resourcePath));
  }

  public static byte[] readAllBytes(String resourcePath) throws IOException, URISyntaxException {
    Path path = getPath(resourcePath);

    return Files.readAllBytes(path);
  }

  public static Path getPath(String resourcePath) throws URISyntaxException {
    URL url = ResourceLoader.class.getResource(resourcePath);
    URI uri = url.toURI();
    Path path = Paths.get(uri);

    return path;
  }
}

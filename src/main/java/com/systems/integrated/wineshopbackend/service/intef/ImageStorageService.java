package com.systems.integrated.wineshopbackend.service.intef;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface ImageStorageService {
    public void init();

    public int saveNewImage(MultipartFile file, Long productId);

    public Resource load(String filename);

    public List<String> getNumberOfImagesForProductId(Long productId);
}

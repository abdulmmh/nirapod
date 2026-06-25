package com.nirapod.controller;

import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.model.Taxpayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@Transactional
@RestController
@RequestMapping("/api/taxpayers")
public class FileUploadController {

  @Value("${app.upload.dir}")
  private String uploadDir;

  @Autowired
  private TaxpayerDAO taxpayerDAO;

  @PostMapping("/{id}/photo")
  public ResponseEntity<?> uploadPhoto(
      @PathVariable Long id,
      @RequestParam("file") MultipartFile file) {

    if (file.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", "File is empty."));
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", "Only image files are allowed."));
    }

    if (file.getSize() > 5 * 1024 * 1024) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", "File size must be less than 5MB."));
    }

    Taxpayer taxpayer = taxpayerDAO.getById(id);
    if (taxpayer == null) {
      return ResponseEntity.notFound().build();
    }

    try {
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      if (taxpayer.getPhotoPath() != null) {
        String oldFileName = Paths.get(taxpayer.getPhotoPath()).getFileName().toString();
        Path oldFile = uploadPath.resolve(oldFileName);
        Files.deleteIfExists(oldFile);
      }

      String ext = getExtension(file.getOriginalFilename());
      String fileName = "taxpayer_" + id + "_" +
          UUID.randomUUID().toString().substring(0, 8) + ext;
      Path filePath = uploadPath.resolve(fileName);
      Files.copy(file.getInputStream(), filePath,
          StandardCopyOption.REPLACE_EXISTING);

      String photoUrl = "/uploads/profile-photos/" + fileName;
      taxpayer.setPhotoPath(photoUrl);
      taxpayerDAO.update(taxpayer);

      return ResponseEntity.ok(Map.of(
          "message", "Photo uploaded successfully.",
          "photoUrl", photoUrl));

    } catch (IOException e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "Failed to upload photo. " + e.getMessage()));
    }
  }

  private String getExtension(String filename) {
    if (filename == null)
      return ".jpg";
    int dot = filename.lastIndexOf('.');
    return dot >= 0 ? filename.substring(dot) : ".jpg";
  }
}

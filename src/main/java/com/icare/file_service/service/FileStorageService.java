package com.icare.file_service.service;

import com.icare.file_service.constansts.ApplicationMap;
import com.icare.file_service.constansts.DoctypeMap;
import com.icare.file_service.dto.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    @Autowired
    private DoctypeMap doctypeMap;

    private final List<String> allowedFileTypes = List.of("png", "jpeg", "pdf", "jpg", "gif", "doc", "docx");

    public SuccessResponse storeFile(MultipartFile file,
                                     Integer app,
                                     Integer docTypeKey) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        // Build the directory path dynamically
        Path uploadPath = Paths.get(uploadDir, "temp");
        String originalFilename = "";
        String fileName = "";
        String fileType = "";
        String uniqueFileName = "";

        Map<Integer, String > docMap = doctypeMap.getDoctypeMap();
        String documentType = docMap.get(docTypeKey);
        if (documentType == null) {
            throw new RuntimeException("Invalid document type");
        }

        String application = ApplicationMap.applicationMap.get(app);
        if (application == null) throw new RuntimeException("Invalid application");

        try {
            // Create directories if they don’t exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                String [] fileNameParts = originalFilename.split("\\.");
                fileType = fileNameParts[fileNameParts.length-1].toLowerCase();
                if (!allowedFileTypes.contains(fileType)){
                    throw new RuntimeException("Invalid file type, " + fileType  +" allowed file types are : " + allowedFileTypes.toString() );
                }

                // ✅ Sanitize base name
                String baseName = originalFilename.replaceAll("\\.[^.]*$", ""); // remove extension
                baseName = baseName.replaceAll("[^a-zA-Z0-9]", "_"); // replace special chars/spaces
                if (baseName.length() > 10) baseName = baseName.substring(0, 10);

                // ✅ Add unique suffix
                String uniqueSuffix = String.valueOf(System.currentTimeMillis()) + UUID.randomUUID().toString().substring(0, 5); // or UUID.randomUUID().toString()
                uniqueFileName = baseName + "_" + uniqueSuffix + "." + fileType;
                fileName = String.join("-", application, documentType, fileType,
                        uniqueFileName);

                // Save the file
                Path filePath = uploadPath.resolve(fileName);
                // ✅ Properly close InputStream
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String staticPath = "/" + application + "/" + documentType + "/" + fileType + "/" +  uniqueFileName;

        // Build access path only
        String path = baseUrl+ "/" + "temp" + "/" + fileName;

        return new SuccessResponse(staticPath, path, 200, "File uploaded successfully", true);
    }


    public SuccessResponse confirmFile(String originalFilename) {
        Path tempDir = Paths.get(uploadDir, "temp");

        if (!Files.exists(tempDir)) {
            throw new RuntimeException("Temp directory not found");
        }

        try (Stream<Path> files = Files.list(tempDir)) {
            // Find the file which ends with -originalFilename
            Path tempFile = files
                    .filter(path -> path.getFileName().toString().endsWith("-" + originalFilename))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("File not found in temp: " + originalFilename));

            // Split name into 4 parts: application, docType, fileType, originalName
            String tempFileName = tempFile.getFileName().toString();
            String[] parts = tempFileName.split("-", 4);
            if (parts.length < 4) {
                throw new RuntimeException("Invalid file name format: " + tempFileName);
            }

            String application = parts[0];
            String docType = parts[1];
            String fileType = parts[2];
            String finalOriginalName = parts[3]; // should equal originalFilename

            // Destination directory
            Path finalDir = Paths.get(uploadDir, application, docType, fileType);
            if (!Files.exists(finalDir)) {
                Files.createDirectories(finalDir);
            }

            Path finalPath = finalDir.resolve(finalOriginalName);

            // Move file
            Files.move(tempFile, finalPath, StandardCopyOption.REPLACE_EXISTING);

            String returnPath = baseUrl + "/" + application + "/"  + docType + "/" + fileType + "/" + originalFilename;
            String staticPath = "/" + application + "/"  + docType + "/" + fileType + "/" + originalFilename;
            return new SuccessResponse(staticPath, returnPath, 200, "File confirmed successfully", true);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to confirm file: " + ex.getMessage(), ex);
        }
    }
}

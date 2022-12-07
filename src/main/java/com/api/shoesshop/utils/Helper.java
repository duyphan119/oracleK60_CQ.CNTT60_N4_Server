package com.api.shoesshop.utils;

import java.io.BufferedReader;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.api.shoesshop.types.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class Helper {
    public static final int SALT = 10;

    public static String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(SALT));
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public static String getRequestBodyJSON(BufferedReader reader) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static String toJson(Object data) {
        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter()
                    .writeValueAsString(data);
        } catch (Exception e) {
            System.out.println(e);
            return "abc";
        }
    }

    public static ResponseEntity<String> responseSuccess(Object data) {
        return new ResponseEntity<>(toJson(new ResponseData<>(200, "Success", data)), HttpStatus.OK);
    }

    public static ResponseEntity<String> responseError() {
        return new ResponseEntity<>(toJson(new ResponseData<>(500, "Error")), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<String> responseCreated(Object data) {
        return new ResponseEntity<>(toJson(new ResponseData<>(201, "Success", data)), HttpStatus.OK);
    }

    public static ResponseEntity<String> responseSuccessNoData() {
        return new ResponseEntity<>(toJson(new ResponseData<>(200, "Success")), HttpStatus.OK);
    }

    public static ResponseEntity<String> responseUnauthorized() {
        return new ResponseEntity<>(toJson(new ResponseData<>(401, "Error")), HttpStatus.UNAUTHORIZED);
    }

    public static Pageable getPageable(Map<String, String> query) {
        String page = query.get("p");
        String pageSize = query.get("limit");
        String sortBy = query.get("sort_by");
        String sortType = query.get("sort_type");
        if (sortBy == null) {
            sortBy = "id";
        }
        Sort sort = Sort.by(sortBy);
        if (sortType == null || sortType.equals("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        return PageRequest.of(page == null ? 0 : Integer.parseInt(page) - 1,
                pageSize == null ? 10 : Integer.parseInt(pageSize), sort);
    }

    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
}

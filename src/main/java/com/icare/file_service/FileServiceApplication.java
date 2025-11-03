package com.icare.file_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileServiceApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("file.upload-dir", dotenv.get("UPLOAD_DIR"));
        System.setProperty("file.base-url", dotenv.get("FILE_BASE_URL"));
        System.setProperty("server.port", dotenv.get("SERVER_PORT"));
        System.setProperty("server.address", dotenv.get("SERVER_ADDRESS"));

		SpringApplication.run(FileServiceApplication.class, args);
	}

}

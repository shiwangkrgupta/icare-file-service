package com.icare.file_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileServiceApplicationTests {

    static {
        io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.load();
        System.setProperty("file.upload-dir", dotenv.get("UPLOAD_DIR"));
        System.setProperty("file.base-url", dotenv.get("FILE_BASE_URL"));
        System.setProperty("server.port", dotenv.get("SERVER_PORT"));
        System.setProperty("server.address", dotenv.get("SERVER_ADDRESS"));
    }

	@Test
	void contextLoads() {
	}

}

package com.icare.file_service.service.fileexplorer;

import com.icare.file_service.dto.fileexplorer.DirectoryDto;
import com.icare.file_service.dto.fileexplorer.FileDto;
import com.icare.file_service.dto.fileexplorer.TempFileDto;

import java.util.List;

public interface FileExplorerService {

    // Applications
    List<DirectoryDto> getApplications();

    // Doctypes under application
    List<DirectoryDto> getDoctypes(String application);

    // File types under doctype
    List<DirectoryDto> getFileTypes(String application, String doctype);

    // Files under filetype
    List<FileDto> getFiles(String application, String doctype, String fileType);

    // Temp files
    List<TempFileDto> getTempFiles();
}

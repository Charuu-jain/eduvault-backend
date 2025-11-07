package com.eduvault.controller;

import com.eduvault.model.StoredFile;
import com.eduvault.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.eduvault.model.Subject;
import com.eduvault.repository.SubjectRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileRepository fileRepository;
    private final SubjectRepository subjectRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileController(FileRepository fileRepository, SubjectRepository subjectRepository) {
        this.fileRepository = fileRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping("/upload")
    public StoredFile uploadFile(@RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "subjectId", required = false) Long subjectId) throws IOException {
        String fileName = file.getOriginalFilename();
        File destinationFile = new File(uploadDir + File.separator + fileName);
        destinationFile.getParentFile().mkdirs();
        file.transferTo(destinationFile);

        StoredFile storedFile = new StoredFile();
        storedFile.setFileName(fileName);
        storedFile.setFilePath(destinationFile.getAbsolutePath());
        storedFile.setFileSize(file.getSize());

        if (subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + subjectId));
            storedFile.setSubject(subject);
        }

        return fileRepository.save(storedFile);
    }

    @GetMapping
    public List<StoredFile> getAllFiles(@RequestParam(value = "subjectId", required = false) Long subjectId) {
        if (subjectId != null) {
            return fileRepository.findBySubjectId(subjectId);
        }
        return fileRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileRepository.deleteById(id);
    }

    @GetMapping("/count")
    public long countFiles() {
        return fileRepository.count();
    }
}
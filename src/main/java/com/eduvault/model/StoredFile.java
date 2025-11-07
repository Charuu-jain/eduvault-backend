package com.eduvault.model;

import jakarta.persistence.*;
import com.eduvault.model.Subject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "files")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private long fileSize;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public StoredFile() {}

    public StoredFile(String fileName, String filePath, long fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
}
package com.eduvault.repository;

import com.eduvault.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileRepository extends JpaRepository<StoredFile, Long> {
    List<StoredFile> findBySubjectId(Long subjectId);
}
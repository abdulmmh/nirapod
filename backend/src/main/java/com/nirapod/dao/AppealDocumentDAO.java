package com.nirapod.dao;

import com.nirapod.model.AppealDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppealDocumentDAO extends JpaRepository<AppealDocument, Long> {

    List<AppealDocument> findByAppealIdAndIsDeletedFalseOrderByUploadedAtDesc(Long appealId);
}
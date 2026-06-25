package com.nirapod.dao;

import com.nirapod.model.RefundDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundDocumentDAO extends JpaRepository<RefundDocument, Long> {

    List<RefundDocument> findByRefund_IdOrderByUploadedAtAsc(Long refundId);

    Optional<RefundDocument> findByIdAndRefund_Id(Long docId, Long refundId);

    void deleteByRefund_Id(Long refundId);

    long countByRefund_Id(Long refundId);
}

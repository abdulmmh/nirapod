package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.AitDocumentRequestDAO;
import com.nirapod.dao.AitRecordDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.AitDocumentRequestDTO;
import com.nirapod.dto.response.AitDocumentRequestDto;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.AitDocumentRequest;
import com.nirapod.model.AitRecord;
import com.nirapod.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AitDocumentRequestService {
	
	@Autowired
    private AitDocumentRequestDAO aitDocumentRequestDAO;
	
	@Autowired
    private AitRecordDAO aitRecordDAO;
	
	@Autowired
    private UserDAO userDAO;


    public AitDocumentRequestDto createRequest(Long aitRecordId, AitDocumentRequestDTO requestDTO,
                                               AuthPrincipal principal) {
        AitRecord aitRecord = aitRecordDAO.getById(aitRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("AIT record not found"));

        User requestedBy = resolveUser(principal);

        if (requestDTO.getDeadline() != null && requestDTO.getDeadline().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past");
        }

        AitDocumentRequest request = new AitDocumentRequest();
        request.setAitRecord(aitRecord);
        request.setRequestType(requestDTO.getRequestType());
        request.setRequestedDocuments(requestDTO.getRequestedDocuments());
        request.setRequestReason(requestDTO.getRequestReason());
        request.setRequestedBy(requestedBy);
        request.setDeadline(requestDTO.getDeadline() != null ? requestDTO.getDeadline() : LocalDate.now().plusDays(7));
        request.setStatus("PENDING");
        request.setIsDeleted(false);

        AitDocumentRequest saved = aitDocumentRequestDAO.save(request);
        return AitDocumentRequestDto.from(saved);
    }

    public List<AitDocumentRequestDto> getRequestsByAitRecordId(Long aitRecordId) {
        aitRecordDAO.getById(aitRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("AIT record not found"));

        return aitDocumentRequestDAO.getByAitRecordId(aitRecordId).stream()
            .map(AitDocumentRequestDto::from)
            .collect(Collectors.toList());
    }

    public List<AitDocumentRequestDto> getPendingRequests() {
        return aitDocumentRequestDAO.getPending().stream()
            .map(AitDocumentRequestDto::from)
            .collect(Collectors.toList());
    }

    public List<AitDocumentRequestDto> getOverdueRequests() {
        return aitDocumentRequestDAO.getOverdue().stream()
            .map(AitDocumentRequestDto::from)
            .collect(Collectors.toList());
    }

    public AitDocumentRequestDto markFulfilled(Long requestId, AuthPrincipal principal) {
        AitDocumentRequest request = aitDocumentRequestDAO.getByAitRecordId(requestId).stream()
            .filter(r -> r.getId().equals(requestId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Document request not found"));

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Can only fulfill PENDING requests");
        }

        request.setStatus("FULFILLED");
        AitDocumentRequest updated = aitDocumentRequestDAO.save(request);
        return AitDocumentRequestDto.from(updated);
    }

    public void deleteRequest(Long requestId, AuthPrincipal principal) {
        List<AitDocumentRequest> requests = aitDocumentRequestDAO.getPending().stream()
            .filter(r -> r.getId().equals(requestId))
            .collect(Collectors.toList());

        if (requests.isEmpty()) {
            throw new ResourceNotFoundException("Document request not found");
        }

        AitDocumentRequest request = requests.get(0);
        request.setIsDeleted(true);
        aitDocumentRequestDAO.save(request);
    }

    private User resolveUser(AuthPrincipal principal) {
        return userDAO.findByEmail(principal.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.IT10BDAO;
import com.nirapod.dao.IncomeTaxReturnDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.model.IT10B;
import com.nirapod.model.IncomeTaxReturn;
import com.nirapod.model.User;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IT10BService {

    @Autowired
    private IT10BDAO it10bDAO;
    
    @Autowired 
    private UserDAO userDAO;

    @Autowired
    private IncomeTaxReturnDAO incomeTaxReturnDAO;  
    
    private static final Set<String> IT10B_BLOCKED_CATEGORIES = Set.of("Company");
    
	private static final Set<String> IT10B_ACTIVE_STATUSES = Set.of(
		    "Draft", "Submitted", "Under Review", "Send Back", "Overdue", "Amended"
		);

    @Transactional
    public IT10B createStatement(IT10B it10b) {
    	

        if (it10b.getReturnId() == null) {
            throw new IllegalArgumentException("returnId is required to link an IT-10B statement.");
        }

        IncomeTaxReturn itr = incomeTaxReturnDAO.findByIdAndIsDeletedFalse(it10b.getReturnId())
        	    .orElseThrow(() -> new IllegalArgumentException("ITR not found or deleted."));
        
        String category = itr.getItrCategory();
    	if ("Company".equals(category)) {
    	    throw new IllegalArgumentException(
    	        "IT-10B is for Individual and Partnership taxpayers only. " +
    	        "Companies submit audited financial statements instead.");
    	}
    	

    		if (!IT10B_ACTIVE_STATUSES.contains(itr.getStatus())) {
    		    throw new IllegalStateException(
    		        "Cannot file IT-10B for a return with status '" + itr.getStatus() + "'.");
    		}
        
        if (it10bDAO.existsByIncomeTaxReturn_IdAndIsDeletedFalse(itr.getId())) {
            throw new IllegalStateException(
                    "An IT-10B statement already exists for return ID: " + itr.getId()
                    + " (Return No: " + itr.getReturnNo() + ").");
        }

        it10b.setIncomeTaxReturn(itr);

        it10b.setNetWealth(calculateNetWealth(it10b));

        return it10bDAO.save(it10b);
    }


    public IT10B getStatementByReturnId(Long returnId) {
        return it10bDAO.findByIncomeTaxReturn_IdAndIsDeletedFalse(returnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No IT-10B statement found for Income Tax Return ID: " + returnId));
    }


    public IT10B getStatementById(Long id) {
        return it10bDAO.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "IT-10B statement not found with ID: " + id));
    }


    @Transactional
    public IT10B updateStatement(Long id, IT10B updatedData) {
        IT10B existing = getStatementById(id);
        
        IncomeTaxReturn itr = existing.getIncomeTaxReturn();
        if (itr != null && (
            "Accepted".equals(itr.getStatus()) || "Rejected".equals(itr.getStatus()))) {
            throw new IllegalStateException(
                "Cannot update IT-10B for a return that is already " + itr.getStatus() + ".");
        }

        existing.setNonAgriculturalProperty(updatedData.getNonAgriculturalProperty());
        existing.setAgriculturalProperty(updatedData.getAgriculturalProperty());
        existing.setInvestments(updatedData.getInvestments());
        existing.setMotorVehicles(updatedData.getMotorVehicles());
        existing.setBankBalances(updatedData.getBankBalances());
        existing.setPersonalLiabilities(updatedData.getPersonalLiabilities());

        existing.setNetWealth(calculateNetWealth(existing));

        return it10bDAO.save(existing);
    }
    
    public Long resolveMyTaxpayerId(AuthPrincipal me) {
        return userDAO.findByEmail(me.getEmail())
            .map(User::getTaxpayerId)
            .orElse(null);
    }

    @Transactional
    public void deleteStatement(Long id) {
        IT10B existing = getStatementById(id);
        existing.setDeleted(true);
        it10bDAO.save(existing);
    }
    
    
    public IT10B getStatementByReturnId(Long returnId, AuthPrincipal me) {
        IT10B stmt = it10bDAO.findByIncomeTaxReturn_IdAndIsDeletedFalse(returnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No IT-10B statement found for Return ID: " + returnId));

        if ("TAXPAYER".equals(me.getRole())) {
            Long myTaxpayerId = resolveMyTaxpayerId(me);
            IncomeTaxReturn itr = stmt.getIncomeTaxReturn();
            if (itr == null || itr.getTaxpayer() == null
                    || !itr.getTaxpayer().getId().equals(myTaxpayerId)) {
                throw new SecurityException("You do not have access to this IT-10B statement.");
            }
        }
        return stmt;
    }

    private Double calculateNetWealth(IT10B it10b) {
        double totalAssets =
                nullSafe(it10b.getNonAgriculturalProperty()) +
                nullSafe(it10b.getAgriculturalProperty())    +
                nullSafe(it10b.getInvestments())             +
                nullSafe(it10b.getMotorVehicles())           +
                nullSafe(it10b.getBankBalances());

        double totalLiabilities = nullSafe(it10b.getPersonalLiabilities());

        return totalAssets - totalLiabilities;
    }

    private double nullSafe(Double value) {
        return value != null ? value : 0.0;
    }
}

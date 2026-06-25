package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.service.AitCertificateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ait-records")
public class AitCertificateController {

  @Autowired
  private AitCertificateService certificateService;

  @GetMapping("/{id}/certificate")
  @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
  public void downloadCertificate(
      @PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal principal,
      HttpServletResponse response) throws Exception {

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition",
        "inline; filename=\"AIT-Certificate-" + id + ".pdf\"");

    certificateService.generateCertificate(id, response);
  }
}

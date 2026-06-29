package com.musicdiary.controllers;

import com.musicdiary.dtos.DashboardResponseDTO;
import com.musicdiary.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> renderDashboard() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(dashboardService.getDashboard(email));
    }
}

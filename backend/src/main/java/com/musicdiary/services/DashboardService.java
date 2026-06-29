package com.musicdiary.services;

import com.musicdiary.dtos.DashboardResponseDTO;

public interface DashboardService {

    DashboardResponseDTO getDashboard(String email);
}

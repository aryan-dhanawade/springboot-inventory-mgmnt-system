package com.aryan.inventory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aryan.inventory.dto.DashboardResponse;
import com.aryan.inventory.service.DashboardService;


@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
	
	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}
	
	@GetMapping
	public DashboardResponse getDashboard() {
		return dashboardService.getDashboard();
	}
	
	

}

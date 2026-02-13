package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.util.AIHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for AI-based doctor recommendations.
 */
@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final DoctorService doctorService;

    public AIController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<Doctor>> recommendDoctors(@RequestParam String symptoms) {
        List<Doctor> recommended = AIHelper.recommendDoctors(symptoms, doctorService.getAllDoctors());
        return ResponseEntity.ok(recommended);
    }

    @GetMapping("/slots")
    public ResponseEntity<Map<String, Object>> suggestSlots() {
        return ResponseEntity.ok(Map.of("availableSlots", AIHelper.suggestTimeSlots()));
    }
}

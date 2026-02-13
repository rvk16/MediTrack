package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Rule-based AI helper for doctor recommendations and appointment slot suggestions.
 * Demonstrates: collections, streams, lambdas, Map usage.
 */
public final class AIHelper {

    private static final Logger log = LoggerFactory.getLogger(AIHelper.class);

    private AIHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Symptom-to-specialization mapping
    private static final Map<String, Specialization> SYMPTOM_MAP = new LinkedHashMap<>();

    static {
        // Cardiology symptoms
        SYMPTOM_MAP.put("chest pain", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("heart", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("palpitation", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("blood pressure", Specialization.CARDIOLOGY);

        // Dermatology symptoms
        SYMPTOM_MAP.put("skin", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("rash", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("acne", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("allergy", Specialization.DERMATOLOGY);

        // Neurology symptoms
        SYMPTOM_MAP.put("headache", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("migraine", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("dizziness", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("seizure", Specialization.NEUROLOGY);

        // Orthopedics symptoms
        SYMPTOM_MAP.put("bone", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("joint pain", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("fracture", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("back pain", Specialization.ORTHOPEDICS);

        // Pediatrics
        SYMPTOM_MAP.put("child", Specialization.PEDIATRICS);
        SYMPTOM_MAP.put("infant", Specialization.PEDIATRICS);

        // ENT
        SYMPTOM_MAP.put("ear", Specialization.ENT);
        SYMPTOM_MAP.put("throat", Specialization.ENT);
        SYMPTOM_MAP.put("nose", Specialization.ENT);
        SYMPTOM_MAP.put("sinus", Specialization.ENT);

        // Ophthalmology
        SYMPTOM_MAP.put("eye", Specialization.OPHTHALMOLOGY);
        SYMPTOM_MAP.put("vision", Specialization.OPHTHALMOLOGY);

        // Psychiatry
        SYMPTOM_MAP.put("anxiety", Specialization.PSYCHIATRY);
        SYMPTOM_MAP.put("depression", Specialization.PSYCHIATRY);
        SYMPTOM_MAP.put("insomnia", Specialization.PSYCHIATRY);

        log.info("[Static Block] AIHelper symptom map loaded with {} entries.", SYMPTOM_MAP.size());
    }

    /**
     * Recommend doctors based on patient symptoms using streams & lambdas.
     */
    public static List<Doctor> recommendDoctors(String symptoms, List<Doctor> allDoctors) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String lowerSymptoms = symptoms.toLowerCase();

        // Find matching specializations using streams
        Set<Specialization> matchedSpecs = SYMPTOM_MAP.entrySet().stream()
                .filter(entry -> lowerSymptoms.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());

        if (matchedSpecs.isEmpty()) {
            // Default to General Medicine
            matchedSpecs.add(Specialization.GENERAL);
        }

        // Filter doctors by matched specializations, sorted by experience
        return allDoctors.stream()
                .filter(doc -> matchedSpecs.contains(doc.getSpecialization()))
                .sorted(Comparator.comparingInt(Doctor::getYearsOfExperience).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Suggest available time slots.
     */
    public static List<String> suggestTimeSlots() {
        return List.of(
                "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM",
                "11:00 AM", "11:30 AM", "02:00 PM", "02:30 PM",
                "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM"
        );
    }
}

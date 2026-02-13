package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.repository.BillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for billing operations.
 * Demonstrates: Strategy pattern usage, Factory pattern usage,
 * JPA repository, streams & lambdas, dynamic dispatch.
 */
@Service
@Transactional
public class BillingService {

    private final BillRepository billRepository;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final Map<String, BillingStrategy> strategyMap;

    public BillingService(BillRepository billRepository,
                          AppointmentService appointmentService,
                          DoctorService doctorService,
                          PatientService patientService,
                          List<BillingStrategy> strategies) {
        this.billRepository = billRepository;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;

        // Build strategy map from injected strategies — demonstrates dynamic dispatch
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(BillingStrategy::getStrategyName, s -> s));
    }

    /**
     * Generate a bill for an appointment using the Factory and Strategy patterns.
     */
    public Bill generateBill(String appointmentId, String billType) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        Doctor doctor = doctorService.getDoctorById(appointment.getDoctorId())
                .orElseThrow(() -> new InvalidDataException("doctorId", "Doctor not found"));
        Patient patient = patientService.getPatientById(appointment.getPatientId())
                .orElseThrow(() -> new InvalidDataException("patientId", "Patient not found"));

        // Factory pattern — create bill based on type
        Bill bill = BillFactory.createBill(billType, appointment, doctor, patient);

        // Strategy pattern — apply billing calculation strategy
        BillingStrategy strategy = strategyMap.getOrDefault(
                billType.toUpperCase(), strategyMap.get("STANDARD"));
        if (strategy != null) {
            strategy.calculate(bill);
        }

        return billRepository.save(bill);
    }

    @Transactional(readOnly = true)
    public Optional<Bill> getBillById(String id) {
        return billRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Bill> getBillsByPatient(String patientId) {
        return billRepository.findByPatientId(patientId);
    }

    /**
     * Generate immutable BillSummary from a Bill.
     * Demonstrates: immutable class usage, polymorphism (generateBill).
     */
    @Transactional(readOnly = true)
    public BillSummary getBillSummary(String billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new InvalidDataException("billId", "Bill not found: " + billId));
        return bill.generateBill();
    }

    // --- Analytics ---

    @Transactional(readOnly = true)
    public double getTotalRevenue() {
        return billRepository.findAll().stream()
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }

    @Transactional(readOnly = true)
    public Map<String, Double> getRevenueByBillType() {
        return billRepository.findAll().stream()
                .collect(Collectors.groupingBy(Bill::getBillType,
                        Collectors.summingDouble(Bill::getTotalAmount)));
    }

    public BillRepository getRepository() {
        return billRepository;
    }
}

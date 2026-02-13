package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.service.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Billing operations.
 * Demonstrates: Factory + Strategy pattern usage via BillingService.
 */
@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillingService billingService;

    public BillController(BillingService billingService) {
        this.billingService = billingService;
    }

    /**
     * Generate a bill for an appointment.
     * Request body: { "appointmentId": "...", "billType": "STANDARD|INSURANCE|EMERGENCY" }
     */
    @PostMapping
    public ResponseEntity<Bill> generateBill(@RequestBody Map<String, String> request) {
        String appointmentId = request.get("appointmentId");
        String billType = request.getOrDefault("billType", "STANDARD");
        return new ResponseEntity<>(billingService.generateBill(appointmentId, billType), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable String id) {
        return billingService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<BillSummary> getBillSummary(@PathVariable String id) {
        return ResponseEntity.ok(billingService.getBillSummary(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Bill>> getBillsByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(billingService.getBillsByPatient(patientId));
    }

    @GetMapping("/analytics/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueAnalytics() {
        return ResponseEntity.ok(Map.of(
                "totalRevenue", billingService.getTotalRevenue(),
                "revenueByType", billingService.getRevenueByBillType()
        ));
    }
}

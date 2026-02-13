package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe ID generator using Singleton pattern.
 * Demonstrates: Singleton (both eager and lazy), AtomicInteger for concurrency,
 * static initialization, synchronized access.
 */
public class IdGenerator {

    // --- Eager Singleton instance ---
    private static final IdGenerator INSTANCE = new IdGenerator();

    private final AtomicInteger doctorCounter;
    private final AtomicInteger patientCounter;
    private final AtomicInteger appointmentCounter;
    private final AtomicInteger billCounter;

    // Static block — demonstrates static initialization
    static {
        System.out.println("[Static Block] IdGenerator singleton initialized (eager).");
    }

    // Private constructor — prevents external instantiation
    private IdGenerator() {
        this.doctorCounter = new AtomicInteger(1000);
        this.patientCounter = new AtomicInteger(2000);
        this.appointmentCounter = new AtomicInteger(3000);
        this.billCounter = new AtomicInteger(4000);
    }

    // Eager singleton accessor
    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    // --- Lazy Singleton (inner class holder pattern — bonus demo) ---
    private static class LazyHolder {
        private static final IdGenerator LAZY_INSTANCE = new IdGenerator();
    }

    public static IdGenerator getLazyInstance() {
        return LazyHolder.LAZY_INSTANCE;
    }

    // --- ID generation methods (thread-safe via AtomicInteger) ---

    public String nextDoctorId() {
        return "DOC-" + doctorCounter.incrementAndGet();
    }

    public String nextPatientId() {
        return "PAT-" + patientCounter.incrementAndGet();
    }

    public String nextAppointmentId() {
        return "APT-" + appointmentCounter.incrementAndGet();
    }

    public String nextBillId() {
        return "BILL-" + billCounter.incrementAndGet();
    }
}

package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.interfaces.Payable;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manual test runner for MediTrack.
 * Demonstrates and verifies all core OOP concepts, design patterns, and Java features.
 * Run via: java com.airtribe.meditrack.test.TestRunner
 */
public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  MediTrack Manual Test Runner");
        System.out.println("========================================\n");

        testStaticBlocksAndConstants();
        testEnums();
        testEncapsulationAndValidation();
        testInheritanceAndConstructorChaining();
        testPolymorphismOverloading();
        testPolymorphismOverridingAndDynamicDispatch();
        testAbstractionAndInterfaces();
        testDeepVsShallowCopy();
        testImmutableClass();
        testGenericsAndDataStore();
        testComparatorAndSorting();
        testCustomExceptions();
        testSingletonPattern();
        testFactoryPattern();
        testStrategyPatternConcept();
        testStreamsAndLambdas();
        testEqualsAndHashCode();
        testDateUtil();
        testIdGenerator();

        System.out.println("\n========================================");
        System.out.println("  RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("========================================");
    }

    // --- Helper ---
    private static void assertTest(String name, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + name);
            passed++;
        } else {
            System.out.println("  [FAIL] " + name);
            failed++;
        }
    }

    // ==================== TESTS ====================

    private static void testStaticBlocksAndConstants() {
        System.out.println("\n--- Static Blocks & Constants ---");
        assertTest("Constants.APP_NAME is MediTrack", "MediTrack".equals(com.airtribe.meditrack.constants.Constants.APP_NAME));
        assertTest("Constants.TAX_RATE is 0.18", com.airtribe.meditrack.constants.Constants.TAX_RATE == 0.18);
        assertTest("MedicalEntity totalEntitiesCreated >= 0", MedicalEntity.getTotalEntitiesCreated() >= 0);
    }

    private static void testEnums() {
        System.out.println("\n--- Enums ---");
        assertTest("Specialization.CARDIOLOGY displayName", "Cardiology".equals(Specialization.CARDIOLOGY.getDisplayName()));
        assertTest("AppointmentStatus.CONFIRMED displayName", "Confirmed".equals(AppointmentStatus.CONFIRMED.getDisplayName()));
        assertTest("Enum valueOf works", Specialization.valueOf("NEUROLOGY") == Specialization.NEUROLOGY);
        assertTest("Enum values count", AppointmentStatus.values().length == 5);
    }

    private static void testEncapsulationAndValidation() {
        System.out.println("\n--- Encapsulation & Validation ---");
        Doctor d = new Doctor();
        d.setName("Dr. Smith");
        d.setAge(45);
        d.setConsultationFee(200.0);
        assertTest("Doctor name via getter", "Dr. Smith".equals(d.getName()));
        assertTest("Doctor age via getter", d.getAge() == 45);

        // Validation
        try {
            Validator.validateNotEmpty("", "testField");
            assertTest("Validator rejects empty string", false);
        } catch (InvalidDataException e) {
            assertTest("Validator rejects empty string", true);
        }

        try {
            Validator.validateAge(200);
            assertTest("Validator rejects invalid age", false);
        } catch (InvalidDataException e) {
            assertTest("Validator rejects invalid age", true);
        }
    }

    private static void testInheritanceAndConstructorChaining() {
        System.out.println("\n--- Inheritance & Constructor Chaining ---");
        Doctor doc = new Doctor("DOC-1", "Dr. Jones", 50, "Male", "1234567890", "jones@med.com",
                Specialization.CARDIOLOGY, 300.0, 20);

        assertTest("Doctor is instance of Person", doc instanceof Person);
        assertTest("Doctor is instance of MedicalEntity", doc instanceof MedicalEntity);
        assertTest("Constructor chaining sets ID", "DOC-1".equals(doc.getId()));
        assertTest("Constructor chaining sets name via Person", "Dr. Jones".equals(doc.getName()));
        assertTest("Constructor chaining sets specialization", Specialization.CARDIOLOGY == doc.getSpecialization());
        assertTest("getEntityType returns Doctor", "Doctor".equals(doc.getEntityType()));

        Patient pat = new Patient("PAT-1", "Alice", 30, "Female", "9876543210", "alice@test.com", "A+");
        assertTest("Patient is instance of Person", pat instanceof Person);
        assertTest("Patient constructor chaining works", "PAT-1".equals(pat.getId()));
    }

    private static void testPolymorphismOverloading() {
        System.out.println("\n--- Polymorphism: Method Overloading ---");
        Doctor doc = new Doctor("DOC-1", "Dr. Smith", 40, "Male", "1234567890", "smith@med.com",
                Specialization.NEUROLOGY, 250.0, 15);

        assertTest("matchesById works", doc.matchesById("DOC-1"));
        assertTest("matchesByName works", doc.matchesByName("Smith"));
        assertTest("matchesBySpecialization works", doc.matchesBySpecialization(Specialization.NEUROLOGY));

        Patient pat = new Patient("PAT-1", "Bob", 25, "Male", "9876543210", "bob@test.com", "B+");
        assertTest("Patient matchesById works", pat.matchesById("PAT-1"));
        assertTest("Patient matchesByName works", pat.matchesByName("Bob"));
        assertTest("Patient matchesByAge works", pat.matchesByAge(25));
        assertTest("Patient matchesByAge negative", !pat.matchesByAge(30));
    }

    private static void testPolymorphismOverridingAndDynamicDispatch() {
        System.out.println("\n--- Polymorphism: Overriding & Dynamic Dispatch ---");
        // Dynamic dispatch — calling getSummary via MedicalEntity reference
        MedicalEntity doc = new Doctor("DOC-1", "Dr. Heart", 45, "Female", "1234567890", "heart@med.com",
                Specialization.CARDIOLOGY, 500.0, 25);
        MedicalEntity pat = new Patient("PAT-1", "Charlie", 35, "Male", "9876543210", "charlie@test.com", "O+");

        assertTest("Dynamic dispatch: Doctor.getSummary", doc.getSummary().contains("Dr. Heart"));
        assertTest("Dynamic dispatch: Patient.getSummary", pat.getSummary().contains("Charlie"));
        assertTest("Dynamic dispatch: Doctor.getEntityType", "Doctor".equals(doc.getEntityType()));
        assertTest("Dynamic dispatch: Patient.getEntityType", "Patient".equals(pat.getEntityType()));
    }

    private static void testAbstractionAndInterfaces() {
        System.out.println("\n--- Abstraction & Interfaces ---");
        Doctor doc = new Doctor("DOC-1", "Dr. Neuro", 50, "Male", "1234567890", "neuro@med.com",
                Specialization.NEUROLOGY, 400.0, 20);

        // Searchable interface
        assertTest("Doctor implements Searchable", doc instanceof Searchable);
        assertTest("Searchable.matchesSearchCriteria", doc.matchesSearchCriteria("Neuro"));
        assertTest("Searchable default method containsIgnoreCase", doc.containsIgnoreCase("Hello", "hello"));
        assertTest("Searchable.getSearchableSummary", doc.getSearchableSummary().contains("Dr. Neuro"));

        // Payable interface (static method)
        double tax = Payable.calculateTax(100.0, 0.18);
        assertTest("Payable.calculateTax static method", Math.abs(tax - 18.0) < 0.001);

        // Bill implements Payable
        Bill bill = new Bill("BILL-1", "APT-1", "PAT-1", "Alice", "Dr. Neuro", 400.0, "STANDARD");
        assertTest("Bill implements Payable", bill instanceof Payable);
        assertTest("Bill.calculateTotal > 0", bill.calculateTotal() > 0);
        assertTest("Payable default getPaymentSummary", bill.getPaymentSummary().contains("Total:"));
    }

    private static void testDeepVsShallowCopy() {
        System.out.println("\n--- Deep vs Shallow Copy (Cloneable) ---");
        Patient original = new Patient("PAT-1", "Original", 30, "Female", "1234567890", "orig@test.com", "A+");
        original.setAllergies(List.of("Peanuts", "Dust"));
        original.setMedicalHistory(List.of("Asthma"));

        // Need mutable lists for clone test
        original.setAllergies(new java.util.ArrayList<>(List.of("Peanuts", "Dust")));
        original.setMedicalHistory(new java.util.ArrayList<>(List.of("Asthma")));

        Patient cloned = original.clone();

        assertTest("Clone is not same reference", original != cloned);
        assertTest("Clone has same name", original.getName().equals(cloned.getName()));
        assertTest("Clone allergies are NOT same list reference (deep copy)", original.getAllergies() != cloned.getAllergies());
        assertTest("Clone allergies have same content", original.getAllergies().equals(cloned.getAllergies()));

        // Modify clone — should NOT affect original
        cloned.getAllergies().add("Penicillin");
        assertTest("Modifying clone does NOT affect original (deep copy)", original.getAllergies().size() == 2);

        // Appointment clone
        Appointment apt = new Appointment("APT-1", "DOC-1", "PAT-1", "Dr. X", "Patient Y",
                LocalDateTime.now(), "Test notes");
        Appointment aptClone = apt.clone();
        assertTest("Appointment clone is not same reference", apt != aptClone);
        assertTest("Appointment clone has same ID", apt.getId().equals(aptClone.getId()));
    }

    private static void testImmutableClass() {
        System.out.println("\n--- Immutable Class (BillSummary) ---");
        BillSummary summary = new BillSummary("BILL-1", "Alice", "Dr. Smith",
                500.0, 90.0, 0.0, 590.0, LocalDateTime.now());

        assertTest("BillSummary billId", "BILL-1".equals(summary.getBillId()));
        assertTest("BillSummary patientName", "Alice".equals(summary.getPatientName()));
        assertTest("BillSummary totalAmount", summary.getTotalAmount() == 590.0);
        assertTest("BillSummary is final class", java.lang.reflect.Modifier.isFinal(BillSummary.class.getModifiers()));
        // No setter methods — verify via reflection
        long setterCount = java.util.Arrays.stream(BillSummary.class.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("set"))
                .count();
        assertTest("BillSummary has no setter methods", setterCount == 0);
    }

    private static void testGenericsAndDataStore() {
        System.out.println("\n--- Generics & DataStore<T> ---");
        DataStore<Doctor> store = new DataStore<>();
        Doctor d1 = new Doctor("DOC-A", "Dr. Alpha", 40, "Male", "1111111111", "alpha@med.com",
                Specialization.CARDIOLOGY, 300.0, 10);
        Doctor d2 = new Doctor("DOC-B", "Dr. Beta", 35, "Female", "2222222222", "beta@med.com",
                Specialization.DERMATOLOGY, 200.0, 5);

        store.add(d1);
        store.add(d2);

        assertTest("DataStore size", store.size() == 2);
        assertTest("DataStore getById", store.getById("DOC-A").isPresent());
        assertTest("DataStore getAll", store.getAll().size() == 2);
        assertTest("DataStore filter with lambda", store.filter(d -> d.getConsultationFee() > 250).size() == 1);
        assertTest("DataStore findFirst", store.findFirst(d -> d.getName().contains("Beta")).isPresent());
        assertTest("DataStore exists", store.exists("DOC-A"));
        assertTest("DataStore remove", store.remove("DOC-A") && store.size() == 1);
    }

    private static void testComparatorAndSorting() {
        System.out.println("\n--- Comparator & Sorting ---");
        DataStore<Doctor> store = new DataStore<>();
        store.add(new Doctor("D1", "Dr. Zeta", 50, "M", "1111111111", "z@m.com", Specialization.CARDIOLOGY, 500.0, 25));
        store.add(new Doctor("D2", "Dr. Alpha", 30, "F", "2222222222", "a@m.com", Specialization.NEUROLOGY, 200.0, 5));
        store.add(new Doctor("D3", "Dr. Mid", 40, "M", "3333333333", "m@m.com", Specialization.ORTHOPEDICS, 350.0, 15));

        List<Doctor> sortedByFee = store.getAllSorted(Comparator.comparingDouble(Doctor::getConsultationFee));
        assertTest("Sorted by fee ascending", sortedByFee.get(0).getConsultationFee() == 200.0);
        assertTest("Sorted by fee last", sortedByFee.get(2).getConsultationFee() == 500.0);

        List<Doctor> sortedByName = store.getAllSorted(Comparator.comparing(Doctor::getName));
        assertTest("Sorted by name first is Alpha", sortedByName.get(0).getName().contains("Alpha"));
    }

    private static void testCustomExceptions() {
        System.out.println("\n--- Custom Exceptions & Chaining ---");
        try {
            throw new InvalidDataException("email", "Invalid email format");
        } catch (InvalidDataException e) {
            assertTest("InvalidDataException message", e.getMessage().contains("Invalid email"));
            assertTest("InvalidDataException field", "email".equals(e.getFieldName()));
        }

        try {
            throw new AppointmentNotFoundException("APT-999");
        } catch (AppointmentNotFoundException e) {
            assertTest("AppointmentNotFoundException message", e.getMessage().contains("APT-999"));
            assertTest("AppointmentNotFoundException id", "APT-999".equals(e.getAppointmentId()));
        }

        // Exception chaining
        try {
            try {
                throw new NumberFormatException("bad number");
            } catch (NumberFormatException nfe) {
                throw new InvalidDataException("age", "Failed to parse age", nfe);
            }
        } catch (InvalidDataException e) {
            assertTest("Exception chaining — cause preserved", e.getCause() instanceof NumberFormatException);
        }
    }

    private static void testSingletonPattern() {
        System.out.println("\n--- Singleton Pattern (IdGenerator) ---");
        IdGenerator inst1 = IdGenerator.getInstance();
        IdGenerator inst2 = IdGenerator.getInstance();
        assertTest("Singleton: same instance", inst1 == inst2);

        String id1 = inst1.nextDoctorId();
        String id2 = inst1.nextDoctorId();
        assertTest("IdGenerator produces unique IDs", !id1.equals(id2));
        assertTest("IdGenerator ID prefix", id1.startsWith("DOC-"));

        // Lazy singleton
        IdGenerator lazy = IdGenerator.getLazyInstance();
        assertTest("Lazy singleton not null", lazy != null);
    }

    private static void testFactoryPattern() {
        System.out.println("\n--- Factory Pattern (BillFactory) ---");
        Doctor doc = new Doctor("DOC-1", "Dr. Factory", 40, "M", "1234567890", "f@m.com",
                Specialization.GENERAL, 300.0, 10);
        Patient pat = new Patient("PAT-1", "Factory Pat", 25, "F", "9876543210", "fp@t.com", "O+");
        Appointment apt = new Appointment("APT-1", "DOC-1", "PAT-1", "Dr. Factory", "Factory Pat",
                LocalDateTime.now(), "Test");

        Bill standard = BillFactory.createStandardBill(apt, doc, pat);
        assertTest("Factory standard bill type", "STANDARD".equals(standard.getBillType()));
        assertTest("Factory standard bill fee", standard.getConsultationFee() == 300.0);

        Bill insurance = BillFactory.createInsuranceBill(apt, doc, pat);
        assertTest("Factory insurance bill type", "INSURANCE".equals(insurance.getBillType()));

        Bill emergency = BillFactory.createEmergencyBill(apt, doc, pat);
        assertTest("Factory emergency bill surcharge", emergency.getConsultationFee() == 450.0);

        // Generic factory
        Bill generic = BillFactory.createBill("EMERGENCY", apt, doc, pat);
        assertTest("Factory generic method", "EMERGENCY".equals(generic.getBillType()));
    }

    private static void testStrategyPatternConcept() {
        System.out.println("\n--- Strategy Pattern (Billing) ---");
        Bill bill = new Bill("B1", "A1", "P1", "Pat", "Doc", 1000.0, "STANDARD");

        // Standard strategy
        var standard = new com.airtribe.meditrack.service.StandardBillingStrategy();
        double stdTotal = standard.calculate(bill);
        assertTest("Standard strategy total = fee + 18% tax", Math.abs(stdTotal - 1180.0) < 0.01);

        // Insurance strategy
        bill = new Bill("B2", "A1", "P1", "Pat", "Doc", 1000.0, "INSURANCE");
        var insurance = new com.airtribe.meditrack.service.InsuranceBillingStrategy();
        double insTotal = insurance.calculate(bill);
        // fee=1000, discount=150, afterDiscount=850, tax=153, total=1003
        assertTest("Insurance strategy applies discount", bill.getDiscount() == 150.0);
        assertTest("Insurance strategy total < standard", insTotal < 1180.0);
    }

    private static void testStreamsAndLambdas() {
        System.out.println("\n--- Java 8+ Streams & Lambdas ---");
        DataStore<Doctor> store = new DataStore<>();
        store.add(new Doctor("D1", "Dr. A", 40, "M", "1111111111", "a@m.com", Specialization.CARDIOLOGY, 500.0, 20));
        store.add(new Doctor("D2", "Dr. B", 35, "F", "2222222222", "b@m.com", Specialization.CARDIOLOGY, 300.0, 10));
        store.add(new Doctor("D3", "Dr. C", 45, "M", "3333333333", "c@m.com", Specialization.NEUROLOGY, 400.0, 15));

        // Filter by specialization
        List<Doctor> cardiologists = store.getAll().stream()
                .filter(d -> d.getSpecialization() == Specialization.CARDIOLOGY)
                .collect(Collectors.toList());
        assertTest("Stream filter by specialization", cardiologists.size() == 2);

        // Average fee
        double avgFee = store.getAll().stream()
                .mapToDouble(Doctor::getConsultationFee)
                .average().orElse(0.0);
        assertTest("Stream average fee", Math.abs(avgFee - 400.0) < 0.01);

        // Group by specialization
        Map<Specialization, List<Doctor>> grouped = store.getAll().stream()
                .collect(Collectors.groupingBy(Doctor::getSpecialization));
        assertTest("Stream groupingBy", grouped.size() == 2);
        assertTest("Stream groupingBy cardiology count", grouped.get(Specialization.CARDIOLOGY).size() == 2);

        // Map to names
        List<String> names = store.getAll().stream()
                .map(Doctor::getName)
                .sorted()
                .collect(Collectors.toList());
        assertTest("Stream map + sort", "Dr. A".equals(names.get(0)));
    }

    private static void testEqualsAndHashCode() {
        System.out.println("\n--- equals / hashCode ---");
        Doctor d1 = new Doctor("DOC-1", "Dr. One", 40, "M", "1111111111", "one@m.com",
                Specialization.GENERAL, 200.0, 5);
        Doctor d2 = new Doctor("DOC-1", "Dr. Two", 50, "F", "2222222222", "two@m.com",
                Specialization.NEUROLOGY, 300.0, 10);
        Doctor d3 = new Doctor("DOC-2", "Dr. One", 40, "M", "1111111111", "one@m.com",
                Specialization.GENERAL, 200.0, 5);

        assertTest("Same ID => equals true", d1.equals(d2));
        assertTest("Different ID => equals false", !d1.equals(d3));
        assertTest("Same ID => same hashCode", d1.hashCode() == d2.hashCode());
    }

    private static void testDateUtil() {
        System.out.println("\n--- DateUtil ---");
        LocalDateTime now = LocalDateTime.of(2025, 3, 15, 10, 30);
        String display = DateUtil.formatForDisplay(now);
        assertTest("DateUtil formatForDisplay", "15-03-2025 10:30".equals(display));

        String iso = DateUtil.formatForStorage(now);
        LocalDateTime parsed = DateUtil.parseFromStorage(iso);
        assertTest("DateUtil round-trip parse", now.equals(parsed));

        assertTest("DateUtil null handling", "N/A".equals(DateUtil.formatForDisplay(null)));
    }

    private static void testIdGenerator() {
        System.out.println("\n--- IdGenerator ---");
        IdGenerator gen = IdGenerator.getInstance();
        String docId = gen.nextDoctorId();
        String patId = gen.nextPatientId();
        String aptId = gen.nextAppointmentId();
        String billId = gen.nextBillId();

        assertTest("Doctor ID prefix", docId.startsWith("DOC-"));
        assertTest("Patient ID prefix", patId.startsWith("PAT-"));
        assertTest("Appointment ID prefix", aptId.startsWith("APT-"));
        assertTest("Bill ID prefix", billId.startsWith("BILL-"));
    }
}

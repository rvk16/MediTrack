# MediTrack

A full-featured **Medical Appointment Management System** built with **Spring Boot 4**, **Spring Data JPA**, and **H2 Database**. MediTrack demonstrates core Java and Spring concepts through a real-world healthcare domain — managing doctors, patients, appointments, and billing.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 4.0.2 |
| **Persistence** | Spring Data JPA + Hibernate ORM |
| **Database** | H2 (in-memory) |
| **Language** | Java 21 |
| **Build** | Maven |
| **Code Generation** | Lombok |
| **Frontend** | Single-page HTML dashboard (embedded) |

---

## Java & Spring Concepts Demonstrated

### OOP Principles
- **Abstraction** — `MedicalEntity` and `Person` abstract base classes
- **Encapsulation** — Private fields with Lombok getters/setters, `BillSummary` immutable record-style class
- **Inheritance** — `Doctor extends Person extends MedicalEntity`, `Bill extends MedicalEntity implements Payable`
- **Polymorphism** — Overloaded `searchDoctors(String)` / `searchDoctors(Specialization)`, interface-based dispatch

### Design Patterns
- **Singleton** — `IdGenerator` with thread-safe lazy initialization
- **Factory** — `BillFactory` creates Standard / Insurance / Emergency bills
- **Strategy** — `BillingStrategy` interface with `StandardBillingStrategy` and `InsuranceBillingStrategy`
- **Observer** — `AppointmentObserver` notified on create, cancel, and status change events

### Java Features
- **Enums with fields** — `Specialization` (10 medical specializations), `AppointmentStatus` (5 states)
- **Generics** — `DataStore<T extends MedicalEntity>` type-safe in-memory collection
- **Streams & Lambdas** — Analytics (grouping, counting, averaging, sorting)
- **Collections** — `List`, `Map`, `Optional` throughout service layer
- **Custom Exceptions** — `AppointmentNotFoundException`, `InvalidDataException` with exception chaining
- **Interfaces** — `Searchable`, `Payable`, `Cloneable` (deep copy on `Patient`)
- **Static blocks** — Entity counter in `MedicalEntity`
- **File I/O** — CSV save/load with try-with-resources (`CSVUtil`)
- **Immutable class** — `BillSummary` with final fields, no setters

### Spring Boot Features
- **Spring Data JPA** — `JpaRepository` interfaces with derived queries and `@Query` JPQL
- **Transaction management** — `@Transactional` / `@Transactional(readOnly = true)`
- **Dependency Injection** — Constructor injection across all services and controllers
- **REST controllers** — Full CRUD APIs with `@RestController`, `@RequestBody`, `@PathVariable`, `@RequestParam`
- **H2 Console** — In-browser SQL access at `/h2-console`

---

## Project Structure

```
src/main/java/com/airtribe/meditrack/
├── MediTrackApplication.java          # Spring Boot entry point
├── constants/
│   └── Constants.java                 # App-wide constants
├── controller/
│   ├── DoctorController.java          # Doctor REST API
│   ├── PatientController.java         # Patient REST API
│   ├── AppointmentController.java     # Appointment REST API
│   ├── BillController.java            # Billing REST API
│   ├── AIController.java              # AI recommendations API
│   └── DataController.java            # CSV I/O & system stats
├── entity/
│   ├── MedicalEntity.java             # @MappedSuperclass — base (id, timestamps)
│   ├── Person.java                    # @MappedSuperclass — name, age, contact
│   ├── Doctor.java                    # @Entity — specialization, fee, slots
│   ├── Patient.java                   # @Entity — blood group, allergies, history
│   ├── Appointment.java               # @Entity — doctor/patient link, status
│   ├── Bill.java                      # @Entity — fees, tax, discount, total
│   ├── BillSummary.java               # Immutable bill summary
│   ├── Specialization.java            # Enum with display names
│   └── AppointmentStatus.java         # Enum (PENDING → COMPLETED)
├── repository/
│   ├── DoctorRepository.java          # JpaRepository + custom JPQL queries
│   ├── PatientRepository.java         # JpaRepository + keyword search
│   ├── AppointmentRepository.java     # JpaRepository + date/status filters
│   └── BillRepository.java            # JpaRepository + patient/type queries
├── service/
│   ├── DoctorService.java             # Doctor business logic
│   ├── PatientService.java            # Patient business logic + clone
│   ├── AppointmentService.java        # Appointment logic + observer pattern
│   ├── BillingService.java            # Billing + factory + strategy patterns
│   ├── BillingStrategy.java           # Strategy interface
│   ├── StandardBillingStrategy.java   # Standard billing calculation
│   └── InsuranceBillingStrategy.java  # Insurance billing calculation
├── factory/
│   └── BillFactory.java               # Factory pattern for bill creation
├── observer/
│   ├── AppointmentObserver.java       # Observer interface
│   └── ...                            # Observer implementations
├── interfaces/
│   ├── Searchable.java                # Search contract
│   └── Payable.java                   # Payment contract
├── exception/
│   ├── AppointmentNotFoundException.java
│   └── InvalidDataException.java
├── util/
│   ├── DataStore.java                 # Generic in-memory store <T>
│   ├── IdGenerator.java               # Singleton ID generator
│   ├── Validator.java                 # Input validation utilities
│   ├── CSVUtil.java                   # CSV file I/O
│   ├── DateUtil.java                  # Date formatting helpers
│   └── AIHelper.java                  # Rule-based AI recommendations
└── test/
    └── TestRunner.java                # Standalone OOP concept tests
```

---

## Getting Started

### Prerequisites
- **Java 21+**
- **Maven 3.9+** (or use the included Maven wrapper)

### Run the Application

```bash
# Clone the repository
git clone https://github.com/rvk16/MediTrack.git
cd MediTrack

# Build and run
./mvnw spring-boot:run
```

The application starts on **http://localhost:8080**.

### Access Points

| URL | Description |
|-----|------------|
| http://localhost:8080 | Web dashboard (HTML UI) |
| http://localhost:8080/h2-console | H2 database console |
| http://localhost:8080/api/doctors | Doctors REST API |
| http://localhost:8080/api/patients | Patients REST API |
| http://localhost:8080/api/appointments | Appointments REST API |
| http://localhost:8080/api/bills | Billing REST API |
| http://localhost:8080/api/ai/recommend | AI doctor recommendations |
| http://localhost:8080/api/data/stats | System statistics |

### H2 Console Login
- **JDBC URL:** `jdbc:h2:mem:meditrackdb`
- **Username:** `sa`
- **Password:** *(empty)*

---

## API Reference

### Doctors `/api/doctors`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/doctors` | Create a doctor |
| `GET` | `/api/doctors` | List all doctors |
| `GET` | `/api/doctors/{id}` | Get doctor by ID |
| `PUT` | `/api/doctors/{id}` | Update doctor |
| `DELETE` | `/api/doctors/{id}` | Delete doctor |
| `GET` | `/api/doctors/search?keyword=` | Search by keyword |
| `GET` | `/api/doctors/specialization/{spec}` | Filter by specialization |
| `GET` | `/api/doctors/sorted/fee` | Sorted by consultation fee |
| `GET` | `/api/doctors/analytics/average-fee` | Average consultation fee |
| `GET` | `/api/doctors/analytics/by-specialization` | Count per specialization |

<details>
<summary>Example: Create Doctor</summary>

```json
POST /api/doctors
{
  "name": "Dr. Arun Sharma",
  "age": 45,
  "gender": "Male",
  "phone": "9876543210",
  "email": "arun.sharma@meditrack.com",
  "specialization": "CARDIOLOGY",
  "consultationFee": 500.00,
  "yearsOfExperience": 20,
  "availableSlots": ["09:00-10:00", "14:00-15:00"]
}
```
</details>

### Patients `/api/patients`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/patients` | Create a patient |
| `GET` | `/api/patients` | List all patients |
| `GET` | `/api/patients/{id}` | Get patient by ID |
| `PUT` | `/api/patients/{id}` | Update patient |
| `DELETE` | `/api/patients/{id}` | Delete patient |
| `GET` | `/api/patients/search?keyword=` | Search by keyword |
| `GET` | `/api/patients/search/name?name=` | Search by name |
| `GET` | `/api/patients/search/age?age=` | Search by age |
| `POST` | `/api/patients/{id}/clone` | Clone patient (deep copy demo) |

<details>
<summary>Example: Create Patient</summary>

```json
POST /api/patients
{
  "name": "Rahul Verma",
  "age": 30,
  "gender": "Male",
  "phone": "9123456780",
  "email": "rahul.verma@email.com",
  "bloodGroup": "O+",
  "allergies": ["Penicillin", "Dust"],
  "medicalHistory": ["Asthma", "Appendectomy 2020"]
}
```
</details>

### Appointments `/api/appointments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/appointments` | Create appointment |
| `GET` | `/api/appointments` | List all appointments |
| `GET` | `/api/appointments/{id}` | Get by ID |
| `PUT` | `/api/appointments/{id}/cancel` | Cancel appointment |
| `PUT` | `/api/appointments/{id}/status?status=` | Update status |
| `GET` | `/api/appointments/doctor/{doctorId}` | By doctor |
| `GET` | `/api/appointments/patient/{patientId}` | By patient |
| `GET` | `/api/appointments/upcoming` | Upcoming appointments |
| `GET` | `/api/appointments/analytics/per-doctor` | Count per doctor |
| `GET` | `/api/appointments/analytics/per-status` | Count per status |

<details>
<summary>Example: Create Appointment</summary>

```json
POST /api/appointments
{
  "doctorId": "DOC-001",
  "patientId": "PAT-001",
  "dateTime": "2026-03-15T10:00:00",
  "notes": "Routine cardiac checkup"
}
```
</details>

### Bills `/api/bills`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/bills` | Generate bill (Standard/Insurance/Emergency) |
| `GET` | `/api/bills` | List all bills |
| `GET` | `/api/bills/{id}` | Get bill by ID |
| `GET` | `/api/bills/{id}/summary` | Immutable bill summary |
| `GET` | `/api/bills/patient/{patientId}` | Bills by patient |
| `GET` | `/api/bills/analytics/revenue` | Total revenue & by type |

<details>
<summary>Example: Generate Bill</summary>

```json
POST /api/bills
{
  "appointmentId": "APT-001",
  "billType": "STANDARD"
}
```
Bill types: `STANDARD`, `INSURANCE`, `EMERGENCY`
</details>

### AI & Data

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/ai/recommend?symptoms=` | AI doctor recommendations |
| `GET` | `/api/ai/slots` | Suggest available time slots |
| `POST` | `/api/data/save` | Export all data to CSV |
| `POST` | `/api/data/load` | Import data from CSV |
| `GET` | `/api/data/stats` | System statistics |

---

## Postman Collection

A ready-to-use Postman collection is included at [`postman/MediTrack_API_Collection.json`](postman/MediTrack_API_Collection.json).

**Import:** Open Postman → Import → select the JSON file.

The collection includes:
- All 38 API endpoints organized in 6 folders
- Sample request bodies with realistic data
- Auto-populated collection variables (`doctorId`, `patientId`, `appointmentId`, `billId`)
- Test scripts for response validation

**Recommended order:** Add Doctor → Add Patient → Create Appointment → Generate Bill

---

## Specializations

| Enum | Display Name | Description |
|------|-------------|-------------|
| `CARDIOLOGY` | Cardiology | Heart and cardiovascular system |
| `DERMATOLOGY` | Dermatology | Skin, hair, and nails |
| `NEUROLOGY` | Neurology | Brain and nervous system |
| `ORTHOPEDICS` | Orthopedics | Bones and joints |
| `PEDIATRICS` | Pediatrics | Children's health |
| `GENERAL` | General Medicine | General health and wellness |
| `ENT` | ENT | Ear, Nose, and Throat |
| `OPHTHALMOLOGY` | Ophthalmology | Eye care |
| `PSYCHIATRY` | Psychiatry | Mental health |
| `GYNECOLOGY` | Gynecology | Women's health |

---

## License

This project is for educational purposes.

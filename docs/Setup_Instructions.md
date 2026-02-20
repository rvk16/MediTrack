# MediTrack — Setup Instructions

## Prerequisites

| Tool        | Version   | Download Link                          |
|-------------|-----------|----------------------------------------|
| **JDK**     | 21+       | https://adoptium.net/ or Oracle JDK    |
| **Maven**   | 3.9+      | https://maven.apache.org/download.cgi  |
| **Git**     | 2.40+     | https://git-scm.com/downloads          |
| **IDE**     | IntelliJ / VS Code | https://www.jetbrains.com/idea/ |

---

## 1. Install Java JDK 21

### macOS (Homebrew)
```bash
brew install openjdk@21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### Windows
1. Download JDK 21 from [Adoptium](https://adoptium.net/).
2. Run the installer.
3. Add `JAVA_HOME` to System Environment Variables pointing to your JDK installation folder.
4. Add `%JAVA_HOME%\bin` to your `PATH`.

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-21-jdk
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
```

### Verify Installation
```bash
java -version
javac -version
```
Expected output:
```
openjdk version "21.x.x"
```

---

## 2. Install Maven

### macOS
```bash
brew install maven
```

### Windows / Linux
Download from https://maven.apache.org/download.cgi and follow the installation guide.

### Verify
```bash
mvn -version
```

---

## 3. Clone the Repository

```bash
git clone <repository-url>
cd MediTrack
```

---

## 4. Build the Project

```bash
# Using Maven wrapper (recommended)
./mvnw clean compile

# Or with system Maven
mvn clean compile
```

---

## 5. Run the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or with system Maven
mvn spring-boot:run
```

The application starts at **http://localhost:8080**.

---

## 6. Run the Manual Test Runner

```bash
# Compile and run
./mvnw compile exec:java -Dexec.mainClass="com.airtribe.meditrack.test.TestRunner"
```

Or run via your IDE by right-clicking `TestRunner.java` → Run.

---

## 7. Load Persisted Data (Optional)

To load previously saved CSV data on startup, use the REST endpoint:
```bash
curl -X POST http://localhost:8080/api/data/load
```

Or click **"Load Data from CSV"** in the web UI under the **Data I/O** tab.

---

## 8. Access the Web UI

Open your browser and navigate to:
```
http://localhost:8080
```

The web UI provides:
- **Doctors** — Add, search, delete doctors
- **Patients** — Add, search, clone (deep copy demo), delete patients
- **Appointments** — Create, view, cancel appointments
- **Billing** — Generate bills with Standard/Insurance/Emergency strategies
- **AI Recommend** — Rule-based doctor recommendation by symptoms
- **Data I/O** — Save/load data to/from CSV files

---

## Project Structure

```
src/main/java/com/airtribe/meditrack/
├── MediTrackApplication.java          # Spring Boot entry point
├── constants/Constants.java           # App-wide constants (static blocks)
├── entity/
│   ├── MedicalEntity.java             # Abstract base class
│   ├── Person.java                    # Abstract (extends MedicalEntity)
│   ├── Doctor.java                    # Extends Person, implements Searchable
│   ├── Patient.java                   # Extends Person, Searchable, Cloneable
│   ├── Appointment.java               # Extends MedicalEntity, Cloneable
│   ├── Bill.java                      # Implements Payable
│   ├── BillSummary.java               # Immutable class (final)
│   ├── Specialization.java            # Enum
│   └── AppointmentStatus.java         # Enum
├── service/
│   ├── DoctorService.java
│   ├── PatientService.java
│   ├── AppointmentService.java
│   ├── BillingService.java
│   ├── BillingStrategy.java           # Strategy pattern interface
│   ├── StandardBillingStrategy.java   # Strategy impl
│   └── InsuranceBillingStrategy.java  # Strategy impl
├── controller/                        # REST API controllers
├── util/
│   ├── Validator.java                 # Centralized validation
│   ├── DateUtil.java
│   ├── CSVUtil.java                   # CSV File I/O
│   ├── IdGenerator.java               # Singleton pattern
│   ├── DataStore.java                 # Generic DataStore<T>
│   └── AIHelper.java                  # Rule-based AI
├── exception/                         # Custom exceptions
├── interfaces/                        # Searchable, Payable
├── observer/                          # Observer pattern
├── factory/                           # Factory pattern
└── test/TestRunner.java               # Manual test runner
```

# JVM Report — MediTrack

## 1. JVM Architecture Overview

The **Java Virtual Machine (JVM)** is the runtime engine that executes Java bytecode. It is the core component that enables Java's platform independence.

```
┌──────────────────────────────────────────────────────────┐
│                    Java Source Code (.java)               │
│                           │                               │
│                     javac (Compiler)                      │
│                           │                               │
│                    Bytecode (.class)                      │
│                           │                               │
│  ┌────────────────────────▼─────────────────────────┐    │
│  │                      JVM                          │    │
│  │  ┌─────────────┐  ┌──────────────┐  ┌────────┐  │    │
│  │  │ Class Loader │→│Runtime Data  │→│Execution│  │    │
│  │  │  Subsystem   │  │   Areas      │  │ Engine  │  │    │
│  │  └─────────────┘  └──────────────┘  └────────┘  │    │
│  └──────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────┘
```

---

## 2. Class Loader Subsystem

The Class Loader is responsible for **loading**, **linking**, and **initializing** classes.

### 2.1 Loading
Three built-in class loaders work in a hierarchical delegation model:

| Class Loader        | Responsibility                                      |
|---------------------|-----------------------------------------------------|
| **Bootstrap**       | Loads core Java classes (`java.lang`, `java.util`)  |
| **Extension/Platform** | Loads classes from `jdk.ext` or platform modules |
| **Application**     | Loads classes from the application classpath         |

**Delegation Model:** When a class is requested, the Application loader delegates to its parent (Extension), which delegates to Bootstrap. Only if the parent cannot find the class does the child attempt to load it.

### 2.2 Linking
- **Verification** — Ensures bytecode is valid and doesn't violate security.
- **Preparation** — Allocates memory for static variables with default values.
- **Resolution** — Resolves symbolic references to direct references.

### 2.3 Initialization
- Executes **static initializers** and **static blocks**.
- In MediTrack, `Constants.java` and `MedicalEntity.java` use static blocks:
  ```java
  static {
      APP_NAME = "MediTrack";
      System.out.println("[Static Block] Constants class loaded");
  }
  ```

---

## 3. Runtime Data Areas

The JVM allocates memory in several distinct areas during execution:

### 3.1 Heap
- **Shared** across all threads.
- Stores **all object instances** and arrays.
- Managed by the **Garbage Collector (GC)**.
- Divided into:
  - **Young Generation** (Eden + Survivor spaces) — new objects.
  - **Old Generation** — long-lived objects promoted from Young Gen.
- In MediTrack: Every `Doctor`, `Patient`, `Appointment`, `DataStore<T>` instance lives here.

### 3.2 Stack (JVM Stack)
- **Per-thread** — each thread has its own stack.
- Stores **stack frames**, one per method invocation.
- Each frame contains:
  - **Local Variables Array** — method parameters and local variables.
  - **Operand Stack** — intermediate computation values.
  - **Frame Data** — reference to the constant pool, exception handlers.
- In MediTrack: When `DoctorService.addDoctor()` is called, a new stack frame is pushed.

### 3.3 Method Area (Metaspace in modern JVMs)
- **Shared** across all threads.
- Stores **class-level data**:
  - Class structure (fields, methods, interfaces).
  - Runtime constant pool.
  - Static variables.
  - Method bytecode.
- In MediTrack: The `Specialization` enum constants, `Constants.TAX_RATE`, and class definitions are stored here.

### 3.4 PC (Program Counter) Register
- **Per-thread**.
- Holds the address of the **currently executing JVM instruction**.
- If executing a native method, the PC is undefined.

### 3.5 Native Method Stack
- **Per-thread**.
- Supports execution of **native (non-Java) methods** via JNI.

---

## 4. Execution Engine

The Execution Engine reads bytecode and executes it. It has three main components:

### 4.1 Interpreter
- Reads bytecode **one instruction at a time** and executes it.
- **Fast startup** but slower runtime for frequently executed code.

### 4.2 JIT (Just-In-Time) Compiler
- Identifies **hot spots** — code paths executed frequently.
- Compiles hot bytecode into **native machine code** for faster execution.
- The compiled code is cached for subsequent calls.
- Uses techniques like:
  - **Method inlining**
  - **Loop unrolling**
  - **Dead code elimination**
  - **Escape analysis**

### 4.3 JIT Compiler vs Interpreter — Comparison

| Aspect              | Interpreter                    | JIT Compiler                        |
|---------------------|--------------------------------|-------------------------------------|
| **Speed**           | Slower at runtime              | Faster after compilation            |
| **Startup**         | Fast startup                   | Slower startup (compilation cost)   |
| **Memory**          | Low memory usage               | Higher (stores compiled code)       |
| **When Used**       | Infrequently executed code     | Hot paths (frequently called code)  |
| **Optimization**    | None                           | Aggressive optimizations            |

**Modern JVMs use both:** The interpreter starts executing code immediately, while the JIT compiler runs in the background, compiling hot methods to native code. This is called **tiered compilation**.

### 4.4 Garbage Collector (GC)
- Automatically reclaims memory from objects that are no longer reachable.
- Major GC algorithms in modern JVMs:
  - **G1 GC** (default in JDK 21) — region-based, low-latency.
  - **ZGC** — ultra-low-latency, concurrent.
  - **Shenandoah** — concurrent compaction.

---

## 5. JDK vs JRE vs JVM

```
┌─────────────────────────────────────────────────┐
│  JDK (Java Development Kit)                     │
│  ┌───────────────────────────────────────────┐  │
│  │  JRE (Java Runtime Environment)           │  │
│  │  ┌─────────────────────────────────────┐  │  │
│  │  │  JVM (Java Virtual Machine)         │  │  │
│  │  │  • Class Loader                     │  │  │
│  │  │  • Runtime Data Areas               │  │  │
│  │  │  • Execution Engine                 │  │  │
│  │  └─────────────────────────────────────┘  │  │
│  │  + Core Libraries (java.lang, java.util)  │  │
│  └───────────────────────────────────────────┘  │
│  + Development Tools (javac, javadoc, jdb)      │
└─────────────────────────────────────────────────┘
```

| Component | Contains                                    | Purpose                  |
|-----------|---------------------------------------------|--------------------------|
| **JVM**   | Class Loader, Memory, Execution Engine       | Runs bytecode            |
| **JRE**   | JVM + Core Libraries                         | Run Java applications    |
| **JDK**   | JRE + Dev Tools (javac, javadoc, jar, jdb)   | Develop + Run Java apps  |

> **Note:** Since JDK 11, Oracle no longer ships a standalone JRE. The JDK includes everything needed.

---

## 6. "Write Once, Run Anywhere" (WORA)

Java achieves platform independence through its **compilation + interpretation** model:

1. **Source Code** (`.java`) is written once by the developer.
2. **`javac`** compiles it to **bytecode** (`.class`) — platform-independent intermediate code.
3. The **JVM** on the target platform interprets/JIT-compiles the bytecode to **native machine code**.

```
Developer's Machine          Any Target Machine
┌──────────────┐            ┌──────────────────┐
│  Hello.java  │            │  JVM (Windows)   │
│      │       │            │      │           │
│   javac      │            │  Interprets/JIT  │
│      │       │            │      │           │
│ Hello.class  │ ────────→  │  Native x86 code │
└──────────────┘            └──────────────────┘
                            ┌──────────────────┐
                            │  JVM (macOS ARM) │
                ────────→   │  Native ARM code │
                            └──────────────────┘
                            ┌──────────────────┐
                            │  JVM (Linux)     │
                ────────→   │  Native x64 code │
                            └──────────────────┘
```

**Key insight:** The bytecode is the same everywhere. Only the JVM implementation differs per platform. This is why Java is described as **"Write Once, Run Anywhere"**.

### WORA in MediTrack
- We compile MediTrack on any OS: `./mvnw clean compile`
- The resulting `.class` files can run on **any machine with a JDK 21+ JVM**.
- Spring Boot packages everything into a single `.jar`:
  ```bash
  ./mvnw package
  java -jar target/MediTrack-0.0.1-SNAPSHOT.jar
  ```
  This JAR runs identically on Windows, macOS, and Linux.

---

## 7. MediTrack — JVM Concepts in Action

| JVM Concept             | Where in MediTrack                                        |
|-------------------------|-----------------------------------------------------------|
| **Class Loading**       | `MedicalEntity`, `Constants` — static blocks execute on load |
| **Heap**                | All entity objects (`Doctor`, `Patient`, `DataStore`)     |
| **Stack**               | Method calls in services (`addDoctor`, `createAppointment`) |
| **Method Area**         | Enum constants (`Specialization`, `AppointmentStatus`), static fields |
| **JIT Compilation**     | Hot REST endpoints get JIT-compiled for better throughput |
| **GC**                  | Temporary objects (e.g., stream intermediates) collected  |
| **Bytecode**            | All `.java` → `.class` via `javac`, run by JVM           |
| **WORA**                | Same JAR runs on any platform with JDK 21                |

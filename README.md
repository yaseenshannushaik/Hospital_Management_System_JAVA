# 🏥 Hospital Management System (Java Swing + MySQL)

A desktop-based Hospital Management System built with **Java Swing** for the GUI and **MySQL** as the database.
This application allows management of patients, doctors, and appointments with full CRUD (Create, Read, Update, Delete) functionality.

## ✨ Features

### **Patients**

* View patient list
* Add new patients with validation for age and required fields
* Update patient details (name, age, gender)
* Delete patients (removes all their appointments)

### **Doctors**

* View doctor list
* Add new doctors with specialization
* Update doctor details
* Delete doctors (removes all their appointments)

### **Appointments**

* Book appointments by selecting patient, doctor, and date
* Prevent booking for past dates
* Ensure doctors are available for the chosen date
* Cancel existing appointments

---

## 📂 Project Structure

* **`HospitalManagementSystemGUI.java`** – Main Swing-based GUI application
* **`Doctor.java`** – Backend operations for managing doctor records
* **`Outputs.pdf`** – Screenshots and visual outputs of the application

---

## 🛠️ Requirements

* **Java 8+**
* **MySQL Database**
* MySQL JDBC Driver (`mysql-connector-java`)
* IDE (Eclipse / IntelliJ / NetBeans) or command line

---

## 📦 Database Setup

1. Create a MySQL database:

   ```sql
   CREATE DATABASE hospital;
   USE hospital;
   ```

2. Create tables:

   ```sql
   CREATE TABLE patients (
       id INT PRIMARY KEY AUTO_INCREMENT,
       name VARCHAR(100),
       age INT,
       gender VARCHAR(10)
   );

   CREATE TABLE doctors (
       id INT PRIMARY KEY AUTO_INCREMENT,
       name VARCHAR(100),
       specialization VARCHAR(100)
   );

   CREATE TABLE appointments (
       id INT PRIMARY KEY AUTO_INCREMENT,
       patient_id INT,
       doctor_id INT,
       appointment_date DATE,
       FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
       FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
   );
   ```

---

## 🚀 How to Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/HospitalManagementSystem.git
   cd HospitalManagementSystem
   ```

2. **Configure Database Connection**
   In `HospitalManagementSystemGUI.java`, update:

   ```java
   static final String URL = "jdbc:mysql://localhost:3306/hospital";
   static final String USER = "root";
   static final String PASS = "your_password";
   ```

3. **Add MySQL Connector**

   * If using an IDE, add `mysql-connector-java.jar` to your project libraries.
   * If using command line, include it in the classpath.

4. **Compile & Run**

   ```bash
   javac HospitalManagementSystemGUI.java
   java HospitalManagementSystemGUI
   ```

---

## 📸 Outputs

Refer to `Outputs.pdf` for screenshots of:

* Viewing patients/doctors
* Adding, updating, and deleting records
* Booking and cancelling appointments

---


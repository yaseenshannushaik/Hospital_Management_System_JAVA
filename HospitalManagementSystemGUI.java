import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
  /// //////////////

public class HospitalManagementSystemGUI extends JFrame {
    static final String URL = "jdbc:mysql://localhost:3306/hospital";
    static final String USER = "root";
    static final String PASS = "ammu2914";
/// ///////
    DefaultTableModel patientTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Age", "Gender"}, 0);
    DefaultTableModel doctorTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Specialization"}, 0);
    DefaultTableModel appointmentTableModel = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "Date"}, 0);

    // Maps to store ID-name pairs for patients and doctors
    Map<Integer, String> patientMap = new HashMap<>();
    Map<Integer, String> doctorMap = new HashMap<>();
/// ///////
    // Patient fields
    JTextField patientName = new JTextField(10);
    JTextField patientAge = new JTextField(5);
    JComboBox<String> patientGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
    JTextField updatePatientId = new JTextField(5);
    JTextField updatePatientName = new JTextField(10);
    JTextField updatePatientAge = new JTextField(5);
    JComboBox<String> updatePatientGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
/// ////////
    // Doctor fields
    JTextField doctorName = new JTextField(10);
    JTextField doctorSpec = new JTextField(10);
    JTextField updateDoctorId = new JTextField(5);
    JTextField updateDoctorName = new JTextField(10);
    JTextField updateDoctorSpec = new JTextField(10);

    // Appointment fields - ComboBoxes will show "ID - Name"
    JComboBox<String> patientCombo = new JComboBox<>();
    JComboBox<String> doctorCombo = new JComboBox<>();
    JTextField appointmentDate = new JTextField(10);
    JTextField cancelAppointmentId = new JTextField(5);

    // Deletion fields
    JTextField deletePatientId = new JTextField(5);
    JTextField deleteDoctorId = new JTextField(5);

    public HospitalManagementSystemGUI() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        JTabbedPane tabs = new JTabbedPane();

        // Patients Tab
        JPanel patientPanel = new JPanel(new BorderLayout());
        JTable patientTable = new JTable(patientTableModel);
        patientTable.setFont(fieldFont);
        patientTable.setRowHeight(22);
        patientPanel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        // Add Patient Panel
        JPanel addPatientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel nameLbl = new JLabel("Name:");
        JLabel ageLbl = new JLabel("Age:");
        JLabel genderLbl = new JLabel("Gender:");

        nameLbl.setFont(labelFont);
        ageLbl.setFont(labelFont);
        genderLbl.setFont(labelFont);

        patientName.setFont(fieldFont);
        patientAge.setFont(fieldFont);
        patientGender.setFont(fieldFont);

        JButton addPatientBtn = new JButton("Add Patient");
        addPatientBtn.setFont(buttonFont);

        addPatientPanel.add(nameLbl);
        addPatientPanel.add(patientName);
        addPatientPanel.add(ageLbl);
        addPatientPanel.add(patientAge);
        addPatientPanel.add(genderLbl);
        addPatientPanel.add(patientGender);
        addPatientPanel.add(addPatientBtn);

        // Update Patient Panel
        JPanel updatePatientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel updatePatientIdLbl = new JLabel("Patient ID:");
        JLabel updateNameLbl = new JLabel("New Name:");
        JLabel updateAgeLbl = new JLabel("New Age:");
        JLabel updateGenderLbl = new JLabel("New Gender:");

        updatePatientIdLbl.setFont(labelFont);
        updateNameLbl.setFont(labelFont);
        updateAgeLbl.setFont(labelFont);
        updateGenderLbl.setFont(labelFont);

        updatePatientId.setFont(fieldFont);
        updatePatientName.setFont(fieldFont);
        updatePatientAge.setFont(fieldFont);
        updatePatientGender.setFont(fieldFont);

        JButton updatePatientBtn = new JButton("Update Patient");
        updatePatientBtn.setFont(buttonFont);

        updatePatientPanel.add(updatePatientIdLbl);
        updatePatientPanel.add(updatePatientId);
        updatePatientPanel.add(updateNameLbl);
        updatePatientPanel.add(updatePatientName);
        updatePatientPanel.add(updateAgeLbl);
        updatePatientPanel.add(updatePatientAge);
        updatePatientPanel.add(updateGenderLbl);
        updatePatientPanel.add(updatePatientGender);
        updatePatientPanel.add(updatePatientBtn);

        // Delete Patient Panel
        JPanel deletePatientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel deletePatientLbl = new JLabel("Delete Patient ID:");
        deletePatientLbl.setFont(labelFont);
        deletePatientId.setFont(fieldFont);
        JButton deletePatientBtn = new JButton("Delete Patient");
        deletePatientBtn.setFont(buttonFont);

        deletePatientPanel.add(deletePatientLbl);
        deletePatientPanel.add(deletePatientId);
        deletePatientPanel.add(deletePatientBtn);

        // Combine all patient panels
        JPanel patientButtonPanel = new JPanel(new GridLayout(3, 1));
        patientButtonPanel.add(addPatientPanel);
        patientButtonPanel.add(updatePatientPanel);
        patientButtonPanel.add(deletePatientPanel);

        patientPanel.add(patientButtonPanel, BorderLayout.SOUTH);

        // Add patient button listeners
        addPatientBtn.addActionListener(e -> addPatient());
        updatePatientBtn.addActionListener(e -> updatePatient());
        deletePatientBtn.addActionListener(e -> deletePatient());
        tabs.add("Patients  ", patientPanel);

        // Doctors Tab
        JPanel doctorPanel = new JPanel(new BorderLayout());
        JTable doctorTable = new JTable(doctorTableModel);
        doctorTable.setFont(fieldFont);
        doctorTable.setRowHeight(22);
        doctorPanel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);

        // Add Doctor Panel
        JPanel addDoctorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel docNameLbl = new JLabel("Name:");
        JLabel specLbl = new JLabel("Specialization:");

        docNameLbl.setFont(labelFont);
        specLbl.setFont(labelFont);
        doctorName.setFont(fieldFont);
        doctorSpec.setFont(fieldFont);

        JButton addDoctorBtn = new JButton("Add Doctor");
        addDoctorBtn.setFont(buttonFont);

        addDoctorPanel.add(docNameLbl);
        addDoctorPanel.add(doctorName);
        addDoctorPanel.add(specLbl);
        addDoctorPanel.add(doctorSpec);
        addDoctorPanel.add(addDoctorBtn);

        // Update Doctor Panel
        JPanel updateDoctorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel updateDoctorIdLbl = new JLabel("Doctor ID:");
        JLabel updateDocNameLbl = new JLabel("New Name:");
        JLabel updateSpecLbl = new JLabel("New Specialization:");

        updateDoctorIdLbl.setFont(labelFont);
        updateDocNameLbl.setFont(labelFont);
        updateSpecLbl.setFont(labelFont);

        updateDoctorId.setFont(fieldFont);
        updateDoctorName.setFont(fieldFont);
        updateDoctorSpec.setFont(fieldFont);

        JButton updateDoctorBtn = new JButton("Update Doctor");
        updateDoctorBtn.setFont(buttonFont);

        updateDoctorPanel.add(updateDoctorIdLbl);
        updateDoctorPanel.add(updateDoctorId);
        updateDoctorPanel.add(updateDocNameLbl);
        updateDoctorPanel.add(updateDoctorName);
        updateDoctorPanel.add(updateSpecLbl);
        updateDoctorPanel.add(updateDoctorSpec);
        updateDoctorPanel.add(updateDoctorBtn);

        // Delete Doctor Panel
        JPanel deleteDoctorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel deleteDoctorLbl = new JLabel("Delete Doctor ID:");
        deleteDoctorLbl.setFont(labelFont);
        deleteDoctorId.setFont(fieldFont);
        JButton deleteDoctorBtn = new JButton("Delete Doctor");
        deleteDoctorBtn.setFont(buttonFont);

        deleteDoctorPanel.add(deleteDoctorLbl);
        deleteDoctorPanel.add(deleteDoctorId);
        deleteDoctorPanel.add(deleteDoctorBtn);

        // Combine all doctor panels
        JPanel doctorButtonPanel = new JPanel(new GridLayout(3, 1));
        doctorButtonPanel.add(addDoctorPanel);
        doctorButtonPanel.add(updateDoctorPanel);
        doctorButtonPanel.add(deleteDoctorPanel);

        doctorPanel.add(doctorButtonPanel, BorderLayout.SOUTH);

        // Add doctor button listeners
        addDoctorBtn.addActionListener(e -> addDoctor());
        updateDoctorBtn.addActionListener(e -> updateDoctor());
        deleteDoctorBtn.addActionListener(e -> deleteDoctor());
        tabs.add("Doctors  ", doctorPanel);




        // Appointments Tab
        JPanel appointmentPanel = new JPanel(new BorderLayout());
        JTable appointmentTable = new JTable(appointmentTableModel);
        appointmentTable.setFont(fieldFont);
        appointmentTable.setRowHeight(22);
        appointmentPanel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        // Book Appointment Panel
        JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel patLbl = new JLabel("Patient:");
        JLabel docLbl = new JLabel("Doctor:");
        JLabel dateLbl = new JLabel("Date (YYYY-MM-DD):");

        patLbl.setFont(labelFont);
        docLbl.setFont(labelFont);
        dateLbl.setFont(labelFont);

        patientCombo.setFont(fieldFont);
        doctorCombo.setFont(fieldFont);
        appointmentDate.setFont(fieldFont);

        JButton bookBtn = new JButton("Book");
        bookBtn.setFont(buttonFont);

        bookPanel.add(patLbl);
        bookPanel.add(patientCombo);
        bookPanel.add(docLbl);
        bookPanel.add(doctorCombo);
        bookPanel.add(dateLbl);
        bookPanel.add(appointmentDate);
        bookPanel.add(bookBtn);
        appointmentPanel.add(bookPanel, BorderLayout.NORTH);

        // Cancel Appointment Panel
        JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel cancelLbl = new JLabel("Appointment ID to Cancel:");
        cancelLbl.setFont(labelFont);
        cancelAppointmentId.setFont(fieldFont);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(buttonFont);

        cancelPanel.add(cancelLbl);
        cancelPanel.add(cancelAppointmentId);
        cancelPanel.add(cancelBtn);
        appointmentPanel.add(cancelPanel, BorderLayout.SOUTH);

        // Add appointment button listeners
        bookBtn.addActionListener(this::bookAppointment);
        cancelBtn.addActionListener(this::cancelAppointment);
        tabs.add("Appointments   ", appointmentPanel);

        add(tabs, BorderLayout.CENTER);

        // Exit Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(exitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initial data load
        refreshPatients();
        refreshDoctors();
        refreshAppointments();
        populateCombos();
    }

    // Method to add a patient
    void addPatient() {
        String name = patientName.getText().trim();
        String ageText = patientAge.getText().trim();
        String gender = patientGender.getSelectedItem().toString();

        if (name.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0) {
                JOptionPane.showMessageDialog(this, "Age must be a positive number.");
                return;
            }
            if (age > 120) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (1-120).");
                return;
            }

            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                String sql = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, gender);
                ps.executeUpdate();

                // Get the generated ID
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    patientMap.put(id, name);
                }

                JOptionPane.showMessageDialog(this, "Patient added successfully!");
                patientName.setText("");
                patientAge.setText("");
                refreshPatients();
                populateCombos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding patient: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Method to update a patient
    void updatePatient() {
        String idStr = updatePatientId.getText().trim();
        String name = updatePatientName.getText().trim();
        String ageText = updatePatientAge.getText().trim();
        String gender = updatePatientGender.getSelectedItem().toString();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID is required.");
            return;
        }

        if (name.isEmpty() && ageText.isEmpty() && gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one field to update is required.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            int age = ageText.isEmpty() ? -1 : Integer.parseInt(ageText);

            if (!ageText.isEmpty() && age <= 0) {
                JOptionPane.showMessageDialog(this, "Age must be a positive number.");
                return;
            }
            if (!ageText.isEmpty() && (age > 120)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (1-120).");
                return;
            }

            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                // Check if patient exists
                String checkSql = "SELECT * FROM patients WHERE id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Patient not found with ID: " + id);
                    return;
                }

                // Build the update query based on which fields are provided
                StringBuilder sqlBuilder = new StringBuilder("UPDATE patients SET ");
                boolean needsComma = false;

                if (!name.isEmpty()) {
                    sqlBuilder.append("name = ?");
                    needsComma = true;
                }

                if (!ageText.isEmpty()) {
                    if (needsComma) sqlBuilder.append(", ");
                    sqlBuilder.append("age = ?");
                    needsComma = true;
                }

                if (gender != null && !gender.isEmpty()) {
                    if (needsComma) sqlBuilder.append(", ");
                    sqlBuilder.append("gender = ?");
                }

                sqlBuilder.append(" WHERE id = ?");

                PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString());
                int paramIndex = 1;

                if (!name.isEmpty()) {
                    ps.setString(paramIndex++, name);
                }

                if (!ageText.isEmpty()) {
                    ps.setInt(paramIndex++, age);
                }

                if (gender != null && !gender.isEmpty()) {
                    ps.setString(paramIndex++, gender);
                }

                ps.setInt(paramIndex, id);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Patient updated successfully!");
                    updatePatientId.setText("");
                    updatePatientName.setText("");
                    updatePatientAge.setText("");
                    refreshPatients();
                    populateCombos();
                } else {
                    JOptionPane.showMessageDialog(this, "No changes made to patient.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID and age.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating patient: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Method to add a doctor
    void addDoctor() {
        String name = doctorName.getText().trim();
        String spec = doctorSpec.getText().trim();

        if (name.isEmpty() || spec.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "INSERT INTO doctors(name, specialization) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, spec);
            ps.executeUpdate();

            // Get the generated ID
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                doctorMap.put(id, name);
            }

            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            doctorName.setText("");
            doctorSpec.setText("");
            refreshDoctors();
            populateCombos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding doctor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Method to update a doctor
    void updateDoctor() {
        String idStr = updateDoctorId.getText().trim();
        String name = updateDoctorName.getText().trim();
        String spec = updateDoctorSpec.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Doctor ID is required.");
            return;
        }

        if (name.isEmpty() && spec.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one field to update is required.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                // Check if doctor exists
                String checkSql = "SELECT * FROM doctors WHERE id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Doctor not found with ID: " + id);
                    return;
                }

                // Build the update query based on which fields are provided
                StringBuilder sqlBuilder = new StringBuilder("UPDATE doctors SET ");
                boolean needsComma = false;

                if (!name.isEmpty()) {
                    sqlBuilder.append("name = ?");
                    needsComma = true;
                }

                if (!spec.isEmpty()) {
                    if (needsComma) sqlBuilder.append(", ");
                    sqlBuilder.append("specialization = ?");
                }

                sqlBuilder.append(" WHERE id = ?");

                PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString());
                int paramIndex = 1;

                if (!name.isEmpty()) {
                    ps.setString(paramIndex++, name);
                }

                if (!spec.isEmpty()) {
                    ps.setString(paramIndex++, spec);
                }

                ps.setInt(paramIndex, id);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
                    updateDoctorId.setText("");
                    updateDoctorName.setText("");
                    updateDoctorSpec.setText("");
                    refreshDoctors();
                    populateCombos();
                } else {
                    JOptionPane.showMessageDialog(this, "No changes made to doctor.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid doctor ID.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating doctor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Method to delete a patient
    void deletePatient() {
        String idStr = deletePatientId.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter patient ID to delete.");
            return;
        }

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "This will delete the patient and all their appointments. Continue?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            int id = Integer.parseInt(idStr);

            // Check if patient exists
            String check = "SELECT * FROM patients WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Patient not found.");
                return;
            }

            // First delete all appointments for this patient
            String deleteAppointments = "DELETE FROM appointments WHERE patient_id = ?";
            try (PreparedStatement delApptStmt = conn.prepareStatement(deleteAppointments)) {
                delApptStmt.setInt(1, id);
                int appointmentsDeleted = delApptStmt.executeUpdate();
                System.out.println("Deleted " + appointmentsDeleted + " appointments");
            }

            // Then delete the patient
            String delete = "DELETE FROM patients WHERE id = ?";
            try (PreparedStatement delStmt = conn.prepareStatement(delete)) {
                delStmt.setInt(1, id);
                int rowsAffected = delStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Patient and associated appointments deleted.");
                    deletePatientId.setText("");
                    patientMap.remove(id);
                    refreshPatients();
                    refreshAppointments();
                    populateCombos();
                } else {
                    JOptionPane.showMessageDialog(this, "Patient not found.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid patient ID format.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting patient: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    // Method to delete a doctor
    void deleteDoctor() {
        String idStr = deleteDoctorId.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter doctor ID to delete.");
            return;
        }

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "This will delete the doctor and all their appointments. Continue?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            int id = Integer.parseInt(idStr);

            // Check if doctor exists
            String check = "SELECT * FROM doctors WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(check)) {
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Doctor not found.");
                    return;
                }
            }

            // First delete all appointments for this doctor
            String deleteAppointments = "DELETE FROM appointments WHERE doctor_id = ?";
            try (PreparedStatement delApptStmt = conn.prepareStatement(deleteAppointments)) {
                delApptStmt.setInt(1, id);
                int appointmentsDeleted = delApptStmt.executeUpdate();
                System.out.println("Deleted " + appointmentsDeleted + " appointments");
            }

            // Then delete the doctor
            String delete = "DELETE FROM doctors WHERE id = ?";
            try (PreparedStatement delStmt = conn.prepareStatement(delete)) {
                delStmt.setInt(1, id);
                int rowsAffected = delStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Doctor and associated appointments deleted.");
                    deleteDoctorId.setText("");
                    doctorMap.remove(id);
                    refreshDoctors();
                    refreshAppointments();
                    populateCombos();
                } else {
                    JOptionPane.showMessageDialog(this, "Doctor not found.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid doctor ID format.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting doctor: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    void refreshPatients() {
        patientTableModel.setRowCount(0);
        patientMap.clear();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM patients ORDER BY id");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                patientTableModel.addRow(new Object[]{
                        id,
                        name,
                        rs.getInt("age"),
                        rs.getString("gender")
                });
                patientMap.put(id, name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void refreshDoctors() {
        doctorTableModel.setRowCount(0);
        doctorMap.clear();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM doctors ORDER BY id");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                doctorTableModel.addRow(new Object[]{
                        id,
                        name,
                        rs.getString("specialization")
                });
                doctorMap.put(id, name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void refreshAppointments() {
        appointmentTableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT a.id, p.name AS patient, d.name AS doctor, a.appointment_date " +
                    "FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.id " +
                    "JOIN doctors d ON a.doctor_id = d.id " +
                    "ORDER BY a.appointment_date";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                appointmentTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("patient"),
                        rs.getString("doctor"),
                        rs.getString("appointment_date")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void populateCombos() {
        patientCombo.removeAllItems();
        doctorCombo.removeAllItems();

        // Add patients in format "ID - Name"
        for (Map.Entry<Integer, String> entry : patientMap.entrySet()) {
            patientCombo.addItem(entry.getKey() + " - " + entry.getValue());
        }

        // Add doctors in format "ID - Name"
        for (Map.Entry<Integer, String> entry : doctorMap.entrySet()) {
            doctorCombo.addItem(entry.getKey() + " - " + entry.getValue());
        }
    }

    void bookAppointment(ActionEvent e) {
        if (patientCombo.getSelectedItem() == null || doctorCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select both a patient and a doctor.");
            return;
        }

        // Extract ID from the selected combo item (format is "ID - Name")
        String patientSelection = (String) patientCombo.getSelectedItem();
        String doctorSelection = (String) doctorCombo.getSelectedItem();

        int patientId = Integer.parseInt(patientSelection.split(" - ")[0]);
        int doctorId = Integer.parseInt(doctorSelection.split(" - ")[0]);

        String dateStr = appointmentDate.getText().trim();

        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            LocalDate date = LocalDate.parse(dateStr);

            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Date cannot be in the past.");
                return;
            }

            // Check if doctor is available on this date
            String check = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, doctorId);
            checkStmt.setString(2, dateStr);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Doctor is not available on this date.");
                return;
            }

            // Book the appointment
            String insert = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, dateStr);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            appointmentDate.setText("");
            refreshAppointments();
        } catch (DateTimeParseException dt) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error booking appointment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void cancelAppointment(ActionEvent e) {
        String idStr = cancelAppointmentId.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an appointment ID to cancel.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            int id = Integer.parseInt(idStr);

            // Check if appointment exists
            String check = "SELECT * FROM appointments WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Appointment not found.");
                return;
            }

            // Cancel the appointment
            String delete = "DELETE FROM appointments WHERE id = ?";
            PreparedStatement delStmt = conn.prepareStatement(delete);
            delStmt.setInt(1, id);
            int rowsAffected = delStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.");
                cancelAppointmentId.setText("");
                refreshAppointments();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel appointment.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid appointment ID number.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cancelling appointment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to load MySQL JDBC driver. Make sure it's in your classpath.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        SwingUtilities.invokeLater(() -> {
            HospitalManagementSystemGUI gui = new HospitalManagementSystemGUI();
            gui.setVisible(true);
        });
    }
}
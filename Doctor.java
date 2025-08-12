package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Doctor {
    private Connection connection;
    private Scanner scanner;

    public Doctor(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public boolean addDoctor(String name, String specialization) {
        try {
            String query = "INSERT INTO doctors(name, specialization) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, specialization);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding doctor: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, String>> getAllDoctors() {
        List<Map<String, String>> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, String> doctor = new HashMap<>();
                doctor.put("id", resultSet.getString("id"));
                doctor.put("name", resultSet.getString("name"));
                doctor.put("specialization", resultSet.getString("specialization"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching doctors: " + e.getMessage());
        }
        return doctors;
    }

    public boolean doctorExists(int id) {
        String query = "SELECT * FROM doctors WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking doctor: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, String>> getAppointmentsByDoctor(int doctorId) {
        List<Map<String, String>> appointments = new ArrayList<>();
        String query = "SELECT a.id, p.name AS patient_name, a.appointment_date " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "WHERE a.doctor_id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> appointment = new HashMap<>();
                appointment.put("appointment_id", rs.getString("id"));
                appointment.put("patient_name", rs.getString("patient_name"));
                appointment.put("appointment_date", rs.getString("appointment_date"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching appointments: " + e.getMessage());
        }
        return appointments;
    }

    // Console-specific methods (for backward compatibility)
    public void viewDoctorsConsole() {
        List<Map<String, String>> doctors = getAllDoctors();
        System.out.println("Doctors: ");
        System.out.println("+------------+------------------------------+--------------------+");
        System.out.println("| Doctor Id  | Name                         | Specialization     |");
        System.out.println("+------------+------------------------------+--------------------+");

        for (Map<String, String> doctor : doctors) {
            System.out.printf("|%-12s|%-30s|%-20s|\n",
                    doctor.get("id"),
                    doctor.get("name"),
                    doctor.get("specialization"));
            System.out.println("+------------+------------------------------+--------------------+");
        }
    }

    public void viewAppointmentsByDoctorConsole(int doctorId) {
        List<Map<String, String>> appointments = getAppointmentsByDoctor(doctorId);

        System.out.println("Appointments for Doctor ID: " + doctorId);
        System.out.println("+--------------+----------------------+------------------+");
        System.out.println("| Appointment  | Patient Name         | Appointment Date |");
        System.out.println("+--------------+----------------------+------------------+");

        if (appointments.isEmpty()) {
            System.out.println("| No appointments found for this doctor.                         |");
        } else {
            for (Map<String, String> app : appointments) {
                System.out.printf("| %-12s | %-20s | %-16s |\n",
                        app.get("appointment_id"),
                        app.get("patient_name"),
                        app.get("appointment_date"));
            }
        }
        System.out.println("+--------------+----------------------+------------------+");
    }
}
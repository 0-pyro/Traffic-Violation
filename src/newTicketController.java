import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class newTicketController {

    @FXML
    private TextField officerIdField;
    @FXML
    private TextField offenderIdField;

    @FXML
    private TextField licenseIdField;
    @FXML
    private TextField licenseTypeField;
    @FXML
    private TextField licenseAddressField;
    @FXML
    private TextField licenseAgeField;
    @FXML
    private TextField licensePhoneField;

    @FXML
    private TextField vehicleNumberField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField townField;
    @FXML
    private TextField roadField;
    @FXML
    private TextField penaltyField;
    @FXML
    private TextField violationField;
    @FXML
    private TextField feeField;

    @FXML
    private void handleCheckOffender() {
        String offStr = offenderIdField.getText().trim();
        if (offStr.isEmpty()) {
            showAlert("Offender ID required",
                    "Please enter an Offender ID to check or leave empty to create new offender.");
            return;
        }

        int enteredId;
        try {
            enteredId = Integer.parseInt(offStr);
        } catch (NumberFormatException e) {
            showAlert("Invalid ID", "Offender ID must be a number.");
            return;
        }

        boolean exists = Offender.check(enteredId);
        if (exists) {
            String q = "SELECT license_id FROM offender WHERE offender_id = ?";
            try (Connection c = DBC.conDB();
                    PreparedStatement ps = c.prepareStatement(q)) {
                ps.setInt(1, enteredId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int licenseId = rs.getInt("license_id");
                        if (rs.wasNull()) {
                            clearLicenseFields();
                            showAlert("No license linked", "Offender exists but has no license_id.");
                        } else {
                            String q2 = "SELECT license_id, age, phone, validity, type, address FROM driving_license WHERE license_id = ? LIMIT 1";
                            try (PreparedStatement ps2 = c.prepareStatement(q2)) {
                                ps2.setInt(1, licenseId);
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    if (rs2.next()) {
                                        licenseIdField.setText(String.valueOf(rs2.getInt("license_id")));
                                        licenseTypeField.setText(rs2.getString("type"));
                                        licenseAddressField.setText(rs2.getString("address"));
                                        licenseAgeField.setText(String.valueOf(rs2.getInt("age")));
                                        licensePhoneField.setText(String.valueOf(rs2.getLong("phone")));
                                        showInfo("Found", "Offender and license details loaded.");
                                    } else {
                                        clearLicenseFields();
                                        showAlert("No license found",
                                                "No driving_license record found for license_id: " + licenseId);
                                    }
                                }
                            }
                        }
                    } else {
                        clearLicenseFields();
                        showAlert("Not found", "Offender record not found after check.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("DB Error", "Failed to load license info: " + ex.getMessage());
            }
        } else {
            Optional<String> optName = prompt("Create Offender",
                    "Offender not found. Enter offender name to create new offender:");
            if (!optName.isPresent() || optName.get().trim().isEmpty()) {
                showAlert("Cancelled", "Offender creation cancelled.");
                return;
            }
            String offenderName = optName.get().trim();

            Optional<String> optLicenseId = prompt("License ID", "Enter new License ID (numeric):");
            if (!optLicenseId.isPresent()) {
                showAlert("Cancelled", "License creation cancelled.");
                return;
            }
            int licenseId;
            try {
                licenseId = Integer.parseInt(optLicenseId.get().trim());
            } catch (NumberFormatException ex) {
                showAlert("Invalid", "License ID must be numeric.");
                return;
            }

            Optional<String> optAge = prompt("License Age", "Enter age (numeric):");
            if (!optAge.isPresent()) {
                showAlert("Cancelled", "License creation cancelled.");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(optAge.get().trim());
            } catch (NumberFormatException ex) {
                showAlert("Invalid", "Age must be numeric.");
                return;
            }

            Optional<String> optPhone = prompt("License Phone", "Enter phone (numeric):");
            if (!optPhone.isPresent()) {
                showAlert("Cancelled", "License creation cancelled.");
                return;
            }
            long phone;
            try {
                phone = Long.parseLong(optPhone.get().trim());
            } catch (NumberFormatException ex) {
                showAlert("Invalid", "Phone must be numeric.");
                return;
            }

            Optional<String> optValidity = prompt("License Valid", "Is the license valid? (true/false):");
            if (!optValidity.isPresent()) {
                showAlert("Cancelled", "License creation cancelled.");
                return;
            }
            boolean validity = Boolean.parseBoolean(optValidity.get().trim());

            Optional<String> optAddress = prompt("License Address", "Enter address:");
            if (!optAddress.isPresent()) {
                showAlert("Cancelled", "License creation cancelled.");
                return;
            }
            String address = optAddress.get().trim();

            Optional<String> optType = prompt("License Type", "Enter license type:");
            if (!optType.isPresent()) {
                showAlert("Cancelled", "License creation cancelled.");
                return;
            }
            String type = optType.get().trim();

            try {
                new DrivingLicense(licenseId, 0, age, phone, validity, address, type);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to create license: " + ex.getMessage());
                return;
            }

            try {
                new Offender(0, offenderName, licenseId);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to create offender: " + ex.getMessage());
                return;
            }

            int newOffenderId = findOffenderIdByLicense(licenseId);
            if (newOffenderId == -1) {
                showAlert("Warning",
                        "Created license and offender but could not resolve new offender ID. Use dashboard to verify.");
            } else {
                offenderIdField.setText(String.valueOf(newOffenderId));
                licenseIdField.setText(String.valueOf(licenseId));
                licenseTypeField.setText(type);
                licenseAddressField.setText(address);
                licenseAgeField.setText(String.valueOf(age));
                licensePhoneField.setText(String.valueOf(phone));
                showInfo("Created", "New offender and license created with ID: " + newOffenderId);
            }
        }
    }

    private void clearLicenseFields() {
        licenseIdField.clear();
        licenseTypeField.clear();
        licenseAddressField.clear();
        licenseAgeField.clear();
        licensePhoneField.clear();
    }

    @FXML
    private void handleCreateTicket() {
        String officerStr = officerIdField.getText().trim();
        String offenderStr = offenderIdField.getText().trim();
        String vehicleNo = vehicleNumberField.getText().trim();
        String city = cityField.getText().trim();
        String town = townField.getText().trim();
        String road = roadField.getText().trim();
        String penalty = penaltyField.getText().trim();
        String violation = violationField.getText().trim();
        String feeStr = feeField.getText().trim();

        if (officerStr.isEmpty() || offenderStr.isEmpty() || vehicleNo.isEmpty() || feeStr.isEmpty()) {
            showAlert("Missing", "Officer ID, Offender ID, Vehicle No. and Amount are required.");
            return;
        }

        int officerId, offenderId, fee;
        try {
            officerId = Integer.parseInt(officerStr);
            offenderId = Integer.parseInt(offenderStr);
            fee = Integer.parseInt(feeStr);
        } catch (NumberFormatException e) {
            showAlert("Invalid", "IDs and Fee must be numeric.");
            return;
        }

        if (!Officer.check(officerId)) {
            showAlert("Invalid Officer", "Officer ID not found.");
            return;
        }

        if (!Offender.check(offenderId)) {
            Optional<String> optName = prompt("Create Offender",
                    "Offender ID not found. Enter name to create new offender (will ignore provided numeric ID):");
            if (!optName.isPresent() || optName.get().trim().isEmpty()) {
                showAlert("Cancelled", "Offender creation cancelled.");
                return;
            }
            String name = optName.get().trim();

            Optional<String> optLicenseId = prompt("License ID", "Enter license ID to link to offender (numeric):");
            if (!optLicenseId.isPresent()) {
                showAlert("Cancelled", "Offender creation cancelled.");
                return;
            }
            int licenseId;
            try {
                licenseId = Integer.parseInt(optLicenseId.get().trim());
            } catch (NumberFormatException ex) {
                showAlert("Invalid", "License ID must be numeric.");
                return;
            }

            new Offender(0, name, licenseId);
            int createdId = findOffenderIdByLicense(licenseId);
            if (createdId == -1) {
                showAlert("Error", "Could not create offender or resolve new ID.");
                return;
            }
            offenderId = createdId;
            offenderIdField.setText(String.valueOf(offenderId));
        }

        if (!Vehicle.check(vehicleNo)) {
            showInfo("Vehicle", "Vehicle not found. You will be prompted to enter vehicle details to create it.");
            Optional<String> optModel = prompt("Model", "Enter vehicle model:");
            Optional<String> optType = prompt("Type", "Enter vehicle type:");
            Optional<String> optFuel = prompt("Fuel", "Enter fuel type:");
            Optional<String> optColour = prompt("Colour", "Enter colour:");
            Optional<String> optInsured = prompt("Insured", "Is vehicle insured? (true/false):");
            Optional<String> optRegDate = prompt("Reg Date", "Enter registration date (YYYY-MM-DD):");

            if (!optModel.isPresent() || !optType.isPresent() || !optFuel.isPresent() || !optColour.isPresent()
                    || !optInsured.isPresent() || !optRegDate.isPresent()) {
                showAlert("Cancelled", "Vehicle creation cancelled.");
                return;
            }

            String model = optModel.get().trim();
            String type = optType.get().trim();
            String fuel = optFuel.get().trim();
            String colour = optColour.get().trim();
            boolean insured = Boolean.parseBoolean(optInsured.get().trim());
            java.sql.Date regDate;
            try {
                regDate = java.sql.Date.valueOf(optRegDate.get().trim());
            } catch (IllegalArgumentException ex) {
                showAlert("Invalid Date", "Registration date must be in YYYY-MM-DD format.");
                return;
            }

            new Vehicle(vehicleNo, offenderId, insured, regDate, type, model, fuel, colour);
        }

        String sql = "INSERT INTO ticket (officer_id, offender_id, vehicle_number, city, town, road, penalty, violation, fee, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, officerId);
            ps.setInt(2, offenderId);
            ps.setString(3, vehicleNo);
            ps.setString(4, city);
            ps.setString(5, town);
            ps.setString(6, road);
            ps.setString(7, penalty);
            ps.setString(8, violation);
            ps.setInt(9, fee);
            ps.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            showInfo("Success", "Ticket created successfully.");
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("DB Error", "Failed to create ticket: " + e.getMessage());
        }
    }

    private int findOffenderIdByLicense(int licenseId) {
        String q = "SELECT offender_id FROM offender WHERE license_id = ? ORDER BY offender_id DESC LIMIT 1";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q)) {
            ps.setInt(1, licenseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("offender_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Optional<String> prompt(String title, String content) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle(title);
        dlg.setHeaderText(null);
        dlg.setContentText(content);
        return dlg.showAndWait();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void clearForm() {
        vehicleNumberField.clear();
        cityField.clear();
        townField.clear();
        roadField.clear();
        penaltyField.clear();
        violationField.clear();
        feeField.clear();
    }
}

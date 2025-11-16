import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    String vehicle_number;
    int offender_id;
    boolean insured;
    java.sql.Date reg_date;
    String type, model, fuel, colour;
    String sql = "INSERT INTO vehicle (vehicle_number, offender_id, insured, reg_date, type, model, fuel, colour) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public Vehicle(String vehicle_number, int offender_id, boolean insured, java.sql.Date reg_date,
            String type, String model, String fuel, String colour) {
        this.vehicle_number = vehicle_number;
        this.offender_id = offender_id;
        this.insured = insured;
        this.reg_date = reg_date;
        this.type = type;
        this.model = model;
        this.fuel = fuel;
        this.colour = colour;
        insert();
    }

    public Vehicle() {
        this.vehicle_number = InputHelper.getString("Enter Vehicle Number");
        this.offender_id = InputHelper.getInt("Offender ID");

        if (!Offender.check(offender_id)) {
            System.out.println("Offender not found. Enter details for new offender:");
            new Offender();
        }

        this.insured = InputHelper.getBoolean("Is Vehicle Insured (true/false): ");
        this.reg_date = java.sql.Date.valueOf(InputHelper.getString("Enter Registration Date (YYYY-MM-DD)"));
        this.type = InputHelper.getString("Enter Vehicle Type");
        this.model = InputHelper.getString("Enter Model");
        this.fuel = InputHelper.getString("Enter Fuel Type");
        this.colour = InputHelper.getString("Enter Colour");
        insert();
    }

    private void insert() {
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, vehicle_number);
            ps.setInt(2, offender_id);
            ps.setBoolean(3, insured);
            ps.setDate(4, reg_date);
            ps.setString(5, type);
            ps.setString(6, model);
            ps.setString(7, fuel);
            ps.setString(8, colour);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean check(String number) {
        boolean exists = false;
        String q = "SELECT COUNT(*) FROM vehicle WHERE vehicle_number = ?";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q)) {
            ps.setString(1, number);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                exists = rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    private Vehicle(String vehicle_number, int offender_id, boolean insured, java.sql.Date reg_date,
            String type, String model, String fuel, String colour, boolean skipInsert) {
        this.vehicle_number = vehicle_number;
        this.offender_id = offender_id;
        this.insured = insured;
        this.reg_date = reg_date;
        this.type = type;
        this.model = model;
        this.fuel = fuel;
        this.colour = colour;
    }

    public static List<Vehicle> getAll() {
        List<Vehicle> list = new ArrayList<>();
        String q = "SELECT vehicle_number, offender_id, insured, reg_date, type, model, fuel, colour FROM vehicle";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getString("vehicle_number"),
                        rs.getInt("offender_id"),
                        rs.getBoolean("insured"),
                        rs.getDate("reg_date"),
                        rs.getString("type"),
                        rs.getString("model"),
                        rs.getString("fuel"),
                        rs.getString("colour"),
                        true);
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getVehicleNumber() {
        return vehicle_number;
    }

    public void setVehicleNumber(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public int getOffenderId() {
        return offender_id;
    }

    public void setOffenderId(int offender_id) {
        this.offender_id = offender_id;
    }

    public boolean isInsured() {
        return insured;
    }

    public void setInsured(boolean insured) {
        this.insured = insured;
    }

    public java.sql.Date getRegDate() {
        return reg_date;
    }

    public void setRegDate(java.sql.Date reg_date) {
        this.reg_date = reg_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}

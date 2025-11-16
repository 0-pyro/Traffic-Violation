import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    int ticket_id, officer_id, offender_id, fee;
    String vehicle_number, city, town, road, penalty, violation;
    java.sql.Timestamp date;
    String sql = "INSERT INTO ticket (officer_id, offender_id, vehicle_number, city, town, road, penalty, violation, fee, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public Ticket() {
        this.officer_id = InputHelper.getInt("Officer ID");
        if (!Officer.check(officer_id)) {
            System.out.println("Officer not found. Enter details for new officer:");
            new Officer();
        }

        this.offender_id = InputHelper.getInt("Offender ID");
        if (!Offender.check(offender_id)) {
            System.out.println("Offender not found. Enter details for new offender:");
            new Offender();
        }

        this.vehicle_number = InputHelper.getString("Enter Vehicle Number");
        if (!Vehicle.check(vehicle_number)) {
            System.out.println("Vehicle not found. Enter details for new vehicle:");
            new Vehicle();
        }

        this.city = InputHelper.getString("Enter City");
        this.town = InputHelper.getString("Enter Town");
        this.road = InputHelper.getString("Enter Road");
        this.penalty = InputHelper.getString("Enter Penalty Description");
        this.violation = InputHelper.getString("Enter Violation Description");
        this.fee = InputHelper.getInt("Enter Fee Amount");
        this.date = new java.sql.Timestamp(System.currentTimeMillis());
        insert();
    }

    public Ticket(int ticket_id, int officer_id, int offender_id, String vehicle_number,
            String city, String town, String road, String penalty, String violation,
            int fee, java.sql.Timestamp date) {
        this.ticket_id = ticket_id;
        this.officer_id = officer_id;
        this.offender_id = offender_id;
        this.vehicle_number = vehicle_number;
        this.city = city;
        this.town = town;
        this.road = road;
        this.penalty = penalty;
        this.violation = violation;
        this.fee = fee;
        this.date = date;
    }

    private void insert() {
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, officer_id);
            ps.setInt(2, offender_id);
            ps.setString(3, vehicle_number);
            ps.setString(4, city);
            ps.setString(5, town);
            ps.setString(6, road);
            ps.setString(7, penalty);
            ps.setString(8, violation);
            ps.setInt(9, fee);
            ps.setTimestamp(10, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Ticket> getAll() {
        List<Ticket> list = new ArrayList<>();
        String q = "SELECT ticket_id, officer_id, offender_id, vehicle_number, city, town, road, penalty, violation, fee, date FROM ticket";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getInt("officer_id"),
                        rs.getInt("offender_id"),
                        rs.getString("vehicle_number"),
                        rs.getString("city"),
                        rs.getString("town"),
                        rs.getString("road"),
                        rs.getString("penalty"),
                        rs.getString("violation"),
                        rs.getInt("fee"),
                        rs.getTimestamp("date"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTicketId() {
        return ticket_id;
    }

    public void setTicketId(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getOfficerId() {
        return officer_id;
    }

    public void setOfficerId(int officer_id) {
        this.officer_id = officer_id;
    }

    public int getOffenderId() {
        return offender_id;
    }

    public void setOffenderId(int offender_id) {
        this.offender_id = offender_id;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getVehicleNumber() {
        return vehicle_number;
    }

    public void setVehicleNumber(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }
}

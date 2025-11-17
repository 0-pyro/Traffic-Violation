import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrivingLicense {
    int id, age, offender_id;
    long phone;
    boolean validity;
    String address, type;
    String sql = "INSERT INTO driving_license (license_id, age, phone, validity, type, address) VALUES (?, ?, ?, ?, ?, ?)";

    // Constructor with offender_id (for newTicketController)
    public DrivingLicense(int id, int offender_id, int age, long phone, boolean validity, String address, String type) {
        this.id = id;
        this.offender_id = offender_id;
        this.age = age;
        this.phone = phone;
        this.validity = validity;
        this.address = address;
        this.type = type;
        insert();
    }

    // Constructor without offender_id
    public DrivingLicense(int id, int age, long phone, boolean validity, String address, String type) {
        this.id = id;
        this.offender_id = 0;
        this.age = age;
        this.phone = phone;
        this.validity = validity;
        this.address = address;
        this.type = type;
        insert();
    }

    public DrivingLicense() {
        this.id = InputHelper.getInt("License ID");
        this.offender_id = InputHelper.getInt("Offender ID");
        this.age = InputHelper.getInt("Age");
        this.phone = InputHelper.getLong("Phone Number");
        this.address = InputHelper.getString("Address");
        this.type = InputHelper.getString("License Type");
        this.validity = InputHelper.getBoolean("Is License Valid (true/false): ");
        insert();
    }

    public DrivingLicense(int id, int offender_id, int age, long phone, boolean validity, String address, String type,
            boolean skipInsert) {
        this.id = id;
        this.offender_id = offender_id;
        this.age = age;
        this.phone = phone;
        this.validity = validity;
        this.address = address;
        this.type = type;
    }

    private void insert() {
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, age);
            ps.setLong(3, phone);
            ps.setBoolean(4, validity);
            ps.setString(5, type);
            ps.setString(6, address);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean check(int id) {
        boolean exists = false;
        String q = "SELECT COUNT(*) FROM driving_license WHERE license_id = ?";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                exists = rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public static List<DrivingLicense> getAll() {
        List<DrivingLicense> list = new ArrayList<>();
        String q = "SELECT dl.license_id, o.offender_id, dl.age, dl.phone, dl.validity, dl.type, dl.address " +
                "FROM driving_license dl " +
                "LEFT JOIN offender o ON dl.license_id = o.license_id";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DrivingLicense dl = new DrivingLicense(
                        rs.getInt("license_id"),
                        rs.getInt("offender_id") == 0 ? 0 : rs.getInt("offender_id"),
                        rs.getInt("age"),
                        rs.getLong("phone"),
                        rs.getBoolean("validity"),
                        rs.getString("address"),
                        rs.getString("type"),
                        true);
                list.add(dl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOffenderId() {
        return offender_id;
    }

    public void setOffenderId(int offender_id) {
        this.offender_id = offender_id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

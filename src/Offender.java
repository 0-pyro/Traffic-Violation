import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Offender {
    int offender_id;
    String name;
    int license_id;
    String sql = "INSERT INTO offender (name, license_id) VALUES (?, ?)";

    public Offender(int offender_id, String name, int license_id) {
        this.offender_id = offender_id;
        this.name = name;
        this.license_id = license_id;
        insert();
    }

    public Offender() {
        this.name = InputHelper.getString("Enter Offender Name");
        this.license_id = InputHelper.getInt("License ID");

        if (!DrivingLicense.check(license_id)) {
            System.out.println("License not found. Enter details for new license:");
            new DrivingLicense(license_id,
                    InputHelper.getInt("Age"),
                    InputHelper.getLong("Phone Number"),
                    InputHelper.getBoolean("Is License Valid (true/false): "),
                    InputHelper.getString("Address"),
                    InputHelper.getString("Type"));
        }
        insert();
    }

    public Offender(int offender_id, String name, int license_id, boolean skipInsert) {
        this.offender_id = offender_id;
        this.name = name;
        this.license_id = license_id;
    }

    private void insert() {
        try (Connection c = DBC.conDB()) {
            if (this.offender_id <= 0) {
                String nextQ = "SELECT COALESCE(MAX(offender_id), 0) + 1 AS next_id FROM offender";
                try (PreparedStatement psn = c.prepareStatement(nextQ);
                        ResultSet rs = psn.executeQuery()) {
                    if (rs.next()) {
                        this.offender_id = rs.getInt("next_id");
                    } else {
                        this.offender_id = 1;
                    }
                }
            }
            String ins = "INSERT INTO offender (offender_id, name, license_id) VALUES (?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(ins)) {
                ps.setInt(1, this.offender_id);
                ps.setString(2, this.name);
                ps.setInt(3, this.license_id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean check(int id) {
        boolean exists = false;
        String q = "SELECT COUNT(*) FROM offender WHERE offender_id = ?";
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

    public static List<Offender> getAll() {
        List<Offender> list = new ArrayList<>();
        String q = "SELECT offender_id, name, license_id FROM offender";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Offender o = new Offender(
                        rs.getInt("offender_id"),
                        rs.getString("name"),
                        rs.getInt("license_id"),
                        true);
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getOffenderId() {
        return offender_id;
    }

    public void setOffenderId(int offender_id) {
        this.offender_id = offender_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLicenseId() {
        return license_id;
    }

    public void setLicenseId(int license_id) {
        this.license_id = license_id;
    }
}

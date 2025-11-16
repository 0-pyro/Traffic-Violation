import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Officer {
    int officer_id;
    String name, email, password;
    String sql = "INSERT INTO officer (name, email, password) VALUES (?, ?, ?)";

    public Officer(int officer_id, String name, String email, String password) {
        this.officer_id = officer_id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Officer() {
        this.name = InputHelper.getString("Enter Officer Name");
        this.email = InputHelper.getString("Enter Officer Email");
        this.password = InputHelper.getString("Enter Officer Password");
        insert();
    }

    public static Officer getOfficer(int id) {
        String q = "SELECT officer_id, name, email, password FROM officer WHERE officer_id = ?";
        Officer o = null;

        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int oid = rs.getInt("officer_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");

                o = new Officer(oid, name, email, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return o;
    }

    void insert() {
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean check(int id) {
        boolean exists = false;
        String q = "SELECT COUNT(*) FROM officer WHERE officer_id = ?";
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

    public static List<Officer> getAll() {
        List<Officer> list = new ArrayList<>();
        String q = "SELECT officer_id, name, email, password FROM officer";
        try (Connection c = DBC.conDB();
                PreparedStatement ps = c.prepareStatement(q);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Officer o = new Officer(
                        rs.getInt("officer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getOfficerId() {
        return officer_id;
    }

    public void setOfficerId(int officer_id) {
        this.officer_id = officer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

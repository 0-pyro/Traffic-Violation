import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String idStr = usernameField.getText().trim();
        String password = passwordField.getText();

        if (idStr.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both Officer ID and password.");
            return;
        }

        try {
            int officerId = Integer.parseInt(idStr);
            Officer officer = Officer.getOfficer(officerId);

            if (officer != null && officer.getPassword().equals(password)) {
                System.out.println("Login successful! Officer: " + officer.getName());
                loadDashboard(officer);
            } else {
                showAlert("Login Failed", "Invalid Officer ID or password.");
                passwordField.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Officer ID must be a number.");
            usernameField.clear();
        }
    }

    private void loadDashboard(Officer officer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();

            // pass logged in officer to dashboard controller
            DashboardController controller = loader.getController();
            controller.setOfficer(officer);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

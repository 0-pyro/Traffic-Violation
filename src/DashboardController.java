import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Label loggedInLabel;

    public void setOfficer(Officer officer) {
        if (officer != null) {
            loggedInLabel.setText("Logged in: " + officer.getName() + " (ID: " + officer.getOfficerId() + ")");
        } else {
            loggedInLabel.setText("Logged in: -");
        }
    }

    @FXML
    private void showTickets() {
        loadPage("tickets.fxml");
    }

    @FXML
    private void showOffenders() {
        loadPage("offenders.fxml");
    }

    @FXML
    private void showVehicles() {
        loadPage("vehicles.fxml");
    }

    @FXML
    private void showLicenses() {
        loadPage("licenses.fxml");
    }

    private void loadPage(String page) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(page));
            contentArea.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTicketCreationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newTicket.fxml"));
            Parent root = loader.load();

            Stage ticketStage = new Stage();
            ticketStage.setTitle("Create New Ticket");
            ticketStage.setScene(new Scene(root, 700, 600));
            ticketStage.setResizable(false);
            ticketStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

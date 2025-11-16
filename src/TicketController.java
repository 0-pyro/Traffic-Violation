import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TicketController {

    @FXML
    private TableView<Ticket> ticketTable;
    @FXML
    private TableColumn<Ticket, Integer> colTicketId;
    @FXML
    private TableColumn<Ticket, Integer> colOfficerId; // new
    @FXML
    private TableColumn<Ticket, Integer> colOffender;
    @FXML
    private TableColumn<Ticket, String> colVehicle;
    @FXML
    private TableColumn<Ticket, String> colCity;
    @FXML
    private TableColumn<Ticket, String> colTown;
    @FXML
    private TableColumn<Ticket, String> colRoad;
    @FXML
    private TableColumn<Ticket, String> colViolation;
    @FXML
    private TableColumn<Ticket, String> colPenalty;
    @FXML
    private TableColumn<Ticket, Integer> colAmount;
    @FXML
    private TableColumn<Ticket, java.sql.Timestamp> colDate;

    @FXML
    public void initialize() {
        colTicketId.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        colOfficerId.setCellValueFactory(new PropertyValueFactory<>("officerId")); // new
        colOffender.setCellValueFactory(new PropertyValueFactory<>("offenderId"));
        colVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colTown.setCellValueFactory(new PropertyValueFactory<>("town"));
        colRoad.setCellValueFactory(new PropertyValueFactory<>("road"));
        colViolation.setCellValueFactory(new PropertyValueFactory<>("violation"));
        colPenalty.setCellValueFactory(new PropertyValueFactory<>("penalty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<Ticket> items = FXCollections.observableArrayList(Ticket.getAll());
        ticketTable.setItems(items);

        System.out.println("Tickets page loaded. " + items.size() + " rows.");
    }
}

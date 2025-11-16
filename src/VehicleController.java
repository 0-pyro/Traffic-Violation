import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class VehicleController {

    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, String> colVehicleNo;
    @FXML
    private TableColumn<Vehicle, Integer> colOwnerId;
    @FXML
    private TableColumn<Vehicle, String> colModel;
    @FXML
    private TableColumn<Vehicle, String> colType;

    // newly added columns
    @FXML
    private TableColumn<Vehicle, Boolean> colInsured;
    @FXML
    private TableColumn<Vehicle, java.sql.Date> colRegDate;
    @FXML
    private TableColumn<Vehicle, String> colFuel;
    @FXML
    private TableColumn<Vehicle, String> colColour;

    @FXML
    public void initialize() {
        colVehicleNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colOwnerId.setCellValueFactory(new PropertyValueFactory<>("offenderId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        colInsured.setCellValueFactory(new PropertyValueFactory<>("insured"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));
        colFuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        colColour.setCellValueFactory(new PropertyValueFactory<>("colour"));

        ObservableList<Vehicle> items = FXCollections.observableArrayList(Vehicle.getAll());
        vehicleTable.setItems(items);

        System.out.println("Vehicles page loaded. " + items.size() + " rows.");
    }
}

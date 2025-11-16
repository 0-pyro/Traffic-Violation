import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class LicenseController {

    @FXML
    private TableView<DrivingLicense> licenseTable;
    @FXML
    private TableColumn<DrivingLicense, Integer> colLicenseNo;
    @FXML
    private TableColumn<DrivingLicense, Integer> colOffenderId;
    @FXML
    private TableColumn<DrivingLicense, Integer> colAge;
    @FXML
    private TableColumn<DrivingLicense, Long> colPhone;
    @FXML
    private TableColumn<DrivingLicense, Boolean> colValidity;
    @FXML
    private TableColumn<DrivingLicense, String> colType;
    @FXML
    private TableColumn<DrivingLicense, String> colAddress;

    @FXML
    public void initialize() {
        colLicenseNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOffenderId.setCellValueFactory(new PropertyValueFactory<>("offenderId"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colValidity.setCellValueFactory(new PropertyValueFactory<>("validity"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        ObservableList<DrivingLicense> items = FXCollections.observableArrayList(DrivingLicense.getAll());
        licenseTable.setItems(items);

        System.out.println("Licenses page loaded. " + items.size() + " rows.");
    }
}

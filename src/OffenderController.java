import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class OffenderController {

    @FXML
    private TableView<Offender> offenderTable;
    @FXML
    private TableColumn<Offender, Integer> colOffenderId;
    @FXML
    private TableColumn<Offender, String> colName;
    @FXML
    private TableColumn<Offender, Integer> colLicenseId;

    @FXML
    public void initialize() {
        colOffenderId.setCellValueFactory(new PropertyValueFactory<>("offenderId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLicenseId.setCellValueFactory(new PropertyValueFactory<>("licenseId"));

        ObservableList<Offender> items = FXCollections.observableArrayList(Offender.getAll());
        offenderTable.setItems(items);

        System.out.println("Offenders page loaded. " + items.size() + " rows.");
    }
}

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class WinnerController {
    @FXML
    private Button saveButton;
    @FXML
    private TextField NameTextField;

    private RecordTable recordTable = new RecordTable();
    private CellOfTable newResult = new CellOfTable();

    public void Init(){

        newResult.Time = 2;
        newResult.Moves = 3;
        saveButton.setOnAction(event -> {

            newResult.Name = NameTextField.getText();
            NameTextField.setText("");

            recordTable.addNewRecord(newResult);
        });
    }

}

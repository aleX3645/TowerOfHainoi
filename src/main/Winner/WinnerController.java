package main.Winner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MainGame.Time.Time;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;
import main.RecordTable.RecordTableController;

public class WinnerController {
    @FXML
    private Button saveButton;
    @FXML
    private TextField NameTextField;

    private RecordTableData recordTableData = new RecordTableData(0);
    RecordTableController recordController;

    int moves = 0;
    Time time;

    public void Init(int difficulty, int moves, Time time, RecordTableController recordController){

        this.moves = moves;
        this.time = time;
        this.recordController = recordController;

        recordTableData = new RecordTableData(difficulty);

        saveButton.setOnAction(event -> {

            recordTableData.addNewRecord(new Data(NameTextField.getText(),moves,time));
            recordController.Refresh();

            NameTextField.setText("");
            closeStage();
        });
    }

    private void closeStage()
    {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

}

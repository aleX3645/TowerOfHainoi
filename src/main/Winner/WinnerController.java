package main.Winner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.MainGame.Time.Time;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;

public class WinnerController {
    @FXML
    private Button saveButton;
    @FXML
    private TextField NameTextField;

    private RecordTableData recordTableData = new RecordTableData();

    int moves = 0;
    Time time;

    public void Init(int moves, Time time){

        this.moves = moves;
        this.time = time;

        saveButton.setOnAction(event -> {

            recordTableData.addNewRecord(new Data(NameTextField.getText(),moves,time));
            NameTextField.setText("");
        });
    }

}

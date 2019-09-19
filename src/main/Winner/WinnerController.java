package main.Winner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MainGame.Time.Time;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;
import main.RecordTable.RecordTableController;

/**
 * Контроллер победного окна
 * */
public class WinnerController {

    @FXML
    private Button saveButton;
    @FXML
    private TextField NameTextField;

    /**Таблица рекордов*/
    private RecordTableData recordTableData = new RecordTableData(3);
    /**Ссылка на контроллер таблицы ркордов*/
    RecordTableController recordController;

    /**Количество ходов*/
    int moves = 0;
    /**Затраченное время*/
    Time time;
    /**
     * инициализирует контроллер по сложности, количеству шагов и времени
     * */
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

    /**
     * Закрывает окно
     * */
    private void closeStage()
    {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

}

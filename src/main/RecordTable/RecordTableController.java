package main.RecordTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Difficulty.DifficultyScene;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;

import java.io.IOException;

/**
 * Контроллер таблицы рекордов
 * */
public class RecordTableController {

    @FXML
    TableView recordTable;
    @FXML
    TableColumn nameColumn;
    @FXML
    TableColumn movesColumn;
    @FXML
    TableColumn timeColumn;
    @FXML
    Button toMenuButton;
    @FXML
    ChoiceBox<String> choiceBox;


    /**Таблица рекордов*/
    RecordTableData recordTableData = new RecordTableData(3);
    /**Лист со словестным выбором сложности для ChoiceBox*/
    final ObservableList<String> forChoiceBox = FXCollections.observableArrayList("Легкий", "Средний", "Сложный", "Очень сложный");

    /**Сложность*/
    int difficulty = 0;
    /**
     * Загружает нужную таблицу по сложности.
     * */
    public void Init(int difficulty){
        this.difficulty = difficulty;
        recordTableData = new RecordTableData(this.difficulty);

        choiceBox.setItems(forChoiceBox);
        choiceBox.setValue(intToChoice(difficulty));

        nameColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("name"));
        movesColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("moves"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("time"));

        recordTable.setItems(recordTableData.getList());
    }

    /**
     * Событие нажатия на меню
     * */
    @FXML
    public void onClickToMenu(ActionEvent event) throws Exception{

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/main/Menu/Menu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");
        stage.setScene(new Scene(root, 310, 310));
        stage.setMinWidth(300);
        stage.setMinHeight(300);
        stage.show();
        CloseStage();

    }

    /**
     * Событие нажатия на сброс, сбрасывает таблицу.
     * */
    @FXML
    public void onClickReset(){
        recordTableData.Reset();
        Refresh();
    }

    /**
     * Событие выбора сложности
     * */
    @FXML
    public void onClickChoice(){
        difficulty = choiceToInt(choiceBox.getValue());
        Refresh();
    }

    /**
     * Обновление таблицы
     * */
    public void Refresh(){
        recordTableData = new RecordTableData(difficulty);
        recordTable.setItems(recordTableData.getList());
    }

    /**
     * Переводит слово в сложность
     * */
    private int choiceToInt(String choice){
        switch(choice){
            case("Средний"):
                return 5;
            case("Сложный"):
                return 8;
            case("Очень сложный"):
                return 15;
            case("Легкий"):
            default:
                return 3;
        }
    }

    /**
     * Переводит сложность в слово
     * */
    private String intToChoice(int code){
        switch(code){
            case(5):
                return "Средний";
            case(8):
                return "Сложный";
            case(15):
                return "Очень сложный";
            case(3):
            default:
                return "Легкий";
        }
    }

    /**
     * Закрывает окно
     * */
    public void CloseStage()
    {
        Stage stage = (Stage) toMenuButton.getScene().getWindow();
        stage.close();
    }
}


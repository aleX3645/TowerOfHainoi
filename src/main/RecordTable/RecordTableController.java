package main.RecordTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import main.BuildPane.GamePane;
import main.Menu.MenuController;
import main.RecordTable.RecordData.Data;
import main.RecordTable.RecordData.RecordTableData;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

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
    private RecordTableData recordTableData = new RecordTableData(3);
    /**Лист со словестным выбором сложности для ChoiceBox*/
    private final ObservableList<String> forChoiceBox = FXCollections.observableArrayList("Легкий", "Средний", "Сложный", "Очень сложный");

    /**Сложность*/
    private int difficulty = 0;
    Stage primaryStage;
    GamePane gamePane;
    /**
     * Загружает нужную таблицу по сложности.
     * */
    public void Init(int difficulty, Stage primaryStage, GamePane gamePane){
        this.difficulty = difficulty;
        this.primaryStage = primaryStage;
        this.gamePane = gamePane;

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
    public void onClickToMenu(){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Menu/Menu.fxml"));

        Parent root = new Group();
        try {
            root = loader.load();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        MenuController menuController = loader.getController();
        menuController.setPrimaryStage(primaryStage);
        menuController.setGamePain(gamePane);

        gamePane.clearPain();
        gamePane.setRoot(root);
    }

    /**
     * Событие нажатия на сброс, сбрасывает таблицу.
     * */
    @FXML
    public void onClickReset(){
        if(gamePane.returnGame().getTime().getMinutes() != 0 ||
                gamePane.returnGame().getTime().getSeconds() != 0 ||
                gamePane.returnGame().getTime().getMseconds() != 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Внимание");
            alert.setHeaderText("Вы уверены, что хотите сбросить таблицу рекордов?");
            ButtonType yes = new ButtonType("Сбросить");
            ButtonType no = new ButtonType("Отмена");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(yes,no);
            Optional<ButtonType> option = alert.showAndWait();
            if(option.get() == no || option.get() == null){return;}
        }

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


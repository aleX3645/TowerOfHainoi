package main.MainGame;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import javafx.util.Duration;
import main.BuildPane.GamePane;
import main.Difficulty.DifficultyScene;
import main.MainGame.Main.Game;
import main.MainGame.Time.Time;
import main.RecordTable.RecordTableController;
import main.Winner.WinnerController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Главный контроллер игры
 * */
public class ControllerMain{

    public ControllerMain(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    public ControllerMain(Stage primaryStage, GamePane gamePane){
        this.primaryStage = primaryStage;

        this.gamePane = gamePane;
        game = gamePane.returnGame();

        camera = gamePane.getCamera();
        rotateY = gamePane.getRotateY();

        time = game.getTime();
        moves = game.getMoves();
    }

    GamePane gamePane;
    /**Класс игры*/
    private Game game;
    /**Затраченные шаги*/
    private int moves = 0;
    /**Затраченное время*/
    private Timer timer;
    /**Главная сцена*/
    Stage primaryStage;
    /**Сложность, означает количество колец*/
    private int difficulty = 3;
    /**
     * Инициализирует и создает view с полем по переданному количеству колец.
     * */
    public void Init(int difficulty){

        this.difficulty = difficulty;

        Scene scene;
        if (game == null) {
            gamePane = new GamePane(difficulty);
            game = gamePane.returnGame();
            camera = gamePane.getCamera();
            rotateY = gamePane.getRotateY();
            scene = new Scene(gamePane.returnPane());
            primaryStage.setScene(scene);

            game.setControllerMain(this);
            addMouseEvent(scene);
            buildPane();
        }else{
            scene = primaryStage.getScene();

            game.setControllerMain(this);
            addMouseEvent(scene);
            buildPane();

            timeLabel.setText(time.toString());
        }

        primaryStage.setOnCloseRequest(event -> {
            try {
                if(!timeLabel.getText().equals("0")){
                    timer.cancel();
                }

                game.save(time, moves);
                FileOutputStream fos = new FileOutputStream("save.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(game);

                oos.flush();
                oos.close();
                fos.close();

                primaryStage.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            event.consume();
        });
        gamePane.UnBlur();
    }

    private Label timeLabel;
    private Label movesLabel;
    private Button autoButton;
    /**
     * Добавляет элементы на AnchorPane
     * */
    private Pane buildPane(){

        AnchorPane pane = gamePane.returnPane();

        Font f = new Font(40);
        Background background= new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY));

        timeLabel = new Label("0");
        timeLabel.setFont(f);
        timeLabel.setBackground(background);

        pane.setTopAnchor(timeLabel, 0.0);
        pane.setLeftAnchor(timeLabel,10.0);

        movesLabel = new Label(Integer.toString(moves));
        movesLabel.setFont(f);
        movesLabel.setBackground(background);

        pane.setTopAnchor(movesLabel, 0.0);
        pane.setRightAnchor(movesLabel,10.0);

        autoButton = new Button("Автоматическая сборка");
        autoButton.setOnAction((event-> game.auto()));

        pane.setBottomAnchor(autoButton,10.0);
        pane.setLeftAnchor(autoButton,10.0);

        pane.getChildren().add(timeLabel);
        pane.getChildren().add(movesLabel);
        pane.getChildren().add(autoButton);

        return pane;
    }

    /**Камера для сцены*/
    private PerspectiveCamera camera;
    /**Класс вращения вокруг Y*/
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    public void StartTimer(){
        timer = new Timer();
        myTimerTask myTask = new myTimerTask();
        timer.schedule(myTask, 0,9);
    }

    /**
     * Получает из класса game результат передвижения элемента:
     * 1 - Передвинуть можно. Просто передвигает элемент
     * -1 - Передвинуть нельзя.
     * 0 - Победа пользователя. Вызывает победное view
     * */
    public void MoveHandler(int state){

        switch(state){
            case 1:
                moves++;
                movesLabel.setText(Integer.toString(moves));
                break;
            case -1:
                break;
            case 0:
                moves++;
                movesLabel.setText(String.valueOf(moves));
                if(time.getSeconds() != 0 && time.getMseconds() != 0){
                    timer.cancel();
                }

                FXMLLoader recordLoader = new FXMLLoader(getClass().getResource("/main/RecordTable/RecordTable.fxml"));
                GamePane gamePane = new GamePane(5);

                Parent recordRoot = new Group();
                try {
                    recordRoot = recordLoader.load();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                gamePane.setRoot(recordRoot);

                Scene scene = new Scene(gamePane.returnPane());
                primaryStage.setScene(scene);

                RecordTableController recordController = recordLoader.getController();

                RecordTableController  controller = recordLoader.getController();
                controller.Init(difficulty,primaryStage,gamePane);


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Winner/WinnerScene.fxml"));

                Parent root = new Group();
                try {
                     root = loader.load();
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setScene(new Scene(root, 400, 270));

                WinnerController winController = loader.getController();
                winController.Init(moves, time, difficulty, recordController);

                stage.show();
                break;
            case 2:
                moves++;
                FXMLLoader load = new FXMLLoader(getClass().getResource("/main/Difficulty/difficultyScene.fxml"));
                Parent rootForAuto = new Group();
                GamePane gamePane1 = new GamePane(5);

                try {
                    rootForAuto = load.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gamePane1.setRoot(rootForAuto);


                DifficultyScene controller1 = load.getController();
                controller1.Init(primaryStage,gamePane1);
                break;
        }

    }

    /**Позиция мыши*/
    private double mousePosX;
    /**Позиция мыши до начало передвижения*/
    private double mouseOldX;
    /**Смещение мыши*/
    private double mouseDeltaX;
    private static double MOUSE_SPEED = 0.2;
    private int scrollCounter = 0;
    /**
     * Добавляет события для мыши
     * */
    private void addMouseEvent(Scene scene) {

        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mouseOldX = me.getSceneX();
        });

        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mousePosX = me.getSceneX();
            mouseDeltaX = (mousePosX - mouseOldX);

            if (me.isPrimaryButtonDown()) {
                rotateY.angleProperty().setValue(rotateY.angleProperty().getValue() + mouseDeltaX * MOUSE_SPEED);
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            if(delta <0 && scrollCounter != -5){
                scrollCounter--;
                camera.getTransforms().addAll (
                        new Translate(0, 0, delta*3)
                );
            }
            if(delta>0 && scrollCounter != 7){
                scrollCounter++;
                camera.getTransforms().addAll (
                        new Translate(0, 0, delta*3)
                );
            }
        });
    }

    /**Лист для таблицы рекордов*/
    private Time time = new Time();
    /**
     * Класс для потока секундамера
     * */
    class myTimerTask extends TimerTask{

        @Override
        public void run(){
            Platform.runLater(() -> {
                time.addTime(1);
                timeLabel.setText(time.toString());
            });
        }
    }

    /**
     * Закрывает окно
     * */
    private void closeStage()
    {
        primaryStage.close();
    }

}

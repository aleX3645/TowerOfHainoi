package main.MainGame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.MainGame.Main.Game;
import main.MainGame.Time.Time;
import main.RecordTable.RecordTableController;
import main.Winner.WinnerController;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Главный контроллер игры
 * */
public class ControllerMain{

    public ControllerMain(){}
    public ControllerMain(Game game){

        this.game = game;
        game.setControllerMain(this);

        time = game.getTime();
        moves = game.getMoves();
    }

    /**Класс игры*/
    private Game game;
    /**Затраченные шаги*/
    private int moves = 0;
    /**Затраченное время*/
    Timer timer;
    /**Главная сцена*/
    Stage stage = new Stage();
    /**Сложность, означает количество колец*/
    int difficulty = 3;
    /**
     * Инициализирует и создает view с полем по переданному количеству колец.
     * */
    public void Init(int difficulty){

        stage = new Stage();
        stage.setFullScreen(true);
        stage.setTitle("Хайнойские башни");

        this.difficulty = difficulty;

        if (game == null) {
            game = new Game(difficulty);
        }

        game.setControllerMain(this);

        Scene scene = new Scene(buildPane());
        addMouseEvent(scene);

        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            try {
                timer.cancel();
                game.save(time, moves);
                FileOutputStream fos = new FileOutputStream("save.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(game);

                oos.flush();
                oos.close();
                fos.close();

                stage.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            event.consume();
        });

        timer = new Timer();
        myTimerTask myTask = new myTimerTask();
        timer.schedule(myTask, 0,9);


    }

    /**Группа со всеми элементами*/
    private Group root3d = new Group();

    private Label timeLabel;
    private Label movesLabel;
    /**
     * Создает AnchorPane и добавляет на него элементы
     * */
    private Pane buildPane(){

        AnchorPane pane = new AnchorPane();
        PerspectiveCamera camera = setCamera();

        Font f = new Font(40);

        root3d = game.returnGameField();
        //root3d.getChildren().add(camera);

        SubScene sub = new SubScene(root3d,1920,1080,true,SceneAntialiasing.BALANCED);
        sub.setCamera(camera);
        sub.setFill(Color.AQUAMARINE);

        pane.setTopAnchor(sub, 0.0);
        pane.setLeftAnchor(sub, 0.0);
        pane.setRightAnchor(sub, 0.0);
        pane.setBottomAnchor(sub, 0.0);

        timeLabel = new Label("0");

        timeLabel.setFont(f);

        pane.setTopAnchor(timeLabel, 0.0);
        pane.setLeftAnchor(timeLabel,10.0);

        movesLabel = new Label(Integer.toString(moves));

        movesLabel.setFont(f);

        pane.setTopAnchor(movesLabel, 0.0);
        pane.setRightAnchor(movesLabel,10.0);

        pane.getChildren().add(sub);
        pane.getChildren().add(timeLabel);
        pane.getChildren().add(movesLabel);

        return  pane;
    }

    /**Камера для сцены*/
    PerspectiveCamera camera;
    /**Точка вокруг которой вращается камера*/
    Translate pivot = new Translate();
    /**Класс вращения вокруг Y*/
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    /**
     * Возвращает камеру с выставленными параметрами.
     * */
    private PerspectiveCamera setCamera(){

        camera = new PerspectiveCamera(true);

        pivot.setX(700);
        pivot.setY(500);
        pivot.setZ(0);

        camera.getTransforms().addAll (
                pivot,
                rotateY,
                new Rotate(-10, Rotate.X_AXIS),
                new Translate(0, 0, -1500)
        );

        camera.setNearClip(0.1);
        camera.setFarClip(5000);

        return camera;
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
                timer.cancel();

                Stage recordStage = new Stage();
                recordStage.setTitle("Хайнойские башни");
                FXMLLoader recordLoader = new FXMLLoader(getClass().getResource("/main/RecordTable/RecordTable.fxml"));

                try {
                    Parent recordRoot = recordLoader.load();
                    recordStage.setScene(new Scene(recordRoot, 600, 400));
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                RecordTableController recordController = recordLoader.getController();

                RecordTableController  controller = recordLoader.getController();
                controller.Init(difficulty);
                recordStage.show();

                Stage stage = new Stage();
                stage.setTitle("Хайнойские башни");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Winner/WinnerScene.fxml"));

                try {
                    Parent root = loader.load();
                    stage.setScene(new Scene(root, 300, 200));
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                WinnerController winController = loader.getController();
                winController.Init(difficulty, moves,time, recordController);
                stage.show();

                closeStage();
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
    int scrollCounter = 0;
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
    Time time = new Time();
    /**
     * Класс для потока секундамера
     * */
    class myTimerTask extends TimerTask{

        @Override
        public void run(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    time.addTime(1);
                    timeLabel.setText(time.toString());
                }
            });
        }
    }

    /**
     * Закрывает окно
     * */
    private void closeStage()
    {
        stage.close();
    }

}

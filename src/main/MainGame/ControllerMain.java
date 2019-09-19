package main.MainGame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
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
import main.MainGame.Main.XGroup;
import main.MainGame.Time.Time;
import main.RecordTable.RecordTableController;
import main.Winner.WinnerController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerMain{
    public ControllerMain(){}

    public ControllerMain(Game game){

        this.game = game;
        game.setControllerMain(this);

        time = game.getTime();
        moves = game.getMoves();
    }

    private Game game;
    private int from = -1;

    private double mouseStartPosX, mouseStartPosY;
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private double mouseDeltaX, mouseDeltaY;
    private static double MOUSE_SPEED = 0.1;
    private static double ROTATION_SPEED = 2.0;

    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    private XGroup root3d = new XGroup();

    private Label timeLabel;
    private Label movesLabel;

    private int moves = 0;

    Timer timer;

    Stage stage = new Stage();
    int difficulty = 0;
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
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
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
            }
        });

        timer = new Timer();
        myTimerTask myTask = new myTimerTask();
        timer.schedule(myTask, 0,10);


    }

    private Pane buildPane(){

        AnchorPane pane = new AnchorPane();
        PerspectiveCamera camera = setCamera();

        Font f = new Font(40);

        root3d = new XGroup(game.returnGameField());
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

    PerspectiveCamera camera;
    private PerspectiveCamera setCamera(){

        camera = new PerspectiveCamera(true);

        pivot.setX(700);
        pivot.setY(500);
        pivot.setZ(0);

        camera.getTransforms().addAll (
                pivot,
                rotateX,
                rotateY,
                new Rotate(-10, Rotate.X_AXIS),
                new Translate(0, 0, -1500)
        );

        camera.setNearClip(0.1);
        camera.setFarClip(5000);

        return camera;
    }

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
                controller.Init(codeToInt(difficulty));
                recordStage.show();

                Stage stage = new Stage();
                stage.setTitle("Хайнойские башни");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Winner/WinnerScene.fxml"));

                try {
                    Parent root = loader.load();
                    stage.setScene(new Scene(root, 600, 400));
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                WinnerController winController = loader.getController();
                winController.Init(codeToInt(difficulty), moves,time, recordController);
                stage.show();

                closeStage();
                break;
        }

    }

    Translate pivot = new Translate();
    int scrollCounter = 0;

    private void addMouseEvent(Scene scene) {

        scene.setOnMousePressed(me -> {
            mouseStartPosX = me.getSceneX();
            mouseStartPosY = me.getSceneY();

            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;

            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();

            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            if (me.isPrimaryButtonDown()) {
                //root3d.addRotation(-mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED, Rotate.Y_AXIS);
                rotateY.angleProperty().setValue(rotateY.angleProperty().getValue() + mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED); //root3d.addRotation(mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED, Rotate.X_AXIS);
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            System.out.println(scrollCounter);
            double delta = event.getDeltaY();
            System.out.println(delta);
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

    private int codeToInt(int code){
        switch(code){
            case(5):
                return 1;
            case(8):
                return 2;
            case(15):
                return 3;
            case(3):
            default:
                return 0;
        }
    }

    class myTimerTask extends TimerTask{

        @Override
        public void run(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addTime();
                }
            });
        }
    }

    Time time = new Time();
    private void addTime(){
        time.addTime(10);
        timeLabel.setText(time.toString());
    }

    private void closeStage()
    {
        stage.close();
    }

}

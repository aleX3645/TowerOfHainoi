package main.MainGame;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import main.MainGame.Time.Time;
import main.Winner.WinnerController;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerMain{

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

    public void Init(int difficulty){

        Stage stage = new Stage();
        stage.setFullScreen(true);
        stage.setTitle("Хайнойские башни");

        game = new Game(difficulty);


        Scene scene = new Scene(buildPane());
        addKeyEvent(stage);
        addMouseEvent(scene);

        stage.setScene(scene);
        stage.show();

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

        timeLabel = new Label("text");

        timeLabel.setFont(f);

        pane.setTopAnchor(timeLabel, 0.0);
        pane.setLeftAnchor(timeLabel,10.0);

        movesLabel = new Label("0");

        movesLabel.setFont(f);

        pane.setTopAnchor(movesLabel, 0.0);
        pane.setRightAnchor(movesLabel,10.0);

        pane.getChildren().add(sub);
        pane.getChildren().add(timeLabel);
        pane.getChildren().add(movesLabel);

        return  pane;
    }

    private PerspectiveCamera setCamera(){

        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.translateXProperty().set(700);
        camera.translateYProperty().set(500);
        camera.translateZProperty().set(-1500);

        camera.getTransforms().addAll (rotateX, rotateY);//Math.asin(250/1500);

        camera.setNearClip(0.1);
        camera.setFarClip(5000);

        return camera;
    }

    private void moveHandler(int state) throws IOException {

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

                Stage stage = new Stage();
                stage.setTitle("Хайнойские башни");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Winner/WinnerScene.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root, 600, 400));

                WinnerController controller = loader.getController();
                controller.Init(moves,time);
                stage.show();

                break;
        }

    }

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
                root3d.addRotation(-mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED, Rotate.Y_AXIS);
                //root3d.addRotation(mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED, Rotate.X_AXIS);
            }
        });

        /*
        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            //here need to implement scrolling
        });*/
    }


    private void addKeyEvent(Stage myStage) {

        myStage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {

            switch (event.getCode()){
                case DIGIT1:
                    if(from == -1){
                        from = 0;
                    }else{
                        try {
                            moveHandler(game.Move(from,0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        from = -1;
                    }
                    break;
                case DIGIT2:
                    if(from == -1){

                        from = 1;
                    }else{
                        try {
                            moveHandler(game.Move(from,1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        from = -1;
                    }
                    break;
                case DIGIT3:
                    if(from == -1){
                        from = 2;
                    }else{
                        try {
                            moveHandler(game.Move(from,2));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        from = -1;
                    }
                    break;
            }
        });
    }

    Time time = new Time();
    private void addTime(){
        time.addTime(10);
        timeLabel.setText(time.toString());
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

}

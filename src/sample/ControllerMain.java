package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class ControllerMain{

    private Game game;
    PerspectiveCamera camera;
    private int from = -1;

    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    public void Init(int difficulty){
        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");

        game = new Game(difficulty);
        camera = setCamera();

        Group root3d = new Group(game.returnGameField());
        root3d.getChildren().add(camera);

        SubScene sub = new SubScene(root3d,1920,1080,true,SceneAntialiasing.BALANCED);
        sub.setFill(Color.AQUAMARINE);
        sub.setCamera(camera);
        addKeyEvent(stage);

        BorderPane pane = new BorderPane();
        pane.setCenter(sub);
        Label timeLabel = new Label("text");
        timeLabel.setTextAlignment(TextAlignment.CENTER);
        pane.setTop(timeLabel);

        Scene scene = new Scene(pane);

        scene.setOnMousePressed((MouseEvent me) -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            //here need to implement scrolling
        });

        stage.setScene(scene);
        stage.show();
    }

    private PerspectiveCamera setCamera(){
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.translateXProperty().set(700);
        camera.translateYProperty().set(500);
        camera.translateZProperty().set(-1500);

        camera.getTransforms().addAll (rotateX, rotateY, new Translate(0, 0, 0));
        camera.setNearClip(0.1);
        camera.setFarClip(5000);
        return camera;
    }

    private void addKeyEvent(Stage myStage){
        myStage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {

            switch (event.getCode()){
                case DIGIT1:
                    if(from == -1){
                        from = 0;
                    }else{
                        game.Move(from,0);
                        from = -1;
                    }
                    break;
                case DIGIT2:
                    if(from == -1){

                        from = 1;
                    }else{
                        game.Move(from,1);
                        from = -1;
                    }
                    break;
                case DIGIT3:
                    if(from == -1){
                        from = 2;
                    }else{
                        game.Move(from,2);
                        from = -1;
                    }
                    break;
            }
        });
    }

}

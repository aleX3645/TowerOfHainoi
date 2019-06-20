package sample;

import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ControllerMain{

    private Game game;
    private PerspectiveCamera camera;
    private int from = -1;

    double mouseStartPosX, mouseStartPosY;
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    double mouseDeltaX, mouseDeltaY;
    private static double MOUSE_SPEED = 0.1;
    private static double ROTATION_SPEED = 2.0;

    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    XGroup root3d = new XGroup();

    public void Init(int difficulty){

        Stage stage = new Stage();
        stage.setTitle("Хайнойские башни");

        Scene scene = new Scene( buildPane(difficulty) );
        addKeyEvent(stage);
        addMouseEvent(scene);

        stage.setScene(scene);
        stage.show();
    }

    private Pane buildPane(int difficulty){

        game = new Game(difficulty);
        camera = setCamera();

        root3d = new XGroup(game.returnGameField());
        root3d.getChildren().add(camera);

        SubScene sub = new SubScene(root3d,1920,1080,true,SceneAntialiasing.BALANCED);
        sub.setFill(Color.AQUAMARINE);

        BorderPane pane = new BorderPane();
        pane.setCenter(sub);
        Label timeLabel = new Label("text");

        timeLabel.setTextAlignment(TextAlignment.CENTER);
        pane.setTop(timeLabel);

        return  pane;
    }

    private PerspectiveCamera setCamera(){

        PerspectiveCamera camera = new PerspectiveCamera(false);

        camera.translateXProperty().set(700);
        camera.translateYProperty().set(500);
        camera.translateZProperty().set(-500);

        camera.getTransforms().addAll (rotateX, rotateY, new Translate(0, 0, 0));
        camera.setNearClip(0.1);
        camera.setFarClip(5000);

        return camera;
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
                root3d.addRotation(mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED, Rotate.X_AXIS);
            }
        });

        /*
        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            //here need to implement scrolling
        });*/
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

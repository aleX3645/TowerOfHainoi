import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;

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

    public void Init(int difficulty){

        Stage stage = new Stage();
        stage.setFullScreen(true);
        stage.setTitle("Хайнойские башни");

        game = new Game(difficulty,this);


        Scene scene = new Scene(buildPane());
        addKeyEvent(stage);
        addMouseEvent(scene);

        stage.setScene(scene);
        stage.show();
    }

    private Pane buildPane(){


        PerspectiveCamera camera = setCamera();

        root3d = new XGroup(game.returnGameField());
        //root3d.getChildren().add(camera);

        SubScene sub = new SubScene(root3d,1920,1080,true,SceneAntialiasing.BALANCED);
        sub.setCamera(camera);
        sub.setFill(Color.AQUAMARINE);

        BorderPane pane = new BorderPane();
        pane.setCenter(sub);

        Label timeLabel = new Label("text");

        timeLabel.setTextAlignment(TextAlignment.CENTER);

        pane.setLeft(timeLabel);

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
                break;
            case -1:
                break;
            case 0:
                Stage stage = new Stage();
                stage.setTitle("Хайнойские башни");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("WinnerScene.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root, 600, 400));

                WinnerController controller = loader.getController();
                controller.Init();
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

}

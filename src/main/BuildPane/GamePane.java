package main.BuildPane;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import main.MainGame.Main.Game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class GamePane {
    public GamePane(){

        try {
            FileInputStream fis = new FileInputStream("save.ser");
            ObjectInputStream oin = new ObjectInputStream(fis);
            game = (Game) oin.readObject();
            game.buildTorus();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            game = new Game(5);
        } catch (IOException ex) {
            ex.printStackTrace();
            game = new Game(5);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            game = new Game(5);
        }

        pane = new AnchorPane();
        Group root3d = game.returnGameField();
        camera = setCamera();

        SubScene sub = new SubScene(root3d,1920,1080,true, SceneAntialiasing.BALANCED);
        sub.setCamera(camera);
        sub.setFill(Color.AQUAMARINE);
        sub.setEffect(gb);

        sub.heightProperty().bind(pane.heightProperty());
        sub.widthProperty().bind(pane.widthProperty());

        pane.setTopAnchor(sub, 0.0);
        pane.setLeftAnchor(sub, 0.0);
        pane.setRightAnchor(sub, 0.0);
        pane.setBottomAnchor(sub, 0.0);

        pane.getChildren().add(sub);
    }

    public GamePane(Parent root){
        this();

        this.root = root;
        pane.setTopAnchor(this.root, 0.0);
        pane.setLeftAnchor(this.root, 0.0);
        pane.setRightAnchor(this.root, 0.0);
        pane.setBottomAnchor(this.root, 0.0);

        pane.getChildren().add(this.root);
    }

    public GamePane(int difficulty){

        game = new Game(difficulty);

        pane = new AnchorPane();
        Group root3d = game.returnGameField();
        camera = setCamera();

        SubScene sub = new SubScene(root3d,1920,1080,true, SceneAntialiasing.BALANCED);
        sub.setCamera(camera);
        sub.setFill(Color.AQUAMARINE);
        sub.setEffect(gb);

        sub.heightProperty().bind(pane.heightProperty());
        sub.widthProperty().bind(pane.widthProperty());

        pane.setTopAnchor(sub, 0.0);
        pane.setLeftAnchor(sub, 0.0);
        pane.setRightAnchor(sub, 0.0);
        pane.setBottomAnchor(sub, 0.0);

        pane.getChildren().add(sub);
    }

    Game game;

    AnchorPane pane;
    Parent root;

    GaussianBlur gb = new GaussianBlur(30);

    PerspectiveCamera camera;
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private PerspectiveCamera setCamera(){
        camera = new PerspectiveCamera(true);

        Translate pivot = new Translate();
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

    public AnchorPane returnPane(){
        return pane;
    }

    public void clearPain(){
        if(root != null) {
            pane.getChildren().remove(root);
            root = null;
        }
    }

    public Game returnGame(){
        return game;
    }

    public void UnBlur(){
        Timeline unBlur = new Timeline();
        unBlur.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(gb.radiusProperty(), gb.getRadius())),

                new KeyFrame(new Duration(1500),
                        new KeyValue(gb.radiusProperty(), 0)));
        unBlur.play();
    }

    public void setRoot(Parent root){
        this.root = root;

        pane.setTopAnchor(this.root, 0.0);
        pane.setLeftAnchor(this.root, 0.0);
        pane.setRightAnchor(this.root, 0.0);
        pane.setBottomAnchor(this.root, 0.0);

        pane.getChildren().add(this.root);

    }

    public PerspectiveCamera getCamera(){
        return camera;
    }

    public Rotate getRotateY(){
        return rotateY;
    }
}

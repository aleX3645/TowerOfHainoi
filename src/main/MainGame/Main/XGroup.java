package main.MainGame.Main;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

/**
 * Группа в которой определены афинные преобразования.
 * */
public class XGroup extends Group {

    public XGroup() {
        super();
        getTransforms().add(new Affine());

    }

    public XGroup(Group g) {
        super(g);
        getTransforms().add(new Affine());
    }
}

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

class XGroup extends Group {

    XGroup() {
        super();
        getTransforms().add(new Affine());

    }

    XGroup(Group g) {
        super(g);
        getTransforms().add(new Affine());
    }


    public void addRotation(double angle, Point3D axis) {
        Rotate r = new Rotate(angle,700,500,0,axis);
        getTransforms().set(0, r.createConcatenation(getTransforms().get(0)));
    }

    public void reset() {

        getTransforms().set(0, new Affine());
    }
}

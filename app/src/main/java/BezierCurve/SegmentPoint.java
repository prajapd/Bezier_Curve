package BezierCurve;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SegmentPoint {
    private Circle sp;
    private boolean smooth;
    public SegmentPoint(Coordinates segPoint) {

        sp = new Circle();
        sp.setCenterX(segPoint.getX());
        sp.setCenterY(segPoint.getY());
        sp.setRadius(4);
        sp.setStroke(Color.WHITE);
        sp.setFill(Color.CORNFLOWERBLUE);
        smooth = true;

    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean s) {
        smooth = s;
    }

    public double getX() {
        return sp.getCenterX();
    }
    public double getY() {
        return sp.getCenterY();
    }
    public Circle getSp() {
        return sp;
    }
    public void bindControlPoint(ControlPoint cp) {
        sp.setOnMouseDragged(e -> {
            double x = cp.getCPX() - sp.getCenterX();
            double y = cp.getCPY() - sp.getCenterY();
            sp.setCenterX(e.getX());
            sp.setCenterY(e.getY());
            cp.getCp().setCenterX(e.getX() + x);
            cp.getCp().setCenterY(e.getY() + y);

        });
    }

    //will override the above one, for the intersecting points
    public void bindControlPoints(ControlPoint cp1, ControlPoint cp2) {
        sp.setOnMouseDragged(e -> {
            double x1 = cp1.getCPX() - sp.getCenterX();
            double y1 = cp1.getCPY() - sp.getCenterY();
            double x2 = cp2.getCPX() - sp.getCenterX();
            double y2 = cp2.getCPY() - sp.getCenterY();
            sp.setCenterX(e.getX());
            sp.setCenterY(e.getY());
            cp1.getCp().setCenterX(e.getX() + x1);
            cp1.getCp().setCenterY(e.getY() + y1);
            cp2.getCp().setCenterX(e.getX() + x2);
            cp2.getCp().setCenterY(e.getY() + y2);

        });

    }
}

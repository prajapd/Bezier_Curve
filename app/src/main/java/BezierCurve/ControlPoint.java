package BezierCurve;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ControlPoint {
    private Circle cp;
    private Line connector;
    private double smooth_x;
    private double smooth_y;
    public ControlPoint(SegmentPoint start, double x, double y) {
        cp = new Circle();
        cp.setCenterX(x);
        cp.setCenterY(y);
        cp.setRadius(3);
//        cp.setVisible(false);

        connector = new Line();
        connector.setStartX(start.getX());
        connector.setStartY(start.getY());
        connector.setEndX(x);
        connector.setEndY(y);
//        connector.setVisible(false);
        //binds the end of the line to be forever attacked to the control point radius
        connector.endXProperty().bind(cp.centerXProperty());
        connector.endYProperty().bind(cp.centerYProperty());
        connector.visibleProperty().bind(cp.visibleProperty());

        smooth_x = x;
        smooth_y = y;

    }
    public double getCPX() {
        return cp.getCenterX();
    }
    public double getCPY() {
        return cp.getCenterY();
    }
    public Circle getCp() {
        return cp;
    }
    public Line getConnector() {
        return connector;
    }

    public double getSmooth_x() {
        return smooth_x;
    }

    public double getSmooth_y() {
        return smooth_y;
    }

    public void setSmooth_x(double x) {
        smooth_x = x;
    }

    public void setSmooth_y(double smooth_y) {
        this.smooth_y = smooth_y;
    }

    public void bindPoints(SegmentPoint shared, ControlPoint cp2) {

        cp.setOnMouseDragged(e -> {
            cp.setCenterX(e.getX());
            cp.setCenterY(e.getY());
            cp2.getCp().setCenterX((e.getX() - shared.getSp().getCenterX())*(-1) + shared.getSp().getCenterX());
            cp2.getCp().setCenterY((e.getY() - shared.getSp().getCenterY())*(-1) + shared.getSp().getCenterY());
        });


    }
}

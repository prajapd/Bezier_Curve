package BezierCurve;

import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;

import java.util.Random;

public class Curve {
    private CubicCurve cc;
    private SegmentPoint start;
    private SegmentPoint end;
    private ControlPoint cp1;
    private ControlPoint cp2;
    private Paint paint = Color.BLACK;
    public Curve() {
    }
    public void InitializeStartSeg(Coordinates start) {
        this.start = new SegmentPoint(start);
    }
    public void InitializeEndSeg(Coordinates end) {
        this.end = new SegmentPoint(end);
    }

    public void InitializeCp1(double x, double y) {

        cp1 = new ControlPoint(start, x, y);
    }

    public void InitializeCp2() {
        int min = -25;
        int max = 25;
        double x = Math.random() * (max - min + 1) + min;
        double y = Math.random() * (max - min + 1) + min;
        if(x < 15 && x >= 0) {
            x = 15;
        } else if(x <= 0 && x > -15) {
            x = -15;
        }
        if(y < 15 && y >= 0) {
            y = 15;
        } else if(y <= 0 && y > -15) {
            y = -15;
        }
        cp2 = new ControlPoint(end, x + end.getX(), y + end.getY());

    }

    public void InitializeCurve() {

            int min = -25;
            int max = 25;
            double x = Math.random() * (max - min + 1) + min;
            double y = Math.random() * (max - min + 1) + min;
            if(x < 15 && x >= 0) {
                x = 15;
            } else if(x <= 0 && x > -15) {
                x = -15;
            }
            if(y < 15 && y >= 0) {
                y = 15;
            } else if(y <= 0 && y > -15) {
                y = -15;
            }
            cc = new CubicCurve();
            cc.setStartX(start.getX());
            cc.setStartY(start.getY());
            if(cp1 != null) {
                cc.setControlX1(cp1.getCPX());
                cc.setControlY1(cp1.getCPY());
            } else {
                cc.setControlX1(x + start.getX());
                cc.setControlY1(y + start.getY());
                cp1 = new ControlPoint(start, x + start.getX(), y + start.getY());
            }
            cc.setControlX2(cp2.getCPX());
            cc.setControlY2(cp2.getCPY());
            cc.setEndX(end.getX());
            cc.setEndY(end.getY());
            cc.setStroke(paint); // need to show only outline
            //cc.strokeWidth() // changes width
            //cc.strokeDashOffset() // different types of lines
            cc.setFill(Color.LIGHTGRAY); // need to set as background colour

        //connects the curve start and end to the segment point
        cc.startXProperty().bind(start.getSp().centerXProperty());
        cc.startYProperty().bind(start.getSp().centerYProperty());
        cc.endXProperty().bind(end.getSp().centerXProperty());
        cc.endYProperty().bind(end.getSp().centerYProperty());

        //connects control points to where the circle is defined on graph
        cc.controlX1Property().bind(cp1.getCp().centerXProperty());
        cc.controlY1Property().bind(cp1.getCp().centerYProperty());
        cc.controlX2Property().bind(cp2.getCp().centerXProperty());
        cc.controlY2Property().bind(cp2.getCp().centerYProperty());

        //connects the control point 1 line to start at the start segment
        cp1.getConnector().startXProperty().bind(start.getSp().centerXProperty());
        cp1.getConnector().startYProperty().bind(start.getSp().centerYProperty());

        //connects the control point 2 line to start at the end segment
        cp2.getConnector().startXProperty().bind(end.getSp().centerXProperty());
        cp2.getConnector().startYProperty().bind(end.getSp().centerYProperty());

        cp1.getCp().setOnMouseDragged(e -> {
            cp1.getCp().setCenterX(e.getX());
            cp1.getCp().setCenterY(e.getY());
        });
        cp2.getCp().setOnMouseDragged(e -> {
            cp2.getCp().setCenterX(e.getX());
            cp2.getCp().setCenterY(e.getY());
        });

        start.bindControlPoint(cp1);
        end.bindControlPoint(cp2);

        start.getSp().visibleProperty().bindBidirectional(end.getSp().visibleProperty());
    }

    public CubicCurve getCc() {
        return cc;
    }
    public SegmentPoint getStart() {
        return start;
    }

    public SegmentPoint getEnd() {
        return end;
    }
    public ControlPoint getCp1() { return cp1; }

    public ControlPoint getCp2() {
        return cp2;
    }

    public void setStrokeCc() {
        cc.setStroke(paint);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

}

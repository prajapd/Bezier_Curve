package BezierCurve;

import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private List<Curve> drawing;
    int LineStrokeWidth;
    int LineStrokeType;

    public Drawing() {
        drawing = new ArrayList<Curve>();
        LineStrokeWidth = 1;
        LineStrokeType = 1;
    }

    public List<Curve> getList() {
        return drawing;
    }

    public Curve getLastCurve() {
       return drawing.get(drawing.size() - 1);
    }

    public int getSize() {
        return drawing.size();
    }

    public void clearList() {
        drawing.clear();
    }

}

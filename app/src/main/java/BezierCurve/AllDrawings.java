package BezierCurve;

import java.util.ArrayList;
import java.util.List;

public class AllDrawings {
    private List<Drawing> allDrawings;
    public AllDrawings() {
        allDrawings = new ArrayList<Drawing>();
    }

    public void addDrawing(Drawing newDrawing) {
        allDrawings.add(newDrawing);
    }
    public int getSize() {
        return allDrawings.size();
    }

    public List<Drawing> getAllDrawings() {
        return allDrawings;
    }
}

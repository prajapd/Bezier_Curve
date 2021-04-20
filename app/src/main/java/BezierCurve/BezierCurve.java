/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package BezierCurve;


import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;


public class BezierCurve extends Application {

    public Coordinates start = null;
    public Coordinates end = null;
    public Drawing currDrawing = new Drawing();
    public Curve currCurve = null;
    public AllDrawings allDrawings = new AllDrawings();

    public static boolean draw = false;
    public boolean select = true;
    public boolean drawing_selected = false;
    public boolean point_tool = false; //can edit points
    public boolean eraser_tool = false;

    public static boolean cp_dragged = false;

    int drawing = -1;

    @Override
    public void start(Stage stage) {
        Group body = new Group();
        Scene scene = new Scene(body, 1200, 780, Color.LIGHTGRAY);

        Dialog<String> dialog = new Dialog<String>();
        ButtonType type = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.setTitle("About");
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.add(new Label("Program Name: Bezier Curve Drawing Tool"), 0, 0);
        grid.add(new Label("Name: Disa Prajapati"), 0, 1);
        grid.add(new Label("WatID: d4prajap"), 0, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().add(type);

        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(scene.widthProperty());

        Menu file = new Menu("File");
        MenuItem menuItem1 = new MenuItem("New");
        MenuItem menuItem2 = new MenuItem("Save");
        MenuItem menuItem3 = new MenuItem("Load");
        MenuItem menuItem4 = new MenuItem("Quit");

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setOnAction(e -> {
            dialog.showAndWait();
        });

        file.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
        help.getItems().add(about);
        menuBar.getMenus().addAll(file, help);


        ToolBar toolBar1 = new ToolBar();
        toolBar1.setOrientation(Orientation.VERTICAL);
        toolBar1.setPrefHeight(scene.getHeight()/2);
        toolBar1.setMaxWidth(160);
        toolBar1.setPrefWidth(160);
        body.getChildren().add(toolBar1);

        Button cursor = new Button();
        cursor.setPrefSize(60, 30);
        Image cursor_image = new Image("cursor.png");
        ImageView cursor_image_view = new ImageView(cursor_image);
        cursor_image_view.setFitHeight(20);
        cursor_image_view.setPreserveRatio(true);
        cursor.setGraphic(cursor_image_view);
        cursor.setOnMouseClicked(e -> {
            select = true;
            draw = false;
        });

        Button pen_tool = new Button("");
        pen_tool.setPrefSize(60, 30);
        Image pen_image = new Image("pen_tool.png");
        ImageView pen_image_view = new ImageView(pen_image);
        pen_image_view.setFitHeight(20);
        pen_image_view.setPreserveRatio(true);
        pen_tool.setGraphic(pen_image_view);


        Button eraser = new Button();
        eraser.setPrefSize(60, 30);
        Image eraser_image = new Image("eraser.png");
        ImageView eraser_image_view = new ImageView(eraser_image);
        eraser_image_view.setFitHeight(20);
        eraser_image_view.setPreserveRatio(true);
        eraser.setGraphic(eraser_image_view);

        Button curve = new Button();
        curve.setPrefSize(60, 30);
        Image curve_image = new Image("curve.png");
        ImageView curve_image_view = new ImageView(curve_image);
        curve_image_view.setFitHeight(20);
        curve_image_view.setPreserveRatio(true);
        curve.setGraphic(curve_image_view);
        curve.setDisable(true);

        pen_tool.setOnMouseClicked(e -> {
            draw = true;
            select = false;
            eraser_tool = false;
            point_tool = false;

            cursor.setDisable(true);
            eraser.setDisable(true);
            curve.setDisable(true);
        });

        cursor.setOnMouseClicked(e -> {
            draw = false;
            select = true;
            eraser_tool = false;
            point_tool = false;

            pen_tool.setDisable(true);
            eraser.setDisable(true);
            curve.setDisable(true);
        });

        eraser.setOnMouseClicked(e -> {
            draw = false;
            select = false;
            point_tool = false;
            eraser_tool = true;

            pen_tool.setDisable(false);
            cursor.setDisable(false);
            curve.setDisable(true);
        });

        curve.setOnMouseClicked(e -> {
            draw = false;
            select = true;
            point_tool = true;
            eraser_tool = false;

            cursor.setDisable(false);
            pen_tool.setDisable(true);
            eraser.setDisable(true);
        });

        HBox prop1 = new HBox(cursor, pen_tool);
        prop1.setSpacing(5);
        HBox prop2 = new HBox(eraser, curve);
        prop2.setSpacing(5);

        toolBar1.getItems().addAll(prop1, prop2);

        Separator s1 = new Separator(Orientation.HORIZONTAL);
        s1.maxWidthProperty().bind(toolBar1.maxWidthProperty());

        //need to figure out how to position on top of one another
        ToolBar toolBar2 = new ToolBar();
        toolBar2.setOrientation(Orientation.VERTICAL);
        toolBar2.setPrefHeight(scene.getHeight()/2);
        toolBar2.prefWidthProperty().bind(toolBar1.prefWidthProperty());
        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        colorPicker.setDisable(true);
        colorPicker.setOnAction(t -> {
            for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                temp.setPaint(colorPicker.getValue());
                temp.setStrokeCc(); //when a colour is picked while drawing is selected, it will change it to be this colour
            }
        });
        colorPicker.setMaxWidth(130);
        colorPicker.setStyle("-fx-padding-bottom: 10px");

        ToggleButton sw1 = new ToggleButton();
        sw1.maxWidthProperty().bind(colorPicker.maxWidthProperty());
        sw1.setPrefSize(130, 10);
        Image sw1_image = new Image("SW1.png");
        ImageView sw1_image_view = new ImageView(sw1_image);
        sw1_image_view.setFitHeight(20);
        sw1_image_view.setFitWidth(100);
        sw1.setGraphic(sw1_image_view);
        sw1.setDisable(true);

        ToggleButton sw2 = new ToggleButton();
        sw2.maxWidthProperty().bind(colorPicker.maxWidthProperty());
        sw2.prefHeightProperty().bind(sw1.prefHeightProperty());
        sw2.prefWidthProperty().bind(sw1.prefWidthProperty());
        Image sw2_image = new Image("SW2.png");
        ImageView sw2_image_view = new ImageView(sw2_image);
        sw2_image_view.setFitHeight(20);
        sw2_image_view.setFitWidth(100);
        sw2.setGraphic(sw2_image_view);
        sw2.setDisable(true);

        ToggleButton sw3 = new ToggleButton();
        sw3.maxWidthProperty().bind(colorPicker.maxWidthProperty());
        sw3.prefHeightProperty().bind(sw1.prefHeightProperty());
        sw3.prefWidthProperty().bind(sw1.prefWidthProperty());
        Image sw3_image = new Image("SW3.png");
        ImageView sw3_image_view = new ImageView(sw3_image);
        sw3_image_view.setFitHeight(20);
        sw3_image_view.setFitWidth(100);
        sw3.setGraphic(sw3_image_view);
        sw3.setDisable(true);

        ToggleGroup lineGroup = new ToggleGroup();
        sw1.setToggleGroup(lineGroup);
        sw2.setToggleGroup(lineGroup);
        sw3.setToggleGroup(lineGroup);

        Text line_thickness = new Text("line Thickness");
        VBox toggleLines = new VBox(line_thickness, sw1, sw2, sw3);
        VBox.setMargin(line_thickness, new Insets(20, 0, 0, 0));
        toggleLines.setSpacing(5);



        ToggleButton solid_line = new ToggleButton();
        solid_line.maxWidthProperty().bind(colorPicker.maxWidthProperty());
        solid_line.setPrefSize(130, 10);
        Image solid_image = new Image("SW1.png");
        ImageView solid_image_view = new ImageView(solid_image);
        solid_image_view.setFitHeight(20);
        solid_image_view.setFitWidth(100);
        solid_line.setGraphic(solid_image_view);
        solid_line.setDisable(true);

        ToggleButton dashed_line = new ToggleButton();
        dashed_line.maxWidthProperty().bind(colorPicker.maxWidthProperty());
        dashed_line.prefHeightProperty().bind(solid_line.prefHeightProperty());
        dashed_line.prefWidthProperty().bind(solid_line.prefWidthProperty());
        Image dashed_line_image = new Image("dashed_line.png");
        ImageView dashed_image_view = new ImageView(dashed_line_image);
        dashed_image_view.setFitHeight(20);
        dashed_image_view.setFitWidth(100);
        dashed_line.setGraphic(dashed_image_view);
        dashed_line.setDisable(true);

        ToggleButton dotted_line = new ToggleButton();
        dotted_line.maxWidthProperty().bind(colorPicker.maxWidthProperty());
        dotted_line.prefHeightProperty().bind(solid_line.prefHeightProperty());
        dotted_line.prefWidthProperty().bind(solid_line.prefWidthProperty());
        Image dotted_image = new Image("dotted_line.png");
        ImageView dotted_image_view = new ImageView(dotted_image);
        dotted_image_view.setFitHeight(20);
        dotted_image_view.setFitWidth(100);
        dotted_line.setGraphic(dotted_image_view);
        dotted_line.setDisable(true);

        ToggleGroup lineTypeGroup = new ToggleGroup();

        solid_line.setToggleGroup(lineTypeGroup);
        dashed_line.setToggleGroup(lineTypeGroup);
        dotted_line.setToggleGroup(lineTypeGroup);

        Text line_type = new Text("line Type");
        VBox toggleLinesType = new VBox(line_type, solid_line, dashed_line, dotted_line);
        VBox.setMargin(line_type, new Insets(20, 0, 0, 0));

        toolBar2.getItems().addAll(colorPicker, toggleLines, toggleLinesType);


        body.getChildren().add(toolBar2);


        VBox vBox = new VBox(menuBar, toolBar1, s1, toolBar2);
        body.getChildren().addAll(vBox);


        scene.setOnMouseClicked(e-> {
            if(draw) {
                    if (currDrawing.getSize() == 0) {
                        if (start == null) {
                            currCurve = new Curve();
                            start = new Coordinates(e.getX(), e.getY());
                            currCurve.InitializeStartSeg(start);
                            body.getChildren().add(currCurve.getStart().getSp());
                        } else {
                            end = new Coordinates(e.getX(), e.getY());
                            currCurve.getStart().getSp().setStroke(Color.CORNFLOWERBLUE);
                            currCurve.getStart().getSp().setFill(Color.WHITE);
                            currCurve.InitializeEndSeg(end);
                            currCurve.InitializeCp2();
                            currCurve.InitializeCurve();
                            body.getChildren().add(currCurve.getCc());
                            body.getChildren().add(currCurve.getCp1().getConnector());
                            body.getChildren().add(currCurve.getCp1().getCp());
                            body.getChildren().add(currCurve.getCp2().getConnector());
                            body.getChildren().add(currCurve.getCp2().getCp());
                            body.getChildren().add(currCurve.getEnd().getSp());
                            currDrawing.getList().add(currCurve);
                        }
                    } else {
                        boolean no_draw = false;
                        for(int i = 0; i < currDrawing.getSize(); i++) {
                            Curve temp = currDrawing.getList().get(i);
                            Point2D point = new Point2D(e.getX(), e.getY());
                            if (temp.getCp1().getCp().contains(point) || temp.getCp2().getCp().contains(point)) {
                                no_draw = true;
                            }
                        }
                        if(!no_draw) {
                            currCurve = new Curve();
                            start.setX(currDrawing.getLastCurve().getEnd().getX()); //prev curve ending point
                            start.setY(currDrawing.getLastCurve().getEnd().getY());

                            currDrawing.getLastCurve().getEnd().getSp().setStroke(Color.CORNFLOWERBLUE);
                            currDrawing.getLastCurve().getEnd().getSp().setFill(Color.WHITE);

                            currCurve.InitializeStartSeg(start);
                            //binding the "start" of the new curve to the "end" of the previous graph
                            currCurve.getStart().getSp().centerXProperty().bind(currDrawing.getLastCurve().getEnd().getSp().centerXProperty());
                            currCurve.getStart().getSp().centerYProperty().bind(currDrawing.getLastCurve().getEnd().getSp().centerYProperty());

                            end.setX(e.getX());
                            end.setY(e.getY());
                            currCurve.InitializeEndSeg(end);
                            double x = (currDrawing.getLastCurve().getCp2().getCPX() - start.getX()) * (-1) + start.getX();
                            double y = (currDrawing.getLastCurve().getCp2().getCPY() - start.getY()) * (-1) + start.getY();
                            currCurve.InitializeCp1(x, y);
                            currCurve.InitializeCp2();
                            currCurve.InitializeCurve();

                            currDrawing.getLastCurve().getEnd().bindControlPoints(currDrawing.getLastCurve().getCp2(), currCurve.getCp1());

                            currDrawing.getLastCurve().getCp2().bindPoints(currCurve.getStart(), currCurve.getCp1());
                            currCurve.getCp1().bindPoints(currCurve.getStart(), currDrawing.getLastCurve().getCp2());

                            body.getChildren().add(currCurve.getEnd().getSp());
                            body.getChildren().add(currCurve.getCp1().getConnector());
                            body.getChildren().add(currCurve.getCp1().getCp());
                            body.getChildren().add(currCurve.getCp2().getConnector());
                            body.getChildren().add(currCurve.getCp2().getCp());
                            body.getChildren().add(currCurve.getCc());

                            currDrawing.getList().add(currCurve);
                        }
                    }
                    for (int i = 0; i < currDrawing.getSize(); i++) {
                        Curve temp = currDrawing.getList().get(i);
                        temp.getStart().getSp().toFront();
                        temp.getEnd().getSp().toFront();
                    }
            } else if(select) {
                if(!drawing_selected) {
                    drawing = -1;
                    for (int i = 0; i < allDrawings.getSize(); i++) {
                        Drawing temp = allDrawings.getAllDrawings().get(i);
                        for (int j = 0; j < temp.getSize(); j++) {
                            Curve temp_c = temp.getList().get(j);
                            Point2D point = new Point2D(e.getX(), e.getY());
                            if (temp_c.getCc().contains(point)) {
                                drawing = i;
                                drawing_selected = true;
                            } else {
                                temp_c.getStart().getSp().setVisible(false);
                                temp_c.getCp1().getCp().setVisible(false);
                                temp_c.getCp2().getCp().setVisible(false);
                            }
                        }
                    }
                    if (drawing != -1) {
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().setStroke(Color.CORNFLOWERBLUE);
                            temp.getStart().getSp().setVisible(true);
                            temp.getCp1().getCp().setVisible(true);
                            temp.getCp2().getCp().setVisible(true);
                        }
                        curve.setDisable(false);
                        colorPicker.setDisable(false);

                        sw1.setDisable(false);
                        sw2.setDisable(false);
                        sw3.setDisable(false);
                        solid_line.setDisable(false);
                        dashed_line.setDisable(false);
                        dotted_line.setDisable(false);

                        if(allDrawings.getAllDrawings().get(drawing).LineStrokeWidth == 1) {
                            sw1.setSelected(true);
                        }
                        else if(allDrawings.getAllDrawings().get(drawing).LineStrokeWidth == 2) {
                            sw2.setSelected(true);
                        }
                        else {
                            sw3.setSelected(true);
                        }
                        if(allDrawings.getAllDrawings().get(drawing).LineStrokeType == 1) {
                            solid_line.setSelected(true);
                        }
                        else if(allDrawings.getAllDrawings().get(drawing).LineStrokeType == 2) {
                            dashed_line.setSelected(true);
                        }
                        else {
                            dotted_line.setSelected(true);
                        }
                        // everytime a drawing is selected, we set the stroke to cornflowerblue, but once it is deselected,
                        //we shoudl set it back to being the colour it was originally chosen
                        colorPicker.setValue(Color.web(allDrawings.getAllDrawings().get(drawing).getLastCurve().getPaint().toString()));
                    }
                    sw1.setOnMouseClicked(t -> {
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().setStrokeWidth(1);
                            allDrawings.getAllDrawings().get(drawing).LineStrokeWidth = 1;
                        }
                    });
                    sw2.setOnMouseClicked(t -> {
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().setStrokeWidth(2);
                            allDrawings.getAllDrawings().get(drawing).LineStrokeWidth = 2;
                        }
                    });
                    sw3.setOnMouseClicked(t -> {
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().setStrokeWidth(3);
                            allDrawings.getAllDrawings().get(drawing).LineStrokeWidth = 3;
                        }
                    });

                    solid_line.setOnMouseClicked(t -> {
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().getStrokeDashArray().removeAll(25d, 20d, 5d, 20d, 2d, 21d);
                            temp.getCc().getStrokeDashArray().addAll();
                        }
                        allDrawings.getAllDrawings().get(drawing).LineStrokeType = 1;
                    });
                    dashed_line.setOnMouseClicked(t -> {
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().getStrokeDashArray().removeAll(25d, 20d, 5d, 20d, 2d, 21d);
                        }
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().getStrokeDashArray().addAll(25d, 20d, 5d, 20d);
                        }
                        allDrawings.getAllDrawings().get(drawing).LineStrokeType = 2;
                    });

                    dotted_line.setOnMouseClicked(t -> {
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().getStrokeDashArray().removeAll(25d, 20d, 5d, 20d, 2d, 21d);
                        }
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getCc().getStrokeDashArray().addAll(2d, 21d);
                        }
                        allDrawings.getAllDrawings().get(drawing).LineStrokeType = 3;
                    });
                }
                else if(point_tool) {
                    Drawing temp = allDrawings.getAllDrawings().get(drawing);
                    Point2D point = new Point2D(e.getX(), e.getY());
                    for(int i = 0; i < temp.getSize(); i++) {
                        Curve temp_c = temp.getList().get(i);
                        if(temp_c.getStart().getSp().contains(point)) {
                            if(temp_c.getStart().isSmooth()) {
                                //control point x1 in curve is bound to cp1
                                temp_c.getCp1().setSmooth_x(temp_c.getCp1().getCPX());
                                temp_c.getCp1().setSmooth_y(temp_c.getCp1().getCPY());
                                temp_c.getCp1().getCp().setCenterX(temp_c.getStart().getX());
                                temp_c.getCp1().getCp().setCenterY(temp_c.getStart().getY());
                                temp_c.getCp1().getCp().setVisible(false);
                                temp_c.getStart().setSmooth(false);
                                temp_c.getStart().getSp().setStroke(Color.WHITE);
                                temp_c.getStart().getSp().setFill(Color.BLACK);
                            } else {
                                temp_c.getCp1().getCp().setCenterX(temp_c.getCp1().getSmooth_x());
                                temp_c.getCp1().getCp().setCenterY(temp_c.getCp1().getSmooth_y());
                                temp_c.getCp1().getCp().setVisible(true);
                                temp_c.getStart().getSp().setStroke(Color.CORNFLOWERBLUE);
                                temp_c.getStart().getSp().setFill(Color.WHITE);
                                temp_c.getStart().setSmooth(true);
                            }
                        } else if(temp_c.getEnd().getSp().contains(point)) {
                            if(temp_c.getEnd().isSmooth()) {
                                //control point x1 in curve is bound to cp1
                                temp_c.getCp2().setSmooth_x(temp_c.getCp2().getCPX());
                                temp_c.getCp2().setSmooth_y(temp_c.getCp2().getCPY());
                                temp_c.getCp2().getCp().setCenterX(temp_c.getEnd().getX());
                                temp_c.getCp2().getCp().setCenterY(temp_c.getEnd().getY());
                                temp_c.getCp2().getCp().setVisible(false);
                                temp_c.getEnd().setSmooth(false);
                                temp_c.getEnd().getSp().setStroke(Color.WHITE);
                                temp_c.getEnd().getSp().setFill(Color.BLACK);
                            } else {
                                temp_c.getCp2().getCp().setCenterX(temp_c.getCp2().getSmooth_x());
                                temp_c.getCp2().getCp().setCenterY(temp_c.getCp2().getSmooth_y());
                                temp_c.getCp2().getCp().setVisible(true);
                                temp_c.getEnd().getSp().setStroke(Color.CORNFLOWERBLUE);
                                temp_c.getEnd().getSp().setFill(Color.WHITE);
                                temp_c.getEnd().setSmooth(true);
                            }
                        }
                    }
                }
            } else if(eraser_tool) {
                int remove_drawing = -1;
                for (int i = 0; i < allDrawings.getSize(); i++) {
                    Drawing temp = allDrawings.getAllDrawings().get(i);
                    for (int j = 0; j < temp.getSize(); j++) {
                        Curve temp_c = temp.getList().get(j);
                        Point2D point = new Point2D(e.getX(), e.getY());
                        if (temp_c.getCc().contains(point)) {
                            remove_drawing = i;
                            i = allDrawings.getSize();
                            break;
                        }
                    }
                }
                if(remove_drawing > -1) {
                    for(int i = 0; i < allDrawings.getAllDrawings().get(remove_drawing).getSize(); i++) {
                        Curve temp = allDrawings.getAllDrawings().get(remove_drawing).getList().get(i);
                        body.getChildren().remove(temp.getCc());
                        body.getChildren().remove(temp.getStart().getSp());
                        body.getChildren().remove(temp.getEnd().getSp());
                        body.getChildren().remove(temp.getCp1().getCp());
                        body.getChildren().remove(temp.getCp1().getConnector());
                        body.getChildren().remove(temp.getCp2().getCp());
                        body.getChildren().remove(temp.getCp2().getConnector());
                    }
                    allDrawings.getAllDrawings().remove(remove_drawing);
                }

            }
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.ESCAPE && draw) {
                for(int i = 0; i < currDrawing.getSize(); i++) {
                    Curve temp = currDrawing.getList().get(i);
                    temp.getStart().getSp().setVisible(false);
                    temp.getCp1().getCp().setVisible(false);
                    temp.getCp2().getCp().setVisible(false);
                }
                allDrawings.addDrawing(currDrawing);
                currCurve.getEnd().getSp().setStroke(Color.CORNFLOWERBLUE);
                currCurve.getEnd().getSp().setFill(Color.WHITE);
                currDrawing = new Drawing();
                start = null;
                end = null;

                cursor.setDisable(false);
                eraser.setDisable(false);
                curve.setDisable(true);
                draw = false;
            } else if(select) {
                if(event.getCode() == KeyCode.ESCAPE) {
                    if(drawing_selected && point_tool) {
                        point_tool = false;
                    }
                    else if(drawing_selected) {
                        drawing_selected = false;
                        for (int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                            Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                            temp.getStart().getSp().setVisible(false);
                            temp.getCp1().getCp().setVisible(false);
                            temp.getCp2().getCp().setVisible(false);
                            temp.setStrokeCc(); //sets the stroke colour to be the one set by the user
                        }
                        drawing = -1;
                        curve.setDisable(true);
                        point_tool = false;
                        colorPicker.setDisable(true);
                        sw1.setDisable(true);
                        sw2.setDisable(true);
                        sw3.setDisable(true);
                        solid_line.setDisable(true);
                        dashed_line.setDisable(true);
                        dotted_line.setDisable(true);
                    }
                    else {
                        select = false;
                        pen_tool.setDisable(false);
                        eraser.setDisable(false);
                        curve.setDisable(true);
                    }
                }
                else if(event.getCode() == KeyCode.DELETE && drawing_selected) {
                    for(int i = 0; i < allDrawings.getAllDrawings().get(drawing).getSize(); i++) {
                        Curve temp = allDrawings.getAllDrawings().get(drawing).getList().get(i);
                        body.getChildren().remove(temp.getCc());
                        body.getChildren().remove(temp.getStart().getSp());
                        body.getChildren().remove(temp.getEnd().getSp());
                        body.getChildren().remove(temp.getCp1().getCp());
                        body.getChildren().remove(temp.getCp1().getConnector());
                        body.getChildren().remove(temp.getCp2().getCp());
                        body.getChildren().remove(temp.getCp2().getConnector());
                    }
                    allDrawings.getAllDrawings().remove(drawing);
                    drawing_selected = false;
                    drawing = -1;
                }
            }
        });
        stage.setTitle("Bezier Curve Drawing Tool d4prajap");
        stage.setScene(scene);
        stage.show();
    }
}
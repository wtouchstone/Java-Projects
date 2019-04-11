import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Paint;
import javafx.scene.SnapshotParameters;

/** @author Will Touchstone
 *  @version 1.0
 *  This is a program that lets you paint things.
 */
public class CSPaint extends Application {
    private double redVal, greenVal, blueVal;
    private Canvas canvas = new Canvas(650, 450);
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private double mousex = 0;
    private double mousey = 0;
    private int weight = 15;
    private double mouseDownX;
    private double mouseDownY;
    private Image tempSave = canvas.snapshot(new SnapshotParameters(), new WritableImage(650, 450));
    private StackPane stack = new StackPane();
    private RadioButton drawFree = new RadioButton("Draw");
    private RadioButton drawCircle = new RadioButton("Circle");
    private RadioButton drawErase = new RadioButton("Erase");
    private RadioButton drawRectangle = new RadioButton("Rectangle");
    private RadioButton drawText = new RadioButton("Text");
    private CheckBox hollow = new CheckBox("Draw hollow");
    private CheckBox hollowR = new CheckBox("Draw hollow");
    private CheckBox callig = new CheckBox("Calligraphy");
    private String textString = "";
    private BorderPane pane = new BorderPane();
    private int shapeCount = 0;
    private Slider weightSlider = new Slider(0.0, 100.0, 0.0);
    @Override
    public void start(Stage primaryStage) {
        layoutMaker();
        gc.setLineWidth(10);
        canvas.setOnMousePressed(e -> {
                mouseDownX = e.getX();
                mouseDownY = e.getY();
                tempSave = canvas.snapshot(new SnapshotParameters(), new WritableImage(650, 450));
            });
        canvas.setOnMouseReleased(e -> {
                if (drawCircle.isSelected()) {
                    Paint tempColor = gc.getFill();
                    if (hollow.isSelected()) {
                        double radius = radius(mouseDownX, mouseDownY, e.getX(), e.getY(), true);
                        gc.drawImage(tempSave, 0, 0);
                        radius -= 5;
                        gc.setStroke(new Color(redVal, greenVal, blueVal, 1.0));
                        gc.setFill(Color.TRANSPARENT);
                        gc.strokeOval(mouseDownX - radius, mouseDownY - radius, 2 * radius, 2 * radius);
                        gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                    } else {
                        double radius = radius(mouseDownX, mouseDownY, e.getX(), e.getY(), false);
                        gc.drawImage(tempSave, 0, 0);
                        gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                        gc.fillOval(mouseDownX - radius, mouseDownY - radius, 2 * radius, 2 * radius);
                    }
                    shapeCount++;
                    gc.setFill(tempColor);
                }
                if (drawRectangle.isSelected()) {
                    Paint tempColor = gc.getFill();
                    double[] coords = rectangleCoordFinder(mouseDownX, mouseDownY, e.getX(), e.getY());
                    if (hollowR.isSelected()) {
                        gc.drawImage(tempSave, 0, 0);
                        gc.setStroke(new Color(redVal, greenVal, blueVal, 1.0));
                        gc.setFill(Color.TRANSPARENT);
                        gc.strokeRect(coords[0], coords[1], coords[2] - 5, coords[3] - 5);
                        gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                    } else {
                        gc.drawImage(tempSave, 0, 0);
                        gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                        gc.fillRect(coords[0], coords[1], coords[2], coords[3]);
                    }
                    shapeCount++;
                }

            });
        canvas.setOnMouseDragged(e -> {
                if (drawErase.isSelected()) {
                    gc.beginPath();
                    gc.setFill(Color.WHITE);
                    gc.fillOval(e.getX() - weight / 2, e.getY() - weight / 2, weight, weight);
                    gc.closePath();
                }
                if (drawFree.isSelected()) {
                    gc.beginPath();
                    gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                    if (callig.isSelected()) {
                        gc.fillOval(e.getX() - weight / 8, e.getY() - weight / 2, weight / 4, weight);
                    } else {
                        gc.fillOval(e.getX() - weight / 2, e.getY() - weight / 2, weight, weight);
                    }
                    gc.closePath();
                }
                if (drawCircle.isSelected()) {
                    Paint tempFill = gc.getFill();
                    gc.setFill(new Color(.7, .7, .7, 0.6));
                    gc.setStroke(new Color(.7, .7, .7, 0.6));
                    gc.drawImage(tempSave, 0, 0);
                    if (hollow.isSelected()) {
                        double radius = radius(mouseDownX, mouseDownY, e.getX(), e.getY(), true);
                        radius -= 5;
                        gc.strokeOval(mouseDownX - radius, mouseDownY - radius, 2 * radius, 2 * radius);
                    } else {
                        double radius = radius(mouseDownX, mouseDownY, e.getX(), e.getY(), false);
                        gc.fillOval(mouseDownX - radius, mouseDownY - radius, 2 * radius, 2 * radius);
                    }
                    gc.setFill(tempFill);
                }
                if (drawRectangle.isSelected()) {
                    gc.setFill(new Color(.7, .7, .7, 0.6));
                    gc.setStroke(new Color(.7, .7, .7, 0.6));
                    gc.drawImage(tempSave, 0, 0);
                    double[] coords = rectangleCoordFinder(mouseDownX, mouseDownY, e.getX(), e.getY());
                    if (hollowR.isSelected()) {
                        gc.strokeRect(coords[0], coords[1], coords[2] - 5, coords[3] - 5);
                    } else {
                        gc.fillRect(coords[0], coords[1], coords[2], coords[3]);
                    }
                }
            });
        canvas.setOnKeyPressed(e -> {
                if (drawText.isSelected()) {
                    gc.fillText(e.getCharacter(), mouseDownX, mouseDownY);
//                textDrawer(textString);
                }
            });
        canvas.setOnMouseClicked(e -> {
                if (drawCircle.isSelected() && !hollow.isSelected()) {
                    gc.fillOval(mouseDownX - 7.5, mouseDownY - 7.5, 15, 15);
                }
            });
        Scene scene = new Scene(pane);
        primaryStage.setTitle("CSPaint"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.setResizable(false);
        primaryStage.show(); // Display the stage
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Here's how this works.");
        alert.setHeaderText("Instructions");
        alert.setContentText("There are multiple tools to select from. Selecting draw will allow the user to "
                + "free draw. Default weight is set to 2, unless Calligraphy is selected. When circle is selected, "
                + "on click it will generate a circle of given color with size 15. This can also be dragged to "
                + "create circles of varying sizes, including hollow ones. Rectangle can also be dragged. Erase's "
                + "default weight is set to 10, but can be modified with slider.");
        alert.showAndWait();
    }
    private VBox colorSliderMaker() {
        Slider redSlider = new Slider(0.0, 255.0, 0.0);
        redSlider.setShowTickMarks(true);
        redSlider.setMaxWidth(200);
        Slider blueSlider = new Slider(0.0, 255.0, 0.0);
        blueSlider.setShowTickMarks(true);
        blueSlider.setMaxWidth(200);
        Slider greenSlider = new Slider(0.0, 255.0, 0.0);
        greenSlider.setShowTickMarks(true);
        greenSlider.setMaxWidth(200);
        redVal = 0.0;
        greenVal = 0.0;
        blueVal = 0.0;
        VBox sliderBox = new VBox();
        Label redLabel = new Label("Red");
        Label greenLabel = new Label("Green");
        Label blueLabel = new Label("Blue");
        sliderBox.getChildren().addAll(redLabel, redSlider, greenLabel, greenSlider, blueLabel, blueSlider);
        Rectangle rect = new Rectangle(20, 20, 230, 170);
        rect.setFill(Color.BLACK);
        TextField colorName = new TextField("Enter a color");
        TextField hexCode = new TextField("Enter a Hex Code");
        redSlider.valueProperty().addListener(ov -> {
                redVal = redSlider.getValue() / 255.0;
                rect.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                String colorText = toHexString((int) (redVal * 255), (int) (greenVal * 255), (int) (blueVal * 255));
                hexCode.setText(colorText);
            });
        greenSlider.valueProperty().addListener(ov -> {
                greenVal = greenSlider.getValue() / 255.0;
                rect.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                String colorText = toHexString((int) (redVal * 255), (int) (greenVal * 255), (int) (blueVal * 255));
                hexCode.setText(colorText);
            });
        blueSlider.valueProperty().addListener(ov -> {
                blueVal = blueSlider.getValue() / 255.0;
                rect.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                String colorText = toHexString((int) (redVal * 255), (int) (greenVal * 255), (int) (blueVal * 255));
                hexCode.setText(colorText);
            });
        sliderBox.getChildren().add(rect);
        HBox textInput = new HBox();
        colorName.setMaxWidth(115);
        hexCode.setMaxWidth(115);
        textInput.getChildren().addAll(colorName, hexCode);
        colorName.setOnKeyPressed(e -> {
                if (colorName.getCharacters().toString().equals("Enter a color")) {
                    colorName.clear();
                }
            });
        hexCode.setOnKeyPressed(e -> {
                if (hexCode.getCharacters().toString().equals("Enter a Hex Code")) {
                    hexCode.clear();
                }
            });
        colorName.setOnAction(e -> {
                gc.setFill(Color.valueOf(colorName.getCharacters().toString()));
                rect.setFill(Color.valueOf(colorName.getCharacters().toString()));
                redSlider.setValue(Color.valueOf(colorName.getCharacters().toString()).getRed() * 255);
                greenSlider.setValue(Color.valueOf(colorName.getCharacters().toString()).getGreen() * 255);
                blueSlider.setValue(Color.valueOf(colorName.getCharacters().toString()).getBlue() * 255);
            });
        hexCode.setOnAction(e -> {
                String inString = hexCode.getCharacters().toString();
                try {
                    String redStr = inString.substring(0, 2);
                    String greenStr = inString.substring(2, 4);
                    String blueStr = inString.substring(4, 6);
                    redVal = (double) Integer.parseInt(redStr, 16) / 255;
                    greenVal = (double) Integer.parseInt(greenStr, 16) / 255;
                    blueVal = (double) Integer.parseInt(blueStr, 16) / 255;
                    gc.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                    rect.setFill(new Color(redVal, greenVal, blueVal, 1.0));
                    redSlider.setValue(redVal * 255.0);
                    greenSlider.setValue(greenVal * 255.0);
                    blueSlider.setValue(blueVal * 255.0);
                } catch (Exception exception) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Invalid Hex");
                    alert.setHeaderText("Invalid Hex");
                    alert.setContentText("The hex code you entered is invalid. Use the sliders, or try again.");
                    alert.showAndWait();
                }
            });
        sliderBox.getChildren().add(textInput);
        return sliderBox;
    }
    private void layoutMaker() {
        VBox toolsPane = new VBox();
        toolsPane.setStyle("-fx-border-color: black");
        pane.setLeft(toolsPane);
        stack.setStyle("-fx-border-color: black; -fx-background-color: white");
        stack.getChildren().add(canvas);
        pane.setRight(stack);
        ToggleGroup tools = new ToggleGroup();
        drawFree.setToggleGroup(tools);
        drawCircle.setToggleGroup(tools);
        drawErase.setToggleGroup(tools);
        drawRectangle.setToggleGroup(tools);
        drawText.setToggleGroup(tools);
        GridPane shapeSelect = new GridPane();
        shapeSelect.add(drawFree, 0, 0);
        shapeSelect.add(drawCircle, 0, 1);
        shapeSelect.add(drawErase, 0, 3);
        shapeSelect.add(hollow, 1, 1);
        shapeSelect.add(drawRectangle, 0, 2);
        shapeSelect.add(hollowR, 1, 2);
        //shapeSelect.add(drawText, 0,4); i just cant figure out how to actually do this
        shapeSelect.add(callig, 1, 0);
        VBox colorMenu = colorSliderMaker();
        pane.setBottom(bottomMaker());
        toolsPane.getChildren().add(shapeSelect);
        toolsPane.getChildren().add(weightSliderMaker());
        toolsPane.getChildren().add(colorMenu);
        shapeSelect.setHgap(5);
        shapeSelect.setVgap(5);
        toolsPane.setSpacing(5);
        drawErase.setOnAction(e -> {
                weightSlider.setValue(10);
                weight = 10;
            });
        drawFree.setOnAction(e -> {
                if (callig.isSelected()) {
                    weightSlider.setValue(4);
                    weight = 4;
                } else {
                    weightSlider.setValue(2);
                    weight = 2;
                }
            });
        drawCircle.setOnAction(e -> {
                weightSlider.setValue(15);
                weight = 15;
            });
        callig.setOnAction(e -> {
                if (callig.isSelected()) {
                    weightSlider.setValue(4);
                    weight = 4;
                } else {
                    weightSlider.setValue(2);
                    weight = 2;
                }
            });
    }
    private HBox bottomMaker() {
        HBox bottom = new HBox();
        HBox data = new HBox();
        Button clear = new Button("Clear Canvas");
        clear.setOnAction(e -> {
                clearCanvas();
            });
        bottom.setStyle("-fx-border-color: black");
        bottom.getChildren().add(clear);
        Label mouseLoc = new Label("0.00,   0.00");
        Label numShapes = new Label("Shapes drawn: 0");
        mouseLoc.setMaxWidth(100);
        mouseLoc.setMinWidth(100);
        numShapes.setMaxWidth(100);
        numShapes.setMinWidth(100);
        canvas.setOnMouseMoved(e -> {
                mousex = e.getX();
                mousey = e.getY();
                String str = String.format("%.2f,   %.2f", e.getX(), e.getY());
                mouseLoc.setText(str);
                numShapes.setText("Shapes drawn: " + shapeCount);
            });
        data.getChildren().addAll(mouseLoc, numShapes);
        data.setSpacing(50);
        bottom.getChildren().add(data);
        bottom.setSpacing(545);
        return bottom;
    }
    private void clearCanvas() {
        gc.clearRect(0, 0, 650, 450);
    }
    private HBox weightSliderMaker() {
        weightSlider = new Slider(0.0, 100.0, 0.0);
        weightSlider.setValue(0);
        weightSlider.setBlockIncrement(10);
        weightSlider.setShowTickMarks(true);
        weightSlider.setShowTickLabels(true);
        weightSlider.setMaxWidth(135);
        Label currWeight = new Label("    0");
        Label weightLabel = new Label("Weight: ");
        weightSlider.valueProperty().addListener(ov -> {
                weight = (int) weightSlider.getValue();
                currWeight.setText("    " + weight);
            });
        HBox returnable = new HBox();
        returnable.getChildren().addAll(weightLabel, weightSlider, currWeight);

        return returnable;
    }
    private String toHexString(int r, int g, int b) {
        String returnString = "";
        int[] arr = {r, g, b};
        for (int i : arr) {
            if (i < 16) {
                returnString += "0" + Integer.toHexString(i);
            } else {
                returnString += Integer.toHexString(i);
            }
        }
        return returnString;
    }
    private double radius(double mDX, double mDY, double mouseFinalX, double mouseFinalY, boolean stroke)
    {
        if (stroke) {
            double radius = (mDX - mouseFinalX) * (mDX - mouseFinalX)
                    + (mDY - mouseFinalY) * (mDY - mouseFinalY) + 50;
            radius = Math.pow(radius, .5);
            return radius;
        } else {
            double radius = (mDX - mouseFinalX) * (mDX - mouseFinalX)
                    + (mDY - mouseFinalY) * (mDY - mouseFinalY);
            radius = Math.pow(radius, .5);
            return radius;
        }
    }
    private double[] rectangleCoordFinder(double initx, double inity, double finx, double finy) {
        double[] returned = {0, 0, 0, 0};
        if (initx < finx && inity < finy) {
            double[] temp = {initx, inity, finx - initx, finy - inity};
            returned = temp;
        }
        if (initx > finx && inity < finy) {
            double[] temp = {finx, inity, initx - finx, finy - inity};
            returned = temp;
        }
        if (initx < finx && inity > finy) {
            double[] temp = {initx, finy, finx - initx, inity - finy};
            returned = temp;
        }
        if (initx > finx && inity > finy) {
            double[] temp = {finx, finy, initx - finx, inity - finy};
            returned = temp;
        }
        return returned;

    }

    /**
     * This is the main method. It launches the program.
     * @param args Args for the main method, not utilized in this program.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

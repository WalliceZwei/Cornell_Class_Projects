package MVC;

import ast.Action;
import cms.util.maybe.NoMaybeValue;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import model.Constants;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class View extends Application implements Initializable {

    /** Clears TaskQueue, adds a stopRunning Task and createNewWorld Task to Controller TaskQueue */
    @FXML
    private MenuItem newWorld;

    /** adds a loadWorld Task associated with a file path to Controller TaskQueue */
    @FXML
    private MenuItem loadWorld;

    /** is disabled when continuousStep button is toggled.
     * <br>Adds a loadCritterRandomly Task associated with a file path to Controller TaskQueue */
    @FXML
    private MenuItem randomCritter;

    /** disables {@link #advanceStep} when its toggled, adds a runContinuously/stopRunning Task to Controller TaskQueue  */
    @FXML
    private ToggleButton continuousStep;

    /** Adds a step Task to Controller TaskQueue */
    @FXML
    private Button advanceStep;

    /** Updates the view to show world info */
    @FXML
    private ToggleButton worldInfoToggle;

    /** Updates the view to show world info */
    @FXML
    private ToggleButton critterInfoToggle;

    @FXML
    private Slider speedSlider;

    @FXML
    private Text speedSliderDisplay;

    /**  */
    @FXML
    private Text fpsCounter;

    /** A function in View that Controller calls to update Text directly*/
    @FXML
    private Text stepsPerSecCounter;

    /** A function in View that Controller calls to update Text directly */
    @FXML
    private Text aliveCritters;

    /** A function in View that Controller calls to update Text directly */
    @FXML
    private Text timeElapsed;

    /** A function in View that Controller calls to update Text directly */
    @FXML
    private Text numFoodTiles;

    /** A function in View that Controller calls to update Text directly */
    @FXML
    private Text numRockTiles;

    /** Controller will update this text through a function */
    @FXML
    private Text memsizeText;
    /** Controller will update this text through a function */
    @FXML
    private Text defenseText;
    /** Controller will update this text through a function */
    @FXML
    private Text attackText;
    /** Controller will update this text through a function */
    @FXML
    private Text sizeText;
    /** Controller will update this text through a function */
    @FXML
    private Text energyText;
    /** Controller will update this text through a function */
    @FXML
    private Text passnumText;
    /** Controller will update this text through a function */
    @FXML
    private Text postureText;

    /** Controller will update this text through a function */
    @FXML
    private TextArea lastRuleDone;

    /** Initializes when user clicks on a critter hex tile.
     * <br>Controller will erase this text through a function once critter dies */
    @FXML
    private TextArea critterProgram;

    @FXML
    private GridPane worldGridPane;

    @FXML
    private GridPane critterGridPane;

    @FXML
    private ImageView backgroundImage;

    /**
     * Adds a 'Select Critter' task with a reference to the critter itself to TaskQueue
     * <br>Be able to select any hex tiles, only send event when selected hex tile is a critter
     */
    @FXML
    private Canvas canvas;

    private Affine canvasTransform;

    @FXML private Button recenterButton;

    @FXML
    private StackPane worldScroller;

    @FXML
    private MenuBar menuBar;

    /** This class should have an iterator that communicates to Controller the information in TaskQueue */
    private Queue<Task> TaskQueue;

    /**
     * Stores the instance of the model this View renders.
     * <br>Synchronized on {@link #canvas},
     * Controller writes to model via {@link #updateViewModel},
     * View reads from model via {@link #drawCanvas()}
     */
    private ReadOnlyWorld model;

    private boolean redrawCanvas;

    /** Shows the current selected Tile's coordinate */
    private int[] selectedTileCoords;

    /** The Hexagonal array of even rows used in rendering {@link #canvas} and detecting mouse click locations
     * <br>(col,row).
     * <br>Synchronized on View.
     * Controller writes via {@link #loadNewViewModel},
     * View reads via {@link #drawCanvas}
     */
    private Polygon[][] even;
    /** The Hexagonal array of odd rows used in rendering {@link #canvas} and detecting mouse click locations
     * <br>(col,row)
     * <br>Synchronized on View.
     * Controller writes via {@link #loadNewViewModel},
     * View reads via {@link #drawCanvas}
     */
    private Polygon[][] odd;

    /**
     * Stores the maximum X and Y values for the hexagons.
     * <br>Synchronized on View.
     * Controller writes via {@link #loadNewViewModel},
     * View reads via {@link #drawCanvas}
     */
    private double maxHexX,maxHexY;

    /** Maps each unique Critter Species String to a unique ID displayed on their back */
    private HashMap<String, Integer> critterSpeciesToID;
    /** The Critter Image file used to draw Critters onto the canvas */
    private Image critter;

    public static boolean disableMutation = false;

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("--disable-mutation")) {
                disableMutation = true;
                System.out.println("Mutation disabled.");
            }
        }
        launch(args);
    }


    @Override
    public void start(final Stage stage) {
        try {
            final URL r = getClass().getResource("/GUI.fxml");
            if (r == null) {
                System.err.println("No FXML resource found.");
                try {
                    stop();
                } catch (final Exception e) {
                }
            }
            final Parent node = FXMLLoader.load(r);
            final Scene scene = new Scene(node);
            stage.setTitle("WALLY'S WORLD");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
//            scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
//                System.out.println(event.getX() + "," + event.getY());
//            });
//            scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
//                System.out.println(scene.getWidth() + "," + scene.getHeight());
//            });
            stage.setMinWidth(Constants.MIN_STAGE_WIDTH);
            stage.setMinHeight(Constants.MIN_STAGE_HEIGHT);
        } catch (final IOException ioe) {
            System.err.println("Can't load FXML file.");
            ioe.printStackTrace();
            System.out.println(ioe);
            try {
                stop();
            } catch (final Exception e) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //todo implement. initializes everything, think of this as Constructor for any object
        TaskQueue = new LinkedList<>();
        model = null;
        critterSpeciesToID = new HashMap<>();
        critter = new Image(getClass().getResource("/images/critter.png").toString());

        canvasTransform = new Affine();

        redrawCanvas = false;

//        backgroundImage.fitWidthProperty().bind(worldScroller.widthProperty());
//        backgroundImage.fitHeightProperty().bind(worldScroller.heightProperty());
        worldScroller.widthProperty().addListener((obs, oldVal, newVal) -> {
            Rectangle2D currentViewport = backgroundImage.getViewport();
            double newWidth = newVal.doubleValue();
            double newHeight = worldScroller.getHeight();
            backgroundImage.setViewport(new Rectangle2D(
                    currentViewport.getMinX(),
                    currentViewport.getMinY(),
                    newWidth,
                    newHeight
            ));
        });

        backgroundImage.setPreserveRatio(true);
        backgroundImage.setViewport(new Rectangle2D(0,0,backgroundImage.getFitWidth(),backgroundImage.getFitHeight()));

        worldScroller.heightProperty().addListener((obs, oldVal, newVal) -> {
            Rectangle2D currentViewport = backgroundImage.getViewport();
            double newWidth = worldScroller.getWidth();
            double newHeight = newVal.doubleValue();
            backgroundImage.setViewport(new Rectangle2D(
                    currentViewport.getMinX(),
                    currentViewport.getMinY(),
                    newWidth,
                    newHeight
            ));
        });

        canvas.widthProperty().bind(worldScroller.widthProperty().add(Constants.CANVAS_PADDING * 2));
        canvas.heightProperty().bind(worldScroller.heightProperty().add(Constants.CANVAS_PADDING * 2));

        canvas.widthProperty().addListener(event->redrawCanvas=true);
        canvas.heightProperty().addListener(event->redrawCanvas=true);

        newWorld.setOnAction(event-> {
            addTask(new Task(Task.TaskType.CREATE_NEW_WORLD));
        });

        backgroundImage.fitWidthProperty().bind(canvas.widthProperty());
        backgroundImage.fitHeightProperty().bind(canvas.heightProperty());

        addTask(new Task(Task.TaskType.CREATE_NEW_WORLD));

        randomCritter.setOnAction(action -> {
            File file = openFileExplorer();
            if(file == null) return;
            Popup popup = new Popup();
            Label popupLabel = new Label("Choose the number of random critters to load");
            Button closePopupButton = new Button("Close");
            Button saveButton = new Button("Save");

            Slider slider = new Slider(0, model.getNumEmptyTiles(), 0);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.setMajorTickUnit(100);
            slider.setSnapToTicks(true);
            slider.setBlockIncrement(1);

            Label valueLabel = new Label("Num Critters Selected: ");
            TextField valueField = new TextField(String.valueOf ((int)slider.getValue()));

            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                //newValue is num of critters to load
                int roundedValue = (int) newValue.doubleValue();
                valueField.setText(String.valueOf(roundedValue));
            });

            valueField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value >= slider.getMin() && value <= slider.getMax()) {
                        slider.setValue(value);
                    } else {
                        valueField.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    valueField.setText("");
                }
            });

            closePopupButton.setOnAction(event -> {
                popup.hide();
                slider.setValue(0);
            });

            saveButton.setOnAction(event -> {
                popup.hide();
                System.out.println("LOADING "+slider.getValue()+" RANDOM CRITTERS ON FILE "+file.getAbsolutePath());
                addTask(new Task(Task.TaskType.LOAD_CRITTER_RANDOMLY,file.getAbsolutePath(),(int) slider.getValue()));
            });

            VBox popupContent = new VBox(10, popupLabel, slider, valueLabel, valueField, closePopupButton, saveButton);
            popupContent.setStyle("-fx-padding: 10; -fx-background-color: lightgray; -fx-border-color: black;");

            popup.getContent().add(popupContent);
            popup.show(menuBar.getScene().getWindow());
        });

        loadWorld.setOnAction(event -> {
            File file = openFileExplorer();
            if(file == null) return;
            addTask(new Task(Task.TaskType.LOAD_WORLD,file.getAbsolutePath()));
        });

        worldScroller.setOnScroll(ae -> {
            if (ae.getDeltaY() != 0) {  // Only react to vertical scroll
                double zoomFactor = ae.getDeltaY() > 0 ? 1.1 : 0.9;  // Zoom in or out
                if ((canvasTransform.getMxx() >= Constants.MAX_ZOOM / 1.1 || canvasTransform.getMyy() >= Constants.MAX_ZOOM / 1.1) && zoomFactor > 1)
                    return;
                if ((canvasTransform.getMxx() <= Constants.MIN_ZOOM / 0.9 || canvasTransform.getMyy() <= Constants.MIN_ZOOM / 0.9) && zoomFactor < 1)
                    return;

                Point2D mouseCoords;
                try{
                    mouseCoords = canvasTransform.inverseTransform(canvas.sceneToLocal(ae.getSceneX(), ae.getSceneY()));
                } catch (NonInvertibleTransformException e) {
                    throw new RuntimeException(e);
                }

                double oldScaleX = canvasTransform.getMxx(), oldScaleY = canvasTransform.getMyy();

                canvasTransform.setMxx(Math.clamp(oldScaleX * zoomFactor, Constants.MIN_ZOOM, Constants.MAX_ZOOM));
                canvasTransform.setMyy(Math.clamp(oldScaleY * zoomFactor, Constants.MIN_ZOOM, Constants.MAX_ZOOM));

                // Translate to maintain zoom focus on the mouse position
                canvasTransform.setTx(canvasTransform.getTx() - mouseCoords.getX() * (canvasTransform.getMxx() - oldScaleX));
                canvasTransform.setTy(canvasTransform.getTy() - mouseCoords.getY() * (canvasTransform.getMyy() - oldScaleY));

                redrawCanvas = true;
            }
        });

        final double[] dragAnchor = new double[2]; // To store initial mouse click position
        worldScroller.setOnMousePressed(ae -> {
            // Store initial mouse position for panning
            dragAnchor[0] = ae.getSceneX() - canvasTransform.getTx();
            dragAnchor[1] = ae.getSceneY() - canvasTransform.getTy();
        });

        worldScroller.setOnMouseDragged(ae -> {
            // Calculate new position for panning
            double offsetX = ae.getSceneX() - dragAnchor[0];
            double offsetY = ae.getSceneY() - dragAnchor[1];
            canvasTransform.setTx(offsetX);
            canvasTransform.setTy(offsetY);

            redrawCanvas = true;
        });

        worldScroller.setOnMouseReleased(ae -> {
            if (model == null) return;

            Point2D mouseCoords;
            try{
                mouseCoords = canvasTransform.inverseTransform(canvas.sceneToLocal(ae.getSceneX(), ae.getSceneY()));
            } catch (NonInvertibleTransformException e) {
                throw new RuntimeException(e);
            }

            redrawCanvas = true;

            for (int x = 0; x < model.getWidth(); x++) {
                Polygon[] column = (x % 2 == 0) ? even[x / 2] : odd[x / 2];
                for (int i = 0; i < column.length; i++) {
                    Polygon hexagon = column[i];
                    if (!hexagon.contains(mouseCoords)) continue;

                    int y = (x % 2 == 0) ? 2 * i : 2 * i + 1;
                    selectedTileCoords = new int[]{x,y};
                    updateSelectedCritterTile();

                    if (!continuousStep.isSelected() && ae.isPopupTrigger() && model.getTerrainInfo(selectedTileCoords[0], selectedTileCoords[1]) == 0){
                        ContextMenu contextMenu = new ContextMenu();

                        MenuItem item1 = new MenuItem("Load Critter");

                        contextMenu.getItems().add(item1);

                        item1.setOnAction(e -> {
                            File file = openFileExplorer();
                            if(file==null) return;
                            addTask(new Task(Task.TaskType.LOAD_CRITTER_SELECTIVELY,file.getAbsolutePath(),selectedTileCoords[0],selectedTileCoords[1]));
                        });
                        contextMenu.show(canvas.getScene().getWindow(), ae.getScreenX(), ae.getScreenY());
                    }

                    return;
                }
            }

            selectedTileCoords = null;
        });

        recenterButton.setOnAction(event -> {
            canvasTransform.setMxx(1);
            canvasTransform.setMyy(1);
            canvasTransform.setTx(0);
            canvasTransform.setTy(0);

            redrawCanvas = true;
        });

        critterGridPane.setVisible(false);
        worldInfoToggle.setSelected(true);
        critterInfoToggle.setDisable(true);

        speedSlider.setValue(100);
        speedSlider.setMax(1000);
        speedSliderDisplay.setText("100 steps/sec");
        speedSlider.valueProperty().addListener(num -> {
            synchronized (speedSlider){
                int simSpeed = (int) speedSlider.getValue();
                speedSliderDisplay.setText(simSpeed == 1000 ? "MAX SPEED" : simSpeed + " steps/sec");
            }
        });
        speedSlider.setOnMouseReleased(event -> {
            synchronized (speedSlider) {
                int simSpeed = (int) speedSlider.getValue();
                speedSliderDisplay.setText(simSpeed == 1000 ? "MAX SPEED" : simSpeed + " steps/sec");
            }
        });

        // starts the Controller Thread
        new Thread(new Controller(this)).start();

        // start the periodic drawCanvas Timeline
        Timeline updateCanvasPeriodically = new Timeline(new KeyFrame(
                Duration.seconds(1.0 / Constants.MAX_FPS),
                event -> {
                    if (model == null || !redrawCanvas) return;
                    drawCanvas();

                    updateSelectedCritterTile();

                    timeElapsed.setText(String.valueOf(model.getSteps()));
                    aliveCritters.setText(String.valueOf(model.getNumberOfAliveCritters()));

                    numFoodTiles.setText(String.valueOf(model.getNumberOfFood()));
                    numRockTiles.setText(String.valueOf(model.getNumberOfRocks()));
                }
        ));

        new AnimationTimer() {
            private long lastUpdate = 0;
            private int frameCount = 0;
            private long lastFrameTime = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    frameCount++;

                    // Update FPS every 0.2 seconds
                    if (now - lastFrameTime >= 200_000_000) { // 0.5 seconds = 500,000,000 nanoseconds
                        double fps = frameCount / ((now - lastFrameTime) / 1_000_000_000.0);
                        fpsCounter.setText(String.valueOf(((int)fps*100)/100.0));
                        frameCount = 0;
                        lastFrameTime = now;
                    }
                }
                lastUpdate = now;
            }
        }.start();

        updateCanvasPeriodically.setCycleCount(Timeline.INDEFINITE);
        updateCanvasPeriodically.play();
    }

    private void addTask(Task task) {
        synchronized (TaskQueue) {
            if(TaskQueue.size() > 100) return;
            TaskQueue.add(task);
        }
    }

    /** Allows the MVC Controller to remove 1 task from the TaskQueue. null if there is no Tasks */
    public Task pollTaskQueue() {
        synchronized (TaskQueue) {
            return TaskQueue.poll();
        }
    }

    /** Allows the MVC Controller to update text field */
    public void updateStepsPerSec(double stepsPerSec) {
        stepsPerSecCounter.setText(Double.toString(stepsPerSec));
    }

    /**
     * Allows the MVC Controller to poll simulation step speed.
     * @return any double from [0,1000], if returned value is 1000, run simulation as fast as possible */
    public int pollSimSpeed() {
        synchronized (speedSlider){
            return (int) speedSlider.getValue();
        }
    }

    /**
     * Updates the View's rendering Model according to {@code newTiles}, used by Controller.
     * <br> Different from {@link #loadNewViewModel}, requires that {@code newModel} has the same
     * dimensions as the previous model
     */
    public void updateViewModel(ReadOnlyWorld newModel) {
        assert newModel != null;
        synchronized (canvas){
            model = newModel;
            redrawCanvas = true;
            randomCritter.setDisable(continuousStep.isSelected() || model.getNumEmptyTiles() == 0);
        }
    }

    /**
     * Loads a completely new Model into the GUI.
     * <br>generates all Hex rendering and is a lot more resource intensive than {@link #updateViewModel}
     */
    public synchronized void loadNewViewModel(ReadOnlyWorld newModel) {
        assert newModel != null;

        continuousStep.setSelected(false);
        Platform.runLater(() -> handleContinuousStepPressed(new ActionEvent()));

        selectedTileCoords = null;
        int width = newModel.getWidth(), height = newModel.getHeight();

        even = new Polygon[(width + 1) / 2][(height + 1) / 2];
        odd = new Polygon[width / 2][height / 2];

        // Calculate the center position for the hexagon (in terms of screen coordinates)
        maxHexX = Integer.MIN_VALUE;
        maxHexY = Integer.MIN_VALUE;
        for (int x = 0; x < width; x++) {
            boolean isEven = (x % 2 == 0);
            Polygon[] column = isEven ? even[x / 2] : odd[x / 2];
            for (int y = 0; y < column.length; y++) {
                double centerX = Constants.HEX_RADIUS * (x * 1.5 + 1) + Constants.CANVAS_PADDING;  // Base horizontal position
                double centerY = Constants.HEX_RADIUS * ((isEven ? y * 2 : y * 2 + 1) * Math.sqrt(3) / 2 + 1) + Constants.CANVAS_PADDING;  // Base vertical position

                // Create the hexagon as a Polygon with 6 points
                Polygon hexagon = new Polygon();

                for (int j = 0; j < 6; j++) {
                    // Calculate the angle for each of the 6 vertices of the hexagon
                    double angle = Math.toRadians(60 * j);
                    double xOffset = centerX + Constants.HEX_RADIUS * Math.cos(angle);  // x offset for each vertex
                    double yOffset = centerY + Constants.HEX_RADIUS * Math.sin(angle);  // y offset for each vertex
                    hexagon.getPoints().addAll(xOffset, yOffset);  // Add the points to the Polygon
                    maxHexX = Math.max(maxHexX, xOffset);
                    maxHexY = Math.max(maxHexY, yOffset);
                } //x = (points[0] + points[6]) / 2. y = points[1]

                column[y] = hexagon;
            }
        }

        updateViewModel(newModel);
    }

    private void updateSelectedCritterTile() {
        if (selectedTileCoords!=null &&  model.getTerrainInfo(selectedTileCoords[0], selectedTileCoords[1]) > 0) {
            critterInfoToggle.setDisable(false);
            worldGridPane.setVisible(false);
            critterGridPane.setVisible(true);
            worldInfoToggle.setSelected(false);
            critterInfoToggle.setSelected(true);
            ReadOnlyCritter readOnlyCritter;
            try {
                readOnlyCritter = model.getReadOnlyCritter(selectedTileCoords[0], selectedTileCoords[1]).get();
            } catch (NoMaybeValue e) {
                throw new RuntimeException("Unexpected outcome whilst trying to obtain Maybe value");
            }

            int[] critterMem = readOnlyCritter.getMemory();
            memsizeText.setText(String.valueOf(critterMem[0]));
            defenseText.setText(String.valueOf(critterMem[1]));
            attackText.setText(String.valueOf(critterMem[2]));
            sizeText.setText(String.valueOf(critterMem[3]));
            energyText.setText(String.valueOf(critterMem[4]));
            passnumText.setText(String.valueOf(critterMem[5]));
            postureText.setText(String.valueOf(critterMem[6]));
            lastRuleDone.setText(readOnlyCritter.getLastRuleString()
                    .orElse("This Critter hasn't executed any rules yet..."));
            critterProgram.setText(readOnlyCritter.getProgramString());
        } else {
            critterInfoToggle.setDisable(true);
            worldInfoToggle.setSelected(true);
            critterGridPane.setVisible(false);
            worldGridPane.setVisible(true);
            critterInfoToggle.setSelected(false);
        }
    }

    /** A method that draws the Canvas according to {@link #even} and {@link #odd} */
    private synchronized void drawCanvas() {
        ReadOnlyWorld
                model;
        synchronized (canvas) {
            model = this.model;
        }

        double minX = Math.clamp(
                (Constants.CANVAS_PADDING - canvasTransform.getTx()) / canvasTransform.getMxx(),
                Constants.CANVAS_PADDING,
                maxHexX
        );
        double minY = Math.clamp(
                (Constants.CANVAS_PADDING - canvasTransform.getTy()) / canvasTransform.getMyy(),
                Constants.CANVAS_PADDING,
                maxHexY
        );
        double maxX = Math.clamp(
                (canvas.getWidth() - Constants.CANVAS_PADDING - canvasTransform.getTx()) / canvasTransform.getMxx(),
                Constants.CANVAS_PADDING,
                maxHexX
        );
        double maxY = Math.clamp(
                (canvas.getHeight() - Constants.CANVAS_PADDING - canvasTransform.getTy()) / canvasTransform.getMyy(),
                Constants.CANVAS_PADDING,
                maxHexY
        );

//      Create the bounding box
        Rectangle2D canvasCameraBoundingBox = new Rectangle2D(minX, minY, maxX - minX, maxY - minY);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        gc.save();
        gc.translate(canvasTransform.getTx(),canvasTransform.getTy());  // Translate first
        gc.scale(canvasTransform.getMxx(),canvasTransform.getMyy());  // Then apply scale

        gc.setStroke(Color.BLACK);
        for (int x = 0; x < model.getWidth(); x++) {
            Polygon[] hexes = (x % 2 == 0) ? even[x / 2] : odd[x / 2];

            for (int i = 0; i < hexes.length; i++) {
                Polygon hexagon = hexes[i];
                assert hexagon != null;

                if(!canvasCameraBoundingBox.intersects(
                        hexagon.getPoints().get(6),
                        hexagon.getPoints().get(9),
                        Constants.HEX_RADIUS*2,
                        Constants.HEX_RADIUS*2*Math.cos(Math.toRadians(30))
                )) continue;

                // Draw the hexagon on the Canvas
                double[] xPoints = new double[hexagon.getPoints().size() / 2];
                double[] yPoints = new double[hexagon.getPoints().size() / 2];

                // Fill xPoints and yPoints arrays
                for (int j = 0; j < hexagon.getPoints().size(); j += 2) {
                    xPoints[j / 2] = hexagon.getPoints().get(j);
                    yPoints[j / 2] = hexagon.getPoints().get(j + 1);
                }

                //draw tiles things
                int y = (x % 2 == 0) ? 2 * i : 2 * i + 1;
                int terrainInfo = model.getTerrainInfo(x, y);

                int midX = (int) (hexagon.getPoints().get(0) + hexagon.getPoints().get(6)) / 2;
                int midY = hexagon.getPoints().get(1).intValue();
                if (terrainInfo == -1) { //rock
                    gc.setFill(Color.GRAY);
                    gc.fillPolygon(xPoints, yPoints, 6);
                } else if (terrainInfo < -1) { //food
                    gc.setFill(Color.LIME);
                    gc.fillPolygon(xPoints, yPoints, 6);

                    Font font = Font.font("Arial", FontWeight.BOLD, 12);
                    gc.setFont(font);
                    Text helperText = new Text(String.valueOf(-terrainInfo - 1));
                    helperText.setFont(font);
                    gc.setFill(Color.BLACK);
                    gc.fillText(helperText.getText(), midX - helperText.getBoundsInLocal().getWidth() / 2, midY + helperText.getBoundsInLocal().getHeight() / 3);

                } else if (terrainInfo > 0) { //critter
                    gc.setFill(Color.WHITE);
                    gc.fillPolygon(xPoints, yPoints, 6);
                    ReadOnlyCritter readOnlyCritter;
                    try {
                        readOnlyCritter = model.getReadOnlyCritter(x, y).get();
                    } catch (NoMaybeValue ignored) {
                        throw new RuntimeException();
                    }

                    double weightedSize = Constants.getCritterLength(readOnlyCritter.getMemory()[3]);
                    int rotation = readOnlyCritter.getOrientation();

                    gc.save();
                    gc.translate(midX, midY);
                    gc.rotate(rotation * -60);
                    gc.drawImage(critter, -weightedSize / 2, -weightedSize / 2, weightedSize, weightedSize);
                    gc.restore();

                    if (!critterSpeciesToID.containsKey(readOnlyCritter.getSpecies()))
                        critterSpeciesToID.put(readOnlyCritter.getSpecies(), critterSpeciesToID.size());

                    Font font = Font.font("Arial", FontWeight.EXTRA_LIGHT, Constants.getCritterSpeciesFontSize(readOnlyCritter.getMemory()[3]));
                    Text helperText = new Text(String.valueOf(critterSpeciesToID.get(readOnlyCritter.getSpecies())));
                    helperText.setFont(font);
                    gc.setFont(font);

                    gc.setFill(Color.BLACK);
                    gc.fillText(helperText.getText(), midX - helperText.getBoundsInLocal().getWidth() / 2, midY + helperText.getBoundsInLocal().getHeight() / 3);
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillPolygon(xPoints, yPoints, 6);
                }

                // Draw the hexagon on the Canvas
                gc.strokePolygon(xPoints, yPoints, 6); // 6 points for a hexagon
            }
        }

        if(selectedTileCoords != null) {
            Polygon hexagon = (selectedTileCoords[0] % 2 == 0 ? even : odd)[selectedTileCoords[0] / 2][selectedTileCoords[1] / 2];

            double[] xPoints = new double[hexagon.getPoints().size() / 2];
            double[] yPoints = new double[hexagon.getPoints().size() / 2];

            // Fill xPoints and yPoints arrays
            for (int j = 0; j < hexagon.getPoints().size(); j += 2) {
                xPoints[j / 2] = hexagon.getPoints().get(j);
                yPoints[j / 2] = hexagon.getPoints().get(j + 1);
            }

            gc.setStroke(Color.RED);
            gc.strokePolygon(xPoints, yPoints, 6);
        }

        gc.restore();
        redrawCanvas = false;
    }

    @FXML
    private void handleWorldCritterTogglePressed(final ActionEvent e) {
        if (e.getSource() == worldInfoToggle) {
            worldInfoToggle.setSelected(true);
            critterGridPane.setVisible(false);
            worldGridPane.setVisible(true);
            critterInfoToggle.setSelected(false);
        } else if (e.getSource() == critterInfoToggle) {
            worldGridPane.setVisible(false);
            critterGridPane.setVisible(true);
            worldInfoToggle.setSelected(false);
        }
    }

    @FXML
    private void handleContinuousStepPressed(final ActionEvent e) {
        if (continuousStep.isSelected()) {
            addTask(new Task(Task.TaskType.RUN_CONTINUOUSLY));
            continuousStep.setText("Pause");
            advanceStep.setDisable(true);
            randomCritter.setDisable(true);
        } else {
            addTask(new Task(Task.TaskType.STOP_RUNNING));
            continuousStep.setText("Play");
            advanceStep.setDisable(false);
            randomCritter.setDisable(false);
        }
    }

    @FXML
    private void handleAdvanceStepPressed(final ActionEvent e) {
        addTask(new Task(Task.TaskType.STEP));
    }

    /** Uses a popup to prompt the user for a file, returns null if user closes the popup */
    private File openFileExplorer() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Window stage = menuBar.getScene().getWindow();

        //critter/world to be loaded
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection canceled.");
        }

        return selectedFile;
    }
}

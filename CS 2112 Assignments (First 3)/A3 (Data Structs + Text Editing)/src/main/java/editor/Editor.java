package editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Editor extends Application {

    private EditorModel editorModel;
    private List<Button> controls;
    private Text autocompletion;
    private Text timer;
    private Button searchButton;
    private TextField searchBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("2112 Editor");

        editorModel = new EditorModel();
        controls = new ArrayList<>();

        final HTMLEditor htmlEditor = new HTMLEditor();
        setupDictionary(htmlEditor, stage);
        setupSpellCheck(htmlEditor);
        setupClearFormatting(htmlEditor);
        setupAutocomplete(htmlEditor);
        getToolBar(htmlEditor).getItems().add(new Separator());

        setupSearch(htmlEditor);

        autocompletion = new Text();
        timer = new Text();

        VBox root = new VBox();
        BorderPane controlBar = new BorderPane();
        root.getChildren().add(htmlEditor);
        root.getChildren().add(controlBar);

        controlBar.setLeft(autocompletion);
        HBox searchSection = new HBox();
        searchSection.getChildren().add(searchButton);
        searchSection.getChildren().add(searchBox);
        searchSection.setAlignment(Pos.CENTER);
        controlBar.setCenter(searchSection);
        controlBar.setRight(timer);

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setupDictionary(final HTMLEditor htmlEditor, final Stage stage) {
        ToolBar bar = getToolBar(htmlEditor);
        ImageView graphic = new ImageView(new Image("dict.png", 24, 24, true, true));
        graphic.setEffect(new DropShadow());
        Button loadDict = new Button("", graphic);
        bar.getItems().add(loadDict);
        controls.add(loadDict);
        loadDict.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Open Dictionary File");
                        File dict = fileChooser.showOpenDialog(stage);
                        if (editorModel.loadDictionary(dict))
                            for (Button b : controls) {
                                b.setDisable(false);
                            }
                    }
                });
    }

    private void setupSpellCheck(final HTMLEditor htmlEditor) {
        ToolBar bar = getToolBar(htmlEditor);
        ImageView graphic = new ImageView(new Image("check.png", 24, 24, true, true));
        graphic.setEffect(new DropShadow());
        Button spellCheck = new Button("", graphic);
        spellCheck.setDisable(true);
        bar.getItems().add(spellCheck);
        controls.add(spellCheck);
        spellCheck.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        htmlEditor.setHtmlText(editorModel.spellCheck(htmlEditor.getHtmlText()));
                        timer.setText(
                                "Spell check time: " + editorModel.getSpellCheckTime() + " μs.");
                    }
                });
    }

    private void setupClearFormatting(final HTMLEditor htmlEditor) {
        ToolBar bar = getToolBar(htmlEditor);
        ImageView graphic = new ImageView(new Image("cancel.png", 24, 24, true, true));
        graphic.setEffect(new DropShadow());
        Button clear = new Button("", graphic);
        clear.setDisable(true);
        bar.getItems().add(clear);
        controls.add(clear);
        clear.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        htmlEditor.setHtmlText(
                                editorModel.clearFormatting(htmlEditor.getHtmlText()));
                    }
                });
    }

    private void setupAutocomplete(final HTMLEditor htmlEditor) {
        htmlEditor.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent e) {
                        autocompletion.setText(editorModel.autocomplete(htmlEditor.getHtmlText()));
                    }
                });
    }

    private void setupSearch(final HTMLEditor htmlEditor) {
        searchBox = new TextField();
        ImageView graphic = new ImageView(new Image("search.png", 24, 24, true, true));
        graphic.setEffect(new DropShadow());
        searchButton = new Button("", graphic);
        searchButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        htmlEditor.setHtmlText(
                                editorModel.search(searchBox.getText(), htmlEditor.getHtmlText()));
                    }
                });
        searchButton.setPrefHeight(searchBox.getHeight());
    }

    private ToolBar getToolBar(final HTMLEditor htmlEditor) {
        Node node = htmlEditor.lookup(".top-toolbar");
        return node instanceof ToolBar ? (ToolBar) node : null;
    }
}

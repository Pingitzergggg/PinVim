package pinvim.com.pinvim.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pinvim.com.pinvim.Model.Model;
import pinvim.com.pinvim.View.View;
import pinvim.com.pinvim.executor.Executor;
import pinvim.com.pinvim.helper.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller {
    private final FileChooser fileChooser = new FileChooser();
    private File lastKnownDirectory = new File(System.getProperty("user.home"));
    private Executor executor;
    private final View view = new View();
    private final Model model = new Model();
    private boolean commandMode = false;
    @FXML ScrollPane scrollpane;
    @FXML TextFlow codeView;
    @FXML Label fileName;
    @FXML Label state;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        scrollpane.setFocusTraversable(false);
        codeView.setFocusTraversable(true);
        model.getTab().setFile(new File("untitled.pin"));
        Platform.runLater(() -> codeView.requestFocus());
    }

    @FXML private Label codeSpace;

    @FXML
    public void openExecutor() throws IOException {
        if (model.getTab().getFile() != null && model.getTab().getFile().exists()) {
            saveFile();
            executor = new Executor(model.getTab().getFile());
        }
    }

    @FXML
    public void openHelp() throws IOException {
        Helper helper = new Helper(400, 400, "Test", Color.BLUE);
    }

    @FXML
    public void openFile() throws FileNotFoundException {
        fileChooser.setInitialDirectory(lastKnownDirectory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pingy script .pin", "*.pin"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            lastKnownDirectory = file.getParentFile();
            model.getTab().setFile(file);
            model.getTab().open();
            fileName.setText("Editing: "+file.getName());
            requestCodeBase();
            displayCurrentState();
        }
    }

    @FXML
    public void saveFile() throws IOException {
        if (model.getTab().getFile() == null || model.getTab().getFile().exists()) {
            model.getTab().write();
        } else {
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                model.getTab().setFile(file);
                model.getTab().write();
            }
        }
        displayCurrentState();
    }

    @FXML
    public void saveFileAs() throws IOException {
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            model.getTab().setFile(file);
            model.getTab().write();
            model.getTab().open();
            fileName.setText("Editing: "+file.getName());
            displayCurrentState();
        }
    }

    @FXML
    public void keyPressed(KeyEvent event) throws IOException {
        switch (event.getCode()) {
            case BACK_SPACE -> model.getTab().removeLastFromContent();
            case LEFT -> model.getTab().changeCursorHorizontal(-1);
            case RIGHT -> model.getTab().changeCursorHorizontal(+1);
            case CONTROL -> commandMode = true;
            case HOME -> model.getTab().bringCursorHome();
            case END -> model.getTab().bringCursorToEnd();
            case UP -> model.getTab().changeCursorVertical(-1);
            case DOWN -> model.getTab().changeCursorVertical(+1);
            case TAB -> {
                event.consume();
                model.getTab().appendContent(event.getText());
            }
            case S -> {
                if (commandMode) {
                    saveFile();
                } else {
                    model.getTab().appendContent(event.getText());
                }
            }
            case O -> {
                if (commandMode) {
                    openFile();
                } else {
                    model.getTab().appendContent(event.getText());
                }
            }
            case ENTER -> {
                if (commandMode) {
                    openExecutor();
                } else {
                    model.getTab().insertNewLine();
                    model.getTab().appendContent("\n");
                }
            }
            case ADD, PLUS, MINUS, SUBTRACT -> {
                if (commandMode) {
                    view.changeFontSize(
                            event.getCode() == KeyCode.ADD || event.getCode() == KeyCode.PLUS ?
                                    +1 : -1
                    );
                    model.getTab().changeCursorHorizontal(0);
                } else {
                    model.getTab().appendContent(event.getText());
                }
            }
            default -> model.getTab().appendContent(event.getText());
        }
        scrollpane.setVvalue(1.0);
        requestCodeBase();
        displayCurrentState();
    }

    @FXML
    public void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case CONTROL -> commandMode = false;
        }
    }

    @FXML
    public void focus() {
        codeView.requestFocus();
    }

    public void requestCodeBase() {
        codeView.getChildren().clear();
        Text[] content = view.getTextFlowContent(model.getTab().getContent());
        for (Text text : content) {
            codeView.getChildren().add(text);
        }
    }

    public void displayCurrentState() {
        if (model.getTab().checkForChanges()) {
            state.setText("*unsaved");
            state.setStyle("-fx-text-fill: red;");
        } else {
            state.setText("saved");
            state.setStyle("-fx-text-fill: green");
        }
    }
}

package pinvim.com.pinvim.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import pingy.Main;
import pinvim.com.pinvim.Model.ExecutorModel.ExecutorModel;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ExecutorController {
    private LinkedList<String> oldState;
    private Main interpreter;


    @FXML private Button terminateButton;
    @FXML private TextFlow outputDisplay;
    @FXML private Label title;

    public void execute(File file) {
        if (title != null) title.setText("Window bound to: "+file.getName()+" standard output");
        this.interpreter = new Main(file);
        interpreter.setDaemon(true);
        interpreter.start();
        detectOutputStreamChanges();
    }

    public void terminate() {
        interpreter.interrupt();
        Stage stage = (Stage) terminateButton.getScene().getWindow();
        stage.close();
    }

    private void detectOutputStreamChanges() {
        Thread printer = new Thread(() -> {
            ExecutorModel.init();
            boolean dumpBeforeTerminate = false;
            while (true) {
                if (!interpreter.isAlive() && !interpreter.isInterrupted()) dumpBeforeTerminate = true;
                if (interpreter.isInterrupted()) break;
                ExecutorModel.run();
                LinkedList<String> changedOutput = ExecutorModel.getChangedOutputState() == null ? new LinkedList<>() : ExecutorModel.getChangedOutputState();
                LinkedList<String> changedError = ExecutorModel.getChangedErrorState() == null ? new LinkedList<>() : ExecutorModel.getChangedErrorState();
                LinkedList<LinkedList<String>> streamCollection = new LinkedList<>(List.of(changedOutput, changedError));
                for (int i = 0; i < streamCollection.size(); ++i) {
                    LinkedList<String> collection = streamCollection.get(i);
                    if (!collection.isEmpty()) {
                        StringBuilder bldr = new StringBuilder();
                        for (String value : collection) {
                            bldr.append(value);
                        }
                        System.out.println("BLDR: "+bldr);
                        Text printable = new Text(bldr.toString());
                        printable.setStyle(i == 0 ? "-fx-text-fill: black;" : "-fx-text-fill: red;");
                        Platform.runLater(() -> outputDisplay.getChildren().add(printable));
                    }
                }
                if (dumpBeforeTerminate) break;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        printer.setDaemon(true);
        printer.start();
    }
}

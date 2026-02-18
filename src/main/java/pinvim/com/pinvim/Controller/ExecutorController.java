package pinvim.com.pinvim.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pingy.Main;
import pingy.Pool;

import java.io.File;
import java.util.LinkedList;

public class ExecutorController {
    private LinkedList<String> oldState;
    private Main interpreter;


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
    }

    private void detectOutputStreamChanges() {
        Thread printer = new Thread(() -> {
            Pool output = Pool.getInstance();
            System.out.println("THREAD WARNING: Old value"+output.getOutputStream());
            LinkedList<String> oldState = new LinkedList<>(output.getOutputStream());
            LinkedList<String> oldErrorState = new LinkedList<>(output.getErrorStream());
            while (true) {
                LinkedList<String> newState = new LinkedList<>(output.getOutputStream());
                LinkedList<String> newErrorState = new LinkedList<>(output.getErrorStream());
                System.out.println("THREAD WARNING: New value "+newState);
                System.out.println(oldState.size()+" "+newState.size());
                if (oldState.size() < newState.size()) {
                    System.out.println("THREAD WARNING CHANGE DETECTED");
                    Platform.runLater(() -> {
                        outputDisplay.getChildren().clear();
                        for (String printline : output.getOutputStream()) {
                            outputDisplay.getChildren().add(new Text(printline));
                        }
                    });
                    oldState = newState;
                }
                if (oldErrorState.size() < newErrorState.size()) {
                    Platform.runLater(() -> {
                        for (int i = newErrorState.size()-1;
                             i >= newErrorState.size()-1-(newErrorState.size()-oldErrorState.size());
                             --i) {
                                Text errorText = new Text(newErrorState.get(i));
                                errorText.setStyle("fx-text-fill: red;");
                                outputDisplay.getChildren().add(errorText);
                        }
                    });
                }
                if (!interpreter.isAlive()) {
                    System.out.println("THIS RAN DOWN");
                    Platform.runLater(() -> {
                        outputDisplay.getChildren().clear();
                        for (String printline : output.getOutputStream()) outputDisplay.getChildren().add(new Text(printline));
                        for (int i = 1; i < output.getErrorStream().size(); ++i) {
                            Text errorText = new Text(output.getErrorStream().get(i));
                            errorText.setStyle("-fx-text-fill: red;");
                            outputDisplay.getChildren().add(errorText);
                        }
                    });
                    break;
                }
            }
        });
        printer.setDaemon(true);
        printer.start();
    }
}

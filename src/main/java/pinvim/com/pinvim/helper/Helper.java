package pinvim.com.pinvim.helper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pinvim.com.pinvim.Controller.HelperController;

import java.io.IOException;

public class Helper {
    private final Stage stage;

    public Helper(double width, double height, String text, Color color) throws IOException {
        this.stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("helper.fxml"));
        Scene display = new Scene(loader.load(), width, height);
        stage.setTitle("peter alert");
        stage.setScene(display);
        stage.setResizable(false);
        stage.show();
    }
}

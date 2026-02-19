package pinvim.com.pinvim;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 666);
        stage.setTitle("PinVim ver.1.0!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("icon.png").toExternalForm()));
        stage.show();
    }
}

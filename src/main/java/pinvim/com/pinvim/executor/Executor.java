package pinvim.com.pinvim.executor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pinvim.com.pinvim.Controller.ExecutorController;

import java.io.File;
import java.io.IOException;

public class Executor {
    private final Stage stage;
    private final File file;

    public Executor(File file) throws IOException {
        this.stage = new Stage();
        this.file = file;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("executor.fxml"));
        Scene display = new Scene(loader.load(), 600, 300);
        ExecutorController controller = loader.getController();
        controller.execute(file);
        stage.setTitle("Run - "+file.getName());
        stage.setResizable(true);
        stage.setScene(display);
        stage.getIcons().add(new Image(getClass().getResource("icon.png").toExternalForm()));
        stage.show();
    }
}

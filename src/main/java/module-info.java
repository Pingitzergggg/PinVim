module pinvim.com.pinvim {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens pinvim.com.pinvim to javafx.fxml;
    exports pinvim.com.pinvim;
    exports pinvim.com.pinvim.Controller;
    opens pinvim.com.pinvim.Controller to javafx.fxml;
    exports pinvim.com.pinvim.debugger;
    opens pinvim.com.pinvim.debugger to javafx.fxml;
    exports pinvim.com.pinvim.executor;
    opens pinvim.com.pinvim.executor to javafx.fxml;
    exports pinvim.com.pinvim.helper;
    opens pinvim.com.pinvim.helper to javafx.fxml;
}
package pinvim.com.pinvim.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import static pinvim.com.pinvim.Model.GentleSplitter.gentleSplitter;

public class Model {
    private LinkedList<Tab> tabs;
    private int tabIndex;

    public Model() {
        this.tabs = new LinkedList<>();
        this.tabIndex = 0;
        tabs.add(new Tab());
    }

    public Tab getTab() {
        return tabs.get(tabIndex);
    }
}

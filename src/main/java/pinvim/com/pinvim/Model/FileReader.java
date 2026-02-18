package pinvim.com.pinvim.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class FileReader {
    private final File file;
    private final StringBuilder content = new StringBuilder();

    public FileReader(File file) throws FileNotFoundException {
        this.file = file;
        extract();
    }

    public void extract() throws FileNotFoundException {
        Scanner scn = new Scanner(file);
        while(scn.hasNextLine()) {
            content.append(scn.nextLine());
            content.append("\n");
        }
    }

    public String getContent() {return this.content.toString();}
}

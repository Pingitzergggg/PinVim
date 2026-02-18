package pinvim.com.pinvim.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import static pinvim.com.pinvim.Model.GentleSplitter.gentleSplitter;

public class Tab {
    private File file;
    private final char cursorChar = '\u00a6';
    private int[] cursor = {0, 0};
    private String originalInput;
    private final StringBuilder input = new StringBuilder();
    private LinkedList<Token> output;

    public Tab() {
        deployCursor();
        originalInput = "";
    }

    public void deployCursor() {
        if (input.indexOf(String.valueOf(cursorChar)) == -1) {
            cursor[0] = 0;
            cursor[1] = 0;
            input.insert(0, cursorChar);
            refresh();
        }
    }

    public void removeCursorCharacter() {
        if (input.indexOf(String.valueOf(cursorChar)) != -1) {
            input.deleteCharAt(input.indexOf(String.valueOf(cursorChar)));
        }
    }

    public void open() throws FileNotFoundException {
        if (file != null) {
            FileReader scn = new FileReader(file);
            originalInput = scn.getContent();
            setContent(scn.getContent());
            deployCursor();
            bringCursorToEnd();
        }
    }

    public void write() throws IOException {
        if (file != null) {
            FileWriter writer = new FileWriter(file);
            removeCursorCharacter();
            originalInput = input.toString();
            writer.write(input.toString());
            writer.close();
            deployCursor();
            bringCursorToEnd();
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {return this.file;}

    public void setContent(String content) {this.input.setLength(0);this.input.append(content);}
    public LinkedList<Token> getContent() {
        return this.output;
    }

    public void appendContent(String value) {
        if (value.equals(String.valueOf(cursorChar))) throw new IllegalArgumentException("\u00a6 character is used as cursor thus is invalid in this context!");
        if (!value.isEmpty()) {
            this.input.insert(cursor[0]++, value);
            if (value.equals("(")) this.input.insert(cursor[0] + 1, ")");
            if (value.equals("{")) this.input.insert(cursor[0] + 1, "}");
            refresh();
        }
    }

    public void removeLastFromContent() {
        if (cursor[0] > 0) {
            input.deleteCharAt(cursor[0]-1);
            cursor[0] = cursor[0] - 1 >= 0 ? cursor[0] - 1 : cursor[0];
            refresh();
        }
    }

    public void bringCursorHome() {
        input.deleteCharAt(cursor[0]);
        this.cursor[0] = 0;
        input.insert(cursor[0], cursorChar);
        refresh();
    }

    public void bringCursorToEnd() {
        input.deleteCharAt(cursor[0]);
        this.cursor[0] = input.length() - 1 > 0 ? input.length() - 1 : 0;
        input.insert(cursor[0], cursorChar);
        refresh();
    }

    public void changeCursorHorizontal(int amount) {
        if (cursor[0] + amount >= 0 && cursor[0] + amount < input.length()) {
            input.deleteCharAt(cursor[0]);
            this.cursor[0] += amount;
            input.insert(cursor[0], cursorChar);
            refresh();
        }
    }

    public void changeCursorVertical(int amount) {
        LinkedList<StringBuilder> lines = new LinkedList<>();
        removeCursorCharacter();
        for (String line : input.toString().split("\n")) lines.add(new StringBuilder(line));
        System.out.println(input);
        System.out.println(lines);
        System.out.println(cursor[1]);
        if (cursor[1] + amount < lines.size() && cursor[1] + amount >= 0) {
            cursor[1] += amount;
//            System.out.println("this ran");
        } else if (cursor[1] + amount < 0) {
            cursor[1] = 0;
//            System.out.println("this ran");
        } else {
            cursor[1] = lines.size()-1;
            System.out.println("this ran");
        }
        System.out.println(cursor[1]);
        input.setLength(0);
        for (int i = 0; i < lines.size(); ++i) {
            input.append(lines.get(i).toString());
            if (i == cursor[1]) input.append(cursorChar);
            input.append("\n");
        }
        cursor[0] = input.indexOf(String.valueOf(cursorChar));
        input.deleteCharAt(input.length()-1);
        refresh();
    }

    public void insertNewLine() {
        cursor[1]++;
        System.out.println("new line break "+cursor[1]);
    }

    public boolean checkForChanges() {
        if (originalInput != null) {
            if (input.indexOf(String.valueOf(cursorChar)) != -1){
                StringBuilder removedCursorFromContent = new StringBuilder(input);
                removedCursorFromContent.deleteCharAt(removedCursorFromContent.indexOf(String.valueOf(cursorChar)));
                return !file.exists() || !originalInput.contentEquals(removedCursorFromContent);
            } else {
                return !file.exists() || !originalInput.contentEquals(input);
            }
        } else {
            return false;
        }
    }

    private void refresh() {
        CodeBase codeBase = new CodeBase(gentleSplitter(input.toString(), Token.dividerPattern));
        this.output = codeBase.getContent();
    }
}

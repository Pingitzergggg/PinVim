package pinvim.com.pinvim.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CodeBase {
    private final LinkedList<String> codebase;
    private final LinkedList<Token> content = new LinkedList<>();

    public CodeBase(LinkedList<String> codebase) {
        this.codebase = codebase;
        keyWordExtractor();
    }

    private void keyWordExtractor() {
        for (String token : this.codebase) {
            Token extractToken = new Token(token);
            content.add(extractToken);
        }
    }

    public LinkedList<Token> getContent() {return this.content;}
}

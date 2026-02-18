package pinvim.com.pinvim.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
    public static final Pattern dividerPattern = Pattern.compile("^[+\\-*/%=<>!|&^{}();,\\s\\t]$");
    private static final String[] keywords = {
            "var", "if", "elif", "else", "while", "for", "in", "print", "true", "false", "loop", "with", "by", "check"
    };
    private static final String[] types = {"@bool", "@string", "@byte", "@short", "@int", "@float", "@double", "@long"};
    private static final Pattern operatorPattern = Pattern.compile("^[+\\-*/%=<>!|&^]$");
    private static final Pattern separatorPattern = Pattern.compile("^[{}();]$");
    private static final Pattern numericPattern = Pattern.compile("^[+\\-]?(0|0\\.0|([1-9][0-9]*(\\.[0-9]+)?))$");

    private TokenTypes type;
    private final String value;

    public Token(String value) {
        this.value = value;
        for (String type : types) {
            if (value.equals(type)) {
                this.type = TokenTypes.TYPE;
                return;
            }
        }
        for (String keyword : keywords) {
            if (value.equals(keyword)) {
                this.type = TokenTypes.KEYWORD;
                return;
            }
        }
        if (operatorPattern.matcher(value).matches()) {
            this.type = TokenTypes.OPERATOR;
        } else if (separatorPattern.matcher(value).matches()) {
            this.type = TokenTypes.SEPARATOR;
        } else if (numericPattern.matcher(value).matches()) {
            this.type = TokenTypes.NUMERIC;
        } else if (value.split("")[0].equals("\"")) {
            this.type = TokenTypes.STRING;
        } else if (value.equals("\u00a6")) {
            this.type = TokenTypes.CURSOR;
        } else {
            this.type = TokenTypes.DEFAULT;
        }
    }

    public TokenTypes getType() {return this.type;}
    public String getValue() {return this.value;}
}

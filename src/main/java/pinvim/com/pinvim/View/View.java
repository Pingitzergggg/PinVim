package pinvim.com.pinvim.View;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import pinvim.com.pinvim.Model.Token;
import pinvim.com.pinvim.Model.TokenTypes;

import java.util.LinkedList;

public class View {
    private float fontSize = 20;

    public Text[] getTextFlowContent(LinkedList<Token> tokens) {
        if (tokens == null) return new Text[0];
        Text[] coloredContent = new Text[tokens.size()];
        for (int i = 0; i < tokens.size(); ++i) {
            Text text = new Text(tokens.get(i).getValue());
            text.setFont(Font.font("ariel", this.fontSize));
            text.setFill(Color.web(getColorCodeForType(tokens.get(i).getType())));
            coloredContent[i] = text;
        }
        return coloredContent;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void changeFontSize(float fontSize) {
        this.fontSize += fontSize;
    }

    private String getColorCodeForType(TokenTypes type) {
        switch (type) {
            case KEYWORD -> {
                return  "#b812c4";
            }
            case VARIABLE -> {
                return  "#1280c4";
            }
            case TYPE -> {
                return "#74c412";
            }
            case OPERATOR -> {
                return "#636363";
            }
            case SEPARATOR -> {
                return "#4f230d";
            }
            case NUMERIC -> {
                return "#d90928";
            }
            case STRING -> {
                return "#1dd909";
            }
            default -> {
                return "#000000";
            }
        }
    }
}

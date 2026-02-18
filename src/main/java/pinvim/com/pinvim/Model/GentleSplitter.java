package pinvim.com.pinvim.Model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class GentleSplitter {

    public static LinkedList<String> gentleSplitter(String input, String symbol) {
        String[] charSequence = input.split("");
        StringBuilder block = new StringBuilder();
        LinkedList<String> output = new LinkedList<>();
        for (String character : charSequence) {
            if (character.equals(symbol)) {
                if (!block.isEmpty()) output.add(block.toString());
                output.add(character);
                block.setLength(0);
            } else {
                block.append(character);
            }
        }
        if (!block.isEmpty()) output.add(block.toString());
        return output;
    }

    public static LinkedList<String> gentleSplitter(String input, Pattern symbol) {
        String[] charSequence = input.split("");
        StringBuilder block = new StringBuilder();
        LinkedList<String> output = new LinkedList<>();
        for (int i = 0; i < charSequence.length; ++i) {
            if (symbol.matcher(charSequence[i]).matches()) {
                if (!block.isEmpty()) output.add(block.toString());
                output.add(charSequence[i]);
                block.setLength(0);
            } else if (charSequence[i].equals("\"")) {
                HashMap<String, Object> stringValue = extractStringLiterals(i, charSequence);
                if (stringValue != null) {
                    i = (int) stringValue.get("index");
                    output.add(stringValue.get("value").toString());
                } else {
                    block.append(charSequence[i]);
                }
            } else {
                block.append(charSequence[i]);
            }
        }
        if (!block.isEmpty()) output.add(block.toString());
        return output;
    }

    private static HashMap<String, Object> extractStringLiterals(int index, String[] sequence) {
        StringBuilder bldr = new StringBuilder();
        bldr.append("\"");
        for (int i = index+1; i < sequence.length; ++i) {
            if (sequence[i].equals("\"")) {
                HashMap<String, Object> result = new HashMap<>();
                bldr.append("\"");
                result.put("index", i);
                result.put("value", bldr.toString());
                return result;
            } else {
                bldr.append(sequence[i]);
            }
        }
        return null;
    }
}

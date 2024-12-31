import java.util.ArrayList;
import java.util.List;

import logicircuit.CmdProcessor;

public class Parser implements CmdProcessor {
    @FunctionalInterface
    public interface HandleTokensInterface {
        String handleTokensFunc(ArrayList<String> tokens);
    }

    private Character[] tokens = { ';', ' ', '(', ')', '{', '}', '[', ']', ',', '.', ':', '=', '+', '-', '*', '/', '%',
            '!', '<', '>', '&', '|', '^', '~', '?', '@', '#', '$', '\'', '"', '\\', '\n', '\t', '\r', '"' };
    private int tokenIndex = 0;
    public MainCircuit circuit;

    private HandleTokensInterface handleTokensInterface;

    public Parser(MainCircuit circuit) {
        this.circuit = circuit;
    }

    public void setHandleTokensInterface(HandleTokensInterface handleTokensInterface) {
        this.handleTokensInterface = handleTokensInterface;
    }

    private Character isAToken(char c) {
        for (int i = 0; i < tokens.length; i++) {
            if (c == tokens[i]) {
                return tokens[i];
            }
        }
        return '\0';
    }

    public String getStringAsString(String text) {
        String token = new String();
        int old_index = tokenIndex;

        int i = 1;
        Character current = text.charAt(old_index + i);

        while (current != '"' && old_index + i < text.length()) {
            token += text.charAt(old_index + i); // add char to token
            i++;
            if (old_index + i < text.length()) {
                current = text.charAt(old_index + i); // check if next char is a token
            } else {
                break;
            }
        }
        tokenIndex = old_index + i + 1;
        return token;
    }

    // ola;ola -> [ola, ;, ola]
    public String getToken(String text) {
        if (tokenIndex >= text.length()) {
            return "vazio";
        }

        String token = new String();
        int old_index = tokenIndex;

        int i = 0;
        Character current = isAToken(text.charAt(old_index + i));
        if (current == '"') {
            return getStringAsString(text);
        } else if (current != '\0') {
            tokenIndex = old_index + i + 1;
            return current.toString();
        }

        while (current == '\0' && old_index + i < text.length()) { // while not EOF and not a token

            token += text.charAt(old_index + i); // add char to token
            i++;
            if (old_index + i < text.length()) {
                current = isAToken(text.charAt(old_index + i)); // check if next char is a token
            } else {
                break;
            }
        }
        tokenIndex = old_index + i;

        return token;
    }

    public static List<List<String>> divideByChar(ArrayList<String> list, char separator) {
        List<List<String>> result = new ArrayList<>();
        List<String> currentSegment = new ArrayList<>();

        for (String item : list) {
            if (item.equals(String.valueOf(separator))) {
                result.add(new ArrayList<>(currentSegment));
                currentSegment.clear();
            } else {
                currentSegment.add(item);
            }
        }

        if (!currentSegment.isEmpty()) {
            result.add(new ArrayList<>(currentSegment));
        }

        return result;
    }

    public String process(String text) {
        text = text.toLowerCase();

        ArrayList<String> tokens = new ArrayList<String>();
        // get last char
        tokenIndex = 0;
        String token = getToken(text);
        while (!token.equals("vazio")) {

            // only add token if it is not empty
            if (!token.trim().isEmpty()) {
                tokens.add(token);
            }
            token = getToken(text);
        }

        List<List<String>> commands = divideByChar(tokens, ';');

        for (List<String> command : commands) {
            String err = handleTokensInterface.handleTokensFunc(new ArrayList<String>(command));
            if (!err.isEmpty()) {
                return err;
            }
        }
        return "";
    }
}

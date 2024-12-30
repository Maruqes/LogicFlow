import java.util.ArrayList;

import logicircuit.CmdProcessor;

public class Parser implements CmdProcessor {
    private Character[] tokens = { ';', ' ', '(', ')', '{', '}', '[', ']', ',', '.', ':', '=', '+', '-', '*', '/', '%',
            '!', '<', '>', '&', '|', '^', '~', '?' };
    private int tokenIndex = 0;

    private Character isAToken(char c) {
        for (int i = 0; i < tokens.length; i++) {
            if (c == tokens[i]) {
                return tokens[i];
            }
        }
        return '\0';
    }

    // ola;ola
    public String getToken(String text) {
        if (tokenIndex >= text.length()) {
            return "vazio";
        }

        String token = new String();
        int old_index = tokenIndex;

        int i = 0;
        Character current = isAToken(text.charAt(old_index + i));
        if (current != '\0') {
            tokenIndex = old_index + i + 1;
            return current.toString();
        }

        while (current == '\0' && old_index + i < text.length()) {

            token += text.charAt(old_index + i);
            i++;
            if (old_index + i < text.length()) {
                current = isAToken(text.charAt(old_index + i));
            } else {
                break;
            }
        }
        tokenIndex = old_index + i;

        return token;
    }

    // return error message
    private String handleTokens(ArrayList<String> tokens) {
        System.out.println(tokens.size());
        System.out.println(tokens);

        if (tokens.get(0).equals("add")) {
        } else if (tokens.get(0).equals("wire")) {
        } else if (tokens.get(0).equals("turn")) {
        } else if (tokens.get(0).equals("save")) {
        } else if (tokens.get(0).equals("open")) {
        } else {
            return "Comando não reconhecido";
        }
        return "";
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

        return handleTokens(tokens);
    }
}

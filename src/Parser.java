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

        while (current == '\0') {

            token += text.charAt(old_index + i);
            i++;
            current = isAToken(text.charAt(old_index + i));
        }
        tokenIndex = old_index + i;

        return token;
    }

    public String process(String text) {
        // get last char
        tokenIndex = 0;
        String token = getToken(text);
        while (!token.equals("vazio")) {
            if (!token.trim().isEmpty()) {
                System.out.println(token);
            }
            token = getToken(text);
        }
        return "";
    }
}

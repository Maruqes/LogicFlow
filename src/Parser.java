import java.security.cert.CertPathValidatorException.BasicReason;
import java.util.ArrayList;
import java.util.List;

import logicircuit.CmdProcessor;
import logicircuit.LCComponent;

public class Parser implements CmdProcessor {
    private Character[] tokens = { ';', ' ', '(', ')', '{', '}', '[', ']', ',', '.', ':', '=', '+', '-', '*', '/', '%',
            '!', '<', '>', '&', '|', '^', '~', '?', '@', '#', '$', '\'', '"', '\\', '\n', '\t', '\r' };
    private int tokenIndex = 0;
    private MainCircuit circuit;

    public Parser(MainCircuit circuit) {
        this.circuit = circuit;
    }

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

    private String addFunc(ArrayList<String> tokens) {
        if (tokens.size() < 8) {
            return "Error: Missing required parameters for add function";
        }
        String err = "";
        String nome = tokens.get(1);
        String tipoPorta = tokens.get(3);
        String cordX = tokens.get(5);
        String cordY = tokens.get(7);
        String legends = "";

        for (int i = 8; i < tokens.size(); i++) {
            legends += tokens.get(i) + " ";
        }

        int x = Integer.parseInt(cordX);
        int y = Integer.parseInt(cordY);

        try {
            LCComponent type = BasicComponent.getTypeWithComponent(tipoPorta);
            if (type == LCComponent.SWITCH) {
                circuit.add(type, false, nome, x, y, legends);
            } else if (type == LCComponent.BIT3_DISPLAY || type == LCComponent.LED) {
                circuit.add(type, 0, nome, x, y, legends);
            } else {
                circuit.add(type, nome, x, y, legends);
            }
            circuit.drawCircuit();
        } catch (Exception e) {
            err = e.getMessage();
        }
        return err;
    }

    private String wireFunc(ArrayList<String> tokens) {
        String err = "";
        if (tokens.size() < 4) {
            return "Error: Missing required parameters for wire function";
        }

        String from = tokens.get(1);
        String to = tokens.get(2);
        String pin = tokens.get(3);

        try {
            circuit.wire(from, to, Wire.getWithNome(pin));
            circuit.drawCircuit();
        } catch (Exception e) {
            err = e.getMessage();
        }
        return err;
    }

    private String turnFunc(ArrayList<String> tokens) {
        if (tokens.size() < 3) {
            return "Error: Missing required parameters for turn function";
        }
        String err = "";
        String onOff = tokens.get(1);
        String nome = tokens.get(2);

        try {
            err = circuit.turn(onOff, nome);
            circuit.drawCircuit();
        } catch (Exception e) {
            return e.getMessage();
        }
        return err;
    }

    // return error message
    private String handleTokens(ArrayList<String> tokens) {
        System.out.println(tokens.size());
        System.out.println(tokens);

        if (tokens.get(0).equals("add")) {
            return addFunc(tokens);
        } else if (tokens.get(0).equals("wire")) {
            return wireFunc(tokens);
        } else if (tokens.get(0).equals("turn")) {
            return turnFunc(tokens);
        } else if (tokens.get(0).equals("save")) {
        } else if (tokens.get(0).equals("open")) {
        } else {
            return "Comando não reconhecido";
        }
        return "";
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
            String err = handleTokens(new ArrayList<String>(command));
            if (!err.isEmpty()) {
                return err;
            }
        }
        return "";
    }
}

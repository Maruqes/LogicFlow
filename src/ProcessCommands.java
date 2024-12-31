import java.util.ArrayList;

import logicircuit.LCComponent;

public class ProcessCommands extends Parser {

    public ProcessCommands(MainCircuit circuit) {
        super(circuit, null);

        HandleTokensInterface handleTokensInterface = tokens -> {
            return handleTokens(tokens);
        };

        this.handleTokensInterface = handleTokensInterface;

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

    private String removeFunc(ArrayList<String> tokens) {
        if (tokens.size() < 2) {
            return "Error: Missing required parameters for turn function";
        }
        String err = "";
        String nome = tokens.get(1);

        try {
            err = circuit.removeElement(nome);
            circuit.drawCircuit();
        } catch (Exception e) {
            return e.getMessage();
        }
        return err;
    }

    private String moveFunc(ArrayList<String> tokens) {
        if (tokens.size() != 5) {
            return "Error: Missing required parameters for turn function";
        }
        String err = "";
        String nome = tokens.get(1);
        String x = tokens.get(2);
        String y = tokens.get(4);

        try {
            err = circuit.move(nome, Integer.parseInt(x), Integer.parseInt(y));
            circuit.drawCircuit();
        } catch (Exception e) {
            return e.getMessage();
        }
        return err;
    }

    private String handleOpen(ArrayList<String> tokens) {
        if (tokens.size() != 2) {
            return "Error: Missing required parameters for open function";
        }
        circuit.open(tokens.get(1));
        return "";
    }

    private String handleSave(ArrayList<String> tokens) {
        if (tokens.size() != 2) {
            return "Error: Missing required parameters for save function";
        }
        circuit.save(tokens.get(1));
        return "";
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
            handleSave(tokens);
        } else if (tokens.get(0).equals("open")) {
            handleOpen(tokens);
        } else if (tokens.get(0).equals("move")) {
            return moveFunc(tokens);
        } else if (tokens.get(0).equals("remove")) {
            return removeFunc(tokens);
        } else if (tokens.get(0).equals("tabela")) {
            circuit.printTabeldaDaVerdade();
        } else if (tokens.get(0).equals("animacaotabela")) {
            circuit.animacaoTabela();
        } else {
            return "Comando não reconhecido";
        }
        return "";
    }

}

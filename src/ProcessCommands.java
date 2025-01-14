import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class ProcessCommands extends Parser {

    private HashMap<String, HandleTokensInterface> commands;
    public MainCircuit circuit;

    public ProcessCommands(MainCircuit circuit) {
        this.circuit = circuit;

        HandleTokensInterface handleTokensInterface = tokens -> {
            return handleTokens(tokens);
        };
        super.setHandleTokensInterface(handleTokensInterface);

        commands = new HashMap<String, HandleTokensInterface>();

        HandleTokensInterface addFunc = (tokensVar) -> addFunc(tokensVar);
        commands.put("add", addFunc);

        HandleTokensInterface wireFunc = (tokensVar) -> wireFunc(tokensVar);
        commands.put("wire", wireFunc);

        HandleTokensInterface deWirefunc = (tokensVar) -> deWirefunc(tokensVar);
        commands.put("dewire", deWirefunc);

        HandleTokensInterface turnFunc = (tokensVar) -> turnFunc(tokensVar);
        commands.put("turn", turnFunc);

        HandleTokensInterface saveFunc = (tokensVar) -> handleSave(tokensVar);
        commands.put("save", saveFunc);

        HandleTokensInterface openFunc = (tokensVar) -> handleOpen(tokensVar);
        commands.put("open", openFunc);

        HandleTokensInterface moveFunc = (tokensVar) -> moveFunc(tokensVar);
        commands.put("move", moveFunc);

        HandleTokensInterface removeFunc = (tokensVar) -> removeFunc(tokensVar);
        commands.put("remove", removeFunc);

        HandleTokensInterface validateCircuitInter = (tokensVar) -> validateCircuit(tokensVar);
        commands.put("validatecircuit", validateCircuitInter);

        HandleTokensInterface undoFunc = (tokensVar) -> undoCircuit(tokensVar);
        commands.put("undo", undoFunc);

        HandleTokensInterface redoFunc = (tokensVar) -> redoCircuit(tokensVar);
        commands.put("redo", redoFunc);

        HandleTokensInterface printAll = (tokensVar) -> {
            circuit.printAllInfo();
            return "";
        };
        commands.put("printall", printAll);

        HandleTokensInterface tabelaFunc = (tokensVar) -> {
            circuit.printTabeldaDaVerdade();
            return "";
        };
        commands.put("tabela", tabelaFunc);

        HandleTokensInterface animacaoTabelaFunc = (tokensVar) -> {
            circuit.animacaoTabela();
            return "";
        };
        commands.put("animacaotabela", animacaoTabelaFunc);

        HandleTokensInterface clearFunc = (tokensVar) -> {
            circuit.clear();
            saveCurrentState();
            ProgCircuito.DRAW_ALL_STUFF(circuit);
            return "";
        };
        commands.put("clear", clearFunc);

        HandleTokensInterface screenWH = (tokensVar) -> {
            String comma = tokensVar.get(2);
            if (!comma.equals(",")) {
                return "Error: Missing comma between width and height";
            }

            int width = Integer.parseInt(tokensVar.get(1));
            int height = Integer.parseInt(tokensVar.get(3));

            if (width < 400 || height < 400) {
                return "Error: Screen width and height must be greater than 100";
            }

            try {
                ProgCircuito.restartProgram(tokensVar.get(1), tokensVar.get(3));
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
            return "";
        };
        commands.put("screen", screenWH);

    }

    private ArrayList<MainCircuit> redoHistory = new ArrayList<>();
    private ArrayList<MainCircuit> circuitsHistory = new ArrayList<>();

    public void saveCurrentState() {
        if (circuitsHistory == null) {
            circuitsHistory = new ArrayList<>();
        }
        circuitsHistory.add(circuit.clone());
        redoHistory.clear(); // limpa o redo
    }

    private String undoCircuit(ArrayList<String> tokens) {
        if (circuitsHistory == null || circuitsHistory.size() <= 1) {
            return "Error: No history to undo.";
        }

        redoHistory.add(circuit.clone()); // salva o estado atual para o redo

        // reverte
        circuitsHistory.remove(circuitsHistory.size() - 1); // remove o estado atual
        circuit = circuitsHistory.get(circuitsHistory.size() - 1); // vai pra o ultimo estado
        ProgCircuito.DRAW_ALL_STUFF(circuit);
        return "Undo successful.";
    }

    private String redoCircuit(ArrayList<String> tokens) {
        if (redoHistory.isEmpty()) {
            return "Error: No history to redo.";
        }

        circuitsHistory.add(circuit.clone()); // salva o atual para o undo

        circuit = redoHistory.remove(redoHistory.size() - 1); // vai para o ultimo estado do redo
        ProgCircuito.DRAW_ALL_STUFF(circuit);
        return "Redo successful.";
    }

    public void addComands(HashMap<String, HandleTokensInterface> commands) {
        this.commands.putAll(commands);
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

        int x = 0;
        int y = 0;

        try {
            x = Integer.parseInt(cordX);
            y = Integer.parseInt(cordY);
        } catch (NumberFormatException e) {
            return "Error: Invalid coordinates";
        }

        try {
            LCComponent type = BasicComponent.getTypeWithComponent(tipoPorta);
            if (type == LCComponent.SWITCH) {
                circuit.add(type, false, nome, x, y, legends);
            } else if (type == LCComponent.BIT3_DISPLAY || type == LCComponent.LED) {
                circuit.add(type, 0, nome, x, y, legends);
            } else {
                circuit.add(type, nome, x, y, legends);
            }
            saveCurrentState();
            ProgCircuito.DRAW_ALL_STUFF(circuit);
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
        LCInputPin pinL = Wire.getWithNome(pin);
        if (pinL == null) {
            return "Error: Invalid pin";
        }
        try {
            circuit.wire(from, to, pinL);
            saveCurrentState();
            ProgCircuito.DRAW_ALL_STUFF(circuit);
        } catch (Exception e) {
            err = e.getMessage();
        }
        return err;
    }

    private String deWirefunc(ArrayList<String> tokens) {
        String err = "";
        if (tokens.size() < 4) {
            return "Error: Missing required parameters for wire function";
        }

        String from = tokens.get(1);
        String to = tokens.get(2);
        String pin = tokens.get(3);

        LCInputPin pinL = Wire.getWithNome(pin);
        if (pinL == null) {
            return "Error: Invalid pin";
        }

        try {
            circuit.dewire(from, to, pinL);
            saveCurrentState();
            ProgCircuito.DRAW_ALL_STUFF(circuit);
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
            saveCurrentState();
            ProgCircuito.DRAW_ALL_STUFF(circuit);
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
            saveCurrentState();
            ProgCircuito.DRAW_ALL_STUFF(circuit);
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
        String Sx = tokens.get(2);
        String Sy = tokens.get(4);

        int x = 0;
        int y = 0;

        try {
            x = Integer.parseInt(Sx);
            y = Integer.parseInt(Sy);
        } catch (NumberFormatException e) {
            return "Error: Invalid coordinates";
        }

        try {
            err = circuit.move(nome, x, y);
            saveCurrentState();
            ProgCircuito.DRAW_ALL_STUFF(circuit);
        } catch (Exception e) {
            return e.getMessage();
        }
        return err;
    }

    private String handleOpen(ArrayList<String> tokens) {
        if (tokens.size() != 2) {
            return "Error: Missing required parameters for open function";
        }
        return circuit.open(tokens.get(1));
    }

    private String handleSave(ArrayList<String> tokens) {
        if (tokens.size() != 2) {
            return "Error: Missing required parameters for save function";
        }
        return circuit.save(tokens.get(1));
    }

    // return error message
    private String handleTokens(ArrayList<String> tokens) {
        System.out.println(tokens.size());
        System.out.println(tokens);

        if (tokens.size() == 0) {
            return "Error: Empty command";
        }

        if (commands != null && commands.containsKey(tokens.get(0))) {
            return commands.get(tokens.get(0)).handleTokensFunc(tokens);
        } else {
            return "Error: Command not found";
        }
    }

    private String validateCircuit(ArrayList<String> tokens) {
        return circuit.validateCircuit();
    }

}

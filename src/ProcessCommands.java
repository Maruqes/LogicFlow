import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

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

        HandleTokensInterface lsFiles = (tokensVar) -> lsfiles();
        commands.put("ls", lsFiles);

        HandleTokensInterface lsSharedFiles = (tokensVar) -> lsSharedFiles();
        commands.put("lsshared", lsSharedFiles);

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
            circuit.drawCircuit();
            return "";
        };
        commands.put("clear", clearFunc);

        HandleTokensInterface createMiniCircuit = (tokensVar) -> {
            String nameLegends = "";
            String filename = "";
            try {
                nameLegends = tokensVar.get(2);
                filename = tokensVar.get(1);
            } catch (Exception e) {
                return "Error: Missing name for mini circuit";
            }

            MainCircuit miniOpenFile = new MainCircuit();
            try {
                miniOpenFile.open(filename);

                MiniCircuit miniCircuit = new MiniCircuit(miniOpenFile.switches, miniOpenFile.components,
                        miniOpenFile.outputs, miniOpenFile.wires, nameLegends, nameLegends, filename);

                if (miniCircuit.validateCircuit() != "Circuito válido!") {
                    return "Error: Not valid Mini circuit must have only 1 output and max 8 switches";
                }

                System.out.println(miniCircuit.getOutput());
                miniCircuit.setPosition(500, 500);
                circuit.add_miniCircuit(miniCircuit);
                Main.DRAW_ALL_STUFF(circuit);
            } catch (Exception e) {
                miniOpenFile.clear();
                return "Error: Problem with mini circuit remeber to save the circuit with only 1 output and max 8 switches";
            }
            return "";
        };
        commands.put("mini", createMiniCircuit);

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
                Main.restartProgram(tokensVar.get(1), tokensVar.get(3));
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
            return "";
        };
        commands.put("screen", screenWH);

        HandleTokensInterface sendCircuit = (tokensVar) -> {
            try {
                String username = tokensVar.get(1);
                String circuitSend = tokensVar.get(2);

                return LoginRegisterPanel.sendCircuit(username, circuitSend);
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        };
        commands.put("send", sendCircuit);
    }

    private ArrayList<MainCircuit> redoHistory = new ArrayList<>();
    private ArrayList<MainCircuit> circuitsHistory = new ArrayList<>();

    public void saveCurrentState() {
        if (circuitsHistory == null) {
            circuitsHistory = new ArrayList<>();
        }
        circuitsHistory.add(circuit.cloneMainCircuit());
        redoHistory.clear(); // limpa o redo
    }

    private String undoCircuit(ArrayList<String> tokens) {
        if (circuitsHistory == null || circuitsHistory.size() <= 1) {
            return "Error: No history to undo.";
        }

        redoHistory.add(circuit.cloneMainCircuit()); // salva o estado atual para o redo

        // reverte
        circuitsHistory.remove(circuitsHistory.size() - 1); // remove o estado atual
        circuit = circuitsHistory.get(circuitsHistory.size() - 1); // vai pra o ultimo estado
        Main.DRAW_ALL_STUFF(circuit);
        return "Undo successful.";
    }

    private String redoCircuit(ArrayList<String> tokens) {
        if (redoHistory.isEmpty()) {
            return "Error: No history to redo.";
        }

        circuitsHistory.add(circuit.cloneMainCircuit()); // salva o atual para o undo

        circuit = redoHistory.remove(redoHistory.size() - 1); // vai para o ultimo estado do redo
        Main.DRAW_ALL_STUFF(circuit);
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
            Main.DRAW_ALL_STUFF(circuit);
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
            Main.DRAW_ALL_STUFF(circuit);
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
            Main.DRAW_ALL_STUFF(circuit);
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
            Main.DRAW_ALL_STUFF(circuit);
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
            Main.DRAW_ALL_STUFF(circuit);
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
            Main.DRAW_ALL_STUFF(circuit);
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

    private String lsfiles() {
        ArrayList<String> files = LoginRegisterPanel.ListFiles();
        // Criar a janela popup
        JDialog popup = new JDialog((Frame) null, "File Selector", true);
        popup.setSize(430, 300);
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Criar o painel principal para os botões
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5)); // GridLayout para alinhamento vertical
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espaçamento interno

        // Adicionar os botões dinamicamente
        if (files == null || files.isEmpty()) {
            JLabel noFilesLabel = new JLabel("No files available.");
            noFilesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noFilesLabel);
        } else {
            for (String file : files) {
                JButton fileButton = new JButton(file.trim());
                fileButton.setFocusPainted(false);
                fileButton.setBackground(new Color(240, 240, 240));
                fileButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                fileButton.addActionListener(e -> {
                    circuit.open(fileButton.getText());
                    popup.dispose(); // Fechar o popup após a seleção
                });
                panel.add(fileButton);
            }
        }

        // Adicionar o painel ao JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Adicionar o JScrollPane à janela popup
        popup.add(scrollPane, BorderLayout.CENTER);

        // Tornar a janela visível
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);

        return "";
    }

    private String lsSharedFiles() {
        ArrayList<String> files = LoginRegisterPanel.ListSharedFiles();
        // Criar a janela popup
        JDialog popup = new JDialog((Frame) null, "File Selector", true);
        popup.setSize(430, 300);
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Criar o painel principal para os botões
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5)); // GridLayout para alinhamento vertical
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espaçamento interno

        // Adicionar os botões dinamicamente
        if (files == null || files.isEmpty()) {
            JLabel noFilesLabel = new JLabel("No files available.");
            noFilesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noFilesLabel);
        } else {
            for (String file : files) {
                JButton fileButton = new JButton(file.trim());
                fileButton.setFocusPainted(false);
                fileButton.setBackground(new Color(240, 240, 240));
                fileButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                fileButton.addActionListener(e -> {

                    LoginRegisterPanel.AcceptCircuit(fileButton.getText());
                    popup.dispose(); // Fechar o popup após a seleção
                });
                panel.add(fileButton);
            }
        }

        // Adicionar o painel ao JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Adicionar o JScrollPane à janela popup
        popup.add(scrollPane, BorderLayout.CENTER);

        // Tornar a janela visível
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);

        return "";
    }

}

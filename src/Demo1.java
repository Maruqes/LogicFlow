
import logicircuit.*;

import java.util.HashMap;
import java.util.Map;

import static logicircuit.LCComponent.*;
import static logicircuit.LCInputPin.*;

public class Demo1 {
    private final LCDPanel drawPanel;
    // Variables to store the state of SWITCH d, E, F
    static boolean sd = false, se = false, sf = false;

    /**
     * Médod main
     */
    public static void main(String[] args) {
        new Demo1();
    }

    /**
     * Contrutor da class Demo1
     */
    private Demo1() {
        ProcessCommand processCommand = new ProcessCommand();
        LCDFrameCmd frame = new LCDFrameCmd(processCommand, "Demo de Logic Circuit", 900, 700);
        drawPanel = frame.drawPanel();
        // Create circuit2 in memory
        // createCircuit2();
    }

    /**
     * Classe que implementa a Interface LCDFrameCmd.CmdProcessor
     * para processar os commandos and develver, se necessário, mensagens de erro
     */
    class ProcessCommand implements CmdProcessor {
        // @Override
        public String process(String text) {
            return processCommand(text);
        }
    }

    /**
     * Método para processar os comandos
     */
    private String processCommand(String text) {
        int opcao = 0;
        boolean invalid = false;
        boolean isInteger;
        try {
            opcao = Integer.parseInt(text);
            isInteger = true;
        } catch (Exception e) {
            isInteger = false;
        }
        // Execute according to command
        if (isInteger) {
            switch (opcao) {
                case 1:
                    drawCircuit1();
                    break;
                case 2:
                    drawCircuit2();
                    break;
                case 3:
                    boolean actual = circuit.elems.get("led").state;
                    circuit.elems.get("switchC").state = actual;
                    circuit.connections.get("switchC-and2").state = actual;
                    circuit.connections.get("and2-or1").state = actual;
                    circuit.connections.get("or1-not1").state = actual;
                    circuit.connections.get("not1-led").state = !actual;
                    circuit.elems.get("led").state = !actual;
                    drawCircuit2();
                    break;
                default:
                    invalid = true;
            }
        } else { // not an integer
            invalid = false;
            String cmd = text.trim().toUpperCase();
            String[] tokens = cmd.split(" ");

            if (tokens.length >= 2) {
                if (tokens[1].equals("ON") || tokens[1].equals("OFF")) {
                    boolean isON = tokens[1].equals("ON");
                    if (tokens[0].equals("SD"))
                        sd = !sd;
                    else if (tokens[0].equals("SE"))
                        se = !se;
                    else if (tokens[0].equals("SF"))
                        sf = !sf;
                    else
                        invalid = true;
                } else
                    invalid = true;
            } else {
                invalid = true;
            }
            if (!invalid)
                changeCircuit2(sd ? 1 : 0, se ? 1 : 0, sf ? 1 : 0);
        }
        return invalid ? "Comandos validos: 1,2,3,SD ON,SD OFF,SE ON,SE off,SF on,SF OFF, exit" : null;
    }

    /**
     * Desenha o circuito exemplo 1
     */
    private void drawCircuit1() {
        // Clear logic canvas
        drawPanel.clear();
        // Draw switches (input)
        drawPanel.drawComponent(SWITCH, 10, 50, false, "switch01");
        drawPanel.drawComponent(SWITCH, 10, 150, true);
        drawPanel.drawComponent(SWITCH, 10, 330, true);
        // draw logic gates
        drawPanel.drawComponent(AND, 100, 100);
        drawPanel.drawComponent(AND, 220, 220, "AND e2");
        drawPanel.drawComponent(OR, 330, 330, "OR ou1");
        // Draw links
        drawPanel.drawWire(SWITCH, 10, 50, AND, 100, 100, PIN_A, false);
        drawPanel.drawWire(SWITCH, 10, 150, AND, 100, 100, PIN_B, true);
        drawPanel.drawWire(AND, 100, 100, AND, 220, 220, PIN_A, false);
        drawPanel.drawWire(SWITCH, 10, 330, AND, 220, 220, PIN_B, true);
        drawPanel.drawWire(SWITCH, 10, 330, OR, 330, 330, PIN_B, true);
        drawPanel.drawWire(AND, 220, 220, OR, 330, 330, PIN_A, false);
        // draw leds
        drawPanel.drawComponent(LED, 440, 220, false);
        drawPanel.drawComponent(LED, 440, 330, true);
        // Draw links
        drawPanel.drawWire(AND, 220, 220, LED, 440, 220, PIN_A, false);
        drawPanel.drawWire(OR, 330, 330, LED, 440, 330, PIN_A, true);
    }

    /**
     * Inner classes to store data on components and connections
     * Only for demonstration purpose
     * The source code is not well-structured
     */
    private static class Element {
        String id;
        LCComponent component;
        int x, y;
        boolean state;
        String legend;
        int value;

        public Element(String id, LCComponent component, int x, int y, boolean state, String legend, int value) {
            this.id = id;
            this.component = component;
            this.x = x;
            this.y = y;
            this.state = state;
            this.legend = legend;
            this.value = value;
        }
    }

    private static class Connection {
        String idInp;
        String idEnd;
        LCInputPin pin;
        boolean state;

        Connection(String idIni, String idEnd, LCInputPin pin, boolean state) {
            this.idInp = idIni;
            this.idEnd = idEnd;
            this.pin = pin;
            this.state = state;
        }
    }

    private static class Circuit {
        Map<String, Element> elems;
        Map<String, Connection> connections;

        Circuit() {
            this.elems = new HashMap<>();
            this.connections = new HashMap<>();
        }

        public void addConnection(String id1, String id2, LCInputPin inputPin, boolean state) {
            this.connections.put(id1 + "-" + id2, new Connection(id1, id2, inputPin, state));
        }
    }

    /**
     * Campo para aramzenar o circuito 2
     */
    private Circuit circuit = null;

    /**
     * Criar o circuito 2
     */
    private void createCircuit2() {
        circuit = new Circuit();
        // Components
        circuit.elems.put("switchB", new Element("switchB", SWITCH, 10, 50, false, "switchB", 0));
        circuit.elems.put("switchA", new Element("switchA", SWITCH, 10, 150, true, "switchA", 0));
        circuit.elems.put("switchC", new Element("switchC", SWITCH, 10, 250, false, "switchC", 0));
        circuit.elems.put("and1", new Element("and1", AND, 100, 100, false, null, 0));
        circuit.elems.put("and2", new Element("and2", AND, 100, 200, false, null, 0));
        circuit.elems.put("or1", new Element("or1", OR, 230, 150, false, null, 0));
        circuit.elems.put("not1", new Element("not1", NOT, 330, 150, false, null, 0));
        circuit.elems.put("led", new Element("led", LED, 430, 155, true, null, 0));
        circuit.elems.put("switchD", new Element("switchD", SWITCH, 200, 350, false, "switchD", 0));
        circuit.elems.put("switchE", new Element("switchE", SWITCH, 200, 450, false, "switchE", 0));
        circuit.elems.put("switchF", new Element("switchF", SWITCH, 200, 550, false, "switchF", 0));
        circuit.elems.put("bit3", new Element("bit3", BIT3_DISPLAY, 400, 450, true, null, 0));
        // Connections
        circuit.addConnection("switchB", "and1", PIN_A, false);
        circuit.addConnection("switchA", "and1", PIN_B, true);
        circuit.addConnection("switchA", "and2", PIN_A, true);
        circuit.addConnection("switchC", "and2", PIN_B, false);
        circuit.addConnection("and1", "or1", PIN_A, false);
        circuit.addConnection("and2", "or1", PIN_B, false);
        circuit.addConnection("or1", "not1", PIN_B, false);
        circuit.addConnection("not1", "led", PIN_B, true);
        circuit.addConnection("switchD", "bit3", PIN_A, false);
        circuit.addConnection("switchE", "bit3", PIN_B, false);
        circuit.addConnection("switchF", "bit3", PIN_C, false);
    }

    /**
     * Desenhar o circuito 2
     */
    private void drawCircuit2() {
        drawPanel.clear();
        ;
        for (Element el : circuit.elems.values()) {
            if (el.component == BIT3_DISPLAY)
                drawPanel.drawComponent(el.component, el.x, el.y, el.value);
            else if (el.component.hasState())
                drawPanel.drawComponent(el.component, el.x, el.y, el.state, el.legend);
            else
                drawPanel.drawComponent(el.component, el.x, el.y, el.legend);
        }
        for (Connection c : circuit.connections.values()) {
            Element inp = circuit.elems.get(c.idInp);
            Element end = circuit.elems.get(c.idEnd);
            drawPanel.drawWire(inp.component, inp.x, inp.y, end.component, end.x, end.y, c.pin, c.state);
        }

    }

    /**
     * Exemplo de alterar estado de um dos switchs ligados ao display
     * com a atualização das conexões e do display
     * 
     * @param d Define o estado do switch D: 0 para ON e OFF para restantes valores
     * @param e Define o estado do switch E: 0 para ON e OFF para restantes valores
     * @param f Define o estado do switch F: 0 para ON e OFF para restantes valores
     */
    private void changeCircuit2(int d, int e, int f) {
        Element elD = circuit.elems.get("switchD");
        Element elE = circuit.elems.get("switchE");
        Element elF = circuit.elems.get("switchF");
        Element bit3 = circuit.elems.get("bit3");

        drawPanel.drawComponent(elD.component, elD.x, elD.y, d != 0, elD.legend);
        drawPanel.drawWire(elD.component, elD.x, elD.y, bit3.component, bit3.x, bit3.y, PIN_A, d != 0);
        drawPanel.drawComponent(elE.component, elE.x, elE.y, e != 0, elE.legend);
        drawPanel.drawWire(elE.component, elE.x, elE.y, bit3.component, bit3.x, bit3.y, PIN_B, e != 0);
        drawPanel.drawComponent(elF.component, elF.x, elF.y, f != 0, elF.legend);
        drawPanel.drawWire(elF.component, elF.x, elF.y, bit3.component, bit3.x, bit3.y, PIN_C, f != 0);
        drawPanel.drawComponent(bit3.component, bit3.x, bit3.y, d * 4 + e * 2 + f);
    }
}

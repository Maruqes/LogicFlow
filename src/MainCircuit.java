import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.Timer;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class MainCircuit {
    private ArrayList<Switch> switches;
    private ArrayList<IOComponent> components;
    private ArrayList<OutputInterface> outputs; // leds and displays

    private ArrayList<Wire> wires;

    public MainCircuit() {
        switches = new ArrayList<Switch>();
        components = new ArrayList<IOComponent>();
        outputs = new ArrayList<OutputInterface>();
        wires = new ArrayList<Wire>();
    }

    /**
     * <b>VERIFY IF NAME IS REPEATED</b>
     * 
     * @param name Name to be verifyed if that is repeated.
     * @return {@code true} if some name is repeated or {@code false} if not.
     */
    private boolean block_same_names(String name) {
        for (int i = 0; i < switches.size(); i++) {
            if (switches.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        for (int i = 0; i < outputs.size(); i++) {
            if (outputs.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * <b>ADD A COMPONENT THAT HAS NO STATE</b>
     * 
     * @param cmp     Component type from LCComponent <b>AND, NAND, NOR, NOT, OR,
     *                XOR</b>.
     * @param nome    Component name.
     * @param x       Component coordinate <b>X</b>.
     * @param y       Component coordinate <b>Y</b>.
     * @param legenda Component legend.
     * 
     */
    public void add(LCComponent cmp, String nome, int x, int y, String legenda) {
        if (block_same_names(nome)) {
            throw new IllegalArgumentException("Name already exists");
        }
        if (cmp == LCComponent.AND) {
            And and1 = new And(nome, x, y, legenda);
            components.add(and1);
        } else if (cmp == LCComponent.NAND) {
            Nand nadn1 = new Nand(nome, x, y, legenda);
            components.add(nadn1);
        } else if (cmp == LCComponent.NOR) {
            Nor nor1 = new Nor(nome, x, y, legenda);
            components.add(nor1);
        } else if (cmp == LCComponent.NOT) {
            Not not1 = new Not(nome, x, y, legenda);
            components.add(not1);
        } else if (cmp == LCComponent.OR) {
            Or or1 = new Or(nome, x, y, legenda);
            components.add(or1);
        } else if (cmp == LCComponent.XOR) {
            Xor xor1 = new Xor(nome, x, y, legenda);
            components.add(xor1);
        } else {
            throw new IllegalArgumentException("Invalid value");
        }
    }

    /**
     * <b> ADD A COMPONENT THAT HAS STATE </b>
     * 
     * @param cmp    Component type from LCComponent <b>SWITCH</b>.
     * @param state  Component state defined with {@code true} or {@code false}.
     * @param nome   Component name.
     * @param setX   Component coordinate <b>X</b>.
     * @param setY   Component coordinate <b>Y</b>.
     * @param legend Component legend.
     */
    public void add(LCComponent cmp, boolean state, String nome, int setX, int setY, String legend) {
        if (block_same_names(nome)) {
            throw new IllegalArgumentException("Name already exists");
        }
        if (cmp == LCComponent.SWITCH) {
            Switch switch1 = new Switch(state, nome, setX, setY, legend);
            switches.add(switch1);
        } else {
            throw new IllegalArgumentException("Invalid value");
        }
    }

    /**
     * <b> ADD A COMPONENT THAT HAS STATE </b>
     * 
     * @param cmp    Component type from LCComponent <b>DISPLAY, LED</b>.
     * @param value  Component state defined whith {@code 1} or {@code 0}.
     * @param nome   Component name.
     * @param setX   Component coordinate <b>X</b>.
     * @param setY   Component coordinate <b>Y</b>.
     * @param legend Component legend.
     */
    public void add(LCComponent cmp, int value, String nome, int setX, int setY, String legend) {
        if (block_same_names(nome)) {
            throw new IllegalArgumentException("Name already exists");
        }
        if (cmp == LCComponent.BIT3_DISPLAY) {
            Display3bit display1 = new Display3bit(value, nome, setX, setY, legend);
            outputs.add(display1);
        } else if (cmp == LCComponent.LED) {
            Led led1 = new Led(value, nome, setX, setY, legend);
            outputs.add(led1);
        } else {
            throw new IllegalArgumentException("Invalid value");
        }
    }

    /**
     * <b>ADD A WIRE</b>
     * 
     * @param from Origin component name (Ex: <b>Switch1, Or9</b>).
     * @param to   Destination component name (Ex: <b>And1, Xor7</b>).
     * @param pin  Destination PIN {@code PIN_A, PIN_B, PIN_C}.
     *
     */
    public void wire(String from, String to, LCInputPin pin) {

        BasicComponentInterface fromType = null;
        BasicComponentInterface toType = null;

        for (int i = 0; i < switches.size(); i++) {
            if (switches.get(i).getName().equalsIgnoreCase(from)) {
                fromType = switches.get(i);
            }

            if (switches.get(i).getName().equalsIgnoreCase(to)) {
                toType = switches.get(i);
            }
        }

        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getName().equalsIgnoreCase(from)) {
                fromType = (BasicComponentInterface) components.get(i);
            }

            if (components.get(i).getName().equalsIgnoreCase(to)) {
                toType = (BasicComponentInterface) components.get(i);
            }
        }

        for (int i = 0; i < outputs.size(); i++) {
            if (outputs.get(i).getName().equalsIgnoreCase(from)) {
                fromType = (BasicComponentInterface) outputs.get(i);
            }

            if (outputs.get(i).getName().equalsIgnoreCase(to)) {
                toType = (BasicComponentInterface) outputs.get(i);
            }
        }

        if (fromType == null || toType == null) {
            throw new IllegalArgumentException("Invalid value for from or to");
        }
        if (fromType instanceof Led || fromType instanceof Display3bit) {
            throw new IllegalArgumentException("Invalid value for to");
        }
        if (toType instanceof Switch) {
            throw new IllegalArgumentException("Invalid value for to");
        }

        Wire wire = new Wire(fromType, toType, pin);

        wires.add(wire);

    }

    /**
     * <b>REMOVE A WIRE</b>
     * 
     * @param from Origin component name (Ex: <b>Switch1, Or9</b>).
     * @param to   Destination component name (Ex: <b>And1, Xor7</b>).
     * @param pin  Destination PIN {@code PIN_A, PIN_B, PIN_C}.
     *
     */
    public void dewire(String from, String to, LCInputPin pin) throws IllegalArgumentException {

        for (int i = 0; i < wires.size(); i++) {
            if (wires.get(i).getComponent1().getName().equalsIgnoreCase(from)
                    && wires.get(i).getComponent2().getName().equalsIgnoreCase(to)
                    && wires.get(i).getPin() == pin) {
                if (wires.get(i).getComponent2() instanceof OutputInterface) {
                    OutputInterface o = (OutputInterface) wires.get(i).getComponent2();
                    o.setNotUsedPin(pin);
                    wires.remove(i);
                    return;

                } else if (wires.get(i).getComponent2() instanceof IOComponent) {
                    IOComponent c = (IOComponent) wires.get(i).getComponent2();
                    c.setNotUsedPin(pin);
                    wires.remove(i);
                    return;
                } else {
                    throw new IllegalArgumentException("Invalid value or problem with dewire");
                }

            }
        }
        throw new IllegalArgumentException("Not found");
    }

    /**
     * <b>REMOVE THE WIRES THAT HAS CONNECTED TO SOME COMPONENT</b>
     * 
     * @param name Component name (Ex: <b>Switch1, Or9</b>) where wires gonna be
     *             disconnected.
     *
     */
    public void dewireElement(String name) throws IllegalArgumentException {

        for (int i = 0; i < wires.size(); i++) {
            if (wires.get(i).getComponent1().getName().equalsIgnoreCase(name)
                    || wires.get(i).getComponent2().getName().equalsIgnoreCase(name)) {
                dewire(wires.get(i).getComponent1().getName(), wires.get(i).getComponent2().getName(),
                        wires.get(i).getPin());
                i--;
            }
        }
    }

    /**
     * <b>SAVE THE CURRENT DIAGRAM IN A TEXT FILE</b>
     * 
     * @param filename File name to be saved (Ex:{@code filename.txt}).
     *
     */
    public String save(String filename) {
        if (filename.contains("/") || filename.contains("\\") || filename.contains("..")) {
            return "Error: Invalid filename";
        }
        try {
            Files.createDirectories(Paths.get("./saves"));
        } catch (IOException e) {
            return e.getMessage();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./saves/" + filename))) {
            for (Switch sw : switches) {
                writer.write(sw.Strigonize());
                writer.newLine();
            }
            for (IOComponent ic : components) {
                writer.write(ic.Strigonize());
                writer.newLine();
            }
            for (OutputInterface oi : outputs) {
                writer.write(oi.Strigonize());
                writer.newLine();
            }
            for (Wire w : wires) {
                writer.write(w.Strigonize());
                writer.newLine();
            }
        } catch (IOException e) {
            return e.getMessage();
        }
        return "";
    }

    /**
     * <b>OPEN A SAVED FILE IMAGE HAS DIAGRAM</b>
     * 
     * @param filename File name to be opened (Ex:{@code filename.txt}).
     *
     */
    public String open(String filename) {
        if (filename.contains("/") || filename.contains("\\") || filename.contains("..")) {
            return "Error: Invalid filename";
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("./saves/" + filename))) {
            while (reader.ready()) {

                // ler linha
                String aux = reader.readLine();
                // dividir a linha
                String[] res = aux.split("//");
                String nome = res[2];

                if (res[0].equals("Switch")) {

                    LCComponent cmp = BasicComponent.getTypeWithComponent(res[1]);
                    String state = res[3];
                    boolean stateBool = Boolean.parseBoolean(state);
                    int x = Integer.parseInt(res[4]);
                    int y = Integer.parseInt(res[5]);
                    String legend = "";

                    try {
                        legend = res[6];
                    } catch (Exception e) {
                    }

                    // adicionar elemento
                    if (cmp == LCComponent.SWITCH) {
                        add(LCComponent.SWITCH, stateBool, nome, x, y, legend);
                    }

                } else if (res[0].equals("Wire")) {
                    String from = res[1];
                    String to = res[2];
                    LCInputPin pin = Wire.getWithNome(res[3]);
                    System.out.println(from + " " + to + " " + pin);
                    wire(from, to, pin);
                } else if (res[0].equals("BasicCmp")) {
                    LCComponent cmp = BasicComponent.getTypeWithComponent(res[1]);
                    int x = Integer.parseInt(res[3]);
                    int y = Integer.parseInt(res[4]);
                    String legend = "";

                    try {
                        legend = res[5];
                    } catch (Exception e) {
                    }

                    // adicionar elemento
                    if (cmp == LCComponent.BIT3_DISPLAY || cmp == LCComponent.LED) {
                        add(cmp, 0, nome, x, y, legend);
                    } else {
                        add(cmp, nome, x, y, legend);
                    }
                }

            }
        } catch (IOException e) {
            e.getStackTrace();
            return e.getMessage();
        } catch (IllegalArgumentException er) {
            er.getStackTrace();
            return er.getMessage();
        }
        ProgCircuito.DRAW_ALL_STUFF(this);
        return "";
    }

    /**
     * <b>REMOVE A COMPONENT</b>
     * 
     * @param name Component name to be removed.
     *
     */
    public String removeElement(String name) {
        dewireElement(name);

        for (int i = 0; i < wires.size(); i++) {
            if (wires.get(i).getComponent1().getName().equalsIgnoreCase(name)
                    || wires.get(i).getComponent2().getName().equalsIgnoreCase(name)) {
                wires.remove(i);
                i--;
            }
        }

        for (int i = 0; i < switches.size(); i++) {
            if (switches.get(i).getName().equalsIgnoreCase(name)) {
                switches.remove(i);
                return "";
            }
        }

        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getName().equalsIgnoreCase(name)) {
                components.remove(i);
                return "";
            }
        }

        for (int i = 0; i < outputs.size(); i++) {
            if (outputs.get(i).getName().equalsIgnoreCase(name)) {
                outputs.remove(i);
                return "";
            }
        }

        return "Error: Element not found";
    }

    /**
     * <b>PRINT ALL NAMES ON CONSOLE</b>
     */
    public void printAllNames() {
        for (Switch s : switches) {
            System.out.println(s.getName());
        }
        for (IOComponent c : components) {
            System.out.println(c.getName());
        }
        for (OutputInterface o : outputs) {
            System.out.println(o.getName());
        }
    }

    /**
     * <b>SET WIRE STATE</b>
     * <p>Get origin wire state, if {@code  true}, turn wire <b>green (ON)</b>, else {@code false} <b>gray (OFF)</b>.</p>
     */
    private void setWires() {
        if (wires.isEmpty()) {
            return;
        }
        for (Wire w : wires) {
            for (IOComponent c : components) {
                if (w.getComponent1().getName().equals(c.getName())) {
                    try {
                        w.setState(c.getOutput());
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * <b>SET COMPONENT</b>
     * <p>In each component, get a <b>previous wire state</b> to define the <b>PINs states</b>.</p>
     */
    private void setComponent() {
        if (components.isEmpty()) {
            return;
        }
        for (IOComponent c : components) {
            ArrayList<Boolean> wiresToComponent = new ArrayList<Boolean>();
            for (Wire w : wires) {
                if (w.getComponent2().getName().equals(c.getName())) {
                    wiresToComponent.add(w.getState());
                }
            }
            boolean[] inputs = new boolean[wiresToComponent.size()];
            for (int i = 0; i < wiresToComponent.size(); i++) {
                inputs[i] = wiresToComponent.get(i);
            }
            try {
                c.setInput(inputs);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                if (!e.getMessage().contains("gate must have")) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * <b>SET DISPLAY AND LED OUTPUTS</b>
     * 
     * @return
     * <b>LED</b> <ul><li>Case {@code 0} turn led <b>off</b>.</li> <li>Case {@code 1} turn led <b>on</b>.</li></ul>
     * 
     * <b>DISPLAY3BIT</b> 
     * <p>The display with <b>3 binery inputs</b> can represent on output <b>8 decimal numbers</b> from <b>0</b> to <b>7</b>.</p>
     * <ul>
     *  <li>Case {@code 000}, represent <b>0</b>.</li>
     *  <li>Case {@code 001}, represent <b>1</b>.</li>
     *  <li>Case {@code 010}, represent <b>2</b>.</li>
     *  <li>Case {@code 011}, represent <b>3</b>.</li>
     *  <li>Case {@code 100}, represent <b>4</b>.</li>
     *  <li>Case {@code 101}, represent <b>5</b>.</li>
     *  <li>Case {@code 110}, represent <b>6</b>.</li>
     *  <li>Case {@code 111}, represent <b>7</b>.</li>
     * </ul>
     */
    private void setOutputs() {
        if (outputs.isEmpty()) {
            return;
        }
        // manualmente os outputs
        for (OutputInterface o : outputs) {
            ArrayList<Boolean> wiresToOutput = new ArrayList<Boolean>();
            for (Wire w : wires) {

                if (o instanceof Led) {
                    if (w.getComponent2().getName().equals(o.getName())) {
                        o.setValue(w.getState() ? 1 : 0);
                    }
                }
                if (o instanceof Display3bit) {
                    if (w.getComponent2().getName().equals(o.getName())) {
                        wiresToOutput.add(w.getState());
                    }
                }
            }

            if (o instanceof Display3bit) {
                if (wiresToOutput.size() < 3) {
                    return;
                }
                try {
                    o.setValue(Display3bit.getNumberWithPins(wiresToOutput.get(0), wiresToOutput.get(1),
                            wiresToOutput.get(2)));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * <b>SET SWITCHES WIRE</b>
     * <p>In each switch, get a <b>state</b> to define the <b>next wire state</b> connected.</p>
     */
    private void setSwitchesWire() {
        if (wires.isEmpty()) {
            return;
        }
        // manualmente os swicthes
        for (Wire w : wires) {
            for (Switch s : switches) {
                if (w.getComponent1().getName().equals(s.getName())) {
                    w.setState(s.getState());
                }
                if (w.getComponent2().getName().equals(s.getName())) {
                    w.setState(s.getState());
                }
            }
        }
    }

    /**
     * <b>SET ALL COMPONENTS</b>
     * <p>Execute all set functions.</p>
     * @see 
     * <ul>
     * <li>{@link #setWires()}</li>
     * <li>{@link #setComponent()}</li>
     * <li>{@link #setSwitchesWire()}</li>
     * </ul>
     */
    public void setAllStates() {
        // settar wires dos swictches
        // settar componentes dos wires
        // settar wires dos componentes
        // repeat
        // settar outputs dos wires

        setSwitchesWire();

        // automaticamente os componentes
        for (int i = 0; i < components.size() + 1; i++) {
            setWires();
            setComponent();
        }
        setOutputs();
    }

    /**
     * <b>FORCE DRAW</b>
     * <p>Draw all basic components and wires.</p>
     */
    public void forceDraw() {
        ProgCircuito.drawPannel.clear();

        for (Switch s : switches) {
            s.draw();
        }
        for (IOComponent c : components) {
            c.draw();
        }
        for (OutputInterface o : outputs) {
            o.draw();
        }
        for (Wire w : wires) {
            w.draw();
        }
    }

    /**
     * 
     * <b>DRAW CIRCUIT</b>
     * <p>Execute set all states function and draw all basic components and wires.</p>
     * @see 
     * <ul>
     * <li>{@link #setAllStates()}</li>
     * <li>{@link #forceDraw()}</li>
     * </ul>
     * 
     */
    public void drawCircuit() {
        setAllStates();
        forceDraw();
    }

    /**
     * 
     * <b>REMOVE ALL BASIC COMPONENTS, OUTPUTS AND WIRES</b>
     * <p>Remove all {@code ArrayLists} elements ({@code switches[], components[], outputs[], wires[]}) and draw a new empety circuit.</p> 
     * 
     */
    public void clear() {
        switches.clear();
        components.clear();
        outputs.clear();
        wires.clear();
        drawCircuit();
    }

    /**
     * 
     * <b>ANIMATE ALL SET STATES</b>
     * @param animacao 
     * <ul>
     * <li>Case {@code true} animate the circuit.</li>
     * <li>Case {@code false} do not animate the circuit.</li>
     * </ul>
     */
    public void setAllStates(boolean animacao) throws InterruptedException {
        if (animacao) {

            Timer timer = new Timer(10, new java.awt.event.ActionListener() {
                int i = 0;

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ProgCircuito.drawPannel.clear();
                    MainCircuit.this.drawCircuit();
                    if (i == components.size() + 1) {
                        ((Timer) e.getSource()).stop();
                    }
                    i++;
                }

            });
            timer.start();
        } else {
            setAllStates();
        }
    }

    /**
     * 
     * <b>DRAW THE ANIMATION</b>
     * @param animacao 
     * <ul>
     * <li>Case {@code true} draw animation.</li>
     * <li>Case {@code false} do not draw animation.</li>
     * </ul>
     * 
     */
    public Thread drawCircuitAnimation(boolean animacao) {
        Thread myThread = new Thread(() -> {
            if (animacao) {
                try {
                    setAllStates(animacao);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                drawCircuit();
            }
        });

        myThread.start();
        return myThread;
    }

    /**
     * 
     * <b>TURN SWITCHES ON OR OFF</b>
     * @param name  name of component to turn <b>on</b> or <b>off</b>.
     * @param onOff
     *  <ul>
     * <li>Case <b>onOff</b>: {@code on} turn switch <b>on</b>.</li>
     * <li>Case  <b>onOff</b>: {@code off} turn switch <b>off</b>.</li>
     * </ul>
     * 
     */
    public String turn(String onOff, String name) {
        for (Switch s : switches) {
            if (s.getName().equalsIgnoreCase(name)) {
                if (onOff.equalsIgnoreCase("on")) {
                    s.setState(true);
                } else if (onOff.equalsIgnoreCase("off")) {
                    s.setState(false);
                } else {
                    throw new IllegalArgumentException("Needs to be ON or OFF");
                }
                return "";
            }
        }
        return "Error: Switch not found";
    }

    /**
     * 
     * <b>MOVE COMPONENT POSITION</b>
     * @param name name of component to be moved.
     * @param x new x cordinate.
     * @param y new y cordinate.
     * 
     */
    public String move(String name, int x, int y) {
        for (Switch s : switches) {
            if (s.getName().equalsIgnoreCase(name)) {
                s.setPosition(x, y);
                return "";
            }
        }

        for (IOComponent c : components) {
            if (c.getName().equalsIgnoreCase(name)) {
                c.setPosition(x, y);
                return "";
            }
        }

        for (OutputInterface o : outputs) {
            if (o.getName().equalsIgnoreCase(name)) {
                o.setPosition(x, y);
                return "";
            }
        }

        return "Error: Element not found";
    }

    public void printOutput() {
        for (OutputInterface o : outputs) {
            System.out.println(o.getName() + ": " + o.getValue());
        }
    }

    private static boolean extractBit(int number, int position) {
        return (number & (1 << position)) != 0;
    }

    /**
     * 
     * <b>PRINT TRUTH TABLE</b>
     *
     */
    public void printTabeldaDaVerdade() {
        ArrayList<Switch> oldState = new ArrayList<Switch>();
        for (Switch s : switches) {
            oldState.add(new Switch(s.getState(), s.getName(), s.getXY()[0], s.getXY()[1], s.getLegend()));
        }

        int numSwitches = switches.size();
        int numCombinations = (int) Math.pow(2, numSwitches);

        for (int i = 0; i < numCombinations; i++) {
            for (int j = 0; j < numSwitches; j++) {
                boolean state = extractBit(i, j);
                switches.get(j).setState(state);
            }
            ProgCircuito.drawPannel.clear();
            this.drawCircuit();
            System.out.print("\nCombination " + i + ": ");
            for (Switch s : switches) {
                System.out.print(s.getState() ? "1 " : "0 ");
            }
            System.out.println("\n");
            printOutput();
            System.out.println();
        }

        switches.clear();
        switches.addAll(oldState);

        ProgCircuito.DRAW_ALL_STUFF(this);
    }

    /**
     * 
     * <b>ANIMATE TRUTH TABLE</b>
     *
     */
    public void animacaoTabela() {
        // Cria um thread
        Thread myThread = new Thread(() -> {
            ArrayList<Switch> oldState = new ArrayList<Switch>();
            for (Switch s : switches) {
                oldState.add(new Switch(s.getState(), s.getName(), s.getXY()[0], s.getXY()[1], s.getLegend()));
            }

            int numSwitches = switches.size();
            int numCombinations = (int) Math.pow(2, numSwitches);

            for (int i = 0; i < numCombinations; i++) {
                for (int j = 0; j < numSwitches; j++) {
                    boolean state = extractBit(i, j);
                    switches.get(j).setState(state);
                }

                try {
                    Thread th = drawCircuitAnimation(true);
                    th.join(); // Aguarda a conclusão do thread
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            switches.clear();
            switches.addAll(oldState);

            ProgCircuito.DRAW_ALL_STUFF(this);
        });

        myThread.start();

    }

    /**
     * 
     * <b>PRINT ALL COMPONENTS INFO ON CONSOLE</b>
     *
     */
    public void printAllInfo() {
        System.out.println("\n\nSwitches");
        for (Switch s : switches) {
            s.PrintAllInfo();
        }

        System.out.println("\n\nComponents");
        for (IOComponent c : components) {
            c.PrintAllInfo();
        }

        System.out.println("\n\nOutputs");
        for (OutputInterface o : outputs) {
            o.PrintAllInfo();
        }

        System.out.println("\n\nWires");
        for (Wire w : wires) {
            w.PrintAllInfo();
        }
    }

    /**
     * 
     * <b>VALIDATE IF CIRCUIT IS CORRECTLY IMPLEMENTED</b>
     *
     */
    public String validateCircuit() {
        // Check for inputs and outputs
        if (switches.isEmpty()) {
            return "Erro: O circuito não possui nenhuma entrada (Switch).";
        }
        if (outputs.isEmpty()) {
            return "Erro: O circuito não possui nenhuma saída (LED ou Display).";
        }

        HashSet<BasicComponentInterface> visited = new HashSet<>();
        boolean hasCycle = false;

        for (IOComponent component : components) {
            BasicComponentInterface basicComponent = (BasicComponentInterface) component;
            if (!visited.contains(basicComponent)) {
                hasCycle = hasCycle || detectCicle(basicComponent, visited, new HashSet<>());
            }
        }

        if (hasCycle) {
            return "Erro: O circuito possui loops infinitos.";
        }

        return "Circuito válido!";
    }

     /**
     * 
     * <b>DETECT IF CIRCUIT IS CORRECTLY IMPLEMENTED</b>
     *
     */
    private boolean detectCicle(BasicComponentInterface component, HashSet<BasicComponentInterface> visited,
            HashSet<BasicComponentInterface> currentPath) {
        if (currentPath.contains(component)) {
            // Cycle detected
            return true;
        }

        if (visited.contains(component)) {
            // Already fully explored
            return false;
        }

        visited.add(component);
        currentPath.add(component);

        // Explore connected components
        for (Wire wire : wires) {
            if (wire.getComponent1().equals(component)) {
                BasicComponentInterface nextComponent = wire.getComponent2();
                if (detectCicle(nextComponent, visited, currentPath)) {
                    return true;
                }
            }
        }

        // remove to avoid false positives
        currentPath.remove(component);

        return false;
    }

     /**
     * 
     * <b>DUPLICATE ALL CIRCUIT</b>
     *
     */
    public MainCircuit clone() {
        MainCircuit newCircuit = new MainCircuit();
        for (Switch s : switches) {
            newCircuit.add(LCComponent.SWITCH, s.getState(), s.getName(), s.getXY()[0], s.getXY()[1], s.getLegend());
        }

        for (IOComponent c : components) {
            newCircuit.add(c.getType(), c.getName(), c.getXY()[0], c.getXY()[1], c.getLegend());
        }

        for (OutputInterface o : outputs) {
            newCircuit.add(o.getType(), o.getValue(), o.getName(), o.getXY()[0], o.getXY()[1], o.getLegend());
        }

        for (Wire w : wires) {
            newCircuit.wire(w.getComponent1().getName(), w.getComponent2().getName(), w.getPin());
        }

        return newCircuit;
    }
}

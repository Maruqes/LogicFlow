import java.util.ArrayList;
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
     * Draw a component that has no state
     * 
     * @param cmp     Component type from LCComponent (AND, NAND, NOR, NOT, OR, XOR)
     * @param nome    name
     * @param x       coordinate X
     * @param y       coordinate Y
     * @param legenda legend
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
     * Draw a component that has no state
     * 
     * @param cmp    Component type from LCComponent (SWITCH)
     * @param state  state
     * @param nome   name
     * @param setX   coordinate X
     * @param setY   coordinate Y
     * @param legend legend
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
     * Draw a component that has no state
     * 
     * @param cmp    Component type from LCComponent (DISPLAY, LED)
     * @param value  value
     * @param nome   name
     * @param SetX   coordinate X
     * @param SetY   coordinate Y
     * @param legend legend
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

        Wire wire = new Wire(fromType, toType, pin);

        wires.add(wire);

    }

    public String save(String filename) {
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

    public String open(String filename) {
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
            return e.getMessage();
        } catch (IllegalArgumentException er) {
            return er.getMessage();
        }
        drawCircuit();
        return "";
    }

    public String removeElement(String name) {
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

    public void setWires() {
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

    public void setComponent() {
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
            }
        }
    }

    private void setOutputs() {
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
                try {
                    o.setValue(Display3bit.getNumberWithPins(wiresToOutput.get(0), wiresToOutput.get(1),
                            wiresToOutput.get(2)));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private void setSwitchesWire() {
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

    public void setAllStates() {
        // settar wires dos swictches
        // settar componentes dos wires
        // settar wires dos componentes
        // repeat
        // settar outputs dos wires

        setSwitchesWire();

        // automaticamente os componentes
        for (int i = 0; i < components.size(); i++) {
            setWires();
            setComponent();
        }

        setOutputs();
    }

    public void forceDraw() {
        Main.drawPannel.clear();

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

    // add cada um deles uma funcao pra settar valores
    public void drawCircuit() {
        setAllStates();
        forceDraw();
    }

    public void clear() {
        switches.clear();
        components.clear();
        outputs.clear();
        wires.clear();
        drawCircuit();
    }

    public void setAllStates(boolean animacao) throws InterruptedException {
        if (animacao) {
            setSwitchesWire();
            setOutputs();
            forceDraw();
            Thread.sleep(700);

            // Atualiza automaticamente os componentes
            for (int i = 0; i < components.size(); i++) {
                setWires();
                setComponent();
                setOutputs();
                forceDraw();
                Thread.sleep(700);
            }

            setOutputs();
            forceDraw();
            Thread.sleep(700);
        } else {
            setAllStates();
        }
    }

    public Thread drawCircuitAnimation(boolean animacao) {
        Thread myThread = new Thread(() -> {
            if (animacao) {
                try {
                    setAllStates(animacao);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                forceDraw();
            } else {
                drawCircuit();
            }
        });

        myThread.start();
        return myThread;
    }

    public String turn(String onOff, String nome) {
        for (Switch s : switches) {
            if (s.getName().equalsIgnoreCase(nome)) {
                if (onOff.equalsIgnoreCase("on")) {
                    s.setState(true);
                } else if (onOff.equalsIgnoreCase("off")) {
                    s.setState(false);
                } else {
                    throw new IllegalArgumentException("Invalid value");
                }
                return "";
            }
        }
        return "Error: Switch not found";
    }

    public String move(String nome, int x, int y) {
        for (Switch s : switches) {
            if (s.getName().equalsIgnoreCase(nome)) {
                s.setPosition(x, y);
                return "";
            }
        }

        for (IOComponent c : components) {
            if (c.getName().equalsIgnoreCase(nome)) {
                c.setPosition(x, y);
                return "";
            }
        }

        for (OutputInterface o : outputs) {
            if (o.getName().equalsIgnoreCase(nome)) {
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
            drawCircuit();
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

        drawCircuit();
    }

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

            drawCircuit();
        });

        myThread.start();

    }

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

}

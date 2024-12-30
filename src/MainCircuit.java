import java.util.ArrayList;

import logicircuit.LCComponent;

public class MainCircuit {
    private ArrayList<Switch> switches;
    private ArrayList<IOComponent> components;
    private ArrayList<OutputInterface> outputs; // leds and displays

    public MainCircuit() {
        switches = new ArrayList<Switch>();
        components = new ArrayList<IOComponent>();
        outputs = new ArrayList<OutputInterface>();
    }

    // for IO
    public void add(LCComponent cmp, String nome, int x, int y, String legenda) {

    }

    // for swicthes
    public void add(LCComponent cmp, boolean state, String nome, int setX, int setY, String legend) {

    }

    // for outputs
    public void add(LCComponent cmp, int value, String nome, int setX, int setY, String legend) {

    }

    // add cada um deles uma funcao pra settar valores

    public void drawCircuit() {
        for (Switch s : switches) {
            s.draw();
        }
        for (IOComponent c : components) {
            c.draw();
        }
        for (OutputInterface o : outputs) {
            o.draw();
        }
    }

}

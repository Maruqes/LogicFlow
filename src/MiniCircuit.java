import java.util.ArrayList;

import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class MiniCircuit extends MainCircuit implements IOComponent, BasicComponentInterface {
    private boolean[] pins;
    private int[] xy = { 0, 0 };
    private String legend = "";
    private String name = "";
    private LCComponent type;
    private String filename = "";

    public MiniCircuit(ArrayList<Switch> switches, ArrayList<IOComponent> components,
            ArrayList<OutputInterface> outputs,
            ArrayList<Wire> wires, String name, String legend, String filename) {
        if (switches.size() > 8) {
            throw new IllegalArgumentException("Too many switches");
        }

        if (outputs.size() != 1) {
            throw new IllegalArgumentException("Should have exactly one output as a led");
        }

        if (outputs.get(0).getType() != LCComponent.LED) {
            throw new IllegalArgumentException("Output should be a led");
        }

        pins = new boolean[switches.size()];
        for (int i = 0; i < switches.size(); i++) {
            pins[i] = false;
        }
        for (Switch s : switches) {
            super.add(LCComponent.SWITCH, s.getState(), s.getName(), s.getXY()[0], s.getXY()[1], s.getLegend());
        }

        for (IOComponent c : components) {
            super.add(c.getType(), c.getName(), c.getXY()[0], c.getXY()[1], c.getLegend());
        }

        for (OutputInterface o : outputs) {
            super.add(o.getType(), o.getValue(), o.getName(), o.getXY()[0], o.getXY()[1], o.getLegend());
        }

        for (Wire w : wires) {
            super.wire(w.getComponent1().getName(), w.getComponent2().getName(), w.getPin());
        }

        this.name = name;
        this.legend = legend;
        this.filename = filename;

        if (switches.size() == 1) {
            type = LCComponent.ONE_INPUT;
        } else if (switches.size() == 2) {
            type = LCComponent.TWO_INPUTS;
        } else if (switches.size() == 3) {
            type = LCComponent.THREE_INPUTS;
        } else if (switches.size() == 4) {
            type = LCComponent.FOUR_INPUTS;
        } else if (switches.size() == 5) {
            type = LCComponent.FIVE_INPUTS;
        } else if (switches.size() == 6) {
            type = LCComponent.SIX_INPUTS;
        } else if (switches.size() == 7) {
            type = LCComponent.SEVEN_INPUTS;
        } else if (switches.size() == 8) {
            type = LCComponent.EIGHT_INPUTS;
        } else {
            throw new IllegalArgumentException("Invalid number of inputs");
        }
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != pins.length || inputs.length != super.switches.size()) {
            throw new IllegalArgumentException("MiniCircuit gate must have X inputs");
        }

        for (int i = 0; i < inputs.length; i++) {
            super.switches.get(i).setState(inputs[i]);
        }
    }

    @Override
    public boolean getOutput() {
        try {
            super.setAllStates();
            return super.outputs.get(0).getValue() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setNotUsedPin(LCInputPin pin) {
        for (int i = 0; i < pins.length; i++) {
            if (pin == LCInputPin.values()[i]) {
                pins[i] = false;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid input pin");
    }

    @Override
    public void setUsedPin(LCInputPin pin) {
        for (int i = 0; i < pins.length; i++) {
            if (pin == LCInputPin.values()[i]) {
                System.out.println("Setting pin " + i + " to true");
                pins[i] = true;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid input pin");
    }

    @Override
    public boolean allowPin(LCInputPin pin) {
        for (int i = 0; i < pins.length; i++) {
            if (pin == LCInputPin.values()[i]) {
                System.out.println("returning " + !pins[i]);
                return !pins[i];
            }
        }
        System.out.println("Invalid pin on MiniCircuit");
        return false;
    }

    @Override
    public void draw() {
        Main.drawPannel.drawComponent(type, xy[0], xy[1], legend);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int maxInput() {
        return pins.length;
    }

    @Override
    public int maxOutput() {
        return 1;
    }

    @Override
    public String Strigonize() {
        return "MiniCircuit" + "//" + name + "//" + xy[0] + "//" + xy[1] + "//" + filename;
    }

    @Override
    public void setPosition(int x, int y) {
        xy[0] = x;
        xy[1] = y;
    }

    @Override
    public void PrintAllInfo() {
        System.out.println("MiniCircuit: " + name + " " + xy[0] + " " + xy[1] + " " + legend);
    }

    @Override
    public LCComponent getType() {
        return type;
    }

    @Override
    public int[] getXY() {
        return xy;
    }

    @Override
    public String getLegend() {
        return legend;
    }

    @Override
    public IOComponent clone() {
        return new MiniCircuit(super.switches, super.components, super.outputs, super.wires, name, legend, filename);
    }

    @Override
    public void setLegend(String legend) {
        this.legend = legend;
    }

    @Override
    public BasicComponentInterface cloneCMP() {
        return new MiniCircuit(super.switches, super.components, super.outputs, super.wires, name, legend, filename);
    }

}

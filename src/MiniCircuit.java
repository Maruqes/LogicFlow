import java.util.ArrayList;

import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class MiniCircuit extends MainCircuit implements IOComponent {
    private boolean[] inputs;

    private boolean[] pins;

    public MiniCircuit(ArrayList<Switch> switches, ArrayList<IOComponent> components,
            ArrayList<OutputInterface> outputs,
            ArrayList<Wire> wires) {
        if (switches.size() > 3) {
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
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != pins.length || inputs.length != super.switches.size()) {
            throw new IllegalArgumentException("Invalid number of inputs");
        }

        for (int i = 0; i < inputs.length; i++) {
            pins[i] = inputs[i];
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

    }

    @Override
    public void setUsedPin(LCInputPin pin) {
    }

    @Override
    public boolean allowPin(LCInputPin pin) {
        return false;
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public int maxInput() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maxInput'");
    }

    @Override
    public int maxOutput() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maxOutput'");
    }

    @Override
    public String Strigonize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Strigonize'");
    }

    @Override
    public void setPosition(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPosition'");
    }

    @Override
    public void PrintAllInfo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'PrintAllInfo'");
    }

    @Override
    public LCComponent getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

    @Override
    public int[] getXY() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getXY'");
    }

    @Override
    public String getLegend() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLegend'");
    }

    @Override
    public IOComponent clone() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLegend'");
    }

}

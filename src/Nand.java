import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Nand extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private LCInputPin currentPin;
    private int pinCallCount;

    public Nand(String nome) {
        super(LCComponent.NAND, nome);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    public Nand(String nome, int x, int y) {
        super(LCComponent.NAND, nome, x, y);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    public Nand(String nome, int x, int y, String legenda) {
        super(LCComponent.NAND, nome, x, y, legenda);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("NAND gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("NAND gate inputs are not set");
        }
        return !(inputs[0] && inputs[1]);
    }

    @Override
    public LCInputPin getNextPin() {
        if (pinCallCount == 0) {
            currentPin = LCInputPin.PIN_A;
        } else if (pinCallCount == 1) {
            currentPin = LCInputPin.PIN_B;
        } else {
            throw new IllegalStateException("Invalid pin state");
        }
        pinCallCount++;
        return currentPin;
    }
}

import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public class Xor extends BasicComponent implements IOComponent {
    private boolean[] inputs;
    private LCInputPin currentPin;
    private int pinCallCount;

    public Xor(String nome) {
        super(LCComponent.XOR, nome);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    public Xor(String nome, int x, int y) {
        super(LCComponent.XOR, nome, x, y);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    public Xor(String nome, int x, int y, String legenda) {
        super(LCComponent.XOR, nome, x, y, legenda);
        currentPin = LCInputPin.PIN_A;
        pinCallCount = 0;
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("XOR gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("XOR gate inputs are not set");
        }
        return inputs[0] ^ inputs[1];
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

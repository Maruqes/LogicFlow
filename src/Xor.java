import logicircuit.LCComponent;

public class Xor extends BasicComponent implements IOComponent {
    private boolean[] inputs;

    public Xor(String nome) {
        super(LCComponent.XOR, nome);
    }

    public Xor(String nome, int x, int y) {
        super(LCComponent.XOR, nome, x, y);
    }

    public Xor(String nome, int x, int y, String legenda) {
        super(LCComponent.XOR, nome, x, y, legenda);
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

}

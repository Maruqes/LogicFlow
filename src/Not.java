import logicircuit.LCComponent;

public class Not extends BasicComponent implements IOComponent {
    private boolean[] inputs;

    public Not(String nome) {
        super(LCComponent.NOT, nome);
    }

    public Not(String nome, int x, int y) {
        super(LCComponent.NOT, nome, x, y);
    }

    public Not(String nome, int x, int y, String legenda) {
        super(LCComponent.NOT, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 1) {
            throw new IllegalArgumentException("Not gate must have 1 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("Not gate inputs are not set");
        }
        return !inputs[0];
    }
}

//

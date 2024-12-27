import logicircuit.LCComponent;

public class Or extends BasicComponent implements IOComponent {
    private boolean[] inputs;

    public Or(String nome) {
        super(LCComponent.OR, nome);
    }

    public Or(String nome, int x, int y) {
        super(LCComponent.OR, nome, x, y);
    }

    public Or(String nome, int x, int y, String legenda) {
        super(LCComponent.OR, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("OR gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("OR gate inputs are not set");
        }
        return inputs[0] || inputs[1];
    }

}

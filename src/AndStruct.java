import logicircuit.LCComponent;

public class AndStruct extends BasicComponent implements IOComponent {
    private boolean[] inputs;

    public AndStruct(String nome) {
        super(LCComponent.AND, nome);
    }

    public AndStruct(String nome, int x, int y) {
        super(LCComponent.AND, nome, x, y);
    }

    public AndStruct(String nome, int x, int y, String legenda) {
        super(LCComponent.AND, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("AND gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("AND gate inputs are not set");
        }
        return inputs[0] && inputs[1];
    }
}

//

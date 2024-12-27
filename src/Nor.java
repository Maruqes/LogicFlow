import logicircuit.LCComponent;

public class Nor extends BasicComponent implements IOComponent {
    private boolean[] inputs;

    public Nor(String nome) {
        super(LCComponent.NOR, nome);
    }

    public Nor(String nome, int x, int y) {
        super(LCComponent.NOR, nome, x, y);
    }

    public Nor(String nome, int x, int y, String legenda) {
        super(LCComponent.NOR, nome, x, y, legenda);
    }

    @Override
    public void setInput(boolean[] inputs) {
        if (inputs.length != 2) {
            throw new IllegalArgumentException("NOR gate must have 2 inputs");
        }
        this.inputs = inputs;
    }

    @Override
    public boolean getOutput() {
        if (inputs == null) {
            throw new IllegalStateException("NOR gate inputs are not set");
        }
        return !(inputs[0] || inputs[1]);
    }

}

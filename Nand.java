import logicircuit.LCComponent;

public class Nand extends BasicComponent implements IOComponent {
    private boolean[] inputs;

    public Nand(String nome) {
        super(LCComponent.NAND, nome);
    }

    public Nand(String nome, int x, int y) {
        super(LCComponent.NAND, nome, x, y);
    }

    public Nand(String nome, int x, int y, String legenda) {
        super(LCComponent.NAND, nome, x, y, legenda);
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
}



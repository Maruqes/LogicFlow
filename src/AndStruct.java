public class AndStruct implements ComponentInterface {
    private boolean[] inputs;

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
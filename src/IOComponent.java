import logicircuit.LCInputPin;

public interface IOComponent {
    public void setInput(boolean[] inputs);

    public boolean getOutput();

    public LCInputPin getNextPin();

}

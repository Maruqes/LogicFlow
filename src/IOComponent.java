import logicircuit.LCComponent;
import logicircuit.LCInputPin;

public interface IOComponent {
    public void setInput(boolean[] inputs);

    public boolean getOutput();

    public void draw();

    public boolean allowPin(LCInputPin pin);

    public void setNotUsedPin(LCInputPin pin);

    public void setUsedPin(LCInputPin pin);

    public String getName();

    public int maxInput();

    public int maxOutput();

    public String Strigonize();

    public void setPosition(int x, int y);

    public void PrintAllInfo();

    public IOComponent clone();

    public LCComponent getType();

    public int[] getXY();

    public String getLegend();
}

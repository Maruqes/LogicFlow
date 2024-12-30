import logicircuit.LCComponent;

public interface BasicComponentInterface {
    public void draw();

    public void setPosition(int x, int y);

    public void setLegend(String legend);

    public String getName();

    public String getLegend();

    public int[] getXY();

    public LCComponent getType();

}

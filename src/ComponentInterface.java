public interface ComponentInterface {
    public void setInput(boolean[] inputs);
    public boolean getOutput();
    public void draw();
    public void setPosition(int x, int y);
    public void setLegend(String legend);
    public String getName();
    public String getLegend();
    public int[] getXY();
}

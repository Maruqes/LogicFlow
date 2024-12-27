import logicircuit.LCComponent;

public class BasicComponent implements BasicComponentInterface {

    private int setX;
    private int setY;
    private String legend;
    private String name;
    private logicircuit.LCComponent ourCmp;

    public BasicComponent(logicircuit.LCComponent cmp, String nome) {
        name = nome;
        setX = 0;
        setY = 0;
        legend = "";
        ourCmp = cmp;
    }

    public BasicComponent(logicircuit.LCComponent cmp, String nome, int x, int y) {
        name = nome;
        setX = x;
        setY = y;
        legend = "";
        ourCmp = cmp;
    }

    public BasicComponent(logicircuit.LCComponent cmp, String nome, int x, int y, String legenda) {
        name = nome;
        setX = x;
        setY = y;
        legend = legenda;
        ourCmp = cmp;
    }

    @Override
    public void setPosition(int x, int y) {
        setX = x;
        setY = y;
    }

    @Override
    public void setLegend(String legend) {
        this.legend = legend;
    }

    @Override
    public void draw() {
        Main.drawPannel.drawComponent(ourCmp, setX, setY, legend);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int[] getXY() {
        return new int[] { setX, setY };
    }

    @Override
    public String getLegend() {
        return legend;
    }

    @Override
    public LCComponent getType() {
        return ourCmp;
    }
}

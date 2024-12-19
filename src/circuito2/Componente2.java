package circuito2;

import logicircuit.LCComponent;
import logicircuit.LCDPanel;

import static logicircuit.LCComponent.BIT3_DISPLAY;

public class Componente2 {
    private String id;
    private LCComponent component;
    private int x, y;
    private boolean state;
    private String legend;
    private int value;

    public Componente2(String id, LCComponent component, int x, int y, boolean state, String legend, int value) {
        this.id = id;
        this.component = component;
        this.x = x;
        this.y = y;
        this.state = state;
        this.legend = legend;
        this.value = value;
    }

    public void draw(LCDPanel drawPanel) {
        if (component == BIT3_DISPLAY)
            drawPanel.drawComponent(component, x, y, value);
        else if (component.hasState())
            drawPanel.drawComponent(component, x, y, state, legend);
        else drawPanel.drawComponent(component, x, y, legend);
    }

    public String getId() {
        return id;
    }

    public LCComponent getComponent() {
        return component;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isState() {
        return state;
    }

    public String getLegend() {
        return legend;
    }

    public int getValue() {
        return value;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}

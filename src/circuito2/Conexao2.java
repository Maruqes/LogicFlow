package circuito2;

import logicircuit.LCDPanel;
import logicircuit.LCInputPin;

public class Conexao2 {
    Componente2 begin;
    Componente2 end;
    LCInputPin pin;
    boolean state;

    public Conexao2(Componente2 begin, Componente2 end, LCInputPin pin, boolean state) {
        this.begin = begin;
        this.end = end;
        this.pin = pin;
        this.state = state;
    }

    public void draw(LCDPanel drawPanel) {
        drawPanel.drawWire(begin.getComponent(), begin.getX(), begin.getY(),
                end.getComponent(), end.getX(), end.getY(), pin, state);
    }
}

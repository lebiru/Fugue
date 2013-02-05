import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;


public class ClickyCanvas extends Canvas {
	/**
	 * 
	 */
	int x = 50,y = 150;
    int ex = 10, ey = 10;
    
    @Override
    public void paint(Graphics g)
    {g.setColor(Color.RED);
     g.drawRect(x, y, 200, 20);
     //g.setColor(Color.BLUE);
     //g.drawRect(ex-10, ey-10, 20, 20);
    }
}

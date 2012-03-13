package gerda;
import javax.microedition.lcdui.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author mirontoli
 */
public class MySplashScreen extends Canvas{
    private Display display;
    private Displayable next;
    private Timer timer = new Timer();
    private Image logo = null;

    public MySplashScreen(Display display, Displayable next) {
        this.display = display;
        this.next = next;
        try {
            logo = Image.createImage("/gerda/gym.png");
        }
        catch (IOException e) {
            System.out.println("ingen bild");
        }
        display.setCurrent(this);
    }
    protected void paint(Graphics g) {
        g.setColor(255,255,255);
        g.fillRect(0,0,g.getClipWidth()-1, g.getClipHeight()-1);
        g.drawRect(0, 0, g.getClipWidth()-1, g.getClipHeight()-1);
        g.drawImage(logo, 3, 10, Graphics.TOP|Graphics.LEFT);
    }
    protected void keyPressed(int keyCode) {
        this.dismiss();
    }
    protected void pointerPressed(int x, int y) {
        this.dismiss();
    }
    protected void showNotify () {
        timer.schedule(new CountDownTask(), 5000);
    }
    private void dismiss() {
        timer.cancel();
        display.setCurrent(next);
    }
    private class CountDownTask extends TimerTask {
        public void run() {
            dismiss();
        }
    }


}

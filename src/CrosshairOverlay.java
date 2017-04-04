import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Henrik on 2015-08-31.
 */
public class CrosshairOverlay {

    private Color crosshairColor = Color.GREEN;
    private boolean enableCrosshair = false;
    private final int CENTERX = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);
    private final int CENTERY = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
    private int ls = 7;
    private int ss = 1;
    private int cd = 2;
    private boolean dot = false;
    private int xOffset = 0;
    private int yOffset = 0;
    GraphicsDevice myDevice;

    Window crosshair = new Window(null){

        @Override
        public void paint(Graphics g){
            int longSide = ls;
            int shortSide = ss;
            int gap = cd + ss;
            int zGap = (shortSide%2 == 0) ? gap : gap + 1;
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setColor(crosshairColor);
            g2.fillRect(CENTERX - longSide - gap, CENTERY - shortSide / 2, longSide, shortSide);
            g2.fillRect(CENTERX + zGap, CENTERY - shortSide/2, longSide, shortSide);
            g2.fillRect(CENTERX - shortSide/2, CENTERY - longSide - gap, shortSide, longSide);
            g2.fillRect(CENTERX - shortSide / 2, CENTERY + zGap, shortSide, longSide);
            if(dot)
                g2.fillRect(CENTERX-shortSide/2, CENTERY-shortSide/2, shortSide, shortSide);
        }

        @Override
        public void update(Graphics g){
            paint(g);
            System.out.println("gubbrunk");
        }

    };

    public void setCrosshairColor(String hashColorCode){
        crosshairColor = Color.decode(hashColorCode);
        forceRefreshCrosshair();
    }

    public void toggleCrosshairVisibility(){
        enableCrosshair = !enableCrosshair;
        crosshair.setVisible(enableCrosshair);
    }

    public void setDimensions(int l, int s, int c, boolean dot){
        this.ls = l;
        this.ss = s;
        this.cd = c;
        this.dot = dot;
        forceRefreshCrosshair();
    }

    public CrosshairOverlay(){
        crosshair.setAlwaysOnTop(true);
        crosshair.setBounds(crosshair.getGraphicsConfiguration().getBounds());
        crosshair.setBackground(new Color(0, true));
        crosshair.setVisible(enableCrosshair);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Point hotSpot = new Point(0,0);
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
        Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");
        crosshair.setCursor(invisibleCursor);
        com.sun.awt.AWTUtilities.setWindowOpaque(crosshair,false);
    }

    private void forceRefreshCrosshair(){
        crosshair.setVisible(false);
        crosshair.setVisible(true);
    }

    public void setCrosshairPosition(String[] pos) {
        xOffset = Integer.parseInt(pos[0]);
        yOffset = Integer.parseInt(pos[1]);
        forceRefreshCrosshair();
    }
}

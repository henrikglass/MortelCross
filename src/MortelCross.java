import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Henrik on 2015-08-31.
 */
public class MortelCross implements NativeKeyListener{

    private final String versionText = "MortelCross V1.0";
    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
    private static TrayIcon trayIcon = null;
    // get the SystemTray instance
    private static SystemTray tray = SystemTray.getSystemTray();

    public static void main(String[] args){
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        new MortelCross();
    }

    public MortelCross(){
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        createSystemTrayIcon();
    }

    public static void removeTrayIcon(){
        tray.remove(trayIcon);
        System.out.println("kärringbajs");
    }

    private void createSystemTrayIcon() {

        if (SystemTray.isSupported()) {
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("bilder/mortelcross.png");

            // create a action listener to listen for default action executed on the tray icon
            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    MortelCross.removeTrayIcon();
                    System.exit(0);
                }
            };
            ActionListener toggleListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    crosshairOverlay.toggleCrosshairVisibility();
                }
            };
            ActionListener setColorListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String hashColorCode = JOptionPane.showInputDialog("enter HTML hash color code (e.g #FF00FF):");
                    crosshairOverlay.setCrosshairColor(hashColorCode);
                }
            };
            ActionListener setPositionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String positionString = JOptionPane.showInputDialog("enter crosshair position \"int,int\" \"0,0\" is center:");
                    String[] pos = positionString.split(",");
                    crosshairOverlay.setCrosshairPosition(pos);
                }
            };
            ActionListener setDimensionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String dimensionsString = JOptionPane.showInputDialog("enter size, thickness, gap and enableDot " +
                            "formatted as \"int,int,int,boolean\", without the quotation marks. \n \"default\" resets settings to default:");
                    String[] args = dimensionsString.split(",");
                    if(args[0].equalsIgnoreCase("default"))
                        crosshairOverlay.setDimensions(5, 1, 5, false);
                    else{
                        try {
                            int ls = Integer.parseInt(args[0]);
                            int ss = Integer.parseInt(args[1]);
                            int cd = Integer.parseInt(args[2]);
                            boolean dot = Boolean.valueOf(args[3]);
                            crosshairOverlay.setDimensions(ls, ss, cd, dot);
                        } catch (Exception ex) {
                            System.err.println("parsing failed");
                            ex.printStackTrace();
                        }
                    }
                }
            };

            // create a popup menu
            PopupMenu popup = new PopupMenu();

            // create menu item for the default action
            MenuItem toggleItem = new MenuItem("toggle visibility");
            toggleItem.addActionListener(toggleListener);

            MenuItem setColorItem = new MenuItem("set color");
            setColorItem.addActionListener(setColorListener);

            MenuItem setDimensionItem = new MenuItem("change dimensions");
            setDimensionItem.addActionListener(setDimensionListener);

            MenuItem setPositionItem = new MenuItem("change position");
            setPositionItem.addActionListener(setPositionListener);

            MenuItem exitItem = new MenuItem("exit");
            exitItem.addActionListener(exitListener);

            popup.add(toggleItem);
            popup.add(setDimensionItem);
            popup.add(setPositionItem);
            popup.add(setColorItem);
            popup.addSeparator();
            popup.add(exitItem);

            // construct a TrayIcon
            trayIcon = new TrayIcon(image, versionText, popup);

            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        } else {
            // disable tray option in your application or
            // perform other actions
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if(nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_F8) {
            crosshairOverlay.toggleCrosshairVisibility();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }
}

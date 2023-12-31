import static java.util.Calendar.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * ストップウォッチ。
 */
public final class StopWatch extends JFrame implements Runnable {

    boolean running;
    long startTime;
    long restartTime;

    private final JTextField tView;

    public StopWatch() {
        setTitle("StopWatch");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.tView = new JTextField();
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JButton(new AbstractAction("START") {

            public void actionPerformed(ActionEvent e) {
                running = true;
                startTime = System.currentTimeMillis();
                Thread thread = new Thread(StopWatch.this);
                thread.start();
            }

        }));
        panel.add(new JButton(new AbstractAction("STOP") {
            
            public void actionPerformed(ActionEvent e) {
                if (running == true){
                    running = false;
                    restartTime = System.currentTimeMillis() - startTime;
                }else{
                    running = true;
                    startTime = System.currentTimeMillis() - restartTime;
                    Thread thread = new Thread(StopWatch.this);
                    thread.start();
                }
                
            }

        }));
        panel.add(new JButton(new AbstractAction("RAP") {
            
            public void actionPerformed(ActionEvent e){
                if(running == false){
                    Calendar b = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                    b.setTimeInMillis(System.currentTimeMillis() - (System.currentTimeMillis() - restartTime));
                    System.out.println(String.format("%2d:%02d:%02d:%03d",
                                            b.get(HOUR_OF_DAY),
                                            b.get(MINUTE),
                                            b.get(SECOND),
                                            b.get(MILLISECOND)));
                }else{
                    Calendar b = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                    b.setTimeInMillis(System.currentTimeMillis() - startTime);
                    System.out.println(String.format("%2d:%02d:%02d:%03d",
                                            b.get(HOUR_OF_DAY),
                                            b.get(MINUTE),
                                            b.get(SECOND),
                                            b.get(MILLISECOND)));
                }
            }
        }));
        setLayout(new BorderLayout());
        add(tView, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        JPopupMenu pmenu = new JPopupMenu();
        pmenu.add(new JCheckBoxMenuItem(new AbstractAction("Stay on Top") {

            public void actionPerformed(ActionEvent e) {
                setAlwaysOnTop(!isAlwaysOnTop());
            }

        }));
        tView.setComponentPopupMenu(pmenu);
        pack();
    }

    public void run() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        while (running) {
            c.setTimeInMillis(System.currentTimeMillis() - startTime);
            tView.setText(String.format("%2d:%02d:%02d:%03d",
                                        c.get(HOUR_OF_DAY),
                                        c.get(MINUTE),
                                        c.get(SECOND),
                                        c.get(MILLISECOND)));
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JFrame f = new StopWatch();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }

        });
    }

}
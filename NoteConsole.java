

/*
 * NoteConsole.java
 *
 */
import java.awt.*;
import java.awt.event.*;
public class NoteConsole extends Frame implements ActionListener 
{
  
    public NoteConsole() 
    {
        super(" \"Automatic Portal Notification\" ");
        setSize(525, 150);
        setResizable(false);
        setLayout(new BorderLayout());
        Menu m1 = new Menu("File");
        MenuItem mi1 = new MenuItem("Exit");
        m1.add(mi1);
        mi1.addActionListener(this);
        this.addWindowListener(closeWindow);
        MenuBar mb = new MenuBar();
        setMenuBar(mb);
        mb.add(m1);
        add(new Label("(Approx) heapsize free" + CheckHeapSize.formatSize(CheckHeapSize.heapFreeSize)));
        Button button = new Button(new java.sql.Timestamp(System.currentTimeMillis()).toString());
        Button anotherOne = new Button( System.getenv("USERNAME") ) ;
        add(anotherOne);
        anotherOne.setEnabled(false);
        add(button, BorderLayout.SOUTH);
        final java.awt.SplashScreen splash = java.awt.SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        for(int i=0; i<100; i++) {
            renderSplashFrame(g, i);
            splash.update();
            try {
                Thread.sleep(90);
            }
            catch(InterruptedException e) {
            }
        }
        splash.close();
        setVisible(true);
        toFront();
        return;
    }    
    
    static void renderSplashFrame(Graphics2D g, int frame) 
    {
        final String[] comps = {"RecordReview", "Coordination", "ApplicationAgency"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120,140,200,40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading "+comps[(frame/5)%3]+"...", 120, 150);
    }

    public void actionPerformed(ActionEvent ae) {
        System.exit(0);
    }
    
    private static WindowListener closeWindow = new WindowAdapter(){
        public void windowClosing(WindowEvent e){
            e.getWindow().dispose();
        }
    };
    
    public static void main (String args[]) {
        NoteConsole test = new NoteConsole();
        Agent agent = new Agent( );
        return;
    }
};

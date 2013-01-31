
public class View {
//Menu
private JFrame f= new JFrame ("View"); //frame
private JMenuBar mb = new JMenuBar(); //MenuBar
private JMenu mnuFile = new JMenu ("File");//File Entry on Menu Bar
private JMenuItem mnuItemQuit = new JMenuItem("Quit");//Quit Button

//Constructor
public class View(){
//Set MenuBar
f.setJMenuBar(mb);

//Build Menus
mnuFile.add(mnuItemQuit);
mb.add(mnuFile);

//allows program to be closed
f.addWindowListener (new CloseWdw());

//add menu listerner
mnuItemQuit.addAction (new MenuQuit());

public class MenuQuit implements Action{
	public void actionPerformed (ActionEvent e){
	System.exit(0);
	}
}
 public class CloseWdw extends WindowAdapter{
	public void windowClosing (WindowEvent e){
	}
}
public void launchFrame (){
//displays frame
f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
f.pack();
f.setVisible(true);
}



}

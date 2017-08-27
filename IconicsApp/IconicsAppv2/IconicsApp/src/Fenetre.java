import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Fenetre extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 8134034113775260241L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	private double height = screenSize.getHeight();

	String cheminSource = "/Users/jeanhourmant/Dropbox/Work_30072017/Fichiers/Source/";
	String cheminDestination = "/Users/jeanhourmant/Dropbox/Work_30072017/Fichiers/Destination/";
	final Collection<File> all = new ArrayList<File>();

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu1 = new JMenu("Fichier");
	private JMenuItem item1 = new JMenuItem("Visualiser");
	private JMenuItem item2 = new JMenuItem("Executer");
	private JMenuItem item3 = new JMenuItem("Remplacer");

	private JPanel panPrin = new JPanel();

	private JPanel pan = new JPanel();
	private JPanel pansyno = new JPanel();
	private JPanel pansymb = new JPanel();
	private JPanel panprop = new JPanel();
	private JPanel panprop1 = new JPanel();
	private JPanel panpropt = new JPanel();

	private JPanel panexec = new JPanel();

	private List<Synoptique> synoptiques = new ArrayList<Synoptique>();
	private List<SmartSymbol> symboles = new ArrayList<SmartSymbol>();
	private List<Property> proprietes = new ArrayList<Property>();
	private JComboBox<String> syno = new JComboBox<String>();
	private JComboBox<String> symb = new JComboBox<String>();
	private List<JTextField> prop = new ArrayList<JTextField>();
	private List<JTextField> prop1 = new ArrayList<JTextField>();
	private JTextArea textexec = new JTextArea();

	public Fenetre() {
		this.setTitle("Synoptiques");
		this.setSize(800, (int)height);
		this.setLocation((int)width/3,0);
		initialize1();
	}

	public void initialize1() {
		execAction();
		for(int i=0;i<synoptiques.size();i++) {
			syno.addItem(synoptiques.get(i).getName().toString());
		}
		syno.addActionListener(new SynoAction());
		syno.setSelectedItem(1);
		getSymboles();
		symb.addActionListener(new SmartAction());
		symb.setSelectedItem(1);
		getProperties();

		pansyno.setLayout(new BoxLayout(pansyno, BoxLayout.LINE_AXIS));
		pansyno.setMinimumSize(new Dimension(800,30));
		pansyno.add(syno);

		pansymb.setLayout(new BoxLayout(pansymb, BoxLayout.LINE_AXIS));
		pansymb.setMinimumSize(new Dimension(800,30));
		pansymb.add(symb);

		panpropt.setLayout(new BoxLayout(panpropt,BoxLayout.LINE_AXIS));

		menu1.add(item1);
		item1.addActionListener(new VisuAction());
		menu1.add(item2);
		item2.addActionListener(new ExecAction());
		menu1.add(item3);
		menuBar.add(menu1);

		this.setJMenuBar(menuBar);

		this.setContentPane(panPrin);
		this.setVisible(true);
	}

	private void getSymboles() {
		panprop.removeAll();
		prop.clear();
		panprop1.removeAll();
		prop1.clear();
		symb.removeAllItems();
		symboles = synoptiques.get(syno.getSelectedIndex()).getListeSS();
		for(int i=0;i<symboles.size();i++) {
			symb.addItem(symboles.get(i).getKeyword() + " - " + symboles.get(i).getName().toString());
		}
		pan.repaint();
	}

	private void getProperties() {
		panprop.removeAll();
		panprop1.removeAll();
		prop.clear();
		prop1.clear();
		proprietes = symboles.get(symb.getSelectedIndex()).getListProperty();
		for(int i=0;i<proprietes.size();i++) {
			prop.add(new JTextField(proprietes.get(i).getValue().toString()));
			prop.get(i).setEnabled(false);
			panprop.add(prop.get(i));
			prop1.add(new JTextField(proprietes.get(i).getName().toString()));
			prop1.get(i).setEnabled(false);
			panprop1.add(prop1.get(i));
		}
		panprop.setMinimumSize(new Dimension(600,proprietes.size()*30));
		panprop.setLayout(new BoxLayout(panprop,BoxLayout.PAGE_AXIS));
		panprop1.setMinimumSize(new Dimension(200,proprietes.size()*30));
		panprop1.setLayout(new BoxLayout(panprop1,BoxLayout.PAGE_AXIS));
		panpropt.add(panprop1);
		panpropt.add(panprop);
		pan.repaint();
		pan.revalidate();
	}

	private void execAction() {
		synoptiques.clear();
		findFilesRecursively(new File(cheminSource), all, ".gdfx");
		for(File file11 : all){
			synoptiques.add(new Synoptique(file11.getName().substring(0, file11.getName().toString().length() - 5),file11.getAbsolutePath()));
		}
		for (int i = 0;i<synoptiques.size();i++){
			synoptiques.get(i).findSmartSymbols();
		}
	}

	private static void findFilesRecursively(File file, Collection<File> all, final String extension) {
		//Liste des fichiers correspondant a l'extension souhaitee
		final File[] children = file.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(extension) ;
			}
		}
				);
		if (children != null) {
			//Pour chaque fichier recupere, on appelle a nouveau la methode
			for (File child : children) {
				all.add(child);
				findFilesRecursively(child, all, extension);
			}
		}
	}

	class SynoAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			getSymboles();
		}
	}

	class ExecAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			execAction();
		}
	}

	class SmartAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(!(symb.getItemCount()==0)) {
				getProperties();
			}
		}
	}

	class VisuAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println("t");
			pan.setLayout(new BoxLayout(pan,BoxLayout.PAGE_AXIS));
			pan.add(pansyno);
			pan.add(pansymb);
			pan.add(panpropt);
			panPrin.add(pan);
			panPrin.repaint();
			panPrin.revalidate();
		}
	}
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 8134034113775260241L;

	//Paramètres de la fenêtre d'application
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	private double height = screenSize.getHeight();

	//Paranmètres des chemins des folders de l'application + liste des files
	String cheminSource = "/Users/jeanhourmant/Dropbox/Work_30072017/Fichiers/Source/";
	String cheminDestination = "/Users/jeanhourmant/Dropbox/Work_30072017/Fichiers/Destination/";
	final Collection<File> all = new ArrayList<File>();

	//Paramètres des objets du menu
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu1 = new JMenu("Fichier");
	private JMenuItem item1 = new JMenuItem("Visualiser");
	private JMenuItem item2 = new JMenuItem("Executer");
	private JMenuItem item3 = new JMenuItem("Remplacer");
	private int itemnav = 0;

	//Panel d'affichage des données
	private JPanel panPrin = new JPanel();
	private JPanel pansyno = new JPanel();
	private JPanel pansymb = new JPanel();
	private JPanel panprop = new JPanel();
	private JPanel panprop1 = new JPanel();
	private JPanel panprop2 = new JPanel();
	private JPanel panpropt = new JPanel();
	private JPanel panMap = new JPanel();

	private JPanel panMenu1 = new JPanel();
	private JPanel panMenu2 = new JPanel();
	private JPanel panMenu3 = new JPanel();

	//Paramètres d'utilisation globale
	private List<Synoptique> synoptiques = new ArrayList<Synoptique>();
	private JComboBox<String> syno = new JComboBox<String>();
	private JComboBox<String> symb = new JComboBox<String>();
	private List<JTextField> prop = new ArrayList<JTextField>();
	private List<JTextField> prop1 = new ArrayList<JTextField>();
	private List<JTextField> prop2 = new ArrayList<JTextField>();
	private JTextArea textexec = new JTextArea();
	private Font font1 = new Font("SansSerif", Font.BOLD, 10);
	private JFrame window = new JFrame();
	private Point p = new Point((int)this.width-970, 0);

	//Constructeur
	public Fenetre() {
		this.setTitle("Synoptiques");
		this.setSize((int)width/3, (int)height);
		this.setLocation(0,0);
		this.window.setLocation((int)width-970, 0);
		this.addComponentListener(new componentAction());
		initialize1();
	}

	//Initialisation de l'affichage
	public void initialize1() {
		//Chargement des items par lecture des fichiers de l'application
		execAction();

		//Ajouts des noms des synoptiques pour accès liste
		for(int i=0;i<synoptiques.size();i++) {
			syno.addItem(synoptiques.get(i).getName().toString());
		}

		itemnav=1;

		//Listener sur le bouton liste synoptique pour MAJ de la liste des smarts symbols
		syno.addActionListener(new SynoAction());
		syno.setSelectedItem(1);
		//Récupération des symboles en fonction du choix utilisateur
		getSymboles();
		//Listener sur le bouton liste smart symboles pour MAJ des propriétés
		symb.addActionListener(new SmartAction());
		symb.setSelectedItem(1);
		//Récupération des propriétés du symbole en fonction du choix utilisateur
		getProperties();

		//Paramétrage de l'affichage du panel synoptique
		pansyno.setLayout(new BoxLayout(pansyno, BoxLayout.LINE_AXIS));
		pansyno.setPreferredSize(new Dimension((new Dimension(this.getSize())).width-10,30));
		pansyno.add(syno);

		//Paramétrage de l'affichage du panel symbole
		pansymb.setLayout(new BoxLayout(pansymb, BoxLayout.LINE_AXIS));
		pansymb.setPreferredSize(new Dimension((new Dimension(this.getSize())).width-10,30));
		pansymb.add(symb);

		//Paramétrage de l'affichage du panel propriétés
		panpropt.setLayout(new BoxLayout(panpropt,BoxLayout.LINE_AXIS));
		panprop.setLayout(new BoxLayout(panprop,BoxLayout.PAGE_AXIS));
		panprop1.setLayout(new BoxLayout(panprop1,BoxLayout.PAGE_AXIS));
		panprop2.setLayout(new BoxLayout(panprop2,BoxLayout.PAGE_AXIS));
		panMap.setLayout(new BoxLayout(panMap,BoxLayout.PAGE_AXIS));
		//Ajout des items du menu au bandeau de menu de la fenêtre
		menu1.add(item1);
		item1.addActionListener(new VisuAction());
		menu1.add(item2);
		item2.addActionListener(new ExecAction());
		menu1.add(item3);
		item3.addActionListener(new RempAction());
		menuBar.add(menu1);
		this.setJMenuBar(menuBar);

		//Affectation de panPrin au contenu de la fenêtre
		this.setContentPane(panPrin);
		this.setVisible(true);
	}

	//Fonction de nettoyage des panels et listes utilisés
	private void clearAll(){
		panprop.removeAll();
		panprop1.removeAll();
		panprop2.removeAll();
		panpropt.removeAll();
		prop.clear();
		prop1.clear();
		prop2.clear();
	}

	//Fonction getSymboles
	//- Supprime le contenu des différents panels pour mise à jour
	//- Recherche les symboles du synoptique selectionné
	//- Appelle la fonction rempAction() si l'item3 du menu est selectionné
	//- Appelle la fonction getProperties() si l'item1 du menu est selectionné
	private void getSymboles() {
		clearAll();
		symb.removeAllItems();
		//symboles = synoptiques.get(syno.getSelectedIndex()).getListeSS();
		for(int i=0;i<synoptiques.get(syno.getSelectedIndex()).getNbSSIn();i++) {
			symb.addItem(synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getKeyword().toString() + " - " + synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getName().toString());
		}

		/*for(int i=0;i<symboles.size();i++) {
			symb.addItem(symboles.get(i).getKeyword() + " - " + symboles.get(i).getName().toString());
		}*/
		pansymb.repaint();
		pansymb.revalidate();
		if (itemnav == 1) getProperties();
		if (itemnav == 3) {
			rempAction();
			mapAction();
		}
	}

	//Fonction getProperties pour Menu 1
	//- Supprime le contenu des différents panels pour mise à jour
	//- Recherche toutes les propriétés du smart symbole selectionné
	//- Ajoute le tag associé au smart symbole au panel pour l'affichage
	//- Ajoute les propriétés aux panels pour l'affichage
	//- Set les paramètres des pannels
	//- Mets à jour le panel principal
	private void getProperties() {
			clearAll();

			//proprietes = symboles.get(symb.getSelectedIndex()).getListProperty();
			prop1.add(new JTextField("Tag associé :"));
			//prop.add(new JTextField(symboles.get(symb.getSelectedIndex()).getAssociatedTag()));
			prop.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getAssociatedTag()));
			prop1.add(new JTextField("Position X :"));
			//prop.add(new JTextField((String.valueOf(symboles.get(symb.getSelectedIndex()).getX()))));
			prop.add(new JTextField(String.valueOf(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getX())));
			prop1.add(new JTextField("Position Y :"));
			//prop.add(new JTextField(String.valueOf(symboles.get(symb.getSelectedIndex()).getY())));
			prop.add(new JTextField(String.valueOf(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getY())));
			for(int i=0;i<3;i++) {
				prop.get(i).setEnabled(false);
				panprop.add(prop.get(i));
				prop1.get(i).setEnabled(false);
				panprop1.add(prop1.get(i));
				prop.get(i).setFont(font1);
				prop.get(i).setPreferredSize(new Dimension(3*((new Dimension(this.getSize())).width-10)/4,17));
				prop1.get(i).setFont(font1);
				prop1.get(i).setPreferredSize(new Dimension(1*((new Dimension(this.getSize())).width-10)/4,17));
			}
			for(int i=0;i<synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getListProperty().size();i++) {
				prop.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getProperty(i).getValue().toString()));
				//prop.add(new JTextField(proprietes.get(i).getValue().toString()));
				prop.get(i+3).setEnabled(false);
				prop.get(i+3).setFont(font1);
				prop.get(i+3).setPreferredSize(new Dimension(3*((new Dimension(this.getSize())).width-10)/4,17));
				panprop.add(prop.get(i+3));
				prop1.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getProperty(i).getName().toString()));
				//prop1.add(new JTextField(proprietes.get(i).getName().toString()));
				prop1.get(i+3).setEnabled(false);
				prop1.get(i+3).setFont(font1);
				prop1.get(i+3).setPreferredSize(new Dimension(1*((new Dimension(this.getSize())).width-10)/4,17));
				panprop1.add(prop1.get(i+3));
			}
			panpropt.add(panprop1);
			panpropt.add(panprop);
			reprintPanPrin(panMenu1);
	}

	//Fonction rempAction pour Menu 3
	//- Supprime le contenu des différents panels pour mise à jour
	//- Ajoute trois colonnes avec les informations suivantes :
	//		1- Le keyword et le nom de tous les symboles recenssé pour le synoptique selectionné
	//		2- Le tag associé au smart symbol
	//		3- Une colonne vide pour indiquer le nouveau tag du smart symbole (remplace alors le tag associé par ce dernier)
	private void rempAction() {
			clearAll();
			for(int i=0;i<synoptiques.get(syno.getSelectedIndex()).getNbSSIn();i++) {
				prop1.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getKeyword() + " - " + synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getName().toString()));
				prop1.get(i).setEnabled(false);
				prop1.get(i).setFont(font1);
				prop1.get(i).setPreferredSize(new Dimension(2*((new Dimension(this.getSize())).width-10)/5,17));
				panprop1.add(prop1.get(i));
				prop.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getAssociatedTag()));
				prop.get(i).setEnabled(false);
				prop.get(i).setFont(font1);
				prop.get(i).setPreferredSize(new Dimension((int)(1.5*((new Dimension(this.getSize())).width-10)/5),17));
				panprop.add(prop.get(i));
				prop2.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getChangedTag()));
				prop2.get(i).setEnabled(true);
				prop2.get(i).setFont(font1);
				prop2.get(i).addFocusListener(new Surbrillance(i));
				prop2.get(i).setPreferredSize(new Dimension((int)(1.5*((new Dimension(this.getSize())).width-10)/5),17));
				panprop2.add(prop2.get(i));
			}
			panpropt.add(panprop1);
			panpropt.add(panprop);
			panpropt.add(panprop2);
			reprintPanPrin(panMenu3);
	}

	private void mapAction() {
		if(window.getX() != p.getX() || window.getY() != p.getY()) p = window.getLocation();
		window.dispose();
        window.pack();
        window.add(synoptiques.get(syno.getSelectedIndex()).getControl());
        window.setSize(970, 475);
        window.setLocation(p);
        window.setVisible(true);
	}

	//Fonction execAction
	//- Recherche les fichiers des synoptiques dans le chemin Source
	//- Ajouter à la liste les nouveaux synoptiques
	//- Effectue la recherche complète sur chacun des fichiers (smart symboles et propriétés associées pour chaque item)
	//- En sortie, nous avons tous les objects Synoptique, Tag, SmartSymbol et Property nécessaire à l'application
	private void execAction() {
		synoptiques.clear();
		findFilesRecursively(new File(cheminSource), all, ".gdfx");
		for(File file11 : all){
			synoptiques.add(new Synoptique(file11.getName().substring(0, file11.getName().toString().length() - 5),file11.getAbsolutePath()));
		}
		for (int i = 0;i<synoptiques.size();i++){
			synoptiques.get(i).findSmartSymbols(this.textexec);
		}
	}

	//Listener sur la ComboBox listant les synoptiques
	//En fonction du menu appelant, exécute l'une ou l'autre des fonctions
	class SynoAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			getSymboles();
		}
	}

	//Listener sur la ComboBox listant les smart symboles
	//Execute la fonction getProperties uniquement si item 1 du menu en sélection active
	class SmartAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(!(symb.getItemCount()==0)) {
				getProperties();
			}
		}
	}

	class componentAction implements ComponentListener{
		public void componentResized(ComponentEvent e) {
			if (itemnav == 1 || itemnav == 3) getSymboles();
		}
		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub

		}
	}

	class ChangeTag implements ActionListener{
		public void actionPerformed(ActionEvent e) {

		}
	}

	class Surbrillance implements FocusListener{
		private int num;

		public Surbrillance(int j) {
			this.num = j;
		}

		public void focusGained(FocusEvent e) {
			synoptiques.get(syno.getSelectedIndex()).repaintPoint(this.num,Color.GREEN);
			window.revalidate();
			window.repaint();
		}

		@Override
		public void focusLost(FocusEvent e) {
			synoptiques.get(syno.getSelectedIndex()).getSmartS(num).setChangedTag(prop2.get(num).getText());
			if(!(synoptiques.get(syno.getSelectedIndex()).getSmartS(num).getChangedTag().toString().equals(""))) synoptiques.get(syno.getSelectedIndex()).repaintPoint(this.num,Color.YELLOW);
			else synoptiques.get(syno.getSelectedIndex()).repaintPoint(this.num,Color.RED);
			window.revalidate();
			window.repaint();
		}

	}

	//Listener sur l'item 1 du menu
	//En fonction du menu appelant, exécute l'une ou l'autre des fonctions
	class VisuAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 1;
			menu1.setText(item1.getText());
			panMenu1.setVisible(true);
			panMenu2.setVisible(false);
			panMenu3.setVisible(false);
			panMenu1.setLayout(new BoxLayout(panMenu1,BoxLayout.PAGE_AXIS));
			getSymboles();
			panMenu1.add(pansyno);
			panMenu1.add(pansymb);
			panMenu1.add(panpropt);
			reprintPanPrin(panMenu1);
		}
	}

	//Listener sur l'item 2 du menu
	//Permet la mise à jour du Panel pour cette action
	class ExecAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 2;
			menu1.setText(item2.getText());
			execAction();
			panMenu1.setVisible(false);
			panMenu2.setVisible(true);
			panMenu3.setVisible(false);
			reprintPanPrin(panMenu2);
		}
	}

	//Listener sur l'item 3 du menu
	//En fonction du menu appelant, exécute l'une ou l'autre des fonctions
	class RempAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 3;
			menu1.setText(item3.getText());
			panMenu1.setVisible(false);
			panMenu2.setVisible(false);
			panMenu3.setVisible(true);
			panMenu3.setLayout(new BoxLayout(panMenu3,BoxLayout.PAGE_AXIS));
			getSymboles();
			panMenu3.add(pansyno);
			panMenu3.add(panpropt);
			reprintPanPrin(panMenu3);
		}
	}

	public void reprintPanPrin(JPanel pan) {
		panPrin.removeAll();
		panPrin.add(pan);
		panPrin.repaint();
		panPrin.revalidate();
	}

	//Fonction findFilesRecursively
	//- Recherche selon une méthode récursive tous les fichiers .gdfx contenu dans le repértoire cheminSource
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
}

import java.awt.BorderLayout;
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
	private JPanel pan = new JPanel();
	private JPanel pansyno = new JPanel();
	private JPanel pansymb = new JPanel();
	private JPanel panprop = new JPanel();
	private JPanel panprop1 = new JPanel();
	private JPanel panprop2 = new JPanel();
	private JPanel panpropt = new JPanel();
	private JPanel panexec = new JPanel();

	//Paramètres d'utilisation globale
	private List<Synoptique> synoptiques = new ArrayList<Synoptique>();
	private List<SmartSymbol> symboles = new ArrayList<SmartSymbol>();
	private List<Property> proprietes = new ArrayList<Property>();
	private JComboBox<String> syno = new JComboBox<String>();
	private JComboBox<String> symb = new JComboBox<String>();
	private List<JTextField> prop = new ArrayList<JTextField>();
	private List<JTextField> prop1 = new ArrayList<JTextField>();
	private List<JTextField> prop2 = new ArrayList<JTextField>();
	private JTextArea textexec = new JTextArea();

	//Constructeur
	public Fenetre() {
		this.setTitle("Synoptiques");
		this.setSize(800, (int)height);
		this.setLocation((int)width/3,0);
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
		pansyno.setMinimumSize(new Dimension(800,30));
		pansyno.add(syno);

		//Paramétrage de l'affichage du panel symbole
		pansymb.setLayout(new BoxLayout(pansymb, BoxLayout.LINE_AXIS));
		pansymb.setMinimumSize(new Dimension(800,30));
		pansymb.add(symb);

		//Paramétrage de l'affichage du panel propriétés
		panpropt.setLayout(new BoxLayout(panpropt,BoxLayout.LINE_AXIS));
		panprop.setLayout(new BoxLayout(panprop,BoxLayout.PAGE_AXIS));
		panprop1.setLayout(new BoxLayout(panprop1,BoxLayout.PAGE_AXIS));
		panprop2.setLayout(new BoxLayout(panprop2,BoxLayout.PAGE_AXIS));
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
		symboles = synoptiques.get(syno.getSelectedIndex()).getListeSS();
		for(int i=0;i<symboles.size();i++) {
			symb.addItem(symboles.get(i).getKeyword() + " - " + symboles.get(i).getName().toString());
		}
		pan.repaint();
		if (itemnav == 3) rempAction();
		if (itemnav == 1) getProperties();
	}

	//Fonction getProperties
	//- Supprime le contenu des différents panels pour mise à jour
	//- Recherche toutes les propriétés du smart symbole selectionné
	//- Ajoute le tag associé au smart symbole au panel pour l'affichage
	//- Ajoute les propriétés aux panels pour l'affichage
	//- Set les paramètres des pannels
	//- Mets à jour le panel principal
	private void getProperties() {
		if(itemnav == 1) {
			pan.setVisible(true);
			clearAll();
			proprietes = symboles.get(symb.getSelectedIndex()).getListProperty();
			prop1.add(new JTextField("Tag associé :"));
			prop.add(new JTextField(symboles.get(symb.getSelectedIndex()).getAssociatedTag()));
			for(int i=0;i<proprietes.size();i++) {
				prop.add(new JTextField(proprietes.get(i).getValue().toString()));
				prop.get(i).setEnabled(false);
				panprop.add(prop.get(i));
				prop1.add(new JTextField(proprietes.get(i).getName().toString()));
				prop1.get(i).setEnabled(false);
				panprop1.add(prop1.get(i));
			}
			prop.get(0).setEnabled(true);
			panprop.setMinimumSize(new Dimension(600,proprietes.size()*30));
			panprop1.setMinimumSize(new Dimension(200,proprietes.size()*30));
			panpropt.add(panprop1);
			panpropt.add(panprop);
			pan.repaint();
			pan.revalidate();
		}
	}

	//Fonction rempAction
	//- Supprime le contenu des différents panels pour mise à jour
	//- Ajoute trois colonnes avec les informations suivantes :
	//		1- Le keyword et le nom de tous les symboles recenssé pour le synoptique selectionné
	//		2- Le tag associé au smart symbol
	//		3- Une colonne vide pour indiquer le nouveau tag du smart symbole (remplace alors le tag associé par ce dernier)
	private void rempAction() {
		if (itemnav == 3) {
			pan.setVisible(true);
			pansymb.setVisible(false);
			clearAll();
			for(int i=0;i<symboles.size();i++) {
				prop1.add(new JTextField(symboles.get(i).getKeyword() + " - " + symboles.get(i).getName().toString()));
				prop1.get(i).setEnabled(false);
				panprop1.add(prop1.get(i));
				prop.add(new JTextField(symboles.get(i).getAssociatedTag()));
				prop.get(i).setEnabled(false);
				panprop.add(prop.get(i));
				prop2.add(new JTextField(""));
				prop2.get(i).setEnabled(true);
				panprop2.add(prop2.get(i));
			}
			panprop.setMinimumSize(new Dimension(200,symboles.size()*30));
			panprop1.setMinimumSize(new Dimension(400,symboles.size()*30));
			panprop2.setPreferredSize(new Dimension(200,symboles.size()*30));
			panpropt.add(panprop1);
			panpropt.add(panprop);
			panpropt.add(panprop2);
			panpropt.setAutoscrolls(true);
			pan.repaint();
			pan.revalidate();
		}
	}

	//Fonction execAction
	//- Recherche les fichiers des synoptiques dans le chemin Source
	//- Ajouter à la liste les nouveaux synoptiques
	//- Effectue la recherche complète sur chacun des fichiers (smart symboles et propriétés associées pour chaque item)
	//- En sortie, nous avons tous les objects Synoptique, Tag, SmartSymbol et Property nécessaire à l'application
	private void execAction() {
		synoptiques.clear();
		panexec.setVisible(true);
		pan.setVisible(false);
		findFilesRecursively(new File(cheminSource), all, ".gdfx");
		for(File file11 : all){
			synoptiques.add(new Synoptique(file11.getName().substring(0, file11.getName().toString().length() - 5),file11.getAbsolutePath()));
		}
		for (int i = 0;i<synoptiques.size();i++){
			synoptiques.get(i).findSmartSymbols(this.textexec);
		}
		pan.add(this.textexec);
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

	//Listener sur la ComboBox listant les synoptiques
	//En fonction du menu appelant, exécute l'une ou l'autre des fonctions
	class SynoAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			pan.setVisible(true);
			getSymboles();
		}
	}

	//Listener sur l'item 2 du menu
	//Permet la mise à jour du Panel pour cette action
	class ExecAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 2;
			execAction();
		}
	}

	//Listener sur l'item 3 du menu
	//En fonction du menu appelant, exécute l'une ou l'autre des fonctions
	class RempAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 3;
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

	//Listener sur l'item 1 du menu
	//En fonction du menu appelant, exécute l'une ou l'autre des fonctions
	class VisuAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 1;
			pansymb.setVisible(true);
			pan.setVisible(true);
			pan.setLayout(new BoxLayout(pan,BoxLayout.PAGE_AXIS));
			getSymboles();
			pan.add(pansyno);
			pan.add(pansymb);
			pan.add(panpropt);
			panPrin.add(pan);
			panPrin.repaint();
			panPrin.revalidate();
		}
	}
}

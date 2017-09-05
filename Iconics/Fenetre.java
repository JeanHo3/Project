import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ComponentEvent;
//import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 8134034113775260241L;
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	//Paramètres de la fenêtre d'application
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	//private double height = screenSize.getHeight();

	//Paranmètres des chemins des folders de l'application + liste des files
	String cheminSource = "/Users/jeanhourmant/Dropbox/Work_30072017/Fichiers/Source/";
	String cheminDestination = "/Users/jeanhourmant/Dropbox/Work_30072017/Fichiers/Destination/";
	final Collection<File> all = new ArrayList<File>();

	//Paramètres des objets du menu
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu1 = new JMenu("Fichier");
	private JMenuItem item1 = new JMenuItem("Visualiser");
//	private JMenuItem item2 = new JMenuItem("Executer");
	private JMenuItem item3 = new JMenuItem("Remplacer");
	private JMenuItem item4 = new JMenuItem("Contrôler");
	private JMenuItem item5 = new JMenuItem("Cross References");
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

	private JPanel panMenu0 = new JPanel();
	private JPanel panMenu1 = new JPanel();
	private JPanel panMenu2 = new JPanel();
	private JPanel panMenu3 = new JPanel();
	private JPanel panMenu4 = new JPanel();
	private JPanel panMenu5 = new JPanel();

	//Paramètres d'utilisation globale
	private List<Synoptique> synoptiques = new ArrayList<Synoptique>();
	private JComboBox<String> syno = new JComboBox<String>();
	private JComboBox<String> symb = new JComboBox<String>();
	private List<JTextField> prop = new ArrayList<JTextField>();
	private List<JTextField> prop1 = new ArrayList<JTextField>();
	private List<JTextField> prop2 = new ArrayList<JTextField>();
	private JTextArea textexec = new JTextArea();
	private JTextArea textQuality = new JTextArea();
	private JTextArea textComplet = new JTextArea();
	private Font font1 = new Font("SansSerif", Font.BOLD, 10);
	private JFrame window = new JFrame();
	private Point p = new Point((int)this.width-970, 0);

    private int ref = 0;

	//Constructeur
	public Fenetre() {
		this.setTitle("Utilitaires pour l'application Iconics");
		//this.setSize((int)width/3, (int)height);
		this.setSize(880,805);
		this.setLocation(0,0);
		this.window.setLocation((int)width-970, 0);
		//this.addComponentListener(new componentAction());
		this.setResizable(false);
		initialize1();
	}

	//Initialisation de l'affichage
	public void initialize1() {
		//Chargement des items par lecture des fichiers de l'application
		this.setContentPane(panPrin);
		this.setVisible(true);

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
		//menu1.add(item2);
		//item2.addActionListener(new ExecAction());
		menu1.add(item3);
		item3.addActionListener(new RempAction());
		menu1.add(item4);
		item4.addActionListener(new ControleAction());
		menu1.add(item5);
		item5.addActionListener(new CrossRefAction());
		menuBar.add(menu1);
		this.setJMenuBar(menuBar);

		itemnav = 0;
		accueil();

		//Affectation de panPrin au contenu de la fenêtre

	}

	private void accueil() {
		clearAll();
		ImageIcon image = new ImageIcon("/Users/jeanhourmant/Desktop/Actemium.png");
		JLabel label = new JLabel("", image, JLabel.CENTER);
		panMenu0.add(label, BorderLayout.CENTER);
		panMenu0.setVisible(true);
		reprintPanPrin(panMenu0);
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
		for(int i=0;i<synoptiques.get(syno.getSelectedIndex()).getNbSSIn();i++) {
			symb.addItem(synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getKeyword().toString() + " - " + synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getName().toString());
		}
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
			prop1.add(new JTextField("Tag associé :"));
			prop.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getAssociatedTag()));
			prop1.add(new JTextField("Position X :"));
			prop.add(new JTextField(String.valueOf(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getX())));
			prop1.add(new JTextField("Position Y :"));
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
				prop.get(i+3).setEnabled(false);
				prop.get(i+3).setFont(font1);
				prop.get(i+3).setPreferredSize(new Dimension(3*((new Dimension(this.getSize())).width-10)/4,17));
				panprop.add(prop.get(i+3));
				prop1.add(new JTextField(synoptiques.get(syno.getSelectedIndex()).getSmartS(symb.getSelectedIndex()).getProperty(i).getName().toString()));
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
		reprintPanPrin(panMenu3);
	}

	private void getControle() {
		//Sélection de la référence sur le synoptique Symboles OptimisésV2 -> Ajouter aux paramètres
		for(int i=0;i<this.synoptiques.size();i++) {
			if(this.synoptiques.get(i).getReference()==false) {
				this.synoptiques.get(i).controlQuality(this.synoptiques.get(ref).getListeK(), this.synoptiques.get(ref).getListeC());
			}
		}
		if (itemnav==4) showQualityBySyno();
	}

	private void showQualityBySyno() {
		textQuality.selectAll();
		textQuality.replaceSelection("");
		for (int i=0;i<this.synoptiques.size();i++) {
			textQuality.append("\n" + this.synoptiques.get(i).getName().toString());
			if(this.synoptiques.get(i).getListeInval().size()>0) {
				for(int j=0;j<this.synoptiques.get(i).getListeInval().size();j++) textQuality.append(";" + this.synoptiques.get(i).getListeInval().get(j).toString());
			}
			else textQuality.append(";");
		}
		JScrollPane panMenu4sc = new JScrollPane(textQuality);
		panMenu4sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panMenu4.setPreferredSize(new Dimension((int)this.getSize().width-10,(int)this.getSize().height-80));
		panMenu4.add(panMenu4sc);
		reprintPanPrin(panMenu4);
	}

	/*private void showQualityBySmart() {
		textQuality.selectAll();
		textQuality.replaceSelection("");
		for(String getK : this.synoptiques.get(ref).getListeK()) {
			textQuality.append("\n" + getK.toString());
			for(Synoptique getSyn : this.synoptiques) {
				if(getSyn.getListeInval().contains(getK.toString())) {
					textQuality.append(";" + getSyn.getName().toString());
				}
			}
		}
		JScrollPane panMenu4sc = new JScrollPane(textQuality);
		panMenu4sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panMenu4.setPreferredSize(new Dimension((int)this.getSize().width-10,(int)this.getSize().height-80));
		panMenu4.add(panMenu4sc);
		reprintPanPrin(panMenu4);
	}*/

	private void showComplete() {
		getControle();
		String API = "";
		String CHMACQ = "";
		String CHMALM = "";
		textComplet.selectAll();
		textComplet.replaceSelection("");
		textComplet.append("Nom du synoptique;Nom du symbole;Valide;Tag Associé;ShareKeyword;CustomData de référence;CustomData du synoptique;Position X;Position Y;API Associé;CHM_ACQ Associé;CHM_ALM Associé");
		for(int i=0;i<this.synoptiques.size();i++) {
			for(int j=0;j<this.synoptiques.get(i).getListeSS().size();j++) {
				textComplet.append("\n" + this.synoptiques.get(i).getName().toString());
				textComplet.append(";" + this.synoptiques.get(i).getSmartS(j).getName().toString() + ";" + this.synoptiques.get(i).getSmartS(j).getQuality() + ";" + this.synoptiques.get(i).getSmartS(j).getAssociatedTag().toString() + ";" + this.synoptiques.get(i).getSmartS(j).getShareKeyword().toString());
				int poin = 0;
				while(poin<this.synoptiques.get(ref).getListeK().size()-1 && !(this.synoptiques.get(i).getSmartS(j).getKeyword().equals(this.synoptiques.get(ref).getListeK().get(poin).toString()))) {
					poin++;
				}
				if(this.synoptiques.get(i).getSmartS(j).getKeyword().equals(this.synoptiques.get(ref).getListeK().get(poin).toString()))
					textComplet.append(";" + this.synoptiques.get(ref).getListeC().get(poin).toString());
				else textComplet.append(";");
				textComplet.append(";" + this.synoptiques.get(i).getSmartS(j).getCustomData().toString() + ";" + this.synoptiques.get(i).getSmartS(j).getX() + ";" + this.synoptiques.get(i).getSmartS(j).getY());
				API = this.synoptiques.get(i).getSmartS(j).getValueIfExistsProp("API");
				CHMACQ = this.synoptiques.get(i).getSmartS(j).getValueIfExistsProp("Chemin_Acquisition");
				CHMALM = this.synoptiques.get(i).getSmartS(j).getValueIfExistsProp("Chemin_Alarme");
				if(API != "") textComplet.append(";" + API);
				else textComplet.append(";");

				if(CHMACQ != "") textComplet.append(";" + CHMACQ);
				else textComplet.append(";");

				if(CHMALM != "") textComplet.append(";" + CHMALM);
			}
		}
		Date date = new Date();
		String datej = sdf.format(date).toString().replaceAll(":", "_");
		datej = datej.replaceAll("/", "_");
		String fileName = "temp" + datej + ".csv";
        try {
            FileWriter fileWriter = new FileWriter("/Users/jeanhourmant/Desktop/-JHO/" + fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            textComplet.selectAll();
            bufferedWriter.write(textComplet.getSelectedText());
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '"+ fileName + "'");
        }
		JScrollPane panMenu5sc = new JScrollPane(textComplet);
		panMenu5sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panMenu5.setPreferredSize(new Dimension((int)this.getSize().width-10,(int)this.getSize().height-80));
		panMenu5.add(panMenu5sc);
		reprintPanPrin(panMenu5);

	}

	//Fonction execAction
	//- Recherche les fichiers des synoptiques dans le chemin Source
	//- Ajouter à la liste les nouveaux synoptiques
	//- Effectue la recherche complète sur chacun des fichiers (smart symboles et propriétés associées pour chaque item)
	//- En sortie, nous avons tous les objects Synoptique, Tag, SmartSymbol et Property nécessaire à l'application
	private void execAction() {
		textexec.selectAll();
		textexec.replaceSelection("");

		JScrollPane panMenu2sc = new JScrollPane(textexec);

		panMenu2sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panMenu2sc.setPreferredSize(new Dimension((int)this.getSize().width-10,(int)this.getSize().height-80));
		panMenu2sc.setAutoscrolls(true);

		DefaultCaret caret = (DefaultCaret)textexec.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		panMenu2.add(panMenu2sc);

		findFilesRecursively(new File(cheminSource), all, ".gdfx");

		for(File file11 : all){
			this.synoptiques.add(new Synoptique(file11.getName().substring(0, file11.getName().toString().length() - 5),file11.getAbsolutePath()));
		}
		System.out.println(this.synoptiques.size());
		for (int i = 0;i<this.synoptiques.size();i++){

			if(this.synoptiques.get(i).getName().toString().equals("Symboles OptimisesV2")) {
				this.synoptiques.get(i).setReference(true);
				this.ref = i;
			}

			this.synoptiques.get(i).findSmartSymbols();
			textexec.append("Synoptique " + this.synoptiques.get(i).getName().toString() + ", " + this.synoptiques.get(i).getNbSSIn() + " smart symboles, " + this.synoptiques.get(i).getNbTagsIn() + " tags.\n");
			reprintPanPrin(panMenu2);
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
			panMenu0.setVisible(false);
			panMenu1.setVisible(true);
			panMenu2.setVisible(false);
			panMenu3.setVisible(false);
			panMenu4.setVisible(false);
			panMenu5.setVisible(false);
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
	/*class ExecAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			panMenu2.removeAll();
			itemnav = 2;
			menu1.setText(item2.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(false);
			panMenu2.setVisible(true);
			panMenu3.setVisible(false);
			panMenu4.setVisible(false);
			panMenu5.setVisible(false);
			execAction();
		}
	}*/

	//Listener sur l'item 3 du menu
	//En fonction du menu appelant, exécute l'une ou l'autre des fonctions
	class RempAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 3;
			menu1.setText(item3.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(false);
			panMenu2.setVisible(false);
			panMenu3.setVisible(true);
			panMenu4.setVisible(false);
			panMenu5.setVisible(false);
			panMenu3.setLayout(new BoxLayout(panMenu3,BoxLayout.PAGE_AXIS));
			getSymboles();
			panMenu3.add(pansyno);
			panMenu3.add(panpropt);
			reprintPanPrin(panMenu3);
		}
	}

	class ControleAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 4;
			menu1.setText(item4.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(false);
			panMenu2.setVisible(false);
			panMenu3.setVisible(false);
			panMenu4.setVisible(true);
			panMenu5.setVisible(false);
			panMenu4.setLayout(new BoxLayout(panMenu4,BoxLayout.PAGE_AXIS));
			getControle();
		}
	}

	class CrossRefAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 5;
			menu1.setText(item5.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(false);
			panMenu2.setVisible(false);
			panMenu3.setVisible(false);
			panMenu4.setVisible(false);
			panMenu5.setVisible(true);
			panMenu5.setLayout(new BoxLayout(panMenu5,BoxLayout.PAGE_AXIS));
			showComplete();
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

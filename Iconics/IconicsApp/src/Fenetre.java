import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
//import java.awt.event.ComponentEvent;
//import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 8134034113775260241L;
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	//Param�tres de la fen�tre d'application
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	private double height = screenSize.getHeight();

	//Param�tres des chemins des folders de l'application + liste des files
	String cheminSource = "";
	String cheminDestination = "";
	final Collection<File> all = new ArrayList<File>();

	//Param�tres des objets du menu
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu1 = new JMenu("Fichier");
	private JMenu menu2 = new JMenu("Recherche");
	private JMenuItem item1 = new JMenuItem("Visualiser");
	private JMenuItem item3 = new JMenuItem("Remplacer");
	private JMenuItem item4 = new JMenuItem("Controler");
	private JMenuItem item5 = new JMenuItem("Cross References");
	private JMenuItem item11 = new JMenuItem("Recherche simple");
	private JMenuItem item12 = new JMenuItem("Search & Replace simple");
	private JMenuItem item13 = new JMenuItem("Search & Replace compose");
	private int itemnav = 0;

	//Panel d'affichage des donn�es
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
	private JPanel panMenu11 = new JPanel();
	private JPanel panMenu12 = new JPanel();
	private JPanel panMenu13 = new JPanel();

	//Param�tres d'utilisation globale
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
	private ListSelectionModel ListSelectionModel1;
	private JTextField text = new JTextField();
	private JTextField text5 = new JTextField();
	private JTextArea textSR = new JTextArea();
	JCheckBox check1 = new JCheckBox();
	JCheckBox check2 = new JCheckBox();

    private int ref = 0;

	//Constructeur
	public Fenetre(String chemin) {
		this.setTitle("Utilitaires pour l'application Iconics");
		//this.setSize((int)width/3, (int)height);
		this.setSize(880,805);
		this.setLocation(((int)width/2)-440, ((int)height/2)-402);
		this.window.setLocation((int)width-970, 0);
		//this.addComponentListener(new componentAction());
		this.cheminDestination = (chemin);
		this.cheminSource = (chemin);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initialize1();
	}

	//Initialisation de l'affichage
	public void initialize1() {
		//Chargement des items par lecture des fichiers de l'application
		this.setContentPane(panPrin);
		this.setVisible(true);

		JOptionPane jop = new JOptionPane();   
		int option = jop.showConfirmDialog(null, "Pour que l'utilitaire fonctionne avec la derniere version des synoptiques,\n il est necessaire d'effectuer une recherche complete. Souhaitez vous demarrer la recherche ?", "Demarrage de l'utilitaire", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(option == JOptionPane.OK_OPTION){
			execAction();
		}
		else {
			System.exit(0);
		}
		
		//execAction();

		//Ajouts des noms des synoptiques pour acc�s liste
		for(int i=0;i<synoptiques.size();i++) {
			syno.addItem(synoptiques.get(i).getName().toString());
		}
		itemnav=1;

		//Listener sur le bouton liste synoptique pour MAJ de la liste des smarts symbols
		syno.addActionListener(new SynoAction());
		syno.setSelectedItem(1);
		//R�cup�ration des symboles en fonction du choix utilisateur
		getSymboles();
		//Listener sur le bouton liste smart symboles pour MAJ des propri�t�s
		symb.addActionListener(new SmartAction());
		symb.setSelectedItem(1);
		//R�cup�ration des propri�t�s du symbole en fonction du choix utilisateur
		getProperties();

		//Param�trage de l'affichage du panel synoptique
		pansyno.setLayout(new BoxLayout(pansyno, BoxLayout.LINE_AXIS));
		pansyno.setPreferredSize(new Dimension((new Dimension(this.getSize())).width-10,30));
		pansyno.add(syno);

		//Param�trage de l'affichage du panel symbole
		pansymb.setLayout(new BoxLayout(pansymb, BoxLayout.LINE_AXIS));
		pansymb.setPreferredSize(new Dimension((new Dimension(this.getSize())).width-10,30));
		pansymb.add(symb);

		//Param�trage de l'affichage du panel propri�t�s
		panpropt.setLayout(new BoxLayout(panpropt,BoxLayout.LINE_AXIS));
		panprop.setLayout(new BoxLayout(panprop,BoxLayout.PAGE_AXIS));
		panprop1.setLayout(new BoxLayout(panprop1,BoxLayout.PAGE_AXIS));
		panprop2.setLayout(new BoxLayout(panprop2,BoxLayout.PAGE_AXIS));
		panMap.setLayout(new BoxLayout(panMap,BoxLayout.PAGE_AXIS));
		//Ajout des items du menu au bandeau de menu de la fen�tre
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
		menu2.add(item11);
		item11.addActionListener(new ResearchAction());
		menu2.add(item12);
		item12.addActionListener(new SaRSimpleAction());
		menu2.add(item13);
		item13.addActionListener(new SaRMultiAction());
		menuBar.add(menu2);
		this.setJMenuBar(menuBar);

		itemnav = 0;
		accueil();
		
		//Affectation de panPrin au contenu de la fen�tre

	}

	private void accueil() {
		clearAll();
		ImageIcon image = new ImageIcon("/Users/jeanhourmant/Desktop/Actemium.png");
		JLabel label = new JLabel("", image, JLabel.CENTER);
		panMenu0.add(label, BorderLayout.CENTER);
		panMenu0.setVisible(true);
		reprintPanPrin(panMenu0);
	}

	//Fonction de nettoyage des panels et listes utilis�s
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
	//- Supprime le contenu des diff�rents panels pour mise � jour
	//- Recherche les symboles du synoptique selectionn�
	//- Appelle la fonction rempAction() si l'item3 du menu est selectionn�
	//- Appelle la fonction getProperties() si l'item1 du menu est selectionn�
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
	//- Supprime le contenu des diff�rents panels pour mise � jour
	//- Recherche toutes les propri�t�s du smart symbole selectionn�
	//- Ajoute le tag associ� au smart symbole au panel pour l'affichage
	//- Ajoute les propri�t�s aux panels pour l'affichage
	//- Set les param�tres des pannels
	//- Mets � jour le panel principal
	private void getProperties() {
			clearAll();
			prop1.add(new JTextField("Tag associe :"));
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
	//- Supprime le contenu des diff�rents panels pour mise � jour
	//- Ajoute trois colonnes avec les informations suivantes :
	//		1- Le keyword et le nom de tous les symboles recenss� pour le synoptique selectionn�
	//		2- Le tag associ� au smart symbol
	//		3- Une colonne vide pour indiquer le nouveau tag du smart symbole (remplace alors le tag associ� par ce dernier)
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
		Point p = new Point((int)this.width-970, 0);
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
		//S�lection de la r�f�rence sur le synoptique Symboles Optimis�sV2 -> Ajouter aux param�tres
		for(int i=0;i<this.synoptiques.size();i++) {
				this.synoptiques.get(i).controlQuality(this.synoptiques.get(ref).getListeK(), this.synoptiques.get(ref).getListeC());
		}
		if (itemnav==4) showQualityBySyno();
	}

	private void showQualityBySyno() {
		panMenu4.removeAll();
		JTextArea textQuality = new JTextArea();
		
		textQuality.selectAll();
		textQuality.replaceSelection("");
		for (int i=0;i<this.synoptiques.size();i++) {
			textQuality.append("\n" + this.synoptiques.get(i).getName().toString());
			if(this.synoptiques.get(i).getListeInval().size()>0) {
				for(int j=0;j<this.synoptiques.get(i).getListeInval().size();j++) textQuality.append(";" + this.synoptiques.get(i).getListeInval().get(j).toString());
			}
			else textQuality.append(";");
		}
		Date date = new Date();
		String datej = sdf.format(date).toString().replaceAll(":", "_");
		datej = datej.replaceAll("/", "_");
		String fileName = "Controle_Qualite_" + datej + ".csv";
        try {
            FileWriter fileWriter = new FileWriter(this.cheminDestination + fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            textQuality.selectAll();
            bufferedWriter.write(textQuality.getSelectedText());
            bufferedWriter.close();
            JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a ete ajoute au dossier :\n" + this.cheminDestination, "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '"+ fileName + "'");
        }
		JScrollPane panMenu4sc = new JScrollPane(textQuality);
		panMenu4sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panMenu4.setPreferredSize(new Dimension((int)this.getSize().width-10,(int)this.getSize().height-80));
		panMenu4.add(panMenu4sc);
		reprintPanPrin(panMenu4);
	}
	/*
	private void showQualityBySmart() {
		String[] listData = null;
		for(int i = 0; i < this.synoptiques.size(); i++) listData[i]=this.synoptiques.get(i).getName().toString();
		JList list = new JList(listData);
		ListSelectionModel1 = list.getSelectionModel();
		ListSelectionModel1.addListSelectionListener(
				new SharedListSelectionHandler());
		JScrollPane listPane = new JScrollPane(list);
		
		textQuality.selectAll();
		textQuality.replaceSelection("");
		for(String getK : this.synoptiques.get(ref).getListeK()) textQuality.append(";" + getK.toString());
		
		for(Synoptique getS : this.synoptiques) {
			textQuality.append("\n" + getS.getName().toString());
			for(String getK : this.synoptiques.get(ref).getListeK()) {
				if(getS.getListeInval().contains(getK.toString())) textQuality.append(";X");
				else textQuality.append(";");
			}
		}
		/*for(String getK : this.synoptiques.get(ref).getListeK()) {
			textQuality.append("\n" + getK.toString());
			for(Synoptique getSyn : this.synoptiques) {
				if(getSyn.getListeInval().contains(getK.toString())) textQuality.append(";" + getSyn.getName().toString());
				else textQuality.append(";");
			}
		}
		JScrollPane panMenu4sc = new JScrollPane(textQuality);
		panMenu4sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panMenu4.setPreferredSize(new Dimension((int)this.getSize().width-10,(int)this.getSize().height-80));
		panMenu4.add(pansyno);
		panMenu4.add(panMenu4sc);
		reprintPanPrin(panMenu4);
	}*/

	private void showComplete() {
		panMenu5.removeAll();
		JTextArea textComplet = new JTextArea();
		
		getControle();
		String API = "";
		String CHMACQ = "";
		String CHMALM = "";
		textComplet.selectAll();
		textComplet.replaceSelection("");
		textComplet.append("Nom du synoptique;Nom du symbole;Valide;Tag Associe;ShareKeyword;CustomData de reference;CustomData du synoptique;Position X;Position Y;API Associe;CHM_ACQ Associe;CHM_ALM Associe");
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
		String fileName = "Cross_References_" + datej + ".csv";
        try {
            FileWriter fileWriter = new FileWriter(this.cheminDestination + fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            textComplet.selectAll();
            bufferedWriter.write(textComplet.getSelectedText());
            bufferedWriter.close();
            JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a ete ajoute au dossier :\n" + this.cheminDestination, "Information", JOptionPane.INFORMATION_MESSAGE);
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
	//- Ajouter � la liste les nouveaux synoptiques
	//- Effectue la recherche compl�te sur chacun des fichiers (smart symboles et propri�t�s associ�es pour chaque item)
	//- En sortie, nous avons tous les objects Synoptique, Tag, SmartSymbol et Property n�cessaire � l'application
	private void execAction() {
		JTextArea textexec = new JTextArea();
		
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
		getControle();
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
	
	private void Research(JPanel pan0) {
		pan0.removeAll();
		JScrollPane panMenu2sc = new JScrollPane(textexec);

		panMenu2sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panMenu2sc.setPreferredSize(new Dimension((int)this.getSize().width-10,(int)this.getSize().height-400));
		panMenu2sc.setAlignmentY((float)this.getSize().height);
		panMenu2sc.setAutoscrolls(true);

		DefaultCaret caret = (DefaultCaret)textexec.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.LINE_AXIS));
		pan.setPreferredSize(new Dimension((this.getSize()).width-10,30));
		JTextField text0 = new JTextField();
		text0.setText("Texte a rechercher :");
		text0.setBackground(null);
		text0.setEnabled(false);
		text0.setPreferredSize(new Dimension(((this.getSize()).width)/3,30));
		text.setPreferredSize(new Dimension(2*((this.getSize()).width)/3,30));

		JPanel pan1 = new JPanel();
		pan1.setLayout(new BoxLayout(pan1, BoxLayout.LINE_AXIS));
		pan1.setPreferredSize(new Dimension((this.getSize()).width-10,30));
		JTextField text1 = new JTextField();
		text1.setText("Texte de remplacement :");
		text1.setBackground(null);
		text1.setEnabled(false);
		text1.setPreferredSize(new Dimension(((this.getSize()).width)/3,30));
		text5.setPreferredSize(new Dimension(2*((this.getSize()).width)/3,30));

		JButton bouton = new JButton();
		bouton.setPreferredSize(new Dimension(90,30));
		if(itemnav==12) bouton.setText("Rechercher & Remplacer");
		else bouton.setText("Rechercher");
		bouton.addActionListener(new RechercheAction());
		
		JPanel pan2 = new JPanel();
		pan2.setLayout(new BoxLayout(pan2, BoxLayout.LINE_AXIS));
		if(itemnav == 11) pan2.setPreferredSize((new Dimension((int)this.getSize().width-10,(int)this.getSize().height-550)));
		else if (itemnav == 12) pan2.setPreferredSize((new Dimension((int)this.getSize().width-10,(int)this.getSize().height-640)));

		pan.add(text0);
		pan.add(text);
		pan0.add(pan,BorderLayout.NORTH);
		if(itemnav == 12) {
			check1.setText("Application Complete");
			check2.setText("Selection de Synoptiques");
			check1.addActionListener(new StateListener());
			check2.addActionListener(new StateListener());
			check1.setPreferredSize(new Dimension(1*((this.getSize()).width)/3,30));
			check2.setPreferredSize(new Dimension(1*((this.getSize()).width)/3,30));

			pan1.add(text1);
			pan1.add(text5);
			pan0.add(pan1);
			pan0.add(check1);
			pan0.add(check2);
		}

		pan0.add(bouton);
		pan0.add(pan2);
		pan0.add(panMenu2sc);
		reprintPanPrin(pan0);
	}

	class RechercheAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int answer = 1;
			String line;
			int reponse = 1;
			int start = 1;
			int modif = 0;
			textexec.selectAll();
			textexec.setText("");
			if(itemnav == 11) {
				if(text.getText().isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "Saisissez un texte à rechercher.", "Information", JOptionPane.ERROR_MESSAGE);
				}
				else {
					for(File file11 : all){
						try {
							String fileContent = readFileAsString(file11.getPath());
							if (text.getText().isEmpty() == false && fileContent.contains(text.getText()) == true) {
								textexec.append(file11.getName() + "\n");
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					if(itemnav == 11) {
						panMenu11.repaint();
						panMenu11.revalidate();
					}
				}
			}
			else if (itemnav == 12) {
				if(text.getText().isEmpty() == true) {
					JOptionPane.showMessageDialog(null, "Saisissez un texte a rechercher.", "Information", JOptionPane.ERROR_MESSAGE);
				}
				if(text.getText().isEmpty() == false && text5.getText().isEmpty() == true && (check1.isSelected() == true || check2.isSelected() == true)) {
					answer = JOptionPane.showConfirmDialog(null, "Etes vous sur de vouloir remplacer par un texte vide ?", "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				}
				if(answer == 0 || (text5.getText().isEmpty() == false && answer == 1 && text.getText().isEmpty() == false)) {
					for(File file11 : all){
						try {
							textexec.setLineWrap(true);
							Path path = Paths.get(file11.getPath());
							Charset charset = StandardCharsets.UTF_8;
							String content = new String(Files.readAllBytes(path), charset);
							if(content.contains(text.getText())) {
								if(start == 1) {
									BufferedReader brTest = new BufferedReader(new FileReader(file11.getPath()));
								    while (!((line = brTest.readLine()).contains(text.getText()))) {
								    }
									textexec.append(line);
									start = 0;
									reponse = JOptionPane.showConfirmDialog(null, "Etes vous sur de valider le remplacement ?", "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
									textexec.selectAll();
									textexec.setText("");
								}
								if(start == 0 && reponse == 0)
								{
										content = content.replaceAll(text.getText(), text5.getText());
										Files.write(path, content.getBytes(charset));	
										textexec.append(file11.getName() + " modifié.\n");
										modif++;
								}
							}
							else if (content.contains(text.getText()) == false && modif == 0) {
								textexec.selectAll();
								textexec.setText("Aucune occurence trouvée");
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			}
		}
	}
	
	class StateListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(check1.isSelected()) {
				check2.setSelected(false);
				check2.setEnabled(false);
			}
			else if(check2.isSelected()) {
				check1.setSelected(false);
				check1.setEnabled(false);
			}
			else {
				check1.setEnabled(true);
				check2.setEnabled(true);
			}
		}
	}

	//Listener sur la ComboBox listant les synoptiques
	//En fonction du menu appelant, ex�cute l'une ou l'autre des fonctions
	class SynoAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			getSymboles();
		}
	}

	//Listener sur la ComboBox listant les smart symboles
	//Execute la fonction getProperties uniquement si item 1 du menu en s�lection active
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
	//En fonction du menu appelant, ex�cute l'une ou l'autre des fonctions
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
			panMenu11.setVisible(false);
			panMenu12.setVisible(false);
			panMenu13.setVisible(false);
			panMenu1.setLayout(new BoxLayout(panMenu1,BoxLayout.PAGE_AXIS));
			getSymboles();
			panMenu1.add(pansyno);
			panMenu1.add(pansymb);
			panMenu1.add(panpropt);
			reprintPanPrin(panMenu1);
			setMenuColor();
		}
	}

	//Listener sur l'item 2 du menu
	//Permet la mise � jour du Panel pour cette action
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
	//En fonction du menu appelant, ex�cute l'une ou l'autre des fonctions
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
			panMenu11.setVisible(false);
			panMenu12.setVisible(false);
			panMenu13.setVisible(false);
			panMenu3.setLayout(new BoxLayout(panMenu3,BoxLayout.PAGE_AXIS));
			getSymboles();
			panMenu3.add(pansyno);
			panMenu3.add(panpropt);
			reprintPanPrin(panMenu3);
			setMenuColor();
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
			panMenu11.setVisible(false);
			panMenu12.setVisible(false);
			panMenu13.setVisible(false);
			panMenu4.setLayout(new BoxLayout(panMenu4,BoxLayout.PAGE_AXIS));
			getControle();
			setMenuColor();
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
			panMenu11.setVisible(false);
			panMenu12.setVisible(false);
			panMenu13.setVisible(false);
			panMenu5.setLayout(new BoxLayout(panMenu5,BoxLayout.PAGE_AXIS));
			showComplete();
			setMenuColor();
		}
	}
	
	class ResearchAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 11;
			menu2.setText(item11.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(false);
			panMenu2.setVisible(false);
			panMenu3.setVisible(false);
			panMenu4.setVisible(false);
			panMenu5.setVisible(false);
			panMenu11.setVisible(true);
			panMenu12.setVisible(false);
			panMenu13.setVisible(false);
			panMenu11.setLayout(new BoxLayout(panMenu11,BoxLayout.PAGE_AXIS));
			Research(panMenu11);
			setMenuColor();
		}
	}

	class SaRSimpleAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 12;
			menu2.setText(item12.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(false);
			panMenu2.setVisible(false);
			panMenu3.setVisible(false);
			panMenu4.setVisible(false);
			panMenu5.setVisible(false);
			panMenu11.setVisible(false);
			panMenu12.setVisible(true);
			panMenu13.setVisible(false);
			panMenu12.setLayout(new BoxLayout(panMenu12,BoxLayout.PAGE_AXIS));
			Research(panMenu12);
			setMenuColor();

		}
	}

	class SaRMultiAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 13;
			menu2.setText(item13.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(false);
			panMenu2.setVisible(false);
			panMenu3.setVisible(false);
			panMenu4.setVisible(false);
			panMenu5.setVisible(false);
			panMenu11.setVisible(false);
			panMenu12.setVisible(false);
			panMenu13.setVisible(true);
			panMenu13.setLayout(new BoxLayout(panMenu13,BoxLayout.PAGE_AXIS));
			setMenuColor();


		}
	}
	
	public void setMenuColor() {
		if (itemnav<10){
			menu1.setForeground(Color.RED);
			menu2.setForeground(Color.BLACK);
		}
		else {
			menu2.setForeground(Color.RED);
			menu1.setForeground(Color.BLACK);
		}
	}

	public void reprintPanPrin(JPanel pan) {
		panPrin.removeAll();
		panPrin.add(pan);
		panPrin.repaint();
		panPrin.revalidate();
	}
	
	class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) { 
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
 
            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting(); 
 
            if (lsm.isSelectionEmpty()) {

            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {

                    }
                }
            }

        }
    }
	
	private static String readFileAsString(String filePath) throws java.io.IOException{

		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = null;

		try {
			f = new BufferedInputStream(new FileInputStream(filePath));
			f.read(buffer);
		} finally {
			if (f != null) try { f.close(); } catch (IOException ignored) { }
		}
		return new String(buffer);
	}

	//Fonction findFilesRecursively
	//- Recherche selon une m�thode r�cursive tous les fichiers .gdfx contenu dans le rep�rtoire cheminSource
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
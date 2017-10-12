import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Thread.State;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.ScrollPaneConstants;

import javax.swing.text.DefaultCaret;

import org.apache.commons.lang3.StringUtils;


public class Fenetre extends JFrame {
	private static final long serialVersionUID = -3504631783859510644L;

	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	private double height = screenSize.getHeight();

	String cheminSource = "";
	String cheminDestination = "";
	final Collection<File> all = new ArrayList<File>();
	final Collection<File> allPop = new ArrayList<File>();

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu1 = new JMenu("Fichier");
	private JMenu menu2 = new JMenu("Recherche");
	private JMenuItem item1 = new JMenuItem("Visualiser");
	private JMenuItem item5 = new JMenuItem("Cross References");
	private JMenuItem item11 = new JMenuItem("Recherche simple");
	private JMenuItem item12 = new JMenuItem("Search & Replace simple");
	private int itemnav = 0;

	private JPanel panPrin = new JPanel();
	private JPanel pansyno = new JPanel();
	private JPanel pansymb = new JPanel();
	private JPanel panprop = new JPanel();
	private JPanel panprop1 = new JPanel();
	private JPanel panprop2 = new JPanel();
	private JPanel panpropt = new JPanel();
	private JPanel panMenu0 = new JPanel();
	private JPanel panMenu1 = new JPanel();
	private JPanel panMenu2 = new JPanel();
	private JPanel panMenu5 = new JPanel();
	private JPanel panMenu11 = new JPanel();
	private JPanel panMenu12 = new JPanel();

	private List<Synoptique> synoptiques = new ArrayList<Synoptique>();
	private JComboBox<String> syno = new JComboBox<String>();
	private JComboBox<String> symb = new JComboBox<String>();
	private List<JTextField> prop = new ArrayList<JTextField>();
	private List<JTextField> prop1 = new ArrayList<JTextField>();
	private List<JTextField> prop2 = new ArrayList<JTextField>();
	private JTextArea textexec = new JTextArea();
	private Font font1 = new Font("SansSerif", Font.BOLD, 10);
	private JFrame window = new JFrame();
	private JTextField text = new JTextField();
	private JTextField text5 = new JTextField();
	private JCheckBox check1 = new JCheckBox();
	private JCheckBox check2 = new JCheckBox();
	
	private List<ThreadExec> listeExec = new ArrayList<ThreadExec>();

    private int ref = 0;

	public Fenetre(String chemin) {
		this.setTitle("Utilitaires pour l'application Iconics");
		this.setSize(880,805);
		this.setLocation(((int)width/2)-440, ((int)height/2)-402);
		this.window.setLocation((int)width-970, 0);
		int lio = chemin.lastIndexOf("/");
		this.cheminDestination = chemin.substring(0,lio) + "/";
		this.cheminSource = (chemin);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initialize1();
	}

	//Initialisation de l'affichage
	public void initialize1() {
		this.setContentPane(panPrin);
		this.setVisible(true);

		int option = JOptionPane.showConfirmDialog(null, "Pour que l'utilitaire fonctionne avec la derniere version des synoptiques,\n il est necessaire d'effectuer une recherche complete. Souhaitez vous demarrer la recherche ?", "Demarrage de l'utilitaire", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(option == JOptionPane.OK_OPTION){
			execAction();
		}
		else {
			System.exit(0);
		}

		for(int i=0;i<synoptiques.size();i++) {
			syno.addItem(synoptiques.get(i).getName().toString());
		}
		itemnav=1;

		syno.addActionListener(new SynoAction());
		syno.setSelectedItem(1);
		getSymboles();
		symb.addActionListener(new SmartAction());
		symb.setSelectedItem(1);
		getProperties();

		pansyno.setLayout(new BoxLayout(pansyno, BoxLayout.LINE_AXIS));
		pansyno.setPreferredSize(new Dimension((new Dimension(this.getSize())).width-10,30));
		pansyno.add(syno);

		pansymb.setLayout(new BoxLayout(pansymb, BoxLayout.LINE_AXIS));
		pansymb.setPreferredSize(new Dimension((new Dimension(this.getSize())).width-10,30));
		pansymb.add(symb);

		panpropt.setLayout(new BoxLayout(panpropt,BoxLayout.LINE_AXIS));
		panprop.setLayout(new BoxLayout(panprop,BoxLayout.PAGE_AXIS));
		panprop1.setLayout(new BoxLayout(panprop1,BoxLayout.PAGE_AXIS));
		panprop2.setLayout(new BoxLayout(panprop2,BoxLayout.PAGE_AXIS));
		
		menu1.add(item1);
		item1.addActionListener(new VisuAction());
		menu1.add(item5);
		item5.addActionListener(new CrossRefAction());
		menuBar.add(menu1);
		menu2.add(item11);
		item11.addActionListener(new ResearchAction());
		menu2.add(item12);
		item12.addActionListener(new SaRSimpleAction());
		menuBar.add(menu2);
		this.setJMenuBar(menuBar);
		itemnav = 0;
		accueil();
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

	private void getSymboles() {
		clearAll();
		symb.removeAllItems();
		for(int i=0;i<synoptiques.get(syno.getSelectedIndex()).getNbSSIn();i++) {
			symb.addItem(synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getKeyword().toString() + " - " + synoptiques.get(syno.getSelectedIndex()).getSmartS(i).getName().toString());
		}
		pansymb.repaint();
		pansymb.revalidate();
		if (itemnav == 1) getProperties();
	}

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

	private void getControle() {
		for(int i=0;i<this.synoptiques.size();i++) {
				this.synoptiques.get(i).controlQuality(this.synoptiques.get(ref).getListeK(), this.synoptiques.get(ref).getListeC());
		}
	}

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

	private void execAction() {
		ObjectOutputStream oos;
		ObjectInputStream ois;
		JProgressBar pbar = new JProgressBar();
		
		findFilesRecursively(new File(cheminSource), all, ".gdfx");

		for(File file11 : all){
			this.synoptiques.add(new Synoptique(file11.getName().substring(0, file11.getName().toString().length() - 5),file11.getAbsolutePath()));
		}
		getControle();
		
		
		pbar.setMinimum(0);
		pbar.setMaximum(this.synoptiques.size());
		panMenu2.add(pbar);
		reprintPanPrin(panMenu2);
		
		int j = 0;
		while(j<this.synoptiques.size()) {
			if(this.synoptiques.get(j).getName().toString().equals("Symboles OptimisesV2")) {
				this.synoptiques.get(j).setReference(true);
				this.ref = j;
			}
			
			if (listeExec.size()<4) {
				listeExec.add(this.synoptiques.get(j).alternativeFindSmart());
				pbar.setValue(j);
				j++;
			}
			else {
				for(int i=0;i<listeExec.size();i++) {
					if (listeExec.get(i).getState()==State.TERMINATED) {
						try {
							listeExec.get(i).join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						listeExec.remove(i);
					}
				}
			}
		}
		while(listeExec.isEmpty()==false) {
			for(int i=0;i<listeExec.size();i++) {
				if (listeExec.get(i).getState()==State.TERMINATED) {
					try {
						listeExec.get(i).join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					listeExec.remove(i);
				}
			}
		}

		try {
			oos = new ObjectOutputStream( new BufferedOutputStream ( new FileOutputStream( new File(cheminSource + "_backup_smarts.txt"))));
			for(int i =0;i<this.synoptiques.size();i++) {
				oos.writeObject(this.synoptiques.get(i));
			}
			oos.close();
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new  File(cheminSource + "_backup_smarts.txt"))));
			for(int i=0;i<this.synoptiques.size();i++) {
				try {
					System.out.println(((Synoptique)ois.readObject()));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			ois.close();
		}catch (IOException e) {
			e.printStackTrace();
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
		JButton bouton1 = new JButton();
		bouton1.setPreferredSize(new Dimension(90,30));
		if(itemnav==12) bouton.setText("Rechercher & Remplacer");
		else bouton.setText("Rechercher");
		bouton.addActionListener(new RechercheAction());
		bouton1.addActionListener(new RecherchePopUpAction());
		
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
		if(itemnav==11) pan0.add(bouton1);
		pan0.add(pan2);
		pan0.add(panMenu2sc);
		reprintPanPrin(pan0);
	}
	
	class RecherchePopUpAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(new File(cheminSource + "/PopUp/").exists()) {
				ArrayList<String> str = new ArrayList<String>();
				ArrayList<String> listePop = new ArrayList<String>();
				findFilesRecursively(new File(cheminSource + "/PopUp/"),allPop,".gdfx");
				int i = 0;
				for(File file12 : allPop) {
					listePop.add(file12.getName().toString());
					str.add(file12.getName().toString());
				}
				
				textexec.selectAll();
				textexec.setText("");
				try {
					//INIT
					
					Date date = new Date();
					String datej = sdf.format(date).toString().replaceAll(":", "_");
					datej = datej.replaceAll("/", "_");
					String fileName = "Cross_References_PopUp_" + datej + ".csv";
					FileWriter fileWriter;
					fileWriter = new FileWriter(cheminDestination + fileName, true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					
					//CONTROLES ET ECRITURE
					for(File file11 : all){
						i = 0;
						Path path = Paths.get(file11.getPath());
						Charset charset = StandardCharsets.UTF_8;
						String content = new String(Files.readAllBytes(path), charset);
						for(String strin : listePop) {
							if(content.contains(strin.substring(0, strin.length()-5))) {
								str.set(i,str.get(i).toString() + ";" + file11.getName().toString());
							}
							i++;
						}
					}
					
					for(String stri : str) {
						int j = StringUtils.countMatches(stri,";");
						textexec.append(j + ";" + stri + "\n");
					}
					
					textexec.selectAll();
		            bufferedWriter.write(textexec.getSelectedText());
		            
					//FERMETURE
					bufferedWriter.close();
					JOptionPane.showMessageDialog(null, "Le fichier " + fileName + " a ete ajoute au dossier :\n" + cheminDestination, "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(IOException ex) {
					System.out.println("Error writing to file");
				}
			}
		}
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
									brTest.close();
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

	//Listener sur l'item 1 du menu
	//En fonction du menu appelant, ex�cute l'une ou l'autre des fonctions
	class VisuAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			itemnav = 1;
			menu1.setText(item1.getText());
			panMenu0.setVisible(false);
			panMenu1.setVisible(true);
			panMenu2.setVisible(false);
			panMenu5.setVisible(false);
			panMenu11.setVisible(false);
			panMenu12.setVisible(false);
			panMenu1.setLayout(new BoxLayout(panMenu1,BoxLayout.PAGE_AXIS));
			getSymboles();
			panMenu1.add(pansyno);
			panMenu1.add(pansymb);
			panMenu1.add(panpropt);
			reprintPanPrin(panMenu1);
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
			panMenu5.setVisible(true);
			panMenu11.setVisible(false);
			panMenu12.setVisible(false);
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
			panMenu5.setVisible(false);
			panMenu11.setVisible(true);
			panMenu12.setVisible(false);
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
			panMenu5.setVisible(false);
			panMenu11.setVisible(false);
			panMenu12.setVisible(true);
			panMenu12.setLayout(new BoxLayout(panMenu12,BoxLayout.PAGE_AXIS));
			Research(panMenu12);
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
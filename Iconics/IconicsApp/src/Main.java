import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Test;


public class Main {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("");
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Sélection du repértoire des sources .gdfx");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		frame.getContentPane().add(chooser,"Center");
		frame.setSize(chooser.getPreferredSize());

		
		if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			String str = chooser.getSelectedFile().getAbsolutePath();
			System.out.println(str);
			File f = new File(str);
			String str2 = "Le dossier \n" + str + "\n" + " est vide.\nPar conséquent, l'utilitaire ne peut pas fonctionner.\nPlacez-y les sources .gdfx de l'application Iconics et relancez l'utilitaire.";
			String str3 = "Le dossier \n" + str + "\n" + " ne contient pas de gdfx.\nPar conséquent, l'utilitaire ne peut pas fonctionner.\nPlacez-y les sources .gdfx de l'application Iconics et relancez l'utilitaire.";
			if (f.list().length == 0) {
				JOptionPane.showMessageDialog(null,str2, "Information", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			else if (f.list().length > 0) {
				File[] files = new File(str).listFiles();
				int gdfx=0;
				for (File file : files){
					if(file.isFile() && file.getName().contains(".gdfx"))  gdfx++;
				}
				if (gdfx == 0) {
					JOptionPane.showMessageDialog(null,str3, "Information", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				else {
					frame.setVisible(false);
					frame.removeAll();
					Fenetre fenetre = new Fenetre(str);
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(null,"Aucun fichier selectionné", "Information", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		
		

	//	bw.close();
	//	GenerateDoc doc = new GenerateDoc("Test");
	//	doc.toExcel(data);
	}

	//Instance permettant de rechercher selon une mï¿½thode rï¿½cursive les fichiers d'un dossier.

	//-----------------------------------------------------------------------------------------------------	
}

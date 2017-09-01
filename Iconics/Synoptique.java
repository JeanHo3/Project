import java.io.BufferedInputStream;
//import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

public class Synoptique {
	private String name;
	private String path;
	private int nbTagsIn;
	private int nbSmartSymbolsIn;
	private List<SmartSymbol> smartSymbolsIn = new ArrayList<SmartSymbol>();
	private List<Tag> tagsIn = new ArrayList<Tag>();
	private static int id;
	private int idp;

	public Synoptique() {	//Création synoptique simple (non renseigné)
		this.name = "Default";
		this.path = "Default";
		this.nbTagsIn = 0;
		this.nbSmartSymbolsIn = 0;
		this.smartSymbolsIn = null;
		this.tagsIn = null;
		id++;
		this.idp = id;
	}

	public Synoptique(String pname, String ppath) {	//Création synoptique complexe (renseigné)
		this.name = pname;
		this.path = ppath;
		id++;
		this.idp = id;
	}

	public void addTagsIn (String pMnemonique, String pApi, boolean pFromExcel, boolean isInfo) {	//Ajout d'un tag au synoptique par construction de l'objet
		this.tagsIn.add(new Tag(pMnemonique,pApi,pFromExcel,isInfo));
	}


	public void addSmartSymbolIn (String pName, String pKeyword, String pCustomData) {	//Ajout d'un smart symbol au synoptique par création de l'objet
		this.smartSymbolsIn.add(new SmartSymbol(pName,pKeyword,pCustomData));
		countSmartSymbolsIn();
	}

	public String getName() {	//Récupération du nom du Synoptique
		return this.name;
	}

	public String getPath() {	//Récupération du chemin source du Synoptique
		return this.path;
	}

	public void getNbTagsIn() {	//Récupération du nombre de tags présents sur le Synoptique
		int p = 0;
		for (int i=0;i<this.smartSymbolsIn.size();i++) {
			p = p + this.smartSymbolsIn.get(i).hasTag();
		}
		this.nbTagsIn = p;
	}

	public List<SmartSymbol> getListeSS(){
		return this.smartSymbolsIn;
	}

	public int getNbSSIn() {
		return this.nbSmartSymbolsIn;
	}

	//Methode rename 1 (basée sur le nom du smart existant Ico)
	public void autoRenameSmart(){
		for(int i=0;i<this.smartSymbolsIn.size();i++){
			//Substring du SK de l'objet (all before first " ") -> Rename avec ajout numérotation
		}
	}

	public void descriptionComplete(BufferedWriter bw) {
		try {
			getNbTagsIn();
			bw.write("\r\r\rSynoptique " + this.idp + " : " + this.name + ", " + this.nbSmartSymbolsIn + " smarts S, " + this.nbTagsIn + " tags.");
			bw.write("\r---------------------------");
			bw.write("\rListe des smarts symboles :");
			bw.write("\r---------------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0;i<this.smartSymbolsIn.size();i++) {
			String l = this.smartSymbolsIn.get(i).descriptionRet();
			String g = this.smartSymbolsIn.get(i).descriptionPropertiesRet();

			try {
				bw.write(l);
				bw.write(g);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void findSmartSymbols(JTextArea textarea) {
		try{
			String mnemo = "";
			String API = "";
			String fileContent = readFileAsString(this.path);
			Matcher l = Pattern.compile("<gwx:SmartSymbol Name=.+Keyword=.+>").matcher(fileContent);
			while (!(l.hitEnd()))   {
				if(l.find()) {
					int a = l.start();
					Matcher name = Pattern.compile("<gwx:SmartSymbol Name=\"(.*?)\"").matcher(l.group(0));
					Matcher customData = Pattern.compile("Tag=\"(.*?)\"").matcher(l.group(0));
					Matcher keyword = Pattern.compile("gwx:GwxProperties.Keyword=\"(.*?)\"").matcher(l.group(0));
					name.find();
					keyword.find();
					if(customData.find()) {
						addSmartSymbolIn (name.group(1),keyword.group(1),customData.group(1));
					}
					else {
						addSmartSymbolIn (name.group(1),keyword.group(1),"");
					}
					Matcher getProperties = Pattern.compile("</gwx:SmartSymbol.PropertyDefinitions>").matcher(fileContent);
					getProperties.find(a);
					int b = getProperties.start();
					String toProperties = fileContent.substring(a, b);
					this.smartSymbolsIn.get(smartSymbolsIn.size()-1).findProperties(toProperties);
					mnemo = this.smartSymbolsIn.get(smartSymbolsIn.size()-1).findTags();
					if (!(mnemo.equals(""))) {
						API = mnemo.substring(0, 3);
						mnemo = mnemo.substring(3);
						if (mnemo.length() == 22)
							this.tagsIn.add(new Tag(mnemo,API,false,false));
						if (mnemo.length() == 26)
							this.tagsIn.add(new Tag(mnemo,API,false,true));
					}
					Matcher getX = Pattern.compile("Canvas.Left=\"(.*?)\"").matcher(fileContent);
					Matcher getY = Pattern.compile("Canvas.Top=\"(.*?)\"").matcher(fileContent);
					getX.find();
					int c = getX.start();
					getY.find(c);
					this.smartSymbolsIn.get(smartSymbolsIn.size()-1).setgetX(getX.group(1));
					this.smartSymbolsIn.get(smartSymbolsIn.size()-1).setgetY(getY.group(1));
				}
			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	//	public Map<String, Object[]> addToDatas(Map<String, Object[]> data) {
	//		for(int i = 0;i<this.smartSymbolsIn.size();i++) {
	//			data.put(this.smartSymbolsIn.get(i).getName(), new Object[]{this.smartSymbolsIn.get(i).getId(), this.smartSymbolsIn.get(i).getName()});
	//		}
	//		return data;
	//	}


	private void countSmartSymbolsIn() {	//Fonction de comptage du nombre de Smart Symbol sur le Synoptique
		this.nbSmartSymbolsIn = smartSymbolsIn.size();
	}

	//Instance pour lire un fichier comme une chaine de caract�re (�quivalent tampon pour regex)
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
	//-----------------------------------------------------------------------------------------------------
}

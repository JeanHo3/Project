
import java.awt.Color;
import java.io.BufferedInputStream;
//import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Synoptique {
	private String name;
	private String path;
	private int nbTagsIn;
	private int nbSmartSymbolsIn;
	private List<SmartSymbol> smartSymbolsIn = new ArrayList<SmartSymbol>();
	private List<String> listeKeyword = new ArrayList<String>();
	private List<String> listeCustomData = new ArrayList<String>();
	private List<String> listeInvalide = new ArrayList<String>();
	private List<Tag> tagsIn = new ArrayList<Tag>();
	private static int id;
	private int idp;
	private CoreControl controlg = new CoreControl();
	private boolean isReference;

	public Synoptique() {	//Création synoptique simple (non renseigné)
		this.name = "Default";
		this.path = "Default";
		this.nbTagsIn = 0;
		this.nbSmartSymbolsIn = 0;
		this.smartSymbolsIn = null;
		this.tagsIn = null;
		id++;
		this.idp = id;
		this.isReference = false;
	}

	public Synoptique(String pname, String ppath) {	//Création synoptique complexe (renseigné)
		this.name = pname;
		this.path = ppath;
		id++;
		this.idp = id;
		this.isReference = false;
	}

	public List<String> getListeInval(){
		return this.listeInvalide;
	}

	public List<String> getListeK(){
		return this.listeKeyword;
	}

	public List<String> getListeC(){
		return this.listeCustomData;
	}

	public void addTagsIn (String pMnemonique, String pApi, boolean pFromExcel, boolean isInfo) {	//Ajout d'un tag au synoptique par construction de l'objet
		this.tagsIn.add(new Tag(pMnemonique,pApi,pFromExcel,isInfo));
	}


	public void addSmartSymbolIn (String pName, String pKeyword, String pCustomData) {	//Ajout d'un smart symbol au synoptique par création de l'objet
		this.smartSymbolsIn.add(new SmartSymbol(pName,pKeyword,pCustomData));
		countSmartSymbolsIn();
	}

	public void setReference(boolean ref) {
		this.isReference = ref;
	}

	public boolean getReference () {
		return this.isReference;
	}

	public CoreControl getControl() {
		return this.controlg;
	}

	public int getId() {
		return this.idp;
	}

	public String getName() {	//Récupération du nom du Synoptique
		return this.name;
	}

	public String getPath() {	//Récupération du chemin source du Synoptique
		return this.path;
	}

	public int getNbTagsIn() {	//Récupération du nombre de tags présents sur le Synoptique
		int p = 0;
		for (int i=0;i<this.smartSymbolsIn.size();i++) {
			p = p + this.smartSymbolsIn.get(i).hasTag();
		}
		this.nbTagsIn = p;
		return this.nbTagsIn;
	}

	public List<SmartSymbol> getListeSS(){
		return this.smartSymbolsIn;
	}

	public int getNbSSIn() {
		return this.nbSmartSymbolsIn;
	}

	public SmartSymbol getSmartS(int id) {
		return this.smartSymbolsIn.get(id);
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

	public void ajoutPoint() {
		for (int i=0;i<this.smartSymbolsIn.size();i++) {
			int l = (int)this.smartSymbolsIn.get(i).getX()/8;
			int m = (int)this.smartSymbolsIn.get(i).getY()/8;
			if(l>0 && l<970 && m>0 && m<450) {
				this.controlg.fillCell(l, m, Color.RED);
			}
		}
	}

	public void repaintPoint(int ssID, Color col) {
		int l = (int)this.smartSymbolsIn.get(ssID).getX()/8;
		int m = (int)this.smartSymbolsIn.get(ssID).getY()/8;
		if(l>0 && l<970 && m>0 && m<450) {
			this.controlg.fillCell(l, m, col);
		}
	}

	public void findSmartSymbols() {
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
						this.listeCustomData.add(customData.group(1));
						this.listeKeyword.add(keyword.group(1));
					}
					else {
						addSmartSymbolIn (name.group(1),keyword.group(1),"");
						this.listeCustomData.add("");
						this.listeKeyword.add(keyword.group(1));
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
					Matcher end = Pattern.compile("</gwx:SmartSymbol>").matcher(fileContent);
					end.find(a);
					int g = end.start();
					double x = 0.0;
					double y = 0.0;
					int r = a;
					while((x<=0.0 || x>1950.0 || y<=0.0 || y>1950.0) && !(r>g)) {
						getX.find(r);
						int c = getX.start();
						getY.find(c);
						r = getX.end();
						x = Double.parseDouble(getX.group(1));
						y = Double.parseDouble(getY.group(1));
					}
					if((x<=0.0 || x>1950.0) && (y<=0.0 || y>1950.0)){
						this.smartSymbolsIn.get(smartSymbolsIn.size()-1).setgetX(0.0);
						this.smartSymbolsIn.get(smartSymbolsIn.size()-1).setgetY(0.0);
					}
					else {
						this.smartSymbolsIn.get(smartSymbolsIn.size()-1).setgetX(Double.parseDouble(getX.group(1)));
						this.smartSymbolsIn.get(smartSymbolsIn.size()-1).setgetY(Double.parseDouble(getY.group(1)));
					}
				}
			}
		}catch (Exception e){//Catch exception if any
			//System.err.println("Error: " + e.getMessage() + " //" + getName());
		}
		getNbTagsIn();
		ajoutPoint();
	}

	//	public Map<String, Object[]> addToDatas(Map<String, Object[]> data) {
	//		for(int i = 0;i<this.smartSymbolsIn.size();i++) {
	//			data.put(this.smartSymbolsIn.get(i).getName(), new Object[]{this.smartSymbolsIn.get(i).getId(), this.smartSymbolsIn.get(i).getName()});
	//		}
	//		return data;
	//	}

	public void controlQuality(List<String> refK, List<String> refC) {
		int index = 0;
		int indexLocal = 0;
		for (String k:this.listeKeyword) {	//Pour tous les keyword de la liste (keywords des symboles du synoptique)
			while (index<refK.size()-1 && !(k.toString().equals(refK.get(index).toString()))) {	//Je le cherche dans la liste de référence refk	Si je le trouve, je vérifie si les CustomData sont identiques (quality true)
				index++;
			}
			if((k.toString().equals(refK.get(index).toString())) && (this.listeCustomData.get(indexLocal).toString().equals(refC.get(index).toString()))) {
				this.smartSymbolsIn.get(indexLocal).setQuality(true);
			}
			if(this.getName().toString() == "Symboles OptimisesV2") this.smartSymbolsIn.get(indexLocal).setQuality(true);
			indexLocal++;
			index=0;
		}
		for (int i=0;i<this.smartSymbolsIn.size();i++) {
			if (!(this.name.contains("Symbole")) && this.smartSymbolsIn.get(i).getQuality() == false && !(this.listeInvalide.contains(this.smartSymbolsIn.get(i).getKeyword().toString()))) {
				this.listeInvalide.add(this.smartSymbolsIn.get(i).getKeyword().toString());
			}
		}
		Collections.sort(this.listeInvalide);
	}

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

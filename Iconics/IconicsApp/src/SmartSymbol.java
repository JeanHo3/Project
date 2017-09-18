import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartSymbol {
	private String name;
	private String keyword;
	private String customData;
	private List<Property> listProperties = new ArrayList<Property>();
	private String associatedTag;
	private String changedTag;
	private double posX;
	private double posY;
	private static int id;
	private int idp;
	private boolean qualityGood;

	public SmartSymbol(String pName, String pKeyword, String pCustomData) {
		this.setName(pName);
		this.setKeyword(pKeyword);
		this.setCustomData(pCustomData);
		this.associatedTag = "";
		this.changedTag = "";
		id++;
		this.idp = id;
		this.qualityGood = false;
	}

	public SmartSymbol(String pName, String pKeyword, String pCustomData, List<Property> pListProperty) {
		this.setName(pName);
		this.setKeyword(pKeyword);
		this.setCustomData(pCustomData);
		this.setListProperties(pListProperty);
		this.associatedTag = "";
		this.changedTag = "";
		id++;
		this.idp = id;
		this.qualityGood = false;
	}

	public List<String> getListStrProp() {
		List<String> listeProp = new ArrayList<String>();
		for(int p=0;p<this.listProperties.size();p++) listeProp.add(this.listProperties.get(p).getName().toString());
		return listeProp;
	}

	public String getValueIfExistsProp(String name) {
		int ind = -1;
		for(int p=0;p<this.listProperties.size();p++) {
			if(this.listProperties.get(p).getName().toString().equals(name)) ind = p;
		}
		if (ind != -1)  return this.listProperties.get(ind).getValue().toString();
		else return "";
	}

	public void setQuality(boolean setQ) {
		this.qualityGood = setQ;
	}

	public boolean getQuality() {
		return this.qualityGood;
	}

	public String getShareKeyword() {
		return this.keyword;
	}

	public String getCustomData() {
		return this.customData;
	}

	public int getId() {
		return this.idp;
	}

	public String getChangedTag() {
		return this.changedTag;
	}

	public void setgetX(double x){
		this.posX = x;
	}

	public void setgetY(double y){
		this.posY = y;
	}

	public double getX(){
		return this.posX;
	}

	public double getY(){
		return this.posY;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List<Property> getListProperty(){
		return this.listProperties;
	}

	public Property getProperty(int id) {
		return listProperties.get(id);
	}

	public void setChangedTag(String tag) {
		this.changedTag = tag;
	}

	public String getAssociatedTag() {
		return this.associatedTag;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int hasTag() {
		if ((this.associatedTag.length()>=20))
			return 1;
		else
			return 0;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public void setListProperties(List<Property> listProperties) {
		this.listProperties = listProperties;
	}

	public String descriptionRet() {
		if(!(this.customData.isEmpty()))
			return ("\rSmart Symbol N" + this.idp + " - Nom = " + this.name + ", Keyword = " + this.keyword + ", Custom Data = " + this.customData + ".\r-- Tag associé : " + this.associatedTag);
		else
			return ("\rSmart Symbol N" + this.idp + " - Nom = " + this.name + ", Keyword = " + this.keyword + ".\r-- Tag associé : " + this.associatedTag);
	}

	public String descriptionPropertiesRet() {
		String l = "";
		if (this.listProperties.size()!=0) {
			for(int i = 0;i<this.listProperties.size();i++) {
				l = l + "\r" + this.listProperties.get(i).descriptionRet();
			}
		}
		return l;
	}

	public String findTags() {
		String API = "";
		String Site = "";
		String CIF = "";
		String Equipement = "";
		String ISA = "";
		String Info = "";
		String mnemo = "";

		for(int i=0;i<this.listProperties.size();i++) {
			if(this.listProperties.get(i).getName().toString().startsWith("API") && !(this.listProperties.get(i).getName().toString().length() < 3) && !(this.listProperties.get(i).getName().toString().length() > 6)) {
				API = this.listProperties.get(i).getValue();
			}
			if(this.listProperties.get(i).getName().equals("Site") && this.listProperties.get(i).getValue().length() == 4)
				Site = this.listProperties.get(i).getValue();
			if(this.listProperties.get(i).getName().equals("CIF") && this.listProperties.get(i).getValue().length() == 6)
				CIF = this.listProperties.get(i).getValue();
			if(this.listProperties.get(i).getName().equals("Equipement") && this.listProperties.get(i).getValue().length() == 5)
				Equipement = this.listProperties.get(i).getValue();
			if(this.listProperties.get(i).getName().equals("Norme_ISA_CLIM") && this.listProperties.get(i).getValue().length() == 7)
				ISA = this.listProperties.get(i).getValue();
			if(this.listProperties.get(i).getName().toString().startsWith("Info") && this.listProperties.get(i).getValue().length() == 4)
				Info = this.listProperties.get(i).getValue();
		}
		if(Info == "") {
			mnemo = ((Site.concat(CIF)).concat(Equipement)).concat(ISA);
		}
		else {
			mnemo = (((Site.concat(CIF)).concat(Equipement)).concat(ISA)).concat(Info);
		}
		if ((mnemo.length() >= 22 && mnemo.length() <= 26)) {
			this.associatedTag = mnemo;
			return API + this.associatedTag;
		}
		else
			return "";
	}

	public void findProperties(String subString) {
		Matcher match = Pattern.compile("<gwx:PropertyDefinition.+Name=.+PropertyType=.+Value=.+>").matcher(subString);
		while (!(match.hitEnd()))  {
			if(match.find()) {
				Matcher name = Pattern.compile(" Name=\"(.*?)\"").matcher(match.group(0));
				name.find();
				Matcher type = Pattern.compile(" PropertyType=\"(.*?)\"").matcher(match.group(0));
				type.find();
				Matcher value = Pattern.compile(" Value=\"(.*?)\"").matcher(match.group(0));
				if(value.find()) {
					this.listProperties.add(new Property(name.group(1),type.group(1),value.group(1)));
				}
				else {
					this.listProperties.add(new Property(name.group(1),type.group(1)));
				}
			}
		}
	}
}

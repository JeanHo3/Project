
public class Tag {
	private String Mnemonique;
	private String Site;
	private String CIF;
	private String Equipement;
	private String NormeISA;
	private String Info;
	private String API;
	private boolean fromExcel;
	private static int id;

	public Tag() {
		this.setMnemonique("");
		setId(getId() + 1);
		affectStrings(true);
	}

	public Tag(String pMnemonique, String pApi, boolean pfromExcel, boolean withInfo) {
			this.setMnemonique(pMnemonique);
			this.setAPI(pApi);
			this.setFromExcel(pfromExcel);
			setId(getId() + 1);
			affectStrings(withInfo);
	}

	private void affectStrings(boolean withInfo) {
		if(this.Mnemonique.toString() != "") {
			if(withInfo == true) {
				this.Site = this.Mnemonique.substring(0, 4);
				this.CIF = this.Mnemonique.substring(4, 10);
				this.Equipement = this.Mnemonique.substring(10, 15);
				this.NormeISA = this.Mnemonique.substring(15, 22);
				this.Info = this.Mnemonique.substring(22, 26);
			}
			else {
				this.Site = this.Mnemonique.substring(0, 4);
				this.CIF = this.Mnemonique.substring(4, 10);
				this.Equipement = this.Mnemonique.substring(10, 15);
				this.NormeISA = this.Mnemonique.substring(15, 22);
				this.Info = "";
			}
		}
		else {
			this.Site = "";
			this.CIF = "";
			this.Equipement = "";
			this.NormeISA = "";
			this.Info = "";
		}
	}

	public String getMnemonique() {
		return Mnemonique;
	}

	public void setMnemonique(String mnemonique) {
		Mnemonique = mnemonique;
	}

	public String getSite() {
		return Site;
	}

	public void setSite(String site) {
		Site = site;
	}

	public String getCIF() {
		return CIF;
	}

	public void setCIF(String cIF) {
		CIF = cIF;
	}

	public String getEquipement() {
		return Equipement;
	}

	public void setEquipement(String equipement) {
		Equipement = equipement;
	}

	public String getNormeISA() {
		return NormeISA;
	}

	public void setNormeISA(String normeISA) {
		NormeISA = normeISA;
	}

	public String getInfo() {
		return Info;
	}

	public void setInfo(String info) {
		Info = info;
	}

	public String getAPI() {
		return API;
	}

	public void setAPI(String aPI) {
		API = aPI;
	}

	public void description() {
		System.out.println("Site - " + this.Site + ", CIF - " + this.CIF + ", Equipement - " + this.Equipement + ", ISA - " + this.NormeISA + ", Info - " + this.Info + ".");;
	}

	public boolean isFromExcel() {
		return fromExcel;
	}

	public void setFromExcel(boolean fromExcel) {
		this.fromExcel = fromExcel;
	}


	public static int getId() {
		return id;
	}


	public static void setId(int id) {
		Tag.id = id;
	}
}

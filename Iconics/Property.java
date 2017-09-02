
public class Property {
	private String name;
	private String value;
	private String type;
	private static int id;
	private int idp;

	public Property() {
		this.setName("");
		this.setType("");
		this.setValue("");
		id++;
		this.setIdp(id);
	}

	public Property(String pName, String pType, String pValue) {
		this.setName(pName);
		this.setType(pType);
		this.setValue(pValue);
		id++;
		this.setIdp(id);
	}

	public Property(String pName, String pType) {
		this.setName(pName);
		this.setType(pType);
		this.value = "";
		id++;
		this.setIdp(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void description() {
		System.out.println("-- Nom : " + this.name + ", Valeur : " + this.value + ", Type : " + this.type + ".");
	}

	public String descriptionRet() {
		return ("\t Nom : " + this.name + ", Valeur : " + this.value + ", Type : " + this.type + ".");
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Property.id = id;
	}

	public int getIdp() {
		return idp;
	}

	public void setIdp(int idp) {
		this.idp = idp;
	}
}

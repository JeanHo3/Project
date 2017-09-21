import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadExec extends Thread{
	String name = "";
	String fileContent = "";
	Matcher l;
	private Synoptique syno;

	public ThreadExec(Synoptique syno) {
		try {
			super.setName(syno.getName());
			this.fileContent = readFileAsString(syno.getPath());
			this.l = Pattern.compile("<gwx:SmartSymbol Name=.+Keyword=.+>").matcher(fileContent);
			this.syno = syno;
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try{
			String mnemo = "";
			String API = "";
			while (!(l.hitEnd()))   {
				if(l.find()) {
					int a = l.start();
					Matcher name = Pattern.compile("<gwx:SmartSymbol Name=\"(.*?)\"").matcher(l.group(0));
					Matcher customData = Pattern.compile("Tag=\"(.*?)\"").matcher(l.group(0));
					Matcher keyword = Pattern.compile("gwx:GwxProperties.Keyword=\"(.*?)\"").matcher(l.group(0));
					name.find();
					keyword.find();
					customData.find();
					if(!(customData.hitEnd())) {
						syno.addSmartSymbolIn (name.group(1),keyword.group(1),customData.group(1));
						syno.addCustomInList(customData.group(1));
						syno.addKeywordInList(keyword.group(1));
					}
					else {
						syno.addSmartSymbolIn (name.group(1),keyword.group(1),"");
						syno.addCustomInList("");
						syno.addKeywordInList(keyword.group(1));
					}
					Matcher getProperties = Pattern.compile("</gwx:SmartSymbol.PropertyDefinitions>").matcher(fileContent);
					Matcher end = Pattern.compile("</gwx:SmartSymbol>").matcher(fileContent);
					end.find(a);
					int g = end.start();
					getProperties.find(a);
					if(!(getProperties.hitEnd())) {
						int b = getProperties.start();
						if(b<g) {
							String toProperties = fileContent.substring(a, b);
							syno.getOneSmart(syno.getSmartSize()-1).findProperties(toProperties);
							mnemo = syno.getOneSmart(syno.getSmartSize()-1).findTags();
							if (!(mnemo.equals(""))) {
								API = mnemo.substring(0, 3);
								mnemo = mnemo.substring(3);
								if (mnemo.length() == 22)
									syno.addTagInList(new Tag(mnemo,API,false,false));
								if (mnemo.length() == 26)
									syno.addTagInList(new Tag(mnemo,API,false,true));
							}
						}
					}
					Matcher getX = Pattern.compile("Canvas.Left=\"(.*?)\"").matcher(fileContent);
					Matcher getY = Pattern.compile("Canvas.Top=\"(.*?)\"").matcher(fileContent);


					double x = 0.0;
					double y = 0.0;
					int r = a;
					while((x<=0.0 || y<=0.0) && !(r>g)) {
						getX.find(r);
						int c = getX.start();
						getY.find(c);
						r = getX.end();
						x = Double.parseDouble(getX.group(1));
						y = Double.parseDouble(getY.group(1));
					}
					if((x<=0.0 || x>1950.0) && (y<=0.0 || y>1950.0)){
						syno.getOneSmart(syno.getSmartSize()-1).setgetX(0.0);
						syno.getOneSmart(syno.getSmartSize()-1).setgetY(0.0);

					}
					else {
						syno.getOneSmart(syno.getSmartSize()-1).setgetX(Double.parseDouble(getX.group(1)));
						syno.getOneSmart(syno.getSmartSize()-1).setgetY(Double.parseDouble(getY.group(1)));

					}
				}
			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage() + " //" + getName());
		}
		syno.getNbTagsIn();
		syno.ajoutPoint();
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
}

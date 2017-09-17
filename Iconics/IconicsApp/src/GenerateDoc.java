import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GenerateDoc {
	private String nomGen;
	private String txtDate;
	
	public GenerateDoc(String pNomGen) {
		this.nomGen = pNomGen;
		this.txtDate = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
	}
	//Map : (Key,Syno,...,Chemin_Alarme)
	//Toute les clés ayant le même syno sur une même page
	public void toExcel(Map<Integer, String[]> data) {
		//Blank workbook
		boolean init = false;
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    Set<Integer> keyset = data.keySet();
	    String[] entete = data.get(0);	//Entête de la feuille Excel
	    data.remove(0);
	    
	    for(Integer key : keyset) {
	    		
	    		String[] objString = data.get(key);
	    		String el0 = objString[0].toString();
	    		if(workbook.getNumberOfSheets()==0) {
	    			XSSFSheet sheet = workbook.createSheet(el0);
    				sheet.setSelected(true);
    				init=false;
	    		}
	    		else{
	    			int i = 0;
	    			while((!(workbook.getSheetName(i).equals(el0))) && i<workbook.getNumberOfSheets()) {
	    				i++;
	    			}
	    			if(i==workbook.getNumberOfSheets()-1) {
	    				XSSFSheet sheet = workbook.createSheet(el0);
	    				sheet.setSelected(true);
	    				init=false;
	    			}
	    			else init=true;
	    		}
	    		
	    		
	    }
	    //Create a blank sheet

	    //Iterate over data and write to sheet
	    

//	    int rownum = 0;
//	    for (Integer key : keyset) 
//	    {
//	        //create a row of excelsheet
//	        Row row = sheet.createRow(rownum++);
//
//	        //get object array of prerticuler key
//	        Object[] objArr = data.get(key);
//
//	        int cellnum = 0;
//
//	        for (Object obj : objArr) 
//	        {
//	            Cell cell = row.createCell(cellnum++);
//	            if (obj instanceof String) 
//	            {
//	                cell.setCellValue((String) obj);
//	            }
//	            else if (obj instanceof Integer) 
//	            {
//	                cell.setCellValue((Integer) obj);
//	            }
//	        }
//	    }
	    try 
	    {
	        //Write the workbook in file system
	        FileOutputStream out = new FileOutputStream(new File("/Users/jeanhourmant/Dropbox/Work_30072017/Fichiers/Source/howtodoinjava_demo.xlsx"));
	        workbook.write(out);
	        out.close();
	        System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	    } 
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}
}

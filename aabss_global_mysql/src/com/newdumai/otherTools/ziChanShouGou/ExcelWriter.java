package com.newdumai.otherTools.ziChanShouGou;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import com.exception.SimpleException;

  
/** 
 * 从excel读取数据/往excel中写入 excel有表头，表头每列的内容对应实体类的属性 
 *  
 * @author nagsh 
 *  
 */  
public class ExcelWriter {  
	static HSSFWorkbook hssfworkbook=null;
	static HSSFSheet    hssfsheet;
	static FileOutputStream fileoutputstream;

	public static void main(String[] args) throws Exception {
        Writer("d:/book.xls");
    }  

	public static void WriterInit(){
		hssfworkbook = new HSSFWorkbook();
		hssfsheet = hssfworkbook.createSheet();
		try {
			fileoutputstream = new FileOutputStream("d:/book.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
	public static void Writer(Map<String, String> sys_interface_source,List<Map<String, Object>> paraList,List<Map<String, Object>> paraList2,int num){
      HSSFRow hssfrow = hssfsheet.createRow((short)num);
      for (int i = 0; i < paraList.size(); i++) {
    	  int cm=i*2;
          hssfrow.createCell((short)cm).setCellValue((String)sys_interface_source.get((String)paraList.get(i).get("interface_source_code")));
          hssfrow.createCell((short)cm+1).setCellValue((String)paraList.get(0).get("result"));
			}
		for (int i = 0; i < paraList2.size(); i++) {
			int cm=i*2+22;
			hssfrow.createCell((short)cm).setCellValue((String)sys_interface_source.get((String)paraList2.get(i).get("interface_source_code")));
			hssfrow.createCell((short)cm+1).setCellValue((String)paraList2.get(0).get("result"));
		}
		try {
			hssfworkbook.write(fileoutputstream);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public static void WriterDestroy() throws Exception{
//        FileOutputStream fileoutputstream = new FileOutputStream("d:/book.xls");
        fileoutputstream.close();
    }
    
    
    private static void Writer(String file) throws Exception {
    	@SuppressWarnings("resource")
		HSSFWorkbook hssfworkbook = new HSSFWorkbook();
        HSSFSheet    hssfsheet    = hssfworkbook.createSheet();
        HSSFRow      hssfrow      = hssfsheet.createRow(0);

        hssfrow.createCell((short)7).setCellValue("a");
        hssfrow.createCell((short)8).setCellValue("b");
        hssfrow.createCell((short)9).setCellValue("c");
        
        
        FileOutputStream fileoutputstream = new FileOutputStream(file);
        hssfworkbook.write(fileoutputstream);
        fileoutputstream.close();
	}
    
    
}
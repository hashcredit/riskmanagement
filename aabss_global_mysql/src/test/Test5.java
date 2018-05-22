package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.newdumai.global.dao.Dumai_newBaseDao;
import com.newdumai.util.Dumai_newConfig;

public class Test5 {
	
	@Test
	public void getTableInfo() throws IOException {
		ApplicationContext ctx =  new AnnotationConfigApplicationContext(Dumai_newConfig.class);
		Dumai_newBaseDao dao = (Dumai_newBaseDao) ctx.getBean("mysqlSpringJdbcBaseDao");
		/*List<Map<String,Object>> tables = dao.queryForList("SELECT"
				+ "	t.TABLE_NAME,t.TABLE_COMMENT,c.COLUMN_NAME,c.DATA_TYPE,c.IS_NULLABLE,c.CHARACTER_MAXIMUM_LENGTH,c.NUMERIC_PRECISION,c.NUMERIC_SCALE,c.COLUMN_TYPE "
				+ " FROM"
				+ "	information_schema.`COLUMNS` c"
				+ " JOIN information_schema.`TABLES` t ON c.table_name = t.table_name "
				+ " WHERE"
				+ "	t.TABLE_SCHEMA = 'new_dumai'"
				+ " AND t.TABLE_TYPE = 'BASE TABLE'");*/
		List<Map<String,Object>> tables = dao.queryForList("SELECT"
				+ "	t.TABLE_NAME,t.TABLE_COMMENT"
				+ " FROM"

				+ " information_schema.`TABLES` t "
				+ " WHERE"
				+ "	t.TABLE_SCHEMA = 'new_dumai'"
				+ " AND t.TABLE_TYPE = 'BASE TABLE'");

		Workbook book = new HSSFWorkbook();
		Sheet sheet = book.createSheet("表结构信息");
		
		sheet.setDefaultRowHeightInPoints(20);  
		HSSFCellStyle cellStyle= (HSSFCellStyle) book.createCellStyle();
		HSSFCellStyle cellStyle1= (HSSFCellStyle) book.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
		cellStyle1.setAlignment(HorizontalAlignment.LEFT);
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
	    cellStyle.setWrapText(true);
		for(int i = 0 ; i < tables.size() ; i ++){
			Map<String,Object> table = tables.get(i);
			
			Map<String,Object> createInfo = dao.queryForMap("show create table " + table.get("TABLE_NAME") );
			Row row =  sheet.createRow(i);
			Cell cell0 = row.createCell(0);
			Cell cell1 = row.createCell(1);
			Cell cell2 = row.createCell(2);
			cell0.setCellValue((String)table.get("TABLE_COMMENT"));
			cell1.setCellValue((String)table.get("TABLE_NAME"));
			cell2.setCellValue((String)createInfo.get("Create Table"));
			cell0.setCellStyle(cellStyle1);
			cell1.setCellStyle(cellStyle1);
			cell2.setCellStyle(cellStyle);
			//CSV文件
			//System.out.println("\""+table.get("TABLE_NAME") +"\",\""+ table.get("TABLE_COMMENT") +"\",\"" + createInfo.get("Create Table")+"\"");
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		book.write(new FileOutputStream("D:/tables.xls"));
		book.close();

	}
	@Test
	public void generateTableColumsInfo() throws IOException {
		ApplicationContext ctx =  new AnnotationConfigApplicationContext(Dumai_newConfig.class);
		Dumai_newBaseDao dao = (Dumai_newBaseDao) ctx.getBean("mysqlSpringJdbcBaseDao");
		
		List<Map<String,Object>> colmuns = dao.queryForList("SELECT"
				+ "	t.TABLE_NAME,t.TABLE_COMMENT,c.COLUMN_NAME,c.COLUMN_COMMENT,c.DATA_TYPE,"
				+ " case c.IS_NULLABLE when 'NO' then '是' else '否' end IS_NULLABLE"
				+ ",c.CHARACTER_MAXIMUM_LENGTH,c.NUMERIC_PRECISION,c.NUMERIC_SCALE,c.COLUMN_TYPE "
				+ " FROM"
				+ "	information_schema.`COLUMNS` c"
				+ " JOIN information_schema.`TABLES` t ON c.table_name = t.table_name "
				+ " WHERE"
				+ "	t.TABLE_SCHEMA = 'new_dumai' and c.TABLE_SCHEMA='new_dumai'"
				+ " AND t.TABLE_TYPE = 'BASE TABLE' order by c.table_name");
		
		Workbook book = new HSSFWorkbook();
		CreationHelper createHelper = book.getCreationHelper();  
		Sheet sheet = book.createSheet("表结构信息");
		Sheet sheet2 = book.createSheet("表信息");
		sheet.setDefaultRowHeightInPoints(20);
		sheet2.setDefaultRowHeightInPoints(20);
		
		HSSFCellStyle cellStyle1= (HSSFCellStyle) book.createCellStyle();
		cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND );
		cellStyle1.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		cellStyle1.setAlignment(HorizontalAlignment.CENTER);
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle1.setBorderTop(BorderStyle.THIN);
		cellStyle1.setBorderRight(BorderStyle.THIN);
		cellStyle1.setBorderBottom(BorderStyle.THIN);
		cellStyle1.setBorderLeft(BorderStyle.THIN);
		
		HSSFCellStyle cellStyle2= (HSSFCellStyle) book.createCellStyle();
		
		cellStyle2.setAlignment(HorizontalAlignment.CENTER);
		cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle2.setBorderTop(BorderStyle.THIN);
		cellStyle2.setBorderRight(BorderStyle.THIN);
		cellStyle2.setBorderBottom(BorderStyle.THIN);
		cellStyle2.setBorderLeft(BorderStyle.THIN);
		
		HSSFCellStyle cellStyle= (HSSFCellStyle) book.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(true);
		
		String[] colums = {"COLUMN_NAME","COLUMN_TYPE","IS_NULLABLE","COLUMN_COMMENT"};
		String[] columsNames = {"字段名","类型","非空","说明"};
		HSSFCellStyle[] columsCellStyles = {cellStyle,cellStyle,cellStyle2,cellStyle};
		String lastTableName = null;
		int index = 0;
		int index2 = 0;
		for(int i = 0 ; i < colmuns.size() ; i ++){
			Map<String,Object> table = colmuns.get(i);
			
			String currTableName = (String) table.get("TABLE_NAME");
			
			if(!currTableName.equals(lastTableName)){
				if(lastTableName!=null) index ++;
				Row row =  sheet.createRow(index);
				Row row2 =  sheet2.createRow(index2);
				index2 ++;
				Cell nameCell = row2.createCell(0);
				Cell cmtCell = row2.createCell(1);
				nameCell.setCellValue(currTableName);
				Hyperlink link =createHelper.createHyperlink(HyperlinkType.DOCUMENT);
				link.setAddress("#表结构信息!A"+(index+1));
				nameCell.setHyperlink(link);
				cmtCell.setCellValue((String) table.get("TABLE_COMMENT"));
				
				
				Cell tableCell = row.createCell(0);
				tableCell.setCellValue(currTableName);
				CellRangeAddress rangeAddress = new CellRangeAddress(index, index, 0, 3);
				sheet.addMergedRegion(rangeAddress);
				RegionUtil.setBorderBottom(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				RegionUtil.setBorderTop(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				RegionUtil.setBorderLeft(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				RegionUtil.setBorderRight(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				tableCell.setCellStyle(cellStyle1);
				index ++;
				row =  sheet.createRow(index);
				Cell tableCommentCell = row.createCell(0);
				tableCommentCell.setCellValue((String) table.get("TABLE_COMMENT"));
				rangeAddress = new CellRangeAddress(index, index, 0, 3);
				RegionUtil.setBorderBottom(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				RegionUtil.setBorderTop(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				RegionUtil.setBorderLeft(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				RegionUtil.setBorderRight(BorderStyle.THIN.ordinal(),rangeAddress, sheet);  
				sheet.addMergedRegion(rangeAddress);
				tableCommentCell.setCellStyle(cellStyle1);
				index ++;
				
				row =  sheet.createRow(index);
				index ++;
				for(int k = 0 ; k < columsNames.length ; k ++ ){
					Cell tableColumnNameCell = row.createCell(k);
					tableColumnNameCell.setCellValue(columsNames[k]);
					tableColumnNameCell.setCellStyle(cellStyle2);
				}
				
			}
			lastTableName = currTableName;
			
			Row row =  sheet.createRow(index);
			index ++;
			for(int k = 0 ; k < colums.length ; k ++ ){
				Cell tableColumnNameCell = row.createCell(k);
				tableColumnNameCell.setCellValue((String)table.get(colums[k]));
				tableColumnNameCell.setCellStyle(columsCellStyles[k]);
			}
		}
	
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.setColumnWidth(2,256*10);
		
		sheet2.autoSizeColumn(0);
		sheet2.setColumnWidth(1,1256*10);
		
		book.write(new FileOutputStream("D:/bbb.xls"));
		book.close();
		
	}
	
}

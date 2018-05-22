package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.newdumai.dumai_data.dm.Dm_sourceService;
import com.newdumai.global.dao.Dumai_newBaseDao;
import com.newdumai.util.Dumai_sourceConfig;

public class Test8 {
	private AnnotationConfigApplicationContext ctx;
	@Test
	public void testName() throws Exception {
		ctx = new AnnotationConfigApplicationContext(Dumai_sourceConfig.class);
		Dumai_newBaseDao dao = ctx.getBean("mysqlSpringJdbcBaseDao", Dumai_newBaseDao.class);
		
		String dumai_source_codes[] = {//
//			"4235c551-0ed3-459e-9d59-c245c83dcd1d",//手机实名
//			"edd257aa-8a11-4946-90b9-4b23024840c6",//银行卡实名
//			"4efb92bf-47cd-417c-b137-41b23ce762c7",//同住人信息
//			"d193373e-b505-4245-b09d-927ebdafac5f",//凭安染黑度，联系人
			"6f0001c4-4f4c-4e3c-a923-44b881a4b485",//凭安黑名单
			"ebf38a23-0169-41ed-809d-3701e8a8dc35",//凭安逾期
//			"b9cd50f9-27f6-4810-bee8-a7ec888477fc",//涉诉
//			"26fb58cc-f315-4c7d-9517-a5bad2e58969",//黑名单验证
			
		};
		
		List<Map<String,Object>> dumai_sources = new ArrayList<Map<String,Object>>();
		for(String code : dumai_source_codes){
			Map<String,Object> dumai_source = dao.queryForMap("select code,name from dm_source where code=?", code);
			List<Map<String, Object>> inParams = dao.queryForList("select * from dm_source_para where type='0' and dm_source_code=?", code);
			dumai_source.put("inParams", inParams);
			dumai_sources.add(dumai_source);
		}
		runDMAndSaveResult(dumai_sources);
		ctx.close();
	}
	
	@SuppressWarnings("unchecked")
	private void runDMAndSaveResult(List<Map<String,Object>> dumai_sources) throws IOException{
		Map<Integer,String> header = new HashMap<Integer, String>();
		String filePath = "d:/20161228阳海资产2.xls";
		String sheet =  "附1 借款人资料";
		/*header.put(0, "name");
		header.put(1, "card_num");
		header.put(2, "mobile");
		header.put(3, "bank_num");
		header.put(6, "plate");
		header.put(7, "plateType");*/
		header.put(1, "name");
		header.put(2, "card_num");
		header.put(3, "bank_num");
		header.put(4, "mobile");
		header.put(5, "vihicle_number");
		header.put(6, "plate");
		
		Map<Integer,String> headerOut = new HashMap<Integer, String>();
		
		List<Map<String, Object>> datas = readExcel(filePath, sheet, 3, 1, 50, 6, header);
		
		Dm_sourceService dm_sourceService = ctx.getBean(Dm_sourceService.class);
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> data : datas){
			Map<String,Object> resultMap = new HashMap<String, Object>();
			
			int i = 0;
			for (Map<String, Object> dumai_source : dumai_sources) {
				List<Map<String, Object>> inParams = (List<Map<String, Object>>) dumai_source.get("inParams");
				String dumai_source_code = (String) dumai_source.get("code");
				String dumai_source_name = (String) dumai_source.get("name");
				Map<String, Object> inParamMap = new HashMap<String, Object>();
				
				boolean valid = true;//参数是否有效
				
				for (Map<String, Object> inPara : inParams) {
					String name = (String) inPara.get("name");
					String fk_orderinfo_name = (String) inPara.get("fk_orderinfo_name");
					Object value = data.get(fk_orderinfo_name);
					if(value==null || value.equals("")){
						valid = false;
						break;
					}
					inParamMap.put(name, value);
				}
				
				String result = "-";
				if (valid) {
					result = dm_sourceService.testDM(null, null, dumai_source_code, inParamMap);
					//result = "对对对";//ldm_sourceService.testDM(null, null, dumai_source_code, inParamMap);
				}
				
				resultMap.put(dumai_source_name, result);
				headerOut.put(i, dumai_source_name);
				i++;
			}
			results.add(resultMap);
		}
		writeExcel(filePath, sheet, 3, 8, headerOut, results);
	}
	
	/**
	 * 读取指定区域的数据
	 * @param filePath 文件路径
	 * @param sheetName sheet名称
	 * @param startRow 起始行(0开始)
	 * @param startCell(起始列开始)
	 * @param endRow 结束行
	 * @param endCell 结束列
	 * @param header 列下标(0开始)与表头(key)的映射
	 * @return
	 * @throws IOException
	 */
	public static void writeExcel(String filePath, String sheetName, int startRow, int startCell,Map<Integer, String> header,List<Map<String,Object>> datas) throws IOException {
		
		File file = new File(filePath);
		  
		Workbook book = null;
		if(file.exists()){
			if(file.isFile()){
				POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(file)); 
				book = new HSSFWorkbook(fs);
			}
			else{
				throw new IOException(file + " 是已存在的目录!");
			}
		}
		else{
			 book = new HSSFWorkbook();
		}
		
		Sheet sheet = book.getSheet(sheetName);

		if (sheet == null) {
			sheet = book.createSheet();
		}
		int size = datas.size();
		int endRow = startRow + size -1;
		
		Set<Integer> keySet = header.keySet();
		if(startRow>=1){
			Row row = sheet.getRow(startRow-1);
			row = row == null ? sheet.createRow(startRow-1) : row;
			for(int key : keySet){
				int cellIndex = key + startCell;
				if(cellIndex>0){
					Cell cell = row.getCell(cellIndex);
					cell = cell == null ? row.createCell(cellIndex) : cell;
					cell.setCellValue(header.get(key));
				}
			}
		}
		
		for (int i = startRow; i <= endRow; i++) {
			Row row = sheet.getRow(i);
			row = row == null ? sheet.createRow(i) : row;
			Map<String, Object> map = datas.get(i-startRow);
			for(int key : keySet){
				Cell cell = row.getCell(key + startCell);
				cell = cell == null ? row.createCell(key + startCell) : cell;
				cell.setCellValue(String.valueOf(map.get(header.get(key))));
			}
		}
		book.write(new FileOutputStream(file));
		book.close();
	}
	
	/**
	 * 读取指定区域的数据
	 * @param filePath 文件路径
	 * @param sheetName sheet名称
	 * @param startRow 起始行(0开始)
	 * @param startCell 起始列(0开始)
	 * @param endRow 结束行
	 * @param endCell 结束列
	 * @param header 列下标(0开始)与表头(key)的映射
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> readExcel(String filePath, String sheetName, int startRow, int startCell, int endRow, int endCell, Map<Integer, String> header) throws IOException {
		Workbook book = new HSSFWorkbook(new FileInputStream(filePath));
		Sheet sheet = book.getSheet(sheetName);

		if (sheet == null) {
			book.close();
			return null;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = startRow; i <= endRow; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = startCell; j <= endCell; j++) {
					Cell cell = row.getCell(j);
					String key = header.get(j);
					if (key != null){
						//key = key == null ? String.valueOf(j) : key;
						Object value = cell == null ? null : getCellValue(cell);
						map.put(key, value);
					}
				}
				list.add(map);
			}
		}
		book.close();
		return list;
	}

	@SuppressWarnings("deprecation")
	private static Object getCellValue(Cell cell) {
		Object value = null;

		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				Double val = cell.getNumericCellValue();
				if (DateUtil.isCellDateFormatted(cell)) {
					value = cell.getDateCellValue();
				}
				else value = val;
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				value = "";
				break;
			default:
				value = null;
				break;
		}

		return value;
	}
	public static void main(String[] args) throws IOException {
		Map<Integer,String> header = new HashMap<Integer, String>();
		
		header.put(0, "name");
		header.put(1, "card_num");
		header.put(2, "mobile");
		header.put(3, "bank_num");
		//header.put(4, "plate");
		header.put(5, "plateType");
		
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("name", "张梧桐");
		data.put("card_num", "7727373737101901");
		data.put("mobile","12312132");
		data.put("bank_num", "2321212121212");
		data.put("plate", "晋U44450");
		data.put("plateType", "02");
		datas.add(data);
		datas.add(data);
		datas.add(data);
		
		writeExcel("d:/datas2.xls", "Sheet1", 10, 10, header, datas);
	}
}

package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.global.dao.Dumai_newBaseDao;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.util.Dumai_sourceConfig;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.SpringApplicationContextHolder;

public class Test92 {
	//private AnnotationConfigApplicationContext ctx;
	
//	@Before
//	public void init(){
//		ctx = new AnnotationConfigApplicationContext(Dumai_sourceConfig.class);
//	}
//	
//	@After
//	public void destroy(){
//		ctx.close();
//	}
	
	public static class Options{
		
		private List<Map<String,Object>> third_interface_sources;
		private String srcFileAsset;
		private String srcRange;
		private Map<String,String> headers;
		private Map<Integer,String> indexedHeaders = new HashMap<Integer,String>();
		private Map<String,Object> fixedProps;
		private String destFilePrefix;
		private String destStartPos;
		private String sheet;
		
		public String getSrcFileAsset() {
			return srcFileAsset;
		}
		public void setSrcFileAsset(String srcFileAsset) {
			this.srcFileAsset = srcFileAsset;
		}
		public String getSrcRange() {
			return srcRange;
		}
		public void setSrcRange(String srcRange) {
			this.srcRange = srcRange;
		}
		public Map<String, String> getHeaders() {
			return headers;
		}
		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}
		public Map<String, Object> getFixedProps() {
			return fixedProps;
		}
		public void setFixedProps(Map<String, Object> fixedProps) {
			this.fixedProps = fixedProps;
		}
		public String getDestFilePrefix() {
			return destFilePrefix;
		}
		public void setDestFilePrefix(String destFilePrefix) {
			this.destFilePrefix = destFilePrefix;
		}
		public String getDestStartPos() {
			return destStartPos;
		}
		public void setDestStartPos(String destStartPos) {
			this.destStartPos = destStartPos;
		}
		public Map<Integer, String> getIndexedHeaders() {
			indexedHeaders.clear();
			if (this.headers != null) {
				Set<String> coordinates = this.headers.keySet();
				for(String coordinate : coordinates){
					indexedHeaders.put(getIndexedPos(coordinate)[1],  this.headers.get(coordinate));
				}
			}
			return indexedHeaders;
		}
		
		public int[] getIndexedDestStartPos() {
			return getIndexedPos(destStartPos);
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return new GsonBuilder().serializeNulls().create().toJson(this);
		}
		public List<Map<String,Object>> getThird_interface_sources() {
			return third_interface_sources;
		}
		public void setThird_interface_sources(List<Map<String,Object>> third_interface_sources) {
			this.third_interface_sources = third_interface_sources;
		}
		public String getSheet() {
			return sheet;
		}
		public void setSheet(String sheet) {
			this.sheet = sheet;
		}
	}
	
	@Test
	@SuppressWarnings({"unchecked"})
	public InputStream test(String optionsText,InputStream in) throws Exception {
		Options options = new Gson().fromJson(optionsText, Options.class);
		int[] range = getIndexedRange(options.getSrcRange());
		
		String sheet = options.getSheet();
		Map<String,Object> fixedPros = options.getFixedProps();
		
		byte[] bytes = IOUtils.toByteArray(in);
		
		List<Map<String, Object>> datas = readExcel(new ByteArrayInputStream(bytes), sheet, range[0], range[1], range[2], range[3],options.getIndexedHeaders());
		
		Dumai_sourceBaseDao dao = (Dumai_sourceBaseDao) SpringApplicationContextHolder.getBean("dumai_sourceBaseDao");
		
		Dm_3rd_interfaceService dm_3rd_interfaceService = SpringApplicationContextHolder.getBean(Dm_3rd_interfaceService.class);
		List<Map<String,Object>> third_interface_sources = options.getThird_interface_sources();
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(baos);
		
		for (Map<String, Object> dumai_source : third_interface_sources) {
			String dm_3rd_interface_code = (String) dumai_source.get("code");
			Map<String,Object> third_interface_sourceMap = dao.queryForMap("select code,name from dm_3rd_interface where code=?", dm_3rd_interface_code);
			List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
			
			Map<String, String> para_order = (Map<String, String>) dumai_source.get("para_order");
			Object out_paras = dumai_source.get("out_paras");
			
			List<Map<String, Object>> in_paraList = dao.queryForList("select * from dm_3rd_interface_para where dm_3rd_interface_code=? and type='0'", dm_3rd_interface_code);;
			List<Map<String, Object>> out_paraList = null;
			
			if(out_paras==null || out_paras.toString().toUpperCase().equals("ALL")){
				out_paraList = dao.queryForList("select * from dm_3rd_interface_para where dm_3rd_interface_code=? and type='1'", dm_3rd_interface_code);
			}
			else{
				out_paraList = (List<Map<String, Object>>) out_paras;
			}
			
			Map<Integer,String> headerOut = new HashMap<Integer, String>();
			for(int i = 0; i < out_paraList.size(); i++){
				Map<String,Object> outPara =  out_paraList.get(i);
				String name = (String) out_paraList.get(i).get("name");
				headerOut.put(i, name);
				if (!"$".equals(name) && outPara.get("para_group")==null) {
					Map<String,Object> paraMap = dao.queryForMap("select code,name,para_group from dm_3rd_interface_para where name=? and dm_3rd_interface_code=?", name,dm_3rd_interface_code);
					outPara.put("para_group", paraMap.get("para_group"));
				}
			}
			
			String dumai_source_name = (String) third_interface_sourceMap.get("name");
			for(Map<String,Object> data : datas){
			
				Map<String, Object> inParamMap = new HashMap<String, Object>();
				
				boolean valid = true;//参数是否有效
				
				for(Map<String,Object> inPara : in_paraList){
					String name = (String) inPara.get("name");
					String defaultValue = (String) inPara.get("value");
					String fk_orderinfo_name = (String) inPara.get("fk_orderinfo_name");
					Object value =  (String) data.get(fk_orderinfo_name);
					value = StringUtils.isEmpty(defaultValue) ? value : defaultValue;
					if(value == null || value.equals("")){
						valid = false;
						break;
					}
					inParamMap.put(name, value);
				}
				
				Set<Map.Entry<String, Object>> entrySetPros = fixedPros.entrySet();
				for (Map.Entry<String, Object> entry : entrySetPros) {
					String name = entry.getKey();
					Object value = entry.getValue();
					inParamMap.put(name, value);
				}
				System.out.println(inParamMap);
				Map<String,Object> result = new HashMap<String,Object>();
				if (valid) {
					//String rs = dm_3rd_interfaceService.testDS(dm_3rd_interface_code, inParamMap);
					String rs = org.apache.commons.io.FileUtils.readFileToString(new java.io.File("D:/fakedatas/" + dm_3rd_interface_code + ".json"), "UTF-8");
					if(StringUtils.isNotEmpty(rs)){
						result = JsonToMap.gson2Map(rs);
					}
				}
				results.add(transResult(result,out_paraList));
			}
			int[] destStartPos = options.getIndexedDestStartPos();
			ZipEntry entry = new ZipEntry(options.getDestFilePrefix() + " - " + dumai_source_name + ".xls");
			
			zipOut.putNextEntry(entry);
			writeExcel(new ByteArrayInputStream(bytes), sheet,destStartPos[0], destStartPos[1], headerOut, results,zipOut);
			zipOut.closeEntry();
		}
		
		System.out.println(datas);
		zipOut.close();
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> transResult(Map<String,Object> result,List<Map<String, Object>> out_paras){
		Map<String,Object> resultNew = new HashMap<String, Object>();
		for(int i = 0; i < out_paras.size(); i++){
			Map<String,Object> outPara =  out_paras.get(i);
			String key = (String) outPara.get("name");
			if("$".equals(key)){
				resultNew.put(key, result);
				continue;
			}
			String para_group = (String) outPara.get("para_group");
			para_group = StringUtils.isEmpty(para_group) ? key : para_group + "_" + key;
			String paths[] = para_group.split("_");
			Map<String,Object> currentMap = result;
			for (int j = 0; j < paths.length ; j++) {
				String path = paths[j];
				if (j == paths.length - 1) {
					resultNew.put(path, currentMap.get(path));
				} else {
					currentMap = (Map<String, Object>) currentMap.get(path);
					if(currentMap==null) break;
				}
			}
		}
		
		return resultNew;
	}
	
	/*
	 * @param filePath
	 * @param sheetName
	 * @param startRow
	 * @param startCell
	 * @param header
	 * @param datas
	 * @param targetFile
	 * @throws IOException
	 */
	public static void writeExcel(InputStream in, String sheetName, int startRow, int startCell,Map<Integer, String> header,List<Map<String,Object>> datas,OutputStream out) throws IOException {
		  
		
		POIFSFileSystem fs=new POIFSFileSystem(in);
		Workbook book  = new HSSFWorkbook(fs);
			
		
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
				Object value = map.get(header.get(key));
				cell.setCellValue(value == null ? "" : value.toString());
			}
		}
		
		book.write(out);
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
	public static List<Map<String, Object>> readExcel( InputStream in, String sheetName, int startRow, int startCell, int endRow, int endCell, Map<Integer, String> header) throws IOException {
		Workbook book = new HSSFWorkbook(in);
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
	
	/**
	 * @param in 输入流
	 * 读取JSON文本去掉注释(仅支持"//"形式的注释)
	 */
	public static String readUncommentJSON(InputStream in) throws IOException {
		List<String> lines = IOUtils.readLines(in);
		StringBuffer sb = new StringBuffer();
		for(String line : lines){
			char last = 0;
			int quotCount = 0;
			int backslashCount = 0;
			int sbLength = sb.length();
			line: for(int i = 0 ; i < line.length() ; i ++){
				if (i == 0) {
					last = line.charAt(i);
					if (last!='/') {
						sb.append(last);
					}
				}
				else{
					char current = line.charAt(i);
					switch (current) {
						case '/':
							if(last=='/'&&quotCount%2==0){
								break line;
							}
							break;
						case '"':
							if(backslashCount %2==0){
								quotCount++;
							}
							break;
						case '\\':
							if(last!='\\'){
								backslashCount++;
							}
							else{
								backslashCount--;
							}
							break;
					}
					if(last=='/'){
						sb.append(last);
					}
					if(current!='/'){
						sb.append(current);
					}
					last = current;
				}
			}
			if (sbLength != sb.length()) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param coordinate
	 * @return
	 */
	public static int[] getIndexedPos(String coordinate) {
		Pattern pattern = Pattern.compile("(?<Col>[A-Za-z]+),?(?<Row>[1-9][0-9]*)");
		Matcher matcher = pattern.matcher(coordinate);
		if (matcher.matches()) {
			String col = matcher.group("Col");
			col = col.toUpperCase();
			int colIndex = -1;
			int base = 1;
			for (int i = col.length() - 1; i >= 0; i--) {
				colIndex += base * (col.charAt(i) - 64);
				base *= 26;
			}
			int rowIndex = Integer.valueOf(matcher.group("Row")) - 1;
			//System.out.println("(" + rowIndex + "," + colIndex + ")");
			return new int[] { rowIndex, colIndex };
		}
		else {
			throw new IllegalArgumentException("coordinate[" + coordinate + "] can't match as cell location expession");
		}
	}
	
	/**
	 * 
	 * @param coordinate
	 * @return
	 */
	public static int[] getIndexedRange(String coordinate) {
		Pattern pattern = Pattern.compile("(?<Col>[A-Za-z]+)(?<Row>[1-9][0-9]*),?(?<Col1>[A-Za-z]+)(?<Row1>[1-9][0-9]*)");
		Matcher matcher = pattern.matcher(coordinate);
		if (matcher.matches()) {
			String col = matcher.group("Col");
			String col1 = matcher.group("Col1");
			col = col.toUpperCase();
			col1 = col1.toUpperCase();
			int colIndex = -1;
			int col1Index = -1;
			int base = 1;
			for (int i = col.length() - 1; i >= 0; i--) {
				colIndex += base * (col.charAt(i) - 64);
				base *= 26;
			}
			base = 1;
			for (int i = col1.length() - 1; i >= 0; i--) {
				col1Index += base * (col1.charAt(i) - 64);
				base *= 26;
			}
			int rowIndex = Integer.valueOf(matcher.group("Row")) - 1;
			int row1Index = Integer.valueOf(matcher.group("Row1")) - 1;
			return new int[] { rowIndex, colIndex,row1Index,col1Index};
		}
		else {
			throw new IllegalArgumentException("coordinate[" + coordinate + "] can't match as cell range expession");
		}
	}
	
}

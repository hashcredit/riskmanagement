package com.newdumai.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.newdumai.global.dao.Dumai_newBaseDao;

public class TableColumsConstsClassGenerator {
	
	@Test
	public void generateTableFieldConstClass() throws IOException {
		String TABLE_SCHEMA = "dumai_source";
		generateTableFieldConstClass(TABLE_SCHEMA);
	}
	@Test
	public void generateTableConstsClass() throws IOException {
		String TABLE_SCHEMA = "dumai_source";
		generateTableConstsClass(TABLE_SCHEMA);
	}
	
	/**
	 * 生成表名为类名，字段名为静态属性的多个类
	 * @param table_schema 数据库
	 * @throws IOException
	 */
	public void generateTableFieldConstClass(String table_schema) throws IOException {
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Dumai_newConfig.class);
		Dumai_newBaseDao dao = ctx.getBean(Dumai_newBaseDao.class);
		
		List<Map<String,Object>> colmuns = dao.queryForList("SELECT"
				+ "	t.TABLE_NAME,t.TABLE_COMMENT,c.COLUMN_NAME,c.COLUMN_COMMENT,c.COLUMN_KEY,c.DATA_TYPE,"
				+ " case c.IS_NULLABLE when 'NO' then '否' else '是' end IS_NULLABLE"
				+ ",c.CHARACTER_MAXIMUM_LENGTH,c.NUMERIC_PRECISION,c.NUMERIC_SCALE,c.COLUMN_TYPE "
				+ " FROM"
				+ "	information_schema.`COLUMNS` c"
				+ " JOIN information_schema.`TABLES` t ON c.table_name = t.table_name "
				+ " WHERE"
				+ "	t.TABLE_SCHEMA = '"+table_schema+"' and c.TABLE_SCHEMA='"+table_schema+"'"
				+ " AND t.TABLE_TYPE = 'BASE TABLE' order by c.table_name");
		
		String lastTableName = null;
		
		FileWriter fw = null;
		
		String packageName = "com.newdumai.global."+table_schema;
		String srcDir = "D:/haha/";
		String baseDir = srcDir + packageName.replace(".", "/") +"/";
		new File(baseDir).mkdirs();
		
		for(int i = 0 ; i < colmuns.size() ; i ++){
			Map<String,Object> table = colmuns.get(i);
			
			String currTableName = (String) table.get("TABLE_NAME");
			
			if(!currTableName.equals(lastTableName)){
				endTableClass(fw);
				fw = new FileWriter(baseDir + currTableName +".java");
				fw.write("package " + packageName +";\n");
				fw.write("/**\n");
				fw.write(" * 表名："+currTableName+"<br/>\n");
				fw.write(" * 说明："+table.get("TABLE_COMMENT")+"\n");
				fw.write(" * @author 读脉表字段常量类生成器<br/>\n");
				fw.write(" */\n");
				fw.write("public final class " + currTableName + " {\n\n");
			}
			String colName = (String) table.get("COLUMN_NAME");
			fw.write("\t/**\n");
			
			String colKey =(String) table.get("COLUMN_KEY");
			
			fw.write("\t * 字段名称："+(String) colName + "<br/>\n");
			fw.write("\t * "+("PRI".equals(colKey)?"主键<br/>" : "") + "\n");
			fw.write("\t * 字段描述："+(String) table.get("COLUMN_COMMENT")+ "<br/>\n");
			fw.write("\t * 可否为空："+(String) table.get("IS_NULLABLE")+ "<br/>\n");
			fw.write("\t * 字段类型："+(String) table.get("COLUMN_TYPE")+ "<br/>\n");
			fw.write("\t */\n");
			fw.write("\tpublic static final String " + colName.toUpperCase() + " = \"" + colName+"\";\n\n");
		
			lastTableName = currTableName;
		}
		endTableClass(fw);
		ctx.close();
	}
	
	private void endTableClass(FileWriter fw){
		if(fw!=null){
			try {
				fw.write("}");
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成数据库名为子包名，类名为TableNames，表名为静态属性的类
	 * @param database 数据库名称
	 */
	public void generateTableConstsClass(String database ){
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Dumai_newConfig.class);
		
		//String database = "new_dumai";
		
		Dumai_newBaseDao dao = ctx.getBean(Dumai_newBaseDao.class);
		List<Map<String,Object>> tables = dao.queryForList("SELECT"
				+ "	t.TABLE_NAME,t.TABLE_COMMENT,t.ENGINE,t.CREATE_TIME,t.UPDATE_TIME "
				+ " FROM information_schema.TABLES t"
				+ " WHERE"
				+ "	t.TABLE_SCHEMA = '"+database+"' and t.table_catalog='def'"
				+ " AND t.TABLE_TYPE = 'BASE TABLE' order by t.table_name");
		String packageName = "com.newdumai.global." + database ;
		String srcDir = "D:/haha/";
		String baseDir = srcDir + packageName.replace(".", "/") +"/";
		new File(baseDir).mkdirs();
		
		String className = "TableNames";
		
		StringBuilder classString = new StringBuilder();
		classString.append("package "+ packageName + ";\n\n");
		
		int size = tables.size();
		
		classString.append("/**\n");
		classString.append(" * 数据库：" + database + "<br/>\n");
		classString.append(" * 表个数：" + size + "<br/>\n");
		classString.append(" * @author 读脉表常量类生成器<br/>\n");
		classString.append(" */\n");
		classString.append("public final class "+className+" {\n");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0 ; i < size ; i++) {
			Map<String,Object> table = tables.get(i);
			String tableName = (String) table.get("TABLE_NAME");
			classString.append("\n\t/**\n");
			classString.append("\t * 表名：" + tableName + "<br/>\n");
			classString.append("\t * 说明：" + table.get("TABLE_COMMENT") + "<br/>\n");
			classString.append("\t * 引擎：" + table.get("ENGINE") + "<br/>\n");
			classString.append("\t * 创建时间：" + sdf.format(table.get("CREATE_TIME")) + "<br/>\n");
			classString.append("\t * 修改时间：" + sdf.format(table.get("UPDATE_TIME")) + "<br/>\n");
			classString.append("\t */\n");
			String tableNameField = tableName.toUpperCase();
			classString.append("\tpublic static final String "+tableNameField+" = \""+tableName+"\";\n");
		}
		
		classString.append("}");
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(baseDir + className+".java"));
			fw.write(classString.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close(fw);
		}
		ctx.close();
		
	}
	
	private void close(Closeable closeable){
		if (closeable!=null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

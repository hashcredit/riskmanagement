package com.newdumai.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.newdumai.global.dao.Dumai_sourceBaseDao;

 

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Dm_3rd_InterfaceUtils {

	public static Object OutParaValue(Map<String, Object> dm_3rd_interfaceMap,String result) {
		;
		Map<String, Object> outMap =new HashMap<String, Object>();// JsonToMap.jsonToMap(result);
		String sql = "select * from dm_3rd_interface_para  where  dm_3rd_interface_code = '"+dm_3rd_interfaceMap.get("code")+"' and type='1' order by id ";//04b5b7df-cc67-49b1-a709-6dcc3af2710d
		// DaoHelper.getDumai_sourceBaseDao().queryForList(sql) ;
		Object vRef=null;
		Dm_3rd_InterfaceUtils outPara=new Dm_3rd_InterfaceUtils();
		List<Map<String, Object>> params = DaoHelper.getDumai_sourceBaseDao().queryForList (sql);
			int size = params.size();
			for (int i = 0; i < size; i++) {
				Map<String, Object> outParaMap = params.get(i);
				 String name = DM.NullToStr(outParaMap.get("name"));
				 String level = DM.NullToStr(outParaMap.get("level"));
				 try{vRef=" ";
				 if(level.equals("2")||level.equals("1") ){
					  vRef=outPara.getJsonParaValue(outParaMap,result,outMap);
				 }else if(level.equals("3")){/*自定义 计算表达式 字段*/
					  vRef=outPara.getExpressionParaValue(outParaMap,result,outMap);   
				 }
				 }catch(Exception ex){
					 vRef=DM.NullToStr(outParaMap.get("defaultvalue"));
					 DM.getErrorMsg(ex,"输出参数"+DM.NullToStr(outParaMap.get("name_zh")) + name+"发生错误", true);
				 }
		    outMap.put(name, vRef);
	    }
		return new GsonBuilder().serializeNulls().create().toJson(outMap);
	}

	@SuppressWarnings("unchecked")
	public static Object getOutParaValue(Map<String, Object> result, Map<String, Object> dm_3rd_interface_paraMap) {
		String para_group =DM.NullToStr(dm_3rd_interface_paraMap.get("para_group"));
		String name =DM.NullToStr(dm_3rd_interface_paraMap.get("name"));
		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(para_group)) {
			String para_path[] = para_group.split("_");// 多层的支持
			Map<String, Object> curJsonData = result;
			for (String key : para_path) {
				curJsonData = (Map<String, Object>) curJsonData.get(key);
				if (curJsonData == null) {
					return null;
				}
			}
			data = curJsonData;
		} else {
			data = result;
		}
		return data.get(name);
	}
	
	private static String getNodePropertyName(String vNodeStr ,StringBuilder sbValue){
		if(sbValue==null){sbValue=new StringBuilder("");}
		sbValue.delete(0, sbValue.length());
		String vName="",strT="";
	if(vNodeStr.indexOf("[")>-1 && vNodeStr.indexOf("]")>-1){
		strT=vNodeStr.substring(vNodeStr.indexOf("[")+1 ,  vNodeStr.indexOf("]"));
		if(strT.indexOf("=")>-1){
			vName=strT.split("=")[0].trim();
			sbValue.append(strT.split("=")[1].trim());
		}else{
			DM.OutPrint(vNodeStr+"段配置 不规范，没有属性条件" );
		}
	}
	
	return vName;
}	
	public static boolean isGoodJson(String json) {    
	      
		   try {    
		       new JsonParser().parse(json);  
		       return true;    
		   } catch (JsonParseException e) {    
		      // System.out.println("bad json: " + json);    
		       return false;    
		   }   
		}
	public static String getExpressionParaValue(Map<String, Object> dm_3rd_interface_paraMap,String result,Map<String, Object> outParaValue) {
		String para_group = DM.NullToStr( dm_3rd_interface_paraMap.get("para_group"));//"RESULTS<Map>_<List>[TYPE=EMR004]_DATA<Map>_<List>$SYSCOUNT()";
		String name = DM.NullToStr(dm_3rd_interface_paraMap.get("name"));//EMR004=38 EMR009=0
		String strExpression = DM.NullToStr(dm_3rd_interface_paraMap.get("value"));
		/*自定义 计算表达式 字段*/
	 	if( DM.NullToStr(dm_3rd_interface_paraMap.get("value")).equals("")){
	 	
	 	}
	 		strExpression=DM.NullToStr(dm_3rd_interface_paraMap.get("value"));/*计算公式*/
			String vParaField=DM.NullToStr(dm_3rd_interface_paraMap.get("parafields"));
			Iterator iter = outParaValue.entrySet().iterator(); 
			 while(iter.hasNext())  //第二种迭代方式取键值  
			 {
			  Map.Entry enPara = (Map.Entry)iter.next(); 
			  if(strExpression.indexOf(enPara.getKey()+"" )>-1){
				  strExpression= strExpression.replace(enPara.getKey()+"",DM.NullToStr(enPara.getValue() , "0"));//参数默认值为0
			  }
			}
			 DM.OutPrint(DM.NullToStr(dm_3rd_interface_paraMap.get("name_zh")) + "("+DM.NullToStr(dm_3rd_interface_paraMap.get("name")) +") 计算公式为:"+ strExpression);
			//vFieldValue=DM.getParaFldStr(vFieldValue, vParaField, "3,5");
			   strExpression="function a(){return ("+strExpression+").toFixed(2);};a();";
			Map<String, Object> mFieldValues = new HashMap<String, Object>();
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
			Bindings bindings = jsEngine.createBindings();
			bindings.putAll(mFieldValues);
			try {
				Object ref= jsEngine.eval(strExpression, bindings);
				strExpression=DM.NullToStr(ref, "0");
				
			} catch (ScriptException ex) {
				//e.printStackTrace();
				DM.getErrorMsg(ex, "getOutParaValueAdvanced () ", true);
			}
			return strExpression; /*自定义 计算表达式 字段  计算完就退出*/
		 
	 
	}
	/*
	 * */
	@SuppressWarnings("unchecked")
	public static Object getJsonParaValue(Map<String, Object> dm_3rd_interface_paraMap,String result,Map<String, Object> outParaValue) {
	
		  /*
		   * "RESULTS<Map>_<List>[TYPE=EMR002]_CYCLE<Map>";// 
		   result={"CODE":"200","PHONE":"18580161066","PROVINCE":"重庆","CITY":"重庆","RESULTS":[{"TYPE":"EMR002","CYCLE":"2015-07-07--2017-07-07","DATA":[{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/2 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/10/31 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/23 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/27 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/22 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/13 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/3 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/23 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/14 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/9/4 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/10/17 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/9 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/9/14 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/8/8 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/4 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/24 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/15 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/8/14 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/9/29 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/8/19 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/19 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/8/20 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/8/9 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/5 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/8/10 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/21 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/11/25 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/10/15 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/12 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/8/11 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/10/1 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/9/6 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","REGISTERTIME":"2015/12/22 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_111488","REGISTERTIME":"2015/10/30 0:00:00"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650 ","REGISTERTIME":"2015/9/7 0:00:00"}]},{"TYPE":"EMR004","CYCLE":"2015-07-07--2017-07-07","DATA":[{"P_TYPE":"2","PLATFORMCODE":"EM_111488","APPLICATIONTIME":"2015/10/30 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/9/7 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/2 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/10/31 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/23 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/27 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/22 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/13 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/3 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/23 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/14 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/9/4 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/10/17 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/9 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/9/14 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/8/8 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/4 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/24 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/15 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/8/14 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/9/29 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/8/19 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/19 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/8/20 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/8/9 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/5 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/8/10 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_100001","APPLICATIONTIME":"2016/10/19 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":" "},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/21 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/11/25 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/10/15 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/12 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/8/11 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/10/1 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/9/6 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""},{"P_TYPE":"2","PLATFORMCODE":"EM_100001","APPLICATIONTIME":"2016/9/22 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":" "},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","APPLICATIONTIME":"2015/12/22 0:00:00","APPLICATIONAMOUNT":"0W～0.2W","APPLICATIONRESULT":""}]},{"TYPE":"EMR007","CYCLE":"2015-07-07--2017-07-07","DATA":[{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/12/22 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_111488","LOANLENDERSTIME":"2015/10/30 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/9/7 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/11/2 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/10/31 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/12/23 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/12/27 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/11/22 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/12/13 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/11/3 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/11/23 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/12/14 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/9/4 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/10/17 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/12/9 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/9/14 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/8/8 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/11/4 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_TYPE":"2","PLATFORMCODE":"EM_115650","LOANLENDERSTIME":"2015/11/24 0:00:00","LOANLENDERSAMOUNT":"0W～0.2W"},{"P_T...
		//--------------------------------------------
		  strNode="[{id:11,\"Name\":\"李坤\",\"bir\":\"2012-06-22 21:26:40:592\"},{id:12,\"Name\":\"曹贵生\",\"bir\":\"2012-06-22 21:26:40:625\"},{\"Name\":\"柳波\",\"bir\":\"2012-06-22 21:26:40:625\"}]";
		  para_group ="<List>[id=11.0]_bir<String>"
		//--------  
		strNode="{\"name\":\"lily\",\"age\":23,\"addr\":{\"city\":\"guangzhou\",\"province\":\"guangdong\"}}";
		para_group ="<Map>addr_city<Map>";
		*/
		String para_group=DM.NullToStr( dm_3rd_interface_paraMap.get("para_group"));//"RESULTS<Map>_<List>[TYPE=EMR004]_DATA<Map>_<List>$SYSCOUNT()";
		String name = DM.NullToStr(dm_3rd_interface_paraMap.get("name"));//EMR004=38 EMR009=0
		String strExpression = DM.NullToStr(dm_3rd_interface_paraMap.get("value"));
		Gson gson = new Gson(); // para_group="RESULTS<Map>_<List>[TYPE=EMR004]_DATA<Map>_<List>$SYSCOUNT()";
		 List  list  =null;
		 Map<String, Object> nMap=null;
		 String strNode=result;//gson.toJson(result);
		 String[] pNode=para_group.split("_");
		 String[] pType=new String[pNode.length];
		 String[] pName=new String[pNode.length];
		 String[] pNodeCDTName=new String[pNode.length];
		 String[] pNodeCDTValue=new String[pNode.length];
		 String[] pNodeStr=new String[pNode.length];
		 Object[] pNodeObject=new Object[pNode.length];
		 String pNodeValue=" ";
		 String isFindOK="";
		 for(int iLevel=0;iLevel<pNode.length;iLevel++){/*解析 表达式的 层定义 关系*/
			 if(pNode[iLevel].indexOf("<List>")>-1){
				 pType[iLevel]="<List>";
				 pName[iLevel]= pNode[iLevel]=pNode[iLevel].replace("<List>", "");
				 if(pNode[iLevel].indexOf("[")>-1 && pNode[iLevel].indexOf("]")>-1){
					 pName[iLevel]= pName[iLevel].substring(0, pName[iLevel].indexOf("["));
					 StringBuilder sbValue=new StringBuilder();
					 pNodeCDTName[iLevel]=getNodePropertyName(pNode[iLevel],sbValue);
					 pNodeCDTValue[iLevel]=sbValue.toString();
				 }
			 }else if(pNode[iLevel].indexOf("<Map>")>-1){
				 pType[iLevel]="<Map>";
				 pName[iLevel]=pNode[iLevel].replace("<Map>", "");
				 if(pNode[iLevel].indexOf("[")>-1 && pNode[iLevel].indexOf("]")>-1){
					 pName[iLevel]= pName[iLevel].substring(0, pName[iLevel].indexOf("["));
					 StringBuilder sbValue=new StringBuilder();
					 pNodeCDTName[iLevel]=getNodePropertyName(pNode[iLevel],sbValue);
					 pNodeCDTValue[iLevel]=sbValue.toString();
				 }
			 }else {
				 pType[iLevel]="<String>";
				 pName[iLevel]=pNode[iLevel]=pNode[iLevel].replace("<String>", "");
			 }
		 }
		 for(int iLevel=0;iLevel<pNode.length;iLevel++){
			 if(iLevel>0){
				 pNodeStr[iLevel-1]=strNode;
			 }else{
				 pNodeStr[0]=strNode;
			 }
			 if(pType[iLevel].indexOf("<List>")>-1){
				 if(pName[iLevel].equals("$SYSCOUNT()")&&( strNode.length()<5 ||!DM.checkList((List)pNodeObject[iLevel-1]))){
					 pNodeValue="0";
					 strNode=pNodeValue;
					 break;
				 }
				 if( isGoodJson(strNode)){
				    list = gson.fromJson(strNode, new TypeToken<List>() {}.getType());  
				 }else{
					 list =(List)pNodeObject[iLevel-1];
				 }
				 if(pNodeCDTName[iLevel]!=null){
					 isFindOK="0";
				 for(int i=0;i<list.size();i++){/*循环 查找找  满足条件的list节点*/
					     if(iLevel<pName.length-1){
					    	 if(pType[iLevel].indexOf("<String>")>-1){//待完善
					    		// strNode=pNodeValue="0";
					    		// pNodeObject[iLevel]=list;
					    		 break;
					    	 }
					     }
					
						//pNodeObject[iLevel]=pMap;
						if(!pNodeCDTName[iLevel].equals("")){
							Map pMap=(Map)list.get(i) ;/*此处假设都是 list 里的节点是Map*/
							//ItemValue=DM.NullToStr(pMap.get("TYPE"));//EMR002
							if(pMap.containsKey(pNodeCDTName[iLevel])){
							  if(DM.NullToStr(pMap.get(pNodeCDTName[iLevel])).equals(pNodeCDTValue[iLevel])){//EMR002
								  strNode= gson.toJson(pMap);//找到满足条件的list节点，然后  退出
								  pNodeObject[iLevel]=pMap;
								  isFindOK="1";
								  break;
							  }
							}else{/*list 的当前节点没有 pNodeCDTName[iLevel] 条件Key 的情况*/
								if(pName[pName.length-1].indexOf("$SYSCOUNT()")>-1){/*判断 是否为倒数第二个节点*/
							    	 pNodeValue="0";
							    	 strNode="0";
							    	 isFindOK="1";
							    	 break;
							    }else{
							    		 //pNodeValue="";
							    		 //strNode="";
							    }
								
								
							}
						}else{/*此处假设都是 list 里的节点是不是Map 的情况*/
							if(pType.length-1==iLevel+1){/*当前是倒数第二节点的情况*/
								if(pName[pName.length-1].indexOf("$SYSCOUNT()")>-1){/*判断 是否为倒数第二个节点*/
									strNode= pNodeValue=list.size()+"";
									break;
							    }
								 if(pType[iLevel+1].indexOf("<String>")>-1){/* 这种场景 待完善*/
									
									// String pMap=(String)list.get(i);
								 }
							}
						}
					}//for i
				 /*
				  * list 循环没找到匹配条件的节点 的情况
				  * */
				 if(isFindOK.equals("0")){
					 System.out.println("list 循环没找到匹配条件的节点: 条件是 "+pNodeCDTName[iLevel]+"="+pNodeCDTValue[iLevel]+"\r\n本list数据是：\r\n"+gson.toJson(list, list.getClass()));
				     break;
				 }
				 }else if(pName[iLevel].equals("$SYSCOUNT()")){
					 //pNodeStr[iLevel]=strNode;
					 //pNodeObject[iLevel]=list;
					 if(DM.checkList(list)){
						 pNodeValue=(list.size())+"";
					 }else{
						 pNodeValue="0";
					 }
					 strNode=pNodeValue;
					 break;
				 }
			 }else if(pType[iLevel].indexOf("<Map>")>-1){//=
					if( isGoodJson(strNode)){
			          nMap = gson.fromJson(strNode, new TypeToken<Map>() { }.getType());  
					}else{
						nMap =(Map)pNodeObject[iLevel-1];
					}
				     if(nMap.containsKey(pName[iLevel] )){
				    	 pNodeObject[iLevel]=nMap.get(pName[iLevel]);
				    	 strNode=DM.NullToStr(nMap.get(pName[iLevel])) ;
				     }else{
				    	 if(pName[pName.length-1].indexOf("$SYSCOUNT()")>-1){
				    	 pNodeValue="0";
				    	 strNode="0";
				    	 }else{
				    		 pNodeValue="";
				    		 strNode="";
				    	 }
				    	 break;
				     }
			 }else {//<String>
				 if(pName[iLevel]==null){/*纠错*/
					 pName[iLevel]=""; 
				 }
				 if( pName[iLevel].indexOf("$SYSCOUNT()") >-1){//待完善
					 if( pType[iLevel-1].indexOf("<List>")>-1){
						 list = gson.fromJson(pNodeStr[iLevel-1], new TypeToken<List>() {  }.getType()); 
						 if(DM.checkList(list)){
							 pNodeValue=(list.size())+"";
						 }else{
							 pNodeValue="0";
						 }
						 strNode=pNodeValue;
						 break;
					 }else  if(pType[iLevel-1].indexOf("<Map>")>-1){//待完善
						 if( pType[iLevel-1].indexOf("<List>")>-1){
							 list = gson.fromJson(pNodeStr[iLevel-1], new TypeToken<List>() {  }.getType()); 
							 if(DM.checkList(list)){
								 
							 }else{
								 
							 }
					 } 
				 } 
			 }else{/*String*/
				  if( pType[iLevel-1].indexOf("<Map>")>-1){
					 nMap = gson.fromJson(pNodeStr[iLevel-1], new TypeToken<Map>(){}.getType());  
					 strNode=DM.NullToStr(nMap.get(pName[iLevel]));
				  }else if( pType[iLevel-1].indexOf("<List>")>-1){/*一般是上级阶段已经取出满足条件的  List 中的Node*/
					  if( isGoodJson(strNode)){
						  nMap = gson.fromJson(strNode, new TypeToken<Map>(){}.getType()); 
					  }else{
					     nMap =(Map) pNodeObject[iLevel-1];
					  }
					  if(nMap.containsKey(pName[iLevel])){
					  strNode=DM.NullToStr(nMap.get(pName[iLevel]));
					  }
					  break;
				  }
			 }/*String*/
			 }
		 }//for 
		 return strNode;
		 
	}
	
	
	@SuppressWarnings("unchecked")
	public static Object getOutParaValueAdvanced2(Map<String, Object> result, Map<String, Object> dm_3rd_interface_paraMap) {
		String para_group = (String) dm_3rd_interface_paraMap.get("para_group");
		String name = DM.NullToStr(dm_3rd_interface_paraMap.get("name"));
		String vFieldValue=" ";
		Map<String, Object> data = new HashMap<String, Object>();
		int applyCount=0;
		int RejectCount=0;
		int overdueCount=0;
		double RejectedRatio=0;
		
		if (StringUtils.isNotEmpty(para_group) || DM.NullToStr(dm_3rd_interface_paraMap.get("level")).equals("2")) {
			String vPara_group=DM.NullToStr(dm_3rd_interface_paraMap.get("parafields"));
			/*自定义 计算表达式 字段*/
			if(DM.NullToStr(dm_3rd_interface_paraMap.get("level")).equals("2") &&! DM.NullToStr(dm_3rd_interface_paraMap.get("value")).equals("")){
				vFieldValue=DM.NullToStr(dm_3rd_interface_paraMap.get("value"));
				String vParaField=DM.NullToStr(dm_3rd_interface_paraMap.get("parafields"));
				//vFieldValue=DM.getParaFldStr(vFieldValue, vParaField, "3,5");
				
				vFieldValue=vFieldValue.replace("applyCount", "26");
				vFieldValue=vFieldValue.replace("RejectCount", "6");
				vFieldValue="function a(){return 26/6;};a();";
				
				Map<String, Object> mFieldValues = new HashMap<String, Object>();
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
				Bindings bindings = jsEngine.createBindings();
				bindings.putAll(mFieldValues);
				try {
					Object ref= jsEngine.eval(vFieldValue, bindings);
					vFieldValue=DM.NullToStr(ref, "0");
					
				} catch (ScriptException ex) {
					//e.printStackTrace();
					DM.getErrorMsg(ex, "getOutParaValueAdvanced () ", true);
				}
				 
				
			}else if(para_group.indexOf("$SYS")>-1) {/*使用了高级系统函数情况 RESULTS_DATA_[TYPE="EMR004"]$SYS_COUNT() */
				String para_path[] = para_group.split("_");// 多层的支持
				Map<String, Object> JsonNode = result;
				ArrayList dtNode=null;
				String strNode="";
				JsonObject jsonObjectNode=null; 
				JsonArray jsonArray=null;
				for (int i=0;i<para_path.length-1;i++ ) {
					if(para_path[i].indexOf("[")>-1 && para_path[i].indexOf("]")>-1){
						JsonNode = (Map<String, Object>) JsonNode.get(para_path[i-1]);
						
						String vName=para_path[i].substring(para_path[i].indexOf("[") ,  para_path[i].indexOf("]")-1);
					    String CondtionFieldValue= DM.NullToStr(JsonNode.get(vName));
					    //JsonObject vJson= JsonToMap.toMap(curJsonData);
					}else{
						if(JsonNode.containsKey(para_path[i] )){
							Object object= JsonNode.get(para_path[i]);
							if(object instanceof JsonObject ){
								jsonObjectNode=(JsonObject)object;
							}else if (object instanceof JsonArray) {
								jsonArray=(JsonArray)object;
							}else if(object instanceof Map ){
								JsonNode = (Map<String, Object>) JsonNode.get(para_path[i]);
								dtNode=null;
							}else if(object instanceof ArrayList ){
								dtNode=(ArrayList) object;
                                JsonNode=null;
                                for(int n=0;n<dtNode.size()-1;n++){
                                	 Map pMap=(Map)dtNode.get(n);//RESULTS_DATA_[TYPE="EMR004"]$SYSCOUNT()
                                	if(((String)pMap.get("TYPE")).equals("EMR004")){//亿美_申请贷款数
                                		if(pMap.containsKey("DATA" )){
                                			ArrayList Node=(ArrayList) pMap.get("DATA");
                                		    applyCount=Node.size();
                                		}
                                	}else if(((String)pMap.get("TYPE")).equals("EMR009")){//亿美_贷款驳回数
                                		if(pMap.containsKey("DATA" )){
                                			ArrayList Node=(ArrayList) pMap.get("DATA");
                                    		RejectCount=Node.size();
                                    	}else{
                                    		RejectCount=0;
                                    	}
                                		 
                                	}else if(((String)pMap.get("TYPE")).equals("EMR013")){//亿美_逾期次数
                                		if(pMap.containsKey("DATA" )){
                                			ArrayList Node=(ArrayList) pMap.get("DATA");
                                    		overdueCount=Node.size();
                                    	}
                                 	}
                                }
							}else if(object instanceof String ){
								strNode= DM.NullToStr(object);
							}
						 
							//object.getClass().getName();
						}else{
							DM.OutPrint(para_group +"定义中"+para_path[i] +"有错误");
							
						}
					}
					if (JsonNode == null) {
						return null;
					}
				}
				
			}else{	
			String para_path[] = para_group.split("_");// 多层的支持
			Map<String, Object> curJsonData = result;
			for (String key : para_path) {
				curJsonData = (Map<String, Object>) curJsonData.get(key);
				if (curJsonData == null) {
					return null;
				}
			}
			
			data = curJsonData;
			}
		} else {
			data = result;
		}
		return data.get(name);
	}

	public static Object getOutParaValue(String result, Map<String, Object> dm_3rd_interface_paraMap) {
		 return getOutParaValue(JsonToMap.jsonToMap(result), dm_3rd_interface_paraMap);
		//return getOutParaValueAdvanced(JsonToMap.jsonToMap(result), dm_3rd_interface_paraMap);
		
	}

	public static Map<String, Object> trans_dm_label_inPara(String in_para, Map<String, Object> orderMap) {
		Map<String, Object> returnMap = new TreeMap<String, Object>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				return obj2.compareTo(obj1);
			}
		});
		;
		try {
			Map<String, Object> inParaMap = JsonToMap.jsonToMap(in_para);
			for (String dm_3rd_interface_para_code : inParaMap.keySet()) {
				String sql = " select * from dm_3rd_interface_para where code = '" + dm_3rd_interface_para_code + "' ";
				String dm_3rd_interface_para_name = (String) DaoHelper.getDumai_sourceBaseDao().queryForMap(sql).get("name");
				String orderMap_name = (String) inParaMap.get(dm_3rd_interface_para_code);
				if (orderMap.containsKey(orderMap_name)) {
					returnMap.put(dm_3rd_interface_para_name.trim(), orderMap.get(orderMap_name));
				}
			}
		} catch (Exception e) {
			returnMap = null;
		}
		return returnMap;
	}

    public static Map<String, Object> transInParaToColumn(String in_para, Map<String, Object> orderMap, Dumai_sourceBaseDao dumai_sourceBaseDao) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String sql;
        String dm_3rd_interface_para_name;
        String detail_name;
        try {
            Map<String, Object> inParaMap = JsonToMap.gson2Map(in_para);
            Set<String> keySet = inParaMap.keySet();
            for (String dm_3rd_interface_para_code : keySet) {
                dm_3rd_interface_para_name = "";
                detail_name = "";
                sql = " select * from dm_3rd_interface_para where code = '" + dm_3rd_interface_para_code + "' ";
                dm_3rd_interface_para_name = (String) dumai_sourceBaseDao.queryForMap(sql).get("name");
                detail_name = (String) inParaMap.get(dm_3rd_interface_para_code);
                if (orderMap.containsKey(dm_3rd_interface_para_name)) {
                    returnMap.put(detail_name, orderMap.get(dm_3rd_interface_para_name));
                }
            }
        } catch (Exception e) {
            returnMap = null;
        }
        return returnMap;
    }
}

package com.newdumai.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;    
import java.util.regex.Pattern;  
import java.io.UnsupportedEncodingException ;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

 
public class DM {
	private static final String BASE_STRING = "dumai";
	private static String strT="";
	public static void Out(HttpServletResponse response,String vMessage) {
		 
		response.setContentType("text/html;charset=UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			if (StringUtils.isNotEmpty(vMessage)) {
				pw.print(vMessage);
			}
			pw.flush();
			pw.close();
		} catch (IOException e) {
		}
	}
	public static Properties getProperties() {
		Properties p = new Properties();
		try {
			InputStream inputStream = DM.class.getClassLoader().getResourceAsStream("conf.properties");
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}
	public static Properties getProperties2() {
		Properties p = new Properties();
		try {
			InputStream inputStream = DM.class.getClassLoader().getResourceAsStream("conf.properties");
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}
	public static long toDateLong(String vDateString)
	{
		if(vDateString==null){return 0; }
		if(vDateString.equals("")){return 0; }
		String  Str ="";
		SimpleDateFormat vBartDateFormat = null;
		try
        {
			vBartDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			if(vDateString.indexOf("-")>0){
				vBartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}else if(vDateString.indexOf("/")>0){
				vBartDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			} else if(vDateString.indexOf("CST")>-1){ // String dateString2="Wed Nov 03 00:00:00 CST 2010";  
				vBartDateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy" ,Locale.US);  
		   } 
			{
				 Date date=vBartDateFormat.parse(vDateString);  
			     Str =vBartDateFormat.format(date);
			  return date.getTime();
		  }
        }
        catch (Exception e)
        {
        	 System.out.println("toDateLong 日期格式转换异常,原始数据 "+vDateString);
            return 0;
        } 
	}
	public static String toDate(String vDateString)
	{
		
		if(vDateString==null){return null; }
		if(vDateString.equals("")){return null; }
		String  Str ="";
		SimpleDateFormat vBartDateFormat =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try
        {
		 // String dateString2="Wed Nov 03 00:00:00 CST 2010";  
		  if(vDateString.indexOf("CST")>-1){
		  SimpleDateFormat vSdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy" ,Locale.US);  
		  Date date=vSdf.parse(vDateString);  
		     Str =vBartDateFormat.format(date);
		  }else{
			  ParsePosition pos = new ParsePosition(0);
			  Date strtodate = vBartDateFormat.parse(vDateString, pos);
			  Str = vBartDateFormat.format( strtodate);
		  }
		  //System.out.println(Str);
        }
        catch (Exception e)
        {
        	//System.out.println("toDateTime 日期格式转换异常,原始数据 "+vDateString);
            return "";
        }finally{;}
		return "";
		
	}
	public static String getNowDateTime() {
		   Date vCurrentTime = new Date();
		   SimpleDateFormat vFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   String vDateString = vFormatter.format(vCurrentTime);
		   ParsePosition pos = new ParsePosition(8);
		   vCurrentTime = vFormatter.parse(vDateString, pos);
		   
		   return vDateString;
		}
	public static Date getNowDate() {
		   Date vCurrentTime = new Date();
		   SimpleDateFormat vFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   String vDateString = vFormatter.format(vCurrentTime);
		   ParsePosition pos = new ParsePosition(8);
		     vCurrentTime = vFormatter.parse(vDateString, pos);
		   return vCurrentTime;
		}
	public static String getTime() {//http://zhan.renren.com/jerry88?gid=3602888498030356078&from=post&checked=true
		   SimpleDateFormat vFormatter = new SimpleDateFormat("HH:mm:ss");
		   Date vCurrentTime = new Date();
		   String vDateString = vFormatter.format(vCurrentTime);
		   return vDateString;
		}
	public static String getAutoUrl(HttpServletRequest request) {
		String vT=request.getQueryString();
		String vUrlExt="";
		vT=DM.StrReplecAll(vT,"&&","&");
		String[] vVFs=vT.split("&");
		String vF=" ";
		for (int n = 0; n < vVFs.length; n++){
			vF = vVFs[n];
			if(vF.toLowerCase().indexOf("__wintype")>-1){
				vUrlExt=vUrlExt+"&"+vF.substring(0, vF.indexOf("="))+"="+ vF.substring(vF.indexOf("=")+1);
			}else if ( vF.indexOf("=") >= 0 &&(( !vF.startsWith("__")  && vF.indexOf("_UrlRandom")==-1 )||vF.toLowerCase().indexOf("__appid")>-1)){//排除掉系统参数
				vUrlExt=vUrlExt+"&"+vF.substring(0, vF.indexOf("="))+"="+ vF.substring(vF.indexOf("=")+1);
			}
		}
		if(vUrlExt.startsWith("&")){ vUrlExt=vUrlExt.substring(1);}
		return vUrlExt;
	}
	public static String getUrlSysField (HttpServletRequest request,String vUrl){
		    vUrl=DM.StrReplecAll(vUrl,"&&","&");
		    //vUrl="../../Base/WFP/block.jsp?PRODUCTID=100003&PACTID=100002&PACTCODE=HT10001&PRODUCTNAME=AC1001&__wintype=8&MCOMPONENTID=&PMPRODUCTID=100003&PACTCODE=HT10001&PACTID=100002&TASKID=&PRODUCTCODE="; 
		   String vFrameid=DM.getRequestValueURL(request, "__frameid");
		     if(vFrameid.equals("")){ 
		    	 if(vUrl.toLowerCase().indexOf("__pkid")<1){
		    		 vFrameid=DM.getRequestValueURL(request, "__pkid");
		    	 }
		     }
		    if(vUrl.toLowerCase().indexOf("__frameid")<0 && !vFrameid.equals("")){
				if(vUrl.equals("")){vUrl="?";}else{vUrl=vUrl+"&";};
			    vUrl=vUrl +"__frameid="+vFrameid;
            }
		   if(vUrl.toLowerCase().indexOf("__checkType")<0 && !DM.getRequestValueURL(request, "__checkType").equals("")){
			   if(vUrl.equals("")){vUrl="?";}else{vUrl=vUrl+"&";};
			   vUrl=vUrl+"__checkType="+DM.getRequestValueURL(request, "__checkType");
           }
           if(vUrl.toLowerCase().indexOf("__wintype")<0 && !DM.getRequestValueURL(request, "__wintype").equals("")){
        	   if(vUrl.equals("")){vUrl="?";}else{vUrl=vUrl+"&";};
        	   vUrl=vUrl+"__wintype="+DM.getRequestValueURL(request, "__wintype");
            }
           if(vUrl.toLowerCase().indexOf("__isdebug")<0 && !DM.getRequestValueURL(request, "__isdebug").equals("")){
        	   if(vUrl.equals("")){vUrl="?";}else{vUrl=vUrl+"&";};
        	   vUrl=vUrl+"__isdebug="+DM.getRequestValueURL(request, "__isdebug");
            }
           if(vUrl.toLowerCase().indexOf("__appid")<0 && !DM.getRequestValueURL(request, "__appid").equals("")){
        	   if(vUrl.equals("")){vUrl="?";}else{vUrl=vUrl+"&";};
        	   vUrl=vUrl+"__appid="+DM.getRequestValueURL(request, "__appid");
            }
		return vUrl;
	}
	public static String getAutoUrl(String vURL,HttpServletRequest request) {
		String vT=request.getQueryString();
		String vUrlExt="";
		vT=DM.StrReplecAll(vT,"&&","&");
		String[] vVFs=vT.split("&");
		String vF="";
		if(vT.indexOf("?")>-1){vT=DM.StrReplecAll(vT,"?","&");}
		for (int n = 0; n < vVFs.length; n++){
			vF = vVFs[n].trim();
			if(vF.toLowerCase().indexOf("__")<0 && vF.toLowerCase().indexOf("_urlrandom=")<0 && !vF.equals("")){
				if(vUrlExt.equals("")){vUrlExt="";}else{vUrlExt=vUrlExt+"&";};
				vUrlExt=vUrlExt +vF.substring(0, vF.indexOf("="))+"="+ vF.substring(vF.indexOf("=")+1);
			}
		}
		   if(vURL.endsWith("&")){
			vURL=vURL+vUrlExt.substring(1);
			}else{
				vURL=vURL+"&"+vUrlExt;
			}
		return vURL;
	}
	public static Date toDateTime2(String vDateString)
	{   if(vDateString==null){return null; }//http://zhan.renren.com/jerry88?gid=3602888498030356078&from=post&checked=true
		if(vDateString.equals("")){return null; }
		 String  Str ="";
		SimpleDateFormat vBartDateFormat =   new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		  try
	        {
			 // String dateString2="Wed Nov 03 00:00:00 CST 2010";  
			  if(vDateString.indexOf("CST")>-1){
			  SimpleDateFormat vSdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy" ,Locale.US);  
			  Date date=vSdf.parse(vDateString);  
			  return date;
			    // Str =bartDateFormat.format(date);
			  }else{
				  ParsePosition pos = new ParsePosition(1);
				  vBartDateFormat =   new SimpleDateFormat("yyyy-MM-dd");
				  Date vCurrentTime = vBartDateFormat.parse(vDateString,new ParsePosition(0));
				  //  strtodate = DateFormat.getDateInstance().parse(date,new ParsePosition(1));
                  //Date date = DateFormat.getDateInstance().parse(date);
				  return vCurrentTime;
				  // Str = bartDateFormat.format( strtodate);
			  }
			 // System.out.println(Str);
	        }
	        catch (Exception e)
	        {
	        	//System.out.println("toDateTime 日期格式转换异常,原始数据 "+vDateString);
	            return null;
	        }finally{;}
	
	}
	public static String  toDateTime(String vDateString)
	{   if(vDateString==null){return ""; }//http://zhan.renren.com/jerry88?gid=3602888498030356078&from=post&checked=true
		if(vDateString.equals("")){return ""; }
		 String  Str ="";
		SimpleDateFormat vBartDateFormat =   new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		  try
	        {
			 // String dateString2="Wed Nov 03 00:00:00 CST 2010";  
			  if(vDateString.indexOf("CST")>-1){
			  SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy" ,Locale.US);  
			  Date date=sdf.parse(vDateString);  
			     Str =vBartDateFormat.format(date);
			  }else{
				  ParsePosition pos = new ParsePosition(0);
				  Date vCurrentTime = vBartDateFormat.parse(vDateString, pos);
				  Str = vBartDateFormat.format( vCurrentTime);
			  }
			 // System.out.println(Str);
	        }
	        catch (Exception e)
	        {
	        	//System.out.println("toDateTime 日期格式转换异常,原始数据 "+vDateString);
	            return "";
	        }finally{;}
		return Str;
		
	}
 
	  
	  public static String getAlert( String vMsg) {
		  return  "<script language=\"javascript\" type=\"text/javascript\">window.setTimeout(\"ThinkP.alert('"+ vMsg +"')\",200); </script>";
	  }
	  public static void Alert(javax.servlet.jsp.PageContext vPage ,String vMsg) {
		     try {
		    	 if(vMsg==null){vMsg="No massage";}
		    		 vPage.popBody().println("<script language=\"javascript\" type=\"text/javascript\">window.setTimeout(\"ThinkP.alert('"+ vMsg +"')\",200); </script>") ;
		    	 } catch ( Exception e) {
		 	}finally{;}
		  return  ;
	  }
	 
	  public static void PagePrint(javax.servlet.jsp.PageContext Page,String vMsg){
       	try {
       		  Page.popBody().println(vMsg);
			} catch (IOException e) {
				//e.printStackTrace();
			}finally{;}
	  }
	  public static void Alert(javax.servlet.jsp.PageContext Page,String vWinType,String vMsgType,String vMsg) {
		     try {
		    	 if(vMsg==null){vMsg="No massage";}
		    	/*
			    	 Page.getSession().setAttribute("__AlertType", vWinType);
			    	 Page.getSession().setAttribute("__AlertTitle", "信息提醒");
			    	 Page.getSession().setAttribute("__AlertMsg", vMsg);
			    	 
			    	 Page.getServletContext().setAttribute("__AlertType", vWinType );//"信息类型"
			    	 Page.getServletContext().setAttribute("__AlertTitle", "信息提醒");
			    	 Page.getServletContext().setAttribute("__AlertMsg", vMsg);
		    	 */
		    	 if(vWinType==null || vWinType=="0"){
		    		// Page.popBody().println("<script language=\"javascript\" type=\"text/javascript\">window.setTimeout(\"ThinkP.alert('"+ vMsg +"')\",200); </script>") ;
		    		 Page.popBody().println("<script language=\"javascript\" type=\"text/javascript\">alert('"+ vMsg +"'); </script>") ;
		    	 } else if(vWinType.equals("3")){
		    		 vMsg=DM.StrReplecAll(vMsg, "'", "");
		    		 //Page.popBody().println("<script language=\"javascript\" type=\"text/javascript\">ThinkP.alert( \""+vMsg+"\");</script>");
		    		 Page.popBody().println("<script language=\"javascript\" type=\"text/javascript\"> alert( \""+vMsg+"\");</script>");
		    	 }else{
		    		 //Page.popBody().println("<script language=\"javascript\" type=\"text/javascript\">ThinkP.alert( \""+vMsg+"\"); </script>") ; 
		    		 Page.popBody().println("<script language=\"javascript\" type=\"text/javascript\">alert( \""+vMsg+"\"); </script>") ;// 
		    	 }
		    	 }catch( Exception e) {}finally{;}
		    	 try {return  ;} catch (Exception ex) {return ;}finally{strT="";}
 		  
	  }
	  
	 
	  public static void Alert(javax.servlet.jsp.PageContext vPage,String vWinType,String vMsgType,String vMsg,String vWidth,String vHeight,String vReflash) {
		     try {
		    	 if(vMsg==null){vMsg="No massge";}
		    	 if(vReflash==null || vReflash.equals("") || vReflash.equals("0") ){vReflash="0";}else{vReflash="1";}
		    	 if(vWinType==null || vWinType=="0"){
		    		 if(! vWidth.equals("") && !vHeight.equals("")){
		    			// vPage.popBody().println("<script language=\"javascript\" type=\"text/javascript\">window.setTimeout(\"ThinkP.alert('"+ vMsg +"','"+vWidth+"','"+vHeight+"')\",200);</script>") ;
		    			 vPage.popBody().println("<script language=\"javascript\" type=\"text/javascript\">window.alert(\""+ vMsg +"\");</script>") ;
		    		 }else{
		    		 //vPage.popBody().println("<script language=\"javascript\" type=\"text/javascript\">window.setTimeout(\"ThinkP.alert('"+ vMsg +"')\",200);</script>") ;
		    			 vPage.popBody().println("<script language=\"javascript\" type=\"text/javascript\">window.alert('"+ vMsg +"');</script>") ;
		    		 }
		    		 }else{
		    		 vPage.getSession().setAttribute("__AlertType",vMsgType);// "信息类型"
			    	 vPage.getSession().setAttribute("__AlertTitle", "信息提醒");
			    	 vPage.getSession().setAttribute("__AlertMsg", vMsg);
		    	    //vPage.popBody().println("<script language=\"javascript\" type=\"text/javascript\">ThinkP.alert(\""+vMsg+"\");</script>") ;//"+vMsg+"
			    	 vPage.popBody().println("<script language=\"javascript\" type=\"text/javascript\">window.alert(\""+vMsg+"\");</script>") ;
		    		 }
		    	 } catch ( Exception e) {
		 	} 
		 	try {return  ;} catch (Exception ex) {return ;}finally{strT="";}
	  }
	  //判断是否是偶数
	  public static boolean IsEven (int vInt){
			 // int a=Integer.parseInt(vInt);//将字符转成整形。
		        if(vInt%2==0)
		            {
		        	//System.out.println("偶数"）;
		        	return true ;
		            }
		            else
		           {
		            	return false;
		          //System.out.println("奇数");
		            }

		  }
	 //  //判断是否是奇数
	  public static boolean IsOdd (int vInt){
		 // int a=Integer.parseInt(vInt);//将字符转成整形。
	        if(vInt%2==0)
	            {
	        	//System.out.println("偶数"）;
	        	return false;
	            }
	            else
	           {
	            	return true;
	          //System.out.println("奇数");
	            }
	  }
	
	  public static boolean Isdate(String s)
	    {
		   if(s.equals("")){return false;}
	        try
	        { 
	        	SimpleDateFormat vFormatter = null;
	        	vFormatter = new SimpleDateFormat("yyyy-MM-dd");
	        	vFormatter.setLenient(false);
	        	vFormatter.parse(s);
	             return true;
	         }
	        catch (Exception e)
	        {
	            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
	            return false;
	        }finally{;}
	    }
	  public static boolean IsdateTime(String s)
	    {
	        try
	        { 
	        	SimpleDateFormat vFormatter = null;
	        	if(s.length()>10){
	        		if(s.indexOf("-")>0){
	        			vFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        		}else if(s.indexOf("/")>0){
	        			vFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        		}else if(s.indexOf("CST")>-1){ // String dateString2="Wed Nov 03 00:00:00 CST 2010";  
	        			vFormatter=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy" ,Locale.US); 
	        		}
	        	}else{
	        		vFormatter = new SimpleDateFormat("yyyy-MM-dd");
	        	}
	        	vFormatter.setLenient(false);
	        	 vFormatter.parse(s );
	             return true;
	         }
	        catch (Exception ex)
	        {
	            return false;
	        }finally{;}
	    }
	  //byte[]转string
	  public static String Byte2String (byte[]bytes,Boolean vIsUTF8 ){
		 
		  try {
		  if(vIsUTF8){
			  return  new String(bytes,"UTF-8");
		  }else{
			  return new String(bytes,"ISO-8859-1");
		  }
		  } catch (UnsupportedEncodingException e) {
		   }finally{;}
		return "";
	  }
	  //string 转 byte[]
	  public static byte[] Str2Byte(String  vStr,Boolean vIsUTF8 ){
		  /*
		   1.string 转 byte[]byte[] midbytes=isoString.getBytes("UTF8");
			//为UTF8编码
			byte[] isoret = srt2.getBytes("ISO-8859-1");
			//为ISO-8859-1编码
			其中ISO-8859-1为单字节的编码
		   * */
		try {
			if(vIsUTF8){
				return vStr.getBytes("UTF8"); 
			}else{
				return  vStr.getBytes("ISO-8859-1");
			}
			 
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}finally{;}
		  
		  return null;
	  }
	  
	  //将INT型变量翻转  第一种方式
	  public static int getIntReserve(int iNum){   
		    int tempNum=iNum,count,result=0;   
		    for(count=0;tempNum>0;tempNum/=10,count++);//算出该数有多少位 

		    while(iNum>0){   
		        int num=iNum%10;//取出该位上的数.   
		        for(int i=1;i<count;i++){//count标识当前正在处理第几位数.   
		            num*=10;   
		        }   
		        count--;//标识向前移一位.   
		        result+=num;   
		        iNum/=10;//切掉处理过的位数.   
		    };   
		    return result;   
		} 
	  //将INT型变量翻转 1第二种方式
	  public static int getIntReserve2(int num) {   
	        int result = 0;   
	           
	        if(num == 0) {   
	            return result;   
	        }   
	           
	        int sign = num / Math.abs(num);   
	        num *= sign;   
	           
	        while(num > 0) {   
	            int i = num % 10;   
	            num = num / 10;   
	               
	            result = result * 10 + i;   
	               
	        }   
	           
	           
	        return result * sign;   
	    } 



		/* 转码函数，防止中文显示为乱码 */
		public static String ConvertString(String vMsg) {
			String strT;
			try {
				strT = new String(vMsg.getBytes("ISO-8859-1"), "GB2312");
				return strT;
			} catch (UnsupportedEncodingException ex) {
				return "can not encoding";
			}finally{;}
		}
	public static void  PutPara(HashMap vPara, String vName,Object vV ){
		vName=vName.toUpperCase();
		if(vPara.containsKey(vName )){
			vPara.remove(vName);
			vPara.put(vName, vV);
		}else{
			vPara.put(vName, vV);
		}
		
	}
	public static void  PutPara2(HashMap vPara, String vName,int vV ){
		vName=vName.toUpperCase();
		if(vPara.containsKey(vName )){
			vPara.remove(vName);
			vPara.put(vName, vV);
		}else{
			vPara.put(vName, vV);
		}
		
	}
	  public static String getCurrentUrlPath(HttpServletRequest request) {
		     String vPath=request.getRequestURI();
		    // String strPath = request.getContextPath();
		     int point = vPath.lastIndexOf('/');
		     if (point > -1) {
		    	 vPath=vPath.substring(0,vPath.lastIndexOf('/'));
		     }
		     return  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+vPath+"/";
		   }
	  public static String getSpecialUrlPath(HttpServletRequest request,String vPathPlus) {
		  if(vPathPlus.startsWith("/")){
			  vPathPlus=vPathPlus.substring(1, vPathPlus.length());
		  }
		  if(!vPathPlus.endsWith("/") && !vPathPlus.equals("")){
			  vPathPlus=vPathPlus.substring(0, vPathPlus.length()-1);
		  }
		     return  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+vPathPlus;
		   } 
		 //JAVA 获取完整URL 方法 
		 public static String getRequestURL(HttpServletRequest request){ 
		     if (request == null){
		     	return "";
		     }
		     String url = "";
		     url = request.getContextPath();
		     url = url + request.getServletPath();
		     java.util.Enumeration names = request.getParameterNames();
		     int i = 0;
		     if (!"".equals(request.getQueryString()) || request.getQueryString()!= null){
		     	url = url + "?" + request.getQueryString();
		     }
		     return url;
		     /*
		     if (names != null) { 
		 	    while (names.hasMoreElements()) { 
		 		    String name = (String) names.nextElement(); 
		 		    if (i == 0) { 
		 		    	url = url + "?"; 
		 		    } else { 
		 		    	url = url + "&"; 
		 		    } 
		 		    i++; 
		 		    String value = request.getParameter(name); 
		 		    if (value == null) { 
		 		    	value = ""; 
		 		    }
		 			    url = url + name + "=" + value; 
		 	    }//while
		     }//if
		     return url;
		  */

		 }

	public static int getParaInt(HashMap vPara, String vName ){
		vName=vName.toUpperCase();
		 Object vV="";
		if(vPara.containsKey(vName )){
			 vV=vPara.get(vName);
	    	 if(vV==null){vV="0";}
	         return Integer.parseInt(vV.toString()) ;
			 
		}else{
			return 0;
		}
		
	}
	public static String getParaStr(HashMap vPara, String vName ){
		vName=vName.toUpperCase();
		 Object vV="";
		if(vPara.containsKey(vName )){
			 vV=vPara.get(vName);
	    	 if(vV==null){vV="";}
	         return vV.toString() ;
			 
		}else{
			return "";
		}
		
	}
	public static String getRuleStr(String vStr){
		if(vStr.equals("")|| vStr==null){return "";}
		vStr=vStr.replace(";", ",");
		vStr=vStr.replace("、", ",");
		vStr=vStr.replace("，", ",");
		vStr=vStr.replace("；", ",");
		return vStr;
	}
	public static String StrReplecAll2(String vSource,String vOld,String vNew ){
		if(vSource.equals("")|| vSource==null){return "";}
		 while (vSource.indexOf(vOld)>-1){
			 vSource= vSource.replace(vOld, vNew);
		 }
		return vSource;
	}
	public static String StrReplecAll(String vSource, String strFrom, String strTo) {    
	    if (vSource == null || vSource.equals("")) {        
	      return "";    
	    }  
	    int i = 0;
	  //  vSource="<a title=\"点击直接进入页面信息\"  #__RIGHTTYPE# style=\"CURSOR: hand;\" onclick=\"javascript:ThinkP.gvViewLink('10170',this,'#CUSTOMERID#','10170','11','10','#__RIGHTTYPE#','#__RIGHTID#','#__ACTINROLEID#','#__FLOWID#','10615','','#WINDOWTYPE#','','#WINURL#','800','600','#WINSCR#','#__BTNOPTION#');\"  href=\"javascript:void(0);\">${NAME}</a>";
	    while ((i = vSource.indexOf(strFrom, i)) >= 0) {
	      char[] cSrc = vSource.toCharArray();
	      char[] cTo = strTo.toCharArray();
	      int len = strFrom.length();  
	      StringBuffer buf = new StringBuffer(cSrc.length);  
	      buf.append(cSrc, 0, i).append(cTo);
	      i += len;
	      int j = i;
	      while ((i = vSource.indexOf(strFrom, i)) > 0) {  
	        buf.append(cSrc, j, i - j).append(cTo);   
	        i += len;  
	        j = i;
	      }
	      buf.append(cSrc, j, cSrc.length - j); 
	      return buf.toString(); 
	    } 
	    strT="";
	    if(strT.equals("")){
	    	return vSource;
	    }else{
	    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
	    }
	  } 
	
	  public static String[] getSplitStr(String strOld)
      {
		  String schar = "\uff0c";
          strOld = strOld.replace(schar, ",");// 
          strOld = StrDH(strOld);
          return strOld.split(",");
      }
	public static final boolean isCN(String  strCN) {
		if(strCN.equals("")){return false;}
		  char[] vCharArray = strCN.toCharArray();
		   for (int i = 0; i < vCharArray.length; i++) {
		   if ((vCharArray[i] >= 0x4e00) && (vCharArray[i] <= 0x9fbb)) {
		    return true;
		   }
		  }
		  return false;
	}
	public static String NumberToStr(Number vValue,String vPattern){
		  try{
		   DecimalFormat dFormat = new DecimalFormat(vPattern);
		   return dFormat.format(vValue);
		  }catch (Exception e){
		  // log.error("飞龙decimalFormat异常,传入的参数为,[value:" + value + "],[pattern:" + vPattern + "]");
		  }finally{;}
		  return "";
		 }
	
 
	public static Map getUrlField (String vUrl){
		//alert(vName+"   "+vUrl);
		Map vMap=new HashMap();
		if (vUrl==null || vUrl.equals("")) return new HashMap();
 
		if (vUrl.indexOf("?") == 0) vUrl = vUrl.substring(1);
		if (vUrl.indexOf("&") >= 0){
			if (vUrl.indexOf("&&") >= 0){
				vUrl=DM.StrReplecAll(vUrl,"&&","&");
			}
			String[]vFs = vUrl.split("&");
			String sTempQuery="",vN="",vV="";
			for (int nTempCount = 0; nTempCount < vFs.length; nTempCount++){
				sTempQuery = vFs[nTempCount];
				if (sTempQuery.indexOf("=") >= 0){
					vN=sTempQuery.substring(0,sTempQuery.indexOf("="));
					vV=sTempQuery.substring(sTempQuery.indexOf("=")+1,sTempQuery.length()).trim();
					if(vMap.containsKey(vN) && vV.equals("")){
						if(vMap.get(vN).equals("")){
							vMap.remove(vN);
					         vMap.put(vN, vV);
						}
					}else if(!vV.equals("")){
						 vMap.put(vN, vV);
					}
				} 
			}
		}
		 
		 strT="";
		    if(strT.equals("")){
		    	return vMap;
		    }else{
		    	try {return  null;} catch (Exception ex) {return null;}finally{strT="";}
		    }
	}
	
 
	/**
	 * 特定“字符串”在 某字符串 中出现的次数
	 * 
	 * @param vSource
	 * @param vSubStr
	 * @return
	 */
	public static int  getStrOfSubStrTimes(String vSource, String vSubStr) {
		if(vSource==null){return 0;}
		if(vSubStr==null){return 0;}
		if(vSource.trim().equals("") || vSubStr.trim().equals("")){return 0;}
		String[] array = vSource.split(vSubStr);
		return array.length-1;
	}
	public static boolean checkList(List vLst){
		if(vLst==null){
			return false;
			}else if(vLst.size()==0){
				return false;
			}else {
				return true;
			}
	 
	}
	public static boolean checkMap(HashMap vMap){
		if(vMap==null){
			return false;
			}else if(vMap.isEmpty()){
				return false;
			}else {
				return true;
			}
	 
	}
public static void OutPrint( String vStr ){
		  System.out.println(vStr);
}
 
public static String[] getArraySort(String[]vStr)
{
	String[] text = vStr; 
	String strT="";
	  for (int i = 0; i < text.length; i++) {
		  for (int j = i+1; j < text.length; j++) {
			  if (text[i].compareTo(text[j]) > 0) {
				  strT = text[i];
				  text[i] = text[j]; 
				  text[j] = strT;
				  }
			  } 
		  } 
	return text;
}

public static boolean isEmpty(String strV)
{
	return  StringUtils.isEmpty(strV);
}
public static String strJoinArray(String[] array)
{ 
	return StringUtils.join(array);
}
public static String NullToStr(Object obj)
{
    String vStr = "";
    if (obj != null)
    { 
        vStr = obj.toString();
        return vStr.trim();
    }
    else
    {
        return vStr;
    }
}
public static String NullToStr(Object obj,boolean vIsUpper)
	{
	    String vStr = "";
	    if (obj != null)
	    { 
	    	 vStr = obj.toString().trim();
	    	if(vIsUpper){vStr.toUpperCase();}

	        return vStr;
	    }
	    else
	    {
	        return vStr;
	    }
	}
 
	
	public static String NullToStr(Object obj,String  vValue)
	{
	    String vStr = "";
	    if (obj != null)
	    { 
	    	 vStr = obj.toString().trim();
	        return vStr;
	    }
	    else
	    {
	        return vValue.trim();
	    }
	}
	/*
		 这种特殊码是什么，其实就是unicode吗 只是有格式而已。 
		&#x96C6;  96c6 是16进制的格式。
	  */
	public static String getStrHexDecode(String vSource )
	{
		 // 测试用中文字符  
	      //vSource = "C&#x96C6;&#x56E2;&#x5929;c&#x6D25;&#x5927;&#x5510;&#x56FD;&#x9645;&#x76D8;&#x5C71;&#x53D1;&#x7535;&#x6709;&#x9650;&#x8D23;&#x4EFB;&#x516C;&#x53F8;";  
		if(vSource==null){return"";} 
		// 定义正则表达式来搜索中文字符的转义符号  
        Pattern compile = Pattern.compile("&#.*?;");  
        Matcher matcher = compile.matcher(vSource);  
        // 循环搜索 并转换 替换  
        while (matcher.find()) {  
            String group = matcher.group();  
            // 获得16进制的码  
            String hexcode = "0" + group.replaceAll("(&#|;)", "");  
            // 字符串形式的16进制码转成int并转成char 并替换到源串中  
            vSource = vSource.replaceAll(group, (char) Integer.decode(hexcode).intValue() + "");  
	   }
        return vSource;
	}
public static String getParaDecode(String vSource, String strParaField, boolean IsCn,  List contentList, int iRow)
{
	if(!checkList(contentList)|| strParaField.equals("") || vSource.equals("")){return vSource;}
    String strValue = "", strTmp = ""; ;
   // vSource = getCurent_User_Content(vSource);
    strParaField = strParaField.toUpperCase();
    String[] strShowParas = strParaField.split(",");//url参数解析
    for (int cP = 0; cP < strShowParas.length; cP++)
    {
        strShowParas[cP] =   strShowParas[cP].trim();
        if (!strShowParas[cP].equals(""))
        {
            strValue = getLstV(contentList, iRow, strShowParas[cP]);
            if (IsCn && vSource.indexOf("#" + strShowParas[cP] + "#") > -1 && !strValue.equals(""))
            {
                strTmp = getLstV(contentList, iRow, strShowParas[cP] + "_CN");
                if (strTmp != "") { strValue = strTmp; }
            }
            if (vSource.indexOf("#" + strShowParas[cP] + "#") > -1 && !strValue.equals(""))
            {
            	vSource=DM.StrReplecAll(vSource,"#" + strShowParas[cP] + "#", strValue);
            }
        }
    }
    
    strT="";
    if(strT.equals("")){
    	return vSource;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
}
public static String getParaDecode(String vSource, String strParaField,  List  contentList,int iRow,String vDefaultValue)
{
 if(!checkList(contentList)|| strParaField.equals("") || vSource.equals("")){return vSource;}
    String strValue = "", strTmp = ""; ;
   // vSource = getCurent_User_Content(vSource);
    strParaField = strParaField.toUpperCase();
    String[] strShowParas = strParaField.split(",");//url参数解析
    for (int cP = 0; cP < strShowParas.length; cP++)
    {
        strShowParas[cP] =  DM.StrDH(strShowParas[cP]);
        if (!strShowParas[cP].equals(""))
        {
            strValue = getLstV(contentList, iRow, strShowParas[cP]);
            if(strValue.equals("")&& !vDefaultValue.equals("")){
            	strValue=vDefaultValue;
            }
            vSource = vSource.replace("#" + strShowParas[cP] + "#", strValue);
        }
    }
    
    strT="";
    if(strT.equals("")){
    	return vSource;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
    
}
public static String getRequestParaDecode(HttpServletRequest request,String vSource, String strParaFields,String vDefaultValue)
{//可以自动屏蔽为 _isnull_ 的情况
   strParaFields = DM.StrDH(strParaFields);
   String[] vPField = DM.getSplitStr(strParaFields);
// strRequestUrl = DM.getRequestParaValue(request, PField[0], true);
   String []vdV=vDefaultValue.split(",");
for (int i = 0; i < vPField.length; i++)
{
    if (vSource.indexOf("#" + vPField[i] + "#") > -1 &&( DM.getRequestValueURL (request, vPField[i] ) ==null ||DM.getRequestValueURL (request, vPField[i] ).equals("_isnull_")  )  )
    {
    	if(vdV.length==1){
       vSource = vSource.replace("#" + vPField[i] + "#", vDefaultValue);
    	}else if(vdV.length==vPField.length ){
    		 vSource = vSource.replace("#" + vPField[i] + "#", vdV[i]);
    	}
   }else{
	   vSource = vSource.replace("#" + vPField[i] + "#", DM.getRequestValueURL(request, vPField[i]));
   }
}
strT="";
if(strT.equals("")){
	return vSource;
}else{
	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
}
 
}
public static String getRequestParaDecode(HttpServletRequest request,String vSource, String strParaFields,boolean bIsNulltoStr){
	 try { ;} catch (Exception ex) {;}finally{;}
		if(request==null){return vSource;}
		if(vSource.indexOf("#")<0){return vSource; }
	   strParaFields = DM.StrDH(strParaFields);
	   String[] vPField = DM.getSplitStr(strParaFields);
	 // strRequestUrl = DM.getRequestParaValue(request, vPField[0], true);
	  String strT="";
	for (int i = 0; i < vPField.length; i++)
	{   vPField[i]= vPField[i].trim();
	    if (vSource.indexOf("#" + vPField[i] + "#") > -1 && DM.getRequestValueURL (request, vPField[i]) !=null)
	    {
	    	strT=DM.getRequestValue(request, vPField[i]);
	         if(!strT.equals("") ||bIsNulltoStr){
	        	vSource =DM.StrReplecAll(vSource,"#" + vPField[i] + "#",strT );
	         }
	   }
	}
	strT="";
	if(strT.equals("")){
		return vSource;
	}else{
		try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
	}
}

public static String getRequestParaDecode(HttpServletRequest request,String vSource, String strParaFields )
{ try { ;} catch (Exception ex) {;}finally{;}
	if(request==null){return vSource;}
	if(strParaFields.equals("")){return vSource;}
	if(vSource.indexOf("#")<0){return vSource; }
   strParaFields = DM.StrDH(strParaFields);
   String[] vPField = DM.getSplitStr(strParaFields);
 // strRequestUrl = DM.getRequestParaValue(request, vPField[0], true);
  String strT="";
for (int i = 0; i < vPField.length; i++)
{   vPField[i]= vPField[i].trim();
 
    if (vSource.indexOf("#" + vPField[i] + "#") > -1 && DM.getRequestValueURL (request, vPField[i]) !=null)
    {
    	strT=DM.getRequestValue(request, vPField[i]);
         if(!strT.equals("")){
        	vSource =DM.StrReplecAll(vSource,"#" + vPField[i] + "#",strT );
         }
   }
}
strT="";
if(strT.equals("")){
	return vSource;
}else{
	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
}
}
public static String getStringParameter(HttpServletRequest request, String vName)
{
//	if (vName == null)
 		return null;
//	if (request.getParameter("fileFlag") != null && request.getAttribute("uploadWrapper") != null)
//	{
//		DiskFileUploadWrapper uploadWrapper = (DiskFileUploadWrapper)request.getAttribute("uploadWrapper");
//		return uploadWrapper.getParameter(vName.trim());
//	}
//	if ("true".equals(request.getParameter("isAjaxRequest")))
//	{
//		String value = request.getParameter(vName.trim());
//		return value == null ? value : Escape.unescape(value);
//	} else
//	{
//		return request.getParameter(vName.trim());
//	}
}

public static String[] getStringParameters(HttpServletRequest request, String vName)
{
	if (vName == null)
		return null;
	if ("true".equals(request.getParameter("isAjaxRequest")))
	{
		String vValue[] = request.getParameterValues(vName.trim());
		if (vValue != null)
		{
//			for (int i = 0; i < value.length; i++)
//				value[i] = Escape.unescape(value[i]);

		}
		return vValue;
	} else
	{
		return request.getParameterValues(vName.trim());
	}
}

public static String getStringParameter(HttpServletRequest request, String vName, String def)
{
	String value = getStringParameter(request, vName);
	if (value == null)
		return def;
	else
		return value.trim();
}
public static String getRequestParaDecode(HttpServletRequest request, String strParaField )
{  try { ;} catch (Exception ex) {;}finally{;}
	
	if(strParaField==null || strParaField.equals("")){return"";}
    String strRequestUrl = "";
    strParaField = DM.StrDH(strParaField);
    String[] vPField = DM.getSplitStr(strParaField);
    for (int i = 0; i < vPField.length; i++)
    {
    	 if(DM.getRequestValue(request,vPField[i],false)!=null){
           strRequestUrl = strRequestUrl + "," + DM.getRequestValueURL (request, vPField[i]);
    	 }
    }
    strT="";
    if(strT.equals("")){
    	 return StrDH(strRequestUrl);
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
}
 
 
 

public static String getRequestValue(HttpServletRequest request, String vName )
{try { ;} catch (Exception ex) {;}finally{;}
    return getRequestValue(request, vName, true);
}

 
public static String getRequestValue(HttpServletRequest request, String vName,String vValue )
{
	 String v=getRequestValue(request, vName, true);
	 if(v.equals("")){v=vValue;}
    return v ;
}

public static boolean  IsEmpty(String obj)
{
    
    if (obj == null)
    { 
        return true;
    }
    else if(obj.trim().equals(""))
    {
    	
        return true;
    }
    return false;
}
public static String getRequestValue1(HttpServletRequest request, String vName )
{
    String vT=getRequestValue(request, vName, true); 
    	if(vT.equals("")&& vName.indexOf("__")==-1){
    		vT=getRequestValue(request,"__"+vName, true); 
    	}
    return vT;
}
 
public static String getRequestValueForm(HttpServletRequest request, String vName, boolean IsToString  ){
	strT=getRequestValueForm( request, vName);
	if(IsToString&& strT==null){return "";}
	return strT;
}
public static String getRequestValueForm(HttpServletRequest request, String vName ){//待完善
	if(request==null){return "";}
	 String vT=null,strValue=""; 
	 /* 
	 Enumeration rnames=request.getParameterNames();
	 for (Enumeration e = rnames ; e.hasMoreElements() ;) {
	         String thisName=e.nextElement().toString();
	         String thisValue=request.getParameter(thisName);
	         if(thisName.toLowerCase().equals(vName.toLowerCase())){
	        	 return  thisValue ;
	         }
	        // System.out.println(thisName+"-------"+thisValue);
	 } 
	 */
	  String vT11="";vT=null;
	 Map<String,String[]> map = request.getParameterMap();
     for(Entry<String, String[]> me : map.entrySet()){
       String name = me.getKey();
       String [] v = me.getValue();
       if(name.toLowerCase().equals(vName.toLowerCase())){
    	   vT="";
    	   vT11= DM.getRequestValueURL(request, name);
    	   if(!vT11.equals("")){
    		   if(v.length==1){
    			   return null;
    		   }else if(v.length==2){
    			   return v[1];
    		   }
    	   }
    	   for(int i=0;i<v.length;i++){
    		   if(!v[0].equals(vT11)){
    			   vT=vT+(!vT.equals("") ? "," : "") +v[i];
    		   }
    	   }
      	 return  vT;
       } 
     }
	 return vT;
}
public static String getRequestValueURL(HttpServletRequest request, String vName,String vDefaultValue ){
	if(request==null){return "";}
	 if(vName==null){return "";}
	 if(vDefaultValue==null){return "";}
	 String vT= getRequestValueURL( request,  vName );
	 if(vT.equals("")){vT=vDefaultValue;}
	return vT;
}
public static String getRequestValueURL(HttpServletRequest request, String vName ){//待完善

	if(request==null){return "";}
	 if(vName==null){return "";}
	 if(vName.equals("")){return "";}
	 String vT="";//getRequestValue(request,vName,true);
	 if(!vT.equals("") && vT.indexOf(",")<0){
		 return vT;
	 }else{
		 String v=NullToStr(request.getQueryString());
		 String v121="";
		 if(v.equals("")){return v;}
		 v=DM.StrReplecAll(v, "&&", "&");
		 v=DM.StrReplecAll(v, "?", "&");
		 v121=v.toLowerCase();
		 vName=vName.toLowerCase();
		 if(("&"+v121).indexOf("&"+vName+"=")==-1 ){return "";}
		 v121="&"+v121+"&";
		 v="&"+v+"&";
		// vT=v.substring(v.indexOf("&"+vName+"=")+vName.length()+2, v.indexOf("&",v.indexOf("&"+vName+"=")+1));
		 vT=v.substring(v121.indexOf("&"+vName+"=")+vName.length()+2, v121.indexOf("&",v121.indexOf("&"+vName+"=")+1));
		if(vT.indexOf("%")>-1){
			if(request.getCharacterEncoding() !=null){
				//vT=BaseUtil.decode(vT,request.getCharacterEncoding());//
				try {
					if(vT!=null && !vT.equals(""))
					{
						vT=URLDecoder.decode(vT,request.getCharacterEncoding());
						//vT=URLDecoder.decode(vT);
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
				
			}else{
			 vT=java.net.URLDecoder.decode(vT);
			}
		}
	 }
	 return NullToStr(vT);
}
public static String getRequestValue(HttpServletRequest request, String vName, boolean IsToString)
{   if(request==null){return "";}
	if(vName.equals("") && IsToString){return "";}else if(vName.equals("") && !IsToString){return null;}
	 String strValue="",strValue1="",strValue2="";
	 strValue1=DM.getRequestValueURL(request,vName);
	 if(strValue1.equals("")){
		 strValue1=DM.getRequestValueForm(request,vName);
		 if(strValue1==null&&IsToString){
			 return"";
		 }
		 if(strValue1==null ){
			 return null;
		 }
		if(!strValue1.equals("")){
			return strValue1;
		 }
	 }else{
		 if(!strValue1.equals("")){
			 return strValue1;
		 }
		  
	 }
	 if(!IsToString && strValue1==null){
		return null;
	 }else{
		 return "";
	 }
	 /*
	 15-06-30
	Map vPFs = request.getParameterMap();//把请求的KEY都取出来
	String vN="";
	if( !vPFs.containsKey(vName)){//纠正参数大小写，
		Iterator i = vPFs.keySet().iterator();//准备迭代输出
		vName=vName.toLowerCase();
		while(i.hasNext()){//开始迭代
			vN=i.next().toString();
			 if (vN.toLowerCase().equals(vName)){
				 vName=vN;
				 break;
			 }
		}
	}
    String strCNEncodeType = "0";
    if (!IsToString )
    {
    	if(request.getParameter(vName)==null){return  null;}
    }
        if (request == null) { return ""; }
       
        String tV[]=request.getParameterValues(vName);
        if(tV!=null){// checkbox or array
        	for(int i=0;i<tV.length;i++){// != ""
        	strValue= (!strValue.equals("") ? strValue + "," : "") +l10001011011(tV[i]); 
        	}
        }else{
        	 strValue = l10001011011(request.getParameter(vName) );
        }
     
        String  strT=strValue;
            if(strValue.getBytes().length > strValue.length()){
            	 
        	   if(isMessyCode(strValue)){
		         
        	   }else{
               	//System.out.println(java.nio.charset.Charset.forName("GB2312").newEncoder().canEncode("汉字"));   
               	//System.out.println(java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode("汉字"));  
               	 if(java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode(strT)){
               		 
               	  }
            }
            }
           if(strT.indexOf("?")<0 && strValue.indexOf("?")>-1){
        	   strValue= strT;
           }
           String strT1=request.getQueryString();
           int iEnd=0;
           if(strT1 !=null){
	           if(strT1.indexOf("__debug=")>-1&& !strT1.equals("") ){
	        	   iEnd= strT1.length();
	        	   if (strT1.substring(strT1.indexOf("__debug=")+8,iEnd).equals(vName) ){
	        		   System.out.println("字段调试:"+request.getCharacterEncoding()+" "+vName+"="+ strValue  );
	        	   }
	          }
         }
           strT="";
           if(strT.equals("")){
           	return strValue;
           }else{
           	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
           }*/
  
}
private static boolean isChinese(char c) {    
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);    
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS    
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS    
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A    
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION    
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION    
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {    
      return true;    
    }    
    return false;    
  }
/*
 * 判断是否为乱码
 *   System.out.println(isMessyCode("*JTP.jar"));    
    System.out.println(isMessyCode("你好"));
  */   
  public static boolean isMessyCode(String strName) {    
	  if(strName.equals("")){return false;}
    Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");    
    Matcher m = p.matcher(strName);    
    String after = m.replaceAll("");    
    String strT = after.replaceAll("\\p{P}", "");    
    char[] ch = strT.trim().toCharArray();    
    float chLength = ch.length;    
    float count = 0;    
    for (int i = 0; i < ch.length; i++) {    
      char c = ch[i];    
      if (!Character.isLetterOrDigit(c)) {    
    
        if (!isChinese(c)) {    
          count = count + 1;    
         //System.out.print(c);    
        }    
      }    
    }    
    float result = count / chLength;    
    if (result > 0.4) {    
      return true;    
    } else {    
      return false;    
    }    
    
  }    

public static String getDistinctValue(String vValue)
{  
    if (vValue == null) { return ""; }
    if(vValue.equals("")){return"";}
    vValue=vValue.toUpperCase();
    vValue = DM.StrDH(vValue);
    boolean IsSame = false;

    String[] sV = vValue.split(",");
    for (int i = 0; i < sV.length; i++)
    {
        IsSame = false;
        for (int j = 0; j < sV.length; j++)
        {
            if (sV[i].equals( sV[j]) && IsSame)
            {
                sV[i] = "";
            }
            if (sV[i].equals(sV[j]))
            {
                IsSame = true;
            }

        }//j
    }//i
    vValue = "";
    for (int i = 0; i < sV.length; i++)
    {
        vValue = vValue + "," + sV[i];
    }
    vValue = DM.StrDH(vValue);
    return vValue;
}
public static String getStr_Char_Str(String vStr1,String vStr2,String vChar ){
	if( vStr1.equals("") || vStr2.equals("") ){vStr1=vStr1+vStr2;return vStr1;}
 	   if(vStr1.endsWith(vChar)&& vStr2.startsWith(vChar)){
 		  vStr1=vStr1+vStr2.subSequence(vChar.length(),vStr2.length());
        }else if(vStr1.endsWith(vChar)|| vStr2.startsWith(vChar)){
        	
        	if(vStr1.indexOf("?")>-1){
        		vStr1=vStr1+vStr2;
            	}else{
            		if(vStr2.startsWith("&")){vStr2=vStr2.substring(1);}
            		vStr1=vStr1+"?"+vStr2;
            	}
        	
        	
        }else {
        	if(vStr1.indexOf("?")>-1){
        	vStr1=vStr1+vChar+vStr2;
        	}else{
        		vStr1=vStr1+"?"+vStr2;
        	}
        }
     
	 return vStr1;
}
public static String getExpressStr(String vTmp,String vPrefix ,String vExValue)
{if(vExValue.equals("")){return vTmp;}
	if(vPrefix.equals("")){vPrefix= ",";}
	vTmp = (vTmp.equals("") ? vTmp : vTmp + vPrefix ) +vExValue;
    return vTmp;
}
//取得字符串 规范后 符合逗号分隔的规则串,用来Splite分割成数组用
public static String StrDH(String vTmp)
{
    String strTmp = "";
    if (vTmp == null) { return ""; }
    String schar = "\uff0c";
    vTmp = vTmp.trim();
    vTmp = vTmp.replace(schar, ",");

    String[] str = vTmp.split(",");
    for (int i = 0; i < str.length; i++)
    {
        if (!str[i].trim().equals(""))
        {
            strTmp = (strTmp.equals("") ? strTmp : strTmp + ",") + str[i];
        }
    }
    
    strT="";
    if(strT.equals("")){
    	return strTmp;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
}
/*
 * 数组自动补充到iCharMaxCount个
 * vSoure=原字符串
 * iCharMaxCount=总共个数
 * vAppendChar要补充的默认字符
 * */
public static String StrApendChar(String vSoure,int iCharMaxCount,String vAppendChar)
{  
	if(iCharMaxCount<1){iCharMaxCount=1;}if(vAppendChar==null||vAppendChar.equals("")){vAppendChar="0";}
    String strTmp = "";
    if (vSoure == null||vSoure.equals("") ) { return vAppendChar; }
     String[] str = vSoure.split(",");
    for (int i = 0; i < str.length ; i++)
    { 
        if (str[i].trim().equals(""))
        {str[i]="0";}
            strTmp = (strTmp.equals("") ? strTmp : strTmp + ",") + str[i];
        
    }
    for(int i=0; i<(iCharMaxCount-str.length);i++){
    	strTmp = (strTmp.equals("") ? strTmp : strTmp + ",") + vAppendChar;
    }
    
    return strTmp;
}
/// <summary>
/// 给字符串加单引号
/// </summary>
/// <param vName="str"></param>
/// <returns></returns>
private String getWhereInComma(String str)
{
    String vValue = "";
    for (int i = 0; i < str.split(",").length; i++)
    {
        if (vValue.equals(""))
        {
            if (!this.NullToStr(str.split(",")[i]).equals(""))
            {
                vValue = "'" + str.split(",")[i] + "'";
            }
        }
        else
        {
            if (!NullToStr(str.split(",")[i]).equals(""))
            {
                vValue += ",'" + str.split(",")[i] + "'";

            }
        }

    }
    if (vValue.equals(""))
    {
        return str;

    }
    strT="";
    if(strT.equals("")){
    	return vValue;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
    
}

public static String getWhereOrEqualsField(String strFieldName, String vValueS, boolean IsNumbericField)
{
    String strWhere = "";
    if (vValueS == "") { return " and 1=1 "; }
    vValueS = DM.StrDH(vValueS);
    String[] vValue = vValueS.split(",");
    String sChar = "'";
    if (IsNumbericField) { sChar = ""; }
    for (int i = 0; i < vValue.length; i++)
    {
        vValue[i] = vValue[i].trim();
        if (i == 0)
        {
            strWhere = strWhere + " " + strFieldName + " =" + sChar + vValue[i] + sChar + " ";
        }
        else
        {
            strWhere = strWhere + " or " + strFieldName + "=" + sChar + vValue[i] + sChar + " ";
        }
    }
      strWhere = " and (  " + strWhere + ")";
    strT="";
    if(strT.equals("")){
    	return strWhere;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
    
}

public static String getWhereOrNotInField(String strFieldName, String vValueS, boolean IsNumbericField)
{
    String strWhere = "";
    if (vValueS == "") { return " and 1=1 "; }
    vValueS = DM.StrDH(vValueS);
    String[] vValue = vValueS.split(",");
    String sChar = "'";
    if (IsNumbericField) { sChar = ""; }
    for (int i = 0; i < vValue.length; i++)
    {
        vValue[i] = vValue[i].trim();
        if (i == 0)
        {
            strWhere = strWhere + " " + strFieldName + " <>" + sChar + vValue[i] + sChar + " ";
        }
        else
        {
            strWhere = strWhere + " and " + strFieldName + "<>" + sChar + vValue[i] + sChar + " ";
        }
    }
      strWhere = " and (  " + strWhere + ")";
    
    strT="";
    if(strT.equals("")){
    	return strWhere;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
}
//取得字段strFieldNameOr或条件
public static String getWhereOrLikeField(String strFieldName, String vValueS)
{
    String strWhere = "";
    vValueS = DM.StrDH(vValueS);
    String[] vValue = vValueS.split(",");
    for (int i = 0; i < vValue.length; i++)
    {
        if (i == 0)
        {
            strWhere = strWhere + " " + strFieldName + " like'%" + vValue[i] + "%'";
        }
        else
        {
            strWhere = strWhere + " or " + strFieldName + " like'%" + vValue[i] + "%'";
        }
    }
      strWhere = " and (  " + strWhere + ")";
    
    strT="";
    if(strT.equals("")){
    	return strWhere;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
}
public static String getWhereOrNotLikeField(String strFieldName, String vValueS)
{
    String strWhere = "";
    vValueS = DM.StrDH(vValueS);
    String[] vValue = vValueS.split(",");
    for (int i = 0; i < vValue.length; i++)
    {
        if (i == 0)
        {
            strWhere = strWhere + " " + strFieldName + " like'%" + vValue[i] + "%'";
        }
        else
        {
            strWhere = strWhere + " and " + strFieldName + " not like'%" + vValue[i] + "%'";
        }
    }
      strWhere = " and (  " + strWhere + ")";
    strT="";
    if(strT.equals("")){
    	return strWhere;
    }else{
    	try {return  "";} catch (Exception ex) {return "";}finally{strT="";}
    }
}
//取得字符串schart，在vSource中重复的次数
public static int getStrRepeatCount(String vSource,String schart){
	if(vSource==null ){return 0;}
	if(vSource.equals("") ){return 0;}
	if(schart==null ){return 0;}
	if(schart.equals("") ){return 0;}
	int iCount=0;
	int iS=0;
	while (iS>-1){
		iS=vSource.indexOf(schart,iS);
		if(iS<0){ return iCount;}
		iS=iS+schart.length();
		iCount++;
	}
	return iCount;
}
public static int StrToInteger(String strV )
{
	if(strV==null ){return 0;}
	strV=DM.NullToStr(strV);
	if(strV.equals("")){return 0;}
	if(strV.indexOf(".")>-1){
		strV=strV.substring(0,strV.indexOf("."));
	}
    int iV = 0;int iValue=0;
    if(strV.equals("") ){return 0;}
    try{
	    if (DM.IsNumeric(strV))
	    {
	        iV = Integer.parseInt(strV);
	    }else{
	    	 return 0;
	    }
    }catch( Exception ex){
    	return 0;
    }
    return iV;
}
public static int StrToInteger(String strV, int iValue)
{    strV=strV;
	if(strV==null ){return iValue;}
	strV=DM.NullToStr(strV);
	if(strV.equals("")){return iValue;}
	if(strV.indexOf(".")>-1){
		strV=strV.substring(0,strV.indexOf("."));
	}
    int iV = 0;
    try{
	    if (IsNumeric(strV ))
	    {
	    	iV=Integer.parseInt(strV);
	    }else{
	    	 iV = iValue;	
	    }
    }
    catch( Exception ex){
    	return iValue;
    }
    return iV;
}
public static String IntToStr(Integer  iV)
{
    return iV.toString();
}


public static double StrToDouble(String strV )
{   if(strV==null ){return 0;}
    double iV = 0;double iValue=0;
    strV=DM.NullToStr(strV);
    if(strV.equals("") ){return 0;}
    try{
	    if (DM.IsNumeric(strV))
	    {
	        iV = Double.parseDouble(strV);
	    }else{
	    	 return 0.0;
	    }
    }catch( Exception ex){
    	return 0.0;
    }
    return iV;
}
public static double StrToDouble(String strV,double xV )
{   
	if(strV==null ){return 0;}
    double iV = 0;double iValue=0;
    strV=DM.NullToStr(strV);
    if(strV.equals("") ){return xV;}
    try{
	    if (DM.IsNumeric(strV))
	    {
	        iV = Double.parseDouble(strV);
	    }else{
	    	 return xV;
	    }
    }catch( Exception ex){
    	return xV;
    }
    return iV;
}
public static Object ListToArray(List list )
{
	/*
	Object[] array2=new String[list.size()];  
     for(int i=0;i<list.size();i++){  
         array2[i]=(String)list.get(i);  
     }*/
	Object[] array = (String[])list.toArray(new String[list.size()]);  
	return array;
}
//=============
/** 
 * String转换Map 
 *  
 * @param mapText 
 *            :需要转换的字符串 
 * @param KeySeparator 
 *            :字符串中的分隔符每一个key与value中的分割 
 * @param ElementSeparator 
 *            :字符串中每个元素的分割 
 * @return Map<?,?> 
 */  
public static Map<String, Object> StringToMap(String mapText) {  
	    String SEP1 = "#";  
	     String SEP2 = "|";  
    if (mapText == null || mapText.equals("")) {  
        return null;  
    }  
    mapText = mapText.substring(1);  

   // mapText = EspUtils.DecodeBase64(mapText);  

    Map<String, Object> map = new HashMap<String, Object>();  
    String[] text = mapText.split("\\" + SEP2); // 转换为数组  
    for (String str : text) {  
        String[] keyText = str.split(SEP1); // 转换key与value的数组  
        if (keyText.length < 1) {  
            continue;  
        }  
        String key = keyText[0]; // key  
        String value = keyText[1]; // value  
        if (value.charAt(0) == 'M') {  
            Map<?, ?> map1 = StringToMap(value);  
            map.put(key, map1);  
        } else if (value.charAt(0) == 'L') {  
            List<?> list = StringToList(value);  
            map.put(key, list);  
        } else {  
            map.put(key, value);  
        }  
    }  
    return map;  
}  

/** 
 * String转换List 
 *  
 * @param listText 
 *            :需要转换的文本 
 * @return List<?> 
 */  
public static List<Object> StringToList(String listText) {  
	  String SEP1 = "#";  
	  String SEP2 = "|";  
    if (listText == null || listText.equals("")) {  
        return null;  
    }
    listText = listText.substring(1);  

   // listText = EspUtils.DecodeBase64(listText);  

    List<Object> list = new ArrayList<Object>();  
    String[] text = listText.split(SEP1);  
    for (String str : text) {  
        if (str.charAt(0) == 'M') {  
            Map<?, ?> map = StringToMap(str);  
            list.add(map);  
        } else if (str.charAt(0) == 'L') {  
            List<?> lists = StringToList(str);  
            list.add(lists);  
        } else {  
            list.add(str);  
        }  
    }  
    return list;  
}  

 
//=========================
public static String getParaFldStr(String strOldStr, String strParaFields, String strParaValues)
{
	if(  strOldStr.equals("") || strParaFields.equals("")){return strOldStr;}
    if (strOldStr.indexOf("#") < 0) { return strOldStr; }
    String strValue = "";
    String schar = "\uff0c";
    if (strParaFields != "") { strParaFields = strParaFields.trim().toUpperCase(); }
    strParaFields = strParaFields.replace(schar, ",");// 
    String[] vPField = strParaFields.split(",");
    strParaValues = strParaValues.replace(schar, ",");//
    String[] pValue = strParaValues.split(",");
    int iFCount = vPField.length;
    if (pValue.length > vPField.length)
    {
        iFCount = pValue.length;
    }
    for (int m = 0; m < iFCount; m++)
    {
        vPField[m] = vPField[m].trim();
        if (strOldStr.indexOf("#" + vPField[m] + "#") > -1 && !vPField[m].equals(""))
        {
            strOldStr = strOldStr.replace("#" + vPField[m] + "#", "" + pValue[m] + "");
        }else if (strOldStr.toUpperCase().indexOf("#" + vPField[m] + "#") > -1 && !vPField[m].equals("")){
        	strOldStr = strOldStr.toUpperCase().replace("#" + vPField[m] + "#", "" + pValue[m] + "");
        }
    }
    return strOldStr;

}
private static String getLstV(List list,int i, String vName ){
	vName=vName.toUpperCase();	if(!DM.checkList(list)){return"";}
	 Object vV="";
	if(i>=list.size() || i<0){return null;}
	Map map=(Map)list.get(i);
	if(map.containsKey(vName )){
		 vV=map.get(vName);
    	 if(vV==null){vV="";}
         return vV.toString() ;
		 
	}else{
		return "";
	}
	
}
public static String getParaFldStr(String strOldStr, String strParaFields, List dt, int iRow)
{   
    if(!checkList(dt)|| strOldStr.equals("") || strParaFields.equals("")){return strOldStr;}
    if(dt.size()<iRow){return strOldStr;}
    if (strOldStr.indexOf("#") < 0) { return strOldStr; }
    String strValue = "";
    String[] vPField;
    String schar = "\uff0c";
    if (!strParaFields.equals("")) { strParaFields = strParaFields.trim().toUpperCase(); }
    strParaFields = strParaFields.replace(schar, ",");// 
    vPField = strParaFields.split(",");
    for (int m = 0; m < vPField.length; m++)
    {
        if (strOldStr.indexOf("#" + vPField[m] + "#") > -1 && !vPField[m].equals(""))
        {
            strValue = getLstV(dt, iRow, vPField[m]);
            if(!strValue.equals("")){
            strOldStr = strOldStr.replaceAll("#" + vPField[m] + "#", "" + strValue + "");
            }
            
        }
    }
    return strOldStr;
}

/*
 public int getCharLength(String str)
        {
        	if(str==null){return 0;}
     		byte b[]=str.getBytes(); 
     		int byteLength=b.length; 
     		int strLength=str.length(); 
     		return byteLength-strLength;
        }
 * */
	public static int getStrLength(String str){ 
		if(str==null){return 0;}
		if(str.equals("")){return 0;}
		char c;
		int chineseCount=0;  
		try {
		if(!str.equals("")){//判断是否为空
				str=new String(str.getBytes(),"GBK");//进行统一编码
		}
		for(int i=0;i<str.length();i++){//for循环
			c=str.charAt(i); //获得字符串中的每个字符
			if( String.valueOf(c).getBytes("GBK").length>1){//调用方法进行判断是否是汉字
			     chineseCount++; //等同于chineseCount=chineseCount+1
			}
			 chineseCount++; 
		}
		} catch (UnsupportedEncodingException e) {
			chineseCount=str.length();
		}  finally{;}
		return chineseCount;                   //返回汉字个数
	}
/** 
 * 按字节长度截取字符串 
 * @param str 将要截取的字符串参数 
 * @param toCount 截取的字节长度 
 * @param more 字符串末尾补上的字符串 
 * @return 返回截取后的字符串 
 */ 
public static String getStrLengthStr(String str, int toCount, String more) {  
	if(str==null ){return"";}
	int reInt = 0;
  String reStr = "";  
  if (str == null)return ""; 
  if(str.length()*2 <toCount){ return str;  }

  char[] tempChar = str.toCharArray();  
  for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {  
    String s1 = str.valueOf(tempChar[kk]);  
    byte[] b = s1.getBytes();  
    reInt += b.length;  
    reStr += tempChar[kk];  
  }  
  if (toCount == reInt || (toCount == reInt - 1))  
    reStr += more;  
  return reStr;  
}

public static String getLengthStr2(String oldStr, int maxLength, String endWith)
{

    if (NullToStr(oldStr)=="")
        //   throw   new   NullReferenceException( "原字符串不能为空 "); 
        return oldStr + endWith;
//    if (maxLength < 1)
//        throw new Exception("返回的字符串长度必须大于[0] ");
    if (oldStr.length() > maxLength)
    {
        String strTmp = oldStr.substring(0, maxLength);
        if ( NullToStr(endWith)=="")//String.IsNullOrEmpty
            return strTmp;
        else
            return strTmp + endWith;
    }
    return oldStr;
}
public static String getStrLengthStr11(String s, int l, String endStr)
{

    String strT = s.substring(0, (s.length() < l + 1) ? s.length() : l + 1);
//    byte[] encodedBytes =   System.Text.ASCIIEncoding.ASCII.GetBytes(strT);
//
     String outputStr = "";
//    int count = 0;
//
//    for (int i = 0; i < strT.length(); i++)
//    {
//        if ((int)encodedBytes[i] == 63)
//            count += 2;
//        else
//            count += 1;
//
//        if (count <= l - endStr.length())
//            outputStr += strT.substring(i, 1);
//        else if (count > l)
//            break;
//    }
//
//    if (count <= l)
//    {
//        outputStr = strT;
//        endStr = "";
//    }
//
//    outputStr += endStr;
    return outputStr;
}
 
public static String getFieldValueStr(List dt, String vFieldName)
{
//    if (! checkDataTable(dt)) { return ""; }
//    String strValueStr="";
//    vFieldName = vFieldName.ToUpper();
//    for (int i = 0; i < dt.Columns.Count; i++)
//    {
//        if (dt.Columns[i].ColumnName.ToUpper().equals(vFieldName))
//        {
//            for (int iRow = 0; iRow < dt.Rows.Count; iRow++)
//            {
//               strValueStr = strValueStr + "," + DM.l10001011011(dt.Rows[iRow][vFieldName]);
//            }
//            strValueStr = DM.StrDH(strValueStr); 
//            return strValueStr;
//            break;
//        }
//    }
    return "";
}
 
public static boolean IsNumeric(String vValue)
{  
  return vValue.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	/*if(vValue.startsWith("-")){
		vValue=vValue.replaceAll("-", "");
	}
	 if (vValue == null || vValue.equals("")) {vValue = "0";}
	   try{
		    vValue=vValue.replace(".", "0");
		    Pattern pattern = Pattern.compile("[0-9]*");
		    return pattern.matcher(vValue).matches();
	    }catch( Exception ex){
	    	return false;
	    }*/
}
public static int getCnAscii(char cn) {
	 byte[] bytes=null;
	 try {
	  bytes = (String.valueOf(cn)).getBytes("GB2312");
	 } catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	 }finally{;}
	 if (bytes == null || bytes.length > 2 || bytes.length <= 0) { 
		 return 0;
	  }
	 if (bytes.length == 1) { 
		 return bytes[0];
	  }
	 if (bytes.length == 2) { 
		 int hightByte = 256 + bytes[0];
		 int lowByte = 256 + bytes[1];
		 int ascii = (256 * hightByte + lowByte) - 256 * 256;
		 return ascii;
	  }
	 return 0;
	  }
 
public static String getDateFormateToStr(String strDate,String vFormat){
	  SimpleDateFormat formatter = new SimpleDateFormat (vFormat);  
	  java.util.Date vDate;
	try {
		vDate = formatter.parse(strDate);
		strDate = formatter.format(vDate);  
	} catch (ParseException e) {
 		e.printStackTrace();
	}  
	return strDate;
}
public static String EnCodeBase64(String vValue ){
	return "";
}
 
/// <summary>
///取得当前时间 10000=短日期,10001=长日期,10002=时间(不包含日期),10003=年月,10004=年,10005月
///取得时间(格式化时间)10010=短日期,10011=长日期,10012=时间(不包含日期),10003=年月,10004=年,10005月
/// 
/// </summary>
/// <param name="vType"></param>
/// <param name="vValue"></param>
/// <returns></returns>
public static String getDate(String vType, String vValue)
{
    String str0101 = "";
//    switch (vType)
//    {
//        case "10000"://2007-4-24
//            str0101 = DateTime.Today.ToString("yyyy-MM-dd");
//            break;
//        case "10001"://2007-04-24 15:52:19
//            str0101 = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
//            break;
//        case "10002"://16:30
//
//            str0101 = DateTime.Now.ToString("t");
//            break;
//        case "10003"://2007-04
//            str0101 = DateTime.Now.Year.ToString() + DateTime.Now.Month.ToString(); ;
//            break;
//        case "10004":
//            str0101 = DateTime.Now.Year.ToString();
//            break;
//        case "10005":
//            str0101 = DateTime.Now.Month.ToString();
//            break;
//
//        case "10010"://2007-4-24
//            if (IsDate(vValue))
//            {
//                str0101 = DateTime.Parse(vValue).ToShortDateString();
//            }
//            else
//            {
//                str0101 = "";
//            }
//            break;
//        case "10011"://2007-04-24 15:52:19
//
//            if (IsDate(vValue))
//            {
//                str0101 = DateTime.Parse(vValue).ToString("yyyy-MM-dd HH:mm:ss");
//            }
//            else
//            {
//                str0101 = "";
//            }
//            break;
//        case "10012":
//
//            break;
//
//        case "10013":
//            break;
//
//        case "10014":
//            break;
//        case "10015":
//            break;
//            str0101 = "\u661f\u671f" + DateTime.Parse(vValue).ToString("ddd", new System.Globalization.CultureInfo("zh-cn"));
//    }

    return str0101;
}
public static String getTime(String vType)
{
    String strTmp = "";
//    strTmp = DateTime.Now.AddDays(-1).ToShortDateString() + ";" + DateTime.Now.ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Now.ToShortDateString() + ";" + DateTime.Now.ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Now.AddDays(-1).ToShortDateString() + ";" + DateTime.Now.AddDays(-1).ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Now.AddDays(-6).ToShortDateString() + ";" + DateTime.Now.ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Today.AddDays(0 - GetWeek(DateTime.Now.DayOfWeek)).ToShortDateString() + ";" + DateTime.Now.ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Today.AddDays(0 - GetWeek(DateTime.Now.DayOfWeek) - 7).ToShortDateString() + ";" + DateTime.Now.AddDays(-1 - GetWeek(DateTime.Now.DayOfWeek)).ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Now.AddDays(-29).ToShortDateString() + ";" + DateTime.Now.ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Now.AddDays(1 - DateTime.Now.Day).ToShortDateString() + ";" + DateTime.Now.ToShortDateString() + "#";
//    strTmp = strTmp + DateTime.Now.AddMonths(-1).AddDays(1 - DateTime.Now.Day).ToShortDateString() + ";" + DateTime.Now.AddMonths(-1).AddDays(DateTime.DaysInMonth(DateTime.Now.AddMonths(-1).Year, DateTime.Now.AddMonths(-1).Month) - DateTime.Now.AddMonths(-1).Day).ToShortDateString() + "#";
//    if (strTmp.EndsWith("#"))
//        strTmp = strTmp.SubString(0, strTmp.Length - 1);
    return strTmp;
}
public static String getWeakDay(String vValue)
{   
	if(vValue.equals("&nbsp;")||vValue.equals("") ){return vValue;}
	if(!vValue.equals("")){
		String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		//String s = "2006-01-12 16:30";
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
		date = sdfInput.parse(vValue);
		} catch (ParseException e) {
		//e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		if(dayOfWeek<0)dayOfWeek=0;
 		  return dayNames[dayOfWeek] ;
	}
	 return "";
	 
}


/// <summary>
/// 判断当前日期是本周的第几天

/// </summary>
/// <param name="strDW"></param>
/// <returns></returns>
public static Integer getWeek(String strDW)//GetWeek(DayOfWeek strDW)
{
    Integer Iret = 0;
//    switch (strDW.ToString())
//    {
//        case "Monday":
//            {
//                Iret = 1;
//                break;
//            }
//        case "Tuesday":
//            {
//                Iret = 2;
//                break;
//            }
//        case "Wednesday":
//            {
//                Iret = 3;
//                break;
//            }
//        case "Thursday":
//            {
//                Iret = 4;
//                break;
//            }
//        case "Friday":
//            {
//                Iret = 5;
//                break;
//            }
//        case "Saturday":
//            {
//                Iret = 6;
//                break;
//            }
//        default:
//            {
//                Iret = 0;
//                break;
//            }
//    }
    return Iret;
}
public static void getPageScript(String vForm)
{
    //vForm.Attributes.Add("onsubmit", "return checkSubmit();");
    String script = ""; boolean isInHead = true;
    StringBuilder sbScript = new StringBuilder();
    sbScript.append(String.valueOf((char)13)+"");
    sbScript.append(String.valueOf((char)13)+" <script language=\"javascript\" type =\"text/javascript\">  ");
    sbScript.append(String.valueOf((char)13)+"var checkSubmitFlg = false; ");
    sbScript.append(String.valueOf((char)13)+" function checkSubmit() { ");
    sbScript.append(String.valueOf((char)13)+"    if (checkSubmitFlg == true) { ");
    sbScript.append(String.valueOf((char)13)+"      alert(\"你已经重复提交！\");");
    sbScript.append(String.valueOf((char)13)+"       return false; ");
    sbScript.append(String.valueOf((char)13)+"     } ");
    sbScript.append(String.valueOf((char)13)+"  setTimeout('disabledAllButtons();', 10);   ");
    sbScript.append(String.valueOf((char)13)+" checkSubmitFlg = true; ");
    sbScript.append(String.valueOf((char)13)+"  return true; ");
    sbScript.append(String.valueOf((char)13)+"} ");
    sbScript.append(String.valueOf((char)13)+"document.ondblclick = function docondblclick() { ");
    sbScript.append(String.valueOf((char)13)+" window.event.returnValue = false; ");
    sbScript.append(String.valueOf((char)13)+"} ");
    sbScript.append(String.valueOf((char)13)+"document.onclick = function doconclick() { ");
    sbScript.append(String.valueOf((char)13)+"    if (checkSubmitFlg) { ");
    sbScript.append(String.valueOf((char)13)+"     window.event.returnValue = false; ");
    sbScript.append(String.valueOf((char)13)+"   } ");
    sbScript.append(String.valueOf((char)13)+" } ");
    sbScript.append(String.valueOf((char)13)+" function disabledAllButtons() { ");
    sbScript.append(String.valueOf((char)13)+" var Objbuttons=document.getElementsByTagName(\"input\")   ");
    sbScript.append(String.valueOf((char)13)+" var i;");
    sbScript.append(String.valueOf((char)13)+"   for(i=0;i<Objbuttons.length;i++){   ");
    sbScript.append(String.valueOf((char)13)+"          if(Objbuttons[i].type==\"submit\" ||  Objbuttons[i].type==\"button\" ){   ");
    sbScript.append(String.valueOf((char)13)+"            Objbuttons[i].disabled=true;  ");
    sbScript.append(String.valueOf((char)13)+"         }   ");
    sbScript.append(String.valueOf((char)13)+"   }   ");
    sbScript.append(String.valueOf((char)13)+"  }   ");
    sbScript.append(String.valueOf((char)13)+"</script> ");
   // ((Page)HttpContext.Current.Handler).ClientScript.RegisterStartupScript(Type.GetType("System.String"), Guid.NewGuid().ToString(), sbScript.ToString());
}
public static String getErrorMsg(Exception ex,String vMsg,boolean vIsOutPrint){
	return getErrorMsg(ex,"","2",vMsg,vIsOutPrint);
}
////vErrorShowType 0提醒、1告警、2错误、3严重错误
public static String getErrorMsg(Exception ex,String vErrorShowType ,String vKey,String vMsg,boolean vIsOutPrint){
	 if(vKey.equals("") ||vKey==null ){vKey="dumai";}
	 if(vMsg==null){vMsg="";}
	 if(vErrorShowType==null){return "";}
	StringBuffer sb=new StringBuffer();
	if(vErrorShowType.equals("0") || vErrorShowType.equals("") ){
		 sb.append("提醒:"+DM.NullToStr(ex.getMessage())+String.valueOf((char)13));
	}else if(vErrorShowType.equals("1")){
		 sb.append("告警:"+DM.NullToStr(ex.getMessage())+String.valueOf((char)13));
	}else if(vErrorShowType.equals("2")){
		 sb.append("错误:"+DM.NullToStr(ex.getMessage())+String.valueOf((char)13));
	}else  if(vErrorShowType.equals("3")){
		sb.append("严重错误:"+DM.NullToStr(ex.getMessage())+String.valueOf((char)13));
	}
	 if(ex !=null )
	{   vKey=vKey.toLowerCase();
		if(!vMsg.equals("")){
		    sb.append( vMsg+String.valueOf((char)13));
		}
	    if(ex!=null){
	    sb.append("错误代码追踪如下:"+String.valueOf((char)13));
	    // }else{
	    	for(int i=0;i<ex.getStackTrace().length;i++){
			if(ex.getStackTrace()[i].toString().toLowerCase().indexOf(vKey)>0){
				if(sb.toString().indexOf(ex.getStackTrace()[i].toString())>-1){break;}
				sb.append(ex.getStackTrace()[i].toString()+String.valueOf((char)13));
			}
		 }
	    }
		if(vIsOutPrint){
			  System.out.println(sb.toString() );
			 // throw new  Exception("\u83B7\u53D6\u6570\u636E\u4EA7\u751F\u5F02\u5E38\uFF1A\n " + ex.getMessage()+String.valueOf((char)13)+sb.toString());
			}
		if(vErrorShowType.equals("5")){
			String strT=DM.StrReplecAll(sb.toString(), "\n", "<br>");
			strT=DM.StrReplecAll(strT, String.valueOf((char)13), "<br>");
			strT= DM.StrReplecAll(strT, "提醒:", "");
			strT= DM.StrReplecAll(strT, "告警:", "");
			strT= DM.StrReplecAll(strT, "错误:", "");
			strT= DM.StrReplecAll(strT, "严重错误:", "");
			strT=DM.StrReplecAll(strT, "<br><br>", "<br>"+String.valueOf((char)13));
			return "<div style=\"wdith:100%;text-align:left;\">"+strT+"</div>";
		}
	}
	//return sb.toString();
	try {return sb.toString() ;} catch (Exception ex1) {return sb.toString();}finally{strT="";}
	
}

public static String replaceXmlSpChar(String s)
{
    StringBuffer buffer = new StringBuffer();
    for(int i = 0; i < s.length(); i++)
        switch(s.charAt(i))
        {
        case 38: // '&'
            buffer.append("&amp;");
            break;

        case 60: // '<'
            buffer.append("&lt;");
            break;

        case 34: // '\"'
            buffer.append("&quot;");
            break;
            
        case 39: // '\''
            buffer.append("&quot;");
            break;

        default:
            buffer.append(s.charAt(i));
            break;
        }

    return buffer.toString();
}
public static String  HTMLEncode(String vStr){
if (!vStr.equals("")){
	vStr = vStr.replace( String.valueOf((char)38), "&#38;");
    vStr = vStr.replace ( ">", "&gt;");
    vStr = vStr.replace( "<", "&lt;");
    vStr = vStr.replace( String.valueOf((char)39), "&#39;");
    vStr = vStr.replace( String.valueOf((char)32), "&nbsp;");
    vStr = vStr.replace( String.valueOf((char)34), "&quot;");
    vStr = vStr.replace( String.valueOf((char)13), "<br>");
    vStr = vStr.replace( String.valueOf((char)10), "<br>");
}
return vStr;
}
//取得文件名
public static String extFileName(String vFileName)
{
    vFileName = vFileName.replace('\\', '/');
    int n = vFileName.lastIndexOf("/");
    if(n > -1)
        vFileName = vFileName.substring(n + 1);
    return vFileName;
}
//-----------
private static String toHexString(int i) {
	String strT;
	strT = Integer.toHexString(i).toUpperCase();

	return (strT.length() < 2) ? "0" + strT : strT;
}

/* 加密算法 */
public static String setPassword(String pass) {
	int strLen;
	String strT;
	String sRet;

	sRet = "";
	strLen = pass.length();

	for (int i = 0; i < strLen; i++) {
		strT = toHexString((int)pass.charAt(i) + (int)BASE_STRING.charAt(i % 4));
		if (strT.length() != 2)
			strT = "" + (i + 1) + strT;
		sRet = sRet + strT;
	}

	for (int i = 0; i < 15 - strLen; i++) {
		sRet = sRet + toHexString((int)BASE_STRING.charAt(i % 4) + strLen);
	}

	sRet = sRet + toHexString(strLen + 6);
	 
	try {return sRet ;} catch (Exception ex) {return sRet;}finally{strT="";}
}

private static int hexStringToInt(String sHex) {
	try {
		return Integer.parseInt(sHex, 16);
	} catch (NumberFormatException ex) {
		return 0;
	}finally{;}
}

/* 解密算法 */
public static String getPassword(String pass) {
	int strLen;
	String strT;
	String sRet;

	if (pass.length() != 32)
		return "";

	strT = pass.substring(30, 32);
	strLen = hexStringToInt(strT) - 6;

	if (strLen > 30)
		return "";

	sRet = "";
	for (int i = 0; i < strLen; i++) {
		strT = pass.substring(i * 2, i * 2 + 2);
		sRet += (char) (hexStringToInt(strT) - BASE_STRING.charAt(i % 4));
	}
	 
	try {return sRet ;} catch (Exception ex) {return sRet;}finally{strT="";}
}
//---------------
public static String setEncodeWord(String vPassWord)
{
    String vStr = "1234567890-=+_)(*&^%$#@!{}|\\][:\";?Z,./~`qazxswedcvfrtgbnhyujmkiolpPOLKIMNJUYHBVGTRFCXDEWSQA";
    String vResult = "";
    String strT = "               ";
    if(vPassWord.equals(""))
        return "";
    Random ran1 = new Random();
    vPassWord = vPassWord + strT.substring(vPassWord.length());
    for(int i = 0; i < vPassWord.length(); i++)
    {
        char c = vPassWord.charAt(i);
        int n = 1000 + c;
        for(int j = 0; j < 3; j++)
        {
            int x = n % 10;
            int y = ran1.nextInt(9);
            n /= 10;
            c = vStr.charAt(y * 10 + x);
            vResult = vResult + c + y;
        }

    }
    try {return vResult ;} catch (Exception ex) {return vResult;}finally{strT="";}
    
}

public static String getDecodeWord(String vPassWord)
{
    String vResult = "";
    String a21212 = "1234567890-=+_)(*&^%$#@!{}|\\][:\";?Z,./~`qazxswedcvfrtgbnhyujmkiolpPOLKIMNJUYHBVGTRFCXDEWSQA";
    if(vPassWord == null || vPassWord.equals(""))
        return "";
    if(vPassWord.charAt(0) == '!')
        return vPassWord.substring(1);
    int k = vPassWord.length();
    for(int i = 0; i < k - 1;)
    {
        int l = 0;
        int kl = 0;
        for(; l < 3; l++)
        {
            char c = vPassWord.charAt(i);
            char cn = vPassWord.charAt(i + 1);
            i += 2;
            int n = 0;
            for(int j = 0; j < 90; j++)
            {
                if(c != a21212.charAt(j))
                    continue;
                n = j;
                break;
            }

            int y = n / 10;
            int x = n % 10;
            if(y != cn - 48)
                return "error password";
            kl += x * (int)Math.pow(10D, l);
        }

        vResult = vResult + (char)kl;
    }
    try {return rightTrim(vResult) ;} catch (Exception ex) {return vResult;}finally{strT="";}
     
}
public static String rightTrim(String vValue)
{
    int i;
    for(i = vValue.length(); i > 0; i--)
        if(vValue.charAt(i - 1) != ' ')
            break;

    return vValue.substring(0, i);
}
/*
* 通过递归得到某一路径下所有的目录及其文件 
*/  
public static String getPathJSON(String vID,String vRoot,Boolean vIsTagParent){
StringBuffer ab=new StringBuffer("[");
String vPath="";
if(vID.startsWith("0")){ 
	vPath=vRoot+""+vID.substring(1, vID.length() );
}else{vPath=vID;}

vID= vPath.replace(vRoot, "0");
if(vID.equals("")){vID="0";}
  vPath=vPath.replace("$", "\\");
  File root = new File(vPath);
  File[] vFiles = root.listFiles();
  int iDirCount=0;int f=0;
  for(int i=0;i<vFiles.length;i++){  if(vFiles[i].isDirectory()){iDirCount++; }}
  boolean  vIsParent=false;
  for(int i=0;i<vFiles.length;i++){
	  vIsParent=false;
  if(vFiles[i].isDirectory()){
	  if(vIsTagParent){
		  File vP = new File(vPath+"\\"+vFiles[i].getName());
		  File[] vSubP = vP.listFiles();
		  for(int t=0;t<vSubP.length;t++){  if(vSubP[t].isDirectory()){ vIsParent=true; break; }}
	  }
	  ab.append(" {'id':'"+(vID+"$"+vFiles[i].getName())+"','pid':'"+vID+"','name':'"+(vFiles[i].getName())+"','isParent':"+vIsParent+"}");
	  if(f<iDirCount-1){ ab.append(",\n");}
	  f++;
    // getFiles(file.getAbsolutePath());  
    // filelist.add(file.getAbsolutePath());  
    // System.out.println("显示"+filePath+"下所有子目录及其文件"+file.getAbsolutePath());  
  }else{  
	  //System.out.println("显示"+filePath+"下所有子目录"+file.getAbsolutePath());  
   }
  }//for
  ab.append("]");
  return ab.toString();
}
 

public static void StringBufferClear(StringBuffer sb){
 	if(sb!=null){
 		sb.delete(0, sb.length());
 	}else{
 		sb=new StringBuffer();
 	} 	
 }
public static void ListClear(List vLst){
 	if(vLst!=null){
 		vLst.clear();
 		vLst=null;
 	}
 }
public static void Fake(Exception e)
{
    e.toString();
}
//线程监控器
public static void ThreadMonitor ( ) {
    Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
    Set<Thread> set = map.keySet();
    for(Thread thread : set){
        System.out.println("检测到线程 ["+thread.getId()+":"+thread.getName()+"],线程详细信息：");
        for(StackTraceElement trace:map.get(thread)){
           //System.out.println(trace);
        }
    }
 }
   
    public static String getAsscII(String vCharNum) { 
    	if(DM.IsNumeric(vCharNum)){
    		return String.valueOf((char) DM.StrToInteger(vCharNum));
    	}
    	return"";
    }
    public static String getAsscII(int vCharNum) { 
    		return String.valueOf((char) vCharNum);
     }
    public static String getArrayToStr(ArrayList vL)
    {
    	String str = "";
        for (int i = 0; i < vL.size(); i++)
        {
            str += "," +  NullToStr(vL.get(i));
        }
        return str = DM.StrDH(str);
    }
    public static String getMaxStr(String vValue)
    {
        vValue = DM.StrDH(vValue);
        String[] list = vValue.split(",");
        return list[list.length - 1];
    }
    public static String getMinStr(String vValue)
    {
        vValue = DM.StrDH(vValue);
        String[] list = vValue.split(",");
        return list[0];
    }
    public static String getSortStr(String vValue)
    {
        vValue = DM.StrDH(vValue);
        String[] list = vValue.split(",");
        if (list.length == 1) return vValue;
        int i, j; String temp;
        boolean done = false;
        j = 1;
        while ((j < list.length) && (!done))
        {
            done = true;
            for (i = 0; i < list.length - j; i++)
            {
                if (DM.StrToInteger(list[i]) >DM.StrToInteger(list[i + 1]))
                {
                    done = false;
                    temp = list[i];
                    list[i] = list[i + 1];
                    list[i + 1] = temp;
                }
            }
            j++;
        }
        vValue = list[0];
        for (int t = 1; t < list.length; t++)
        {
            vValue = vValue + "," + list[t];
        }
        return vValue;
    }
    
    public static String getDateToStr(String vV,String vFormate ) {
    	return vV;
    }
    public static void getWebFormPath( ) {
    	
    }
    public static String getVirtualPath( ) {
    	return"";
    }
    /*
    String str = "中国";  
    printBytes("中国的UNICODE编码：", str.getBytes(Charset.forName("unicode")));  
    printBytes("中国的GBK编码：", str.getBytes(Charset.forName("GBK")));  
    printBytes("中国的UTF-8编码：", str.getBytes(Charset.forName("UTF-8")));  
     * */
    public static void printBytes(String title, byte[] data) {  
       // System.out.println(title);  
        for (byte b : data) {  
            System.out.print("0x" + toHexString(b) + " ");  
        }
       // System.out.println();     
    }  
    private static String toHexString(byte vValue) {  
        String tmp = Integer.toHexString(vValue & 0xFF);  
        if (tmp.length() == 1) {  
            tmp = "0" + tmp;
        }  
        return tmp.toUpperCase();  
    }
    public static String getArrayToString(String [] data) {  
    	return   Arrays.toString(data);
    }
	/**
	 * @param 转为String 为空返回默认值def
	 * @param o
	 * @param def
	 * @return
	 */
	public static String getString(Object o, String def){
		if(o==null){
			return def;
		}
		return String.valueOf(o);		
	}
	
	/**
	 * 转为String
	 * @param o
	 * @return
	 */
	public static String getString(Object o){
		return String.valueOf(o);		
	}
	/**
	 * 转为double
	 * @param o
	 * @return
	 */
	public static double getDouble(Object o){
		  double aa=Double.parseDouble(o.toString());
		return aa;		
	}
	
	/**
	 * 转为Boolean
	 * @param o
	 * @return
	 */
	public static boolean getBoolean(Object o, boolean def ){
		try{
			if(o==null){
				return def;
			}
			//def =true; 无论怎么返回都是token过期
			def = Boolean.parseBoolean(String.valueOf(o));
		}catch(Exception e){
			e.printStackTrace();
		}		
		return def;
	}
	
	/**
	 * @param 转为INT
	 * @param o
	 * @return
	 */
	public static int getInt(Object o, int def ){
		try{
			if(o==null){
				return def;
			}
			def = Integer.parseInt(o.toString());
		}catch(Exception e){
			e.printStackTrace();
		}		
		return def;
	}
	
	/**
	 * 转为long
	 * @param o
	 * @return
	 */
	public static long getLong(Object o, long def ){
		try{
			if(o==null){
				return def;
			}
			String str = o.toString();
			if(str.equals("")||str.equals("null")){
				return def;
			}
			
			def = Long.parseLong(o.toString());
		}catch(Exception e){
			e.printStackTrace();
		}		
		return def;
	}
}

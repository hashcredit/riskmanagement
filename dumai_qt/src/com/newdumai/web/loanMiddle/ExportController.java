package com.newdumai.web.loanMiddle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URL;

/**
 * Created by zhang on 2017/6/20.
 */
@Controller
public class ExportController {

    /**
     * 报表导出PDF
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportPdf.do", method = {RequestMethod.GET, RequestMethod.POST})
    public void exportPdf(HttpServletRequest request, HttpServletResponse response) {

        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        String code = request.getParameter("code");
//        StringBuffer html = new StringBuffer();
//        html.append("<html>")
//                .append("<head>")
//                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />")
//                .append("<style type=\"text/css\">*{font-family:KaiTi_GB2312;}</style>")
//                .append("</head>")
//                .append("<body>")
//                .append("<font face=\"KaiTi_GB2312\">")
//                .append("<font color='red' size=22>hello world 显示中文</font>")
//                .append("</font>")
//                .append("</body></html>");
//        String html = html(code ,basePath);
//        StringReader strReader = new StringReader(html);

        try {
            PD4ML pd4ml = new PD4ML();
            pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
            pd4ml.setHtmlWidth(1300);
            pd4ml.setPageSize(PD4Constants.A4);
            pd4ml.useTTF("java:", true);
            pd4ml.setDefaultTTFs("KaiTi_GB2312", "KaiTi_GB2312", "KaiTi_GB2312");
            response.setContentType("application/pdf; charset=utf-8");
            response.setHeader("Content-disposition", "attachment; filename=report.pdf");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            pd4ml.render(strReader, baos);
            pd4ml.render(new URL("http://localhost:8080/test.htm"), baos);
            byte[] result = baos.toByteArray();
            response.setContentLength(result.length);
            ServletOutputStream sos = response.getOutputStream();
            sos.write(result);
            if (sos != null) {
                sos.close();
            }
        } catch (Exception e) {
        }
    }

    private String html(String code,String basePath) {
        System.out.println("     <link rel=\"stylesheet\"  href=\"" + basePath + "static/dumai/loanMiddle/css/model.css\"> ");
//        System.out.println("     <script type=\"text/javascript\" src=\"" + basePath + "static/script/lib/jquery.min.js\"></script> ");
//        System.out.println("     <script type=\"text/javascript\" src=\"" + basePath + "static/script/lib/less.min.js\"></script> ");
        StringBuffer html = new StringBuffer();
        html.append("<!DOCTYPE html>")
                .append(" <html>")
                .append(" <head> ")
                .append("     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />")
                .append("     <style type=\"text/css\">*{font-family:KaiTi_GB2312;}</style> ")
                .append("     <link rel=\"stylesheet\" href=\"" + basePath + "static/dumai/loanMiddle/css/model.css\"> ")
                .append(" </head> ")
                .append(" <body> ")
                .append(" <div class=\"markReport\"> ")
                .append("     <div class=\"baseReport\" id=\"baseReport\"> ")
                .append("         <ul class=\"base_header\"> ")
                .append("             <li><img src=\"" + basePath + "static/images/dumai_logo.png\" alt=\"logo\"></li> ")
                .append("             <ul class=\"base_right\"> ")
                .append("                 <li><span>编号：</span><span ng-bind=\"loanReportFront.DeviceNumber\"></span></li> ")
                .append("                 <li><span>日期：</span><span ng-bind=\"dateTime\">2017-04-01</span></li> ")
//                .append("                 <li><a id=\"btn\" ng-click=\"exportImg()\">导出</a><span ng-click=\"closeReport()\" class=\"report_close spriteIcon\"></span></li> ")
                .append("             </ul> ")
                .append("         </ul> ")
                .append("         <div class=\"base_title\"><p>读脉风控评审报告</p> <span></span></div> ")
                .append("         <ul class=\"base_list\"> ")
                .append("              <div class=\"soloInfo\"> ")
                .append("                 <p class=\"solo_title\"><span class=\"spriteIcon icon_baseinfo\"></span>基本信息</p> ")
                .append("                  <div class=\"basicInfo\"> ")
                .append("                     <ul> ")
                .append("                        <li><span>姓名：</span><span>张三</span></li> ")
                .append("                        <li><span>性别：</span><span>男</span></li> ")
                .append("                        <li><span>年龄：</span><span>18</span></li> ")
                .append("                        <li><span>婚姻（查询）：</span><span>已婚</span></li> ")
                .append("                        <li><span>学历：</span><span>高中</span></li> ")
                .append("                        <li><span>身份证号码：</span><span>123456789123456789</span></li> ")
                .append("                        <li><span>手机号码：</span><span>男</span></li> ")
                .append("                        <li><span>银行卡：</span><span>男</span></li> ")
                .append("                        <li><span>职业情况 ：</span><span>男</span></li> ")
                .append("                        <li><span>常住地址：</span><span>男</span></li> ")
                .append("                        <li><span>固定收入 ：</span><span>男</span></li> ")
                .append("                        <li class=\"emeLink\"><span>紧急联系人1：</span><span>王小二</span>123456<span></span><span ng-bind=\"loanReportFront.linkReation1\"></span></li> ")
                .append("                        <li class=\"emeLink\"><span>紧急联系人1：</span><span>李二黑</span>456123<span></span><span ng-bind=\"loanReportFront.linkReation1\"></span></li> ")
                .append("                     </ul> ")
//                .append("                     <img src=\"" + basePath + "static/images/userMO.png\" > ")
                .append("                  </div> ")
                .append("              </div> ")
                .append("         </ul> ")
                .append("     </div> ")
                .append(" </div> ")
                .append(" </body> ")
                .append(" </html> ");

        System.out.println(html.toString());

        return html.toString();
    }
}

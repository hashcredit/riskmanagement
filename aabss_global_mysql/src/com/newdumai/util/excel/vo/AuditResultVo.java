package com.newdumai.util.excel.vo;

import com.newdumai.util.excel.ExcelField;

import java.util.List;
import java.util.Map;

/**
 * 导出电核结果vo
 *
 * Created by zhang on 2017/3/28.
 */
public class AuditResultVo {

    private String customer_name;
    private String mobile;
    private String card_num;
    private String bank_num;
    private String other_exception;
    private String description;

    private List<Map<String,Object>> ruleList ;

    @ExcelField(title = "姓名", align = 2, type = 1, sort = 10)
    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    @ExcelField(title = "手机号", align = 2, type = 1, sort = 20)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @ExcelField(title = "身份证号码", align = 2, type = 1, sort = 30)
    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    @ExcelField(title = "银行卡号", align = 2, type = 1, sort = 40)
    public String getBank_num() {
        return bank_num;
    }

    public void setBank_num(String bank_num) {
        this.bank_num = bank_num;
    }

    @ExcelField(title = "电核时发现异常", align = 2, type = 1, sort = 60)
    public String getOther_exception() {
        return other_exception;
    }

    public void setOther_exception(String other_exception) {
        this.other_exception = other_exception;
    }

    @ExcelField(title = "电核详情", align = 2, type = 1, sort = 50)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Map<String, Object>> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Map<String, Object>> ruleList) {
        this.ruleList = ruleList;
    }
}

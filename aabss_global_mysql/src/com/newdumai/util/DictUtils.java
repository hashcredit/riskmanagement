package com.newdumai.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.newdumai.global.dao.Dumai_newBaseDao;
import com.newdumai.sysmgr.DictService;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 字典工具类
 */
public class DictUtils {

    public static String getDictLabel(String value, String type, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            for (Map<String, String> dict : getDictList(type)) {
                if (type.equals(dict.get("type")) && value.equals(dict.get("value"))) {
                    return (String) dict.get("label");
                }
            }
        }
        return defaultValue;
    }

    public static String getDictLabels(String values, String type, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)) {
            List<String> valueList = Lists.newArrayList();
            for (String value : StringUtils.split(values, ",")) {
                valueList.add(getDictLabel(value, type, defaultValue));
            }
            return StringUtils.join(valueList, ",");
        }
        return defaultValue;
    }

    public static String getDictValue(String label, String type, String defaultLabel) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
            for (Map<String, String> dict : getDictList(type)) {
                if (type.equals(dict.get("type")) && label.equals(dict.get("label"))) {
                    return dict.get("value");
                }
            }
        }
        return defaultLabel;
    }

    public static List<Map<String, String>> getDictList(String type) {
        List<Map<String, String>> dictList = new ArrayList<Map<String, String>>();
        List<Map<String, Object>> dict = SpringApplicationContextHolder.getBean(DictService.class).findAllList(type);
        for (Map<String, Object> map : dict) {
            dictList.add(MapObjUtil.mapObjToString(map));
        }
        return dictList;
    }

    /**
     * 返回字典列表（JSON）
     *
     * @param type
     * @return
     */
    public static String getDictListJson(String type) {
        return new Gson().toJson(getDictList(type));
    }

}

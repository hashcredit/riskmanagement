package com.newdumai.ht.manager.rule.rule;

import com.newdumai.global.service.BaseService;

import java.util.Map;

/**
 * Created by zhang on 2017/4/26.
 */
public interface CustomModelService extends BaseService {

    /**
     * 获取商户业务类型对应的模型
     *
     * @param map
     * @return
     */
    String getModels(Map<String, Object> map);

    /**
     * 前台-贷前-模型-启用模型
     *
     * @return
     */
    int enableModel(String sys_company_type_code, String manager_model_code);

    /**
     * 前台-贷前-模型-停用模型
     *
     * @return
     */
    int disableModel(String sys_company_type_code, String manager_model_code);
}

package com.newdumai.dumai_data.dm_label;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.Dumai_sourceBaseService;

public interface DmLabelService extends Dumai_sourceBaseService {

	/**
	 * 根据code查询已关联的第三方数据源
	 * 
	 * @param code
	 * @return
	 * @zgl Jan 12, 2017 4:01:22 PM
	 */
	String getThirdSource(String code);

	/**
	 * 根据code查询未关联的第三方数据源
	 * 
	 * @param code
	 * @return
	 * @zgl Jan 12, 2017 2:41:25 PM
	 */
	String getUnRelateThirdSource(String code);

	/**
	 * 根据code查询表的信息
	 * 
	 * @param tablename
	 * @param code
	 * @return
	 * @zgl Jan 12, 2017 3:20:21 PM
	 */
	Map<String, Object> getBycode(String tablename, String code);

	/**
	 * 删除画像与第三方数据源的关系
	 * 
	 * @param dm_label_code
	 * @param dm_3rd_interface_code
	 * @return
	 * @zgl Jan 12, 2017 4:13:50 PM
	 */
	void delRelation(String dm_label_code, String dm_3rd_interface_code);

	/**
	 * 为用户画像添加第三方数据源
	 * 
	 * @param userImgCode
	 * @param sourceCodes
	 * @return
	 * @zgl Jan 12, 2017 5:10:52 PM
	 */
	boolean addSources(String userImgCode, String[] sourceCodes);

	/**
	 * 根据第三方数据源code查询参数
	 * 
	 * @param dm_3rd_interface_code
	 * @param flag
	 *            :0 输入，1 输出
	 * @return
	 * @zgl Jan 12, 2017 6:17:39 PM
	 */
	List<Map<String, Object>> getSourcePara(String dm_3rd_interface_code, String flag);

	/**
	 * 更新<用户标签项目与数据源关联关系表>的第三方数据源参数code
	 * 
	 * @param dmLabelCode
	 * @param sourceCode
	 * @param dm3rdParaCode
	 * @return
	 * @zgl Jan 13, 2017 9:54:26 AM
	 */
	Integer updateSourcePara(String dmLabelCode,String sourceCode, String dm3rdParaCode);

	/**
	 * 根据标签表code和第三方数据源表code查询关联关系信息
	 * 
	 * @param dm_label_code
	 * @param dm_3rd_interface_code
	 * @return
	 * @zgl Jan 13, 2017 10:49:24 AM
	 */
	Map<String, Object> getImg3rdRelationInfo(String dm_label_code, String dm_3rd_interface_code);

	/**
	 * 根据画像项目code查询第三方数据源输入参数
	 * 
	 * @param dm_label_code
	 * @return
	 * @zgl Jan 13, 2017 3:08:48 PM
	 */
	String toTestPage(String dm_label_code);

	/**
	 * 设置结果转换条件json
	 * 
	 * @param request2Map
	 * @return
	 * @zgl Jan 13, 2017 4:56:49 PM
	 */
	void updateOutInterface(Map<String, Object> request2Map);

	/**
	 * 根据身份证查询<用户画像表>信息
	 *
	 * @param idCard
	 * @zgl Jan 16, 2017 10:26:36 AM
	 */
	Map<String, Object> getDetailByIdCard(String idCard);

	/**
	 * 根据code和列进行更新
	 *
	 * @param code
	 * @param column
	 * @param detailMapStr
	 * @zgl Jan 16, 2017 10:43:09 AM
	 */
	void updateByCode(String code, String column, String detailMapStr);

	String testImg(Map<String, Object> params);

	/**
	 * 获取分组名（下拉列表）
	 *
	 * @return
	 */
	List<Map<String, Object>> getGroupName();

	/**
	 * 根据dm_label_code查询dm_label表和dm_label_group表信息
	 *
	 * @param dm_label_code
	 * @return
	 */
	String findByCode(String dm_label_code);

	List<Map<String, Object>> findByGroupCode(String dm_label_group_code);

	Integer updateInPara(String code, String inPara);

	Map<String, Object> getBigLabelMap(String dm_label_detail_code);
	
	/**
	 * bank_num,card_num,mobile
	 * @param params
	 * @return
	 */
	Map<String, Object> getBigLabelMap(String card_num,String bank_num,String mobile);

	Map<String, Object> getListWithGroup(Map<String, Object> params);
	
}

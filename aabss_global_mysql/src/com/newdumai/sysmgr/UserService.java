package com.newdumai.sysmgr;

import java.util.List;
import java.util.Map;

import com.newdumai.global.vo.Page;
import com.newdumai.global.vo.PageConfig;

public interface UserService {
    public Page<Map<String, Object>> findAsPage(PageConfig config, String sub_entity_id);

    public boolean del(String parameter);

    boolean addUser(Map<String, Object> user);

    public boolean updateUser(Map<String, Object> user);

    /**
     * 修改密码
     *
     * @param code
     * @param pwd
     * @param flag 0 仅修改密码 ，1 修改密码，同时修改用户为老用户
     * @return
     */
    public boolean updatePwd(String code, String pwd, String flag);

    public boolean existsUsername(String uesername);


    /**
     * 根据类型查询所有用户(且不包含电核管理员)（已用getSubUsers替换）
     *
     * @param type
     * @return
     */
    @Deprecated
    List<Map<String, Object>> getAllUserByType(String type);

    /**
     * 根据sub_entity_id查询电核员
     *
     * @param type : 4 电核管理员，5 电核员
     * @return
     */
    List<Map<String, Object>> getSubUsers(String type, String sub_entity_id);
}

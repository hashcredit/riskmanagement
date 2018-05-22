package com.newdumai.sysmgr.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.vo.Page;
import com.newdumai.global.vo.PageConfig;
import com.newdumai.sysmgr.UserService;


@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Override
    public Page<Map<String, Object>> findAsPage(PageConfig config, String sub_entity_id) {
        String sqlTotal = "select count(*) from sys_user where sub_entity_id=?";
        String sql = "select * from sys_user where sub_entity_id=?";
        return findAsPage(config, sqlTotal, sql, sub_entity_id);
    }


    @Override
    public boolean del(String userId) {
        try {
            mysqlSpringJdbcBaseDao.delete("delete from sys_user where code=? ", userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    @Override
    public boolean addUser(Map<String, Object> user) {
        try {
            add(user, "sys_user");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(Map<String, Object> user) {
        try {

            Map<String, Object> where = new HashMap<String, Object>();
            Map<String, Object> colums = new HashMap<String, Object>();
            where.put("code", user.get("code"));

            colums.putAll(user);
            colums.remove("code");
            Update(user, "sys_user", where);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePwd(String code, String pwd, String flag) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> where = new HashMap<String, Object>();
        where.put("code", code);
        params.put("pwd", pwd);
        if ("1".equals(flag)) {
            params.put("status", "1");
        }
        int i = Update(params, "sys_user", where);
        if (i == 0) {
            return false;
        }
        return true;
    }

    protected Page<Map<String, Object>> findAsPage(PageConfig config, String sqlTotal, String sql, Object... args) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        page.setRows(mysqlSpringJdbcBaseDao.queryForList(sql + " limit " + (config.getCurrPage() - 1) * config.getPageSize() + "," + config.getPageSize(), args));
        page.setTotal(mysqlSpringJdbcBaseDao.executeSelectSqlInt(sqlTotal, args));
        return page;
    }


    @Override
    public boolean existsUsername(String uesername) {
        List<Map<String, Object>> users = mysqlSpringJdbcBaseDao.queryForList("select code from sys_user where user_name=?", uesername);
        return users.size() > 0;
    }

    @Override
    public List<Map<String, Object>> getAllUserByType(String type) {
        List<Map<String, Object>> users = mysqlSpringJdbcBaseDao.queryForList("select code,user_name,surname from sys_user where isleader=? and id != 49", type);
        return users;
    }

    @Override
    public List<Map<String, Object>> getSubUsers(String type, String sub_entity_id) {
    	List<Map<String, Object>> users ;
    	if(sub_entity_id==null){
    		users= mysqlSpringJdbcBaseDao.queryForList("select code,user_name,surname from sys_user where isleader=?", type);
    	}else{
    		users = mysqlSpringJdbcBaseDao.queryForList("select code,user_name,surname from sys_user where isleader=? and sub_entity_id=?", type, sub_entity_id);
    	}
        return users;
    }
}

package com.newdumai.web.sysmgr;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newdumai.global.vo.JsonResult;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.global.vo.Page;
import com.newdumai.sysmgr.UserService;
import com.newdumai.web.util.WebUtil;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/sysmgr/toUserList.do")
    public String toUserList(HttpServletRequest request) {
        return "sysmgr/user/userList";
    }

    @RequestMapping("/sysmgr/userList.do")
    @ResponseBody
    public JsonResult userList(HttpServletRequest request) {
        LoginVo login = (LoginVo) WebUtil.getSession(request).getAttribute("login");
        Page<Map<String, Object>> page = userService.findAsPage(WebUtil.getPageConfig(request), login.getSub_entity_id());
        JsonResult result = JsonResult.successResult(page);
        return result;
    }

    @RequestMapping("/sysmgr/userDel.do")
    @ResponseBody
    public JsonResult userDel(HttpServletRequest request) {
        boolean success = userService.del(request.getParameter("user_id"));
        JsonResult result = JsonResult.successResult(success);
        return result;
    }


    @RequestMapping("/sysmgr/usernameNotDuplicated.do")
    @ResponseBody
    public boolean checkUserExists(HttpServletRequest request) {
        Map<String, Object> user = WebUtil.request2Map(request);
        return !userService.existsUsername((String) user.get("user_name"));
    }

    @RequestMapping("/sysmgr/userAdd.do")
    @ResponseBody
    public JsonResult userAdd(HttpServletRequest request) {
        LoginVo login = (LoginVo) WebUtil.getSession(request).getAttribute("login");
        Map<String, Object> user = WebUtil.request2Map(request);
        user.put("pwd", "123456");
        user.put("sub_entity_id", login.getSub_entity_id());
        boolean success = userService.addUser(user);
        JsonResult result = JsonResult.successResult(success);
        return result;
    }

    @RequestMapping("/sysmgr/userUpdate.do")
    @ResponseBody
    public JsonResult userUpdate(HttpServletRequest request) {
        Map<String, Object> user = WebUtil.request2Map(request);
        user.put("moddate", new Date());
        boolean success = userService.updateUser(user);
        JsonResult result = JsonResult.successResult(success);
        return result;
    }

    @RequestMapping("/sysmgr/toUpateUserPwd.do")
    public String toUpateUserPwd(HttpServletRequest request) {
        return "sysmgr/user/userUpdatePwd";
    }
    
    @RequestMapping("/sysmgr/toUpatePwd.do")
    public String toUpatePwd(HttpServletRequest request) {
        return "sysmgr/user/updatePwd";
    }

    @RequestMapping("/sysmgr/upateUserPwd.do")
    @ResponseBody
    public Object userUpatePwd(HttpServletRequest request) {
        LoginVo login = (LoginVo) WebUtil.getSession(request).getAttribute("login");
        String pwd = request.getParameter("pwd");
        String code = login.getCode();
        try {
            boolean success = userService.updatePwd(code, pwd, "0");
            JsonResult result = JsonResult.successResult(success);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failResult("密码修改错误");
        }
    }

    /**
     * 新用户登录强制修改密码
     *
     * @param request
     * @return
     */
    @RequestMapping("/sysmgr/upatePwd.do")
    public String upatePwd(HttpServletRequest request) {
        LoginVo login = (LoginVo) WebUtil.getSession(request).getAttribute("login");
        String pwd = request.getParameter("pwd");
        String code = login.getCode();
        boolean success = userService.updatePwd(code, pwd, "1");
        if (success) {
            return "redirect:/sysmgr/funsettings/toPage.do";
        } else {
            request.setAttribute("error", "由于网络原因，修改密码失败！");
            return "sysmgr/user/userUpdatePwd";
        }
    }

}

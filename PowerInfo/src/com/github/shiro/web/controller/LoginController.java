package com.github.shiro.web.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.shiro.entity.User;
import com.github.shiro.service.UserService;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
@Controller
public class LoginController {
	@Autowired
	private UserService  userService ;
	
    @RequestMapping(value = "/login")
    public String showLoginForm(HttpServletRequest req, Model model) {
//        String exceptionClassName = (String)req.getAttribute("shiroLoginFailure");
//        String error = null;
        String msg = "";  
        String userName = req.getParameter("username");  
        String password = req.getParameter("password");  
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);  
        token.setRememberMe(true);  
        Subject subject = SecurityUtils.getSubject();  
        try {  
            subject.login(token);  
            if (subject.isAuthenticated()) {
            	User user =userService.findByUsername(userName);
            	
            	req.getSession().removeAttribute("user");			
    			req.getSession().setAttribute("user", user);
                return "redirect:/mondelSel/sel";  
            } else {  
                return "login";  
            } 
        }catch (IncorrectCredentialsException e) {  
            msg = "登录密码错误. ";  
            model.addAttribute("message", msg);  
        } catch (ExcessiveAttemptsException e) {  
            msg = "登录失败次数过多";  
            model.addAttribute("message", msg);  
        } catch (LockedAccountException e) {  
            msg = "帐号已被锁定.";  
            model.addAttribute("message", msg);  
        } catch (DisabledAccountException e) {  
            msg = "帐号已被禁用. ";  
            model.addAttribute("message", msg);  
        } catch (ExpiredCredentialsException e) {  
            msg = "帐号已过期. ";  
            model.addAttribute("message", msg);  
        } catch (UnknownAccountException e) {  
            msg = "帐号不存在.";  
            model.addAttribute("message", msg);  
        } catch (UnauthorizedException e) {  
            msg = "您没有得到相应的授权！" ;  
            model.addAttribute("message", msg);  
        }  
        return "login";
    }
    @RequestMapping(value = "/loginIndex")
    public String loginIndex(HttpServletRequest req, Model model) {

        return "login";
    }
	@RequestMapping(value = "/getRoleByUserName",produces="application/json;charset=UTF-8")
	public @ResponseBody String getRoleByUserName(HttpServletRequest request) {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			String username=currentUser.getPrincipal().toString();
			//String username = request.getParameter("username");
			JSONObject returnobj=new JSONObject();
			long role=userService.findByUsername(username).getRoleIds().get(0);
			returnobj.put("role", role);
			return returnobj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

}

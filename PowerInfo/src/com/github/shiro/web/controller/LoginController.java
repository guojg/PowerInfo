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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
@Controller
public class LoginController {

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
                return "redirect:/mondelSel/sel";  
            } else {  
                return "login";  
            } 
        }catch (IncorrectCredentialsException e) {  
            msg = "登录密码错误. Password for account " + token.getPrincipal() + " was incorrect.";  
            model.addAttribute("message", msg);  
        } catch (ExcessiveAttemptsException e) {  
            msg = "登录失败次数过多";  
            model.addAttribute("message", msg);  
        } catch (LockedAccountException e) {  
            msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";  
            model.addAttribute("message", msg);  
        } catch (DisabledAccountException e) {  
            msg = "帐号已被禁用. The account for username " + token.getPrincipal() + " was disabled.";  
            model.addAttribute("message", msg);  
        } catch (ExpiredCredentialsException e) {  
            msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";  
            model.addAttribute("message", msg);  
        } catch (UnknownAccountException e) {  
            msg = "帐号不存在. There is no user with username of " + token.getPrincipal();  
            model.addAttribute("message", msg);  
        } catch (UnauthorizedException e) {  
            msg = "您没有得到相应的授权！" + e.getMessage();  
            model.addAttribute("message", msg);  
        }  
        return "login";
    }
    @RequestMapping(value = "/loginIndex")
    public String loginIndex(HttpServletRequest req, Model model) {

        return "login";
    }

}

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

import javax.servlet.http.HttpServletRequest;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController {

	@Autowired
	private UserService  userService ;
    @RequestMapping(value = "/index")
    public  String registerForm(HttpServletRequest req, Model model) {
//        String exceptionClassName = (String)req.getAttribute("shiroLoginFailure");
//        String error = null;
    	 try {
    		 req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("usernamesignup");  
         String password = req.getParameter("passwordsignup"); 
         String rolename = req.getParameter("rolename"); 
         User user = new User();
         user.setUsername(username);
         user.setPassword(password);
         user.setLocked(false);
         user.setOrganizationId(1L);
         user.setRoleIdsStr(rolename);
         userService.createUser(user);
     	//return "1";
         req.setAttribute("flag", "1");
         return "login";

 		} catch (Exception e) {
 			e.printStackTrace();
 		//	return "0";
 			req.setAttribute("flag", "0");
 	         return "login";

 		}
    	 
    	
    }
    
    @RequestMapping(value = "/modifyPass")
    public @ResponseBody String modifyPass(HttpServletRequest req, Model model) {
//        String exceptionClassName = (String)req.getAttribute("shiroLoginFailure");
//        String error = null;
    	 try {
         String password = req.getParameter("password"); 
       User session = (User) req.getSession().getAttribute("user");	
        if(session != null){
	     
         userService.changePassword(session.getId(),password);
	         return "1" ;
     	}else{
     		return "0";
     	}

 		} catch (Exception e) {
 			e.printStackTrace();
 		return "0";

 		}
 
    }
   
    @RequestMapping(value = "/validateOnly")
    public @ResponseBody String validateOnly(HttpServletRequest req, Model model) {

    	 try {
         String username = req.getParameter("username"); 
      
         User user = userService.findByUsername(username);
         if(user ==null)
	         return "2" ;
     	
 		} catch (Exception e) {
 			e.printStackTrace();

 		}	
    	 return "1" ;
   }


}

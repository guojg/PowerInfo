package com.github.shiro.web.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;





import com.github.shiro.entity.User;
import com.github.shiro.web.bind.annotation.CurrentUser;

import java.util.List;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-14
 * <p>Version: 1.0
 */
@Controller
public class IndexController {


	 @RequestMapping("/")
	    public String index(@CurrentUser User loginUser, Model model) {
		 
	        return "index2";
	    }
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }


}

package com.github.menu.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.common.tree.NodeMsg;


@Repository
public class MenuDaoImpl implements MenuDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String, Object>> queryAccordion(List<String> years) {
		StringBuffer sb = new StringBuffer();
		sb.append( "SELECT id menuid ,p_id icon,NAME menuname FROM sys_menu WHERE p_id='basic'") ;

	        return jdbcTemplate.queryForList(sb.toString());
	}
	@Override
	public List<Map<String, Object>> queryMenu(List<String> years) {

		StringBuffer sb = new StringBuffer();
		 this.jdbcTemplate.execute("call pro_show_childLst(1)"); 
			sb.append( "      SELECT sys_menu.id,sys_menu.name text,sys_menu.p_id parent_id,sys_menu.url FROM tmpLst,shiro.sys_menu WHERE tmpLst.id=shiro.sys_menu.id ORDER BY tmpLst.sno") ;

	        return jdbcTemplate.queryForList(sb.toString());
	}

}

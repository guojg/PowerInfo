package com.github.menu.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.common.tree.NodeMsg;


@Repository
public class MenuDaoImpl implements MenuDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String, Object>> queryAccordion(JSONObject param) {
		String p_id= param.getString("param");
		StringBuffer sb = new StringBuffer();
		sb.append( "SELECT id menuid ,p_id icon,NAME menuname FROM sys_menu where p_id =?") ;

	        return jdbcTemplate.queryForList(sb.toString(),new Object[]{p_id});
	}
	@Override
	public List<Map<String, Object>> queryMenu(JSONObject param) {
		final int jsonParam = param.getInt("jsonParam");

		StringBuffer sb = new StringBuffer();
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_show_childLst(?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, jsonParam);// 设置输入参数的值   
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

  
		          } 
		       ); 

			sb.append( "      SELECT sys_menu.id,sys_menu.name text,sys_menu.p_id parent_id,sys_menu.unit_code,sys_menu.url,sys_menu.table_name tablename FROM tmpLst,shiro.sys_menu WHERE tmpLst.id=shiro.sys_menu.id ORDER BY tmpLst.sno") ;

	        return jdbcTemplate.queryForList(sb.toString());
	}

}

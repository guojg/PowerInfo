package com.github.totalquantity.sysdict.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;

import com.github.totalquantity.sysdict.entity.Sysdict;

@Repository
public class SysdictDaoImpl implements SysdictDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Sysdict> queryData(JSONObject obj) {
		String domain_id = obj.getString("domain_id");
		String sql ="select code ,value from sys_dict_table where domain_id=?";
		List<Sysdict> list = this.jdbcTemplate.query(sql, new Object[]{domain_id}, new ParameterizedRowMapper<Sysdict>() {
            @Override
            public Sysdict mapRow(ResultSet rs, int index)
                    throws SQLException {
            	Sysdict sd = new Sysdict();
            	sd.setCode(rs.getString("code"));
            	sd.setValue(rs.getString("value"));
                return sd;
            }
        });
		return list;
	}

	@Override
	public List<Map<String, Object>> queryDataByMap(JSONObject obj) {
		String domain_id = obj.getString("domain_id");
		String sql ="select code ,value from sys_dict_table where domain_id=?";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql, new Object[]{domain_id});
		return list;
	}


}

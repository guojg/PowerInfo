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
		String sql ="select code ,value from sys_dict_table where domain_id=? ORDER BY ORD";
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
		String condition = obj.getString("condition");
		String sql ="select code ,value from sys_dict_table where domain_id=?" ;
				if(!"".equals(condition)){
					sql +=" and  code not in ("+condition+") ";
				}
				sql += " ORDER BY ORD";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql, new Object[]{domain_id});
		return list;
	}

	@Override
	public List<Sysdict> queryDataNotCondition(JSONObject obj) {
		String domain_id = obj.getString("domain_id");
		String condition = "and code not in ("+obj.getString("condition")+")";
		String sql ="select code ,value from sys_dict_table where domain_id=? "+condition+" ORDER BY ORD";
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
	public List<Map<String, Object>> getBalanceYearExtend(){
		String sql ="SELECT start_year,stop_year FROM balance_param_year ";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql);

		return list;
	}

	@Override
	public List<Map<String, Object>> queryCompany(JSONObject obj) {
		String sql ="select code ,value from bn_code_company  ORDER BY ORD";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql);

		return list;
	}

	@Override
	public List<Map<String, Object>> queryCompanyByCode(String organCode) {
		String sql ="select code ,value from bn_code_company where code=?  ORDER BY ORD";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql,new Object[]{organCode});

		return list;
	}


}

package com.github.totalquantity.basedata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;

import com.github.totalquantity.basedata.entity.QuoteBase;
import com.github.totalquantity.prepareData.entity.PrepareData;

@Repository
public class BaseDaoImpl implements BaseDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<QuoteBase> queryBaseData(JSONObject param) {
        String indexs=param.get("indexs")==null?null:param.get("indexs").toString();
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String sql ="SELECT tb.index_item,tb.yr,tb.value from powersupply_data tb where tb.yr=? ";
		sb.append(sql);
		String year = param.getString("year") ;
		List<String> params = new ArrayList<String>();
		params.add(year)  ;
		if(!"".equals(indexs) && indexs!=null){
			sb.append(" and   instr(?,','||tb.index_item||',')>0") ;
			params.add(indexs) ;
		}
		List<QuoteBase> list = this.jdbcTemplate.query(sql, params.toArray(), new ParameterizedRowMapper<QuoteBase>() {
            @Override
            public QuoteBase mapRow(ResultSet rs, int index)
                    throws SQLException {
            	QuoteBase qb = new QuoteBase();
            	qb.setIndexItem(rs.getString("index_item"));
            	qb.setYear(rs.getString("yr"));
            	qb.setValue(rs.getDouble("value"));
                return qb;
            }
        });
		return list;
	}
	
}

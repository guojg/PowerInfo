package com.github.regionalanalysis.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TotalDataAnalysisDaoImpl implements TotalDataAnalysisDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void totalData(final Long  fdj_id,final Integer  area_id) throws Exception {
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_total_cost(?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setLong(1, fdj_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

  
		          } 
		       ); 
	}
	
	@Override
	public void totalDataAnalysis(final Long fdj_id, final Integer area_id,final Long task_id) throws Exception {
		// TODO Auto-generated method stub
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_total_cost_fx(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setLong(1, fdj_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

 
		          } 
		       ); 
	}

	@Override
	public void totalDatadCompare(final Long fdj_id,final Integer area_id,final Long task_id) throws Exception {
		// TODO Auto-generated method stub
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_total_cost_db(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setLong(1, fdj_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);

		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

 
		          } 
		       ); 
	}

	@Override
	public void totalDataPlant(final Integer dc_id,final Integer area_id) throws Exception {
		// TODO Auto-generated method stub
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_total_cost_fdc(?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setLong(1, dc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

 
		          } 
		       ); 
	}

	@Override
	public void totalDataPlantAnalysis(final Integer dc_id,final Integer area_id,final Long task_id) throws Exception {
		// TODO Auto-generated method stub
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_total_cost_fdc_fx(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setLong(1, dc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

 
		          } 
		       ); 
	}

	@Override
	public void totalDataPlantCompare(final Integer dc_id,final Integer area_id,final Long task_id) throws Exception {
		// TODO Auto-generated method stub
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_total_cost_fdc_db(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setLong(1, dc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }

 
		          } 
		       ); 
	}

	@Override
	public void HalfFdjAssigned(final Integer dc_id,final  Integer area_id) throws Exception {
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call half_fdj_assigned(?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, dc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }


		          } 
		       ); 
		
	}

	@Override
	public void HalfFdjAssignedFx(final Integer dc_id,final  Integer area_id,final Long task_id) throws Exception {
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call half_fdj_assigned_fx(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, dc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }


		          } 
		       ); 
		
	}

	@Override
	public void HalfFdjAssignedDb(final Integer dc_id,final  Integer area_id,final  Long task_id) throws Exception {
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call half_fdj_assigned_db(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, dc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }


		          } 
		       ); 
	}

	@Override
	public void fdcSaveTotal(final Integer fdc_id,final  Integer area_id) throws Exception {
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_fdc_total_save(?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, fdc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }


		          } 
		       ); 
		
	}

	@Override
	public void fdcSaveTotalFx(final Integer fdc_id,final  Integer area_id,final Long task_id) throws Exception {
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_fdc_total_save_fx(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, fdc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }


		          } 
		       ); 
		
	}

	@Override
	public void fdcSaveTotalDb(final Integer fdc_id,final  Integer area_id, final  Long task_id) throws Exception {
		 //this.jdbcTemplate.execute("call pro_show_childLst(?)"); 
		 this.jdbcTemplate.execute(   
		         new CallableStatementCreator() {   
		             public CallableStatement createCallableStatement(Connection con) throws SQLException {   
		                String storedProc = "{call pro_fdc_total_save_fx_db(?,?,?)}";// 调用的sql   
		                CallableStatement cs = con.prepareCall(storedProc);   
		                cs.setInt(1, fdc_id);// 设置输入参数的值   
		                cs.setInt(2, area_id);
		                cs.setLong(3, task_id);
		                return cs;   
		             }
		             }, new CallableStatementCallback() {   
		                 public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		                     cs.execute();   
		                     return null;// 获取输出参数的值   
		               }


		          } 
		       ); 
		
	}

}

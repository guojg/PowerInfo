<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>
<html>
<head>
<title>基础数据库填报</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<% 
TotalTask tt=  (TotalTask)request.getSession().getAttribute("totaltask");
String algorithm = tt.getAlgorithm() ;
String taskid = tt.getId();
String planyear = tt.getPlanyear();
%>

<script type="text/javascript">
var taskid='<%=taskid%>';  //任务号
var planyear='<%=planyear%>';//规划年
//id,taskid,algorithm,year,value from totaldata
	var cols;
	var frozenCols = [ [ {
		field : 'algorithm_name',
		title : '算法',
		width : 100,
		align : 'center'
	} ] ];
	$(function() {
		
		var itemJson =[{    
		    "ID":planyear,    
		    "TEXT":planyear+"年"   
		}];
		$('#years').combobox({   
		    data:itemJson,   
		    valueField:'ID',   
		    textField:'TEXT',
		    multiple:false
		});
		$('#years').combobox('setValue', planyear);
		queryData();
	});

	//查询方法调用的函数
	function queryData() {
		var years='';
		//非冰冻列
		cols = createCols(years);
		//查询条件暂时放外面
		//var queryParams = {};
		var queryParams = {"taskid":taskid,"planyear":planyear,"index_type":"1,2,3,4,5,6"};

		var url = path + '/totalData/queryData';
		var Height_Page = $("html").height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height - 5;
		$('#datagrid').datagrid({
			width : 'auto',
			height : height,
			autoRowHeight : false,
			collapsible : true,
			url : url,
			remoteSort : false,
			frozenColumns : frozenCols,
			columns : cols,
			rownumbers : true,
			pagination : false,
			queryParams : queryParams
		});
	}

	//动态生成列
	function createCols(years) {
		var cols = [];
		var tmp = [];
		var years = planyear;
		tmp = years.split(",");
		for (var i = 0; i < tmp.length; i++) {
			cols.push({
				'field' : tmp[i] + "",
				'title' : "" + tmp[i] + "年",
				'align' : 'center',
				'width' : 120,
				'editor' : 'text'

			});
		}
		return new Array(cols);
	}

	
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_save"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
			<tr>
				<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
			</tr>
		</table>
	</fieldset>
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>
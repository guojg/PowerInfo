<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>任务</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/sysdict.js"></script>

	<%@include file="../../common/commonDefineBtn.jsp" %>

<script type="text/javascript">
	var frozenCols = [ [ {
		field : 'task_name',
		title : '任务名',
		width : 100,
		align : 'center',
		formatter: function(value,row,index){

    		return '<a href="#" onclick="detail(\''+row.task_name+"\',"+row.baseyear+','+row.id+','+row.planyear+",\'"+row.algorithmradio+'\',\''+row.algorithm+'\')">'+value+'</a> ';
		}
	} ] ];
	
	var cols ='';
	var algorithmJson=getSysDict();

	$(function() {
	
	
		
		 cols = [ [
		          	 {
		          		field : 'baseyear',
		          		title : '基准年',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : 'planyear',
		          		title : '预测年',
		          		width : 100,
		          		align : 'center'
		          	}, {
		          		field : 'algorithm',
		          		title : '算法',
		          		width : 400,
		          		align : 'center',
		          		formatter: function(value,row,index){
		          		  var valueArr= value.split(',');
		          		    var resultShow='';
		          		  for(var i=0;i<valueArr.length;i++){
		          				resultShow +=algorithmJson[valueArr[i]]+",";
		          		  }
		          		  return resultShow.substring(0,resultShow.length-1);
		    			}

		          	}] ];
		$("#tool_xjrw").bind("click", function() {
			xjrw();
		});
		queryData();
	});

	//查询方法调用的函数
	function queryData() {
	
		//查询条件暂时放外面
		var queryParams = {};

		var url = path + '/task/queryData';
		var Height_Page = $(document).height();
		var datagrid_title_height = $("#datagrid_div").position().top;
		var height = Height_Page - datagrid_title_height;
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
			queryParams : queryParams,
			pagination: true
		});
	}
	function xjrw(){
		commonHelper.toAdd({
			title : '增加年份',
			width : 500,
			height : 300,
			url : path + "/task/taskAdd"
		});
	}

	function detail(task_name,baseyear,id,planyear,algorithmradio,algorithm){
		var param={
				"baseyear":baseyear,
				"planyear":planyear,
				"algorithm":algorithm,
				'algorithmRadio':algorithmradio,
				"taskid":id,
				"task_name":task_name
		};
		$.ajax({
			 type : 'POST',
			 url : path+'/task/taskDetail',
			 dataType: 'text',
			 data: param,
			 async:false,
			 success:function(msg){
					//页面跳转
					window.parent.addNav(5);
		 	 }
		});
	
	}
	
	
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_xjrw"> <img src='<%=path%>/static/images/xjrw.gif'
			align='top' border='0' title='新建任务' />
		</a>
	</div>

	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>

</body>
</html>
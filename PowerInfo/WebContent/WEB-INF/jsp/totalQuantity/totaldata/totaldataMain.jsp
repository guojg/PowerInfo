<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>
<!DOCTYPE html>
<html>
<head>
<title>基础数据库展示</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
	
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp" %>	
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/sysdict.js"></script>
<% 
TotalTask tt=  (TotalTask)request.getSession().getAttribute("totaltask");
String algorithm = tt.getAlgorithm() ;
String taskid = tt.getId();
String planyear = tt.getPlanyear();
String algorithmRadio = tt.getAlgorithmRadio();
%>
<script type="text/javascript">
var taskid='<%=taskid%>';  //任务号
var planyear='<%=planyear%>';//规划年
var algorithmRadio='<%=algorithmRadio%>';
var algorithm='<%=algorithm%>';

var algorithmJson=getSysDict();
$( function() {

	var srcs = {
			'综合展示' : path+'/totalData/showData'
	};
	//加载选项卡页面

	var Height_Page = $(document).height();
	var datagrid_height = $("#datagrid_div").position().top;
	var height= Height_Page-datagrid_height;
	$('#iframe0').attr('src', path+'/totalData/showData');

	var algorithmstr=algorithm;
	if(algorithmRadio!=null && algorithmRadio !=""){
		algorithmstr=algorithm+","+algorithmRadio;
	}
	
	var algorithmArray = algorithmstr.split(",");
	for(var i=0 ; i <algorithmArray.length ;++i){
		if(algorithmArray[i]!="5" && algorithmArray[i]!="6"){
		//addTab(algorithmJson[algorithmArray[i]], path+'/totalData/showData'+algorithmArray[i],i+1);
		//srcs[algorithmJson[algorithmArray[i]]]=path+'/totalData/showData'+algorithmArray[i];
		}else{
			addTab('建议值', path+'/totalData/showData'+algorithmArray[i],1);
			srcs['建议值']=path+'/totalData/showData'+algorithmArray[i];

		}
	}
	$('#tt').tabs({
		fitColumns : true,
		height : height,
		tabHeight:32,
        closable:false ,
		onSelect : function(title, index){
			$('#iframe' + index).attr('src', srcs[title]);
		}
	});

	
});

function addTab(title, url,a){    
  
        var content = '<iframe id=\'iframe'+a+'\' scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:98%;" ></iframe>';    
        $('#tt').tabs('add',{    
            title:title,    
            content:content,    
            closable:false ,
            selected: false

        });    
 
} 
</script>
</head>
<body>
<!-- 选项卡 -->	
<div id="datagrid_div"  class="easyui-layout" style="overflow-y:hidden">
	<div id="tt" class="easyui-tabs" data-options="tools:'#tab-tools'" >
		 <div title="综合展示"  id="p0">
			<iframe id='iframe0' scrolling="auto" frameborder="0"  src="" style="width:100%;height:98%;"></iframe>
		</div>
	
	</div>
</div>
</body>
</html>		
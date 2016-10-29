<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.balance.task.entity.BalanceTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>电力平衡</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<% 
		BalanceTask tt=  (BalanceTask)request.getSession().getAttribute("balancetask");
		String taskid = tt.getId();
		String year = tt.getYear();
		String task_name = tt.getTask_name();
		
		%>
<script type="text/javascript">
var taskid='<%=taskid%>';
var task_name='<%=task_name%>';
var years = '<%=year%>';
var cols;
var savEvtTime = 0;
var dcAt = 0;
var dcTime = 250;
var savTO = null;
var editingId ;
$(function() {
	$('#task_name').html('<b>'+task_name+'</b>');
	$("#tool_extract").bind("click", function() {
		extractData();
	});
	$("#tool_query").bind("click", function() {
		queryData();
	});
	$("#tool_save").bind("click", function() {
		save();
	});
	$("#tool_export").bind("click", function() {
		ExportExcel();
	});
	 comboBoxInit({
			id : "years",
			url : path + '/sysdict/getBalanceYears?year='+years,
			textkey : "value",
			valuekey : "code",
			multiple : true
		});
	 queryData();
});
var noeditId=['500','500-1','500-2','500-3','500-4','500-5','500-6','500-7','500-8','700','800','900','200','300','400','600','1000','100-100','200-1'];
function queryData(){
	var yrs = $('#years').combo('getValues').join(",");

	//非冰冻列
	cols = createCols(yrs);
	var Height_Page = $(document).height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var height = Height_Page - datagrid_title_height;
$('#datagrid').treegrid({    
   url:path+'/powerbalance/queryData',   
   //url:path+'/js/basicData/treegrid_data3.json',
   height : height,
    idField:'id',    
    treeField:'code_name',  
	queryParams : {"year":yrs,"taskid":taskid},
    frozenColumns:[[    
        {title:'id',field:'id',width:180,hidden:true},    
        {field:'parentid',title:'_parentId',width:180,align:'right',hidden:true},  
        {field:'code_name',title:'指标',width:180},  
    ]] ,
	columns : cols,
	onLoadSuccess: function () {$('#datagrid').treegrid('collapseAll')},
	onClickCell : function(field,row) {
		for(var i=0 ; i<noeditId.length ;++i){
			if(row.id==noeditId[i]) return ;
		}
	
		clickEvent(field,row);


	},
	onAfterEdit:function(row ,changes){
		calculateSum();
	}
}); 
}
//动态生成列
function createCols(years) {
	var cols = [];
	var tmp = [];

	tmp = years.split(",");
	for (var i = 0; i < tmp.length; i++) {
		cols.push({
			'field' : tmp[i] + "",
			'title' : "" + tmp[i] + "年",
			'align' : 'center',
			'width' : 120,
			'editor' : 'text',
			//'editor' : 'numberspinner'
			'editor':{
				type:'numberspinner',
				options: {  
                    increment:10,  
                    precision:3
                   
                }  
			}

		});
	}
	return new Array(cols);
}
function ExportExcel() {//导出Excel文件
	var yrs = $('#years').combo('getValues').join(",");

	//用ajax发动到动态页动态写入xls文件中
	var f = $('<form action="'+path+'/powerbalance/exportData" method="post" id="fm1"></form>');  
    var i = $('<input type="hidden" id="year" name="year" />');  
    var m = $('<input type="hidden" id="taskid" name="taskid" />');  
	i.val(yrs);  
	i.appendTo(f);  
	
	m.val(taskid);  
	m.appendTo(f); 
	f.appendTo(document.body).submit();  
	document.body.removeChild(f);  
}
function calculateSum(){
	/*100 最大负荷
	200 需要有效装机容量
	300 投产机组容量
	400 退役关停机组容量
	500 年底装机容量
	600 受阻及空闲容量
	700 当年可用装机容量
	800 外购外送
	900装机盈余   =  当年可用容量 - 需要有效装机容量 +  控制月外购(+)外送(-)  700-200+800
	1000 现有装机容量  
	*/
	calculatezjrl();
	 calculatetcrl();
	 calculatetyrl();
	 calculatexyrl();
	 calculatendrl();
	 calculatendrlsub();
	 calculateszrl();
	 calculatekyrl();
	calculatezjyy();
	
}
/*
 * 200 需要有效装机容量
 */
 function calculatezjrl(){

		var row200 = $('#datagrid').treegrid('find',200);  //需要有效装机容量  控制月最高发电负荷*( 1 +有效备用率)  200
		var row100 = $('#datagrid').treegrid('find',100);	//最大负荷
		var row2002 = $('#datagrid').treegrid('find','200-2');	//有效备用率
		var row2001 = $('#datagrid').treegrid('find','200-1');	//需要有效装机容量－控制月最高发电负荷    200-1
		var tmp = [];
		tmp = $('#years').combo('getValues').join(",").split(",");
		for (var i = 0; i < tmp.length; i++) {
			if(!row100[tmp[i]]&&!row2002[tmp[i]]){
				
			}else{
			row200[tmp[i]] = fixNum(parseNumberExt(row100[tmp[i]])*(1+parseNumberExt(row2002[tmp[i]])/100.0));
			row2001[tmp[i]] = fixNum(parseNumberExt(row100[tmp[i]])*parseNumberExt(row2002[tmp[i]])/100.0);

			}
		}
		 $('#datagrid').treegrid('refresh',200);
		 $('#datagrid').treegrid('refresh','200-1');

	}
	
 /*
  * 300 投产
  */
  function calculatetcrl(){

 		var row300 = $('#datagrid').treegrid('find',300);  
 		var row3001 = $('#datagrid').treegrid('find','300-1');	
 		var row3002 = $('#datagrid').treegrid('find','300-2');	
 		var row3003 = $('#datagrid').treegrid('find','300-3');	
 		var row3004 = $('#datagrid').treegrid('find','300-4');	
 		var row3005 = $('#datagrid').treegrid('find','300-5');	
 		var row3006 = $('#datagrid').treegrid('find','300-6');	
 		var row3007 = $('#datagrid').treegrid('find','300-7');	

 		var tmp = [];
 		tmp = $('#years').combo('getValues').join(",").split(",");
 		for (var i = 0; i < tmp.length; i++) {
 			if(!row3001[tmp[i]]&&!row3002[tmp[i]]&&!row3003[tmp[i]]&&!row3004[tmp[i]]&&!row3005[tmp[i]]&&!row3006[tmp[i]]&&!row3007[tmp[i]]){
 				
 			}else{
 			row300[tmp[i]] = fixNum(parseNumberExt(row3001[tmp[i]])+parseNumberExt(row3002[tmp[i]])+parseNumberExt(row3003[tmp[i]])+parseNumberExt(row3004[tmp[i]])
 					+parseNumberExt(row3005[tmp[i]])+parseNumberExt(row3006[tmp[i]])+parseNumberExt(row3007[tmp[i]]));
 			}
 		}
 		 $('#datagrid').treegrid('refresh',300);

 	}
  /*
   * 400 退役
   */
   function calculatetyrl(){

  		var row400 = $('#datagrid').treegrid('find',400);  
  		var row4001 = $('#datagrid').treegrid('find','400-1');	
  		var row4002 = $('#datagrid').treegrid('find','400-2');	
  		var row4003 = $('#datagrid').treegrid('find','400-3');	
  		var row4004 = $('#datagrid').treegrid('find','400-4');	
  		var row4005 = $('#datagrid').treegrid('find','400-5');	
  		var row4006 = $('#datagrid').treegrid('find','400-6');	
  		var row4007 = $('#datagrid').treegrid('find','400-7');	

  		var tmp = [];
  		tmp = $('#years').combo('getValues').join(",").split(",");
  		for (var i = 0; i < tmp.length; i++) {
  			if(!row4001[tmp[i]]&&!row4002[tmp[i]]&&!row4003[tmp[i]]&&!row4004[tmp[i]]&&!row4005[tmp[i]]&&!row4006[tmp[i]]&&!row4007[tmp[i]]){
  				
  			}else{
  			row400[tmp[i]] = fixNum(parseNumberExt(row4001[tmp[i]])+parseNumberExt(row4002[tmp[i]])+parseNumberExt(row4003[tmp[i]])+parseNumberExt(row4004[tmp[i]])
  					+parseNumberExt(row4005[tmp[i]])+parseNumberExt(row4006[tmp[i]])+parseNumberExt(row4007[tmp[i]]));
  			}
  		}
  		 $('#datagrid').treegrid('refresh',400);

  	}
  
   /*
    * 1000 现有
    */
    function calculatexyrl(){

   		var row1000 = $('#datagrid').treegrid('find',1000);  
   		var row10001 = $('#datagrid').treegrid('find','1000-1');	
   		var row10002 = $('#datagrid').treegrid('find','1000-2');	
   		var row10003 = $('#datagrid').treegrid('find','1000-3');	
   		var row10004 = $('#datagrid').treegrid('find','1000-4');	
   		var row10005 = $('#datagrid').treegrid('find','1000-5');	
   		var row10006 = $('#datagrid').treegrid('find','1000-6');	
   		var row10007 = $('#datagrid').treegrid('find','1000-7');	

   		var tmp = [];
   		tmp = $('#years').combo('getValues').join(",").split(",");
   		for (var i = 0; i < tmp.length; i++) {
   			if(!row10001[tmp[i]]&&!row10002[tmp[i]]&&!row10003[tmp[i]]&&!row10004[tmp[i]]&&!row10005[tmp[i]]&&!row10006[tmp[i]]&&!row10007[tmp[i]]){
   				
   			}else{
   			row1000[tmp[i]] = fixNum(parseNumberExt(row10001[tmp[i]])+parseNumberExt(row10002[tmp[i]])+parseNumberExt(row10003[tmp[i]])+parseNumberExt(row10004[tmp[i]])
   					+parseNumberExt(row10005[tmp[i]])+parseNumberExt(row10006[tmp[i]])+parseNumberExt(row10007[tmp[i]]));
   			}
   		}
   		 $('#datagrid').treegrid('refresh',1000);

   	}
    /*
     * 500 年底
     */
     function calculatendrl(){

    		var row500 = $('#datagrid').treegrid('find',500);  
    		var row300 = $('#datagrid').treegrid('find',300);  
    		var row400 = $('#datagrid').treegrid('find',400);  
    		var row1000 = $('#datagrid').treegrid('find',1000);  
       		
    		var tmp = [];
    		tmp = $('#years').combo('getValues').join(",").split(",");
    		for (var i = 0; i < tmp.length; i++) {
    			if(!row300[tmp[i]]&&!row400[tmp[i]]&&!row1000[tmp[i]]){
    				
    			}else{
    			row500[tmp[i]] = fixNum(parseNumberExt(row300[tmp[i]])-parseNumberExt(row400[tmp[i]])+parseNumberExt(row1000[tmp[i]]));
    			}
    		}
    		 $('#datagrid').treegrid('refresh',500);

    	}
    
    function calculatendrlsub(){
    	var row10001 = $('#datagrid').treegrid('find','1000-1');	
   		var row10002 = $('#datagrid').treegrid('find','1000-2');	
   		var row10003 = $('#datagrid').treegrid('find','1000-3');	
   		var row10004 = $('#datagrid').treegrid('find','1000-4');	
   		var row10005 = $('#datagrid').treegrid('find','1000-5');	
   		var row10006 = $('#datagrid').treegrid('find','1000-6');	
   		var row10007 = $('#datagrid').treegrid('find','1000-7');	
		var row3001 = $('#datagrid').treegrid('find','300-1');	
 		var row3002 = $('#datagrid').treegrid('find','300-2');	
 		var row3003 = $('#datagrid').treegrid('find','300-3');	
 		var row3004 = $('#datagrid').treegrid('find','300-4');	
 		var row3005 = $('#datagrid').treegrid('find','300-5');	
 		var row3006 = $('#datagrid').treegrid('find','300-6');	
 		var row3007 = $('#datagrid').treegrid('find','300-7');	
  		var row4001 = $('#datagrid').treegrid('find','400-1');	
  		var row4002 = $('#datagrid').treegrid('find','400-2');	
  		var row4003 = $('#datagrid').treegrid('find','400-3');	
  		var row4004 = $('#datagrid').treegrid('find','400-4');	
  		var row4005 = $('#datagrid').treegrid('find','400-5');	
  		var row4006 = $('#datagrid').treegrid('find','400-6');	
  		var row4007 = $('#datagrid').treegrid('find','400-7');	
  		var row5001 = $('#datagrid').treegrid('find','500-1');	
  		var row5002 = $('#datagrid').treegrid('find','500-2');	
  		var row5003 = $('#datagrid').treegrid('find','500-3');	
  		var row5004 = $('#datagrid').treegrid('find','500-4');	
  		var row5005 = $('#datagrid').treegrid('find','500-5');	
  		var row5006 = $('#datagrid').treegrid('find','500-6');	
  		var row5007 = $('#datagrid').treegrid('find','500-7');	
  		var tmp = [];
		tmp = $('#years').combo('getValues').join(",").split(",");
		for (var i = 0; i < tmp.length; i++) {
			if(!row3001[tmp[i]]&&!row4001[tmp[i]]&&!row10001[tmp[i]]){
				
			}else{
			row5001[tmp[i]] = fixNum(parseNumberExt(row3001[tmp[i]])-parseNumberExt(row4001[tmp[i]])+parseNumberExt(row10001[tmp[i]]));
			}
			
			if(!row3002[tmp[i]]&&!row4002[tmp[i]]&&!row10002[tmp[i]]){
				
			}else{
			row5002[tmp[i]] = fixNum(parseNumberExt(row3002[tmp[i]])-parseNumberExt(row4002[tmp[i]])+parseNumberExt(row10002[tmp[i]]));
			}
			if(!row3003[tmp[i]]&&!row4003[tmp[i]]&&!row10003[tmp[i]]){
				
			}else{
			row5003[tmp[i]] = fixNum(parseNumberExt(row3003[tmp[i]])-parseNumberExt(row4003[tmp[i]])+parseNumberExt(row10003[tmp[i]]));
			}
			if(!row3004[tmp[i]]&&!row4004[tmp[i]]&&!row10004[tmp[i]]){
				
			}else{
			row5004[tmp[i]] = fixNum(parseNumberExt(row3004[tmp[i]])-parseNumberExt(row4004[tmp[i]])+parseNumberExt(row10004[tmp[i]]));
			}
			if(!row3005[tmp[i]]&&!row4005[tmp[i]]&&!row10005[tmp[i]]){
				
			}else{
			row5005[tmp[i]] = fixNum(parseNumberExt(row3005[tmp[i]])-parseNumberExt(row4005[tmp[i]])+parseNumberExt(row10005[tmp[i]]));
			}
			if(!row3006[tmp[i]]&&!row4006[tmp[i]]&&!row10006[tmp[i]]){
				
			}else{
			row5006[tmp[i]] = fixNum(parseNumberExt(row3006[tmp[i]])-parseNumberExt(row4006[tmp[i]])+parseNumberExt(row10006[tmp[i]]));
			}
			if(!row3007[tmp[i]]&&!row4007[tmp[i]]&&!row10007[tmp[i]]){
				
			}else{
			row5007[tmp[i]] = fixNum(parseNumberExt(row3007[tmp[i]])-parseNumberExt(row4007[tmp[i]])+parseNumberExt(row10007[tmp[i]]));
			}
		}
		 $('#datagrid').treegrid('refresh','500-1');
		 $('#datagrid').treegrid('refresh','500-2');
		 $('#datagrid').treegrid('refresh','500-3');
		 $('#datagrid').treegrid('refresh','500-4');
		 $('#datagrid').treegrid('refresh','500-5');
		 $('#datagrid').treegrid('refresh','500-6');
		 $('#datagrid').treegrid('refresh','500-7');


    }
    /*
     * 600受阻容量
     */
     function calculateszrl(){

    		var row600 = $('#datagrid').treegrid('find',600);  
    		var row6002 = $('#datagrid').treegrid('find','600-2');	
    		var row6003 = $('#datagrid').treegrid('find','600-3');	
    		var row6004 = $('#datagrid').treegrid('find','600-4');	
    		var row6005 = $('#datagrid').treegrid('find','600-5');	
    		var row6006 = $('#datagrid').treegrid('find','600-6');	
    		var row6007 = $('#datagrid').treegrid('find','600-7');	
    		var row6008 = $('#datagrid').treegrid('find','600-8');	

    		var tmp = [];
    		tmp = $('#years').combo('getValues').join(",").split(",");
    		for (var i = 0; i < tmp.length; i++) {
    			if(!row6008[tmp[i]]&&!row6002[tmp[i]]&&!row6003[tmp[i]]&&!row6004[tmp[i]]&&!row6005[tmp[i]]&&!row6006[tmp[i]]&&!row6007[tmp[i]]){
    				
    			}else{
    			row600[tmp[i]] = fixNum(parseNumberExt(row6008[tmp[i]])+parseNumberExt(row6002[tmp[i]])+parseNumberExt(row6003[tmp[i]])+parseNumberExt(row6004[tmp[i]])
    					+parseNumberExt(row6005[tmp[i]])+parseNumberExt(row6006[tmp[i]])+parseNumberExt(row6007[tmp[i]]));
    			}
    		}
    		 $('#datagrid').treegrid('refresh',600);

    	}
    
     /*
      * 700当年可用装机容量
      */
      function calculatekyrl(){
     		var row700 = $('#datagrid').treegrid('find',700);  
     		var row500 = $('#datagrid').treegrid('find','500');	
     		var row600 = $('#datagrid').treegrid('find','600');	
     		var row3003 = $('#datagrid').treegrid('find','300-3');	
     	

     		var tmp = [];
     		tmp = $('#years').combo('getValues').join(",").split(",");
     		for (var i = 0; i < tmp.length; i++) {
     			if(!row500[tmp[i]]&&!row600[tmp[i]]&&!row3003[tmp[i]]){
     				
     			}else{
     			row700[tmp[i]] = fixNum(parseNumberExt(row500[tmp[i]])-parseNumberExt(row600[tmp[i]])-parseNumberExt(row3003[tmp[i]]));
     			}
     		}
     		 $('#datagrid').treegrid('refresh',700);

     	}
/*
900装机盈余   =  当年可用容量 - 需要有效装机容量 +  控制月外购(+)外送(-)  700-200+800
*/
 function calculatezjyy(){

	var row900 = $('#datagrid').treegrid('find',900);  //900装机盈余
	var row200 = $('#datagrid').treegrid('find',200);	//200 需要有效装机容量
	var row700 = $('#datagrid').treegrid('find',700);	//700 当年可用装机容量
	var row800 = $('#datagrid').treegrid('find',800);	//800 外购外送
	var tmp = [];
	tmp = $('#years').combo('getValues').join(",").split(",");
	for (var i = 0; i < tmp.length; i++) {
		if(!row800[tmp[i]]&&!row700[tmp[i]]&&!row200[tmp[i]]){
			
		}else{
		row900[tmp[i]] = fixNum(parseNumberExt(row700[tmp[i]])-parseNumberExt(row200[tmp[i]])+parseNumberExt(row800[tmp[i]]));
		}
	}
	 $('#datagrid').treegrid('refresh',900);
}


function  parseNumberExt(num){
	if(!num){
		return 0;
	}else{
		 return parseFloat(num);
	}
}

function fixNum(num){
	return num.toFixed(3);
}
//点击事件
function clickEvent( field,row) {

		var d = new Date();
		savEvtTime = d.getTime();
		savTO = setTimeout(function() {
			clickonetime(field, row);
		}, dcTime);

}

/**
 * 单击事件
 */
function clickonetime(field, row) {
	if (savEvtTime - dcAt <= 0) {
		return false;
	}
	editCell(field, row);
}

/**
 * 解开一个格子的编辑状态
 */
function editCell( field, row) {
	if(editingId != undefined ){
		endEdit(editingId);
	}			
			if (row){
				editingId = row.id ;
				$('#datagrid').treegrid('beginEdit', editingId+'');
			}

}

/**
 * 结束表格编辑状态
 */
function endEdit(id) {
	$('#datagrid').treegrid('endEdit', id) ;

}
function extractData(){
	 var param = {
				'taskid':taskid,
				'year':years
			 };
	 $.ajax({
			type : 'POST',
			async : false,
			dataType: 'json',
			data: param,
			url :  path+'/powerbalance/extractData',
			success : function(data) {
				
				$.messager.alert("提示", "计算成功！");
				queryData();
			}
		});
}
/**
 * ‘保存’按钮功能
 */
function save() {
	if(editingId != undefined ){
		endEdit(editingId);
	}	
	var updates = $('#datagrid').treegrid('getChanges');
	if (updates.length <= 0) {
		return;
	}
	var param = JSONH.stringify($('#datagrid').treegrid('getData'));
	var data = {
		"taskid":taskid,
		"editObj" : param
	};
	$.ajax({
		type : 'POST',
		async : false,
		url : path + '/powerbalance/saveData',
		data : data,
		success : function(data) {
			if (data == "undifined") {
				$.messager.alert("提示", "保存失败！");
				$('#datagrid').datagrid('reload');
			} else {
				$.messager.alert("提示", "保存成功！");
				$('#datagrid').datagrid('reload');
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}
</script>
</head>
<body>
	<!-- 引入自定义按钮页面 -->
	<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a> <a id="tool_extract"> <img src='<%=path%>/static/images/js.gif'
			align='top' border='0' title='计算' />
		</a>
		<a id="tool_save"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' />
		</a>
		<a id="tool_export"> <img
			src='<%=path%>/static/images/daochu.gif' align='top' border='0'
			title='导出' />
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
		<tr>
					<td class="tdlft">任务：</td>
				<td class="tdrgt"><span id="task_name"></span></td>
		<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
		</tr>
		</table>
	</fieldset>
		<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦、%</b></div>
	
	<div id="datagrid_div">
		<table id="datagrid" ></table>
	</div>

</body>
</html>
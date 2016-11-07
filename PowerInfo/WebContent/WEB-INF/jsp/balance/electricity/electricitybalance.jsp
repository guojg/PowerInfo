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
	<%@include file="../../common/commonDefineBtn.jsp" %>

<% 
		BalanceTask tt=  (BalanceTask)request.getSession().getAttribute("balancetask");
		String taskid = tt.getId();
		String year = tt.getYear();
		String task_name = tt.getTask_name();
		%>
<script type="text/javascript">
var taskid='<%=taskid%>';
var years = '<%=year%>';
var task_name='<%=task_name%>';
var cols;
var savEvtTime = 0;
var dcAt = 0;
var dcTime = 250;
var savTO = null;
var editingId ;
$(function() {
	$('#task_name').html('<b>'+task_name+'</b>');

	$("#tool_query").bind("click", function() {
		queryData();
	});
	$("#tool_save").bind("click", function() {
		save();
	});
	$("#tool_extract").bind("click", function() {
		extractData();
	});
	$("#tool_export").bind("click", function() {
		ExportExcel();
	});
	$("#tool_xjrw").bind("click", function() {
		xjrw();
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
var noeditId=['100-100','200-1','300','300-1','300-2','300-3','300-4','300-5','300-6','300-7','300-8','400'];
var nosubeditId=['100-100','200-1','300','400','300-3'];
var hideeditId=['700','500-1','500-2','500-4','500-5','500-6','500-7','500-8','500'];

function queryData(){
	var yrs = $('#years').combo('getValues').join(",");

	//非冰冻列
	cols = createCols(yrs);

$('#datagrid').treegrid({    
	fitColumns:false,
   url:path+'/electricitybalance/queryData',   
   //url:path+'/js/basicData/treegrid_data3.json',
    idField:'id',    
    treeField:'code_name', 
    singleSelect:true,
	queryParams : {"year":years,"taskid":taskid},
    frozenColumns:[[    
        {title:'id',field:'id',width:180,hidden:true},    
        {field:'parentid',title:'_parentId',width:180,align:'right',hidden:true},  
        {field:'code_name',title:'指标',width:180},  
    ]] ,
	columns : cols,
	onLoadSuccess: function () {
		for(var i=0 ; i<hideeditId.length ;++i){
			$('tr[node-id='+ hideeditId[i] +']').hide();
		}
		 $('#datagrid').treegrid('resize');

		for(var i=0 ; i<noeditId.length ;++i){
			$('tr[node-id='+ noeditId[i] +']').css( 'background','buttonface');
			if( noeditId[i]=='300-1' || noeditId[i]=='300-2' || noeditId[i]=='300-4'||noeditId[i]=='300-5'
					||noeditId[i]=='300-6' ||noeditId[i]=='300-7' ||noeditId[i]=='300-8'){
				$('tr[node-id='+ noeditId[i] +']').find('td[field=hour_num]').css( 'background','white');

			}
		}
		$('tr[node-id=100]').find('td[field=hour_num]').css( 'background','buttonface');
		$('tr[node-id=200]').find('td[field=hour_num]').css( 'background','buttonface');

			/*$('tr[node-id=700]').hide();
			 $('#datagrid').treegrid('resize');*/
			 


	},
	onClickCell : function(field,row) {
		for(var i=0 ; i<nosubeditId.length ;++i){
			if(row.id==nosubeditId[i]){
				if(editingId != undefined ){
					endEdit(editingId);
				}	
				 return ;
			}
		}
		clickEvent(field,row);


	},
	onAfterEdit:function(row ,changes){
		calculateSum();
		for(var i=0 ; i<noeditId.length ;++i){
			$('tr[node-id='+ noeditId[i] +']').css( 'background','buttonface');
			if( noeditId[i]=='300-1' || noeditId[i]=='300-2' || noeditId[i]=='300-4'||noeditId[i]=='300-5'
					||noeditId[i]=='300-6' ||noeditId[i]=='300-7' ||noeditId[i]=='300-8'){
				$('tr[node-id='+ noeditId[i] +']').find('td[field=hour_num]').css( 'background','white');

			}
		}
		$('tr[node-id=100]').find('td[field=hour_num]').css( 'background','buttonface');
		$('tr[node-id=200]').find('td[field=hour_num]').css( 'background','buttonface');

	}
}); 
}
function ExportExcel() {//导出Excel文件
	var yrs = $('#years').combo('getValues').join(",");

	//用ajax发动到动态页动态写入xls文件中
	var f = $('<form action="'+path+'/electricitybalance/exportData" method="post" id="fm1"></form>');  
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
	calculatezzl();
	calculatezfdl();
	calculatezfdlsub();
	calculatezfdlcoal();
	calculatecoalhour();
}
/**
 * 增长率
 */
function calculatezzl(){
	var row100 = $('#datagrid').treegrid('find',100);  //全社会用电量
	var row100100 = $('#datagrid').treegrid('find','100-100');  //增长率

	var tmp = [];

	tmp = years.split(",");
	for (var i = 1; i < tmp.length; i++) {
		if(!row100[tmp[i]]&&!row100[tmp[i-1]]){
			
		}else if(parseNumberExt(row100[tmp[i-1]])==0){
			
		}else{
		row100100[tmp[i]] = fixNum((Math.pow(parseNumberExt(row100[tmp[i]])/parseNumberExt(row100[tmp[i-1]]),1.0/(parseNumberExt(tmp[i])-parseNumberExt(tmp[i-1])))-1)*100);
		}
	}
	 $('#datagrid').treegrid('refresh','100-100');

}
/*
 * 需自发用电量
 */
function calculatezfdl(){
	var row100 = $('#datagrid').treegrid('find',100);  //全社会用电量
	var row200 = $('#datagrid').treegrid('find',200);	//外购(+)外送(-)
	var row300 = $('#datagrid').treegrid('find',300);	//需自发用电量
	var tmp = [];

	tmp = $('#years').combo('getValues').join(",").split(",");
	for (var i = 0; i < tmp.length; i++) {
		if(!row100[tmp[i]]&&!row200[tmp[i]]){
			
		}else{
		row300[tmp[i]] = fixNum(parseNumberExt(row100[tmp[i]])-parseNumberExt(row200[tmp[i]]));
		}
	}
	 $('#datagrid').treegrid('refresh',300);
}
/*
 * 需自发用电量子项
 */
function calculatezfdlsub(){
	var row5001 = $('#datagrid').treegrid('find','500-1');  
	var row5002 = $('#datagrid').treegrid('find','500-2');  
	var row5004 = $('#datagrid').treegrid('find','500-4');  
	var row5005 = $('#datagrid').treegrid('find','500-5');  
	var row5006 = $('#datagrid').treegrid('find','500-6');  
	var row5007 = $('#datagrid').treegrid('find','500-7'); 
	var row5008 = $('#datagrid').treegrid('find','500-8');  
	
	var row3001 = $('#datagrid').treegrid('find','300-1');  
	var row3002 = $('#datagrid').treegrid('find','300-2');  
	var row3004 = $('#datagrid').treegrid('find','300-4');  
	var row3005 = $('#datagrid').treegrid('find','300-5');  
	var row3006 = $('#datagrid').treegrid('find','300-6');  
	var row3007 = $('#datagrid').treegrid('find','300-7'); 
	var row3008 = $('#datagrid').treegrid('find','300-8');  
	var tmp = [];

	tmp = $('#years').combo('getValues').join(",").split(",");
	for (var i = 0; i < tmp.length; i++) {
		if(!row5001){
			
		}else{
				if(!row5001[tmp[i]]&&!row3001['hour_num']){
					
				}else{
					row3001[tmp[i]] = fixNum(parseNumberExt(row5001[tmp[i]])*parseNumberExt(row3001['hour_num'])/10000);
				}
		}
		if(!row5002){
			
		}else{
				if(!row5002[tmp[i]]&&!row3002['hour_num']){
					
				}else{
					row3002[tmp[i]] = fixNum(parseNumberExt(row5002[tmp[i]])*parseNumberExt(row3002['hour_num'])/10000);
				}
		}
		if(!row5004){
			
		}else{
				if(!row5004[tmp[i]]&&!row3004['hour_num']){
					
				}else{
					row3004[tmp[i]] = fixNum(parseNumberExt(row5004[tmp[i]])*parseNumberExt(row3004['hour_num'])/10000);
				}
		}
		if(!row5005){
			
		}else{
				if(!row5005[tmp[i]]&&!row3005['hour_num']){
					
				}else{
					row3005[tmp[i]] = fixNum(parseNumberExt(row5005[tmp[i]])*parseNumberExt(row3005['hour_num'])/10000);
				}
		}
		if(!row5006){
			
		}else{
				if(!row5006[tmp[i]]&&!row3006['hour_num']){
					
				}else{
					row3006[tmp[i]] = fixNum(parseNumberExt(row5006[tmp[i]])*parseNumberExt(row3006['hour_num'])/10000);
				}
		}
		if(!row5007){
			
		}else{
				if(!row5007[tmp[i]]&&!row3007['hour_num']){
					
				}else{
					row3007[tmp[i]] = fixNum(parseNumberExt(row5007[tmp[i]])*parseNumberExt(row3007['hour_num'])/10000);
				}
		}
		if(!row5008){
			
		}else{
				if(!row5008[tmp[i]]&&!row3008['hour_num']){
					
				}else{
					row3008[tmp[i]] = fixNum(parseNumberExt(row5008[tmp[i]])*parseNumberExt(row3008['hour_num'])/10000);
				}
		}
		
	}
	 $('#datagrid').treegrid('refresh','300-1');
	 $('#datagrid').treegrid('refresh','300-2');
	 $('#datagrid').treegrid('refresh','300-4');
	 $('#datagrid').treegrid('refresh','300-5');
	 $('#datagrid').treegrid('refresh','300-6');
	 $('#datagrid').treegrid('refresh','300-7');
	 $('#datagrid').treegrid('refresh','300-8');

}

/*
 * 
 */
function  calculatecoalhour(){
	var row3003 = $('#datagrid').treegrid('find','300-3');  
	var row700 = $('#datagrid').treegrid('find','700');  
	var row400 = $('#datagrid').treegrid('find','400');  

	var tmp = [];

	tmp = $('#years').combo('getValues').join(",").split(",");
	for (var i = 0; i < tmp.length; i++) {
		if(!row3003[tmp[i]]&&!row700[tmp[i]]){
			
		}else{
		row400[tmp[i]] = fixNum(parseNumberExt(row3003[tmp[i]])/parseNumberExt(row700[tmp[i]])*10000);
		}
	}
	 $('#datagrid').treegrid('refresh','400');
}

/*
 * 需自发用电量(煤)
 */
function calculatezfdlcoal(){
	var row300 = $('#datagrid').treegrid('find',300);	//需自发用电量
	var row3001 = $('#datagrid').treegrid('find','300-1');  
	var row3002 = $('#datagrid').treegrid('find','300-2');  
	var row3004 = $('#datagrid').treegrid('find','300-4');  
	var row3005 = $('#datagrid').treegrid('find','300-5');  
	var row3006 = $('#datagrid').treegrid('find','300-6');  
	var row3007 = $('#datagrid').treegrid('find','300-7'); 
	var row3008 = $('#datagrid').treegrid('find','300-8');  
	
	var row3003 = $('#datagrid').treegrid('find','300-3');  
	var tmp = [];

	tmp = $('#years').combo('getValues').join(",").split(",");
	for (var i = 0; i < tmp.length; i++) {
		if(!row300[tmp[i]]&&!row3001[tmp[i]]&&!row3002[tmp[i]]&&!row3004[tmp[i]]&&!row3005[tmp[i]]&&!row3006[tmp[i]]&&!row3007[tmp[i]]&&!row3008[tmp[i]]){
			
		}else{
		row3003[tmp[i]] = fixNum(parseNumberExt(row300[tmp[i]])-parseNumberExt(row3001[tmp[i]])-parseNumberExt(row3002[tmp[i]])-parseNumberExt(row3004[tmp[i]])
				-parseNumberExt(row3005[tmp[i]])-parseNumberExt(row3006[tmp[i]])-parseNumberExt(row3007[tmp[i]])-parseNumberExt(row3008[tmp[i]]));
		}
	}
	 $('#datagrid').treegrid('refresh','300-3');
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
			'editor':{
				type:'numberspinner',
				options: {  
                    increment:10,  
                    precision:3
                   
                }  
			}

		});
	}
	cols.push({
		'field' : "hour_num",
		'title' : "机组利用小时数",
		'align' : 'center',
		'width' : 120,
		'editor':{
			type:'numberspinner',
			options: {  
                increment:10,  
                precision:3
               
            }  
		}

	});
	return new Array(cols);
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


		if( row.id=='300-1' || row.id=='300-2' || row.id=='300-4'||row.id=='300-5'
			||row.id=='300-6' ||row.id=='300-7' ||row.id=='300-8'){
		
			if ( cols && cols.length>=1) {
		
				for (var i = 0; i < cols[0].length-1; i++) {
				var columnOption = $('#datagrid').datagrid('getColumnOption',
						cols[0][i]['field']);	
					columnOption.editor = {};
				}
			}
		}
		if( row.id=='100' ||row.id=='200'){
			$('#datagrid').datagrid('getColumnOption',
					cols[0][cols[0].length-1]['field']).editor = {};
		}
		editingId = row.id ;
		$('#datagrid').treegrid('beginEdit', editingId);

		var editors = $('#datagrid').treegrid('getEditors', editingId);
		
		$.each(editors, function(i, editor) {
			if (editor.field === field) {
				$(editor.target).parent.find('tr[node-id='+row.id+']').find('td[field='+field+']').blur(function(){  
				    alert(2222);  
				}) 
			} else {
				//$(editor.target).hide().closest('div').text(editor.oldHtml);
			}
		});
		if( row.id=='300-1' || row.id=='300-2' || row.id=='300-4'||row.id=='300-5'
			||row.id=='300-6' ||row.id=='300-7' ||row.id=='300-8'){
		
			if ( cols && cols.length>=1) {
		
				for (var i = 0; i < cols[0].length-1; i++) {
				var columnOption = $('#datagrid').datagrid('getColumnOption',
						cols[0][i]['field']);	
					columnOption.editor = {
							type:'numberspinner',
							options: {  
				                increment:10,  
				                precision:3
				               
				            }  
						};
				}
			}
		}
		if( row.id=='100' ||row.id=='200'){
			$('#datagrid').datagrid('getColumnOption',
					cols[0][cols[0].length-1]['field']).editor = {
							type:'numberspinner',
							options: {  
				                increment:10,  
				                precision:3
				               
				            }  
						};
		}
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
			url :  path+'/electricitybalance/extractData',
			success : function(data) {
				
				$.messager.alert("提示", "计算成功！");
				queryData();
			}
		});
}
function xjrw(){
	commonHelper.toAdd({
		title : '图形分析',
		width : 550,
		height : 350,
		url : path + "/electricitybalance/electricitybalanceImage"
	});
	}
/**
 * ‘保存’按钮功能
 */
function save() {
	//endEdit();
	if(editingId != undefined ){
		endEdit(editingId);
	}	
	var updates = $('#datagrid').datagrid('getChanges');
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
		url : path + '/electricitybalance/saveData',
		data : data,
		success : function(data) {
			if (data == "undifined") {
				$.messager.alert("提示", "保存失败！");
				$('#datagrid').datagrid('reload');
				$('#datagrid').treegrid('acceptChanges');  

			} else {
				$.messager.alert("提示", "保存成功！");
				$('#datagrid').datagrid('reload');
				$('#datagrid').treegrid('acceptChanges');  

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
			<a id="tool_xjrw"> <img src='<%=path%>/static/images/xjrw.gif'
			align='top' border='0' title='图形分析' />
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
	<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦、亿千瓦时、小时</b></div>
	<div id="datagrid_div">
		<table id="datagrid" ></table>
	</div>

</body>
</html>
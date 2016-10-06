<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.regionalanalysis.db.task.entity.DbTask"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<title>电厂修改</title>
</head>
<%@include file="../../common/commonInclude.jsp" %>	
<%
String id=request.getParameter("id");
DbTask tt=  (DbTask)request.getSession().getAttribute("dbtask");
String taskid = tt.getId();
String task_name = tt.getTask_name();
Object obj=  request.getSession().getAttribute("maparea");
String organCode="";
if(obj != null) {
	organCode = obj.toString() ; }
%>
<script type="text/javascript">
//var area_id="1";
var id='<%=id%>';
var task_id='<%=taskid%>';
var area_id='<%=organCode%>';
$(function() {
	var data = {
			id:id,
			'task_id':task_id
		};
		$.ajax({
			type : 'POST',
			async : false,
			url : path + '/plantAnalysisdb/detailData',
			data : data,
			success : function(data) {
				var row=$.parseJSON(data);
				fzData(row[0]);
			}
		});
		initAccordion(data);

}

);

function initAccordion(data){
	var ids;
	var src=[];
	$.ajax({
		type : 'POST',
		async : false,
		url : path + '/plantAnalysisdb/getFdjByDc',
		data : data,
		success : function(data) {
			 ids=$.parseJSON(data);
			
		}
	});
	var Height_Page = $(document).height();
	var title_height = $("#fdjlist").position().top;
	var height = Height_Page - title_height;
	//加载选项卡页面
	var appenddiv="";
	for(var i=0;i<ids.length;i++){
		var count=i+1;
		appenddiv+='<div title="发电机-'+count+'"  id="p'+count+'">';
		appenddiv+='<iframe id="iframe'+i+'" scrolling="auto" frameborder="0"  style="width:100%;height:98%"></iframe>';
		appenddiv+='</div>';
		src[i]=path+'/generatorSetDbController/main?id='+ids[i]["id"]+"&task_id="+task_id;
	}
	$('#fdjlist').html(appenddiv).addClass("easyui-accordion");
	$('#fdjlist').accordion({
		height : height,
		tabHeight:'auto',
		onSelect : function(title, index){
			$('#iframe' + index).attr('src',src[index]);
		}
	});
}
//赋值
function fzData(item){
	//赋值开始
	$("#detailTable1 input").each(function(){
		var attrName = $(this).attr('id');//id
  	if(attrName!=null && attrName!=""){
  		var upperCaseAttrName = attrName;
  		var clas= $(this).attr('class');//用于区分是否是combo
  		if(clas!=null && clas!=""){
  			if(clas.indexOf("comboboxComponent")>=0){//combobox
          		$(this).combobox('setValue',item[upperCaseAttrName]);
          	}else if(clas.indexOf("easyui-datebox")>=0){
          		$(this).datebox('setValue',item[upperCaseAttrName]);
          	}
  		}else{
  			$(this).val(item[upperCaseAttrName]);//普通文本
  		}
  		//$(this).attr("disabled",true);
      }
	});
}
//取消
function cancel(){
	//关闭窗口
	window.parent.closeSingleExtent('电厂修改');
}
function save(){
	var formData = new Object();
	//取值
	$("#detailTable1 input").each(function(){
		var attrName = $(this).attr('id');//id
  	if(attrName!=null && attrName!=""){
  		var upperCaseAttrName = attrName;
  		var clas= $(this).attr('class');//用于区分是否是combo
  		if(clas!=null && clas!=""){
  			if(clas.indexOf("comboboxComponent")>=0){//combobox
          		_value=$(this).combobox('getValue');
  				formData[attrName]=_value;
          	}else if(clas.indexOf("easyui-datebox")>=0){
          		_value=$(this).datebox('getValue');
          		formData[attrName]=_value;
          	}
  		}else{
  			_value=$(this).val();//普通文本
  			formData[attrName]=_value;
  		}
      }
	});
	//if(!validate(operationdata)){
	//	return;
	//}
	var param={"editObj":JSONH.stringify(formData),"area_id":area_id,"task_id":task_id};
	debugger;
	$.ajax({
		  type: "post",
		  url: path + '/plantAnalysisdb/updateRecord',
		  data:param,
		  success:function(obj){
			  if(obj=='1'){
				  window.parent.$.messager.alert('提示','修改成功！','info',function(){
						//关闭窗口
						window.parent.queryData();
						});
				  window.parent.$('#win_div').window('close');
			  }else{
					$.messager.alert('提示','修改失败！','info');
					//关闭窗口
					window.parent.$('#win_div').window('close');
			   }
			}
		});
}
</script>
<body>
	<!-- 引入自定义按钮页面 -->
<div id="btn_div">
		<a id="btn_save"   href="javascript:save();" ><img src="/PowerInfo/static/images/save.gif" border="0" ></a>

<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0"></a>
</div>
<form id="paramsForm">
		<table id="detailTable1">
			<tr>
				<td class="tdlft" style='width: 180px' align="right">电厂名称：</td>
				<td class="tdrgt" style='width: 150px'><input id="plant_name"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">装机容量：</td>
				<td class="tdrgt" style='width: 150px'><input id="plant_capacity"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">发电量：</td>
				<td class="tdrgt" style='width: 150px'><input id="generating_capatity"
					type="text" style='width: 150px'/></td>
			</tr>
			<tr>
				<td class="tdlft" style='width: 180px' align="right">电厂损耗：</td>
				<td class="tdrgt" style='width: 150px'><input id="plant_loss"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">初始投资：</td>
				<td class="tdrgt" style='width: 150px'><input id="start_outlay"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">投产年：</td>
				<td class="tdrgt" style='width: 150px'><input id="product_year"
					type="text" style='width: 150px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px' align="right">经济运行寿命：</td>
				<td class="tdrgt" style='width: 150px'><input id="economical_life"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">期望收益率：</td>
				<td class="tdrgt" style='width: 150px'><input id="equired_return"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">年财务成本：</td>
				<td class="tdrgt" style='width: 150px'><input id="financial_cost"
					type="text" style='width: 150px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px' align="right">发电煤耗：</td>
				<td class="tdrgt" style='width: 150px'><input id="generation_coal"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">运行维护费率：</td>
				<td class="tdrgt" style='width: 150px'><input id="operation_rate"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">运行维护成本：</td>
				<td class="tdrgt" style='width: 150px'><input id="operation_cost"
					type="text" style='width: 150px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px' align="right">燃料单位成本：</td>
				<td class="tdrgt" style='width: 150px'><input id="unit_cost"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">电厂材料费（元/年）：</td>
				<td class="tdrgt" style='width: 150px'><input id="materials_cost"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">工资、奖金及福利费（元/年）：</td>
				<td class="tdrgt" style='width: 150px'><input id="salary"
					type="text" style='width: 150px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px' align="right">修理费（元/年）：</td>
				<td class="tdrgt" style='width: 150px'><input id="repairs_cost"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">其他费用（元/年）：</td>
				<td class="tdrgt" style='width: 150px'><input id="other_cost"
					type="text" style='width: 150px' /></td>
			</tr>
				<input type="hidden" id="id"/>	
 		</table>
 	<div id="fdjlist">  

	</div>  
	</form>
</body>
</html>
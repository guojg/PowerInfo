<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" /> 
<title>电厂详情</title>
</head>
<%@include file="../../common/commonInclude.jsp" %>	
<%
String id=request.getParameter("id"); 
Object obj=request.getSession().getAttribute("maparea");
String organCode="";
if(obj!=null){
	organCode=obj.toString();
}
%>
<script type="text/javascript">
var area_id="<%=organCode%>";
var id='<%=id%>';
$(function() {
	var data = {
			id:id
		};
		$.ajax({
			type : 'POST',
			async : false,
			url : path + '/plantAnalysis/detailData',
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
		url : path + '/plantAnalysis/getFdjByDc',
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
		src[i]=path+'/generatorSetController/detail?id='+ids[i]["id"];
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
  		$(this).attr("disabled",true);
      }
	});
}
//取消
function cancel(){
	//关闭窗口
	window.parent.closeSingleExtent('电厂详情');
}
</script>
<body>
	<!-- 引入自定义按钮页面 -->
<div id="btn_div">
<a id="btn_cancel" href="javascript:cancel();" ><img src="/PowerInfo/static/images/quxiao.gif" border="0"></a>
</div>
<form id="paramsForm">
		<table id="detailTable1">
			<tr>
				<td class="tdlft" style='width: 180px' align="right">电厂名称：</td>
				<td class="tdrgt" style='width: 150px'><input id="plant_name"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">装机容量(万千瓦)：</td>
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
				<td class="tdlft" style='width: 180px' align="right">初始投资(元)：</td>
				<td class="tdrgt" style='width: 150px'><input id="start_outlay"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">投产年：</td>
				<td class="tdrgt" style='width: 150px'><input id="product_year"
					type="text" style='width: 150px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px' align="right">经济运行寿命(年)：</td>
				<td class="tdrgt" style='width: 150px'><input id="economical_life"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">期望收益率(%)：</td>
				<td class="tdrgt" style='width: 150px'><input id="equired_return"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">年财务成本(元)：</td>
				<td class="tdrgt" style='width: 150px'><input id="financial_cost"
					type="text" style='width: 150px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px' align="right">发电煤耗(吨)：</td>
				<td class="tdrgt" style='width: 150px'><input id="generation_coal"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">运行维护费率：</td>
				<td class="tdrgt" style='width: 150px'><input id="operation_rate"
					type="text" style='width: 150px' /></td>
				<td class="tdlft" style='width: 180px' align="right">运行维护成本(元)：</td>
				<td class="tdrgt" style='width: 150px'><input id="operation_cost"
					type="text" style='width: 150px' /></td>
			</tr>	
			<tr>
				<td class="tdlft" style='width: 180px' align="right">燃料单位成本(元)：</td>
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
 		</table>
 	<div id="fdjlist">  

	</div>  
	</form>
</body>
</html>
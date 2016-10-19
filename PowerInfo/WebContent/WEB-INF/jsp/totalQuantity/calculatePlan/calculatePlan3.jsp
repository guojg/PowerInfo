<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>
 <!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../../common/commonInclude.jsp" %>	
<% 
TotalTask tt=  (TotalTask)request.getSession().getAttribute("totaltask");
String algorithm = tt.getAlgorithm() ;
String taskid = tt.getId();
String task_name = tt.getTask_name();
String algorithmRadio = tt.getAlgorithmRadio();
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/calculatePlanStyle.css" />
<script type="text/javascript" src="<%=path %>/js/totalquantity/common/sysdict.js"></script>
<script type="text/javascript" src="<%=path%>/js/totalquantity/jsonutil.js"></script>
<script type="text/javascript">
var algorithmStr='<%=algorithm%>';  //算法代号
var taskid='<%=taskid%>';  //算法代号
var algorithmRadio='<%=algorithmRadio%>';//综合算法
var task_name='<%=task_name%>';
var form = document.forms[0];





//保存form里的所有原始属性
var alSavedElements = new Array();
//是否判断form的属性改变，true判断，false不判断
var bCheckForm = true;

/**//**
* 页面加载时候的操作，所有页面加载时的操作可以在这里进行扩展
* @param form 所要保存属性的form
*/
function saveElementsOnLoad(form) {
   saveFormElements(form);
}

/**//**
* 离开页面时的操作，所有离开页面前的操作可在这里进行扩展
* @param form 所要保存属性的form
* @param elements 所保存的属性的数组
* @return 离开页面的提示信息
*/
function checkFormOnUnload(form) {
   var bFormStatus = isFormChanged(form);
   if(bCheckForm && bFormStatus) {
       return "该页面已经被修改，离开后所有修改全部丢失！";
   }
   return;
}

/**//**
* 保存form的所有属性
* @param form 所要保存的属性的form
*/
function saveFormElements(form) {
   for(var i = 0 ; i < form.elements.length ; i++ ) {
       if("select-one" == form.elements[i].type) {
           alSavedElements.push(form.elements[i].selectedIndex);
           continue;
       }
       if("radio" == form.elements[i].type || "checkbox" == form.elements[i].type) {
           alSavedElements.push(form.elements[i].checked);
           continue;
       }
       alSavedElements.push(form.elements[i].value);
   }
}

/**//**
* 检查form的属性是否改变
* @param form 所比较的form
* @param alSavedElements 所比较的原始属性数组
* @return true form已变化 false form没变化
*/
function isFormChanged(form) {
   var bChanged = false;
   if(form.elements.length!=alSavedElements.length) {
       bChanged = true;
       return bChanged;
   }
   debugger;
   for(var i = 0 ; i < form.elements.length ; i++ ) {
       if("submit" != form.elements[i].type && "button" != form.elements[i].type && "reset" != form.elements[i].type && "hidden" != form.elements[i].type && "radio" != form.elements[i].type && "checkbox" != form.elements[i].type && "select-one" != form.elements[i].type && form.elements[i].value!=alSavedElements[i]) {
           bChanged = true;
           break;
       }
       if("select-one" == form.elements[i].type && form.elements[i].selectedIndex!=alSavedElements[i]) {
           bChanged = true;
           break;
       }
       if(("radio" == form.elements[i].type || "checkbox" == form.elements[i].type ) && form.elements[i].checked != alSavedElements[i]) {
           bChanged = true;
           break;
       }
   }
   return bChanged;
}

/**//**
* 忽略form改变检查
* 说明：该函数调用后即使form改变也不做离开页面的提示
*/
function ignoreFormCheck() {
   bCheckForm = false;
}

/**//**
* 强制进行form改变检查
* 说明：该函数调用后无论form是否改变都会提示form已改变，是否离开页面
*/
function forceFormCheck() {
   bCheckForm = true;
}

/**//**
* 返回form的当前状态
* 说明：用户可以通过该状态决定程序的流程走向
* @param form 所检查的form对象
* @return ture form改变，true form没改变
*/
function getFormStatus(form) {
  return isFormChanged(form);
}
</script>
<script type="text/javascript" src="<%=path %>/js/totalquantity/calculatePlan/calculatePlan.js"></script>

</head>
<body>
<div id="btn_div">
		<a id="tool_save" href="javascript:save();" style="margin:5px;"> <img src='<%=path%>/static/images/save.gif'
			align='top' border='0' title='保存' /></a>
			</div>
				<fieldset id="field">
		<legend></legend>
		<table id="search_tbl">
			<tr>
			<td class="tdlft">任务：</td>
				<td class="tdrgt" style="width:300px"><span id="task_name"></span></td>
			
			</tr>
		</table>
	</fieldset>
	<form  id="aa" >
	<table id="calculateTable">
		
			<tr>
			<td rowspan="4" class="bs1">预测算法</td>
				<td  class="bs3"><input name="algorithm" type="checkbox" value="1" />平均增长率法</td>
				<td class="bs3"><input name="algorithm" type="checkbox" value="2" />产值单耗法</td>
			
			</tr>
			<tr>
				<td class="bs3">
					<table id="1"  class="bs2" style="width:270px">
						   <tr> <td class="tdlft">最大值(%)：</td> <td class="tdrgt"><input  type="text" name="maxRate" id="maxRate"></td></tr>
						    <tr> <td  class="tdlft">最小值(%)：</td> <td class="tdrgt"><input  type="text" name="minRate" id="minRate"></td></tr>
						 <tr> <td class="tdlft">最可能值(%)：</td> <td class="tdrgt"><input  type="text" name="possibleRate" id="possibleRate"></td></tr>
					</table> 
				</td>
				<td class="bs3">
					<table id="2" class="bs2" style="width:300px">
						     <tr> 
						     	<td class="tdlft"> 一产单耗增长率(%)：</td> 
						     	<td class="tdrgt"><input  type="text" name="oneProductionRate" id="oneProductionRate"></td>
						     </tr>
						     <tr> 
						       <td class="tdlft">二产单耗增长率(%)：</td>
						       <td class="tdrgt"><input  type="text" name="twoProductionRate" id="twoProductionRate"></td>
						     </tr>
						    <tr> 
						       <td class="tdlft">三产单耗增长率(%)：</td> 
						       <td class="tdrgt"><input  type="text" name="threeProductionRate" id="threeProductionRate"></td>
						   </tr>
						  <tr> 
						  	   <td class="tdlft">  人均居民生活用电量增长率(%)：</td> 
						  	   <td class="tdrgt"><input  type="text" name="avgElectricityRate" id="avgElectricityRate"></td>
						  </tr>
					</table> 
				</td>
				
			</tr>
			<tr>
				<td class="bs3"><input name="algorithm" type="checkbox" value="3" />弹性系数法</td>
				<td class="bs3"><input name="algorithm" type="checkbox" value="4" />人均用电量法</td>
			</tr>
			<tr>
				<td  class="bs3">	
					<table id="3" class="bs2" style="width:300px">
						 <tr> 
						  	   <td class="tdlft">  期间电力弹性系数：</td> 
						       <td class="tdrgt"><input  type="text" name="coefficient" id="coefficient"></td>
						   </tr>
						  <tr> 
						  	   <td class="tdlft">  国内生产总值平均年增长速度(%)：</td> 
						       <td class="tdrgt"><input  type="text" name="incrementSpeed" id="incrementSpeed"></td>
						   </tr>
					</table> 
				</td>
				<td class="bs3">
					<table id="4"  class="bs2" style="width:270px">
						   <tr> <td class="tdlft">最大值(%)：</td> <td class="tdrgt"><input  type="text" name="avgMaxRate" id="avgMaxRate"></td></tr>
						    <tr> <td  class="tdlft">最小值(%)：</td> <td class="tdrgt"><input  type="text" name="avgMinRate" id="avgMinRate"></td></tr>
						 <tr> <td class="tdlft">最可能值(%)：</td> <td class="tdrgt"><input  type="text" name="avgPossibleRate" id="avgPossibleRate"></td></tr>
					</table> 
				</td>
			</tr>
			<tr id="ccc">
				
				<td class="bs1" id="aaa" rowspan="2">综合预测</td>
				<td class="bs3"><input name="algorithmRadio" type="radio" value="5" /><font class="fontred">平均值法</font></td>
				<td class="bs3"><input name="algorithmRadio" type="radio" value="6" /><font class="fontred">最优权重法 </font></td>
			</tr>
			<tr id="bbb">
				<td class="bs3">
				<div id="avgvalidate" class="divred"></div>
				</td>
				<td class="bs3"></td>
			</tr>
		
			
			
	</table>
	</form>
 


</body>
</html>
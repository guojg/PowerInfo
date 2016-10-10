<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>
 <!DOCTYPE html>
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
String baseyear = tt.getBaseyear();
String planyear = tt.getPlanyear();
String algorithmRadio = tt.getAlgorithmRadio();
String task_name = tt.getTask_name();

%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/icon.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css//button.css" >
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/chart.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/index.css" />
<script type="text/javascript">
var taskid='<%=taskid%>';  //任务号
var baseyear='<%=baseyear%>';//规划年
var planyear='<%=planyear%>';//规划年
var algorithmRadio='<%=algorithmRadio%>';
var task_name='<%=task_name%>';

var algorithm='<%=algorithm%>';

//id,taskid,algorithm,year,value from totaldata
	var cols;
	// 图表
	var chart;
	var areas = [];

	// 图表固定配置
	var options = {
		chart : {
			renderTo : 'container'
		},
		credits : {
			enabled : false
		},
		tooltip : {
			crosshairs : true,
			borderRadius : 5,
			borderColor : '#00B997',
			shared : true,
			formatter : function() {
				if (this.x) {
					var s = '<b>' + this.x + '</b>';
					if (this.points) {
						$.each(this.points, function(i, point) {
							s += '<br/><span style="color:' + point.series.color
									+ '">' + point.series.name + '</span>: <b>'
									+ point.y + '</b>';
						});
					} else {
						s += '<br/><span style="color:' + this.series.color + '">'
								+ this.series.name + '</span>: <b>' + this.y
								+ '</b>';
					}

					return s;
				} else {
					return '<b>' + this.y + '</b>';
				}
			}
		},
		exporting : {
			enabled : false,
			url : path + "/export/exportImage"
		},
		plotOptions : {
			column : {
				pointPadding : 0.2,
				borderWidth : 0
			},
			pie : {
				allowPointSelect : true,
				cursor : 'pointer',
				showInLegend : true,
				dataLabels : {
					formatter : function() {
						return Number(this.percentage).toFixed(2) + "%";
					},
					enabled : true
				}
			}
		}
	};
	var categories = [];
	$(function() {
		$('#task_name').val(task_name);

		comboBoxInit({
			id : "years",
			url : path + '/totalData/getyears?baseyear'+baseyear+'&planyear='+planyear,
			textkey : "yearName",
			valuekey : "year",
			multiple : true
		});
		$("#tool_query").bind("click", function() {
			queryData();
		});
		$("#tool_export").bind("click", function() {
			ExportExcel();
		});
		$("#tool_exporttu").bind("click", function() {
			ExportTu();
		});
		queryData();
		
	});

	//查询方法调用的函数
	function queryData() {
		var years=$('#years').combobox('getValues').join(",");
		var baseyearInt = parseInt(baseyear);

		//非冰冻列
		cols = createCols(years);
		//查询条件暂时放外面
		//var queryParams = {};
		var queryParams = {"taskid":taskid,"year":years,"baseyearInt":baseyearInt,"algorithm":3};

		var url = path + '/totalData/queryData6';
		$('#datagrid').datagrid({
			width : 'auto',
			autoRowHeight : true,
			collapsible : true,
			url : url,
			remoteSort : false,
			columns : cols,
			rownumbers : true,
			pagination : false,
			queryParams : queryParams,
			onLoadSuccess:function(data){
				drawChart();
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
				'editor' : 'text'

			});
		}
		return new Array(cols);
	}

	/**
	 * 绘制图表
	 */
	function drawChart() {
		// 绘制图表
		categories=[];
		chart = new Highcharts.Chart($.extend(options, getSettings()));
	}

	/**
	 * 获取图表配置（动态）
	 */
	function getSettings() {
		// 解决导出时batik 不支持 css3 rgba属性的问题
		/*Highcharts
				.wrap(
						Highcharts.Chart.prototype,
						'getSVG',
						function(proceed) {
							return proceed
									.call(this)
									.replace(
											/(fill|stroke)="rgba\(([ 0-9]+,[ 0-9]+,[ 0-9]+),([ 0-9\.]+)\)"/g,
											'$1="rgb($2)" $1-opacity="$3"');
						});*/
						
		
		var pic_type = "spline";
		//var data = loadData( pic_type, 0, true);
		var data = loadData( pic_type, 0, true);
		var settings = {};
		settings.title = {
			text : ""
		};
		settings.xAxis = [ {
			categories : categories,
			plotLines : []
		} ];
		settings.yAxis = [ {
			min : 0,
			labels : {
				formatter : function() {
					return this.value;
				}
			},
			plotLines : [],
			stackLabels : {
				enabled : true,
				style : {
					fontWeight : 'bold'
				}
			},
			title : {
				rotation : 0,
				text : ""
			}
		} ];
		settings.series = data;
		return settings;
	}
	/**
	 * 取数组中resKey = resVal的对象的retKey的值
	 * 
	 * @param resKey
	 *            数组中某一对象的key
	 * @param resVal
	 *            数组中某一对象的value
	 * @param retKey
	 *            数组中该对象的其它key
	 * @return 返回匹配的对象的key对应的值
	 */
	Array.prototype.getValue = function(resKey, resVal, retKey) {
		for (var i = 0; i < this.length; i++) {
			for ( var j in this[i]) {
				if (j == resKey && this[i][j] == resVal) {
					return this[i][retKey];
				}
			}
		}
	}
	function loadData(chartType, yIndex, isInit) {
		var selections = $('#datagrid').datagrid('getRows');
		var type = chartType;
		var xLastLevel =cols[0];
		var frozon =$('#datagrid').datagrid('getColumnFields',true);
		//var ylastField = frozon.pop();
		var xlastField = null;
		var list = [];
		
		for (var i = 0,len = selections.length; i < len; i++) {
			var series = {};
			var data = [];
			series.name ="用电量";
			series.type = type;
			
			if (!isInit) {
				series.color = getRandomColor();
			}
			for (var j = 0, len2 = xLastLevel.length; j < len2; j++) {
				var slice = [];
				xlastField = xLastLevel[j]['field'];
				var shortname = areas.getValue('name', xLastLevel[j]['title'], 'shortname') || xLastLevel[j]['title'];
				slice.push(shortname);
				if(!isNaN(Number(selections[i][xlastField]))){
					slice.push(Number(selections[i][xlastField]));
				}else{
					slice.push(0);
				}
				
				data.push(slice);
				if (i == 0) {
					categories.push(shortname);
				}
			}
			series.dataLabels = {enabled: true};
			series.data = data;
			list.push(series);
		}
		return list;
	}
	function ExportTu() {//导出Excel文件
		chart.exportChart();
		/*var yrs = $('#years').combo('getValues').join(",");
		var baseyearInt = parseInt(baseyear);

		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/totalData/exportData6" method="post" id="fm1"></form>');  
	    var i = $('<input type="hidden" id="year" name="year" />');  
	    var l = $('<input type="hidden" id="baseyearInt" name="baseyearInt" />');
	    var m = $('<input type="hidden" id="taskid" name="taskid" />');  
	    var n= $('<input type="hidden" id="algorithm" name="algorithm"  />');  

		i.val(yrs);  
		i.appendTo(f);  
		l.val(baseyearInt);  
		l.appendTo(f);  
		m.val(taskid);  
		m.appendTo(f); 
		n.val("5");  
		n.appendTo(f); 
		f.appendTo(document.body).submit();  
		document.body.removeChild(f);  */
		//abc();
	}
	function abc(){
		var yrs = $('#years').combo('getValues').join(",");
		var baseyearInt = parseInt(baseyear);
		var param = 'baseyearInt=' + baseyearInt + '&year=' + yrs
		+ '&taskid=' + taskid + '&algorithm=3';
	window.location.href = path+'/totalData/exportData6?' + param;
	}
	
	function ExportExcel(){
		var yrs = $('#years').combo('getValues').join(",");
		var baseyearInt = parseInt(baseyear);

		//用ajax发动到动态页动态写入xls文件中
		var f = $('<form action="'+path+'/totalData/exportData6" method="post" id="fm1"></form>');  
	    var i = $('<input type="hidden" id="year" name="year" />');  
	    var l = $('<input type="hidden" id="baseyearInt" name="baseyearInt" />');
	    var m = $('<input type="hidden" id="taskid" name="taskid" />');  
	    var n= $('<input type="hidden" id="algorithm" name="algorithm"  />');  

		i.val(yrs);  
		i.appendTo(f);  
		l.val(baseyearInt);  
		l.appendTo(f);  
		m.val(taskid);  
		m.appendTo(f); 
		n.val("3");  
		n.appendTo(f); 
		f.appendTo(document.body).submit();  
		document.body.removeChild(f);  
	}
</script>
</head>
<body>
<div id="btn_div">
		<a id="tool_query"> <img src='<%=path%>/static/images/query.gif'
			align='top' border='0' title='查询' />
		</a> 
		<a id="tool_export"> <img
			src='<%=path%>/static/images/daochubiao.jpg' align='top' border='0'
			title='导出表' />
		</a>
		<a id="tool_exporttu"> <img
			src='<%=path%>/static/images/daochutu.jpg' align='top' border='0'
			title='导出图' />
		</a>
	</div>
	<fieldset id="field">
		<legend>查询条件</legend>
		<table id="search_tbl">
			<tr>
				<td class="tdlft">任务：</td>
				<td class="tdrgt"><input id="task_name" name="task_name" type="text" disabled="disabled"/></td>
				<td class="tdlft">年份：</td>
				<td class="tdrgt"><input id="years" class="comboboxComponent" /></td>
			</tr>
		</table>
	</fieldset>
			<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦时</b></div>
	
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>
	<br>
	<div id="container" style="width: 100%; height: 60%"></div>
	<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/raphael.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/js/highcharts.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/js/themes/custom.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/js/modules/exporting.src.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.js"></script>
				
</body>
</html>
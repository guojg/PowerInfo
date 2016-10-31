<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.github.totalquantity.task.entity.TotalTask"%>
 <!DOCTYPE html>
<html>
<head>
<title>图形分析</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/themes/icon.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css//button.css" >
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/chart.css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/index.css" />
<script type="text/javascript">



//id,taskid,algorithm,year,value from totaldata

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
					var s = '';
					if (this.points) {
						$.each(this.points, function(i, point) {
							s += '<b>' +point.key + '</b>'+'<br/><span style="color:' + point.series.color
									+ '">' + point.series.name + '</span>: <b>'
									+ point.y + '</b>';
						});
					} 
					return s;
				
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
		$("#tool_exporttu").bind("click", function() {
			ExportTu();
		});
	drawChart();
	});
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
		Highcharts
				.wrap(
						Highcharts.Chart.prototype,
						'getSVG',
						function(proceed) {
							return proceed
									.call(this)
									.replace(
											/(fill|stroke)="rgba\(([ 0-9]+,[ 0-9]+,[ 0-9]+),([ 0-9\.]+)\)"/g,
											'$1="rgb($2)" $1-opacity="$3"');
						});
		
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
		debugger;
		var selections =  window.parent.$('#datagrid').treegrid('find',400);;
		var type = chartType;
		var xLastLevel = window.parent.cols[0];
		//var frozon = window.parent.$('#datagrid').datagrid('getColumnFields',true);
		//var ylastField = frozon.pop();
		var ylastField ="煤电利用小时数";
		var list = [];
		
		//for (var i = 0,len = xLastLevel.length; i < len; i++) {
			var series = {};
			var data = [];
			series.name = ylastField ;
			series.type = type;
			
			if (!isInit) {
				series.color = getRandomColor();
			}
			for (var j = 0, len2 = xLastLevel.length-1; j < len2; j++) {
				var slice = [];
				var shortname =  xLastLevel[j].title;
				slice.push(shortname);
				slice.push(Number(selections[xLastLevel[j].field]));
				data.push(slice);
			}
			series.dataLabels = {enabled: true};
			series.data = data;
			list.push(series);
		//}
		return list;
	}
	
	function ExportTu() {//导出Excel文件
		chart.exportChart();
		
	}
</script>
</head>
<body>
	<div id="btn_div">
	
		<a id="tool_exporttu"> <img
			src='<%=path%>/static/images/daochutu.jpg' align='top' border='0'
			title='导出图' />
		</a>
	</div>
	<div id="container" style="padding-left: 5px;padding-right: 5px;padding-top: 5px;height: 90%"></div>
	<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/raphael.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/js/highcharts.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/js/themes/custom.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/Highcharts-4.0.1/js/modules/exporting.js"></script>
			<script type="text/javascript"
				src="<%=path%>/static/js/jquery-easyui-1.4/farbtastic/farbtastic.js"></script>
</body>
</html>
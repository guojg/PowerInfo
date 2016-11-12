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
String task_name = tt.getTask_name();

String algorithmRadio = tt.getAlgorithmRadio();
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
var algorithm='<%=algorithm%>';
var task_name='<%=task_name%>';


//id,taskid,algorithm,year,value from totaldata
	var cols;
	var frozenCols = [ [ {
		field : 'algorithm_name',
		title : '算法',
		width : 100,
		align : 'center'
	} ] ];
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
			url : path + "/chartExport"
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
		$('#task_name').html('<b>'+task_name+'</b>');

		queryData();
		
	});

	//查询方法调用的函数
	function queryData() {
		var years='';
		var baseyearInt = parseInt(baseyear);
		var planyearInt = parseInt(planyear);

		for(var i = baseyearInt+1 ; i<=planyearInt ;++i){
			years +=i+",";
		}
		years= years.substring(0,years.length-1);
		//非冰冻列
		cols = createCols(years);
		//查询条件暂时放外面
		//var queryParams = {};
		var queryParams = {"taskid":taskid,"year":years,"index_type":algorithm+','+algorithmRadio};

		var url = path + '/totalData/queryData';
		$('#datagrid').datagrid({
			width : 'auto',
			autoRowHeight : true,
			collapsible : true,
			url : url,
			remoteSort : false,
			frozenColumns : frozenCols,
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
		var selections = $('#datagrid').datagrid('getRows');
		var type = chartType;
		var xLastLevel =cols[0];
		var frozon =$('#datagrid').datagrid('getColumnFields',true);
		var ylastField = frozon.pop();
		var xlastField = null;
		var list = [];
		
		for (var i = 0,len = selections.length; i < len; i++) {
			var series = {};
			var data = [];
			series.name = selections[i][ylastField];
			series.type = type;
			
			if (!isInit) {
				series.color = getRandomColor();
			}
			for (var j = 0, len2 = xLastLevel.length; j < len2; j++) {
				var slice = [];
				xlastField = xLastLevel[j]['field'];
				var shortname = areas.getValue('name', xLastLevel[j]['title'], 'shortname') || xLastLevel[j]['title'];
				slice.push(shortname);
				var num = selections[i][xlastField];
				if(isNaN(num)){
					num=0;
				}
				slice.push(num);
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
</script>
</head>
<body>
<fieldset id="field">
		<legend></legend>
		<table id="search_tbl">
			<tr>
			<td class="tdlft">任务：</td>
				<td class="tdrgt" style="width:300px"><span id="task_name"></span></td>
				
			</tr>
		</table>
	</fieldset>
		<div id="title" style="padding-right: 5px;text-align: right"><b>单位：万千瓦时</b></div>
	
	<div id="datagrid_div">
		<table id="datagrid"></table>
	</div>
	<br>
	<div id="container" style="width: 100%; height: 50%"></div>
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
// 图表
var chart;
var chart1;
var chart2;
var chart3;


//window.parent.frames["iframe0"].jQuery;
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
			return '<span >' +this.key+"<br/>百分比 :"+Number(this.percentage).toFixed(2) +  '%</span><b>值:'+this.y+ '</b>';
		}
	},
	exporting : {
		enabled : false,
		url : path + "/export/exportImage",
		type:"image/png"
	},
	plotOptions : {
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
var options1= {
		chart : {
			renderTo : 'container1'
		},
		credits : options.credits,
		tooltip : options.tooltip,
		exporting : options.exporting,
		plotOptions : options.plotOptions
	};
var options2= {
		chart : {
			renderTo : 'container2'
		},
		credits : options.credits,
		tooltip : options.tooltip,
		exporting : options.exporting,
		plotOptions : options.plotOptions
	};
var options3= {
		chart : {
			renderTo : 'container3'
		},
		credits : options.credits,
		tooltip : options.tooltip,
		exporting : options.exporting,
		plotOptions : options.plotOptions
	};

var categories = [];
var categories1 = [];
var categories2 = [];
var categories3 = [];
$(function() {
	/*$("#exportBtn").bind("click", function() {
		$("#exportBtn1").click();
		$("#exportBtn2").click();

	});
	$("#exportBtn1").bind("click", function() {
		chart1.exportChart();
		
	});
	$("#exportBtn2").bind("click", function() {
		chart1.exportChart();
		
	});*/


	drawChart();
	drawChart1();
	drawChart2();
	drawChart3();

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
 * 绘制图表
 */
function drawChart1() {
	// 绘制图表
	categories1=[];
	chart1 = new Highcharts.Chart($.extend(options1, getSettings1()));
}
/**
 * 绘制图表
 */
function drawChart2() {
	// 绘制图表
	categories2=[];
	chart2 = new Highcharts.Chart($.extend(options2, getSettings2()));
}
/**
 * 绘制图表
 */
function drawChart3() {
	// 绘制图表
	categories3=[];
	chart3 = new Highcharts.Chart($.extend(options3, getSettings3()));
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
	var pic_type = "pie";
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
	settings.series =  data;
	return settings;
}

/**
 * 获取图表配置（动态）
 */
function getSettings1() {
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
	var pic_type = "pie";
	var data = loadData1( pic_type, 0, true);
	var settings = {};
	settings.title = {
		text : ""
	};
	settings.xAxis = [ {
		categories : categories1,
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
	settings.series =  data;
	return settings;
}

/**
 * 获取图表配置（动态）
 */
function getSettings2() {
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
	var pic_type = "pie";
	var data = loadData2( pic_type, 0, true);
	var settings = {};
	settings.title = {
		text : ""
	};
	settings.xAxis = [ {
		categories : categories2,
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
	settings.series =  data;
	return settings;
}

/**
 * 获取图表配置（动态）
 */
function getSettings3() {
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
	var pic_type = "pie";
	var data = loadData3( pic_type, 0, true);
	var settings = {};
	settings.title = {
		text : ""
	};
	settings.xAxis = [ {
		categories : categories3,
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
	settings.series =  data;
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
/*
function createXLastLevel(years) {
	var cols=[];
	for (var i = 0; i < years.length; i++) {
		cols.push({
			'year' : years[i] + "",
			'yearName' : "" + years[i] + "年"
		});
	}
	return cols;
}*/

function createXLastLevel(years) {
	var index_all = $("#index_x").combobox("getData");
	var cols=[];
	for (var i = 0; i < years.length; i++) {
		cols.push({
			'code' : years[i] + "",
			'value' : index_all[years[i]].value + ""
		});
	}
	return cols;
}
/**
 * 加载数据
 */
function loadData( chartType, yIndex, isInit) {
	var data=[];
	var queryParams = {
			"index_ys" :"2,8",
			"id":id
	};
	$.ajax({
		type : "post",
		async:false,
		data:queryParams,
		url :  path+'/generatorContrastFxYearController/queryData',
		success : function(msg) {
				data=$.parseJSON(msg).rows;
		}
	});
	// 筛选数据
	var selections = data;
	var type = "pie";// 图表类型
	
	var list = [];
	
	var m =[];
	var series = {};
	series.type = type;
		for (var i = 0, len = selections.length; i < len; i++) {
			var a = {'name':selections[i]['index_y_name'],'y':selections[i]['value']};
			list.push(a);
		}
		series.data=list;
		//list.push(series);

	 m.push(series);
	 return m;
}
/**
 * 加载数据
 */
function loadData1( chartType, yIndex, isInit) {
	var data=[];
	var queryParams = {
			"index_ys" :"3,4,5,6,7",
			"id":id
	};
	$.ajax({
		type : "post",
		async:false,
		data:queryParams,
		url :  path+'/generatorContrastFxYearController/queryData',
		success : function(msg) {
				data=$.parseJSON(msg).rows;
		}
	});
	// 筛选数据
	var selections = data;
	var type = "pie";// 图表类型
	
	var list = [];
	
	var m =[];
	var series = {};
	series.type = type;
		for (var i = 0, len = selections.length; i < len; i++) {
			var a = {'name':selections[i]['index_y_name'],'y':selections[i]['value']};
			list.push(a);
		}
		series.data=list;
		//list.push(series);

	 m.push(series);
	 return m;
}

/**
 * 加载数据
 */
function loadData2( chartType, yIndex, isInit) {
	var data=[];
	var queryParams = {
			"index_ys" :"9,10,11,12",
			"id":id
	};
	$.ajax({
		type : "post",
		async:false,
		data:queryParams,
		url :  path+'/generatorContrastFxYearController/queryData',
		success : function(msg) {
				data=$.parseJSON(msg).rows;
		}
	});
	// 筛选数据
	var selections = data;
	var type = "pie";// 图表类型
	
	var list = [];
	
	var m =[];
	var series = {};
	series.type = type;
		for (var i = 0, len = selections.length; i < len; i++) {
			var a = {'name':selections[i]['index_y_name'],'y':selections[i]['value']};
			list.push(a);
		}
		series.data=list;
		//list.push(series);

	 m.push(series);
	 return m;
}

/**
 * 加载数据
 */
function loadData3( chartType, yIndex, isInit) {
	var data=[];
	var queryParams = {
			"index_ys" :"3,4,5,6,7,9,10,11,12",
			"id":id
	};
	$.ajax({
		type : "post",
		async:false,
		data:queryParams,
		url :  path+'/generatorContrastFxYearController/queryData',
		success : function(msg) {
				data=$.parseJSON(msg).rows;
		}
	});
	// 筛选数据
	var selections = data;
	var type = "pie";// 图表类型
	
	var list = [];
	
	var m =[];
	var series = {};
	series.type = type;
		for (var i = 0, len = selections.length; i < len; i++) {
			var a = {'name':selections[i]['index_y_name'],'y':selections[i]['value']};
			list.push(a);
		}
		series.data=list;
		//list.push(series);

	 m.push(series);
	 return m;
}
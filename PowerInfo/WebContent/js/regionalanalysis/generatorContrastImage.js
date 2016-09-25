// 图表
var chart;
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
		url : path + "/export/exportImage",
		type:"image/png"
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
	$("#exportBtn").bind("click", function() {
		chart.exportChart();
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
	var pic_type = $("#pic_type").combo("getValue");
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
	var index_s = $("#index_x").combo("getValues").join(",");
	var index_y =  $("#index_y").combo("getValue");
	var queryParams = {
			"index_xs" : index_s,
			"index_ys" :index_y
	};
	$.ajax({
		type : "post",
		async:false,
		url :  path+'/generatorContrastController/queryData',
		data : queryParams,
		success : function(msg) {
				data=$.parseJSON(msg).rows;
		}
	});

	// 筛选数据
	var selections = data;
	var type = chartType;// 图表类型
	var xLastLevel = createXLastLevel($("#index_x").combobox("getValues"));
	var ylastField = 'dc_jz_name';
	var xlastField = null;
	var list = [];
	debugger;

	for (var i = 0, len = selections.length; i < len; i++) {
		var series = {};
		var picdata = [];
		series.name = selections[i][ylastField];
		series.type = type;
		// series.yAxis = yIndex;
		// 追加数据时，进行随机选色
		if (!isInit) {
			series.color = getRandomColor();
		}
		for (var j = 0, len2 = xLastLevel.length; j < len2; j++) {
			var slice = [];
			xlastField = xLastLevel[j]['code'];
			var shortname = areas.getValue('name', xLastLevel[j]['code'],
					'shortname')
					|| xLastLevel[j]['value'];
			slice.push(shortname);
			var num=Number(selections[i][xlastField]);
			if(isNaN(num)){
				num=0;
			}
			slice.push(num);
			picdata.push(slice);
			if (i == 0) {				
				categories.push(shortname);
			}
		}
		series.dataLabels = {
			enabled : true
		};
		series.data = picdata;
		list.push(series);
	}
	return list;
}
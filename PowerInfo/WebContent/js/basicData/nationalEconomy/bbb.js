// 图表
var chart;
var _$ = window.parent.frames["iframe0"].jQuery;
var areas = [];

// 图表固定配置
var options = {
	chart: {
		renderTo: 'container'
	},
	credits: {
		enabled: false
	},
	tooltip: {
		crosshairs: true,
		borderRadius: 5,
		borderColor: '#00B997',
	    shared: true,
	    formatter: function() {
			if (this.x) {
				var s = '<b>'+ this.x + '</b>';
				if(this.points){
					$.each(this.points, function(i, point) {
						s += '<br/><span style="color:' + point.series.color + '">' + point.series.name +'</span>: <b>'+ point.y +'</b>';
					});
				}else{
						s += '<br/><span style="color:' + this.series.color + '">' + this.series.name +'</span>: <b>'+ this.y +'</b>';
				}
				
				return s;
			} else {
				return '<b>'+ this.y + '</b>';
			}
    	}
	},
	exporting: {
		enabled:false,
		url: path + "/chartExport"
    },
    plotOptions: {
    	column: {
	        pointPadding: 0.2,
	        borderWidth: 0
	    },
	    pie: {
	      allowPointSelect: true,
	      cursor: 'pointer',
	      showInLegend: true,                                          
	      dataLabels: {
	    	  formatter: function() {
	    		return Number(this.percentage).toFixed(2) + "%";
	    	  },
	          enabled: true
	      }
	    }
    }
};
var categories = [];
$(function() {
	
	var enableDataLabels = false;
	drawChart();
});

/**
 * 绘制图表
 */
function drawChart() {
	// 绘制图表
	chart = new Highcharts.Chart($.extend(options, getSettings()));
}

/**
* 获取图表配置（动态）
*/
function getSettings() {
	// 解决导出时batik 不支持 css3 rgba属性的问题
	Highcharts.wrap(Highcharts.Chart.prototype, 'getSVG', function (proceed) {
	    return proceed.call(this)
	        .replace(
	            /(fill|stroke)="rgba\(([ 0-9]+,[ 0-9]+,[ 0-9]+),([ 0-9\.]+)\)"/g, 
	            '$1="rgb($2)" $1-opacity="$3"'
	    );
	});
		
	var data = loadData(_$('#datagrid'), 'column', 0, true);
	var settings = {};
	settings.title = {
		text: ""
	};
	
	settings.xAxis = [{
		categories: categories,
		plotLines: []
	}];
	settings.yAxis = [{
		min:0,
		labels: {
			formatter: function () {
            	return this.value;
          	}
		},
		plotLines: [],
		stackLabels: {
            enabled: true,
            style: {
                fontWeight: 'bold'
            }
        },
        title: {
        	rotation: 0,
        	text: ""
        }
	}];
//	settings.chart={
//         type: 'column'
//     };
    /* settings.series = [{
        name: '人口',
        data: [49.9, 71.5, 106.44]

    }, {
        name: '面积',
        data: [83.6, 78.8, 98.5]

    }, {
        name: 'GDP',
        data: [48.9, 38.8, 39.3]

    }];*/
	settings.series = data;
	return settings;
}
/**
 *  取数组中resKey = resVal的对象的retKey的值
 *  @param resKey 数组中某一对象的key
 *  @param resVal 数组中某一对象的value
 *  @param retKey 数组中该对象的其它key
 *  @return 返回匹配的对象的key对应的值
 */
Array.prototype.getValue = function(resKey, resVal, retKey) {
	for(var i = 0; i < this.length; i++) {
		for(var j in this[i]) {
			if(j == resKey && this[i][j] == resVal) {
				return this[i][retKey];
			}
		}
	}
}

/**
* 加载数据
*/
function loadData($datagrid, chartType, yIndex, isInit) {
	// 筛选数据
	debugger;
	var selections = $datagrid.datagrid('getRows');
	var type = chartType;//图表类型
	var xLastLevel =window.parent.frames["iframe0"].cols[0];
	var frozon = $datagrid.datagrid('getColumnFields',true);
	var ylastField = frozon.pop();
	var xlastField = null;
	var list = [];
	
	for (var i = 0,len = selections.length; i < len; i++) {
		var series = {};
		var data = [];
		series.name = selections[i][ylastField];
		series.type = type;
		//series.yAxis = yIndex;
		// 追加数据时，进行随机选色
		if (!isInit) {
			series.color = getRandomColor();
		}
		for (var j = 0, len2 = xLastLevel.length; j < len2; j++) {
			var slice = [];
			xlastField = xLastLevel[j]['field'];
			var shortname = areas.getValue('name', xLastLevel[j]['title'], 'shortname') || xLastLevel[j]['title'];
			slice.push(shortname);
			slice.push(Number(selections[i][xlastField]));
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
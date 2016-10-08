$(function() {
	tabClose();
	tabCloseEven();
	var keyValue={"basic":1,"totalQuantity":4,"balance":157,"station":168};
	//$('#css3menu').css("margin-left", $('#logo1').width());
	
	
	$('#css3menu a').click(function() {
		$('#mm-tabcloseall').click();
		$('#css3menu a').removeClass('active');
		$(this).addClass('active');
		$("#css3menu a").css("color","black");
		var d = $(this).attr('name') ;
		$(this).css("color","red");
		addNav(keyValue[d]);
		InitLeftMenu();
	});

	// 导航菜单绑定初始化
	var firstMenuName = $('#css3menu a:eq('+flag+')').attr('name');
	$('#css3menu a:eq('+flag+')').css("color","red");//.css("background","#3385FF");
	addNav(keyValue[firstMenuName]);
	InitLeftMenu();
	
	 openPwd();

     $('#editpass').click(function() {
         $('#w').window('open');
     });

     $('#btnEp').click(function() {
         serverLogin();
     });

		$('#btnCancel').click(function(){closePwd();});

     $('#loginOut').click(function() {
         $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {

             if (r) {
                 location.href = path +'/loginout';
             }
         });
     });
});
function getAccordion(param) {
	$.ajax({
		type : 'POST',
		async : false,
		data:{"param":param},
		dataType : 'json',
		url : '/PowerInfo/menu/queryAccordion',
		success : function(data) {
			_menus = data;

		}
	});
}

function getMenu() {
	$.ajax({
		type : 'POST',
		async : false,
		dataType : 'json',
		url : '/PowerInfo/menu/queryMenu',
		success : function(data) {
			_menus = data;

		}
	});
}


function addNav(data) {

	/** 初始化树* */
	InitTreeData(data);

}
/**
 * 基础数据删除节点
 */
function remove() {
	$.messager.confirm('提示', '确认删除该节点?', function(r) {
		if (r) {
			var node = $('#tt').tree('getSelected');

			$.post(path + '/basicData/removeleaf', {
				"id" : node.id
			}, function(data) {
				var data = $.parseJSON(data);
				if (data.flag == '1') {
					$.messager.alert('提示', '删除成功！', 'info', function() {
						$('#tt').tree("remove", node.target);
					});
				}
			});
		}
	});

}
/**
 * 基础数据增加年份
 */
function addyear() {
	commonHelper.toAdd({
		title : '增加年份',
		width : 260,
		height : 180,
		url : "basicData/openAddYear"
	});

}

/** 初始化树结构* */
function InitTreeData(data) {
	$('#tt').tree(
			{
				url : 'menu/queryMenu?pid='+data,
				onClick : function(node) {
					if (node.attributes.url != "") {
						addTab(node.text, path + node.attributes.url + "?pid="
								+ node.id, "static/images/top_03.png");
					}

				},
				onContextMenu : function(e, node) {
					//增加年份是基础数据库才可以添加
					if(node.id==1){
						$("#addyear").show();
					}else{
						$("#addyear").hide();
					}
					e.preventDefault();
					// 选择节点
					$('#tt').tree('select', node.target);
					// 显示上下文菜单
					$('#treemm').menu("show", {
						left : e.pageX,
						top : e.pageY
					});
				},onLoadSuccess:function(){
					var rooNode = $("#tt").tree('getRoot');
					$("#tt").tree("expand",rooNode.target);  
				}
				

			});
}
function update() {

	commonHelper.toAdd({
		title : '修改指标',
		width : 400,
		height : 300,
		url : "basicData/openUpdateLeaf"
	});

}
function append() {
	debugger;
	commonHelper.toAdd({
		title : '增加指标',
		width : 400,
		height : 300,
		url : "basicData/openAddLeaf"
	});

}

// 初始化左侧
function InitLeftMenu() {

	//hoverMenuItem();

	$('#tt li a').live('click', function() {
		var tabTitle = $(this).children('.nav').text();

		var url = $(this).attr("rel");
		var menuid = $(this).attr("ref");
		var icon = getIcon(menuid, icon);

		addTab(tabTitle, url, icon);
		$('#tt li div').removeClass("selected");
		$(this).parent().addClass("selected");
	});

}

/**
 * 菜单项鼠标Hover
 */
function hoverMenuItem() {
	$(".easyui-accordion").find('a').hover(function() {
		$(this).parent().addClass("hover");
	}, function() {
		$(this).parent().removeClass("hover");
	});
}

// 获取左侧导航的图标
function getIcon(menuid) {
	var icon = 'icon ';
	$.each(_menus, function(i, n) {
		$.each(n, function(j, o) {
			$.each(o.menus, function(k, m) {
				if (m.menuid == menuid) {
					icon += m.icon;
					return false;
				}
			});
		});
	});
	return icon;
}

function addTab(subtitle, url, icon) {
	if (!$('#tabs').tabs('exists', subtitle)) {
		$('#tabs').tabs('add', {
			title : subtitle,
			content : createFrame(url),
			closable : true,
			icon : icon
		});
	} else {
		$('#tabs').tabs('select', subtitle);
		$('#mm-tabupdate').click();
	}
	tabClose();
}

function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
			+ '" style="width:100%;height:98%;"></iframe>';
	return s;
}

function tabClose() {
	/* 双击关闭TAB选项卡 */
	$(".tabs-inner").dblclick(function() {
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close', subtitle);
	});
	/* 为选项卡绑定右键 */
	$(".tabs-inner").bind('contextmenu', function(e) {
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});

		var subtitle = $(this).children(".tabs-closable").text();

		$('#mm').data("currtab", subtitle);
		$('#tabs').tabs('select', subtitle);
		return false;
	});
}

function closeSingleExtent(subtitle){
	$('#tabs').tabs('close', subtitle);
}
// 绑定右键菜单事件
function tabCloseEven() {
	// 刷新
	$('#mm-tabupdate').click(function() {
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		$('#tabs').tabs('update', {
			tab : currTab,
			options : {
				content : createFrame(url)
			}
		});
	});
	// 关闭当前
	$('#mm-tabclose').click(function() {
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close', currtab_title);
	});
	// 全部关闭
	$('#mm-tabcloseall').click(function() {
		$('.tabs-inner span').each(function(i, n) {
			var t = $(n).text();
			$('#tabs').tabs('close', t);
		});
	});
	// 关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function() {
		$('#mm-tabcloseright').click();
		$('#mm-tabcloseleft').click();
	});
	// 关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function() {
		var nextall = $('.tabs-selected').nextAll();
		if (nextall.length == 0) {
			// msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});
	// 关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function() {
		var prevall = $('.tabs-selected').prevAll();
		if (prevall.length == 0) {
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});

	// 退出
	$("#mm-exit").click(function() {
		$('#mm').menu('hide');
	});
}

// 弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

// 本地时钟
function clockon() {
	var now = new Date();
	var year = now.getFullYear(); // getFullYear getYear
	var month = now.getMonth();
	var date = now.getDate();
	var day = now.getDay();
	var hour = now.getHours();
	var minu = now.getMinutes();
	var sec = now.getSeconds();
	var week;
	month = month + 1;
	if (month < 10)
		month = "0" + month;
	if (date < 10)
		date = "0" + date;
	if (hour < 10)
		hour = "0" + hour;
	if (minu < 10)
		minu = "0" + minu;
	if (sec < 10)
		sec = "0" + sec;
	var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
	week = arr_week[day];
	var time = "";
	time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu
			+ ":" + sec + " " + week;

	$("#bgclock").html(time);

	var timer = setTimeout("clockon()", 200);
}
//设置登录窗口
function openPwd() {
    $('#w').window({
        title: '修改密码',
        width: 300,
        modal: true,
        shadow: true,
        closed: true,
        height: 160,
        resizable:false
    });
}
//关闭登录窗口
function closePwd() {
    $('#w').window('close');
}

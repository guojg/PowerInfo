//common_page类
//function common_page_ext(){
//	//扩展属性
//	this.level = "extClass";
//	//扩展方法
//	methods = {
//		doHobby: function(){ console.log( this.level + "'s hobby is Glof"); },
//		run: function(){ console.log( this.level + " running is fast!" ); }
//	};
//	statics = {
//		_secret: function(){ console.log("this Father's Class statics methods");}
//	};
//	return common_page.extend(common_page_ext, methods, statics);
//}

//datagrid---查询条件
function hide_show(id,datagrid_id){
    //点击的a标签初始状态
    if(id =="less"){
    	$("#less").css("display","block");
    	$("#more").css("display","none");
    	$("#search_tbl .hidetr").each(function(){
			$(this).show();
		});
    }else{
    	$("#less").css("display","none");
    	$("#more").css("display","block");
    	$("#search_tbl .hidetr").each(function(){
			$(this).hide();
		});
    }
	var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_height = $("#datagrid_note").height();
	var height= Height_Page-datagrid_title_height-datagrid_note_height-5;
	//加载datagrid
	if(datagrid_id!=null && datagrid_id!=""){
		$('#'+datagrid_id).datagrid('resize',{
			height: height
		});
	}else{
		$('#datagrid').datagrid('resize',{
			height: height
		});
	}
	
}
//非layout-note-注释
function hide_show_note(id,datagrid_id){
    //点击的a标签初始状态
    if(id =="less_note"){
    	$("#less_note").css("display","block");
    	$("#more_note").css("display","none");
    	$("#datagrid_note ul").last().show();
    }else{
    	$("#less_note").css("display","none");
    	$("#more_note").css("display","block");
    	$("#datagrid_note ul").last().hide();
    }
	var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_height = $("#datagrid_note").height();
	var height= Height_Page-datagrid_title_height-datagrid_note_height-5;
	//加载datagrid
	if(datagrid_id!=null && datagrid_id!=""){
		$('#'+datagrid_id).datagrid('resize',{
			height: height
		});
	}else{
		$('#datagrid').datagrid('resize',{
			height: height
		});
	}
	
}
//datagrid--layout---查询条件
function hide_show_layoutg(id,datagrid_id){
    //点击的a标签初始状态
    if(id =="less"){
    	$("#less").css("display","block");
    	$("#more").css("display","none");
    	$("#search_tbl .hidetr").each(function(){
			$(this).show();
		});
    }else{
    	$("#less").css("display","none");
    	$("#more").css("display","block");
    	$("#search_tbl .hidetr").each(function(){
			$(this).hide();
		});
    }
	var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_height = $("#datagrid_note").height();
	var height= Height_Page-datagrid_title_height-datagrid_note_height-5;
	//加载datagrid
	//加载布局
	$("#datagrid_div").layout('resize',{
		width: $("#body").width(),
		height: height
	});
	$('#'+datagrid_id).datagrid('resize',{
		height: height
	});
}
//layout-note-注释
function hide_show_layoutn(id,datagrid_id){
    //点击的a标签初始状态
    if(id =="less_note"){
    	$("#less_note").css("display","block");
    	$("#more_note").css("display","none");
    	$("#datagrid_note ul").last().show();
    }else{
    	$("#less_note").css("display","none");
    	$("#more_note").css("display","block");
    	$("#datagrid_note ul").last().hide();
    }
	var Height_Page = $("html").height();
	var datagrid_title_height = $("#datagrid_div").position().top;
	var datagrid_note_height = $("#datagrid_note").height();
	var height= Height_Page-datagrid_title_height-datagrid_note_height-5;
	//加载datagrid
	//加载布局
	$("#datagrid_div").layout('resize',{
		width: $("#body").width(),
		height: height
	});
	$('#datagrid_div').layout('collapse','east');
	$('#'+datagrid_id).datagrid('resize',{
		height: height
	});
}

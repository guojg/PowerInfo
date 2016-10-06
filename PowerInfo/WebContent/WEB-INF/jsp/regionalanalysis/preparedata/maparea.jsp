<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>区域</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp" %>
<script type="text/javascript" src="<%=path%>/static/js/map/lib/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/map/lib/raphael.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/map/svg_data/chinaMapConfig.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/map/map_new.js"></script>

<style type="text/css">
/* 提示自定义 */
.stateTip, #StateTip{display:none; position:absolute; padding:8px; background:#fff; border:2px solid #2385B1; -moz-border-radius: 4px; -webkit-border-radius: 4px; border-radius: 4px; font-size:12px; font-family:Tahoma; color:#333;}

#ChinaMap {
	padding-left: 10px; 
	padding-bottom: 10px;
	margin: 0px auto;
	padding-top: 5px;
	position: relative;
	text-align: center;
}
#map {
	margin: 0px auto;
}
#tiplayer {
	z-index: 1000;
	min-height: 2em;
	background: #000;
	font: 12px 'Microsoft YaHei',Arial,宋体,Tahoma,Sans-Serif;
	color: #fff;	
	text-align: left;
	word-wrap: break-word;
	-moz-border-radius: 3px;
	-khtml-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
}
#tiplayergw {
	background: #6AAD92;
	color: #000;
	border-radius: 3px;
	float: right;
}

</style>
<script type="text/javascript">

$(function(){
	$('#map').SVGMap({
		mapName: 'china',
		mapWidth: 700,
		mapHeight: 450
	});
	/*var R = Raphael('map',600,500);
	printMap(R);*/
	
});
	
	
</script>
</head>
<body>
		 <div id="map"></div>  
	

</body>
</html>
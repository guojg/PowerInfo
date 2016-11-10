<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>区域</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<!--引入此文件包含jquery_easyui的css样式与公用js以及登录用户信息-->
<%@include file="../../common/commonInclude.jsp"%>
<%@include file="../../common/commonDefineBtn.jsp" %>

<script type="text/javascript" src="<%=path%>/static/js/map/lib/raphael.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/map/chinamapPath.js"></script>

<style type="text/css">
.demo{width:760px; height:500px; margin:30px auto 0 auto; font-size:14px;}
.demo p{line-height:30px}
</style>
</head>

<body>

<div id="main">
   <div class="demo">
   		<div id="map"></div>
   		
   </div>
	
</div>

<script type="text/javascript">
window.onload = function () {
    var R = Raphael("map", 600, 500);
	//调用绘制地图方法
    paintMap(R);
	
	var textAttr = {
        "fill": "#000",
        "font-size": "12px",
        "cursor": "pointer"
    };
			
           
    for (var state in china) {
		china[state]['path'].color = Raphael.getColor(0.9);
        (function (st, state) {
			
			//获取当前图形的中心坐标
            var xx = st.getBBox().x + (st.getBBox().width / 2);
            var yy = st.getBBox().y + (st.getBBox().height / 2);
			
            //***修改部分地图文字偏移坐标
            switch (china[state]['name']) {
                case "江苏":
                    xx += 5;
                    yy -= 10;
                    break;
                case "河北":
                    xx -= 10;
                    yy += 20;
                    break;
                case "天津":
                    xx += 10;
                    yy += 10;
                    break;
                case "上海":
                    xx += 10;
                    break;
                case "广东":
                    yy -= 10;
                    break;
                case "澳门":
                    yy += 10;
                    break;
                case "香港":
                    xx += 20;
                    yy += 5;
                    break;
                case "甘肃":
                    xx -= 40;
                    yy -= 30;
                    break;
                case "陕西":
                    xx += 5;
                    yy += 10;
                    break;
                case "内蒙古":
                    xx -= 15;
                    yy += 65;
                    break;
                default:
            }
			//写入文字
			china[state]['text'] = R.text(xx, yy, china[state]['name']).attr(textAttr).click(function () {
            	clickMap(china[state]['name']);

			});
			
			st[0].onmouseover = function () {
                st.animate({fill: st.color, stroke: "#eee"}, 500);
				china[state]['text'].toFront();
                R.safari();
            };
            st[0].onmouseout = function () {
                st.animate({fill: "#97d6f5", stroke: "#eee"}, 500);
				china[state]['text'].toFront();
                R.safari();
            };
       	 //写入地名,并加点击事件,部分区域太小，增加对文字的点击事件
			st[0].onclick =function(){
            	clickMap(china[state]['name']);
			};	
			
					
         })(china[state]['path'], state);
    }
    
    
}
	function clickMap(selectName){
								var orgCode = "";
								switch(selectName){
									case '黑龙江':
										orgCode = "12321";
										break;
									case '吉林':
										orgCode = "12222";
										break;
									case '辽宁':
										orgCode = "12123";
										break; 
									case '河北':
										orgCode = "11213";
										break;
									case  '山东':
										orgCode = "11417";
										break; 
									case '江苏':
										orgCode = "13232";
										break; 
									case '浙江':
										orgCode = "13333";
										break;
									case  '安徽':
										orgCode = "13434";
										break; 
									case  '河南':
										orgCode = "14141";
										break;
									case  '山西':
										orgCode = "11314";
										break; 
									case  '陕西':
										orgCode = "15161";
										break; 
									case '甘肃':
										orgCode = "15262";
										break; 
									case '湖北':
										orgCode = "14142";
										break;
									case '江西':
										orgCode = "14144";
										break; 
									case '福建':
										orgCode = "13537";
										break; 
									case '湖南':
										orgCode = "14143";
										break; 
									case '贵州':
										orgCode = "";
										break; 
									case '四川':
										orgCode = "14246";
										break; 
									case '云南':
										orgCode = "";
										break; 
									case '青海':
										orgCode = "15463";
										break; 
									case '海南':
										orgCode = "";
										break; 
									case '上海':
										orgCode = "13135";
										break; 
									case '重庆':
										orgCode = "14247";
										break; 
									case '天津':
										orgCode = "11116";
										break; 
									case '北京':
										orgCode = "11111";
										break; 
									case '宁夏':
										orgCode = "15364";
										break; 
									case '内蒙古':
										orgCode = "12424";
										break; 
									case '广西':
										orgCode = "";
										break; 
									case '新疆':
										orgCode = "15565";
										break; 
									case '西藏':
										orgCode = "16181";
										break; 
									case  '广东':
										orgCode = "";
										break; 
									case '香港':
										orgCode = "";
										break; 
									case '澳门':
										orgCode = "";
										break;
									
								}
								$.ajax({
									type : 'POST',
									async : false,
									url : path +'/mapController/setSession?organCode=' + orgCode,
									success : function(data) {
										
									}
								});
								window.parent.addNav(189);
								
							
								//window.location.href = path+'/mapController/setSession?organCode=' + orgCode;

							}
</script>

</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Date"%>
<%

String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path + "/";

   
%>
<!--------------------------------------easyui公共style样式------------------------------------------>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/js/jquery-easyui-1.4/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/js/jquery-easyui-1.4/themes/icon.css" />
<!--------------------------------------列表布局style样式------------------------------------------>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/commonStyle.css" />

<!------------引用js,jquery版本过高会导致兼容性问题，所以用jquery-1.8.3.js取代jquery.easyui.min.js------------>
<script type="text/javascript" src="<%=path%>/static/js/jquery-easyui-1.4/jquery-1.8.3.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<!------------jqueryeasyui扩展,导出表格，合并表格，验证框架，英文转汉语------------>
<script type="text/javascript" src="<%=path%>/static/js/jquery-easyui-1.4/ext/jquery.datagrid.export-ext.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-easyui-1.4/ext/jquery.datagrid.megincell-ext.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-easyui-1.4/ext/jquery.validatebox-ext.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>


<!-------字典表加载---------------列表公用js---------------增，删，改，查，保存，可编辑表格基本操作。-----弹出模式窗口调用--------->
<script type="text/javascript" src="<%=path%>/static/js/common/common_page.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/common/common_page_ext.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/common/apppub.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/common/exportExcel.js"></script>
<script type="text/javascript" src="<%=path%>/static/common/map.js"></script>
<script type="text/javascript" src="<%=path%>/static/common/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/common/common_dictionary.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jtip/js/tooltip.js"></script>
<link rel="stylesheet" href="<%=path%>/static/js/jtip/css/tooltip.css" type="text/css" />
<script type="text/javascript">
	var path="<%=path%>";
</script>






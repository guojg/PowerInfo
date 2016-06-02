<%@ page language="java" pageEncoding="UTF-8"%>

<div id="btn_div">
	<a id="tool_query">
		<img src='<%=commonPath%>/resources/images/query.gif' align='top' border='0' title='查询'/>
	</a>
	<a id="tool_create">
		<img src='<%=commonPath%>/resources/images/new.gif' align='top' border='0' title='新增' />
	</a>
	<a id="tool_edit">  
		<img src='<%=commonPath%>/resources/images/xiugai.gif' align='top' border='0' title='修改' />
	</a>
	<a id="tool_delete" >
		<img src='<%=commonPath%>/resources/images/delete.gif' align='top' border='0' title='删除' />
	</a>
	<a id="tool_export">
		<img src='<%=commonPath%>/resources/images/daochu.gif' align='top' border='0' title='导出' />
	</a>
	
</div>

<div id="win_div" class="easyui-window" data-options="closed:true" style="width:0px;height:0px;">
	<iframe id="win_src"  frameborder="0" src="" style="width: 100%;height: 100%;scrolling:no;"></iframe>
</div>


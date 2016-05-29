//带返回值的模态窗口
function openWindowModal(url,wWith,wHeight){   
	 var x1=Math.round((screen.availWidth - wWith)/2);
	 var y1=Math.round((screen.availHeight - wHeight)/2);	 
	 var returnVs= window.showModalDialog(url, window, 'dialogWidth:' + wWith + 'px;dialogHeight:' + wHeight + 'px;dialogTop:'+y1+';center:yes;help:no;resizable:no;scroll:no;status:no;unadorned:no;');
     return returnVs;
} 
//打开最大化窗口(yesscroll)
function openMaxWindow(url,name){
	var newWindow = window.open('about:blank','_blank','toolbar=no,menubar=no,status=yes,resizable=yes,scrollbars=yes');
	newWindow.moveTo(0,0);
	newWindow.resizeTo(screen.availWidth,screen.availHeight);
	newWindow.location = url;
	return newWindow;
}

//打开最大化窗口(noscroll)
function openMaxWindowWithNoScroll(url){
	var newWindow = window.open('about:blank','_blank','toolbar=no,menubar=no,status=yes,resizable=yes,scrollbars=no');
	newWindow.moveTo(0,0);
	newWindow.resizeTo(screen.availWidth,screen.availHeight);
	newWindow.location = url;
	return newWindow;
}

//打开窗口处于中央没有滚动条
function openCenterWindow(URLStr, width, height){
  var left = (screen.width/2) - width/2;
  var top = (screen.height/2) - height/2;
  if(top>30){
	  top = top -20;
  }
  var styleStr = 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbar=no,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+',top='+top+',screenX='+left+',screenY='+top;
  var msgWindow = window.open(URLStr,"msgWindow", styleStr);
  return msgWindow;
}
//打开窗口处于中央有滚动条
function openCenterWindowWithScroll(URLStr, width, height){
  var left = (screen.width/2) - width/2;
  var top = (screen.height/2) - height/2;
  if(top>30){
	  top = top -20;
  }
  var styleStr = 'directories=no,location=no,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no,copyhistory=yes,width='+width+',height='+height+',left='+left+',top='+top+',screenX='+left+',screenY='+top;
  var msgWindow = window.open(URLStr,"msgWindow", styleStr);
  return msgWindow;
}









//获取应用路径
function openPostWindow(url, data, name){  
	var tempForm = document.createElement("form");  
	tempForm.id="tempForm1";  
	tempForm.method="post";  
	tempForm.action=url;  
	tempForm.target=name;  
	var hideInput = document.createElement("input");  
	hideInput.type="hidden";  
	hideInput.name= "organidsAndtypes"
	hideInput.value= data;
	tempForm.appendChild(hideInput);   
	tempForm.attachEvent("onsubmit",function(){ openWindow(name); });
	document.body.appendChild(tempForm);  
	tempForm.fireEvent("onsubmit");
	tempForm.submit();
	document.body.removeChild(tempForm);
}
//post传递参数---//增加窗口大小控制
function openPostWindowWithSize(url, data,dataname,width,height,name){  
	var tempForm = document.createElement("form");  
	tempForm.id="tempForm1";  
	tempForm.method="post";  
	tempForm.action=url;  
	tempForm.target=name;  
	var hideInput = document.createElement("input");  
	hideInput.type="hidden";  
	hideInput.name= dataname;
	hideInput.value= data;
	tempForm.appendChild(hideInput);   
	tempForm.attachEvent("onsubmit",function(){ openWindowWithSize(name,width,height); });
	document.body.appendChild(tempForm);  
	tempForm.fireEvent("onsubmit");
	tempForm.submit();
	document.body.removeChild(tempForm);
}

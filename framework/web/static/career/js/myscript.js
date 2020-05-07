//页面结构，异步调用
//$('.header-login-wrap').load('global.html .header-login-inner');
//$('.footer-wrap').load('global.html .footer-inner');

//取消链接点击外部虚线框
$('a').attr('hideFocus','true');


var cre_contextPath="";
var isBuy=false;
function buyVip(){
	if(isBuy){
		return;
	}
	isBuy=true;
	var url=cre_contextPath+"/careerPlanning/vippayindex/page";
	//window.location.href=url; 
	window.open(url);
	//弹出提示框
	//showSuccessMsg('#tipsLayer','.close','提示');
	isBuy=false;
}

function backCareer(){
	var url=cre_contextPath+"/careerPlanning/careerplanIndex/page";
	window.location.href=url; 
}


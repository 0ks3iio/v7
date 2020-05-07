/**
 * 
 * @param contextPath 必须
 * @param containerName 必须
 * @param label 必须
 * @param parameters	
 * @param width	例如100%或者200px
 * @param height	例如100%或者200px
 * @param iframeId	用于区分，若同一个页面有多个图表
 */
function loadChartsDiv(contextPath,containerName,label,parameters,width,height,iframeId){
	if(!parameters){
		parameters = "";
	}
	if(!width){
		width = "100%";
	}
	if(!height){
		height = "600px";
	}
	if(!iframeId){
		iframeId = "myframe";
	}
	var url=contextPath+"/basedata/charts/page?label="+label+"&parameters="+encodeURIComponent(parameters)+"&width="+encodeURIComponent(width)
		+"&height="+encodeURIComponent(height)+"&iframeId="+iframeId;
	$(containerName).load(url);
}
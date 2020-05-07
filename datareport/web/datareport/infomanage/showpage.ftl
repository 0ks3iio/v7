<div id="dataInfoManageDiv">
</div>
<form id="dataform" name="dataform" method="post">
 	<input type="hidden" name="downPath" id="downPath"/>
</form>
<script>
	$(function(){
		showInfoListHead();
	});
	
	function showInfoListHead() {
		var url = "${request.contextPath}/datareport/infomanage/showinfolisthead";
		$("#dataInfoManageDiv").load(url);
	}
	
	var loadTime;

	function layerTime(title) {
		var width = ($(window).width() - 210)/2+120;
		var height = $(window).height()/2-32;
		loadTime = layer.msg(title, {
  			icon: 16,
  			shade: 0.01,
  			time: 60*1000,
  			offset: [height+'px', width+'px']
		});
	}

	function layerClose() {
		layer.close(loadTime);
	}
	
	function loadExcel(objId,objectType) {
		location.href = '${request.contextPath}/datareport/table/loadexceltable?objId='+objId+"&objectType="+objectType;
	}
	
	function printPDF(infoId,taskId,type,tableType) {
		layerTime("加载中");
		$.ajax({
			url:'${request.contextPath}/datareport/table/printPDF',
			type:'post',
			data:{'infoId':infoId,'taskId':taskId,'type':type,'tableType':tableType},
			success:function(data) {
				if(data!=""){
					$("#downPath").val(data);
					exportPdf(infoId,taskId,type);
				} else {
					layerClose();
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function exportPdf(infoId,taskId,type){
		var carplanform = document.getElementById('dataform');
		if(carplanform){
			carplanform.action="${request.contextPath}/datareport/table/downPDF?infoId="+infoId+"&taskId="+taskId+"&type="+type;
			carplanform.submit();
		}
		layerClose();
	}
	
	function printExcel(infoId,taskId,tableType,type,objthis) {
		layerTime("加载中");
		$.ajax({
			url:'${request.contextPath}/datareport/table/exportexcel',
			type:'post',
			data:{'infoId':infoId,'taskId':taskId,'tableType':tableType,'type':type},
			dataType : 'json',
			success:function(data) {
				layerClose();
				if(data.success){
					if (type == "1") {
						$(objthis).attr("onClick","loadExcel("+taskId+",'REPORT_TASK_ATT')");
					} else {
						$(objthis).attr("onClick","loadExcel("+infoId+",'REPORT_INFO_STATS')");
					}
					if (type == "1") {
						loadExcel(taskId,'REPORT_TASK_ATT');
					} else {
						loadExcel(infoId,'REPORT_INFO_STATS');
					}
				} else {
					layerTipMsg(data.success,"失败",data.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	<#-- 字数限制  -->
    function limitWords(objthis){
		var max = $(objthis).attr('maxlength');
		if(objthis.value.length+1 > max){
			layer.tips('最好在'+max+'个字数内哦', objthis, {
				tips: [1, '#000'],
				time: 2000
			})
		}
	}
</script>
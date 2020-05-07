<#--<a id="backA" onclick="goBack();" style="display: none" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
    <div class="box-body" id="studentContent">
        <div class="filter" id="searchDiv">
            <div class="filter-item">
                <span class="filter-name">学生姓名：</span>
                <div class="filter-content">
                    <input type="text" id="studentName" style="width:168px;" class="form-control" onkeypress="if(event.keyCode==13){searchStudent();}" >
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">身份证号：</span>
                <div class="filter-content">
                    <input type="text" id="idCard" style="width:168px;" class="form-control" onkeypress="if(event.keyCode==13){searchStudent();}" >
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">学号：</span>
                <div class="filter-content">
                    <input type="text" id="studentCode" style="width:168px;" class="form-control" onkeypress="if(event.keyCode==13){searchStudent();}" >
                </div>
            </div>
            <div class="filter-item filter-item-left">
                <div class="filter-content">
                    <a class="btn btn-blue" onclick="searchStudent();" >查找</a>
                </div>
            </div>
        </div>
        <div class="text-right" id="btnDivId">
			<button class="btn btn-sm btn-waterblue" onClick="pdfBackList();" style="margin-bottom:5px;margin-right:10px;">返回</button>
		</div>
        <div id="showList" >
        </div>
        <div id="showPdf">
        </div>
    </div>
</div>
<script type="text/javascript" src="${request.contextPath}/studevelop/js/pdfobject.min.js"></script>
<script type="text/javascript" >
	$(function(){
		$("#btnDivId").hide();	
		$("#showPdf").hide();	
	});
	function pdfBackList(){
		$("#btnDivId").hide();
		$("#showPdf").hide();
		$("#showList").show();
	}
	function showPdf(studentId,acadyear,semester){
	if(!("ActiveXObject" in window)&&PDFObject.supportsPDFs){//支持在线查看的  目前：chrome,安转pdf阅读器后，IE8-11、firefox也行
		var url = '';
		url = "${request.contextPath}/studevelop/stuQualityReport/getPdf?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId;
		$("#showPdf").attr('style',"height:700px");
		PDFObject.embed(url, "#showPdf");
		$("#showList").hide();
		$("#btnDivId").show();
		$("#showPdf").show();
		return;
	}
}
    function　searchStudent(){
        var studentName = $.trim($('#studentName').val());
        if(studentName != ''){
            if(studentName.indexOf('\'')>-1||studentName.indexOf('%')>-1){
                layerTipMsgWarn("请确认欲查询的学生姓名不包含单引号、百分号等特殊符号！");
                return ;
            }
        }
        var idCard = $.trim($('#idCard').val());
        if(idCard != ''){
            if(idCard.indexOf('\'')>-1||idCard.indexOf('%')>-1){
                layerTipMsgWarn("请确认欲查询的身份证号不包含单引号、百分号等特殊符号！");
                return ;
            }
        }
        var studentCode = $.trim($('#studentCode').val());
        if(studentCode != ''){
            if(studentCode.indexOf('\'')>-1||studentCode.indexOf('%')>-1){
                layerTipMsgWarn("请确认欲查询的学号不包含单引号、百分号等特殊符号！");
                return ;
            }
        }
        if(studentName=='' && idCard=='' && studentCode==''){
        	layerTipMsgWarn("查询条件不能为空，请先维护！");
            return ;
        }
        var url="${request.contextPath}/studevelop/historyReport/list?&studentName="+studentName+ "&idCard="+idCard+"&studentCode="+studentCode;
        $("#showList").load(encodeURI(url));
    }

    function goBack(){
        $("#backA").hide();
        $("#searchDiv").show();
        searchList();
    }
</script>
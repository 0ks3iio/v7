<#assign DEPLOY_REGION_BINJIANG = stack.findValue("@net.zdsoft.studevelop.data.constant.StuDevelopConstant@DEPLOY_BINJIANG") >
<script src="${request.contextPath}/static/js/LodopFuncs.js"></script>
	   <div class="box box-default" >
	      <div class="box-body">
		  <#-- PAGE CONTENT BEGINS -->
				<div class="filter clearfix">
					<div class="filter-item">
						<label for="" class="filter-name">学年：</label>
							<div class="filter-content">
								<select class="form-control" id="acadyear" name="acadyear" onChange="searchByAcadyear()">
									<#if acadyearList?exists && (acadyearList?size>0)>
					                    <#list acadyearList as item>
						                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
					                    </#list>
				                    <#else>
					                    <option value="">未设置</option>
				                     </#if>
								</select>
							</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">学期：</label>
							<div class="filter-content">
								<select class="form-control" id="semester" name="semester" onChange="searchBySemester()">
									${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
								</select>
							</div>
					</div>
					<#if !isOne?default(false)>
						<div class="filter-item">
		        			 <label for="" class="filter-name">班级：</label>
				             <div class="filter-content">
					            <select class="form-control" id="classId" onChange="searchStudent()">
						           <#if classList?exists && (classList?size>0)>
					                    <#list classList as item>
						                     <option value="${item.id!}">${item.classNameDynamic!}</option>
					                    </#list>
				                    <#else>
					                    <option value="">---请选择---</option>
				                     </#if>						           
					            </select>
				             </div>
			        	</div>
						<a href="javascript:void(0)" class="btn btn-blue pull-left btn-blue" onclick="onBatchPrint('');">批量打印</a>
						<a href="javascript:void(0)"style="margin-left: 5px;" id="btnPdfId" class="btn btn-blue pull-left btn-blue" onclick="onBatchToPdf('1');">生成pdf</a>
			        </#if>
			        <input type="hidden" id="studentId">
					
				</div><#-- 筛选结束 -->
				<div class="text-right" id="btnDivId">
					<button class="btn btn-sm btn-waterblue" onClick="pdfBackList();" style="margin-bottom:5px;margin-right:10px;">返回</button>
				</div>
				<div class="table-wrapper" id="showList">
				</div>
		</div>
	</div>	
<iframe name="batchPrintId" id="batchPrintId" style="display:none;" width="100%" height="100%" frameborder="0" ></iframe>
<script type="text/javascript" src="${request.contextPath}/studevelop/js/pdfobject.min.js"></script>
<script>
$(function(){
	searchStudent();
	$("#btnDivId").hide();	
});
function showPdf(studentId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	if(!("ActiveXObject" in window)&&PDFObject.supportsPDFs){//支持在线查看的  目前：chrome,安转pdf阅读器后，IE8-11、firefox也行
		var url = '';
		url = "${request.contextPath}/studevelop/stuQualityReport/getPdf?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId;
		$("#showList").attr('style',"height:700px");
		PDFObject.embed(url, "#showList");
		$("#btnDivId").show();
		return;
	}
}
function pdfBackList(){
	$("#btnDivId").hide();
	searchStudent();
}
function searchByAcadyear(){
	var isOne='${(isOne?default(false))?string('true', 'false')}';
	if(isOne && isOne=='true'){
		searchStudent()
	}else{
	   	$("#btnDivId").hide();
	 	var acadyear = $('#acadyear').val();
	 	var clsIdSel=$("#classId");
	  	$.ajax({
			url:"${request.contextPath}/studevelop/stuQualityReport/clsList",
			data:{acadyear:acadyear},
			dataType: "json",
			success: function(data){
				clsIdSel.html("");
				clsIdSel.chosen("destroy");
				if(!data || data.length==0){
					clsIdSel.append("<option value='' >---请选择---</option>");
					$("#classId").val("");
				}else{
				    $("#classId").val(data[0].id);
					for(var m=0;m<data.length;m++){
						clsIdSel.append("<option value='"+data[m].id+"' >"+data[m].classNameDynamic+"</option>");
					}
				}
				searchStudent();
			}
		});
	}
}
function searchBySemester(){
	var isOne='${(isOne?default(false))?string('true', 'false')}';
	if(isOne && isOne=='true'){
		searchStudent()
	}else{
		$("#btnDivId").hide();
		var acadyear = $('#acadyear').val();
		var semester = $('#semester').val();
	    var studentId = $('#studentId').val();
	    if(studentId != ''){
	        var str = "?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId;
	        var url = "${request.contextPath}/studevelop/stuQualityReport/report"+str;
	        $("#showList").load(url);
	    }else{
	        var classId = $("#classId").val();
	        var url = "${request.contextPath}/studevelop/stuQualityReport/studentList?classId="+classId+"&acadyear="+acadyear+"&semester="+semester;
	        $("#showList").load(url);
	    }
	}
}
function searchStudent(){
	$("#btnDivId").hide();
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var isOne='${(isOne?default(false))?string('true', 'false')}';
	var url ='';
	if(isOne && isOne=='true'){
		url = "${request.contextPath}/studevelop/stuQualityReport/report?studentId=${studentId!}&acadyear="+acadyear+"&semester="+semester;
	}else{
	   	var classId = $('#classId').val();
	   	url = "${request.contextPath}/studevelop/stuQualityReport/studentList?classId="+classId+"&acadyear="+acadyear+"&semester="+semester;
	}
   	$("#showList").load(url);
}	

function stuHavePdf(acadyear,semester,classId){
   	if(!acadyear){
		acadyear = $('#acadyear').val();
	}
	if(!semester){
		semester = $('#semester').val();
	}
	if(!classId){
		classId = $('#classId').val();
	}
	$.ajax({
		url:"${request.contextPath}/studevelop/stuQualityReport/stuHavePdf",
		data:{acadyear:acadyear,semester:semester,classId:classId},
		dataType: "json",
		success: function(data){
			try{
				if(data){
					var haveList=data.haveList;	
					if(haveList && haveList.length>0){
						var haveStu=null;
						for(var i=0;i<haveList.length;i++){
							haveStu=haveList[i];
							if(haveStu.havePdf==true){
								$("#stuPdf_"+haveStu.studentId).show();
							}
						}
					}
 				}
			}catch(e){
			}
		}
	});
}
function onBatchToPdf(type,acadyear,semester,classId){
	var stuIds = "";
	if(type && type=='1'){
	 	var id_array=[];
	    var ind=0;
	    $("input[name='stu-checkbox']:checked").each(function(){
	        id_array[ind++]=$(this).val();
	        if(stuIds != ''){
	        	stuIds=stuIds+',';
	        }
	        stuIds=stuIds+$(this).val();
	    });
	    if(id_array.length == 0){
	        layer.alert('至少选择一个学生！',{icon:7});
	        return;
	    }
	    $("#btnPdfId").addClass("disabled");
	}
	if(!acadyear){
		acadyear = $('#acadyear').val();
	}
	if(!semester){
		semester = $('#semester').val();
	}
	if(!classId){
		classId = $('#classId').val();
	}
	$.ajax({
		url:"${request.contextPath}/studevelop/stuQualityReport/toPdf",
		data:{acadyear:acadyear,semester:semester,classId:classId,stuIds:stuIds},
		dataType: "json",
		success: function(data){
			if(data.type=="success"){
				stuHavePdf();
 				$("#btnPdfId").removeClass("disabled");
 			}else if(data.type=="error"){
 				stuHavePdf();
 				$("#btnPdfId").removeClass("disabled");
 			}else{
 				//循环访问结果
 				window.setTimeout("onBatchToPdf('','"+acadyear+"','"+semester+"','"+classId+"')",2000);
 			}
		}
	});
}
var isSubmit=false;
function onBatchPrint(index){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var stuIds = "";
    var id_array=[];
    var ind=0;
    $("input[name='stu-checkbox']:checked").each(function(){
        id_array[ind++]=$(this).val();
        if(stuIds != ''){
        	stuIds=stuIds+',';
        }
        stuIds=stuIds+$(this).val();
    });
    if(id_array.length == 0){
        layer.alert('至少选择一个学生！',{icon:7});
        return;
    }
    document.getElementById('batchPrintId').src="${request.contextPath}/studevelop/stuQualityReport/onBatchPrint?batchId="+stuIds+"&acadyear="+acadyear+"&semester="+semester;
}

function onPrint() {
	var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
	var batchIdLeftVal = window.frames["batchPrintId"].document.getElementById("batchIdLeft").value;
	var printType=2;
	<#if deployRegion?default('') == DEPLOY_REGION_BINJIANG>
        printType=1;
	</#if>
	if (batchIdLeftVal == "") {
		if (window.frames["batchPrintId"].document.getElementById("doNotPrint").value == "0") {
			LODOP=getLodop();  
			LODOP.SET_PRINT_PAGESIZE(printType, 0, 0,"A4");
			LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
		  	LODOP.ADD_PRINT_HTM("5mm","4mm","RightMargin:4mm","BottomMargin:2mm",batchPrintId.window.getSubContent());
			LODOP.PRINT();
	     	isSubmit=false;
	     	showMsgSuccess("打印成功！");
		} 
	} else {
		LODOP=getLodop();  
		LODOP.SET_PRINT_PAGESIZE(printType, 0, 0,"A4");
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
	  	LODOP.ADD_PRINT_HTM("5mm","4mm","RightMargin:4mm","BottomMargin:2mm",batchPrintId.window.getSubContent());
		LODOP.PRINT();
		document.getElementById('batchPrintId').src="${request.contextPath}/studevelop/stuQualityReport/onBatchPrint?batchId="+batchIdLeftVal+"&acadyear="+acadyear+"&semester="+semester;
	}
}
</script>
       
<div class="filter">
	<div class="filter-item">
		<button class="btn btn-blue" onClick="printPDF('')">导出pdf</button>
		<button class="btn btn-white" onClick="onBatchPrint('')">打印</button>
		<button class="btn btn-white" onClick="deleteAll()">删除</button>
	</div>
	<div class="filter-item header_filter">
		<span class="filter-name">班级：</span>
		<div class="filter-content" style="width:150px;">
			<select vtype="selectOne" class="form-control" id="classId" name="classId" onChange="selectClass()">
				<option value="">---请选择---</option>
				<#if classList?exists && (classList?size>0)>
				<#list classList as class>
					<option value="${class.id!}">${class.classNameDynamic!}</option>
				</#list>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<div class="filter-content">
			<div class="input-group input-group-search">
				<div class="pos-rel pull-left">
					<input type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入学生姓名" id="studentName" name="studentName">
				</div>
				<div class="input-group-btn">
					<button type="button" class="btn btn-default" onClick="findByName()">
						<i class="fa fa-search"></i>
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="resultListDiv">
</div>
<iframe name="batchPrintId" id="batchPrintId" style="display:none;" width="100%" height="100%" frameborder="0" ></iframe>
<form id="carplanform" name="carplanform" method="post">
 	<input type="hidden" name="studentIds" id="studentIds"/>
</form>
<script>
$(function(){
	initChosenOne(".header_filter");
    showResultList('','');
});

function selectClass() {
	var classId = $("#classId").val();
	$("#studentName").val("");
	showResultList(classId,'');
}

$("#studentName").bind("keyup",function(event){
	if(event.keyCode ==13){
   		findByName();
 	 }
});

function findByName() {
	var studentName = $("#studentName").val();
	var classId = $("#classId").val();
	showResultList(classId,studentName);
}

function showResultList(classId,studentName) {
	var url = "${request.contextPath}/careerplan/teacher/testresult/list?classId="+classId+"&studentName="+studentName;
	url = encodeURI(encodeURI(url));
	$("#resultListDiv").load(url);
}

var studentIds = "";
function selectStudents() {
	studentIds = "";
	var carbox=$('input:checkbox[name=carTestCheckbox]');
	if (carbox == null) {
		layerClose();
		return;
	} else {
		if (carbox.length>0) {
			$('input:checkbox[name=carTestCheckbox]').each(function(i){
				if($(this).is(':checked')){
					studentIds += "," + $(this).val();
				}
			});
		} else {
			layerClose();
			layer.msg('请先选择学生！');
			return;
		}
	}
	
	if(studentIds==""){
		layerClose();
		layer.msg('请先选择学生！');
		return;
	}
	studentIds = studentIds.substring(1);
}

function printPDF(studentId) {
	layerTime();
	if (studentId != "") {
		studentIds = "";
		studentIds = studentId;
	} else {
		selectStudents();
	}
	if (studentIds != "") {
		$.ajax({
			url:'${request.contextPath}/careerplan/teacher/testresult/exportPDF',
			type:'post',
			data:{"studentIds":studentIds},
			success:function(data) {
				var jsonO = JSON.parse(data);
				if(jsonO.success){
					$("#studentIds").val(studentIds);
					exportPdf();
				} else {
					layer.msg('生成PDF文件失败！');
					layerClose();
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
}

function exportPdf(){
	var carplanform=document.getElementById('carplanform');
	if(carplanform){
		carplanform.action="${request.contextPath}/careerplan/teacher/testresult/downPDF";
		carplanform.submit();
	}
	layerClose();
}


function onBatchPrint(studentId) {
	layerTime();
	if (studentId != "") {
		document.getElementById('batchPrintId').src="${request.contextPath}/careerplan/teacher/common/testresult/batchPrintId?studentIds="+studentId+"&showprint=true";
	} else {
		selectStudents();
		if (studentIds != "") {
			document.getElementById('batchPrintId').src="${request.contextPath}/careerplan/teacher/common/testresult/batchPrintId?studentIds="+studentIds+"&showprint=true";
		}
	}
}

function printDetail(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		if (LODOP==undefined || LODOP==null) {
			layerClose();
			return;
		}
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_HTM("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",batchPrintId.window.getSubContent());
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");//横向打印
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
		LODOP.PREVIEW();//打印预览
		layerClose();
}

function deleteAll() {
	selectStudents();
	if (studentIds != "") {
		deleteOne(studentIds);
	}
}

function deleteOne(studentIds) {
layer.confirm('确定要删除吗？', function(index){
	$.ajax({
		url:'${request.contextPath}/careerplan/teacher/testresult/delete',
		type:'post',
		data:{'studentIds':studentIds},
		success:function(data) {
			layer.close(index);
			var jsonO = JSON.parse(data);
			if(jsonO.success){
				showResultList($("#classId").val(),$("#studentName").val());
	        }else{
	        	layerTipMsgWarn("删除失败","");
	        }
		},
	 	error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
});
}
</script>
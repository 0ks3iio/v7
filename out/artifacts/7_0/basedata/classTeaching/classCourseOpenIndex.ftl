<div class="tab-pane active" role="tabpanel">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">学年：</span>
			<div class="filter-content">
				<select name="acadyear" id="acadyear" class="form-control" onchange="change2()">
					<#if acadyearList?exists && (acadyearList?size>0)>
	                    <#list acadyearList as item>
		                     <option value="${item!}" <#if acadyear?default('')==item>selected</#if>>${item!}</option>
	                    </#list>
	                <#else>
	                    <option value="">未设置</option>
	                 </#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">学期：</span>
			<div class="filter-content">
				<select name="semester" id="semester" class="form-control" onchange="change2()">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
				</select>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">年级：</span>
			<div class="filter-content">
				<select name="gradeId" id="gradeId" class="form-control" onchange="gradeChange()">
					<#if gradeList?exists && (gradeList?size>0)>
	                    <#list gradeList as item>
		                     <option value="${item.id!}" <#if gradeId?default('')==item.id>selected</#if>>${item.gradeName!}</option>
	                    </#list>
	                 <#else>
	                	<option value="">暂无年级</option>
	                 </#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">班级：</span>
			<div class="filter-content">
				<select name="classId" id="classId" class="form-control" onchange="change2()">
					<#if clazzList?exists && (clazzList?size>0)>
	                    <#list clazzList as item>
		                     <option value="${item.id!}">${item.className!}</option>
	                    </#list>
	                <#else>
	                	<option value="">暂无班级</option>
	                </#if>
				</select>
			</div>
		</div>
		<div class="filter-item filter-item-right">
			<button class="btn btn-blue" onclick="addClassTeaching()">添加课程开设</button>
			<#--<button class="btn btn-blue" onclick="courseScheduleImport()">行政班课表导入</button>-->
		</div>
	</div>
	<div class="row" id="classDetail">
		
	</div>
	<div class="navbar-fixed-bottom opt-bottom">
		<button class="btn btn-blue js-addCourse" onclick="saveCourseOpen()">保存</button>
	</div>
</div>
<div id="addCourseDiv" style="display:none">
</div>
<script>
$(function(){
	change2();
});

function courseScheduleImport(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val();
	var url = "${request.contextPath}/basedata/import/courseScheduleImport/head/page?tabIndex=2&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
	$("#showList").load(url);
}

function change2(useMaster){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var classId = $("#classId").val();
	var url = "${request.contextPath}/basedata/courseopen/detail/index/page?acadyear=" + acadyear + "&semester=" + semester + "&classId=" + classId + "&useMaster="+useMaster;
	$("#classDetail").load(url);
}

function gradeChange(){
	var gradeId = $("#gradeId").val(); 
	$.ajax({
		url:"${request.contextPath}/basedata/courseopen/classByGradeId",
		data:{"gradeId":gradeId},
		dataType: "json",
		success: function(data){
			$("#classId").html("");
			var content = "";
			var jsonArr = data.jsonArr;
			if(jsonArr.length>0){
				for(var i=0;i<jsonArr.length;i++){
					var arrData=jsonArr[i];
					tempContent = '<option value='+ arrData.classId +'>' + arrData.className +'</option>';
					content += tempContent;
				}
			}else{
				tempContent = "<option value=''>去设置</option>";
				content += tempContent;
			}
			$("#classId").html(content);
			change2();
		}
	});
}

function saveCourseOpen(){
	//debugger;
	var isSubmit=false;
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	var classId = $("#classId").val(); 
	if(gradeId == ''){
		layer.alert('请选择年级',{icon:7});
		return;
	}
	if(classId == ''){
		layer.alert('请选择班级',{icon:7});
		return;
	}
	if(isSubmit){
		return;
	}
	/*var check = checkValue('#myFormOpenSave');
	if(!check){
		isSubmit=false;
		return;
	}*/
	
	var reg =  /^[1-9]\d{0,2}$/;
	var f=false;
	$(".score").each(function(){
		var temp = $(this).val();
		if(temp == "") {
			return true;
		}
		if(temp == "0") {
			return true;
		}
		var r = $(this).val().match(reg);
		
		if(r==null){
			f=true;
			layer.tips('格式不正确(最多3位整数)!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
	});
	var reg =  /^[1-9]\d{0,2}$/;
	
	$(".other").each(function(){
		var temp = $(this).val();
		if(temp == "") {
			return true;
		}
		if(temp == "0") {
			return true;
		}
		var r = $(this).val().match(reg);
		if(r==null){
			f=true;
			layer.tips('格式不正确(最多3位整数)!', $(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}
	});

	$(".credit").each(function () {
		var credit=$(this).val();
		if(credit!=""){
			if (!/^\d+$/.test(credit)){
				f=true;
				layer.tips("格式不正确(最多2位整数)！",$(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
		}
	});

	$(".xCredit").each(function () {
		var credit=$(this).val();
		if(credit!=""){
			if (!/^\d+$/.test(credit)){
				f=true;
				layer.tips("格式不正确(最多2位整数)！",$(this), {
					tipsMore: true,
					tips: 3
				});
				return false;
			}
		}
	});
	
	if(f){
		isSubmit=false;
		return;
	}
	layer.confirm('如果存在课程更改为以教学班形式教学，保存后，该班级此课程所对应的课程表数据都会被删除。确定要保存吗？', function(index){
		layer.close(index);
		isSubmit=true;
		var url = "${request.contextPath}/basedata/class/courseopen/save?acadyear=" + acadyear + "&semester=" + semester + "&classId=" + classId;
		var options = {
			url : url,
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.msg(data.msg, {offset: 't',time: 2000});
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#myFormOpenSave").ajaxSubmit(options);
	},function(index){
		isSubmit=false;
		return;
	})
	
}

function addClassTeaching(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	var classId = $("#classId").val(); 
	if(gradeId == ''){
		layer.alert('请选择年级',{icon:7});
		return;
	}
	if(classId == ''){
		layer.alert('请选择班级',{icon:7});
		return;
	}
	var url = "${request.contextPath}/basedata/courseopen/class/add/index/page?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&classId="+classId;
	$("#addCourseDiv").load(url);
	layer.open({
			type: 1,
			shadow: 0.5,
			title: '添加开设课程',
			area: ['1000px','660px'],
			scrollbar:false,
			btn: ['确定', '取消'],
			content: $('#addCourseDiv'),
			yes:function(index,layero){
				saveCourseList();
			}
	})
}
</script>

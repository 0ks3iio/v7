<div class="filter clearfix">
	<div class="filter-item">
		<label for="" class="filter-name">学年：</label>
		<div class="filter-content">
			<select class="form-control" id="acadyear" name="acadyear" <#if type?default('')=='1'>onChange="changeTime()"<#else>onChange="changeGradedId()"</#if>>
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
			<select class="form-control" id="semester" name="semester" <#if type?default('')=='1'>onChange="changeTime()"<#else>onChange="changeGradedId()"</#if>>
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<#if type?default('')=='1'>
	<div class="filter-item">
		<label for="" class="filter-name">考试：</label>
		<div class="filter-content">
			<select class="form-control" id="examId" name="examId" onChange="changeExamId()">
				<option value="">---请选择---</option>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<label for="" class="filter-name">班级：</label>
		<div class="filter-content">
			<select class="form-control" id="classId" name="classId" onChange="changeClassId()">
				<option value="">---请选择---</option>
			</select>
		</div>
	</div>
	<#elseif type?default('')=='2'>
	<input type="hidden" name="examId" id="examId" value="${examId!}"/>
	<div class="filter-item">
		<label for="" class="filter-name">年级：</label>
		<div class="filter-content">
			<select class="form-control" id="gradeId" name="gradeId" onChange="changeGradedId()">
				<option value="">---请选择---</option>
				
				<#if gradeList?exists && (gradeList?size>0)>
                    <#list gradeList as item>
	                     <option value="${item.id!}">${item.gradeName!}</option>
                    </#list>
                </#if>
			</select>
		</div>
	</div>
	</#if>
	<div class="filter-item filter-item-right">
		<div class="filter-content">
			<a class="btn btn-sm btn-blue js-init" href="javascript:void(0)" id="limitInitBtn" onclick="limitInit()" data-toggle="tooltip" data-original-title="根据教师任课信息初始化">初始化</a>
		</div>
	</div>
</div>
<div class="tab-container">
	<ul class="nav nav-tabs" role="tablist" id="subjectTab">
	</ul>
	<div  id="subjectTabList">
	</div>
</div>
<script type="text/javascript">
	$(function(){
		<#if type?default('')=='1'>
			changeTime();
		<#elseif type?default('')=='2'>
			changeGradedId();
		</#if>
		
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});
		
	})
	function limitInit(){
		var acadyear=$("#acadyear").val();
		if(acadyear==""){
			layer.tips("请选择学年", "#acadyear", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		var semester=$("#semester").val();
		if(semester==""){
			layer.tips("请选择学期", "#semester", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		var examId=$("#examId").val();
		if(examId==""){
			layer.tips("请选择考试", "#examId", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		$.ajax({
			url:"${request.contextPath}/scoremanage/scoreLimit/limitInit",
			data:{acadyear:acadyear,semester:semester,examId:examId,type:'${type!}'},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
				}else{
					layerTipMsg(data.success,"失败",data.msg);
					return;
				}
				var subjectId="";
				$("#subjectTab").find("li").each(function(){
					if($(this).hasClass("active")){
						subjectId=$(this).find("a").attr("id");
						return false;
					}
				})
				showList(subjectId);
			}
		});
	}
	<#if type?default('')=='1'>
	function changeTime(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		//根据学年学期拿到考试id
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				examClass.html("");
				if(data.length==0){
					examClass.append("<option value='' >-----请选择-----</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						if(i==0){
							examClass.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].examNameOther+"</option>");
						}else{
							examClass.append("<option value='"+data[i].id+"' >"+data[i].examNameOther+"</option>");
						}
					}
				}
				changeExamId();
			}
		});
	}
	function changeExamId(){
		//根据考试id获取班级信息
		var examId=$("#examId").val();
		var clazzClass=$("#classId");
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/clazzList",
			data:{unitId:'${unitId!}',examId:examId},
			dataType: "json",
			success: function(data){
				clazzClass.html("");
				if(data==null){
					clazzClass.append("<option value='' >---请选择---</option>");
				}else{
					clazzClass.append("<option value='' >---请选择---</option>");
					for(key in data){
						clazzClass.append("<option value='"+key+"' >"+data[key]+"</option>");
					}
				}
				changeClassId();
			}
		});
	}
	
	function changeClassId(){
		var examId=$("#examId").val();
		var classId=$("#classId").val();
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/subjectList",
			data:{unitId:'${unitId!}',examId:examId,classId:classId},
			dataType: "json",
			success: function(data){
				makeSubjectTab(data);
			}
		});
		
	}
	<#elseif type?default('')=='2'>
	
	function changeGradedId(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeId=$("#gradeId").val();
		$.ajax({
			url:"${request.contextPath}/scoremanage/common/subjectByGradeList",
			data:{unitId:'${unitId!}',acadyear:acadyear,semester:semester,gradeId:gradeId},
			dataType: "json",
			success: function(data){
				makeSubjectTab(data);
			}
		});
	}
	</#if>
	function makeSubjectTab(data){
		var subjectTabClass=$("#subjectTab");
		subjectTabClass.html("");
		if(data.length>0){
			var subjectId="";
			for(var i = 0; i < data.length; i ++){
				if(i==0){
					subjectId=data[i].id;
					var htmlText='<li role="presentation" class="active">'
					+'<a role="tab" data-toggle="tab" href="#" id="'+data[i].id+'">'+data[i].subjectName
					+'</a></li>';
					subjectTabClass.append(htmlText);
				}else{
					var htmlText='<li role="presentation">'
					+'<a role="tab" data-toggle="tab" href="#" id="'+data[i].id+'">'+data[i].subjectName
					+'</a></li>';
					subjectTabClass.append(htmlText);
				}
			}
			showList(subjectId);
		}else{
			$("#subjectTabList").html("没有科目信息");
		}
	}
	$(document).on('click', '#subjectTab a', function(e){
		e.preventDefault();
		var subjectId=$(this).attr("id");
		showList(subjectId);
	})
	function showList(subjectId){
		if(subjectId==""){
			$("#subjectTabList").html("没有科目信息");
			return;
		}
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var parmUrl="?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&subjectId="+subjectId;
		<#if type?default('')=='1'>
			var classId=$("#classId").val();
			parmUrl=parmUrl+"&classId="+classId+"&type="+1;
		<#elseif type?default('')=='2'>
			var gradeId=$("#gradeId").val();
			parmUrl=parmUrl+"&gradeId="+gradeId+"&type="+2;
		</#if>
		var url="${request.contextPath}/scoremanage/scoreLimit/limitList/page"+parmUrl;
		$("#subjectTabList").load(url);
	}
	
</script>	
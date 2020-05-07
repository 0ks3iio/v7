<div class="tab-pane active">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">学年：</span>
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
			<span class="filter-name">学期：</span>
			<div class="filter-content">
				<select class="form-control" id="semester" name="semester" <#if type?default('')=='1'>onChange="changeTime()"<#else>onChange="changeGradedId()"</#if>>
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
				</select>
			</div>
		</div>
		<#if type?default('')=='1'>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select class="form-control" id="gradeCode" name="gradeCode" onChange="changeTime()">
		                <#if gradeList?? && (gradeList?size>0)>
							<#list gradeList as item>
								<option value="${item.gradeCode!}" >${item.gradeName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">考试名称：</span>
				<div class="filter-content">
					<select class="form-control" id="examId" name="examId" onChange="changeExamId()" style="width:200px;">
						<option value="">---请选择考试---</option>
					</select>
				</div>
			</div>
		<#elseif type?default('')=='2'>
			<input type="hidden" name="examId" id="examId" value="${examId!}"/>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
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
			<#--<button class="btn btn-blue">初始化</button>-->
			<a class="btn btn-sm btn-blue js-init" href="javascript:void(0)" id="limitInitBtn" onclick="limitInit()" data-toggle="tooltip" data-original-title="根据教师任课信息初始化录分人员">初始化</a>
		</div>
	</div>		
	<div class="clearfix">
		<div class="tree-wrap">
			<table class="table table-striped table-hover no-margin">
                <thead>
					<tr>
						<th>科目</th>
					</tr>
				</thead>
			</table>
			<div class="tree-list" style="height:600px;margin-top: 0;" id="subjectTab">
			</div>
		</div>
		<div style="margin-left: 200px;">
			<table class="table table-bordered table-striped table-hover no-margin">
                <thead>
					<tr>
						<th>教学班录分人员</th>
					</tr>
				</thead>
			</table>
			<div class="padding-10" id="subjectTabList" style="border-right: 1px solid #ddd;border-bottom: 1px solid #ddd;">
			</div>
		</div>
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
			url:"${request.contextPath}/exammanage/scoreLimit/limitInit",
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
				$("#subjectTab").find("a").each(function(){
					if($(this).hasClass("active")){
						subjectId=$(this).attr("id");
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
		var gradeCode=$("#gradeCode").val();
		//根据学年学期拿到考试id
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}',gradeCode:gradeCode},
			dataType: "json",
			success: function(data){
				examClass.html("");
				if(data.length==0){
					examClass.append("<option value='' >-----请选择考试-----</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						if(i==0){
							examClass.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].examName+"</option>");
						}else{
							examClass.append("<option value='"+data[i].id+"' >"+data[i].examName+"</option>");
						}
					}
				}
				changeExamId();
			}
		});
	}
	function changeExamId(){
		var examId=$("#examId").val();
		$.ajax({
			url:"${request.contextPath}/exammanage/common/subjectList",
			data:{unitId:'${unitId!}',examId:examId},
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
					var htmlText = '<a class="active" href="#" id="'+data[i].id+'">'+data[i].subjectName+'</a>';
					subjectTabClass.append(htmlText);
				}else{
					var htmlText = '<a href="#" id="'+data[i].id+'">'+data[i].subjectName+'</a>';
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
		$(this).siblings(".active").removeClass("active");
		$(this).attr("class","active");
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
			var gradeCode=$("#gradeCode").val();
			parmUrl=parmUrl+"&gradeCode="+gradeCode+"&type="+1;
		<#elseif type?default('')=='2'>
			var gradeId=$("#gradeId").val();
			parmUrl=parmUrl+"&gradeId="+gradeId+"&type="+2;
		</#if>
		var url="${request.contextPath}/exammanage/scoreLimit/limitList/page"+parmUrl;
		$("#subjectTabList").load(url);
	}
	
</script>	
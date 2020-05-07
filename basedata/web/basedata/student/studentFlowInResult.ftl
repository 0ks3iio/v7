<#import "/fw/macro/webmacro.ftl" as w>
<#--
<#assign STUDENT_FLOW_LEAVE=stack.findValue("@net.zdsoft.basedata.entity.StudentFlow@STUDENT_FLOW_LEAVE") />
<#assign STUDENT_FLOW_IN=stack.findValue("@net.zdsoft.basedata.entity.StudentFlow@STUDENT_FLOW_IN") />
-->
<#assign STUDENT_FLOW_LEAVE="0" />
<#assign STUDENT_FLOW_IN="1" />
<#if errorMsg?default("") != "">
<p class="alert alert-warning">${errorMsg!}</p>
<#else>
<div class="filter table-wrapper">
		<#if studentDtos?exists && studentDtos?size gt 0>
	<div class="row">
		<#list studentDtos as studentDto>
		<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
			<div class="filter-item block">
				<label for="" class="filter-name">学生姓名：</label>
				<div class="filter-content">
					<p>${studentDto.student.studentName!}</p>
					<input type="hidden" value="${studentDto.student.id!}" id="studentId" />
					<input type="hidden" value="${studentDto.studentFlow.pin!}" id="pinCode" />
				</div>
			</div>
			<div class="filter-item block">
				<label for="" class="filter-name">身份证件号：</label>
				<div class="filter-content">
					<p>${studentDto.student.identityCard!}</p>
				</div>
			</div>
			<#if studentDto.studentFlow.flowType==STUDENT_FLOW_IN>
			<div class="filter-item block">
				<label for="" class="filter-name">验证码：</label>
				<div class="filter-content">
					<p>${studentDto.studentFlow.pin!}</p>
				</div>
			</div>
			</#if>
			<div class="filter-item block">
				<label for="" class="filter-name">原学校：</label>
				<div class="filter-content">
					<p>${studentDto.studentFlow.schoolName!}</p>
				</div>
			</div>
			<div class="filter-item block">
				<label for="" class="filter-name">原班级：</label>
				<div class="filter-content">
					<p>${studentDto.studentFlow.className!}</p>
				</div>
			</div>
			<#if studentDto.studentFlow.flowType==STUDENT_FLOW_IN>
			<div class="filter-item block">
				<label for="" class="filter-name">是否转入：</label>
				<div class="filter-content">
					<p>否</p>
				</div>
			</div>
			</#if>
			<button style="margin-bottom:20px;" studentId="${studentDto.student.id!}" class="btn btn-darkblue btn-lg js-inSchool js-inSchool_${studentDto.student.id!} width-180">转入</button>
		</div>
		</#list>
	</div>
		<#else>
					<p class="alert alert-warning">未找到该学生!</p>
		</#if>
</div>
<#--
-->
<#if  studentDtos?exists && studentDtos?size gt 0 && !isUseIdentityCard?default(true)>
<@w.pagination  container=".searchResult" pagination=pagination page_index=2 />
</#if>
<script>
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		$(".js-inSchool").on("click", function(){
		    var studentId = $(this).attr("studentId");
			var url = "${request.contextPath}/basedata/studentFlowIn/detail/page/" + studentId+"?pinCode="+$("#pinCode").val();
			var indexDiv = layerDivUrl(url,{height:380,width:1000,title:'转入操作'});
			resizeLayer(indexDiv, 1000, 350);
		});
		
	});
	
</script>
</#if>

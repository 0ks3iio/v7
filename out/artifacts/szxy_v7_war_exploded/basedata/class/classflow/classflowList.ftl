<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>
<#import "/basedata/class/detailmacro.ftl" as d>
<#import "/fw/macro/htmlcomponent.ftl" as htmlmacro>


<#if errorMsg?default("") != "">
<p class="alert alert-warning">${errorMsg!}</p>
<#else>
<div class="filter table-wrapper">
	<#if datas?exists && datas?size gt 0>
	<div class="row">
		<#assign st=0 />
		<#list datas as studentDto>
		<#if (studentDto.student.isLeaveSchool?exists && studentDto.student.isLeaveSchool.toString()?string?default('0') != '1') || !(studentDto.student.isLeaveSchool?exists)>
		<#assign st = st+1 />
		<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
			<@d.searchItem label="学生姓名" type="nested" id="studentName" itemClass="block" >
				<p>${studentDto.student.studentName!}</p>
			</@d.searchItem>
			<@d.searchItem label="身份证件号" type="nested" id="identityCard" itemClass="block" >
				<p>${studentDto.student.identityCard!}</p>
			</@d.searchItem>
			<@d.searchItem label="当前班级" type="nested" id="className" itemClass="block" >
				<p>${studentDto.className!}</p>
			</@d.searchItem>
			<button style="margin-bottom:20px;" studentId="${studentDto.student.id!}" class="btn btn-darkblue btn-lg btn-classflow width-180">转班操作</button>
		</div>
		</#if>
		</#list>
	</div>
		<#if st ==0>

		</#if>
		<#else>
				<p class="alert alert-warning">未找到该学生!</p>
		</#if>
</div>
<#if st!=0>
<@w.pagination  container="#a .searchResult" pagination=pagination page_index=1/>
</#if>
<script>
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		$(".btn-classflow").on("click", function(){
			
			var studentId = $(this).attr("studentId");
			var url = "${request.contextPath}/basedata/classflow/student/detail/page?unitId=${unitId!}"+"&studentId="+studentId;
			var indexDiv = layerDivUrl(url,{height:270,width:400,title:'转班操作'});
			resizeLayer(indexDiv, 270, 400);
		});
		
	});
	
</script>
</#if>

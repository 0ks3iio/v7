<!-- ajax layout which only needs content area -->
<#if gradeList?exists && (gradeList?size>0) && roleMap?exists && (roleMap?size>0)>
	<#assign roleAdmin=roleMap['1']?exists>
	<#list gradeList as item>
	<#assign roleGrade=roleMap['2']?exists && roleMap['2']?size gt 0 && roleMap['2']?seq_contains(item.id!)>
	<#assign roleClass=roleMap['3']?exists && roleMap['3']?size gt 0 && roleMap['3']?seq_contains(item.id!)>
	<#if roleAdmin || roleGrade || roleClass>
		<div class="box box-default">
			<div class="box-header">
				<h3 class="box-title">${item.gradeName!}</h3>
				<#if roleAdmin>
				<a href="javascript:" class="btn btn-white pull-right place-add" onclick="doEnterSetp('${item.id!}',4)">上报</a>
				</#if>
			</div>
			<div class="box-body">
				<div class="system-flow clearfix">
					<#if roleAdmin || roleGrade>
					<a href="javascript:" class="flow" onclick="doEnterSetp('${item.id!}',0)">
						<span class="flow-icon flow-icon-data"></span>
						<div class="flow-content">
							<h4 class="flow-name">基础条件</h4>
							<p class="flow-txt">设置默认条件</p>
						</div>
					</a>
					</#if>
					<#if roleAdmin || roleGrade || roleClass>
					<a href="javascript:" class="flow" onclick="doEnterSetp('${item.id!}',1)">
						<span class="flow-icon flow-icon-selectCourse"></span>
						<div class="flow-content">
							<h4 class="flow-name">选课</h4>
							<p class="flow-txt">支持线上线下选课</p>
						</div>
					</a>
					</#if>
					<#if roleAdmin || roleGrade>
					<a href="javascript:" class="flow" onclick="doEnterSetp('${item.id!}',2)">
						<span class="flow-icon flow-icon-splitClass"></span>
						<div class="flow-content">
							<h4 class="flow-name">分班</h4>
							<p class="flow-txt">可手动、自动分班</p>
						</div>
					</a>
					<a href="javascript:" class="flow" onclick="doEnterSetp('${item.id!}',3)">
						<span class="flow-icon flow-icon-arrangCourse"></span>
						<div class="flow-content">
							<h4 class="flow-name">排课</h4>
							<p class="flow-txt">算法自动排课</p>
						</div>
					</a>
					</#if>
				</div>
			</div>
		</div>
		</#if>
		</#list>
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<#if roleMap?exists && (roleMap?size>0)>
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/7choose3/noSelectSystem.png" alt="">
			</span>
			<div class="no-data-body">
				<h3>暂无年级设置</h3>
			</div>
			<#else>
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/limit.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">对不起，暂时无权限访问<br>请联系管理员</p>
			</div>
			</#if>
		</div>
	</div>
</#if>
<script>
	$(document).ready(function(){
		hidenBreadBack();
	});
	
	function doEnterSetp(id,step,isMaster){
		var url = '';
		if(step == 0){
			url =  '${request.contextPath}/newgkelective/'+id+'/goBasic/index/page';
		}else if(step == 1){
			url =  '${request.contextPath}/newgkelective/'+id+'/goChoice/index/page?isMaster='+isMaster;
		}else if(step == 2){
			url =  '${request.contextPath}/newgkelective/'+id+'/goDivide/index/page';
		}else if(step == 3){
			url =  '${request.contextPath}/newgkelective/'+id+'/goArrange/index/page';
		}else if(step == 4){
			url =  '${request.contextPath}/newgkelective/edu/upload/index?gradeId='+id;
		}
		$("#showList").load(url);
	}
</script>
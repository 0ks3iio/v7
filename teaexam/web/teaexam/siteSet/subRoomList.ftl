<div id="a1" class="tab-pane active">
	<#assign hasData = false />
	<#if dtos?exists && dtos?size gt 0>
	<#assign hasData = true />
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">各科考生数总和：</span>
			<div class="filter-content">${totalStu?default(0)}</div>
		</div>
	</div>
	<div class="table-container">
		<div class="table-container-body">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th rowspan="2">序号</th>
						<th rowspan="2" width="20%">科目</th>
						<th rowspan="2">学段</th>
						<th rowspan="2">考试时间</th>
						<th rowspan="2">已通过审核人数</th>
						<th colspan="3" class="text-center">考场设置</th>
						<th rowspan="2">操作</th>
					</tr>
					<tr>
						<th>考点名称</th>
						<th>考场数</th>
						<th>可容纳人数</th>
					</tr>
				</thead>
				<tbody>
					<#list dtos as dto>
					<tr>
					    <#assign rowNum = 1 />
					    <#if dto.sites?exists && dto.sites?size gt 0>
					    <#assign rowNum = dto.sites?size />
					    </#if>
					    <td rowspan="${rowNum}">${dto_index+1}</td>
					    <td rowspan="${rowNum}" style="word-break:break-all;">${dto.subject.subjectName!}</td>
					    <td rowspan="${rowNum}">${mcodeSetting.getMcode("DM-XD",dto.subject.section?string)}</td>
					    <td rowspan="${rowNum}">${(dto.subject.startTime?string('yyyy-MM-dd HH:mm'))?if_exists}-${(dto.subject.endTime?string('HH:mm'))?if_exists}</td>
					    <td rowspan="${rowNum}">
					    	${dto.stuCount?default(0)}<span class="color-red">（已分配${dto.hasCount?default(0)}人，待分配${dto.noCount?default(0)}人）</span>
					    </td>
					    <#if dto.sites?exists && dto.sites?size gt 0>
					    <#list dto.sites as site>
						    <#if site_index gt 0>
						    	<tr>
						    </#if>
						    <td>${site.schoolName!}</td>
						    <td>${site.roomNum?default(0)}</td>
						    <td>${site.roomNum?default(0)*site.perNum?default(0)}</td>
						    <#if site_index == 0>
						    	<td rowspan="${rowNum}"><a class="color-blue js-add" href="javascript:;" onclick="toEdit('${dto.subject.id!}');">添加考场</a></td>
						    </#if>
						    </tr>
					    </#list>
					    <#else>
						    <td></td>
						    <td></td>
						    <td></td>
						    <td><a class="color-blue js-add" href="javascript:;" onclick="toEdit('${dto.subject.id!}');">添加考场</a></td>
						    </tr>
					    </#if>
					</#list>
				</tbody>
			</table>
		</div>
	</div>
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">没有相关数据</p>
			</div>
		</div>
	</div>
	</#if>
</div>
<script>
function toEdit(infoId){
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/room/edit?subInfoId='+infoId;
	indexDiv = layerDivUrl(url,{title: "添加考场",width:750,height:650});
}
</script>
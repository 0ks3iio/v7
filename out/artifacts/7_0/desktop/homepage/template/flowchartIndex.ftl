<div class="col-sm-${(data.col)!}">
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">新高考选课流程</h4>
	</div>
	<div class="box-body">
		<ol class="flow-selectCourse">
		<#if data.dtoList?exists && (data.dtoList?size > 0)>
			<#list data.dtoList as dto>
			<li class="flow-selectCourse-step flow-selectCourse-step0${dto_index+1}">
				<div><a href="###"  class="flow-selectCourse-item">${dto.title!}<span>0${dto_index+1}</span></a></div>
				<ul>
					<#if (dto.dtos)?exists && ((dto.dtos)?size > 0)>
						<#list (dto.dtos) as dtoList>
						<li title='${dtoList.prompt!}'><a href="###"  <#if (dtoList.authority) == true>onclick="openModel('${dtoList.serialNumber!}','${dtoList.title!}','${dtoList.style!}','${dtoList.url!}','','','','')"</#if> style='padding: 8px 8px;' class="flow-selectCourse-item <#if (dtoList.authority) == false>disabled</#if>">${dtoList.title!}</a></li>
						</#list>
					</#if>
				</ul>
			</li>
			</#list>
		</#if>
		</ol>
	</div>
</div>
</div>
<#if eccInfos?exists&&eccInfos?size gt 0>   
    <ul class="card-list">
  	<#list eccInfos as item>
		<li class="card-item">
			<div class="class-card <#if item_index%4==0>class-card01 <#elseif item_index%4==1>class-card02 <#elseif item_index%4==2>class-card03<#elseif item_index%4==3>class-card04</#if>">
				<div class="class-card-header">
				<#if item.className?exists>
					<span>${item.className!}</span>
				<#else>
					<span>${item.placeName!}</span>
				</#if>
				</div>
				<div class="class-card-body">
					<ul>
						<li style="white-space:nowrap;text-overflow:ellipsis;overflow:hidden;" width="230px"><span>安装场地：</span>${item.placeName!}</li>
						<li><span>用途：</span>${usedForMap[item.type!]!}</li>
						<li><span>设备号：</span>${item.name!}</li>
					</ul>
					<div class="text-center"><a class="btn btn-blue" href="javascript:;" onclick="myEclasscardEdit('${item.id!}','0')">进入</a></div>
				</div>
			</div>
		</li>
    </#list>
	</ul>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
		</span>
		<div class="no-data-body">
			<h3>暂无班牌</h3>
		</div>
	</div>
</div>
</#if>
		


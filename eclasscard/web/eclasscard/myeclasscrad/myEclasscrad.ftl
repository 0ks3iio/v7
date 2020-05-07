    <ul class="card-list">
<#if eccInfos?exists&&eccInfos?size gt 0>
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
					<div class="text-center"><a class="btn btn-blue" href="javascript:;" onclick="myEclasscardEdit('${item.id!}')">进入</a></div>
				</div>
			</div>
		</li>
    </#list>
</#if>
	</ul>
		


<script type="text/javascript">
function myEclasscardEdit(id){
	var url =  '${request.contextPath}/eclasscard/myClassCard/tab?id='+id;
	$("#myEclasscardDiv").load(url);
}
</script>

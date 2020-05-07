<div>
		<#if classDtoList?exists && (classDtoList?size > 0)>
		<#assign colCount = 12>
		<#assign xunCount = (classDtoList?size/colCount)?number >
		<#if (classDtoList?size%colCount > 0) >
			<#assign xunCount = xunCount + 1 >
		</#if>
		<#list 1..xunCount as cou>
			<table class="table table-bordered table-striped table-hover <#if (cou!=1)>showtableClass</#if> ">
				<tr>
					<#list classDtoList as item>
						<#if (item_index >= (cou-1)*colCount) && (item_index < cou*colCount)>
							<td <#if item.num==0>class="color-red"</#if>><a href="javascript:" onclick="selectClazz('${item.type!}#${item.classId!}#${item.subjectIds!}#${item.batch!}#${item.classType!}')">${item.name!}(${item.num})</a></td>
						</#if>
					</#list>
				</tr>
			</table>
		</#list>
		<#if (xunCount>1) ><a href="javascript:" class="tableHideOrShowButtn" id="tableHideOrShowButtn" onclick="tableHideOrShow()">收起详情</a></#if>
	</#if>
</div>
<script>
	var i=0;
	function tableHideOrShow(){
		if(i==0){
			$(".showtableClass").hide();
			$("#tableHideOrShowButtn").html("展开详情");
			i=1;
		}else{
			$(".showtableClass").show();
			$("#tableHideOrShowButtn").html("收起详情");
			i=0;
		}
	}
</script>

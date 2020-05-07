<h2 class="ecard-name">${placeName!}</h2>
<table class="table">
	<tbody>
		<tr>
			<td>场地名称</td>
			<td>${placeName!}</td>
		</tr>
		<tr>
			<td>类型</td>
			<td>
			 <#assign placeTypes = (placeType?default(''))?split(',')>
		       <#list 0..(placeTypes?size - 1) as index>
		          ${mcodeSetting.getMcode("DM-CDLX","${placeTypes[index]}")}
		          <#if index != (placeTypes?size - 1)>
		          ,
		          </#if>
		       </#list>
			</td>
		</tr>
		<tr>
			<td>场地位置</td>
			<td>${placeAddress!}</td>
		</tr>
		<tr>
			<td>容纳人数</td>
			<td>${placeNum!}</td>
		</tr>
	</tbody>
</table>
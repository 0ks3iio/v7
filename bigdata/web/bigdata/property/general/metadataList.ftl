<div class="card-made bg-fff">
    <div class="card-made-head">
        <b>元数据</b>
    </div>
    <div class="card-made-body">
        <table class="tables">
        	<thead>
        		<tr>
        			<th>数据名称</th>
        			<th>数据量</th>
        			<th>接口数量</th>
        			<th>数据质量</th>
                    <th>更新时间</th>
        		</tr>
        	</thead>
        	<tbody class="scrollBar4">
        	<#if metadatas?exists&&metadatas?size gt 0>
          		<#list metadatas as meta>
        		<tr>
        			<td class="ellipsis" width="25%">
                        <img src="${request.contextPath}/bigdata/v3/static/images/data-warehouse/icon-metadata.png" >
                        <a href="javascript:void(0);" onclick="showMetadataDetail('${meta.id!}')" class="pl-20"  title="${meta.name!}" >${meta.name!}</a>
                    </td>
        			<td class="font-16"><b>${meta.dataCount?default(0)}</b></td>
        			<td class="font-16"><b>${meta.apiCount?default(0)}</b></td>
        			<td class="rating-show" data="${meta.dataQuality!}">无
                    </td>
                    <td>${meta.statDate!}</td>
        		</tr>
        		</#list>
	      	</#if>
        	</tbody>
        </table>
    </div>
</div>
<script type="text/javascript">

$(function(){
	$('.metadata-table .rating-show').each(function(){
		var data = $(this).attr("data");
		if(data&&data!='')$(this).html(getStarsHtml(returnEmptyNum(data)));
	})
})
</script>
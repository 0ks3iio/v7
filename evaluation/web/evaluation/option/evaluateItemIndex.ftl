			<div class="box box-default">
				<input type="hidden" value="${itemType!}" id="itemTypeIndex">				
				<div class="box-body" style="padding-bottom : 0px">
					<ul class="nav nav-tabs nav-tabs-1" role="tablist">
						<#if zbmcodeList?exists && zbmcodeList?size gt 0>
						<#list zbmcodeList as item>
							<li <#if item_index==0>class="active"</#if> role="presentation"><a href="javascript:void(0)" onclick="itemShowList('${item.thisId!}')" role="tab" data-toggle="tab">${item.mcodeContent!}</a></li>
						</#list>
						</#if>
					</ul>
				</div>
				<div class="tab-content" id="itemShowDivId" style="padding-top : 0px">
				</div>
			</div>
<script>
	$(function(){
		itemShowList($("#itemTypeIndex").val());
	});
	function itemShowList(itemType){
	    var  url =  '${request.contextPath}/evaluate/option/list/page?itemType='+itemType;
        $("#itemShowDivId").load(url);
	}

</script>

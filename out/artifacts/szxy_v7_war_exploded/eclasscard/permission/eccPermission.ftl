<div class="box box-default">
	<div class="box-body">

		<div class="explain">
			<p>说明：默认班主任具有各行政班班牌所有权限（包括简介、照片和公告）<#if !showxzb>，行政班班牌权限不可设置</#if>。</p>
		</div>
		
		<div class="filter filter-right">
			<div class="filter-item filter-item-left">
				<span class="filter-name">用途：</span>
				<div class="filter-content">
					<select name="bpyt" id="bpyt" class="form-control" onchange="showbpytList()">
						<option value="">---请选择---</option>
						<#if usedForDtos?exists&&usedForDtos?size gt 0>
		                  	<#list usedForDtos as item>
							<option value="${item.thisId!}">${item.content!}</option>
		              	    </#list>
                    	</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<a class="btn btn-blue" href="javascript:void(0);" onclick="editALL()">批量设置</a>
			</div>
		</div>
		<div id="showEccPermissionList">
			
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	showbpytList();
})

function showbpytList() {
	var bpyt = $("#bpyt").val();
	var url ='${request.contextPath}/eclasscard/permission/showList?bpyt='+bpyt;
    $("#showEccPermissionList").load(url);
}
</script>
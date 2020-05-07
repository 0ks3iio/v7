<#if memberList?exists && memberList?size gt 0>
<ul class="mui-table-view mui-approval"  id="selectDiv">
	<#list memberList as mem>
	<li class="mui-table-view-cell mui-media mui-approvalParty-list">
	    <div class="mui-input-row mui-checkbox mui-left">
			<div class="mui-media-body" teaId="${mem.id!}" >
				<p class="mui-approvalParty-tit"><span>${mem.teacherName!}</span><span>${mcodeSetting.getMcode("DM-XB",mem.sex?default(0)?string)}</span><span>${mem.phone!}</span></p>
				<p class="mui-ellipsis">所属单位：${mem.unitName!}</p>
			</div>
		    <input name="checkbox" class="checkbox-list" value="${mem.id!}" type="checkbox">
		</div>
	</li>
	</#list>
</ul>
<#else>
<div class="mui-page-error mui-noRecord">
	<p>暂时还没有相关内容！</p>
</div>	
</#if>
<div class="mui-content" id="remarkDiv" style="display:none;padding-top:0px;">
    <form class="mui-input-group mui-activist-list" id="orgForm">
        <div class="mui-input-row mui-bg-row-grey">
            <span class="activist-tit">审核意见：</span>
        </div>
        <div class="mui-input-row mui-activist-info">
		    <textarea class="activist-introduce" id="remark" name="applicationContent" class="mui-input-clear" placeholder="请输入审核意见...">同意</textarea>
        </div>
    </form>
    <div class="mui-activist-btn"><a href="#" onclick="save();">保存</a></div>
</div>
<script type="text/javascript" charset="utf-8">
var str = '&applyState=${applyState?default(0)}&auditState=${auditState?default(0)}';

$('div.mui-media-body').click(function(){
	var id = $(this).attr('teaId');
	load("${request.contextPath}/mobile/open/partybuild7/memberList/detail?teacherId="+id+str);
});		
</script>  
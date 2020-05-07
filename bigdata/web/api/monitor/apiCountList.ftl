<div class="chartm-header">
    <div class="chartm-header-left">
        <i class="iconfont icon-caret-right"></i>
    </div>
    <span>接口数据量</span>
</div>
<div class="chartm-body gun-content">
   <#if interfaceMonitorDtos?exists &&(interfaceMonitorDtos?size gt 0)>
         <#list interfaceMonitorDtos as dto>
               <div class="chartm-li">
                <span>${dto.description!}：</span>
				      拉取数据：${dto.findCount!}条， 拉取次数：${dto.findNum!}次，推送数据：${dto.saveCount!}条，推送次数：${dto.saveNum!}次
				</div>
         </#list>
   </#if>
</div>
<script type="text/javascript">
$(".chartm-header-left").click(function () {
    $(".inter-chart-modal").css("right", "-230px");
})
</script>
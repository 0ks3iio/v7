<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if emExamInfoList?exists&& (emExamInfoList?size > 0)>
<table class="table table-bordered table-striped table-hover">
    <thead>
    <tr>
        <th width="10%">考试编号</th>
        <th width="10%">考试名称</th>
        <th width="10%">考试类型</th>
        <th width="10%">科目</th>
        <th width="15%">报名起始时间</th>
        <th width="15%">报名截止时间</th>
        <th width="10%">状态</th>
        <th width="10%">操作</th>
    </tr>
    </thead>
    <tbody>
	    <#list emExamInfoList as item>
        <tr>
            <td >${item.examCode}</td>
            <td >${item.examName!}</td>
            <td >${mcodeSetting.getMcode("DM-KSLB", item.examType?default(1)?string)}</td>
            <td>${item.subjectNames!}</td>
            <td >${item.signStartTime}</td>
            <td >${item.signEndTime!}</td>
            <td ><#if item.state??&&item.state=="-1">待报名<#elseif item.state??&&item.state=="0">审核中<#elseif item.state??&&item.state=="1">已通过<#else>未通过</#if></td>
            <td><#if item.state??&&item.state=="-1"><a class="table-btn show-details-btn" href="javascript:void(0);" onclick="doEnroll('${item.id}','${item.unitId}')">报名</a><#else ><a href="javascript:void(0);">已报名</a></#if></td>
        </tr>
        </#list>
    </tbody>
</table>
<#else >
<div class="no-data-container">
    <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
        <div class="no-data-body">
            <p class="no-data-txt">暂无相关数据</p>
        </div>
    </div>
</div>
</#if>
<#--<@htmlcom.pageToolBar container="#tabDiv" class="noprint">-->
<#--</@htmlcom.pageToolBar>-->
<script type="text/javascript">

    function doEnroll(id,unitId) {
        var url = "${request.contextPath}/exammanage/stu/stuSign/stuSignConfirmInfo?id="+id+"&unitId="+unitId;
        indexDiv = layerDivUrl(url,{title: "信息",width:500,height:500});
        <#--showConfirmMsg('确认报名？','提示',function(){-->
            <#--var ii = layer.load();-->
            <#--$.ajax({-->
                <#--url: '${request.contextPath}/exammanage/stu/stuSign/doEnroll?',-->
                <#--data: {'examId':id,'unitId':unitId},-->
                <#--type:'post',-->
                <#--success:function(data) {-->
                    <#--layer.closeAll();-->
                    <#--var jsonO = JSON.parse(data);-->
                    <#--if(jsonO.success){-->
                        <#--changeTime();-->
                    <#--}else{-->
                        <#--layerTipMsg(jsonO.success,"失败",jsonO.msg);-->
                    <#--}-->
                    <#--layer.close(ii);-->
                <#--},-->
                <#--error:function(XMLHttpRequest, textStatus, errorThrown){}-->
            <#--});-->
        <#--});-->
    }

</script>
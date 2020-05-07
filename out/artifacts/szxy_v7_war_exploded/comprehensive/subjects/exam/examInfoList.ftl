<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">
	<thead>
		<tr>
			<th class="text-center">序号</th>
			<th class="text-center">考试名称</th>
			<th class="text-center">年级</th>
            <th class="text-center">状态</th>
			<th class="text-center">科目设置</th>
			<th class="text-center">操作</th>
		</tr>
	</thead>
	<tbody>
	<#if examInfoList?exists>
	    <#list examInfoList as item>
		<tr>
		    <input type="hidden" class="tid" value="${item.id!}">
			<td class="text-center">${item_index+1!}</td>
			<td class="text-center">${item.examName!}</td>
			<td class="text-center">${item.gradeName!}</td>
			<td class="text-center">
			<#if '${item.isStat!}' == '1'>
			    <i class="fa fa-circle color-green"></i>已完成
			<#else>
			    <i class="fa fa-circle color-red"></i> 未完成
			</#if>
			</td>
			<td class="text-center">
				
				     <a href="javascript:doSubjectEdit('${item.examId!}','${item.gradeId!}');" class="table-btn show-details-btn">科目设置</a>
			
			</td>
			<td class="text-center">
			
				     <#if '${item.isStat!}' == '1'>
                         <a href="javascript:doEdit('${item.examId!}','${item.gradeId!}','${item.isCheackSubject!}');" class="table-btn">去查看</a>
			         <#else>
                         <a href="javascript:doEdit('${item.examId!}','${item.gradeId!}','${item.isCheackSubject!}');" class="table-btn">去设置</a>
			         </#if>
			
			</td>
		</tr>
		</#list>
		
	</#if>												
	</tbody>
</table>	
<div id="scoreEdit"></div>
<script type="text/javascript">

   function doSubjectEdit(id,gradeId){
        var url =  "${request.contextPath}/comprehensive/subjects/score/eaxmEdit?examId="+id+"&gradeId="+gradeId;
		$("#scoreEdit").load(url,function() {
			layerShow();
		});
   }
   
   function layerShow(){
     layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: '科目设置',
	    	area: '500px',
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    saveSubjects();
			},
	    	content: $('.layer-add')
	    });
    }
    
    function doEdit(examId,gradeId,isCheackSubject){
        if('0'==isCheackSubject){
            layerTipMsgWarn("提示","请先在科目设置中选择至少一个科目！");
            return;
        }
        var url =  '${request.contextPath}/comprehensive/subjects/score/toScoreEdit?examId='+examId+'&gradeId='+gradeId;
		$("#exammanageDiv").load(url);
    }

</script>

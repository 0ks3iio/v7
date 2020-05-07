<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">
			<div class="box-body clearfix">
                <div class="tab-container">
					<div class="tab-header clearfix">
						<ul class="nav nav-tabs nav-tabs-1">
							<#if (subjectInfoList?exists && subjectInfoList?size>0)>
								<#list subjectInfoList as item>
								<li <#if item_index==0>class="active" </#if>>
									<a href="#aa_${item_index}"  id="aa_${item_index}" data-toggle="tab" onclick="setSubjectParm('${item.subjectId!}','${isView!}')">${item.courseName!}</a>
								</li>
								</#list>
								<li>
									<a href="#aaAll"  id="aaAll" data-toggle="tab" onclick="setSubjectParm('all','${isView!}')">总分</a>
								</li>
								<#if isgk! == "1">
								<li>
									<a href="#aaConAll"  id="aaConAll" data-toggle="tab" onclick="setSubjectParm('conAll','${isView!}')">高考赋分总分</a>
								</li>
								</#if>
							</#if>
						</ul>
					</div>
					<!-- tab切换开始 -->
					<div class="tab-content" id="showTabDiv">
						
					</div><!-- tab切换结束 -->
			    </div>
			</div>
		</div>
	</div><!-- /.col -->
</div><!-- /.row -->

<script type="text/javascript">
$(function(){
	<#if (subjectInfoList?exists && subjectInfoList?size>0)>
		<#list subjectInfoList as item>
			<#if item_index==0>
				setSubjectParm('${item.subjectId!}','${isView}');
			</#if>
		</#list>
	</#if>
	showBreadBack(goBack,true,"成绩统计");
});
function goBack(){
	var url =  '${request.contextPath}/exammanage/scoreStat/head/page';
	$("#scoreStatDiv").load(url);
}
function setSubjectParm(subjectId,isView){
	var url =  '${request.contextPath}/exammanage/scoreStat/showParmBySubject/page?examId=${examId!}&subjectId='+subjectId+'&isView='+isView;
	$("#showTabDiv").load(url);
}
</script>


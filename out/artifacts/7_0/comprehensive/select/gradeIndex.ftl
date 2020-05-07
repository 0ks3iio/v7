<div class="box box-default">
	<div class="box-body clearfix">
		<div class="pull-left" style="width: 210px;">
			<div class="accordionContainer">
				<div class="panel-group" id="accordion">
					<#if dtoList?exists && dtoList?size gt 0 && paramMap?exists && paramMap?size gt 0>
					<#list dtoList as dto>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="collapsed clearfix a1-mark" data-toggle="collapse" data-parent="#accordion" href="#collapse${dto_index+1}" aria-expanded="true"><span class="pull-left">${dto.gradeName!}（${dto.openAcadyear!}年入学）</span><i class="pull-right fa fa-angle-down"></i></a>
                            </h4>
                        </div>
                        <div id="collapse${dto_index+1}" class="panel-collapse collapse">
                            <ul class="list-group">
                            	<#if dto.infoList?exists && dto.infoList?size gt 0>
                            	<#list dto.infoList as info>
                            	<#if dto.curkey?number gte info.gradeCode?number>
                            	<a class="list-group-item a2-mark" href="javascript:;" onClick="showDetailList('${info.id!}')">${paramMap[info.gradeCode!]!}</a>
                            	<#else>
                            	<a class="list-group-item disabled" href="javascript:;">${paramMap[info.gradeCode!]!}</a>
                            	</#if>
                            	</#list>
                            	</#if>
                            </ul>
                        </div>
                    </div>
                    </#list>
                    </#if>
                </div>
			</div>
		</div>
		<div style="margin-left: 230px;" id="detailDivId">
		</div>
    </div>
</div>
<script type="text/javascript">
	$(function(){
		$(".accordionContainer .list-group-item").click(function(){
			if($(this).hasClass("disabled")){
				return;
			}
			$(".accordionContainer .list-group-item").removeClass("active")
			$(this).addClass("active");
		})
		$("a.a1-mark:first").click();
		$("a.a2-mark:first").click();
	});
	function showDetailList(infoId){
	    var url = '${request.contextPath}/comprehensive/select/detailList/page?infoId='+infoId;
        $("#detailDivId").load(url);
	}
</script>
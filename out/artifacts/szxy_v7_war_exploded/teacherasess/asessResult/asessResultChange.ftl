<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="picker-table">
				<table class="table">
					<tbody>			
							<tr id="courseTr">
								<th width="150" style="vertical-align: top;">科目：</th>
								<td>
									<div class="outter3">
										<#if courses?exists && courses?size gt 0>
											<#list courses as item>
												<a class="subject-item <#if courseId?default('')==item.id>selected</#if>" href="javascript:void(0);" data-id="${item.id!}">${item.subjectName!}</a>
											</#list>
										</#if>
									</div>
								</td>
								<td width="75" style="vertical-align: top;">
									<div class="outter">
										<a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
									</div>
								</td>
							</tr>
					</tbody>
				</table>
			</div>
			<div class="table-container">
				<div class="table-container-body" id="showList2">
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	function gobackIndex(){
	    var url =  '${request.contextPath}/teacherasess/asess/index/page?acadyear=${teacherAsess.acadyear!}';
	    $(".model-div").load(url);
	}
	$(function(){
		showBreadBack(gobackIndex,true,"AB分层");
		//picker
		$('.picker-more').click(function(){
			if($(this).children('span').text()=='展开'){
				$(this).children('span').text('折叠');
				$(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
			}else{
				$(this).children('span').text('展开');
				$(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
			};
			$(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
	    });
	    
		doSearch();		
		$('.subject-item').on('click', function() {
			var subjectId = $(this).data('id');	
			$(".outter3").find("a").removeClass("selected");
			$(this).addClass("selected");
			var url ='${request.contextPath}/teacherasess/asessResult/asessResultChange/edit?subjectId='+subjectId+"&teacherAsessId="+'${teacherAsessId!}';
			$("#showList2").load(url);
		});
	})
	function doSearch(subjectId,resultType,classType){
		if(!subjectId){
			subjectId= $(".subject-item.selected").data('id');	
		}
		var url ='${request.contextPath}/teacherasess/asessResult/asessResultChange/edit?subjectId='+subjectId+"&teacherAsessId="+'${teacherAsessId!}';
		$("#showList2").load(url);
	}
	
</script>
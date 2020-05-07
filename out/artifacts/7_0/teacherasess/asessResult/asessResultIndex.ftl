<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter">
				<div class="filter-item">
					<span class="filter-name">方案名称：</span>
					<div class="filter-content">${teacherAsess.name!}</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">学年：</span>
					<div class="filter-content">${teacherAsess.acadyear!}</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">年级：</span>
					<div class="filter-content">${teacherAsess.gradeName!}</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">本次考核方案：</span>
					<div class="filter-content">${teacherAsess.convertName!}</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">原始参照方案：</span>
					<div class="filter-content">${teacherAsess.referConvertName!}</div>
				</div>
				<div class="filter-item filter-item-right">
					<button class="btn btn-white" onclick="export2();">导出</button>
				</div>	
			</div>
			<div class="picker-table">
				<table class="table">
					<tbody>			
						<tr>
							<th width="150" style="vertical-align: top;">结果类型：</th>
							<td colspan="2"> 
								<div class="outter1">
										<a class="result-item selected" href="javascript:void(0);" data-id="1">总名次排名表</a>
										<a class="result-item" href="javascript:void(0);" data-id="2">进步人次排名表</a>
										<a class="result-item" href="javascript:void(0);" data-id="3">最终考核排名表</a>
								</div>
							</td>
						</tr>
						<tr>
							<th width="150" style="vertical-align: top;">班级维度：</th>
							<td colspan="2"> 
								<div class="outter2">
									<a class="class-item selected" href="javascript:void(0);" data-id="1">行政班考核结果</a>
									<a class="class-item" href="javascript:void(0);" data-id="2">教学班考核结果</a>
								</div>
							</td>
						</tr>
							<tr id="courseTr" style="display:none">
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
		showBreadBack(gobackIndex,true,"查看结果");
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
		$('.result-item').on('click', function() {
			var resultType = $(this).data('id');	
			$(".outter1").find("a").removeClass("selected");
			$(this).addClass("selected");
			doSearch("",resultType,"");
		});
		$('.class-item').on('click', function() {
			var classType = $(this).data('id');	
			$(".outter2").find("a").removeClass("selected");
			$(this).addClass("selected");
			if(classType=="2"){
				$("#courseTr").show();
			}else{
				$("#courseTr").hide();
			}
			doSearch("","",classType);
		});
		$('.subject-item').on('click', function() {
			var subjectId = $(this).data('id');	
			$(".outter3").find("a").removeClass("selected");
			$(this).addClass("selected");
			doSearch(subjectId,"","");
			//var url ='${request.contextPath}/teacherasess/asessResult/asessResultList/page?subjectId='+subjectId+"&teacherAsessId="+'${teacherAsessId!}';
			//$("#showList2").load(url);
		});
	})
	function doSearch(subjectId,resultType,classType){
		if(!subjectId){
			subjectId= $(".subject-item.selected").data('id');	
		}
		if(!resultType){
			resultType= $(".result-item.selected").data('id');	
		}
		if(!classType){
			classType= $(".class-item.selected").data('id');	
		}
		var url ='${request.contextPath}/teacherasess/asessResult/asessResultList/page?subjectId='+subjectId+"&resultType="+resultType+"&classType="+classType+"&teacherAsessId="+'${teacherAsessId!}';
		$("#showList2").load(url);
	}
	function export2(){
	    var subjectId= $(".subject-item.selected").data('id');	
	    var resultType= $(".result-item.selected").data('id');	
	    var classType= $(".class-item.selected").data('id');	
	    var url = '${request.contextPath}/teacherasess/asessResult/asessResult/export?subjectId='+subjectId+"&resultType="+resultType+"&classType="+classType+"&teacherAsessId="+'${teacherAsessId!}';
	    window.location.href=url;
	}
	
</script>
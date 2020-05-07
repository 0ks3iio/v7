<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">所属教育局：</span>
				<div class="filter-content">${unitName!}</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">已上报学校数：</span>
				<div class="filter-content">${reportNum}所</div>
			</div>
		</div>
		<div class="picker-table" id="myPicker">
			<table class="table">
				<tbody>
					<tr>
						<th width="150"><#if searchType?default('')=='1'>科目：<#else>选课组合：</#if></th>
						<td>
							<div class="outter single-choice">
								<#list searchList as item>
								<a <#if item_index==0> class="selected"</#if> href="#" data-value="${item[0]!}" >${item[1]!}</a>
								</#list>
							</div>
						</td>
						<td width="75" style="vertical-align: top;">
							<div class="outter">
								<a class="picker-more" href="javascript:"><span>展开</span><i class="fa fa-angle-down"></i></a>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="mylisttable">
		
		</div>
	</div>
</div>
<script>
	$(function(){
		showBreadBack(toMyBack,true,"<#if searchType?default('')=='1'>单科选课统计<#else>组合选课统计</#if>");
		
		//选取条件,单选
	    $('#myPicker').on('click','.single-choice a',function(){
	    	$(this).toggleClass('selected').siblings().removeClass('selected');
	    	chooseBySubjectIds2($(this).attr("data-value"));
	    });
	    
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
		chooseBySubjectIds2('${subjectIds!}');
	})
	function toMyBack(){
		$("#showDate").load("${request.contextPath}/newgkelective/edu/itemList/page?type=1");
	}
	
	function chooseBySubjectIds2(subjectIds){
		$("#mylisttable").load("${request.contextPath}/newgkelective/edu/subjectsResultList/page?unitId=${unitId!}&gradeYear=${gradeYear!}&subjectIds="+subjectIds);
	}
</script>
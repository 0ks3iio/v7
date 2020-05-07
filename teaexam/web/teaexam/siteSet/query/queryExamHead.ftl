<div class="box box-default">
	<div class="box-body">
		<div class="filter" id="searchType">
			<div class="filter-item">
				<span class="filter-name">年份：</span>
                <div class="filter-content">
                    <select name="year" id="year" class="form-control" onchange="searchList()" style="width:135px">
                        <#list minYear..maxYear as item>
	                        <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
	                    </#list>			                       
                    </select>
                </div>
			</div>
		</div>						
		<div class="table-container" id="showList">
			
		</div>
	</div>
</div>


<script>
$(function(){	
	searchList();
});
function searchList(){
    var acadyear = $('#year').val();
    var url = "${request.contextPath}/teaexam/siteSet/query/list?year="+acadyear;
    $("#showList").load(url);
}
</script>

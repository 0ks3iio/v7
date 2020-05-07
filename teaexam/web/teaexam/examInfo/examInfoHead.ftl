
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
							<div class="filter-item">
								<span class="filter-name">类型：</span>
				                <div class="filter-content">
				                    <select name="type" id="type" class="form-control" onchange="searchList()" style="width:135px">
				                        <option value="0" <#if '${type!}'=='0'>selected="selected"</#if>>考试</option>	
				                        <option value="1" <#if '${type!}'=='1'>selected="selected"</#if>>培训</option>			                       
				                    </select>
				                </div>
							</div>
						<button class="btn btn-blue" onclick="addExam()" id="addBtn">发布考试</button>
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
    var semester = $('#type').val();
    if(semester==1){
    	$('#addBtn').text('发布培训');
    } else {
    	$('#addBtn').text('发布考试');
    }
    var url = "${request.contextPath}/teaexam/examInfo/examInfoList?year="+acadyear+"&type="+semester;
    $("#showList").load(url);
}
function addExam(){
    var acadyear = $('#year').val();
    var semester = $('#type').val();
    var url = "${request.contextPath}/teaexam/examInfo/examInfoEdit?year="+acadyear+"&type="+semester+"&state=1";
    $(".model-div").load(url);
}
</script>

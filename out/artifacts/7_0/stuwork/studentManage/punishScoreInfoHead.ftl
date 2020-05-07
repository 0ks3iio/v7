<div class="main-content" id="importDiv">
	<div class="main-content-inner">
		<div class="page-content">
			<div class="box box-default">
				<div class="box-body">
                    <div class="filter">
						<div class="filter-item">
							<span class="filter-name">学年：</span>
							<div class="filter-content">
								<select name="acadyearId" id="acadyearId" class="form-control" onChange="searchList();">
									<#if acadyearList?exists && acadyearList?size gt 0>
										<#list acadyearList as acadyear>
											<option value="${acadyear!}" <#if acadyear == nowAcadyear>selected</#if>>${acadyear!}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">学期：</span>
							<div class="filter-content">
								<select name="semesterId" id="semesterId" class="form-control" onChange="searchList();">
									<option value="1" <#if "1"==nowSemester>selected</#if>>第一学期</option>
									<option value="2" <#if "2"==nowSemester>selected</#if>>第二学期</option>
								</select>
							</div>
						</div>
						<div class="filter-item">
							<span class="filter-name">姓名：</span>
							<div class="filter-content">
								<input type="text" class="form-control" id="stuName">
							</div>
							<a class="btn btn-blue" onClick="searchList();">查询</a>
						</div>									
					</div>
					<div class="table-container-body" id="showList">
		            </div>
				</div>
		    </div>
	    </div>
	</div>
<div id="punishEdit" class="layer layer-add">
</div>
</div>
<script>
$(function(){		
	searchList();
});

function searchList(){
    var studentName = $.trim(encodeURI($('#stuName').val()));
    var url = "${request.contextPath}/stuwork/studentManage/punishScoreInfoList?acadyear=" + $("#acadyearId").val() + "&semester=" + $("#semesterId").val() + "&studentName=" + studentName;
    $("#showList").load(url);
}
</script>

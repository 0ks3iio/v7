<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">
	      <div class="box-body">
		  <!-- PAGE CONTENT BEGINS -->
				<div class="filter clearfix">
					<div class="filter-item">
						<label for="" class="filter-name">学年：</label>
							<div class="filter-content">
								<select class="form-control" id="searchAcadyear" name="searchAcadyear" onChange="showList()">
									<#if acadyearList?exists && (acadyearList?size>0)>
					                    <#list acadyearList as item>
						                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
					                    </#list>
				                    <#else>
					                    <option value="">未设置</option>
				                     </#if>
								</select>
							</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">学期：</label>
							<div class="filter-content">
								<select class="form-control" id="searchSemester" name="searchSemester" onChange="showList()">
									${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
								</select>
							</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">类型：</label>
							<div class="filter-content">
								<select class="form-control" style="width:180px;" id="searchType" name="searchType" onChange="showList()">
									<option value="1">本单位设定的考试</option>
				                    <#if unitClass?default(-1) == 2>
				                        <option value="2">直属教育局设定的考试</option>
				                        <option value="3">参与的校校联考</option>
				                    </#if>
								</select>
							</div>
					</div>
					<div class="filter-item pull-right">
						<a class="btn btn-blue js-addTerm" onclick="addExam();">+新增考试</a>
					</div>
				</div><!-- 筛选结束 -->
				<div class="table-wrapper" id="showList">
				</div>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(function(){
		showList();
	});
	function showList(){
		 var searchAcadyear = $('#searchAcadyear').val();
	     var searchSemester = $('#searchSemester').val();
		 var searchType = $('#searchType').val();
		 var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&searchType="+searchType;
	     var url =  '${request.contextPath}/scoremanage/examInfo/list/page'+str;
		 $("#showList").load(url);
	}
	
	function addExam(){
	    var searchAcadyear = $('#searchAcadyear').val();
	    var searchSemester = $('#searchSemester').val();
		var searchType = $('#searchType').val();
		var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&searchType="+searchType;
	    var url = "${request.contextPath}/scoremanage/examInfo/edit/page"+str;
	    indexDiv = layerDivUrl(url,{title: "信息",width:750,height:600});
	}
</script>







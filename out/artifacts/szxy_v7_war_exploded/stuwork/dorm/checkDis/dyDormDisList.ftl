<div class="box box-default">
	<div class="box-body" id="searchType">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" onchange="doSearch()" class="form-control">
						<#if acadyearList?exists && acadyearList?size gt 0>
							<#list acadyearList as item >
								<option value="${item!}" <#if item==dormDto.acadyear?default("")>selected="selected"</#if>>${item!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semesterStr" id="semesterStr" onchange="doSearch()" class="form-control">
						<option value="1" <#if "1"==dormDto.semesterStr?default("")>selected="selected"</#if>>第一学期</option>
						<option value="2" <#if "2"==dormDto.semesterStr?default("")>selected="selected"</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学段：</span>
				<div class="filter-content">
					<select name="section" id="section" class="form-control" onchange="doSearch()">
						<#if sectionList?exists && sectionList?size gt 0>
							<#list sectionList as item >
								<option value="${item!}" <#if item==dormDto.section?default(0)>selected="selected"</#if>><#if item==0>幼儿园<#elseif item==1>小学<#elseif item==2>初中<#elseif item==3>高中<#else>剑桥高中</#if></option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">寝室楼：</span>
				<div class="filter-content">
					<select name="searchBuildId" id="searchBuildId" class="form-control" onchange="doSearch()" style="width:120px">
	                    <#if buildingList?? && (buildingList?size>0)>
	                    	<option value="">全部</option>
	                        <#list buildingList as building>
	                        <option value="${building.id!}" <#if building.id==dormDto.searchBuildId?default("")>selected="selected"</#if>>${building.name!}</option>
	                        </#list>
	                    </#if> 
	                  </select>
				</div>
			</div>
			<div class="filter-item classDiv" >
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select vtype="selectOne" name="classId" id="classId" class="form-control input-sm" data-placeholder="" onchange="doSearch()">
						<option value="">全部</option>
						<#if clazzList?exists && (clazzList?size>0)>
							<#list clazzList as clazz>
								<option value="${clazz.id!}" <#if clazz.id==dormDto.classId?default("")>selected="selected"</#if> >${clazz.classNameDynamic!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item studentDiv">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<select vtype="selectOne" name="studentId" id="studentId" class="form-control input-sm" data-placeholder="" onchange="doSearch()">
						<option value="">全部</option>
						<#if studentList?exists && (studentList?size>0)>
							<#list studentList as student>
								<option value="${student.id!}" <#if student.id==dormDto.studentId?default("")>selected="selected"</#if> >${student.studentName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
		</div>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>姓名</th>
					<th>班级</th>
					<th>寝室楼</th>
					<th>寝室号</th>
					<th>累计各次扣分</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#if checkDisList?exists && checkDisList?size gt 0>
					<#list checkDisList as item>
						<tr>
							<td>${item.studentName!}</td>
							<td>${item.className!}</td>
							<td>${item.buildingName!}</td>
							<td>${item.roomName!}</td>
							<td>${(item.score?default(0))?string("0.#")}</td>
							<td><a  onclick="getDetail('${item.studentId!}')">查看详情</a></td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
		
	</div>
</div>
<script>
$(function(){
	//初始化单选控件
		var viewContent={
			'allow_single_deselect':'false',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '140px',//输入框的宽度
			'margin-right':'40px',
			'results_height' : '150px'//下拉选择的高度
		}
		initChosenOne(".classDiv","",viewContent);
		initChosenOne(".studentDiv","",viewContent);
});
function doSearch(){
	$("#itemShowDivId").load("${request.contextPath}/stuwork/dorm/checkDis/list?"+searchUrlValue("#searchType"));
}
function getDetail(studentId){
	var acadyear=$("#acadyear").val();
	var semesterStr=$("#semesterStr").val();
	var url="${request.contextPath}/stuwork/dorm/checkDis/detail?studentId="+studentId+"&acadyear="+acadyear+"&semesterStr="+semesterStr;
	indexDiv = layerDivUrl(url,{title: "个人违纪明细",width:600,height:350});
}
</script>

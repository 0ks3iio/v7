<div class="picker-table">
	<table class="table">
		<tbody>			
			<tr id="courseSearch">
				<th width="150" style="vertical-align: top;">科目：</th>
				<td>
					<div class="outter">
						<a class="selected" href="javascript:" data-value="" onClick="chooseMoreSearch('courseSearch','')" id="courseSearch_">全部</a>
						<#if courseNameMap?exists && courseNameMap?size gt 0>
						<#list courseNameMap?keys as key>
						<a <#if subjectIdsMap?exists && subjectIdsMap[course.id]?exists>class="selected"</#if> href="javascript:" data-value="${key!}" id="courseSearch_${key!}"  onClick="chooseMoreSearch('courseSearch','${key!}')">${courseNameMap[key]!}</a>
						</#list>
						</#if>
					</div>
				</td>
				<td width="75" style="vertical-align: top;">
					<div class="outter">
						<a class="picker-more" href="javascript:"><span>展开</span><i class="fa fa-angle-down"></i></a>
					</div>
				</td>
			</tr>
			<tr id="subjectSearch">
				<th>考试类型：</th>
				<td>
					<div class="outter">
						<a class="selected" href="javascript:" data-value="" onClick="chooseMoreSearch('subjectTypeSearch','')" id="subjectTypeSearch">全部</a>
						<#if subjectTypeMap?exists && subjectTypeMap?size gt 0>
						<#list subjectTypeMap?keys as key>
						<a href="javascript:" data-value="${key!}" onClick="chooseMoreSearch('subjectTypeSearch','${key!}')" id="subjectTypeSearch${key!}">${subjectTypeMap[key]!}</a>
						</#list>
						</#if>
					</div>
				</td>
				<td></td>
			</tr>
			<tr id="bestSearch">
				<th>班级类型：</th>
				<td>
					<div class="outter">
						<a class="selected" href="javascript:" data-value="" onClick="chooseMoreSearch('bestTypeSearch','')" id="bestTypeSearch">全部</a>
						<#if bestTypeMap?exists && bestTypeMap?size gt 0>
						<#list bestTypeMap?keys as key>
						<a href="javascript:" data-value="${key!}" onClick="chooseMoreSearch('bestTypeSearch','${key!}')" id="bestTypeSearch${key!}">${bestTypeMap[key]!}</a>
						</#list>
						</#if>
					</div>
				</td>
				<td></td>
			</tr>
			<tr id="placeSearch">
				<th>教室：</th>
				<td>
					<div class="outter">
						<a class="selected" href="javascript:" data-value="" onClick="chooseMoreSearch('placeSearch','')" id="placeSearch_">全部</a>
						<#if placeNameMap?exists && placeNameMap?size gt 0>
						<#list placeNameMap?keys as key>
						<a href="javascript:" data-value="${key!}" onClick="chooseMoreSearch('placeSearch','${key!}')" id="placeSearch_${key!}">${placeNameMap[key]!}</a>
						</#list>
						</#if>
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
<#--
<div class="picker">
	<ul class="picker-list">
		<li class="picker-item" id="courseSearch">
			<span class="picker-name">科目：</span>
			<div class="picker-content">
				<ul class="picker-value">
					<li class="selected"><a href="javascript:" data-value="" onClick="chooseMoreSearch('courseSearch','')" id="courseSearch_">全部</a></li>
					<#if courseNameMap?exists && courseNameMap?size gt 0>
					<#list courseNameMap?keys as key>
					<li <#if subjectIdsMap?exists && subjectIdsMap[course.id]?exists>class="selected"</#if>><a href="javascript:" data-value="${key!}" id="courseSearch_${key!}"  onClick="chooseMoreSearch('courseSearch','${key!}')">${courseNameMap[key]!}</a></li>
					</#list>
					</#if>
				</ul>
			</div>
		</li>
		<li class="picker-item" id="subjectSearch">
			<span class="picker-name">考试类型：</span>
			<div class="picker-content">
				<ul class="picker-value">
					<li class="selected"><a href="javascript:" data-value="" onClick="chooseMoreSearch('subjectTypeSearch','')" id="subjectTypeSearch">全部</a></li>
					<#if subjectTypeMap?exists && subjectTypeMap?size gt 0>
					<#list subjectTypeMap?keys as key>
					<li><a href="javascript:" data-value="${key!}" onClick="chooseMoreSearch('subjectTypeSearch','${key!}')" id="subjectTypeSearch${key!}">${subjectTypeMap[key]!}</a></li>
					</#list>
					</#if>
				</ul>
			</div>
		</li>
		<li class="picker-item" id="bestSearch">
			<span class="picker-name">班级类型：</span>
			<div class="picker-content">
				<ul class="picker-value">
					<li class="selected"><a href="javascript:" data-value="" onClick="chooseMoreSearch('bestTypeSearch','')" id="bestTypeSearch">全部</a></li>
					<#if bestTypeMap?exists && bestTypeMap?size gt 0>
					<#list bestTypeMap?keys as key>
					<li><a href="javascript:" data-value="${key!}" onClick="chooseMoreSearch('bestTypeSearch','${key!}')" id="bestTypeSearch${key!}">${bestTypeMap[key]!}</a></li>
					</#list>
					</#if>
				</ul>
			</div>
		</li>
		<li class="picker-item" id="placeSearch">
			<span class="picker-name">教室：</span>
			<div class="picker-content">
				<ul class="picker-value">
					<li class="selected"><a href="javascript:" data-value="" onClick="chooseMoreSearch('placeSearch','')" id="placeSearch_">全部</a></li>
					<#if placeNameMap?exists && placeNameMap?size gt 0>
					<#list placeNameMap?keys as key>
					<li><a href="javascript:" data-value="${key!}" onClick="chooseMoreSearch('placeSearch','${key!}')" id="placeSearch_${key!}">${placeNameMap[key]!}</a></li>
					</#list>
					</#if>
				</ul>
			</div>
		</li>
	</ul>
</div>
-->
<div class="filter">
	<div class="filter-item">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
		        	<input type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="输入教师姓名查询" name="searchTex" id="searchTex">
		        </div>
			    <div class="input-group-btn">
			    	<button type="button" class="btn btn-default">
				    	<i class="fa fa-search"></i>
				    </button>
			    </div>
		    </div>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<button class="btn btn-blue">打印</button>
		<button class="btn btn-blue">导出到Excel</button>
	</div>
</div>

<div class="table-container">
	<div class="table-container-header">共<#if dtoList?exists>${dtoList?size!}<#else>0</#if>份结果</div>
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>班级名称</th>
					<th>考试类型</th>
					<th>班级类型</th>
					<th>教室</th>
					<th>任课老师</th>
					<th>总人数</th>
					<th>男</th>
					<th>女</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
				<#list dtoList as item>
				<tr>
					<td>${item_index+1}</td>
					<td>${item.className!}</td>
					<td>${item.subjectType!}</td>
					<td>${item.bestType!"平行班"}</td>
					<td>
					<#if item.placeIdSet?exists && item.placeIdSet?size gt 0>
					<#list item.placeIdSet as placeId>
					<#if placeId_index+1 < item.placeIdSet?size>
					${placeNameMap[placeId]!}、
					<#else>
					${placeNameMap[placeId]!}
					</#if>
					</#list>
					</#if>	
					</td>
					<td>
					<#if item.teacherIdSet?exists && item.teacherIdSet?size gt 0>
					<#list item.teacherIdSet as teacherId>
					<#if teacherId_index+1 < item.teacherIdSet?size>
					${teacherNameMap[teacherId]!}、
					<#else>
					${teacherNameMap[teacherId]!}
					</#if>
					</#list>
					</#if>
					</td>
					<td>${item.studentNum!}</td>
					<td>${item.boyNum!}</td>
					<td>${item.girlNum!}</td>
					<td><a href="javascript:void(0);" onClick="searchList('${item.classId!}');">查看学生</a></td>
				</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>

<script>

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

	function searchList(classId){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectStudentResult?classId='+classId;
		$("#showList").load(url);	
	}
	
	function findByCondition(){
		var parmTex=searchDtoUrl();
		<!--
		var url='${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectStudentResult'+parmTex;
		$("#tableList").load(encodeURI(url));
		-->
	}
	
	function searchDtoUrl(){
		var parmTex="subjectIds="+makeUlDatas("courseSearch");
		parmTex=parmTex+"&subjectType="+makeUlDatas("subjectTypeSearch");
		parmTex=parmTex+"&bestType="+makeUlDatas("bestTypeSearch");
		parmTex=parmTex+"&placeIds="+makeUlDatas("placeSearch");
		var searchTex=$("#searchTex").val();
		searchTex=$.trim(searchTex);
		parmTex=parmTex+"&searchTex="+searchTex;
		return parmTex;
	}
	function makeUlDatas(objId){
		var parmm="";
		$("#"+objId).find("a").each(function(){
			var dataValue=$(this).attr("data-value");
			if($(this).hasClass("selected")){
				if(dataValue==""){
					parmm="";
					return false;
				}else{
					parmm=parmm+","+dataValue;
				}
			}
		});
		if(parmm!=""){
			parmm=parmm.substring(1);
		}
		
		return parmm;
	}
	
	function chooseMoreSearch(objUlId,dataValues){
		if(dataValues!=""){
			// 标签本身添加选中
			var objUl=$("#"+objUlId+"_"+dataValues);
			if($(objUl).hasClass("selected")){
				$(objUl).removeClass("selected");
			}else{
				$(objUl).addClass("selected");
			}
			// 
			if("courseSearch"==objUlId || "placeSearch"==objUlId){
				var ff=false;
				$("#"+objUlId).find("a").each(function(){
					var dataValueA=$(this).attr("data-value");
					if(dataValueA!="" && $(this).hasClass("selected")){
						ff=true;
						return false;
					}
				});
				if(ff){
					$("#"+objUlId+"_").removeClass("selected");
				}else{
					if(!$("#"+objUlId+"_").hasClass("selected")){
						$("#"+objUlId+"_").addClass("selected");
					}
				}
			}
		}else{
			$("#"+objUlId).find("a").each(function(){
				var dataValueA=$(this).attr("data-value");
				if(dataValueA!=dataValues && $(this).hasClass("selected")){
					$(this).removeClass("selected");
				}
			});
			if(!$("#"+objUlId+"_").hasClass("selected")){
				$("#"+objUlId+"_").addClass("selected");
			}
		}
		
		findByCondition();
	}
	
</script>
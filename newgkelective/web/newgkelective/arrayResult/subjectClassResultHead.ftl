<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="picker-table">
	<table class="table">
		<tbody>			
			<tr id="courseSearch">
				<th width="150" style="vertical-align: top;">科目：</th>
				<td>
					<div class="outter">
						<a class="selected" href="javascript:" data-value="" onClick="chooseMoreSearch('courseSearch','')" id="courseSearch_">全部</a>
						<#if courseList?exists && courseList?size gt 0>
						<#list courseList as course>
						<a href="javascript:" data-value="${course.id!}" id="courseSearch_${course.id!}"  onClick="chooseMoreSearch('courseSearch','${course.id!}')">${course.subjectName!}</a>
						</#list>
						</#if>
					</div>
				</td>
				<td width="75">
				</td>
			</tr>
			<tr id="subjectTypeSearch" <#if isXzbArray!>style="display:none;"</#if>>
				<th>考试类型：</th>
				<td>
					<div class="outter">
						<a class="selected" href="javascript:" data-value="" onClick="chooseSingleSearch('subjectTypeSearch','')" id="subjectTypeSearch_">全部</a>
						<#if subjectTypeMap?exists && subjectTypeMap?size gt 0>
						<#list subjectTypeMap?keys as key>
						<a href="javascript:" data-value="${key!}" onClick="chooseSingleSearch('subjectTypeSearch','${key!}')" id="subjectTypeSearch_${key!}">${subjectTypeMap[key]!}</a>
						</#list>
						</#if>
					</div>
				</td>
				<td></td>
			</tr>
			<#--tr id="bestSearch">
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
			</tr-->
			<tr id="placeSearch">
				<th>场地：</th>
				<td>
					<div class="outter">
						<a href="javascript:" data-value="" onClick="chooseMoreSearch('placeSearch','')" id="placeSearch_">全部</a></li>
						<#if placeNameMap?exists && placeNameMap?size gt 0>
						<#list placeNameMap?keys as placeId>
						<#if placeId_index = 0>
							<a class="selected" href="javascript:" data-value="${placeId!}" onClick="chooseMoreSearch('placeSearch','${placeId!}')" id="placeSearch_${placeId!}">${placeNameMap[placeId]!}</a>
						<#else>
							<a href="javascript:" data-value="${placeId!}" onClick="chooseMoreSearch('placeSearch','${placeId!}')" id="placeSearch_${placeId!}">${placeNameMap[placeId]!}</a>
						</#if>
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



<#--<div class="picker">
	<ul class="picker-list">
		<li class="picker-item" id="courseSearch">
			<span class="picker-name">科目：</span>
			<div class="picker-content">
				<ul class="picker-value">
					<li class="selected"><a href="javascript:" data-value="" onClick="chooseMoreSearch('courseSearch','')" id="courseSearch_">全部</a></li>
					<#if courseNameMap?exists && courseNameMap?size gt 0>
					<#list courseNameMap?keys as key>
					<li><a href="javascript:" data-value="${key!}" id="courseSearch_${key!}"  onClick="chooseMoreSearch('courseSearch','${key!}')">${courseNameMap[key]!}</a></li>
					</#list>
					</#if>
				</ul>
			</div>
		</li>
		<li class="picker-item" id="subjectTypeSearch">
			<span class="picker-name">考试类型：</span>
			<div class="picker-content">
				<ul class="picker-value">
					<li class="selected"><a href="javascript:" data-value="" onClick="chooseSingleSearch('subjectTypeSearch','')" id="subjectTypeSearch_">全部</a></li>
					<#if subjectTypeMap?exists && subjectTypeMap?size gt 0>
					<#list subjectTypeMap?keys as key>
					<li><a href="javascript:" data-value="${key!}" onClick="chooseSingleSearch('subjectTypeSearch','${key!}')" id="subjectTypeSearch_${key!}">${subjectTypeMap[key]!}</a></li>
					</#list>
					</#if>
				</ul>
			</div>
		</li>-->
		<#--
		<li class="picker-item" id="bestTypeSearch">
			<span class="picker-name">班级类型：</span>
			<div class="picker-content">
				<ul class="picker-value">
					<li class="selected"><a href="javascript:" data-value="" onClick="chooseSingleSearch('bestTypeSearch','')" id="bestTypeSearch_">全部</a></li>
					<#if bestTypeMap?exists && bestTypeMap?size gt 0>
					<#list bestTypeMap?keys as key>
					<li><a href="javascript:" data-value="${key!}" onClick="chooseSingleSearch('bestTypeSearch','${key!}')" id="bestTypeSearch_${key!}">${bestTypeMap[key]!}</a></li>
					</#list>
					</#if>
				</ul>
			</div>
		</li>
		-->
		<#--
		<li class="picker-item" id="placeSearch">
			<span class="picker-name">教室：</span>
			<div class="picker-content">
				<ul class="picker-value" style="height:auto">
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
</div>-->
<div class="filter">
	<div class="filter-item filter-item-left">
		<@htmlcomponent.printToolBar container=".print"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		<a href="javascript:doExportAll();" class="btn btn-white">导出全部课表</a>
		<a href="javascript:doExportAllStu();" class="btn btn-white" style="display:none">导出各班学生名单</a>
	</div>
</div>
<div class="table-container print" id="detailList">
</div>
<script>
	$(document).ready(function(){
		findByCondition();
		
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
	});
	
	function findByCondition(){
		var parmTex=searchDtoUrl();
		var url='${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectClassResult/list/page?'+parmTex;
		$("#detailList").load(url);
	}
	
	function searchDtoUrl(){
		var parmTex="subjectIds="+makeUlDatas("courseSearch");
		parmTex=parmTex+"&subjectType="+makeUlDatas("subjectTypeSearch");
		parmTex=parmTex+"&bestType="+makeUlDatas("bestTypeSearch");
		parmTex=parmTex+"&placeIds="+makeUlDatas("placeSearch");
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
			var objUl=$("#"+objUlId+"_"+dataValues);
			if($(objUl).hasClass("selected")){
				$(objUl).removeClass("selected");
			}else{
				$(objUl).addClass("selected");
			}
			
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
	
	function chooseSingleSearch(objUlId,dataValues){
		if(dataValues!=""){
			var objUl=$("#"+objUlId+"_"+dataValues);
			if($(objUl).hasClass("selected")){
				$(objUl).removeClass("selected");
				
				$("#"+objUlId+"_").addClass("selected");
			}else{
				$(objUl).addClass("selected");
				
				$("#"+objUlId).find("a").each(function(){
					var dataValueA=$(this).attr("data-value");
					if(dataValueA!=dataValues && $(this).hasClass("selected")){
						$(this).removeClass("selected");
					}
				});
			}
		}else{
			
			$("#"+objUlId).find("a").each(function(){
				var dataValueA=$(this).attr("data_value");
				if(dataValueA!="" && $(this).hasClass("selected")){
					$(this).removeClass("selected");
				}
			});
			$("#"+objUlId+"_").addClass("selected");
		}
		findByCondition();
		
	}
	
	function doExportAll(){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/class/exportTimetableAll?classType=2';
	  	document.location.href=url;
	}
	
	function doExportAllStu(){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/exportJxbClaStu';
	  	document.location.href=url;
	}
</script>
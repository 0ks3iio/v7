<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#--<a href="javascript:" class="page-back-btn gotoChoiceClass"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-body">
		<#if scourceType?default('') != '9'>
		<div class="nav-tabs-wrap">
		    <ul id = 'genre' class="nav nav-tabs nav-tabs-1 " role="tablist">
		        <li role="presentation" <#if chosenType?default('1')=='1'>class="active"</#if>><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList('1')">已选</a></li>
		        <li role="presentation" <#if chosenType?default('1')=='0'>class="active"</#if>><a href="#bb" id="bb" role="tab" data-toggle="tab" onclick="itemShowList('0')">未选</a></li>
		        <li role="presentation" <#if chosenType?default('1')=='2'>class="active"</#if>><a href="#bb" id="bb" role="tab" data-toggle="tab" onclick="goChoiceResult('${choiceId!}')">选课结果统计</a></li>
				<li role="presentation"><a href="#bb" id="changeLog" role="tab" data-toggle="tab" onclick="goChangeLog('${choiceId!}')">选课变更记录</a></li>
				<#if isGradeRole?default(false)><li role="presentation"><a href="#bb" id="resourceEva" role="tab" data-toggle="tab" onclick="goResource('${choiceId!}')">资源评估</a></li></#if>
			</ul>
		</div>
		</#if>
		<div class="tab-content">
			<div class="picker-table" id="selDiv">
				<table class="table">
					<tbody>			
						<tr id="courseSearch" <#if chosenType?default('1')=='0'>style="display:none"</#if>>
							<th width="150" style="vertical-align: top;">科目：</th>
							<td>
								<div class="outter course-mark">
									<#if courseList?exists && courseList?size gt 0>
										<#list courseList as course>
										<a <#if subjectIdsMap?exists && subjectIdsMap[course.id]?exists>class="selected"</#if> href="javascript:" data-value="${course.id!}" id="courseSearch_${course.id!}"  onclick="chooseMoreSearch('courseSearch','${course.id!}')">${course.subjectName!}</a>
										</#list>
									</#if>
								</div>
							</td>
							<td></td>
						</tr>
						<tr id="clazzSearch">
							<th width="150">行政班：</th>
							<td>
								<div class="outter">
									<a class="selected" href="javascript:" data-value="" onclick="chooseMoreSearch('clazzSearch','')" id="clazzSearch_">全部</a>
									<#if clazzList?exists && clazzList?size gt 0>
									<#list clazzList as clazz>
									<a href="javascript:" data-value="${clazz.id!}" onclick="chooseMoreSearch('clazzSearch','${clazz.id!}')" id="clazzSearch_${clazz.id!}">${clazz.classNameDynamic!}</a>
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
						<tr id="sexSearch">
							<th>性别：</th>
							<td>
								<div class="outter">
									<a class="selected" href="javascript:" data-value="" onclick="chooseSingleSearch('sexSearch','')" id="sexSearch_">全部</a>
									<a href="javascript:" data-value="1" onclick="chooseSingleSearch('sexSearch','1')" id="sexSearch_1">男</a>
									<a href="javascript:" data-value="2" onclick="chooseSingleSearch('sexSearch','2')" id="sexSearch_2">女</a>
								</div>
							</td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="filter" id="buttonDiv">
				<div class="filter-item" id="chosenButton" <#if chosenType?default('1')=='0'>style="display:none"</#if>>
					<button class="btn btn-blue" onclick="doChoosenExport();">导出已选名单</button>
					<#if scourceType?default('') != '9'>
					<#if !isClassRole?default(false)>
					<button class="btn btn-blue" onclick="unchoosenImport(1);">导入选课</button>
					</#if>
					<#--<button class="btn btn-white" onclick="choosenScoreImport();">导入成绩</button>-->
					<button class="btn btn-white" onclick="choosenLock('1');">全部锁定</button>
					<button class="btn btn-white" onclick="choosenLock('0');">全部解锁</button>
					</#if>
				</div>
				<div class="filter-item" id="unchosenButton" <#if chosenType?default('1')=='1'>style="display:none"</#if>>
					<#if !isClassRole?default(false)>
					<button class="btn btn-blue" onclick="unchoosenImport(0);">导入选课</button>
					</#if>
					<button class="btn btn-blue" onclick="unchoosenExport();">导出未选名单</button>
					<button class="btn btn-blue js-saveSelectResult" onclick="unchoosenSave();" >保存</button>
				</div>
				<div class="filter-item filter-item-right">
					<div class="filter-content">
						<div class="input-group input-group-search">
					        <select name="searchType" id="searchType" class="form-control">
					        	<option value="2">学号</option>
					        	<option value="1">姓名</option>
					        </select>
					        <div class="pos-rel pull-left">
					        	<input type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" name="searchTex" id="searchTex">
					        </div>
						    
						    <div class="input-group-btn">
						    	<a href="javascript:" type="button" class="btn btn-default" onclick="findByCondition()">
							    	<i class="fa fa-search"></i>
							    </a>
						    </div>
					    </div><!-- /input-group -->
					</div>
				</div>
			</div>
			
			<div id="chooseDiv">
		
			</div>
		</div>
		
	</div>
</div>
<script>
	showBreadBack(toBackChoice,false,"选课结果");
	function toBackChoice(){
		<#if scourceType?default('') != '9'>
		var url = '${request.contextPath}/newgkelective/${gradeId!}/goChoice/index/page';
		$("#showList").load(url);
		<#else>
		if(clickGrade){
			clickGrade();
		}
		</#if>
	}
	
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
	
	var chosenType="";
	$(document).ready(function(){ 
		chosenType="${chosenType?default('1')}";
		var classIds = "${classIds?default('')}";
		if(classIds!=''){
			$("#clazzSearch_").removeClass("selected");
			$("#clazzSearch_").siblings().each(function(){
				if(classIds.indexOf($(this).attr("data-value"))>=0){
					$(this).addClass("selected");
				}
			})
			$('#clazzSearch .picker-more').click();
		} else {
			if($("#courseSearch").find("a.selected").length==0){
				$("#clazzSearch_").removeClass("selected");
				$("#clazzSearch_").next().addClass('selected');
			}
		}
		findByCondition();
	})

	function goChoiceResult(choiceId) {
		$("#buttonDiv").hide();
		$("#selDiv").hide();
		var url = '${request.contextPath}/newgkelective/' + choiceId + '/choiceResult/list/head';
		$("#chooseDiv").load(url);
	}

	function goChangeLog(choiceId) {
		$("#buttonDiv").hide();
		$("#selDiv").hide();
		var url = '${request.contextPath}/newgkelective/' + choiceId + '/change/log/list/page';
		$("#chooseDiv").load(url);
	}

	function goResource(choiceId) {
		$("#buttonDiv").hide();
		$("#selDiv").hide();
		var url = '${request.contextPath}/newgkelective/' + choiceId + '/resource/evaluation/list/page';
		$("#chooseDiv").load(url);
	}
	
	function itemShowList(chooseType){
		$("#buttonDiv").show();
		$("#selDiv").show();
		
		if(chooseType=="1"){
			$("#courseSearch").show();
			$("#intentionSearch").show();
			$("#chosenButton").show();
			$("#unchosenButton").hide();
		}else{
			$("#courseSearch").hide();
			$("#intentionSearch").hide();
			$("#chosenButton").hide();
			$("#unchosenButton").show();
			$(".course-mark a").removeClass('selected');
		}
		if($("#courseSearch").find("a.selected").length==0){
			$("#clazzSearch_").removeClass('selected').siblings().removeClass('selected');
			$("#clazzSearch_").next().addClass('selected');
		}
		$("#sexSearch_").addClass('selected').siblings().removeClass('selected');
		chosenType=chooseType;
		findByCondition();
	}
	
	function findByCondition(isMaster){
		$("#chooseDiv").empty();
		var parmTex=searchDtoUrl();
		var url='${request.contextPath}/newgkelective/${choiceId!}/chosen/list/page'+parmTex+'&isMaster='+isMaster;
		$("#chooseDiv").load(encodeURI(url));
	}
	
	function searchDtoUrl(){
		var parmTex="?scourceType=${scourceType!}&chosenType="+chosenType;
		if(chosenType=="1"){
			parmTex=parmTex+"&subjectIds="+makeUlDatas("courseSearch");
		}
		parmTex=parmTex+"&classIds="+makeUlDatas("clazzSearch");
		parmTex=parmTex+"&sex="+makeUlDatas("sexSearch");
		parmTex=parmTex+"&searchType="+$("#searchType").val();
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
		
		if("sexSearch"==objId && parmm!="" && parmm.length>1){
			parmm="";
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
			
			if("clazzSearch"==objUlId){
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
				
				$("#"+objUlId+"_").parent().addClass("selected");
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
	
	function choosenScoreImport(){
		<#--
			<#if refscoreId?default('')!=''>
				layer.confirm("已经有参考成绩，是否需要重新进入导入成绩？", {
		         	 title: ['提示','font-size:20px;'],
		         	 btn: ['是','否'] //按钮
		        }, function(){
		        	layer.closeAll();
		            doImport();
		        }, function(){
		            layer.closeAll();
		        });
		     <#else>
		     	 doImport();
		     </#if>
	     -->
	     doImport();
	}
	

	function doImport(){
	    var chioceId = '${choiceId!}';
	    var url = "${request.contextPath}/newgkelective/newGkScoreResult/index?chioceId="+chioceId;
	    $("#showList").load(url);
	}
	
	function unchoosenImport(chosenType){
	    var chioceId = '${choiceId!}';
	    var url = "${request.contextPath}/newgkelective/newGkChoResult/import/main?chioceId="+chioceId+"&chosenType="+chosenType;
	    $("#showList").load(url);
	}
	
	function doChoosenExport(){
		var filterType = $(".adjust-filter.btn-blue").attr("adjustType");
		var url = "${request.contextPath}/newgkelective/${choiceId!}/selected/export?filterType=";
		if (filterType == "adjust-filter-no") {
			url += "1";
		} else if (filterType == "adjust-filter-can") {
			url += "2";
		} else if (filterType == "adjust-filter-priority") {
			url += "3";
		} else {
			url += "";
		}
		document.location.href=url;
	}
	
	function unchoosenExport(){
		var url = "${request.contextPath}/newgkelective/${choiceId!}/unselect/export";
		document.location.href=url;
	}

</script>

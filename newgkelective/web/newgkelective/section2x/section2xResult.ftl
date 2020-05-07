<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<form id="resultForm" name="resultForm" method="post" >
<div class="box box-default">
	<input type="hidden" name="sectionId" value="${newGkSection.id!}">
	<div class="box-header header_filter">
		<h3 class="box-title">参数</h3>
		<div class="filter">
			<#--
			<div class="filter-item">
				<label for="" class="filter-name">开设行政班数量：</label>
				<div class="filter-content">
				${newGkSection.openClassNum!0}
				</div>
			</div>-->
			
			<div class="filter-item">
				<label for="" class="filter-name">班级人数：</label>
				<div class="filter-content">
				${newGkSection.meanSize!0}
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">浮动人数：</label>
				<div class="filter-content">
				${newGkSection.marginSize!0}
				</div>
			</div>
		</div>
	</div>
	<div class="box-body" id="section2xDiv">
	<div class="row">
	<#if xList?exists && xList?size gt 0>
	<div class="col-sm-12">
	<table class="table table-bordered table-striped table-hover openXTable" tyle="margin-top:10px">
	<tr>
		<th>
			科目
		</th>
		<#list xList as xEx>
			<td>${xEx[1]!}</td>
		</#list>
	</tr>
	</tr>
		<th>
			人数
		</th>
		<#list xList as xEx>
			<td class="subject_${xEx[0]!}">${xEx[2]!}</td>
		</#list>
	</tr>
	</table>
	</div>
	</#if>
	</div>
	<#if (beginList?exists && beginList?size gt 0) || (dtoList?exists && dtoList?size gt 0) || (resultList?exists && resultList?size gt 0)>
	<div class="row">
	<div class="col-sm-6">
		<table class="table table-bordered openGroupTable" style="margin-top:10px">
		<thead>
			<tr>
				<th>班级</th>
				<th>科目组合</th>
				<th>来源组合</th>
				<th>总人数</th>
				<th>组合</th>
				<th>人数</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#if resultList?exists && resultList?size gt 0>
			<#list resultList as ex>
				<#assign subject3Map=ex.subject3Map>
				<#if subject3Map?exists && subject3Map?size gt 0>
					<#assign sizeNum=subject3Map?size>
					<#assign ii=0>
					<#list subject3Map?keys as key>
						<#if ii==0>
						<tr class="result_${ex_index}" data-value="result_${ex_index}" data-id="${ex.itemId!}" data-index="${ex_index}" data-subjectIds="${ex.resultSubs!}">
							<td rowspan="${sizeNum}">
								<input type="hidden" name="sectionResultList[${ex_index}].id" value="${ex.itemId!}"/>
								<input type="hidden" name="sectionResultList[${ex_index}].subjectIds" value="${ex.resultSubs!}"/>
								<input type="hidden" name="sectionResultList[${ex_index}].subjectType" value="${ex.subjectType!}"/>
								<input type="hidden" name="sectionResultList[${ex_index}].arrangeType" value="${ex.arrangeType!}"/>
								<input type="hidden" name="sectionResultList[${ex_index}].groupClassId" value="${ex.groupClassId!}"/>
								<input type="text" name="sectionResultList[${ex_index}].groupName" readonly value="${ex.groupName!}"/>
							</td>
							<td rowspan="${sizeNum}">${ex.resultSubNames!}</td>
							<td rowspan="${sizeNum}">${ex.subjectNames!}</td>
							<td rowspan="${sizeNum}">${ex.allStudengNum?default(0)}</td>
							<td data-value="${subjectName3Map[key]!}"><input type="hidden" name="sectionResultList[${ex_index}].studentScourceSub" class="studentScourceSub" value="${key!}"/>${subjectName3Map[key]!}</td>
							<td><input type="text" name="sectionResultList[${ex_index}].studentScourceNum" readonly class="studentScourceNum" value="${subject3Map[key]?default(0)}"/>
							</td>
							<td rowspan="${sizeNum}" data-value="${ex.subjectIds!}"><a href="javascript:void(0)" onclick="deleteGroupClass(this)">删除</a></td>
						</tr>
						<#else>
						<tr class="result_${ex_index}" data-value="result_${ex_index}">
							<td data-value="${subjectName3Map[key]!}"><input type="hidden" name="sectionResultList[${ex_index}].studentScourceSub" class="studentScourceSub" value="${key!}"/>${subjectName3Map[key]!}</td>
							<td><input type="text" name="sectionResultList[${ex_index}].studentScourceNum" readonly class="studentScourceNum" value="${subject3Map[key]?default(0)}"/>
							</td>
						</tr>
						</#if>
						<#assign ii=ii+1>
					</#list>
				</#if>

			</#list>
			</#if>
		</tbody>
	</table>
	<table class="table table-bordered openThreeTable" style="margin-top:10px">
		<thead>
			<tr>
				<th>3科组合</th>
				<th>总人数</th>
				<th>安排人数</th>
				<th>算法安排人数</th>
				<th>剩余人数</th>
				<th>是否参与</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#if beginList?exists && beginList?size gt 0>
			<#list beginList as ex>
			<tr class="sub3_${ex.subjectIds}">
				<td>${ex.groupName!}</td>
				<td>${ex.studentNum?default(0)}</td>
				<td>${ex.arrangeNum?default(0)}</td>
				<td>${(ex.leftArrangeNum?default(0))}</td>
				<td>${(ex.studentNum?default(0)-ex.arrangeNum?default(0)-ex.leftArrangeNum?default(0))}</td>
				
				<td><#if ex.noJoin?default(1)==0>不参与</#if></td>
				<td><a href="javascript:void(0)" onclick="moveNum(this)">移动</a></td>
			</tr>
			</#list>
			<#else>
			</#if>
		</tbody>
	</table>
	</div>
	<div class="col-sm-6">
	<table class="table table-bordered openTwoTable" style="margin-top:10px">
		<tbody>
			<tr>
				<th>2科组合</th>
				<th>总人数</th>
				<th>已开班级数</th>
				<th>组合</th>
				<th>人数</th>
				<th width="20%">已开设班级</th>
				<th>操作</th>
			</tr>
			<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as ex>
				<#assign subject3Map=ex.subject3Map>
				<#if subject3Map?exists && subject3Map?size gt 0>
					<#assign sizeNum=subject3Map?size>
					<#assign ii=0>
					<#list subject3Map?keys as key>
						<#if ii==0>
						<tr class="sub_${ex.subjectIds}" data-value="${ex.subjectIds}" data-itemId="${ex.itemId!}" data-num='${ex_index}'>
							<td rowspan="${sizeNum}" data-value="${ex.subjectNames!}">
							<input type="hidden" name="sectionEndList[${ex_index}].id" value="${ex.itemId!}">
							<input type="hidden" name="sectionEndList[${ex_index}].subjectIds" value="${ex.subjectIds!}">
							${ex.subjectNames!}</td>
							<td rowspan="${sizeNum}">${ex.allStudengNum?default(0)}</td>
							<#--<td rowspan="${sizeNum}">${ex.openClassNum?default(0)}</td>-->
							<#if ex.clazzList ?exists && ex.clazzList?size gt 0>
								<td rowspan="${sizeNum}">${ex.clazzList?size}</td>
							<#else>
								<td rowspan="${sizeNum}">0</td>
							</#if>
							<td data-value="${key!}"  data-name="${subjectName3Map[key]!}"> 
							<input type="hidden" name="sectionEndList[${ex_index}].studentScourceSub" value="${key!}">
							${subjectName3Map[key]!}</td>
							<td><input type="text" name="sectionEndList[${ex_index}].studentScourceNum"  readonly class="studentScourceNum" value="${subject3Map[key]?default(0)}">
								<a href="javascript:void(0)" onclick="adj2StudentNum(this)">移动</a>
							</td>
							<td rowspan="${sizeNum}">
							
							<#if ex.clazzList ?exists && ex.clazzList?size gt 0>
								<#list  ex.clazzList  as item>
									<span class="mb10 mr10 w155" data-id="${item[0]!}" data-index="">${item[1]!}</span>
								</#list>
							</#if>
							
							</td>
							<td rowspan="${sizeNum}">
							<a href="javascript:void(0)" onclick="openClass(this)">开班</a>
							</td>
						</tr>
						<#else>
						<tr class="sub_${ex.subjectIds}">
							<td data-value="${key!}" data-name="${subjectName3Map[key]!}"> 
							<input type="hidden" name="sectionEndList[${ex_index}].studentScourceSub" value="${key!}">
							${subjectName3Map[key]!}</td>
							<td><input type="text" name="sectionEndList[${ex_index}].studentScourceNum" readonly class="studentScourceNum" value="${subject3Map[key]?default(0)}">
								<a href="javascript:void(0)" onclick="adj2StudentNum(this)">移动</a>
							</td>
						</tr>
						</#if>
						<#assign ii=ii+1>
					</#list>
				</#if>

			</#list>
			</#if>
		</tbody>
	</table>
	</div>
	</div>
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/gkelective/images/noOpenClass.png" alt="">
			</span>
		</div>
	</div>
	</#if>
</div>

</div>
</form>
<div class="layer layer-opensubjects">
	<div class="layer-content">
		<input type="hidden" class="subjectIds" value=""/>
		<div style="max-height:350px;overflow-y:scroll;">
		<table class="table table-bordered table-striped table-hover" style="margin-bottom:0px;">
			<thead>
				<tr>
					<th width="40%">组合</th>
					<th width="40%">人数</th>
					<th width="20%">取出人数</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		</div>
	</div>
</div>

<div class="layer layer-moveStudentNum">
	<div class="layer-content">
		<input type="hidden" class="subjectIds" value=""/>
		<input type="hidden" class="subjectNames" value=""/>
		<div style="max-height:350px;overflow-y:scroll;">
		<table class="table table-bordered table-striped table-hover" style="margin-bottom:0px;">
			<thead>
				<tr>
					<th width="40%">2科组合</th>
					<th width="40%">移动人数</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		</div>
	</div>
</div>

<div class="navbar-fixed-bottom opt-bottom">
    <a href="javascript:" class="btn btn-blue" onclick="saveSectionResult()">保存</a>
</div>
<script type="text/javascript">
//最终班级保存序号
var resultIndex=0;
<#if resultList?exists && resultList?size gt 0>
	resultIndex=${resultList?size};
</#if>

var courseMap={};
<#if courseMap?exists && courseMap?size gt 0>
<#list courseMap?keys as key>
	courseMap['${key}']='${courseMap[key].shortName?default("")}';
	if(courseMap['${key}']==""){
		courseMap['${key}']='${courseMap[key].subjectName?default("")}';
	}
</#list>
</#if>

$(function(){
	showBreadBack(toBack,false,"2+x");
})
function toBack(){
	$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/section2x/index/page");
}
//开设班级名称后面序号
var openNum=1;
function openClass(obj){
	var $tr=$(obj).parents("tr");
	var sub2Ids=$($tr).attr("data-value");
	var sub2ItemId=$($tr).attr("data-itemid");
	var sub2Name=$($tr).find("td:eq(0)").attr("data-value");
	//选中的所有行
	var myClassName="sub_"+sub2Ids;
	var subsMap={};
	var subsNameMap={};
	var n=0;
	$("."+myClassName).each(function(){
		if(n==0){
			var sub3Id=$(this).find("td:eq(3)").attr("data-value");
			var subName=$(this).find("td:eq(3)").attr("data-name");
			var subNum=$(this).find("td:eq(4)").find(".studentScourceNum").val();
			subsMap[sub3Id]=parseInt(subNum);
			subsNameMap[sub3Id]=subName;
		}else{
			var sub3Id=$(this).find("td:eq(0)").attr("data-value");
			var subName=$(this).find("td:eq(0)").attr("data-name");
			var subNum=$(this).find("td:eq(1)").find(".studentScourceNum").val();
			subsMap[sub3Id]=parseInt(subNum);
			subsNameMap[sub3Id]=subName;
		}
		n++;
	});
	
	
	//打开弹出框
	var $tbody=$(".layer-opensubjects").find("tbody");
	$tbody.html("");
	for(var kkk in subsMap){
		var addRows=addSubjectsRow(kkk,subsNameMap[kkk],subsMap[kkk],);
		$($tbody).append(addRows);
	}
	
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '开班设置',
		area: '520px',
		btn: ['确定', '取消'],
		scrollbar:false,
		btn1:function(){
			var f=checkValue(".layer-opensubjects");
			if(!f){
				return;
			}
			var returnSubNumMap={};
			var returnSubNameMap={};
			var allStuNum=0;
			var ss=0;
			var ff=false;
			$(".layer-opensubjects").find("tbody").find("tr").each(function(){
				var oldNum=parseInt($(this).find("td:eq(1)").html());
				var chooseNumStr=$(this).find(".chooseNum").val()
				if(chooseNumStr!=""){
					var chooseNum=parseInt(chooseNumStr);
					if(chooseNum>oldNum){
						ff=true;
						layer.tips("不能超过人数", $(this).find(".chooseNum"), {
							tipsMore: true,
							tips:3		
						});
						return false;
					}
					if(chooseNum>0){
						var chooseSub3Ids=$(this).attr("data-value");
						var chooseSub3Names=$(this).find("td:eq(0)").html();
						returnSubNumMap[chooseSub3Ids]=chooseNum;
						returnSubNameMap[chooseSub3Ids]=chooseSub3Names;
						allStuNum=allStuNum+chooseNum;
						ss++;
					}
				}
			})
			if(ff){
				return;
			}
			
			if(ss>0){
				//开班openNum
				$groupTbody=$(".openGroupTable").find("tbody");
				var dd=0;
				if(ss==1){
					//是否独立成班
					layer.confirm("是否独立成班，3+0", function(){
						var classNames="";
						for(var kkk in returnSubNumMap){
							if(dd==0){
								classNames=returnSubNameMap[kkk]+openNum;
								$($groupTbody).append(addResultRow(ss,"2",kkk,returnSubNameMap[kkk],returnSubNameMap[kkk]+openNum,sub2Ids,sub2Name,allStuNum,kkk,returnSubNameMap[kkk],returnSubNumMap[kkk],sub2ItemId));
							}else{
								$($groupTbody).append(addResultRow(0,"","","","","","",0,kkk,returnSubNameMap[kkk],returnSubNumMap[kkk],""));
							}
							dd++;
						}
						$($tr).find("td:eq(5)").append('<span class="mb10 mr10 w155" data-id="" data-index="'+resultIndex+'">'+classNames+'<span>');
						resultIndex++;
						openNum++;
						deleteNum(myClassName,returnSubNumMap);
						updateThreeSub(returnSubNumMap);
						//某一个科目的人数
						//kkk 移除sub2Ids的科目
						var x=sub3movesub2(kkk,sub2Ids);
						if(x!=""){
							var openxtd=$(".openXTable").find(".subject_"+x);
							console.log(openxtd);
							if(openxtd){
								var xNum=$(openxtd).html();
								var xNumInt=parseInt(xNum)-allStuNum;
								$(openxtd).html(""+xNumInt);
							}
						}
						
						layer.closeAll();
					},function(){
						for(var kkk in returnSubNumMap){
							if(dd==0){
								$($groupTbody).append(addResultRow(ss,"2",sub2Ids,sub2Name,sub2Name+openNum,sub2Ids,sub2Name,allStuNum,kkk,returnSubNameMap[kkk],returnSubNumMap[kkk],sub2ItemId));
							}else{
								$($groupTbody).append(addResultRow(0,"","","","","","",0,kkk,returnSubNameMap[kkk],returnSubNumMap[kkk],""));
							}
							dd++;
						}
						$($tr).find("td:eq(5)").append('<span class="mb10 mr10 w155" data-id="" data-index="'+resultIndex+'">'+sub2Name+openNum+'<span>');
						openNum++;
						resultIndex++
						deleteNum(myClassName,returnSubNumMap);
						updateThreeSub(returnSubNumMap);
						layer.closeAll();
					})
					
				}else{
					for(var kkk in returnSubNumMap){
						if(dd==0){
							$($groupTbody).append(addResultRow(ss,"2",sub2Ids,sub2Name,sub2Name+openNum,sub2Ids,sub2Name,allStuNum,kkk,returnSubNameMap[kkk],returnSubNumMap[kkk],sub2ItemId));
						}else{
							$($groupTbody).append(addResultRow(0,"","","","","","",0,kkk,returnSubNameMap[kkk],returnSubNumMap[kkk],""));
						}
						dd++;
					}
					$($tr).find("td:eq(5)").append('<span class="mb10 mr10 w155" data-id="" data-index="'+resultIndex+'">'+sub2Name+openNum+'<span>');
					
					openNum++;
					resultIndex++;
					deleteNum(myClassName,returnSubNumMap);
					updateThreeSub(returnSubNumMap);
					layer.closeAll();
				}
				
				
				
				
			}
			
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-opensubjects')
	});
}
//3科 2科 获得x
function sub3movesub2(sub3,sub2){
	var arr=sub3.split("_");
	for(var i=0;i<arr.length;i++){
		if(sub2.indexOf(arr[i])>-1){
			continue;
		}else{
			return arr[i];
		}
	}
	return "";
}

//开班成功后3科table数量变化
function updateThreeSub(subNumMap){
	//安排人数增多  算法剩余人数减少
	for(var mmm in subNumMap){
		var classNames='sub3_'+mmm;
		if($(".openThreeTable").find("."+classNames)){
			var choTr=$(".openThreeTable").find("."+classNames);
			var arrNum=$(choTr).find("td:eq(2)").html();
			var arrNumInt=parseInt(arrNum)+subNumMap[mmm];
			$(choTr).find("td:eq(2)").html(""+arrNumInt);
			var leftNum=$(choTr).find("td:eq(3)").html();
			var leftNumInt=parseInt(leftNum)-subNumMap[mmm];
			$(choTr).find("td:eq(3)").html(""+leftNumInt);
		}
	}
}
//开班后 2科table数量变化
function deleteNum(myClassName,returnMap){
	//原来要减去人数
	var m=0;
	$("."+myClassName).each(function(){
		if(m==0){
			var subIds=$(this).find("td:eq(3)").attr("data-value");
			var subNum=$(this).find("td:eq(4)").find(".studentScourceNum").val();
			if(returnMap[subIds]){
				var mm=parseInt(subNum)-returnMap[subIds];
				$(this).find("td:eq(4)").find(".studentScourceNum").val(mm);
			}
			//班级数+1
			var openClassNum=$(this).find("td:eq(2)").html();
			var openClassNumInt=parseInt(openClassNum)+1;
			$(this).find("td:eq(2)").html(""+openClassNumInt);
		}else{
			var subIds=$(this).find("td:eq(0)").attr("data-value");
			var subNum=$(this).find("td:eq(1)").find(".studentScourceNum").val();
			if(returnMap[subIds]){
				var mm=parseInt(subNum)-returnMap[subIds];
				$(this).find("td:eq(1)").find(".studentScourceNum").val(mm);
			}
		}
		m++;
	});
}

//rowspans 总行数,subjectType 类型,resultSubIds 组合科目,resultSubNames组合科目名
//groupName 班级名 groupSubIds 来源科目,groupSubNames 来源组合名,allStuNum 总人数,groupSub3Ids 三科目,groupSub3Names,stunum 人数
//groupClassId 来源科目对应endId
function addResultRow(rowspans,subjectType,resultSubIds,resultSubNames,groupName,groupSubIds,groupSubNames,allStuNum,groupSub3Ids,groupSub3Names,stunum,groupClassId){
	var $html="";
	if(rowspans>0){
		$html='<tr class="result_'+resultIndex+'" data-value="result_'+resultIndex+'" data-id="" data-index="'+resultIndex+'" data-subjectIds="'+resultSubIds+'"  ><td rowspan="'+rowspans+'" >'
			+'<input type="hidden" name="sectionResultList['+resultIndex+'].id" value="">'
			+'<input type="hidden" name="sectionResultList['+resultIndex+'].subjectIds" value="'+resultSubIds+'">'
			+'<input type="hidden" name="sectionResultList['+resultIndex+'].subjectType" value="'+subjectType+'">'
			+'<input type="hidden" name="sectionResultList['+resultIndex+'].arrangeType" value="1"/>'
			+'<input type="hidden" name="sectionResultList['+resultIndex+'].groupClassId" value="'+groupClassId+'">'
			+'<input type="text" name="sectionResultList['+resultIndex+'].groupName" readonly value="'+groupName+'">'
			+'</td>';
			$html=$html	+'<td rowspan="'+rowspans+'">'+resultSubNames+'</td>'
			+'<td rowspan="'+rowspans+'">'+groupSubNames+'</td>'
			+'<td rowspan="'+rowspans+'">'+allStuNum+'</td>'
		
			+'<td data-value="'+groupSub3Names+'"><input type="hidden" name="sectionResultList['+resultIndex+'].studentScourceSub" class="studentScourceSub" value="'+groupSub3Ids+'">'+groupSub3Names+'</td>'
			+'<td><input type="text" name="sectionResultList['+resultIndex+'].studentScourceNum" readonly class="studentScourceNum" value="'+stunum+'"></td>'
			+'<td rowspan="'+rowspans+'" data-value="'+groupSubIds+'"><a href="javascript:void(0)" onclick="deleteGroupClass(this)">删除</a></td>';
	
	}else{
		$html='<tr class="result_'+resultIndex+'" data-value="result_'+resultIndex+'"><td data-value="'+groupSub3Names+'"><input type="hidden" name="sectionResultList['+resultIndex+'].studentScourceSub"  class="studentScourceSub" value="'+groupSub3Ids+'"/>'+groupSub3Names+'</td>'
			+'<td><input type="text" name="sectionResultList['+resultIndex+'].studentScourceNum" readonly class="studentScourceNum" value="'+stunum+'"/></td></tr>';
	}
	return $html;
}
function addSubjectsRow(subsIds,subNames,subnums){
	var $html='<tr data-value="'+subsIds+'"><td>'+subNames+'</td><td>'+subnums+'</td><td><input type="text" value="0" vtype="int" nullable = "false" class="form-control chooseNum" id="key_'+subsIds+'"></td></tr>';
	return $html;
}



var isSubmit=false;
function saveSectionResult(){
	if(isSubmit){
		return ;
	}
	isSubmit=true;
	var options = {
		url : '${request.contextPath}/newgkelective/${divideId!}/section2x/saveSectionResult',
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isSubmit=false;
	 			return;
	 		}else{
	 			layer.closeAll();
	 			layer.msg("保存成功", {
					offset: 't',
					time: 2000
				});
				toRefesh();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#resultForm").ajaxSubmit(options);
}
function toRefesh(){
	var url =  '${request.contextPath}/newgkelective/${divideId!}/section2x/result/page?sectionId=${newGkSection.id!}';
	$("#showList").load(url);
}


function deleteGroupClass(obj){
	var objTd=$(obj).parents("td");
	var objTr=$(obj).parents("tr");
	var objClassName=$(objTr).attr("data-value");
	var subIds=$(objTd).attr("data-value");
	
	//组合
	var resultSubjectIds=$(objTr).attr("data-subjectIds");
	
	
	var sub3NumMap={};
	var sub3NumName={};
	var allStu=0;
	$(".openGroupTable").find("."+objClassName).each(function(){
		var sub3=$(this).find(".studentScourceSub").val();
		var sub3Num=$(this).find(".studentScourceNum").val();
		var sub3NumInt=parseInt(sub3Num);
		sub3NumMap[sub3]=sub3NumInt;
		sub3NumName[sub3]=$(this).find(".studentScourceSub").parent("td").attr("data-value");
		allStu=allStu+sub3NumInt;
	});
	
	var dataId=$(objTr).attr("data-id");
	var dataIndex=$(objTr).attr("data-index");
	
	var ff=	false;
	if(subIds!=""){
		//移除到2科数据
		//找到2科中=subIds  sub_00000000000000000000000000000002_00000000000000000000000000000006
		var ss=$(".openTwoTable").find(".sub_"+subIds).length;
		if(ss>0){
			ff=true;
			//已经存在组合
			var ss=0;
			var firstTd;
			var lastTd;
			var arrMap={};
			$(".openTwoTable").find(".sub_"+subIds).each(function(){
				var sub3Ids="";
				if(ss==0){
					//第一行
					firstTd=$(this);
					sub3Ids=$(this).find("td:eq(3)").attr("data-value");
				}else{
					sub3Ids=$(this).find("td:eq(0)").attr("data-value");
				}
				if(sub3NumMap[sub3Ids]){
					//存在 增加人员
					var sub3Num=$(this).find(".studentScourceNum").val();
					var sub3NumInt=parseInt(sub3Num)+sub3NumMap[sub3Ids];
					$(this).find(".studentScourceNum").val(""+sub3NumInt);
					arrMap[sub3Ids]=sub3Ids;
				}
				lastTd=$(this);
				ss++;
			})
			var sss=0;
			var $htmlTr="";
			var dataNum=$(firstTd).attr("data-num");
			for(var mmm in sub3NumMap){
				if(!arrMap[mmm] && sub3NumMap[mmm]>0){
					//lastTd后面需要增加行数
					sss++;
					$htmlTr=$htmlTr+'<tr class="sub_'+subIds+'">'
							+'<td data-value="'+mmm+'" data-name="'+sub3NumName[mmm]+'">'
							+'<input type="hidden" name="sectionEndList['+dataNum+'].studentScourceSub" value="'+mmm+'">'
							+sub3NumName[mmm]+'</td>'
							+'<td><input type="text" name="sectionEndList['+dataNum+'].studentScourceNum" readonly class="studentScourceNum" value="'+sub3NumMap[mmm]+'"><a href="javascript:void(0)" onclick="adj2StudentNum(this)">移动</a></td>'
						    +'</tr>';
				}
			}
			if(sss>0){
				var hhh=ss+sss;
				$(firstTd).find("td:eq(0)").attr("rowspan",hhh);
				$(firstTd).find("td:eq(1)").attr("rowspan",hhh);
				$(firstTd).find("td:eq(2)").attr("rowspan",hhh);
				$(firstTd).find("td:eq(5)").attr("rowspan",hhh);
				$(firstTd).find("td:eq(6)").attr("rowspan",hhh);
				$(lastTd).after($htmlTr);
			}
			//修改总人数
			var oldStuNum=$(firstTd).find("td:eq(1)").html();
			var stuNumInt=parseInt(oldStuNum)+allStu;
			$(firstTd).find("td:eq(1)").html(""+stuNumInt);
			
			//修改3科这边人数 安排人数减少 算法啊那批人数增多
			for(var mmm in sub3NumMap){
				var choTr=$(".openThreeTable").find(".sub3_"+mmm);
				if(choTr){
					var arrNum=$(choTr).find("td:eq(2)").html();
					var arrNumInt=parseInt(arrNum)-sub3NumMap[mmm];
					var leftNum=$(choTr).find("td:eq(3)").html();
					var leftNumInt=parseInt(leftNum)+sub3NumMap[mmm];
					$(choTr).find("td:eq(2)").html(""+arrNumInt);
					$(choTr).find("td:eq(3)").html(""+leftNumInt);
				}
			}
			//开设班级数量减少
			var oldNum=$(firstTd).find("td:eq(2)").html();
			var newNum=parseInt(oldNum)-1;
			$(firstTd).find("td:eq(2)").html(""+newNum);
			//开设班级删除
			var spanNum=$(firstTd).find("td:eq(5)").find("span").length;
			if(spanNum>0){
				$(firstTd).find("td:eq(5)").find("span").each(function(){
					var dataId1=$(this).attr("data-id");
					var dataIndex1=$(this).attr("data-index");
					if(dataId!="" ){
						if(dataId1==dataId){
							$(this).remove();
							return false;
						}
					}else if(dataIndex==dataIndex1){
						$(this).remove();
						return false;
					}
				})
			}
			for(var mmm in sub3NumMap){
				//3+0到2+0是增加x
				if(subIds!=mmm){
					var x=sub3movesub2(mmm,subIds);
					if(x!=""){
						var openxtd=$(".openXTable").find(".subject_"+x);
						if(openxtd){
							var xNum=$(openxtd).html();
							var xNumInt=parseInt(openxtd)+sub3NumMap[mmm];
							$(openxtd).html(""+xNumInt);
						}
					}
				}
			}
		}
	}
	if(!ff){
		//直接移除到3科这边
		for(var mmm in sub3NumMap){
			var choTr=$(".openThreeTable").find(".sub3_"+mmm);
			if(choTr){
				var arrNum=$(choTr).find("td:eq(2)").html();
				
				var arrNumInt=parseInt(arrNum)-sub3NumMap[mmm];
				var leftNum=$(choTr).find("td:eq(4)").html();
				
				var leftNumInt=parseInt(leftNum)+sub3NumMap[mmm];
				$(choTr).find("td:eq(2)").html(""+arrNumInt);
				$(choTr).find("td:eq(4)").html(""+leftNumInt);
			}
			
			//对应x减少
			if(resultSubjectIds!=mmm){
				var x=sub3movesub2(mmm,resultSubjectIds);
				if(x!=""){
					var openxtd=$(".openXTable").find(".subject_"+x);
					if(openxtd){
						var xNum=$(openxtd).html();
						var xNumInt=parseInt(openxtd)-sub3NumMap[mmm];
						$(openxtd).html(""+xNumInt);
					}
				}
			}
			
		}
		
		
		
	}
	
	//要移除
	$(".openGroupTable").find("."+objClassName).remove();
	
}
function moveNum(obj){
	var trObj=$(obj).parents("tr");
	var leftNum=$(trObj).find("td:eq(4)").html();
	var leftNumInt=parseInt(leftNum);
	if(leftNumInt==0){
		layer.msg("没有剩余人数可以调整", {
			offset: 't',
			time: 2000
		});
		return;
	}
	//调整去向
	//sub3_00000000000000000000000000000003_00000000000000000000000000000006_00000000000000000000000000000011
	var sub3IdsArr=$(trObj).attr("class").split("_");
	var sub3IdsName=$(trObj).find("td:eq(0)").html();
	var sub3Ids=sub3IdsArr[1]+"_"+sub3IdsArr[2]+"_"+sub3IdsArr[3];
	//2科去向
	var sub2Id1=sub3IdsArr[1]+"_"+sub3IdsArr[2];
	var sub2Id2=sub3IdsArr[1]+"_"+sub3IdsArr[3];
	var sub2Id3=sub3IdsArr[2]+"_"+sub3IdsArr[3];
	var length1=$(".openTwoTable").find(".sub_"+sub2Id1).length;
	var length2=$(".openTwoTable").find(".sub_"+sub2Id2).length;
	var length3=$(".openTwoTable").find(".sub_"+sub2Id3).length;
	if(length1+length2+length3==0){
		layer.msg("没有去向的2科组合可以调整", {
			offset: 't',
			time: 2000
		});
		return;
	}
	//打开弹出框 
	var $tbody=$(".layer-moveStudentNum").find("tbody");
	$tbody.html("");
	if(length1>0){
		var sub2Name=$(".openTwoTable").find(".sub_"+sub2Id1+":first").find("td:eq(0)").attr("data-value");
		var addRows=addMoveRow(sub2Id1,sub2Name);
		$($tbody).append(addRows);
	}
	if(length2>0){
		var sub2Name=$(".openTwoTable").find(".sub_"+sub2Id2+":first").find("td:eq(0)").attr("data-value");
		var addRows=addMoveRow(sub2Id2,sub2Name);
		$($tbody).append(addRows);
	}
	if(length3>0){
		var sub2Name=$(".openTwoTable").find(".sub_"+sub2Id3+":first").find("td:eq(0)").attr("data-value");
		var addRows=addMoveRow(sub2Id3,sub2Name);
		$($tbody).append(addRows);
	}
	
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '移动设置',
		area: '520px',
		btn: ['确定', '取消'],
		scrollbar:false,
		btn1:function(){
			var num=0;
			var numMap={};
			//验证输入的参数是否准确
			$($tbody).find("tr").each(function(){
				var moveNum=$(this).find(".moveNum").val();
				var moveNumInt=parseInt(moveNum);
				var sub2=$(this).attr("data-value");
				if(moveNumInt>0){
					numMap[sub2]=moveNumInt;
					num=num+moveNumInt;
				}
			})
			if(num==0){
				//没有调整
				layer.msg("没有调整", {
					offset: 't',
					time: 2000
				});
				layer.closeAll();
			}else{
				if(num>leftNumInt){
					//超过剩余人数
					layer.msg("超过剩余人数,请修改", {
						offset: 't',
						time: 2000
					});
					
				}else{
					//可以调整
					//两个组合--总人数增多
					for(var kkk  in numMap){
						//2科组合增加
						var ss=0;
						var firstTd=null;
						var chooseTd=null;
						var lastTd=null;
						var arrMap={};
						$(".openTwoTable").find(".sub_"+kkk).each(function(){
							var choosesub3Ids="";
							if(ss==0){
								//第一行
								firstTd=$(this);
								choosesub3Ids=$(this).find("td:eq(3)").attr("data-value");
							}else{
								choosesub3Ids=$(this).find("td:eq(0)").attr("data-value");
							}
							if(choosesub3Ids==sub3Ids){
								//存在
								chooseTd=$(this);
								return false;
							}
							lastTd=$(this);
							ss++;
						})
						if(chooseTd!=null){
							//存在
							var sub3Num=$(chooseTd).find(".studentScourceNum").val();
							var sub3NumInt=parseInt(sub3Num)+numMap[kkk];
							$(chooseTd).find(".studentScourceNum").val(""+sub3NumInt);
						}else{
							//不存在 添加一行
							var dataNum=$(firstTd).attr("data-num");
							var $htmlTr='<tr class="sub_'+kkk+'">'
										+'<td data-value="'+sub3Ids+'" data-name="'+sub3IdsName+'">'
										+'<input type="hidden" name="sectionEndList['+dataNum+'].studentScourceSub" value="'+sub3Ids+'">'
										+sub3IdsName+'</td>'
										+'<td><input type="text" name="sectionEndList['+dataNum+'].studentScourceNum" readonly class="studentScourceNum" value="'+numMap[kkk]+'"><a href="javascript:void(0)" onclick="adj2StudentNum(this)">移动</a></td>'
									    +'</tr>';
							var hhh=ss+1;
							$(firstTd).find("td:eq(0)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(1)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(2)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(5)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(6)").attr("rowspan",hhh);		    
							$(lastTd).after($htmlTr);
						}
					
						//修改总人数
						var oldStuNum=$(firstTd).find("td:eq(1)").html();
						var stuNumInt=parseInt(oldStuNum)+numMap[kkk];
						$(firstTd).find("td:eq(1)").html(""+stuNumInt);
						
					}
					//3科 剩余人数减少 安排人数增多
					//num
					var arrNum=$(trObj).find("td:eq(3)").html();
					var arrNumInt=parseInt(arrNum)+num;
					$(trObj).find("td:eq(3)").html(""+arrNumInt);
					var finalLeftNum=leftNumInt-num;
					$(trObj).find("td:eq(4)").html(""+finalLeftNum);
					layer.closeAll();
				}
			}
			
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-moveStudentNum')
	});
	
}

function addMoveRow(ibjId,objName){
	var $html='<tr data-value="'+ibjId+'"><td>'+objName+'</td><td><input type="text" value="0" vtype="int" nullable = "false" class="form-control moveNum" id="key_'+ibjId+'"></td></tr>';
	return $html;
}
//2科移动到2科
function adj2StudentNum(obj){
	var tdObj=$(obj).parents("td");
	var trObj=$(obj).parents("tr");
	var left2Num=$(trObj).find(".studentScourceNum").val();
	var left2NumInt=parseInt(left2Num);
	
	if(left2NumInt<=0){
		layer.msg("没有调整", {
			offset: 't',
			time: 2000
		});
		return;
	}
	var subject3="";
	var subject3Name="";
	var sub2Obj=$(trObj).attr("class").substring(4);
	if($(trObj).find("td").length>2){
		subject3=$(trObj).find("td:eq(3)").attr("data-value");
		subject3Name=$(trObj).find("td:eq(3)").attr("data-name");
	}else{
		subject3=$(trObj).find("td:eq(0)").attr("data-value");
		subject3Name=$(trObj).find("td:eq(0)").attr("data-name");
	}
	//移动到其他两科组合
	var sub3Arr=subject3.split("_");
	var sub1=sub3Arr[0]+"_"+sub3Arr[1];
	var sub1Name=courseMap[sub3Arr[0]]+courseMap[sub3Arr[1]];
	var sub2=sub3Arr[0]+"_"+sub3Arr[2];
	var sub2Name=courseMap[sub3Arr[0]]+courseMap[sub3Arr[2]];
	var sub3=sub3Arr[1]+"_"+sub3Arr[2];
	var sub3Name=courseMap[sub3Arr[1]]+courseMap[sub3Arr[2]];
	var length1=0;
	var length2=0;
	var length3=0
	if(sub1!=sub2Obj){
		length1=$(".openTwoTable").find(".sub_"+sub1).length;
	}
	if(sub2!=sub2Obj){
		length2=$(".openTwoTable").find(".sub_"+sub2).length;
	}
	if(sub3!=sub2Obj){
		length3=$(".openTwoTable").find(".sub_"+sub3).length;
	}
	if(length1+length2+length3==0){
		layer.msg("没有其他去向的2科组合可以调整", {
			offset: 't',
			time: 2000
		});
		return;
	}
	
	var $tbody=$(".layer-moveStudentNum").find("tbody");
	$tbody.html("");
	if(length1>0){
		var addRows=addMoveRow(sub1,sub1Name);
		$($tbody).append(addRows);
	}
	if(length2>0){
		var addRows=addMoveRow(sub2,sub2Name);
		$($tbody).append(addRows);
	}
	if(length3>0){
		var addRows=addMoveRow(sub3,sub3Name);
		$($tbody).append(addRows);
	}
	
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '移动设置',
		area: '520px',
		btn: ['确定', '取消'],
		scrollbar:false,
		btn1:function(){
			var num=0;
			var numMap={};
			//验证输入的参数是否准确
			var f=checkValue(".layer-moveStudentNum");
			if(!f){
				return;
			}
			
			
			$($tbody).find("tr").each(function(){
				var moveNum=$(this).find(".moveNum").val();
				var moveNumInt=parseInt(moveNum);
				var sub2=$(this).attr("data-value");
				if(moveNumInt>0){
					numMap[sub2]=moveNumInt;
					num=num+moveNumInt;
				}
			})
			if(num==0){
				//没有调整
				layer.msg("没有调整", {
					offset: 't',
					time: 2000
				});
				layer.closeAll();
			}else{
				if(num>left2NumInt){
					//超过剩余人数
					layer.msg("超过剩余人数,请修改", {
						offset: 't',
						time: 2000
					});
					
				}else{
					//可以调整
					//两个组合--总人数增多
					for(var kkk  in numMap){
						//2科组合增加
						var ss=0;
						var firstTd=null;
						var chooseTd=null;
						var lastTd=null;
						var arrMap={};
						$(".openTwoTable").find(".sub_"+kkk).each(function(){
							var choosesub3Ids="";
							if(ss==0){
								//第一行
								firstTd=$(this);
								choosesub3Ids=$(this).find("td:eq(3)").attr("data-value");
							}else{
								choosesub3Ids=$(this).find("td:eq(0)").attr("data-value");
							}
							if(choosesub3Ids==subject3){
								//存在
								chooseTd=$(this);
								return false;
							}
							lastTd=$(this);
							ss++;
						})
						if(chooseTd!=null){
							//存在
							var sub3Num=$(chooseTd).find(".studentScourceNum").val();
							var sub3NumInt=parseInt(sub3Num)+numMap[kkk];
							$(chooseTd).find(".studentScourceNum").val(""+sub3NumInt);
						}else{
							//不存在 添加一行
							var dataNum=$(firstTd).attr("data-num");
							var $htmlTr='<tr class="sub_'+kkk+'">'
										+'<td data-value="'+subject3+'" data-name="'+subject3Name+'">'
										+'<input type="hidden" name="sectionEndList['+dataNum+'].studentScourceSub" value="'+subject3+'">'
										+subject3Name+'</td>'
										+'<td><input type="text" name="sectionEndList['+dataNum+'].studentScourceNum" readonly class="studentScourceNum" value="'+numMap[kkk]+'"><a href="javascript:void(0)" onclick="adj2StudentNum(this)">移动</a></td>'
									    +'</tr>';
							var hhh=ss+1;
							$(firstTd).find("td:eq(0)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(1)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(2)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(5)").attr("rowspan",hhh);
							$(firstTd).find("td:eq(6)").attr("rowspan",hhh);		    
							$(lastTd).after($htmlTr);
						}
					
						//修改总人数
						var oldStuNum=$(firstTd).find("td:eq(1)").html();
						var stuNumInt=parseInt(oldStuNum)+numMap[kkk];
						$(firstTd).find("td:eq(1)").html(""+stuNumInt);
						
					}
					//人数减少
					//num
					var arrNumInt=left2NumInt-num;
					$(trObj).find(".studentScourceNum").val(arrNumInt);
					//总人数减少
					var allNums=$(".openTwoTable").find(".sub_"+sub2Obj+":first").find("td:eq(1)").html();
					var allNumsInt=parseInt(allNums)-num;
					$(".openTwoTable").find(".sub_"+sub2Obj+":first").find("td:eq(1)").html(""+allNumsInt);
					layer.closeAll();
				}
			}
			
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-moveStudentNum')
	});
	
}



</script>
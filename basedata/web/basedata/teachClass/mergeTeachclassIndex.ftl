<a href="javascript:void(0)" onclick="goBackTeachClass()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<form id="mergeClassTeach">
<input type="hidden" name="teachClass.id" id="id" value="${teachClass.id!}">
<input type="hidden" name="teachClass.unitId" id="unitId" value="${teachClass.unitId!}">
<input type="hidden" name="teachClass.acadyear" id="acadyear" value="${teachClass.acadyear!}">
<input type="hidden" name="teachClass.semester" id="semester" value="${teachClass.semester!}">
<input type="hidden" id="teachClassIds" name="teachClass.teachClassIds">
<#if isEdit>
<input type="hidden" name="teachClass.courseId"  value="${subjectId!}">
<input type="hidden"  name="teachClass.teacherId" value="${teacherId!}">
</#if>
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">合并教学班</h3>
	</div>
	<div class="box-body">
		<div class="form-horizontal" role="form">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">教学班名称：</label>
				<div class="col-sm-3">
					<#if teachClass.isUsing?default('1')=='1'>
						<input type="text" name="teachClass.name" id="teacherClassName" value="${teachClass.name!}" class="form-control">
					<#else>
						<input type="text" disabled name="teachClass.name" id="teacherClassName" value="${teachClass.name!}" class="form-control">
					</#if>
				</div>
				<div class="col-sm-4 control-tips"></div>
			</div>
			<#if teachClass.isUsing?default('1')=='1'>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">选择课程：</label>
					<div class="col-sm-3">
						<select name="teachClass.courseId" <#if isEdit> disabled </#if> id="subjectId" class="form-control" >
					     	<#if (courseList?exists && courseList?size>0)>
								<#list courseList as item>
									<option value="${item.id}">${item.subjectName}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
			<#else>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">选择课程：</label>
					<div class="col-sm-3">
						<select name="teachClass.courseId" disabled id="subjectId" class="form-control" >
					     	<#if (courseList?exists && courseList?size>0)>
								<#list courseList as item>
									<option value="${item.id}">${item.subjectName}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
			</#if>
			<#if teachClass.isUsing?default('1')=='1'>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">任课教师：</label>
					<div class="col-sm-3">
						<select name="teachClass.teacherId" id="teacherId" <#if isEdit> disabled </#if> class="form-control" onChange="getTeacherList()">
							<#if (teacherList?exists && teacherList?size>0)>
								<#list teacherList as item>
									<option value="${item.id}">${item.teacherName}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
			<#else>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">任课教师：</label>
					<div class="col-sm-3">
						<select name="teachClass.teacherId" id="teacherId" disabled class="form-control" onChange="getTeacherList()">
							<#if (teacherList?exists && teacherList?size>0)>
								<#list teacherList as item>
									<option value="${item.id}">${item.teacherName}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
			</#if>
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">用于合并的教学班：</label>
				<div class="col-sm-5" style="height:150px;overflow-y:auto;" id="teacherClassIds">
					<#if teachClass.isUsing?default('1')!='1'>
						<#if tClassList?exists && (tClassList?size>0)>
							<#list tClassList as item>
								<label class="pos-rel">
								     <input name="teachClassId" type="checkbox" checked="true" class="wp" value="${item.id!}">
								     <span class="lbl"> ${item.name!}</span>
								</label>
							</#list>
						</#if>
					</#if>
				</div>
			</div>
			
			<#if teachClass.isUsing?default('1')=='1'>
			<div class="form-group punchCardDiv">
				<label for="" class="control-label no-padding-right col-sm-2">是否考勤：</label>
				<div class="col-sm-8">
					<input type="hidden" id="punchCard" name="teachClass.punchCard" value="${teachClass.punchCard!}">
						<label><input type="checkbox" <#if teachClass.punchCard?default(0)==1>checked="checked"</#if> class="wp wp-switch js-punchCardInputer"><span class="lbl"></span></label>
				</div>
			</div>
			<div class="form-group timePlaceDiv">
				<label for="" class="control-label no-padding-right col-sm-2">上课时间与场地：</label>
				<div class="col-sm-5">
					<table id="addTimePlace" class="table table-bordered table-striped table-condensed table-hover">
						<tfoot>
				            <tr>
				            	<td colspan="5" class="text-center"><a class="js-add js-addTimeRow" id="js-addTimeRow" href="javascript:" onclick="addTimeRow()">+新增</a></td>
				            </tr>
				        </tfoot>
						<tbody>
							<#if exList?exists && exList?size gt 0>
							<#list exList as ex>
								<tr>
									<td>
										<select name="timePlaceDtoList[${ex_index}].dayOfWeek" id="dayOfWeek_${ex_index}" class="form-control dayOfWeekClass" nullable="false">
											<#list 0..(dayOfWeek-1) as day>
												<#if day==0>
													<option value="${day}" <#if ex.dayOfWeek?default(0)==day>selected="selected"</#if>>星期一</option>
												<#elseif day==1>
													<option value="${day}" <#if ex.dayOfWeek?default(0)==day>selected="selected"</#if>>星期二</option>
												<#elseif day==2>
													<option value="${day}" <#if ex.dayOfWeek?default(0)==day>selected="selected"</#if>>星期三</option>
												<#elseif day==3>
													<option value="${day}" <#if ex.dayOfWeek?default(0)==day>selected="selected"</#if>>星期四</option>
												<#elseif day==4>
													<option value="${day}" <#if ex.dayOfWeek?default(0)==day>selected="selected"</#if>>星期五</option>
												<#elseif day==5>
													<option value="${day}" <#if ex.dayOfWeek?default(0)==day>selected="selected"</#if>>星期六</option>
												<#elseif day==6>
													<option value="${day}" <#if ex.dayOfWeek?default(0)==day>selected="selected"</#if>>星期天</option>
												</#if>
											</#list>
										</select>
									</td>
									<td>
										<select name="timePlaceDtoList[${ex_index}].periodInterval" id="periodInterval_${ex_index}" class="form-control periodIntervalClass" nullable="false">
											<#if p_2 gt 0>
												<option value="2" <#if ex.periodInterval?default('2')== '2'>selected="selected"</#if> >上午</option>
											</#if>
											<#if p_3 gt 0>
												<option value="3" <#if ex.periodInterval?default('2')== '3'>selected="selected"</#if> >下午</option>
											</#if>
											<#if p_4 gt 0>
												<option value="4" <#if ex.periodInterval?default('2')== '4'>selected="selected"</#if>>晚上</option>
											</#if>
										</select>
									</td>
									<td>
										<#assign chooseIn=ex.periodInterval?default('2')>
										<select name="timePlaceDtoList[${ex_index}].period" id="preiod_${ex_index}" class="form-control periodClass" nullable="false">
											<#assign countNum=0>
											<#if chooseIn=="2">
												<#assign countNum=p_2>
											<#elseif chooseIn=="3">
												<#assign countNum=p_3>
											<#elseif chooseIn=="4">
												<#assign countNum=p_4>
											</#if>
											<#if countNum gt 0>
												<#list 1..countNum as num>
													<option value="${num}" <#if ex.period?default(1)==num> selected="selected"</#if> >${num}</option>
												</#list>
											<#else>
												<option value="">未设置</option>
											</#if>
										</select>
									</td>
									<td>
										<select name="timePlaceDtoList[${ex_index}].placeId" id="placeId_${ex_index}" class="form-control placeIdClass" >
											<#if placeList?exists && placeList?size gt 0>
												<#list placeList as place>
													<option value="${place.id!}" <#if ex.placeId?default('')==place.id>selected="selected"</#if>>${place.placeName!}</option>
												</#list>
											<#else>
												<option value="">暂无场地</option>'
											</#if>
										</select>
									</td>
									<td>
										<a class="js-del js-delTime" href="javascript:" id="deleteTime_${ex_index}">删除</a>
									</td>
								</tr>
								</#list>
							</#if>
						</tbody>
					</table>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-6 col-sm-offset-2">
					<a href="javascript:void(0)" class="btn btn-long btn-blue" onclick="saveMergeTeachClass()">确定</a></p>
				</div>
			</div>
			<#else>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-2">是否考勤：</label>
					<div class="col-sm-8">
						<label><#if teachClass.punchCard?default(0)==1>是<#else>否</#if></label>
					</div>
				</div>
				<div class="form-group timePlaceDiv">
					<label for="" class="control-label no-padding-right col-sm-2">上课时间与场地：</label>
					<div class="col-sm-8">
						<table id="addTimePlace" class="table table-bordered table-striped table-condensed table-hover">
							<tr>
								<th>
									时间
								</th>
								<th>
									地点
								</th>
							</tr>
							<#if exList?exists && exList?size gt 0>
							<#list exList as ex>
								<tr>
									<td>
										${ex.timeStr!}
									</td>
									<td>
										${ex.placeName!}
									</td>
								</tr>
							</#list>
							</#if>
						</table>
					</div>
				</div>
			</#if>
		</div>
	</div>
</div>
</form>

<script>
var days=parseInt('${dayOfWeek}');
var am=parseInt('${p_2}');
var pm=parseInt('${p_3}');
var nm=parseInt('${p_4}');
var indexNum=0;
$(function(){
	<#if exList?exists && exList?size gt 0>
		indexNum=${exList?size};
	</#if>
	$(".timePlaceDiv").on('click', '.js-delTime', function(e){
		e.preventDefault();
		var that = $(this);
		layer.confirm('确定删除吗？', function(index){
			that.closest('tr').remove();
			layer.close(index);
		})
	})
	$(".timePlaceDiv").on('click', '.periodIntervalClass', function(e){
		e.preventDefault();
		var that = $(this);
		var chooseTime=that.val();
		var periodText=makePeriodSelectTxt(chooseTime);
		
		var chooseTr=that.closest('tr');
		
		chooseTr.find(".periodClass").html('');
		chooseTr.find(".periodClass").html(periodText);
	})
		
	<#if teachClass.isUsing?default('1')=='1'>
		getTeacherList();
	</#if>
})

function getTeacherList(){
	var teacherId = $("#teacherId option:selected").val(); 
	var url =  '${request.contextPath}/basedata/teachclass/getmergeteachclass/index/page?acadyearSearch=${acadyearSearch}&semesterSearch=${semesterSearch}&showTabType=${semesterSearch}&teacherId=' + teacherId+"&tcIds=${tcIds!}";
	$("#teacherClassIds").load(url);
}

$('.js-punchCardInputer').click(function(){
	if(this.checked){
		$("#punchCard").val("1");
	}else{
		$("#punchCard").val("0");
	}
})


//星期一默认选中
function makeWeekDay(days){
	var weekHtml='';
	if(days<=0){
		return weekHtml;
	}else if(days==1){
		weekHtml=weekHtml+'<option value="0" selected="selected">星期一</option>';
		return weekHtml;
	}else if(days==2){
		weekHtml=weekHtml+'<option value="0" selected="selected">星期一</option>'
			+'<option value="1">星期二</option>';
		return weekHtml;
	}else if(days==3){
		weekHtml=weekHtml+'<option value="0" selected="selected">星期一</option>'
			+'<option value="1">星期二</option>'
			+'<option value="2">星期三</option>';
		return weekHtml;
	}else if(days==4){
		weekHtml=weekHtml+'<option value="0" selected="selected">星期一</option>'
			+'<option value="1">星期二</option>'
			+'<option value="2">星期三</option>'
			+'<option value="3">星期四</option>';
		return weekHtml;
	}else if(days==5){
		weekHtml=weekHtml+'<option value="0" selected="selected">星期一</option>'
			+'<option value="1">星期二</option>'
			+'<option value="2">星期三</option>'
			+'<option value="3">星期四</option>'
			+'<option value="4">星期五</option>';
		return weekHtml;
	}else if(days==6){
		weekHtml=weekHtml+'<option value="0" selected="selected">星期一</option>'
			+'<option value="1">星期二</option>'
			+'<option value="2">星期三</option>'
			+'<option value="3">星期四</option>'
			+'<option value="4">星期五</option>'
			+'<option value="5">星期六</option>';
		return weekHtml;
	}else{
		weekHtml=weekHtml+'<option value="0" selected="selected">星期一</option>'
			+'<option value="1">星期二</option>'
			+'<option value="2">星期三</option>'
			+'<option value="3">星期四</option>'
			+'<option value="4">星期五</option>'
			+'<option value="5">星期六</option>'
			+'<option value="6">星期天</option>';
		return weekHtml;
	}
}
function makePeriodInteral(){
	var interalHtml='';
	var flag=false;
	
	if(am>0){
		if(flag){
			interalHtml=interalHtml+'<option value="2">上午</option>';
		}else{
			flag=true;
			interalHtml=interalHtml+'<option value="2" selected="selected">上午</option>';
		}
		
	}
	if(pm>0){
		if(flag){
			interalHtml=interalHtml+'<option value="3">下午</option>';
		}else{
			flag=true;
			interalHtml=interalHtml+'<option value="3" selected="selected">下午</option>';
		}
		
	}
	if(nm>0){
		if(flag){
			interalHtml=interalHtml+'<option value="4">晚上</option>';
		}else{
			flag=true;
			interalHtml=interalHtml+'<option value="4" selected="selected">晚上</option>';
		}
		
	}
	if(interalHtml==''){
		interalHtml=interalHtml+'<option value="">未设置</option>';
	}
	return interalHtml;
}
function makePlace(){
	var placeHtml='';
	<#if placeList?exists && placeList?size gt 0>
		<#list placeList as place>
			placeHtml=placeHtml+'<option value="${place.id!}">${place.placeName!}</option>';
		</#list>
	<#else>
		placeHtml=placeHtml+'<option value="">暂无场地</option>';
	</#if>
	return placeHtml;
}
function addPlaceTime(index){
	//场地可为空
	var htmlText='<tr><td><select name="timePlaceDtoList['+index+'].dayOfWeek" id="dayOfWeek_'+index+'" class="form-control dayOfWeekClass" nullable="false">'
	+makeWeekDay(days)+'</select></td>'
	+'<td><select name="timePlaceDtoList['+index+'].periodInterval" id="periodInterval_'+index+'" class="form-control periodIntervalClass" nullable="false">'
	+makePeriodInteral()+'</select></td>'
	+'<td><select name="timePlaceDtoList['+index+'].period" id="preiod_'+index+'" class="form-control periodClass" nullable="false">'
	+'<option value="">未设置</option>'
	+'</select></td>'
	+'<td><select name="timePlaceDtoList['+index+'].placeId" id="placeId_'+index+'" class="form-control placeIdClass" >'
	+makePlace()+'</select></td>'
	+'<td><a class="js-del js-delTime" href="javascript:" id="deleteTime_'+index+'">删除</a></td></tr>';
	return 	htmlText;							
}
function changeInteral(index){
	var ii=$("#periodInterval_"+index).val();
	$("#preiod_"+index).html('');
	
	$("#preiod_"+index).html(makePeriodSelectTxt(ii));
}

function makePeriodSelectTxt(chooseII){
	var periodHtml="";
	if(chooseII=='2'){
		if(am>0){	
			for(var i=1;i<=am;i++){
				periodHtml=periodHtml+'<option value="'+i+'">'+i+'</option>';
			}
		}else{
			periodHtml='<option value="">未设置</option>';
		}
	}else if(chooseII=='3'){
		if(pm>0){	
			for(var i=1;i<=pm;i++){
				periodHtml=periodHtml+'<option value="'+i+'">'+i+'</option>';
			}
		}else{
			periodHtml='<option value="">未设置</option>';
		}
	}else if(chooseII=='4'){
		if(nm>0){	
			for(var i=1;i<=nm;i++){
				periodHtml=periodHtml+'<option value="'+i+'">'+i+'</option>';
			}
		}else{
			periodHtml='<option value="">未设置</option>';
		}
	}else{
		periodHtml='<option value="">未设置</option>';
	}
	return periodHtml;
}
function addTimeRow(){
	var item=addPlaceTime(indexNum);
	$('#addTimePlace').find('tbody').append(item);
	changeInteral(indexNum);
	initDel(indexNum);
	indexNum=indexNum+1;
	
}
function initDel(iinn){
	$("#deleteTime_"+iinn).on('click',function(){
		var that = $(this);
		layer.confirm('确定删除吗？', function(index){
			that.closest('tr').remove();
			layer.close(index);
		})
	});
}

var isSubmit=false;
function saveMergeTeachClass() {
	if(isSubmit){
		return;
	}
	isSubmit=true;	
	var check = checkValue('#mergeClassTeach');
    if(!check){
        isSubmit=false;
        return;
    }
    
    var teacherClassName = $("#teacherClassName").val();
    if(teacherClassName=="") {
    	layer.tips('不能为空!', $("#teacherClassName"), {
			time:1000,
			tipsMore: true,
			tips: 2
		});
        isSubmit=false;
        return;
    }
    
    //课程不能为空
    var subjectId=$("#subjectId  option:selected").val();
    if(subjectId==""){
    	layer.tips('请选择课程!', $("#subjectId"), {
			time:1000,
			tipsMore: true,
			tips: 2
		});
        isSubmit=false;
        return;
    }
    
    //任课老师不能为空
    var teacherId=$("#teacherId  option:selected").val();
    if(!teacherId){
    	layer.tips('不能为空!', $("#teacherId"), {
			time:1000,
			tipsMore: true,
			tips: 2
		});
        isSubmit=false;
        return;
    }
    if(teacherId==""){
    	layer.tips('不能为空!', $("#teacherId"), {
			time:1000,
			tipsMore: true,
			tips: 2
		});
        isSubmit=false;
        return;
    }

	var teachClassIds="";
	$("input[name='teachClassId']:checked").each(function(){
		var teachClassId=$(this).val();
		teachClassIds=teachClassIds+","+teachClassId;
	});
	if(teachClassIds!=""){
		teachClassIds=teachClassIds.substring(1);
	}
	if(teachClassIds=="") {
		layer.alert('请选择教学班',{icon:7});
		isSubmit=false;
		return;
	}
	
	$("#teachClassIds").val(teachClassIds);
	
	  
    var punchCard=$("#punchCard").val();
   
	//上课时间不能重复
	var timestr="";
	var trlength=$('#addTimePlace').find('tbody').find('tr').length;
	var ff=false;
	if(trlength>0){
		$('#addTimePlace').find('tbody').find('tr').each(function(){
			var dayOfWeekItem=$(this).find(".dayOfWeekClass").val();
			var intervalItem=$(this).find(".periodIntervalClass").val();
			var periodItem=$(this).find(".periodClass").val();
			
			var str=","+dayOfWeekItem+"_"+intervalItem+"_"+periodItem+"$";
			if(timestr!=""){
				if(timestr.indexOf(str)>=0){
					ff=true;
					return false;//跳出
				}
			}
			timestr=timestr+","+str;
			
			
		})
	}
	if(ff){
		layerTipMsg(false,"提示","上课时间重复！");
		$(this).removeClass("disabled");
        isSubmit=false;
        return;
	}
    
	var options = {
		url : "${request.contextPath}/basedata/teachclass/saveorupdatemerge",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit=false;
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	goBackTeachClass();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#mergeClassTeach").ajaxSubmit(options);
		
}
function goBackTeachClass1(){
	var url =  '${request.contextPath}/basedata/teachclass/indexItem/page';
	$("#indexContent").load(url);
}

function goBackTeachClass(){
	var url =  '${request.contextPath}/basedata/teachclass/index/page?flag=1&acadyear=${teachClass.acadyear!}&semester=${teachClass.semester!}&showTabType=2';
	$("#indexContent").load(url);
}


</script>
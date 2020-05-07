<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<form id="teachClassForm">
	<input type="hidden" name="teachClass.id" id="id" value="${teachClass.id!}">
	<div class="layer-content" id="myTeachDiv">
		<div class="form-horizontal" style="height:680px;overflow-y:auto;overflow-x:hidden;">
			
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">面向对象：</label>
				<div class="col-sm-8">
					<label>${gradeNames!}</label>
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">教学班名称：</label>
				<div class="col-sm-8">
					
					<input type="text" disabled class="form-control"  value="${teachClass.name!}">
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">选择课程：</label>
				<div class="col-sm-8">
					<input type="text" disabled class="form-control"   value="${teachClass.courseName!}">
				</div>
			</div>
			
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">任课老师：</label>
				<div class="col-sm-8">
					<@popupMacro.selectOneTeacher clickId="teacherName" id="teacherId" name="teacherName">
						<div class="input-group">
							<input type="hidden" id="teacherId" name="teachClass.teacherId" value="${teachClass.teacherId!}">
							<input type="text" id="teacherName" class="form-control teachTeacherId" style="width:100%;" value="${teachClass.teacherNames!}">
						</div>
					</@popupMacro.selectOneTeacher>
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">学分：</label>
				<div class="col-sm-8">
					<input type="text" class="form-control"  <#if !isEdit> disabled </#if> maxlength="3" name="teachClass.credit" id="credit" vtype="int" value="${teachClass.credit!}">
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">满分值：</label>
				<div class="col-sm-8">
					<input type="text" class="form-control" <#if !isEdit> disabled </#if>  maxlength="3" name="teachClass.fullMark" id="fullMark" vtype="int" value="${teachClass.fullMark!}">
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">及格分：</label>
				<div class="col-sm-8">
					<input type="text" class="form-control" <#if !isEdit> disabled </#if> maxlength="3" name="teachClass.passMark" id="passMark" vtype="int" value="${teachClass.passMark!}">
				</div>
			</div>
			
			<#----isEdit=true-->
			
			<#if isEdit>
				<#if limit?default('') == '0'> 
					<#if tabType?default('') == '2'>
					<div class="form-group">
						<label for="" class="control-label no-padding-right col-sm-3">是否用于合并教学班：</label>
						<div class="col-sm-8">
							<input type="hidden" id="usingMerge" name="teachClass.isUsingMerge" value="${teachClass.isUsingMerge?default('0')}">
							<label><input type="checkbox" class="wp wp-switch js-isUsingMergeInputer" <#if teachClass.isUsingMerge?default('0')=="1">checked</#if>><span class="lbl"></span></label>
						</div>
					</div>
					</#if>
				</#if>
			
				<div class="form-group punchCardDiv" <#if teachClass.isUsingMerge?default('0')=="1">style="display:none"</#if>>
					<label for="" class="control-label no-padding-right col-sm-3">是否考勤：</label>
					<div class="col-sm-8">
						<input type="hidden" id="punchCard" name="teachClass.punchCard" value="${teachClass.punchCard?default(0)}">
						<label><input type="checkbox" class="wp wp-switch js-punchCardInputer" <#if teachClass.punchCard?default(0)==1>checked</#if>><span class="lbl"></span></label>
					</div>
				</div>
			
			
				<#if teachClass.classType?default('') == '1'>
				
					<div class="form-group relaOpenDiv">
						<label for="" class="control-label no-padding-right col-sm-3">是否关联走班课程：</label>
						<div class="col-sm-8">
							<input type="hidden" id="relaOpen" name="relaOpen"  <#if teachClass.relaCourseId?default('')!=''>value="1"<#else>value="0"</#if> >
							<label><input type="checkbox" class="wp wp-switch js-relaOpen" <#if teachClass.relaCourseId?default('')!=''>checked</#if>><span class="lbl"></span></label>
						</div>
					</div>
					<div class="form-group relaOpenDiv2" <#if teachClass.relaCourseId?default('')==''>style="display:none"</#if>>
						<label for="" class="control-label no-padding-right col-sm-3">关联课程：</label>
						<div class="col-sm-8">
							<select id="myrelaCourseId" class="form-control" onChange="changeCourseIds()">
								<#if relaCourseList?exists && relaCourseList?size gt 0>
								<#list relaCourseList as item>
									<option value="${item.id!}" <#if teachClass.relaCourseId?default('')==item.id>selected="selected"</#if>>${item.subjectName!}</option>
								</#list>
								<#else>
									<option value="">暂无可关联课程</option>
								</#if>
							</select>
						</div>
					</div>
					
					<div class="form-group relaOpenDiv2" <#if teachClass.relaCourseId?default('')==''>style="display:none"</#if>>
						<label for="" class="control-label no-padding-right col-sm-3">关联班级：</label>
						<div class="col-sm-8">
							<select name="teachClass.relaCourseId" id="relaCourseId" class="form-control">
								
							</select>
						</div>
					</div>
					
					
					
					<div class="form-group relaOpenDiv2" <#if teachClass.relaCourseId?default('')==''>style="display:none"</#if>>
						<label for="" class="control-label no-padding-right col-sm-3">场地：</label>
						<div class="col-sm-8">
							<select name="teachClass.placeId" id="placeId" class="form-control">
								<#if placeList?exists && placeList?size gt 0>
									<option value="">--请选择--</option>
								<#list placeList as place>
									<option value="${place.id!}" <#if teachClass.placeId?default('')==place.id>selected="selected"</#if> >${place.placeName!}</option>
								</#list>
								<#else>
									<option value="">暂无场地</option>
								</#if>
							</select>
						</div>
					</div>
			
				</#if>
			
			
			
				<div class="form-group timePlaceDiv" <#if teachClass.relaCourseId?default('')!='' || teachClass.isUsingMerge?default('0')=="1">style="display:none"</#if>>
					<label for="" class="control-label no-padding-right col-sm-3">上课时间与场地：</label>
					<div class="col-sm-8">
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
			<#else>
				<#if tabType?default('') == '2'>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">是否用于合并教学班：</label>
					<div class="col-sm-8">
						<input type="hidden" id="usingMerge" name="teachClass.isUsingMerge" value="${teachClass.isUsingMerge?default('0')}">
						<label><#if teachClass.isUsingMerge?default('0')=="1">是<#else>否</#if></label>
					</div>
				</div>
				</#if>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">是否考勤：</label>
					<div class="col-sm-8">
						<label><#if teachClass.punchCard?default(0)==1>是<#else>否</#if></label>
					</div>
				</div>
				<#if teachClass.classType?default('') == '1'>
					<div class="form-group relaOpenDiv">
						<label for="" class="control-label no-padding-right col-sm-3">是否关联走班课程：</label>
						<div class="col-sm-8">
							
							<label> <#if teachClass.relaCourseId?default('')!=''>是<#else>否</#if></label>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="control-label no-padding-right col-sm-3">关联课程：</label>
						<div class="col-sm-8">
							<label>${relaName!}</label>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="control-label no-padding-right col-sm-3">场地：</label>
						<div class="col-sm-8">
							<label>${placeName!}</label>
						</div>
					</div>
				</#if>
				
					<#if teachClass.relaCourseId?default('')=='' && teachClass.isUsingMerge?default('0')=="0" >
					<div class="form-group timePlaceDiv">
						<label for="" class="control-label no-padding-right col-sm-3">上课时间与场地：</label>
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
			</#if>
		</div>
	</div>
</form>	
<div class="layer-footer">
	<#if isEdit> 
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	</#if>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script type="text/javascript">
	<#if isEdit> 
	var days=parseInt('${dayOfWeek}');
	var am=parseInt('${p_2}');
	var pm=parseInt('${p_3}');
	var nm=parseInt('${p_4}');
	var indexNum=0;
	var mapList={};
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
		
	
		//考勤 控制自己
		$('.js-punchCardInputer').click(function(){
			if(this.checked){
				$("#punchCard").val("1");
			}else{
				$("#punchCard").val("0");
			}
		})
		
		//是否关联
		$('.js-relaOpen').click(function(){
			if(this.checked){
				$("#relaOpen").val("1");
				$(".relaOpenDiv2").show();
				$(".timePlaceDiv").hide();
			}else{
				$("#relaOpen").val("0");
				$(".relaOpenDiv2").hide();
				$(".timePlaceDiv").show();
			}
		})
		
		<#if teachClass.classType?default('') == '1'>
		var defaultId='${teachClass.relaCourseId!}';
		var hh;
			<#if allHourList?exists && allHourList?size gt 0>
				<#list allHourList as item>
					if(!mapList['${item.subjectId!}']){
						mapList['${item.subjectId!}']=new Array();
					}
					mapList['${item.subjectId!}'].push(['${item.id!}','${item.classNames!}']);
					if(defaultId!="" && defaultId=='${item.id!}'){
						hh=new Array();
						hh[0]='${item.subjectId!}';
						hh[1]='${item.id!}';
					}
				</#list>
			</#if>
		if(hh){
			$("#myrelaCourseId").val(hh[0]);
			changeCourseIds();
			$("#relaCourseId").val(hh[1]);
		}else{
			changeCourseIds();
		}
		</#if>
		
		//是否用于合并--是 不维护时间场地
		$('.js-isUsingMergeInputer').click(function(){
			if(this.checked){
				$("#usingMerge").val("1");
				$(".punchCardDiv").hide();
				$(".timePlaceDiv").hide();
				$(".relaOpenDiv").hide();
				$(".relaOpenDiv2").hide();
			}else{
				$("#usingMerge").val("0");
				$(".punchCardDiv").show();
				$(".relaOpenDiv").show();
				if($("#relaOpen").val()=="1"){
					$(".relaOpenDiv2").show();
				}else{
					$(".timePlaceDiv").show();
				}
				
				
			}
		})
	})
	<#if teachClass.classType?default('') == '1'>
	function changeCourseIds(){
		var cId=$("#myrelaCourseId").val();
		$("#relaCourseId").html('');
		if(cId==""){
			$("#relaCourseId").html('<option value="">暂无可选数据</option>');
		}else{
			if(mapList[cId]){
				var htmlText='';
				var arr=mapList[cId];
				for(var i=0;i<arr.length;i++){
					htmlText=htmlText+'<option value="'+arr[i][0]+'">'+arr[i][1]+'</option>';
				}
				$("#relaCourseId").html(htmlText)
			}else{
				$("#relaCourseId").html('<option value="">暂无可选数据</option>');
			}
		}
		
	}
	</#if>
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

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
	
var isSubmit=false;
$("#arrange-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;	
	$(this).addClass("disabled");
	var check = checkValue('#myTeachDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    //任课老师不能为空
    var teacherId=$("#teacherId").val();
    if(!teacherId){
    	layer.tips('不能为空!', $(".teachTeacherId"), {
			time:1000,
			tipsMore: true,
			tips: 2
		});
    	$(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    var punchCard=$("#punchCard").val()
    if(punchCard=="1"){
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
    }
	var options = {
		url : "${request.contextPath}/basedata/teachclass/saveorupdate",
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
			  	findList("1");
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#teachClassForm").ajaxSubmit(options);
		
});
<#else>
// 取消按钮操作功能
$("#arrange-close").on("click", function(){
   layer.closeAll()     
 });
</#if> 	
	
	
</script>



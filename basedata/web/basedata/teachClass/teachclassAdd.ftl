<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<form id="teachClassForm">
	<input type="hidden" name="teachClass.id" id="id" value="${teachClass.id!}">
	<input type="hidden" name="teachClass.unitId" id="unitId" value="${teachClass.unitId!}">
	<input type="hidden" name="teachClass.acadyear" id="acadyear" value="${teachClass.acadyear!}">
	<input type="hidden" name="teachClass.semester" id="semester" value="${teachClass.semester!}">
	<input type="hidden" name="teachClass.classType" id="classType" value="${teachClass.classType!}">
	<input type="hidden" name="teachClass.gradeId" id="gradeId" value="${teachClass.gradeId!}">
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
					<input type="text" class="form-control" nullable="false" maxlength="100" name="teachClass.name" id="teachClassName">
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">选择课程：</label>
				<div class="col-sm-8">
					<select name="teachClass.courseId" id="courseId" class="form-control">
						<#if courseList?exists && courseList?size gt 0>
						<#list courseList as item>
							<option value="${item.id!}">${item.subjectName!}</option>
						</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">任课老师：</label>
				<div class="col-sm-8">
					<@popupMacro.selectOneTeacher clickId="teacherName" id="teacherId" name="teacherName">
						<div class="input-group">
							<input type="hidden" id="teacherId" name="teachClass.teacherId" value="" nullable="false" >
							<input type="text" id="teacherName" class="form-control teachTeacherId" style="width:100%;" value="">
						</div>
					</@popupMacro.selectOneTeacher>
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">学分：</label>
				<div class="col-sm-8">
					<input type="text" class="form-control"  maxlength="3" name="teachClass.credit" id="credit" vtype="int">
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">满分值：</label>
				<div class="col-sm-8">
					<input type="text" class="form-control"  maxlength="3" name="teachClass.fullMark" id="fullMark" vtype="int">
				</div>
			</div>
			<div class="form-group">
				<label for="" class="control-label no-padding-right col-sm-3">及格分：</label>
				<div class="col-sm-8">
					<input type="text" class="form-control"  maxlength="3" name="teachClass.passMark" id="passMark" vtype="int">
				</div>
			</div>
			<#if limit?default('') == '0'>
				<#if tabType?default('') == '2'>
					<div class="form-group">
						<label for="" class="control-label no-padding-right col-sm-3">是否用于合并教学班：</label>
						<div class="col-sm-8">
							<input type="hidden" id="usingMerge" name="teachClass.isUsingMerge" value="0">
							<label><input type="checkbox" class="wp wp-switch js-isUsingMergeInputer"><span class="lbl"></span></label>
						</div>
					</div>
				</#if>
			</#if>
			
			<div class="form-group punchCardDiv">
				<label for="" class="control-label no-padding-right col-sm-3">是否考勤：</label>
				<div class="col-sm-8">
					<input type="hidden" id="punchCard" name="teachClass.punchCard" value="0">
					<label><input type="checkbox" class="wp wp-switch js-punchCardInputer"><span class="lbl"></span></label>
				</div>
			</div>
			<#if teachClass.classType?default('') == '1'>
			
			<div class="form-group relaOpenDiv">
				<label for="" class="control-label no-padding-right col-sm-3">是否关联走班课程：</label>
				<div class="col-sm-8">
					<input type="hidden" id="relaOpen" name="relaOpen"  value="0">
					<label><input type="checkbox" class="wp wp-switch js-relaOpen"><span class="lbl"></span></label>
				</div>
			</div>
			
			<div class="form-group relaOpenDiv2" style="display:none">
				<label for="" class="control-label no-padding-right col-sm-3">关联课程：</label>
				<div class="col-sm-8">
					<select id="myrelaCourseId" class="form-control" onChange="changeCourseIds()">
						<#if relaCourseList?exists && relaCourseList?size gt 0>
							<#list relaCourseList as item>
								<option value="${item.id!}">${item.subjectName!}</option>
							</#list>
						<#else>
							<option value="">暂无可关联课程</option>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group relaOpenDiv2" style="display:none">
				<label for="" class="control-label no-padding-right col-sm-3">关联班级：</label>
				<div class="col-sm-8">
					<select name="teachClass.relaCourseId" id="relaCourseId" class="form-control">
						
					</select>
				</div>
			</div>
			<div class="form-group relaOpenDiv2" style="display:none">
				<label for="" class="control-label no-padding-right col-sm-3">场地：</label>
				<div class="col-sm-8">
					<select name="teachClass.placeId" id="placeId" class="form-control">
						<#if placeList?exists && placeList?size gt 0>
							<option value="">--请选择--</option>
						<#list placeList as place>
							<option value="${place.id!}">${place.placeName!}</option>
						</#list>
						<#else>
							<option value="">暂无场地</option>
						</#if>
					</select>
				</div>
			</div>
			</#if>
			<div class="form-group timePlaceDiv">
				<label for="" class="control-label no-padding-right col-sm-3">上课时间与场地：</label>
				<div class="col-sm-8">
					<table id="addTimePlace" class="table table-bordered table-striped table-condensed table-hover">
						<tfoot>
				            <tr>
				            	<td colspan="5" class="text-center"><a class="js-add js-addTimeRow" id="js-addTimeRow" href="javascript:" onclick="addTimeRow()">+新增</a></td>
				            </tr>
				        </tfoot>
						<tbody>
							
						</tbody>
					</table>
				</div>
			</div>
			
		</div>
	</div>
</form>	
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script type="text/javascript">
	var days=parseInt('${dayOfWeek}');
	var am=parseInt('${p_2}');
	var pm=parseInt('${p_3}');
	var nm=parseInt('${p_4}');
	var mapList={};
	
	$(function(){
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
		<#if teachClass.classType?default('') == '1'>
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
		
			<#if allHourList?exists && allHourList?size gt 0>
				<#list allHourList as item>
					if(!mapList['${item.subjectId!}']){
						mapList['${item.subjectId!}']=new Array();
					}
					mapList['${item.subjectId!}'].push(['${item.id!}','${item.classNames!}']);
				</#list>
			</#if>
		changeCourseIds();
		</#if>
		
		//是否用于合并--是 不维护时间场地
		$('.js-isUsingMergeInputer').click(function(){
			if(this.checked){
				$("#usingMerge").val("1");
				$(".punchCardDiv").hide();
				$(".timePlaceDiv").hide();
				<#if teachClass.classType?default('') == '1'>
				$(".relaOpenDiv").hide();
				$(".relaOpenDiv2").hide();
				</#if>
			}else{
				$("#usingMerge").val("0");
				$(".punchCardDiv").show();
				<#if teachClass.classType?default('') == '1'>
				$(".relaOpenDiv").show();
				if($("#relaOpen").val()=="1"){
					$(".relaOpenDiv2").show();
				}else{
					$(".timePlaceDiv").show();
				}
				<#else>
					$(".timePlaceDiv").show();
				</#if>
				
				
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
	var indexNum=0;
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
    if(!teacherId || teacherId==""){
    	layer.tips('不能为空!', $(".teachTeacherId"), {
			time:1000,
			tipsMore: true,
			tips: 2
		});
    	$(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
  <#if teachClass.classType?default('') == '1'>
    var relaOpen=$("#relaOpen").val();
    if(relaOpen=="0"){
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
    }else{
    	//关联
    	var relaCourseId=$("#relaCourseId").val();
    	if(!relaCourseId || relaCourseId==""){
    		layer.tips('不能为空!', $("#relaCourseId"), {
				time:1000,
				tipsMore: true,
				tips: 2
			});
	    	$(this).removeClass("disabled");
	        isSubmit=false;
	        return;
    	}
    }
    </#if>
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
	
	
	
</script>



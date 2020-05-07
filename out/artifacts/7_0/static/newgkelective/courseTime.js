/**
 * 
 * @param indexTr 序号
 * @param subjectIdType subjectId-subjectType
 * @param subjectName 新增科目名称
 * @param indexNumbers name中参数
 * @returns
 */
function addSubjectRow(indexTr,subjectIdType,subjectName,indexNumbers){
		var arr=subjectIdType.split("-");
		var $html='<tr><td><input type="hidden" name="subjectIdType" class="subjectIdType" value="'+subjectIdType+'">'
		+'<input type="hidden" name="dtoList['+indexNumbers+'].subjectId" class="subjectId" value="'+arr[0]+'">'
		+'<input type="hidden" name="dtoList['+indexNumbers+'].subjectType" class="subjectType" value="'+arr[1]+'">'+indexTr+'</td>'
		+' <td class="td-subjectName">'+subjectName+'</td>'
		+'<td><div class="form-number  form-number-sm period-number" data-step="1">'
	    		+'<input type="text" name="dtoList['+indexNumbers+'].courseWorkDay" id="" class="form-control courseWorkDay"  value="0" onblur="changeInputNum(this)"/>'
	    		+'<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>'
		    	+'<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>'
	    		$html=$html+makeFirstsdWeekSubjectIdSelect(indexNumbers,arr[0],arr[1]);
		
		$dayHtml=addSelectHtml(arrangeDayList,'dtoList['+indexNumbers+'].arrangeDay',"","arrangeDay",true);
		$halfdayHtml=addSelectHtml(arrangeHalfList,'dtoList['+indexNumbers+'].arrangeHalfDay',"","arrangeHalfDay",true)
		
		$html=$html+'<td><div class="form-number  form-number-sm courseCoupleTimes-number" data-step="1">'
				    	+'	<input type="text" name="dtoList['+indexNumbers+'].courseCoupleTimes" id="" class="form-control courseCoupleTimes" disabled value="0" onblur="changeInputNum(this)"/>'
				    	+'<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>'
				    	+'<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>'
				    	+' <td class="ellipsis courseCoupleTimes-times">'
				    	+' <input type="hidden" name="dtoList['+indexNumbers+'].courseCoupleTypeTimes" class="js-choosetime courseCoupleTypeTimes" value="">'
						+' <p><label><input type="radio" name="dtoList['+indexNumbers+'].courseCoupleType" value="0" checked="checked" disabled  class="wp js-timeAreaNo"><span class="lbl"> 无限制</span></label></p>'
						+' <p><label><input type="radio" name="dtoList['+indexNumbers+'].courseCoupleType" value="1" class="wp js-timeArea" disabled  ><span class="lbl"> 指定时间范围</span></label></p></td>'
						
				+' <td><div class="clearfix js-hover"><span class="pull-left js-hover-num noContinueNum color-blue">0</span>'
					    +	'<span class="pull-right js-hover-opt" style="display: block;">'
					    	+	'<a href="javascript:void(0)" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑" class="js-separate"><i class="fa fa-edit color-blue"></i></a>'
					    	+'</span></div></td>'
				+ ' <td>'+$dayHtml+'</td>'
				 +'   <td>'+$halfdayHtml+'</td>'
				  +'  <td><div class="clearfix js-hover"> '
				  +  		'<input type="hidden" class="js-choosetime noArrangeTime" name="dtoList['+indexNumbers+'].noArrangeTime" value="" >'
				    +		'<span class="pull-left js-hover-num color-blue">0</span>'
					 +   	'<span class="pull-right js-hover-opt" style="display: block;">'
					  +  		'<a href="javascript:void(0)" class="js-changeTime" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑" onclick="changeLimitTime(this)"><i class="fa fa-edit color-blue"></i></a>'
					   + 	'</span></div></td>'
				    +'<td><select name="dtoList['+indexNumbers+'].arrangePrior" id="" class="form-control arrangePrior" style="min-width:90px;" onChange="changeFunctionOpo(this)"><option value="0" selected="selected">默认</option><option value="1">高</option></select></td>'
				    
				    //+'<td><label><input type="checkbox" name="dtoList['+indexNumbers+'].punchCard" value="1" class="wp wp-switch" checked><span class="lbl"></span></label></td>'
			    +'<td><label><input type="checkbox" name="dtoList['+indexNumbers+'].needRoom" value="1" class="wp wp-switch" checked><span class="lbl"></span></label></td>'
				+(isZugd?('<td><label><input '+(arr[1] != 'O'?'disabled':'')+' type="checkbox" name="dtoList['+indexNumbers+'].followZhb" value="1" class="wp wp-switch"><span class="lbl"></span></label></td>'):'')
			    //+'<td><label><input '+(arr[1] != 'O'?'disabled':'')+' type="checkbox" name="dtoList['+indexNumbers+'].followZhb" value="1" class="wp wp-switch"><span class="lbl"></span></label></td>'

				    +'<td class="ellipsis"><a class="color-blue" href="javascript:void(0)" onclick="copySubject(this)">复制到</a>'
				    +'	<a class="color-blue" href="javascript:void(0)" onclick="deleteSubject(this)">删除</a></td></tr>'
	return $html;
}
/**
 * 组装单双周下拉框
 * @param indexNumbers
 * @param subjectId
 * @returns
 */
function makeFirstsdWeekSubjectIdSelect(indexNumbers,subjectId, subjectType){
	if(subjectType!="O"){
		return ' <td><select disabled name="dtoList['+indexNumbers+'].firstsdWeekSubjectId" id="" class="form-control firstsdWeekSubjectId" disabled="disabled" onChange="changeFirstsdWeek(this)">'
    	+' <option value="">无</option> ';
	}
	var $selecthtml=' <td><select name="dtoList['+indexNumbers+'].firstsdWeekSubjectId" id="" class="form-control firstsdWeekSubjectId" disabled="disabled" onChange="changeFirstsdWeek(this)">'
			    	+' <option value="">无</option> ';
	for(var kk in dtoAllMap){
		if(dtoAllMap[kk]!=null){
			if(kk.indexOf(subjectId)==-1){
				$selecthtml=$selecthtml+' <option value="'+kk+'">'+dtoAllMap[kk]+'</option> ';
			}
		}
		
	}		    		
	$selecthtm=$selecthtml+	' </select></td>'
	return $selecthtm;
}
/**
 * 单双周改变选项事件
 * @param obj
 * @returns
 */
function changeFirstsdWeek(obj){
	opoNum++;
	changeFirstsdWeekEffect(obj);
	countTotal();
}
/**
 * 复选框增加check属性值
 * @param obj
 * @returns
 */
function checkChange(obj) {
	if($(obj).attr("checked")){
		$(obj).attr("checked",false);
	}else{
		$(obj).attr("checked",true);
	}	
}



function deleteOneSubject(subjectIdType){
	//去除关联值
	$(".firstsdWeekSubjectId").each(function(){
		var subjectTypeId=$(this).parents("tr").find("input[name='subjectIdType']").val();
		if(subjectTypeId!=subjectIdType){
			var deleteOption=null;
			$(this).find("option").each(function(){
				if($(this).attr("value")==subjectIdType){
					deleteOption=$(this);
					return false;
				}
			})
			if(deleteOption!=null){
				deleteOption.remove();
			}
		}
	})
	if(dtoAllMap[subjectIdType]){
		//删除
		dtoAllMap[subjectIdType]=null;
	}
	//删除关联数据
	for(var ff in noContinueSubject){
		if(ff.indexOf(subjectIdType)>-1){
			noContinueSubject[ff]="";
		}
	}
	deleteCopyCourse(subjectIdType);
	
	makeNoContinueNum();
}


//删除科目
function deleteCopyCourse(subjectIdType){
	$(".copyCourse").find("label").each(function(){
		if($(this).find("input[type='checkbox']").val()==subjectIdType){
			$(this).remove();
			return false;
		}
	})
}

//增加一行科目相对应需要增加的地方
function addSomeSubjectInOther(addCourseMap){
	//每个单双周下拉增加科目
	$(".firstsdWeekSubjectId").each(function(){
		var subjectTypeId=$(this).parents("tr").find("input[name='subjectIdType']").val();
		var arr2=subjectTypeId.split("-");
		for(var keykey in addCourseMap){
			if(keykey.indexOf(arr2[0])==-1){
				$(this).append('<option value="'+keykey+'">'+addCourseMap[keykey]+'</option>');
			}
		}
	})
	//复制弹出框增加
	for(var keykey in addCourseMap){
		addCopyCourse(keykey,addCourseMap[keykey]);
	}
}

//增加科目
function addCopyCourse(subjectIdType,subjectName){
	var labelhtml='<label class=" mb10 mr10 w155 ellipsis" ><input type="checkbox" class="wp" value="'+subjectIdType+'" onclick="checkChange(this)"><span class="lbl"> '+subjectName+'</span></label>'+
	' ';
	$(".copyCourse").append(labelhtml);
}
 
/**
 * 
 * @param objArr [[id,name,'0'],[id,name,'1']] 1:选中
 * @param selectName
 * @param selectId
 * @param selectClass
 * @returns
 */
function addSelectHtml(objArr,selectName,selectId,selectClass,isFunction){
	var $html='<select name="'+ selectName +'" id="'+ selectId +'" class="form-control '+selectClass+'" styple="min-width:90px;" >';
	if(isFunction){
		$html='<select name="'+ selectName +'" id="'+ selectId +'" class="form-control '+selectClass+'" styple="min-width:90px;" onChange="changeFunctionOpo(this)">';
	}
	for(var i=0;i<objArr.length;i++){
		if(objArr[i][2]=='0'){
			$html+='<option value="'+objArr[i][0]+'" >'+objArr[i][1]+'</option>';
		}else{
			$html+='<option value="'+objArr[i][0]+'" selected="selected" >'+objArr[i][1]+'</option>';
		}
		
	}
	$html+='</select>';
	return $html;
}
/**
 * 根据周课时改变连堂是否可操作
 * @param obj
 * @param num
 * @returns
 */
function changeIsDisabled(obj,num){
	if($(obj).parent().hasClass("period-number")){
		$tr=$(obj).parents("tr");
		changePeriodEffect($tr);
		
	}else if($(obj).parent().hasClass("courseCoupleTimes-number")){
		if(num==0){
			$(obj).parents("tr").find(".courseCoupleTimes-times").find("input[type='radio']").attr("disabled",true);
		}else{
			$(obj).parents("tr").find(".courseCoupleTimes-times").find("input[type='radio']").attr("disabled",false);
		}
	}
}
//某一行连堂次数是否disabled
function changeDisabled(trObj,flag){
	$(trObj).find(".courseCoupleTimes-number > button").attr("disabled",flag);
	$(trObj).find(".courseCoupleTimes-number").find("input[type='text']").attr("disabled",flag);
}
//复制
function copySubject(obj){
	var keyId=$(obj).parents("tr").find("input[name='subjectIdType']").val();
	var subjectName=$(obj).parents("tr").find(".td-subjectName").html().trim();
	$('.layer-copyCourseParm').find(".nowSubjectName").html(subjectName);
	$(".copyCourse").find("label").each(function(){
		$(this).find("input[type='checkbox']").attr("checked",false);
		if($(this).find("input[type='checkbox']").val()==keyId){
			$(this).find("input[type='checkbox']").attr("disabled",true);
		}else{
			$(this).find("input[type='checkbox']").attr("disabled",false);
		}
	})
	clearCheckedForCopy();
	
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '复制到',
		area: '620px',
		btn: ['确定', '取消'],
		btn1:function(){
			var chooseSubMap={};
			var chooseSubNum=0;
			$(".copyCourse").find("input[type='checkbox']").each(function(){
				var vv=$(this).val();
				if($(this).attr("disabled")){
					
				}else{
					if($(this).attr("checked")){
						chooseSubMap[vv]=vv;
						chooseSubNum++;
					}
				}
			})
			var chooseData={};
			var chooseCopyOpta={};
			var chooseCopyOptaNum=0;
			var $tr=$(obj).parents("tr");
			$(".copyContentOpto").find("input[type='checkbox']").each(function(){
				var vv=$(this).val();
				if($(this).attr("checked")){
					chooseCopyOpta[vv]=vv;
					chooseCopyOptaNum++;
					if(vv=="1"){
						chooseData[vv]=new Array($(obj).parents("tr").find(".courseWorkDay").val());
					}else if(vv=="2"){
						var courseCoupleTimes=$tr.find(".courseCoupleTimes").val();
						var courseCoupleType=$tr.find("input[type='radio']:checked").val();
						var courseCoupleTypeTimes=$tr.find(".courseCoupleTypeTimes").val();
						chooseData[vv]=new Array(courseCoupleTimes,courseCoupleType,courseCoupleTypeTimes);
					}else if(vv=="3"){
						//不连排科目
						var l=new Array()
						var mm=0;
						for(var kkk in noContinueSubject){
							if(kkk.indexOf(keyId)>-1){
								var arr=kkk.split("_");
								var c=arr[0];
								if(c==keyId){
									c=arr[1];
								}
								l[mm]=c+"_"+noContinueSubject[kkk];
								mm++;
							}
						}
						chooseData[vv]=l;
					}else if(vv=="4"){
						chooseData[vv]=new Array($tr.find(".arrangeDay").val());
					}else if(vv=="5"){
						chooseData[vv]=new Array($tr.find(".arrangeHalfDay").val());
					}else if(vv=="6"){
						chooseData[vv]=new Array($tr.find(".noArrangeTime").val(),$tr.find(".noArrangeTime").parent().find(".js-hover-num").html());
					}else{
						chooseData[vv]=new Array($tr.find(".arrangePrior").val());
					}
				}
			})
			if(chooseSubMap && chooseSubNum>0){
				if(chooseCopyOpta && chooseCopyOptaNum>0){
					$("#subjectTable").find("tbody").find("tr").each(function(){
						var chooseSub=$(this).find("input[name='subjectIdType']").val();
						if(chooseSubMap[chooseSub]){
							//复制
							for(var tt in chooseCopyOpta){
								if(tt=="1"){
									$(this).find(".courseWorkDay").val(chooseData[tt][0]);
									if(chooseData[tt][0] !="" && parseInt(chooseData[tt][0])>1){
										changeDisabled($(this),false);
									}else{
										changeDisabled($(this),true);
									}
								}else if(tt=="2"){
									$(this).find(".courseCoupleTimes").val(chooseData[tt][0]);
									
									$(this).find("input[type='radio'][value='"+chooseData[tt][1]+"']").prop("checked",true);
									$(this).find(".courseCoupleTypeTimes").val(chooseData[tt][2]);
									if(chooseData[tt][0] !="" && parseInt(chooseData[tt][0])>0){
										$(this).find("input[type='radio']").attr("disabled",false);
									}else{
										$(this).find("input[type='radio']").attr("disabled",true);
									}
									
								}else if(tt=="3"){
									//不连排科目
									if(chooseData[tt].length>0){
										var data1=chooseData[tt];
										for(var j=0;j<data1.length;j++){
											var dd=data1[j].split("_");
											if(dd[0]!=chooseSub){
												var dd1=dd[0]+"_"+chooseSub;
												var dd2=chooseSub+"_"+dd[0];
												if(noContinueSubject[dd1]){
													noContinueSubject[dd1]=dd[1];
												}else{
													noContinueSubject[dd2]=dd[1];
												}
											}
											
										}
									}
									//计算数量noContinueSubject
								}else if(tt=="4"){
									$(this).find(".arrangeDay").val(chooseData[tt][0]);
								}else if(tt=="5"){
									$(this).find(".arrangeHalfDay").val(chooseData[tt][0]);
									
								}else if(tt=="6"){
									var noTimeType = "1";
									noTimeType = $(".copyContentOpto").find("[name='noTimeType']:checked").val();
									console.log("noTimeType = "+noTimeType);
									//禁排时间复制时只增不减
									var oldNoArrngeTime=$(this).find(".noArrangeTime").val();
									var oldNoNum=$(this).find(".noArrangeTime").parent().find(".js-hover-num").html();
									var oldnum1=0;
									if(oldNoNum!=""){
										oldnum1=parseInt(oldNoNum);
									}
									if(oldNoArrngeTime=="" || noTimeType == "2"){
										$(this).find(".noArrangeTime").val(chooseData[tt][0]);
										$(this).find(".noArrangeTime").parent().find(".js-hover-num").html(chooseData[tt][1]);
									}else{
										if(chooseData[tt][0]!=""){
											var arr=chooseData[tt][0].split(",");
											for(var ss=0;ss<arr.length;ss++){
												if(oldNoArrngeTime.indexOf(arr[ss])>-1){
													
												}else{
													oldNoArrngeTime=oldNoArrngeTime+","+arr[ss];
													oldnum1=oldnum1+1;
												}
											}
											$(this).find(".noArrangeTime").val(oldNoArrngeTime);
											$(this).find(".noArrangeTime").parent().find(".js-hover-num").html(oldnum1);
										}else{
											//无需修改
										}
									}
									
								}else{
									$(this).find(".arrangePrior").val(chooseData[tt][0]);
								}
							}
							if(chooseData["3"] && (chooseData["3"].length>0)){
								//不连排科目有调整
								makeNoContinueNum();
							}
						}
					})
					opoNum++;
					layer.closeAll();
					countTotal();
				}else{
					layer.msg('请先选择需要复制的内容！', {
						icon: 2,
						time: 1500,
						shade: 0.2
					});
				}
				
			}else{
				layer.msg('请先选择需要复制的科目！', {
					icon: 2,
					time: 1500,
					shade: 0.2
				});
			}
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-copyCourseParm')
	});
}

function clearCheckedForCopy(){
	$(".copyContentOpto").find("input[type='checkbox']").each(function(){
		var vv=$(this).val();
		if($(this).attr("checked")){
			$(this).attr("checked",false);
		}
	});
	$(".copyContentOpto").find("input:radio:eq(0)").click();
}
	/**
	 * 不连排科目 数量统一设置
	 */
	function makeNoContinueNum(){
		var numMap={};
		for(var kkk in noContinueSubject){
			if(noContinueSubject[kkk]==null || noContinueSubject[kkk]==""){
				continue;
			}
			var arr=kkk.split("_");
			if(numMap[arr[0]]){
				numMap[arr[0]]=numMap[arr[0]]+1;
			}else{
				numMap[arr[0]]=1;
			}
			if(numMap[arr[1]]){
				numMap[arr[1]]=numMap[arr[1]]+1;
			}else{
				numMap[arr[1]]=1;
			}
		}
		$(".noContinueNum").each(function(){
			var chooseSub=$(this).parents("tr").find("input[name='subjectIdType']").val();
			if(numMap[chooseSub]){
				$(this).html(numMap[chooseSub]);
			}else{
				$(this).html(0);
			}
		})
		return ;
	}
	/**
	 * 组装不连排科目 科目1_科目2_type,科目1_科目2_type,....
	 */
	function makeNoContinueStr(){
		var str="";
		for(var kkk in noContinueSubject){
			if(noContinueSubject[kkk]==null || noContinueSubject[kkk]==""){
				continue;
			}
			str=str+","+kkk+"_"+noContinueSubject[kkk];
		}	
		if(str!=""){
			str=str.substring(1);
		}
		return str;
	}
	/**
	 * 单双周 一科目对一科目
	   周课时>=连排*2
	 */
	function checkSubjectIdParm(){
		var mmap={};
		var f=true;
		
		$("#subjectTable tbody").find("tr").each(function(){
			var id=$(this).find('.firstsdWeekSubjectId').val();
			if(id!=""){
				var keyId=$(this).find("input[name='subjectIdType']").val();
				if(mmap[keyId] && mmap[keyId]!=id){
					f=false;
					layer.tips("单双周,存在一科目对多科目！",$(this).find('.firstsdWeekSubjectId'), {
						tipsMore: true,
						tips: 3
					});
					return false;
				}else{
					mmap[keyId]=id;
				}
				
				if(mmap[id] && mmap[id]!=keyId){
					f=false;
					layer.tips("单双周,存在一科目对多科目！",$(this).find('.firstsdWeekSubjectId'), {
						tipsMore: true,
						tips: 3
					});
					return false;
				}else{
					mmap[id]=keyId;
				}
			}
			//周课时
			var courseWorkDay=$(this).find('.courseWorkDay').val();
			var courseWorkDayInt=0;
			if(courseWorkDay!=""){
				if (!/^\d+$/.test(courseWorkDay)) {
					layer.tips("请输入整数！",$(this).find('.courseWorkDay'), {
						tipsMore: true,
						tips: 3
					});
					f=false;
					return false;
				}
				courseWorkDayInt=parseInt(courseWorkDay);
			}else{
				$(this).find('.courseWorkDay').val(0);
				$(this).find('.courseCoupleTimes').val(0);
			}
			
			var courseCoupleTimes=$(this).find('.courseCoupleTimes').val();
			var courseCoupleTimesInt=0;
			if(courseCoupleTimes!=""){
				if (!/^\d+$/.test(courseCoupleTimes)) {
					layer.tips("请输入整数！",$(this).find('.courseCoupleTimes'), {
							tipsMore: true,
							tips: 3
						});
					f=false;
					return false;
				}
				courseCoupleTimesInt=parseInt(courseCoupleTimes);
			}else{
				$(this).find('.courseCoupleTimes').val(0);
			}
			
			if(courseWorkDayInt>0 && courseWorkDayInt<(courseCoupleTimesInt*2)){
				layer.tips("周课时数不能小于2倍的连堂次数！",$(this).find('.courseWorkDay'), {
						tipsMore: true,
						tips: 3
					});
				f=false;
				return false;
			}
			
		})
		return f;
	}
	
	//快捷设置  批量修改改变周课时--连堂次数
	function changeWeekPeriod(){
		$("#subjectTable tbody").find("tr").each(function(){
			var courseWorkDay=$(this).find('.courseWorkDay').val();
			var courseCoupleTimesInt=0;
			//周课时
			var courseWorkDay=$(this).find('.courseWorkDay').val();
			var courseWorkDayInt=0;
			if(courseWorkDay!=""){
				if (!/^\d+$/.test(courseWorkDay)) {
					layer.tips("请输入整数！",$(this).find('.courseWorkDay'), {
						tipsMore: true,
						tips: 3
					});
					return true;
				}
				courseWorkDayInt=parseInt(courseWorkDay);
			}else{
				$(this).find('.courseWorkDay').val(0);
				$(this).find('.courseCoupleTimes').val(0);
			}
			
			if(courseWorkDayInt>0){
				//单双周科目去除disabled
				$(this).find(".firstsdWeekSubjectId").attr("disabled",false);
			}else{
				var chose=$(this).find(".firstsdWeekSubjectId").val();
				if(chose!=""){
					//同时去除选中的单双周以及关联数据
					$(this).find(".firstsdWeekSubjectId").val('');
					changeFirstsdWeekEffect($(this).find(".firstsdWeekSubjectId"));
				}
				$(this).find(".firstsdWeekSubjectId").attr("disabled",true);
			}
			if(courseWorkDayInt>1){
				
				changeDisabled($(this),false);
			}else{
				
				changeDisabled($(this),true);
			}
			
			var courseCoupleTimes=$(this).find('.courseCoupleTimes').val();
			var courseCoupleTimesInt=0;
			if(courseCoupleTimes!=""){
				if (!/^\d+$/.test(courseCoupleTimes)) {
					layer.tips("请输入整数！",$(this).find('.courseCoupleTimes'), {
							tipsMore: true,
							tips: 3
						});
					return true;
				}
				courseCoupleTimesInt=parseInt(courseCoupleTimes);
			}else{
				$(this).find('.courseCoupleTimes').val(0);
			}
			if(courseCoupleTimesInt>0){
				$(this).find("input[type='radio']").attr("disabled",false);
			}else{
				$(this).find("input[type='radio']").attr("disabled",true);
			}
		})
	}
	/**
	 * 修改周课时数 对 修改周课时数 单双周科目连堂次数 连堂时间范围
	 * 1、周课时大于0 单双周科目可以操作
	 * 2、周课时==0  单双周科目不可以操作 如果有单双周科目 需要去除
	 * 		       单双周科目连堂次数 连堂时间范围都不能操作
	 * 3、周课时>1   连堂次数可以操作 周课时>=2*连堂次数这个保存的时候判断
	 * @param obj 选中的某一行值
	 * @returns
	 */
	function changePeriodEffect(obj){
		var courseWorkDay=$(obj).find('.courseWorkDay').val();
		var courseCoupleTimesInt=0;
		//周课时判断是不是符合
		var courseWorkDay=$(obj).find('.courseWorkDay').val();
		var courseWorkDayInt=0;
		if(courseWorkDay!=""){
			if (!/^\d+$/.test(courseWorkDay)) {
				layer.tips("请输入整数！",$(obj).find('.courseWorkDay'), {
					tipsMore: true,
					tips: 3
				});
				return true;
			}
			courseWorkDayInt=parseInt(courseWorkDay);
		}else{
			$(obj).find('.courseWorkDay').val(0);
			$(obj).find('.courseCoupleTimes').val(0);
		}
		
		//单双周判断
		if(courseWorkDayInt>0){
			//单双周科目去除disabled
			//debugger;
			console.log("subjectType="+$(obj).find(".subjectType").val());
			if($(obj).find(".subjectType").val() == "O")
				$(obj).find(".firstsdWeekSubjectId").attr("disabled",false);
		}else{
			var chose=$(obj).find(".firstsdWeekSubjectId").val();
			if(chose!=""){
				//同时去除选中的单双周以及关联数据
				$(obj).find(".firstsdWeekSubjectId").val('');
				//直接变成空 
				changeFirstsdWeekEffect($(obj).find(".firstsdWeekSubjectId"));
			}
			$(obj).find(".firstsdWeekSubjectId").attr("disabled",true);
		}
		
		if(courseWorkDayInt>1){
			changeDisabled($(obj),false);
		}else{
			changeDisabled($(obj),true);
		}
		//连堂次数
		var courseCoupleTimes=$(obj).find('.courseCoupleTimes').val();
		var courseCoupleTimesInt=0;
		if(courseCoupleTimes!=""){
			if (!/^\d+$/.test(courseCoupleTimes)) {
				layer.tips("请输入整数！",$(obj).find('.courseCoupleTimes'), {
						tipsMore: true,
						tips: 3
					});
				return true;
			}
			courseCoupleTimesInt=parseInt(courseCoupleTimes);
		}else{
			$(obj).find('.courseCoupleTimes').val(0);
		}
		if(courseCoupleTimesInt>0){
			$(obj).find("input[type='radio']").attr("disabled",false);
		}else{
			$(obj).find("input[type='radio']").attr("disabled",true);
		}
		
	}
	
	
	/**
	 * 单双周改变后的影响
	 * @param obj
	 * @returns
	 */
	function changeFirstsdWeekEffect(obj){
		var rowkey=$(obj).parents("tr").find("input[name='subjectIdType']").val();
		var firstSubjectId=$(obj).val();
		//数据改变为空 直接去除关联数据
		if(firstSubjectId==""){
			$("#subjectTable tbody").find("tr").each(function(){
				var keyId=$(this).find("input[name='subjectIdType']").val();
				var firstSId=$(this).find(".firstsdWeekSubjectId").val();
				if(firstSId!=""){
					if(keyId!=rowkey){
						if(firstSId==rowkey){
							$(this).find('.firstsdWeekSubjectId').val("");
						}
					}
				}
				
			})
		}else{
			//切换单双周会出现的 有关联 关联地方加上单科
			$("#subjectTable tbody").find("tr").each(function(){
				var keyId=$(this).find("input[name='subjectIdType']").val();
				if(keyId!=rowkey){
					if(keyId==firstSubjectId){
						$(this).find('.firstsdWeekSubjectId').val(rowkey);
						
						//如果周课时==0 增加1
						var courseWorkDay=$(this).find('.courseWorkDay').val();
						var courseCoupleTimesInt=0;
						//周课时判断是不是符合
						var courseWorkDay=$(this).find('.courseWorkDay').val();
						var courseWorkDayInt=0;
						if(courseWorkDay!=""){
							if (!/^\d+$/.test(courseWorkDay)) {
								//不是整数 暂定为0
							}else{
								courseWorkDayInt=parseInt(courseWorkDay);
							}
							
						}
						if(courseWorkDayInt==0){
							$(this).find('.courseWorkDay').val(1);
							//根据courseWorkDayInt
							$(this).find('.firstsdWeekSubjectId').attr("disabled",false);
						}
					}else{
						if($(this).find('.firstsdWeekSubjectId').val()==firstSubjectId){
							$(this).find('.firstsdWeekSubjectId').val("");
						}else if($(this).find('.firstsdWeekSubjectId').val()==rowkey){
							$(this).find('.firstsdWeekSubjectId').val("");
						}
					}
				}
			})
		}
		
	}

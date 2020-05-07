<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<style>
.publish-course span{
	min-width:90px;
	width:auto;
}
</style>
<script type="text/javascript" >
	function refList(useMaster){
		var cId=$('#course-tabs-ul a.active').attr('data-course-id');
		var url="${request.contextPath}/newgkelective/${divideId!}/teacherClass/selected?gradeId=${gradeId!}&arrayId=${arrayId!}"
			+"&arrayItemId=${arrayItemId!}&courseId="+cId+"&useMaster="+useMaster;
		$('#teacherArrangeDiv').load(url);
	}
	// 同步教师 其他年级 已安排课程，作为 此处排课禁排时间
	function refreshTeacherExisted(){
		var url = '${request.contextPath}/newgkelective/${divideId!}/teacher/updateExisted';
		console.log("url = "+url);
		var ii = layer.load();
		$.ajax({
			url:url,
			type:'post', 
			data:{"arrayItemId":"${arrayItemId!}"},
			dataType:'json',
			success:function(data){
				layer.closeAll();
 			 	layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
		    	if(data.success){
					refList();
		 		}
			}
		});
	}
	// 同步 教师组 禁排时间
	function refreshTeacherGroupTime(){
		var url = '${request.contextPath}/newgkelective/${divideId!}/teacher/updateTeacherGroupTime';
		console.log("url = "+url);
		var ii = layer.load();
		$.ajax({
			url:url,
			type:'post', 
			data:{"arrayItemId":"${arrayItemId!}"},
			dataType:'json',
			success:function(data){
				layer.closeAll();
				
		    	if(data.success){
	 			 	layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
					refList();
		 		}else{
		 			alert("失败："+data.msg);
		 		}
			}
		});
	}
	
	var isSaveTea=false;
    function saveTeaCla(){
    	if(isSaveTea){
    		return false;
    	}
    	isSaveTea=true;
    	if(!checkValue('#myTeaForm')){
    		isSaveTea=false;
    		return false;
    	}
    	
    	$('.mutexTea-td').each(function(){
    		var tid = $(this).attr('mutexTea-id');
    		var nid=$(this).attr('mutexnum-id');
    		var tidval = $('#'+tid).val();
    		var nidval = parseInt($('#'+nid).val());
    		if(tidval=='' && nidval > 0){
    			layer.tips('没有维护互斥教师时，互斥课时数不能维护',$('#'+nid), {
					tipsMore: true,
					tips: 3
				});
				isSaveTea=false;
    			return false;
    		}
    		var ctid=$('#'+tid).data('tid');
    		if(tidval.indexOf(ctid)>=0){
    			layer.tips('教师不能选择自己为互斥教师',$('#'+$(this).attr('mutexTea-name')), {
					tipsMore: true,
					tips: 3
				});
				isSaveTea=false;
    			return false;
    		}
    	});
    	if(!isSaveTea){
    		return false;
    	}
        $("td.itemArea").each(function(){
            var ids = new Array();
            if($(this).find(".item")){
            	$(this).find(".item").each(function(){
	               var id=$(this).find(".bj").attr("pid");
	                ids.push(id);
	            });
            }
            
            var classIds = "";
            if(ids.length > 0){
                for(var i=0;i<ids.length;i++){
                    if(i == 0){
                        classIds +=ids[i];
                    }else{
                        classIds +=","+ids[i];
                    }
                }
            }
            var cid = $(this).attr('id')+'classIds';
            $('#'+cid).val(classIds);
        });
        var options = {
            url : '${request.contextPath}/newgkelective/${divideId}/subjectTeacherArrange/saveClass',
            data: {"itemId" : '${arrayItemId!}',"arrayId":"${arrayId!}","subjectId":"${subjectId!}"},
            dataType : 'json',
            success : function(data){
                if(data.success){
                    layer.msg(data.msg, {offset: 't',time: 2000});
					refList("1");
                }
                else{
                    isSaveTea=false;
                    layerTipMsg(data.success,"保存失败",data.msg);
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#myTeaForm").ajaxSubmit(options);
        return true;
    }
</script>

<style>
.classroomArrangementSimple .pastDistribution .item {width:125px !important;}
</style>
<form id="myTeaForm">
<div class="tab-pane active" role="tabpanel">
	<div class="classroomArrangement classroomArrangementSimple clearfix">
		<div class="float-left" style="width: 20%;">
			<table class="table no-margin table-bordered" >
				<thead>
					<tr>
						<th>&nbsp;</th>
					</tr>
					<tr>
						<th rowspan="2">未安排的班级</th>
					</tr>
				</thead>
			</table>
            <div class="opt">
                <div><a class="btn btn-blue btn-block js-random" href="javascript:">智能安排</a></div>
                <div><a class="btn btn-white btn-block js-teacherSelect" href="javascript:">重新安排</a></div>
            </div>
			<div class="willDistribution" style="height: 400px;">
				<#if batchKeys?exists && batchKeys?size gt 0>
				<#list batchKeys as batchKey>
				<div>
					<p class="category">
						<span class="num <#if batchKey[1]=='A'> xuan <#elseif batchKey[1]=='B'> xue <#else> xing </#if>"><#if batchKey[2]?default('')==''>&nbsp;<#else>${batchKey[2]!}</#if></span>
						<strong>${batchKey[3]!}</strong>
					</p>
					<div class="clearfix category-itemList">
						<#assign bathClass=bathClassMap[batchKey[0]]>
						<#if bathClass?exists && bathClass?size gt 0>
                        <#list bathClass as cla>
                            <a class="item" href="javascript:" data-value="${batchKey[0]!}" clatime="${cla.classNum?default(0)}">
								<span class="del">&times;</span>
								<span class="huan">换</span>
								<span class="bj" pid="${cla.id}" title='${cla.className!}'>
									<span class="num <#if batchKey[1]=='A'> xuan <#elseif batchKey[1]=='B'> xue <#else> xing </#if>"><#if batchKey[2]?default('')==''>&nbsp;<#else>${batchKey[2]!}</#if></span>
									${cla.className!}
								</span>
							</a>
                        </#list>
                        <#else>
                        	<p class='color-999 nodata-class'>暂无待安排的班级</p>
                        </#if>
					</div>
				</div>
				</#list>
				</#if>
			</div>												
		</div>
		<div class="float-left" style="width: 80%;">
			<table class="table table-bordered no-margin">
				<thead>
					 <tr>
			          	<th width="30" rowspan="2"></th>
			            <th width="90" rowspan="2">任课教师</th>
			            <th rowspan="2">任课班级</th>
			            <th width="100" rowspan="2">每天课时</br>分配</th>
			            <th width="100" rowspan="2">周课时</br>分布</th>
			            <th width="50" rowspan="2">周课</br>时数</th>
			            <th class="text-center" colspan="2">互斥设置</th>
			            <th width="50" rowspan="2">禁排</br>时间</th>
			            <th width="60" rowspan="2">操作</th>
			            <th width="19" rowspan="2"></th>
			        </tr>
			        <tr>
			            <th width="145">互斥教师</th>
			            <th width="93">互斥课时数</th>
			        </tr>
				</thead>
			</table>	
			<div class="pastDistribution" style="height: 440px;">
				<table class="table table-bordered" style="margin-top: -1px;margin-bottom: -1px;">
					<tbody>
					 	<#if planExList?exists && planExList?size gt 0>
   						 <#list planExList as planEx >
                    	<tr>
                    		<td width="30" class="arrow">
								<span class="glyphicon glyphicon-arrow-right"></span>
							</td>
							<td width="90" class="text-center">
								<input type="hidden" name="teacherPlanExList[${planEx_index}].id" value="${planEx.id!}" >
					            <input type="hidden" name="teacherPlanExList[${planEx_index}].teacherPlanId" value="${planEx.teacherPlanId!}" >
					            <input type="hidden" name="teacherPlanExList[${planEx_index}].teacherId" value="${planEx.teacherId!}" >
					            <input type="hidden" name="teacherPlanExList[${planEx_index}].classNum" value="${planEx.classNum!}" >
					            <input type="hidden" name="teacherPlanExList[${planEx_index}].classIds" id="${courseId}${planEx.teacherId!}classIds" class="classIds"  value="" >
					            <input type="hidden" name="teacherPlanExList[${planEx_index}].creationTime" value="${(planEx.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >
					            <input type="hidden" name="teacherPlanExList[${planEx_index}].modifyTime" value="${(planEx.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >
								${planEx.teacherName!}
							</td>
							
							<td class="itemArea" id="${courseId}${planEx.teacherId!}" data-value="${planEx.teacherId!}" >
								 <#if planEx.classIdList?exists &&  planEx.classIdList?size gt 0>
			                        <#assign classIds = planEx.classIdList >
			                        <#list classIds as classId>
			                            <#if divideClassIdMap[classId]??>
			                            <#assign cla =divideClassIdMap[classId]! >
			                            
			                             <a class="item" href="javascript:" data-value="${cla.subjectType!}_${cla.batch!}" clatime="${cla.classNum?default(0)}">
											<span class="del">&times;</span>
											<span class="huan">换</span>
											<span class="bj" pid="${cla.id}" title='${cla.className!}'>
												<span class="num <#if cla.subjectType?default('')=='A'> xuan <#elseif cla.subjectType?default('')=='B'> xue <#else>xing</#if>"><#if cla.batch?default('')==''>&nbsp;<#else><#if batchMap??>${batchMap[cla.batch]!}<#else>${cla.batch!}</#if></#if></span>
												${cla.className!}
											</span>
										</a>
			                            </#if>
			                        </#list>
			                    </#if>
							</td>
							<td width="100">
						    	<select name="teacherPlanExList[${planEx_index}].dayPeriodType" class="form-control">
									${mcodeSetting.getMcodeSelect('DM-KSFP',(planEx.dayPeriodType?default(1))?string,'0')}
								</select>
						    </td>
						    <td width="100">
						    	<select name="teacherPlanExList[${planEx_index}].weekPeriodType" class="form-control">
						    		${mcodeSetting.getMcodeSelect('DM-KSFB',(planEx.weekPeriodType?default(1))?string,'0')}>
								</select>
				   			</td>
							<td width="50"  id="weekTimes${planEx_index}" class="${courseId}${planEx.teacherId!}weekTimes">${planEx.weekTime!'0'}</td>
				            <td width="145" class="mutexTea-td" mutexTea-id="mutexTeaIds${planEx_index}" mutexnum-id="mutexNum${planEx_index}"  mutexTea-name="mutexTeaNameList${planEx_index!}">
								<input type="hidden" id="mutexTeaIds${planEx_index!}" name="teacherPlanExList[${planEx_index}].mutexTeaIds" value="${planEx.mutexTeaIds!}" data-tid="${planEx.teacherId!}"/>
								<input type="hidden" name="mutexTeaNames${planEx_index}" id="mutexTeaNames${planEx_index}" value="${planEx.mutexTeaNames!}">
						    	<div class="clearfix js-hover">
						    		<span class="pull-left" id="mutexTeaNameList${planEx_index!}" style="width:90%;">
										<#if planEx.mutexTeaIdList??&&planEx.mutexTeaIdList?size gt 0>
										<#list planEx.mutexTeaIdList as tid>
						    			<a class="item" href="javascript:void(0);" data-tid="${tid!}" data-ind="${tid_index!}" onClick="doRemoveThis(this,'${planEx_index!}')">
						    				<span class="del">&times;</span>
						    				<span class="bj">${teacherNameMap[tid]!}</span>
						    			</a>
						    			</#list>
				    					</#if>
						    		</span>
							    	<span class="pull-right js-hover-opt" style="width:10%;">
							    		<a href="javascript:doClickTea('${planEx_index}');" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑"><i class="fa fa-edit color-blue"></i></a>
							    	</span>
						    	</div>
							</td>
				            <td width="93">
				            	<div class="form-number form-number-sm" data-step="1">
						    		<input type="text" name="teacherPlanExList[${planEx_index!}].mutexNum" id="mutexNum${planEx_index!}" class="form-control" value="${planEx.mutexNum?default(0)}" vtype="int" min="0" >
						    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
							    	<button class="btn btn-sm btn-default btn-block form-number-sub" <#if planEx.mutexNum?default(0)==0>disabled="disabled"</#if>><i class="fa fa-angle-down"></i></button>
				    			</div>
				            </td>
				            <td width="50">
						    	<div class="clearfix js-hover">
						    		<span class="pull-left js-hover-num color-blue notime_${planEx.teacherId!}">${planEx.noTimeStr!}</span>
							    	<span class="pull-right js-hover-opt" style="display: block;">
							    		<a href="javascript:void(0)" data-toggle="tooltip" data-placement="top" title="编辑" teacherId="${planEx.teacherId!}" class="js-changeTime" data-original-title="编辑"><i class="fa fa-edit color-blue"></i></a>
							    	</span>
						    	</div>
						    </td>
						    <td width="60">
						    	<a href="javascript:void(0)" class="color-blue js-copy" data-toggle="tooltip" data-placement="top" title="复制到" data-original-title="复制到" data-value="${planEx.teacherId!}" data-tname="${planEx.teacherName!}" data-exid="${planEx.id!}" data-course="${courseId!}" >复制到</a>
						    </td>
						</tr>
                    	</#list>
                    	
                	</#if>
					</tbody>
				</table>
		</div>
	</div>

</div>
<@popupMacro.selectMoreTeacher clickId="teaNamesTemp" id="teaIdsTemp" name="teaNamesTemp" resourceUrl="${request.contextPath}/static" handler="dealTea()">
	<input type="hidden" id="teaIdsTemp" name="teaIdsTemp" value=""/>
	<input type="hidden" name="teaNamesTemp" id="teaNamesTemp" class="form-control" value="">
</@popupMacro.selectMoreTeacher>
<div class="navbar-fixed-bottom opt-bottom">
    <#--<a class="btn btn-white" onclick="backPreStep();" href="javascript:;">上一步</a>-->
    <a href="javascript:void(0)" class="btn btn-blue js-courseExportTeacher" onclick="courseExportTeacher()">教师任课信息导入</a>
	<#if !(isXzbDivide?default(true)) >
    <a  onclick="refreshTeacherGroupTime()" class="btn btn-blue "> 更新教师组禁排  </a>
	</#if>
    <a  onclick="refreshTeacherExisted()" class="btn btn-blue "> 更新教师已排课时间  </a>
    <a  onclick="saveTeaCla();" class="btn btn-blue "> 保存</a>
</div>
</form>
<script>
	var tindex='';
	function doClickTea(ind){
		tindex = ind;
		$('#teaNamesTemp').val($('#mutexTeaNames'+ind).val());
		$('#teaIdsTemp').val($('#mutexTeaIds'+ind).val());
		$('#teaNamesTemp').click();
	}

	function dealTea(){
		if(tindex && tindex != ''){
			$('#mutexTeaIds'+tindex).val($('#teaIdsTemp').val());
			$('#mutexTeaNames'+tindex).val($('#teaNamesTemp').val());
			var content = "";
			if($('#teaIdsTemp').val()!=""){
				var tids = $('#teaIdsTemp').val().split(",");
				var tnames = $('#teaNamesTemp').val().split(",");
				for(i=0;i<tids.length;i++){
					content+='<a class="item" href="javascript:void(0);" data-tid="'+tids[i]+'" data-ind="'+i+'" onClick="doRemoveThis(this,\''+tindex+'\')"><span class="del">&times;</span><span class="bj">'+tnames[i] +'</span></a>';
				}
			}
			$('#mutexTeaNameList'+tindex).html(content);
		}
	}

	function doRemoveThis(obj,ind){
		var tids = $('#mutexTeaIds'+ind).val().split(",");
		var tnames = $('#mutexTeaNames'+ind).val().split(",");
		tids.splice($(obj).data('ind'),1);
		tnames.splice($(obj).data('ind'),1);
		$(obj).siblings().each(function(){
			if($(this).data('ind')>$(obj).data('ind')){
				$(this).data('ind',$(this).data('ind')-1);
			}
		})
		$('#mutexTeaIds'+ind).val(tids.join(","));
		$('#mutexTeaNames'+ind).val(tnames.join(","));
		$(obj).remove();
	}

    function backPreStep(){
    	teaTable();
    }
    
    function findNoItem(){
    	$(".willDistribution").find(".nodata-class").remove();
    	$(".willDistribution").find(".category-itemList").each(function(){
    		if(!$(this).find(".item").length){
    			$(this).append("<p class='color-999 nodata-class'>暂无待安排的班级</p>")
    		}
    	})
    }
    
    
	
    
    $(function(){
	    // 教研组选老师
		$('.js-teacherSelect').on('click', function(){
			//var url ='${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherSet/selectTeacher';
			var url = '${request.contextPath}/newgkelective/${divideId!}/subjectTeacherArrange/add?selectTeacher=1&gradeId=${gradeId!}&itemId=${arrayItemId!}&arrayId=${arrayId!}&courseId=${courseId!}';
			indexDiv = layerDivUrl(url,{title: "选择教师",width:820,height:600});
		});
		$('.form-number button').on('click',function(e){
			e.preventDefault();
			var step = $(this).parents(".form-number").attr("data-step");
			step = Number(step);
			var $input = $(this).parents(".form-number").find("input");
			var num = $input.val();
			if(!num){
				num = 0;
			}
			num = Number(num);
			if($(this).hasClass("form-number-sub")){
				if(num - step <= 0){
					$input.val(0);
					$(this).attr("disabled","disabled");
				}else{
					$input.val(num - step);
				}
			}else if($(this).hasClass("form-number-add")){
				if($(this).siblings(".form-number-sub").prop("disabled")){
					$(this).siblings(".form-number-sub").prop("disabled",false);
				}
				$input.val(num + step);
			}
		});
    	$(".willDistribution").on('click','.item',function(){
			var $willitem = $(this);
			var $willitemparent = $(this).parent();
			var $willnum = $(this).find(".num").text();  // 批次值
			if(!$willnum){
				$willnum="";
			}
			var $willtype="";
			if($(this).find(".num").hasClass("xuan")){
				$willtype="xuan";
			}else if($(this).find(".num").hasClass("xue")){
				$willtype="xue";
			}else{
				$willtype="xing";
			}
			
			
			if(!$(".pastDistribution").find(".active").length){
				// 由左 向右 安排班级
				if($(this).hasClass("active")){
					// 取消选中
					$(this).removeClass("active")
					$(".pastDistribution .arrow").removeClass("current");
					$(".pastDistribution .item").removeClass("exchange");
				}else{
					// 选中班级
					$(".willDistribution .item").removeClass("active");
					$(this).addClass("active");
					$(".pastDistribution .arrow").removeClass("current");
					$(".pastDistribution .item").removeClass("exchange");
					
					$(".pastDistribution .itemArea").each(function(){
						var $children = $(this).children();
						var $length = $(this).children().length;
						<#if isFakeXzb!false>
							$(this).siblings(".arrow").addClass("current");
							//$($children).addClass("exchange");
							//return;
						</#if>
						if($willtype=="xing"){
							// 选中 行政班
							$(this).siblings(".arrow").addClass("current");
							if($length){
								$($children).each(function(){
									
									var $pastnum = $(this).find(".num").text();
									if(!$pastnum){
										$pastnum="";
									}
									var $pasttype="";
									if($(this).find(".num").hasClass("xuan")){
										$pasttype="xuan";
									}else if($(this).find(".num").hasClass("xue")){
										$pasttype="xue";
									}else{
										$pasttype="xing";
									}
									if($willnum == $pastnum && $willtype == $pasttype){
										$(this).addClass("exchange");
									}
								});
							}
						}else{
							// 选中教学班
							if($length){
								$($children).each(function(){
									var $pastnum = $(this).find(".num").text();
									if(!$pastnum){
										$pastnum="";
									}
									var $pasttype="";
									if($(this).find(".num").hasClass("xuan")){
										$pasttype="xuan";
									}else if($(this).find(".num").hasClass("xue")){
										$pasttype="xue";
									}else{
										$pasttype="xing";
									}
									if($willnum == $pastnum && $willtype == $pasttype){
										$(this).addClass("exchange");
									}
								});
								// 没有同一个批次的 班级在这个老师这里上课，则 可以添加
								if(!$(this).find(".exchange").length){
									$(this).siblings(".arrow").addClass("current")
								}
							}else{
								$(this).siblings(".arrow").addClass("current");
							}
						}
					});
				}
			}else{
				
				$(".pastDistribution .item").each(function(){
					if($(this).hasClass("active")){
						var $pastitem = $(this);
						var $pastitemparent = $(this).parent();
						if($willitem.hasClass("exchange")){
							$pastitemparent.append($willitem); 
							$willitemparent.append($pastitem); 
							
							toMakeWeeks($pastitemparent);			
							
							$(".classroomArrangement .item").removeClass("exchange active");
						}else{
							layer.msg('请选择加亮的教学班进行更换');
						}
					}
				});
				findNoItem();
			}	
					
		});
		
		
		$(".pastDistribution").on('click','.arrow',function(){
			var $itemArea = $(this).siblings(".itemArea");
			if($(this).hasClass("current")){
				// 右向左移动
				$(".willDistribution .item").each(function(){
					if($(this).hasClass("active")){
						var $active = $(this);
						$itemArea.append($active); 
						toMakeWeeks($itemArea);
						$(".pastDistribution .arrow").removeClass("current");
						$(".pastDistribution .item").removeClass("exchange active");
					}
				});
			}
			findNoItem();
		});
		// 点击右侧班级
		$(".pastDistribution").on('click','.item',function(){
			var $pastitem = $(this);
			var $pastitemparent = $(this).parent();
			var $pastnum = $(this).find(".num").text();
			if(!$pastnum){
				$pastnum="";
			}
			var $pasttype="";
			if($(this).find(".num").hasClass("xuan")){
				$pasttype="xuan";
			}else if($(this).find(".num").hasClass("xue")){
				$pasttype="xue";
			}else{
				$pasttype="xing";
			}
			
			var $tdId=$pastitemparent.attr("id");
			
			if(!$(".willDistribution").find(".active").length){
				// <-- 右至左移动
				if($pastitem.hasClass("active")){
					// 取消选中
					$pastitem.removeClass("active");
					$(".classroomArrangement .item").removeClass("exchange");
				}else{
					// 第一次选中
					if(!$(".pastDistribution").find(".active").length){
						$pastitem.addClass("active");
						$(".classroomArrangement .item").removeClass("exchange");
						$(".classroomArrangement .item").not(".active").each(function(){
							var $num = $(this).find(".num").text();
							if(!$num){
								$num="";
							}
							var $type="";
							if($(this).find(".num").hasClass("xuan")){
								$type="xuan";
							}else if($(this).find(".num").hasClass("xue")){
								$type="xue";
							}else{
								$type="xing";
							}
							if($(this).parent().hasClass('itemArea')){
								if($tdId==$(this).parent().attr("id")){
									return true;
								}
								<#if isFakeXzb!false>
								$(this).addClass("exchange");
								return true;
								</#if>
							}
							
							if($num == $pastnum && $type == $pasttype){
								$(this).addClass("exchange");
							}								
						});
					}else{
						// 已经选中某个点
						if($pastitem.hasClass("exchange")){
							// 和其他老师的班级进行交换
							$(".pastDistribution .item").each(function(){
								if($(this).hasClass("active")){
									var $activeitem = $(this);
									var $activeitemparent = $(this).parent();
									$pastitemparent.append($activeitem); 
									$activeitemparent.append($pastitem); 
									toMakeWeeks($pastitemparent);
									toMakeWeeks($activeitemparent);
									$(".classroomArrangement .item").removeClass("exchange active");
								}
							});
						}else if($pastitemparent.find(".active").length){
							// 点击同一个老师的 其他班级
							$pastitemparent.children().removeClass("active");
							$pastitem.addClass("active");
							$(".classroomArrangement .item").removeClass("exchange");
							$(".classroomArrangement .item").not(".active").each(function(){
								
								var $num = $(this).find(".num").text();
								if(!$num){
									$num="";
								}
								var $type="";
								if($(this).find(".num").hasClass("xuan")){
									$type="xuan";
								}else if($(this).find(".num").hasClass("xue")){
									$type="xue";
								}else{
									$type="xing";
								}	
								if($(this).parent().hasClass('itemArea')){
									if($tdId==$(this).parent().attr("id")){
										return true;
									}
									<#if isFakeXzb!false>
									$(this).addClass("exchange");
									return true;
									</#if>
								}
								
								if($num == $pastnum && $type == $pasttype){
									$(this).addClass("exchange");
								}								
							});
						}else{
							layer.msg('请选择加亮的教学班进行更换');
						}
					}
				}
			}else{
				// 左边已经有选中的点 和 右边的点 交换
				$(".willDistribution .item").each(function(){
					if($(this).hasClass("active")){
						var $willitem = $(this);
						var $willitemparent = $(this).parent();
						if($pastitem.hasClass("exchange")){
							$pastitemparent.append($willitem); 
							$willitemparent.append($pastitem); 
							toMakeWeeks($pastitemparent);
							$(".pastDistribution .arrow").removeClass("current");
							$(".classroomArrangement .item").removeClass("exchange active");
						}else{
							layer.msg('请选择加亮的教学班进行更换');
						}
					}
				});
			}	
			findNoItem();
		});
		$(".pastDistribution").on('click','.del',function(e){
			e.stopPropagation();
			var $item = $(this).parent();
			var $itemParent=$item.parent();
			var $pastnum = $item.find(".num").text();
			if(!$pastnum){
				$pastnum="";
			}
			var $pasttype="";
			if($item.find(".num").hasClass("xuan")){
				$pasttype="xuan";
			}else if($item.find(".num").hasClass("xue")){
				$pasttype="xue";
			}else{
				$pasttype="xing";
			}
			if($(".willDistribution").find(".active").length){
				if($item.hasClass("exchange")){
					$item.removeClass("exchange");
					$item.parent(".itemArea").siblings(".arrow").addClass("current");
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
							toMakeWeeks($itemParent);
						}
					});
				}else{
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						if(!$willnum){
							$willnum="";
						}
						var $willtype="";
						if($(this).find(".num").hasClass("xuan")){
							$willtype="xuan";
						}else if($(this).find(".num").hasClass("xue")){
							$willtype="xue";
						}else{
							$willtype="xing";
						}
						
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
							toMakeWeeks($itemParent);
						}
					});
				}
			}else if($(".pastDistribution").find(".active").length){
				if($item.hasClass("active")){
					$item.removeClass("active");
					$(".classroomArrangement .item").removeClass("exchange");
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						if(!$willnum){
							$willnum="";
						}
						var $willtype="";
						if($(this).find(".num").hasClass("xuan")){
							$willtype="xuan";
						}else if($(this).find(".num").hasClass("xue")){
							$willtype="xue";
						}else{
							$willtype="xing";
						}
						
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
							toMakeWeeks($itemParent);
						}
					});
				}else{
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						if(!$willnum){
							$willnum="";
						}
						var $willtype="";
						if($(this).find(".num").hasClass("xuan")){
							$willtype="xuan";
						}else if($(this).find(".num").hasClass("xue")){
							$willtype="xue";
						}else{
							$willtype="xing";
						}
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
							toMakeWeeks($itemParent);
						}
					});
				}
			}else{
				$(".willDistribution .category").each(function(){
					var $willnum = $(this).find(".num").text();
					if(!$willnum){
						$willnum="";
					}
					var $willtype="";
					if($(this).find(".num").hasClass("xuan")){
						$willtype="xuan";
					}else if($(this).find(".num").hasClass("xue")){
						$willtype="xue";
					}else{
						$willtype="xing";
					}
					if($willnum == $pastnum && $willtype == $pasttype){
						$(this).siblings().append($item);
						toMakeWeeks($itemParent);
					}
				});
			}
			findNoItem();
		});
    
    
   		var $window = $(window).height();
		$(".willDistribution").height($window-415);
		$(".pastDistribution").height($window-370.5);
		$("#course-tabs-ul").height($window-346.5);
    

		
		var typeLen = $('.publish-course-parent').length;
		var courseLen = $('.publish-course-parent span').length;
		var teacherLen = $('.publish-course-children').length;

		$('.js-random').click(function(){
			if($('.willDistribution').find(".item")){
				$('.willDistribution').find(".item").each(function(){
					$chooseClass=$(this);
					var bathKey=$(this).attr("data-value");
					var $td=getBestTeacher(bathKey);
					if($td!=null){
						$td.append($chooseClass.clone());
						$chooseClass.remove();
						toMakeWeeks($td);
					}
					
				});
				findNoItem();
			}
		
		});
		function getBestTeacher(bathKey){
			var $return=null;
			var moveMap={};
			var moveCount={};
			var moveClassCount={};
			var flag=false;
			
			if(bathKey.indexOf('xing')>-1){
				//行政班
				$(".pastDistribution .itemArea").each(function(){
					var tId=$(this).attr("data-value");
					var ttId=$(this).attr("id");
					if($(this).find(".item").length){
						var tm = parseInt($("."+ttId+"weekTimes").text());
						moveCount[tId]=tm;
						moveMap[tId]=$(this);
						moveClassCount[tId]=$(this).find(".item").length;
					}else{
						moveCount[tId]=0;
						moveMap[tId]=$(this);
						moveClassCount[tId]=0;
					}
				});
			}else{
				$(".pastDistribution .itemArea").each(function(){
					var tId=$(this).attr("data-value");
					var ttId=$(this).attr("id");
					if($(this).find(".item").length){
						flag=false;
						$(this).find(".item").each(function(){
							var bathKeyOne=$(this).attr("data-value");
							if(bathKey==bathKeyOne){
								flag=true;
								return false;
							}
						})
						if(!flag){
							var tm = parseInt($("."+ttId+"weekTimes").text());
							moveCount[tId]=tm;
							moveMap[tId]=$(this);
							moveClassCount[tId]=$(this).find(".item").length;
						}
					}else{
						moveCount[tId]=0;
						moveMap[tId]=$(this);
						moveClassCount[tId]=0;
					}
				});
			}
			
			//找到周课时少的老师，且班级少
			//找到最小的周课时对应的老师
			var minCount=-1;
			if(moveCount){
				for(var keykey in moveCount){
					if(moveCount[keykey]==0){
						minCount=moveCount[keykey];
						break;
					}
					if(minCount==-1){
						minCount=moveCount[keykey];
					}else{
						if(moveCount[keykey]<minCount){
							minCount=moveCount[keykey];
						}
					}
				}
				var minClassNum=0;
				if(minCount>-1){
					for(var keykey in moveCount){
						if(moveCount[keykey]==minCount){
							if($return==null){
								minClassNum=moveClassCount[keykey];
								$return=moveMap[keykey];
							}else{
								if(moveClassCount[keykey]<minClassNum){
									minClassNum=moveClassCount[keykey];
									$return=moveMap[keykey];
								}
							}
						}
					}
				}
			}
			return $return;
		}

		function toMakeWeeks(chooseTd){
			if(chooseTd==null){
				return;
			}
			var countPeriod=0;
			if($(chooseTd).find(".item").length){
				$(chooseTd).find(".item").each(function(){
					countPeriod=countPeriod+parseInt($(this).attr("clatime"));
				})
				
			}
			var keyId=$(chooseTd).attr("id");
			$('.'+keyId+'weekTimes').text(countPeriod)
		}

		// 修改时间
		$('.js-changeTime').on('click', function(e){
			var teaRowIndex =1; 
			<#--$(this).parentNode.parentNode.rowIndex;
			if(!teaRowIndex){
				teaRowIndex=1;
			}-->
			var tid = $(this).attr('teacherId');
			var url = '${request.contextPath}/newgkelective/${divideId!}/teacherClass/teacherTime/index/page?arrayItemId=${arrayItemId!}&teacherId='
									+tid+'&rowIndex='+teaRowIndex;
			var indexDiv = layerDivUrl(url,{title:"时间设置",width:720,height:600});
		});
		
		
		// 复制到
		$('.js-copy').on('click', function(e){
		
		
			//数字清空
			$(".gk-copy-nav").find("span").each(function(){
				$(this).text("");
			});
			//去除之前的查询结果
			$('#findTeacher').val('');
			findTeacher();
			
			//取消全选
			$('input:checkbox[name=copyTeacherAll]').prop("checked",false);
			
			
			//tab默认选中本科目
			$(".courseLi").removeClass("active");
			var course_id=$(this).attr("data-course");
			$("#course_"+course_id).addClass("active");
			
			var planExId=$(this).data("exid");
			var teacher_id=$(this).attr("data-value");
			$("#curTeacher").text($(this).attr("data-tname"));
			//取消所有选中老师与复制的条件
			$('.layer-copy').find("input:checkbox[name=copyTeacher]").prop('checked',false);
			$('.layer-copy').find("input:checkbox[name=copyField]").prop('checked',false);
			
			//取消所有不可选
			$('.layer-copy').find("input:checkbox[name=copyTeacher]").prop('disabled',false);
			
			$(".layer-copy").find("input:checkbox[name=copyTeacher]").each(function(){
				//不能选择的同科目同个老师
				if($(this).val()==planExId){
					$(this).attr("disabled",true);
				}
			})
			//全选显示 取隐藏
			$(".js-allChoose").show();
			$(".js-clearChoose").hide();
		
			e.preventDefault();
			layer.open({
				type: 1,
				shadow: 0.5,
				title: '复制到',
				area: ['1000px','700px'],
				btn: ['确定', '取消'],
				content: $('.layer-copy'),
				yes:function(index,layero){
					layer.confirm('确定需要复制？如果复制，将会追加上复制的限制条件。', function(index2){
						var copyFields="";
						var le=$('input:checkbox[name=copyField]:checked').length;
						if(le>0){
							$('input:checkbox[name=copyField]:checked').each(function(){
								copyFields+=","+$(this).val();
							})
						}else{
							layer.msg('请先选择需要复制的内容！', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
							return;
						}
						var exIds="";
						var ll=$('input:checkbox[name=copyTeacher]:checked').length;
						if(ll>0){
							$('input:checkbox[name=copyTeacher]:checked').each(function(){
								var exId=$(this).val();
								exIds+=","+exId;
							});
						}else{
							layer.msg('请先选择需要复制的老师！', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
							return;
						}
						exIds=exIds.substring(1);
						copyFields=copyFields.substring(1);
						submitCopyTeacher(planExId,exIds,copyFields);
					})
				}
			})
		});
		
		function submitCopyTeacher(planExId,exIds,copyFields){
			var url = '${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherSet/copyArrange';
			$.ajax({
				url:url,
				data:{'planExId':planExId,'planExIds':exIds,'copyFields':copyFields},
				type:'post', 
				dataType:'json',
				success:function(data){
					layer.closeAll();
			    	if(data.success){
			    		// 显示成功信息
			 			layer.msg("复制成功", {
								offset: 't',
								time: 2000
							});
						//更新
						refList("1");
			 		}else{
		 			 	layer.msg(data.msg, {
							offset: 't',
							time: 2000
						});
			 		}	
				}
			});
		}
		
		
		
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});

    });
    
    function courseExportTeacher(){
    	var url = '${request.contextPath}/newgkelective/exportXzbIndex/main?arrayItemId=${arrayItemId!}&arrayId=${arrayId!}';
		$("#gradeTableList").load(url);
    }

</script>
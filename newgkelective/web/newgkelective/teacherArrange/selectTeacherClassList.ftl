<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<script src="${request.contextPath}/static/components/jquery-ui/jquery-ui.min.js"></script>
<script src="${request.contextPath}/static/components/dragsort/jquery-list-dragsort.js"></script>
<script src="${request.contextPath}/static/sortable/Sortable.min.js"></script>
<style>
.publish-course span{
	min-width:90px;
	width:auto;
}
</style>
<script type="text/javascript" >
	var isSaveTea=false;
    function saveTeaCla(){
    	if(isSaveTea){
    		return;
    	}
    	isSaveTea=true;
    	if(!checkValue('#myTeaForm')){
    		isSaveTea=false;
    		return;
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
    			return;
    		}
    	});
    	if(!isSaveTea){
    		return;
    	}
        $("td.publish-course-children").each(function(){
            var ids = new Array();
            $(this).find("span").each(function(){
               var id=$(this).attr("claId");
                ids.push(id);
            });
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
            data: {"itemId" : '${arrayItemId!}'},
            dataType : 'json',
            success : function(data){
                if(data.success){
                    layer.msg(data.msg, {offset: 't',time: 2000});
					$('.course-tabs li.active').click();
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
    }
</script>
<form id="myTeaForm">
<div class="tab-pane active" role="tabpanel">
	<div class="explain">
		<p>小提示：切换科目或者复制不排课节次数前请先保存该页面的其他信息，以免丢失数据造成不必要的重复操作。</p>
	</div>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th width="25%" rowspan="2">未安排的班级<span class="explain">（拖动至任课班级）</span></th>
            <th width="7%" rowspan="2">任课教师</th>
            <th rowspan="2">任课班级</th>
            <th width="7%" rowspan="2">周课时数</th>
            <th width="19%" class="text-center" colspan="2">互斥教师</th>
            <th width="7%" rowspan="2">不排课节次数</th>
            <th width="7%" rowspan="2">操作</th>
        </tr>
        <tr>
            <th width="12%">互斥教师</th>
            <th width="7%">互斥课时数</th>
        </tr>
        </thead>
        <tbody>
        <#assign ind =0 />
        <#if planExList?exists && planExList?size gt 0>
        <#list planExList as planEx >
        <tr>
            <#if planEx_index == 0>
            <td rowspan="${planExList?size}">
                <div class="filter">
                    <div class="filter-item">
                        <div class="filter-content">
                            <#if batchKeys?exists && batchKeys?size gt 0><a class="btn btn-sm btn-blue js-random">随机安排</a></#if>
                        </div>
                    </div>
                </div>
                <#if batchKeys?exists && batchKeys?size gt 0>
                <ul>
                    <#list batchKeys as batchKey>
					<li>
						<h4>${batchKey[1]!}</h4>
                        <#assign bathClass=bathClassMap[batchKey[0]]>
                        <div class="publish-course publish-course-parent ${batchKey[0]!}" id="${batchKey[0]!}" style="min-height: 10px;">
                            <#if bathClass?exists && bathClass?size gt 0>
                            <#list bathClass as cla>
                                <span claId="${cla.id}" id="${cla.id}-${cla.classNum?default(0)}" class="${cla.subjectType!}_${cla.batch!}" clatime="${cla.classNum?default(0)}" title='${cla.className!}'><#if cla.className?default('')?length gt 5>${cla.className?default('')?substring(0,5)}...<#else>${cla.className!}</#if>
                                </span>
                            </#list>
                            </#if>
                        </div>
                        
                        <div class="filter filter_${batchKey[0]!}" <#if bathClass?exists && bathClass?size gt 0 >style="display: none;"</#if>>
							<div class="filter-item">
								<div class="filter-name">
									暂无未安排的班级
								</div>
							</div>
						</div>
                    </li>
                    </#list>
                </ul>
                <#else>
					<div class="filter" style="display: none;">
						<div class="filter-item">
							<div class="filter-name">
								暂无未安排的班级
							</div>
						</div>
					</div>
				</#if>
            </td>
            </#if>
            <td>
            <input type="hidden" name="teacherPlanExList[${ind}].id" value="${planEx.id!}" >
            <input type="hidden" name="teacherPlanExList[${ind}].teacherPlanId" value="${planEx.teacherPlanId!}" >
            <input type="hidden" name="teacherPlanExList[${ind}].teacherId" value="${planEx.teacherId!}" >
            <input type="hidden" name="teacherPlanExList[${ind}].classNum" value="${planEx.classNum!}" >
            <input type="hidden" name="teacherPlanExList[${ind}].classIds" id="${courseId}${planEx.teacherId!}classIds" class="classIds"  value="" >
            <input type="hidden" name="teacherPlanExList[${ind}].creationTime" value="${(planEx.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >
            <input type="hidden" name="teacherPlanExList[${ind}].modifyTime" value="${(planEx.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >
            ${planEx.teacherName!}</td>
            <td class="publish-course publish-course-children td-children-${ind}" ex-index="${ind}" id="${courseId}${planEx.teacherId!}">
                    <#if planEx.classIdList?exists &&  planEx.classIdList?size gt 0>
                        <#assign classIds = planEx.classIdList >
                        <#list classIds as classId>
                            <#assign cla =divideClassIdMap[classId] >
                            <span claid="${classId!}" id="${cla.id}-${cla.classNum?default(0)}" class="${cla.subjectType!}_${cla.batch!}" clatime="${cla.classNum?default(0)}" class="ui-sortable-handle" style="position: relative; left: 0px; top: 0px;">${cla.className!}
                            </span>
                        </#list>
                    </#if>
            </td>
            <td id="weekTimes${ind}" class="${courseId}${planEx.teacherId!}weekTimes">${planEx.weekTime!'0'}</td>
            <td class="mutexTea-td" mutexTea-id="mutexTeaIds${ind}" mutexnum-id="mutexNum${ind}">
            <#--<@popupMacro.selectMoreTeacher clickId="mutexTeaNames"+ind id="mutexTeaIds"+ind name="mutexTeaNames"+ind resourceUrl="${request.contextPath}/static">
			</@popupMacro.selectMoreTeacher>-->
				<input type="hidden" id="mutexTeaIds${ind}" name="teacherPlanExList[${ind}].mutexTeaIds" value="${planEx.mutexTeaIds!}"/>
				<input type="text" name="mutexTeaNames${ind}" id="mutexTeaNames${ind}" class="form-control" value="${planEx.mutexTeaNames!}" onclick="doClickTea('mutexTeaIds${ind}','mutexTeaNames${ind}','${ind}');">
			
            </td>
            <td><input type="text" name="teacherPlanExList[${ind}].mutexNum" id="mutexNum${ind}" class="form-control" value="${planEx.mutexNum?default(0)}" vtype="int" min="0"></td>
            <td teacherId="${planEx.teacherId!}" class="js-changeTime notime_${planEx.teacherId!}">${planEx.noTimeStr!}</td>
            <td><a href="javascript:;" data-value="${planEx.teacherId!}" data-course="${courseId!}" class="table-btn js-copy">复制到</a></td>
        </tr>
        <#assign ind=ind+1 />
        </#list>
        </#if>
        </tbody>
    </table>
</div>
<@popupMacro.selectMoreTeacher clickId="teaNamesTemp" id="teaIdsTemp" name="teaNamesTemp" resourceUrl="${request.contextPath}/static" handler="dealTea()">
	<input type="hidden" id="teaIdsTemp" name="teaIdsTemp" value=""/>
	<input type="hidden" name="teaNamesTemp" id="teaNamesTemp" class="form-control" value="">
</@popupMacro.selectMoreTeacher>
<div class="navbar-fixed-bottom opt-bottom">
    <a class="btn btn-white" onclick="backPreStep();" href="javascript:;">上一步</a>
    <a  onclick="saveTeaCla();" class="btn btn-blue "> 保存</a>
</div>
</form>
<script>

	var tindex='';
	function doClickTea(tid,tnameId,ind){
		tindex = ind;
		$('#teaNamesTemp').val($('#'+tnameId).val());
		$('#teaIdsTemp').val($('#'+tid).val());
		$('#teaNamesTemp').click();
	}
	
	function dealTea(){
		if(tindex && tindex != ''){
			$('#mutexTeaIds'+tindex).val($('#teaIdsTemp').val());
			$('#mutexTeaNames'+tindex).val($('#teaNamesTemp').val());
		}
	}

    function backPreStep(){
    	teaTable();
    }
    
    $(function(){
    
    	$('.courseLi').on('click','a',function(e){
    		//取消全选
    		$('input:checkbox[name=copyTeacherAll]').prop("checked",false);
    	})
		
		var typeLen = $('.publish-course-parent').length;
		var courseLen = $('.publish-course-parent span').length;
		var teacherLen = $('.publish-course-children').length;

		$('.js-random').click(function(){
			for(var m=0; m<typeLen; m++){
				// m 左班级 i右老师班级
				moveClsItem(m);
			};
		});
		
		function moveClsItem(m){
			var paitem = $('.publish-course-parent:eq('+ m +')');
			if($(paitem).children('span').length==0){
				$('.publish-course-parent:eq('+ m +')').siblings('.filter').show();
			} else {
				moveItemFun(paitem);
				if($(paitem).children('span').length==0){
					$('.publish-course-parent:eq('+ m +')').siblings('.filter').show();
				}
			}
		}
		
		function getBestTd(paitem){
			var toTds = $('.publish-course-children');
			var spancls = $(paitem).attr('id');
			
			var bestTd=null;
			var min = 0;
			var clsNum =0;
			
			for(var k=0;k<toTds.length;k++){
			   //classNum
				var exSpn = $(toTds[k]).find('span');
				if(!exSpn || exSpn.length == 0){
					return toTds[k];
				}
				var flag=false;
				// 判断是否能放
				for(var j=0;j<exSpn.length;j++){
					var excls = $(exSpn[j]).attr('class')
					if(excls.indexOf('${xzCls!}') == -1 
						&& excls.charAt(excls.length-1) != '_'
						&& excls.indexOf(spancls) != -1){
						flag = true;
						break;
					}
				}
				if(flag){
					continue;
				}
				
				var ind = $(toTds[k]).attr('ex-index');
				var tm = parseInt($('#weekTimes'+ind).text());
				
				if(bestTd==null){
					bestTd=toTds[k];
					min=tm;
					clsNum=exSpn.length;
				}else{
					if(tm<min){
						bestTd=toTds[k];
						min=tm;
						clsNum=exSpn.length;
					}else if(tm==min && exSpn.length<clsNum){
						bestTd=toTds[k];
						min=tm;
						clsNum=exSpn.length;
					}
				}
			}
			return bestTd;
		}
		
		function moveItemFun(paitem){
			var paspans = $(paitem).children('span');
			if(!paspans || paspans.length==0){
				return;
			}
			for(var j=0;j<paspans.length;j++){
				var toItem=paspans[j];
				var toTd = getBestTd(paitem);
				if(toTd==null){// 已经都有了 不能安排了
					break;
				}
				var ind = $(toTd).attr('ex-index');
				var wk = parseInt($(toItem).attr('clatime')); 
				var twk = parseInt($('#weekTimes'+ind).text())+wk;
				$('#weekTimes'+ind).text(twk);
				$(toItem).appendTo('#'+$(toTd).attr('id'));
			}
		}
		
		<#if planExList?exists && planExList?size gt 0 && batchKeys?exists && batchKeys?size gt 0>
		<#list batchKeys as batchKey>
		var p1 = document.getElementById("${batchKey[0]!}");
		Sortable.create(p1, 
		{
		    group: {name: 'one-group', pull: true, put: true},
		    sort: false,
		    onAdd: function(evt){
				var from = evt.from;
				var to = evt.to;
				var item = evt.item;
				var hasReturn=false;
				var itemcls = $.trim(item.className);
				var toCls = to.className;
				for(var i = 0; i <= to.children.length-1; i++){
					if(to.children[i]!=item && $.trim(to.children[i].className) != itemcls){
						to.removeChild(item);
						from.appendChild(item);
						hasReturn=true;
						layer.msg("移动的班级不属于本区域", {offset: 't',time: 2000});
						break;
					}
				}
				if(!hasReturn){
					if(toCls.indexOf('publish-course-parent') != -1 && toCls.indexOf(itemcls) == -1){
						to.removeChild(item);
						from.appendChild(item);
						hasReturn=true;
						layer.msg("移动的班级不属于本区域", {offset: 't',time: 2000});
					}
				}
				changeTdCon(from, to, item, hasReturn);	
				return true;
			}
		});
		</#list>
		</#if>

		<#if planExList?exists && planExList?size gt 0>
            <#list planExList as planEx >
		var c1 = document.getElementById("${courseId}${planEx.teacherId!}");
		Sortable.create(c1, 
		{
			group: {name: 'one-group', pull: true, put:true},
			sort: false,
			onAdd: function(evt){
				var from = evt.from;
				var to = evt.to;
				var item = evt.item;
				var itemcls = $.trim(item.className);
				var toCls = to.className;
				var hasReturn=false;
				for(var i = 0; i <= to.children.length-1; i++){
					if(to.children[i]!=item && $.trim(to.children[i].className)==itemcls && itemcls != '${xzCls!}'){
						to.removeChild(item);
						from.appendChild(item);
						layer.msg("教师在同一个选考/学考区域只能安排一个班级", {offset: 't',time: 2000});
						hasReturn=true;
						break;
					}
				}
				if(!hasReturn){
					if(toCls.indexOf('publish-course-parent') != -1 && toCls.indexOf(itemCls) == -1){
						to.removeChild(item);
						from.appendChild(item);
						hasReturn=true;
						layer.msg("教师在同一个选考/学考区域只能安排一个班级", {offset: 't',time: 2000});
					}
				}
				
				changeTdCon(from, to, item, hasReturn);	
				return true;
			}
		});
		</#list>
		</#if>
		
		function changeTdCon(from, to, item, hasReturn){
			if(from.children.length==0 && from.className.indexOf('publish-course-parent') != -1){
				$('.filter_'+from.id).show();
			}
			if(to.children.length!=0 && to.className.indexOf('publish-course-parent') != -1){
				$('.filter_'+to.id).hide();
			}
			if(hasReturn){
				return;
			}
			var itemid = item.id;
			var chtime = 0;
			if(itemid){
				chtime = parseInt(itemid.split('-')[1]);
			}
			if(chtime && chtime != 0){
				if(from.className.indexOf('publish-course-children') != -1){
					var old = parseInt($('.'+from.id+'weekTimes').text());
					$('.'+from.id+'weekTimes').text(old-chtime)
				}
				if(to.className.indexOf('publish-course-children') != -1){
					var old = parseInt($('.'+to.id+'weekTimes').text());
					$('.'+to.id+'weekTimes').text(old+chtime)
				}
			}
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
		
		
		//复制
		$('.js-copy').on('click', function(e){
			//数字清空
			$(".gk-copy-nav").find("span").each(function(){
				$(this).text("");
			});
			//全选
			$('input:checkbox[name=copyTeacherAll]').prop("checked",false);
			//tab默认选中本科目
			$(".courseLi").removeClass("active");
			var course_id=$(this).attr("data-course");
			$("#course_"+course_id).find("a").click();
			var teacher_id=$(this).attr("data-value");
			//取消所有选中
			$('.layer-gk-copy').find("input:checkbox[name=copyTeacher]").prop('checked',false);
			//取消所有不可选
			$('.layer-gk-copy').find("input:checkbox[name=copyTeacher]").prop('disabled',false);
			$('.copyTeacher_'+teacher_id).each(function(){
				//不能选择同样的老师
				$(this).find("input:checkbox[name=copyTeacher]").attr("disabled",true);
			})
			e.preventDefault();
			layer.open({
				type: 1,
				shadow: 0.5,
				title: '复制到',
				area: ['620px', '460px'],
				btn: ['确定', '取消'],
				content: $('.layer-gk-copy'),
				yes:function(index,layero){
					layer.confirm('确定需要复制？如果复制，将会追加上复制的限制条件。', function(index2){
						var teacherIds="";
						var ll=$('input:checkbox[name=copyTeacher]:checked').length;
						if(ll>0){
							$('input:checkbox[name=copyTeacher]:checked').each(function(){
								var ttId=$(this).val();
								if(teacherIds.indexOf(ttId) >-1){
									//重复老师
								}else{
									teacherIds=teacherIds+","+ttId;
								}
							});
						}else{
							layer.closeAll();
							layerTipMsg(false,"失败","请选择需要复制的老师");
							return;
						}
						if(teacherIds==""){
							layer.closeAll();
							layerTipMsg(false,"失败","请选择需要复制的老师");
							return;
						}
						teacherIds=teacherIds.substring(1);
						submitCopyTeacher(course_id,teacher_id,teacherIds);
					})
				}
			})
		});
		
		function submitCopyTeacher(subjectId,teacherId,teacherIds){
			var url = '${request.contextPath}/newgkelective/teacherClass/teacherTime/copyTeacherTime?arrayItemId=${arrayItemId!}';
			$.ajax({
				url:url,
				data:{'teacherId':teacherId,'teacherIds':teacherIds},
				type:'post', 
				dataType:'json',
				success:function(data){
					layer.closeAll();
			    	if(data.success){
			    		// 显示成功信息
			 			layer.msg("保存成功", {
								offset: 't',
								time: 2000
							});
						//更新
						//var courseId = $("#myForm ul.nav-tabs li.active").attr("data-course-id");
						$('.course-tabs li.active').click();
						//changeFirstTab('${type!}',courseId);
			 		}else{
			 			if("没有需要复制的时间限制"==data.msg){
			 			 	layer.msg("没有限制可以复制", {
								offset: 't',
								time: 2000
							});
			 			}else{
			 				layerTipMsg(data.success,"失败",data.msg);
			 			}

			 		}	
				}
			});

		}
		
		//点中数量
		$(".copyteacherTab").off('click').on('click','input:checkbox[name=copyTeacher]',function(){
			var closeDiv=$(this).closest("div");
			var course_id=$(closeDiv).attr("data-value");
			var num=$("#course_"+course_id).find("span").text();
			if(num.trim()==""){
				num=parseInt(0);
			}else{
				num=parseInt(num);
			}
			if($(this).is(":checked")){
				//+1
				num=num+1;
			}else{
				//-1
				num=num-1;
			}
			if(num>0){
				$("#course_"+course_id).find("span").text(""+num);
			}else{
				$("#course_"+course_id).find("span").text("");
			}
		})
		
		$('input:checkbox[name=copyTeacherAll]').on('change',function(){
			var actioveDiv=$(".copyteacherTab").find("div.active");
			var course_id=$(actioveDiv).attr("data-value")
			if($(this).is(':checked')){
				$(actioveDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
					if(!$(this).is(':disabled')){
						$(this).prop('checked',true);
					}
					
				})
			}else{
				$(actioveDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
					$(this).prop('checked',false);
				})
			}
			//计算数量
			var length=$(actioveDiv).find('input:checkbox[name=copyTeacher]:checked').length;
			if(length>0){
				$("#course_"+course_id).find("span").text(""+length);
			}else{
				$("#course_"+course_id).find("span").text("");
			}
		});

    });

</script>
<#import "/fw/macro/popupMacro.ftl" as popupMacro>
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
    		var ctid=$('#'+tid).data('tid');
    		if(tidval.indexOf(ctid)>=0){
    			layer.tips('教师不能选择自己为互斥教师',$('#'+$(this).attr('mutexTea-name')), {
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
        var options = {
            url : '${request.contextPath}/newgkelective/${arrayItemId!}/subjectTeacherArrange/saveClass',
            data: {"itemId" : '${arrayItemId!}'},
            dataType : 'json',
            success : function(data){
                if(data.success){
                    layer.msg(data.msg, {offset: 't',time: 2000});
                    doSearchTeacher('${courseId!}', '1');
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
	<div class="explain">
		<p>小提示：选择科目或者复制内容到其他老师前请先保存该页面的其他信息，以免丢失数据造成不必要的重复操作。</p>
	</div>
<div class="filter">
	<div class="filter-item">
		<button class="btn btn-blue js-teacherSelect">教研组</button>
		<button class="btn btn-blue js-teacherGroupSet">教师组设置</button>
	</div>
	<div class="filter-item filter-item-right">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
		        	<input type="text" class="typeahead scrollable form-control" id="searchTeaNameId" autocomplete="off" data-provide="typeahead" placeholder="输入教师姓名查询" value="${teacherName!}">
		        </div>
			    <div class="input-group-btn">
			    	<button type="button" class="btn btn-default" onClick="doSearchTeacher('', '0');">
				    	<i class="fa fa-search"></i>
				    </button>
			    </div>
		    </div>
		</div>
	</div>
</div>
<div class="picker-table">
	<table class="table">
		<tbody>			
			<tr>
				<th width="150">科目：</th>
				<td>
					<div class="outter">
						<a <#if ''==courseId?default('')>class="selected"</#if> href="javascript:doSearchTeacher('', '0');">全部</a>
						<#if courseNameMap?exists>
	                    <#list courseNameMap?keys as mkey >
	                    <#assign count=0>
	                    <#if courseCountMap[mkey]?exists>
	                    <#assign count=courseCountMap[mkey]>
	                    </#if>
						<a href="javascript:doSearchTeacher('${mkey!}', '0');" <#if mkey?default('') ==courseId?default('')>class="selected"</#if>>${courseNameMap[mkey]!}(<#if count?number==0><font color="red">0</font><#else>${count!}</#if>)</a>
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
<#if subjectExMap?exists && subjectExMap?size gt 0>
<form id="myTeaForm">
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th rowspan="2" width="4%">序号</th>
					<th rowspan="2" width="8%">科目</th>
					<th rowspan="2" width="8%">教师</th>
					<th rowspan="2" width="14%">每天课时分配</th>
					<th rowspan="2" width="10%">周课时分布</th>
					<th colspan="2" class="text-center">互斥设置</th>
					<th rowspan="2" width="6%">禁排时间</th>
					<th rowspan="2" width="6%">操作</th>
				</tr>
				<tr>
					<th>互斥教师</th>
					<th width="93">互斥课时数</th>
				</tr>
			</thead>
			<tbody>
				<#assign index=-1>
   				<#list subjectExMap? keys as key>
   				<#list subjectExMap[key] as planEx>
   				<#assign index=index+1>
				<tr>
					<#if planEx_index==0>
				    <td rowspan='${subjectExMap[key]?size!}'>${key_index+1!}</td>
				    <td rowspan='${subjectExMap[key]?size!}'>${courseNameMap[key]!}</td>
				    </#if>
				    <td>
				    <input type="hidden" name="teacherPlanExList[${index!}].id" value="${planEx.id!}" >
		            <input type="hidden" name="teacherPlanExList[${index!}].teacherPlanId" value="${planEx.teacherPlanId!}" >
		            <input type="hidden" name="teacherPlanExList[${index!}].teacherId" value="${planEx.teacherId!}" >
		            <input type="hidden" name="teacherPlanExList[${index!}].creationTime" value="${(planEx.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >
		            <input type="hidden" name="teacherPlanExList[${index!}].modifyTime" value="${(planEx.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}" >
				    ${planEx.teacherName!}
				    </td>
				    <td>
				    	<select name="teacherPlanExList[${index!}].dayPeriodType" class="form-control">
				    		${mcodeSetting.getMcodeSelect('DM-KSFP',(planEx.dayPeriodType?default(1))?string,'0')}
						</select>
				    </td>
				    <td>
				    	<select name="teacherPlanExList[${index!}].weekPeriodType" class="form-control">
				    		${mcodeSetting.getMcodeSelect('DM-KSFB',(planEx.weekPeriodType?default(1))?string,'0')}
						</select>
		   			</td>
				    <td class="mutexTea-td" mutexTea-id="mutexTeaIds${index!}" mutexnum-id="mutexNum${index!}" mutexTea-name="mutexTeaNameList${index!}">
				    	<input type="hidden" id="mutexTeaIds${index}" name="teacherPlanExList[${index}].mutexTeaIds" value="${planEx.mutexTeaIds!}" data-tid="${planEx.teacherId!}"/>
						<input type="hidden" name="mutexTeaNames${index}" id="mutexTeaNames${index}" value="${planEx.mutexTeaNames!}">
				    	<div class="clearfix js-hover">
				    		<span class="pull-left classroomArrangement classroomArrangementSimple classList clearfix no-padding mb-8" id="mutexTeaNameList${index!}" style="width:95%">
								<#if planEx.mutexTeaIdList??&&planEx.mutexTeaIdList?size gt 0>
								<#list planEx.mutexTeaIdList as tid>
								<a class="item" href="javascript:void(0);" data-tid="${tid!}" data-ind="${tid_index!}" onClick="doRemoveThis(this,'${index!}')">
									<span class="del">&times;</span>
									<span class="bj">${teacherNameMap[tid]!}</span>
								</a>
				    			</#list>
		    					</#if>
				    		</span>
					    	<span class="pull-right js-hover-opt" style="width:5%">
					    		<a href="javascript:doClickTea('${index}');" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑"><i class="fa fa-edit color-blue"></i></a>
					    	</span>
				    	</div>
				    </td>
				    <td>
				    	<div class="form-number form-number-sm" data-step="1">
				    		<input type="text" name="teacherPlanExList[${index!}].mutexNum" id="mutexNum${index!}" class="form-control" value="${planEx.mutexNum?default(0)}" vtype="int" min="0">
				    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
					    	<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
				    	</div>
				    </td>
				    <td>
				    	<div class="clearfix js-hover">
				    		<span class="pull-left js-hover-num color-blue notime_${planEx.teacherId!}">${planEx.noTimeStr!}</span>
					    	<span class="pull-right js-hover-opt" style="display: block;">
					    		<a href="javascript:void(0)" data-toggle="tooltip" data-placement="top" title="编辑" teacherId="${planEx.teacherId!}" class="js-changeTime" data-original-title="编辑"><i class="fa fa-edit color-blue"></i></a>
					    	</span>
				    	</div>
				    </td>
				    <td>
				    	<a href="javascript:void(0)" class="color-blue js-copy" data-toggle="tooltip" data-placement="top" title="复制到" data-original-title="复制到" data-value="${planEx.teacherId!}" data-tname="${planEx.teacherName!}" data-exid="${planEx.id!}" data-course="${key!}" >复制到</a>
				    </td>
				</tr>
				</#list>
				</#list>
			</tbody>
		</table>
	</div>
	<div class="navbar-fixed-bottom opt-bottom">
		<a src="javascript:void(0);" onClick="saveTeaCla();" class="btn btn-blue" id="confirm-submit">保存</a>
	</div>
</div>
</form>
<@popupMacro.selectMoreTeacher clickId="teaNamesTemp" id="teaIdsTemp" name="teaNamesTemp" resourceUrl="${request.contextPath}/static" handler="dealTea()">
	<input type="hidden" id="teaIdsTemp" name="teaIdsTemp" value=""/>
	<input type="hidden" name="teaNamesTemp" id="teaNamesTemp" class="form-control" value="">
</@popupMacro.selectMoreTeacher>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">请去教研组添加教师</p>
		</div>
	</div>
</div>
 </#if>
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
	<#--$(obj).parent('span').siblings('.js-hover-opt').hide();-->
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

function doSearchTeacher(courseId, useMaster){
	var teacherName = $("#searchTeaNameId").val();
	var url = '${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherSet/index/list?teacherName='+teacherName+'&courseId='+courseId+'&useMaster='+useMaster;
    $("#showTeacherArrange").load(encodeURI(url));
}


function findTeacher(){
	var teacherName=$('#findTeacher').val().trim();
	if(teacherName!=""){
		$(".gk-copy-main .lbl").removeClass("color-blue");
		var first;
		$(".gk-copy-main input").each(function(){
			if ($(this).attr("data-value").includes(teacherName)) {
				if(!first){
					first=$(this);
				}
				$(this).siblings().addClass("color-blue");
			}
		});
		if(first){
			//模仿锚点定位
			var divId=$(first).parents("div").attr("id");
			document.getElementById(divId).scrollIntoView();
			
			var buid = $(first).parents("div").attr("data-value");
			document.getElementById("course_"+buid).scrollIntoView();
			setTimeout(function(){
				$("#course_"+buid).siblings().removeClass("active");
				$("#course_"+buid).addClass("active");
			},50);
		}
	}else{
		$(".gk-copy-main .lbl").removeClass("color-blue");
	}
}
$(function(){
	//编辑
	<#--
	$('.js-hover').hover(function(){
		$(this).children('.js-hover-opt').show();
	}, function(){
		$(this).children('.js-hover-opt').hide();
	});
	-->
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
    
    $('.picker-more').click();//默认展开
	
	// 教研组选老师
	$('.js-teacherSelect').on('click', function(){
		var url ='${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherSet/selectTeacher?courseId=${courseId!}';
		indexDiv = layerDivUrl(url,{title: "选择教师",width:1000,height:660});
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
			area: ['1000px','660px'],
			scrollbar:false,
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
	
	//回车
	$('#findTeacher').bind('keypress',function(event){//监听回车事件
        if(event.keyCode == "13" || event.which == "13")    
        {  
            findTeacher();
        }
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
					doSearchTeacher('${courseId!}', '1');
		 		}else{
	 			 	layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
		 		}	
			}
		});
	}
	
	//点中数量
	$(".copyteacherTab").off('click').on('click','input:checkbox[name=copyTeacher]',function(){
		var closeDiv=$(this).closest("div");
		var course_id=$(closeDiv).attr("data-value");
		var num=$("#course_"+course_id).find("span.badge").text();
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
			$("#course_"+course_id).find("span.badge").text(""+num);
			//用取消
			$(closeDiv).find(".js-allChoose").hide();
			$(closeDiv).find(".js-clearChoose").show();
		}else{
			$("#course_"+course_id).find("span.badge").text("");
			//用全选
			$(closeDiv).find(".js-allChoose").show();
			$(closeDiv).find(".js-clearChoose").hide();
		}
	})
	
	$('input:checkbox[name=copyTeacherAll]').on('change',function(){
		var actioveDiv=$(".copyteacherTab").find("div.active");
		if($(this).is(':checked')){
			$('input:checkbox[name=copyTeacher]').each(function(i){
				if(!$(this).is(':disabled')){
					$(this).prop('checked',true);
				}
			})
		}else{
			$('input:checkbox[name=copyTeacher]').each(function(i){
				$(this).prop('checked',false);
			})
		}
		//整体数量操作
		$(".courseLi").each(function(){
			var cid=$(this).find("a").attr("data-value");
			//计算数量
			var length=$("#aaa_"+cid).find('input:checkbox[name=copyTeacher]:checked').length;
			if(length>0){
				$(this).find("span").text(""+length);
				$("#aaa_"+cid).find(".js-allChoose").hide();
				$("#aaa_"+cid).find(".js-clearChoose").show();
			}else{
				$(this).find("span").text("");
				$("#aaa_"+cid).find(".js-allChoose").show();
				$("#aaa_"+cid).find(".js-clearChoose").hide();
			}
		})
	});
	
	$(".js-allChoose").on('click',function(){
		var closeDiv=$(this).parent().parent();
		var cId=$(closeDiv).attr("data-value");
		var num=0;
		$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
			if(!$(this).is(':disabled')){
				$(this).prop('checked',true);
				num++;
			}
		})
		$("#course_"+cId).find("span").text(""+num);
		$(closeDiv).find(".js-allChoose").hide();
		$(closeDiv).find(".js-clearChoose").show();
	})
	
	$(".js-clearChoose").on('click',function(){
		var closeDiv=$(this).parent().parent();
		var cId=$(closeDiv).attr("data-value");
		$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
			$(this).prop('checked',false);
		})
		$("#course_"+cId).find("span").text("");
		$(closeDiv).find(".js-allChoose").show();
		$(closeDiv).find(".js-clearChoose").hide();
	})
	
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
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
	
	// 修改时间
	$('.js-changeTime').on('click', function(e){
		var teaRowIndex =1; 
		<#--$(this).parentNode.parentNode.rowIndex;
		if(!teaRowIndex){
			teaRowIndex=1;
		}-->
		var tid = $(this).attr('teacherId');
		var url = '${request.contextPath}/newgkelective/${arrayItemId!}/teacherClass/teacherTime/index/page?arrayItemId=${arrayItemId!}&teacherId='
								+tid+'&rowIndex='+teaRowIndex+'&basicSave=true';
		var indexDiv = layerDivUrl(url,{title:"时间设置",width:720,height:600});
	});
	// 教师组设置
	$(".js-teacherGroupSet").on('click',function(){
		var url = '${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherGroup/index';
		$("#gradeTableList").load(url);
	});
})
</script>


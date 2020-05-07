<#if gkdto?exists>
	<div class="box box-default">
		<div class="box-header">
			<h4 class="box-title">${gkdto.gsaEnt.arrangeName!}（任选${gkdto.gsaEnt.subjectNum!}门）</h4>
		</div>
		<div class="box-body">
			<div class="filter filter-f16">
				<div class="filter-item">
					<div class="filter-name">身份证号：</div>
					<div class="filter-content">
						<p>${gkdto.stu.identityCard!}</p>
					</div>
				</div>
				<div class="filter-item">
					<div class="filter-name">姓名：</div>
					<div class="filter-content">
						<p>${gkdto.stu.studentName!}</p>
					</div>
				</div>
				<div class="filter-item">
					<div class="filter-name">行政班：</div>
					<div class="filter-content">
						<p>${gkdto.gradeName!}${gkdto.stu.className!}</p>
					</div>
				</div>
				<div class="filter-item">
					<div class="filter-name">选课时间范围：</div>
					<div class="filter-content">
						<p>${gkdto.gsaEnt.startTime?string("yyyy-MM-dd HH:mm")}&nbsp;&nbsp;到&nbsp;&nbsp;${gkdto.gsaEnt.limitedTime?string("yyyy-MM-dd HH:mm")}</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-7">
					<table class="table table-striped table-hover table-selectCourse">
						<thead>
							<tr>
								<th width="60">操作</th>
								<th>科目<em>（选满3门）</em></th>
							</tr>
						</thead>
						<tbody>
						<#assign fag = '0'>
						<#list subList as gksub>
							<tr class="parentTrClass">
								<td>
								<#if canEdit?exists && canEdit=='1'>
									<label class="pos-rel js-select">
										<input name="course-checkbox" type="checkbox" 
										 class="wp" value="${gksub.id}"
							            <#list gkResult as item>
								            <#if item.subjectId == gksub.id>
								            	<#if item.status == 1>
								            		disabled="disabled"
								            		<#assign fag = '1'>
								            	</#if> checked
								            </#if>
							            </#list>>
										<span class="lbl"></span>
									</label>
								<#else>
									<label class="pos-rel js-select">
										<input <#if (gkResult?size>0) || !isShowCommitButton>disabled</#if> name="course-checkbox" type="checkbox" 
										 class="wp" value="${gksub.id}"
							            <#list gkResult as item>
								            <#if item.subjectId == gksub.id>
 								            	checked
								            </#if>
							            </#list>>
										<span class="lbl"></span>
									</label>
									</#if>
								</td>
								<td class="subjectName">
									<span class="name">${gksub.subjectName}</span>
									<span class="fa fa-question-circle color-grey" <#if fag=='0'>style="display: none;"</#if>  data-toggle="tooltip" data-placement="top" title="" data-original-title="该科目已经被教师审核通过，无法修改！"></span>
									<#assign fag = '0'>
								</td>
							</tr>
						</#list>
						</tbody>
					</table>
					<div class="">
						<#if isShowCommitButton>
							<div>
							<#if canEdit?exists && canEdit == '1'>
								<button class="btn btn-blue " id="btn_submit">提交</button>
								<button class="btn btn-blue " id="btn_update" style="display:none">修改</button>
							<#else>
								<button class="btn btn-blue " id="btn_submit" <#if (gkResult?size>0)>style="display:none"</#if>>提交</button>
								<button class="btn btn-blue " id="btn_update" <#if !(gkResult?size>0)>style="display:none"</#if>>修改</button>
							</#if>
							</div>
							<#if (limitSubjectName!) != "">
							<div>
								<em>限制不能选：${limitSubjectName!}</em>
							</div>
							</#if>
						<#else>
							<em>不在选课时间范围</em>
						</#if>
					</div>
				</div>
				<div class="col-sm-5">
					<div class="selectResult-box">
						<div class="selectResult-header">
							<h5 class="selectResult-title">已选科目</h5>
						</div>
						<div class="selectResult-body " id="selectResult_body">
							<div class="selectResult-content" style="display:none;" id="selectResult_content">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<script>
$(function(){
	var spanHtml = "";
	$("input[name='course-checkbox']").each(function(){
		if($(this).is(':checked')){
			spanHtml+='<span>'+$(this).parents(".parentTrClass").find(".subjectName .name").html()+'</span>';
		}
	});
	if(spanHtml == ""){
		$("#selectResult_body").addClass("unselect");
	}else{
		$("#selectResult_content").append(spanHtml);
		$("#selectResult_content").show();
	}
    // 提交选课结果
    $('#btn_submit').click(function(){
        var id_array=[];
        var ind=0;
    	var subjectIds = "";
    	var subjectNames = "";
    	var spanHtml = "";
        $("input[name='course-checkbox']:checked").each(function(){
        	id_array[ind++]=$(this).val();
        	subjectIds+=','+$(this).val();
        	subjectNames+=','+$(this).parents(".parentTrClass").find(".subjectName .name").html();
        	spanHtml+='<span>'+$(this).parents(".parentTrClass").find(".subjectName .name").html()+'</span>';
        })
        if(id_array.length != 3){
            layer.alert('必须且只能选择3门课程，请修改！',{icon:7});
            return;
        }
        subjectNames=subjectNames.substring(1,subjectNames.length);
        subjectIds=subjectIds.substring(1,subjectIds.length);
    	showConfirmMsg('您选的科目为：'+subjectNames+'（确认无误后，请点击确定按钮。）','提示',function(){
		    var ii = layer.load();
		    $.post("${request.contextPath}/gkelective/studentChooseSubject/save?subjectId="+subjectIds+"&arrangeId=${arrangeId}",function(data){
		        var jsonO = JSON.parse(data);
		        if(jsonO.success){
		            layer.closeAll();
		            layerTipMsg(jsonO.success,"成功",jsonO.msg);
		            $('.table-selectCourse input[type=checkbox]').attr('disabled','disabled');
		            $("#btn_submit").hide();
		            $("#btn_update").show();
		            $("#selectResult_content").html('');
		            $("#selectResult_content").append(spanHtml);
		            $("#selectResult_content").show();
		            if($("#selectResult_body").hasClass("unselect"))
		          		$("#selectResult_body").removeClass("unselect");
		        }
		        else{
		            layer.closeAll();
		            layerTipMsg(jsonO.success,"失败",jsonO.msg);
		        }
		        layer.close(ii);
		    })
    	});
    });
    $('#btn_update').click(function(){
    	var acadyearSearch = $("#acadyearSearch").val();
		var semesterSearch = $("#semesterSearch").val();
		var url =  '${request.contextPath}/gkelective/studentChooseSubject/list/page?acadyearSearch='+acadyearSearch+'&semesterSearch='+semesterSearch+'&canEdit=1';
		$("#itemShowDivId").load(url);
    });
    $('.table-selectCourse > tbody > tr').on('click',function(){
		var $checkbox=$(this).find('input[type=checkbox]')
		if($checkbox.attr('disabled')){
			return;
		}
		if($checkbox.is(':checked')){
			$checkbox.prop('checked',false)
		}else{						
			$checkbox.prop('checked',true);
		}
	});
	// #############提示工具#############
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});
})
</script>
<div class="row">
	<div class="col-sm-12">
		<div class="choose-explain">
			<h5>选课说明</h5>
			<p>${notice!}</p>
			<#-- <p>1.每位学生必须选择三门课程，且只能选择三门课程，否则无法提交。</p>
			<p>2.本次选择不作为学生高考选考课程的最终依据，仅为意向调查。</p>
			<p>3.使用中如遇浏览器兼容性原因，建议使用谷歌浏览器，或360极速模式。</p>
			<p>4.非选课时间学生无法查看相应信息，管理员发布选课项目后，学生才能看到选课信息。</p>
			<p>5.学生在选课截止时间之前，可以操作选课，并且可以修改再提交，一旦超过选课截止时间，学生无法继续操作。</p>
			<p>6.学生个人信息和密码暂不支持修改。</p> -->
		</div>
	</div>
</div>
<#else>
	<div class="no-data-container">
		<div class="no-data no-data-hor">
			<span class="no-data-img">
				<img src="${request.contextPath}/gkelective/images/noSelectSystem.png" alt="">
			</span>
			<div class="no-data-body">
				<h3>暂无选课项目</h3>
				<p class="no-data-txt">如需进行选课，请联系教务管理员新建选课项目</p>
			</div>
		</div>
	</div>
</#if>
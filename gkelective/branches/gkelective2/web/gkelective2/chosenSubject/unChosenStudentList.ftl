<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="bb" class="tab-pane active" role="tabpanel">
	<em>共${unChosenCount!}个学生未选择</em>
	<p class="tip tip-grey pull-right noprint">
	<#if gkResultList?? && (gkResultList?size>0) && gkArrange.isLock=0>
		<a href="javascript:" class="btn btn-blue js-saveSelectResult" onclick="saveAll()">保存</a>
	</#if>
	</p>
<form id="myform" class="print">
<table class="table table-striped table-hover table-layout-fixed no-margin">
	<thead>
	<tr>
		<th class="noprint" style="width:5%"><input type="checkbox" id="resultCheckboxAll" value="" onchange="resultCheckboxAllSelect()"><span class="fa fa-question-circle color-grey" data-toggle="tooltip" data-placement="top" title="" data-original-title="勾选多个学生，同时操作"/></th>
		<th style="width:15%">学号</th>
		<th style="width:7%">姓名</th>
		<th style="width:5%">性别</th>
		<th style="width:10%">行政班</th>
		<#if coursesList?? && (coursesList?size>0)>
		<#list coursesList as course>
			<th class="noprint">${course.subjectName!}</th>
		</#list>
		</#if> 
		<th  width="10%" class="noprint">备注</th>
	</tr>
</thead>
<tbody>
<#if gkResultList?? && (gkResultList?size>0)>
<#list gkResultList as gkResult>
<tr class="resultInfoClass resultInfo${gkResult_index}">  
	<td class="noprint"><input type="checkbox" name="resultCheckboxName" class="resultCheckboxOneClass" value="${gkResult.studentId!}" ></td>
	<td>${gkResult.stucode!}</td>
	<td>${gkResult.stuName!}</td>
	<td>${mcodeSetting.getMcode("DM-XB","${gkResult.stuSex!}")}</td>
	<td>${gkResult.className!}
	<input type="hidden"  id="gkResults[${gkResult_index}].studentId" name="gkResults[${gkResult_index}].studentId" value="${gkResult.studentId!}">
	<input type="hidden"  id="gkResults[${gkResult_index}].classId" name="gkResults[${gkResult_index}].classId" value="${gkResult.classId!}">
	<input type="hidden"  id="${gkResult.studentId!}" name="gkResults[${gkResult_index}].courseIds" value="">
	</td>
	<#if coursesList?? && (coursesList?size>0)>
	<#list coursesList as course>
	<td class="noprint"><span id="${course.id!}" class="course-choose ${gkResult.studentId!}" value="${gkResult.studentId!}">选择</span></td>
	</#list>
	</#if> 
	<td class="noprint"><input class="form-control" name="gkResults[${gkResult_index}].remark" id="remark${gkResult_index}" value="${gkResult.remark!}" type="text" maxLength="100"></td>
</tr>
</#list>
<#else>
<tr >
	<td  colspan="88" align="center">
	暂无数据
	</td>
<tr>
</#if> 
</tbody>
</table>
</form>
	<div class="page-footer-btns text-left">
	<#if gkResultList?? && (gkResultList?size>15) && gkArrange.isLock=0>
	<p class="tip tip-grey noprint">
		<a href="javascript:" class="btn btn-blue js-saveSelectResult" onclick="saveAll()">保存</a>
	</p>
	</#if>
</div>
</div>

<script type="text/javascript">
	$(function(){
		// #############提示工具#############
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});
	});
	// 选课
	function changeChosenSubject(obj,isSelected){
		if(obj.hasClass('disabled')) return;
		if(isSelected){
			obj.addClass('selected');
		}else{
			obj.removeClass('selected');
		}
		var studentId = obj.attr('value');
		var subs = $('.'+studentId+'.selected');
		var allSubs = $('.'+studentId);
		var courseIds = '';
		if(subs.length<3){
			for(var i=0;i<allSubs.length;i++){
				if($('.'+studentId+':eq('+i+')').hasClass('disabled')){
					$('.'+studentId+':eq('+i+')').removeClass('disabled');
				}
				if($('.'+studentId+':eq('+i+')').hasClass('selected')){
					if(courseIds && courseIds.length > 0){
						courseIds = courseIds + ',' + $('.'+studentId+':eq('+i+')').attr('id'); 
					}else{
						courseIds = $('.'+studentId+':eq('+i+')').attr('id');
					}
				}
			}	
		}else{
			for(var i=0;i<allSubs.length;i++){
				if(!$('.'+studentId+':eq('+i+')').hasClass('disabled')){
					if(!$('.'+studentId+':eq('+i+')').hasClass('selected')){
						$('.'+studentId+':eq('+i+')').addClass('disabled');
					}
				}
				if($('.'+studentId+':eq('+i+')').hasClass('selected')){
					if(courseIds && courseIds.length > 0){
						courseIds = courseIds + ',' + $('.'+studentId+':eq('+i+')').attr('id'); 
					}else{
						courseIds = $('.'+studentId+':eq('+i+')').attr('id');
					}
				}
			}
		}
		$('#'+studentId).val(courseIds);
	}
	$('.course-choose').on('click',function(){
		var isSelected = true;
		if($(this).hasClass('selected')){
			isSelected = false;
		}
		if($('input:checkbox[name=resultCheckboxName]:checked').length>0){
			var studentId = $(this).attr('value');
			var boo = true;
			$('input:checkbox[name=resultCheckboxName]:checked').each(function(i){
				if(studentId == $(this).val()){
					boo = false;
				}
			});
			if(boo){
				changeChosenSubject($(this),isSelected);
				return;
			}
			var subjectId = $(this).attr('id');
			$('input:checkbox[name=resultCheckboxName]:checked').each(function(i){
				var obj = $(this).parents('.resultInfoClass').find('#'+subjectId);
				changeChosenSubject(obj,isSelected);
			});
		}else{
			changeChosenSubject($(this),isSelected);
		}
	});
	function resultCheckboxAllSelect(){
		if($("#resultCheckboxAll").is(':checked')){
			$('input:checkbox[name=resultCheckboxName]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$('input:checkbox[name=resultCheckboxName]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	}
	$('.resultCheckboxOneClass').on('change',function(){
		if($('input:checkbox[name=resultCheckboxName]:checked').length>0){
			$("#resultCheckboxAll").prop('checked',true);
		}else{
			$("#resultCheckboxAll").prop('checked',false);
		}
	});

var isSubmit=false;
function saveAll(){
  if(isSubmit){
    return;
    }
  isSubmit=true;
  $("#save").addClass("disabled");

  var check = checkValue('#myform');
  if(!check){
    $(this).removeClass("disabled");
    isSubmit=false;
    return;
  }
  var obj = new Object();
  // 获取此控件下所有的可输入内容，并组织成json格式
  // obj，是因为url所对应的接收对象是个dto，数据是存在dto
  obj= JSON.parse(dealValue('#myform'));
  var options = {
      url : '${request.contextPath}/gkelective/${arrangeId}/chosenSubject/saveAll',
      dataType : 'json',
      success : function(data){
          if(data.success){
              layer.closeAll();
              layerTipMsg(data.success,"成功",data.msg);
              findByCondition();
          }
          else{
              layerTipMsg(data.success,"失败",data.msg);
              $("#result-commit").removeClass("disabled");
              isSubmit=false;
          }
      },
      clearForm : false,
      resetForm : false,
      type : 'post',
      error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
  };
  $("#myform").ajaxSubmit(options);
}

function saveResult(index){
       if(isSubmit){
          return;
      }
      isSubmit=true;
      $("save").addClass("disabled");
      
      var check = checkValue('.resultInfo'+index+'');
      if(!check){
          $(this).removeClass("disabled");
          isSubmit=false;
          return;
      }
      var obj = new Object();
      // 获取此控件下所有的可输入内容，并组织成json格式
      // obj，是因为url所对应的接收对象是个dto，数据是存在dto
      obj= JSON.parse(dealValue('.resultInfo'+index+''));
      var studentId = obj["studentId"];
      var courseIds = obj["courseIds"];
      // 提交数据
      $.ajax({
          url:'${request.contextPath}/gkelective/${arrangeId}/chosenSubject/save?studentId='+studentId+"&courseIds="+courseIds,
          type:'post',  
          cache:false,  
          contentType: "application/json",
          success:function(data) {
              var jsonO = JSON.parse(data);
              if(jsonO.success){
                  layer.closeAll();
                  layerTipMsg(jsonO.success,"成功",jsonO.msg);
                  findByCondition();
              }
              else{
                  layerTipMsg(jsonO.success,"失败",jsonO.msg);
                  $("#result-commit").removeClass("disabled");
                  isSubmit=false;
              }
           }
      });
}

$(function(){
  if($('.chosen-select')){
    $('.chosen-select').chosen({
        allow_single_deselect:true,
        disable_search_threshold: 10
    }); 
    //resize the chosen on window resize

    $(window)
    .off('resize.chosen')
    .on('resize.chosen', function() {
        $('.chosen-select').each(function() {
            var $this = $(this);
            $this.next().css({'width': '539px'});
        })
    }).trigger('resize.chosen');
}
$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    $(window).trigger('resize.chosen')
})
});

</script>
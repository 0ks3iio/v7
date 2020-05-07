<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="bb" class="tab-pane active" role="tabpanel">
<div class="mb10">
	<span class="color-999 mr20">共<#if studentList?? && (studentList?size>0)>${studentList?size}<#else>0</#if>份结果</span>
</div>
<form id="myform" class="print">
<table class="table table-striped table-hover table-layout-fixed no-margin">
	<thead>
	<tr>
		<th style="width:15%">序号</th>
		<th style="width:7%">姓名</th>
		<th style="width:15%">学号</th>
		<th style="width:5%">性别</th>
		<th style="width:10%">行政班</th>
		<#if courseList?? && (courseList?size>0)>
		<#list courseList as course>
			<th class="noprint">${course.subjectName!}</th>
		</#list>
		</#if> 
		<th style="">是否参与选课</th>
	</tr>
</thead>
<tbody>
<#if studentList?? && (studentList?size>0)>
	<#list studentList as item>
	<tr>
		<td>${item_index+1}</td>
		<td>${item.studentName!}</td>
		<td>${item.studentCode!}
			<input type="hidden"  id="resultDtoList[${item_index}].studentId" name="resultDtoList[${item_index}].studentId" value="${item.studentId!}">
			<input type="hidden"  id="${item.studentId!}" name="resultDtoList[${item_index}].courseIds" value="">	
			<input type="hidden"  id="nojoinChoose_${item.studentId!}" name="resultDtoList[${item_index}].nojoinChoose" value="${item.nojoinChoose?default('0')}">	
				
		</td>
		<td>${mcodeSetting.getMcode("DM-XB","${item.sex!}")}</td>
		<td>${item.className!}</td>
		<#if courseList?? && (courseList?size>0)>
			<#list courseList as course>
				<td><span data-value="${course.id!}"  class="course-choose ${item.studentId!}<#if item.nojoinChoose?default('0')=='1'> disabled</#if>"  value="${item.studentId!}">${course.subjectName!}</span></td>
			</#list>
		</#if> 
		<td><label><input type="checkbox" class="wp wp-switch js-toggleOpen js-nojoinChoose"<#if item.nojoinChoose?default('0')=='0'> checked</#if> value="${item.studentId!}"><span class="lbl"></span></label></td>
	</tr>
	</#list>
<#else>
<tr>
	<td colspan="88" align="center">
		暂无数据
	</td>
<tr>
</#if> 
</tbody>
</table>
</form>
	<div class="navbar-fixed-bottom opt-bottom">
	<p class="tip tip-grey noprint">
		<a href="javascript:" class="btn btn-blue js-saveSelectResult" onclick="unchoosenSave()">保存</a>
	</p>
</div>
</div>

<script type="text/javascript">
$(function(){
	var expr = new RegExp('>[ \t\r\n\v\f]*<', 'g');
	$("#myform table tbody").html($("#myform table tbody").html().replace(expr, '><'));
})
var chooseNum=${chooseNum};
$('.js-nojoinChoose').click(function(){
	var stuId=$(this).val();
	if(this.checked){
		$("#nojoinChoose_"+stuId).val("");
		$("."+stuId).removeClass('disabled');
	}else{
		$("#nojoinChoose_"+stuId).val("1");
		$("#"+stuId).val("");
		$("."+stuId).removeClass('selected');
		$("."+stuId).addClass('disabled');
	}
});
	
$('.course-choose').on('click',function(){
	var isSelected = true;
	if($(this).hasClass('selected')){
		isSelected = false;
	}
	changeChosenSubject($(this),isSelected);
});
function changeChosenSubject(obj,isSelected){
	if(obj.hasClass('disabled')) 
		return;
	if(isSelected){
		obj.addClass('selected');
	}else{
		obj.removeClass('selected');
	}
	var studentId = obj.attr('value');
	
	var subs = $('.'+studentId+'.selected');
	
	var allSubs = $('.'+studentId);
	var courseIds = '';
	if(subs.length<chooseNum){
		for(var i=0;i<allSubs.length;i++){
			if($('.'+studentId+':eq('+i+')').hasClass('disabled')){
				$('.'+studentId+':eq('+i+')').removeClass('disabled');
			}
			if($('.'+studentId+':eq('+i+')').hasClass('selected')){
				if(courseIds && courseIds.length > 0){
					courseIds = courseIds + ',' + $('.'+studentId+':eq('+i+')').attr('data-value'); 
				}else{
					courseIds = $('.'+studentId+':eq('+i+')').attr('data-value');
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
					courseIds = courseIds + ',' + $('.'+studentId+':eq('+i+')').attr('data-value'); 
				}else{
					courseIds = $('.'+studentId+':eq('+i+')').attr('data-value');
				}
			}
		}
	}
	$('#'+studentId).val(courseIds);
}
var isSaveSubmit=false;
function unchoosenSave(){
	if(isSaveSubmit){
		return;
	}
	isSaveSubmit=true;
	$(".js-saveSelectResult").addClass("disabled");

  var check = checkValue('#myform');
  if(!check){
    $(".js-saveSelectResult").removeClass("disabled");
    isSaveSubmit=false;
    return;
  }
  
  var obj = new Object();
  // 获取此控件下所有的可输入内容，并组织成json格式
  // obj，是因为url所对应的接收对象是个dto，数据是存在dto
  obj= JSON.parse(dealValue('#myform'));
  var options = {
      url : '${request.contextPath}/newgkelective/${gkChoice.id!}/chosenSubject/saveAll',
      dataType : 'json',
      success : function(data){
          if(data.success){
              layer.closeAll();
              isSaveSubmit=false;
              $(".js-saveSelectResult").removeClass("disabled");
              layer.msg(data.msg, {
							offset: 't',
							time: 2000
					});
              findByCondition(1);
          }
          else{
              layerTipMsg(data.success,"失败",data.msg);
              $(".js-saveSelectResult").removeClass("disabled");
              isSaveSubmit=false;
          }
      },
      clearForm : false,
      resetForm : false,
      type : 'post',
      error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
  };
  $("#myform").ajaxSubmit(options);
}
</script>
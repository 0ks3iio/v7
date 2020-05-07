<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
		<div class="box box-default">
			<div class="box-body">
				<ul class="nav nav-tabs" role="tablist" style="margin:0">
					<li class="active" role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('1')">补课日设置</a></li>
					<#if !isStandard>
						<li role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('2')">未签到通知设置</a></li>
					</#if>
					<li role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('3')">宿舍考勤时间设置</a></li>
					<li role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('4')">出校时间设置</a></li>
					<#if isStandard>
						<li role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('2')">其他设置</a></li>
					</#if>
					<#if isUseFace>
						<li role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('5')">人脸识别设置</a></li>
					</#if>
					<#if isUseTiming>
						<li role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('6')">定时开关机设置</a></li>
					</#if>
					<li role="presentation"><a href="#tabList" role="tab" data-toggle="tab" onclick="showList('7')">上下学考勤时间设置</a></li>
				</ul>
				<div class="tab-content">
					<div id="tabList" class="tab-pane active" role="tabpanel">
					</div>
				</div>
			</div>
		</div>

<!-- page specific plugin scripts -->
<script src="${request.contextPath}/static/components/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script type="text/javascript">
$(function(){
		showList('1');

		// 编辑考勤时间
	    $('.js-editCheckTime').on('click',function(e){
	    	e.preventDefault();
		    layer.open({
		    	type: 1,
		    	shade: 0.5,
		    	title: '编辑',
		    	area: '400px',
		    	btn: ['确定','取消'],
		    	zIndex: 1030,
		    	content: $('.layer-editCheckTime')
		    })
	    });


	    $('.js-editLeaveTime').on('click',function(e){
	    	e.preventDefault();
		    layer.open({
		    	type: 1,
		    	shade: 0.5,
		    	title: '编辑',
		    	area: '540px',
		    	btn: ['确定','取消'],
		    	content: $('.layer-editLeaveTime')
		    })
	    })

	    if($('.js-changeType').val()==='1'){
	    	$('.timepicker02').val('').timepicker({
		    	defaultTime: ''
		    })
	    }

	    $('.js-changeType').on('change',function(){
	    	var timer = $('.timepicker02');
	    	if( $(this).val()==='4' ){
	    		timer.val('').datetimepicker({
			    	language: 'zh-CN',
			    	autoclose: true
			    })
	    	}else{
	    		timer.val('').timepicker({
			    	defaultTime: ''
			    })
	    	}
	    })
});

function showList(type){
	var url = '';
	if (type == '2') {
		url = '${request.contextPath}/eclasscard/attence/notice/set';
	} else if (type == '5') {
		url =  '${request.contextPath}/eclasscard/face/student/tab';
	} else if (type == '6') {
		url = '${request.contextPath}/eclasscard/attence/timing/set';
	} else {
		url =  '${request.contextPath}/eclasscard/attence/query/head?type='+type;
	}
	hidenBreadBack();
	$("#tabList").load(url);
}
function compareTimes(elem1, elem2) {
    if (elem1.val() != "" && elem2.val() != "") {
      var date1;
      var date2;
      try {
        date1 = elem1.val().split(':');
        date2 = elem2.val().split(':');
      } catch (e) {
        date1 = elem1.split(':');
        date2 = elem2.split(':');
      }
      if (eval(date1[0]) > eval(date2[0])) {
        return 1;
      } else if (eval(date1[0]) == eval(date2[0])) {
        if (eval(date1[1]) > eval(date2[1])) {
          return 1;
        } else if (eval(date1[1]) == eval(date2[1])) {
            return 0;
        } else {
          return -1;
        }
      } else {
        return -1;
      }
    }
  }
</script>


<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<link href="${request.contextPath}/static/tablesort/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${request.contextPath}/static/tablesort/js/jquery.tablesorter.js"></script>
<script>
$.tablesorter.addWidget({  
  id: 'fixFirstNumber',  
  format: function(table, config, widgetOptions, initFlag) {  
    for(var i=1;i<table.rows.length;i++){  
        //console.log(table.rows[i].cells[0].innerHTML);  
        table.rows[i].cells[0].innerHTML = i;  
    }  
  }  
});
$.tablesorter.addParser({ 
    id: 'grade', 
    is: function(s) { 
        return false; 
    }, 
    format: function(s) {
        return doFormat(s); 
    }, 
    type: 'text' 
});
function doFormat(s){
	if(''==s){
		return s;
	}
	var arr = s.trim().split("(");
	if(arr.length==2){
		var num = arr[1].split(")")[0];
		num = parseFloat(num)/10000;
		return arr[0].substring(arr[0].length-2)+num;
	}
	return arr[0].substring(arr[0].length-3);
} 
$(function(){
	var expr = new RegExp('>[ \t\r\n\v\f]*<', 'g');
	$("#myform table tbody").html($("#myform table tbody").html().replace(expr, '><'));
	
	var last = $('.tablesorter thead tr th').length-1;
	$(".tablesorter").tablesorter({
		headers:{
			0:{sorter:false},
			1:{sorter:false},
			2:{sorter:false},
			3:{sorter:false},
			4:{sorter:false},
			<#if courseList??&&courseList?size gt 0>
			<#list courseList as item>
			${item_index+5}:{sorter:"grade"},
			</#list>
			</#if>
			${courseList?size+5!}:{sorter:false}
		},
		sortInitialOrder: 'desc',
		widgets: ['fixFirstNumber']  
	});
});
</script>
<div id="aa" class="tab-pane active" role="tabpanel">
<div class="mb10">
	<span class="color-999 mr20">共<#if studentList?exists && (studentList?size gt 0)>${studentList?size}<#else>0</#if>份结果</span>
	<button class="btn btn-small btn-white mr20 adjust-filter" adjustType="adjust-filter-no"><i class="fa fa-circle font-12 color-d9d9d9"></i> 不调剂</button>
	<button class="btn btn-small btn-white mr20 adjust-filter" adjustType="adjust-filter-can"><i class="fa fa-circle font-12 color-yellow"></i> 可调剂</button>
	<button class="btn btn-small btn-white adjust-filter" adjustType="adjust-filter-priority"><i class="fa fa-circle font-12 color-green"></i> 优先调剂</button>
</div>
<form id="myform">
<table class="table table-striped table-bordered table-hover no-margin mainTable tablesorter" style="font-size:1em;">
    <thead>
        <tr>
        	<th width="4%">序号</th>
        	<th width="6%">姓名</th>
            <th width="10%">学号</th>
            <th width="8%">性别</th>
            <th width="8%">行政班</th>
   			<#if courseList?? && courseList?size gt 0>
   			<#list courseList as item>
   			<th>${item.subjectName!}</th>
   			</#list>
   			</#if>         
            <#if scourceType?default('') != '9'>
            <th width="13%">操作</th>
            </#if>
        </tr>
    </thead>
    <tbody id="chosen-detail">
	<#if studentList?? && (studentList?size>0)>
		<#list studentList as item>
		<tr <#if item.lock?default('0')=='1'>class="forbidden"</#if>>
			<td>${item_index+1}</td>
			<td>${item.studentName!}</td>
			<td>${item.studentCode!}</td>
			<td>${mcodeSetting.getMcode("DM-XB","${item.sex!}")}</td>
			<td>${item.className!}</td>
			<#if courseList?? && courseList?size gt 0>
			<#if (item.resultList)?exists && (item.resultList)?size gt 0>
   				<#list courseList as course>
				<td>
				<#list item.resultList as result>
					<#if course.id==result.subjectId>
					<span class="<#if result.subjectType?default('1')=='1'>adjust-filter-no<#elseif result.subjectType?default('1')=='2'>adjust-filter-can<#else>adjust-filter-priority</#if>">
						<i class="fa fa-circle font-12
							<#if result.subjectType?default('1')=='1'>
								color-d9d9d9
							<#elseif result.subjectType?default('1')=='2'>
								color-yellow
							<#else>
								color-green
							</#if>">
						</i> ${result.subjectName!}
						<#if item.subjectScore?exists && item.subjectScore[result.subjectId]?exists>(${item.subjectScore[result.subjectId]?string("##.##")})</#if>
					</span>
					</#if>
				</#list>
				</td>
   				</#list>
			</#if>
   			</#if>
			<#if scourceType?default('') != '9'>
			<td>
				
				<a href="javascript:;" class="table-btn color-green js-toggleLock unlock_${item.studentId!}" onclick="lockResult('${item.studentId!}','0')" <#if item.lock?default('0')=='0'>style="display:none"</#if>>解锁</a>
				<a href="javascript:;" class="table-btn color-green js-toggleLock lock_${item.studentId!}" onclick="lockResult('${item.studentId!}','1')"  <#if item.lock?default('0')=='1'>style="display:none"</#if>>锁定</a>
				<a href="javascript:;" class="table-btn color-orange js-editCourse edit_${item.studentId!} <#if item.lock?default('0')=='1'>disabled</#if>" onclick="editResult('${item.studentId!}')">编辑</a>
				<a href="javascript:;" class="table-btn color-red js-delete delete_${item.studentId!} <#if item.lock?default('0')=='1'>disabled</#if>" onclick="doDeleteChoose('${item.studentId!}','${item.studentName!}')">删除</a>
			</td>
			</#if>
		</tr>
	</#list>
	</#if> 
	</tbody>
</table>
</form>
</div>
<div class="layer layer-editCourse"></div>
<script type="text/javascript">
	$(function () {
		$(".adjust-filter").on("click", function () {
			if ($(this).hasClass("btn-blue")) {
				$(this).removeClass("btn-blue");
				$(this).addClass("btn-white");
			} else {
				$(this).removeClass("btn-white");
				$(this).addClass("btn-blue");
				$(this).siblings().each(function () {
					if ($(this).hasClass("btn-blue")) {
						$(this).removeClass("btn-blue");
						$(this).addClass("btn-white");
					}
				});
			}
			if ($(this).parent().find("button.btn-blue").size() == 0) {
				$("#chosen-detail").find("tr").show();
			} else {
				$("#chosen-detail").find("tr").hide();
				$(this).parent().find("button.btn-blue").each(function () {
					var typeTmp = $(this).attr("adjustType");
					$("#chosen-detail").find("tr").each(function () {
						if ($(this).find("." + typeTmp).size() > 0) {
							$(this).show();
						}
					});
				});
			}
		});
	});

	function lockResult(studentId, lock) {
		$.ajax({
			url: '${request.contextPath}/newgkelective/${gkChoice.id!}/chosenSubject/lockOne?lock=' + lock + '&studentId=' + studentId,
			type: 'post',
			dataType: 'json',
			success: function (data) {
				if (data.success) {
					if (lock == '1') {
						$(".edit_" + studentId).addClass("disabled");
						$(".delete_" + studentId).addClass("disabled");
						$(".unlock_" + studentId).show();
						$(".lock_" + studentId).hide();
						$(".edit_" + studentId).closest('tr').addClass('forbidden');
						$(".delete_" + studentId).closest('tr').addClass('forbidden');
					} else {
						$(".edit_" + studentId).removeClass("disabled");
						$(".delete_" + studentId).removeClass("disabled");
						$(".lock_" + studentId).show();
						$(".unlock_" + studentId).hide();
						$(".edit_" + studentId).closest('tr').removeClass('forbidden');
						$(".delete_" + studentId).closest('tr').removeClass('forbidden');
					}
				} else {
					layerTipMsg(data.success, "失败", data.msg);
				}
			}
		});
	}

	function editResult(studentId) {
		if ($(".edit_" + studentId).hasClass("disabled")) {
			return;
		}
		var url = "${request.contextPath}/newgkelective/${gkChoice.id!}/chosenSubject/edit/page?studentId=" + studentId;
		$(".layer-editCourse").load(url, function () {
			layer.open({
				type: 1,
				shade: 0.5,
				title: '编辑选课信息',
				move: true,
				area: ['400px', '460px'],
				btn: ['确定', '取消'],
				btnAlign: 'c',
				yes: function (index) {
					doSaveCourse();
				},
				content: $('.layer-editCourse')
			});
		});
	}

	function doDeleteChoose(studentId, stuName) {
		if ($(".delete_" + studentId).hasClass("disabled")) {
			return;
		}
		layer.confirm("确定要删除<span style='color:red'>" + stuName + "</span>的选课信息吗？", {
			title: ['提示', 'font-size:20px;'],
			btn: ['确认', '取消'] //按钮
		}, function () {
			doDeleteByStudentId(studentId);
		}, function () {
			layer.closeAll();
		});
	}

	function doDeleteByStudentId(studentId) {
		var ii = layer.load();
		$.ajax({
			url: '${request.contextPath}/newgkelective/${gkChoice.id!}/chosenSubject/delete',
			data: {'studentId': studentId},
			type: 'post',
			success: function (data) {
				var jsonO = JSON.parse(data);
				if (jsonO.success) {
					layer.closeAll();
					layer.msg(jsonO.msg, {offset: 't', time: 2000});
					findByCondition(1);
				} else {
					layerTipMsg(jsonO.success, "失败", jsonO.msg);
				}
				layer.close(ii);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	}

	function choosenLock(lock) {
		$.ajax({
			url: '${request.contextPath}/newgkelective/${gkChoice.id!}/chosenSubject/lockAll?lock=' + lock,
			type: 'post',
			dataType: 'json',
			success: function (data) {
				if (data.success) {
					layer.msg(data.msg, {offset: 't', time: 2000});
					findByCondition(1);
				} else {
					layerTipMsg(data.success, "失败", data.msg);
				}
			}
		});
	}
</script>
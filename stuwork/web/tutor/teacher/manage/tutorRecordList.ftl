<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>登记类型</th>
				<th>登记导师</th>
				<th>学生姓名</th>
				<th>班级</th>
				<th>内容</th>
				<th>最近登记时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		   <#if tutorShowRecordDtos?exists && tutorShowRecordDtos?size gt 0>
              <#list tutorShowRecordDtos as stu>
                  <tr>
					<td>${mcodeSetting.getMcode("DM-DSJL-LX","${stu.tutorRecordDetailed.recordType!}")}</td>
					<td>${stu.teacherName!}</td>
					<td>${stu.stuNames!}</td>
					<td>${stu.gcName!}</td>
					<td>${stu.tutorRecordDetailed.recordResult!}</td>                    
					<td>${stu.tutorRecordDetailed.creationTime?string('yyyy-MM-dd HH:mm')}</td>
					<td>
						<a href="javascript:void(0);" class="js-show-tutorRecord" value="${stu.tutorRecordDetailed.id!}">查看</a>
					    <#if isAll?default(true)>
						  	<a href="javascript:void(0);" class="js-tutor-checkIn"  value="${stu.tutorRecordDetailed.id!}">编辑</a>
							<a href="javascript:void(0);" class="color-red js-del"  value="${stu.tutorRecordDetailed.id!}">删除</a>
						</#if>
					</td>
					<input type="hidden"  class="detailedId"  value="${stu.tutorRecordDetailed.id!}"/>
				  </tr>
              </#list>
           <#else>
               <tr>
					<td  colspan="88" align="center">
					暂无记录
					</td>
			   <tr>
               <#--  "暂无学生" -->
           </#if>   
		</tbody>
	</table>
</div>
<div class="table-container-footer">
	<nav class="nav-page no-margin clearfix">
		<ul class="pagination pagination-sm pull-right">
		  <#if tutorShowRecordDtos?exists&&tutorShowRecordDtos?size gt 0>
		    <@htmlcom.pageToolBar container=".table-container" class="noprint"/>
		  </#if>
		</ul>
	</nav>
</div> 

<script>

 //删除记录
$('.js-del').on('click', function(e){
	        var detailedId = $(this).attr("value");
			e.preventDefault();
			var that = $(this);
			var index = layer.confirm("是否删除这条记录？", {
			 btn: ["确定", "取消"]
			}, 
			function(){
				$.ajax({
		            url:"${request.contextPath}/tutor/record/manage/deleteRecord?tutorRecordDetailedId="+detailedId,
		            data:{},
		            dataType:'json',
		            contentType:'application/json',
		            type:'GET',
		            success:function (data) {
		                if(data.success){
		                    showSuccessMsgWithCall(data.msg,mangeStudentIndex);
		                }else{
		                    showErrorMsg(data.msg);
		                }
		            }
		        });
			  layer.close(index);
			})
	});
//编辑
$('.js-tutor-checkIn').on('click', function(e){
	    var detailedId = $(this).attr("value");
	    var isEdit = "true";
		$('.layer-tutor-detailedId').load("${request.contextPath}/tutor/record/manage/register?tutorRecordDetailedId="+detailedId+"&isEdit="+isEdit,function(){			
	    layer.open({
				type: 1,
				shade: .5,
				title: '登记记录',
				area: '500px',
				btn: ['确定','取消'],
				scrollbar: false,
				yes:function(index,layero){
				   isShow();
				   if(isOk){
				    updateTutorRecord("${request.contextPath}/tutor/record/manage/saveRecord?tutorRecordDetailedId="+detailedId);
				   }
	            },
				content: $('.layer-tutor-detailedId')
	           })
        });
})
	
//登记的修改保存
function updateTutorRecord(contextPath){
  $.ajax({
        url:contextPath,
        data:dealDValue(".layer-tutor-detailedId"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            if(data.success){
                showSuccessMsgWithCall(data.msg,showRecordList);
            }else{
                showErrorMsg(data.msg);
            }
        }
  })
}

//查看
$('.js-show-tutorRecord').click(function(){
    var detailedId = $(this).attr("value");
 	var isSee = "true";
	$('.layer-tutor-detailedId').load("${request.contextPath}/tutor/record/manage/register?tutorRecordDetailedId="+detailedId+"&isSee="+isSee,function(){			
	layer.open({
				type: 1,
				shade: .5,
				title: '查看记录',
				area: '500px',
				btn: [],
				disabled:"true",
				zIndex: 1000,
				content: $('.layer-tutor-detailedId')
	           })
	 });



})
</script>


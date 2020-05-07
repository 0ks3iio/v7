<#if inputDtoList?exists && inputDtoList?size gt 0>
<div class="table-container-body">
	<div class="clearfix">
	</div>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>班级名称</th>
				<th>任课老师</th>
				<th>成绩是否已提交</th>
				<th>录入人数</th>
				<th>成绩锁定</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#list inputDtoList as item>
			<tr>
				<td>${item_index + 1}</td>
				<td>${item.className!}</td>
				<td>${item.teacherNames!}</td>
				<td><#if item.lock>是<#else>否</#if></td>
				<td><span id="numSpan${item_index}"><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="20">
					</span>
				</td>
				<td>
				<label class="no-margin"><input type="checkbox" <#if noLimit?default("0")=="0">disabled="disabled" </#if> class="wp wp-switch js-toggleLock" <#if item.lock>checked="true"</#if> value="${item.classId!}_${item.classType!}"><span class="lbl"></span>
				</label>
				</td>
				<td><a href="javascript:" onclick="toInput('${item.classId!}','${item.classType!}')" class="table-btn show-details-btn">进入录入</a></td>
			</tr>
			</#list>
		</tbody>
	</table>
</div>
<script>
	$(function(){
		//解锁 上锁
	    $('.js-toggleLock').on('change', function(){
	    	var classId=$(this).val();
			if($(this).prop('checked') === true){
				//解锁
				changeLockStatus("1",classId);
			}else{
				//上锁
				changeLockStatus("0",classId);
			}
		});
		setNumSpan();
		
	});
	function setNumSpan(){
		var noLimit=$("#noLimit").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var examId=$("#examId").val();
		var subjectId=$("#subjectId").val();
		if(examId && subjectId){
			$.ajax({
				url:"${request.contextPath}/exammanage/scoreNewInput/setNumSpan",
				data:{examId:examId,subjectId:subjectId,noLimit:noLimit,acadyear:acadyear,semester:semester,gradeCode:gradeCode},
				dataType: "json",
				success: function(data){
					if(data.length!=0){
						for(var i = 0; i < data.length; i ++){
							$("#numSpan"+i).text(data[i].allNum+"/"+data[i].scoreNum);
						}
					}
				}
			});
		}
	}
	function changeLockStatus(status,classIdType){
		var examId=$("#examId").val();
		var subjectId=$("#subjectId").val();
		$.ajax({
			url:"${request.contextPath}/exammanage/scoreNewInput/changeLockStatus",
			data:{examId:examId,subjectId:subjectId,classIdType:classIdType,status:status},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
				}else{
					layerTipMsg(data.success,"失败",data.msg);
				}
			}
		});
	}
	function toInput(classId,classType){
		var noLimit=$("#noLimit").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var subjectId=$("#subjectId").val();
		var examId=$("#examId").val();
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&gradeCode='+gradeCode+'&classType='+classType+'&classId='+classId+'&subjectId='+subjectId+'&noLimit='+noLimit;
		var url='${request.contextPath}/exammanage/scoreNewInput/scoreIndex/page'+c2;
		$(".model-div").load(url);
	}
</script>
<#else>
 	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</#if>
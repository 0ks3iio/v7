<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>序号</th>
			<th>教学班名称</th>
			<th>课程名</th>
			<#if tabType?default('')=='1'>
				<th>所属学科</th>
			<#elseif tabType?default('')=='2'>
				<th>选修课类型</th>
			</#if>
			<th>上课时间与场地</th>
			<th>授课老师</th>
			<th>是否考勤</th>
			<#if tabType?default('')=='2'>
				<th>班级类型</th>
			</#if>
			<th>学生人数</th>
			<th style="width:90px;">学生名单</th>
			<th style="width:100px;">操作</th>
			<th>完成教学</th>
		</tr>
	</thead>
	<tbody>
		<#if teachClassList?exists && teachClassList?size gt 0>
		<#list teachClassList as item>
			<tr>
			<td>${item_index+1}</td>
			<td>${item.name!}</td>
			<td>${item.courseName!}</td>
			<#if tabType?default('')=='1' || tabType?default('')=='2'>
				<td>${item.fromCourseType!}</td>
			</#if>
			<td>
				<#if item.exList?exists && item.exList?size gt 0>
					<#list item.exList as ex>
					<#if ex_index gt 0>
						<br/>
					</#if>
					${ex.timeStr!}<#if ex.placeName?default('')!='' ><#if ex.timeStr?default('')!=''>，</#if>${ex.placeName!}</#if>
					</#list>
				</#if>
			</td>
			<td>${item.teacherNames!}</td>
			<td><#if item.punchCard?default(0)==1>是<#else>否</#if></td>
			<#if tabType?default('')=='2'>
				<td>
					<#if item.isUsingMerge?default('')=='1'>
						用于合并小班
					</#if>
					<#if item.isMerge?default('')=='1'>
						合并大班
					</#if>
					<#if item.isUsingMerge?default('')!='1' && item.isMerge?default('')!='1'>
						普通班
					</#if>
				</td>
			</#if>
			<td>${item.studentNum!}</td>
			<td>
				<#if tabType?default('1')=='0' || item.isUsing?default('1')=='0' || item.isMerge?default('0')=='1'>
					<a href="javascript:" class="table-btn" onclick="showStudents('${item.id!}','1')">查看名单</a>
				<#else>
					<a href="javascript:" class="table-btn" onclick="showStudents('${item.id!}','0')">管理名单</a>
				</#if>
				
			</td>
			<#if tabType?default('1')!='0'>
				<#if item.isUsing?default('1')!='0'>
					<td>
						<a href="javascript:" class="table-btn js-addClass" onclick="editClass('${item.id!}','1','${item.isMerge!}')">编辑</a>
						<a href="javascript:" class="table-btn js-del" onclick="deleteClass('${item.id!}','${item.punchCard?default(0)}')">删除</a>
					</td>
					<td>
						<label><input type="checkbox" class="wp wp-switch js-endClass" value="${item.id!}" <#if item.isUsing?default('1')!='1'>checked="checked"</#if>><span class="lbl"></span></label>
					</td>
				<#else>
					<td><a href="javascript:" class="table-btn js-addClass" onclick="editClass('${item.id!}','0','${item.isMerge!}')">查看</a></td>
					<td>完成</td>
				</#if>
			
			<#else>
				<td><#if item.isUsing?default('1')=='1'>未完成<#else>完成</#if>
				</td>
			</#if>
			
		</tr>
		</#list>
		</#if>
	</tbody>
</table>

<script>
<#if teachClassList?exists && teachClassList?size gt 0>	
$('.js-endClass').click(function(){
	var using="";
	var id=$(this).attr('value');
	if(this.checked){
		using="0";
	}else{
		using="1";
	}
	//修改
	updateUsing(id,using);
});
	
</#if>

function updateUsing(id,using){
	if(using=="1"){
	 	//暂时只有完成 没有反向
	 	layerTipMsg("false","失败","当前状态就是完成,无需操作");
	 	return;
	}
	var msg="完成";
	if(confirm('您确认要修改选中班级教学状态为'+msg+'，确定后不能撤销，确定吗？')){
		$.ajax({
			url:'${request.contextPath}/basedata/teachclass/updateUsing', 
			data:{id:id,using:using},
		    dataType:'json',
		    type:'post',
		    success:function(data) {
		    	if(data.success){
		    		layer.msg(data.msg, {
							offset: 't',
							time: 2000
						});
		             findList();
		        }else{
		         	layerTipMsg(data.success,"失败",data.msg);
		        }
		    },
		    error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});	
	}else{
		findList();
	}
}

function showStudents(teachClassId,isView){
	//页面查询条件
	var acadyearSearch=$("#acadyearSearch").val();
	var semesterSearch=$("#semesterSearch").val();
	var teachClassName=$("#teachClassName").val();
	var studentName = $("#studentName").val();
	var teacherName = $("#teacherName1").val();
	var gradeIds=makeGradeIds();
	var type="";
	$('.mynav').find('li').each(function(){
		if($(this).hasClass("active")){
			type=$(this).attr("id");
			return false;//跳出循环
		}
	})
	var searchParm="&acadyearSearch="+acadyearSearch+"&semesterSearch="+semesterSearch+"&gradeIds="+gradeIds+"&showTabType="+type+"&isView="+isView+"&teachClassName="+teachClassName+"&studentName="+studentName+"&teacherName="+teacherName;
	var url =  '${request.contextPath}/basedata/teachclass/studentList/page?teachClassId='+teachClassId+searchParm;
	$("#tablistDiv").load(encodeURI(url));
}
function deleteClass(id,punchCard){
	//不管有没有上课时间 都当作清除所有

	if(confirm('您确认要删除选中班级？')){
		var isAll = 0;
		//if(punchCard=="1"){
		if(true){
			if(confirm('清空该班级整个学期课表请点“确定”，清空本周及以后请点“取消”。在学期开始前操作，选哪个都一样。')){
				isAll = 1;
			}
			var msg = isAll == 1 ? "本学期" : "本周及以后";
			if(confirm('即将清空'+msg+'课表，清空后不能撤销，确定吗?')){
				$.ajax({
					url:'${request.contextPath}/basedata/teachclass/delete', 
					data:{id:id,isAll:isAll},
				    dataType:'json',
				    type:'post',
				    success:function(data) {
				    	layer.closeAll();
				    	if(data.success){
				    		layer.msg("删除成功！", {
								offset: 't',
								time: 2000
							});
				            findList("1");
				        }else{
				         	layerTipMsg(data.success,"失败",data.msg);
				        }
				        
				    },
				    error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
				});	
			}
		}else{
			$.ajax({
				url:'${request.contextPath}/basedata/teachclass/delete', 
				data:{id:id},
			    dataType:'json',
			    type:'post',
			    success:function(data) {
			   		layer.closeAll();
			    	if(data.success){
			    		layer.msg("删除成功！", {
								offset: 't',
								time: 2000
							});
			            findList("1");
			        }else{
			         	layerTipMsg(data.success,"失败",data.msg);
			        }
			    },
			    error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});	
		}
	}
}
function editClass(classId,type,isMerge){
	var title="查看教学班";
	if('1'==type){
		title="修改教学班";
	}
	if(isMerge == '0'){
		var url = "${request.contextPath}/basedata/teachclass/addOredit/page?id="+classId+"&showView="+type;
	    indexDiv = layerDivUrl(url,{title: title,width:800,height:800});
	}else {
		var acadyearSearch=$("#acadyearSearch").val();
		var semesterSearch=$("#semesterSearch").val();
		var showTabType=$("#tabType").val();
		var url =  "${request.contextPath}/basedata/teachclass/mergeclass/edit/page?id="+classId+"&showView="+type+"&acadyearSearch="+acadyearSearch+"&semesterSearch="+semesterSearch+"&showTabType="+showTabType;
		$("#indexContent").load(url);
		
	}
}
</script>


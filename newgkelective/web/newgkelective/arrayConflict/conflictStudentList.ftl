<#assign xzbId=stuCourseDto.xzbId?default('')>
<#assign searchXzbList=stuCourseDto.searchXzbList>
<#assign xzbSubjectTime=stuCourseDto.xzbSubjectTime>
<#assign cNameMap=courseNameMap>
<#assign sameTimeMap=sameMap>
<#assign jxbSubjectTime=stuCourseDto.jxbSubjectTime>
<#assign jxbList=stuCourseDto.jxbList>
<#assign searchJxbList=stuCourseDto.searchJxbList>

<div id="myTable">
<table  class="table table-bordered table-striped js-sort-table">
	<tr>
		<th>班级名称</th>
		<th>科目</th>
		<th>上课时间</th>
		<th>选择班级</th>
		<th>选中班级科目</th>
		<th>选中班级上课时间</th>
	</tr>
	<#if xzbId!="">
		<#assign chooseXZB=xzbSubjectTime[xzbId]>
		<#assign ii=0>
		<#list chooseXZB?keys as key>
		<tr>
			<#if ii==0><td rowspan="${chooseXZB?size}">${stuCourseDto.xzbName!}</td></#if>
			<td>${cNameMap[key]!}</td>
			<td>
				<#if chooseXZB[key]?exists && chooseXZB[key]?size gt 0>
					<#list chooseXZB[key] as time>
						<#assign kk=time.dayOfWeek+"_"+time.periodInterval+"_"+time.period>
						<#if time_index !=0>
							<br/>
						</#if>
						<#if sameTimeMap[kk]?default('')!=''>
						<font color="red;">${time.timeTr!}</font>
						<#else>
						${time.timeTr!}
						</#if>
						
					</#list>
				</#if>
			</td>
			<#if isCongZu>
				<#if ii==0>
				<td rowspan="${chooseXZB?size}">
					<select class="changeXZBClass" id="xzbIdClass" nullable="false">
						<option value="">--请选择--</option>
						<#list searchXzbList as item>
						<option value="${item.id}" <#if xzbId==item.id>selected</#if>>${item.className}</option>
						</#list>
					</select>
				</td>
				</#if>
				<td>${cNameMap[key]!}</td>
				<td class="chooseXZBClass" id="chooseXZBClass_${key!}">
					<#if chooseXZB[key]?exists && chooseXZB[key]?size gt 0>
						<#list chooseXZB[key] as time>
							<#if time_index !=0>
								<br/>
							</#if>
							${time.timeTr!},
						</#list>
					</#if>
				</td>
			<#else>
				<#if ii==0>
				<td rowspan="${chooseXZB?size}">
					<input type="hidden" id="xzbIdClass" value="${xzbId!}">
				</td>
				</#if>
				<td>${cNameMap[key]!}</td>
				<td class="chooseXZBClass" id="chooseXZBClass_${key!}">
					<#if chooseXZB[key]?exists && chooseXZB[key]?size gt 0>
						<#list chooseXZB[key] as time>
							<#if time_index !=0>
								<br/>
							</#if>
							${time.timeTr!},
						</#list>
					</#if>
				</td>
			</#if>
		</tr>
		<#assign ii=ii+1>
		</#list>
	</#if>
	<#if jxbList?exists && jxbList?size gt 0>
		<#list jxbList as jxbClass>
			<#assign subjectTime=jxbSubjectTime[jxbClass.id]>
			<tr>
				<#assign ii=0>
				<#list subjectTime?keys as key>
					<#if ii==0>
					<td  rowspan="${subjectTime?size}">${jxbClass.className!}</td>
					</#if>
					<#assign subjectId=key?split('_')>
					<td>${cNameMap[subjectId[0]]!}</td>
					<td>
						<#if subjectTime[key]?exists && subjectTime[key]?size gt 0>
							<#list subjectTime[key] as time>
								<#assign kk=time.dayOfWeek+"_"+time.periodInterval+"_"+time.period>
								<#if time_index !=0>
									<br/>
								</#if>
								<#if sameTimeMap[kk]?default('')!=''>
								<font color="red;">${time.timeTr!}</font>
								<#else>
								${time.timeTr!},
								</#if>
							</#list>
						</#if>
					</td>
					<#if ii==0>
					<td rowspan="${subjectTime?size}">
					<input type="hidden" class="jxbSubjectClass" value="${key!}">
						<select class="changeJXBClass" name="jxbIds" nullable="false" id="jxbIds_${key!}">
							<option value="">--请选择--</option>
							<#list searchJxbList[key] as item>
							<option value="${item.id}" <#if jxbClass.id==item.id>selected</#if>>${item.className}</option>
							</#list>
						</select>
					</td>
					</#if>
					<td>${cNameMap[key]!}</td>
					<td class="chooseJXBClass">
						<#if subjectTime[key]?exists && subjectTime[key]?size gt 0>
							<#list subjectTime[key] as time>
								<#assign kk=time.dayOfWeek+"_"+time.periodInterval+"_"+time.period>
								<#if time_index !=0>
									<br/>
								</#if>
								<#if sameTimeMap[kk]?default('')!=''>
								<red>${time.timeTr!},</red>
								<#else>
								${time.timeTr!},
								</#if>
							</#list>
						</#if>
					</td>
				<#assign ii=ii+1>
				</#list>
				
			</tr>
		</#list>
	</#if>
	
</table>

</div>
<div class="filter-item filter-item-right">
	<a class="btn btn-blue" onclick="saveAll();">保存</a>
</div>
<script>
	var xzbmap={};
	<#list xzbSubjectTime?keys as key>
		var xxMap={};
		<#list xzbSubjectTime[key]?keys as key1>
			var tt="";
			<#list xzbSubjectTime[key][key1] as item>
				<#if item_index==0>
				tt=tt+'${item.timeTr!},';
				<#else>
				tt=tt+'<br/>${item.timeTr!},';
				</#if>
			</#list>
			xxMap['${key1!}']=tt;
			xzbmap['${key!}']=xxMap;
		</#list>
	</#list>
	var jxbMap={};
	<#list jxbSubjectTime?keys as key>
		var xxMap={};
		<#list jxbSubjectTime[key]?keys as key1>
			var tt="";
			<#list jxbSubjectTime[key][key1] as item>
				<#if item_index==0>
				tt=tt+'${item.timeTr!}';
				<#else>
				tt=tt+'<br/>${item.timeTr!}';
				</#if>
			</#list>
			xxMap['${key1!}']=tt;
			jxbMap['${key!}']=xxMap;
		</#list>
	</#list>
	<#if isCongZu>
	$(".changeXZBClass").on("click",function(){
		var xzbId=$(this).val();
		if(xzbId==""){
			$(".chooseXZBClass").html("");
		}else{
			if(xzbmap[xzbId]){
				var xxMap=xzbmap[xzbId];
				for(var key in xxMap){
					$("#chooseXZBClass_"+key).html(xxMap[key]);
				}
			}else{
				$(".chooseXZBClass").html("");
			}
		}
		
	})
	</#if>
	$(".changeJXBClass").on("click",function(){
		var jxbId=$(this).val();
		var subject=$(this).closest("tr").find(".jxbSubjectClass").val();
		var timeTd=$(this).closest("tr").find(".chooseJXBClass");
		if(jxbId==""){
			timeTd.html("");
		}else{
			if(jxbMap[jxbId]){
				var xxMap=jxbMap[jxbId];
				if(xxMap[subject]){
					timeTd.html(xxMap[subject]);
				}else{
					timeTd.html("");
				}
			}else{
				timeTd.html("");
			}
		}
	})
	
	
	var isSubmit=false;
	function saveAll(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var checkVal = checkValue('#myTable');
		if(!checkVal){
			isSubmit=false;
			return;
		}
		var xzbId=$("#xzbIdClass").val();
		var jxbIds="";
		$(".changeJXBClass").each(function(){
			var jxbId=$(this).val();
			jxbIds=jxbIds+","+jxbId;
		})
		if(jxbIds!=""){
			jxbIds=jxbIds.substring(1);
		}
		$.ajax({
			url:'${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/saveStudentAllClass',
			data: {'studentId':'${studentId!}','xzbId':xzbId,'jxbIds':jxbIds},
			type:'post',
			success:function(data) {
				layer.closeAll();
				var jsonO = JSON.parse(data);
				if(jsonO.success){
					layer.msg(jsonO.msg, {offset: 't',time: 2000});
				}else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				isSubmit =false;
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>
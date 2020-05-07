<#assign weekDays = (weekDays!7) - 1>
<#assign wratio = (100 - 12.5)/(weekDays+1)>
<div class="table-container-body">
	<table class="table table-bordered layout-fixed table-editable table-adjustConflict">
		<thead>
			<tr>
				<th width="8%"></th>
				<th width="4.5%"></th>
				<#list 0..weekDays as day>
	            <th width="${wratio}%" class="text-center">${dayOfWeekMap[day+""]!}</th>
	            </#list>
			</tr>
		</thead>
		<input type="hidden" id="classId" value="${classId!}">
		<input type="hidden" id="timeId" value="${timeId!}">
		<tbody>
		<#assign x=1>
		<#list piMap?keys as piFlag>
		    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
		    <#assign interval = piMap[piFlag]>
		    <#assign intervalName = intervalNameMap[piFlag]>
		    <#list 1..interval as pIndex>
		    <tr>
		    <#if pIndex == 1>
		    	<td rowspan="${interval!}" class="text-center">${intervalName!}</td>
		    </#if>
	        	<td class="text-center">${pIndex!}</td>
					<#assign key = "p" + x />
					<#assign item=(values[key])!>
					<#assign x = x+1>  
					
		            <#if item.subjectName1?default("")=="">
						<td class="droppable" id="0,${piFlag!},${pIndex!}"></span></td>
					<#else>
						<#if item.haveConflict1?default(false)>
							<td class="active"><span id="${item.dayOfWeek1!},${item.periodInterval1!},${item.period1!}" weekType="${item.firstsdWeek1!}" name="${item.otherId1!},${item.placeId1!}" class="conflict-course conflict-course-debugging">${item.subjectName1!}</span></td>
						<#else>
							<td class="disabled"></td>
						</#if>
					</#if>
					<#if item.subjectName2?default("")==""><td class="droppable" id="1,${piFlag!},${pIndex!}"></td>
					<#else><#if item.haveConflict2?default(false)><td class="active"><span id="${item.dayOfWeek2!},${item.periodInterval2!},${item.period2!}" weekType="${item.firstsdWeek2!}" name="${item.otherId2!},${item.placeId2!}" class="conflict-course conflict-course-debugging">${item.subjectName2!}</span></td>
						   <#else><td class="disabled"></td></#if>
					</#if>
					<#if item.subjectName3?default("")==""><td class="droppable" id="2,${piFlag!},${pIndex!}"></td>
					<#else><#if item.haveConflict3?default(false)><td class="active"><span id="${item.dayOfWeek3!},${item.periodInterval3!},${item.period3!}" weekType="${item.firstsdWeek3!}" name="${item.otherId3!},${item.placeId3!}" class="conflict-course conflict-course-debugging">${item.subjectName3!}</span></td>
						   <#else><td class="disabled"></td></#if>
					</#if>
					<#if item.subjectName4?default("")==""><td class="droppable" id="3,${piFlag!},${pIndex!}"></td>
					<#else><#if item.haveConflict4?default(false)><td class="active"><span id="${item.dayOfWeek4!},${item.periodInterval4!},${item.period4!}" weekType="${item.firstsdWeek4!}" name="${item.otherId4!},${item.placeId4!}" class="conflict-course conflict-course-debugging">${item.subjectName4!}</span></td>
						   <#else><td class="disabled"></td></#if>
					</#if>
					<#if item.subjectName5?default("")==""><td class="droppable" id="4,${piFlag!},${pIndex!}"></td>
					<#else><#if item.haveConflict5?default(false)><td class="active"><span id="${item.dayOfWeek5!},${item.periodInterval5!},${item.period5!}" weekType="${item.firstsdWeek5!}" name="${item.otherId5!},${item.placeId5!}" class="conflict-course conflict-course-debugging">${item.subjectName5!}</span></td>
						   <#else><td class="disabled"></td></#if>
					</#if>
					<#if weekDays gte 5>
					<#if item.subjectName6?default("")==""><td class="droppable" id="5,${piFlag!},${pIndex!}"></td>
					<#else><#if item.haveConflict6?default(false)><td class="active"><span id="${item.dayOfWeek6!},${item.periodInterval6!},${item.period6!}" weekType="${item.firstsdWeek6!}" name="${item.otherId6!},${item.placeId6!}" class="conflict-course conflict-course-debugging">${item.subjectName6!}</span></td>
						   <#else><td class="disabled"></td></#if>
					</#if>
					
					</#if>
					
					<#if weekDays gte 6>
						<#if item.subjectName7?default("")==""><td class="droppable" id="6,${piFlag!},${pIndex!}"></span></td>
						<#else><#if item.haveConflict7?default(false)><td class="active"><span id="${item.dayOfWeek7!},${item.periodInterval7!},${item.period7!}" weekType="${item.firstsdWeek7!}" name="${item.otherId7!},${item.placeId7!}" class="conflict-course conflict-course-debugging">${item.subjectName7!}</span></td>
							   <#else><td class="disabled"></td></#if>
						</#if>
					</#if>
	            	
				
		    </tr>
		    </#list>
		    </#if>
	    </#list>
			
		</tbody>
	</table>
</div>
<script>
	$(function(){
		// 设置表格高度
		$('.table-adjustConflict').each(function(){
			$(this).css({
				height: $(window).innerHeight() - $(this).offset().top - 40
			})
		});
	
		var tipHtml = '<div class="conflict-course-tip">\
							<h4>小提示</h4>\
							<p>试试将“红色冲突的班级”移动至“空白单元格“</p>\
							<div class="text-center"><button class="btn btn-sm btn-long btn-blue">我知道了</button></div>\
						</div>';
		$('.conflict-course').eq(0).append(tipHtml);
	
		$(document).on('click', '.conflict-course-tip .btn', function(e){
			e.preventDefault();
			$('.conflict-course-tip').remove();
		});
		
		var isSubmit=false;
		// 放置区域初始化
		$('.droppable').droppable({
			accept: $('.conflict-course'),
			tolerance: 'intersect',
			hoverClass: "active",
	
			drop: function(event, ui){
				var val = $(ui.draggable).text();
				if(isSubmit){ 
					return false;
				}
				// 当放置区域有课程时还原拖拽元素
				if($(this).text() !== ''){
					$(ui.draggable).draggable('option', 'revert', true);
					layer.msg('同一时间只能排一门课', {
						offset: 't'
					});
					return false;
				}
				isSubmit=true;
				var otherStr=$(this).attr("id") +","+ $(ui.draggable).attr("weektype");
				var otherIdAndPlaceId=$(ui.draggable).attr("name");
				var timeId=$("#timeId").val();
				var classId=$("#classId").val();
				$.ajax({
					url:"${request.contextPath}/newgkelective/${arrayId!}/arrayLesson/checkSaveOther/page",
					data:{"otherStr":otherStr,"otherIdAndPlaceId":otherIdAndPlaceId,"classId":classId,"timeId":timeId},
					dataType: "json",
					success: function(data){
						isSubmit=false;
						var jsonO = data;
			            if(jsonO.success){
			                //layer.closeAll();
			                layer.confirm('确定保存？', function(index){
				                justSaveOther(otherStr,otherIdAndPlaceId,timeId);
			                })
			                $(ui.draggable).draggable('option', 'revert', true);
			            }else{
			            	$(ui.draggable).draggable('option', 'revert', true);
							layer.msg(jsonO.msg, {
								offset: 't'
							});
							return false;
			            }
			            //$(ui.draggable).remove();
					}
				});	
			}
		});
	
		$(document).on('mouseover', '.conflict-course', function(){
			
	
			$(this).draggable({
				revert: 'invalid',
				opacity: 0.6,
				zIndex: 100,
				scroll: true,
				containment: $('.table-container-body'),
				start: function(){
					if($(this).find('.conflict-course-tip').length > 0) {
						$('.conflict-course-tip').remove()
					}
				}
			});
			$('.droppable').droppable('option', 'accept', $('.conflict-course'))
		})
	})
	function justSaveOther(otherStr,otherIdAndPlaceId,timeId){
		$.ajax({
			url:"${request.contextPath}/newgkelective/${arrayId!}/arrayLesson/justSaveOther/page",
			data:{"otherStr":otherStr,"otherIdAndPlaceId":otherIdAndPlaceId,"timeId":timeId},
			dataType: "json",
			success: function(data){
				var jsonO = data;
	            if(jsonO.success){
	                layer.closeAll();
	                //layerTipMsg(jsonO.success,"成功",jsonO.msg);
	                
	                solveError('${arrayId!}',"1");
	            }else{
	            	$(ui.draggable).draggable('option', 'revert', true);
					layer.msg(jsonO.msg, {
						offset: 't'
					});
					return false;
	            }
			}
		});	
	}
</script>
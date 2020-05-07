<input type="hidden" name="minWeek" id="minWeek" value="${minWeek!}">
<table class="table" id="scheduleListTable">
	<thead>
		<tr>
			<th width="5%"><span class="ui-checkbox ui-checkbox-all mycheckAll" data-value="no"><input type="checkbox" class="chk"></span></th>
			<th width="15%">时间</th>
			<th width="12%">节次</th>
			<th width="11%">周次</th>
			<th width="19%">班级</th>
			<th width="19%">教室</th>
			<th width="19%">课程</th>
		</tr>
	</thead>
	<tbody>
		<#if courseScheduleList?exists && courseScheduleList?size gt 0>
		<#list courseScheduleList as courseSchedule>
		<tr>
		    <td><span class="ui-checkbox mycheckOne" data-classId="${courseSchedule.classId!}"><input type="checkbox" class="chk" value="${courseSchedule.id!}"></span></td>
		    <td>${courseSchedule.dayTimeStr!}</td>
		    <td>${courseSchedule.periodTimeStr!}</td>
		    <td>第${courseSchedule.weekOfWorktime}周</td>
		    <td>${courseSchedule.className!}</td>
		    <td>${courseSchedule.placeName!}</td>
		    <td>${courseSchedule.subjectName!}</td>
		</tr>
		</#list>
		</#if>
		
	</tbody>
</table>
<script type="text/javascript">
	$(function(){
		$('#scheduleListTable').on('click','.mycheckOne',function(){
			//本身选中操作 在公用js
			var chkLen=$(this).parents('table').find('.mycheckOne').length;
			if(!$(this).hasClass('ui-checkbox-current')){
				var chkedLen=$(this).parents('table').find('.ui-checkbox-current').not('.mycheckAll').length;
				if(chkLen==chkedLen+1){
					
					$(this).parents('table').find('.mycheckAll').addClass('ui-checkbox-current').find('.chk').attr('checked','checked');
					$(this).parents('table').find('.mycheckAll').attr('data-value','yes');
				}
			}else{
				$(this).parents('table').find('.mycheckAll').removeClass('ui-checkbox-current').find('.chk').removeAttr('checked');
				$(this).parents('table').find('.mycheckAll').attr('data-value','no');
			}
			
		});
		
		$('#scheduleListTable .mycheckAll').click(function(){
			//本身选中操作 在公用js
			var chkAll=$(this).attr('data-value');
			if(chkAll=="no"){
				//未选中--全选
				$(this).attr('data-value','yes');
				$(this).addClass('ui-checkbox-current').find('.chk').attr('checked','checked');
				$(this).parents('table').find('.mycheckOne').addClass('ui-checkbox-current').find('.chk').attr('checked','checked');
			}else{
				//全选--不选
				$(this).attr('data-value','no');
				$(this).removeClass('ui-checkbox-current').find('.chk').removeAttr('checked');
				$(this).parents('table').find('.mycheckOne').removeClass('ui-checkbox-current').find('.chk').removeAttr('checked');
			};
		});
	})
	
</script>

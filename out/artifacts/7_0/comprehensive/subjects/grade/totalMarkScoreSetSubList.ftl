<div id="aa" class="tab-pane active" role="tabpanel">
	<table id="settingTable" class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>考试名称</th>
				<th>按比例计入总评（%）</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<#if examInfos?exists && (examInfos?size>0)>
			<#list examInfos as examInfo>
			<tr>
				<input type="hidden" name="examInfoId" value='${examInfo.id!}'>
				<td>${examInfo.examName!}</td>
				<td>
				<input type="text" name="examInfoVal" class="form-control number" value="${examInfo.setupScore!}">
				</td>
				<td><a class="js-del" href="">删除</a></td>
			</tr>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
	$(function(){
		$(document).on('click','.js-del', function(e){
			e.preventDefault();
			var that = $(this);
			layer.confirm('确定删除吗？', function(index){
				that.closest('tr').remove();
				layer.close(index);
			})
		})
	});
</script>

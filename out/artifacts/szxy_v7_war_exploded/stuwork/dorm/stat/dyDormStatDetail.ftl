<div class="layer layer-openDetail">
	<div class="layer-content">
		<table class="table table-striped no-margin">
			<thead>
				<tr>
					<th>考核项</th>
					<th>周日</th>
					<th>周一</th>
					<th>周二</th>
					<th>周三</th>
					<th>周四</th>
					<th>周五</th>
					<th>周六</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>卫生</td>
					<td>${stat.getGWsScore()!}</td>
					<td>${stat.getAWsScore()!}</td>
					<td>${stat.getBWsScore()!}</td>
					<td>${stat.getCWsScore()!}</td>
					<td>${stat.getDWsScore()!}</td>
					<td>${stat.getEWsScore()!}</td>
					<td>${stat.getFWsScore()!}</td>
				</tr>
				<tr>
					<td>内务</td>
					<td>${stat.getGNwScore()!}</td>
					<td>${stat.getANwScore()!}</td>
					<td>${stat.getBNwScore()!}</td>
					<td>${stat.getCNwScore()!}</td>
					<td>${stat.getDNwScore()!}</td>
					<td>${stat.getENwScore()!}</td>
					<td>${stat.getFNwScore()!}</td>
				</tr>
				<tr>
					<td>纪律</td>
					<td>${stat.getGJlScore()!}</td>
					<td>${stat.getAJlScore()!}</td>
					<td>${stat.getBJlScore()!}</td>
					<td>${stat.getCJlScore()!}</td>
					<td>${stat.getDJlScore()!}</td>
					<td>${stat.getEJlScore()!}</td>
					<td>${stat.getFJlScore()!}</td>
				</tr>
			</tbody>
		</table>
		
	</div>
</div>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-close">确定</a>
</div>
<script>
	$(function(){
		$(".layer-openDetail").show();
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
	})
</script>

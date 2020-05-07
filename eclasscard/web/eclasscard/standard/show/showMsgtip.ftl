<div class="layer-tip">
	<div class="layer-tip-icon">
		<span class="icon icon-warning-red"></span>
	</div>
	<p id="tipMsgText" style="text-align:center" ></p>
</div>
<script>
function showMsgTip(msg){
	$("#tipMsgText").html(msg);
	var index = layer.open({
		type: 1,
		title: '提示',
		shade: 0,
		shadeClose: true,
		area: '425px',
		content: $('.layer-tip')
	});
	layer.style(index, {
		'border-radius': '15px'
	})
	$('.layer-checkin-close').on('click', function(e){
		e.preventDefault();
		layer.close(index);
	})
	setTimeout(function(){
		layer.close(index);
	},1500);
}
</script>
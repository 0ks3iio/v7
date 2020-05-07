<div class="box box-default">
    <div class="filter-made mb-10">
		<div class="filter-item filter-item-right clearfix">
	        <button class="btn btn-lightblue" onclick="tree();">全系统用户树</button>
		</div>
	</div>
</div>
<div class="layer layer-power"></div>
<script>
  	function tree() {
  		  	$.ajax({
	            url: '${request.contextPath}/bigdata/common/tree/userTreeIndex',
	            type: 'POST',
	        data: {users:''},
	            dataType: 'html',
	            beforeSend: function(){
			      	$('.layer-power').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			    },
	            success: function (response) {
	                $('.layer-power').html(response);
	            }
            });
  	
    	var index = layer.open({
			type: 1,
			title: '用户选择',
			shade: .5,
			shadeClose: true,
			closeBtn: 1,
			btn :['确定','取消'],
			area: ['70%','550px'],
			yes:function(index, layero){
				zTreeSelectedUserIdMap.forEach(function (value, key, map) {
				    console.log(key+"--"+value);
				})
			},
			content: $('.layer-power')
		})
    }
</script>
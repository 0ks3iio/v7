<div class="">
	<h2 class="gk-post-title">
		${newGkChoice.choiceName!}-选课说明
	</h2>
		${newGkChoice.notice!}
	<div class="gk-post-footer">
		<label><input type="checkbox" class="wp"><span class="lbl"> 我已阅读选课说明</span></label>
		<button class="btn btn-long btn-blue js-start" disabled="true">开始选课</button>
	</div>
</div>

<script>
	$(function(){
		
		$('.wp').click(function(){
			if($(this).prop('checked') === false){
				$('.js-start').prop('disabled',true);
			}else{
				$('.js-start').prop('disabled',false);
        	}
		})
		
		$('.js-start').on('click', function(){
			var url = '${request.contextPath}/newgkelective/stuChooseSubject/list/page?hasRead=1&choiceId=${newGkChoice.id!}';
        	$("#itemShowDivId").load(url);
        })
	})
</script>


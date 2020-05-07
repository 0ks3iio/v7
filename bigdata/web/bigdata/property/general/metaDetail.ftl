<div class=" form-set">
    <!-- 内容 -->
    <div class="form-set-content-wrap" style="margin-right:calc(12%);margin-left:calc(12%);">
	${html!}
   </div>
</div>
<script>
$(function(){
   if($('.form-bottom').length>0){
		$('.form-bottom').remove();
   }
   if($('.form-item.selected').length>0){
	   $('.form-item.selected').removeClass('selected');
   }
})

</script>
<div class="box">

</div>
<div id="teachBulletinDiv" class="box box-green">
	
</div>
<script>
$(document).ready(function(){
	var bulletinUrl =  "${request.contextPath}/eccShow/eclasscard/showIndex/bulletin?cardId="+_cardId;
	$("#teachBulletinDiv").load(bulletinUrl);
})
</script>
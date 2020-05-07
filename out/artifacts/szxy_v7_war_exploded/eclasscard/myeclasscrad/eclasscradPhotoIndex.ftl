<div id="eclasscardPhotoDiv">

</div>
<script type="text/javascript">
$(function(){
	eclasscardPhotoEdit();
});
function eclasscardPhotoEdit(){
	var url =  '${request.contextPath}/eclasscard/photoall/edit';
	$("#eclasscardPhotoDiv").load(url);
}

</script>

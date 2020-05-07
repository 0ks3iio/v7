<div id="myEclasscardDiv">

</div>
<script type="text/javascript">
$(function(){
	myEclasscardList();
});
function myEclasscardList(){
	var url =  '${request.contextPath}/eclasscard/myClassCard/list';
	$("#myEclasscardDiv").load(url);
}

</script>

<div id="indexShowDiv" class="box box-default">
</div>
<div id="detailShowDiv" class="box box-default" style="display:none">
</div>
<script type="text/javascript">
var isShowDetail = false;
$(function(){
	classSigninTab();
});
function classSigninTab(){
	var url =  '${request.contextPath}/eclasscard/class/signin/tabPage';
	$("#indexShowDiv").load(url);
}
function showDetail(id,type){
	if(isShowDetail){
		return;
	}
	var url =  '${request.contextPath}/eclasscard/mycalss/signin/detail/list?type='+type+'&id='+id;
	isShowDetail = true;
	$("#detailShowDiv").load(url,function(responseTxt,statusTxt,xhr){
		isShowDetail =false;
	});
	$("#detailShowDiv").show();
	$("#indexShowDiv").hide();
}
function backIndex(type){
	if(type=='1'){
		showMyclassAttList('1');
	}
	$("#detailShowDiv").hide();
	$("#indexShowDiv").show();
}
</script>
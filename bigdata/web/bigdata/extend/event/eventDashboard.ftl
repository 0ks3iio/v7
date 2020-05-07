<script src="${request.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js"></script>
<div class="index">
	<div class="overview-set bg-fff metadata clearfix" id="groupListDiv"></div>
	<div id="cotentDiv" class="overview-set-wrap"></div>
</div>
<div id="eventGroupEditDiv" class="layer layer-add-overview"></div>
<input type="hidden" id="currentGroupId" value=""/>
<script type="text/javascript">
	function loadGroupList(){
		var url =  "${request.contextPath}/bigdata/event/dashboard/group/list";
		$("#groupListDiv").load(url);
	}
	
 	$(function(){
		loadGroupList();
    })
</script>
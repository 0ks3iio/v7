<#macro common>
<script>
    self.setInterval(function(){
       	$.post( "${request.contextPath}/checkLive", function( data ) {
		});
   }, 1200000);
</script>
</#macro>
<#--
分页工具条
container 必须 例如：.class  #id
-->
<#macro pageToolBar container="" class="">
	<script>
		 var reloadDataContainer ="${container}"
	</script>
	<div class="pagination ${class!}">
		${htmlOfPaginationLoad}
	</div>
</#macro>
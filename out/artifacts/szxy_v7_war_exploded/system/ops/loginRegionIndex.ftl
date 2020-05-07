<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">单位：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="unitName" onchange="showDomainList();" >
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a href="javascript:void(0);"  class="btn btn-blue each-unitName" onclick="showDomainList();">查找</a>
				<a href="javascript:void(0);"  class="btn btn-blue js-checkIn" onclick="showLoginSet('');" >新增单位设置</a>
			</div>
		</div>
		<div class="table-container" id="showDomainList">
		
		
		</div>
	</div>
</div>
<div class="layer layer-role-detailedId ">

</div><!-- E 登记记录 -->


<script>
    
   $(document).ready(function(){
    $("#unitName").keypress(function () {
			displayResult();
    });
    showDomainList();
  })
  
  function showDomainList(){
	var unitName = $("#unitName").val();
	unitName =  encodeURI(unitName);
	var url =  '${request.contextPath}/system/ops/loginDomain/List/page?unitName='+unitName;
	$(".table-container").load(url);
  }
  
  function displayResult(){	
		var x;
        if(window.event){
        	 // IE8 以及更早版本
        	x=event.keyCode;
        }else if(event.which){
        	// IE9/Firefox/Chrome/Opera/Safari
            x=event.which;
        }
        if(13==x){
        	showDomainList();
        }
   }
</script>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">类型：</span>
				<div class="filter-content">
					<select name="" id="pSource" class="form-control" onchange="showList()">
					    <option value="">请选择</option>
						<option value="1">默认</option>
						<option value="2">其它</option>
					</select>
				</div>
			</div>
			<#--  
			<div class="filter-item">
				<span class="filter-name">权限名称：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="tName" onchange="showList()" >
				</div>
			</div>
			-->
			<div class="filter-item">
				<span class="filter-name">第三方ap：</span>
				<div class="filter-content">
					<select name="searchServer" id="serverId" class="form-control" onChange="showList()">
						    <option value="">---请选择---</option>
							<#if serverList?exists && (serverList?size>0)>
			                    <#list serverList as server>
				                     <option value="${server.id!}">${server.name!}</option>
			                    </#list>
		                    </#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a href="javascript:void(0);"  class="btn btn-blue js-checkIn">登记权限</a>
			</div>
		</div>
		<div class="table-container">
		
		
		</div>
	</div>
</div>
<div class="layer layer-power-detailedId ">

</div><!-- E 登记记录 -->


<script>
    
    $(document).ready(function(){
    <#--  
	    $("#tName").keypress(function () {
				displayResult();
	    });
    -->
    showList();
  })
  
  function showList(){
	var source = $("#pSource").val();
	var powerName = $("#tName").val();
	var serverId = $("#serverId").val();
	powerName =  encodeURI(powerName);
	var url =  '${request.contextPath}/system/ap/power/findPowerList/page?source='+source+'&serverId='+serverId+'&powerName='+powerName;
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
            showList();
        }
    }
    //登记权限
     $('.filter .js-checkIn').on('click', function(e){
		$('.layer-power-detailedId').load("${request.contextPath}/system/ap/power/register/power/page",function(){			
		layer.open({
					type: 1,
					shade: .5,
					title: '登记权限',
					area: '500px',
					btn: ['确定','取消'],
					yes:function(index,layero){
				       savePower("${request.contextPath}/system/ap/power/savePower");
		            },
					content: $('.layer-power-detailedId')
		           })
         });
	})
    //权限的保存
    var isSubmit=false;
	function savePower(contextPath){
	    if(isSubmit){
			return;
	    }
		isSubmit = true;
	    var check = checkValue('.layer-power-detailedId');
		if(!check){
		 	$(this).removeClass("disabled");
		 	isSubmit=false;
		 	return;
		}
	  $.ajax({
	        url:contextPath,
	        data:dealDValue(".layer-power-detailedId"),
	        clearForm : false,
	        resetForm : false,
	        dataType:'json',
	        contentType: "application/json",
	        type:'post',
	        success:function (data) {
	            isSubmit = false;
	            if(data.success){
	               showSuccessMsgWithCall(data.msg,showList);
	            }else{
	               showErrorMsg(data.msg);
	            }
	        }
	  })
	}
	
	function flushShowPower(){
	  showList();
	}
	

</script>
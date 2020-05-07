<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">类型：</span>
				<div class="filter-content">
					<select name="" id="ownerType" class="form-control" onchange="showUserList()">
					    <option value="">请选择</option>
						<option value="1">学生</option>
						<option value="2">教师</option>
						<option value="3">家长</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">账号：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="userName" onchange="showUserList()" >
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="realName" onchange="showUserList()" >
				</div>
			</div>
			<div class="filter-item ">
				<a href="javascript:void(0);"  class="btn btn-blue js-checkIn" click = "showUserList()">查找</a>
			</div>
		</div>
		<div class="table-container" id="showUserList">
		
		
		</div>
	</div>
</div>
<script>
    
    $(document).ready(function(){
	    $("#userName").keypress(function () {
				displayResult();
	    });
	    $("#realName").keypress(function () {
				displayResult();
	    });
	    showUserList();
   })
  
  function showUserList(){
	var ownerType = $("#ownerType").val();
	var userName = $(".filter-item #userName").val();
	var realName = $("#realName").val();
	userName =  encodeURI(userName);
	realName =  encodeURI(realName);
	var url =  '${request.contextPath}/system/user/power/findUser/page?ownerType='+ownerType+'&userName='+userName+'&realName='+realName;
	$("#showUserList").load(url);
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
            showUserRoleList();
        }
    }
</script>

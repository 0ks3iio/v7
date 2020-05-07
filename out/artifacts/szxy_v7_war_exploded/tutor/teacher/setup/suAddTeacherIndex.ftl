<#import "/fw/macro/webmacro.ftl" as w>
<a  class="page-back-btn" onclick="goSetUpIndex();"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">教师情况：</span>
				<div class="filter-content">
					<select name="" id="tStauts" class="form-control" onchange="showList()">
					    <option value="">请选择</option>
						<option value="1">满员</option>
						<option value="0">未满</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="tName" onchange="showList()" >
				</div>
			</div>
		</div>
		<div class="table-container">
			
		</div>
	</div>
</div>

<script>
  
  $(document).ready(function(){
    $("#tName").keypress(function () {
			displayResult();
    });
    showList();
  })
  
  function showList(){
	var isFull = $("#tStauts").val();
	var teacherName = $("#tName").val();
	teacherName =  encodeURI(teacherName);
	var url =  '${request.contextPath}/tutor/teacher/setUp/showTutorList?isFull='+isFull+'&teacherName='+teacherName+'&tutorRoundId='+'${tutorRoundId!}';
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
  
</script>




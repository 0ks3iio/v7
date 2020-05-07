<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="box box-default">
					<div class="box-body">
						<ul class="nav nav-tabs nav-tabs-1" role="tablist">
							<li class="active" role="presentation"><a href="#aa" role="tab" data-toggle="tab">我的导师</a></li>
							<li role="presentation"><a href="#bb" role="tab" data-toggle="tab">我的导师记录</a></li>
						</ul>
						<div class="tab-content">
							<div id="aa" class="tab-pane active" role="tabpanel">
								<#if isDel>
									<p class="tutor-selected">已被导师设置为毕业</p>
								<#else>
									<p class="tutor-selected">我的导师：<#if tatd?exists><a href="javascript:void(0);" class="tutor-item fixed-width" >${tatd.teacher.teacherName!}（${tatd.isChooseNum!}/${tatd.param!}）</a> <#else>暂无选择 </#if></p>
								</#if>
								
								<div class="tutor-choose">
								   
								</div>

							</div>
							<div id="bb" class="tab-pane" role="tabpanel">
								<div class="filter">
									<div class="filter-item">
										<span class="filter-name">学年：</span>
										<div class="filter-content">
											<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showRecordList()">
													<#if acadyearList?exists && (acadyearList?size>0)>
									                    <#list acadyearList as item>
										                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
									                    </#list>
								                    </#if>
											</select>
										</div>
									</div>
									<div class="filter-item">
										<span class="filter-name">学期：</span>
										<div class="filter-content">
											<select id="searchSemester" name="searchSemester" class="form-control" onChange="showRecordList()">
													${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
											</select>
										</div>
									</div>
								</div>
								
								<div class="table-container" id="showStuRList">
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->

<script>
    
    $(document).ready(function(){
       
        showRecordList();
        <#if !isDel>
        	showTutorList();
        </#if>
    });
  
  	function showRecordList(){
		var acadyear = $("#searchAcadyear").val();
		var semester = $("#searchSemester").val();
		$("#showStuRList").load("${request.contextPath}/tutor/student/showRecordList?acadyear="+acadyear+"&semester="+semester+"&studentId="+'${studentId!}');
	};
    function showTutorList(){
		$(".tutor-choose").load("${request.contextPath}/tutor/student/showTutorList?studentId="+'${studentId!}');
	};
	
	var isSubmit = false;
	//选择导师
	function chooseTuror(){
	  if( isSubmit ){
	    return;
	  }
	  var teacherId ;
	  var tutorRoundId;
	  $('.js-select a').each(function(){
	     if($(this).hasClass('active')){
	        teacherId = $(this).attr("value");
	        tutorRoundId = $(this).parent().find('#tutorRoundId').val();
	     }
	  })
	  isSubmit = true;
	  $.ajax({
	            url:"${request.contextPath}/tutor/student/chooseTutor?teacherId="+teacherId+"&studentId="+'${studentId!}'+"&tutorRoundId="+tutorRoundId,
	            data:{},
	            dataType:'json',
	            contentType:'application/json',
	            type:'GET',
	            success:function (data) {
	                if(data.success){
	                    showSuccessMsgWithCall(data.msg,goStuIndex);
	               //     showSuccessMsg(data.msg);
	                }else{
	                	goStuIndex();
	                    showErrorMsg(data.msg);
	                }
	                isSubmit = false;
	            }
	        });
	}
	
	//重新加载页面
	function goStuIndex(){
	  openModel('super5','学生导师管理','1','${request.contextPath}/tutor/student/index?fromDesktop=true','','','superId','');
	}
</script>

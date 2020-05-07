<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">同步阅卷系统考试</h4>
	</div>
	<div class="box-body">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select class="form-control" id="searchAcadyear" name="searchAcadyear" onChange="showRonghouExam()">
						<#if acadyearList?exists && (acadyearList?size>0)>
		                    <#list acadyearList as item>
			                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		                    </#list>
	                    <#else>
		                    <option value="">未设置</option>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select class="form-control" id="searchSemester" name="searchSemester" onChange="showRonghouExam()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			
			<div class="filter-item">
				<span  class="filter-name">考试：</span>
				<div class="filter-content">
					<select class="form-control" style="width:180px;" id="examKeyId" name="examKeyId" onChange="showRonghouSubject()">
						<option value="">--暂无考试--</option>
					</select>
				</div>
			</div>
		</div>
		<div class="data-checking-list data-list-bordered" id="messul" style="min-width:500px;">
		</div>
		<div style="display:none;" id="subjectNameRela">
			<div class="filter filter-f16 filter-right">
				<div class="filter-item">
					<a class="btn btn-blue" href="javascript:void(0)" onClick="sysncyExam()">同步</a>
				</div>
			</div>
			<form id="mySysncyForm">
			<input type="hidden" name="acadyear" id="acadyear">
			<input type="hidden" name="semester" id="semester">
			<input type="hidden" name="examKey" id="examKey">
			<input type="hidden" name="uKeyId" id="uKeyId">
			<input type="hidden" name="gradeCode" id="gradeCode">
			<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
					<tr>
						<th class="text-center" id="sysncySub">原科目</th>
						<th class="text-center">现科目</th>
						<th class="text-center">操作</th>
					</tr>
				</thead>
				<tbody>
				
				</tbody>
			</table>
			</form>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(function(){
		showRonghouExam();
		$("#messul").on("click",".delMyRow",function(){
			$(this).parents("table").remove();
		})
		$("#subjectNameRela").on("click",".deleRowSub",function(){
			$(this).parent().parent().remove();
		})
	});
	showBreadBack(backExamList,true,"返回");
	var examIdUkey={};
	function showRonghouExam(){
		 var searchAcadyear = $('#searchAcadyear').val();
	     var searchSemester = $('#searchSemester').val();
		 $.ajax({
            url:'${request.contextPath}/exammanage/sysncy/findExamList',
            data: {'acadyear':searchAcadyear,'semester':searchSemester},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(!jsonO.success){
                   //失败
                   makeMess("查询考试列表",false,jsonO.msg);
                }
                else{
                   examIdUkey={};
                   var examList=jsonO.examList;
                   if(examList.length==0){
                   		 $("#examKeyId").html('');
                   		 $("#examKeyId").append('<option value="">--暂无考试--</option>');
                   }else{
                   		$("#examKeyId").html('');
                   		var henltext='<option value="">--请选择--</option>';
                   		for(var ii=0;ii<examList.length;ii++){
                   			examIdUkey[examList[ii].examKey]=examList[ii].ownedby;
                   			henltext=henltext+'<option value="'+examList[ii].examKey+'">'+examList[ii].examName+'</option>';
                   		}
                   		$("#examKeyId").html(henltext);
                   }
                   
                }
                $("#subjectNameRela").find("tbody").html('');
                $("#subjectNameRela").hide();
                $("#messul").show();
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
		 
	}
	
	function showRonghouSubject(){
		var searchAcadyear = $('#searchAcadyear').val();
	    var searchSemester = $('#searchSemester').val();
	    var examKeyId=$("#examKeyId").val();
	    if(examKeyId==""){
	    	$("#subjectNameRela").find("tbody").html('');
	    	$("#acadyear").val("");
            $("#semester").val("");
            $("#examKey").val("");
            $("#uKeyId").val("");
            $("#gradeCode").val("");
            $("#subjectNameRela").hide();
            $("#messul").show();
	    	return;
	    }
	    var uKeyId=examIdUkey[examKeyId];
	    $.ajax({
            url:'${request.contextPath}/exammanage/sysncy/findExamSubjectList',
            data: {'acadyear':searchAcadyear,'semester':searchSemester,'examKey':examKeyId,'uKeyId':uKeyId},
            type:'post',
            success:function(data) {
            	$("#subjectNameRela").find("tbody").html('');
                var jsonO = JSON.parse(data);
                if(!jsonO.success){
                   //失败
                   makeMess("查询考试科目列表",false,jsonO.msg);
                   $("#subjectNameRela").hide();
               	   $("#messul").show();
                }
                else{
                   //显示科目
                   $("#acadyear").val(searchAcadyear);
                   $("#semester").val(searchSemester);
                   $("#examKey").val(examKeyId);
                   $("#uKeyId").val(uKeyId);
                   $("#gradeCode").val(jsonO.gradeCode);
                   var courseList=jsonO.courseList;
                   var subjectDtoList=jsonO.subjectDtoList;
                   makeTable(courseList,subjectDtoList);
                   $("#subjectNameRela").show();
               	   $("#messul").hide();
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
	}
	
	function makeTable(courseList,subjectDtoList){
		for(var gg=0;gg<subjectDtoList.length;gg++ ){
			var courTd="";
			if(subjectDtoList[gg].relaSubjectId){
				courTd=makeSelectCourse(courseList,subjectDtoList[gg].relaSubjectId,gg);
			}else{
				courTd=makeSelectCourse(courseList,"",gg);
			}
			addRow(subjectDtoList[gg].subjectName,gg,courTd);
		}
	}
	function addRow(subName,myIndex,coureTd){
		var hh='<tr><td class="text-center"><input type="hidden" value="'+subName+'" name="dtoList['+myIndex+'].subjectName" >'+subName+'</td>'
			+'<td class="text-center">'+coureTd+'</td>'
			+'<td class="text-center"><a href="javascript:" class="deleRowSub">移除</a></td></tr>';
		$("#subjectNameRela").find("tbody").append(hh);
	}
	function makeSelectCourse(courseList,subId,myIndex){
		var selText='<select class="form-control " style="width:300px;" name="dtoList['+myIndex+'].relaSubjectId" >'
		+'<option value="">请选择对应科目</option>';
		for(var gg=0;gg<courseList.length;gg++ ){
			if(subId==courseList[gg].id){
				selText=selText+'<option value="'+courseList[gg].id+'"  selected="selected">'+courseList[gg].subjectName+'</option>';
			}else{
				selText=selText+'<option value="'+courseList[gg].id+'">'+courseList[gg].subjectName+'</option>';
			}
		}								
		selText=selText+'</select>';
		return selText;
	}
	
	var isSysncy=false;
	function sysncyExam(){
		if(isSysncy){
			return;
		}
		isSysncy=true;
		var trNum=$("#subjectNameRela").find("tbody").find("tr").length;
		if(trNum==0){
			layer.tips('需要同步的科目不能为空!', $("#sysncySub"), {
				time:1000,
				tipsMore: true,
				tips: 2
			});
			isSysncy=false;
			return;
		}
		var f=true;
		$("#subjectNameRela").find("tbody").find("tr").each(function(){
			var subId=$(this).find("select").val();
			if(subId==""){
				layer.tips('不能为空!', $(this).find("select"), {
					time:1000,
					tipsMore: true,
					tips: 2
				});
				f=false;
				return false;
			}
		});
		if(!f){
			isSysncy=false;
			return;
		}
	    var examName=$("#examKeyId option:selected").text();
	    var options = {
			url : "${request.contextPath}/exammanage/sysncy/saveSysncy",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
                if(!jsonO.success){
                   //失败
                   makeMess(examName,false,jsonO.msg);
                   layerTipMsg(jsonO.success,"同步失败",jsonO.msg);
                }
                else{
                	$("#subjectNameRela").hide();
               	    $("#messul").show();
                	makeMess(examName,true,"同步成功");
                }
                isSysncy=false;
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#mySysncyForm").ajaxSubmit(options);
	}
	
	function showRonghouExam(){
		 var searchAcadyear = $('#searchAcadyear').val();
	     var searchSemester = $('#searchSemester').val();
		 $.ajax({
            url:'${request.contextPath}/exammanage/sysncy/findExamList',
            data: {'acadyear':searchAcadyear,'semester':searchSemester},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(!jsonO.success){
                   //失败
                   $("#errorDiv").append(jsonO.msg);
                }
                else{
                   var examList=jsonO.examList;
                   if(examList.length==0){
                   		 $("#examKeyId").html('');
                   		 $("#examKeyId").append('<option value="">--暂无考试--</option>');
                   }else{
                   		$("#examKeyId").html('');
                   		var henltext='<option value="">--请选择--</option>';
                   		for(var ii=0;ii<examList.length;ii++){
                   			henltext=henltext+'<option value="'+examList[ii].examKey+'">'+examList[ii].examName+'</option>';
                   		}
                   		$("#examKeyId").html(henltext);
                   }
                   
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
		 
	}
	
	function backExamList(){
		var url =  '${request.contextPath}/exammanage/examInfo/index/page';
		$("#exammanageDiv").load(url);
	}
	
	
	
	function makeMess(examName,flag,mess){
		var date = new Date();
		var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var time1=+date.getFullYear() + seperator1 + month + seperator1 + strDate;
	    var hours=date.getHours();
	    if(hours>=1 && hours<=9){
	    	hours="0"+hours;
	    }
	    var minutes=date.getMinutes();
	    if(minutes>=0 && minutes<=9){
	    	minutes="0"+minutes;
	    }
	    var seconds=date.getSeconds();
	    if(seconds>=0 && seconds<=9){
	    	seconds="0"+seconds;
	    }
	    var time2=hours + seperator2 + minutes + seperator2 + seconds;
	    var $html='<table width="100%" class="box-line mt10" ><tr>';
	    if(flag){
	    	$html=$html+'<td  width="30%"><span><i class="fa fa-check-circle color-green">【'+examName+'】'+time2+'</span></td>';
	    }else{
	    	 $html=$html+'<td  width="30%" ><span class="color-red"><i class="fa fa-times-circle">【'+examName+'】'+time2+'</td>';
	    }
    	$html= $html+'<td >'+mess+'</td><td class="text-center" width="160px;"><a href="javascript:" class="delMyRow">删除</a></td></tr></table>';
	    
	  	$("#messul").append($html);
		
		
	}
</script>


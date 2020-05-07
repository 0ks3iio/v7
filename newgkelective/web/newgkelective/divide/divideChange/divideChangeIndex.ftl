<div class="shift-search-box">
	<div class="shift-search-title">学生调班</div>
	<div class="shift-search-body">
		<select class="form-control radius-4" id="search_type" style="width:75px;float: left;border-bottom-right-radius: 0;border-top-right-radius: 0;">
	      <option value="1">学号</option>
	      <option value="2">姓名</option>
	    </select>
	    <div class="shift-search-input">
	      <div class="input-group">
	        <input type="text" id="search_value" class="form-control" />
	        <a href="#"  class="input-group-addon" hidefocus="true" onclick="findStudentId()">
	       	 	<img src="${request.contextPath}/static/images/icons/search_gray_16x16.png"/>
	        </a>
	      </div>
	    </div>
	    <button class="btn btn-link btn-info js-add" style="float: right;">添加学生</button>
	</div>
</div>
<div id="studentTab" style="dispay:none">
	<ul class="nav nav-tabs nav-tabs-1" role="tablist">
	</ul>
</div>
<div class="shift-detail" id="class_div" style="dispay:none">
  
</div>
<div class="layer layer-add layer-addedit">
	<div class="layer-content">
       <div class="shift-layer-search clearfix" id="addSearch">
          <span class="shift-layer-label">原行政班：</span>
          <select class="form-control radius-4" style="width:130px;float: left;" id="chooseClazzid" onChange="findClassStudentList()">
          	<#if classList?exists && classList?size gt 0>
          		<#list classList as item>
          			<option value="${item.id!}">${item.className!}</option>
          		</#list>
            <#else>
            	<option value="">未找到班级</option>
            </#if>
          </select>
          <span class="shift-layer-label" style="margin-left:20px;">学生：</span>
          <select class="form-control radius-4" id="chooseStudentId" style="width:130px;float: left;">
            <option value="">未找到学生</option>
          </select>
      </div>
   	  <div class="shift-layer-body addSubject" id="addSubject">
   	 	
   	 	<#if courseNameMap?exists && categoryList?exists && categoryList?size gt 0>
		<#list categoryList as category>
			<div class="shift-layer-title">
	       		 ${category.categoryName!}：<img src="${request.contextPath}/static/images/7choose3/icon-warning.png" width="14" height="14" style="margin-left: 8px;"/>
	        	<span class="curricula-detail-tip">
	          		&nbsp;最多选${category.maxNum?default(0)}组/门，最少选${category.minNum?default(0)}组/门
	        	</span>
	     	</div>
	     	<div class="shift-layer-conntent publish-course publish-course-made" style="padding: 10px 0 10px 0;" data-num="${category.maxNum?default(0)}">
	          	<#if category.courseList?exists && category.courseList?size gt 0>
					<#list category.courseList as item>
						<span course-id="${item.id!}">${item.subjectName!}</span>
					</#list>
				</#if>
	          	<#if category.courseCombination?exists && category.courseCombination?size gt 0>
					<#list category.courseCombination as item>
						<span course-id="${item.courseList[0].id!},${item.courseList[1].id!}">${item.courseList[0].subjectName!},${item.courseList[1].subjectName!}</span>
					</#list>
				</#if>
         	</div>
		</#list>
		</#if>
    	</div>
        <div class="text-right">
          <button class="btn btn-default margin-r-10 js-cancel btn-sm"> 取消</button>
          <button class="btn btn-blue  btn-sm js-confirm" onclick="addconfirm()">开始分班</button>
        </div>
 	 </div>
</div> 

<div class="layer layer-edit layer-addedit">
  	<div class="layer-content">
	    <div class="shift-layer-body editSubject" id="editSubject">
   	 	
   	 	<#if courseNameMap?exists && categoryList?exists && categoryList?size gt 0>
		<#list categoryList as category>
			<div class="shift-layer-title">
	       		 ${category.categoryName!}：<img src="${request.contextPath}/static/images/7choose3/icon-warning.png" width="14" height="14" style="margin-left: 8px;"/>
	        	<span class="curricula-detail-tip">
	          		&nbsp;最多选${category.maxNum?default(0)}组/门，最少选${category.minNum?default(0)}组/门
	        	</span>
	     	</div>
	     	<div class="shift-layer-conntent publish-course publish-course-made" style="padding: 10px 0 10px 0;" data-num="${category.maxNum?default(0)}">
	          	<#if category.courseList?exists && category.courseList?size gt 0>
					<#list category.courseList as item>
						<span course-id="${item.id!}">${item.subjectName!}</span>
					</#list>
				</#if>
	          	<#if category.courseCombination?exists && category.courseCombination?size gt 0>
					<#list category.courseCombination as item>
						<span course-id="${item.courseList[0].id!},${item.courseList[1].id!}">${item.courseList[0].subjectName!},${item.courseList[1].subjectName!}</span>
					</#list>
				</#if>
         	</div>
		</#list>
		</#if>
    	</div>
	    <div class="text-right">
	      <button class="btn btn-default margin-r-10 js-cancel btn-sm">取消</button>
	      <button class="btn btn-blue js-confirm btn-sm" onclick="editconfirm()">确定</button>
	    </div>
 	</div>
</div>   
<script>
var stuClassMap={};

function findStudentId(){
	var search_type=$("#search_type").val();
	var search_value=$("#search_value").val();
	if(search_value=="" || $.trim(search_value)==""){
		$("#search_value").val("");
		layer.tips('请输入', $("#search_value"), {
			tipsMore: true,
			tips:3				
		});
		return;
	}
	$.ajax({
		url:"${request.contextPath}/newgkelective/studentAdjust/${divideId!}/findStudentIds",
		data:{"searchType":search_type,"searchValue":search_value},
		dataType: "json",
		success: function(data){
			if(data.length>0){
				$("#studentTab").find(".nav-tabs-1").html("");
				if(data.length==1){
					$("#studentTab").hide();
					$("#class_div").css({"margin-top": "0px"});
				}else{
					var htmlText='';
					for(var i=0;i<data.length;i++){
						if(i==0){
							htmlText=htmlText+'<li role="presentation" class="active"><a href="javascript:void(0)"  data-stuid="'+data[i].id+'" onclick="findItemByStuId(this)" role="tab" data-toggle="tab">'+data[i].studentName+'</a></li>';
						}else{
							htmlText=htmlText+'<li role="presentation"><a href="javascript:void(0)"  data-stuid="'+data[i].id+'" onclick="findItemByStuId(this)" role="tab" data-toggle="tab">'+data[i].studentName+'</a></li>';
						}
					}
					$("#studentTab").find(".nav-tabs-1").html(htmlText);
					$("#studentTab").show();
					$("#class_div").css({"margin-top": "10px"});
				}
				$("#class_div").show();
				loadStuItem(data[0].id);
				
			}else{
				$("#class_div").hide();
				$("#studentTab").hide();
				layer.tips('未找到学生', $("#search_value"), {
					tipsMore: true,
					tips:3				
				});
			}
		}
	});
}

function findItemByStuId(obj){
	var stuId=$(obj).attr("data-stuid");
	loadStuItem(stuId);
}

function loadStuItem(stuId,subjectIds){
	if(!subjectIds){
		subjectIds="";
	}
	var url="${request.contextPath}/newgkelective/studentAdjust/${divideId!}/loadStuItem?studentId="+stuId+"&subjectIds="+subjectIds;
	$("#class_div").load(url);
}

function hideAll(){
	$("#class_div").hide();
	$("#studentTab").hide();
}

var firstAddClassid="";
var isConfirm=false;
$(function() {
  	//隐藏班级
  	hideAll();
  	//添加学生
    $(".js-add").on("click", function(e) {
	    e.preventDefault();
	    if(firstAddClassid!=""){
	    	$("#chooseClazzid").val(firstAddClassid);
	    }else{
	    	firstAddClassid=$("#chooseClazzid").val();
	    }
	    findClassStudentList();
	    //取消前次选中
	    $(".layer-add .publish-course span").removeClass("active").removeClass("disabled");
	    layer.open({
        	type: 1,
        	shadow: 0.5,
        	title: "添加学生",
        	area: "500px",
        	content: $(".layer-add")
	    });
    });
  	
  	//新增，或者修改 弹出框设置
  	$(".js-cancel").on("click", function() {
  		isConfirm=false;
     	$(".layui-layer-close").trigger("click");
    });
    

	//切换spanjs
    $(".layer-addedit .publish-course span").on("click", function(e) {
	      e.preventDefault();
	      if ($(this).hasClass("disabled")) return;
	      
	      if ($(this).hasClass("active")) {
	        	$(this).removeClass("active");
	      } else {
	         $(this).addClass("active");
	      }
	     if( $(this).parent().parent().hasClass("addSubject")){
	     	addOrEditSpanNum("addSubject");
	     }else{
	     	addOrEditSpanNum("editSubject");
	     }
	      
    });

});

function addOrEditSpanNum(keyDiv){
	//已经选中的科目
	var activelength=$("#"+keyDiv+" .publish-course-made").find("span.active").length;
	var chooseArr={};
	if(activelength>0){
		var activeSpan=$("#"+keyDiv+" .publish-course-made").find("span.active");
		for(var s=0;s<activeSpan.length;s++){
			if(Object.keys(chooseArr).length==3){
				$(activeSpan[s]).removeClass("active");
			}else{
					var ids=$(activeSpan[s]).attr("course-id");
					if(ids.indexOf(",")>-1){
						var ss=ids.split(",");
						var sameFlag=false;
						for(var k=0;k<ss.length;k++){
							if(chooseArr[(ss[k])]){
								sameFlag=true;
								break;
							}
						}
						if(sameFlag){
							$(activeSpan[s]).removeClass("active");
						}else{
							if((Object.keys(chooseArr).length+ss.length)>3){
								$(activeSpan[s]).removeClass("active");
							}else{
								for(var k=0;k<ss.length;k++){
									chooseArr[(ss[k])]=(ss[k]);
								}
							}
							
						}
					}else{
						if(chooseArr[ids]){
							//错误交互直接清除
							$(activeSpan[s]).removeClass("active");
						}else{
							chooseArr[ids]=ids;
						}
					}
			}
		}
	}
	var chooselength=Object.keys(chooseArr).length;
	$("#"+keyDiv+" .publish-course-made").find("span").removeClass("disabled");
	if(chooselength>=3){
		$("#"+keyDiv+" .publish-course-made").find("span:not('.active')").addClass("disabled");
	}else{
		$("#"+keyDiv+" .publish-course-made").each(function(){
			var makeThatObj=$(this);
			var maxnum = $(this).parent().attr("data-num");
	        if ($(this).find("span.active").length >= maxnum) {
	         	$(this).find("span:not('.active')").addClass("disabled");
	        }else{
	        	$(this).find("span").each(function(){
	        		if(!$(this).hasClass("active")){
	        				var ids=$(this).attr("course-id");
			        		if(ids.indexOf(",")>-1){
			        			var cc=ids.split(",");
			        			if((chooselength+cc.length)>3){
			        				$(this).addClass("disabled");
			        			}else{
			        				var sameFlag=false;
									for(var k=0;k<cc.length;k++){
										if(chooseArr[(cc[k])]){
											sameFlag=true;
											break;
										}
									}
									if(sameFlag){
										$(this).addClass("disabled");
									}
			        			}
			        		}else{
			        			if(chooseArr[ids]){
			        				$(this).addClass("disabled");
			        			}
			        		}
	        		
	        		}

	        	})
	        }
		})
	}
}

//打开修改
var oldIds="";
var sId="";
function editItem(studentId,oldSubjectIds){
	  sId=studentId;
	  oldIds=oldSubjectIds;
	  $("#editSubject .publish-course-made").find("span").removeClass("active").removeClass("disabled");
      if(oldSubjectIds!=""){
      	 var arr=oldSubjectIds.split(",");
      	 var mmap={};
      	 for(var i=0;i<arr.length;i++){
      	 	mmap[(arr[i])]=arr[i]
      	}

      	$("#editSubject .publish-course-made").find("span").each(function(){
      		var ids=$(this).attr("course-id");
      		if(ids.indexOf(",")>-1){
      			var ss=ids.split(",");
      			var flag=false;
      			for(var k=0;k<ss.length;k++){
					if(!mmap[(ss[k])]){
						flag=true;
						break;
					}
				}
				if(!flag){
					$(this).addClass("active");
				}
      		}else{
      			if(mmap[ids]){
					$(this).addClass("active");	
				}
      		}
      	})
      }
      addOrEditSpanNum("editSubject");
      layer.open({
        type: 1,
        shadow: 0.5,
        area: "500px",
        title: false,
        content: $(".layer-edit")
     });
}


//修改中确定
function editconfirm(){
	if(isConfirm){
		return;
	}
	console.log(111);
	var chooseList=makeChooseSubjects("editSubject");
	if(chooseList.length!=3){
		layer.tips('请选3门科目', $("#editSubject"), {
			tipsMore: true,
			tips:3				
		});
		isConfirm=false;
		return;
	}
	
	//是不是进行修改
	var isUpdate=false;
	if(oldIds==""){
		isUpdate=true;
	}else{
		for(var m=0;m<chooseList.length;m++){
			if(oldIds.indexOf(chooseList[m])>-1){
				
			}else{
				isUpdate=true;
				break;
			}
		}
	}
	if(!isUpdate){
		//直接关闭
		isConfirm=false;
		$(".layui-layer-close").trigger("click");
		return;
	}
	var newChooseIds="";
	for(var m=0;m<chooseList.length;m++){
		newChooseIds=newChooseIds+","+chooseList[m];
	}
	newChooseIds=newChooseIds.substring(1);
	checkSubjectIds(sId,newChooseIds,"editSubject");
}

function addconfirm(){
	if(isConfirm){
		return;
	}
	isConfirm=true;
	var studentId=$("#chooseStudentId").val();
	if(studentId==""){
		layer.tips('请选一个学生', $("#studentId"), {
			tipsMore: true,
			tips:3				
		});
		isConfirm=false;
		return;
	}
	var chooseList=makeChooseSubjects("addSubject");
	
	if(chooseList.length!=3){
		layer.tips('请选3门科目', $("#addSubject"), {
			tipsMore: true,
			tips:3				
		});
		isConfirm=false;
		return;
	}
	var newChooseIds="";
	for(var m=0;m<chooseList.length;m++){
		newChooseIds=newChooseIds+","+chooseList[m];
	}
	newChooseIds=newChooseIds.substring(1);
	checkSubjectIds(studentId,newChooseIds,"addSubject");
}

function makeChooseSubjects(keyDiv){
	var activelength=$("#"+keyDiv+" .publish-course-made").find("span.active").length;
	var chooseList=new Array();
	if(activelength>0){
		var activeSpan=$("#"+keyDiv+" .publish-course-made").find("span.active");
		for(var s=0;s<activeSpan.length;s++){
			var ids=$(activeSpan[s]).attr("course-id");
			if(ids.indexOf(",")>-1){
				var ss=ids.split(",");
				for(var k=0;k<ss.length;k++){
					chooseList.push(ss[k]);
				}
			}else{
				chooseList.push(ids);
			}
		}
	}
	return chooseList;
}

function checkSubjectIds(studentId,chooseSubjectIds,keySubject){
	$.ajax({
		url:"${request.contextPath}/newgkelective/studentAdjust/${divideId!}/checkStudentChoose",
		data:{"subjectIds":chooseSubjectIds},
		dataType: "json",
		success: function(data){
			if(data.success){
				isConfirm=false;
				layer.closeAll();
				$("#class_div").show();
				loadStuItem(studentId,chooseSubjectIds);
			}else{
				layer.tips(data.msg, $("#"+keySubject), {
					tipsMore: true,
					tips:3				
				});
				isConfirm=false;
			}
		}
	});
}

var classStudentListMap={};
function findClassStudentList(){
	var chooseClazzid=$("#chooseClazzid").val();
	$("#chooseStudentId").html("");
	if(chooseClazzid==""){
		return;
	}
	if(classStudentListMap[chooseClazzid]){
		makeStudentOption(classStudentListMap[chooseClazzid]);
	}else{
		$.ajax({
			url:"${request.contextPath}/newgkelective/studentAdjust/${divideId!}/findStudentList",
			data:{"clazzId":chooseClazzid},
			dataType: "json",
			success: function(data){
				if(data.length>0){
					classStudentListMap[chooseClazzid]=data;
				}
				makeStudentOption(data);
			}
		});
	}
}

function makeStudentOption(sList){
	if(sList.length>0){
		var optionHtml="";
		for(var i=0;i<sList.length;i++){
			optionHtml=optionHtml+'<option value="'+sList[i].studentId+'">'+sList[i].studentName+'</option>';
		}
		$("#chooseStudentId").html(optionHtml);
	}else{
		$("#chooseStudentId").html('<option value="">未找到学生</option>');
	}
}


</script>


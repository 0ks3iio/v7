<!--教学班学生编辑页面-->
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as treemacro>

<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>

<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />

<div class="row">
	<div class="win-box gradeset">
		<div class="win-header">
			<h4 class="win-title">步骤一：从行政班中选择学生</h4>
		</div>
		<div class="win-body clearfix">
		<div class="col-sm-3 col-lg-3">
			<@treemacro.gradeClassForSchoolInsetTree height="550" class="widget-color-blue2" click="onTreeClick">
				<div class="widget-header" style="padding:0;margin:0;">
					<input type="text" id="searchStudentName" nullable="false" name="studentname" class="form-control" placeholder="姓名">
				</div>
			</@treemacro.gradeClassForSchoolInsetTree>
		</div>
		<div class="col-sm-9 col-lg-9 studentListDiv " style="height:500px;">
			
		</div>
	</div>
	
</div>
<form id="comfirmForm">
<div class="row">
	<div class="win-box ">
		<div class="win-header col-lg-12 col-sm-12 col-xs-12 col-md-12">
			<h4 class="win-title">步骤二：确认学生名单 </h4>
		</div>
			<div class="win-body clearfix">
				<div class="col-lg-12 col-sm-12 col-xs-12 col-md-12 confirmStudentListDiv" >
				</div>
			</div>
		</div>
	</div>
</div>

<div class="page-btns">
	<a class="btn btn-darkblue" id="btn-confirm">确定</a>
	<a class="btn btn-darkblue" id="btn-cancel">取消</a>
</div>
</form>
<!-- page specific plugin scripts -->
<script type="text/javascript">

	var type = "1";
	var indexDiv = 0;
	
	//单位Id
	//年级Id
	var searchGradeId = "";
	//班级Id
	var searchClazzId = "";
	
	$(window).bind("resize",function(){
		resizeLayer(indexDiv, 1000, 350);
	});

	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		//加载树

		$("#btn-add-permission").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/permission/add/page?parentId=" + currentTreeId + "&subsystemCode=" + currentSubsystemCode, {height:350});
		});
		$("#btn-add-teacher").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/teacher/add/page");
		});

		$("#btn-show-role").on("click", function(){
			reloadRoleTree();
		});
		
		//输入框绑定时间
		$("#searchStudentName").bind("keypress",function(event){
			if(event.keyCode=='13'){
				doSearch();
			}
		});
		$("#searchStudentName").bind("blur",function(){
			doSearch();
		});
		
		var commit = false;
		//绑定保存和取消事件
		$("#btn-confirm").unbind("click").bind("click",function(){
			var ids = new Array();
			$(".confirmStuListTable").each(function(){
				$(this).find("td").each(function(){
					if( typeof($(this).attr("id"))!="undefined" && $(this).attr("id")!=''){
						ids.push($(this).attr("id"));
					}
				});
			});
			if(ids.length==0){
				//swal({title: "操作失败!",text:"没有待确认的学生",type: "error",showConfirmButton: true,confirmButtonText: "确定"});
		    	//return;
			}
			$.ajax({
			    url:'${request.contextPath}/basedata/teachclass/student/save?ids='+JSON.stringify(ids)+"&classId="+'${teachClassId!}',
			    type:'post',  
			    cache:false,  
			    contentType: "application/json",
			    success:function(data) {
			    	var jsonO = JSON.parse(data);
			 		if(!jsonO.success){
			 			swal({title: "操作失败!",
		    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
		    				$("#btn-confirm").removeClass("disabled");
		    			});
			 		}
			 		else{
			 			// 显示成功信息
			 			layer.tips(jsonO.msg, "#teachClass-commit", {tips: [4, '#228B22']});
			 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
			 			// 需要区分移动端和非移动端返回处理不一样
		 				doLayerOk("#btn-confirm", {
							redirect:function(){gotoHash("${request.contextPath}/basedata/teachclass/index/page")},
							window:function(){
								setTimeout(function(){layer.closeAll();}, 300);
								//$("#courseList").trigger("reloadGrid");
								gotoHash("${request.contextPath}/basedata/teachclass/index/page");
				 			}
			 			});				 			
	    			}
			     }
			});
		});
		
		$("#btn-cancel").unbind("click").bind("click",function(){
			gotoHash("${request.contextPath}/basedata/teachclass/index/page");
		});
	});
	
	var currentTreeId;
	var currentSubsystemCode;
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "class"){
			$(".studentListDiv").load("${request.contextPath}/basedata/teachclass/"+treeNode.id+"/"+type+"/student",function(){
				bindClickFunction();
			});
			currentTreeId = "";
			currentSubsystemCode = treeNode.id;
		}
		else if(treeNode.type == 'grade'){
			searchGradeId = treeNode.id;
		}
		else if(treeNode.type == 'class'){
			searchClazzId = treeNode.id;
		}
	}
	$(".confirmStudentListDiv").load("${request.contextPath}/basedata/teachclass/${teachClassId!}/2/student");
		
	$(".studentListDiv").load("${request.contextPath}/basedata/teachclass/1/1/student",function(){bindClickFunction();});
	
	function doSearch(){
	
		if(searchClazzId==''){
				searchClazzId='99';
			}
			if(searchGradeId==''){
				searchGradeId='99';
			}
			var studentName = $("#searchStudentName").val();
			//这里的回调方法应该提取出来
			$(".studentListDiv").load(encodeURI("${request.contextPath}/basedata/teachclass/"+searchClazzId+"/1/student?studentName="+studentName+"&gradeId="+searchGradeId),function(){
				bindClickFunction();
			});
	}
	
	
	/////可以在每次新增的时候使用Map映射，在删除和重组的时候可以简单些
	function bindClickFunction(){
		$(".cbx_td").each(function(){
			$(this).find("span").unbind("click").bind("click",function(){
				var studentId = $(this).attr("id");
				var studentName = $(this).attr("studentName");
				var clazzId = $(this).attr("classid");
				var clazzName = $(this).attr("classname");
				var chck = $(this).attr("chk");
				var isDel = chck=="true"?true:false;
				//检查是新增还是删除
				var operation = "";
				if(!isDel){
					doAddStudent(studentName,studentId,clazzName);
					//设置chk
					$(this).attr("chk","true");
				}else{
					doDeleteStudent(studentId);
					$(this).attr("chk","false");
				}
			});
		});
	}
	
	//计算所有等待确认的学生数，为分组准备
	function countAllConfirmStus(){
		var size = 0;
		$(".confirmStuListTable").each(function(){
			if(!$(this).parent().hasClass("confirm-student-list-none")){
				//计算
				var eSize = $(this).find("tr").size();
				if(eSize != 1){
					size = size + eSize - 1;
				}
			}		
		});
		return size;
	}
	
	//新增的div对应的序号
	function nextDivSize(){
		var maxSize = 0;
		$(".confirmStuListTable").each(function(){
			if(!$(this).parent().hasClass("confirm-student-list-none")){
				var nowSize = parseInt($(this).parent().attr("triggerId").replace("d-",""));
				if(maxSize < nowSize){
					maxSize = nowSize;
				}
			}
		});
		return maxSize + 1;
	}
	
	var maxTableSize = 24;
	var maxRow=6;
	//新增
	function doAddStudent(studentName,studentId,clazzName){
		
		var divSize = -1;
		var allSize = countAllConfirmStus();
		if(allSize<maxTableSize){
			if(allSize>=4){
				var modCols=allSize%4;//平均余数
				if(modCols==0){
					//在第1个div上新增	
					divSize = 1;
				}else if(modCols==1){
					//在第2个div上新增	
					divSize = 2;
				}else if(modCols==2){
					//在第3个div上新增	
					divSize = 3;
				}else if(modCols==3){
					//在第4个div上新增	
					divSize = 4;
				}
			}else{
				//新增Table 新增
				divSize = -1;
			}
		}else{
			//大于
			var tablesCount=parseInt(allSize/maxRow);//table个数
			var tabMod=allSize%maxRow;//
			if(tabMod==0){
				//新增Table 新增
				divSize = -1;
			}else{
				//在最后一个div上新增	
				divSize=tablesCount+1;
			}
			console.log(divSize);
		}
		var isAdd = false;
		if(isAdd){
			return;
		}
		isAdd=true;
		if(divSize==-1){
			//先新增table 再新增
			var newSize = doAddTable();
			$(".confirmStuListTable").each(function(){
				if(!$(this).parent().hasClass("confirm-student-list-none")){
				if($(this).parent().attr("triggerId") == "d-"+newSize){
					doAddTr(studentName,studentId,clazzName,$(this));
					isAdd = false;
				}
				}
			});
		}else{
			$(".confirmStuListTable").each(function(){
				if(!$(this).parent().hasClass("confirm-student-list-none")){
					if($(this).parent().attr("triggerId") == "d-"+divSize){
						doAddTr(studentName,studentId,clazzName,$(this));
						isAdd = false;
					}
				}
			});
		}
	}
	//删除
	function doDeleteStudent(studentId){
		$(".confirmStuListTable").each(function(){
			if(!$(this).parent().hasClass("confirm-student-list-none")){
				var divSize = parseInt($(this).parent("div").attr("triggerId").replace("d-",""));//移除的是哪个table
				var obj = $(this);
				$(this).find("td").each(function(){
					if(typeof($(this).attr("id"))!="undefined" && $(this).attr("id")==studentId){
						$(this).parent().remove();
						reSettable3(divSize,obj);
						reSet4(); 
					}
				});
			}
		});
		//级联问题
		
	}
	//重置table
	function reSettable(size,obj){
		//检查所有数量，是否持平
		var lastDivSize = nextDivSize()-1;
		if(size == lastDivSize){
			//do nothing??
			if($(obj).find("tr").size()==1){
				$(obj).parent("div").remove();
			}
		}
		else{
			//比较前后
			if(size !=1){
				var befoeSize =  countSizeOftable(size-1);
			}
			var nextSize =  countSizeOftable(size+1);
			
			//向后比较
			if(nextSize>$(obj).find("tr").size()-1){
				//将后一个的数据往前一个table移动
				$(".confirmStuListTable").each(function(){
					if(!$(this).parent().hasClass("confirm-student-list-none")){
						if($(this).parent().attr("triggerId")=="d-"+(size+1)){
							$(obj).append($(this).find("tr:last").clone(true));
							$(this).find("tr:last").remove();
							reSettable(size+1,$(this))
						}
					}
				});
			}
			//向前比较
		}
	}
	
	function copyProp2(srcSize,obj){
		var srcTable;
		var objTable;
		$(".confirmStuListTable").each(function(){
			if(!$(this).parent().hasClass("confirm-student-list-none")){
			  if($(this).parent().attr("triggerId")=="d-"+srcSize){
				srcTable=$(this);
			  }
			  if($(this).parent().attr("triggerId")=="d-"+obj){
				objTable=$(this);
			  }
			}
		});
		
		$(objTable).append($(srcTable).find("tr:last").clone(true));
		$(srcTable).find("tr:last").remove();
		
	}
	
	function reSet4(){
		//如果最后一个table为空 那么删除
		$(".confirmStuListTable").each(function(){
			if(!$(this).parent().hasClass("confirm-student-list-none")){
				if($(this).parent().attr("triggerId")=="d-"+(nextDivSize()-1)){
				if($(this).find("tr").size()==1){
					$(this).parent().remove();
				}
				
			 }
			}
		});
	}
	
	function reSettable3(size,obj){
		//所有学生数
		var allcounts = countAllConfirmStus();
		var data=[];
		var tableOldIndex=nextDivSize()-1;//原来table数量
		if(tableOldIndex<4){
			//向后
			for(var x=size;x<tableOldIndex;x++){
				//将后面最后一个复制到前面
				copyProp2(x+1,x);
			}
			return;
		}else if(tableOldIndex==4){
			var rows=parseInt(allcounts/tableOldIndex);
			var yrows=allcounts%tableOldIndex;
			for (var s=0;s<4;s++){
				if(s<yrows){
					data[s]=rows+1;
				}else{
					data[s]=rows;
				}
			}
			var rowcounts1=countSizeOftable(size);//改table学生的数量
			if(rowcounts1==data[size-1]){
				//不需要操作
				return;
			}
			//向后
			for(var m=size;m<4;m++){
				//将后面最后一个复制到前面
				var rowcounts3=countSizeOftable(m);
				if(rowcounts3==data[m-1]){
					break;
				}
				copyProp2(m+1,m);
			}
			var rowcounts2=countSizeOftable(4);
			if(rowcounts2==data[3]){
				//不需要操作
				return;
			}
			//向前
			for(var k=1;k<size;k++){
				if(k==1){
					//将第一个的最后一个复制到第四个
					copyProp2(k,4);
				}else{
					var rowcounts4=countSizeOftable(k-1);
					if(rowcounts4==data[k-2]){
						break;
					}
					//将后面最后一个复制到前面
					copyProp2(k,k-1);
				}
			}
			return;
		}else{
			if(size<5){
				//先进行
				for(var d=0;d<4;d++){
					data[d]=6;//每一table6行
				}
				//向后
				for(var m=size;m<4;m++){
					//将后面最后一个复制到前面
					copyProp2(m+1,m);
				}
				for(var f=5;f<=tableOldIndex;f++){
					//将后面最后一个复制到前面
					copyProp2(f,f-1);
				}
				return;
			}else{
				for(var f=size;f<tableOldIndex;f++){
					//将后面最后一个复制到前面
					copyProp2(f+1,f);
				}
				return;
			}
		}
		
	}
	
	
	function countSizeOftable(size){
		var count = -1;
		var isOk = false;
		$(".confirmStuListTable").each(function(){
			if(!$(this).parent().hasClass("confirm-student-list-none")){
				if(!isOk && $(this).parent().attr("triggerId")=="d-"+size){
					count = $(this).find("tr").size()-1;
					isOk = true; 
				}
			}
		});
		return count;
	}
	
	function doAddTr(studentName,studentId,clazzName,obj){
		var isAdd = false;
		var rhtml = "<tr>"+
					"<td id=\'"+studentId+"\'>"+studentName+"</td>"+
					"<td>"+clazzName+"</td>"+
					"<td><div class=\"action-buttons\"><a class=\"red\" href=\"javascript:;\" onclick=\"doDelete(\'"+studentId+"\')\"><i class=\"fa fa-trash bigger-130\"></i></a></div></td>"+
					"</tr>";
		$(obj).append(rhtml);
	}
	
	function doAddTable(){
		var isClone = false;
		var show = false;
		var divSize = nextDivSize();
		$(".confirm-student-list-none").each(function(){
			if(isClone){
				return divSize;
			}
			$(".confirmStudentListDiv").append($(this).attr("tem_id","tempId").attr("triggerId","d-"+divSize).clone(true));
			isClone = true;
			return divSize;
		});
		if(isClone){
			$(".confirm-student-list-none").each(function(){
				if(show){
					return divSize;
				}
				$(this).removeClass("confirm-student-list-none").show();
				show = true;
			});
		}
		return divSize;
	}
	
	
	function doDelete(id){
		var isClick = false;
		$(".cbx_td").each(function(){
			if($(this).find("span").attr("id")==id){
				$(this).find("span").click();
				isClick = true;
			}
		});
		if(!isClick){
			doDeleteStudent(id);
		}
	}
</script>

<div class="shift-detail">
	<div class="shift-save-box">
   		<button class="btn btn-blue" onclick="saveAll()">保存</button>
  	</div>
  	<div class="shift-basic-box">
  		<input type="hidden" id="chooseIds" value="${stuDto.chooseSubjects!}">
  		<table class="table table-bordered">
  			<tbody>
  				<tr>
  					<td class="shift-row-title">姓名</td>
			        <td>
			            <span>${stuDto.studentName!}</span>
			            <button class="btn btn-link btn-info" style="float: right;padding-top:0;padding-bottom:0;" onclick="deleteStudent()">删除</button>
			        </td>
			        <td class="shift-row-title">学号</td>
			        <td>${stuDto.studentCode!}</td>
			        <td class="shift-row-title">性别</td>
			        <td>${stuDto.sex!}</td>
			        <td class="shift-row-title">新行政班</td>
			        <td>${stuDto.className!}</td> 
		        </tr>
		        <tr>
		          	<td class="shift-row-title choose-subject-title">选科</td>
		         	<td>
		         		<#if stuDto.choResultStr?default('')!=''>
		         			<#list stuDto.choResultStr?split(" ") as sub>
		         			<span class="shift-basic-lable">${sub!}</span>
			            	</#list>
		         		</#if>
			            <button class="btn btn-link btn-info js-edit" style="float: right;padding-top:0;padding-bottom:0;">修改</button>
		          	</td>
		          	<td class="shift-row-title">各科成绩</td>
		         	 <td colspan="5">${stuDto.mark!}</td>
		        </tr>
	      </tbody>
	    </table>
  	</div>

  	<div class="shift-module-box xzb-module-item">
    	<div class="shift-module-title">行政班</div>
    	<div class="shift-module-body">
   		</div>
   		<div class="no-data-container xzbNoData" style="display:none">
			<div class="no-data" style="margin:10px;" >
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
				<div class="no-data-body">
					<p class="no-data-txt">暂无可选行政班</p>
				</div>
			</div>
		</div>
  	</div>
  	<div class="shift-module-box zhb-module-item">
    	<div class="shift-module-title">组合班</div>
    	<div class="shift-module-body">
   		</div>
   		<div class="no-data-container zhbNoData" style="display:none">
			<div class="no-data" style="margin:10px;" >
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
				<div class="no-data-body">
					<p class="no-data-txt">暂无可选组合班</p>
				</div>
			</div>
		</div>
  	</div>
</div>
                  
<script>
	var showAllTipMap={};
	var classDataMap={};
	function findClassIds(){
		var subjectIds=$("#chooseIds").val();
		if(subjectIds==""){
			return;
		}
		$.ajax({
			url:"${request.contextPath}/newgkelective/studentAdjust/${divideId!}/loadClassItem",
			data:{"studentId":"${stuDto.studentId!}","subjectIds":subjectIds},
			dataType: "json",
			success: function(data){
				var xzbList=data.xzbList;
				var zhbList=data.zhbList;
				makeTable(xzbList,"1");
				makeTable(zhbList,"2");
				var oldClassIds=data.oldClassIds;
				//根据结果默认先选中原来的
				if(oldClassIds!=""){
					var oldArr=oldClassIds.split(",");
					for(var j=0;j<oldArr.length;j++){
						$(".xzb-module-item").find(".shift-module-item[data-clazzid='"+oldArr[j]+"']").addClass("active");
						$(".zhb-module-item").find(".shift-module-item[data-clazzid='"+oldArr[j]+"']").addClass("active");
					}
					chooseXzb();
				}
			}
		});
	}
	//初始化显示
	function makeTable(dataList,type){
		var keyItemClass="";
		if(type=="1"){
			keyItemClass="xzb-module-item";
		}else if(type=="2"){
			keyItemClass="zhb-module-item";
		}else{
			return;
		}
		$("."+keyItemClass).find(".shift-module-body").html("");
		var hh="";
		if(dataList && dataList.length>0){
			if(type=="1"){
				$(".xzbNoData").hide();
			}else{
				$(".zhbNoData").hide();
			}
			for(var ii=0;ii<dataList.length;ii++){
				hh=hh+'<div class="shift-module-item" data-clazzId="'+dataList[ii].id+'"><div class="module-item-title">'
				+dataList[ii].name+'<span>('+dataList[ii].stuNum+')</span></div>';
				if(dataList[ii].subjectANames && dataList[ii].subjectANames!=""){
					hh=hh+'<span>选:'+dataList[ii].subjectANames+'</span><span style="margin-left:10px;">';
				}else{
					hh=hh+'<span>选:无</span><span style="margin-left:10px;">';
				}
				if(dataList[ii].subjectBNames && dataList[ii].subjectBNames!=""){
					hh=hh+'学:'+dataList[ii].subjectBNames+'</span><span>';
				}else{
					hh=hh+'学:无</span><span>';
				}
	      		hh=hh+'</div>';
	      		
	      		//缓存班级信息
	      		classDataMap[dataList[ii].id]=dataList[ii];
			}
		}else{
			if(type=="1"){
				$(".xzbNoData").show();
			}else{
				$(".zhbNoData").show();
			}
			
		}
		$("."+keyItemClass).find(".shift-module-body").html(hh);
	}
	
	function chooseXzb(){
		//如果选中的3科组合
		var activeXzb=$(".xzb-module-item").find(".shift-module-item.active");
		$(".zhb-module-item").find(".shift-module-item.disabled").removeClass("disabled");
	 	if(activeXzb && $(activeXzb).length>0){
	 		var activeId=$(activeXzb).attr("data-clazzid");
	 		if(classDataMap[activeId].subjectType && classDataMap[activeId].subjectType=="3"){
	 			$(".zhb-module-item").find(".shift-module-item").addClass("disabled");
	 		}
	 	}	
	}
	
	function showOneClassTip(xzbOrZhbId){
		if(showAllTipMap[xzbOrZhbId]){
			return showAllTipMap[xzbOrZhbId];
		}
		var data=classDataMap[xzbOrZhbId];
		var htm="";
		if(data){
			 htm='<div class="module-model-body"><div class="module-model-title">平均成绩</div>'
		        +'<table class="table  table-bordered"><tbody><tr><td class="shift-row-title">人数</td><td colspan="3">总：'+data.stuNum+'、男：'+data.boyNum+'、女：'+data.girlNum+'</td></tr>';
		     if(data.scoreList.length>1){
		     	for(var g=0;g<data.scoreList.length;g++){
			     	var dd=(data.scoreList)[g];
			     	//最后一位
			     	if(g==data.scoreList.length-1 && g%2==0){
			     		 htm=htm+'<tr><td class="shift-row-title">'+dd[0]+'</td><td colspan="3">'+dd[1]+'</td></tr>';
			     	}else{
			     		if(g%2==0){
			     			htm=htm+'<tr><td class="shift-row-title">'+dd[0]+'</td><td>'+dd[1]+'</td>';
			     		}else{
			     			htm=htm+'<td class="shift-row-title">'+dd[0]+'</td><td>'+dd[1]+'</td></tr>';
			     		}
			     	}
			     } 
		     }else{
		     	var scoreList=data.scoreList;
		     	var dd=scoreList[0];
		     	htm=htm+'<tr><td class="shift-row-title">'+dd[0]+'</td><td colspan="3">'+dd[1]+'</td></tr>';
		     }  
		     htm=htm+'</tbody></table></div>' ; 
		     
		     showAllTipMap[xzbOrZhbId]=htm;
		}else{
			htm="未找到详情";
		}
		
		return htm;
	}
	
  	$(function() {
  	
  		//修改科目
	    $(".js-edit").on("click", function(e) {
		      e.preventDefault();
		     editItem("${stuDto.studentId!}","${stuDto.chooseSubjects?default('')}");
	    });

  	
  		findClassIds();

	   //选择行政班
	   $(".xzb-module-item").on("click",".shift-module-item:not(.disabled)",function(e) {
	     	 e.preventDefault();
	     	 $(this).siblings().removeClass("active");
	     	 if ($(this).hasClass("active")) {
	       		  $(this).removeClass("active");
	     	 } else {
	      		  $(this).addClass("active");
	     	 }
	     	 chooseXzb();
	    });
	    $(".zhb-module-item").on("click",".shift-module-item:not(.disabled)",function(e) {
	     	 e.preventDefault();
	     	 $(this).siblings().removeClass("active");
	     	 if ($(this).hasClass("active")) {
	       		  $(this).removeClass("active");
	     	 } else {
	      		  $(this).addClass("active");
	     	 }
	    });
	    
	    //鼠标移动到行政班 显示内容
	    //上+默认
	    $(".shift-detail").on("mouseenter", ".shift-module-item", function() {
		     var chooseId=$(this).attr("data-clazzId");
		     var tt=showOneClassTip(chooseId);
		     //绑定鼠标进入事件
		     layer.tips(
		       	tt ,this,{tips: [1, "#fff"],area: 300}
		     );
	   	 });
	   	 
    	//移开
    	$(".shift-detail").on("mouseleave", ".shift-module-item", function() {
     		 layer.closeAll("tips");
   		});
    
  });
  

  
  var isSave=false;
  function saveAll(){
  	  if(isSave){
  	  	  return;
  	  }
  	  isSave=true;
  	  var subjectIds=$("#chooseIds").val();
  	  //获取具体班级
  	  var activeXzbNum=$(".xzb-module-item").find(".shift-module-item.active").length;
  	  if(activeXzbNum!=1){
  	  	 layer.tips('请选择一个行政班', $(".xzb-module-item").find(".shift-module-title"), {
			tipsMore: true,
			tips:3				
		});
		isSave=false;
		return;
  	  }
  	  var xzbId=$(".xzb-module-item").find(".shift-module-item.active").attr("data-clazzid");
  	  var classIds=xzbId;
  	  if(classDataMap[xzbId].subjectType && classDataMap[xzbId].subjectType=="3"){
  	  		var activeZhbNum=$(".zhb-module-item").find(".shift-module-item.active").length;
	 	 	if(activeZhbNum!=0){
		  	  	 layer.tips('已选行政班是三科组合班级，无需选中其他组合班', $(".zhb-module-item").find(".shift-module-title"), {
					tipsMore: true,
					tips:3				
				});
				isSave=false;
				return;
	  	 	 }
  	  }else{
  	  	  var activeZhbNum=$(".zhb-module-item").find(".shift-module-item.active").length;
	 	  if(activeZhbNum!=1){
	  	  	 layer.tips('请选择一个组合班', $(".zhb-module-item").find(".shift-module-title"), {
				tipsMore: true,
				tips:3				
			});
			isSave=false;
			return;
	  	  }
	  	  classIds=classIds+","+$(".zhb-module-item").find(".shift-module-item.active").attr("data-clazzid");
  	  }
 	  
  	  $.ajax({
			url:"${request.contextPath}/newgkelective/studentAdjust/${divideId}/saveStudentClass",
			data:{"studentId":"${stuDto.studentId!}","subjectIds":subjectIds,"classIds":classIds},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg("操作成功", {
						offset: 't',
						time: 2000
					});
					showContent('stuchange');
				}else{
					layerTipMsg(data.success,"失败","原因："+data.msg);
				}
				isSave=false;
			}
		});
  }
  
  
  
  var isDeleted=false;
  function deleteStudent(){
	if(isDeleted){
		return ;
	}
	isDeleted=true;
	layer.confirm('确定删除吗？', function(index){
		var ii = layer.load();
		$.ajax({
			url:"${request.contextPath}/newgkelective/studentAdjust/${divideId!}/deleteStudentId",
				data:{"studentId":"${stuDto.studentId!}"},
				dataType: "json",
				success: function(data){
					layer.closeAll();
					if(data.success){
						layer.msg("删除成功", {
							offset: 't',
							time: 2000
						});
						showContent('stuchange');
					}else{
						layerTipMsg(data.success,"失败","原因："+data.msg);
					}
					isDeleted=false;
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});
		
		},function(index){
			isDeleted=false;
		});
 }
  
 
</script>


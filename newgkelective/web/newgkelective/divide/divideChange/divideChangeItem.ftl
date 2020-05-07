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
	<#if anum gt 0>
 	<div class="shift-module-box jxb-module-item" id="jxbtable-a">
	    <div class="shift-module-title">选考教学班</div>
    	<table class="table table-bordered shift-module-table">
	      	<thead>
	       	 	<tr>
	          		<th>选考科目</th>
	          		<#list 1..anum as i>
	          		<th>选考${i}</th>
	          		</#list>
	        	</tr>
	      	</thead>
	      	<tbody> 
	      	</tbody>
   	 	</table>
  	</div>
  	<#else>
  		<div class="shift-module-box jxb-module-item" id="jxbtable-a">
  		</div>
  	</#if>
	<#if bnum gt 0>
 	<div class="shift-module-box jxb-module-item" id="jxbtable-b">
	    <div class="shift-module-title"> 学考教学班</div>
    	<table class="table table-bordered shift-module-table">
	      	<thead>
	       	 	<tr>
	          		<th>学考科目</th>
	          		<#list 1..bnum as i>
	          		<th>学考${i}</th>
	          		</#list>
	        	</tr>
	      	</thead>
	      	<tbody> 
	      	</tbody>
   	 	</table>
  	</div>
  	<#else>
  		<div class="shift-module-box jxb-module-item" id="jxbtable-b">
  		</div>
  	</#if>
</div>
                  
<script>
	var showXzbTipMap={};
	var showJxbTipMap={};
	var xzbMap={};
	var jxbMap={};
	var zhbMap={};
	var needASub;
	var needBSub;
	var pushTimeList=new Array();
	<#if anum gt 0>
		<#list 1..anum as nn>
			pushTimeList.push("A${nn}");
		</#list>
	</#if>
	<#if bnum gt 0>
		<#list 1..bnum as nn>
			pushTimeList.push("B${nn}");
		</#list>
	</#if>
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
				if(data.arrangeBath=="0"){
					//不需要安排批次--doto
				}else{
					var subAList=data.subA;
					var subBList=data.subB;
					var subjectBathAMap=data.subjectBathAMap;
					var subjectBathBMap=data.subjectBathBMap;
					var xzbList=data.xzbList;
					var oldClassIds=data.oldClassIds;
					makeTable("jxbtable-a",subAList,"A",subjectBathAMap);
					makeTable("jxbtable-b",subBList,"B",subjectBathBMap);
					makeXzbTable(xzbList);
					needASub=subAList;
					needBSub=subBList;
					//根据结果默认先选中原来的
					if(oldClassIds!=""){
						var oldArr=oldClassIds.split(",");
						for(var j=0;j<oldArr.length;j++){
							$(".xzb-module-item").find(".shift-module-item[data-clazzid='"+oldArr[j]+"']").addClass("active");
							$(".jxb-module-item").find("span[data-jxbid='"+oldArr[j]+"']").addClass("active");
						}
						chooseXzb();
					}
				}
			}
		});
	}
	//初始化显示
	function makeXzbTable(xzbList){
		$(".xzb-module-item").find(".shift-module-body").html("");
		var hh="";
		if(xzbList && xzbList.length>0){
			$(".xzbNoData").hide();
			for(var ii=0;ii<xzbList.length;ii++){
				hh=hh+'<div class="shift-module-item" data-clazzId="'+xzbList[ii].id+'"><div class="module-item-title">'
				+xzbList[ii].name+'<span>('+xzbList[ii].stuNum+')</span></div>';
				if(xzbList[ii].subjectANames && xzbList[ii].subjectANames!=""){
					hh=hh+'<span>选:'+xzbList[ii].subjectANames+'</span><span style="margin-left:10px;">';
				}else{
					hh=hh+'<span>选:无</span><span style="margin-left:10px;">';
				}
				if(xzbList[ii].subjectBNames && xzbList[ii].subjectBNames!=""){
					hh=hh+'学:'+xzbList[ii].subjectBNames+'</span><span>';
				}else{
					hh=hh+'学:无</span><span>';
				}
	      		hh=hh+'</div>';
	      		
	      		//缓存班级信息
	      		xzbMap[xzbList[ii].id]=xzbList[ii];
			}
			$(".xzb-module-item").find(".shift-module-body").html(hh);
		}else{
			$(".xzbNoData").show();
		}
	}
	
	function makeTable(tableId,subList,type,subjectBathMap){
		if(subList && subList.length>0){
			$("#"+tableId).find("table tbody").html("");
			var htmlTest='';
			for(var i=0;i<subList.length;i++){
				if(type=="A"){
					htmlTest=htmlTest+makeTrTextA(subList[i],subjectBathMap);
				}else{
					htmlTest=htmlTest+makeTrTextB(subList[i],subjectBathMap);
				}
			}
			$("#"+tableId).find("table tbody").html(htmlTest);
		}else{
			$("#"+tableId).find("table tbody").html("");
		}
	}
	
	function makeTrTextA(data,subjectBathMap){
		var subKey=data.courseId;
		var rr='<tr data-subjectId="'+subKey+'"><td>'+data.courseName+'</td>';
			<#if anum gt 0>
				<#list 1..anum as nn>
					var tt="";
					if(subjectBathMap && subjectBathMap[subKey+"_${nn}"]){
						var valueList=subjectBathMap[subKey+"_${nn}"];
						for(var ii=0;ii<valueList.length;ii++){
							tt=tt+'<span data-jxbId="'+valueList[ii].id+'">'+valueList[ii].name+'<i>('+valueList[ii].stuNum+')</i></span>';
							jxbMap[valueList[ii].id]=valueList[ii];
						}
					}
					rr=rr+'<td class="module-table-td" data-time="time_A${nn}">'+tt+'</td>';
				</#list>
			</#if>
		
		rr=rr+'</tr>';
		return rr;
	}
	function makeTrTextB(data,subjectBathMap){
		var subKey=data.courseId;
		var rr='<tr data-subjectId="'+subKey+'"><td>'+data.courseName+'</td>'
			<#if bnum gt 0>
				<#list 1..bnum as nn>
					var tt="";
					if(subjectBathMap && subjectBathMap[subKey+"_${nn}"]){
						var valueList=subjectBathMap[subKey+"_${nn}"];
						for(var ii=0;ii<valueList.length;ii++){
							tt=tt+'<span data-jxbId="'+valueList[ii].id+'">'+valueList[ii].name+'<i>('+valueList[ii].stuNum+')</i></span>';
							jxbMap[valueList[ii].id]=valueList[ii];
						}
					}
					rr=rr+'<td class="module-table-td" data-time="time_B${nn}">'+tt+'</td>';
				</#list>
			</#if>
		
		rr=rr+'</tr>';
		return rr;
	}
	
	function showXzbTip(clazzId){
		if(showXzbTipMap[clazzId]){
			return showXzbTipMap[clazzId];
		}
		var data=xzbMap[clazzId];
		var htm="";
		if(data){
			 htm='<div class="module-model-body"><div class="module-model-title">平均成绩</div>'
		        +'<table class="table  table-bordered"><tbody><tr><td class="shift-row-title">人数</td><td colspan="3">总：'+data.stuNum+'、男：'+data.boyNum+'、女：'+data.girlNum+'</td></tr>';
		     if(data.showBetterA && data.showBetterA!=""){
		     	htm=htm+'<tr><td class="shift-row-title">选考优先</td><td colspan="3">'+data.showBetterA+'</td></tr>';
		     }
		     if(data.showBetterB && data.showBetterB!=""){
		     	htm=htm+'<tr><td class="shift-row-title">学考优先</td><td colspan="3">'+data.showBetterB+'</td></tr>';
		     }
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
		     showXzbTipMap[clazzId]=htm;
		}else{
			htm="未找到详情";
		}
		
		return htm;
	}
	function showJxbTip(jxbId){
		if(showJxbTipMap[jxbId]){
			return showJxbTipMap[jxbId];
		}
		var data=jxbMap[jxbId];
		var htm="";
		if(data){
			var dd=(data.scoreList)[0];
			var htm='<div class="module-model-body"><div class="module-model-title">平均成绩</div>'
				+'<table class="table  table-bordered"><tbody><tr><td class="shift-row-title">人数</td><td colspan="3">总：'+data.stuNum+'、男：'+data.boyNum+'、女：'+data.girlNum+'</td></tr>'
				+'<tr><td class="shift-row-title">'+dd[0]+'</td><td>'+dd[1]+'</td></tr></tbody></table></div>';
	       showJxbTipMap[jxbId]=htm;
	    }else{
	     	htm="未找到详情";
	    }
		return htm;
	}
	
	function chooseXzb(){
		//选中的行政班id
	 	var activeXzb=$(".xzb-module-item").find(".shift-module-item.active");
	 	//清除所有置灰行以及列
	 	$(".jxb-module-item").find("table").find("tr").removeClass("module-table-disabled");
	 	$(".jxb-module-item").find("table").find("td").removeClass("module-disabled-td");
	 	if(activeXzb && $(activeXzb).length>0){
	 		var xzbId=$(activeXzb).attr("data-clazzid");
	 		var xzbData=xzbMap[xzbId];
	 		//行政班科目行对应加上module-table-disabled 同时对应td span取消对应class
	 		if(xzbData.subjectAIds && xzbData.subjectAIds.length>0){
	 			var kk=xzbData.subjectAIds;
	 			for(var i=0;i<kk.length;i++){
	 				var $tr=$("#jxbtable-a").find("table").find("tr[data-subjectId='"+kk[i]+"']");
	 				//去除所有td状态
	 				$($tr).find("td").removeClass("module-disabled-td");
	 				//去除span
	 				$($tr).find("td span").removeClass("active");
	 				$($tr).addClass("module-table-disabled");
	 			}
	 		}
	 		if(xzbData.subjectBIds && xzbData.subjectBIds.length>0){
	 			var kk=xzbData.subjectBIds;
	 			for(var i=0;i<kk.length;i++){
	 				var $tr=$("#jxbtable-b").find("table").find("tr[data-subjectId='"+kk[i]+"']");
	 				//去除所有td状态
	 				$($tr).find("td").removeClass("module-disabled-td");
	 				//去除span
	 				$($tr).find("td span").removeClass("active");
	 				$($tr).addClass("module-table-disabled");
	 			}
	 		}
	 		//根据关联性科目--取消其他选中，留下关联班级默认选中
	 		if(xzbData.zhbId && xzbData.zhbId!=""){
	 			//找到跟其关联的教学班
	 			var relaList=zhbMap[xzbData.zhbId];
	 			if(!relaList){
	 				relaList=new Array();
					for(var key in jxbMap){
						if(jxbMap[key].zhbId && jxbMap[key].zhbId!="" && jxbMap[key].zhbId==xzbData.zhbId){
							relaList.push(jxbMap[key]);
						 }
					}
				   zhbMap[xzbData.zhbId]=relaList;
	 			}
	 			if(relaList.length>0){
	 				for(var ii=0;ii<relaList.length;ii++){
	 					var jxbD=relaList[ii];
	 					var $tr=$(".jxb-module-item").find("tr[data-subjectId='"+jxbD.subjectId+"']");
	 					$($tr).find("span").removeClass("active");
	 					var $span=$($tr).find("span[data-jxbId='"+jxbD.id+"']");
 						$($span).addClass("active");
	 				}
	 			}
	 		}
	 		//不能安排批次的批次点td 增加module-disabled-td
	 		if(xzbData.arrangeBath && xzbData.arrangeBath.length>0){
	 			for(var k=0;k<pushTimeList.length;k++){
	 				var nnn=false;
	 				for(var j=0;j<xzbData.arrangeBath.length;j++){
	 					if((xzbData.arrangeBath)[j]==pushTimeList[k]){
	 						nnn=true;
	 						break;
	 					}
	 				}
	 				if(!nnn){
	 					$("td[data-time='time_"+pushTimeList[k]+"']").find("span").remove("active");
	 					$("td[data-time='time_"+pushTimeList[k]+"']").addClass("module-disabled-td");
	 				}
	 			}
	 			
	 		}
	 		
	 	}
		makeJxbSpanItem("jxbtable-a");
		makeJxbSpanItem("jxbtable-b");
	}
	
	function makeJxbSpanItem(jxbKey){
		//根据选中情况置灰同列td
		var jxbNum=$("#"+jxbKey).find("span.active").length;
	  	var jxbIds=new Array();
	  	if(jxbNum>0){
	  	  	 $("#"+jxbKey).find("span.active").each(function(){
	  	  	  	var choseTd=$(this).parent("td");
	  	  	  	//同行 同列module-disabled-td
	  	  	  	var timeBath=$(choseTd).attr("data-time");
	  	  	  	//同列
	  	  	  	$("td[data-time='"+timeBath+"']:not('.module-disabled-td')").addClass("module-disabled-td");
	  	  	  	//同行
	  	  	  	$(choseTd).siblings("td").addClass("module-disabled-td");
	  	  	  	$(choseTd).removeClass("module-disabled-td");
	  	  	 })
	  	}
	}

  	$(function() {
  	
  		//修改科目
	    $(".js-edit").on("click", function(e) {
		      e.preventDefault();
		     editItem("${stuDto.studentId!}","${stuDto.chooseSubjects?default('')}");
	    });

  	
  		findClassIds();

	   //选择行政班
	   $(".xzb-module-item").on("click",".shift-module-item",function(e) {
	     	 e.preventDefault();
	     	 $(this).siblings().removeClass("active");
	     	 if ($(this).hasClass("active")) {
	       		  $(this).removeClass("active");
	     	 } else {
	      		  $(this).addClass("active");
	     	 }
	     	 chooseXzb();
	    });
	    
	    //鼠标移动到行政班 显示内容
	    //上+默认
	    $(".shift-detail").on("mouseenter", ".shift-module-item", function() {
		     var chooseXzbId=$(this).attr("data-clazzId");
		     var tt=showXzbTip(chooseXzbId);
		     //绑定鼠标进入事件
		     layer.tips(
		       		tt ,this,{tips: [1, "#fff"],area: 300}
		     );
	   	 });
    	//移开
    	$(".shift-detail").on("mouseleave", ".shift-module-item", function() {
     		 layer.closeAll("tips");
   		});
   
	    //选择教学班
	    $(".jxb-module-item").on("click",
	    	".shift-module-table tr:not('.module-table-disabled') td:not('.module-disabled-td') span",
	 			 function() {
	 			 	//同td清除选中
			        $(this).siblings().removeClass("active");
			        if ($(this).hasClass("active")) {
			         	 $(this).removeClass("active");
			        } else {
			          $(this).addClass("active");
			          //同行选中要清除
			           $(this).parents("td").siblings("td").find("span.active").removeClass("active");
			        }
	 				$(".jxb-module-item").find("table").find("td").removeClass("module-disabled-td");
			        makeJxbSpanItem("jxbtable-a");
			        makeJxbSpanItem("jxbtable-b");
		 		}
		);

    	//教学班提示
	    //上+默认
	    $(".shift-detail").on("mouseenter",".module-table-td span",
		      function() {
		        //绑定鼠标进入事件
		         var chooseJxbId=$(this).attr("data-jxbId");
		         var tt=showJxbTip(chooseJxbId);
		        layer.tips(
		         	tt,
		         	this,{	tips: [1, "#fff"],area: 300 }
		        );
		      }
	    );

	    $(".shift-detail").on("mouseleave",".module-table-td span",function() {
	        layer.closeAll("tips");
	      }
	    );
	    
	    $(".shift-detail").on("click", ".module-table-disabled", function() {
		      layer.msg(
		        "该科目已在行政班开设，不需要走班",
		        {
		          icon: 0,
		          offset: "t",
		          time: 3000, //2秒关闭（如果不配置，默认是3秒）
		          area: 500
		        },
		        function() {
		          //关闭后操作
		        }
		      );
	    });

	    $(".shift-detail").on("click", ".module-disabled-td", function() {
	    	if($(this).hasClass("module-table-td")){
	    		layer.msg(
		        "该时间点已有上课科目，请在其他时间点选择教学班",
		        {
		          icon: 0,
		          offset: "t",
		          area: 500,
		          time: 3000 //2秒关闭（如果不配置，默认是3秒）
		        },
		        function() {
		          //关闭后操作
		        }
		      );
	    	}
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
  	  var allClassIds=xzbId;
  	  var jxbANum=$("#jxbtable-a").find("span.active").length;
  	  var jxbAIds=new Array();
  	  if(jxbANum>0){
  	  	  $("#jxbtable-a").find("span.active").each(function(){
  	  	  		jxbAIds.push($(this).attr("data-jxbid"));
  	  	  })
  	  }
  	  var jxbBNum=$("#jxbtable-b").find("span.active").length;
  	  var jxbBIds=new Array();
  	  if(jxbBNum>0){
  	  	  $("#jxbtable-b").find("span.active").each(function(){
  	  	  		jxbBIds.push($(this).attr("data-jxbid"));
  	  	  })
  	  }
  	  var aId={};
  	  var bId={};
  	  var xzbData=xzbMap[xzbId];
  	  if(xzbData.subjectAIds && xzbData.subjectAIds.length>0){
			var kk=xzbData.subjectAIds;
			for(var i=0;i<kk.length;i++){
				aId[(kk[i])]=kk[i];
			}
	  }
	  if(xzbData.subjectBIds && xzbData.subjectBIds.length>0){
			var kk=xzbData.subjectBIds;
			for(var i=0;i<kk.length;i++){
				bId[(kk[i])]=kk[i];
			}
	  }
	  var sameIdA=new Array();
  	  if(jxbANum>0){
  	  	 for(var j=0;j<jxbAIds.length;j++){
  	  		 allClassIds=allClassIds+","+jxbAIds[j];
  	  	 	 var jxbData=jxbMap[(jxbAIds[j])];
  	  	 	 if(aId[(jxbData.subjectId)]){
  	  	 		 sameIdA.add(jxbData.subjectId);
  	  	 	 }else{
  	  	 		 aId[(jxbData.subjectId)]=jxbData.subjectId;
  	  	 	 }
  	  	 }
  	  }
  	  var sameIdB=new Array();
  	  if(jxbBNum>0){
  	  	 for(var j=0;j<jxbBIds.length;j++){
  	  	 	allClassIds=allClassIds+","+jxbBIds[j];
  	  	 	var jxbData=jxbMap[(jxbBIds[j])];
  	  	 	if(bId[(jxbData.subjectId)]){
  	  	 		sameIdB.add(jxbData.subjectId);
  	  	 	}else{
  	  	 		bId[(jxbData.subjectId)]=jxbData.subjectId;
  	  	 	}
  	  	 }
  	  }
  	  if(sameIdA.length>0){
  	  	layer.tips('同科目选中多个班级，包括以行政班上课', $("#jxbtable-a").find(".shift-module-title"), {
			tipsMore: true,
			tips:3				
		});
		isSave=false;
		return;
  	  }
  	  
  	  if(sameIdB.length>0){
  	  	layer.tips('同科目选中多个班级，包括以行政班上课', $("#jxbtable-b").find(".shift-module-title"), {
			tipsMore: true,
			tips:3				
		});
		isSave=false;
		return;
  	  }
  	  if(needASub && needASub.length>0){
			for(var i=0;i<needASub.length;i++){
				if(!aId[(needASub[i].courseId)]){
					layer.tips(needASub[i].courseName+'未选择相应班级', $("#jxbtable-a").find("tr[data-subjectId='"+needASub[i].courseId+"']"), {
						tipsMore: true,
						tips:3				
					});
					isSave=false;
					return;
				}
			}
	  }
  	  if(needBSub && needBSub.length>0){
			for(var i=0;i<needBSub.length;i++){
				if(!bId[(needBSub[i].courseId)]){
					layer.tips(needBSub[i].courseName+'未选择相应班级', $("#jxbtable-b").find("tr[data-subjectId='"+needBSub[i].courseId+"']"), {
						tipsMore: true,
						tips:3				
					});
					isSave=false;
					return;
				}
			}
	  }
  	  $.ajax({
			url:"${request.contextPath}/newgkelective/studentAdjust/${divideId}/saveStudentClass",
			data:{"studentId":"${stuDto.studentId!}","subjectIds":subjectIds,"classIds":allClassIds},
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


<style>
.publish-course-children span{
	margin:0 10px 0 0;
}
span.ui-sortable-handle{
	width:120px;
}
.publish-course span{
	min-width:90px;
	width:auto;
}
</style>
<script src="${request.contextPath}/static/components/jquery-ui/jquery-ui.min.js"></script>
<script src="${request.contextPath}/static/sortable/Sortable.min.js"></script>
<script type="text/javascript" >
	var isSubmit=false;
    function savePlaceArrangeItemBath(){
       var divideId = '${divideId!}';
	   var arrayItemId = '${newGkArrayItem.id!}';
	   if(isSubmit){
	   		return;
	   }
	   isSubmit=true;
	   var flag = false;
	   //验证同批次下不能在同一个场地
	   //根据备注中的值可以判断不能放的批次
	   $('td.publish-course-children').each(function(){
	    	var placeId=$(this).attr("id");
	    	var $this=$(this);
	    	var noBath=$(this).attr("data-value");
	    	var objectIds="";
	    	var pBatch={};
	    	if($(this).find("span").length<0){
	    		return true;
	    	}
	    	$(this).find("span").each(function(){
	    		var classId=$(this).attr('pid');
	    		var batch=$(this).attr('data-value').trim();
	    		var batchName=makeName(batch);
	    		if(noBath!="" && noBath.indexOf(batch)>-1){
	    			//根据备注中的值可以判断不能放的批次
	    			layer.tips("不能存放"+batchName+"下的教学班",$this, {
						tipsMore: true,
						tips: 3
					});
	    			flag=true;
	    			return;
	    		}
	    		if(!pBatch[batch]){
	    			objectIds=objectIds+","+classId;
	    			pBatch[batch]=batch;
	    		}else{
	    			//提示   同一批次在同一地点
	    			layer.tips("存在多个"+batchName+"下的教学班",$this, {
						tipsMore: true,
						tips: 3
					});
	    			flag=true;
	    			return;
	    		}
	    	})
	    	if(flag){
	    		return;
	    	}
	    	if(objectIds!=""){
	    		objectIds=objectIds.substring(1);
	    	}
	    	$('#objectIds_'+placeId).val(objectIds);
        })
        if(flag){
        	isSubmit=false;
        	return;
        }
		var options = {
			url:"${request.contextPath}/newgkelective/'+divideId+'/placeArrange/saveByBath?arrayItemId="+arrayItemId,
			dataType : 'json',
			success : function(data){
		 		var jsonO = data;
			 	if(!jsonO.success){
			 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 		isSubmit=false;
			 		return;
			 	}else{
			 		layer.closeAll();
					layer.msg(jsonO.msg, {offset: 't',time: 2000});
	                var url1="${request.contextPath}/newgkelective/${divideId!}/placeArrange/listByBath?arrayItemId="+arrayItemId
	                		+"&arrayId=${arrayId!}&lessArrayId=${lessArrayId!}";
	                $("#showList").load(url1);
	    		}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#myForm").ajaxSubmit(options);
	  
	}
	
	function makeName(batch){
		var arr=batch.trim().split("_");
		if("A"==arr[0]){
			return "选考"+arr[1];
		}else{
			return "学考"+arr[1];
		}
	}
	
	
	function toBackItemList(){
		layer.confirm("返回前请确定页面数据是否保存，确定返回？", {
			btn: ["确定", "取消"]
		}, function(){
		    layer.closeAll();
		     
		     <#--
		     var divideId = '${divideId!}';
	  		 var url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/index/page?arrayId=${arrayId!}';
	   		 $("#showList").load(url);-->
	   		 
	   		 <#if arrayId?default('')==''>
				   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${newGkArrayItem.id!}';
					$("#showList").load(url);
			 <#else>
				   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
					$("#showList").load(url);
			 </#if>
	    })
	  
	}
	showBreadBack(toBackItemList, false, '教室方案');
	var isDelete=false;
	function deletePlaceArrangeItemBath(){
		if(isDelete){
			return;
		}
		isDelete=true;
		layer.confirm("确定要重新选择？", {
			btn: ["确定", "取消"]
		}, function(){
		    var url =  '${request.contextPath}/newgkelective/${divideId!}/placeArrange/deleteByBath?arrayItemId=${newGkArrayItem.id!}';
			$.ajax({
				url:url,
				dataType: "JSON",
				success: function(data){
					if(data.success){
						layer.closeAll();
						layer.msg(data.msg, {offset: 't',time: 2000});
		                var url1="${request.contextPath}/newgkelective/${divideId!}/placeArrange/listByBath?arrayItemId=${newGkArrayItem.id!}&lessArrayId=${lessArrayId!}";
		                $("#showList").load(url1);
					}else{
						isDelete=false;
						layerTipMsg(data.success,"失败",data.msg);
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});
	    }, function(){
				isDelete=false;
			}
	    )
	}
</script>


<form id="myForm">
   <#--  <a href="javascript:void(0);" onclick="toBackItemList();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a> -->
    <div class="box box-default">
    	<div class="box-header">
   			<h3 class="box-title">${newGkArrayItem.itemName!}</h3>
   			<div class="filter-item filter-item-right">
   				<a class="btn btn-blue" href="javascript:deletePlaceArrangeItemBath();">重新选择</a>
            	<a class="btn btn-blue" href="javascript:savePlaceArrangeItemBath();">保存</a>
            </div>
   		</div>
        <div class="box-body">
            <div class="tab-content active">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th <#if dtoList?exists && dtoList?size gt 0>width="30%"<#else>width="10%"</#if> id="th1">已选班级<span class="explain">（拖动至教学班）</span></th>
                        <th>可选场地</th>
                        <th>教学班</th>
                        <#if isShowGu>
                         	<th>默认教学班</th>
                        </#if>
                        <th width="20%">备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    
                    	<#if dtoList?exists && dtoList?size gt 0>
                    	<tr>
                    	<#list dtoList as dto>
                    		<#if dto_index==0>
                    			 <td rowspan="${dtoList?size}" id="td1">
                    				<div class="filter">
										<div class="filter-item">
											<div class="filter-content">
												<#if batchKeys?exists && batchKeys?size gt 0><a class="btn btn-sm btn-blue js-jxb-random">随机安排</a></#if>
											</div>
										</div>
									</div>
									
									<#if batchKeys?exists && batchKeys?size gt 0>
										<ul>
											<#list batchKeys as batchKey>
												<li>
													<h4>${batchKey[1]!}</h4>
													<#assign bathClass=bathClassMap[batchKey[0]]>
													<div class="publish-course publish-course-parent" id="${batchKey[0]!}" style="min-height: 10px;">
														<#if bathClass?exists && bathClass?size gt 0 >
															<#list bathClass as bathClassOne>
																<span class="${bathClassOne.subjectType!}_${bathClassOne.batch!}" pid="${bathClassOne.id!}" data-value="${bathClassOne.subjectType!}_${bathClassOne.batch!}">${bathClassOne.className!}</span>
															</#list>
														</#if>
													</div>
													
													<div class="filter filter_${batchKey[0]!}" <#if bathClass?exists && bathClass?size gt 0 >style="display: none;"</#if>>
														<div class="filter-item">
															<div class="filter-name">
																暂无未安排的教学班
															</div>
														</div>
													</div>
												</li>
											</#list>
										</ul>
									<#else>
										<div class="filter" style="display: none;">
											<div class="filter-item">
												<div class="filter-name">
													暂无未安排的教学班
												</div>
											</div>
										</div>
									</#if>
									
                        		</td>
                    		</#if>
                    	
                        	<td>
                        	<input type="hidden" name="newGkPlaceItemList1[${dto_index}].placeId" value="${dto.placeId!}">
                        	<input type="hidden" name="newGkPlaceItemList1[${dto_index}].objectIds" id="objectIds_${dto.placeId!}" value="">
                        		${dto.placeName!}
                        	</td>
                        	<td  class="publish-course publish-course-children ${dto.noCanArrangeBath!}" id="${dto.placeId!}" data-value="${dto.noCanArrangeBath!}">
                        	
                        	<#if  dto.jxbClass?exists && dto.jxbClass?size gt 0>
                        		<#list dto.jxbClass as jxbClass>
                        		<span class="${jxbClass.subjectType!}_${jxbClass.batch!}" pid="${jxbClass.id!}" data-value="${jxbClass.subjectType!}_${jxbClass.batch!}">${jxbClass.className!}
                                </span>
                        		</#list>
                    		</#if>
                        	</td>
                        	<#if isShowGu>
                         	<td>
                         		<#if dto.jxbClass2?exists && dto.jxbClass2?size gt 0>
                         			<#assign gudingIds="">
                         			<#list dto.jxbClass2 as jxbClass2>
                         				<#if jxbClass2_index==0>
                         					<#assign gudingIds=jxbClass2.id>
                         					${jxbClass2.className!}
                         				<#else>
                         					<#assign gudingIds=gudingIds+','+jxbClass2.id>
                         					、${jxbClass2.className!}
                         				</#if>
                         				
                         			</#list>
                         			<input type="hidden" name="newGkPlaceItemList1[${dto_index}].objectIds2" value="${gudingIds!}">
                         		</#if>
                         	</td>
                        	</#if>
                        	<td>${dto.remake!}</td>
                        	</tr>
                    	</#list>
                    	
                    	</#if>
                    </tbody>
                  </table>

            </div>
        </div>
        
    </div>
</form>
<script>
$(function(){
	//分区域
	//教学班分组
	<#if batchKeys?exists && batchKeys?size gt 0>
		<#list batchKeys as batchKey>
			var p_${batchKey[0]!} = document.getElementById("${batchKey[0]!}");
				Sortable.create(p_${batchKey[0]!}, 
				{
				    group: {name: 'one-group', pull: true, put: true},
				    sort: false,
				    onAdd: function(evt){
						var from = evt.from;
						var to = evt.to;
						var item = evt.item;
						
						var itemData=item.className;
						if(itemData!=""){
							itemData=itemData.trim();
						}
						if(itemData!=to.id){
							to.removeChild(item);
							from.appendChild(item);	
							layer.msg("移动的教学班不属于本区域", {offset: 't',time: 2000});
						}
						checkDisplay();
						return true;
					}
				});
		</#list>
	</#if>
	//场地
	<#if dtoList?exists && dtoList?size gt 0>         	
         <#list dtoList as dto>
         	var c_${dto.placeId!} = document.getElementById("${dto.placeId!}");
         	Sortable.create(c_${dto.placeId!},
				{
					group: {name: 'one-group', pull: true, put:true},
					sort: false,
					onAdd: function(evt){
						var from = evt.from;
						var to = evt.to;
						var item = evt.item;
						var toClassName=to.className;
						var itemClass=item.className;
						if(itemClass!=""){
							itemClass=itemClass.trim();
						}
						if(toClassName.indexOf(itemClass)>-1){
							to.removeChild(item);
							from.appendChild(item);
							layer.msg("本区域不能进入"+makeName(item.className)+"下的教学班", {offset: 't',time: 2000});
						}else{
							for(var i = 0; i <= to.children.length-1; i++){
								if(to.children[i]!=item && to.children[i].className.trim()==item.className.trim()){
									to.removeChild(item);
									from.appendChild(item);
									layer.msg("本区域已经存在"+makeName(item.className)+"下的教学班", {offset: 't',time: 2000});
									break;
								}
							}
						}
						checkDisplay();	
						return true;
					}
					
				});
         </#list>
     </#if>

				

    $('.js-jxb-random').click(function(){
    	//placeLen 剩余科目数  classLen 总共班级数
    	$(".publish-course-parent").each(function(){
    		var batch=$(this).attr("id");
    		var ll=$(this).find("span").length;
    		if(ll>0){
    			$(this).find("span").each(function(){
    				//找到可选场地中 已存在最少的班级
    				$td=findMintd(batch);
    				if($td==null){
    					return false;
    				}
					$td.append($(this).clone());
					$(this).remove();
    				
    			});
    		}
    		
    	})
    	checkDisplay();
    });
    
})

function findMintd(batch){
	var $return=null;
	var num=-1;
	var b=false;
	$('td.publish-course-children').each(function(){
		var $this=$(this);
		var noBatch=$(this).attr("data-value");
	 	if(noBatch.indexOf(batch)>-1){
	 		//不符合
	 	}else{
	 		var length=$(this).find("span").length;
	 		if(length==0){
	 			//可以放
	 			$return=$this;
	 			return false;
	 		}else{
	 			//如果已经有
	 			var f=false;
	 			$(this).find("span").each(function(){
	 				var spanData=$(this).attr("data-value");
	 				if(batch==spanData){
	 					f=true;
	 					return false;
	 				}
	 			})
	 			if(!f){
	 				//可以
	 				if(num==-1){
	 					num=length;
	 					$return=$this;
	 				}else if(length<num){
	 					num=length;
	 					$return=$this;
	 				}
	 			}
	 		}
	 	}
	});
	return $return;
}
    
function checkDisplay(){
	$(".publish-course-parent").each(function(){
		var id=$(this).attr("id");
		var thisLen = $(this).find('span').length;
        if(thisLen==0){
        	$('.filter_'+id).show();
        }else{
        	$('.filter_'+id).hide();
        }
	});
}
    

</script>
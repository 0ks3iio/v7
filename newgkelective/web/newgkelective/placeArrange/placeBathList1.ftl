<style>
	.tooltip{}
	.tooltip-inner{text-align: left;}
</style>
<form id="myForm">
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<a class="btn btn-blue js-random-jxb" href="javascript:">智能安排</a>
				<a class="btn btn-white" href="javascript:deletePlaceArrangeItemBath();">清空重排</a>
			</div>
			<div class="filter-item filter-item-right">
				<span class="mr20"><i class="fa fa-circle font-12 color-blue"></i> 表示选考班</span>
				<span><i class="fa fa-circle font-12 color-yellow"></i> 表示学考班</span>
			</div>
		</div>
		<div class="classroomArrangement clearfix">
			<div class="float-left" style="width: 40%;">
				<table class="table no-margin">
					<thead>
						<tr>
							<th>待分配班级</th>
						</tr>
					</thead>
				</table>	
				<div class="willDistribution">
					<#if batchKeys?exists && batchKeys?size gt 0>
						<#list batchKeys as batchKey>
							<div class="batch-class" id="${batchKey[1]!}_${batchKey[2]!}" >
								<p class="category"><span class="num <#if batchKey[1]?default('')=='A'>xuan<#else>xue</#if>">${batchKey[2]!}</span><strong>${batchKey[3]!}</strong></p>
								<#assign bathClass=bathClassMap[batchKey[0]]>
								<div class="clearfix category-itemList">
									<#if bathClass?exists && bathClass?size gt 0 >
									<#list bathClass as bathClassOne>
										<a class="item" href="javascript:" pid="${bathClassOne.id!}" data-value="${batchKey[1]!}_${batchKey[2]!}">
											<span class="del">&times;</span>
											<span class="huan">换</span>
											<span class="bj">
												<span class="num <#if batchKey[1]?default('')=='A'>xuan<#else>xue</#if>">${batchKey[2]!}</span>
												${bathClassOne.className!}
											</span>
											<span class="stu clearfix">
												<#assign stuCountByClassId=bathClassOne.stuCountByClassId>
												<#if stuCountByClassId?exists && stuCountByClassId?size gt 0>
													<#assign tt=''>
													<#assign classIdsStr=''>
													<#list stuCountByClassId as stuCount>
														<#if stuCount_index==0>
															<span class="float-left">生源:${stuCount[0]!}(${stuCount[1]!})</span>
															<#assign tt='生源：'+stuCount[0]+'('+stuCount[1]+')'>
															<#assign classIdsStr=stuCount[2]>
														<#else>
															<#assign tt=tt+'、'+stuCount[0]+'('+stuCount[1]+')'>
															<#assign classIdsStr=classIdsStr+'、'+stuCount[2]>
														</#if>
														
													</#list>
													<input type="hidden" class="firstClassId" value="${classIdsStr!}"/>
													<i class="float-right glyphicon glyphicon-option-horizontal mt6" data-toggle="tooltip" data-placement="bottom" title="${tt!}"></i>
												<#else>
												<span class="float-left">生源:未找到</span>
												</#if>
											</span>
										
										</a>
									</#list>
									<#else>
										<#--暂无未安排的教学班-->
										<p class='color-999 nodata-class'>暂无待分配的班级</p>
									</#if>
								</div>
							</div>
						</#list>
					</#if>
				</div>												
			</div>
			<div class="float-left" style="width: 60%;">
				<table class="table no-margin">
					<thead>
						<tr>
							<th width="5%"></th>
							<th width="15%">教室场地</th>
							<th width="60%">已分配班级</th>
							<th width="20%">备注</th>
							<th width=""></th>
						</tr>
					</thead>
				</table>	
				<div class="pastDistribution">
					<table class="table table-bordered" style="margin-top: -1px;margin-bottom: -1px;">
						<tbody>
						 	<#if dtoList?exists && dtoList?size gt 0>
                    		
	                    	<#list dtoList as dto>
	                    	<tr>
	                    		<td width="5%" class="arrow">
									<span class="glyphicon glyphicon-arrow-right"></span>
								</td>
								<td width="15%" class="text-center"> 
									<input type="hidden" name="newGkPlaceItemList1[${dto_index}].placeId" value="${dto.placeId!}">
                        			<input type="hidden" name="newGkPlaceItemList1[${dto_index}].objectIds" id="objectIds_${dto.placeId!}" value="">
									${dto.placeName!}<p class="color-999 font-12">(${dto.xzbClassName?default('备用教室')})</p>
								</td>
								<td width="60%" class="itemArea" id="${dto.placeId!}" data-value="${dto.noCanArrangeBath!}" data-class-id="${dto.xzbId!}">
									<#assign jxbClassList= dto.jxbClass>
									<#if jxbClassList?exists && jxbClassList?size gt 0>
		                        		<#list jxbClassList as jxbClass>
			                        		<a class="item" href="javascript:" pid="${jxbClass.id!}" data-value="${jxbClass.subjectType!}_${jxbClass.batch!}">
												<span class="del">&times;</span>
												<span class="huan">换</span>
												<span class="bj">
													<span class="num <#if jxbClass.subjectType?default('')=='A'>xuan<#else>xue</#if>">${jxbClass.batch!}</span>
													${jxbClass.className!}
												</span>
												<span class="stu clearfix">
													<#assign stuCountByClassId=jxbClass.stuCountByClassId>
													<#if stuCountByClassId?exists && stuCountByClassId?size gt 0>
														<#assign tt=''>
														<#assign classIdsStr=''>
														<#list stuCountByClassId as stuCount>
															<#if stuCount_index==0>
																<span class="float-left">生源:${stuCount[0]!}(${stuCount[1]!})</span>
																<#assign tt='生源：'+stuCount[0]+'('+stuCount[1]+')'>
																<#assign classIdsStr=stuCount[2]>
															<#else>
																<#assign tt=tt+'、'+stuCount[0]+'('+stuCount[1]+')'>
																<#assign classIdsStr=classIdsStr+'、'+stuCount[2]>
															</#if>
															
														</#list>
														<input type="hidden" class="firstClassId" value="${classIdsStr!}">
														<i class="float-right glyphicon glyphicon-option-horizontal mt6" data-toggle="tooltip" data-placement="bottom" title="${tt!}"></i>
													<#else>
													<span class="float-left">生源:未找到</span>
													</#if>
												</span>
											
											</a>
		                        		</#list>
		                    		</#if>
								</td>
								<td width="20%">${dto.remake!}</td>
							</tr>
	                    	</#list>
	                    	
                    	</#if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-white" href="javascript:beforePlaceList()">上一步</a>
	<a class="btn btn-blue" href="javascript:savePlaceArrangeItemBath();">保存</a>
</div>
<script type="text/javascript">
	function beforePlaceList(){
		var url='${request.contextPath}/newgkelective/${divideId!}/placeArrange/list?arrayItemId=${newGkArrayItem.id!}'
			+ '&lessArrayId=${lessArrayId!}&plArrayId=${newGkArrayItem.id!}&arrayId=${arrayId!}';
		$("#showList").load(url);
	}
	
	function findNoItem(){
    	$(".willDistribution").find(".nodata-class").remove();
    	$(".willDistribution").find(".category-itemList").each(function(){
    		console.log($(this).find(".item").length);
    		if(!$(this).find(".item").length){
    			$(this).append("<p class='color-999 nodata-class'>暂无待分配的班级</p>")
    		}
    	})
    }
	function toBackItemList(){
		layer.confirm("返回前请确定页面数据是否保存，确定返回？", {
			btn: ["确定", "取消"]
		}, function(){
		    layer.closeAll();
	   		 <#if arrayId?default('')==''>
				   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${newGkArrayItem.id!}';
					$("#showList").load(url);
			 <#else>
				   var url =  '${request.contextPath}/newgkelective/${arrayId!}/arraySet/pageIndex';
					$("#showList").load(url);
			 </#if>
	    })
	}
	<#--提示工具-->
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});
	<#--
	$(function(){
		
		$(".willDistribution").on('click','.item',function(){
			var $willnum = $(this).find(".num").text();
			var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
			var $willBath=($(this).find(".num").hasClass("xuan") ? "A" : "B")+"_"+$willnum;
			if($(this).hasClass("active")){
				$(this).removeClass("active")
				$(".pastDistribution .arrow").removeClass("active");
				$(".pastDistribution .item").removeClass("exchange");
			}else{
				$(".willDistribution .item").removeClass("active");
				$(this).addClass("active");
				$(".pastDistribution .arrow").removeClass("active");
				$(".pastDistribution .item").removeClass("exchange");
				$(".pastDistribution .itemArea").each(function(){
					var $children = $(this).children();
					var $length = $(this).children().length;
					var noBaths=$(this).attr("data-value");
					if(!noBaths){
						noBaths="";
					}
					if($length){
						if(noBaths.indexOf($willBath)==-1){
							$($children).each(function(){
								var $pastnum = $(this).find(".num").text();
								var $pasttype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
								if($willnum == $pastnum && $willtype == $pasttype){
									$(this).addClass("exchange");
								}
							});
							if(!$(this).find(".exchange").length){
								$(this).siblings(".arrow").addClass("active")
							}
						}
						
					}else{
						if(noBaths.indexOf($willBath)==-1){
							$(this).siblings(".arrow").addClass("active");
						}
					}
				});
			}		
		});
		
		$(".pastDistribution").on('click','.arrow',function(){
			var $itemArea = $(this).siblings(".itemArea");
			if($(this).hasClass("active")){
				$(".willDistribution .item").each(function(){
					if($(this).hasClass("active")){
						var $active = $(this);
						$itemArea.append($active); 
						$(".pastDistribution .arrow").removeClass("active");
						$(".pastDistribution .item").removeClass("exchange active");
					}
				});
			}
		});
		$(".pastDistribution").on('click','.item',function(){
			var $exchange = $(this);
			var $exchangeparent = $(this).parent();
			$(".willDistribution .item").each(function(){
				if($(this).hasClass("active")){
					var $active = $(this);
					var $activeparent = $(this).parent();
					if($exchange.hasClass("exchange")){
						$exchangeparent.append($active); 
						$activeparent.append($exchange); 
						$(".pastDistribution .arrow").removeClass("active");
						$(".classroomArrangement .item").removeClass("exchange active");
					}else{
						layer.msg('冲突');
					}
				}else{

					
				}
			});
				

		});
	})
	
	-->
	
	
	$(function(){
		showBreadBack(toBackItemList, false, '教室方案');
		$(".willDistribution").on('click','.item',function(){
			var $willitem = $(this);
			var $willitemparent = $(this).parent();
			var $willnum = $(this).find(".num").text();
			var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
			var $willBath=($(this).find(".num").hasClass("xuan") ? "A" : "B")+"_"+$willnum;
			
			if(!$(".pastDistribution").find(".active").length){
				if($(this).hasClass("active")){
					$(this).removeClass("active")
					$(".pastDistribution .arrow").removeClass("current");
					$(".pastDistribution .item").removeClass("exchange");
				}else{
					$(".willDistribution .item").removeClass("active");
					$(this).addClass("active");
					$(".pastDistribution .arrow").removeClass("current");
					$(".pastDistribution .item").removeClass("exchange");
					$(".pastDistribution .itemArea").each(function(){
						var $children = $(this).children();
						var $length = $(this).children().length;
						var noBaths=$(this).attr("data-value");
						if(!noBaths){
							noBaths="";
						}
						if($length){
							if(noBaths.indexOf($willBath)==-1){
								$($children).each(function(){
									var $pastnum = $(this).find(".num").text();
									var $pasttype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
									if($willnum == $pastnum && $willtype == $pasttype){
										$(this).addClass("exchange");
									}
								});
								if(!$(this).find(".exchange").length){
									$(this).siblings(".arrow").addClass("current")
								}
							}
						}else{
							if(noBaths.indexOf($willBath)==-1){
								$(this).siblings(".arrow").addClass("current");
							}
						}
					});
				}
			}else{
				
				$(".pastDistribution .item").each(function(){
					if($(this).hasClass("active")){
						var $pastitem = $(this);
						var $pastitemparent = $(this).parent();
						if($willitem.hasClass("exchange")){
							$pastitemparent.append($willitem); 
							$willitemparent.append($pastitem); 
							$(".classroomArrangement .item").removeClass("exchange active");
						}else{
							layer.msg('冲突');
						}
					}
				});
				findNoItem();
			}	
					
		});
		
		
		$(".pastDistribution").on('click','.arrow',function(){
			var $itemArea = $(this).siblings(".itemArea");
			if($(this).hasClass("current")){
				$(".willDistribution .item").each(function(){
					if($(this).hasClass("active")){
						var $active = $(this);
						$itemArea.append($active); 
						$(".pastDistribution .arrow").removeClass("current");
						$(".pastDistribution .item").removeClass("exchange active");
					}
				});
			}
			findNoItem();
		});
		$(".pastDistribution").on('click','.item',function(){
			var $pastitem = $(this);
			var $pastitemparent = $(this).parent();
			var $pastnum = $(this).find(".num").text();
			var $pasttype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
			if(!$(".willDistribution").find(".active").length){
				if($pastitem.hasClass("active")){
					$pastitem.removeClass("active");
					$(".classroomArrangement .item").removeClass("exchange");
				}else{
					if(!$(".pastDistribution").find(".active").length){
						$pastitem.addClass("active");
						$(".classroomArrangement .item").removeClass("exchange");
						$(".classroomArrangement .item").not(".active").each(function(){
							var $num = $(this).find(".num").text();
							var $type = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
							if($num == $pastnum && $type == $pasttype){
								$(this).addClass("exchange");
							}								
						});
					}else{
						if($pastitem.hasClass("exchange")){
							$(".pastDistribution .item").each(function(){
								if($(this).hasClass("active")){
									var $activeitem = $(this);
									var $activeitemparent = $(this).parent();
									$pastitemparent.append($activeitem); 
									$activeitemparent.append($pastitem); 
									$(".classroomArrangement .item").removeClass("exchange active");
								}
							});
						}else if($pastitemparent.find(".active").length){
							$pastitemparent.children().removeClass("active");
							$pastitem.addClass("active");
							$(".classroomArrangement .item").removeClass("exchange");
							$(".classroomArrangement .item").not(".active").each(function(){
								var $num = $(this).find(".num").text();
								var $type = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
								if($num == $pastnum && $type == $pasttype){
									$(this).addClass("exchange");
								}								
							});
						}else{
							layer.msg('冲突');
						}
					}
				}
			}else{
				$(".willDistribution .item").each(function(){
					if($(this).hasClass("active")){
						var $willitem = $(this);
						var $willitemparent = $(this).parent();
						if($pastitem.hasClass("exchange")){
							$pastitemparent.append($willitem); 
							$willitemparent.append($pastitem); 
							$(".pastDistribution .arrow").removeClass("current");
							$(".classroomArrangement .item").removeClass("exchange active");
						}else{
							layer.msg('冲突');
						}
					}
				});
			}	
			findNoItem();
		});
		$(".pastDistribution").on('click','.del',function(e){
			e.stopPropagation();
			var $item = $(this).parent();
			var $pastnum = $item.find(".num").text();
			var $pasttype = $item.find(".num").hasClass("xuan") ? "xuan" : "xue";
			if($(".willDistribution").find(".active").length){
				if($item.hasClass("exchange")){
					$item.removeClass("exchange");
					$item.parent(".itemArea").siblings(".arrow").addClass("current");
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
						}
					});
				}else{
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
						}
					});
				}
			}else if($(".pastDistribution").find(".active").length){
				if($item.hasClass("active")){
					$item.removeClass("active");
					$(".classroomArrangement .item").removeClass("exchange");
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
						}
					});
				}else{
					$(".willDistribution .category").each(function(){
						var $willnum = $(this).find(".num").text();
						var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
						if($willnum == $pastnum && $willtype == $pasttype){
							$(this).siblings().append($item);
						}
					});
				}
			}else{
				$(".willDistribution .category").each(function(){
					var $willnum = $(this).find(".num").text();
					var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
					if($willnum == $pastnum && $willtype == $pasttype){
						$(this).siblings().append($item);
					}
				});
			}
			findNoItem();
		});
		
		
		var $window = $(window).height();
		$(".willDistribution").height($window-308);
		$(".pastDistribution").height($window-300);
	})
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	var isDelete=false;
	function deletePlaceArrangeItemBath(){
		if(isDelete){
			return;
		}
		isDelete=true;
		layer.confirm("清空重排将会清空之前场地安排数据，确定清空重排吗？", {
			btn: ["确定", "取消"]
		}, function(){
		    var url =  '${request.contextPath}/newgkelective/${divideId!}/placeArrange/deleteByBath?arrayItemId=${newGkArrayItem.id!}&arrayId=${arrayId!}';
			$.ajax({
				url:url,
				dataType: "JSON",
				success: function(data){
					if(data.success){
						layer.closeAll();
						layer.msg(data.msg, {offset: 't',time: 2000});
		                var url1="${request.contextPath}/newgkelective/${divideId!}/placeArrange/listByBath?arrayItemId=${newGkArrayItem.id!}&lessArrayId=${lessArrayId!}&arrayId=${arrayId!}";
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
	  $(".pastDistribution .itemArea").each(function(){
	    	var placeId=$(this).attr("id");
	    	var $this=$(this);
	    	var noBath=$(this).attr("data-value");
	    	var objectIds="";
	    	var pBatch={};
	    	if($(this).find(".item").length<0){
	    		return true;
	    	}
	    	$(this).find(".item").each(function(){
	    		var classId=$(this).attr('pid');
	    		var batch=$(this).attr('data-value');
	    		if(!batch){
	    			batch="";
	    		}
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
			url:"${request.contextPath}/newgkelective/"+divideId+"/placeArrange/saveByBath?arrayItemId="+arrayItemId+"&arrayId=${arrayId!}",
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
					<#--
		                var url1="${request.contextPath}/newgkelective/${divideId!}/placeArrange/listByBath?arrayItemId="+arrayItemId
		                		+"&arrayId=${arrayId!}&lessArrayId=${lessArrayId!}";
		                $("#showList").load(url1);
	                -->
	                <#if arrayId?default('')==''>
						   var url1 =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${newGkArrayItem.id!}';
							$("#showList").load(url1);
					 <#else>
							var url1 = '${request.contextPath}/newgkelective/${arrayId!}/arraySet/pageIndex';
            				$("#showList").load(url1);
					 </#if>
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
	
	$('.js-random-jxb').click(function(){
    	//placeLen 剩余科目数  classLen 总共班级数
    	//剩余科目数量
    	var allItemCount=0;
    	var leftBatch={};
    	$(".willDistribution").find(".batch-class").each(function(){
    		var mm=0;
    		var subjectObj=new Array();
    		var batch=$(this).attr("id");
    		var ll=$(this).find(".item").length;
    		if(ll>0){
    			allItemCount=allItemCount+ll;
    			$(this).find(".item").each(function(){
    				subjectObj[mm]=$(this);
    				mm++;
    			});
    			leftBatch[batch]=subjectObj;
    		}
    	});
    	if(allItemCount==0){
    		return;
    	}	
    	var allPalceMap={};
    	var pIdByXzbId={};
    	var noBathPlaceId={};
    	var pIdCountMap={};
    	$('.pastDistribution').find(".itemArea").each(function(){
			var noBatch=$(this).attr("data-value");
			var xzbClassId=$(this).attr("data-class-id");
			var placeId=$(this).attr("id");
			allPalceMap[placeId]=$(this);
			if(xzbClassId && xzbClassId!=""){
				//存在行政班
				pIdByXzbId[xzbClassId]=placeId;
			}
			var length=$(this).find(".item").length;
			if(length){
			 	pIdCountMap[placeId]=length;	
			 	$(this).find(".item").each(function(){
	 				var spanData=$(this).attr("data-value");
	 				if(!noBathPlaceId[spanData]){
			 			noBathPlaceId[spanData]=placeId;
			 		}else{
			 			noBathPlaceId[spanData]=noBathPlaceId[spanData]+","+placeId;
			 		}
	 			})
			}else{
				pIdCountMap[placeId]=0;
			}
					
			for(var key in leftBatch){
				if(noBatch.indexOf(key)>-1){
			 		//不符合
			 		if(!noBathPlaceId[key]){
			 			noBathPlaceId[key]=placeId;
			 		}else{
			 			if(noBathPlaceId[key].indexOf(placeId)==-1){
			 				noBathPlaceId[key]=noBathPlaceId[key]+","+placeId;
			 			}
			 		}
			 	}
			}
	
    	});
    	for(var batch in leftBatch){
    		var subjectObjArr=leftBatch[batch];
    		if(subjectObjArr && subjectObjArr.length>0){
    			for(var kk=0;kk<subjectObjArr.length;kk++){
    				var $changeItem=subjectObjArr[kk];
    				var firstClassIds=$changeItem.find(".firstClassId").val();
    				var $td=null;
    				var choosePId="";
    				
    				if(firstClassIds && firstClassIds!=""){
    					var arr=firstClassIds.split("、");
		    			for(var bb=0;bb<arr.length;bb++){
		    				if(pIdByXzbId[arr[bb]]){
		    					ppId=pIdByXzbId[arr[bb]];
		    					if(noBathPlaceId[batch] && noBathPlaceId[batch].indexOf(ppId)>-1){
		    						//不能安排
		    					}else{
		    						$td=allPalceMap[ppId];
    								choosePId=ppId;
    								break;
		    					}
		    				}
		    			}
    				}
    				if($td==null){
    					//找到可以存放的最小值
		    			var maxCount=0;
		    			for(var pId in pIdCountMap){
		    				if(noBathPlaceId[batch] && noBathPlaceId[batch].indexOf(pId)>-1){
		    					//不能存放
		    				}else{
		    					if(pIdCountMap[pId]==0){
		    						$td=allPalceMap[pId];
		    						choosePId=pId;
		    						break;
		    					}else{
		    						if($td==null){
		    							$td=allPalceMap[pId];
		    							maxCount=pIdCountMap[pId];
		    							choosePId=pId;
		    						}else{
		    							if(maxCount>pIdCountMap[pId]){
		    								$td=allPalceMap[pId];
		    								maxCount=pIdCountMap[pId];
		    								choosePId=pId;
		    							}
		    						}
		    					}
		    				}
		    			}
    				}
		    		if($td!=null){
		    			$td.append($changeItem.clone());
						$changeItem.remove();
						pIdCountMap[choosePId]=pIdCountMap[choosePId]+1;
						if(noBathPlaceId[batch]){
							noBathPlaceId[batch]=noBathPlaceId[batch]+","+choosePId;
						}else{
							noBathPlaceId[batch]=choosePId;
						}
		    		}
    			}
    		}
    		

    	}
    	findNoItem();
    });
    
</script>
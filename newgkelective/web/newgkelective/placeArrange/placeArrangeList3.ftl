<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<form id="myForm">
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<a class="btn btn-blue js-random-xzb" href="javascript:">智能安排</a>
				<a class="btn btn-white" href="javascript:toPlaceArrangEdit2('${newGkArrayItem.id!}');">清空重排</a>
                
                <form id="placeAdjustForm">
                    <input type="hidden" id="placeIdsTemp" name="placeIdsTemp" value="${placeIdsTemp}"/>
                    <input type="hidden" name="placeNamesTemp" id="placeNamesTemp" class="form-control" value="${placeNamesTemp}">
                </form>
                <#if !isFakeXzb?default(false)>
                <a id="placeAddId" class="btn btn-white" href="javascript:" onclick="addPlace();">场地调整</a>
                </#if>
			</div>
		</div>
		<div class="classroomArrangement classroomArrangementSimple clearfix">
			<div class="float-left" style="width: 40%;">
				<table class="table no-margin">
					<thead>
						<tr>
							<th>待分配场地</th>
						</tr>
					</thead>
				</table>	
				<div class="willDistribution" >
					<div>
						<p class="category"><#--<span class="num xuan">1</span>--><strong>场地列表</strong></p>
						
							<div class="clearfix category-itemList">
	                        <#if teachPlaceList?exists && teachPlaceList?size gt 0>
	                            <#--<#list teachPlaceMap?keys as key>
		                            	<#assign tp=teachPlaceMap[key]>-->
								<#list teachPlaceList as key>
		                            	<a class="item" href="javascript:" title="${key.placeName!}${tempShow?default("\n")}${key.teachBuildingName?default('其他楼')}">
											<span class="del">&times;</span>
											<span class="huan">换</span>
											<span class="bj" pid="${key.id}">
												<#--<span class="num xuan">1</span>-->
												${key.placeName!}
											</span>
											<span class="stu clearfix">
												<span class="float-left" style="width:100%">${key.teachBuildingName?default('其他楼')}</span>
											</span>
											
										</a>
		                          </#list>
		                    <#else>
		                          <div class="no-data-container">
										<div class="no-data">
											<span class="no-data-img">
												<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
											</span>
											<div class="no-data-body">
												<p class="no-data-txt">暂无待分配的场地</p>
											</div>
										</div>
									</div>
		                    </#if>
							</div>
					</div>
				</div>												
			</div>
			<div class="float-left" style="width: 60%;">
				<table class="table no-margin">
					<thead>
						<tr>
							<th width="5%"></th>
							<th width="15%"><#if isFakeXzb?default(false)>二科组合班<#else>行政班</#if></th>
							<th width="80%">已分配场地</th>
							<th width=""></th>
						</tr>
					</thead>
				</table>	
				<div class="pastDistribution" >
					<table class="table table-bordered" style="margin-top: -1px;margin-bottom: -1px;">
						<tbody>
						 	<#if divideClassList?exists && divideClassList?size gt 1>
                                <#list divideClassList as divideClass >
                                    <tr>
                                    	<td width="5%" class="arrow">
											<span class="glyphicon glyphicon-arrow-right"></span>
										</td>
										<td width="15%" class="text-center"> 
										<input type="hidden" name="newGkPlaceItemList1[${divideClass_index}].objectId" value="${divideClass.id!}">
                                        <input type="hidden" name="newGkPlaceItemList1[${divideClass_index}].type" value="${divideClass.classType!}">
                                        <input type="hidden" name="newGkPlaceItemList1[${divideClass_index}].placeId" value="${divideClass.placeIds!}" id="${divideClass.id!}pid">
										${divideClass.className!}
										</td>
										<td width="80%" class="itemArea" id="${divideClass.id!}">
											<#if divideClass.placeIds?exists && classPlaceMap[divideClass.id]?exists>
                                                <#assign tp =classPlaceMap[divideClass.id]>
												<a class="item" href="javascript:" title="${tp.placeName!}${tempShow?default("\n")}${tp.teachBuildingName?default('其他楼')}">
													<span class="del">&times;</span>
													<span class="huan">换</span>
													<span class="bj" pid="${tp.id}">
														<#--<span class="num xuan">1</span>-->
														${tp.placeName!}
													</span>
													<span class="stu clearfix">
														<span class="float-left" style="width:100%">${tp.teachBuildingName?default('其他楼')}</span>
													</span>
												</a>
											</#if>
										</td>            
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
<#-- 添加场地 -->
<div class="layer layer-copyCourseParm">
	<div class="layer-content">
		<div class="gk-copy" style="border: 1px solid #eee;">
			<div class="box-body padding-5 clearfix">
				<b class="float-left mt3">各个场地</b>
				<div class="float-right input-group input-group-sm input-group-search">
			        <div class="pull-left">
			        	<input type="text" id="findTeacher" class="form-control input-sm js-search" placeholder="输入场地名称查询" value="">
			        </div>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default" onClick="findTeacher();">
					    	<i class="fa fa-search"></i>
					    </button>
				    </div>
			    </div>
			</div>
			<table class="table no-margin">
				<tr>
					<th width="127">楼层</th>
					<th><label><input type="checkbox" name="copyTeacherAll" class="wp"><span class="lbl"> 全选</span></label></th>
				</tr>
			</table>
			<div class="gk-copy-side" id="myscrollspy">
				<ul class="nav gk-copy-nav" style="margin: 0 -1px 0 -1px;height:437px;">
				<#if buildList?? && buildList?size gt 0>
				<#list buildList as build>
                	<li id="class_${build.id!}" class="courseLi <#if build_index==0>active</#if>">
	                	<a href="#aaa_${build.id!}" data-value="${build.id!}">${build.buildingName!}
	                		<span class="badge badge-default"></span>
	                	</a>
                	</li>
				</#list>
				</#if>
				</ul>
			</div>
			<div class="gk-copy-main copyteacherTab">
				<div data-spy="scroll" data-target="#myscrollspy" id="scrollspyDivId"style="position:relative;overflow:auto;height:437px;border-left: 1px solid #eee;">
					<#-- 这里放 各个班级的课程 -->
				<#if buildList?? && buildList?size gt 0>
				<#list buildList as build>
                	<div id="aaa_${build.id!}"  data-value="${build.id!}">
						<div class="form-title ml-15 mt10 mb10">${build.buildingName!}
							<a class="color-blue ml10 font-12 js-clearChoose" href="javascript:">取消</a> 
							<a class="color-blue ml10 font-12 js-allChoose" href="javascript:">全选</a> 
						</div>
						<ul class="gk-copy-list">
						<#assign places = placeByBuildingMap[build.id!]!>
						<#if places?? && places?size gt 0>
						<#list places as p>
							<label class="mr20">
								<input type="checkbox" class="wp" name="copyTeacher" value="${p.id!}" data-value="${p.placeName!}">
								<span class="lbl">${p.placeName!}</span>
							</label>
						</#list>
						</#if>
						</ul>
					</div>
				</#list>
				</#if>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<#--<a class="btn btn-white" href="javascript:toPlaceArrange();">上一步</a>-->
	<#if isFakeXzb?default(false)>
	<a class="btn btn-white" href="javascript:beforePlaceList()">上一步</a>
	</#if>
	<#if isNext>
        <a class="btn btn-blue" href="javascript:savePlaceArrangeItem('1');">下一步</a>
    <#else>
		 <a class="btn btn-blue" href="javascript:savePlaceArrangeItem('0');">保存</a>
    </#if>
</div>

<script type="text/javascript">
	function beforePlaceList(){
		var url='${request.contextPath}/newgkelective/${divideId!}/placeArrange/list?arrayItemId=${newGkArrayItem.id!}'
			+ '&lessArrayId=${lessArrayId!}&plArrayId=${newGkArrayItem.id!}&arrayId=${arrayId!}';
		$("#showList").load(url);
	}
	
    function dealPlace(){
        savePlaceSet();
    }

    function savePlaceSet(){
        var placeIds = [];
    	$("#scrollspyDivId input:checked").each(function(index,obj){
    		var placeId = $(obj).val();
    		placeIds.push(placeId);
    	});
    	$("#placeIdsTemp").val(placeIds.join());
    	
        $.ajax({
            url : "${request.getContextPath()}/newgkelective/${divideId!}/placeArrange/${arrayItemId!}/placeSetSave",
            type : "post",
            dataType : "json",
			data : {
                "placeIdsTemp" : $("#placeIdsTemp").val(),
                "placeNamesTemp" : $("#placeNamesTemp").val()
			},
            success : function(data){
                if (data.success){
                    var url='${request.contextPath}/newgkelective/${divideId!}/placeArrange/list?arrayItemId=${arrayItemId!}&arrayId=${arrayId!}&useMaster=1'
                    layer.closeAll();
                    $("#showList").load(url);
                    layer.msg("调整成功", {time: 2000});
                } else {
                    layer.msg("调整失败", {time: 2000});
				}
            },
			error : function () {
                layer.msg("调整失败", {time: 2000});
            }
        });
    }

	function toBack(){

        <#if fromSolve?default('0')=='1'>
            var url = '${request.contextPath}/newgkelective/${arrayId!}/arraySet/pageIndex';
            $("#showList").load(url);
	    <#elseif arrayId?default('')==''>
		   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${arrayItemId!}';
			$("#showList").load(url);
	    <#else>
            var url = '${request.contextPath}/newgkelective/${arrayId!}/arraySet/pageIndex';
            $("#showList").load(url);
	    </#if>
	}
	
	function toPlaceArrange(){
		var url = '${request.contextPath}/newgkelective/${divideId!}/placeArrange/eidt?arrayId=${arrayId!}&arrayItemId=${newGkArrayItem.id!}&lessArrayId=${lessArrayId!}&plArrayId=${arrayItemId!}'; 
		$("#showList").load(url);
	}

    function toPlaceArrangEdit2(arrayItemId) {
        var divideId = '${divideId!}';
        var index = layer.confirm("清空重排将会清空之前场地安排数据，确定清空重排吗？", {
            btn: ["确定", "取消"]
        }, function () {
            layer.closeAll();

            var postUrl = '${request.contextPath}/newgkelective/${divideId!}/placeArrange/list?arrayItemId=' + arrayItemId 
            	+ '&lessArrayId=${lessArrayId!}&plArrayId=' + arrayItemId+'&arrayId=${arrayId!}'+'&useMaster=1';
            var url = '${request.contextPath}/newgkelective/${divideId!}/placeArrange/deleteByAll?arrayItemId=' + arrayItemId+'&arrayId=${arrayId!}';
            if(${isFakeXzb?default(false)?string("true","false")}){
            	url += '&placeType=4';
            	postUrl="${request.contextPath}/newgkelective/${divideId!}/placeArrange/listByBath?arrayId=${arrayId!}&arrayItemId="+arrayItemId+"&lessArrayId=${lessArrayId!}";
            }
            $.ajax({
                url: url,
                dataType: "JSON",
                success: function (data) {
                    if (data.success) {
                        layer.closeAll();
                        layer.msg(data.msg, {offset: 't', time: 2000});
                        $("#showList").load(postUrl);
                    } else {
                        isDelete = false;
                        layerTipMsg(data.success, "失败", data.msg);
                    }
                },
                type: 'post',
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }//请求出错
            });

        })
    }

    $('.js-random-xzb').click(function(){
    	//placeLen 剩余科目数  classLen 总共班级数
        var placeLen = $(".willDistribution").find('.item').length;
        var classLen=$(".pastDistribution").find('.itemArea').length;
        if(placeLen==0 || classLen==0){
        	return;
        }
        var leftPlacemap={};
        var k=0;
        $(".willDistribution").find('.item').each(function(){
        	leftPlacemap[k]=$(this);
        	k++;
        });
        var i=0;
        $(".pastDistribution").find('.itemArea').each(function(){
        	if(i==k){
        		return;
        	}
        	var arrangePlace=$(this).find(".item").length
       	    if(arrangePlace==0){
       	    	$(this).append(leftPlacemap[i]); 
       	    	i++;
       	    }
        });
        findNoItem();
    });
    
    function findNoItem(){
    	$(".willDistribution").find(".no-data-container").remove();
    	$(".willDistribution").find(".category-itemList").each(function(){
    		if(!$(this).find(".item").length){
    			//<p class='color-999 nodata-class'>暂无待分配的场地</p>
    			$(this).append('<div class="no-data-container">\
										<div class="no-data">\
											<span class="no-data-img">\
												<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">\
											</span>\
											<div class="no-data-body">\
												<p class="no-data-txt">暂无待分配的场地</p>\
											</div>\
										</div>\
									</div>');
    		}
    	})
    }
    <#--点击事件--->
    $(function(){
    	showBreadBack(toBack, false, '教室方案');
		
		$(".willDistribution").on('click','.item',function(){
			var $willitem = $(this);
			var $willitemparent = $(this).parent();
			//var $willnum = $(this).find(".num").text();
			//var $willtype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
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
						if($length){
							$($children).each(function(){
								//var $pastnum = $(this).find(".num").text();
								//var $pasttype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
								//if($willnum == $pastnum && $willtype == $pasttype){
									$(this).addClass("exchange");
								//}
							});
							if(!$(this).find(".exchange").length){
								$(this).siblings(".arrow").addClass("current")
							}
						}else{
							$(this).siblings(".arrow").addClass("current");
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
			//var $pastnum = $(this).find(".num").text();
			//var $pasttype = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
			if(!$(".willDistribution").find(".active").length){
				if($pastitem.hasClass("active")){
					$pastitem.removeClass("active");
					$(".classroomArrangement .item").removeClass("exchange");
				}else{
					if(!$(".pastDistribution").find(".active").length){
						$pastitem.addClass("active");
						$(".classroomArrangement .item").removeClass("exchange");
						$(".classroomArrangement .item").not(".active").each(function(){
							//var $num = $(this).find(".num").text();
							//var $type = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
							//if($num == $pastnum && $type == $pasttype){
								$(this).addClass("exchange");
							//}								
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
								//var $num = $(this).find(".num").text();
								//var $type = $(this).find(".num").hasClass("xuan") ? "xuan" : "xue";
								//if($num == $pastnum && $type == $pasttype){
									$(this).addClass("exchange");
								//}								
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
	
	
	
	var isSave=false;
    function savePlaceArrangeItem(type){
	   var divideId = '${divideId!}';
	   var arrayItemId = '${arrayItemId!}';
	   if(isSave){
	   		return ;
	   }
	   isSave=true;
	   var check = checkValue('#myForm');
	    if(!check){
	        isSave=false;
	        return;
	    }
	    var flag = false;
	    $('td.itemArea').each(function(){
	    	var thisLen = $(this).find(".item").length;
        	if(thisLen!=1){
        		flag=true;
        		return;
        	}
        	var thisVal = $(this).find(".bj").attr("pid");
	    	var pid = $(this).attr('id')+'pid';
	    	$('#'+pid).val(thisVal);
        })
        
        if(flag){
        	isSave=false;
        	layerTipMsgWarn("提示","行政班有且只能有一个教室！");
        	return;
        }
		var options = {
			url:"${request.contextPath}/newgkelective/"+divideId+"/placeArrange/savePlaceArrangeItem?arrayItemId="+arrayItemId+"&arrayId=${arrayId!}",
			dataType : 'json',
			success : function(data){
		 		var jsonO = data;
			 	if(!jsonO.success){
			 		isSave=false;
			 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 		return;
			 	}else{
			 		layer.closeAll();
					layer.msg(jsonO.msg, {offset: 't',time: 2000});
					//type="0";
					if(type=="0"){
						//var url1="${request.contextPath}/newgkelective/${divideId!}/placeArrange/list?arrayItemId="+arrayItemId+"&lessArrayId=${lessArrayId!}";
	                	//$("#showList").load(url1);
	                	<#if arrayId?default('')==''>
							   var url1 =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${newGkArrayItem.id!}';
								$("#showList").load(url1);
						 <#else>
								var url1 = '${request.contextPath}/newgkelective/${arrayId!}/arraySet/pageIndex';
	            				$("#showList").load(url1);
						 </#if>
					}else{
						var url1="${request.contextPath}/newgkelective/${divideId!}/placeArrange/listByBath?arrayId=${arrayId!}&arrayItemId="+arrayItemId+"&lessArrayId=${lessArrayId!}";
	                	$("#showList").load(url1);
					}
	               
	    		}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#myForm").ajaxSubmit(options);
	}

function initAddPlace(){
	// 清除历史信息
	$(".layer-copyCourseParm :checkbox").prop("checked",false);
	$(".layer-copyCourseParm :checkbox").attr("disabled",false);
	$(".gk-copy-nav").find("span").each(function(){
		$(this).text("");
	});
	$("#scrollspyDivId .js-clearChoose").hide();
	$("#scrollspyDivId .js-allChoose").show();
	$('#findTeacher').val("");
	findTeacher();
	var $placeAear = $("#scrollspyDivId")
	var placeIds = [];
	var placeIdstr = $("#placeIdsTemp").val();
	if(placeIdstr){
		placeIds = placeIdstr.split(",");
	}
	for(x in placeIds){
		var pid = placeIds[x];
		$placeAear.find("input[value='"+pid+"']").click();
	}
}

// 添加场地
function addPlace(){
	//$(".currentSubjectName").html(coursTimeinfo[subjectCode]);
	initAddPlace();
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '添加场地',
		area: ['850px','650px'],
		btn: ['确定', '取消'],
		btn1:function(index){
			dealPlace();
			//layer.close(index);
		},
		btn2:function(){
			layer.closeAll();
		},
		content: $('.layer-copyCourseParm')
	});
}
function findTeacher(){
	//debugger;
	var teacherName=$('#findTeacher').val().trim();
	if(teacherName!=""){
		$(".gk-copy-main .lbl").removeClass("color-blue");
		var first;
		$(".gk-copy-main input").each(function(){
			var tnt = $(this).attr("data-value");
			if (tnt && tnt.includes(teacherName)) {
				if(!first){
					first=$(this);
				}
				$(this).siblings().addClass("color-blue");
			}
		});
		if(first){
			//模仿锚点定位
			var divId=$(first).parents("div").attr("id");
			document.getElementById(divId).scrollIntoView();
			
			var buid = $(first).parents("div").attr("data-value");
			document.getElementById("class_"+buid).scrollIntoView();
			setTimeout(function(){
				$("#class_"+buid).siblings().removeClass("active");
				$("#class_"+buid).addClass("active");
			},50);
		}
	}else{
		$(".gk-copy-main .lbl").removeClass("color-blue");
	}
}
//回车
$('#findTeacher').bind('keypress',function(event){//监听回车事件
    if(event.keyCode == "13" || event.which == "13")    
    {  
        findTeacher();
    }
});

// 选中所有班级所有课程
$('input:checkbox[name=copyTeacherAll]').on('change',function(){
	var actioveDiv=$(".copyteacherTab").find("div.active");
	if($(this).is(':checked')){
		$('input:checkbox[name=copyTeacher]').each(function(i){
			if(!$(this).is(':disabled')){
				$(this).prop('checked',true);
			}
		})
	}else{
		$('input:checkbox[name=copyTeacher]').each(function(i){
			$(this).prop('checked',false);
		})
	}
	//整体数量操作
	$(".courseLi").each(function(){
		var cid=$(this).find("a").attr("data-value");
		//计算数量
		var length=$("#aaa_"+cid).find('input:checkbox[name=copyTeacher]:checked').length;
		if(length>0){
			$(this).find("span").text(""+length);
			$("#aaa_"+cid).find(".js-allChoose").hide();
			$("#aaa_"+cid).find(".js-clearChoose").show();
		}else{
			$(this).find("span").text("");
			$("#aaa_"+cid).find(".js-allChoose").show();
			$("#aaa_"+cid).find(".js-clearChoose").hide();
		}
	})
});


// 班级全选
$(".js-allChoose").on('click',function(){
	var closeDiv=$(this).parent().parent();
	var cId=$(closeDiv).attr("data-value");
	var num=0;
	$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
		if(!$(this).is(':disabled')){
			$(this).prop('checked',true);
			num++;
		}
	})
	$("#class_"+cId).find("span").text(""+num);
	$(closeDiv).find(".js-allChoose").hide();
	$(closeDiv).find(".js-clearChoose").show();
});

// 班级取消全选
$(".js-clearChoose").on('click',function(){
	var closeDiv=$(this).parent().parent();
	var cId=$(closeDiv).attr("data-value");
	$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
		$(this).prop('checked',false);
	})
	$("#class_"+cId).find("span").text("");
	$(closeDiv).find(".js-allChoose").show();
	$(closeDiv).find(".js-clearChoose").hide();
});

//点中数量
$(".copyteacherTab").off('click').on('click','input:checkbox[name=copyTeacher]',function(){
	var closeDiv=$(this).closest("div");
	var course_id=$(closeDiv).attr("data-value");
	var num=$("#class_"+course_id).find("span.badge").text();
	if(num.trim()==""){
		num=parseInt(0);
	}else{
		num=parseInt(num);
	}
	if($(this).is(":checked")){
		//+1
		num=num+1;
	}else{
		//-1
		num=num-1;
	}
	if(num>0){
		$("#class_"+course_id).find("span.badge").text(""+num);
		//用取消
		$(closeDiv).find(".js-allChoose").hide();
		$(closeDiv).find(".js-clearChoose").show();
	}else{
		$("#class_"+course_id).find("span.badge").text("");
		//用全选
		$(closeDiv).find(".js-allChoose").show();
		$(closeDiv).find(".js-clearChoose").hide();
	}
});
$("#myscrollspy").on("click","ul li.courseLi",function(event){
	var obj = this;
	setTimeout(function(){
		$(obj).siblings().removeClass("active");
		$(obj).addClass("active");
		
	},5);
});
$(function(){
	$('#scrollspyDivId').scrollspy({ target: '#myscrollspy' });
});
</script>
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="filter">	
	<div class="filter-item">
		<a href="javascript:" class="btn btn-blue" onclick="addDivide()">新增分班方案</a>
		<a href="javascript:" class="btn btn-white" id="refreshDiv" onclick="combineDivide()">合并分班</a>
		<#--a href="javascript:" class="btn btn-white"  onclick="refreshDivide()">刷新列表</a-->
	</div>
	<div class="filter-item header_filter  filter-item-right">
		<label for="" class="filter-name">分班名称：</label>
		<div class="filter-content" style="width:300px;" >
			<select vtype="selectOne" class="form-control" id="choiceDivideId" onChange="refreshDivide(1,'',this.value)">
				<option value="">---请选择---</option>
			<#if allList?exists && (allList?size>0)>
			<#list allList as e>
				<option value="${e.id!}" <#if divideId! == e.id!>selected</#if>>${e.divideName!}</option>
			</#list>
			</#if>
			</select>		
		</div>
		<#if allList?exists && allList?size gt 0>
			<span class="filter-name color-999" style="margin-left: 10px">共有 ${allList?size} 条分班数据</span>
		</#if>
	</div>
	
</div>

<#if dtos?exists && dtos?size gt 0>

<#list dtos as item>

<div class="box box-default">
	<#if item.ent.isDefault?default(0)==1>
		<span class="default-icon" id="default-span_${item.ent.id!}"></span>
	<#else>
		<span id="default-span_${item.ent.id!}"></span>
	</#if>
	<div class="box-header">
		<h3 class="box-caption itemName myinput" item-id="${item.ent.id!}" onblur="modiName(this,'${item.ent.divideName!}','${item.ent.id!}')">${item.ent.divideName!}</h3>
		&nbsp;
		<#if item.ent.openType?default('')=='01'>
		<span class="badge badge-pink position-relative top-2">
		全固定模式
		</span>
		<#elseif item.ent.openType?default('')=='02'>
		<span class="badge badge-green position-relative top-2">
		半固定模式
		</span>
		<#elseif item.ent.openType?default('')=='06'>
		<span class="badge badge-orange position-relative top-2">
		全手动模式
		</span>
		<#elseif item.ent.openType?default('')=='08'>
		<span class="badge badge-purple position-relative top-2">
		智能组合分班
		</span>
		<#elseif item.ent.openType?default('')=='05'>
		<span class="badge badge-skyblue position-relative top-2">
		全走单科分层模式
		</span>
		<#elseif item.ent.openType?default('')=='03'>
		<span class="badge badge-skyblue position-purple top-2">
		文理科分层教学模式 — 语数外独立分班
		</span>
		<#elseif item.ent.openType?default('')=='04'>
		<span class="badge badge-skyblue position-purple top-2">
		文理科分层教学模式 — 语数外跟随文理组合分班
		</span>
		<#elseif item.ent.openType?default('')=='05'>
		全走班单科分层
		<#elseif item.ent.openType?default('')=='09'>
		<span class="badge badge-skyblue position-purple top-2">
			3+1+2单科分层（重组）
		</span>
		<#elseif item.ent.openType?default('')=='10'>
		<span class="badge badge-skyblue position-purple top-2">
			3+1+2组合固定（重组）
		</span>
		<#elseif item.ent.openType?default('')=='11'>
		<span class="badge badge-skyblue position-purple top-2">
		3+1+2单科分层（不重组）
		</span>
		<#elseif item.ent.openType?default('')=='12'>
		<span class="badge badge-skyblue position-purple top-2">
		3+1+2组合固定（不重组）
		</span>
  		</#if>
		<div class="box-header-tools">
			<div class="btn-group" role="group">
                <#if (item.ent.stat)?default("")=="1">
				    <#if item.ent.isDefault?default(0)==1>
			            <a data-value="${item.ent.id!}" data-stat="1" class="btn btn-sm btn-white" href="javascript:" onclick="setDivideDefault(this)">取消默认</a>
				    <#else>
			            <a data-value="${item.ent.id!}" data-stat="0" class="btn btn-sm btn-white" href="javascript:" onclick="setDivideDefault(this)">置为默认</a>
				    </#if>
                    <#-- 技术拆分 -->
                    <#if (item.ent.stat)?default("")!="0">
                        <#assign showcourseList=item.showCourseList>
                        <#if showcourseList?exists && showcourseList?size gt 0>
                            <#list showcourseList as courseItem>
                                <#if courseItem[0] == technologyId?default('')>
                                    <a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="splitSubject('${item.ent.id!}')">课程拆分</a>
                                </#if>
                            </#list>
                        </#if>
                    </#if>
                </#if>

				<#--<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="floatingPlan('${item.ent.id!}')">DEV</a>-->
				<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="gotoDivideResult('${item.ent.id!}')">查看分班结果</a>
                <div class="modify-name btn-group" role="group" itemName="${item.ent.divideName}" itemId="${item.ent.id}">
                    <button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">修改名称</button>
                </div>
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						更多
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right">
                        <li><a href="javascript:void(0);" onclick="showDivideItem('${item.ent.id!}')">查看分班设置</a></li>
						<li><a href="javascript:void(0);" onclick="copyDivideItem('${item.ent.id!}')">复制为新一轮</a></li>
						<li><a href="javascript:void(0);" id="${item.ent.id!}" class="js-del">删除</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	
	<div class="box-body">
	
		<#if (item.ent.stat)?default("")=="0"><#--未完成-->
			<div class="no-data-container">
				<div class="no-data" style="margin:10px;" >
					<#if item.haveDivideIng?default(false)><#--是否分班中-->
						<span class="no-data-img">
							<img src="${request.contextPath}/static/images/public/nodata2.gif" alt="">
						</span>
						<div class="no-data-body">
							<p class="no-data-txt">智能分班中，请稍后...</p>
						</div>
					<#else>
						<#if item.errorMess?default('')==''>
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata3.png" alt="">
							</span>
							<div class="no-data-body">
								<p class="no-data-txt">方案未完成，请<a class="color-blue" href="javascript:void(0);" onclick="gotoDivideResult('${item.ent.id!}')">继续设置</a></p>
							</div>
						<#else>
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata4.png" alt="">
							</span>
							<div class="no-data-body">
								<p class="no-data-txt">${item.errorMess?default('分班失败')}!</p>
							</div>
						</#if>
					</#if>
				</div>
			</div>
			<div class="text-right color-999 mt20">${(item.ent.creationTime?string('yyyy-MM-dd HH:mm:ss'))!}</div>
		<#else>
			<#assign isSeven=true>
			<#if item.ent.openType?default('')=='03' || item.ent.openType?default('')=='04'>
				<#assign isSeven=false>
			</#if>
			<#assign showcourseList=item.showCourseList>
			<#assign showFen=item.showFen>
			<table class="table table-bordered text-center diy-table">
                <thead>
					<tr>
						<th class="text-center" rowspan="2">行政班</th>
						<#if isSeven && item.ent.openType?default('')!='05'>
						<th class="text-center" rowspan="2">3科组合班</th>
						<#if item.ent.openType?default('')=='01' || item.ent.openType?default('')=='02' || item.ent.openType?default('')=='08'>
						<th class="text-center" rowspan="2">2科组合班</th>
						</#if>
						<#if item.ent.openType?default('')=='08'>
						<th class="text-center" rowspan="2">1科组合班</th>
						</#if>
						<#if item.ent.openType?default('')=='02' || item.ent.openType?default('')=='06'>
						<th class="text-center" rowspan="2">混合</th>
						</#if>
						</#if>
						<#if showcourseList?exists && showcourseList?size gt 0>
						<#list showcourseList as courseItem>
						<th class="text-center" colspan="${showFen?size}">${courseItem[1]}</th>
						</#list>
						</#if>
					</tr>
					<tr>
						<#if showcourseList?exists && showcourseList?size gt 0>
						<#list showcourseList as courseItem>
							<#list showFen as fen>
							<#assign key1=courseItem[0]+"_"+fen>
							<td>${fen!}</td>
							</#list>
					    </#list>
						</#if>
					</tr>
				</thead>
				<tbody>	
				    <tr>
						<td>${item.xzbAllclassNum?default(0)}</td>
						<#if isSeven && item.ent.openType?default('')!='05'>
						<td>${item.threeAllclassNum?default(0)}</td>
						<#if item.ent.openType?default('')=='01' || item.ent.openType?default('')=='02' || item.ent.openType?default('')=='08'>
						<td>${item.twoAllclassNum?default(0)}</td>
						</#if>
						<#if item.ent.openType?default('')=='08'>
						<td>${item.oneAllclassNum?default(0)}</td>
						</#if>
						<#if item.ent.openType?default('')=='02' || item.ent.openType?default('')=='06'>
						<td>${item.mixAllclassNum?default(0)}</td>
						</#if>
						</#if>
						<#assign abAllclassNum=item.abAllclassNum>
						<#if showcourseList?exists && showcourseList?size gt 0>
						<#list showcourseList as courseItem>
							<#list showFen as fen>
							<#assign key1=courseItem[0]+"_"+fen>
							<td>${abAllclassNum[key1]?default(0)}</td>
							</#list>
						</#list>
						</#if>
					</tr>
				</tbody>
			</table>
			<#--
			<table width="100%" class="diy-table">
				<tbody>	
					<tr>
						<td>
							<span class="line"></span>
							<div>
								<div class="numCategory">
									<div class="num">${item.xzbAllclassNum?default(0)}</div>
									<div class="category"></div>
								</div>
							</div>
							<div class="class">行政班</div>
						</td>
						<#if isSeven && item.ent.openType?default('')!='05'>
						<td>
							<span class="line"></span>
							<div>
								<div class="numCategory">
									<div class="num">${item.threeAllclassNum?default(0)}</div>
									<div class="category"></div>
								</div>
							</div>
							<div class="class">3科组合班</div>
						</td>
						<#if item.ent.openType?default('')=='01' || item.ent.openType?default('')=='02'>
						<td>
							<span class="line"></span>
							<div>
								<div class="numCategory">
									<div class="num">${item.twoAllclassNum?default(0)}</div>
									<div class="category"></div>
								</div>
							</div>
							<div class="class">2科组合班</div>
						</td>
						</#if>
						<#if item.ent.openType?default('')=='02' || item.ent.openType?default('')=='06'>
						<td>
							<span class="line"></span>
							<div>
								<div class="numCategory">
									<div class="num">${item.oneAllclassNum?default(0)}</div>
									<div class="category"></div>
								</div>
							</div>
							<div class="class">混合</div>
						</td>
						</#if>
						</#if>
						<#assign abAllclassNum=item.abAllclassNum>
						<#if showcourseList?exists && showcourseList?size gt 0>
						<#list showcourseList as courseItem>
							<td>
								<span class="line"></span>
								<div>
									<#list showFen as fen>
									<#assign key1=courseItem[0]+"_"+fen>
									<div class="numCategory">
										<div class="num">${abAllclassNum[key1]?default(0)}</div>
										<div class="category">${fen!}</div>
									</div>
									</#list>
									
								</div>
								<div class="class">${courseItem[1]}</div>
							</td>
						</#list>
						</#if>					
						</tr>
					</tbody>
				</table>
				-->
				<div class="text-right color-999 mt20">${(item.ent.creationTime?string('yyyy-MM-dd HH:mm:ss'))!}</div>
			</div>
		</#if>
	
	</div>
</div>
</#list>

<#if divideId?exists == false>
		<#assign allNum=0>
		<#if allList?exists>
		<#assign allNum=allList?size>
		</#if>
		<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="refreshDivide" allNum=allNum/>
<#--
	<nav class="nav-page clearfix">
		<ul class="pagination  pull-right" id="pagebar">
			<li <#if ((pageInfo.pageIndex)?default(1) > 1)> onclick="refreshDivide('${(pageInfo.pageIndex)?default(1) -1}')" <#else> class="disabled" </#if>><a href="javascript:void(0)" >&lt;</a></li>
			<li class="active"><a href="javascript:void(0)">${(pageInfo.pageIndex)?default(1)}</a></li>
			<li <#if ((pageInfo.pageIndex)?default(1) < pageInfo.pageCount)> onclick="refreshDivide('${(pageInfo.pageIndex)?default(1) +1}')"<#else> class="disabled"  </#if>><a href="javascript:void(0)" >
				&gt;</a>
			</li>
				
			&nbsp;&nbsp;<span>共${pageInfo.pageCount?default(1)}页</span>
			 <li class="pagination-other">
		    	跳到：
		    	<input type="text" class="form-control" id="skipNum">
		    	<button class="btn btn-white" onclick="skipTo()">确定</button>
		    </li>
		    <li class="pagination-other">
		    	共<#if allList?exists>${allList?size}<#else>0</#if>条
		    </li>
		    <li class="pagination-other">
		    	每页
		    	<select name="" id="pageSize" class="form-control" onchange="skipTo()">
					<#list [2,5,10,15] as num>
						<option value="${num!}" <#if num == pageInfo.pageSize>selected</#if>>${num!}</option>
					</#list>
				</select>
		    	 条
		    </li>
		</ul>
	</nav>
-->
</#if>

<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
		</span>
		<div class="no-data-body">
			<h3>暂无分班方案</h3>
			<p class="no-data-txt">请点击左上角的“新增分班方案”按钮</p>
		</div>
	</div>
</div>
</#if>
<script>
<#--
 
	function skipTo(){
		var skipNum = $('#skipNum').val();
		if(skipNum && skipNum != ''){
			if(isNaN(skipNum) || skipNum < 1 || skipNum.indexOf('.') != -1){
				layer.tips('只能输入正整数!', $('#skipNum'), {
					tipsMore: true,
					tips: 3
				});
				return;
			}
		}
		refreshDivide(skipNum);
	}
-->

	function floatingPlan(divideId){
		$("#showList").load("${request.contextPath}/newgkelective/" + divideId + "/floatingPlan/index?planType=A");
	}

	var setTimeClick;
	$(function(){
        initModifyName();
		<#if true>
			setTimeClick=setTimeout("refreshDivide1()",30000);
		</#if>
	})
	function refreshDivide1(){
		if(document.getElementById("refreshDiv")){
 			refreshDivide();
		} else {
			clearTimeout(setTimeClick);
		}
		
	}


	var isBackIndex=false;
	function gotoIndexFunction(){
		if(isBackIndex){
			return;
		}
		isBackIndex=true;
		clearTimeout(setTimeClick);
		var url =  '${request.contextPath}/newgkelective/index/list/page';
		$("#showList").load(url);
	}
	$(function(){
		<#--返回-->
		showBreadBack(gotoIndexFunction,true,"分班");
		
		//初始化单选控件
		initChosenOne(".header_filter");
		var isDeleted=false;
		$('.js-del').unbind().on('click', function(e){
			e.preventDefault();
			if(isDeleted){
				return ;
			}
			isDeleted=true;
			clearTimeout(setTimeClick);
			var that = $(this);
			var divideId=that.attr("id");
			layer.confirm('确定删除吗？', function(index){
				var ii = layer.load();
				$.ajax({
					url:"${request.contextPath}/newgkelective/"+divideId+"/divideClass/delete",
					dataType: "json",
					success: function(data){
						layer.closeAll();
						if(data.success){
							layer.msg("删除成功", {
								offset: 't',
								time: 2000
							});
							that.closest('.box').remove();
							layer.close(index);
							refreshDivide();
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
			})
		});
	})
	
	
	
	
	
	//新增方案
	var isAddDiv=false;
	function addDivide(){
		if(isAddDiv){
			return;
		}
		isAddDiv=true;
		clearTimeout(setTimeClick);
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/addDivide/page';
		$("#showList").load(url);
	}

function initModifyName(){
    $(".modify-name").each(function(){
        //$(this).children('div').length>0
        if($(this).find('.modify-name-layer').length > 0){
            return;
        }
        var tn = $(this).attr('itemName');
        var itemId = $(this).attr('itemId');
        var dc = ''
        if($(this).hasClass('divide-modi')){
            dc = 'divide-modi';
        }
        var modifyNameLayer = '<div class="modify-name-layer hidden" id="modiv'+itemId+'" style="width:350px;z-index: 999;">\
						<h5>修改名称</h5>\
						<p><input type="text" class="form-control" placeholder="请输入名称" value="'+tn+'"></p>\
						<div class="text-right" item-id="'+itemId+'"><button class="btn btn-sm btn-white modi-cancel">取消</button>&nbsp;&nbsp;&nbsp;<button class="btn btn-sm btn-blue modi-ok ml10 '+dc+'">确定</button></div>\
					</div>';
        $(this).append(modifyNameLayer);
    });

    $(".modify-name button").off('click').click(function(e){
        e.preventDefault();
        var tn = $(this).parent().attr('itemName');
        $(this).next().find(".form-control").val(tn);
        $(this).next().removeClass('hidden').show();
        if($(this).children().length === 1){
        }
    });

    $('.modi-cancel').off('click').on('click', function(e){
        e.preventDefault();
        var itemId = $(this).parent().attr('item-id');
        $("#modiv"+itemId).addClass('hidden');
    });

    $('.modi-ok').off('click').on('click', function(e){
        e.preventDefault();
        var newName = $(this).parents('.modify-name-layer').find('.form-control').val();
        var oldName = $(this).parents('.modify-name').attr('itemName');
        var itemId = $(this).parent().attr('item-id');
        var isDivide = $(this).hasClass('divide-modi');
        var rev = modiName($(this).parents('.modify-name-layer').find('input'),newName,oldName,itemId);
    });

}

	//修改
	var isUpdateName=false;
	function modiName(obj, newName, oldName, id){
		if(isUpdateName){
			return;
		}
		clearTimeout(setTimeClick);
		isUpdateName=true;
		var nn = $.trim(newName);
		if(nn==''){
			layer.tips('名称不能为空！',$(obj), {
					tipsMore: true,
					tips: 3
				});
			isUpdateName=false;
			return;	
		}
		if(getLength(nn)>50){
			layer.tips('名称内容不能超过50个字节（一个汉字为两个字节）！',$(obj), {
					tipsMore: true,
					tips: 3
				});
			isUpdateName=false;
			return;
		}
		if(nn==oldName){
			isUpdateName=false;
            layer.tips('与原名称内容相同！',$(obj), {
                tipsMore: true,
                tips: 3
            });
			return;
		}
		
		nn = nn.replace(/\s/g, " ");
		
		$.ajax({
			url:'${request.contextPath}/newgkelective/${gradeId!}/goDivide/saveName',
			data: {'divideId':id,'divideName':nn},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.closeAll();
				  	layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
					refreshDivide();
		 		}else{
		 			obj.value=oldName;
		 			layer.tips(jsonO.msg,$(obj), {
						tipsMore: true,
						tips: 3
					});
				}
				isUpdateName=false;
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){
                isUpdateName=false;
			}
		});
	}
	
	
	//设置默认
	var isSetDefault=false;
	function setDivideDefault(obj){
		if(isSetDefault){
			return;
		}
		clearTimeout(setTimeClick);
		isSetDefault=true;
		var did=$(obj).attr("data-value");
		var stat=$(obj).attr("data-stat");
		var tt="";
		if("1"==stat){
			tt="取消默认";
		}else{
			tt="设置默认";
		}
		
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/newgkelective/${gradeId!}/goDivide/setDefault',
			data: {'divideId':did,'stat':stat},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.msg(tt+"成功", {
						offset: 't',
						time: 2000
					});
		 			if("1"==stat){
		 				//直接置为默认
						$(obj).attr("data-stat",0);
						$(obj).text("置为默认");
						$("#default-span_"+did).removeClass("default-icon");
						$("#default-span_"+did).html("");
					}else{
						//其他按钮设置为置为默认
		 				//此处 取消默认
		 				refeshDefault(did);
					}
					changePageIndex(1);
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
				isSetDefault=false;
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function refeshDefault(divideId){
		$(".js-divide-default").each(function(){
			var oldId=$(this).attr("data-value");
			if(oldId==divideId){
				$(this).attr("data-stat",1);
				$(this).text("取消默认");
				$("#default-span_"+oldId).addClass("default-icon");
				$("#default-span_"+oldId).html("默认");
			}else{
				$(this).attr("data-stat",0);
				$(this).text("置为默认");
				$("#default-span_"+oldId).removeClass("default-icon");
				$("#default-span_"+oldId).html("");
			}
		})
		
	}
	
	//进入结果页或者手动分班页面
	var isGoNext=false;
	function gotoDivideResult(divideId){
		if(isGoNext){
			return;
		}
		clearTimeout(setTimeClick);
		isGoNext=true;
		//var url =  '${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassList';
		var url =  '${request.contextPath}/newgkelective/'+divideId+'/divideClass/item';
		$("#showList").load(url);
	}
	
	//进入详情页面
	var isShowItem=false;
	function showDivideItem(divideId){
		if(isShowItem){
			return;
		}
		clearTimeout(setTimeClick);
		isShowItem=true;
		var url =  '${request.contextPath}/newgkelective/'+divideId+'/divideClass/showItem';
		$("#showList").load(url);
	}
	
	//页面刷新
	var isRefresh=false;
	function refreshDivide(pageIndex,pageSize,divideId){
		if(isRefresh){
			return;
		}
		clearTimeout(setTimeClick);
		isRefresh=true;
		if(!divideId){
			if($("#choiceDivideId")){
				divideId=$("#choiceDivideId").val();
			}
			if(!divideId){
				divideId = '';
			}
		}
		<#--
			var pageSize = $('#pageSize').val();
			if(!pageIndex){
				pageIndex = $('#pagebar li.active a').text();
			}
			if(!pageIndex){
				pageIndex = 1;
			}
			if(!pageSize){
				pageSize = ${pageInfo.pageSize!};
			}
			if(!pageSize){
				pageSize=1;
			}
		-->		
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page?divideId='+divideId;
		if(pageIndex){
			url=url+'&pageIndex='+pageIndex;
		}
		if(pageSize && pageSize!=""){
			url=url+'&pageSize='+pageSize;
		}
		$("#showList").load(url);
	}
	
	//ajax 复制功能
	var isCopy=false;
	function copyDivideItem(divideId){
		if(isCopy){
			return;
		}
		clearTimeout(setTimeClick);
		isCopy=true;
		layer.confirm('确定复制分班数据到新一轮吗？', function(index){
			var ii = layer.load();
			$.ajax({
				url:"${request.contextPath}/newgkelective/"+divideId+"/divideClass/copy",
				dataType: "json",
				success: function(data){
					if(data.success){
						layer.msg("复制成功", {
							offset: 't',
							time: 2000
						});
						refreshDivide();
					}else{
						layerTipMsg(data.success,"失败","原因："+data.msg);
						isCopy=false;
					}
					layer.close(ii);
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});
		},function(){
			isCopy=false;
		})
	}
	
	//ajax 合并功能
	var isHebing=false;
	function combineDivide(){
		if(isHebing){
			return;
		}
		clearTimeout(setTimeClick);
		isHebing=true;
		//默认取其中的一个结果
		var url ="${request.contextPath}/newgkelective/${gradeId!}/divide/combineDivide";
		indexDiv = layerDivUrl(url,{title: "合并",width:450,height:290});
		isHebing=false;
	}

	function splitSubject(divideId) {
	    var url = "${request.contextPath}/newgkelective/" + divideId + "/divide/splitSubject?gradeId=${gradeId!}";
	    layerDivUrl(url, {title: "课程拆分", width: 450, height: 290});
    }
	
	/*function modiNamePre(event){
		clearTimeout(setTimeClick);
		var t = event.target;
		var $item = $(t).parents(".box-header").find(".itemName");
		var oldName = $item.text();
		var itemId = $item.attr("item-id");
		
		//var htm = '<input type="text" style="width:80%;"  value="'+oldName+'" onblur="modiName(this,\''+oldName+'\',\''+itemId+'\')">';
		$item.attr({"contenteditable":"true"});
		$item.removeClass("myinput").addClass("myinput2");
		//$item.html(htm);
		$item.focus();
	}*/
</script>
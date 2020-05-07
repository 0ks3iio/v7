<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#--<div class="page-toolbar" style="margin-bottom:10px;">
	 <a href="javascript:" class="page-back-btn gotoIndexClass"><i class="fa fa-arrow-left"></i> 返回</a>
		<a href="javascript:" class="btn btn-blue" onclick="addArray()">新增排课</a>
		<a href="javascript:" class="btn btn-white" id="refreshListDiv" onclick="refreshList()">刷新列表</a>
		<a class="btn btn-white"href="javascript:"  onclick="goReport()" >结果对比</a>
		<#if arrayList?exists && arrayList?size gt 0>
			<span class="tip tip-grey pull-right">共有 ${arrayList?size}条排课数据</span>
		</#if>
</div>-->
<div class="filter">	
	<div class="filter-item">
		<a href="javascript:" class="btn btn-blue" id="refreshListDiv"  onclick="addArray()">新增排课</a>
		<#--a href="javascript:" class="btn btn-white" onclick="refreshList()">刷新列表</a-->
	</div>
	<#if allList?exists && allList?size gt 0>
	<div class="filter-item header_filter  filter-item-right">
		<label for="" class="filter-name">排课名称：</label>
		<div class="filter-content" style="width:300px;" >
			<select vtype="selectOne" class="form-control" id="choiceDivideId" onChange="refreshList(1,'',this.value)">
				<option value="">---请选择---</option>
			<#list allList as e>
				<option value="${e.id!}" <#if arrayId! == e.id!>selected</#if>>${e.arrayName!}</option>
			</#list>
			</select>		
		</div>
		<span class="filter-name color-999" style="margin-left: 10px">共有 ${allList?size} 条排课数据</span>
	</div>
	</#if>
</div>
<style>
.myinput2{
	border:1px solid #a5c7fe;
	margin-left:7px;
}
.myinput{
	border:1px solid white;
	margin-left:7px;
}

</style>
<#if arrayList?exists && arrayList?size gt 0>
<#list arrayList as item>
<div class="box box-default">
	<#if item.isDefault?default(0)==1>
    	<span class="app-icon"></span>
	</#if>
	<div class="box-header">
		<h3 class="box-caption itemName myinput" style="width: 50%;" item-id="${item.id!}">${item.arrayName!}</h3>
		<div class="box-header-tools">
			<div class="btn-group" role="group">
                <div class="modify-name btn-group" role="group" itemName="${item.arrayName}" itemId="${item.id}">
                    <button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">修改名称</button>
                </div>
			<#if item.now>

					<div class="btn-group" role="group">
						<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							更多
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu dropdown-menu-right">
							<li><a href="javascript:" onclick="stopArray('${item.id!}')">断开排课</a></li>
                            <li><a href="javascript:" onclick="showArraySet('${item.id!}')">查看排课设置</a></li>
						</ul>
					</div>
			<#else>
				<#if item.stat?default('0')=="0">
					<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="startArray('${item.id!}')">开始排课</a>
					<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="modifyArray('${item.id!}')">手动调课</a>
					<#if item.errorMess?default('') !=''>
					<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="exportExcp('${item.id!}')">导出全部异常信息</a>
					</#if>
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							更多
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu dropdown-menu-right">
                            <li><a href="javascript:" onclick="showArraySet('${item.id!}')">查看排课设置</a></li>
							<li><a href="javascript:" onclick="copyThis('${item.id!}')">复制此轮方案</a></li>
						<#-- 	<li><a href="javascript:" onclick="editArraySet('${item.id!}')" >编辑排课设置</a></li> -->
							<li><a href="javascript:" onclick="deleteArray2('${item.id!}')" class="js-del">删除</a></li>
						</ul>
					</div>
				<#elseif  item.stat?default('0')=="2">
					<#-- <a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="modifyArray('${item.id!}')">手动调课</a>  -->
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							更多
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu dropdown-menu-right">
							<li><a href="javascript:" onclick="deleteArray2('${item.id!}')" class="js-del">删除</a></li>
                            <li><a href="javascript:" onclick="showArraySet('${item.id!}')">查看排课设置</a></li>
						</ul>
					</div>
				<#elseif item.stat?default('0')=="3" || item.stat?default('0')=="4">
					<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="modifyArray('${item.id!}')">手动调课</a>
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							更多
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu dropdown-menu-right">
							<li><a href="javascript:" onclick="deleteArray2('${item.id!}')" class="js-del">删除</a></li>
                            <li><a href="javascript:" onclick="showArraySet('${item.id!}')">查看排课设置</a></li>
						</ul>
					</div>
				<#else>
				
					<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="resultView('${item.id!}','0')">查看排课结果</a>
					<a type="button" class="btn btn-sm btn-white" href="javascript:" onclick="modifyArray('${item.id!}')">手动调课</a>
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							更多
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu dropdown-menu-right">
							<li><a href="javascript:" onclick="showArraySet('${item.id!}')">查看排课设置</a></li>
							<li><a href="javascript:" onclick="solveTeacher('${item.id!}','3')">重新安排教师</a></li>
							<li><a href="javascript:" onclick="copyThis('${item.id!}')">复制此轮方案</a></li>
							<li><a href="javascript:" onclick="useArrayResult('${item.id!}')">应用排课方案</a></li>
							<li><a href="javascript:" onclick="deleteArray2('${item.id!}')" class="js-del">删除</a></li>
						</ul>
					</div>
				</#if>
			</#if>
			   
				
			</div>
		</div>
	</div>
	<div class="box-body">
		<#if item.now>
			<div class="tip tip-big tip-blue">
				<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="">
				排课中，请稍等．．．
			</div>
		<#else>
			<#if item.stat?default('0')=="0">
				<div class="tip tip-big tip-blue">
					<#if item.errorMess?default('')==''>未排课
						
					<#else>
						排课失败！${item.errorMess!}
					</#if>
				</div>
			<#elseif  item.stat?default('0')=="2">
				<a class="link-block" href="javascript:" onclick="solveError('${item.id!}')">
					<div class="tip tip-big tip-blue">
						排课中存在冲突，请前往手动调整
					</div>
					<p>
						<a href="javascript:" onclick="solveError('${item.id!}')">去调整</a>
					</p>
				</a>
			<#elseif item.stat?default('0')=="3" || item.stat?default('0')=="4">
				
				<a class="link-block"  href="javascript:" onclick="solveTeacher('${item.id!}','${item.stat!}')" >
					<div class="tip tip-big tip-blue">
						仅剩最后一步啦！请前往安排教师
					</div>
					<p>
						<a href="javascript:" onclick="solveTeacher('${item.id!}','${item.stat!}')">去安排</a>
					</p>
				</a>
			<#else>
				<ul class="number-list number-list-big">
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','1')"><div class="number-icon number-icon01"><i>行</i></div><span>新行政班结果</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','2')"><div class="number-icon number-icon02"><i>教</i></div><span>各科班级结果</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','3')"><div class="number-icon number-icon03"><i class="wpfont icon-student"></i></div><span>学生上课查询</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','4')"><div class="number-icon number-icon04"><i class="wpfont icon-teacher"></i></div><span>教师上课查询</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','5')"><div class="number-icon number-icon05"><i>班</i></div><span>教室使用查询</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','6')"><div class="number-icon number-icon01"><i class="fa fa-table"></i></div><span>班级总课表</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','7')"><div class="number-icon number-icon02"><i class="fa fa-table"></i></div><span>教师总课表</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','8')"><div class="number-icon number-icon03"><i class="fa fa-table"></i></div><span>教室总课表</span></a>
					</li>
					<#if item.openType?default('')=='01' || item.openType?default('')=='02' || item.openType?default('')=='05' || item.openType?default('')=='06' 
						|| item.openType?default('')=='08' || item.openType?default('')=='09' || item.openType?default('')=='11'>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','9')"><div class="number-icon number-icon04"><i>调</i></div><span>学生调班</span></a>
					</li>
					<li>
						<a href="javascript:" onclick="resultView('${item.id!}','11')"><div class="number-icon number-icon04"><i>换</i></div><span>学生选课调整</span></a>
					</li>
					</#if>
				</ul>
			</#if>
		</#if>
        <div class="text-right color-999 mt20">创建时间：${(item.creationTime?string('yyyy-MM-dd HH:mm:ss'))!}</div>
	</div>
</div>

</#list>
<#-- 分页部件 -->
<#if arrayId?exists == false>
		<#assign allNum=0>
		<#if allList?exists>
		<#assign allNum=allList?size>
		</#if>
		<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="refreshList" allNum=allNum/>
</#if>

<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
		</span>
		<div class="no-data-body">
			<h3>暂无排课结果</h3>
			<p class="no-data-txt">请点击左上角的“新增排课”按钮</p>
		</div>
	</div>
</div>
</#if>
<script type="text/javascript"> 

	var setTimeClick;
	$(function(){
	    initModifyName();
		<#if true>
			setTimeClick=setTimeout("refreshList1()",300000);
		</#if>
		initChosenOne(".header_filter");
	})
	function refreshList1(){
		if(document.getElementById("refreshListDiv")){
 			refreshList();
		} else {
			clearTimeout(setTimeClick);
		}
		
	}

    var isUpdateName=false;
	function modiName(obj, newName, oldName, id){
        if(isUpdateName){
            return;
        }
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
		if(getLength(nn)>80){
			layer.tips('名称内容不能超过80个字节（一个汉字为两个字节）！',$(obj), {
					tipsMore: true,
					tips: 3
				});
            isUpdateName=false;
			return;
		}
		if(nn==oldName){
            layer.tips('与原名称内容相同！',$(obj), {
                tipsMore: true,
                tips: 3
            });
            isUpdateName=false;
			return;
		}
		
		nn=nn.replace(/\s/g, " ");
		
		$.ajax({
			url:'${request.contextPath}/newgkelective/${gradeId!}/goArrange/saveName',
			data: {'arrayId':id,'arrayName':nn},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layer.closeAll();
				  	layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
					useMaster = "1";
					refreshList();
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

	function gonewgkIndex(){
		clearTimeout(setTimeClick);
		var url =  '${request.contextPath}/newgkelective/index/list/page';
		$("#showList").load(url);
	}
	showBreadBack(gonewgkIndex, true, '排课');
	
	var isAddArr=false;
	function addArray(){
		if(isAddArr){
			return;
		}
		isAddArr=true;
		clearTimeout(setTimeClick);
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page';
		$("#showList").load(url);
	}
	function goReport(){
		clearTimeout(setTimeClick);
		var url =  '${request.contextPath}/newgkelective/goReport/${gradeId!}/index/page';
		$("#showList").load(url);
	}
	var isShowSet=false;
	function showArraySet(arrayId){
		if(isShowSet){
			return;
		}
		isShowSet=true;
		clearTimeout(setTimeClick);
		//查看排课设置
		var url ='${request.contextPath}/newgkelective/'+arrayId+'/arraySet/pageIndex';
		$("#showList").load(url);
	}
	
	function useArrayResult(arrayId){
		clearTimeout(setTimeClick);
		//应用排课方案
		var url ='${request.contextPath}/newgkelective/'+arrayId+'/saveClassAndSchedule/pageIndex';
		indexDiv = layerDivUrl(url,{title: "选择开始时间",width:500,height:360});
	}
	
	function resultView(arrayId,viewType){
		clearTimeout(setTimeClick);
		//查看结果
		var url ='${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/pageIndex';
		if(viewType!="0"){
			url=url+'?type='+viewType;
		}
		$("#showList").load(url);
	}
	
	
	function solveError(arrayId,useMaster){
		clearTimeout(setTimeClick);
		//解决冲突
		var url = '${request.contextPath}/newgkelective/'+arrayId+'/arrayLesson/conflictIndex/page?useMaster='+useMaster;
		//console.log("solve error-----------");
		$("#showList").load(url);
	}
	function solveTeacher(arrayId,stat){
		clearTimeout(setTimeClick);
		if(stat=="3"){
			var url =  '${request.contextPath}/newgkelective/'+arrayId+'/array/teacher/page?gradeId=${gradeId}';
			$("#showList").load(url);
		}else if(stat=="4"){
			var url =  '${request.contextPath}/newgkelective/'+arrayId+'/adjust/teacher/page';
			$("#showList").load(url);
		}
		
		<#--
			//直接进入微调页面
			var url =  '${request.contextPath}/newgkelective/'+arrayId+'/adjust2/teacher/page';
			$("#showList").load(url);
		-->
	}
	var useMaster = "";
	function refreshList(pageIndex, pageSize,arrayId){
		//console.log('pageIndex= '+pageIndex+' pageSize='+pageSize+' arrayId='+arrayId);
		clearTimeout(setTimeClick);
		if(!arrayId)
			arrayId = '';
		
		//var ii = layer.load();
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/index/page?arrayId='+arrayId+"&useMaster="+useMaster;
		if(pageIndex){
			url=url+'&pageIndex='+pageIndex;
		}
		if(pageSize && pageSize!=""){
			url=url+'&pageSize='+pageSize;
		}
		$("#showList").load(url);
		useMaster = "";
		//layer.closeAll();
	}
	
	var stopId={};
	function stopArray(arrayId){
		clearTimeout(setTimeClick);
		//结束排课
		if(!stopId[arrayId] && stopId[arrayId]==arrayId){
			return;
		}
		stopId[arrayId]=arrayId;
		$.ajax({
			url:"${request.contextPath}/newgkelective/"+arrayId+"/arrayLesson/cutArrayLesson",
			data:{"arrayId":arrayId},
			dataType: "json",
			success: function(data){
				var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 			refreshList();
		 			stopId[arrayId]="";
		 			return;
		 		}else{
		 			layer.closeAll();
	 				layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
					stopId[arrayId]="";
					refreshList();
					
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
		
		
	}
	
	var startId={};
	function startArray(arrayId){
		clearTimeout(setTimeClick);
		//开始排课
		if(!startId[arrayId] && startId[arrayId]==arrayId){
			return;
		}
		startId[arrayId]=arrayId;
		arrangeLessonNoTeacher2(arrayId);
	}
	
	function arrangeLessonNoTeacher2(arrayId){
		var ii = layer.load();
		$.ajax({
			url:"${request.contextPath}/newgkelective/"+arrayId+"/arrayLesson/autoArrayLessonNoTeacher",
			data:{"arrayId":arrayId},
			dataType: "json",
			success: function(data){
				layer.close(ii);
				if(data.stat=="success"){
					startId[arrayId]="";
					refreshList();
	 			}else if(data.stat=="error"){
	 				startId[arrayId]="";
					refreshList();
	 			}else{
	 				//不循环访问结果
	 				refreshList();
	 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}

	
	function deleteArray2(arrayId) {
		clearTimeout(setTimeClick);
		layer.confirm('确定删除吗？', function(index){
			$.ajax({
				url: '${request.contextPath}/newgkelective/'+arrayId+'/array/delete/page',
				dataType: "json",
				success: function(data){
					if(data.success){
						layer.msg(data.msg, {offset: 't',time: 2000});
						useMaster = "1";
						refreshList();
						layer.close(index);
					}else{
						layerTipMsg(data.success,"失败","原因："+data.msg);
						refreshList();
					}
				},
			});
		})
	}
	
	function editArraySet(arrayId){
		var url='${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId='+arrayId;
		$("#showList").load(url);
	}
	function copyThis(arrayId){
		layer.confirm('确定复制排课数据到新一轮吗？', function(index){
			var ii = layer.load();
			$.ajax({
					url: '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/copy',
					dataType: "json",
					success: function(data){
						layer.close(ii);
						if(data.success){
							layer.msg("复制排课方案成功", {
								offset: 't',
								time: 2000
							});
							useMaster = "1";
							refreshList();
						}else{
							layerTipMsg(data.success,"失败","原因："+data.msg);
						}
					},
				});
			
			});
	}
	
	function modiNamePre(event){
		var t = event.target;
		var $item = $(t).parents(".box-header").find(".itemName");
		var oldName = $item.text();
		var itemId = $item.attr("item-id");
		
		//var htm = '<input type="text" style="width:80%;"  value="'+oldName+'" onblur="modiName(this,\''+oldName+'\',\''+itemId+'\')">';
		$item.attr({"contenteditable":"true"});
		$item.removeClass("myinput").addClass("myinput2");
		//$item.html(htm);
		$item.focus();
	}
	
	function modifyArray(arrayId){
		//console.log("------手动调课---"+arrayId+"---");
		
		var url = "${request.contextPath}/newgkelective/scheduleModify/"+arrayId+"/index?";
		var width = screen.availWidth;
		var height = screen.availHeight -60;
		//var height = window.outerHeight;
		
		window.open  
			(url,'newwindow','fullscreen=no, height='+height+',width='+width+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,\
			resizable=no,location=no, status=no');
	}
	function exportExcp(arrayId){
		var url =  '${request.contextPath}/newgkelective/'+arrayId+'/array/exportExcp';
  		document.location.href=url;
	}
</script>
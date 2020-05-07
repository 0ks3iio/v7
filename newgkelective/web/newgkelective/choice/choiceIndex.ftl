<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="filter">
	<#if !isClassRole?default(false)>	
	<div class="filter-item">
		<a href="javascript:void(0);" class="btn btn-blue" onclick="doPublishChoice('${gradeId}')">发布选课</a>
	</div>
	</#if>
	<div class="filter-item header_filter filter-item-right">
		<label for="" class="filter-name">选课名称：</label>
		<div class="filter-content" style="width:300px;" >
			<select vtype="selectOne" class="form-control" id="choiceId" onChange="reload(1,'',this.value)">
				<option value="">---请选择---</option>
			<#if allList?exists && (allList?size>0)>
			<#list allList as e>
				<option value="${e.id!}" <#if choiceId! == e.id!>selected</#if>>${e.choiceName!}</option>
			</#list>
			</#if>
			</select>		
		</div>
		<#if allList?exists && (allList?size gt 0)>
			<span class="filter-item color-999" style="margin-left: 10px">共有 ${allList?size} 条选课结果</span>
		</#if>
	</div>
	
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
<#if newGkChoiceDtoList?exists && (newGkChoiceDtoList?size>0)>
	<#list newGkChoiceDtoList as item>
		<div class="box box-default">
			<#if item.default>
				<span class="default-icon" id="choice_default_${item.choiceId}"></span>
			<#else>
				<span id="choice_default_${item.choiceId}"></span>
			</#if>
			<div class="box-header">
				<h3 class="box-caption itemName myinput" style="width: 50%;" item-id="${item.choiceId!}" onblur="modiName(this,'${item.choiceName!}','${item.choiceId!}')">${item.choiceName!}</h3>
				<div class="box-header-tools">
					<div class="btn-group" role="group">
						<#if isClassRole?default(false)>
						<a href="javascript:void(0);" class="btn btn-sm btn-white" onclick="goChoiceResultItem('${item.choiceId}')">查看选课结果</a>
						<a href="javascript:void(0);" class="btn btn-sm btn-white" onclick="goChoiceAnalysis('${item.choiceId}')">查看分析结果</a>
						<#else>	
						<#if item.default>
							<a href="javascript:void(0)" class="btn btn-sm btn-white js-my-default"  data-value="${item.choiceId}" data-stat="1" onclick="setChoiceDefault('${gradeId}',this)" >取消默认</a>
						<#else>
							<a href="javascript:void(0)" class="btn btn-sm btn-white js-my-default"  data-value="${item.choiceId}" data-stat="0" onclick="setChoiceDefault('${gradeId}',this)" >置为默认</a>
						</#if>
						
						<a href="javascript:void(0)" class="btn btn-sm btn-white" onclick="goChoiceResultItem('${item.choiceId}')" >查看选课结果</a>
                        <div class="modify-name btn-group" role="group" itemName="${item.choiceName}" itemId="${item.choiceId}">
                            <button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">修改名称</button>
                        </div>
						<div class="btn-group" role="group">
							<button type="button" class="btn btn-sm btn-white dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								更多
								<span class="caret"></span>
							</button>
							<ul class="dropdown-menu dropdown-menu-right">
                                <#--<li>
                                    <a type="button" href="javascript:void(0);" onclick="modiNamePre(event)">修改名称</a>
                                </li>-->
                                <li>
                                <a type="button" href="javascript:void(0);" onclick="goChoiceSetting('${gradeId}','${item.choiceId}')" >查看选课设置</a>
                               </li>
                                <li><a type="button" href="javascript:void(0);"
                                       onclick="goChoiceAnalysis('${item.choiceId}')">查看分析结果</a>
                                </li>
                                <li><a href="javascript:void(0)" class="js-del"
                                       onclick="doDeleteById('${item.choiceId}')">删除</a></li>
							</ul>
						</div>
						</#if>
					</div>
				</div>
			</div>
			<div class="box-body">
				<div class="row chooseChartDiv" data-value="${item.choiceId!}">
					<div class="col-sm-4">
						<div class="chart-result-pie" id='pieChart${item_index}'style="height:160px;"></div>
					</div>
					<div class="col-sm-8">
						<div class="chart-result-bar" id='chart${item_index}' style="height:160px;"></div>
					</div>
				</div>
				<div class="text-right color-999 mt20">选课时间：${item.startTime} ~ ${item.endTime}</div>
			</div>
		</div>
	</#list>
	
	<#if choiceId?exists == false>
		<#assign allNum=0>
		<#if allList?exists>
		<#assign allNum=allList?size>
		</#if>
		<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="reload" allNum=allNum/>
	</#if>
	
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
			</span>
			<div class="no-data-body">
				<h3>暂无选课</h3>
				<p class="no-data-txt">请点击左上角的“发布选课”按钮发布选课</p>
			</div>
		</div>
	</div>
</#if>

<script>
$(function(){
    initModifyName();
	showBreadBack(goBack,true,"选课");
	//初始化单选控件
	initChosenOne(".header_filter");
});
<#if newGkChoiceDtoList?exists && (newGkChoiceDtoList?size>0)>
$(".chooseChartDiv").on("click",function(){
	//var cId=$(this).attr("data-value");
	//goChoiceResult(cId);
})
</#if>

function doReportChoice(gradeId) {
    var url = "${request.contextPath}/newgkelective/report/choice/index/page?gradeId=${gradeId!}";
    $("#showList").load(url);
}

function reload(pageIndex,pageSize,choiceId){
	if(!choiceId){
		choiceId = '';
	}
	<#--
	var pageSize = $('#pageSize').val();
	if(!pageIndex){
		pageIndex = $('#pagebar li.active a').text();
	}
	if(!pageSize){
		pageSize = ${pageInfo.pageSize!};
	}
	-->
	
	var url =  '${request.contextPath}/newgkelective/${gradeId!}/goChoice/index/page?choiceId='+choiceId;
	if(pageIndex){
		url=url+'&pageIndex='+pageIndex;
	}
	if(pageSize && pageSize!=""){
		url=url+'&pageSize='+pageSize;
	}
	
	$('#showList').load(url);
}
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
	
	reload(skipNum);
}
-->
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

function modiName(obj, newName, oldName, id){
	var nn = $.trim(newName);
	if(nn==''){
		layer.tips('名称不能为空！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		return;	
	}
	if(getLength(nn)>50){
		layer.tips('名称内容不能超过50个字节（一个汉字为两个字节）！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		return;	
	}
	if(nn==oldName){
        layer.tips('与原名称内容相同！',$(obj), {
            tipsMore: true,
            tips: 3
        });
		return;
	}
	
	nn=nn.replace(/\s/g, " ");
	
	$.ajax({
		url:'${request.contextPath}/newgkelective/choice/'+id+'/saveName',
		data: {'gradeId':'${gradeId!}','choiceName':nn},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layer.closeAll();
			  	layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				doEnterSetp('${gradeId}',1,1)	
	 		}else{
	 			obj.value=oldName;
	 			layer.tips(jsonO.msg,$(obj), {
					tipsMore: true,
					tips: 3
				});
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function doPublishChoice(gradeId){
	var url =  '${request.contextPath}/newgkelective/'+gradeId+'/choice/publish/page';
	$("#showList").load(url);
}

function goChoiceSetting(gradeId,choiceId){
	var url = '${request.contextPath}/newgkelective/'+gradeId+'/'+choiceId+'/choice/publish/page';
	$("#showList").load(url);
}

//设置默认值
function setChoiceDefault(gradeId,obj){
	var choiceId=$(obj).attr("data-value");
	var stat=$(obj).attr("data-stat");
	var tt="";
	if("1"==stat){
		tt="取消默认";
	}else{
		tt="设置默认";
	}
	var ii = layer.load();
	$.ajax({
		url:'${request.contextPath}/newgkelective/choice/'+choiceId+'/setDefault',
		data: {'gradeId':gradeId,'stat':stat},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layer.msg(tt+"成功", {
					offset: 't',
					time: 2000
				});
				<#--
				if("1"==stat){
	 				//直接置为默认
					$(obj).attr("data-stat",0);
					$(obj).text("置为默认");
					$("#choice_default_"+choiceId).removeClass("default-icon");
					$("#choice_default_"+choiceId).html("");
				}else{
					//其他按钮设置为置为默认
	 				//此处 取消默认
	 				refeshDefault(choiceId);
				}
				-->
	 		}else{
	 			layerTipMsg(jsonO.success,tt+"失败",jsonO.msg);
			}
			layer.close(ii);
            //changePageIndex(1);
			doEnterSetp('${gradeId}',1,1)
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function refeshDefault(choiceId){
	$(".js-my-default").each(function(){
		var chooseId=$(this).attr("data-value");
		if(choiceId==chooseId){
			$(this).attr("data-stat",1);
			$(this).text("取消默认");
			$("#choice_default_"+chooseId).addClass("default-icon");
			$("#choice_default_"+chooseId).html("");
		}else{
			$(this).attr("data-stat",0);
			$(this).text("置为默认");
			$("#choice_default_"+chooseId).removeClass("default-icon");
			$("#choice_default_"+chooseId).html("");
		}
	})
	
}

function goChoiceAnalysis(choiceId){
	var url = '${request.contextPath}/newgkelective/'+choiceId+'/choiceAnalysis/list/page';
	$("#showList").load(url);
}

function goChoiceResult(choiceId) {
	var url = '${request.contextPath}/newgkelective/'+choiceId+'/choiceResult/list/head';
	$("#showList").load(url);
}

function goChoiceResultItem(choiceId){
	var url = '${request.contextPath}/newgkelective/'+choiceId+'/chosen/tabHead/page';
	$("#showList").load(url);
}
<#--
	function doDelete(id){
		var url = "${request.contextPath}/newgkelective/choice/delete/page?choiceId="+id;
		indexDiv = layerDivUrl(url,{title: "删除",width:345,height:225});
	}
-->

function goBack(){
	var url =  '${request.contextPath}/newgkelective/index/page';
	$("#showList").load(url);
}
var isdelete=false;
function doDeleteById(id){
	if(isdelete){
		return;
	}
	isdelete=true;
	layer.confirm('确定删除吗？', function(index){
		var ii = layer.load();
			$.ajax({
			url:'${request.contextPath}/newgkelective/choice/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				layer.closeAll();
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			isdelete=true;
		 			layer.closeAll();
				  	doEnterSetp('${gradeId}',1,1)
		 		}else{
		 			isdelete=false;
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		
	},function(){
		isdelete=false;
	})

}
</script>
<script>
	$(function(){
		
		<#if newGkChoiceDtoList?exists && (newGkChoiceDtoList?size>0)>
			var chooseType=[1,0];
			<#list newGkChoiceDtoList as item>
			     var chart = document.getElementById('pieChart'+'${item_index}');
			     var s0${item.choiceId!} = echarts.init( chart );
			     s0${item.choiceId!}.setOption({
					title:{
					    text: '已选(${item.selectNum})',
						left: 'center',
						top: 'middle',
						textStyle: {
                            color: '#2b9cfe',
                            fontSize: 10
						}
					},
					color:['#317eeb', '#ccc'],
				    tooltip : {
				        trigger: 'item',
				        formatter: "{b} : {c}人"
				    },
				    series : [
				        {
			         		name:'选项',
					   		type:'pie',
					   		radius: ['50%', '70%'],
                            data:  [{value: ${item.selectNum}, name: '已选'},
                                {value: ${item.noSelectNum}, name: '未选'}],
							label: {
			         		    show: false
							},
							labelLine: {
			         		    show: false
							},
					        itemStyle: {
								normal: {
									color: function (params){
				                        var colorList = ['#2b9cfe','#b1d3ff'];
				                        return colorList[params.dataIndex];
				                    }
								}
							}
					    }
					]
			     });
			     s0${item.choiceId!}.on('click', function(params){
				    chooseEcharts('${item.choiceId!}',"",chooseType[params.dataIndex]);
				});
		    </#list>

		</#if>
		
		<#if newGkChoiceDtoList?exists && (newGkChoiceDtoList?size>0)>
			<#list newGkChoiceDtoList as item>
				var choiceIditem='${item.choiceId!}';
				var chart = document.getElementById('chart'+'${item_index}');
			    var jsonStringData1=jQuery.parseJSON('${item.jsonObject}');
			    var legendData1=jsonStringData1.legendData;
			    var loadingData1=jsonStringData1.loadingData;
			    var loadingSubjectIds${item.choiceId!}= jsonStringData1.legendIds;
			    var c0${item.choiceId!} = echarts.init( chart );
				c0${item.choiceId!}.setOption({
					color: ['#60b1cc'],
				    tooltip : {
				        trigger: 'item',
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
				    },
				    grid: {
				        left: '2%',
				        right: '2%',
				        bottom: '2%',
				        top: '10%',
				        containLabel: true,
				        borderColor: '#e8e8e8'
				    },
				    xAxis : [
				        {
				            type : 'category',
				            data : legendData1,
				            axisTick: {
				                alignWithLabel: true
				            }
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
				        }
				    ],
				    series : [
				        {
				            name:'直接访问',
				            type:'bar',
				            barWidth: '60%',
				            tooltip:{
				            	formatter: '{b} {c}人'
				            },
				            data:loadingData1,
				            label: {
				                normal: {
				                    show: true,
				                    color: '#333',
				                    position: 'top'
				                }
				            },
				            itemStyle: {
								normal: {
									color: function (params){
				                        var colorList = ['#47c6a4','#af89f3','#f46e97','#f58a53','#2b9cfe','#f8bd48','#71b0ff'];
				                        return colorList[params.dataIndex];
				                    }
								}
							}
				        }
				    ]
	        });
	        c0${item.choiceId!}.on('click', function(params){
			    var subjectIds=loadingSubjectIds${item.choiceId!}[params.dataIndex];
			    chooseEcharts('${item.choiceId!}',subjectIds,1);
			});
			</#list>
		</#if>
	});

function chooseEcharts(choiceId,subjectId,chooseItem){
	var url = '${request.contextPath}/newgkelective/'+choiceId+'/chosen/tabHead/page?scourceType=1&chosenType='+chooseItem;
	if(subjectId!=""){
		url=url+'&subjectIds='+subjectId;
	}
	$("#showList").load(url);
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
</script>	





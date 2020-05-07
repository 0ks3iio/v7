<div class="table-container">
	<div class="clearfix">
		<#if mymess?exists>
			<span class="tip tip-grey color-orange">
					重复考场有：${mymess!}
			</span>
		</#if>
        <div >
			<span class="tip tip-grey ">(考生总数：${allstuNum?default(0)}，可容纳考生：${arrangeNum?default(0)}位
			<#if (allstuNum?default(0)>arrangeNum?default(0))>
   				 ，还需要提供<em class="color:red;">${allstuNum-arrangeNum}</em>位
   			 <#else>
   				 ，考场已足够容纳考生，无需添加考场
  		    </#if>
    	)
		</span>
		<#if isEdit>
            <div class="filter">
                <div class="filter-item">
                    <button class="btn btn-blue" id="js-saveexaRoom">保存</button>
                    <button class="btn btn-white" id="js-chooseRoom" onclick="editPlaceNumber()">更改容纳人数</button>
					<#if (allstuNum?default(0)>arrangeNum?default(0))>
                        <button class="btn btn-white js-add" id="js-addexaRoom" onclick="addexaRoom()">添加考场</button>
					</#if>
                    <button class="btn btn-white" id="js-chooseRoom" onclick="importExanPlace()">导入</button>
                </div>
            </div>
			 <#--<#if (allstuNum?default(0)>arrangeNum?default(0))>
	    		<a href="javascript:" class="btn btn-blue js-addexaRoom" id="js-addexaRoom" onclick="addexaRoom()">添加考场</a>
	   		 </#if>
             <a href="javascript:" class="btn btn-blue js-chooseRoom" id="js-chooseRoom" onclick="editPlaceNumber()">更改容纳人数</a>
	   		 <a href="javascript:" class="btn btn-blue js-chooseRoom" id="js-chooseRoom" onclick="importExanPlace()">导入</a>

	   		 <a href="javascript:" class="btn btn-blue js-saveexaRoom" id="js-saveexaRoom">保存</a>-->
		</#if>
        </div>
	</div>
<#assign isShowStuNum=false>
<#if (arrangeStuNum>0)>
	<#assign isShowStuNum=true>
</#if>
	<form id="emplaceForm">
		<input type="hidden" name="examId" value="${examInfo.id!}"/>
		<div class="table-container-body j-exaplace">
			<table class="table table-striped table-hover j-exaplacetable">
				<#if (emPlaceList?exists && emPlaceList?size>0)>
                    <thead>
                    <tr>
                        <th style="width:5%">
                            <label class="pos-rel"><input type="checkbox" id="placeCheckboxAll" class="wp"  value="" onchange="placeCheckboxAllSelect()">
                                <span class="lbl" style="font-weight:bold;">全选</span></label>
                        </th>
                        <th class="">考试场地编号</th>
                        <th class="">考试场地</th>
                        <th class="">所属教学楼</th>
                        <th class="">可容纳人数</th>
						<#if isShowStuNum>
                            <th class="">已容纳人数</th>
						</#if>
						<#if isEdit>
                            <th class="">排序</th>
                            <th class="">操作</th>
						</#if>
                    </tr>
                    </thead>
                <tbody id="sortable">
					<#list emPlaceList as item>
					<tr>
	                    <td>
							<label class="pos-rel">
								<input   name="placeCheckboxName" type="checkbox" class="wp" value="${item.id!}">
								<span class="lbl"></span>
	                    	</label>
						</td>
						<td class="">${item.examPlaceCode!}</td>
						<td class="">${item.placeName!}</td>
						<td class="">${item.buildName!}</td>
						<td class=""><input class="table-input table-input-sm count_class" <#if !isEdit>disabled</#if> name="emPlaceList[${item_index}].count" type="text" value="${item.count?default(0)}" maxlength="3"></td>
						<#if isShowStuNum>
						<td class="">${item.stuNum}</td>
						</#if>
						<#if isEdit>
						<td class="text-center my-handle"><i class="glyphicon glyphicon-fullscreen"></i></td>
						<td class="">
						    <a class="color-blue js-outOfClass" href="javascript:;">删除</a>
						</td>
						</#if>
						<input type="hidden" name="emPlaceList[${item_index}].id" value="${item.id!}"/>
						<input type="hidden" name="emPlaceList[${item_index}].examPlaceCode" class="examPlaceCode_class" value="${item.examPlaceCode!}"/>
					    <input type="hidden" name="emPlaceList[${item_index}].placeId" value="${item.placeId!}"/>
					</tr>
					</#list>
				</tbody>
				<#else >
                    <div class="no-data-container">
                        <div class="no-data">
						<span class="no-data-img">
							<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
						</span>
                            <div class="no-data-body">
                                <p class="no-data-txt">暂无相关数据</p>
                            </div>
                        </div>
                    </div>
				</#if>
			</table>
		<div>
	</form>
</div>
<div class="layer layer-chooseType">
    <div class="layer-body">
        <div class="filter clearfix">
            <div class="filter-item block">
                <div class="filter-item block">
                    <label for="" class="filter-name" >容纳人数：</label>
                    <div class="filter-content">
                        <input type="text" class="form-control" id="count" name="count" placeholder="人数" maxLength="4">
                        <span class="lbl">人</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${request.contextPath}/static/sortable/Sortable.min.js"></script>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script>
$(function(){
		var a = document.getElementById('sortable');
		if(a){
			Sortable.create(a,{
				handle: '.my-handle',
				animation: 150,
				onUpdate: function (evt){ //拖拽更新节点位置发生该事件
			   		changeIndex();
			   }
			});
		}
<#if isEdit>
	$(".js-changeUp").on('click',function(){
       $(this).parent().parent().prev("tr").before($(this).parent().parent());
       changeIndex();

	});
	$(".js-changeDown").on('click',function(){
	   $(this).parent().parent().next("tr").after($(this).parent().parent()); 
	   changeIndex();
	});
	//表格删除
	$(".js-outOfClass").on('click',function(){
		var $this=$(this).parent().parent();
		showConfirmMsg('选择删除，请进行页面保存操作，否则删除无效！','提示',function(ii){
		 	$this.remove();
         	changeIndex();
         	layer.close(ii);
		});
	
	
         
	});
                
	</#if>
 });

function placeCheckboxAllSelect(){
    if($("#placeCheckboxAll").is(':checked')){
        $('input:checkbox[name=placeCheckboxName]').each(function(i){
            $(this).prop('checked',true);
        });
    }else{
        $('input:checkbox[name=placeCheckboxName]').each(function(i){
            $(this).prop('checked',false);
        });
    }
}

function editPlaceNumber() {
    var objElem = $('input:checkbox[name=placeCheckboxName]');
    var ids='';
    if(objElem.length>0){
        $('input:checkbox[name=placeCheckboxName]').each(function(i){
            if($(this).is(':checked')){
                ids=ids+","+$(this).val();
            }
        });
	}else{
        layerTipMsg(false,"提示","请先选择考场！");
        return;
    }
    if(ids==""){
        layerTipMsg(false,"提示","请先选择考场！");
        return;
    }
    isSubmit = false;
    layer.open({
        type: 1,
        shade: .5,
        area: '360px',
        title: ['更改容纳人数','font-size:20px;'],
        move: true,
        btn: ['确定','取消'],
        btnAlign: 'C',
        content: $('.layer-chooseType'),
        yes:function(){
            if(isSubmit){
                return;
            }
            var num = $('#count').val();
            if(num==''){
                layer.tips("不能为空", $("#count"), {
                    tipsMore: true,
                    tips:3
                });
                return;
            }else if (!/^\d+$/.test(num)) {
                layer.tips("请输入正整数", $("#count"), {
                    tipsMore: true,
                    tips:3
                });
                return;
            }else if(num<=0){
                layer.tips("请输入正整数", $("#count"), {
                    tipsMore: true,
                    tips:3
                });
                return;
            }
            isSubmit=true;
            var opts = {
                url : "${request.contextPath}/exammanage/examArrange/placeSaveAll",
                dataType : 'json',
				data:{'ids':ids,'num':num+""},
                success : function(data){
                    var jsonO = data;
                    if(!jsonO.success){
                        layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                        isSubmit=false;
                        return;
                    }else{
                        layer.closeAll();
                        layer.msg(jsonO.msg, {
                            offset: 't',
                            time: 2000
                        });
                        itemShowList('2');
                    }
                },
                clearForm : false,
                resetForm : false,
                type : 'post',
                error:function(XMLHttpRequest, textStatus, errorThrown){alert(111);}//请求出错
            };
			$("#emplaceForm").ajaxSubmit(opts);


        },
        btn2:function(index){
            layer.close(index);
        }
    });
}
  //表格序号排列
function changeIndex(){
  $('.j-exaplacetable > tbody > tr').each(function(i){
  	   var trFirstIndex = $(this).index();
  	   var trIndexChange = trFirstIndex+1;
  	   $(this).find('td:nth-child(2)').html(" ");
  	   if(trIndexChange <=9){
  	   	 $(this).find('td:nth-child(2)').html('00'+trIndexChange);
  	   	 $(this).find('.examPlaceCode_class').val('00'+trIndexChange);
  	   }else if ( 9 < trIndexChange <=99){
  	   	 $(this).find('td:nth-child(2)').html('0'+trIndexChange);
  	   	 $(this).find('.examPlaceCode_class').val('0'+trIndexChange);
  	   }else if ( 99 < trIndexChange <=999){
  	   	 $(this).find('td:nth-child(2)').html(trIndexChange);
  	   	 $(this).find('.examPlaceCode_class').val(trIndexChange);
  	   }
  });
}



var isSubmit=false;
$("#js-saveexaRoom").on("click", function(){
	if(isSubmit){
        return;
	}
	changeIndex();
	isSubmit=true;
	var ff=false;
	$('.count_class').each(function(i){
		var mm=$(this).val().trim();
		if(mm==''){
			layer.tips("不能为空", $(this), {
				tipsMore: true,
				tips:3		
			});
			if(!ff){
				ff=true;
			}
		}else if (!/^\d+$/.test(mm)) {
			layer.tips("请输入正整数", $(this), {
					tipsMore: true,
					tips:3		
				});
			if(!ff){
				ff=true;
			}
		}else if(mm<=0){
			layer.tips("请输入正整数", $(this), {
					tipsMore: true,
					tips:3		
				});
			if(!ff){
				ff=true;
			}
		}
		
	});
	if(ff){
		isSubmit=false;
		$("#js-saveexaRoom").removeClass("disabled");
		return;
	}
	$(this).addClass("disabled");
	var options = {
		url : "${request.contextPath}/exammanage/examArrange/placeSaveAll",
		dataType : 'json',
        data:{'ids':"",'num':""},
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#js-saveexaRoom").removeClass("disabled");
	 			isSubmit=false;
	 			return;
	 		}else{
	 			layer.closeAll();
			  	layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
				itemShowList('2');
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){alert(111);}//请求出错 
	};

	$("#emplaceForm").ajaxSubmit(options);
	layer.close(ii);
	isSubmit=false;
	$("#js-saveexaRoom").removeClass("disabled");
 });
function addexaRoom(){
	var url = "${request.contextPath}/exammanage/examArrange/placeAdd/page?examId=${examInfo.id!}";
	indexDiv = layerDivUrl(url,{title: "添加考场",width:450,height:450});
}
function importExanPlace(){
	var url="${request.contextPath}/exammanage/examPlace/main?examId=${examInfo.id!}";
	$("#showTabDiv").load(url);
}
</script>
													
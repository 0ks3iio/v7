<#if (dtolist?exists && dtolist?size>0)>
	<div class="filter">
		<div class="filter-item">
			<button class="btn btn-blue" id = "js-arrange">自动分配</button>
		</div>
	</div>
	<form id="emplaceForm">
		<input type="hidden" name="examId" value="${examInfo.id!}"/>
		<table class="table table-bordered table-striped table-hover no-margin">
			<thead>
				<tr>
					<th>科目</th>
					<th>参考人数</th>
					<th>已分配考场数量</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<#list dtolist as dto>
					<tr>
						<td>${dto.groupName!}</td>
						<td>${dto.arrangeNum!}</td>
						<td>${dto.arrangePlaceNum!}<span class="color-999">(可容纳：${dto.arrangeStuNum!}位，待容纳：<span class="color-orange">${dto.noArrangeStuNum!}</span>位）</span></td>
						<td class="">
						    <a class="color-blue js-distribute" href="javascript:;" onclick="toGroupPlacePage('${dto.groupId!}',${dto.arrangePlaceNum!},${dto.noArrangeStuNum!})">手动分配</a>
						</td>
					</tr>
				</#list>
			</tbody>
		</table>
		</form>
<#else>
	<div class="no-data-container">
		<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
				</span>
			<h3 class="no-data-body">
				还未设置过考试科目
			</h3>
		</div>
	</div>
</#if>
<!-- 手动分配 -->
<div id="showlayer" style=" ">
    <#--<div class="layer layer-distribute">
        <div class="layer-content">
            <p>
                <span class="mr20"><span class="color-999">平均容纳：</span>物理（选考）</span>
                <span class="mr20"><span class="color-999">参考人数：</span>54</span>
                <span class="mr20"><span class="color-999">已选考场：</span>54</span>
                <span><span class="color-999">待容纳人数：</span>54</span>
            </p>
            <table class="table table-bordered table-striped table-hover no-margin">
                <thead>
                <tr>
                    <th>
                        <label><input type="checkbox" name="" class="wp"><span class="lbl"></span></label>
                    </th>
                    <th>考场编号</th>
                    <th>考场场地</th>
                    <th>所属教学楼</th>
                    <th>可容纳人数</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <label><input type="checkbox" name="" class="wp"><span class="lbl"></span></label>
                    </td>
                    <td>001</td>
                    <td>考场1</td>
                    <td>教研室</td>
                    <td>160</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>-->
</div>

<script>
var isSubmit=false;
$("#js-arrange").on("click", function(){	
	if(isSubmit){
        return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var options = {
		url : "${request.contextPath}/exammanage/examArrange/placeArrange/arrangeAutoSave",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"编排失败",jsonO.msg);
	 			$("#js-arrange").removeClass("disabled");
	 			isSubmit=false;
	 			return;
	 		}else{
	 			layer.closeAll();
			  	layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
				isSubmit=false;
				$("#js-arrange").removeClass("disabled");
				itemShowList('8');
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){alert(111);}//请求出错 
	};
	$("#emplaceForm").ajaxSubmit(options);
 });
<#--function toGroupPlacePage(groupId){-->
	<#--var url =  '${request.contextPath}/exammanage/examArrange/placeArrange/placeGroupIndex?examId=${examInfo.id!}&groupId='+groupId;-->
	<#--$("#showTabDiv").load(url);-->
<#--}-->
function toGroupPlacePage(groupId,arrangePlaceNum,noArrangeStuNum){
    var url =  '${request.contextPath}/exammanage/examArrange/placeArrange/placeGroupIndex?examId=${examInfo.id!}&groupId='+groupId+'&arrangePlaceNum='+arrangePlaceNum+'&noArrangeStuNum='+noArrangeStuNum;
    $("#showlayer").load(url,function () {
        var index = layer.open({
            type: 1,
            shadow: 0.5,
            title: '手动分配',
            area: '560px',
            btn: ['确定', '取消'],
            scrollbar:false,    //是否允许浏览器出现滚动条。默认为true
            //content:url,
            content: $('.layer-distribute'),
            yes:function(index,layerDiv){
                if(isSubmit){
                    return;
                }
                isSubmit=true;
                var ff=false;
                $(this).addClass("disabled");
                var options = {
                    url : "${request.contextPath}/exammanage/examArrange/placeArrange/groupPlaceSave",
                    dataType : 'json',
                    success : function(data){
                        var jsonO = data;
                        if(!jsonO.success){
                            layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                            $("#js-save").removeClass("disabled");
                            isSubmit=false;
                            return;
                        }else{
                            layer.closeAll();
                            layer.msg(jsonO.msg, {
                                offset: 't',
                                time: 2000
                            });
                            itemShowList('8');
                            isSubmit=false;
                            $("#js-save").removeClass("disabled");
                        }
                    },
                    clearForm : false,
                    resetForm : false,
                    type : 'post',
                    error:function(XMLHttpRequest, textStatus, errorThrown){alert(111);}//请求出错
                };

                $("#emplaceForm1").ajaxSubmit(options);

            },
            btn2:function(index){
                layer.close(index);
            }
        });
    });
}

</script>
													
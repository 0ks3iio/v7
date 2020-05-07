<#--<a href="javascript:" class="page-back-btn gotoArrangeClass" onclick="backArrayIndex()"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${newArray.arrayName!} - 查看排课设置</h3>
	</div>
	<div class="box-body">
		<#if divideDto?exists>
		<div class="box box-primary">
			<div class="box-header">
				<h3 class="tt box-title">${divideDto.ent.divideName!}</h3>
				&nbsp;
				<#if divideDto.ent.openType?default('')=='01'>
				<span class="badge badge-pink position-relative top-2">
				全固定模式
				</span>
				<#elseif divideDto.ent.openType?default('')=='02'>
				<span class="badge badge-green position-relative top-2">
				半固定模式
				</span>
				<#elseif divideDto.ent.openType?default('')=='06'>
				<span class="badge badge-orange position-relative top-2">
				全手动模式
				</span>
				<#elseif divideDto.ent.openType?default('')=='08'>
				<span class="badge badge-purple position-relative top-2">
				智能组合分班
				</span>
				<#elseif divideDto.ent.openType?default('')=='05'>
				<span class="badge badge-skyblue position-relative top-2">
				全走单科分层模式
				</span>
				<#elseif divideDto.ent.openType?default('')=='03'>
				<span class="badge badge-skyblue position-purple top-2">
				文理科分层教学模式 — 语数外独立分班
				</span>
				<#elseif divideDto.ent.openType?default('')=='04'>
				<span class="badge badge-skyblue position-purple top-2">
				文理科分层教学模式 — 语数外跟随文理组合分班
				</span>
				<#elseif divideDto.ent.openType?default('')=='05'>
				全走班单科分层
				<#elseif divideDto.ent.openType?default('')=='09'>
				<span class="badge badge-skyblue position-purple top-2">
					3+1+2单科分层（重组）
				</span>
				<#elseif divideDto.ent.openType?default('')=='10'>
				<span class="badge badge-skyblue position-purple top-2">
					3+1+2组合固定（重组）
				</span>
				<#elseif divideDto.ent.openType?default('')=='11'>
				<span class="badge badge-skyblue position-purple top-2">
				3+1+2单科分层（不重组）
				</span>
				<#elseif divideDto.ent.openType?default('')=='12'>
				<span class="badge badge-skyblue position-purple top-2">
				3+1+2组合固定（不重组）
				</span>
		  		</#if>
			</div>
			<div class="box-body">
				<div class="explain">
                    <p>
                        <span>创建时间：${(divideDto.ent.creationTime?string('yyyy-MM-dd HH:mm:ss'))!}</span>
                        <span class="float-right">
                            <a class="color-blue dv-detail-btn" item-id='${divideDto.ent.id!}' href="javascript:">查看结果</a>
                            <span class="color-lightblue"> | </span>
                            <span class="modify-name divide-modi position-relative">
                                <a class="color-blue position-relative" href="javascript:void(0);">修改名称</a>
                            </span>
                        </span>
                    </p>
				</div>
				<#assign isSeven=true>
				<#if divideDto.ent.openType?default('')=='03' || divideDto.ent.openType?default('')=='04'>
					<#assign isSeven=false>
				</#if>
				<#assign showcourseList=divideDto.showCourseList>
				<#assign showFen=divideDto.showFen>
				<table class="table table-bordered table-striped table-hover">
                    <thead>
						<tr>
						    <th class="text-center" rowspan="2">类别</th>
							<th class="text-center" rowspan="2">行政班</th>
							<#if isSeven>
							<th class="text-center" rowspan="2">3科组合班</th>
							<th class="text-center" rowspan="2">2科组合班</th>
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
								<th class="text-center">${fen!}</th>
								</#list>
							</#list>
							</#if>										
						</tr>
					</thead>
					<tbody>	
					    <tr>
							<th class="text-center">班级个数</th>
							<td class="text-center">${divideDto.xzbAllclassNum?default(0)}</td>
							<#if isSeven>
							<td class="text-center">${divideDto.threeAllclassNum?default(0)}</td>
							<td class="text-center">${divideDto.twoAllclassNum?default(0)}</td>
							</#if>
							<#assign abAllclassNum=divideDto.abAllclassNum>
							<#if showcourseList?exists && showcourseList?size gt 0>
							<#list showcourseList as courseItem>
								<#list showFen as fen>
									<#assign key1=courseItem[0]+"_"+fen>
									<td class="text-center">${abAllclassNum[key1]?default(0)}</th>
								</#list>
							</#list>
							</#if>	
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		</#if>
		<#if itemMap['01']?exists>
		<#assign itemItem=itemMap['01']>
		<div class="box box-primary">
			<div class="box-header">
				<h3 class="tt box-title">${itemItem.itemName!}</h3>
			</div>
			<div class="box-body">
				<div class="explain">
					<p>
                        <span>
                            创建时间：${(itemItem.creationTime?string('yyyy-MM-dd HH:mm:ss'))!}
                        </span>
                        <span class="float-right">
                            <a class="color-blue position-relative pl-detail-btn" item-id="${newArray.placeArrangeId}" href="javascript:void(0);">查看设置</a>
                            <span class="color-lightblue"> | </span>
                            <span class="modify-name position-relative">
                                <a class="color-blue position-relative modify-name" href="javascript:void(0);">修改名称</a>
                            </span>
                        </span>
                    </p>
				</div>
				<div class="number-container">
					<ul class="number-list">
						<li><em>${itemItem.countPlace!}</em><span>教学楼教室</span></li>
						<#if itemItem.newGkItemDto?exists && itemItem.newGkItemDto.num?exists>
						<#assign t = itemItem.newGkItemDto.num?size>								
						<#list 0..t as c>
						    <li><em>${itemItem.newGkItemDto.num[c]!}</em><span>${itemItem.newGkItemDto.typeName[c]!}</span></li>
						</#list>	
						</#if>	
					</ul>
				</div>
			</div>
		</div>
		</#if>
		<#if itemMap['04']?exists>
		<#assign itemItem=itemMap['04']>
		<div class="box box-primary">
			<div class="box-header">
				<h3 class="tt box-title">${itemItem.itemName!}</h3>
			</div>
			<div class="box-body">
				<div class="explain">
					<p>
                        <span>
                            创建时间：${(itemItem.creationTime?string('yyyy-MM-dd HH:mm:ss'))!}
                        </span>
                        <span class="float-right">
                            <a class="color-blue detail-btn" item-id="${newArray.lessonArrangeId}" href="javascript:void(0);" li-id="01">年级特征</a>
                            <span class="color-lightblue"> | </span>
                            <a class="color-blue detail-btn" item-id="${newArray.lessonArrangeId}" href="javascript:void(0);" li-id="02">课程特征</a>
                            <span class="color-lightblue"> | </span>
                            <a class="color-blue detail-btn" item-id="${newArray.lessonArrangeId}" href="javascript:void(0);" li-id="05">班级特征</a>
                            <span class="color-lightblue"> | </span>
                            <a class="color-blue detail-btn" item-id="${newArray.lessonArrangeId}" href="javascript:void(0);" li-id="03">教师特征</a>
                            <span class="color-lightblue"> | </span>
                            <a class="color-blue detail-btn" item-id="${newArray.lessonArrangeId}" href="javascript:void(0);" li-id="04">课表设置</a>
                            <span class="color-lightblue"> | </span>
                            <span class="modify-name position-relative">
                                <a class="color-blue position-relative" href="javascript:void(0);">修改名称</a>
                            </span>
                        </span>
                    </p>
				</div>
                <table class="table table-bordered table-striped table-hover">
                    <thead>
                    <tr>
                        <th class="text-center">周课时/科目教师数</th>
                        <#if itemItem.newGkItemDto?exists && itemItem.newGkItemDto.num?exists>
                        <#assign t = itemItem.newGkItemDto.num?size>
                        <#list 0..t-1 as c>
                            <th class="text-center">${itemItem.newGkItemDto.typeName[c]!}</th>
                        </#list>
                        </#if>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="text-center">数量</td>
                        <#if itemItem.newGkItemDto?exists && itemItem.newGkItemDto.num?exists>
                            <#assign t = itemItem.newGkItemDto.num?size>
                            <#list 0..t-1 as c>
                            <td class="text-center">${itemItem.newGkItemDto.num[c]!}</td>
                            </#list>
                        </#if>
                    </tr>
                    </tbody>
                </table>
				<#--<div class="number-container">
					<ul class="number-list">
						<#if itemItem.newGkItemDto?exists && itemItem.newGkItemDto.num?exists>
						<#assign t = itemItem.newGkItemDto.num?size>								
						<#list 0..t as c>
						    <li><em>${itemItem.newGkItemDto.num[c]!}</em><span>${itemItem.newGkItemDto.typeName[c]!}</span></li>
						</#list>	
						</#if>	
					</ul>
				</div>-->
			</div>
		</div>
		</#if>

	</div>
</div>
<script>
    var placeArrangeId='${newArray.placeArrangeId?default('')}';
    var lessonArrangeId='${newArray.lessonArrangeId?default('')}';

	$(function(){
	    initPlDetail();
	    initLtDetail();
	    initDvDetail();
	    initModiName();
		showBreadBack(backArrayIndex,false,"排课设置");
	})
	function backArrayIndex(){
		var url =  '${request.contextPath}/newgkelective/${newArray.gradeId!}/goArrange/index/page';
		$("#showList").load(url);
	}

    function getArrayParams(){
        return "&lessArrayId="+lessonArrangeId+"&plArrayId="+placeArrangeId;
    }

    function initPlDetail(){
        $('.pl-detail-btn').off('click').on('click', function(){
            var itemId = $(this).attr('item-id');
            var url='${request.contextPath}/newgkelective/${newDivide.id}/placeArrange/list?fromSolve=1&arrayItemId='+itemId+'&arrayId=${newArray.id!}'+getArrayParams();
            $("#showList").load(url);
        });
    }

    function initLtDetail(){
        $('.detail-btn').off('click').on('click', function(){
            var itemId = $(this).attr('item-id');
            var liId = $(this).attr('li-id');
            var url = "${request.contextPath}/newgkelective/${newDivide.id}/gradeArrange/edit?fromSolve=1&arrayId=${newArray.id!}&arrayItemId="+itemId+"&toLi="+liId+getArrayParams();;
            $("#showList").load(url);
        });
    }

    function initDvDetail(){
        $('.dv-detail-btn').off('click').on('click', function(){
            var itemId = $(this).attr('item-id');
            var url = "${request.contextPath}/newgkelective/"+itemId+"/divideClass/resultClassList?fromSolve=1&fromArray=1&arrayId=${newArray.id!}"+getArrayParams();
            $("#showList").load(url);
        });
    }

    function initModiName(){
        $(".modify-name").each(function(){
            if($(this).find('.modify-name-layer').length > 0){
                return;
            }
            var tn = $(this).parents('.box-primary').find('.tt').text();
            var itemId = $(this).prev().prev().attr('item-id');
            var dc = ''
            if($(this).hasClass('divide-modi')){
                dc = 'divide-modi';
            }
            var modifyNameLayer = '<div class="modify-name-layer hidden" id="modiv'+itemId+'"style="width:350px;">\
						<h5>修改名称</h5>\
						<p><input type="text" class="form-control" placeholder="请输入名称" value="'+tn+'"></p>\
						<div class="text-right" item-id="'+itemId+'"><button class="btn btn-sm btn-white modi-cancel">取消</button><button class="btn btn-sm btn-blue modi-ok ml10 '+dc+'">确定</button></div>\
					</div>';
            $(this).append(modifyNameLayer);
        });

        $(".modify-name a").off('click').click(function(e){
            e.preventDefault();
            var tn = $(this).parents('.box-primary').find('.tt').text();
            $(this).parent().find(".form-control").val(tn);
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
            var oldName = $(this).parents('.box-primary').find('.tt').text();
            var itemId = $(this).parent().attr('item-id');
            var isDivide = $(this).hasClass('divide-modi');
            var obj = e.target;
            var rev = modifyName(newName,oldName,itemId,isDivide,obj);
        });

    }

    var isClick=false;
    function modifyName(newName,oldName,arrayItemId,isDivide,obj){
        if(isClick){
            return false;
        }
        isClick=true;
        var nn = $.trim(newName);
        if(nn==''){
            isClick=false;
            layer.tips('名称不能为空！',$(obj).parent().prev(), {
                tipsMore: true,
                tips: 3
            });
            $(obj).focus();
            return false;
        }
        if(nn==oldName){
            isClick=false;
            $("#modiv"+arrayItemId).addClass('hidden');
            return true;
        }

        var result = false;
        if(!isDivide){
            if(getLength(nn)>80){
                isClick=false;
                layer.tips('名称内容不能超过80个字节（一个汉字为两个字节）！',$(obj).parent().prev(), {
                    tipsMore: true,
                    tips: 3
                });
                $(obj).focus();
                return false;
            }
            $.ajax({
                url:'${request.contextPath}/newgkelective/'+arrayItemId+'/gradeArrange/changName',
                data: {'itemName':newName},
                type:'post',
                dataType:'JSON',
                success:function(data) {
                    result = modiSuccess(data,arrayItemId,oldName,newName,obj);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        } else {
            if(getLength(nn)>50){
                isClick=false;
                layer.tips('名称内容不能超过50个字节（一个汉字为两个字节）！',$(obj).parent().prev(), {
                    tipsMore: true,
                    tips: 3
                });
                $(obj).focus();
                return false;
            }
            $.ajax({
                url:'${request.contextPath}/newgkelective/${newArray.gradeId!}/goDivide/saveName',
                data: {'divideId':arrayItemId,'divideName':newName},
                type:'post',
                dataType:'JSON',
                success:function(data) {
                    result = modiSuccess(data,arrayItemId,oldName,newName,obj);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        }
        return result;
    }

    function modiSuccess(data,itemId,oldName,newName,obj){
        isClick=false;
        if(data.success){
            layer.closeAll();
            layer.msg(data.msg, {
                offset: 't',
                time: 2000
            });
            $("#modiv"+itemId).parents('.box-primary').find('.tt').text(newName);
            $("#modiv"+itemId).addClass('hidden');
            return true;
        }else{
            obj.value=oldName;
            layer.tips(data.msg,$(obj).parent().prev(), {
                tipsMore: true,
                tips: 3
            });
            return false;
        }
    }
</script>

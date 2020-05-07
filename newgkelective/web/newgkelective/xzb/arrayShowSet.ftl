<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${newArray.arrayName!} - 查看排课设置</h3>
	</div>
	<div class="box-body">
		<#if divideDto?exists>
		<div class="box box-primary">
			<div class="box-header">
				<h3 class="tt box-title">${divideDto.ent.divideName!}</h3>
			</div>
			<div class="box-body">
				<div class="explain">
                    <p>
                        <span>创建时间：${(divideDto.ent.creationTime?string('yyyy-MM-dd HH:mm:ss'))!}</span>
                        <span class="float-right">
                            <span class="position-relative" item-id="${newArray.divideId!}">
                                <a class="color-blue position-relative" onclick="showClassList(this);"  href="javascript:void(0);">班级详情</a>
                            </span>
                            <span class="color-lightblue"> | </span>
                            <span class="modify-name divide-modi position-relative" item-id="${newArray.divideId!}">
                                <a class="color-blue position-relative"  href="javascript:void(0);">修改名称</a>
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
						    <th class="text-center" rowspan="1">类别</th>
							<th class="text-center" rowspan="1">行政班</th>
						</tr>
						
					</thead>
					<tbody>	
					    <tr>
							<th class="text-center">班级个数</th>
							<td class="text-center">${divideDto.xzbAllclassNum?default(0)}</td>
						</tr>
					</tbody>
				</table>
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
                            <span class="modify-name position-relative" item-id="${newArray.lessonArrangeId}">
                                <a class="color-blue position-relative" href="javascript:void(0);">修改名称</a>
                            </span>
                        </span>
                    </p>
				</div>
				<div class="number-container">
					<ul class="number-list">
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

	</div>
</div>
<script>
    var placeArrangeId='${newArray.placeArrangeId?default('')}';
    var lessonArrangeId='${newArray.lessonArrangeId?default('')}';

	$(function(){
	   // initPlDetail();
	    initLtDetail();
	    initDvDetail();
	    initModiName();
		showBreadBack(backArrayIndex,false,"排课设置");
	})
	function backArrayIndex(){
		var url =  '${request.contextPath}/newgkelective/xzb/index/page';
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
			var url = "${request.contextPath}/newgkelective/xzb/gradeArrange/edit?divideId=${newDivide.id}&arrayId=${newArray.id!}&arrayItemId="+itemId+"&fromSolve=1&toLi="+liId+getArrayParams();;
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
            var itemId = $(this).attr('item-id');
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
                url:'${request.contextPath}/newgkelective/xzb/saveDivideName',
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
    
    function showClassList(obj){
		var url = "${request.contextPath}/newgkelective/xzb/${newArray.divideId}/divideResult/index?arrayId=${newArray.id!}";
		$("#showList").load(url);
	}
</script>

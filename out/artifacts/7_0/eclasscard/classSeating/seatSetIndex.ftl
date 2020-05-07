<!-- 确认框 -->
<div class="layer layer-confirm" id="seat-clear">
    <div class="layer-content">
        <table width="100%">
            <tr>
                <td class="text-right" valign="top">
                    <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                </td>
                <td>
                    <div class="font-16 mb10">确定要清空座位表上的学生吗？</div>
                    <div class="color-grey">清空后将不可恢复。</div>
                </td>
            </tr>
        </table>
    </div>
    <div class="layer-footer">
        <a class="btn btn-blue" id="btn-clear-confirm">确定</a>
        <a class="btn btn-grey" id="btn-clear-close">取消</a>
    </div>
</div>
<div class="box box-default"  id="box-default">
    <div class="box-header no-border">
        <table class="table table-bordered">
            <tbody class="picker-table picker-table-a">
            <tr>
                <td style="width:120px;" class="text-right h4 bgc-f4f4f4"><strong>其他条件：</strong></td>
                <td colspan="2">
                	<div class="filter-item">
                        <span class="filter-name">年级：</span>
                        <div class="filter-content">
                            <select class="form-control select" id="grade-select" onchange="doChangeGrade('2')">
                                <#if gradeList?? && gradeList?size gt 0>
		                            <#list gradeList as grade>
		                                <option value="${grade.gradeId}"<#if gradeId?default('')==grade.gradeId>selected</#if>>${grade.gradeName}</option>
		                            </#list>
		                        <#else>
		                            <option value="">暂无数据</option>
		                        </#if>
                            </select>
                        </div>
                    </div>
                    <div class="filter-item">
                        <span style="margin:0 20px;">|</span>
                    </div>
                    <div class="filter-item">
                        <span class="filter-name" id="class-id-data" data="">班级类型：</span>
                        <div class="filter-content">
                            <select class="form-control select" id="class-type" onchange="classTypeChange()">
                                <option value="1" <#if classType?default('1')=='1'>selected</#if>>行政班</option>
                        		<option value="2" <#if classType?default('1')=='2'>selected</#if>>教学班</option>
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
            <#-- 教学班-->
            <tr class="teach-class hide">
                <td class="text-right h4 bgc-f4f4f4"><strong>科目</strong>：
                </td>
                <td style="border-right:none">
                    <div class="outter single-choice" id="sub-select">
                    </div>
                </td>
                <td width="100" class="text-right" style="vertical-align: top; border-left:none">
                    <div class="outter">
                        <a class="picker-more" href="#"><span>展开</span
                            ><i class="fa fa-angle-down"></i
                            ></a>
                    </div>
                </td>
            </tr>
            <tr class="teach-class hide">
                <td class="text-right h4 bgc-f4f4f4"><strong>班级</strong>：
                </td>
                <td style="border-right: none">
                    <div class="outter single-choice" id="teach-class-select">

                    </div>
                </td>
                <td width="100" class="text-right" style="vertical-align: top; border-left:none">
                    <div class="outter">
                        <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                    </div>
                </td>
            </tr>
            <#-- 行政班-->
            <tr class="admin-class">
                <td class="text-right h4 bgc-f4f4f4"><strong>班级</strong>：
                </td>
                <td style="border-right: none">
                    <div class="outter single-choice" id="class-select">
                        <#-- 行政班 -->
                    </div>
                </td>
                <td width="100" class="text-right" style="vertical-align: top; border-left:none">
                    <div class="outter">
                        <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="filter" style="padding: 0px 0px 20px 0px;">
	        <div class="filter-item">
	            <button class="btn btn-blue" onclick="doSeatSetImport()">导入</button>
	            <button class="btn btn-default js-new" onclick="findSeatSet()">座位表设置</button>
	            <button class="btn btn-default js-confirm">清空</button>
	        </div>
        </div>
        <div id="seat-table-contain">

        </div>
    </div>
</div>

<style>
	.table-height tr {
	    height: 50px;
	}
	
	.numlist {
	    position: absolute;
	    left: -10px;
	    width: 20px;
	}
	
	.numlist li {
	    margin-top: 30px
	}
	
	.education {
	    display: none;
	}
	
	.select {
	    /*关键：将默认的select选择框样式清除*/
	    appearance: none;
	    -moz-appearance: none;
	    -webkit-appearance: none;
	    background: url("${request.contextPath}/static/assets/images/newdown.png") no-repeat  115px transparent;
	    background-size: 15px;
	    width: 80px;
	    margin-left: 20px;
	}
	select::-ms-expand {
    	display: none;
	}
	.picker-table-a a {
	    padding: 4px 10px;
	}
	
	.picker-table-a .filter-item {
	    margin-bottom: 0;
	    padding-top: 10px;
	}
	
	.desk_table {
	    min-width: 1000px;
	}


</style>
<script>
    var searchSubjectId = '${subjectId!}';
    var searchClassId = '${classId!}';
    $(function () {
        hideTable();
        doChangeGrade('1');
        // 清空 确认框,
        $('.js-confirm').on('click', function () {
            layer.open({
                type: 1,
                shadow: 0.5,
                closeBtn: true,
                title: false,
                area: '350px',
                content: $('.layer-confirm')
            });
        });
    })
    
    function doChangeGrade(iniType){
   		var gradeId = $('#grade-select').val().trim();
   		if (gradeId == undefined || gradeId == '') {
   			//没有参数
            return;
        }
        $.ajax({
			url:"${request.contextPath}/eclasscard/standard/getClassListByGradeId",
			data:{"gradeId":gradeId},
			type:'post',
			dataType:'json',
			success:function(result){
	            if (result['code'] == '00') {
	            	//显示行政班，教学班
	            	$('#teach-class-select').html('');
	                $('#class-select').html('');
	                $('#sub-select').html('');
	                $('#class-id-data').attr('data', '');
	                var classType=$('#class-type').val();
	                if(classType=="1"){
	                	$('.teach-class').addClass("hide");
	                    $('.admin-class').removeClass("hide");
	                }else{
	                	$('.admin-class').addClass("hide");
	                	$('.teach-class').removeClass("hide");
	                }
	                //班级数据  行政班
	                var adminClass = result['classList'];
	                if (adminClass != null && adminClass.length > 0) {
	                    for (var i = 0; i < adminClass.length; i++) {
	                        var e = '<a class="both-class" href="#" data="' + adminClass[i]['classId'] + '">' + adminClass[i]['className'] + '</a>';
	                        $('#class-select').append(e);
	                    }
	                }
	                //科目数据  教学班
	                var subList = result['subList'];
	                if (subList != null && subList.length > 0) {
	                    for (var i = 0; i < subList.length; i++) {
	                        var e = '<a class="subject" href="#" data="' + subList[i]['subId'] + '">' + subList[i]['subName'] + '</a>';
	                        $('#sub-select').append(e);
	                    }
	                }
	            	initDate(iniType);
	            }
			}
		});
    	
    }
    
    
    //默认选中参数
    function initDate(iniType){
    	$('#class-id-data').attr('data', '');
    	var type = $('#class-type').val();
    	if(type=="1"){
    		if(iniType=="1" && searchClassId != '' && searchClassId != undefined){
    			if($('.both-class[data=' + searchClassId + ']').length>0){
    				 $('.both-class[data=' + searchClassId + ']').click();
    			}else{
    				$('#class-select').children(':first').click();
    			}
    		}else{
    			$('#class-select').children(':first').click();
    		}
    	}else{
    		$(".teach-class .subject").removeClass("selected");
    		if(iniType=="1" && searchSubjectId != '' && searchSubjectId != undefined){
    			if( $('.subject[data=' + searchSubjectId + ']').length>0){
    				$('.subject[data=' + searchSubjectId + ']').addClass("selected")
    			}else{
    				$('#sub-select').children(':first').addClass("selected");
    			}
    		}else{
    			//取第一个
    			$('#sub-select').children(':first').addClass("selected");
    		}
    		var subId=$('#sub-select').find(".selected").attr("data");
    		subChange(subId,iniType);
    	}
    	
    }
    //选中某个科目
    function subChange(subId,iniType) {
        hideTable();
        if(!subId != '' && subId != undefined){
        	$('#teach-class-select').html('');
        	return;
        }
        var gradeId= $('#grade-select').val();
        $.ajax({
			url:"${request.contextPath}/eclasscard/standard/getTeachClass",
			data:{"gradeId":gradeId,"subjectId":subId},
			type:'post',
			dataType:'json',
			success:function(data){
				$('#teach-class-select').html('');
				$('#class-id-data').attr('data', '');
				var classList = data['classList'];
                if (classList != null && classList.length > 0) {
                    for (var i = 0; i < classList.length; i++) {
                        var e = '<a class="both-class teach-class" href="#" data="' + classList[i]['classId'] + '">' + classList[i]['className'] + '</a>';
                        $('#teach-class-select').append(e);
                    }
                }
                if(iniType=="1" && searchClassId != '' && searchClassId != undefined){
	    			if($('.both-class[data=' + searchClassId + ']').length>0){
	    				 $('.both-class[data=' + searchClassId + ']').click();
	    			}else{
	    				$('#teach-class-select').children(':first').click();
	    			}
	    		}else{
	    			$('#teach-class-select').children(':first').click();
	    		}
			}
		});
        

    }
    
    
    

    //班级类型切换  1: 行政班   2:教学班
    function classTypeChange() { 
        hideTable();
        var type=$("#class-type").val();
        if (type == '1') {
            $('.teach-class').addClass("hide");
            $('.admin-class').removeClass("hide");
        } else {
            $('.teach-class').removeClass("hide");
            $('.admin-class').addClass("hide");
        }
        initDate('2');
    }


    //教学班,科目切换
    $('.teach-class').on('click', '.subject', function () {
    	$(".teach-class .subject").removeClass("selected");
    	$(this).addClass("selected");
        subChange($(this).attr("data"),'2');
    })

    

    //点击班级
    $('.table-bordered').on('click', '.both-class', function () {
        //按照数据画页面上的表格
        $('.both-class').removeClass("selected");
        $(this).addClass("selected");
        var classId = $(this).attr("data");
        $('#class-id-data').attr('data', classId);
		var url = '${request.contextPath}/eclasscard/standard/seatTable?classId=' + classId;
        $('#seat-table-contain').load(url);
    })
    
    


    //弹出座位设置窗口
    function findSeatSet() {
        var url = '${request.contextPath}/eclasscard/standard/seatSet';
        var classId = $('#class-id-data').attr('data');
        if (classId == undefined || classId == '') {
            layer.msg('请先选择一个班级');
            return;
        } else {
            url += '?classId=' + classId;
        }
        indexDiv = layerDivUrl(url, {title: "座位表设置", width: 400, height: 320});
    }

    // 取消按钮操作功能
    $("#btn-clear-close").on("click", function () {
        doLayerOk("#btn-clear-confirm", {
            redirect: function () {
            },
            window: function () {
                layer.closeAll()
            }
        });
    });

	

    //切换 年级, 班级类型 ,  科目的时候, 隐藏表格和表格标题
    //点击班级的时候显示
    function hideTable() {
        $('#seat-table-contain').html('<div class="no-data" style="padding: 30px 100px 200px 100px">' +
            '<span class="no-data-img">' +
            '<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">' +
            '</span>' +
            '<div class="no-data-body">' +
            '<p class="no-data-txt">暂无设置</p>' +
            '</div>' +
            '</div>')
    }


    //清空确认
    $('#btn-clear-confirm').click(function () {
       var url = '${request.contextPath}/eclasscard/standard/clearClassSeatSet';
        var chooseClassId=$('#class-id-data').attr('data');
		$.ajax({
			url:url,
			data:{"classId":chooseClassId},
			dataType: "json",
			success: function(data){
				layer.closeAll();
				if(data.success){
					layer.msg(data.msg, {
						offset: 't',
						time: 2000
					});
					hideTable();
				}else{
					layerTipMsg(data.success,"失败","原因："+data.msg);
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});         
    })
	
    function doSeatSetImport() {
        var gradeId = $("#grade-select").val();
        var classType = $("#class-type").val();
        var classId = $("#class-id-data").attr('data');
        var url = '${request.contextPath}/eclasscard/seatSetImport/head/page?gradeId=' + gradeId + "&classType=" + classType + "&classId=" + classId;
        if (2 == classType) {
            var subjectId = $('.subject.selected').attr('data');
            url += "&subjectId=" + subjectId;
        }
        $("#showList").load(url);
    }


    //picker
    $(".picker-more").click(function () {
        if (
            $(this)
                .children("span")
                .text() == "展开"
        ) {
            $(this)
                .children("span")
                .text("折叠");
            $(this)
                .children(".fa")
                .addClass("fa-angle-up")
                .removeClass("fa-angle-down");
        } else {
            $(this)
                .children("span")
                .text("展开");
            $(this)
                .children(".fa")
                .addClass("fa-angle-down")
                .removeClass("fa-angle-up");
        }
        $(this)
            .parents("td")
            .siblings("td")
            .children(".outter")
            .toggleClass("outter-auto");
    });
</script>
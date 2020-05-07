<p class="color-yellow">
    <i class="fa fa-exclamation-circle"></i> 默认各科目都分2层
</p>
<form id="myForm">
    <table class="table table-striped table-bordered table-hover no-margin">
        <thead>
        <tr>
            <th>科目</th>
            <th>A层</th>
            <th>B层</th>
        </tr>
        </thead>
        <tbody>
        <#if courseList?exists && courseList?size gt 0>
            <#list courseList as course>
                <tr>
                    <input class="form-control" type="hidden" name="exList[${course_index}].subjectId"
                           value="${course.id!}">
                    <td>${course.subjectName!} (${course.orderId!}人)</td>
                    <td>
                        <div class="input-group form-num float-left width-123">
                            <input id="${course.id!}" class="form-control" type="text" name="exList[${course_index}].classNum" nullable="false" vtype="int" min="0" max="100" value="2">
                            <span class="input-group-btn">
	                                    <button class="btn btn-default classNumUp" type="button">
	                                    	<i class="fa fa-angle-up"></i>
	                                    </button>
	                                    <button class="btn btn-default classNumDown" type="button">
	                                    	<i class="fa fa-angle-down"></i>
	                                    </button>
	                                </span>
                        </div>
                        <span class="float-left ml5 mt3">个班</span>
                    </td>
                    <#if course_index == 0>
                        <td rowspan="6">系统智能计算</td>
                    </#if>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</form>

<div class="layer-footer" style="vertical-align: middle;border-top: 1px solid #eee;">
    <button class="btn btn-lightblue" id="quick3-commit">确定</button>
    <button class="btn btn-grey" id="quick3-close">取消</button>
</div>

<script>
    var isSubmit = false;

    $(function () {
        $(".classNumUp").on("click", function () {
            var i;
            var obj = $(this).parent().prev();
            if (parseInt(obj.val()) || parseInt(obj.val()) == 0) {
                i = parseInt(obj.val());
            } else {
                i = 0;
            }
            obj.val(++i);
        });

        $(".classNumDown").on("click", function () {
            var i;
            var obj = $(this).parent().prev();
            if (parseInt(obj.val()) || parseInt(obj.val()) == 0) {
                i = parseInt(obj.val());
            } else {
                i = 0;
            }
            if (i == 0) {
                return;
            }
            obj.val(--i);
        });

        $("#quick3-close").on("click", function () {
            doLayerOk("#quick3-commit", {
                redirect: function () {
                },
                window: function () {
                    layer.closeAll()
                }
            });
        });

        $("#quick3-commit").off('click').on("click", function () {
            if(isSubmit){
                return false;
            }
            isSubmit=true;
            $(this).addClass("disabled");
            var check = checkValue('#myForm');
            if (!check) {
                isSubmit = false;
                $(this).removeClass("disabled");
                return;
            }
            // 提交数据
            var options = {
                url : '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/saveIntelliStratify',
                dataType : 'json',
                success : function(data){
                    if(data.success){
                        // 休眠1秒
                        var start = (new Date()).getTime();
                        while ((new Date()).getTime() - start < 1000) {
                            continue;
                        }
                        layer.closeAll();
                        layer.msg("保存成功！", {
                            offset: 't',
                            time: 2000
                        });
                        refreshThis();
                    }
                    else{
                        layerTipMsg(data.success,"失败",data.msg);
                        $("#quick3-commit").removeClass("disabled");
                        isSubmit=false;
                    }
                },
                clearForm : false,
                resetForm : false,
                type : 'get',
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            };
            $("#myForm").ajaxSubmit(options);
        });
    });
</script>
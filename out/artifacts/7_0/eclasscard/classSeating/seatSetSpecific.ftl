<form id="myform">
    <div class="seat-set-div" id="${seatSet.id!}">
        <table width="100%" height="180px">
            <tr>
                <td class="text-center"><span>列：</span></td>
                <td class="input-group form-num ">
                    <input class="form-control col-raw-input" value="${seatSet.colNumber!}" min="3" type="text"
                           id="col-val">
                    <span class="input-group-btn">
                            <button class="btn btn-default increment" type="button">
                                <i class="fa fa-angle-up"></i>
                            </button>
                            <button class="btn btn-default decrement" type="button">
                                <i class="fa fa-angle-down"></i>
                            </button>
                        </span>
                </td>
            </tr>
            <tr>
                <td class="text-center"><span>行：</span></td>
                <td class="input-group form-num ">
                    <input class="form-control col-raw-input" value="${seatSet.rowNumber!}" type="text" id="raw-val">
                    <span class="input-group-btn">
                            <button class="btn btn-default increment" type="button">
                                <i class="fa fa-angle-up"></i>
                            </button>
                            <button class="btn btn-default decrement" type="button">
                                <i class="fa fa-angle-down"></i>
                            </button>
                        </span>
                </td>
            </tr>
            <tr>
                <td><span>过道1：</span></td>
                <td class="form-num form-inline space1">
                    <span>介于第</span>
                    <input type="text" value="${seatSet.space1!}" style="width:70px" class="form-control space-input"
                           id="space1">
                    <span>列与第</span>
                    <input type="text" value="${seatSet.space1+1}" style="width:70px" class="form-control" disabled>
                    <span>列之间</span>
                </td>
            </tr>
            <tr>
                <td><span>过道2：</span></td>
                <td class="form-num form-inline space2">
                    <span>介于第</span>
                    <input type="text" value="${seatSet.space2!}" style="width:70px" class="form-control space-input"
                           id="space2">
                    <span>列与第</span>
                    <input type="text" value="${seatSet.space2+1}" style="width:70px" class="form-control" disabled>
                    <span>列之间</span>
                </td>
            </tr>
        </table>
    </div>
</form>

<#-- 确定和取消按钮 -->
<div class="layer-footer">
    <a class="btn btn-blue" id="btn-seat-confirm">保存</a>
    <a class="btn btn-grey" id="btn-seat-close">取消</a>
</div>

<script>
    function closeWindow() {
        doLayerOk("#btn-seat-confirm", {
            redirect: function () {
            },
            window: function () {
                layer.closeAll()
            }
        });
    }

    // 取消按钮操作功能
    $("#btn-seat-close").on("click", closeWindow);

    function checkParams(col, raw, sp1, sp2) {
        if (col == '' || raw == '' || sp1 == '' || sp2 == '') {
            layer.msg('所有参数必填');
            return false;
        }
        col = parseInt(col);
        sp1 = parseInt(sp1);
        sp2 = parseInt(sp2);
        if (col < 3 || col > 15) {
            layer.msg('列值范围必须在3-15之间');
            return false;
        }
        if (raw < 3 || raw > 15) {
            layer.msg('列值范围必须在3-15之间');
            return false;
        }

        if (sp1 >= sp2) {
            layer.msg('过道2位置必须大于过道1');
            return false;
        }
        if (sp2 >= col) {
            layer.msg('过道值必须小于列值');
            return false;
        }
        return true;
    }
	var isSaveSeat=false;
    //保存
    $('#btn-seat-confirm').click(function () {
        var classType = $('#class-type').val();
        var classId;
        if (classType == '1') {
            classId = $('.admin-class a.selected').attr('data');
        } else {
            classId = $('#teach-class-select a.selected').attr('data');
        }
        var colNumber = $('#col-val').val();
        var rawNumber = $('#raw-val').val();
        var space1 = $('#space1').val();
        var space2 = $('#space2').val();
        var flag = checkParams(colNumber, rawNumber, space1, space2);
        if (!flag) {
        	isSaveSeat=false;
            return;
        }
        if(isSaveSeat){
        	return;
        }
		isSaveSeat=true;

        var params = {
            colNumber: colNumber,
            rowNumber: rawNumber,
            space1: space1,
            space2: space2,
            classType: classType,
            classId: classId
        }
        var url = '${request.contextPath}/eclasscard/standard/saveSeatSet';
        $.ajax({
            url: url,
            type: "post",
            data: JSON.stringify(params),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                layer.closeAll();
                isSaveSeat=false;
                if (data.success) {
                    layer.msg(data.msg, {
                        offset: 't',
                        time: 2000
                    });
                    $('#table-title').removeClass('hide');
                    $('#seat-table-contain').removeClass('hide')
                    var classId = $('#class-id-data').attr('data');
                    var url = '${request.contextPath}/eclasscard/standard/seatTable?classId=' + classId;
                    $('#seat-table-contain').load(url);
                }else{
                	layerTipMsg(data.success,"失败","原因："+data.msg);
                }
            }
        })

    })

    $('.increment').click(function () {
        var target = $(this).parent().prev();
        var curVal = target.val();
        if (curVal == '') {
            curVal = 3;
        }
        if (curVal == 15) {
            return;
        }
        target.val(parseInt(curVal) + 1);
    })
    $('.decrement').click(function () {
        var target = $(this).parent().prev();
        var curVal = target.val();
        if (curVal == '') {
            curVal = 15;
        }
        if (curVal == '3') {
            return;
        }
        target.val(parseInt(curVal) - 1);
    })


    //行列正则校验
    $('.col-raw-input').on("input propertychange", function () {
        if (!/^([1-9]|[1][0-5])$/.test($(this).val())) {
            $(this).val('');
        }
    })

    //过道正则校验
    $('.space-input').on("input propertychange", function () {
        if (!/^([1-9]|[1][0-4])$/.test($(this).val())) {
            $(this).val('');
            $(this).next().next().val('');
        } else {
            $(this).next().next().val(parseInt($(this).val()) + 1);
        }
    })
</script>
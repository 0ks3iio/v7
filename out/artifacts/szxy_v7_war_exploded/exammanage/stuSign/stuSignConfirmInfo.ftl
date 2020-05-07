<div class=" layer-addTerm layer-change" style="display:block;" id="myDiv1">
    <form id="subForm">
        <input  type="hidden" nullable="false" name="id" id="id" style="width: 200px" value="${id!}">
        </input>
        <input  type="hidden" nullable="false" name="unitId" id="unitId" style="width: 200px" value="${unitId!}">
        </input>
        <div class="layer-body">
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">学校：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" style="width: 200px" readonly value="${unit.unitName!}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">姓名：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" style="width: 200px" readonly value="${student.studentName!}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">学籍号：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" style="width: 200px" readonly value="${student.studentCode!}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">身份证号：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" style="width: 200px" readonly value="${student.identityCard!}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">性别：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" id="specialNum" readonly name="specialNum" style="width: 200px" value="${mcodeSetting.getMcode("DM-XB","${student.sex!}")}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">民族：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" id="specialNum" readonly name="specialNum" style="width: 200px" value="${mcodeSetting.getMcode("DM-MZ","${student.nation !}")}">
                        </input>
                    </div>
                </div>
            </div>
            <div class="filter clearfix">
                <div class="filter-item">
                    <label for="" class="filter-name">班级：</label>
                    <div class="filter-content">
                        <input  class="form-control" nullable="false" id="specialNum" readonly name="specialNum" style="width: 200px" value="${className!}">
                        </input>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-blue" id="arrange-commit">确定</a>
    <a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
</div>
<script>
    $("#arrange-close").on("click", function(){
        doLayerOk("#arrange-commit", {
            redirect:function(){},
            window:function(){layer.closeAll()}
        });
    });
    $("#arrange-commit").on("click", function(){
        var id = $("#id").val();
        var unitId= $("#unitId").val();
        var ii = layer.load();
        $.ajax({
            url: '${request.contextPath}/exammanage/stu/stuSign/doEnroll?',
            data: {'examId':id,'unitId':unitId},
            type:'post',
            success:function(data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.msg(jsonO.msg, {
                        offset: 't',
                        time: 2000
                    });
                    changeTime();
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    });
    function checkNum(num){
        var r = /^\+?[1-9][0-9]*$/;　　//正整数
        var flag=r.test(num);
        return flag;
    }
</script>
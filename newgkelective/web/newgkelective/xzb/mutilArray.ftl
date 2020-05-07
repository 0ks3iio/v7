<div style="overflow: auto;height: 450px;">
    <table class="table table-bordered table-striped table-hover" id="mutilIds">
        <tbody>
        <#if gradeList?? && gradeList?size gt 0>
        <#list gradeList as grade>
            <#assign arrays = arrayByGrade[grade.id]!>
            <#if arrays?? && arrays?size gt 0>
            <tr>
                <td>${grade.gradeName!}</td>
                <td>
                    <select class="form-control arrayId" name="arrayIds" <#if grade.id == nowGradeId>disabled</#if>>
                        <option value="">请选择</option>
                    <#if arrays?? && arrays?size gt 0>
                    <#list arrays as array>
                        <option value="${array.id!}" <#if arrayId == array.id>selected</#if>>
                            ${array.arrayName!}<#if array.stat! == "0">(未排课)</#if>
                        </option>
                    </#list>
                    </#if>
                    </select>
                </td>
            </tr>
            </#if>
        </#list>
        </#if>
        </tbody>
    </table>
</div>
<style>
    .mylayerbtn {
    position: relative;
    padding: 10px 0;
    margin: 0 10px;
    border-top: 1px solid #eee;
    text-align: center;
}
.mylayerbtn a {
    height: 34px;
    line-height: 34px;
    padding: 6px 20px;
    font-size: 16px;
    text-align: center;
    background-color: #317eeb;
    color: #fff;
    border-radius: 2px;
    font-weight: 400;
    cursor: pointer;
    text-decoration: none;
}

</style>
<div class="mylayerbtn">
    <a class="layui-layer-btn0" id="" onClick="saveMutil();">自动排课</a>
    <a class="layui-layer-btn0" id="" onClick="modifyMutil();">手动调课</a>
    <a class="layui-layer-btn1" style="background-color: #fff;color:black;border: 1px solid #dedede;" id="arrange-close" onclick="closeLayer();">取消</a>
</div>
<div class="layui-layer-btn" style="display: none;">
    <a class="layui-layer-btn1" id="arrange-close2">取消</a>
</div>
<script>
var stMap = ${stMapJsonStr!};
    function closeLayer(){$("#arrange-close2").click()};
    function getSelInfo(type){
        var arrayIds = [];
        var stArr = [];
        $("#mutilIds .arrayId").each(function (i, obj) {
            var arrId = $(obj).val();
            if(arrId){
                arrayIds.push("arrayIds="+arrId);
                var stCode = (stMap[arrId]==0?0:1);
                if(stArr.indexOf(stCode)<0){
                    stArr.push(stCode);
                }
            }
        });
        if(arrayIds.length<=1){
            alert("请至少选择两个排课方案");
            return false;
        }
        if(type == "1" && !(stArr.length=="1"&&stArr[0]=="0")){
            alert("请确保选择的排课方案都为 未排课");
            return false;
        }
        if(stArr.length>1){
            alert("请确保选择的排课方案都为 未排课 或 已排课");
            return false;
        }
        return arrayIds;
    }
    function saveMutil(){

        var arrayIds = getSelInfo("1");
        if(!arrayIds){
            return;
        }

        var arrayIdstr = arrayIds.join("&");
        
        var ii = layer.load();
        $.ajax({
            url:"${request.contextPath}/newgkelective/xzb/${arrayId!}/mutilArray/start",
            data:arrayIdstr,
            dataType: "json",
            success: function(data){
                layer.close(ii);
                if(data.success){
                    closeLayer();
                    refreshList();
                }else{
                    layerTipMsg(data.success,"失败","原因："+data.msg);
                }
            },
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        });
    }
    function modifyMutil(){

        var arrayIds = getSelInfo();
        if(!arrayIds){
            return;
        }
        var arrayIdstr = arrayIds.join("&");

        var url = "${request.contextPath}/newgkelective/scheduleModify/mutil/${arrayId!}/index?"+arrayIdstr;
        var width = screen.availWidth;
        var height = screen.availHeight -60;

        window.open
        (url,'newwindow','fullscreen=no, height='+height+',width='+width+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,\
			resizable=no,location=no, status=no');
        closeLayer();
    }
</script>


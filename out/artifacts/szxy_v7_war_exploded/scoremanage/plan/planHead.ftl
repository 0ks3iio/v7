<div class="box box-default">
    <div class="box-body">
        <!-- PAGE CONTENT BEGINS -->
        <div class="filter clearfix">
            <div class="filter-item">
                <label for="" class="filter-name">学年：</label>
                <div class="filter-content">
                    <select class="form-control" id="acadyear" name="acadyear" onChange="showItem()">
                        <#if acadyearList?exists && (acadyearList?size>0)>
                            <#list acadyearList as item>
                                <option value="${item!}" <#if semester.acadyear?default('')==item>selected</#if>>${item!}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">学期：</label>
                <div class="filter-content">
                    <select class="form-control" id="semester" name="semester" onChange="showItem()">
                        ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
                    </select>
                </div>
            </div>
            <div class="filter-item pull-right">
                <a class="btn btn-blue js-addTerm" onclick="makeScore();">获取补考成绩</a>
                <a class="btn btn-blue js-addTerm" onclick="reStudentList();">插入复学学生</a>
                <a class="btn btn-blue js-addTerm" onclick="addPlan();">新增方案</a>
            </div>
        </div><!-- 筛选结束 -->
        <div class="table-wrapper" id="showItemDiv">
        </div>
    </div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
    $(function(){
        showItem();
    });
    function showItem(){
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var str = "?acadyear="+acadyear+"&semester="+semester;
        var url =  '${request.contextPath}/scoremanage/plan/list/page'+str;
        $("#showItemDiv").load(url);
    }

    function addPlan(){
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var str = "?acadyear="+acadyear+"&semester="+semester;
        var url = "${request.contextPath}/scoremanage/plan/add/page"+str;
        indexDiv = layerDivUrl(url,{title: "新增方案",width:400,height:320});
    }

    var isSubmit = false;
    function makeScore(){
        if(isSubmit){
            return;
        }
        isSubmit = true;
        layer.confirm('是否获取该学年学期下的期末类型考试的补考成绩，并生成新的成绩方案?', function(index){
            var ii = layer.load();
            var acadyear = $('#acadyear').val();
            var semester = $('#semester').val();
            $.ajax({
                url:'${request.contextPath}/scoremanage/plan/makeScore',
                data: {'acadyear':acadyear,'semester':semester},
                type:'post',
                success:function(data) {
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        layer.closeAll();
                        layer.msg(jsonO.msg,{
                            offset: 't',
                            time: 2000
                        });
                        showItem();
                    }
                    else{
                        layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
                        isSubmit=false;
                    }
                    layer.close(ii);
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });

        },function(){
            isSubmit=false;
        })
    }

    function reStudentList(){
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var str = "?acadyear="+acadyear+"&semester="+semester;
        var url = '${request.contextPath}/scoremanage/plan/reStudent/list/page'+str;
        $("#showList").load(url);
    }
</script>






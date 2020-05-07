<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div class="table-container-body">
    <table id="example" class="table table-bordered table-striped table-hover ">
        <thead>
        <#assign  hasData = false />
        <#assign  itemLength = 7 />
        <#if itemList?exists && (itemList?size gt 0) >
            <#assign hasData = true />
            <#assign  itemLength = itemList?size  />
        </#if>
        <tr>
            <#if hasData>
            <th class="text-center"   rowspan="2">&emsp;&emsp;姓名&emsp;&emsp;</th>

            </#if>
            <#if itemList?exists && (itemList?size gt 0) >
                <#list  itemList as item>
                    <th class="text-center"  >${item.itemName!}</th>
                </#list>
            </#if>
        </tr>
        <tr>

        <#assign arr = ['A','B','C','D','E'] >
            <#if itemList?exists && (itemList?size gt 0) >

                <#list  itemList as item>
                <th class="text-center">
                <div class="scoring-container">
                    <#if item.codeList?exists>
                        <#assign itemCodeList = item.codeList >
                        <#if itemCodeList?exists && (itemCodeList?size gt 0)>
                        <#list itemCodeList as itemCode>
                                    <a  href="javascript:void(0)"  val="${itemCode.id!}" parent="${item.id!}" onclick="batchSave(this)" cla="boss" >${arr[itemCode_index]}</a>
                        </#list>
                        </#if>
                    </#if>
                </div>
                </th>
                </#list>
            </#if>
        </tr>
        </thead>
        <tbody>

            <#if studentList?exists && (studentList?size gt 0) && hasData >

                <#list  studentList as student >
                <tr>
                    <td class="text-center"  val="${student.id!}" cla="student">${student.studentName!}</td>
                    <#if itemList?exists && (itemList?size gt 0) >
                        <#list  itemList as item>
                            <td class="text-center"  >
                                <div class="scoring-container">
                                <#if item.codeList?exists>
                                    <#assign itemCodeList = item.codeList >
                                    <#assign itemcodeId = performanceMap[student.id +'_'+item.id]?default("")>
                                   <#if itemCodeList?exists && (itemCodeList?size gt 0)>
                                    <#list itemCodeList as itemCode>
                                        <a href="javascript:void(0)" val="${itemCode.id!}" parent="${item.id!}" cla="employee" onclick="Save('${item.id!}','${itemCode.id!}','${student.id!}' ,this);"  <#if itemcodeId == itemCode.id! > class="active"</#if> >${arr[itemCode_index]}</a>
                                    </#list>
                                   </#if>
                               </#if>
                                </div>
                            </td>
                        </#list>
                    </#if>
                </tr>

                </#list>

            </#if>

        </tbody>
    </table>
</div>
<#--<div class="table-container-footer">-->
<#--<@htmlcom.pageToolBar container="#contentPerformance"/>-->
<#--</div>-->
<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>

<!-- page specific plugin scripts -->
<script src="${request.contextPath}/static/components/layer/layer.js"></script>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<script src="${request.contextPath}/static/components/datatables.net/js/jquery.dataTables.min.js"></script>
<script type="text/javascript">
        $(function(){

//            $('div.scoring-container a').on('click', function(e){
//                alert(123);
//                e.preventDefault();
//                var id = $(this).attr("val");
//                var itemId = $(this).attr("parent");
//                var studentId = "";
//                if("boss" == $(this).attr("cla")){
//                    $("a[val='" + id + "' ]").each(function () {
//                        $(this).addClass('active').siblings().removeClass('active');
//                    })
//                }else{
//                    $(this).addClass('active').siblings().removeClass('active');
//                    studentId = $(this).parent().parent().siblings("td[cla='student']").attr("val");
//                }
//                Save(itemId,id,studentId);
//            });
            <#if hasData>
            var table = $('#example').DataTable( {
                scrollY: "350px",
                info: false,
                searching: false,
                autoWidth: false,
                scrollX: true,
                sort: false,
                scrollCollapse: true,
                paging: false,
                fixedColumns: {
                    leftColumns: 1
                }
            } );
            </#if>
            $("td.dataTables_empty").text("暂无数据");
        });

        function Save(itemId,itemCodeId,studentId ,data){
            if(data != "" ){
                $(data).addClass('active').siblings().removeClass('active');
            }

            var options={
                url:"${request.contextPath}/studevelop/mouthPerformance/save/page",
                type:"get",
                data:{"itemId":itemId ,"resultId":itemCodeId,"studentId":studentId},
                success:function (data) {
                    var jsonO = JSON.parse(data);
                    if(!jsonO['success']){
                        layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
                        return;
                    }
                },
                clearForm:false,
                resetForm:false,
                error:function(XMLHttpRequest ,textStatus,errorThrown){}
            }

            $("#seach").ajaxSubmit(options)
        }

        function  batchSave( data) {
            var id = $(data).attr("val");
            var itemId = $(data).attr("parent");
            $(data).addClass('active').siblings().removeClass('active');
            $("a[val='" + id + "' ]").each(function () {
                        $(this).addClass('active').siblings().removeClass('active');
                    });

            Save(itemId,id,"" ,"");
        }
    </script>
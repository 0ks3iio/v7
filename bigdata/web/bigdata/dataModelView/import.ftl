<#import "/bigdata/v3/templates/webUploaderMacro.ftl" as upload />
<div class="row" id="importExcelDiv">
    <div class="col-xs-12">
        <@upload.fileUpload businessKey="analyse-import" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}"  extensions="xls,xlsx" size="100" fileNumLimit="1" handler="loadFile">
            <label for="file" id="upFile" class="no-margin btn-left-img js-addFiles">添加EXCEL文件<img src="${request.contextPath}/static/bigdata/images/icon-add.png" alt="" /></label>
            <input type="hidden" id="analyse-import-path" value="">
            <input type="hidden" name="filePath" id="filePath" value="${filePath!}">
            <input type="hidden" name="fileName" id="fileName" value="${fileName!}">
        </@upload.fileUpload>
        <button class="btn btn-blue btn-left-img" onclick="eventImport()">开始导入<img src="${request.contextPath}/static/bigdata/images/icon-transfer-white.png" alt="" />
        </button>
        <button class="btn btn-blue" onclick="showExcel()">查看100条数据
        </button>
        <div class="form-group">
            之前创建的Excel表
            <select class="form-control" id="tableNameList">
                <#if tableNameList??>
                    <#list tableNameList as list>
                        <option value="${list}" <#if tableName! ==list>selected</#if>>${list}</option>
                    </#list>
                </#if>
            </select>
        </div>
        <button class="btn btn-blue" onclick="showFirstTitle()">新建(弹窗)
        </button>
        <button class="btn btn-blue" onclick="createTable()">创建表(确定)
        </button>
        <button class="btn btn-blue btn-left-img" onclick="turnToCsv()">CSV导入页面
        </button>
        <ul class="file-list-wrap" id="fileNameDiv">
            <#if fileName??>
                <li class="">${fileName!}<i class="wpfont icon-close js-delete"></i></li>
            </#if>
        </ul>
        <div>
            <table class="tables tables-border no-margin">
                <thead>
                <tr>
                    <th >表名</th>
                    <th >数据量</th>
                    <th >说明</th>
                    <th >操作</th>
                </tr>
                </thead>
                <tbody>
                <#list bgDaCustomTableList as list>
                    <tr data-id="${list.id}">
                        <td>${list.tableName}</td>
                        <td>${list.dataNum}</td>
                        <td>${list.remark}</td>
                        <td><a href="javascript:void(0)" class="look-over clear" onclick="clearTable(this)">清空</a>
                            <span class="tables-line">|</span>
                            <a href="javascript:void(0)" class="look-over delete" onclick="deleteTable(this)">删除</a></td>
                    </tr>

                </#list>


                </tbody>
            </table>
        </div>




        <script type="text/javascript">
            $(function () {
                if ($("#tableNameList").children("option").length >= 1) {
                    selectFunction()
                }
                $("#tableNameList").change(function () {
                    selectFunction()
                })
            })

            function toImport() {
                var url="${request.contextPath}/bigdata/data/analyse/import";
                var fileName=$("#fileName").val();
                var filePath=$("#filePath").val();
                var tableName=$("#tableNameList option:selected").val();
                var params={
                    "fileName":fileName,
                    "filePath":filePath,
                    "tableName":tableName
                };
                $(".page-content").load(url,params)
            }

            var isSubmit=false;
            /**
             * 下拉框选择触发，展示字段
             * */
            function selectFunction() {
                var params={
                    "tableName":$("#tableNameList option:selected").val()
                }
                var url="${request.contextPath}/bigdata/data/analyse/showFields";
                $.post(url,params,function (result) {
                    console.log(result)
                })
            }

            /**
             * 图片上传回调
             */
            function loadFile(){
                if(hasUploadSuc){
                    $("#filePath").val($("#analyse-import-path").val());
                    $("#fileNameDiv").empty();
                    $("#fileNameDiv").html('<li class="">'+fileName+'<i class="wpfont icon-close js-delete"></i></li>');
                    $("#fileName").val(fileName);
                }
            }
            $('body').on('click','.js-delete',function(){
                $(this).parents('li').remove();
                $("#filePath").val('');
            });

            /**
             * 点击新建按钮弹出字段
             * */
            function showFirstTitle() {
                var fileName=$("#fileName").val();
                var filePath=$("#filePath").val();
                if(filePath == ""){
                    showLayerTips4Confirm('error','请先选择导入文件');
                    return;
                }
                var params={
                    "filePath":filePath,
                    "fileName":fileName
                }
                var url="${request.contextPath}/bigdata/data/analyse/showFirstTitle";
                $.post(url,params,function (result) {
                    console.log(result);
                })
            }
            
            
            /**
             * 显示100条数据
             * */
            function showExcel() {
                console.log("显示数据Excel")
                var fileName=$("#fileName").val();
                var filePath=$("#filePath").val();
                if(filePath == ""){
                    showLayerTips4Confirm('error','请先选择导入文件');
                    return;
                }
                var params={
                    "filePath":filePath,
                    "fileName":fileName
                }
                var url="${request.contextPath}/bigdata/data/analyse/showExcel";
                $("#importExcelDiv").addClass("hide")
                $("#showExcelDiv").removeClass('hide').load(url,params)

               /* $.post(url,params,function (result) {
                    console.log(result)
                })*/
            }
            /**
             * 导入excel
             * */
            function eventImport(){
                if(isSubmit){
                    return;
                }
                isSubmit=true;
                var filePath=$("#filePath").val();
                var fileName=$("#fileName").val();
                var firstTitle=true;
                var deleteAll=false;
                var sheetNum=1;
                var tableName=$("#tableNameList option:selected").val()
                var fieldList=new Array("name","age","sex");
                var indexList=new Array("2","4","3");
                if(filePath == ""){
                    showLayerTips4Confirm('error','请先选择导入文件');
                    return;
                }
                var url="${request.contextPath}/bigdata/data/analyse/submit";
                var params={
                    "filePath":filePath,
                    "fileName":fileName,
                    "firstTitle":firstTitle,
                    "deleteAll":deleteAll,
                    "sheetNum":sheetNum,
                    "tableName":tableName,
                    "fieldList":fieldList.join("|"),
                    "indexList":indexList.join("|")
                }
                $.post(url,params,function (result) {
                    alert(result.message)
                    toImport()
                })
            }

            /**
             * 创建表
             * */
            function createTable() {
                var filePath=$("#filePath").val();
                if(filePath == ""){
                    showLayerTips4Confirm('error','请先选择导入文件');
                    return;
                }
                var columns=new Array();//创建字段的集合的集合
                var types=new Array();//创建字段类型的集合
                columns.push("name")
                columns.push("age");
                columns.push("sex");
                columns.push("kk");
                columns.push("school");
                types.push("varchar(50)");
                types.push("varchar(50)");
                types.push("varchar(50)");
                types.push("varchar(50)");
                types.push("varchar(50)");
                console.log(columns)
                console.log(types)
                var fileName=$("#fileName").val();
                var tableName="zjyTest";
                var remark="zjy-test"
                var source="excel";
                var url="${request.contextPath}/bigdata/data/analyse/createTable";
                var params={
                    "columns":columns.join("|"),
                    "types":types.join("|"),
                    "tableName":tableName,
                    "remark":remark,
                    "fileName":fileName,
                    "source":source
                }

                $.post(url,params,function (result) {
                    var selection = document.getElementById("tableNameList")
                    selection.options.add(new Option(result.data, result.data));
                    selection.children[$("#tableNameList").children("option").length-1].selected=true;
                    selectFunction()
                    toImport()
                    console.log(result.message);
                })
            }
            /**
             * 跳转csv
             * */
            function turnToCsv() {
                var url="${request.contextPath}/bigdata/data/analyse/turnToCsv";
                $(".page-content").load(url)
            }

            /**
             * 清空表
             */
            function clearTable(obj) {
                var tableName = $(obj).parents("tr").children().eq(0).html();
                console.log(tableName)

                var url="${request.contextPath}/bigdata/data/analyse/deleteTable";
                var params={
                    "tableName":tableName
                }
                $.post(url,params,function (result) {
                    console.log(result.message)
                    toImport()
                })
            }

            /**
             * 删除表
             */
            function deleteTable(obj) {
                var id= $(obj).parents("tr").data("id");
                console.log(id);
                var params={
                    "id":id
                }
                var url="${request.contextPath}/bigdata/data/analyse/dropTable";
                $.post(url,params,function (result) {
                    console.log(result.message)
                    toImport()
                })
            }
        </script>

    </div>
</div>
<div id="showExcelDiv" class="hide"></div>

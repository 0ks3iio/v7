<#import "/bigdata/v3/templates/webUploaderMacro.ftl" as upload />
<div class="row" id="importCsvDiv">
    <div class="col-xs-12">
        <@upload.fileUpload businessKey="analyse-import" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}"  extensions="csv" size="100" fileNumLimit="1" handler="loadFileCsv">
            <label for="file" id="upFile" class="no-margin btn-left-img js-addFiles">添加csv文件<img
                        src="${request.contextPath}/static/bigdata/images/icon-add.png" alt=""/></label>
            <input type="hidden" id="analyse-import-path" value="">
            <input type="hidden" name="filePath" id="filePath" value="">
            <input type="hidden" name="fileName" id="fileName" value="">
        </@upload.fileUpload>
        <div class="form-group">
            文本分割符
            <select class="form-control" id="separators">
                <option value=",">,</option>
                <option value="@">@</option>
                <option value="/t">/t</option>
            </select>
        </div>
        <div class="form-group">
            编码
            <select class="form-control" id="code">
                <option value="GB2312">GB2312</option>
                <option value="GBK">GBK</option>
                <option value="UTF-8">UTF-8</option>
                <option value="ISO-8859-1">ISO-8859-1</option>
            </select>
        </div>
        <button class="btn btn-blue btn-left-img" onclick="eventImportCsv()">开始导入<img
                    src="${request.contextPath}/static/bigdata/images/icon-transfer-white.png" alt=""/>
        </button>
        <button class="btn btn-blue" onclick="showCSV()">查看100条数据
        </button>
        <div class="form-group">
            之前创建的CSV表
            <select class="form-control" id="tableNames">
                <#if tableNames??>
                    <#list tableNames as list>
                        <option value="${list}">${list}</option>
                    </#list>
                </#if>
            </select>
        </div>
        <button class="btn btn-blue" onclick="createTableCsv()">创建表
        </button>
        <ul class="file-list-wrap" id="fileNameDiv">
        </ul>
        <div>

        </div>
    </div>
</div>
<div id="showCsvDiv" class="hide"></div>

<script type="text/javascript">
    $(function () {
        if ($("#tableNames").children("option").length >= 1) {
            selectFunction()
        }
        $("#tableNames").change(function () {
            selectFunction()
        })
    })


    /**
     * 下拉框选择触发
     * */
    function selectFunction() {
        var params={
            "tableName":$("#tableNames option:selected").val()
        }
        var url="${request.contextPath}/bigdata/data/analyse/showFields";
        $.post(url,params,function (result) {
            console.log(result)
        })
    }
    var isSubmit=false;

    function loadFileCsv() {
        if (hasUploadSuc) {
            $("#filePath").val($("#analyse-import-path").val());
            $("#fileNameDiv").html('<li class="">'+fileName+'<i class="wpfont icon-close js-delete"></i></li>');
            $("#fileName").val(fileName);
        }
    }

    /**
     * 转码显示 100行scv数据
     * */
    function showCSV() {
        console.log("显示数据CSV")
        var filePath = $("#filePath").val();
        if (filePath == "") {
            showLayerTips4Confirm('error', '请先选择导入文件');
            return;
        }
        var fileName = $("#fileName").val();
        var code = $("#code option:selected").val();
        var separators = $("#separators option:selected").val();
        var params = {
            "filePath": filePath,
            "fileName": fileName,
            "code": code,
            "separators": separators
        }
        var url = "${request.contextPath}/bigdata/data/analyse/showCsv";
        $("#importCsvDiv").addClass('hide');
        $("#showCsvDiv").removeClass('hide').load(url,params);

    }

    /**
     * 创建表csv
     * */
    function createTableCsv() {
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
        var tableName="zjyTestCsv";
        var remark="zjy-test-csv"
        var source="csv";
        var url="${request.contextPath}/bigdata/data/analyse/createTable";
        var params={
            "columns":columns.join("|"),
            "types":types.join("|"),
            "tableName":tableName,
            "remark":remark,
            "fileName":fileName,
            "source":source,
        }
        $.post(url,params,function (result) {
            var selection = document.getElementById("tableNames")
            selection.options.add(new Option(result.data, result.data));
            selection.children[$("#tableNames").children("option").length-1].selected=true;
            selectFunction()
            console.log(result.message);
        })
    }

    /**
     * 导入csv数据
     */
    function eventImportCsv() {
        if(isSubmit){
            return;
        }
        var filePath=$("#filePath").val();
        var fileName=$("#fileName").val();
        var firstTitle=true;
        var deleteAll=true;
        var tableName=$("#tableNames option:selected").val();
        var code = $("#code option:selected").val();
        var separators = $("#separators option:selected").val();
        var fieldList=new Array("name","age","sex");
        var indexList=new Array("2","4","3");
        if(filePath == ""){
            showLayerTips4Confirm('error','请先选择导入文件');
            return;
        }
        var url="${request.contextPath}/bigdata/data/analyse/submitCsv";
        var params={
            "filePath":filePath,
            "fileName":fileName,
            "firstTitle":firstTitle,
            "deleteAll":deleteAll,
            "tableName":tableName,
            "code": code,
            "separators": separators,
            "fieldList":fieldList.join("|"),
            "indexList":indexList.join("|")
        }
        $.post(url,params,function (result) {
            console.log(result.message)
        })
    }
</script>

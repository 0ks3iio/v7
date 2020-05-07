<#macro acadyearSemester stuDevelopUrl="" params="">
<div class="mui-content">
    <div class="mui-content-padded">
        <input type="hidden" id="acadyear" value="">
        <input type="hidden" id="semester" value="">
        <button id="showUserPicker" type="button" class="mui-btn mui-btn-block">${acadyear}学年 <#if semester?default('1') == '1'>第一学期<#else>第二学期</#if> </button>
    </div>
</div>
<script type="text/javascript">
    (function($, doc) {
        $.init();
        $.ready(function() {
            //一级
            var userPicker = new $.PopPicker(
                    {layer:2}
            );

            userPicker.setData([
                <#if acadyearList?exists && acadyearList?size gt 0>
                    <#list acadyearList as acad>
                        <#if acad_index != acadyearList?size -1 >
                            {
                                value: '${acad}',
                                text: '${acad}学年',
                                children: [
                                    {
                                        value: '1',
                                        text: '第一学期'
                                    },
                                    {
                                        value: '2',
                                        text: '第二学期'
                                    }]
                            },
                        <#else >
                            {
                                value: '${acad}',
                                text: '${acad}学年',
                                children: [
                                    {
                                        value: '1',
                                        text: '第一学期'
                                    },
                                    {
                                        value: '2',
                                        text: '第二学期'
                                    }]
                            }
                        </#if>
                    </#list>
                </#if>
            ]);
            userPicker.pickers[0].setSelectedValue('${acadyear}');
            userPicker.pickers[1].setSelectedValue('${semester}');
            var showUserPickerButton = doc.getElementById('showUserPicker');
            showUserPickerButton.addEventListener('tap', function(event) {
                userPicker.show(function(items) {
                    showUserPickerButton.innerText = items[0].text + "  " + items[1].text;
                    doc.getElementById("acadyear").value = items[0].value;
                    doc.getElementById("semester").value = items[1].value;
                    doReload();
                    //返回 false 可以阻止选择框的关闭
                    //return false;
                });
            }, false);


        });
    })(mui, document);

    function doReload(){
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        var str = "${params!}&acadyear=" + acadyear +"&semester=" + semester;
        load("${stuDevelopUrl!}"+str);
    }
</script>
</#macro>
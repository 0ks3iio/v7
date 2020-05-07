<title>记录回顾与展望</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>


<!-- /section:basics/navbar.layout -->
<form id="noticeForm">
    <div class="main-container" id="main-container">





        <!-- /section:basics/sidebar -->
        <div class="main-content">
            <!--

            -->

            <div class="main-content-inner">
                <div class="page-content">
                    <div class="box box-default">
                        <div class="box-header">
                            <h4 class="box-title">假期事项</h4>
                        </div>
                        <div class="box-body">
                            <div >
                                <div class="filter">
                                    <div class="filter-item">
                                        <span class="filter-name">学年：</span>
                                        <div class="filter-content">
                                            <select vtype="selectOne"  name="acadyear" id="acadyear" class="form-control" onchange="getNotice();">
                                            <#if acadyearList?exists && (acadyearList?size gt 0)>
                                                <#list acadyearList as item>
                                                    <option value="${item!}" <#if acadyear == item?default('')> selected </#if> >${item!}</option>
                                                </#list>
                                            <#else>
                                                <option value="">暂无数据</option>
                                            </#if>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="filter-item">
                                        <span class="filter-name">学期：</span>
                                        <div class="filter-content">
                                            <select vtype="selectOne"  name="semester" id="semester" class="form-control" onchange="getNotice()">
                                            ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div id="editNotice">

                            </div>
                        </div>
                    </div>
                </div><!-- /.page-content -->
            </div>
        </div><!-- /.main-content -->
    </div><!-- /.main-container -->
</form>
<script type="text/javascript">
    jQuery(document).ready(function(){
        getNotice();
    });
    function getNotice(){
        var acad = $("#acadyear").val();
        var semester = $("#semester").val();
        jQuery("#editNotice").load("${request.contextPath}/studevelop/holidayNotice/getNotice?acadyear=" + acad + "&semester="+semester);
    }
</script>




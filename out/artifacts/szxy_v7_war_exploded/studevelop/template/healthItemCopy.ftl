

<div class="layer-content">
<#--<h4>身心项目</h4>-->
    <div class="filter-item block">
        <div class="filter">
            <input type="hidden" name="schoolId" id="schoolId" value="${schoolId!}" >
            <input type="hidden" name="itemIds" id="itemIds" value="${itemIds!}" >
            <div class="filter-item">
                <label for="" class="filter-name">学年：</label>
                <div class="filter-content">
                    <select class="form-control" id="acadyear" name="acadyear"  style="width:168px;">
                    <#if (acadyearList?size>0)>
                        <#list acadyearList as item>
                            <option value="${item!}" <#if acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">学期：</label>
                <div class="filter-content">
                    <select class="form-control" id="semester"  name="semester" style="width:168px;">
                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">学段：</label>
                <div class="filter-content">
                    <select vtype="selectOne" class="form-control"  name="schSection" id="schSection"  style="width:168px;">
                    <#list sectionMap?keys as key>
                        <option value="${key!}">${sectionMap[key]}</option>
                    </#list>
                    </select>
                </div>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript">

</script>
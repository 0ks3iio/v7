<div class="layer-content">

    <div class="label-select multiselect">
        <#if detailList?exists && (detailList?size gt 0) >
            <#list detailList as detail>
                <#--<option value="${detail.thisId!}">${detail.mcodeContent!}</option>-->
                <h4>${detail.mcodeContent!}</h4>
                <#assign  gradeList = gradeMap['${detail.thisId!}'] !>
                <input type="hidden" value="${gradeList?size}" >
                <div class="label-select multiselect">
                <#if gradeList?exists && (gradeList?size gt 0)>
                    <#list gradeList as grade>
                        <span class="label-select-item" val="${grade.gradeCode!}">${grade.gradeName!}</span>
                    </#list>

                </#if>
                </div>
            </#list>
        </#if>
    </div>
</div>
<script type="text/javascript">

    $('.label-select .label-select-item').on('click',function(){
        if($(this).hasClass('disabled')){
            return false;
        }else if($(this).parent().hasClass('multiselect')){
            $(this).toggleClass('active');
        }else{
            $(this).addClass('active').siblings().removeClass('active');
        }
    });
</script>
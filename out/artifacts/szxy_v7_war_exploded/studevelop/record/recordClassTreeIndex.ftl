<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<div class="box box-default" id="id1">
    <div class="box-header">
        <h3 class="box-title">班级菜单</h3>
    </div>
<@studevelopTreemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick" url="/stuDevelop/proItemResult/studentTree/page?acadyear=${acadyear!}&semester=${semester!}?noTeaching=true"/>
</div>
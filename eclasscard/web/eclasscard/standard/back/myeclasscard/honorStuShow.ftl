<div class="box box-default">
    <div class="box-body">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-title no-padding-right">查看荣誉&nbsp;</label>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">荣誉名称：</label>
                <div class="col-sm-8">
                   <div>
                       <label class="control-label no-margin">${eccHonor.title!}</label>
                   </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">班牌展示时间：</label>
                <div class="col-sm-8">
                    <div>
                        <label class="control-label no-margin">${eccHonor.beginTime!}至${eccHonor.endTime!}</label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">获奖日期：</label>
                <div class="col-sm-8">
                    <div>
                        <label class="control-label no-margin">${(eccHonor.awardTime?string("yyyy-MM-dd"))?if_exists}</label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">获奖学生：</label>
                <div class="col-sm-8">
                    <div>
                        <label class="control-label no-margin">${eccHonor.studentName!}</label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">获奖照片：</label>
                <div class="col-sm-10">
                    <ul class="img-show clearfix">
                        <li>
                            <img src="${request.contextPath}${eccHonor.pictureUrl!}" alt="">
                        </li>
                    </ul>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right"></label>
                <div class="col-sm-4">
                    <button class="btn btn-blue" onclick="gobacktoList()">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
	function gobacktoList(){
		backFolderIndex('2');
	}
	$(function(){
		<#--返回-->
		showBreadBack(gobacktoList,true,"学生荣誉列表");
	});
	
</script>
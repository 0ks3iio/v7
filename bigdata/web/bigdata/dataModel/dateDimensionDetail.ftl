<form id="dateDimSubmitForm">
<input type="hidden" name="type" value="${type!}">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-4 control-label no-padding-right">时间维度表名：</label>
			<div class="col-sm-6">
			  		<input type="text" id="tableName" name="tableName"  value="${tableName!}" class="form-control" nullable="false" readonly>
			</div>
		</div>
		<div class="form-group">
        	<label class="col-sm-4 control-label no-padding-right">起始日期：	</label>
        	<div class="col-sm-8">
                <div class="input-group col-sm-8" id="startTimeDiv">
                    <input type="text" placeholder="请选择日期" id="startDate" name="startDate" value="${startDate!}" class="form-control" nullable="false" readonly>
                    <span class="input-group-addon">
                        <i class="fa fa-calendar"></i>
                    </span>
                </div>
            </div>
        </div>
        <div class="form-group">
        	<label class="col-sm-4 control-label no-padding-right">结束日期：	</label>
        	<div class="col-sm-8">
                <div class="input-group col-sm-8" id="endTimeDiv">
                    <input type="text" placeholder="请选择日期" id="endDate" name="endDate" value="${endDate!}" class="form-control" nullable="false" readonly>
                   <span class="input-group-addon">
                        <i class="fa fa-calendar"></i>
                    </span>
                </div>
            </div>
        </div>
		<div class="form-group">
			<label class="col-sm-4 control-label no-padding-right">记录数：</label>
			<div class="col-sm-8" style="padding-top: 7px;"><label id="totalSize">${totalCount?default(0)}条</label>
			</div>
		</div>
</div>
</form>
<script>
$(document).ready(function(){
    var startTime = laydate.render({
        elem: '#startDate',
        trigger: 'click',
        isInitValue: false,
        done: function (value, date, endD) {
            endTime.config.min={
                year:date.year,
                month:date.month-1,//关键
                date:date.date
            };

            var beginDate = $('#startDate').val();
            var endDate = $('#endDate').val();
            var start = new Date(beginDate.replace("-", "/").replace("-", "/"));
            var end = new Date(endDate.replace("-", "/").replace("-", "/"));
            if(end<start){
                layer.tips("结束日不能早于起始日期!", "#endDate", {
                    tipsMore: true,
                    tips: 3
                });
                $('#endDate').val('');
            }
        }
    });

    var endTime = laydate.render({
        elem: '#endDate',
        trigger: 'click',
        isInitValue: false,
        done: function (value, date, endD) {
            startTime.config.max={
                year:date.year,
                month:date.month-1,//关键
                date:date.date
            }

            var beginDate = $('#startDate').val();
            var endDate = $('#endDate').val();
            var start = new Date(beginDate.replace("-", "/").replace("-", "/"));
            var end = new Date(endDate.replace("-", "/").replace("-", "/"));
            if(end<start){
                layer.tips("结束日不能早于起始日期!", "#endDate", {
                    tipsMore: true,
                    tips: 3
                });
                $('#endDate').val('');
            }
        }
    });
});

function tips(msg, key) {
    layer.tips(msg, key, {
        tipsMore: true,
        tips: 3,
        time: 2000
    });
}

</script>
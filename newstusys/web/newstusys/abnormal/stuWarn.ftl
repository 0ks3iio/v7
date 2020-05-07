<div class="form-horizontal" role="form">
	<div class="form-group mt20">
		<label class="col-sm-3 control-title">查询结果</label>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right">学生姓名：</label>
		<div class="col-sm-3 mt7">${exStu.studentName!}<span class="badge badge-red ml10">未转出</span></div>
		<label class="col-sm-2 control-label no-padding-right">性别：</label>
		<div class="col-sm-3 mt7">${(mcodeSetting.getMcode("DM-XB", exStu.sex?default(0)?string))?if_exists}</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right">身份证号：</label>
		<div class="col-sm-3 mt7">${exStu.identityCard!}</div>
		<label class="col-sm-2 control-label no-padding-right">学校：</label>
		<div class="col-sm-3 mt7">${exStu.schoolName!}</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right">班级：</label>
		<div class="col-sm-3 mt7">${exStu.className!}</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"></label>
		<div class="col-sm-8">
			<a href="javascript:;" class="btn btn-blue disabled">办理转入</a><span class="color-red ml10">该身份证号已存在，请核查输入的信息</span>
		</div>
	</div>
</div>

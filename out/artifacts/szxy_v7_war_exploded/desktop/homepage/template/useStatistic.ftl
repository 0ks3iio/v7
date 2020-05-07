<div class="col-sm-${(data.col)!}">
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">使用统计</h4>
	</div>
	<div class="box-body">
		<div class="row stat-body" gutter="20">
				<div class="col-lg-4 stat-num-body">
					<div class="stat-num-item">
							<img src="../images/school_icon.png">
							<div class="stat-item-num">${data.unitNum!}</div>
							<div class="stat-item-text">总学校数</div>
					</div>
				</div>
				<div class="col-lg-4 stat-num-body">
					<div class="stat-num-item">
						<img src="../images/teacher_icon.png">
						<div class="stat-item-num">${data.teaNum!}</div>
						<div class="stat-item-text">总教师数</div>
					</div>
				</div>
				<div class="col-lg-4 stat-num-body">
					<div class="stat-num-item">
						<img src="../images/student_icon.png">
						<div class="stat-item-num">${data.stuNum!}</div>
						<div class="stat-item-text">总学生数</div>
					</div>
				</div>
		</div>
		<div class="row stat-box">
			<div class="col-sm-6 statist-item col-md-3">
				<div class="stat-item-num">${data.useSchoolsNum!}</div>
				<div class="stat-item-text">在用学校数</div>
			</div>
			<div class="col-sm-6 statist-item col-md-3">
				<div class="stat-item-num">${data.webNum!}</div>
				<div class="stat-item-text">web端用户</div>
			</div>
			<div class="col-sm-6 statist-item col-md-3">
				<div class="stat-item-num">${data.mobileNum!}</div>
				<div class="stat-item-text">移动端用户</div>
			</div>
			<div class="col-sm-6 statist-item col-md-3">
				<div class="stat-item-num">${data.totalNum!}</div>
				<div class="stat-item-text">访问次数</div>
			</div>
		</div>
	</div>
	
</div>
</div>
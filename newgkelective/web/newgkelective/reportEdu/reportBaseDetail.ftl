<div class="row">
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>学生与场地统计</span>
			</div>
			<div class="box-echarts-body">
				<ul class="statistics-list clearfix">
					<li class="statistics-item">
						<div class="statistics-media group"><i class="group"></i></div>
						<div class="statistics-num">${stuNum!0}</div>
						<div class="statistics-txt">学生数(人)</div>
					</li>
					<li class="statistics-item">
						<div class="statistics-media"><i class="place"></i></div>
						<div class="statistics-num">${placeNum!0}</div>
						<div class="statistics-txt">可用场地数(个)</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>班级统计</span>
			</div>
			<div class="box-echarts-body">
				<ul class="statistics-list clearfix">
					<li class="statistics-item">
						<div class="statistics-media"><i class="xing"></i></div>
						<div class="statistics-num">${xzbNum!0}</div>
						<div class="statistics-txt">行政班数(个)</div>
					</li>
					<li class="statistics-item">
						<div class="statistics-media"><i class="jiao"></i></div>
						<div class="statistics-num">${jxbNum!0}</div>
						<div class="statistics-txt">教学班数(个)</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>

<div class="box-echarts">
	<div class="box-echarts-title">
		<span>各科资源配比明细</span>
	</div>
	<div class="box-echarts-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>科目 </th>
					<th>教师数</th>
					<th>学生数</th>
					<th>教学班数</th>
					<th>师生比</th>
					<th>师班比</th>
				</tr>
			</thead>
			<tbody>
			<#if dtoList?? && dtoList?size gt 0>
			<#list dtoList as dto>
				<tr>
					<td>${dto_index+1}</td>
					<td>${dto.subjectName!}</td>
					<td>${dto.teacherNum}</td>
					<td>${dto.stuNum???then(dto.stuNum,'--')}</td>
					<td>${dto.jxbNum???then(dto.jxbNum,'--')}</td>
					<td>${(dto.stuNum?? && dto.stuNum gt 0)?then((dto.teacherNum/dto.stuNum)?string("0.##"),'--')}</td>
					<td>${(dto.jxbNum?? && dto.jxbNum gt 0)?then((dto.teacherNum/dto.jxbNum)?string("0.##"),'--')}</td>
				</tr>
			</#list>
			<#else>
			<tr><td colspan="7" align="center">没有数据</td></tr>
			</#if>
			</tbody>
		</table>
	</div>
</div>

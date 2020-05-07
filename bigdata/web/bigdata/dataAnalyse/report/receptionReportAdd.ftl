<form id="reportSubmitForm">
	<input type="hidden" name="id" value="${multiReport.id!}">
	<input type="hidden" name="type" value="${multiReport.type!}">
	<input type="hidden" name="unitId" value="${multiReport.unitId!}">
	<input type="hidden" name="orderId" value="${multiReport.orderId!}">
	<div class="form-horizontal form-made">
		<div class="form-group">
			<label class="col-sm-2 control-label"><font style="color:red;">*</font>名称：</label>
			<div class="col-sm-8">
				<input type="text" name="name" id="name" maxLength="50" nullable="false" class="form-control" value="">
			</div>
		</div>
	</div>
</form>
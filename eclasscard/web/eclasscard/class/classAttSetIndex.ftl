<div class="box box-default">
	<div class="box-body">

		<div class="explain">
			<p>说明：此处为对接第三方考勤，场地关联配置。</p>
		</div>
		
		<!-- div class="filter filter-right">
			<div class="filter-item filter-item-left">
				<span class="filter-name">类型：</span>
				<div class="filter-content">
					<select name="place-type" id="place-type" class="form-control" onchange="showPlaceTypeList()">
						${mcodeSetting.getMcodeSelect("DM-CDLX","","0")}
					</select>
				</div>
			</div>
		</div -->
		<div id="showPlaceTypeList">
			
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	showPlaceTypeList();
})

function showPlaceTypeList() {
	var url ='${request.contextPath}/officework/calss/attance/placeset/list';
    $("#showPlaceTypeList").load(url);
}
</script>
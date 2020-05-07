<style>
    .table .form-control{
        height: 36px;
    }
</style>
<div class="modal-body">

    <div class="input-group">
        <span class="input-group-addon" >serverName</span>
        <input id="updateModal_serverName" value="${redis_host!}:${redis_port!}" name="serverName" class="form-control" placeholder="serverName" readonly>
    </div>

    <div class="row">
        <br>
    </div>

    <div class="input-group">
        <span class="input-group-addon" >dbIndex</span>
        <input id="updateModal_dbIndex" value="${dbIndex}" name="dbIndex" class="form-control" placeholder="dbIndex" readonly>
    </div>

    <div class="row">
        <br>
    </div>

    <div class="input-group">
        <span class="input-group-addon" >key</span>
        <input id="updateModal_key" value="${key!}" name="key" class="form-control" placeholder="key">
    </div>

    <div class="row">
        <br>
    </div>

    <div class="input-group">
        <span class="input-group-addon" >dataType</span>
        <select name="" id="dataTypeSelect" class="form-control" onchange="dataTypeSelect();">
            <option value="STRING">STRING</option>
            <option value="LIST">LIST</option>
            <option value="ZSET">ZSET</option>
            <option value="SET">SET</option>
            <option value="HASH">HASH</option>
        </select>
    </div>

    <div class="row">
        <br>
    </div>

</div>
<script>
    function dataTypeSelect() {
        var vs = $('#dataTypeSelect option:selected').val();
    }
</script>
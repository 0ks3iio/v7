<#import "datav-create-diagram-config-panel-macro.ftl" as configPanel />
<@configPanel.sameConfig diagram=element diagramGroups=elementGroupVos datasourceTypes=datasourceTypes databases=databases apis=apis key="" root=rootDiagramId />
<script>
    $(function () {
        //是否按钮


        $('.iColor').iColor(function () {
            let config = {};
            let $valdom = $(this);
            config.key = $valdom.attr('name');
            config.value = $valdom.val();
            config.dom = $('div.box-data.choose')[0];
            config.arrayName = $valdom.attr('data-array');
            dataConfig.onChange(config);
        });
        $('.iColor').on('click', function () {
            $('#iColorPicker').css({
                right: '0px',
                left: ''
            })
        });

        $('.parameter-config').on('change', 'input', function () {
            dataConfig.onChange({
                key: $(this).attr('name'),
                value: $(this).val(),
                dom: $('div.box-data.choose')[0],
                arrayName: $(this).data('array')
            })
        });
        $('.parameter-config').on('change', 'select', function () {
            let name = $(this).attr('name');
            if (name == '') {

            }
            dataConfig.onChange({
                key: $(this).attr('name'),
                value: $(this).val(),
                dom: $('div.box-data.choose')[0],
                arrayName: $(this).data('array')
            })
        });
    })
</script>
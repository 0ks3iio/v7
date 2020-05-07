(function (root) {

    //监听
    $(document).keydown(function (event) {
        if (event.keyCode === 8 || event.keyCode === 46) {
            //delete diagram
            $('div.box-data.choose').each(function () {
                dataVUI.deleteDiagram(_screenId, $(this).attr('data-index'), function (diagramId) {
                    $(".scrollbar-made").find('dd[data-index=' + diagramId + ']').remove();
                });
            });
        }
    })
}(this));
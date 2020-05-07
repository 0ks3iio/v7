<div class=" overview-set bg-fff metadata clearfix">
	<input type="hidden" id="etlType" value=""/>
	<div id="kettle-div" class="filter-item active" onclick="showList('1');">
        <span>Kettle</span>
    </div>
    <div id="flink-div" class="filter-item" onclick="showList('6');">
        <span>Flink</span>
    </div>
    <div id="python-div" class="filter-item" onclick="showList('7');">
        <span>Python</span>
    </div>
    <!--
    <div id="spark-div" class="filter-item" onclick="showList('4');">
        <span>Spark</span>
    </div>
    -->
    <div id="kylin-div" class="filter-item" onclick="showList('2');">
        <span>Kylin</span>
    </div>
    <div id="sqoop-div" class="filter-item" onclick="showList('3');">
        <span>Sqoop</span>
    </div>
    <!--
	<div id="system-div" class="filter-item" onclick="showList('0');">
        <span>Java</span>
    </div>
    -->
    <div id="group-div" class="filter-item" onclick="showList('9');">
        <span>批处理组</span>
    </div>
    <#if etlMoreUrl! !="">
		<div id="quality-rule-div" class="filter-item" onclick="go2More();">
            <span>更多</span>
        </div>
	</#if>
</div>
<div class="box box-default scrollBar4 clearfix" id="contentDiv"></div>
<script type="text/javascript">
	function showList(etlType){
		var titleName;
		if(etlType =="1"){
	    	titleName='Kettle';			
	    }else if (etlType == "3"){
	    	titleName='Sqoop';		
	    }else if (etlType == "6"){
            titleName='Flink';
        }else if (etlType == "2"){
            titleName='Kettle';
        }else if (etlType == "4"){
            titleName='Spark';
        }else if (etlType == "0"){
	    	titleName='Java';
	    }else if (etlType == "7"){
            titleName='Python';
        }else if (etlType == "9"){
            titleName='批处理组';
        }
		router.go({
	        path: '/bigdata/etl/list?etlType='+etlType,
	        name:titleName,
	        level: 2
	    }, function () {
		    var index=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
		    });
	
			if(!etlType)
				etlType=$("#etlType").val();
	
		    if(etlType =="1"){
		    	$('#kettle-div').addClass('active').siblings().removeClass('active');			
		    }else if (etlType == "3"){
		    	$('#sqoop-div').addClass('active').siblings().removeClass('active');
		    }else if (etlType == "0"){
		    	$('#system-div').addClass('active').siblings().removeClass('active');
		    }else if (etlType == "4"){
                $('#spark-div').addClass('active').siblings().removeClass('active');
            }else if (etlType == "2"){
                $('#kylin-div').addClass('active').siblings().removeClass('active');
            }else if (etlType == "6"){
                $('#flink-div').addClass('active').siblings().removeClass('active');
            }else if (etlType == "7"){
                $('#python-div').addClass('active').siblings().removeClass('active');
            }else if (etlType == "9"){
                $('#group-div').addClass('active').siblings().removeClass('active');
            }
			$("#etlType").val(etlType);
	
			var url =  "${request.contextPath}/bigdata/etl/list?etlType="+etlType;
			$("#contentDiv").load(url,function() {
	  			layer.close(index);
			});
	    });
	}

	function go2More(){
	 	window.open("${etlMoreUrl!}","streamsets")
	}

	$(document).ready(function(){
		showList("${etlType?default('1')}");
	});

    function init() {
        var $ = go.GraphObject.make;  // for conciseness in defining templates
        myDiagram =
            $(go.Diagram, "myDiagramDiv",  // must name or refer to the DIV HTML element
                {
                    grid: $(go.Panel, "Grid",
                        $(go.Shape, "LineH", { stroke: "lightgray", strokeWidth: 0.5 }),
                        $(go.Shape, "LineH", { stroke: "gray", strokeWidth: 0.5, interval: 10 }),
                        $(go.Shape, "LineV", { stroke: "lightgray", strokeWidth: 0.5 }),
                        $(go.Shape, "LineV", { stroke: "gray", strokeWidth: 0.5, interval: 10 })
                    ),
                    "draggingTool.dragsLink": true,
                    "draggingTool.isGridSnapEnabled": true,
                    "linkingTool.isUnconnectedLinkValid": true,
                    "linkingTool.portGravity": 20,
                    "relinkingTool.isUnconnectedLinkValid": true,
                    "relinkingTool.portGravity": 20,
                    "relinkingTool.fromHandleArchetype":
                        $(go.Shape, "Diamond", { segmentIndex: 0, cursor: "pointer", desiredSize: new go.Size(8, 8), fill: "tomato", stroke: "darkred" }),
                    "relinkingTool.toHandleArchetype":
                        $(go.Shape, "Diamond", { segmentIndex: -1, cursor: "pointer", desiredSize: new go.Size(8, 8), fill: "darkred", stroke: "tomato" }),
                    "linkReshapingTool.handleArchetype":
                        $(go.Shape, "Diamond", { desiredSize: new go.Size(7, 7), fill: "lightblue", stroke: "deepskyblue" }),
                    "rotatingTool.handleAngle": 270,
                    "rotatingTool.handleDistance": 30,
                    "rotatingTool.snapAngleMultiple": 15,
                    "rotatingTool.snapAngleEpsilon": 15,
                    "undoManager.isEnabled": true
                });

        myDiagram.addDiagramListener("Modified", function(e) {
            var button = document.getElementById("SaveButton");
            if (button) button.disabled = !myDiagram.isModified;
            var idx = document.title.indexOf("*");
            if (myDiagram.isModified) {
                if (idx < 0) document.title += "*";
            } else {
                if (idx >= 0) document.title = document.title.substr(0, idx);
            }
        });

        function makePort(name, spot, output, input) {
            return $(go.Shape, "Circle",
                {
                    fill: null,  // not seen, by default; set to a translucent gray by showSmallPorts, defined below
                    stroke: null,
                    desiredSize: new go.Size(7, 7),
                    alignment: spot,  // align the port on the main Shape
                    alignmentFocus: spot,  // just inside the Shape
                    portId: name,  // declare this object to be a "port"
                    fromSpot: spot, toSpot: spot,  // declare where links may connect at this port
                    fromLinkable: output, toLinkable: input,  // declare whether the user may draw links to/from here
                    cursor: "pointer"  // show a different cursor to indicate potential link point
                });
        }
        var nodeSelectionAdornmentTemplate =
            $(go.Adornment, "Auto",
                $(go.Shape, { fill: null, stroke: "deepskyblue", strokeWidth: 1.5, strokeDashArray: [4, 2] }),
                $(go.Placeholder)
            );
        var nodeResizeAdornmentTemplate =
            $(go.Adornment, "Spot",
                { locationSpot: go.Spot.Right },
                $(go.Placeholder),
                $(go.Shape, { alignment: go.Spot.TopLeft, cursor: "nw-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { alignment: go.Spot.Top, cursor: "n-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { alignment: go.Spot.TopRight, cursor: "ne-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { alignment: go.Spot.Left, cursor: "w-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { alignment: go.Spot.Right, cursor: "e-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { alignment: go.Spot.BottomLeft, cursor: "se-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { alignment: go.Spot.Bottom, cursor: "s-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { alignment: go.Spot.BottomRight, cursor: "sw-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue" })
            );
        var nodeRotateAdornmentTemplate =
            $(go.Adornment,
                { locationSpot: go.Spot.Center, locationObjectName: "CIRCLE" },
                $(go.Shape, "Circle", { name: "CIRCLE", cursor: "pointer", desiredSize: new go.Size(7, 7), fill: "lightblue", stroke: "deepskyblue" }),
                $(go.Shape, { geometryString: "M3.5 7 L3.5 30", isGeometryPositioned: true, stroke: "deepskyblue", strokeWidth: 1.5, strokeDashArray: [4, 2] })
            );
        myDiagram.nodeTemplate =
            $(go.Node, "Spot",
                { locationSpot: go.Spot.Center },
                new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                { selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate },
                { resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate },
                { rotatable: true, rotateAdornmentTemplate: nodeRotateAdornmentTemplate },
                new go.Binding("angle").makeTwoWay(),
                // the main object is a Panel that surrounds a TextBlock with a Shape
                $(go.Panel, "Auto",
                    { name: "PANEL" },
                    new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                    $(go.Shape, "Rectangle",  // default figure
                        {
                            portId: "", // the default port: if no spot on link data, use closest side
                            fromLinkable: true, toLinkable: true, cursor: "pointer",
                            fill: "white",  // default color
                            strokeWidth: 2
                        },
                        new go.Binding("figure"),
                        new go.Binding("fill")),
                    $(go.TextBlock,
                        {
                            font: "bold 11pt Helvetica, Arial, sans-serif",
                            margin: 8,
                            maxSize: new go.Size(160, NaN),
                            wrap: go.TextBlock.WrapFit,
                            editable: true
                        },
                        new go.Binding("text").makeTwoWay())
                ),
                // four small named ports, one on each side:
                makePort("T", go.Spot.Top, false, true),
                makePort("L", go.Spot.Left, true, true),
                makePort("R", go.Spot.Right, true, true),
                makePort("B", go.Spot.Bottom, true, false),
                { // handle mouse enter/leave events to show/hide the ports
                    mouseEnter: function(e, node) { showSmallPorts(node, true); },
                    mouseLeave: function(e, node) { showSmallPorts(node, false); }
                }
            );
        function showSmallPorts(node, show) {
            node.ports.each(function(port) {
                if (port.portId !== "") {  // don't change the default port, which is the big shape
                    port.fill = show ? "rgba(0,0,0,.3)" : null;
                }
            });
        }
        var linkSelectionAdornmentTemplate =
            $(go.Adornment, "Link",
                $(go.Shape,
                    // isPanelMain declares that this Shape shares the Link.geometry
                    { isPanelMain: true, fill: null, stroke: "deepskyblue", strokeWidth: 0 })  // use selection object's strokeWidth
            );
        myDiagram.linkTemplate =
            $(go.Link,  // the whole link panel
                { selectable: true, selectionAdornmentTemplate: linkSelectionAdornmentTemplate },
                { relinkableFrom: true, relinkableTo: true, reshapable: true },
                {
                    routing: go.Link.AvoidsNodes,
                    curve: go.Link.JumpOver,
                    corner: 5,
                    toShortLength: 4
                },
                new go.Binding("points").makeTwoWay(),
                $(go.Shape,  // the link path shape
                    { isPanelMain: true, strokeWidth: 2 }),
                $(go.Shape,  // the arrowhead
                    { toArrow: "Standard", stroke: null }),
                $(go.Panel, "Auto",
                    new go.Binding("visible", "isSelected").ofObject(),
                    $(go.Shape, "RoundedRectangle",  // the link shape
                        { fill: "#F8F8F8", stroke: null }),
                    $(go.TextBlock,
                        {
                            textAlign: "center",
                            font: "10pt helvetica, arial, sans-serif",
                            stroke: "#919191",
                            margin: 2,
                            minSize: new go.Size(10, NaN),
                            editable: true
                        },
                        new go.Binding("text").makeTwoWay())
                )
            );

        myPalette =
            $(go.Palette, "myPaletteDiv",  // must name or refer to the DIV HTML element
                {
                    maxSelectionCount: 1,
                    nodeTemplateMap: myDiagram.nodeTemplateMap,  // share the templates used by myDiagram
                    linkTemplate: // simplify the link template, just in this Palette
                        $(go.Link,
                            { // because the GridLayout.alignment is Location and the nodes have locationSpot == Spot.Center,
                                // to line up the Link in the same manner we have to pretend the Link has the same location spot
                                locationSpot: go.Spot.Center,
                                selectionAdornmentTemplate:
                                    $(go.Adornment, "Link",
                                        { locationSpot: go.Spot.Center },
                                        $(go.Shape,
                                            { isPanelMain: true, fill: null, stroke: "deepskyblue", strokeWidth: 0 }),
                                        $(go.Shape,  // the arrowhead
                                            { toArrow: "Standard", stroke: null })
                                    )
                            },
                            {
                                routing: go.Link.AvoidsNodes,
                                curve: go.Link.JumpOver,
                                corner: 5,
                                toShortLength: 4
                            },
                            new go.Binding("points"),
                            $(go.Shape,  // the link path shape
                                { isPanelMain: true, strokeWidth: 2 }),
                            $(go.Shape,  // the arrowhead
                                { toArrow: "Standard", stroke: null })
                        ),
                    model: new go.GraphLinksModel([  // specify the contents of the Palette
                        { text: "Start", figure: "Circle", fill: "#00AD5F" },
                        { text: "Step" },
                        { text: "DB", figure: "Database", fill: "lightgray" },
                        { text: "es", figure: "Diamond", fill: "lightskyblue" },
                        { text: "End", figure: "Circle", fill: "#CE0620" },
                        { text: "Comment", figure: "RoundedRectangle", fill: "lightyellow" }
                    ], [
                        // the Palette also has a disconnected Link, which the user can drag-and-drop
                        { points: new go.List(/*go.Point*/).addAll([new go.Point(0, 0), new go.Point(30, 0), new go.Point(30, 40), new go.Point(60, 40)]) }
                    ])
                });
    }

    function saveFlowChartJson() {
        saveDiagramProperties();
        var saveModel = JSON.parse(myDiagram.model.toJson());
        if (saveModel.nodeDataArray.length != 0) {
            $.each(saveModel.nodeDataArray,function(index,item){
                if (item.size != null) {
                    var size = "";
                    $.each(item.size.split(' '),function(index,item){
                        size += " " + Math.round(Number(item));
                    });
                    item.size = size.substring(1);
                }
                if (item.loc != null) {
                    var loc = "";
                    $.each(item.loc.split(' '),function(index,item){
                        loc += " " + Math.round(Number(item));
                    });
                    item.loc = loc.substring(1);
                }
            });
            $.each(saveModel.linkDataArray,function(index,item){
                var array = new Array();
                $.each(item.points,function(index,item){
                    array[index] = Math.round(item);
                });
                item.points = array;
            });
            document.getElementById("flowChartJson").value = JSON.stringify(saveModel);
        } else {
            document.getElementById("flowChartJson").value = "";
        }
        myDiagram.isModified = false;
    }

    function saveDiagramProperties() {
        myDiagram.model.modelData.position = go.Point.stringify(myDiagram.position);
    }

    function loadFlowChartJson() {
        myDiagram.model = go.Model.fromJson(document.getElementById("flowChartJson").value);
        loadDiagramProperties();
    }
    function loadDiagramProperties() {
        var pos = myDiagram.model.modelData.position;
        if (pos) myDiagram.initialPosition = go.Point.parse(pos);
    }
</script>

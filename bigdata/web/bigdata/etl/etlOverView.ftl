<div class="index no-padding-bottom">
    <div class="row height-1of1 no-padding">
        <div class="col-md-4 height-1of2">
            <div class="box-type-one">
                <div class="box-type-head bg1">
                    <span>KETTLE</span>
                </div>
                <div class="box-type-body short">
                    <p>Kettle是个ETL工具集，支持丰富的数据源，主要用来完成数据的抽取，清洗、转换和加载等数据处理方面的工作。</p>
                    <button type="button" class="btn btn-blue pos-btn" onclick="openEtlByType('1')">点击进入</button>
                </div>
            </div>
        </div>
        <div class="col-md-4 height-1of2">
            <div class="box-type-one">
                <div class="box-type-head bg2">
                    <span>FLINK</span>
                </div>
                <div class="box-type-body short">
                    <p>Flink是一个分布式处理引擎，在有界或无界数据流上进行有状态的计算。Flink可以在所有通用的集群环境上运行，并且可以以内存级的速度进行大批量的计算。</p>
                    <button type="button" class="btn btn-blue pos-btn" onclick="openEtlByType('6')">点击进入</button>
                </div>
            </div>
        </div>
        <div class="col-md-4 height-1of2">
            <div class="box-type-one">
                <div class="box-type-head bg3">
                    <span>PYTHON</span>
                </div>
                <div class="box-type-body short">
                    <p>Python提供了强大的数据处理库，通过Python可以轻松的进行数据分析；同时Python还可以通过第三方库用于人工智能、机器学习等方面。</p>
                    <button type="button" class="btn btn-blue pos-btn" onclick="openEtlByType('7')">点击进入</button>
                </div>
            </div>
        </div>
        <div class="col-md-4 height-1of2">
            <div class="box-type-one">
                <div class="box-type-head bg4">
                    <span>SQOOP</span>
                </div>
                <div class="box-type-body short">
                    <p>Sqoop主要用于在Hadoop(Hive)与传统的数据库间进行数据的传递，可以将一个关系型数据库中的数据导进到Hadoop的HDFS中，也可以将HDFS的数据导进到关系型数据库中。</p>
                    <button type="button" class="btn btn-blue pos-btn" onclick="openEtlByType('3')">点击进入</button>
                </div>
            </div>
        </div>
        <div class="col-md-4 height-1of2">
            <div class="box-type-one">
                <div class="box-type-head bg5">
                    <span>KYLIN</span>
                </div>
                <div class="box-type-body short">
                    <p>Kylin主要提供Hadoop之上的SQL查询接口及多维分析（OLAP）能力以支持超大规模数据,通过预计算，用户可以与Hadoop数据进行亚秒级交互。</p>
                    <button type="button" class="btn btn-blue pos-btn" onclick="openEtlByType('2')">点击进入</button>
                </div>
            </div>
        </div>
        <div class="col-md-4 height-1of2">
            <div class="box-type-one">
                <div class="box-type-head bg6">
                    <span>批处理组</span>
                </div>
                <div class="box-type-body short">
                    <p>可以将多个业务相关联的批处理任务设置成一个组，按照预先设置的前后顺序依次执行。</p>
                    <button type="button" class="btn btn-blue pos-btn" onclick="openEtlByType('9')">点击进入</button>
                </div>
            </div>
        </div>

    </div>
</div>
<script>

    function openEtlByType(etlType) {
        $('.page-content').load('${request.contextPath}/bigdata/etl/detail?etlType='+etlType);
    }

</script>
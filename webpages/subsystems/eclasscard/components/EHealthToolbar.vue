<!-- 健康数据 -->
<template>
    <div id="dd" role="tabpanel">
        <div class="health-toolbar clearfix">
            <div class="btn-group pull-left">
                <button class="btn">&lt;</button>
                <button class="btn">&gt;</button>
            </div>
            <span class="health-date">4/28-5/05</span>
            <div class="btn-group pull-right">
                <button class="btn">日视图</button>
                <button class="btn active">周视图</button>
                <button class="btn">月视图</button>
            </div>
        </div>

        <div id="chart-step" style="height:450px;"></div>

        <ul class="health-data">
            <li>
                <span><i class="icon icon-run"></i></span>
                <em>8000</em>
                <h5>步数</h5>
            </li>
            <li>
                <span><i class="icon icon-km"></i></span>
                <em>10.1</em>
                <h5>公里</h5>
            </li>
            <li>
                <span><i class="icon icon-cal"></i></span>
                <em>566</em>
                <h5>千卡</h5>
            </li>
            <li>
                <span><i class="icon icon-award"></i></span>
                <em>14</em>
                <h5>班级排名</h5>
            </li>
        </ul>
    </div>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
    },
    data () {
        return {

        }
    },
    computed: {
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>

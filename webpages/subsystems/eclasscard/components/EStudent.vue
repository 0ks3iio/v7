<!--  -->
<template>
    <!-- S 学生列表 -->
    <ul class="checkin-stu-list" :style="{height: height}">
        <li class="checkin-animate" v-for="stu in stuList" :key="stu.id">
            <a class="js-noCheckin" href="javascript:void(0);">
                <div class="checkin-stu-avatar"><img src="../assets/imgs/male.png" alt=""></div>
                <span class="checkin-stu-name" style="marginTop: 10px">{{stu.studentName}}
                    <!-- <span class="checkin-flag" v-if="stu.leave">假</span> -->
                </span>
                <!-- <p class="checkin-stu-id" v-if="fullPath==='/classSpace'">学号：{{stu.id}}</p> -->
                <!-- <span :class="`label label-fill label-fill${stu.status === '已签到' ? '-bluePurple' : stu.status === '迟到' ? '-red' : ''}`" v-if="fullPath==='/signin'">{{stu.status}}</span> -->
            </a>
        </li>
    </ul><!-- E 学生列表 -->
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
    props: {
        height: {
            type: String,
            default: '695px'
        }
    },
    computed: {
        ...mapState({
			stuList: (store)=>{return store.classMsg.stuList}
        }),
        fullPath() {
            return this.$route.fullPath
        },
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    .checkin-stu-list{
        overflow: scroll;
    }
</style>

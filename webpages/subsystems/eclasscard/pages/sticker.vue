<!-- 门贴 --> 
<template>
	<div class="main">
        <!-- 标准公告 -->
		<div class="post-fullscreen post-fullscreen-default" v-if="type === 0">
            <div class="post-fullscreen-content post-fullscreen-content-default">
                <h2>2018春季校艺文化节举办通知</h2>
                <p>为丰富校园文化生活，提高学生活动覆盖面和受众面，由共青团南昌工学院委员会主办，南昌工学院学生会承办的南昌工学院首届“草坪音乐节”于4月14日在赣江体育场成功举办。虽然没有草莓音乐节的大牌，没有迷笛音乐节的专业，但南工“草坪音乐节”在2个多小时的音乐狂欢中，唤醒了沉睡的听觉，全场学生享受着这里独有的天空、草坪和音乐，处处散发着青春、活力和自由。</p>
            </div>
        </div>
        <!-- 喜报 -->
        <div class="post-fullscreen post-fullscreen-happy" v-if="type === 1">
            <div class="post-fullscreen-content post-fullscreen-content-happy">
                <span>恭喜本校荣获“浙江省高校2017年度‘美丽校园’”荣誉称号</span>
            </div>
        </div>
        <!-- 热烈欢迎 -->
        <div class="post-fullscreen post-fullscreen-welcome" v-if="type === 2">
            <div class="post-fullscreen-content post-fullscreen-content-welcome">
                <span>市教育局领导李宗贤、张育良、刘丽、董建华、陈萍莅临学校指导</span>
            </div>
        </div>
        <!-- 考场门贴 -->
        <div class="bgColor-green"  v-if="type === 3">
            <header class="header bgColor-darkgreen height-140">
                <div class="logo test-room"><span>第十三考场</span></div>
                <div class="date">
                    <span class="time">{{`${hour}:${minutes}`}}</span>
                    <div class="right">
                        <span class="day">{{`${mounth}月${date}日`}}</span>
                        <span class="week">{{day}}</span> 
                    </div>
                </div>
            </header>
            <div class="main-container no-padding">
                <div class="test-introduce">
                    <div class="grid">
                        <div class="grid-cell grid-1of3 text-right">
                            考试科目：物理
                        </div>
                        <div class="grid-cell grid-1of3 text-center">
                            监考老师：张田梦、甄天喜
                        </div>
                        <div class="grid-cell grid-1of3">
                            考试时间：10:00~12:00
                        </div>
                    </div>
                </div>
                <div class="examinee clearfix">
                    <ul class="student-place clearfix">
                        <li class="first-row clearfix">
                            <ul class="grid clearfix">
                                <li class="grid-cell grid-1of4 clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">01</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">01</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">01</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">01</span>
                                    </div>
                                </li>
                            </ul>
                        </li>
                        
                        <li class="first-row clearfix">
                            <ul class="grid clearfix">
                                <li class="grid-cell grid-1of4 clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">02</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">02</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">02</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">02</span>
                                    </div>
                                </li>
                            </ul>
                        </li>
                        <li class="first-row clearfix">
                            <ul class="grid clearfix">
                                <li class="grid-cell grid-1of4 clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">03</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">03</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">03</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">03</span>
                                    </div>
                                </li>
                            </ul>
                        <li class="first-row clearfix">
                            <ul class="grid clearfix">
                                <li class="grid-cell grid-1of4 clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">04</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">04</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">04</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">04</span>
                                    </div>
                                </li>
                            </ul>
                        </li>
                        <li class="first-row clearfix">
                            <ul class="grid clearfix">
                                <li class="grid-cell grid-1of4 clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">05</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">05</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">05</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">05</span>
                                    </div>
                                </li>
                            </ul>
                        </li>
                        <li class="first-row clearfix">
                            <ul class="grid clearfix">
                                <li class="grid-cell grid-1of4 clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">06</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">06</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">06</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">06</span>
                                    </div>
                                </li>
                            </ul>
                        </li>
                        <li class="first-row clearfix">
                            <ul class="grid clearfix">
                                <li class="grid-cell grid-1of4 clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">07</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">07</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">07</span>
                                    </div>
                                </li>
                                <li class="grid-cell grid-1of4  clearfix vetically-center">
                                    <div class="single clearfix">
                                        <span class="student-number">穆思明</span>
                                        <span class="num">07</span>
                                    </div>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
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
        ...mapState({
            type: store => {return store.sticker.type}
        }),
        ...mapGetters([
            'hour','minutes','day','mounth','date'
		]),
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    .main{
        width: 100%;
        height: 100%;
        .main-container{
            height: 940px;
        }
    }
</style>
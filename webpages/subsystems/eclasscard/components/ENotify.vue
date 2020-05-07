<!-- 通知公告组件 -->
<template>
    <div class="box">
        <div class="box-header">
            <h3 class="box-title">通知公告</h3>
            <div class="box-header-tool">
                <label>
                    <input type="checkbox" value="班级" class="wp" v-model="grade">
                    <span class="lbl"> 班级</span>
                </label>
                <label>
                    <input type="checkbox" value="校级" class="wp" v-model="grade">
                    <span class="lbl"> 校级</span>
                </label>
            </div>
        </div>
        <div class="box-body">
            <div class="post-container" :style="{height:height}">
                <ul class="post-list" data-action="scroll" ref="notifyList" v-if="norifyList.length">
                    <li v-for="(notify,index) in norifyList" :key="index"  v-if="~grade.indexOf(notify.grade)" @click="postDetail"> 
                        <img width="140" height="112" :src="notify.img" alt="" class="post-img">
                        <h4 class="post-title"><span class="label label-line label-line-purple">{{notify.grade}}</span>{{notify.title}}</h4>
                        <p class="post-summary">{{notify.content}}</p>
                        <p class="post-time">{{notify.time}}</p>
                    </li>
                </ul>
                <div class="no-data center" v-else>
                    <div class="no-data-content">
                        <img src="../assets/imgs/no-post.png" alt="">
                        <p>暂无公告，请前往后台发布</p>
                    </div>
                </div>
            </div>
        </div>
        <e-layer :show="showLayer" @closeDetail="closeDetail"/>
    </div>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
import ELayer from './ELayer'
export default {
    name: '',
    components: {
        ELayer
    },
    data () {
        return {
            grade: ['班级','校级'],
            transDistance: 0,
            showLayer: false,
        }
    },
    computed: {
        ...mapState({
            norifyList: store => {return store.notify.notify}
        })
    },
    watch: {
        grade(newVal){
        }
    },
    props: {
        height: {
            type: String,
            default: '300px'
        }
    },
    methods: {
        postDetail() {
           this.showLayer = true
        },
        closeDetail() {
            this.showLayer = false
        }
    },
    mounted() {
        this.int = window.setInterval(()=>{
            if(this.$refs.notifyList && this.$refs.notifyList.children.length>2){
                let firHeight = this.$refs.notifyList.firstChild.offsetHeight
                this.$refs.notifyList.style.transition = `transform .5s ease-in-out`
                this.$refs.notifyList.style.transform = `translateY(-${firHeight}px)`
                setTimeout(() => {
                    if(this.$refs.notifyList){
                        this.$refs.notifyList.style.transition = ''
                        this.$refs.notifyList.style.transform = `translateY(0)`
                        this.$store.commit('changeOrder')
                    }
                }, 1000);
            }
        },30000)
    },
    destroyed() {
        window.clearInterval(this.int)
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss" type="text/css">
</style>

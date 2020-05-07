<!-- 头部组件 -->
<template>
    <header class="header">
		<div class="logo"><span>{{classMsg.className}}</span></div>
		<div class="date">
			<span class="time">{{`${hour}:${minutes}`}}</span>
			<div class="right">
				<span class="day">{{`${mounth}月${date}日`}}</span>
				<span class="week">{{day}}</span> 
			</div>
		</div>
		<div class="notice" v-if="this.$route.fullPath === '/' && noticeList.length">
			<span class="icon icon-voice"></span>
			<div class="notice-list-wrapper">
				<ul class="notice-list wordsLoop inline" ref="noticeList">	
					<li v-for="(notice,index) in noticeList" :key="index">{{notice}}</li>
				</ul>
			</div>
		</div>
	</header>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
export default {
    name: 'eheader',
    components: {
    },
    data () {
        return {
			int: null,
			noticeWidth: null,
        }
    },
    computed: {
        ...mapState({
			classMsg: (store)=>{return {...store.classMsg}},
			noticeList: (store) => {return [...store.notify.notice]}
        }),
        ...mapGetters([
            'hour','minutes','day','mounth','date'
		]),
    },
    updated() {
		this.noticeWidth = this.$refs.noticeList ? this.$refs.noticeList.offsetWidth : null
	},
	mounted() {
		this.noticeWidth = this.$refs.noticeList ? this.$refs.noticeList.offsetWidth : null
		this.int = window.setInterval(()=>{
            if(this.$refs.noticeList && this.noticeWidth >= 700){
                let firWidth = this.$refs.noticeList.firstChild.offsetWidth
                this.$refs.noticeList.style.transition = `transform 4.98s linear`
                this.$refs.noticeList.style.transform = `translateX(-${firWidth}px)`
                setTimeout(() => {
					if(this.$refs.noticeList){
						this.$refs.noticeList.style.transition = ''
						this.$refs.noticeList.style.transform = `translateY(0)`
						this.$store.commit('changeNotice')
					}
                }, 4980);
            }
        },5000)
	},
	destroyed() {
        window.clearInterval(this.int)
	},
	created() {
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
	.inline{
		display: inline-block;
	}
</style>

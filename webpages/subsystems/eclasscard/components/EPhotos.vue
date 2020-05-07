<!--  -->
<template>
    <div class="album-wrapper">
        <div class="album-list-wrapper">
            <div :id="data[0].id" class="class-photos active" ref="scroller">
                <ul id="gallery" class="photo-list" v-if="classAlbum[albumIndex]">
                    <li v-for="(photo,index) in classAlbum[albumIndex].attachments" :key="index">
                        <div class="img-wrap img-wrap-16by9" :style="`background:no-repeat url(${host+photo.showPicUrl}) center center / cover;`" />
                        <h4>{{photo.filename}}</h4>
                    </li>
                </ul>
            </div>
        </div>
        <div class="album-nav-wrapper">
            <ul class="album-nav">
                <li :class="{active:albumIndex === index}" v-for="(album,index) in classAlbum" :key="index" @click="albumClick(index)">
                    <a href="javascript:void(0)">{{album.title}}<span>{{`（ ${album.number} ）`}}</span></a>
                </li>
            </ul>
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
            albumIndex: 0,
            data: [
				{
					id: 'album01',
					name: '校音乐节校音乐节校音乐节',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/honor-cup.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						}
					]
				},
				{
					id: 'album02',
					name: '2017年全校运动会',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/honor-medal.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/honor-medal.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/honor-medal.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						}
					]
				},
				{
					id: 'album03',
					name: '马拉松合集',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/honor-cup.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						},
						{
							title: '校音艺术节合唱比赛',
							src: require('../assets/imgs/img.png')
						}
					]
				},
				{
					id: 'album04',
					name: '马拉松合集',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛04',
							src: require('../assets/imgs/honor-cup.png')
						}
					]
				},
				{
					id: 'album05',
					name: '马拉松合集',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛05',
							src: require('../assets/imgs/honor-cup.png')
						}
					]
				},
				{
					id: 'album06',
					name: '马拉松合集',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛06',
							src: require('../assets/imgs/honor-cup.png')
						}
					]
				},
				{
					id: 'album07',
					name: '马拉松合集',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛07',
							src: require('../assets/imgs/honor-cup.png')
						}
					]
				},
				{
					id: 'album08',
					name: '马拉松合集',
					num: 32,
					photos: [
						{
							title: '校音艺术节合唱比赛08',
							src: require('../assets/imgs/honor-cup.png')
						}
					]
				}
			]
        }
    },
    methods: {
        albumClick(index) {
			this.albumIndex = index
			this.$refs.scroller.scrollTo(0,0)
        }
    },
    computed: {
		...mapState({
			classAlbum: store => {return store.classMsg.classAlbum}
		}),
		host () {
            return this.$store.state.domain
		}
    },
    mounted() {
		this.$store.dispatch('getClassAlbum')
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    .class-photos{
        height: 650px;
        overflow-x: hidden;
        overflow-y: auto; 
    }
    .album-nav{
        display: flex;
        flex-flow: row nowrap;
        justify-content: flex-start;
        align-items: center;
        overflow-x: auto;
        li{
            flex: 0 0 auto;
        }
    }
</style>

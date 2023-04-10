<template>
	<view>
		<view class="content">
			<swiper :indicator-dots="false" :autoplay="true" :interval="3000" :duration="1000">
				<swiper-item>
					<view class="swiper-item">
						<img src="../../static/baneer.png" alt="">
					</view>
				</swiper-item>
				<swiper-item>
					<view class="swiper-item"></view>
				</swiper-item>
			</swiper>
			<view class="nav">
				<view class="all" @click="alls()" :class="bg == 0 ? 'all-hov' : '' ">
					<text>全部</text>
					<uni-badge class="uni-badge-left-margin" :text="jiao" />
				</view>
				<uni-data-select v-model="value" :localdata="range" @change="change" :clear="false"></uni-data-select>
				<uni-data-select v-model="values" :localdata="ranges" @change="changes" :clear="false">
				</uni-data-select>
			</view>
			<view class="article">
				<view class="frame" v-for="(all,index) in all " :key="index" @click="detail($event,index)">
					<view class="pinfen" v-if="all.score > 2" style="background-color: skyblue;color:white;">
						<text>及格</text>
					</view>
					<view class="pinfen" v-if="all.score < 3" style="background-color: red;color:white;">
						<text>不及格</text>
					</view>
					<view class="title">
						<text>{{all.course}}</text>

						<text v-if="all.score != undefined">{{all.score}}星</text>
						<text v-if="all.score == undefined">老师未评分</text>
					</view>
					<view class="cot">
						<text>{{all.content}}</text>
					</view>
					<view class="pinglun">
						<view class="left">
							<text>评审老师:{{all.tname}}</text>
						</view>
						<view class="right">
							<text>{{all.subtime}}</text>
						</view>
					</view>
				</view>
			</view>
			<!-- 			<view class="fye">
				<uni-pagination :total="50" title="标题文字" />
			</view> -->
			<navbar></navbar>
		</view>
	</view>
</template>

<script>
	import navbar from "@/components/navbar/navbar.vue"
	import {
		summarize
	} from "@/api/summarize.js"
	export default {
		data() {
			return {
				bg: 0,
				jiao: "",
				value: 1,
				values: 3,
				text: "我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪，我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪，我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪，我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪，我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪，我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪，我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪，我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪",
				range: [{
						value: 1,
						text: "已阅读"
					},
					{
						value: 2,
						text: "未阅读"
					},
				],
				ranges: [{
						value: 3,
						text: "及格"
					},
					{
						value: 4,
						text: "不及格"
					},
				],
				all: [],
				id: "",
				msg: [],
				list: [],
				index: [],
				start: [],
				score: [],
				loadmore: {
					pages: 1, //第一页
					total: 0, //总页数
					size: 0, //每页显示的数据
					current: 1 // 当前页码
				}
			}
		},
		onLoad() {
			//全局变量获取并赋值
			getApp().globalData.text = this.text;
			this.text = getApp().globalData.text;
		},
		//页面显示一次就执行的方法
		onShow() {
			this.alls();
		},
		components: {
			navbar
		},
		// async onReachBottom() {
		// 	let {
		// 		pages,
		// 	} = this.loadmore
		// 	this.loadmore.current++
		// 	if (this.loadmore.current <= pages) {
		// 		const slg = {
		// 			current: this.loadmore.current,
		// 			size: 5,
		// 			state: 0,
		// 		}
		// 		const result = await information(slg);
		// 		this.all = [...this.all, ...result.data.data.records]
		// 		console.log(result);
		// 	}else{
		// 		uni.showToast({
		// 			title:'没有更多了',
		// 			icon:"none"
		// 		})
		// 	}
		// },
		methods: {
			async change(e) {
				this.bg = 1;
				console.log("e:", e)
				const als = {
					state: e
				}
				const sum = await summarize(als)
				this.all = sum.data.data;
				console.log(this.all);
			},

			//异步发送方法
			async changes(e) {
				this.bg = 2;
				console.log("e:", e)
				const als = {
					state: e
				}
				const sum = await summarize(als)
				this.all = sum.data.data;
				console.log(this.all);
			},

			//获取数据
			async alls() {
				// console.log(this.bg);
				this.bg = 0
				const als = {
					state: 0,
					size:5
				}
				const alls = await summarize(als)
				this.all = alls.data.data
				this.jiao = this.all.length
				console.log(this.all);
				// this.loadmore = {
				// 	pages : alls.data.pages,
				// 	total : alls.data.total,
				// 	size : alls.data.size,
				// 	current : alls.data.current
				// }
			},

			//路径传参对应的下标页面
			detail(e, index) {
				this.id = this.all[index];
				console.log(this.id);
				uni.navigateTo({
					url: `/pages/Details/Details?msg=${encodeURIComponent(JSON.stringify(this.id))}`,
				});
			},
		},
	}
</script>

<style>
	page {
		padding-bottom: 80rpx;
	}

	.swiper-item {
		height: 245rpx;
	}

	.swiper-item img {
		width: 100%;
	}

	.nav {
		height: 100rpx;
		background: white;
		display: flex;
		align-items: center;
		justify-content: space-around;
		text-align: center;
	}

	.nav .all {
		width: 225rpx;
		height: 70rpx;
		border-radius: 10rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
	}

	.uni-badge-left-margin {
		position: absolute;
		top: 10rpx;
		right: 0;
	}

	.nav uni-data-select {
		width: 225rpx;
		height: 70rpx;
	}

	.all-hov {
		background: #479d83;
		color: white;
	}

	.article {
		background: #e9edf9;
		/* height: 300rpx; */
		padding-top: 35rpx;
		padding-bottom: 40rpx;
	}

	.article .frame {
		width: 636rpx;
		height: 259rpx;
		background: #ffffff;
		margin: 0 auto;
		border-radius: 5rpx;
		position: relative;
		margin-top: 40rpx;
		/* margin-bottom: 40rpx; */
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.title {
		width: 500rpx;
		font-size: 16px;
		padding: 25rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.title text:nth-child(2) {
		font-size: 14px;
	}

	.cot {
		width: 590rpx;
		padding-left: 25rpx;
		/* word-wrap: break-word; */
		font-size: 14px;
		color: #82b989;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 3;
		/*想省略几行就写几*/
		-webkit-box-orient: vertical;

	}

	.pinfen {
		width: 100rpx;
		height: 40rpx;
		position: absolute;
		right: 0;
		top: 25rpx;
		background: #f1dddd;
		display: flex;
		align-items: center;
		justify-content: center;
		border-bottom-left-radius: 10rpx;
		border-top-left-radius: 10rpx;
		font-size: 10px;
		color: #d44b4b;
	}

	.pinglun {
		width: 590rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		color: #939393;
		margin: 15rpx auto;
		font-size: 10px;
	}
</style>

<template>
	<view>
		<view class="content">

			<view class="new" v-for="(news,index) in list" @click="detail($event,index)" :key="index">
				<view class="top" v-if="news.subtime == null">
					<view class="title">
						<view class="title-left">
							<text>您收到一条作业通知</text>
						</view>
						<view class="title-right">
							<text>{{news.chetime}}</text>
						</view>
					</view>
					<view class="cot">
						<text>点击签收</text>
					</view>
				</view>

				<view class="topc,new-bg" v-if="news.subtime != null">
					<view class="title">
						<view class="title-left">
							<text>您收到一条作业通知</text>
						</view>
						<view class="title-right">
							<text>{{news.chetime}}</text>
						</view>
					</view>
					<view class="cot">
						<text>点击签收</text>
					</view>
				</view>
			</view>
		</view>
		<navbar></navbar>
	</view>
</template>

<script>
	//导入导航栏组件
	import navbar from "@/components/navbar/navbar.vue"
	//引用请求方法
	import {
		information,
		check,
	} from "@/api/information.js"
	export default {
		data() {
			return {
				outshow: false,
				outbg: 0,
				id: "",
				newid: "",
				list: {},
				msg: [],
				status: 'more',
				text: "我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪,我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪我们也进入了梦乡。一觉醒来时，正是一个寂静的冬日之晨。窗台上堆着厚厚的积雪",
				loadmore: {
					pages: 1, //第一页
					total: 0, //总页数
					size: 0, //每页显示的数据
					current: 1 // 当前页码
				}
			}
		},
		methods: {
			//获取列表展示数据
			async news() {
				const req = {
					whethertopush: 2,
					thejobtype: "null",
					size: 5,
				};
				
				const xiaoxi = await information(req)
				this.list = xiaoxi.data.data.records;
				this.loadmore = {
					pages : xiaoxi.data.data.pages,
					total : xiaoxi.data.data.total,
					size : xiaoxi.data.data.size,
					current : xiaoxi.data.data.current
				}
				console.log(this.list);

			},
			async onReachBottom() {
				let {
					pages,
				} = this.loadmore
				this.loadmore.current++
				if (this.loadmore.current <= pages) {
					const slg = {
						current: this.loadmore.current,
						size: 5,
						whethertopush: 2,
						thejobtype: "null",
					}
					const result = await information(slg);
					this.list = [...this.list, ...result.data.data.records]
					console.log(result);
				}else{
					uni.showToast({
						title:'没有更多了',
						icon:"none"
					})
				}
			},
			//提交数据
			async detail(e, index) {
				this.id = this.list[index].id;
				this.newid = this.id;
				console.log(this.newid);
				const req = {
					aid: this.newid
				}
				const ent = await check(req);
				//路由带参
				uni.navigateTo({
					url: `/pages/new/news?msg=${encodeURIComponent(JSON.stringify(this.list))}`,
				});
			},
			onChange(e) {
				this.status = e.detail.value
			},
			clickLoadMore(e) {
				uni.showToast({
					icon: 'none',
					title: "当前状态：" + e.detail.status
				})
			}

		},
		onShow() {
			this.news()
		},
		components: {
			navbar
		},
	}
</script>

<style>
	page {
		background: #eaeef9;
		padding-bottom: 90rpx;
		padding-top: 100rpx;
	}

	/* 	.new {
			width: 700rpx;
			height: 230rpx;
			background: #21ab38;
			display: flex;
			flex-direction: column;
			margin: 40rpx auto;
			border-bottom-right-radius: 35rpx;
			overflow: hidden;
		} */
	.top {
		width: 700rpx;
		height: 230rpx;
		display: flex;
		flex-direction: column;
		margin: 40rpx auto;
		border-bottom-right-radius: 35rpx;
		background: #22ac38;
		overflow: hidden;
	}

	.topc {
		width: 700rpx;
		height: 230rpx;
		display: flex;
		flex-direction: column;
		margin: 40rpx auto;
		border-bottom-right-radius: 35rpx;
		overflow: hidden;
	}

	.title {
		height: 50rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 25rpx;
	}

	.title-left {
		font-size: 18px;
		color: white;
	}

	.title-right {
		font-size: 10px;
		color: white;
	}

	.cot {
		width: 650rpx;
		/* padding-left: 25rpx; */
		/* background: red; */
		font-size: 12px;
		margin: 0 auto;
		color: white;
		/* overflow: hidden; */
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 5;
		/*想省略几行就写几*/
		-webkit-box-orient: vertical;
	}

	.new-bg {
		background: gray;
	}
</style>

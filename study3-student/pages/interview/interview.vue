<template>
	<view>
		<view class="content">
			<view class="alla" v-for="(list,index) in list" :key="index">
				<view class="frame">
					<view class="fart">
						<text>讲授语速</text>
						<uni-rate isFill="false" readonly="true" size="20" :value="list.a1" @change="onChange()" />
					</view>
					<view class="fart">
						<text>课堂案例</text>
						<uni-rate isFill="false" readonly="true" size="20" :value="list.a2" @change="onChange()" />
					</view>
					<view class="fart">
						<text>答疑解忧</text>
						<uni-rate isFill="false" readonly="true" size="20" :value="list.a3" @change="onChange()" />
					</view>
				</view>
				<view class="frame">
					<view class="fart">
						<text>听课情况</text>
						<uni-rate isFill="false" readonly="true" size="20" :value="list.a4" @change="onChange()" />
					</view>
					<view class="fart">
						<text>练习完成</text>
						<uni-rate isFill="false" readonly="true" size="20" :value="list.a5" @change="onChanges()" />
					</view>
					<view class="fart">
						<text>讲师技能</text>
						<uni-rate isFill="false" readonly="true" size="20" :value="list.a6" @change="onChangess()" />
					</view>
				</view>
				<view class="article">
					<text>{{list.crtm}}</text>
					<text>内容</text>
					<text v-if="list.readst == 1" class="read">已读</text>
					<text v-if="list.readst == 0" class="readred">未读</text>
					<text class="chao">{{list.content}}</text>
				</view>
			</view>
			<view class="nav">
				<view class="yuan" @click="add()">
					<uni-icons type="plusempty" size="28" color="white"></uni-icons>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		inter
	} from "@/api/Evaluation.js"
	export default {
		data() {
			return {
				list: {},
				loadmore: {
					pages: 1, //第一页
					total: 0, //总页数
					size: 0, //每页显示的数据
					current: 1 // 当前页码
				}
			}
		},
		async onReachBottom() {
			let {
				pages,
			} = this.loadmore
			this.loadmore.current++
			if (this.loadmore.current <= pages) {
				const slg = {
					current: this.loadmore.current,
					size: 10,
					readst: ""
				}
				const result = await inter(slg);
				this.list = [...this.list, ...result.data.data.records]
				console.log(result);
			}else{
				uni.showToast({
					title:'没有更多了',
					icon:"none"
				})
			}
		},
		methods: {
			async repost() {
				const slg = {
					readst: "",
					size: 10,
				}
				const res = await inter(slg)
				this.list = res.data.data.records
				// console.log(res);
				console.log(this.list);
				this.loadmore = {
					pages : res.data.data.pages,
					total : res.data.data.total,
					size : res.data.data.size,
					current : res.data.data.current
				}
			},
			add() {
				uni.navigateTo({
					url: "/pages/interview/interviews"
				})
			}
		},
		onShow() {
			this.repost()
		}
	}
</script>

<style>
	textarea {
		height: 0;
	}

	.alla {
		width: 700rpx;
		/* height: 900rpx; */
		margin: 30rpx auto;
		background: #A9A6E3;
		border-radius: 10rpx;
	}

	.frame {
		display: flex;
		justify-content: space-around;
		padding-top: 20rpx;
		color: white;
	}

	.fart {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.article {
		width: 680rpx;
		/* margin-top: 30rpx; */
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-between;
		color: white;
		position: relative;
	}
	.chao {
		width: 650rpx;
		/* height: 130rpx; */
		margin: 0 auto;
		word-wrap: break-word;
		overflow: hidden;
	}

	.read {
		width: 120rpx;
		height: 40rpx;
		border-radius: 10rpx;
		background: green;
		position: absolute;
		right: 0;
		text-align: center;
		font-size: 14px;
	}
	.readred{
		width: 120rpx;
		height: 40rpx;
		border-radius: 10rpx;
		background: red;
		position: absolute;
		right: 0;
		text-align: center;
		font-size: 14px;
	}
	.nav {
		width: 100%;
		height: 80rpx;
		background: #F1FAF3;
		position: fixed;
		bottom: 0;
		display: flex;
		justify-content: center;
	}

	.yuan {
		width: 100rpx;
		height: 100rpx;
		position: fixed;
		background: #FDF71C;
		bottom: 30rpx;
		border-radius: 50rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>

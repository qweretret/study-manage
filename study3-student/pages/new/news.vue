<template>
	<view>
		<view class="top">

			<view class="title">
				<view class="left">
					<text>类型:{{list[0].thejobtype}}</text>
				</view>
				<view class="right">
					<text>{{list[0].chetime}}</text>
				</view>
			</view>

			<view class="content">
				<text>{{list[0].content}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [],
				index: 0,
				content: {}
			}
		},
		
		//路径传参接受
		onLoad($options) {
			// console.log($options.msg);
			let msg = decodeURIComponent($options.msg);
			this.list = JSON.parse(msg);
		},
		async onReachBottom() {
			const slg = {
				current: this.loadmore.current,
				size: 5,
			}
			const result = await summarize(slg);
			this.all = [...this.studensInfo, ...result.data.data.records]
		},
		methods: {

		}
	}
</script>

<style>
	page {
		background: #eaeef9;
	}

	.top {
		width: 700rpx;
		height: 700rpx;
		margin: 30rpx auto;
		background: #ffffff;
		border-radius: 20rpx;
	}

	.title {
		/* height: 40rpx; */
		text-align: center;
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding-top: 50rpx;
	}

	.left {
		font-size: 14px;
	}

	.right {
		font-size: 14px;
	}

	.content {
		width: 600rpx;
		/* text-align: center; */
		padding-top: 40rpx;
		padding-left: 50rpx;
		font-size: 16px;
		word-wrap: break-word;
	}
</style>

<template>
	<view>
		<view class="content">
			<view class="top">
				<view class="article">
					<view class="summarize">
						<view class="t-left">
							<text>课程:{{list.course}}</text>
						</view>
						<view class="r-right">
							<text>{{subtime}}</text>
						</view>
					</view>
					<view class="title">
						<text>{{list.title}}</text>
					</view>
					<view class="cot">
						<text>{{list.content}}</text>
					</view>

				</view>
			</view>


			<navbar></navbar>
		</view>
	</view>
</template>

<script>
	import navbar from "@/components/navbar/navbar.vue"
	export default {
		data() {
			return {
				value: 5,
				values: 5,
				text: "",
				list: {},
				time: "",
				subtime:""
			}
		},
		components: {
			navbar
		},

		//路径传参接受
		onLoad($options) {
			this.text = getApp().globalData.text;
			let msg = decodeURIComponent($options.msg);
			this.list = JSON.parse(msg);
			this.time = this.list.subtime;
			this.subtime = this.time.substring(0,10)
			// console.log("time:" + this.time.substring(0,10));
		},
		methods: {
			onChange(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.value = e.value;
				// this.value = this.newvalue;s
			},
			onChanges(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.values = e.value;
				// this.value = this.newvalue;
			}
		}
	}
</script>

<style>
	page {
		background: #eaeef9;
	}

	.top {
		width: 660rpx;
		height: 550rpx;
		background: #ffffff;
		margin: 25rpx auto;
		border-radius: 20rpx;
	}

	.article {
		width: 600rpx;
		margin: 0 auto;
		padding-top: 40rpx;
	}

	.summarize {
		display: flex;
		justify-content: space-between;
	}

	.t-left {
		font-size: 20px;
	}

	.r-right {
		font-size: 12px;
	}

	.title {
		margin-top: 20rpx;
		font-weight: bold;
	}

	.cot {
		width: 610rpx;
		margin-top: 40rpx;
		margin: 50rpx auto;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 7;
		/*想省略几行就写几*/
		-webkit-box-orient: vertical;
	}

	.bot {
		width: 660rpx;
		height: 388rpx;
		background: #ffffff;
		border-radius: 20rpx;
		margin: 50rpx auto;
	}

	.b-title {
		padding: 30rpx;
		font-size: 20px;
	}

	.frame {
		display: flex;
		justify-content: space-between;
	}

	.start {
		width: 50%;
		padding: 30rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.b-bot {
		/* width: 30%; */
		height: 75rpx;
		display: flex;
	}

	.left {
		width: 43rpx;
		height: 38rpx;
		background-color: #36474f;
		display: flex;
		align-items: center;
		justify-content: center;
		border-bottom-left-radius: 20rpx;
		border-top-left-radius: 20rpx;
		font-size: 12px;
		padding-left: 5rpx;
		color: white;
	}

	.right {
		width: 170rpx;
		height: 40rpx;
		background: #fafaf9;
		display: flex;
		align-items: center;
		padding-left: 10rpx;
		border-bottom-right-radius: 20rpx;
		border-top-right-radius: 20rpx;
	}
</style>

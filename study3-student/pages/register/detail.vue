<template>
	<view>
		<view class="content">
			<view class="frame">
				<view class="name">
					<text>公司名称:</text>
					<view class="ipt">
						<text>{{list.cmpname}}</text>
					</view>
				</view>

				<view class="scale">
					<view class="scales">
						<text>面试时间:</text>
						<text>{{list.mstm}}</text>
					</view>
					<view class="scales">
						<text>登记人:</text>
						<text>{{list.dbColumn_stuid}}</text>
					</view>
				</view>
				<view class="scale">
					<view class="scales">
						<text>招聘岗位:</text>
						<view class="ipt">
							<text>{{list.postid}}</text>
						</view>
					</view>
					<view class="scales">
						<text>面试准备:</text>
						<uni-rate :value="list.mszbqk" :readonly="true" />
					</view>
				</view>
				<view class="scale">
					<view class="scales">
						<text>面试时长:</text>
						<text>{{list.mslast}}分钟</text>
					</view>
					<view class="scales">
						<text>面试结果:</text>
						<text>{{list.msresult}}</text>
					</view>
					<view class="scales">
						<text>面试过程评分:</text>
						<uni-rate :value="list.msscore" :readonly="true" />
					</view>
				</view>
				<view class="scale">
					<view class="scales">
						<text>面试过程评分:</text>
						<uni-rate :value="list.msscore" :readonly="true" />
					</view>
				</view>
				<view class="name">
					<text>面试总结:</text>
					<view class="ipt">
						<text>{{list.mszj}}</text>
					</view>
				</view>
				<view class="name">
					<text>面试难题:</text>
					<view class="ipt" v-for="(img,index) in imglist" :key="index" @click="open()">
						<img :src="img" alt="">
					</view>
				</view>
				<view class="name">
					<text>面试点评:</text>
					<view class="ipt">
						<text v-if="list.msdp">{{list.msdp}}</text>
						<text v-else>无</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>


<script>
	import {
		pathToBase64,
		base64ToPath
	} from '@/js_sdk/mmmm-image-tools/index.js'
	export default {
		data() {
			return {
				list: {},
				imglist: [],
				newimg: ""
			}
		},
		methods: {
			open() {
				uni.previewImage({
					urls: this.imglist,
					longPressActions: {
						itemList: ['发送给朋友', '保存图片', '收藏'],
						success: function(data) {
							console.log('选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data.index + 1) + '张图片');
						},
						fail: function(err) {
							console.log(err.errMsg);
						}
					}
				});
			}
		},
		onLoad($options) {
			let msg = decodeURIComponent($options.msg);
			this.list = JSON.parse(msg);
			// console.log(this.list.exam[0]);
			this.imglist = JSON.parse(this.list.exam)
			// console.log(this.list);
		},
		onShow() {}
	}
</script>

<style>
	page {}

	.content {
		width: 700rpx;
		margin: 0 auto;
		background: lightskyblue;
		border-radius: 10rpx;
	}

	.name .ipt {
		/* height: 50rpx; */
		overflow: hidden;
		word-wrap: break-word;
		display: flex;
		flex-wrap: wrap;
	}

	.name img {
		width: 200rpx;
		height: 100rpx;
		/* background: yellow; */
	}

	.name .ipt text {
		height: 100rpx;
	}

	.name {
		margin-top: 30rpx;
		/* margin-bottom: 40rpx; */
	}

	.frame {
		width: 650rpx;
		margin: 0 auto;
		padding-top: 10rpx;
	}

	.xi {
		margin-top: 30rpx;
	}

	.scale {
		display: flex;
		margin-top: 20rpx;
		justify-content: space-between;
	}

	.scales {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		margin-bottom: 20rpx;
	}

	.scales text {
		margin-bottom: 10rpx;
	}

	.scales uni-combox {
		width: 80%;
	}

	.phone {
		display: flex;
		align-items: center;
	}

	.phone text {
		/* margin-bottom: 10rpx; */
	}

	.xueli,
	.yqiu,
	.bzu,
	.miaos,
	.phone {
		margin-top: 20rpx;
	}

	.xitwo {
		display: flex;
		margin-top: 20rpx;
		/* justify-content: space-between; */
	}

	.xitwo .ipt {
		width: 90%;
	}

	.xithree {
		display: flex;
		margin-top: 20rpx;
		justify-content: space-between;
	}

	.xithree .ipt {
		width: 90%;
	}
</style>

<template>
	<view>
		<view class="content">
			<view class="top">
				<view class="left">
					<image :src="newimg"  @click="look()"></image>
				</view>
				<view class="right">
					<text>姓名:{{name}}</text>
					<text>班级:{{classname}}</text>
				</view>
				<view class="edit" @click="edit()">
					<text>编辑资料</text>
				</view>
			</view>
			<view class="bottom">
				<view class="frame">
					<uni-list-item showArrow title="我的班级" clickable="true" to="/pages/Class/Class" />
					<uni-list-item showArrow title="设置" clickable="true" />
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		student
	} from "@/api/class.js"
	export default {
		data() {
			return {
				student: [],
				stuimg: [],
				name: "",
				classname: "",
				imgurl: "",
				newimg:""
			}
		},
		methods: {
			async getPersonal() {
				const result = await student();
				this.student = result.data.data
				let sums = this.student;
				// console.log(sums);
				sums.map((res) => {
					this.imgurl = res.head.replace(/[\r\n]/g, '')
					this.stuimg.push(this.imgurl);
					this.newimg = Array.from(new Set(this.stuimg))
					console.log(this.newimg);
					this.name = res.name
					this.classname = res.professional
				})
			},
			edit() {
				uni.navigateTo({
					url: '/pages/Personal/Personals',
				})
			},
			look() {
				console.log(this.stuimg);
				uni.previewImage({
					urls: this.newimg,
					longPressActions: {
						itemList: ['保存图片','发送给朋友'],
						success: function(data) {
							console.log('选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data
								.index + 1) + '张图片');
						},
						fail: function(err) {
							console.log(err.errMsg);
						}
					}
				});
			}
		},
		onShow() {
			this.getPersonal()
		}
	}
</script>

<style>
	.content {
		width: 100%;
		margin: 30rpx auto;
		background: green;
	}

	.top {
		width: 650rpx;
		height: 300rpx;
		margin: 0 auto;
		display: flex;
		align-items: center;
	}

	.left {
		width: 150rpx;
		height: 150rpx;
		border-radius: 50%;
		/* background: white; */
	}

	.left image {
		width: 150rpx;
		height: 150rpx;
		border-radius: 50%;
	}

	.right {
		display: flex;
		flex-direction: column;
		color: white;
		margin-left: 30rpx;
	}

	.right text {
		margin-bottom: 10rpx;
	}

	.edit {
		width: 150rpx;
		height: 50rpx;
		background: white;
		border-radius: 10rpx;
		margin-left: 50rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>

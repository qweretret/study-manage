<template>
	<view>
		<view class="content">

			<view class="frame">
				<view class="week">
					<text>请输入标题:</text>
					<uni-easyinput class="uni-mt-5" trim="all" v-model="title" placeholder="请输入标题" @input="inputss">
					</uni-easyinput>
				</view>
				<view class="week">
					<text>请输入周数:</text>
					<uni-easyinput @blur="blurs" type="number" class="uni-mt-5" trim="all" v-model="week"
						placeholder="请输入数字" @input="input"></uni-easyinput>
					<text v-show="warn" style="color: red;">{{warns}}</text>
				</view>
			</view>
			<view class="frame">
				<text>自我评价</text>
				<uni-rate isFill="false" color="white" :value="value" @change="onChange()" />
			</view>

			<view class="frame">
				<text>游戏时长</text>
				<uni-rate isFill="false" color="white" :value="values" @change="onChanges()" />
			</view>

			<view class="frame">
				<text>进度评价</text>
				<uni-rate isFill="false" color="white" :value="valuess" @change="onChangess()" />
			</view>

			<view class="texta">
				<uni-easyinput v-model="content" type="textarea" placeholder="请输入内容" @input="inputs"></uni-easyinput>
			</view>

			<view class="ent" @click="tijiao()">
				<text>提交</text>
			</view>

		</view>
	</view>
</template>

<script>
	import {
		evapost
	} from "@/api/Evaluation.js"
	export default {
		data() {
			return {
				value: 0,
				values: 0,
				valuess: 0,
				week: "",
				title: "",
				content: "",

				warn: false,
				warns: "请输入正确的周数",
				nums: false,
				list: []
			}
		},
		methods: {
			blurs() {
				const zz = /^[1-9]\d{0,1}$/;
				let num = zz.test(this.week);
				this.nums = num;
				if (num == false) {
					this.warn = true;
				} else if (num == true) {
					this.warn = false;
				}
				console.log(num);
			},
			onChange(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.value = JSON.stringify(e.value);
				this.list[0] = this.value;
				console.log(this.value);
			},
			onChanges(e) {
				console.log('rates发生改变:' + JSON.stringify(e))
				this.values = JSON.stringify(e.value);
				this.list[1] = this.values;
				console.log(this.values);
			},
			onChangess(e) {
				console.log('ratess发生改变:' + JSON.stringify(e))
				this.valuess = JSON.stringify(e.value);
				this.list[2] = this.valuess;
				console.log(this.valuess);
			},
			input(e) {
				console.log('输入内容：', e);
				this.week = e;
				this.list[3] = this.week;
			},
			inputs(e) {
				console.log('输入内容：', e);
				this.content = e;
				this.list[4] = this.content;
			},
			inputss(e) {
				console.log('输入内容：', e);
				this.weeks = e;
				this.list[5] = this.weeks;
			},
			async tijiao() {
				console.log(this.list);
				if (this.list.length == 0) {
					uni.showToast({
						title: '请评价完后提交',
						icon: 'error',
					})
				}
				for (var i = 0; i < this.list.length; i++) {
					if (this.list[i] == undefined) {
						uni.showToast({
							title: '请评价完后提交',
							icon: 'error',
						})
					}
				}
				const als = {
					title: this.title,
					week: this.week,
					content: this.content,
					quest1: this.value,
					quest2: this.values,
					quest3: this.valuess,
				}
				const res = await evapost(als)
				console.log(res.data);
				if (this.nums == false) {
					uni.showToast({
						title: "请输入数字",
						icon: "error",
					})
					this.warn = true
				} else {
					if (res.data.code == 1000) {
						uni.showToast({
							title: "提交成功",
							icon: "success",
							success: (res) => {
								const time = setTimeout((res) => {
									uni.navigateBack({
										delta: 1,
										success: (res) => {
											clearTimeout(time);
										}
									})
								}, 1000)
							}
						})

					} else {
						uni.showToast({
							title: "本周已提交过,请下次提交",
						})
					}
				}

			}
		}
	}
</script>

<style>
	page {
		background: #b8b8c1;
	}

	.content {
		width: 700rpx;
		height: 1300rpx;
		margin: 40rpx auto;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-between;
		background: #E5B1D0;
		border-radius: 10rpx;
		padding-bottom: 30rpx;
		color: white;
	}

	.frame {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-between;
		margin-top: 40rpx;
	}

	.ent {
		width: 270rpx;
		height: 80rpx;
		background: skyblue;
		border-radius: 15rpx;
		/* margin-top: 30rpx; */
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.week {
		display: flex;
		align-items: center;
		margin-top: 30rpx;
	}

	.texta {
		width: 500rpx;
		margin-top: 20rpx;
	}
</style>

<template>
	<view>
		<view class="overall">
			<view class="alla">
				<view class="frame">
					<view class="fart">
						<text>讲授语速</text>
						<uni-rate isFill="false" size="20" :value="value" @change="onChange()" />
					</view>
					<view class="fart">
						<text>课堂案例</text>
						<uni-rate isFill="false" size="20" :value="values" @change="onChanges()" />
					</view>
					<view class="fart">
						<text>答疑解忧</text>
						<uni-rate isFill="false" size="20" :value="valuess" @change="onChangess()" />
					</view>
				</view>
				<view class="frame">
					<view class="fart">
						<text>听课情况</text>
						<uni-rate isFill="false" size="20" :value="valuea" @change="onChangea()" />
					</view>
					<view class="fart">
						<text>练习完成</text>
						<uni-rate isFill="false" size="20" :value="valueaa" @change="onChangeaa()" />
					</view>
					<view class="fart">
						<text>讲师技能</text>
						<uni-rate isFill="false" size="20" :value="valueaaa" @change="onChangeaaa()" />
					</view>
				</view>
				<view class="article">
					<text>内容</text>
					<uni-easyinput v-model="content" autoHeight type="textarea" placeholder="请输入内容" @input="inputs">
					</uni-easyinput>
				</view>
				<view class="butt">
					<view class="btn" @click="submit()">
						<text>提交</text>
					</view>
					<view class="btn" @click="close()">
						<text>取消</text>
					</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	import {
		submit
	} from "@/api/Evaluation.js"
	export default {
		data() {
			return {
				value: 0,
				values: 0,
				valuess: 0,
				valuea: 0,
				valueaa: 0,
				valueaaa: 0,
				content: "",
				list: [],
				tai: false
			}
		},
		methods: {
			onChange(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.value = JSON.stringify(e.value);
				this.list[0] = this.value,
					console.log(this.value);
			},
			onChanges(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.values = JSON.stringify(e.value);
				this.list[1] = this.values,
					console.log(this.values);
			},
			onChangess(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.valuess = JSON.stringify(e.value);
				this.list[2] = this.valuess,
					console.log(this.valuess);
			},
			onChangea(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.valuea = JSON.stringify(e.value);
				this.list[3] = this.valuea,
					console.log(this.valuea);
			},
			onChangeaa(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.valueaa = JSON.stringify(e.value);
				this.list[4] = this.valueaa,
					console.log(this.valueaa);
			},
			onChangeaaa(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.valueaaa = JSON.stringify(e.value);
				this.list[5] = this.valueaaa,
					console.log(this.valueaaa);
			},
			inputs(e) {
				console.log('输入内容：', e);
				this.content = e;
				this.list[6] = this.content
			},
			close() {
				uni.navigateBack({
					delta: 1
				})
			},
			async submit() {
				console.log(this.list);
				const slg = {
					a1: this.value,
					a2: this.values,
					a3: this.valuess,
					a4: this.valuea,
					a5: this.valueaa,
					a6: this.valueaaa,
					content: this.content,
				}
				const res = await submit(slg);
				console.log(res);
				if (res.data.code == 1000) {
					uni.showToast({
						title: '提交成功',
						icon: 'success',
						complete: (res) => {
							const time = setTimeout((res) => {
								uni.navigateBack({
									delta: 1,
									success: (res) => {
										clearTimeout(time);
									}
								})
							}, 500)
						}
					})
				} else if (res.data.code == 1001) {
					uni.showToast({
						title: "请明日再提交",
						icon: "none"
					})
				} else if (res.data.code = 1002) {
					uni.showToast({
						title: "请填写完后提交",
						icon: "none"
					})
				} else {
					uni.showToast({
						title: "提交失败",
						icon: "error"
					})
				}
			}


		}

	}
</script>

<style>
	.alla {
		width: 700rpx;
		/* height: 900rpx; */
		margin: 30rpx auto;
		background: #77c0e1;
		border-radius: 10rpx;
		height: 1200rpx;
	}

	.frame {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		padding-top: 40rpx;
		color: white;
	}

	.fart {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.butt {
		display: flex;
	}

	.article {
		/* margin-top: 30rpx; */
		display: flex;
		flex-direction: column;
		color: white;
	}

	.article text:nth-child(1) {
		text-align: center;
	}

	.article text:nth-child(2) {
		width: 650rpx;
		margin: 0 auto;
	}

	.article {
		width: 620rpx;
		margin: 10rpx auto;
		/* height: 50rpx; */
	}

	.article text {
		padding-bottom: 30rpx;
	}

	.btn {
		width: 200rpx;
		height: 100rpx;
		background: #82C5D2;
		margin: 20rpx auto;
		border-radius: 20rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		color: white;
	}
</style>

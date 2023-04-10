<template>
	<view>
		<view class="content">
			<view class="frame">
				<view class="mslast">
					<text>面试时长</text>
					<uni-easyinput v-model="city" placeholder="请输入时间"></uni-easyinput>
				</view>
				<view class="msscore">
					<text>面试过程评分</text>
					<uni-rate v-model="values" @change="onChanges()" />
				</view>
				<view class="msresult">
					<text>面试结果</text>
					<uni-combox :candidates="jieguo" v-model="citys"></uni-combox>
				</view>
				<view class="mszj">
					<text>面试总结</text>
					<view class="ipts">
						<uni-easyinput type="textarea" v-model="textareas" placeholder="请输入内容"></uni-easyinput>
					</view>
				</view>
				<view class="exam">
					<text>面试难题</text>
				</view>
				<view class="img">
					<view class="imgs" v-for="(image,index) in imageList" :key="index">
						<image class="uni-uploader__img" :src="image" :data-src="image" @click="open()">
						</image>
						<uni-icons type="closeempty" color="black" class="close" @click="close(index)"></uni-icons>
					</view>
					<view class="photo" @click="photo()">
						<uni-icons type="plusempty" size="42" color="white"></uni-icons>
					</view>
				</view>
				<view class="btn" @click="Replenish()">
					<text>提交</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		Replenish,
		filepost
	} from '@/api/register.js'



	import {
		pathToBase64,
		base64ToPath
	} from '@/js_sdk/mmmm-image-tools/index.js'
	export default {
		data() {
			return {
				value: '',
				city: '',
				values: 0,
				jieguo: ['office', 'wait','pass'],
				citys: '',
				imageList: [],
				textareas: "",
				img: [],
				miyue: [],
				newmi: [],
				imageValue: [],
				reading: 0,
				list: {}
			}
		},
		onLoad($options) {
			let msg = decodeURIComponent($options.msg);
			this.list = JSON.parse(msg);
			console.log(this.list);
		},
		methods: {
			onChange(e) {
				console.log('rate发生改变:' + JSON.stringify(e))
				this.reading = JSON.stringify(e.value)
			},
			onChanges(e) {
				console.log('rates发生改变:' + JSON.stringify(e))
				this.values = JSON.stringify(e.value)
			},
			open(){
				uni.previewImage({
					urls: this.img,
					longPressActions: {
						itemList: ['发送给朋友', '保存图片', '收藏'],
						success: function(data) {
							console.log('选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data
								.index + 1) + '张图片');
						},
						fail: function(err) {
							console.log(err.errMsg);
						}
					}
				});
			},
			photo() {
				const that = this
				uni.chooseImage({
					count: 6, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album', 'camera '], //从相册选择
					success: ((res) => {
						console.log(res);
						if (res.tempFiles[0].size <= 524288) {
							this.img.push(res.tempFiles[0].path)
							console.log(this.img);
						} else {
							uni.showToast({
								title: '图片不能超过512k',
								icon: 'none'
							})
						}
						this.img.map((res) => {
							// console.log(res);
							pathToBase64(res)
								.then(base64 => {
									//将转化后的base64赋值给miyue
									// console.log(base64);
									this.miyue.push(base64)
									this.newmi = Array.from(new Set(this.miyue))
									console.log(this.newmi)
									base64 = ""
								})
								.catch(error => {
									console.error(error)
								})
						});


						const len = that.imageList.length
						if (len >= 6) {
							uni.showToast({
								title: '图片最多上传6张',
								icon:'none'
							})
						} else {
							for (let i = 0; i < 6 - len; i++) {
								if (res.tempFilePaths[i]) that.imageList.push(res.tempFilePaths[i])
							}
						}
						// console.log(JSON.stringify(res.tempFilePaths));
						// uni.saveImageToPhotosAlbum({
						// 	filePath: res.tempFilePaths[0],
						// 	success: function() {
						// 		console.log('save success');
						// 	}
						// });
					})
				});
			},
			close(index) {
				this.imageList.splice(index, 1)
				this.miyue.splice(index, 1)
			},
			async Replenish() {
				// 把图片地址转为Base64
				// if(this.list.mstm < this.value){

				// }
				// console.log(this.miyue);
				//发送请求,提交base64
				console.log(this.city);
				let database = {
					model: this.newmi,
					uri: "/statics/mianshi"
				}
				const result = await filepost(database)
				console.log(result);
				if (result.data.code == 1000) {
					this.imageList = []
					//发送请求,提交表单数据
					const data = {
						mslast: this.city,
						msscore: this.values,
						msresult: this.citys,
						mszj: this.textareas,
						exam: result.data.data,
						mszbqk: this.list.mszbqk,
						id: this.list.id
					}
					const res = await Replenish(data)
					console.log(res);
					if (res.data.code == 1000) {
						uni.showToast({
							title: '提交成功',
							icon: 'none',
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
					} else if (res.data.code = 1001) {
						uni.showToast({
							title: "请填写完后提交",
							icon: "none"
						})
					}
				} else {
					uni.showToast({
						title: '提交失败',
						icon: 'error'
					})
				}
			}
		}
	}
</script>

<style>
	.content {
		width: 700rpx;
		margin: 0 auto;
		background: #4D7AC6;
		border-radius: 10rpx;
	}

	.frame {
		width: 650rpx;
		margin: 30rpx auto;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;
		color: white;
		text-align: center;
	}

	.mslast,
	.msresult,
	.msscore,
	.mstm {
		margin-top: 30rpx;
	}

	.mslast uni-combox,
	.msresult uni-combox {
		color: black;
	}

	.mszj {
		margin-top: 20rpx;
		display: flex;
		flex-direction: column;
	}

	.ipts {
		width: 500rpx;
		/* height: 50rpx; */
		margin: 0 auto;
	}

	.imgs {
		position: relative;
	}

	.imgs image {
		width: 150rpx;
		height: 100rpx;
	}

	.close {
		position: absolute;
		right: 0;
		top: 0;
		z-index: 20;
	}

	.img {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		
	}

	.photo {
		width: 150rpx;
		/* height: 80rpx; */
		border: 1px dotted white;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.exam {
		margin-top: 30rpx;
	}

	.btn {
		margin-top: 30rpx;
		width: 200rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background: gray;
		border-radius: 10rpx;
	}
</style>

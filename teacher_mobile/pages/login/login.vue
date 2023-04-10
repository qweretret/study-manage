<template>
	<view v-if="show">
		<view class="swiper-container">
			<view class="swiper-item"></view>
			<view class="title">
				<p>靖凯开源</p>
				<text>学生管理Ⅲ老师端</text>
			</view>
		</view>
		<button id="btn" @click="login">进入使用</button>

		<view class="login-container" v-if="isShow">
			<view class="login-mouble">
				<view class="LoginTitle">
					登录
				</view>

				<view class="LoginInput">
					<form action="#">
						<input type="text" name="UserName" placeholder="请输入用户名" v-model="loginParams.name">
						<input type="password" name="Password" placeholder="请输入密码" v-model="loginParams.pwd">
					</form>
				</view>
				<view class="LoginBtn">
					<navigator @click="isShow = false" url="" class="cancel">取消</navigator>
					<navigator url="" class="Login" @click="goto">登录</navigator>
				</view>
			</view>
		</view>
		<!-- 	<u-popup :show="showLogin" mode="center" :round="10">
			<button type="primary" open-type="getUserInfo" withCredentials="true" lang="zh_CN"
				@click="loginOk">授权登录</button>
		</u-popup> -->
	</view>
</template>

<script>
	import {
		APP_ID
	} from '@/utils/common.js'
	import {
		getSessionInfo,
		wxLogin,
		getWxUserInfo
	} from "@/api/login.js"
	import {
		getUserHead
	} from '@/api/login.js'
	export default {
		data() {
			return {
				isShow: false, //控制登录容器的显示
				loginParams: {
					name: '',
					pwd: '',
					openid: '',
					unionid: ''
				},
				show: false,
				//微信授权码
				code: '',
				//正则验证 不能有中文
				reg: /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/g
			}
		},
		methods: {
			loginOk() { //微信登录，进行授权
				return new Promise((resolve) => {
					uni.login({
						provider: 'weixin',
						success: function(loginRes) {
							// that.code = loginRes.code
							resolve(loginRes)
						}
					});
				})
			},
			async API_getSessionInfo(appid, code) {
				////获取微信openid接口
				let result = {
					appid: appid,
					code: code
				}

				return await getSessionInfo(result)
			},
			// 登录
			async login() {
				uni.showLoading({
					title: '加载中'
				})
				//判断是否有授权获取用户信息
				const settingRes = await this.getSetting()
				console.log('-----settingRes--------', settingRes);
				const auth = settingRes.authSetting["scope.userInfo"]
				if (auth) {
					//拿取要解密的数据和秘钥等
					let {
						signature,
						rawData,
						encryptedData,
						iv
					} = await this.getUserInfo()

					// //获取授权码
					var wxloginRes = await this.loginOk()

					// //将授权码和APP_ID发送到后端，后端对微信进行访问，然后拿取openid和unionid
					console.log(" wxloginRes.code== ", wxloginRes.code)
					var openIdRes = await this.API_getSessionInfo(APP_ID, wxloginRes.code)
					console.log(openIdRes.data)

					uni.setStorageSync("openid", openIdRes.data.data.openid)
					uni.setStorageSync("unionid", openIdRes.data.data.unionid)

					console.log("  == openid,unionid== ")
					console.log(openIdRes.data.data.openid)
					console.log(openIdRes.data.data.unionid)

					//传入后端进行解密，拿取用户信息
					let wxParams = {
						appid: APP_ID,
						sessionKey: openIdRes.data.data.sessionKey,
						signature,
						rawData,
						encryptedData,
						iv
					}
					// 
					// let getWxUserInfoRes = await getWxUserInfo(wxParams) //调用接口-获取用户信息
					// console.log(getWxUserInfoRes, '---getWxUserInfoRes---')

					//微信登录
					let wxLoginRes = await wxLogin({
						openid: openIdRes.data.data.openid,
						unionid: openIdRes.data.data.unionid
					})

					uni.hideLoading()
					if (wxLoginRes.data.code == 1000) {
						uni.setStorageSync("jwttoken", wxLoginRes.data.data)
						uni.navigateTo({
							url: '/pages/interface/interface'
						})

					} else {
						// 账户为绑定 弹出注册框
						this.isShow = true
					}


				}
			},
			getUserInfo() {
				return new Promise((resolve) => {
					uni.getUserInfo({
						provider: 'weixin',
						success(res) {
							resolve(res)
						}
					})
				})
			},
			//判断用户是否又授权
			getSetting() {
				return new Promise((resolve) => {
					uni.getSetting({
						success(res) {
							resolve(res)
						}
					})
				})
			},
			async Init() {
				uni.showLoading({
						title: '加载中'
					}),

					setTimeout(async () => {
						if (uni.getStorageSync('jwttoken') == '') {
							uni.hideLoading()
							this.show = true
						} else {
							uni.hideLoading()
							const result = await getUserHead()
							if (result.data.code == 1000) {
								uni.navigateTo({
									url: './/pages/interface/interface',
									animationType: 'fade-in'
								})
							} else {
								uni.showToast({
									icon: 'error',
									title: '登录失效',
									duration: 1000
								})
								this.show = true
							}
						}
					}, 1000)
			},
			async goto() {
				if (this.loginParams.name.trim() == '' || this.loginParams.pwd.trim() == '') {
					uni.showToast({
						icon: 'error',
						title: '账号或密码不能为空'
					})
				} else if (this.reg.test(this.loginParams.pwd)) {
					uni.showToast({
						icon: 'error',
						title: '密码不能有中文'
					})
				}

				uni.showLoading({
					title: '加载中'
				})
				let openid = uni.getStorageSync('openid');
				let unionid = uni.getStorageSync('unionid');
				this.loginParams.openid = openid
				this.loginParams.unionid = unionid
				const result = await wxLogin(this.loginParams);
				if (result.data.code == 1000) {
					uni.showToast({
						icon: 'success',
						title: '登录成功',
						duration: 1000
					})
					uni.setStorageSync("jwttoken", result.data.data)
					uni.hideLoading()
					uni.navigateTo({
						url: '/pages/interface/interface'
					})
				} else if (result.data.code == 1001) {
					uni.showToast({
						icon: 'error',
						title: '登录错误',
						duration: 2000
					})
					uni.hideLoading()
				}
			}
		},
		onLoad() {
			this.Init()
		},
	}
</script>

<style scoped>
	page {
		margin: 0;
		padding: 0;
		box-sizing: border-box;
	}

	.swiper-container {
		width: 674rpx;
		height: 874rpx;
		background-color: #D2D9DB;
		margin: 0 auto;
		border-radius: 10px;
		text-align: center;
		margin-top: 40rpx;
	}

	.swiper-item {
		width: 100%;
		height: 75%;
		background-color: #6572CA;
		border-radius: 10px;
	}

	.title {
		padding-top: 40rpx;
	}

	.title p {
		font-size: 18px;
		color: #6572CA;
	}

	.title text {
		display: block;
		color: #5D4865;
		margin-top: 20rpx;
	}

	#btn {
		display: flex;
		justify-content: center;
		align-items: center;
		position: absolute;
		bottom: 160rpx;
		left: 50%;
		bottom: 60rpx;
		transform: translateX(-50%);
		width: 200rpx;
		height: 72rpx;
		background-color: #1A8BD4;
		color: white;
		font-size: 14px;
	}

	.login-container {
		display: flex;
		justify-content: center;
		align-items: center;
		position: absolute;
		top: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, .5);
		z-index: 1;
	}

	.login-mouble {
		position: relative;
		width: 520rpx;
		height: 474rpx;
		background-color: white;
		border-radius: 10px;
		padding-top: 40rpx;
	}

	.LoginTitle {
		font-size: 40rpx;
		font-weight: bold;
		text-align: center;
		margin-bottom: 30rpx;
	}

	.LoginInput {
		width: 480rpx;
		height: 188rpx;
		margin: 0 auto;
		/* background-color: red; */
	}

	.LoginInput input {
		/* width: 100%; */
		height: 50%;
		border: 1rpx solid #ccc;
		padding-left: 20rpx;
	}

	.LoginInput form input:first-child {
		border-bottom: none;
	}

	.LoginBtn {
		display: flex;
		position: absolute;
		bottom: 0;
		width: 520rpx;
		height: 112rpx;
		/* border-top: 1px solid #ccc; */
		/* background-color: red; */
	}

	.cancel,
	.Login {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 50%;
		background-color: #fff;
		border-top: 1px solid #ccc;
		color: #CED1D4;
	}

	.cancel {
		border-right: 1px solid #ccc;
		border-bottom-left-radius: 10px;
	}

	.Login {
		border-bottom-right-radius: 10px;
	}

	.cancel:active,
	.Login:active {
		color: #666EE8;
	}
</style>

<template>
	<view class="index">
		<!-- 用户头像 -->
		<view class="logo">
			<!-- <img src="" alt=""> -->
		</view>

		<!-- 登陆入口按钮 -->
		<view class="login">
			<button class="login" @click="Authorize()">
				点击进入
			</button>
		</view>


		<!-- 弹出框 -->
		<view class="popup" v-if="show">
			<view class="pop-top">
				<view class="pop-left" @click="usershow=true,pwdshow=false,active=0"
					:class="active == 0 ? 'popactive' : '' " v-model="userName">
					<p>账号密码登陆</p>
				</view>
			</view>

			<view class="pop-content" v-show="usershow">
				<!-- 账号 -->
				<view class="id">
					<uni-easyinput v-model="userName" placeholder="请输入姓名"></uni-easyinput>
				</view>

				<!-- 密码 -->
				<view class="pwd">
					<uni-easyinput type="password" v-model="password" placeholder="请输入密码"></uni-easyinput>
				</view>

				<!-- 确认登陆 -->
				<view class="pop-login">
					<button @click="logint()">
						登&nbsp;陆
					</button>
				</view>

				<!-- 取消登陆 -->
				<view class="pop-bot">
					<p @click="show=false">取消</p>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		login,
		wxLogin,
		getWxUserInfo
	} from "@/api/login.js"
	import {
		APP_ID
	} from "@/utils/common.js"
	export default {
		data() {
			return {
				title: 'Hello',
				show: false,
				usershow: true,
				pwdshow: false,
				active: 0,
				userName: "",
				password: "",
				data: {},
				code: '',
				openid: '',
				session_key: '',
				getuserinfo: {}
			}
		},
		onLoad() {

		},
		methods: {
			//推送消息提示方法
			Authorize() {
				//消息推送APi
				uni.requestSubscribeMessage({
					tmplIds: ['EPrqxRPus0WZw5Vtmdr-bkjaCeREH-xZaOK4LzJQX-E'],
					success: ((res) => {
						console.log(res[`EPrqxRPus0WZw5Vtmdr-bkjaCeREH-xZaOK4LzJQX-E`]);
						if (res[`EPrqxRPus0WZw5Vtmdr-bkjaCeREH-xZaOK4LzJQX-E`] == "reject") {
							uni.showToast({
								title: '请同意订阅消息',
								icon: "none"
							})
						} else if (res[`EPrqxRPus0WZw5Vtmdr-bkjaCeREH-xZaOK4LzJQX-E`] == "accept") {
							this.login();
						}
					})
				})
				// this.login();
			},

			//登陆方法
			async login() {
				uni.showLoading({
					title: '登陆中'
				})
				const settingRes = await this.getSetting()
				console.log('-----settingRes--------', settingRes);
				const auth = settingRes.authSetting["scope.userInfo"]
				console.log(auth);
				if (auth) {
					// 拿取要解密的数据和秘钥等
					let {
						signature,
						rawData,
						encryptedData,
						iv
					} = await this.wxLogin()
				}
				// //获取授权码
				var wxloginRes = await this.wxCode()

				console.log(wxloginRes);
				//将授权码和APP_ID发送到后端，后端对微信进行访问，然后拿取openid和unionid
				var openIdRes = await this.API_getSessionInfo(APP_ID, wxloginRes.code);
				console.log(openIdRes.data);

				if (openIdRes.data.code == 1001) {
					uni.showToast({
						title: '登录失败',
						icon: "none"
					})
				} else {
					//把openid,unionid,sessionKey缓存到本地
					uni.setStorageSync("openid", openIdRes.data.data.openid)
					uni.setStorageSync("unionid", openIdRes.data.data.unionid)
					uni.setStorageSync("sessionKey", openIdRes.data.data.sessionKey)
					console.log("  == openid,unionid,sessionkey== ")
					console.log(openIdRes.data.data.openid)
					console.log(openIdRes.data.data.unionid)
					console.log(openIdRes.data.data.sessionKey)



					//发送openid，unionid到后端
					let wxLoginRes = await wxLogin({
						openid: openIdRes.data.data.openid,
						unionid: openIdRes.data.data.unionid,
					})

					//返回状态码判断是否第一次登陆
					if (wxLoginRes.data.code == 1001) {
						//不是第一次登陆显示登陆框
						uni.hideLoading();
						this.show = true;

					} else if (wxLoginRes.data.code == 1000) {
						//返回jtwtoken缓存到本地
						uni.setStorageSync("jwttoken", wxLoginRes.data.data);
						//跳转到首页
						uni.hideLoading();
						uni.reLaunch({
							url: '/pages/index/index'
						});
					}
				}
			},


			//登陆框判断
			async logint() {

				//openid，unionid缓存到本地
				const openid = uni.getStorageSync("openid")
				const unionid = uni.getStorageSync("unionid")


				//发送到后端验证是否存在数据库
				let wxLoginRest = await wxLogin({
					openid: openid,
					unionid: unionid,
					name: this.userName,
					pwd: this.password
				})
				const token = uni.setStorageSync("jwttoken", wxLoginRest.data.data)
				// console.log(token);

				//判断是否能进入
				console.log("wxLoginRest" + wxLoginRest.data.code);
				if (wxLoginRest.data.code == 1000) {
					uni.reLaunch({
						url: '/pages/index/index',
					})
				} else {
					uni.showToast({
						title: '登陆失败请重试',
						duration: 2000,
						icon: "error"
					});
				}
			},

			//获取用户信息
			wxLogin() {
				return new Promise((resolve) => {
					uni.getUserInfo({
						provider: 'weixin',
						success(res) {
							//获取wx用户敏感信息
							//拿取要解密的数据和秘钥等
							resolve(res)
						}
					})
				})
			},

			//获取授权码
			wxCode() { //微信登录，进行授权
				return new Promise((resolve) => {
					uni.login({
						provider: 'weixin',
						success: function(loginRes) {
							// that.code = loginRes.code
							// console.log(logi);
							resolve(loginRes)

						}
					});
				})
			},

			//判断是否获取授权API
			getSetting() {
				return new Promise((resolve) => {
					uni.getSetting({
						success(res) {
							resolve(res)
						}
					})
				})
			},


			//获取微信openid接口
			async API_getSessionInfo(appid, code) {
				let result = {
					appid: appid,
					code: code
				}
				return await login(result)
			},

		}
	}
</script>

<style>
	.logo {
		width: 400rpx;
		height: 400rpx;
		background: #ccc;
		margin: 100px auto;
		border-radius: 50%;
		position: relative;
	}

	.login {
		width: 350rpx;
		height: 100rpx;
		margin: 100px auto;
		background: #28d094;
		color: white;
		font-size: 16px;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 5px;
	}

	.popup {
		width: 610rpx;
		height: 720rpx;
		background: #f2f2f2;
		position: absolute;
		top: 18%;
		left: 10%;
		border: 1px solid black;
	}

	.pop-top {
		display: flex;
		margin-top: 100rpx;
		color: black;
		text-align: center;
		font-size: 14px;
	}

	.pop-left {
		width: 100%;
		height: 50rpx;
		/* height: 40rpx; */
	}

	.popactive {
		color: #1890ff;
		font-size: 14px;
		border-bottom: 2px solid #1890ff;
	}

	.pop-right {
		width: 50%;
		height: 50rpx;
		font-size: 12px;
	}

	.pop-content {
		margin-top: 100rpx;
	}

	.pop-content .id {
		width: 580rpx;
		margin: 0 auto;
	}

	.pop-content .pwd {
		width: 580rpx;
		margin: 10px auto;
	}

	.pop-login button {
		background: #1890ff;
		color: white;
		width: 580rpx;
		height: 80rpx;
		margin-top: 20px;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.pop-bot {
		margin-top: 10px;
		text-align: center;
		font-size: 12px;
	}

	.pop-pwd {
		width: 580rpx;
		margin: 100rpx auto;
	}

	.pop-pwd .code {
		display: flex;
		margin-top: 20rpx;
	}

	.code button {
		height: 37px;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 12px;
		margin-left: 20rpx;
		background: white;
		color: black;
	}

	.options {
		display: flex;
		justify-content: space-between;
		margin-top: 20rpx;
	}

	.options text {
		font-size: 12px;
	}

	.options span {
		font-size: 12px;
		text-decoration: none;
		color: #1890ff;
		margin-right: 20px;
		margin-top: 5rpx;
	}
</style>

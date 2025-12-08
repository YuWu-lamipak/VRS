<template>
    <view>
        <headerbar title="个人信息" background="#2195F3" title_color="#FFFFFF" :noback="false"/>
        <view class="page"> 
            <u-form :model="form" :borderBottom="true" :rules="formRules" errorType="message" ref="uForm">
				<u-form-item labelWidth="0" prop="avatar" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">头像</view>
                            <view class="leftauto flex" @click="portraitclick()">
                                <img class="dafaultuserIcon" :src="form.avatar ? imgUrl + form.avatar : '/static/dafaultuser.png'"/>
                                <img class="righticon margin_left12" src="/static/selectable.png"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="userName" borderBottom>
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">用户名</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.userName" placeholder="请输入用户名" :disabled="true"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="nickName" borderBottom v-if="checkuserType('carrier')">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">昵称</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.nickName" placeholder="请输入昵称"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="phoneNumber" borderBottom v-if="checkuserType('carrier')">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">电话</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.phoneNumber" placeholder="请输入电话" type="number"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="idcard" borderBottom v-if="checkuserType('carrier')">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">身份证号</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.idcard" placeholder="请输入身份证号"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="place" borderBottom v-if="checkuserType('carrier')">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">地址</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.place" placeholder="请输入地址" mode="textarea" @inputchange="$refs.uForm.validateField('place')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="email" borderBottom v-if="checkuserType('carrier')">
					<view class="formitemframe">
                        <view class="formitem_normal flex">
                            <view class="formitem_leftkey">电子邮箱</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.email" placeholder="请输入电子邮箱"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
                <u-form-item labelWidth="0" prop="personalProfile" borderBottom v-if="checkuserType('carrier')">
					<view class="formitemframe">
                        <view class="formitem_normal flex_only">
                            <view class="formitem_leftkey">简介</view>
                            <view class="leftauto maxwidth400">
                                <formelement v-model="form.personalProfile" placeholder="请输入简介" mode="textarea" @inputchange="$refs.uForm.validateField('personalProfile')"/>
                            </view>
                        </view>
                    </view>
				</u-form-item>
			</u-form>
            <view class="butframe flex_center">
                <view class="but flex_center" @click="submit">
                    <view class="buttext">保存</view>
                </view>
            </view>
        </view>
        <u-toast ref="uToast"></u-toast>
    </view>
</template>

<script>
import {
    chooseImg
} from '@/utils/upload/upload.js'
import { userinfo, userupdate } from '@/api/user.js'
export default {
    data() {
        return {
            imgUrl: "",
            // 用户信息
            userinfo: {},
            //表单
            form: {
                userId: null,
                avatar: null,
                userName: null,
                nickName: null,
                phoneNumber: null,
                idcard: null,
                place: null,
                email: null,
                personalProfile: null,
            },
            formRules: {
                avatar: {
                    required: false,
                    message: "请选择图像",
                    trigger: ["blur", "change"],
                },
                userName: {
                    required: true,
                    message: "请输入用户名",
                    trigger: ["blur", "change"],
                },
                nickName: {
                    required: true,
                    message: "请输入昵称",
                    trigger: ["blur", "change"],
                },
                phoneNumber: [
                    {
                        type: "string",
                        required: "true",
                        message: "请输入手机号",
                        trigger: ["blur", "change"],
                    },
                    {
                        // 自定义验证函数，见上说明
                        validator: (rule, value, callback) => {
                            // 上面有说，返回true表示校验通过，返回false表示不通过
                            // uni.$u.test.mobile()就是返回true或者false的
                            // console.log(rule)
                            return uni.$u.test.mobile(value);
                        },
                        message: "手机号码不正确",
                        // 触发器可以同时用blur和change
                        trigger: ["blur"],
                    },
                ],
                idcard: [
                    {
                        type: "string",
                        required: "true",
                        message: "请输入身份证号",
                        trigger: ["blur", "change"],
                    },
                    {
                        // 自定义验证函数，见上说明
                        validator: (rule, value, callback) => {
                            // 上面有说，返回true表示校验通过，返回false表示不通过
                            // uni.$u.test.mobile()就是返回true或者false的
                            // console.log(rule)
                            return uni.$u.test.idCard(value);
                        },
                        message: "身份证不正确",
                        // 触发器可以同时用blur和change
                        trigger: ["blur"],
                    },
                ],
                place: {
                    required: true,
                    message: "请输入地址",
                    trigger: ["blur", "change"],
                },
                email: [
                    {
                        type: "string",
                        required: "true",
                        message: "请输入电子邮箱",
                        trigger: ["blur", "change"],
                    },
                    {
                        // 自定义验证函数，见上说明
                        validator: (rule, value, callback) => {
                            // 上面有说，返回true表示校验通过，返回false表示不通过
                            // uni.$u.test.email()就是返回true或者false的
                            // console.log(rule)
                            return uni.$u.test.email(value);
                        },
                        message: "电子邮箱格式不正确",
                        // 触发器可以同时用blur和change
                        trigger: ["blur"],
                    },
                ],
                personalProfile: {
                    required: true,
                    message: "请输入简介",
                    trigger: ["blur", "change"],
                },
            },
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
        this.getuserInfo()
		if (!this.checkuserType('carrier')) {
			this.formRules = {
			    avatar: {
			        required: false,
			        message: "请选择图像",
			        trigger: ["blur", "change"],
			    },
			    userName: {
			        required: true,
			        message: "请输入用户名",
			        trigger: ["blur", "change"],
			    }
			}
		}
        // #ifndef H5
        console.log('非H5执行代码')
        this.$refs.uForm.setRules(this.formRules);
        // #endif
    },
    methods: {
        // 提交表单
        submit() {
            // console.log(this.form)
            let that = this
            this.$refs.uForm.validate().then((res) => {
                // console.log(res)
                if (res) {
                    that.userupdateapi()
                }
            }).catch((errors) => {

            });
        },
        // 修改用户信息
        userupdateapi() {
            let that = this
            uni.showLoading({
                title: "保存中...",
                mask: true
            });
            userupdate(this.form).then((res) => {
                uni.hideLoading()
                let dataRes = res.data;
                if (dataRes.code == 200) {
                    this.$refs.uToast.show({
                        type: "success",
                        message: "保存成功",
                        complete() {
                            uni.setStorageSync("needToRefreshmine", true)
                            uni.navigateBack({
                                delta: 1,
                            })
                        },
                    });
                } else {
                    this.$refs.uToast.show({
                        type: "error",
                        message: dataRes.msg,
                    });
                }
            });
        },
        // 获取用户信息 默认填写承运商
        getuserInfo() {
            let that = this
            userinfo().then(res => {
                let dataRes = res.data || {}
                let userinfo = dataRes.data || {}
                that.userinfo = userinfo
                this.form = {
                    // userId: userinfo.userId || null,
                    avatar: userinfo.avatar || null,
                    userName: userinfo.userName || null,
                    nickName: userinfo.nickName || null,
                    phoneNumber: userinfo.phoneNumber || null,
                    idcard: userinfo.idcard || null,
                    place: userinfo.place || null,
                    email: userinfo.email || null,
                    personalProfile: userinfo.personalProfile || null,
                }
            })
        },
        // 选择图片
        portraitclick() {
            // return
            let configs = {
                count: 1,
                sourceType: ['album', 'camera ']
            }
            chooseImg((imgs) => {
                console.log(imgs)
                if (imgs && imgs.length > 0) {
                    this.form.avatar = imgs[0]
                    this.$refs.uForm.validateField('avatar')
                }
            },configs)
        },
    },
};
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';
@import '@/styles/formlist.scss';
.page {
    padding: 12rpx 24rpx 40rpx;
}

.dafaultuserIcon {
    width: 124rpx;
    height: 124rpx;
    border-radius: 50%;
}

.righticon {
    width: 24rpx;
    height: 24rpx;
}

.butframe {
    width: 100%;
    margin-top: 48rpx;

    .but {
        width: 638rpx;
        height: 98rpx;
        background: #2196F3;
        box-shadow: 0rpx 8rpx 16rpx rgba(0,0,0,0.16);
        opacity: 1;
        border-radius: 8rpx;
    }

    .buttext {
        font-size: 36rpx;
        font-family: PingFangSC;
        font-weight: 400;
        color: #FFFFFF;
    }
}
</style>

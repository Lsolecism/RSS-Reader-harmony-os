/*
 * Copyright (c) 2024 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { promptAction } from '@kit.ArkUI';
import http from '@ohos.net.http';
import { AuthResponseDTO, Response } from '../common/types/type';
import { PrivacyDialog } from '../view/PrivacyDialog';

@Extend(TextInput)
function inputStyle() {
  .placeholderColor('#99182431')
  .height('45vp')
  .fontSize('18fp')
  .backgroundColor('#F1F3F5')
  .width('328vp')
  .margin({ top: 12 })
}

@Extend(Line)
function lineStyle() {
  .width('328vp')
  .height('1vp')
  .backgroundColor('#33182431')
}

@Extend(Text)
function blueTextStyle() {
  .fontColor('#007DFF')
  .fontSize('14fp')
  .fontWeight(FontWeight.Medium)
}

/**
 * Login page
 */
// 数据持久化需要的持久化数据
PersistentStorage.persistProp('token',"");
PersistentStorage.persistProp('userId',NaN);
PersistentStorage.persistProp('email',"");
PersistentStorage.persistProp('userName',"")
PersistentStorage.persistProp('avatar',"")
PersistentStorage.persistProp('privacyAgreed',false)
@Entry
@Component
struct LoginPage {
  @State account: string = '';
  @State password: string = '';
  @State verificationCode: string ='';
  @State isShowProgress: boolean = false;
  @State isShowVerification:boolean =false;
  @State login1:string ="短信验证码登录";
  @Provide('NavPathStack') pageStack: NavPathStack = new NavPathStack();
  @StorageLink('token') token: string = "";
  @StorageLink('userId') userId: number = NaN;
  @StorageLink('email') email: string = "";
  @StorageLink('userName') userName: string = "";
  @StorageLink('avatar') avatar: string = "";


  PrivacyDialogController:  CustomDialogController = new CustomDialogController({
    builder: PrivacyDialog({}),
    alignment: DialogAlignment.Bottom,
    customStyle: true,
    offset: { dx: 0, dy: -25 }
  });


  @Builder
  imageButton(src: Resource) {
    Button({ type: ButtonType.Circle, stateEffect: true }) {
      Image(src)
    }
    .height('48vp')
    .width('48vp')
    .backgroundColor('#F1F3F5')
  }

  register():void{
    this.pageStack.pushPathByName('RegisterPage',null);
  }
  aboutToAppear(): void {
    console.info("隐私信息刚登陆时",AppStorage.get("privacyAgreed"))
    AppStorage.setOrCreate('playSongId', "-1")
    AppStorage.setOrCreate('songs',[])
    AppStorage.setOrCreate('song',null)
    AppStorage.setOrCreate('playMode',"order")
    if (!AppStorage.get("privacyAgreed")) {
      this.PrivacyDialogController.open()
    }
    if (this.token !=="" && !isNaN(this.userId) && AppStorage.get("privacyAgreed")===true) {
      this.pageStack.clear();
      this.pageStack.pushPathByName('MainPage', null);
      AppStorage.set('avatar',`http://192.168.22.1:5000/api/avatar/`+this.userId);
    }
  }
  loginByPassword():void{
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/auth/login',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json' },
        extraData: JSON.stringify({
          email: this.account,
          passWord:this.password,
          verificationCode:null
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<AuthResponseDTO> = JSON.parse(data.result as string) as Response<AuthResponseDTO>;
          if (rawResponse.code===200) {
            // 数据持久化到整机之中
            this.token = rawResponse.data?.token as string;
            this.userId = rawResponse.data?.user.userId as number;
            this.email = rawResponse.data?.user.email as string;
            this.userName = rawResponse.data?.user.userName as string;
            this.avatar = "http://192.168.22.1:5000/api/avatar/"+AppStorage.get('userId')
            console.info(AppStorage.get('token')+" 这个是更新后的token")
            promptAction.showToast({ message:"登陆成功" });
            this.pageStack.pushPathByName('MainPage', null);
          }
          else{
            promptAction.showToast({ message:"账号或者密码错误" });
            this.password = "";
          }
        }
        else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
          this.account = "";
          this.password = "";
        }
        httpRequest.destroy();
      }
    )
  }
  loginByVerificationCode():void{
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/auth/verify-code',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json' },
        extraData: JSON.stringify({
          email: this.account,
          passWord:null,
          verificationCode:this.verificationCode
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<AuthResponseDTO> = JSON.parse(data.result as string) as Response<AuthResponseDTO>;
          if (rawResponse.code===200) {
            // 数据持久化到整机之中
            this.token = rawResponse.data?.token as string;
            this.userId = rawResponse.data?.user.userId as number;
            this.email = rawResponse.data?.user.email as string;
            this.userName = rawResponse.data?.user.userName as string;
            console.info(AppStorage.get('token') + " 这个是更新后的token")
            promptAction.showToast({ message: "登陆成功" });
            this.pageStack.pushPathByName('MainPage', null);
          }
          else {
            promptAction.showToast({ message: "验证码错误" });
            this.verificationCode = "";
          }
        }else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
          this.verificationCode  = "";
          this.account  = "";
        }
        httpRequest.destroy();
      }
    )
  }
  getVerificationCode():void{
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/auth/send-code',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json' },
        extraData: JSON.stringify({
          email: this.account,
          passWord:null,
          verificationCode:null,
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response = JSON.parse(data.result as string);
          AlertDialog.show({
            title: '提示',
            message:rawResponse.message
          })
        }else {
          AlertDialog.show({
            title: '提示',
            message:'发送验证码失败'
          })
        }
        httpRequest.destroy();
      }
    )
  }
  changeShowVerification():void{
    this.isShowVerification = !this.isShowVerification;
    if (this.isShowVerification){
      this.login1 = "密码登录";
    }else{
      this.login1 = "短信验证码登录";
    }
  }

  build() {

    Navigation(this.pageStack) {
      Column() {
        Image($r('app.media.logo'))
          .width('78vp')
          .height('78vp')
          .margin({ top: '150vp', bottom: '8vp' })
        Text($r('app.string.login_page'))
          .fontSize('24fp')
          .fontWeight(FontWeight.Medium)
          .fontColor('#182431')
        Text($r('app.string.login_more'))
          .fontSize('16fp')
          .fontColor('#99182431')
          .margin({
            bottom: '30vp',
            top: '8vp'
          })

        TextInput({ placeholder: $r('app.string.account') ,text:this.account})
          .type(InputType.Email)
          .inputStyle()
          .onChange((value: string) => {
            this.account = value;
          })
        Line()
          .lineStyle()

        if (!this.isShowVerification){
          TextInput({ placeholder: $r('app.string.password') ,text:this.password})
            .type(InputType.Password)
            .inputStyle()
            .onChange((value: string) => {
              this.password = value;
            })
        }

        if (this.isShowVerification){
          Row(){
            TextInput({ placeholder: $r('app.string.verification_code'),text:this.verificationCode })
              .maxLength(6)
              .inputStyle()
              .width('200vp')
              .margin({ right: '16vp' })
              .onChange((value: string) => {
                this.verificationCode = value;
              })
            Button("获取验证码")
              .type(ButtonType.Capsule)
              .width('120vp')
              .height('40vp')
              .fontSize('16fp')
              .borderRadius('20vp')
              .fontWeight(FontWeight.Medium)
              .backgroundColor($r('app.color.login_button_color'))
              // 获取验证码
              .onClick(()=>{
                this.getVerificationCode()
              })
          }
        }
        Line()
          .lineStyle()
        Row() {
          Text(this.login1)
            .blueTextStyle()
            .onClick(() => {
              this.changeShowVerification()
            })
          Text($r('app.string.forgot_password'))
            .blueTextStyle()
        }
        .justifyContent(FlexAlign.SpaceBetween)
        .width('328vp')
        .margin({ top: '8vp' })

        Button($r('app.string.login'), { type: ButtonType.Capsule })
          .width('328vp')
          .height('40vp')
          .fontSize('16fp')
          .fontWeight(FontWeight.Medium)
          .backgroundColor($r('app.color.login_button_color'))
          .margin({
            top: '48vp',
            bottom: '12vp'
          })
          .onClick(async () => {
            if(!this.isShowVerification){
              this.loginByPassword()
            }
            else{
              this.loginByVerificationCode()
            }
          })
        Text($r('app.string.register_account'))
          .fontColor('#007DFF')
          .fontSize('16fp')
          .fontWeight(FontWeight.Medium)
          .onClick(()=>{
              this.register();
          })

        if (this.isShowProgress) {
          LoadingProgress()
            .color('#182431')
            .width('30vp')
            .height('30vp')
            .margin({ top: '20vp' })
        }

        Blank()
      }
      .height('100%')
      .width('100%')
      .padding({
        left: '12vp',
        right: '12vp',
        bottom: '24vp'
      })
    }
    .backgroundColor('#F1F3F5')
    .width('100%')
    .height('100%')
    .hideTitleBar(true)
    .hideToolBar(true)
  }
}
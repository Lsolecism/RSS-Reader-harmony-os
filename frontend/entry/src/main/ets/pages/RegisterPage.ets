// entry/src/main/ets/pages/RegisterPage.ets
import http from '@ohos.net.http';
import { Response } from '../common/types/type';

@Builder
export function RegisterPageBuilder() {
  RegisterPage()
}

@Component
export  struct RegisterPage {
  @State email: string = "";
  @State password: string = "";
  @State passwordConfirm: string = "";
  @State nickname: string = "";
  @State showPasswordError: boolean = false; // 新增错误状态
  @Consume('NavPathStack') pageStack: NavPathStack;

  register():void{
    // 制作http注册请求
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/auth/register',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json' },
        extraData: JSON.stringify({
          email: this.email,
          passWord: this.password,
          userName: this.nickname
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response = JSON.parse(data.result as string);
          console.log(rawResponse.message);
          console.info(rawResponse.code.toString());
          console.log(rawResponse.data);
          if (rawResponse.code===200) {
            AlertDialog.show({
              title: '提示',
              message:'注册成功,请登录'
            })
            this.pageStack.pop();
          }
          else {
            AlertDialog.show({
              title: '提示',
              message:'注册失败'
            })
          }
          this.email="";
          this.password="";
          this.passwordConfirm="";
          this.nickname="";
        }
        else {
          AlertDialog.show({
            title: '提示',
            message:'未知错误'
          })
          console.error(err.message);
        }
        httpRequest.destroy();
      }
    )
  }

  build() {
    NavDestination(){
      Column() {
        // 标题
        Image($r('app.media.logo'))
          .width('85vp')
          .height('85vp')
          .borderRadius(100)
          .margin({ top: '48vp',bottom: '24vp'})
        Text('注册账号')
          .fontSize('24fp')
          .fontWeight(FontWeight.Medium)
          .fontColor('#182431')
          .margin({  bottom: '20vp' })

        // 邮箱输入
        TextInput({ placeholder: '邮箱',text:this.email})
          .type(InputType.Email)
          .inputStyle()
          .onChange((value: string) => {
            this.email = value;
          })
        Line().lineStyle()

        // 密码输入
        TextInput({ placeholder: '密码',text:this.password })
          .type(InputType.Password)
          .inputStyle()
          .onChange((value: string) => {
            this.password = value;
          })
        Line().lineStyle()

        // 密码输入
        TextInput({ placeholder: '确认密码' ,text:this.passwordConfirm})
          .type(InputType.Password)
          .inputStyle()
          .onChange((value: string) => {
            this.passwordConfirm = value;
            this.showPasswordError = (this.password !== this.passwordConfirm); // 实时检测
          })
        Line().lineStyle()
        // 错误提示
        if (this.showPasswordError) {
          Text('两次输入的密码不一致')
            .fontColor('#FF0000')
            .fontSize('12fp')
            .margin({ top: '4vp' })
        }
        // 昵称输入
        TextInput({ placeholder: '昵称' ,text:this.nickname})
          .inputStyle()
          .onChange((value: string) => {
            this.nickname = value;
          })
        Line().lineStyle()

        // 注册按钮
        Button('注册', { type: ButtonType.Capsule })
          .width('328vp')
          .height('40vp')
          .fontSize('16fp')
          .fontWeight(FontWeight.Medium)
          .backgroundColor('#007DFF')
          .margin({ top: '48vp' })
          .onClick(() => {
            this.register();
          })


        // 已有账号？去登录
        Text('已有账号？去登录')
          .fontColor('#007DFF')
          .fontSize('14fp')
          .margin({ top: '24vp' })
          .onClick(() => {
            this.pageStack.pop();
          })
      }
      .padding({ left: '12vp', right: '12vp' })
      .backgroundColor('#F1F3F5')
      .width('100%')
      .height('100%')
    }
    }

}

// 复用已有的 @Extend 样式
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

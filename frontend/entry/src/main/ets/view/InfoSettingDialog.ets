import { picker } from '@kit.CoreFileKit';
import fs from '@ohos.file.fs';
import { http } from '@kit.NetworkKit';
import { Response } from '../common/types/type';
import { BusinessError, request } from '@kit.BasicServicesKit';
import { common } from '@kit.AbilityKit';
import { promptAction } from '@kit.ArkUI';


@CustomDialog
export struct InfoSettingDialog{
  @State arr:string[] = [];
  @State userName:string  = '';
  @State email:string  = '';
  @StorageLink('userId') userId: number = NaN;
  @StorageLink('avatar') avatar: string = '';
  InfoSettingDialogController?: CustomDialogController;
  aboutToAppear(): void {
    this.avatar  =`http://192.168.22.1:5000/api/avatar/`+AppStorage.get('userId')
  }
  // 提取图片的函数
  async pickerAvatar() {
    //1.引导用户选择一张系统相册照片
    //1.1绑定用户只能从系统相册选择一张图片
    const options = new picker.PhotoSelectOptions
    options.MIMEType = picker.PhotoViewMIMETypes.IMAGE_TYPE //只能选择相片资源
    options.maxSelectNumber = 1 //最大选择图片数目

    //1.2 利用PhotoViewPicker对象实例中的select自动获取到用户选择的那张图片的地址
    const pickerView = new picker.PhotoViewPicker()
    let urls = await pickerView.select(options)
    if (urls.photoUris.length <= 0) {
      return
    }
    let imgUrl = urls.photoUris[0]
    AlertDialog.show({ message: imgUrl })
    return imgUrl
  }
  async copyImgToCache(photoImgPath: string) {
    //
    // 2.1 根据源地址获取文件的磁盘目录，通过openSync()来获得的内存地址
    const file = fs.openSync(photoImgPath, fs.OpenMode.READ_ONLY)
    let fileFD = file.fd

    //2.2利用fs拷贝到目标地址
    //获取目标地址文件夹目录
    let destPath = getContext().cacheDir
    //随机生成一个文件名
    let fileName = Date.now().toString()
    //固定一个文件扩展名
    const ext = 'jpg'
    //组合成一个完整的目标文件路径
    let fullPath = destPath + '/' + fileName + '.' + ext
    fs.copyFileSync(fileFD, fullPath)
    //internal://cache代表fullPath 的文件路径
    AppStorage.setOrCreate('avatar', `file://${fullPath}`)
    return [`internal://cache/${fileName + '.' + ext}`, fileName + '.' + ext]
  }
  async uploadAvatar(arr: string[]) {
    let context = this.getUIContext().getHostContext() as common.UIAbilityContext;
    let uploadConfig:request.UploadConfig =  {
      method: 'POST',
      url: 'http://192.168.22.1:5000/user/updateAvatarByHMOS',
      header: {
        'Content-Type': 'multipart/form-data',
        'Authorization': `Bearer ${AppStorage.get('token')}`,
      },
      files: [{ filename: this.userId.toString(), type: 'jpg', name: 'file', uri: arr[0] }],
      data: []
    }
    try {
      request.uploadFile(context, uploadConfig).then(() => {
      }).catch((err: BusinessError) => {
        console.error(`Failed to request the upload. Code: ${err.code}, message: ${err.message}`);
      });
    } catch (err) {
      console.error(`Failed to request the upload. err: ${JSON.stringify(err)}`);
    }
  }
  uploadInfo(){
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/user/updateInfoByHarmonyOS',
      {
        method: http.RequestMethod.POST,
        header: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`,
        },
        extraData: JSON.stringify({
          email: this.email,
          userName:this.userName,
          userId: this.userId
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response = JSON.parse(data.result as string) as Response;
          if (rawResponse.code===200) {
            AppStorage.setOrCreate('userName',this.userName)
            AppStorage.setOrCreate('email',this.email)
            promptAction.showToast({ message:"修改成功" });
            this.InfoSettingDialogController?.close();
          }
          else{
            promptAction.showToast({ message:"修改错误" });
          }
        }
        else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
        }
        httpRequest.destroy();
      })
  }

  build() {
    Column() {
      // 标题
      Text('修改个人信息')
        .fontSize(18) // 缩小字号
        .margin({ top: 20, bottom: 15 }) // 缩小间距

      // 头像上传区域
      Stack() {
        Text("请选择新的头像")
          .fontSize(14)
        Image(this.avatar)
          .width(100)    // 缩小头像尺寸
          .height(100)
          .borderRadius(40)
          .margin({ bottom: 10 }) // 缩小间距
          .border({ width: 2, color: '#eee' })
          .onClick(async () => {
            let photoImgPath = await this.pickerAvatar()
            if (!photoImgPath) return
            this.arr = await this.copyImgToCache(photoImgPath)
            this.uploadAvatar(this.arr)
          })
      }
      .margin({ bottom: 20 }) // 缩小区域间距
      // 用户名输入框
      TextInput({ placeholder: '请输入新用户名', text: this.userName })
        .placeholderColor('#999')
        .height(40)    // 缩小高度
        .margin({ bottom: 10 }) // 缩小间距
        .width('90%')
        .borderRadius(24)
        .backgroundColor(Color.White)
        .padding({ left: 20, right: 20 })
        .onChange((value: string) => {
          this.userName = value
        })

      // 邮箱输入框
      TextInput({ placeholder: '请输入新邮箱', text: this.email })
        .type(InputType.Email)
        .placeholderColor('#999')
        .height(40)    // 缩小高度
        .margin({ bottom: 30 }) // 缩小间距
        .width('90%')
        .borderRadius(24)
        .backgroundColor(Color.White)
        .padding({ left: 20, right: 20 })
        .onChange((value: string) => {
          this.email = value
        })
      Button('修改信息', { type: ButtonType.Capsule })
        .width('80%')
        .height(40)    // 缩小按钮高度
        .margin({ bottom: 15 })
        .backgroundColor($r('sys.color.brand'))
        .fontColor(Color.White)
        .fontSize(16)
        .stateEffect(true)
        .onClick(() => {
          this.uploadInfo()
        })
    }
    .width('80%')      // 缩小对话框宽度
    .height('70%')     // 缩小对话框高度
    .padding(15)       // 减少内边距
    .backgroundColor('#F5F5F5')
    .borderRadius(50)
  }
}
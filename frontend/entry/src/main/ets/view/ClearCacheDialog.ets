import storageStatistics from "@ohos.file.storageStatistics";
import { CacheUtil } from "../common/utils/CacheUtils";


@CustomDialog
export struct ClearCacheDialog {
  static clearCache() {
    throw new Error('Method not implemented.');
  }
  @State cache: string = ''
  ClearCacheDialogController?: CustomDialogController;
  calcCache(){
    storageStatistics.getCurrentBundleStats()
      .then(res => {
        this.cache = (res.cacheSize / (1000 * 1000)).toFixed(2) + 'MB'
      })
  }
  aboutToAppear(): void {
    this.calcCache()
  }
  public clearCache() {
    CacheUtil.clearCache()
    this.calcCache()
  }

  build() {
    Column() {
      // 添加标题栏
      Row() {
        Text('缓存清理')
          .fontSize(18)
          .fontColor(Color.Black)
          .fontWeight(FontWeight.Bold)
      }
      .width('100%')
      .padding(10)
      .backgroundColor('#F5F5F5')
      // 内容区域
      Column() {
        // 缓存信息展示
        Row() {
          Text('当前缓存：')
            .fontColor('#666')
          Text(this.cache)
            .fontColor(Color.Black)
            .fontWeight(FontWeight.Medium)
        }
        .margin({ top: 20, bottom: 30 })

        // 操作按钮组
        Row() {
          Button('取消', { type: ButtonType.Normal })
            .width(120)
            .height(40)
            .fontColor('#007AFF')
            .backgroundColor('#EEEEEE')
            .onClick(() => {
              this.ClearCacheDialogController?.close()
            })

          Button('立即清理', { type: ButtonType.Capsule })
            .width(120)
            .height(40)
            .margin({ left: 20 })
            .backgroundColor('#007AFF')
            .fontColor(Color.White)
            .onClick(() => {
              this.clearCache()
              this.calcCache()
            })
        }
        .justifyContent(FlexAlign.Center)
      }
      .padding({ left: 24, right: 24, bottom: 20 })
    }
    .width('80%')
    .borderRadius(16)
    .backgroundColor(Color.White)
  }
}
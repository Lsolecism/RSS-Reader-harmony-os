import { Response, SongItemType } from "../common/types/type";
import { musicPlayer } from "../common/utils/AvPlayerUtils";
import { http } from "@kit.NetworkKit";
import { promptAction } from "@kit.ArkUI";

@Component
export struct Music {
  @StorageLink('fontSizeOffset') fontSizeOffset: number = 0;
  @StorageLink('playSongId') playSongId: string = "-1";
  @Link pathStack: NavPathStack;
  // 设置看一下当前的播放歌曲playSongId
  aboutToAppear(): void {
    this.getSongs();
    if(AppStorage.get('playSongId')!=="-1")
    {
      console.info("现在的id",  AppStorage.get('playSongId'))
    }
    else {
      AppStorage.set('playSongId', "-1")
    }
  }

  getSongs(){
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/music/get-music',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`}
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<Array<SongItemType>> = JSON.parse(data.result as string)as Response<Array<SongItemType>>;
          console.info("获取歌曲列表成功", rawResponse)
          if(rawResponse.code===200){
            if(rawResponse.data){
              this.songs = rawResponse.data;
              this.songs = [...this.songs]
            }
          }else{
            promptAction.showToast({ message: rawResponse.message });
          }
        }
        else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
        }
        httpRequest.destroy();
      }
    )
  }

  @State songs: SongItemType[] = []

  build() {
    Column() {
      Text("今日音乐推荐")
        .fontWeight(700)
        .fontSize(26 + this.fontSizeOffset)
        .width('100%')  // 确保 Row 容器宽度为 100%
        .margin({bottom: '10vp',top: '42vp'})
      Scroll(){
        List() {
          ForEach(this.songs, (item: SongItemType, index: number) => {
            ListItem() {
              Row() {
                // 图
                Stack() {
                  Image(item.img)
                    .width(80)
                    .border({ radius: 8 })
                    .margin({ right: 10 })
                  if(item.id=== this.playSongId){
                    Image($r('app.media.wave'))
                      .width(24)
                  }
                }
                // 字
                Column() {
                  Text(item.name)
                    .fontColor('#ff070202')
                    .width('100%')
                    .fontWeight(700)
                    .margin({ bottom: 15 })
                  Row() {
                    Text(item.author)
                      .fontColor('#696969')
                  }
                  .width('100%')
                }
                .layoutWeight(1)

                // 更多
                Image($r('app.media.ic_more'))
                  .width(24)
              }
              .width('100%')
              .height(80)
              // .backgroundColor(Color.Pink)
              .margin({ bottom: 10 })
              .onClick(async () => {
                AppStorage.set('playSongId', item.id)

                let SongParam: SongItemType = item
                let pethInfo: NavPathInfo = new  NavPathInfo("MusicPlayPage",SongParam);
                this.pathStack.pushDestination(pethInfo,true);
              })
            }
          })
        }
        .scrollBar(BarState.Off)
      }
      .height('660vp')
      .align(Alignment.TopStart)
      .scrollBar(BarState.Off)
    }
    .justifyContent(FlexAlign.Start)
    .alignItems(HorizontalAlign.Start)
    .width('100%')
    .height('100%')
  }
}
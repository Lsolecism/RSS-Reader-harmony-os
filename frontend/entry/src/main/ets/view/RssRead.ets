import { Response, RSSPageParams, RSSSubscriptionWithItems } from "../common/types/type";
import { AddRssDialog } from "./AddRssDialog";
import { http } from "@kit.NetworkKit";
import { ImageUtils } from "../common/utils/ImageUtils";
import { promptAction } from "@kit.ArkUI";

@Component
export default struct RssRead {
  @StorageLink('fontSizeOffset') fontSizeOffset: number = 0;
  @State rssList:  Array<RSSSubscriptionWithItems> = [];
  aboutToAppear(): void {
      this.initRss();
  }
  @Link pathStack: NavPathStack;
  // 初始化读取RSS源
  initRss(){
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/rss/initRSS',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`},
        extraData: JSON.stringify({
          userId:AppStorage.get('userId'),
          rssFeed:null,
          rssTitle:null
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<Array<RSSSubscriptionWithItems>> = JSON.parse(data.result as string)as Response<Array<RSSSubscriptionWithItems>>;
          if(rawResponse.code===200){
            if(rawResponse.data){
              for(let i=0;i<rawResponse.data.length;i++){
                for(let j=0;j<rawResponse.data[i].rssItems.length;j++){
                  rawResponse.data[i].rssItems[j].image = ImageUtils.getFirstImage(rawResponse.data[i].rssItems[j].contents);
                }
                this.rssList = [...this.rssList,  rawResponse.data[i]]
              }
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
  // 回调接收到添加的RSS源
  AddRssDialogController:  CustomDialogController = new CustomDialogController({
    builder: AddRssDialog({
      onSuccess: (rawResponse:Response<RSSSubscriptionWithItems>) => {
        if (rawResponse.data?.rssItems) {
          // 正确遍历 rssItems 数组
          for (let index = 0; index < rawResponse.data.rssItems.length; index++) {
            rawResponse.data.rssItems[index].image = ImageUtils.getFirstImage(rawResponse.data.rssItems[index].contents);
          }
          this.rssList = [...this.rssList,rawResponse.data]
          promptAction.showToast({ message: rawResponse.message });
        }
      }
    }),
    alignment: DialogAlignment.Bottom,
    customStyle: true,
    offset: { dx: 0, dy: -25 }
  });
  // 删除RSS源
  deleteRss(rssId: number) {
    this.rssList = this.rssList.filter(rss => rss.rssSubscription.rssId !== rssId);
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/rss/updateRSS',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`},
        extraData: JSON.stringify({
          userId:AppStorage.get('userId'),
          deleteRSSIds:[rssId],
          deleteRSSItemIds:[],
          likeRSSItemIds:[],
          readRSSItemIds:[]
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<string> = JSON.parse(data.result as string)as Response<string>;
          if(rawResponse.code===200){
            promptAction.showToast({ message: rawResponse.message });
          }else{
            promptAction.showToast({ message: rawResponse.message });
          }
        }
        else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
        }
      }
    )
  }

  build() {
    Column() {
      Row(){
        Text("RSS阅读")
          .fontWeight(700)
          .fontSize(26 + this.fontSizeOffset)
        Image($r('app.media.addRSS'))
          .width('30vp')
          .height('30vp')
          .onClick(() => {
            this.AddRssDialogController.open();
          })
      }
      .width('100%')  // 确保 Row 容器宽度为 100%
      .justifyContent(FlexAlign.SpaceBetween)
      .margin({bottom: '10vp',top: '42vp'})

      List({ space: 3 }) {
        ForEach(this.rssList, (item: RSSSubscriptionWithItems) => {
          ListItem() {
              // 右侧文字信息区域
              Column() {
                // 订阅源标题
                Row(){
                  Text(item.rssSubscription.rssTitle)
                    .fontSize(18 + this.fontSizeOffset)
                    .fontWeight(FontWeight.Bold)
                    .margin({ bottom: 8 })
                    .maxLines(1)
                    .textOverflow({ overflow: TextOverflow.Ellipsis })
                  Image($r('app.media.recycle'))
                    .width(30)
                    .height(30)
                    .onClick(() => {
                       this.deleteRss(item.rssSubscription.rssId);
                    })
                }
                .width('100%')
                .justifyContent(FlexAlign.SpaceBetween)

                // 作者和时间行
                Row() {
                  Text(item.rssItems[0]?.author || '未知作者')
                    .fontSize(14 + this.fontSizeOffset)
                    .fontColor('#666')
                    .margin({ right: 8 })

                  Text("最新日期"+item.rssItems[0]?.pubDate.toString())
                    .fontSize(14 + this.fontSizeOffset)
                    .fontColor('#999')
                }
                .width('100%')
                .justifyContent(FlexAlign.Start)
              }
              .alignItems(HorizontalAlign.Start)
              .layoutWeight(1)
            }
            .onClick(()=>{
              let RSSParam: RSSSubscriptionWithItems  = item;
              let pethInfo: NavPathInfo = new  NavPathInfo("RSSPage",RSSParam);
              this.pathStack.pushDestination(pethInfo,true);
            })
            .padding(16)
            .borderRadius(12)
            .backgroundColor(Color.White)
            .shadow({
              radius: 4,
              color: '#10000000',
              offsetX: 1,
              offsetY: 1
            })
        })
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
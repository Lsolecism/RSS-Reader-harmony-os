import { Response, RSSItem, RSSPageParams, RSSSubscription, RSSSubscriptionWithItems } from "../common/types/type";
import { http } from "@kit.NetworkKit";
import { promptAction } from "@kit.ArkUI";

const TAG = 'RSSPage';

@Builder
export function RSSPageBuilder() {
  RSSPage()
}

@Component
export struct RSSPage {
  @StorageLink('fontSizeOffset') fontSizeOffset: number = 0;
  @State RSSParam: RSSSubscriptionWithItems | undefined = undefined;
  @State rssSubscription:RSSSubscription | undefined = undefined;
  @State rssItems: Array<RSSItem> = [];
  @State favorate: Resource = $r('app.media.ic_public_favor');
  @State pathStack: NavPathStack = new NavPathStack();
  // 删除RSS
  deleteRssItem(rssItemId: number) {
    this.rssItems = this.rssItems.filter(rss => rss.rssItemId!== rssItemId);
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/rss/updateRSS',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`},
        extraData: JSON.stringify({
          userId:AppStorage.get('userId'),
          deleteRSSIds:[],
          deleteRSSItemIds:[rssItemId],
          likeRSSItemIds:[],
          readRSSItemIds:[]
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<string> = JSON.parse(data.result as string)as Response<string>;
          if(rawResponse.code===200){
            promptAction.showToast({ message: "删除成功" });
          }else{
            promptAction.showToast({ message: "删除失败" });
          }
        }
        else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
        }
        httpRequest.destroy()
      }
    )

  }
  // 喜欢RSS
  likeRssItem(rssItemId:number){
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/rss/updateRSS',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`},
        extraData: JSON.stringify({
          userId:AppStorage.get('userId'),
          deleteRSSIds:[],
          deleteRSSItemIds:[],
          likeRSSItemIds:[rssItemId],
          readRSSItemIds:[]
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<string> = JSON.parse(data.result as string)as Response<string>;
          if(rawResponse.code===200){
          }else{
            promptAction.showToast({ message: rawResponse.message });
          }
        }
        else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
        }
        httpRequest.destroy()
      }
    )

  }
  // 阅读RSS
  readRssItem(rssItemId:number){
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/rss/updateRSS',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`},
        extraData: JSON.stringify({
          userId:AppStorage.get('userId'),
          deleteRSSIds:[],
          deleteRSSItemIds:[],
          likeRSSItemIds:[],
          readRSSItemIds:[rssItemId]
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<string> = JSON.parse(data.result as string)as Response<string>;
          if(rawResponse.code===200){
          }else{
            promptAction.showToast({ message: rawResponse.message });
          }
        }
        else {
          promptAction.showToast({ message: "发生错误,请稍后尝试。" });
        }
        httpRequest.destroy()
      }
    )
  }

  build(){
    NavDestination(){
      Column() {
        Text(`${this.rssSubscription?.rssTitle}订阅详情`)
          .width('100%')
          .margin({
            top: '48vp',
            bottom: '12vp',
            left: '12vp',
          })
          .fontWeight(700)
          .fontSize(26 + this.fontSizeOffset)
        Column(){
          Text("订阅源地址："+this.rssSubscription?.rssFeed)
            .width('100%')
            .fontWeight(700)
            .margin({
              bottom: '3vp'
            })
            .fontSize(10 + this.fontSizeOffset)
          Text("订阅日期："+this.rssSubscription?.newTime.toString())
            .width('100%')
            .fontWeight(700)
            .fontSize(10 + this.fontSizeOffset)
            .margin({
              bottom: '3vp'
            })
          Text("订阅源ID："+this.rssSubscription?.rssId)
            .width('100%')
            .fontWeight(700)
            .fontSize(10 + this.fontSizeOffset)
        }.borderRadius(16)
        .backgroundColor($r('app.color.white'))
        .padding(10)
        .margin({left: '12vp', right: '12vp'})

        Text("文章列表")
          .fontSize('18fp')
          .fontWeight(700)
          .width('100%')
          .margin({
            top: '18vp',
            bottom: '8vp',
            left: '12vp',
          })
        Scroll(){
          List() {
            ForEach(this.rssItems, (item: RSSItem) => {
              ListItem() {
                Row() {
                  Image(item.image)
                    .width('130vp')
                    .height('80vp')
                    .objectFit(ImageFit.Cover)
                    .borderRadius(8)
                    .margin({ right: '12vp' })

                  Column() {
                    Text(item.title)
                      .width('190vp')
                      .textOverflow({ overflow: TextOverflow.Ellipsis })
                      .maxLines(1)
                      .fontSize(16 + this.fontSizeOffset)
                      .fontWeight(FontWeight.Medium)
                    Text(item.description)
                      .width('190vp')
                      .textOverflow({ overflow: TextOverflow.Ellipsis })
                      .maxLines(2)
                      .margin({ top: '4vp' })
                      .fontSize(12 + this.fontSizeOffset)
                      .fontColor('#99182431')
                    Row() {
                      Image($r('app.media.ic_public_delete'))
                        .width('20vp')
                        .onClick(() => {
                          this.deleteRssItem(item.rssItemId)
                          this.rssItems = [...this.rssItems];  // 触发列表刷新
                        })
                      Image(item.liked===1
                        ? $r('app.media.ic_public_favor_filled')
                        : $r('app.media.ic_public_favor')
                      )
                        .width('20vp')
                        .onClick(() => {
                          item.liked = item.liked===1?0:1 ; // 直接修改当前项的收藏状态
                          this.rssItems = [...this.rssItems];  // 触发列表刷新
                          this.likeRssItem(item.rssItemId)
                        })
                      Image(item.readed===1
                        ? $r('app.media.ic_public_highlightsed')
                        : $r('app.media.ic_public_highlights')
                      )
                        .width('20vp')
                        .onClick(() => {
                          item.readed = item.readed===1?0:1 ; // 直接修改当前项的阅读状态
                          this.rssItems = [...this.rssItems];  // 触发列表刷新这个是修改值引用来触发ui刷新
                          this.readRssItem(item.rssItemId)
                        })
                      Image($r('app.media.arrow'))
                        .width('20vp')
                        .onClick(()=>{
                          let RSSShowParam: RSSItem  = item;
                          let pethInfo: NavPathInfo = new  NavPathInfo("RSSShowPage",RSSShowParam);
                          this.pathStack.pushDestination(pethInfo,true);
                        })
                    }
                    .width('170vp')
                    .margin({ top: '10.5vp' })
                    .justifyContent(FlexAlign.SpaceBetween)
                    .alignItems(VerticalAlign.Bottom)
                  }
                  .alignItems(HorizontalAlign.Start)
                }
                .width('100%')
                .padding({
                  left: '12vp',
                  right: '12vp',
                  top: '12vp',
                  bottom: '12vp'
                })
                .justifyContent(FlexAlign.SpaceBetween)
              }
              .margin({ bottom: '8vp' })
              .borderRadius('16vp')
              .backgroundColor('#ffffff')
              .borderRadius($r('app.float.home_backgroundImage_borderRadius'))
              .align(Alignment.TopStart)
              .width('100%')
            })
          }
          .margin({bottom:'20vp' })
        }
        .height('660vp')
        .align(Alignment.TopStart)
        .scrollBar(BarState.Off)
      }
      .justifyContent(FlexAlign.Start)
      .alignItems(HorizontalAlign.Start)
      .width('100%')
      .height('100%')
      .backgroundColor($r('app.color.background'))
    }.hideBackButton(true)
    .onReady(cxt=>{
      this.pathStack=cxt.pathStack;
      this.RSSParam=cxt.pathInfo.param as RSSSubscriptionWithItems;
      this.rssSubscription=this.RSSParam.rssSubscription;
      this.rssItems=[...this.RSSParam.rssItems];
    })
  }

}
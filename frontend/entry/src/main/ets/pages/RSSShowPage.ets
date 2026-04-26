import { RSSItem } from "../common/types/type";
import { webview } from "@kit.ArkWeb";
const TAG = 'RSSShowPage';

@Builder
export function RSSShowPageBuilder() {
  RSSShowPage()
}

class RSSItemClass{
  private rssItem:RSSItem;
  constructor(rssItem:RSSItem) {
    this.rssItem = rssItem;
  }
  test():RSSItem{
    return this.rssItem
  }
}
@Component
export struct RSSShowPage {
  @StorageLink('fontSizeOffset') fontSizeOffset: number = 0;
  @State rssItem:RSSItem |undefined = undefined;
  @State testObj:RSSItemClass|undefined =undefined;
  webviewController: webview.WebviewController = new webview.WebviewController();

  // 初始化读取RSS源
  build() {
    NavDestination() {
      Column() {
        Web({src:$rawfile('RSSShowView.html'),controller:this.webviewController})
          .onPageEnd(()=>{
              this.webviewController.runJavaScript('injectHtmlContent()')
          })
          .javaScriptProxy({
            object:this.testObj,
            name:"RSSShow",
            methodList:["test"],
            controller:this.webviewController
          })
      }
    }
    .hideBackButton(true)
    .onReady(cxt=>{
      this.rssItem=cxt.pathInfo.param as RSSItem;
      this.testObj = new RSSItemClass(this.rssItem); // 确保数据有效时创建
      console.info("rssItem:",this.rssItem.description)
    })
  }
}
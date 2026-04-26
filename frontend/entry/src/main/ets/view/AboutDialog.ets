import { webview } from '@kit.ArkWeb';



@CustomDialog
export struct AboutDialog {
  AboutDialogController?: CustomDialogController;
  webviewController: webview.WebviewController = new webview.WebviewController();

  build() {
    Column(){
        Web({src:'https://www.devillusion.asia/', controller:this.webviewController})
    }.margin({top: '40vp'})
    .height('100%')
  }
}
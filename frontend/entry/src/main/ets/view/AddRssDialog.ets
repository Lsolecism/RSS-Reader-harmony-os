import { http } from "@kit.NetworkKit";
import { Response, RSSSubscriptionWithItems } from "../common/types/type";

@CustomDialog
export struct AddRssDialog {
  AddRssDialogController?: CustomDialogController;
  public onSuccess?: (rawResponse: Response<RSSSubscriptionWithItems>) => void;
  @State RSSSource: string = "";
  @State RSSTitle: string = "";

  addRSS(){
    let httpRequest = http.createHttp();
    httpRequest.request(
      'http://192.168.22.1:5000/rss/getRSSItems',
      {
        method: http.RequestMethod.POST,
        header: { 'Content-Type': 'application/json',
          'Authorization': `Bearer ${AppStorage.get('token')}`},
        extraData: JSON.stringify({
          userId:AppStorage.get('userId'),
          rssFeed:this.RSSSource,
          rssTitle:this.RSSTitle
        })
      },
      (err, data) => {
        if (!err) {
          const rawResponse: Response<RSSSubscriptionWithItems> = JSON.parse(data.result as string)as Response<RSSSubscriptionWithItems>;
          this.onSuccess?.(rawResponse); // 传递数据给父组件
          AlertDialog.show({
            title: '提示',
            message:rawResponse.message
          })
          this.AddRssDialogController?.close(); // 关闭对话框
        }else {
          AlertDialog.show({
            title: '提示',
            message:'获取RSS失败'
          })
          this.AddRssDialogController?.close(); // 关闭对话框
        }
        httpRequest.destroy();
      }
    )
  }

  build() {
    Column({space:10}) {
      Text("添加RSS订阅源")
        .fontSize(24)
        .fontWeight(FontWeight.Bold)
      TextInput({placeholder: "请输入RSS源地址",text:this.RSSSource})
        .placeholderColor(Color.Gray)
        .borderRadius(8)
        .borderWidth(1)
        .borderColor(Color.Gray)
        .onChange((value: string) => {
          this.RSSSource = value;
        })
      TextInput({placeholder: "请输入RSS源标题",text:this.RSSTitle})
        .placeholderColor(Color.Gray)
        .borderRadius(8)
        .borderWidth(1)
        .borderColor(Color.Gray)
        .onChange((value: string) => {
          this.RSSTitle = value;
        })
      Button("确认")
        .type(ButtonType.Capsule)
        .width('100%')
        .height('40vp')
        .fontSize('16fp')
        .borderRadius('20vp')
        .fontWeight(FontWeight.Medium)
        .backgroundColor($r('app.color.login_button_color'))
        .onClick(()=>{
          this.addRSS()
        })
    }.backgroundColor($r('app.color.background'))
    .padding(16)
    .borderRadius(16)
  }
}
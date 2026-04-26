

interface PrivacySection {
  title: string;
  content: string;
}

// PrivacyDialog.ets
interface GeneratedObjectLiteralInterface_1 {
  title: string;
  updateDate: string;
  sections: PrivacySection[];
}

@CustomDialog
export struct PrivacyDialog {
  PrivacyDialogController?: CustomDialogController;
  // 协议内容建议单独维护
  private privacyContent: GeneratedObjectLiteralInterface_1 = {
    title: '📜隐私政策',
    updateDate: '2025年6月6日',
    sections: [
      {
        title: '📱 一、信息收集与使用',
        content: `为提供个性化服务与安全保障，我们可能收集：
        设备信息：包括设备型号、操作系统版本、屏幕分辨率、网络类型等基础参数
        使用画像：功能使用路径、操作频次、页面停留时长等行为数据（通过去标识化处理）
        主动提供信息：注册账号时提交的手机号/邮箱；实名认证所需的姓名及身份证号
        第三方接入：当您使用微信/QQ登录时，获取经您授权的公开资料（头像、昵称）
    📝 特别注意：地理位置、通讯录等敏感权限默认关闭，启用时将单独请求授权`
      },
      {
        title: '🔐 二、数据存储与保护',
        content: `我们构建了多层防护体系：
        存储标准：采用AES-256加密存储于境内腾讯云服务器（等保三级认证），数据库实行分片隔离
        传输保障：全链路SSL/TLS加密通信，关键接口增加数字证书双向验证
        数据留存：操作日志保留6个月后自动匿名化；账号注销后数据30天内不可逆删除
    🚨 安全机制：实施7×24小时入侵检测，建立数据泄露应急响应预案`
      },
      {
        title: '👑 三、用户权利',
        content: `您始终享有数据自主权：
        查询/导出：在「账号中心-数据看板」查阅个人数据并申请导出
        更正/遗忘：通过客服通道提交信息更正或删除请求（3个工作日内处理）
        授权管理：在系统权限设置中实时调整相机、通讯录等权限开关
        账号注销：完成账户安全验证后，在设置页面发起永久注销流程
    💡 小贴士：行使权利不会影响您已享受的服务权益`
      },
      {
        title: '🧸 四、未成年人保护',
        content: `我们采取特别防护措施：
        年龄门槛：注册时强制进行年龄验证，14岁以下用户自动进入青少年模式
        监护人机制：提供「亲子守护」功能，支持监护人远程管理使用时长/消费限额
        内容过滤：青少年模式下启用强化内容审核，屏蔽不适合信息
        主动筛查：定期通过AI算法识别未成年人账户，发现后立即启动保护程序
    🏠 建议：监护人可通过service@sealand.com提交监护申请`
      },
      {
        title: '🔄 五、协议变更',
        content: `政策更新将透明化推进：
        通知方式：重大变更前30天通过APP推送、短信、官网横幅（www.sealand.com）三重提醒
        版本追溯：在「设置-法律条款」中保留最近6次历史版本供查阅
        生效规则：继续使用服务视为接受新政策，否则可在此期间注销账号
    📅 最近更新：2023年11月15日（添加数据导出功能说明）`
      }
    ]
  }

  privacy(){
    this.PrivacyDialogController?.close()
    AppStorage.set("privacyAgreed",false);
    console.info("隐私信息拒绝时{}",AppStorage.get("privacyAgreed"))
    // 可根据业务需求处理不同意的情况
    getContext(this).getApplicationContext().clearUpApplicationData();
    console.info("拒绝后的状态{}",AppStorage.get("privacyAgreed"))
  }

  build() {
    Column() {
      // 标题区域
      Row() {
        Text(this.privacyContent.title)
          .fontSize(20)
          .fontColor('#333')
          .fontWeight(FontWeight.Bold)

        Text(`更新日期：${this.privacyContent.updateDate}`)
          .fontSize(12)
          .fontColor('#666')
          .margin({ left: 10 })
      }
      .width('100%')
      .padding(15)
      .backgroundColor('#F8F8F8')

      // 协议内容区域
      Scroll() {
        Column() {
          // 序言
          Text('欢迎使用我们的服务！请仔细阅读本隐私政策，了解我们如何处理您的个人信息。')
            .fontSize(14)
            .fontColor('#444')
            .lineHeight(22)
            .margin({ bottom: 20 })

          // 条款列表
          ForEach(this.privacyContent.sections, (item: PrivacySection) => {
            Column() {
              // 条款标题
              Row() {
                Text(`${item.title}`)
                  .fontSize(16)
                  .fontColor('#333')
                  .fontWeight(FontWeight.Medium)
              }
              .margin({ bottom: 8 })

              // 条款内容
              Text(item.content)
                .fontSize(14)
                .fontColor('#666')
                .lineHeight(20)
                .textAlign(TextAlign.Start)
            }
            .margin({ bottom: 25 })
          })
          // 操作按钮
          Row() {
            Button('不同意')
              .width(120)
              .height(40)
              .fontColor('#666')
              .backgroundColor('#EEE')
              .onClick(() => {
                this.privacy()
              })

            Button('同意协议')
              .width(120)
              .height(40)
              .margin({ left: 20 })
              .backgroundColor('#007AFF')
              .fontColor(Color.White)
              .onClick(() => {
                this.PrivacyDialogController?.close()
                AppStorage.set("privacyAgreed",true)
              })
          }
          .justifyContent(FlexAlign.Center)
          .margin({ bottom: 30 })
        }
        .padding({ left: 20, right: 20, bottom: 20 })
      }
      .scrollable(ScrollDirection.Vertical)
      .scrollBar(BarState.Auto)
    }
    .width('90%')
    .height('90%')
    .backgroundColor(Color.White)
    .borderRadius(16)
  }
}

import { CacheUtil } from '../common/utils/CacheUtils';
import ItemData from '../viewmodel/ItemData';
import mainViewModel from '../viewmodel/MainViewModel';
import { AboutDialog } from './AboutDialog';
import { ClearCacheDialog } from './ClearCacheDialog';
import { InfoSettingDialog } from './InfoSettingDialog';
import { PrivacyDialog } from './PrivacyDialog';
import { SliderCustomDialog } from './SliderCustomDialog';

/**
 * Setting tab content
 */
@Component
export default struct Setting {
  @StorageLink('fontSizeOffset') fontSizeOffset: number = 0;
  @StorageLink('userName') userName: string = '';
  @StorageLink('email') email: string = '';
  @StorageLink('userId') userId: number = NaN;
  @StorageLink('avatar') avatar: string = '';
  SliderCustomDialogController: CustomDialogController = new CustomDialogController({
    builder: SliderCustomDialog({}),
    alignment: DialogAlignment.Bottom,
    customStyle: true,
    offset: { dx: 0, dy: -25 }
  });
  InfoSettingDialogController:  CustomDialogController = new CustomDialogController({
    builder: InfoSettingDialog({}),
    alignment: DialogAlignment.Bottom,
    customStyle: true,
    offset: { dx: 0, dy: -25 }
  });
  ClearCacheDialogController:  CustomDialogController = new CustomDialogController({
    builder: ClearCacheDialog({}),
    alignment: DialogAlignment.Bottom,
    customStyle: true,
    offset: { dx: 0, dy: -25 }
  });
  PrivacyDialogController:  CustomDialogController = new CustomDialogController({
    builder: PrivacyDialog({}),
    alignment: DialogAlignment.Bottom,
    customStyle: true,
    offset: { dx: 0, dy: -25 }
    })
  AboutDialogController:  CustomDialogController = new CustomDialogController({
    builder: AboutDialog({}),
    alignment: DialogAlignment.Bottom,
    customStyle: true,
    offset: { dx: 0, dy: -25 }
  });

  @Builder
  settingCell(item: ItemData, customDialogController: CustomDialogController) {
    Row() {
      Row({}) {
        Image(item.img)
          .height('22vp')
          .margin({
            left: '16vp',
            right: '12vp'
          })
        Text(item.title)
          .fontSize(16 + this.fontSizeOffset)
      }

      if (item.others === undefined) {
        Image($r('app.media.right_grey'))
          .width('12vp')
          .height('24vp')
      } else {
        Toggle({ type: ToggleType.Switch, isOn: false })
      }
    }
    .onClick(() => {
      if (item.id === 1) {
        customDialogController.open();
      }
      if (item.id === 2) {
        this.InfoSettingDialogController.open();
      }
      if (item.id === 3) {
        this.ClearCacheDialogController.open();
      }
      if (item.id === 4) {
        this.PrivacyDialogController.open();
      }
      if (item.id === 5) {
        this.AboutDialogController.open();
      }
    })
    .justifyContent(FlexAlign.SpaceBetween)
    .width('100%')
    .padding({
      left: '8vp',
      right: '22vp'
    })
  }

  build() {
    Scroll() {
      Column({ space: 12 }) {
        Text($r('app.string.mainPage_tabTitles_mine'))
          .width('100%')
          .margin({
            top: '48vp',
            bottom: '12vp'
          })
          .fontWeight(700)
          .fontSize(26 + this.fontSizeOffset)

        Row() {
          Image(this.avatar)
            .width('70vp')
            .height('70vp')
          Column() {
            Text(this.userName)
              .fontSize(20 + this.fontSizeOffset)
            Text(this.email)
              .fontSize(12 + this.fontSizeOffset)
              .margin({ top: '4vp' })
          }
          .alignItems(HorizontalAlign.Start)
          .margin({ left: '12vp' })
        }
        .alignItems(VerticalAlign.Center)
        .width('100%')

        List({ space: 12 }) {
          ForEach(mainViewModel.getSettingListData(), (item: ItemData[]) => {
            ListItemGroup() {
              ForEach(item, (cell: ItemData) => {
                ListItem() {
                  this.settingCell(cell, this.SliderCustomDialogController);
                }
                .height('48vp')
              }, (cell: ItemData) => JSON.stringify(cell))
            }
            .divider({
              strokeWidth: '1vp',
              color: '#0d000000',
              startMargin: '42vp',
              endMargin: '24vp'
            })
            .backgroundColor('#ffffff')
            .borderRadius('16vp')

          }, (item: ItemData) => JSON.stringify(item))
        }
        .scrollBar(BarState.Off)
        .width('100%')
        .padding({
          top: 4,
          bottom: 4
        })

        Blank()

        Button($r('app.string.setting_button'), { type: ButtonType.Capsule })
          .width('90%')
          .height('40vp')
          .fontSize(16 + this.fontSizeOffset)
          .fontColor('#FA2A2D')
          .fontWeight(FontWeight.Medium)
          .backgroundColor('#E5E8EA')
          .margin({ bottom: '16vp' })
          .onClick(() => {
            AppStorage.set("privacyAgreed",false);
            CacheUtil.clearCache();
            getContext(this).getApplicationContext().clearUpApplicationData();
          })
      }
      .height('100%')
    }
    .width('100%')
    .height('100%')
  }
}
/*
 * Copyright (c) 2024 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import ItemData from './ItemData';

/**
 * Binds data to components and provides interfaces.
 */
export class MainViewModel {
  /**
   * Get swiper image data.
   *
   * @return {Array<Resource>} swiperImages.
   */
  // TODO这个是广告栏点击之后跳转到详情页，这个直接上网站就可以
  getSwiperImages(): Array<Resource> {
    let swiperImages: Resource[] = [
      $r('app.media.diygod'),
      $r('app.media.rss_hub'),
      $r('app.media.leijun'),
      $r('app.media.idol')
    ];
    return swiperImages;
  }

  /**
   * Get data of the first grid.
   *
   * @return {Array<PageResource>} firstGridData.
   */
  // 下面的Grid里面的东西
  getFirstGridData(): Array<ItemData> {
    let firstGridData: ItemData[] = [
      new ItemData(0, $r('app.string.rsslogo'), $r('app.media.RSSlogo')),
      new ItemData(1, $r('app.string.bilibili'), $r('app.media.bilibili')),
      new ItemData(2, $r('app.string.aaaabout'), $r('app.media.about')),
      new ItemData(3, $r('app.string.chat'), $r('app.media.chat')),
      new ItemData(4, $r('app.string.my_goal'), $r('app.media.note')),
      new ItemData(5, $r('app.string.piaoliu'), $r('app.media.piaoliuBottle')),
      new ItemData(6, $r('app.string.sharezoom'), $r('app.media.sharezoom')),
      new ItemData(7, $r('app.string.music'), $r('app.media.music')),
    ];
    return firstGridData;
  }

  /**
   * Get data of the setting list.
   *
   * @return {Array<PageResource>} settingListData.
   */
  // 这个是设置的
  getSettingListData(): Array<Array<ItemData>> {
    let settingListData: ItemData[][] = [
      [
        new ItemData(0, $r('app.string.setting_list_news'), $r('app.media.news'), $r("app.string.setting_toggle"))
      ],
      [
        new ItemData(1, $r('app.string.setting_list_font'), $r('app.media.data')),
        new ItemData(2, $r('app.string.setting_list_menu'), $r('app.media.menu')),
      ],
      [
        new ItemData(3, $r('app.string.clear_cache'), $r('app.media.storage')),
        new ItemData(4, $r('app.string.setting_list_privacy'), $r('app.media.person_shield')),
      ],
      [
        new ItemData(5, $r('app.string.setting_list_about'), $r("app.media.aaaabout"))
      ]
    ];
    return settingListData;
  }
}

export default new MainViewModel();
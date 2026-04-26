export interface Response<T = undefined> {
  code: number;
  message: string;
  data?: T;
}

export interface User{
  userId:number;
  userName:string;
  userPassword:string;
  email:string;
}

export interface AuthResponseDTO{
  user:User;
  token:string;
}
export interface PrivacySection {
  title: string;
  content: string;
}

export interface RSSSubscriptionWithItems{
  rssSubscription:  RSSSubscription;
  rssItems:Array<RSSItem>;
}
export interface RSSSubscription {
  rssId:number;
  rssTitle:string;
  rssFeed:string;
  newTime:Date;
}
export interface RSSItem {
  rssItemId:number;
  title:string;
  author:string;
  uri:string;
  link:string;
  description:string;
  contents:string;
  pubDate:Date;
  readed:number;
  liked:number;
  rssId:number;
  image?:string;
}
export interface RSSPageParams{
  id:string;
  name:string;
}
export interface RSSParams{
  rss:RSSSubscriptionWithItems;
  pathStack:NavPathStack;
}

export interface SongItemType{
  img:string;
  name:string;
  author:string;
  url:string;
  id:string
}

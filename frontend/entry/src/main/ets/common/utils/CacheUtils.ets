import { fileIo } from "@kit.CoreFileKit"
import { promptAction } from "@kit.ArkUI"

export class CacheUtil{
  public static clearCache(){
    const ctx = getContext()
    //获取当前ability的缓存路径
    const ctxCacheDir = ctx.cacheDir
    //判断是否有这个文件
    const ctxExist = fileIo.accessSync(ctxCacheDir)
    if (ctxExist) {
      //清除缓存
      fileIo.rmdirSync(ctxCacheDir)
    }
    //获得应用级的上下文
    const app = ctx.getApplicationContext()
    //应用级的缓存目录
    const appCacheDir = app.cacheDir
    //判断有无文件
    const appExist = fileIo.accessSync(appCacheDir)
    if (appExist) {
      //清除缓存
      fileIo.rmdirSync(appCacheDir)
    }
    promptAction.showToast({ message: '清理完毕' })
  }
  public static clearMusicCache(){
    const ctx = getContext()
    //获取当前ability的缓存路径
    const ctxCacheDir = ctx.cacheDir
    //判断是否有这个文件
    const ctxExist = fileIo.accessSync(ctxCacheDir)
    if (ctxExist) {
      //清除缓存
      fileIo.rmdirSync(ctxCacheDir)
    }
    //获得应用级的上下文
    const app = ctx.getApplicationContext()
    //应用级的缓存目录
    const appCacheDir = app.cacheDir
    //判断有无文件
    const appExist = fileIo.accessSync(appCacheDir)
    if (appExist) {
      //清除缓存
      fileIo.rmdirSync(appCacheDir)
    }
  }
}
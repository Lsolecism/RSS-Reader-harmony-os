import { media } from '@kit.MediaKit'
import { SongItemType } from '../types/type';
import { CacheUtil } from './CacheUtils';

class AvPlayerUtils{
  player:media.AVPlayer|null = null;

  async init(){
    if(!this.player){
      this.player = await media.createAVPlayer()
    }
    this.player.on('stateChange',(state)=>{
        if(state==='initialized'){
          this.player?.prepare()
        }else if (state === 'prepared'){
          this.player?.play()
        }
    })
    this.player.on('durationUpdate',(duration)=>{
      AppStorage.setOrCreate('duration',duration)
    })
    this.player.on('timeUpdate',(time)=>{
      AppStorage.setOrCreate('currentTime',time)
    })
  }

  async singPlay(song: SongItemType) {
    if (!this.player) {
      await this.init()
    }else{
      await this.player!.reset()
      this.player!.url = song.url
    }
  }

  seekPlay(value:number){
    this.player?.seek(value)
  }

  paused(){
    this.player?.pause()
  }
}

export const musicPlayer:AvPlayerUtils= new AvPlayerUtils()
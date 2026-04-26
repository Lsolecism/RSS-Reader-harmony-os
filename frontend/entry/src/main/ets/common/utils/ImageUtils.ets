export class ImageUtils{
  public static getFirstImage(contents:string){
    const match = contents.match(/<img[^>]*\bsrc\s*=\s*(["']?)([^"'\s>]+)\1/i);
    const randomId = Math.floor(Math.random() * 1000);
    return match ? match[2] : `https://picsum.photos/200/300?random=${randomId}`;
  }
}
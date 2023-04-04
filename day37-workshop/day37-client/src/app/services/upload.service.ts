import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, Subject } from 'rxjs';
import { DownloadResults, UploadResults } from '../models/model';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  imgSub = new Subject<Blob>();

  constructor(private httpClient: HttpClient) { }

  // 
  uploadImage(data: FormData) {
    console.info("INSIDE SERVICE");
    console.info("Inside FormData(comment): ", data.get('comment'));
    console.info("Inside FormData(picture): ", data.get('picture'));
    return firstValueFrom(
      this.httpClient.post("/api/post", data));
  }

  uploadS3Image(data: FormData) {
    console.info("Making Post Request");
    console.info("Data: ", data);
    console.info("Data: ", data.get("picture"));
    return firstValueFrom(
      this.httpClient.post("/api/posts3", data));
  }

  // Get image from server by id
  getImage(id: string) {
    const url = "/api/image/" + id;
    // Http Request to server
    firstValueFrom(
      this.httpClient.get<DownloadResults>(url)
    ).then(data => {
      console.info("Sending image data", data);
      this.imgSub.next(data.image);
    });
  }

  // TODO: get image from S3 (Digital ocean)
  getS3Image(id: String) {
    const url = "/api/images3/" + id;
    firstValueFrom(
      this.httpClient.get<DownloadResults>(url)
    ).then(data => {
      console.info("Sending image data", data);
      this.imgSub.next(data.image);
    });
  }

}

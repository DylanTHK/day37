import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, Subject } from 'rxjs';
import { DownloadResults, UploadResults } from '../models/model';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  imgSub = new Subject<any>();

  constructor(private httpClient: HttpClient) { }

  uploadImage(data: FormData) {
    console.info("INSIDE SERVICE");
    console.info("Inside FormData(comment): ", data.get('comment'));
    console.info("Inside FormData(picture): ", data.get('picture'));
    return firstValueFrom(
      this.httpClient.post("/api/post", data));
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

}

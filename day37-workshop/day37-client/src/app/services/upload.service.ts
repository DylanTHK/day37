import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { UploadResults } from '../models/model';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  constructor(private httpClient: HttpClient) { }

  uploadImage(data: FormData) {
    console.info("INSIDE SERVICE");

    console.info("Received comment: ", data.get('comment'));
    console.info("Received file: ", data.get('picture'));

    return firstValueFrom(
      this.httpClient.post("/api/post", data));
  }

}

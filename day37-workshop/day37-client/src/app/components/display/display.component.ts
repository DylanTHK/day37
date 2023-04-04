import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UploadService } from 'src/app/services/upload.service';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css']
})
export class DisplayComponent implements OnInit, OnDestroy{

  sqlForm!: FormGroup;
  s3Form!: FormGroup;

  imgData: any = "";

  imgSub$!: Subscription;

  constructor(private fb: FormBuilder, 
    private uploadSvc:UploadService) { }

  ngOnInit(): void {
    // initialise form
    this.sqlForm = this.fb.group({
      uid: this.fb.control<string>('71e434d5', [Validators.required])
    })
    this.s3Form = this.fb.group({
      uid: this.fb.control<string>('13fafa67', [Validators.required])
    })
    // subscribe to changes in UploadService.imgsub
    this.imgSub$ = this.uploadSvc.imgSub.subscribe(
      data => {
        this.imgData = data;
      }
    );
  }

  getImage() {
    const uid = this.sqlForm.get("uid")?.value;
    this.uploadSvc.getImage(uid);
  }

  getS3Image() {
    const uid = this.s3Form.get("uid")?.value;
    this.uploadSvc.getS3Image(uid);
  }

  ngOnDestroy(): void {
      this.imgSub$.unsubscribe();
  }
}

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

  imgForm!: FormGroup;

  imgData: any = "";

  imgSub$!: Subscription;

  constructor(private fb: FormBuilder, 
    private uploadSvc:UploadService) { }

  ngOnInit(): void {
    // initialise form
    this.imgForm = this.fb.group({
      uid: this.fb.control<string>('71e434d5', [Validators.required])
    })
    // subscribe to changes in UploadService
    this.imgSub$ = this.uploadSvc.imgSub.subscribe(
      data => {
        this.imgData = data;
      }
    );
  }

  ngOnDestroy(): void {
      this.imgSub$.unsubscribe();
  }

  getImage() {
    const uid = this.imgForm.get("uid")?.value;
    this.uploadSvc.getImage(uid);
  }
}

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UploadService } from 'src/app/services/upload.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit{
  
  // File is an image file with various image meta data
  image!: File;
  form!: FormGroup;

  // inject formbuilder, service and router
  constructor(private fb:FormBuilder, 
    private uploadSvc: UploadService,
    private router: Router) { }

  // initialise form
  ngOnInit(): void {
      this.form = this.fb.group({
        // image -> blob, comment -> string
        'image': this.fb.control<Blob | null>(null),
        'comment': this.fb.control<string>('nooblet', Validators.required)
      })
  }

  // UPDATES image (activated when changed detected)
  onImageUpdate(event: any) {
    this.image = event.target.files[0] as File;
    // console.info("Image value: ", this.image);
  }

  // call service to upload with given information
  upload() {
    const comment = this.form.get("comment")?.value;
    const formData = new FormData();
    formData.set("comment", comment);
    formData.set("picture", this.image);

    // Make api call to SB (returns a promise)
    this.uploadSvc.uploadImage(formData)
      // resolve promise
      .then(result => {
        this.router.navigate(['/'])
        console.info("Results from httpCall:", result);
      })
      // reject promise
      .catch(error => {
        console.error(error);
      })

  }

  uploadS3() {
    const comment = this.form.get("comment")?.value;
    // populate form
    const formData = new FormData();
    formData.set("comment", comment);
    formData.set("picture", this.image);

    // Make request from S3 Service
    this.uploadSvc.uploadS3Image(formData)
      // resolve promise
      .then(result => {
        this.router.navigate(['/'])
        console.info("Results from httpCall:", result);
      })
      // reject promise
      .catch(error => {
        console.error(error);
      })
  }

}

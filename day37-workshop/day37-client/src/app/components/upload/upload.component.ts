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

  // change event method to update image when picture uploaded
  onImageUpdate(event: any) {
    this.image = event.target.files[0] as File;
    // console.info("Image value: ", this.image);
  }

  // TODO: call service to upload with given information
  upload() {
    // FIXME: how to extract file from form
    // const imgBlob = this.form.get("image")?.value; // not useable
    // console.info("Image: ", imgBlob);

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

  // FIXME: remove if not used
  // dataUriToBlob(dataUri: string) {
  //   var byteString = atob(dataUri.split(',')[1]);
  //   let mimeString = dataUri.split(',')[0].split(';')[0];
  //   var ab = new ArrayBuffer(byteString.length)
  //   var ia = new Uint8Array(ab)
  //   for(var i =0; i <byteString.length; i++){
  //     ia[i] = byteString.charCodeAt(i);
  //   }
  //   return new Blob([ab], {type: mimeString});
  // }

}

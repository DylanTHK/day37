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
  
  @ViewChild('file')
  imageFile!: ElementRef;

  form!: FormGroup;

  constructor(private fb:FormBuilder, 
    private uploadSvc: UploadService,
    private router: Router) { }

  ngOnInit(): void {
      this.form = this.fb.group({
        'image-file': this.fb.control(''),
        'comment': this.fb.control('nooblet', Validators.required)
      })
  }

  // call service to upload with given information
  upload() {
    // FIXME: test if form.get.value works
    const name = this.form.get("image-file")?.value;
    const comment = this.form.get("comment")?.value;
    const imgBlob = this.imageFile.nativeElement.files[0];
    // const imgBlob = this.dataUriToBlob(this.imageFile.nativeElement.files[0]);

    const formData = new FormData();
    formData.set("comment", comment);
    formData.set("name", name);
    formData.set("file", imgBlob);

    // Make api call to SB
    this.uploadSvc.uploadImage(formData)
      .then(result => {
        this.router.navigate(['/'])
      })
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

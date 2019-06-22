import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Pipe({
    name: 'safeServerImage',
})
export class SafeServerImagePipe implements PipeTransform {
    constructor(private sanitizer: DomSanitizer) {}

    transform(value: any, ...args: any[]) {
        return this.sanitizer.bypassSecurityTrustResourceUrl(value);
    }
}

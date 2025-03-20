import { Component, Input } from '@angular/core';
import { SmallBookComponent } from '../small-book/small-book.component';

@Component({
  selector: 'app-sections',
  imports: [SmallBookComponent],
  templateUrl: './sections.component.html',
  styleUrl: './sections.component.css',
})
export class SectionsComponent {
  @Input() title!: String;
  @Input() books!: any[];
}

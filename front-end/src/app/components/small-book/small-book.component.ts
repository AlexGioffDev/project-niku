import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-small-book',
  imports: [RouterLink],
  templateUrl: './small-book.component.html',
  styleUrl: './small-book.component.css',
})
export class SmallBookComponent {
  @Input() book: any;
}

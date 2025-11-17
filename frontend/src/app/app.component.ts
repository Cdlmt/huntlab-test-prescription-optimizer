import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="container">
      <h1>Optical Prescription Checkout</h1>
      <app-checkout></app-checkout>
    </div>
  `
})
export class AppComponent {
  title = 'Prescription Optimizer';
}


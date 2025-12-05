import { Component } from '@angular/core';
import { CheckoutComponent } from './checkout/checkout.component';

@Component({
  selector: 'app-root',
  standalone: false,
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

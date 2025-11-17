import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, OnDestroy {
  checkoutForm!: FormGroup;
  calculatedTotal: number = 0;
  
  // BUG: Memory leak - Subject is never unsubscribed
  private destroy$ = new Subject<void>();
  
  lensTypes = [
    { value: 'single_vision', label: 'Single Vision', price: 150 },
    { value: 'bifocal', label: 'Bifocal', price: 280 },
    { value: 'progressive', label: 'Progressive', price: 350 }
  ];
  
  discountCodes = [
    { code: 'SUMMER2024', percentage: 10 },
    { code: 'VIP10', percentage: 10 },
    { code: 'WELCOME', percentage: 15 }
  ];
  
  constructor(private fb: FormBuilder) {}
  
  ngOnInit(): void {
    this.checkoutForm = this.fb.group({
      patientName: ['', Validators.required],
      lensType: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      discountCode: ['']
    });
    
    // BUG: Memory leak - valueChanges subscription is never unsubscribed
    this.checkoutForm.valueChanges.subscribe(() => {
      this.calculateTotal();
    });
  }
  
  calculateTotal(): void {
    const lensType = this.checkoutForm.get('lensType')?.value;
    const quantity = this.checkoutForm.get('quantity')?.value;
    const discountCode = this.checkoutForm.get('discountCode')?.value;
    
    const selectedLens = this.lensTypes.find(lens => lens.value === lensType);
    const unitPrice = selectedLens?.price || 0;
    
    let subtotal = unitPrice * quantity;
    
    // BUG: NaN occurs when discountCode is empty string or user clears the field
    // The find() returns undefined, and accessing .percentage on undefined causes issues
    const discount = this.discountCodes.find(d => d.code === discountCode);
    const discountPercentage = discount.percentage; // BUG: No null/undefined check!
    
    // BUG: When discountPercentage is undefined, this calculation produces NaN
    const discountAmount = (subtotal * discountPercentage) / 100;
    this.calculatedTotal = subtotal - discountAmount;
  }
  
  onSubmit(): void {
    if (this.checkoutForm.valid) {
      console.log('Order submitted:', this.checkoutForm.value);
      console.log('Total:', this.calculatedTotal);
      alert('Order submitted successfully!');
      this.checkoutForm.reset();
      this.calculatedTotal = 0;
    }
  }
  
  ngOnDestroy(): void {
    // BUG: destroy$ is defined but never used to unsubscribe from valueChanges
    this.destroy$.next();
    this.destroy$.complete();
  }
}


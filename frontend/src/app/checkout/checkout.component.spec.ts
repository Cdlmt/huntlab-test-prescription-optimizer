import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { CheckoutComponent } from './checkout.component';

describe('CheckoutComponent', () => {
  let component: CheckoutComponent;
  let fixture: ComponentFixture<CheckoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheckoutComponent ],
      imports: [ ReactiveFormsModule ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CheckoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // TODO: Add tests for calculateTotal() to verify NaN bug fix
  // Test cases:
  // 1. Calculate total with valid discount code
  // 2. Calculate total with empty discount code (should not produce NaN)
  // 3. Calculate total with invalid discount code (should not produce NaN)
});


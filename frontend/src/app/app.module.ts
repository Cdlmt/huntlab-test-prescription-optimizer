import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';

import { AppComponent } from './app.component';
import { CheckoutComponent } from './checkout/checkout.component';

@NgModule({
  declarations: [AppComponent, CheckoutComponent],
  imports: [BrowserModule, ReactiveFormsModule],
  providers: [provideHttpClient()],
  bootstrap: [AppComponent],
})
export class AppModule {}

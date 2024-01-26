import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgLetModule } from 'ng-let';
import { MaterialModule } from './shared/material.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { LoginComponent } from './features/auth/components/login/login.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { NavigationComponent } from './core/navigation/navigation.component';
import { HomeComponent } from './features/home/home.component';
import { CreatePostDialogComponent } from './features/post/create-post-dialog.component';
import { ProfileComponent } from './features/profile/profile.component';
import { ToastrModule } from 'ngx-toastr';
import { AuthInterceptor } from './core/interceptors/auth-interceptor.service';
import { GroupsComponent } from './features/groups/groups.component';
import { SearchComponent } from './features/search/search.component';
import { NotificationsComponent } from './features/notifications/notifications.component';
import { GroupViewComponent } from './features/group-view/group-view.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    NavigationComponent,
    HomeComponent,
    CreatePostDialogComponent,
    ProfileComponent,
    GroupsComponent,
    SearchComponent,
    NotificationsComponent,
    GroupViewComponent
  ],
  imports: [
    CommonModule, 
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    NgLetModule,
    MaterialModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule,
    ToastrModule.forRoot({
      preventDuplicates: true,
      positionClass: "toast-bottom-center"
    })
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

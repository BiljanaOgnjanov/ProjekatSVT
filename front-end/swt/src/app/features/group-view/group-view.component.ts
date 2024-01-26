import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { catchError, throwError } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';
import { Post, Reaction, ReactionType } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';
import { SuspendGroupDialog } from './suspend-group-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { CreatePostDialog } from './create-post-dialog.component';
import { EditDialog } from '../home/edit-dialog.component';
import { DeleteDialog } from '../home/delete-dialog.component';
import { CommentDialog } from '../home/comment-dialog.component';
import { EditCommentDialog } from '../home/edit-comment-dialog.component';

interface GroupInfo {
  id: number;
  name: string;
  description: string;
  creationDate: Date;
  userCount: number;
  postCount: number;
  isUserInGroup: boolean;
}

interface User {
  id: number;
  username: string;
  displayName: string;
}

interface GroupRequest {
  id: number;
  createdAt: Date;
  user: User;
}

@Component({
  selector: 'app-group-view',
  templateUrl: './group-view.component.html',
  styleUrls: ['./group-view.component.css']
})
export class GroupViewComponent implements OnInit {

  groupId: string | null = null;
  groupInfo: GroupInfo | null = null;
  groupPosts: Post[] = [];
  groupRequests: GroupRequest[] = [];
  role: string = "";
  username: string = "";
  ReactionType = ReactionType;

  constructor(
    private route: ActivatedRoute, 
    private http: HttpClient, 
    private datePipe: DatePipe, 
    private toastr: ToastrService,
    private authService: AuthService,
    public dialog: MatDialog
    ) { }

  ngOnInit() {
    this.groupId = this.route.snapshot.paramMap.get('id');
    this.getGroupInfo();
    this.getGroupRequests();
    let token = this.authService.token;
    if (token !== null) {
      this.role = JSON.parse(atob(token.split('.')[1])).roles[0];
      this.username = JSON.parse(atob(token.split('.')[1])).sub
    }
  }

  getGroupInfo() {
    this.http.get<any>(`${environment.apiURL}/group/${this.groupId}`).subscribe(resp => {
      this.groupInfo = resp.data
      if (this.groupInfo?.isUserInGroup) {
        this.getGroupPosts();
      }
    });
  }

  getGroupPosts() {
    this.http.get<any>(`${environment.apiURL}/group/${this.groupId}/posts`).subscribe(resp => {
      this.groupPosts = resp.data
    });
  }

  getGroupRequests() {
    this.http.get<any>(`${environment.apiURL}/group/${this.groupId}/requests`).subscribe(resp => {
      this.groupRequests = resp.data
    });
  }

  formatDate(creationTime: Date | undefined) {
    if (!creationTime) return null;
    let currentDateString = new Date(creationTime).toDateString()
    var date = new Date().toDateString()
    return currentDateString === date ? "Today" : this.datePipe.transform(creationTime, 'dd.MM.yyyy.') || '';
  }

  formatTime(creationTime: Date | undefined) {
    if (!creationTime) return null;
    return this.datePipe.transform(creationTime, 'HH:mm') || '';
  }

  approveRequest(requestId: number) {
    this.http.patch(`${environment.apiURL}/group/${this.groupId}/requests/${requestId}`, {"approved": true}).subscribe(() => {
      this.toastr.info("Request approved");
      this.getGroupRequests();
    });
  }

  rejectRequest(requestId: number) {
    this.http.patch(`${environment.apiURL}/group/${this.groupId}/requests/${requestId}`, {"approved": false}).subscribe(() => {
      this.toastr.info("Request rejected");
      this.getGroupRequests();
    });
  }

  onJoin() {
    this.http.post(`${environment.apiURL}/group/${this.groupId}/create-request`, {}).pipe(
      catchError(error => {
        this.toastr.error(error.error.message);
        return throwError(() => error);
      })
    ).subscribe(
      () => {
        this.toastr.success('Request sent successfully');
      }
    );
  }

  openSuspendDialog() {

    const dialogRef = this.dialog.open(SuspendGroupDialog, {
      data: {suspendReason: ""}
    });

    dialogRef.afterClosed().pipe(
      catchError(error => {
        console.log(error);
        this.toastr.error(error.error.message);
        return throwError(() => error);
      })
    ).subscribe(result => {
      if (result) {
        this.http.patch<any>(`${environment.apiURL}/group/${this.groupId}/suspend`, {"suspendedReason": result}).subscribe((data) => {
          this.toastr.success(data.message);
        })
      }
    });
  }

  openCreatePostDialog() {

    const dialogRef = this.dialog.open(CreatePostDialog, {
      data: {content: ""}
    });

    dialogRef.afterClosed().pipe(
      catchError(error => {
        console.log(error);
        this.toastr.error(error.error.message);
        return throwError(() => error);
      })
    ).subscribe(result => {
      if (result) {
        this.http.post<any>(`${environment.apiURL}/group/${this.groupId}/create-post`, {"content": result}).subscribe((data) => {
          this.toastr.success(data.message);
          this.getGroupPosts();
          if (this.groupInfo) {
            this.groupInfo.postCount++;
          }
        })
      }
    });
  }

  getLikeType(): ReactionType {
    return ReactionType.LIKE;
  }

  getDislikeType(): ReactionType {
    return ReactionType.DISLIKE;
  }

  getHeartType(): ReactionType {
    return ReactionType.HEART;
  }

  getReaction(reactions: Reaction[], type: ReactionType): number {
    return reactions.reduce((result, next) =>
      result + (next.type === type ? 1 : 0), 0
    );
  }

  onReact(postId: number, reactionType: ReactionType) {
    const post = this.groupPosts.find(post => post.id === postId);
    const reaction = post?.reactions.find(reaction => reaction.user.username === this.username);

    if (reaction?.type !== reactionType) {
      this.http.put<any>(`${environment.apiURL}/post/${postId}/react`, {"reaction": reactionType}).subscribe(() => {
        this.getGroupPosts();
      });
    return;
    }

    this.http.put<any>(`${environment.apiURL}/post/${postId}/react`, {"reaction": null}).subscribe(() => {
      this.getGroupPosts();
    });
  }

  onCommentReact(postId: number, commentId: number, reactionType: ReactionType) {
    const post = this.groupPosts.find(post => post.id === postId);
    const comment = post?.comments.find(comment => comment.id === commentId);
    const reaction = comment?.reactions.find(reaction => reaction.user.username === this.username);

    if (reaction?.type !== reactionType) {
      this.http.put<any>(`${environment.apiURL}/comment/${commentId}/react`, {"reaction": reactionType}).subscribe(() => {
        this.getGroupPosts();
      });
    return;
    }

    this.http.put<any>(`${environment.apiURL}/comment/${commentId}/react`, {"reaction": null}).subscribe(() => {
      this.getGroupPosts();
    });
  }

  isReacted(postId: number, reactionType: ReactionType) {
    const post = this.groupPosts.find(post => post.id === postId);
    const reaction = post?.reactions.find(reaction => reaction.user.username === this.username);
  
    return reaction?.type === reactionType;
  }

  isReactedComment(postId: number, commentId: number, reactionType: ReactionType) {
    const post = this.groupPosts.find(post => post.id === postId);
    const comment = post?.comments.find(comment => comment.id === commentId);
    const reaction = comment?.reactions.find(reaction => reaction.user.username === this.username);
  
    return reaction?.type === reactionType;
  }

  ifOptions(postId: number) {
    const post = this.groupPosts.find(post => post.id === postId);

    return post?.user.username === this.username
  }

  ifCommentOptions(postId:number, commentId: number) {
    const post = this.groupPosts.find(post => post.id === postId);
    const comment = post?.comments.find(comment => comment.id === commentId);
    
    return comment?.user.username === this.username;
  }

  openEditDialog(postId: number) {
    const post = this.groupPosts.find(post => post.id === postId);

    const dialogRef = this.dialog.open(EditDialog, {
      data: {content: post?.content},
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.patch<any>(`${environment.apiURL}/post/${postId}`, {"content": result}).subscribe((data) => {
          this.toastr.success(data.message);
          this.getGroupPosts();
        })
      }
    });
  }

  openDeleteDialog(postId: number): void {
    const dialogRef = this.dialog.open(DeleteDialog, {
      width: '250px',
    });

    dialogRef.componentInstance.deleteConfirmed.subscribe(() => {
      this.http.delete<any>(`${environment.apiURL}/post/${postId}`).subscribe((data) => {
        this.toastr.success(data.message);
        this.getGroupPosts();
      });
    });
  }

  getRepliesCount(postId: number) {
    const post = this.groupPosts.find(post => post.id === postId);

    return post?.comments.length
  }

  openCommentDialog(postId: number) {
    const dialogRef = this.dialog.open(CommentDialog, {
      data: {text: ""},
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.post<any>(`${environment.apiURL}/post/${postId}/comment`, {"text": result}).subscribe((data) => {
          this.toastr.success(data.message);
          this.getGroupPosts();
        })
      }
    });
  }

  toggleComment(postId: number) {
    const post = this.groupPosts.find(post => post.id === postId);
    if (post) {
      post.commentsExpanded = !post.commentsExpanded;
    }
  }

  openEditCommentDialog(postId: number, commentId: number) {
    const post = this.groupPosts.find(post => post.id === postId);
    const comment = post?.comments.find(comment => comment.id === commentId)
    console.log(comment)
    const dialogRef = this.dialog.open(EditCommentDialog, {
      data: {text: comment?.text},
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.patch<any>(`${environment.apiURL}/comment/${commentId}`, {"text": result}).subscribe((data) => {
          this.toastr.success(data.message);
          this.getGroupPosts();
        })
      }
    });
  }

  openDeleteCommentDialog(commentId: number): void {
    const dialogRef = this.dialog.open(DeleteDialog, {
      width: '250px',
    });

    dialogRef.componentInstance.deleteConfirmed.subscribe(() => {
      this.http.delete<any>(`${environment.apiURL}/comment/${commentId}`).subscribe((data) => {
        this.toastr.success(data.message);
        this.getGroupPosts();
      });
    });
  }
}

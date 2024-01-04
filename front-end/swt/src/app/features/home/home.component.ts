import { DatePipe } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { AuthService } from "src/app/core/services/auth.service";
import { environment } from "src/environments/environment";
import { EditDialog } from "./edit-dialog.component";
import { ToastrService } from "ngx-toastr";
import { DeleteDialog } from "./delete-dialog.component";

enum ReactionType {
  LIKE = "LIKE",
  DISLIKE = "DISLIKE",
  HEART = "HEART"
}

interface User {
  username: string;
  displayName?: string;
}

interface Comment {
  id: number;
  text: string;
  timestamp: Date;
}

interface Reaction {
  id: number;
  username: string;
  type: ReactionType;
  timestamp: Date;
}

interface Post {
  id: number;
  content: string;
  creationTime: Date;
  user: User;
  comments: Comment[];
  reactions: Reaction[];
}

export interface EditDialogData {
  content: string;
}

@Component({
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  
  posts!: Post[];
  username: string = "";
  ReactionType = ReactionType;
  
  ngOnInit(): void {
    this.http.get<any>(`${environment.apiURL}/post`).subscribe(resp => {
      this.posts = resp.data
      let token = this.authService.token;
      if (token !== null) {
        this.username = JSON.parse(atob(token.split('.')[1])).sub
      }
    });
  }
  

  updatePosts(): void {
    this.http.get<any>(`${environment.apiURL}/post`).subscribe(resp => {
      this.posts = resp.data
    });
  }

  constructor(
    private http: HttpClient, 
    private datePipe: DatePipe, 
    private authService: AuthService, 
    public dialog: MatDialog, 
    private toastr: ToastrService
    ) { }

  formatDate(creationTime: Date) {
    let currentDateString = new Date(creationTime).toDateString()
    var date = new Date().toDateString()
    return currentDateString === date ? "Today" : this.datePipe.transform(creationTime, 'dd.MM.yyyy.') || '';
  }

  formatTime(creationTime: Date) {
    return this.datePipe.transform(creationTime, 'HH:mm') || '';
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
    const post = this.posts.find(post => post.id === postId);
    const reaction = post?.reactions.find(reaction => reaction.username === this.username);

    if (reaction?.type !== reactionType) {
      this.http.put<any>(`${environment.apiURL}/post/${postId}/react`, {"reaction": reactionType}).subscribe(() => {
        this.updatePosts();
      });
    return;
    }

    this.http.put<any>(`${environment.apiURL}/post/${postId}/react`, {"reaction": null}).subscribe(() => {
      this.updatePosts();
    });
}

  isReacted(postId: number, reactionType: ReactionType) {
    const post = this.posts.find(post => post.id === postId);
    const reaction = post?.reactions.find(reaction => reaction.username === this.username);
  
    return reaction?.type === reactionType;
  }

  ifOptions(postId: number) {
    const post = this.posts.find(post => post.id === postId);

    return post?.user.username === this.username
  }

  openEditDialog(postId: number) {
    const post = this.posts.find(post => post.id === postId);

    const dialogRef = this.dialog.open(EditDialog, {
      data: {content: post?.content},
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.patch<any>(`${environment.apiURL}/post/${postId}`, {"content": result}).subscribe((data) => {
          this.toastr.success(data.message);
          this.updatePosts();
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
        this.updatePosts();
      });
    });
  }
}
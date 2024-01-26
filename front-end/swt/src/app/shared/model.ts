export enum ReactionType {
  LIKE = "LIKE",
  DISLIKE = "DISLIKE",
  HEART = "HEART"
}

export interface User {
  username: string;
  displayName?: string;
}

export interface Comment {
  id: number;
  user: User;
  text: string;
  timestamp: Date;
  reactions: Reaction[];
}

export interface Reaction {
  id: number;
  user: User;
  type: ReactionType;
  timestamp: Date;
}

export interface Post {
  id: number;
  content: string;
  creationTime: Date;
  user: User;
  comments: Comment[];
  reactions: Reaction[];
  commentsExpanded: boolean;
}

export interface EditDialogData {
  content: string;
}

export interface EditCommentDialogData {
  text: string;
}

export interface CommentDialogData {
  text: string;
}

export interface SuspendGroupDialogData {
  suspendReason: string;
}

export interface CreatePostDialogData {
  content: string;
}
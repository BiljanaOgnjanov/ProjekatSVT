import { BehaviorSubject, catchError, EMPTY, Observable } from 'rxjs';

export abstract class GenericDataService<DataType> {
  protected dataSubject: BehaviorSubject<DataType | null> =
    new BehaviorSubject<DataType | null>(null);
  public data$: Observable<DataType | null> = this.dataSubject.asObservable();

  protected errorSubject: BehaviorSubject<string | null> = new BehaviorSubject<
    string | null
  >(null);
  public error$: Observable<string | null> = this.errorSubject.asObservable();

  set setData(data: DataType | null) {
    this.dataSubject.next(data);
  }
  clearData(): void {
    this.dataSubject.next(null);
  }

  set setError(error: string | null) {
    this.errorSubject.next(error);
  }
  clearError(): void {
    this.errorSubject.next(null);
  }

  resetData(): void {
    this.clearData();
    this.clearError();
  }

  protected addErrorHandler(obs: Observable<any>) {
    return obs.pipe(
      catchError((res) => {
        const error = res.error;
        if (error && error.message) {
          this.setError = error.message;
        } else if (res.message) {
          this.setError = res.message;
        }
        return EMPTY;
      })
    );
  }

}
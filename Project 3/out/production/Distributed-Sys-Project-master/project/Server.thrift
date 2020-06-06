namespace java project

enum Operation {
	GET,
	PUT,
	DELETE
}

enum Type {
    Client,
    Server
}

enum Status {
    Commit,
    Abort
}

enum Result {
    Fail,
    Success
}

struct Message {
  1: string key;
  2: string value;
  3: Operation op;
  4: Type type;
  5: Status status = Status.Commit;
  6: Result result = Result.Success;
}

service MultiplicationService
{
        Message process(1:Message message),
        Message canCommit(1:Message message),
        Message doCommit(1:Message message),
        Message doAbort(1:Message message)
}
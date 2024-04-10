package noronhagcp.com.passin.domain.attendee.exceptions;

public class AttendeeAlreadySubscribedException extends RuntimeException {
  public AttendeeAlreadySubscribedException(String message) {
    super(message);
  }
}

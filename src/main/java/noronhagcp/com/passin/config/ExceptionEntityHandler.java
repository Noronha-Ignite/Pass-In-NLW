package noronhagcp.com.passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import noronhagcp.com.passin.domain.attendee.exceptions.AttendeeAlreadySubscribedException;
import noronhagcp.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import noronhagcp.com.passin.domain.checkIn.exceptions.CheckInAlreadyExistsException;
import noronhagcp.com.passin.domain.event.exceptions.EventFullException;
import noronhagcp.com.passin.domain.event.exceptions.EventNotFoundException;
import noronhagcp.com.passin.dto.common.ExceptionResponseDTO;

@ControllerAdvice
public class ExceptionEntityHandler {

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<Object> handleEventNotFound(EventNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AttendeeNotFoundException.class)
  public ResponseEntity<Object> handleAttendeeNotFound(AttendeeNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AttendeeAlreadySubscribedException.class)
  public ResponseEntity<Object> handleAttendeeSubscribed(AttendeeAlreadySubscribedException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(CheckInAlreadyExistsException.class)
  public ResponseEntity<Object> handleCheckInAlreadyExists(CheckInAlreadyExistsException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(EventFullException.class)
  public ResponseEntity<ExceptionResponseDTO> handleEventFull(EventFullException exception) {
    return ResponseEntity.badRequest().body(new ExceptionResponseDTO(exception.getMessage()));
  }
}

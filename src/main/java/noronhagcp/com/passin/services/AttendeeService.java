package noronhagcp.com.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import noronhagcp.com.passin.domain.attendee.Attendee;
import noronhagcp.com.passin.domain.attendee.exceptions.AttendeeAlreadySubscribedException;
import noronhagcp.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import noronhagcp.com.passin.domain.checkIn.CheckIn;
import noronhagcp.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import noronhagcp.com.passin.dto.attendee.AttendeeDetails;
import noronhagcp.com.passin.dto.attendee.AttendeeListResponseDTO;
import noronhagcp.com.passin.dto.attendee.AttendeeBadgeDTO;
import noronhagcp.com.passin.repositories.AttendeeRepository;

@Service
@RequiredArgsConstructor
public class AttendeeService {
  private final CheckInService checkInService;

  private final AttendeeRepository attendeeRepository;

  public Attendee saveAttendee(Attendee newAttendee) {
    this.attendeeRepository.save(newAttendee);

    return newAttendee;
  }

  public List<Attendee> getAllAttendeesFromEvent(String eventId) {
    List<Attendee> attendees = this.attendeeRepository.findByEventId(eventId);

    return attendees;
  }

  public Integer countAttendeesFromEvent(String eventId) {
    Integer attendeeListCount = this.attendeeRepository.countByEventId(eventId);

    return attendeeListCount;
  }

  public AttendeeListResponseDTO getEventAttendees(String eventId) {
    List<Attendee> attendees = this.getAllAttendeesFromEvent(eventId);

    List<AttendeeDetails> attendeeDetails = attendees.stream()
      .map(attendee -> {
        Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());

        LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;

        return new AttendeeDetails(
          attendee.getId(),
          attendee.getName(),
          attendee.getEmail(),
          attendee.getCreatedAt(),
          checkedInAt
        );
      }).toList();

    return new AttendeeListResponseDTO(attendeeDetails);
  }

  public void verifyAttendeeSubscription(String email, String eventId) {
    Optional<Attendee> attendeeSubscribed = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

    if (attendeeSubscribed.isPresent()) {
      throw new AttendeeAlreadySubscribedException("Attendee already subscribed");
    }
  }

  public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    Attendee attendee = this.getAttendeeById(attendeeId);

    String uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

    AttendeeBadgeDTO badge = new AttendeeBadgeDTO(
      attendee.getName(),
      attendee.getEmail(),
      uri,
      attendee.getEvent().getId()
    );

    return new AttendeeBadgeResponseDTO(badge);
  }

  public void checkIn(String attendeeId) {
    Attendee attendee = this.getAttendeeById(attendeeId);

    this.checkInService.saveCheckIn(attendee);
  }

  private Attendee getAttendeeById(String attendeeId) {
    Attendee attendee = this.attendeeRepository.findById(attendeeId).orElseThrow(() -> 
      new AttendeeNotFoundException("Attendee not found with id " + attendeeId)
    );

    return attendee;
  }
}

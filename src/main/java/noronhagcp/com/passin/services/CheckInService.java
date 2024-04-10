package noronhagcp.com.passin.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import noronhagcp.com.passin.domain.attendee.Attendee;
import noronhagcp.com.passin.domain.checkIn.CheckIn;
import noronhagcp.com.passin.domain.checkIn.exceptions.CheckInAlreadyExistsException;
import noronhagcp.com.passin.repositories.CheckInRepository;

@Service
@RequiredArgsConstructor
public class CheckInService {
  private final CheckInRepository checkInRepository;

  public void saveCheckIn(Attendee attendee) {
    this.verifyCheckInExists(attendee.getId());

    CheckIn newCheckIn = new CheckIn();
    newCheckIn.setAttendee(attendee);
    newCheckIn.setCreatedAt(LocalDateTime.now());

    this.checkInRepository.save(newCheckIn);
  }

  public Optional<CheckIn> getCheckIn(String attendeeId) {
    return this.checkInRepository.findByAttendeeId(attendeeId);
  }

  private void verifyCheckInExists(String attendeeId) {
    Optional<CheckIn> checkOptional = this.getCheckIn(attendeeId);

    if (checkOptional.isPresent()) {
      throw new CheckInAlreadyExistsException("Attendee already checked in");
    }
  }
}

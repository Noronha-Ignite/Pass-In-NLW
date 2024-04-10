package noronhagcp.com.passin.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import noronhagcp.com.passin.domain.checkIn.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {
  Optional<CheckIn> findByAttendeeId(String attendeeId);
}

package noronhagcp.com.passin.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import noronhagcp.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import noronhagcp.com.passin.services.AttendeeService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {
  private final AttendeeService attendeeService;
  
  @GetMapping("/{id}/badge")
  public ResponseEntity<AttendeeBadgeResponseDTO> getBadge(@PathVariable String id, UriComponentsBuilder uriComponentsBuilder) {
    AttendeeBadgeResponseDTO badge = attendeeService.getAttendeeBadge(id, uriComponentsBuilder);

    return ResponseEntity.ok(badge);
  }

  @PostMapping("/{attendeeId}/check-in")
  public ResponseEntity<Object> checkAttendeeIn(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    this.attendeeService.checkIn(attendeeId);

    URI uri = uriComponentsBuilder.path("/attendees/{id}/badge").buildAndExpand(attendeeId).toUri();

    return ResponseEntity.created(uri).build();
  }
  
}

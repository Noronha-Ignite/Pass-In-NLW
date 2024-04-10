package noronhagcp.com.passin.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import noronhagcp.com.passin.dto.attendee.AttendeeIdDTO;
import noronhagcp.com.passin.dto.attendee.AttendeeListResponseDTO;
import noronhagcp.com.passin.dto.attendee.AttendeeRequestDTO;
import noronhagcp.com.passin.dto.event.EventIdDTO;
import noronhagcp.com.passin.dto.event.EventRequestDTO;
import noronhagcp.com.passin.dto.event.EventResponseDTO;
import noronhagcp.com.passin.services.AttendeeService;
import noronhagcp.com.passin.services.EventService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
  private final EventService eventService;
  private final AttendeeService attendeeService;
  
  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
    EventResponseDTO event = this.eventService.getEventDetail(id);

    return ResponseEntity.ok().body(event);
  }

  @PostMapping
  public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO eventRequest, UriComponentsBuilder uriComponentsBuilder) {
      EventIdDTO eventIdDTO = this.eventService.createEvent(eventRequest);

      URI uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();
      
      return ResponseEntity.created(uri).body(eventIdDTO);
  }
  
  @GetMapping("/attendees/{id}")
  public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String id) {
    AttendeeListResponseDTO attendees = this.attendeeService.getEventAttendees(id);

    return ResponseEntity.ok().body(attendees);
  }

  @PostMapping("/{eventId}/attendees")
  public ResponseEntity<AttendeeIdDTO> subscribeAttendee(@PathVariable String eventId, @RequestBody AttendeeRequestDTO attendeeRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
      AttendeeIdDTO attendeeIdDTO = this.eventService.subscribeAttendeeToEvent(eventId, attendeeRequestDTO);

      URI uri = uriComponentsBuilder.path("/attendees/{id}/badge").buildAndExpand(attendeeIdDTO.id()).toUri();
      
      return ResponseEntity.created(uri).body(attendeeIdDTO);
  }
}

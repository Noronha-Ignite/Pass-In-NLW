package noronhagcp.com.passin.services;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import noronhagcp.com.passin.domain.attendee.Attendee;
import noronhagcp.com.passin.domain.event.Event;
import noronhagcp.com.passin.domain.event.exceptions.EventFullException;
import noronhagcp.com.passin.domain.event.exceptions.EventNotFoundException;
import noronhagcp.com.passin.dto.attendee.AttendeeIdDTO;
import noronhagcp.com.passin.dto.attendee.AttendeeRequestDTO;
import noronhagcp.com.passin.dto.event.EventIdDTO;
import noronhagcp.com.passin.dto.event.EventRequestDTO;
import noronhagcp.com.passin.dto.event.EventResponseDTO;
import noronhagcp.com.passin.repositories.EventRepository;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;
  
  private final AttendeeService attendeeService;

  public EventResponseDTO getEventDetail(String eventId) {
    Event event = this.getEventById(eventId);
    Integer attendeeListCount = this.attendeeService.countAttendeesFromEvent(eventId);

    return new EventResponseDTO(event, attendeeListCount);
  }

  public EventIdDTO createEvent(EventRequestDTO eventDTO) {
    Event newEvent = new Event();

    newEvent.setTitle(eventDTO.title());
    newEvent.setDetails(eventDTO.details());
    newEvent.setMaximumAttendees(eventDTO.maximumAttendees());

    newEvent.setSlug(this.generateSlug(newEvent.getTitle()));

    this.eventRepository.save(newEvent);

    return new EventIdDTO(newEvent.getId());
  }

  public AttendeeIdDTO subscribeAttendeeToEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO) {
    this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);

    Event event = this.getEventById(eventId);
    List<Attendee> attendees = this.attendeeService.getAllAttendeesFromEvent(eventId);

    if (event.getMaximumAttendees() <= attendees.size()) {
      throw new EventFullException("Event is full");
    }

    Attendee newAttendee = new Attendee();

    newAttendee.setName(attendeeRequestDTO.name());
    newAttendee.setEmail(attendeeRequestDTO.email());
    newAttendee.setEvent(event);
    newAttendee.setCreatedAt(LocalDateTime.now());

    this.attendeeService.saveAttendee(newAttendee);

    return new AttendeeIdDTO(newAttendee.getId());
  }

  private Event getEventById(String id) {
    Event event = this.eventRepository.findById(id).orElseThrow(() -> 
      new EventNotFoundException("Event not found with ID: " + id)
    );

    return event;
  }

  private String generateSlug(String text) {
    String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

    return normalized
      .replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
      .replaceAll("[^\\w\\s]", "")
      .replace("[\\s+]", "-")
      .toLowerCase();
  }
}
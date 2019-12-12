package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcEventDao implements EventDao {
	
	private JdbcTemplate jdbc;
	
	public JdbcEventDao(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Event> getEventsForUser(long userId) {
		String sqlQuery = "SELECT event.event_id, event.menu_id, event.event_name, event.event_date, event.event_time, event.description, event.deadline, event.address_id "
						+ "FROM event "
						+ "JOIN event_attendees USING (event_id) "
						+ "WHERE event_attendees.user_id = ?";
		
		SqlRowSet eventResults = jdbc.queryForRowSet(sqlQuery, userId);
		
		List<Event> eventListForUser = new ArrayList<Event>();
		
		while(eventResults.next()) {
			eventListForUser.add(mapRowToEvent(eventResults));
		}
		
		return eventListForUser;
	}

	@Override
	public Event createEvent(Event event) {
		String sqlQuery = "INSERT INTO event (event_id, menu_id, event_name, event_date, event_time, description, deadline, address_id) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		jdbc.update(sqlQuery,
				event.getEventId(),
				event.getMenuId(),
				event.getName(),
				event.getDate(),
				event.getTime(),
				event.getDescription(),
				event.getDeadline(),
				event.getAddressId());
		
		return event;
	}

	@Override
	public void deleteEvent(long id) {
		String sqlString = "DELETE FROM event WHERE event_id = ?";
		
		jdbc.update(sqlString, id);
	}

	@Override
	public List<EventAttendees> getEventAttendees(long id) {
		String sqlString = 	"SELECT event_id, user_id, is_host, is_attending, first_name, last_name, adult_guests, child_guests "
							+ "FROM event_attendees "
							+ "WHERE event_id = ?";
		
		SqlRowSet attendeeResults = jdbc.queryForRowSet(sqlString, id);
		
		List<EventAttendees> listOfAttendees = new ArrayList<EventAttendees>();
		
		while(attendeeResults.next()) {
			listOfAttendees.add(mapRowToEventAttendees(attendeeResults));
		}
		
		return listOfAttendees;
	}

	@Override
	public EventAttendees addEventAttendee(long id, EventAttendees attendees) {
		String sqlString = "INSERT INTO event_attendees(event_id, user_id, is_host, is_attending, first_name, last_name, adult_guests, child_guests) "
						 + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		
		jdbc.update(sqlString, id, attendees.getUserId(), attendees.isHost(), attendees.isAttending(), attendees.getFirstName(), attendees.getLastName(), attendees.getAdultGuests(), attendees.getChildGuests());
		
		return attendees;
	}

	@Override
	public Event updateEvent(long id, Event event) {
		String sqlString = "UPDATE event SET "
						 + "menu_id = ?, "
						 + "event_name = ?, "
						 + "event_date = ?, "
						 + "event_time = ?, "
						 + "description = ?, "
						 + "deadline = ?, "
						 + "address_id = ? "
						 + "WHERE event_id = ?";
		
		jdbc.update(sqlString, event.getMenuId(), event.getName(), event.getDate(), event.getTime(), event.getDescription(), event.getDeadline(), event.getDescription(), event.getAddressId(), event.getEventId());
		
		return event;
	}

	@Override
	public Event getEventDetails(long id) {
		String sqlString = "SELECT event.event_id, event.menu_id, event.event_name, event.event_date, event.event_time, event.description, event.deadline, event.address_id "
						 + "FROM event WHERE event_id = ?";
		
		SqlRowSet results = jdbc.queryForRowSet(sqlString, id);
		
		Event event = null;
		
		if( results.next() ) {
			event = mapRowToEvent(results);
		}
		
		return event;
	}
	
	@Override
	public Address getAddress(long addressID) {
		String sqlString = "SELECT address_id, street_address, city, state, zip FROM address WHERE address_id = ?";
		
		SqlRowSet results = jdbc.queryForRowSet(sqlString, addressID);
		
		Address address = null;
		
		if( results.next() ) {
			address = mapRowToAddress(results);
		}
		
		return address;
	}
	
	private Event mapRowToEvent(SqlRowSet row) {
		Event event = new Event();
		
		event.setEventId(row.getLong("event_id"));
		event.setMenuId(row.getLong("menu_id"));
		event.setName(row.getString("event_name"));
		event.setDate(row.getDate("event_date").toLocalDate());
		event.setTime(row.getString("event_time"));
		event.setDescription(row.getString("description"));
		event.setDeadline(row.getDate("deadline").toLocalDate());
		
		return event;
	}
	
	private EventAttendees mapRowToEventAttendees(SqlRowSet row) {
		EventAttendees eventAttendees = new EventAttendees();
		
		eventAttendees.setEventId(row.getLong("event_id"));
		eventAttendees.setUserId(row.getLong("user_id"));
		eventAttendees.setHost(row.getBoolean("is_host"));
		eventAttendees.setAttending(row.getBoolean("is_attending"));
		eventAttendees.setFirstName(row.getString("first_name"));
		eventAttendees.setLastName(row.getString("last_name"));
		eventAttendees.setAdultGuests(row.getInt("adult_guests"));
		eventAttendees.setChildGuests(row.getInt("child_guests"));
		
		return eventAttendees;
	}
	
	private Address mapRowToAddress(SqlRowSet row) {
		Address address = new Address();
		
		address.setAddressId(row.getLong("address_id"));
		address.setStreetAddress(row.getString("street_address"));
		address.setCity(row.getString("city"));
		address.setState(row.getString("state"));
		address.setZip(row.getString("zip"));
		
		return address;
	}
}

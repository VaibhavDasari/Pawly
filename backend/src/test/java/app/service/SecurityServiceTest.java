package app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import app.dto.LoginRequest;
import app.model.User;
import app.repository.AppointmentRepository;
import app.repository.HealthRecordRepository;
import app.repository.PetRepository;
import app.repository.UserRepository;
import app.security.JwtService;

class SecurityServiceTest {
    @Test void petReadRejectsAnotherOwnersPet() {
        User owner = new User("Owner", "owner@example.test", "hash");
        UserRepository users = mock(UserRepository.class);
        UserService userService = mock(UserService.class);
        PetRepository pets = mock(PetRepository.class);
        when(userService.getUserByEmail("owner@example.test")).thenReturn(owner);
        when(pets.findByIdAndOwner(42L, owner)).thenReturn(Optional.empty());
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> new PetService(pets, userService).getPet("owner@example.test", 42L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(pets, never()).save(any());
    }

    @Test void appointmentReadRejectsAnotherOwnersAppointment() {
        User owner = new User("Owner", "owner@example.test", "hash");
        UserService userService = mock(UserService.class);
        AppointmentRepository appointments = mock(AppointmentRepository.class);
        when(userService.getUserByEmail("owner@example.test")).thenReturn(owner);
        when(appointments.findByIdAndPetOwner(42L, owner)).thenReturn(Optional.empty());
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> new AppointmentService(appointments, mock(PetRepository.class), userService).getAppointment("owner@example.test", 42L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(appointments, never()).save(any());
    }

    @Test void healthRecordReadRejectsAnotherOwnersRecord() {
        User owner = new User("Owner", "owner@example.test", "hash");
        UserService userService = mock(UserService.class);
        HealthRecordRepository records = mock(HealthRecordRepository.class);
        when(userService.getUserByEmail("owner@example.test")).thenReturn(owner);
        when(records.findByIdAndPetOwner(42L, owner)).thenReturn(Optional.empty());
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> new HealthRecordService(records, mock(PetRepository.class), userService).getHealthRecord("owner@example.test", 42L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(records, never()).save(any());
    }

    @Test void failedLoginUsesSameErrorForUnknownAndWrongPassword() {
        UserRepository users = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        JwtService jwt = mock(JwtService.class);
        User existing = new User("Known", "known@example.test", "stored-hash");
        when(users.findByEmail("known@example.test")).thenReturn(Optional.of(existing));
        when(users.findByEmail("missing@example.test")).thenReturn(Optional.empty());
        when(encoder.matches(anyString(), anyString())).thenReturn(false);
        UserService service = new UserService(users, encoder, jwt);
        LoginRequest known = new LoginRequest(); known.setEmail("known@example.test"); known.setPassword("bad-password");
        LoginRequest missing = new LoginRequest(); missing.setEmail("missing@example.test"); missing.setPassword("bad-password");
        BadCredentialsException first = assertThrows(BadCredentialsException.class, () -> service.login(known));
        BadCredentialsException second = assertThrows(BadCredentialsException.class, () -> service.login(missing));
        assertEquals(first.getMessage(), second.getMessage());
    }
}



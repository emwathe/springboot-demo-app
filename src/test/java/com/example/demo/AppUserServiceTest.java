package com.example.demo;

import com.example.demo.entity.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    public AppUserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        AppUser user1 = new AppUser(); user1.setId(1L); user1.setName("Alice"); user1.setEmail("alice@example.com");
        AppUser user2 = new AppUser(); user2.setId(2L); user2.setName("Bob"); user2.setEmail("bob@example.com");
        when(appUserRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<AppUser> users = appUserService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testGetUserById() {
        AppUser user = new AppUser(); user.setId(1L); user.setName("Alice");
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<AppUser> found = appUserService.getUserById(1L);
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    void testSaveUser() {
        AppUser user = new AppUser(); user.setName("Alice");
        when(appUserRepository.save(user)).thenReturn(user);
        AppUser saved = appUserService.saveUser(user);
        assertEquals("Alice", saved.getName());
    }

    @Test
    void testDeleteUser() {
        appUserService.deleteUser(1L);
        verify(appUserRepository, times(1)).deleteById(1L);
    }
}

package com.aryan.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.aryan.inventory.dto.UpdateRoleRequest;
import com.aryan.inventory.dto.UserResponse;
import com.aryan.inventory.entity.Role;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.exception.CannotDeleteCurrentUserException;
import com.aryan.inventory.exception.CannotModifyOwnRoleException;
import com.aryan.inventory.exception.InternalServerException;
import com.aryan.inventory.exception.UserNotFoundException;
import com.aryan.inventory.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@AfterEach
	void clearSecurity() {
	    SecurityContextHolder.clearContext();
	}
	
	@Test
	void shouldGetAllUsers() {

	    when(userRepository.findAll())
	            .thenReturn(List.of(createUser(1L, "admin", Role.ADMIN)));

	    List<UserResponse> response = userService.getAllUsers();

	    assertEquals(1, response.size());
	    assertEquals("admin", response.get(0).getUsername());

	    verify(userRepository).findAll();
	}
	
	@Test
	void shouldGetUserById() {

	    when(userRepository.findById(1L))
	            .thenReturn(Optional.of(createUser(1L, "admin", Role.ADMIN)));

	    UserResponse response = userService.getUserById(1L);

	    assertEquals(1L, response.getId());
	    assertEquals("admin", response.getUsername());
	    assertEquals(Role.ADMIN, response.getRole());

	    verify(userRepository).findById(1L);
	}
	
	
	@Test
	void shouldThrowUserNotFoundWhenGettingUser() {

	    when(userRepository.findById(1L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            UserNotFoundException.class,
	            () -> userService.getUserById(1L)
	    );

	    verify(userRepository).findById(1L);
	}
	
	@Test
	void shouldUpdateUserRole() {

	    User user = createUser(2L, "user", Role.EMPLOYEE);

	    UpdateRoleRequest request = new UpdateRoleRequest(Role.MANAGER);

	    when(userRepository.findById(1L))
	            .thenReturn(Optional.of(user));

	    when(userRepository.save(any(User.class)))
	            .thenReturn(user);

	    mockAuthenticatedUser("admin");

	    UserResponse response =
	            userService.updateUserRole(1L, request);

	    assertEquals(Role.MANAGER, response.getRole());

	    verify(userRepository).save(any(User.class));
	}
	
	@Test
	void shouldNotUpdateIfRoleAlreadySame() {

		User user = createUser(2L, "user", Role.EMPLOYEE);

	    UpdateRoleRequest request = new UpdateRoleRequest(Role.EMPLOYEE);

	    when(userRepository.findById(1L))
	            .thenReturn(Optional.of(user));

	    mockAuthenticatedUser("admin");

	    UserResponse response =
	            userService.updateUserRole(1L, request);

	    assertEquals(Role.EMPLOYEE, response.getRole());

	    verify(userRepository, never()).save(any());
	}
	
	@Test
	void shouldNotDemoteLastAdmin() {

	    User admin = createUser(2L, "admin", Role.ADMIN);

	    UpdateRoleRequest request = new UpdateRoleRequest(Role.MANAGER);

	    when(userRepository.findById(1L))
	            .thenReturn(Optional.of(admin));

	    when(userRepository.countByRole(Role.ADMIN))
	            .thenReturn(1L);

	    mockAuthenticatedUser("admin");

	    assertThrows(
	            CannotModifyOwnRoleException.class,
	            () -> userService.updateUserRole(1L, request)
	    );

	    verify(userRepository, never()).save(any());
	}


    
    
    @Test
    void shouldDeleteUser() {

    	User user = createUser(1L, "employee", Role.EMPLOYEE);
    	
    	mockAuthenticatedUser("admin");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.countByRole(Role.ADMIN))
                .thenReturn(2L);

        

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }
    
    
    @Test
    void shouldNotDeleteCurrentUser() {

    	User user = createUser(1L, "admin", Role.ADMIN);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        mockAuthenticatedUser("admin");

        assertThrows(
                CannotDeleteCurrentUserException.class,
                () -> userService.deleteUser(1L)
        );

        verify(userRepository, never()).delete(any());
    }
    
    @Test
    void shouldNotDeleteLastAdmin() {

    	User user = createUser(1L, "admin", Role.ADMIN);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.countByRole(Role.ADMIN))
                .thenReturn(1L);

        mockAuthenticatedUser("anotherAdmin");

        assertThrows(
                CannotDeleteCurrentUserException.class,
                () -> userService.deleteUser(1L)
        );

        verify(userRepository, never()).delete(any());
    }
    
    @Test
    void shouldReturnCurrentUser() {

    	User user = createUser(1L, "admin", Role.ADMIN);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        mockAuthenticatedUser("admin");

        UserResponse response = userService.whoAmI();

        assertEquals("admin", response.getUsername());
        assertEquals(Role.ADMIN, response.getRole());
        
        verify(userRepository).findByUsername("admin");
    }
    
    
    @Test
    void shouldThrowInternalServerExceptionWhenCurrentUserMissing() {

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.empty());

        mockAuthenticatedUser("admin");

        assertThrows(
                InternalServerException.class,
                () -> userService.whoAmI()
        );
        
        verify(userRepository).findByUsername("admin");
        
        
    }
    
    private void mockAuthenticatedUser(String username) {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);
    }
    
    
	private User createUser(Long id, String username, Role role) {

		User user = new User();

		user.setId(id);
		user.setUsername(username);
		user.setPassword("encodedPassword");
		user.setRole(role);

		return user;
	}
	

}

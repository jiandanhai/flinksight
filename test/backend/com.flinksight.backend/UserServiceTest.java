package com.flinksight.backend;

import com.flinksight.backend.domain.User;
import com.flinksight.backend.repository.UserRepository;
import com.flinksight.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务单元测试
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateAndGetUser() {
        User user = User.builder()
                .tenantId(1L)
                .username("test_user")
                .password("testpwd")
                .status(1)
                .isDeleted(0)
                .build();
        User created = userService.createUser(user);
        assertNotNull(created.getId());

        User fetched = userService.getUserById(created.getId()).orElse(null);
        assertNotNull(fetched);
        assertEquals("test_user", fetched.getUsername());
    }

    @Test
    public void testSoftDeleteUser() {
        User user = User.builder()
                .tenantId(1L)
                .username("to_delete")
                .password("pwd")
                .status(1)
                .isDeleted(0)
                .build();
        User created = userService.createUser(user);
        assertTrue(userService.softDeleteUser(created.getId()));

        User deleted = userService.getUserById(created.getId()).orElse(null);
        assertNotNull(deleted);
        assertEquals(1, deleted.getIsDeleted());
    }
}

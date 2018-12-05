package aahmf.notepad;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class UserModelUniTest {

    @Test
    public void UserNoParameterConstructor() {
        User user = new User();
        assertEquals(null, user.getName());
    }

    @Test
    public void UserNoParameterConstructorSetName() {
        User user = new User();
        user.setName("testino");
        assertEquals("testino", user.getName());
    }

    @Test
    public void UserNoParameterConstructorSetUserName() {
        User user = new User();
        user.setUsername("testinos");
        assertEquals("testinos", user.getUsername());
    }

    @Test
    public void UserParameterConstructor1() {
        User user = new User("name","username");
        assertEquals("name", user.getName());
    }

    @Test
    public void UserParameterConstructor2() {
        User user = new User("name","username");
        assertEquals("username", user.getUsername());
    }

    @Test
    public void UserParameterConstructorSetName() {
        User user = new User("name","username");
        user.setName("testino");
        assertEquals("testino", user.getName());
    }

    @Test
    public void UserParameterConstructorSetUserName() {
        User user = new User("name","username");
        user.setUsername("testinos");
        assertEquals("testinos", user.getUsername());
    }

}

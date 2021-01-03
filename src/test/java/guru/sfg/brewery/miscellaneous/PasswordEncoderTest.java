package guru.sfg.brewery.miscellaneous;

import org.h2.security.SHA256;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PasswordEncoderTest {
    static final String PASSWORD = "password";

    @Test
    void bcryptExample() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();  // you can set strength - default is 10
        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode(PASSWORD));

        String encodedPassword = encoder.encode(PASSWORD);
        assertThat(encoder.matches(PASSWORD, encodedPassword), is(true));
    }
    @Test
    void bcrypt15Example() {
        String scottsPassword = "tiger";
        PasswordEncoder encoder = new BCryptPasswordEncoder(15);
        System.out.println(encoder.encode(scottsPassword));
        System.out.println(encoder.encode(scottsPassword));

        String encodedPassword = encoder.encode(scottsPassword);
        System.out.println("encoded");
        assertThat(encoder.matches(scottsPassword, encodedPassword), is(true));
        System.out.println("matched");
    }
    @Test
    void sha256Example() {
        PasswordEncoder encoder = new StandardPasswordEncoder();
        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode("test"));

        String encodedPassword = encoder.encode(PASSWORD);
        assertThat(encoder.matches(PASSWORD, encodedPassword), is(true));
    }

    @Test
    void ldapExample() {
        PasswordEncoder encoder = new LdapShaPasswordEncoder(); // Uses randon Salt
        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode("tiger"));

        String encodedPassword = encoder.encode(PASSWORD);
        assertThat(encoder.matches(PASSWORD, encodedPassword), is(true));
    }
    @Test
    void noOpExample() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        System.out.println(encoder.encode(PASSWORD));

        String salted = PASSWORD + "ThisIsMySaltString";
        System.out.println(encoder.encode(salted));
    }

    @Test
    void hashExample() {
        // MD5 will always generate the same value for a string - not recommended
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySaltString";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));

    }
}

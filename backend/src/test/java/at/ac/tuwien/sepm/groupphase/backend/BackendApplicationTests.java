package at.ac.tuwien.sepm.groupphase.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BackendApplicationTests {

    /**
     * This is just a simple 'Hello World Equivalent'
     *
     * You may not write tests here.
     * Too unspecific.
     */

    @Test
    public void helloSpringTest() {
        assertEquals("Hello Spring Test", "Hello Spring Test");
    }
}

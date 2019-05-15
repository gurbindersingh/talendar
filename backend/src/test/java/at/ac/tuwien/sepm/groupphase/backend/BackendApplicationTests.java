package at.ac.tuwien.sepm.groupphase.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BackendApplicationTests {

	@Test
	public void contextLoads() {
	    assertEquals("Hello Test", "Hello Test");
	    assertThat("Hello Test", equalToIgnoringCase("HELLO TEST"));
	}



}

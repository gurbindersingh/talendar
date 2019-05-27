package at.ac.tuwien.sepm.groupphase.backend.tests;

import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.regex.Pattern;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FakeDataTest {

    private String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$";
    private String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private Pattern phonePattern = Pattern.compile(phoneRegex);
    private Pattern emailPattern = Pattern.compile(emailRegex);

    @Test
    public void checkFakeDataEmail () {
        FakeData fakeData = new FakeData();
        for(int i = 0; i < 100; i++) {
            assert ( emailPattern.matcher(fakeData.fakeEmail()).find() );
        }
    }


    @Test
    public void checkFakeDataPhoneNumber () {
        FakeData fakeData = new FakeData();
        for(int i = 0; i < 100; i++) {
            String number = fakeData.fakePhoneNumber();
            assert ( phonePattern.matcher(number).find() );
        }
    }

}

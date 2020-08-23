import com.brxnxx.fakeplayers.factory.FakePlayerGenerator;
import com.brxnxx.fakeplayers.factory.utils.RandomString;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class StringTests {

    public static void main(String[] args) {
        String random = RandomString.digits + "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        RandomString tickets = new RandomString(14, new SecureRandom(), random);

        //RandomString gen = new RandomString(8, ThreadLocalRandom.current());

        System.out.println(tickets.nextString());
    }

}

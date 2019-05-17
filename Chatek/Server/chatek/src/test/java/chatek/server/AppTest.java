package chatek.server;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testingDate()
    {
        Client client = new Client();
        String date = client.getCurrentTime();
        assertTrue( true );
    }

}

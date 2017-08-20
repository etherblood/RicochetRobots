package ricochetrobots;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class BitmaskRicochetStateTest {
    
    public BitmaskRicochetStateTest() {
    }

    @Test
    public void lowerMove() {
        BitmaskRicochetState state = new BitmaskRicochetState(new RicochetStateSettings(5, 16));
        int lowerMove = state.lowerMove(0, (short) 0xff01);
        assertEquals(0, lowerMove);
        int upperMove = state.upperMove(0, (short) 0xff01);
        assertEquals(7, upperMove);
        
        lowerMove = state.lowerMove(8, (short) 0xff01);
        assertEquals(1, lowerMove);
        upperMove = state.upperMove(8, (short) 0xff01);
        assertEquals(8, upperMove);
        
        lowerMove = state.lowerMove(15, (short) 0x80ff);
        assertEquals(8, lowerMove);
        upperMove = state.upperMove(15, (short) 0x80ff);
        assertEquals(15, upperMove);
    }
    
    @Test
    public void lowerMove2() {
        BitmaskRicochetState state = new BitmaskRicochetState(new RicochetStateSettings(5, 16));
        int lowerMove = state.lowerMove(2, (short) 0xfffc);
        assertEquals(0, lowerMove);
    }
    
    @Test
    public void upperMove2() {
        BitmaskRicochetState state = new BitmaskRicochetState(new RicochetStateSettings(5, 16));
        int upperMove = state.upperMove(11, (short) 0x0fff);
        assertEquals(15, upperMove);
    }
    
}

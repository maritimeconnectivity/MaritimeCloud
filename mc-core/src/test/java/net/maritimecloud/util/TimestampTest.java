package net.maritimecloud.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author nielsbechnielsen
 *
 */
public class TimestampTest {

    @Test(expected=IllegalArgumentException.class)
    public void plusNotMinus() {
        Timestamp ts = Timestamp.now();
        ts.plus(-1, TimeUnit.HOURS);
        
    }
    
    @Test
    public void plusOneHour() {
        long now = new Date().getTime();
        
        Timestamp ts = Timestamp.create(now);
        Timestamp newTs = ts.plus(1, TimeUnit.HOURS);
        
        // Don't change the source...
        Assert.assertEquals("Original timestamp was modified", now, ts.getTime());
        
        // Validate the new TS
        Assert.assertEquals("New timestamp not correct", now + (60 * 60 * 1000), newTs.getTime());
    }
    
    @Test
    public void betterToString() {
        Timestamp ts = Timestamp.now();

        String defaultName = ts.getClass().getName() + "@" + Integer.toHexString(ts.hashCode());
        
        Assert.assertNotEquals("Please provide a reasonable toString", defaultName, ts.toString());
    }
}

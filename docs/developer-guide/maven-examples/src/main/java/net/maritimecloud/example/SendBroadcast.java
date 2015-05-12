package net.maritimecloud.example;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import maritimecloud.examples.Hello;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionReader;

public class SendBroadcast {

    public static void main(String[] args) throws Exception {
        MmsClientConfiguration conf = MmsClientConfiguration.create();
        conf.setId(MaritimeId.create("mmsi:" + ThreadLocalRandom.current().nextLong(10000, 100000)));
        conf.setPositionReader(PositionReader.fixedPosition(Position.create(1, 1)));
        conf.properties().setName("MaritimeExampleGuide");
        MmsClient client = conf.build();
        System.out.println("Starting send client " + conf.getId());
        for(;;) {
            Hello hello = new Hello();
            hello.setMsg("HelloWorld");
            client.broadcast(new Hello().setMsg("HelloWorld" + new Date()));
            System.out.println("Broadcast send");
            Thread.sleep(1000);
        }
    }
}

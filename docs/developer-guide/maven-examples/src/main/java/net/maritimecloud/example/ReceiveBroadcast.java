package net.maritimecloud.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import maritimecloud.examples.Hello;
import net.maritimecloud.core.id.MaritimeId;
import net.maritimecloud.net.mms.MmsClient;
import net.maritimecloud.net.mms.MmsClientConfiguration;
import net.maritimecloud.util.geometry.Position;
import net.maritimecloud.util.geometry.PositionReader;

public class ReceiveBroadcast {

    public static void main(String[] args) throws Exception {
        MmsClientConfiguration conf = MmsClientConfiguration.create();
        conf.setId(MaritimeId.create("mmsi:" + ThreadLocalRandom.current().nextLong(10000, 100000)));
        conf.setPositionReader(PositionReader.fixedPosition(Position.create(1, 1)));
        conf.properties().setName("MaritimeExampleGuide");
        MmsClient client = conf.build();

        System.out.println("Starting receive client " + conf.getId());

        client.broadcastSubscribe(Hello.class,
                (con, e) -> System.out.println("Received " + e.getMsg() + " from " + con.getSender()));


        System.out.println("Waiting for Hello broadcasts");
        Thread.sleep(100000);
        client.shutdown();
        client.awaitTermination(1, TimeUnit.SECONDS);
    }
}

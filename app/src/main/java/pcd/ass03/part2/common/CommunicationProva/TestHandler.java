package pcd.ass03.part2.common.CommunicationProva;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestHandler {
    public static void main(String[] args) {
        Rabbit rabbit = null;
        try {
            rabbit = new Rabbit();
        } catch (Exception e) {
            System.out.println("Eccezione a istanziare");
        }

        HandlerMessageDBGameCode.initialize(rabbit);

        HandlerMessageDBGameCode.addGameCode("lupo");

        HandlerMessageDBGameCode.addGameCode("lupo");


        HandlerMessageDBGameCode.addGameCode("carmine");
        HandlerMessageDBGameCode.addGameCode("giorgio");
        HandlerMessageDBGameCode.addGameCode("callo");
        HandlerMessageDBGameCode.addGameCode("car");
        HandlerMessageDBGameCode.addGameCode("curcuma");
        HandlerMessageDBGameCode.removeGameCode("carl");
        System.out.println(HandlerMessageDBGameCode.getMotherString());
        System.out.println(HandlerMessageDBGameCode.isPresent("lupo"));
        System.out.println(HandlerMessageDBGameCode.getMotherString());
        //System.out.println(HandlerMessageDBGameCode.isPresent("callo"));
        //System.out.println(HandlerMessageDBGameCode.getMotherString());


    }
}

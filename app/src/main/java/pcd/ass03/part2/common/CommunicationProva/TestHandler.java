package pcd.ass03.part2.common.CommunicationProva;

public class TestHandler {
    public static void main(String[] args) {

        HandlerMessageDBGameCode.addGameCode("lupo");

        HandlerMessageDBGameCode.addGameCode("lupo");

        HandlerMessageDBGameCode.addGameCode("carmine");
        HandlerMessageDBGameCode.addGameCode("giorgio");
        HandlerMessageDBGameCode.addGameCode("callo");
        HandlerMessageDBGameCode.addGameCode("car");
        HandlerMessageDBGameCode.addGameCode("curcuma");
        HandlerMessageDBGameCode.removeGameCode("carl");
        System.out.println(HandlerMessageDBGameCode.getMotherString());


    }
}

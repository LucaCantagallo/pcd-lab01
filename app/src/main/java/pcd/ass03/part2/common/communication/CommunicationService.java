package pcd.ass03.part2.common.communication;


public interface CommunicationService {
    void createGame(String gameCode) throws Exception;
    void joinGame(String gameCode, String playerId) throws Exception;
    void selectCell(String gameCode, String playerId, int x, int y) throws Exception;
    void insertNumber(String gameCode, String playerId, int x, int y, int value) throws Exception;
    void leaveGame(String gameCode, String playerId) throws Exception;
}
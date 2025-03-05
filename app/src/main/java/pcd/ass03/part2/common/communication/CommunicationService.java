package pcd.ass03.part2.common.communication;

public interface CommunicationService {
    void sendMessage(String queueName, String message) throws Exception;
    String receiveMessage(String queueName) throws Exception;
}

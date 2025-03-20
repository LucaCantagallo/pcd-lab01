package pcd.ass03.part2.part2B.model;


public class User {

    private final String id;
    private final String color;

    public User(String id, String color){
        this.id = id;
        this.color = color;
        private final GameManager gameManager;

        Registry registry = LocateRegistry.getRegistry();
        gameManager = (GameManager) registry.lookup("GameManager");
        gameManager.registerCallback(new UserCallbackImpl(this));
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public void createGrid(String gameCode) throws IOException, TimeoutException {
        gameManager.createGrid(String gameCode);
    }

}
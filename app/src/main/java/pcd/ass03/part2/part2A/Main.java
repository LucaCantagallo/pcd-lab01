package pcd.ass03.part2.part2A;


import pcd.ass03.part2.part2A.controller.StartController;
import pcd.ass03.part2.part2A.model.Rabbit;
import pcd.ass03.part2.part2A.view.StartView;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {

        Rabbit user = new Rabbit("1", "yellow");
        StartView view = new StartView(user.getId());
        new StartController(view, user);
        view.setVisible(true);

        Rabbit user1 = new Rabbit("2", "green");
        StartView view1 = new StartView(user1.getId());
        new StartController(view1, user1);
        view1.setVisible(true);

    }
}

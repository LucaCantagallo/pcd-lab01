package pcd.ass03.part2.part2B.run;

import pcd.ass03.part2.part2B.controller.StartController;
import pcd.ass03.part2.part2B.model.RMI;
import pcd.ass03.part2.part2B.model.remote.UserCallback;
import pcd.ass03.part2.part2B.view.StartView;

public class Client {

    public static void main(String[] args) {
        try {
            RMI user1 = new RMI("1");
            StartView view = new StartView(user1.getUsername());
            new StartController(view, user1);
            view.setVisible(true);

            RMI user2 = new RMI("2");
            StartView view2 = new StartView(user2.getUsername());
            new StartController(view2, user2);
            view2.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package pcd.ass03.part2.part2A;


import pcd.ass03.part2.part2A.controller.StartController;
import pcd.ass03.part2.part2A.model.Rabbit;
import pcd.ass03.part2.part2A.view.StartView;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {

        for(int i = 0; i < 2; i++){
            Rabbit user = new Rabbit(i+"");
            StartView view = new StartView(user.getId());
            new StartController(view, user);
            view.setVisible(true);
        }



    }
}

package pcd.ass03.part2.part2A.controller;

import pcd.ass03.part2.part2A.model.Rabbit;
import pcd.ass03.part2.part2A.view.GameView;
import pcd.ass03.part2.part2A.view.GridListView;
import pcd.ass03.part2.part2A.view.StartView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridListController implements GridUpdateListener {
    private final Rabbit user;
    private final GridListView gridListView;
    private final StartView startView;
    private GameView detailsView;

    public GridListController(Rabbit user, StartView startView, GridListView gridListView) {
        this.user = user;
        this.gridListView = gridListView;
        this.startView = startView;
        user.addGridUpdateListener(this);
        this.gridListView.addBackButtonListener(new BackButtonListener());
        initView();
    }

    private void initView() {
        gridListView.displayGrids(user.getAllGrids(), new GridButtonListener());
    }

    @Override
    public void onGridCreated() {
        gridListView.displayGrids(user.getAllGrids(), new GridButtonListener());
    }

    @Override
    public void onGridUpdated(int gridIndex) {

    }

    @Override
    public void onCellSelected(int gridId, int row, int col, Color color, String idUser) {

    }

    @Override
    public void onCellUnselected(int gridId, int row, int col) {

    }

    @Override
    public void onGridCompleted(int gridId, String userId) {
        gridListView.displayGrids(user.getAllGrids(), new GridButtonListener());
    }

    class GridButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int gridIndex = Integer.parseInt(e.getActionCommand().split(" ")[1]) - 1;
            detailsView = new GameView(user.getId());
            new GameController(user, detailsView, startView, gridIndex);
            detailsView.setVisible(true);
            startView.setVisible(false);
            gridListView.setVisible(false);
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gridListView.setVisible(false);

            if (detailsView != null) {
                    detailsView.setVisible(false);
            }

            startView.setVisible(true);
        }
    }
}

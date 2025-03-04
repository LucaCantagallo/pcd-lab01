package pcd.ass03.part1.view;



import pcd.ass03.part1.abstractSim.AbstractSimulation;
import pcd.ass03.part1.abstractSim.RoadSimStatistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewController {

    private final AbstractSimulation simulation;
    private final RoadSimView view;

    public ViewController(AbstractSimulation simulation, RoadSimView view) {
        this.simulation = simulation;
        this.view = view;

        RoadSimStatistics stat = new RoadSimStatistics(simulation.getNumCars());
        view.display();

        simulation.addSimulationListener(stat);
        simulation.addSimulationListener(view);

        view.addStartListener(new StartButtonListener());
        view.addStopListener(new StopButtonListener());
        view.addPauseListener(new PauseButtonListener());

    }

    public void runSimulation(int numSteps){
        simulation.run(numSteps);
    }

    public class StartButtonListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            runSimulation(view.getSlider().getValue());
            view.getStartButton().setBackground(Color.white);
            view.getStopButton().setBackground(Color.red);
            view.getStartButton().setEnabled(false);
            view.getStopButton().setEnabled(true);
            view.getPauseButton().setEnabled(true);
        }
    }

    public class StopButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            view.getStopButton().setEnabled(false);
			view.getPauseButton().setEnabled(false);
			JOptionPane.showMessageDialog(view, "Simulation closed");
            simulation.shutdown();
			System.exit(0);
        }
    }

    public class PauseButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            view.getStartButton().setBackground(Color.white);
            if(view.getPauseButton().getText().equals("RESUME")){
                simulation.resume();
                view.getPauseButton().setText("PAUSE");
                view.getStopButton().setBackground(Color.red);
                view.getStopButton().setEnabled(true);
            } else {
                simulation.pause();
                view.getPauseButton().setText("RESUME");
                view.getStopButton().setBackground(Color.white);
                view.getStopButton().setEnabled(false);
            }
        }
    }

}

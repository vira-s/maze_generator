package edu.elte.thesis.view.window.preferences;

import edu.elte.thesis.view.window.preferences.generator.GeneratorModelPanel;
import edu.elte.thesis.view.window.preferences.trainer.ModelTrainerAndMazeGenerationPanel;

import javax.swing.JTabbedPane;
import java.awt.Font;

/**
 * @author Viktoria Sinkovics
 */
public class MazePreferenceTabbedPane extends JTabbedPane {

    private static final String[] TAB_TITLES = {"Train", "Generate"};

    private static final String[] TAB_TIPS = {"Generate Training Data and Train Model", "Generate Mazes with Model"};

    private ModelTrainerAndMazeGenerationPanel modelTrainerAndMazeGenerationPanel;

    private GeneratorModelPanel generatorModelPanel;

    public MazePreferenceTabbedPane() {
        super();
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        modelTrainerAndMazeGenerationPanel = new ModelTrainerAndMazeGenerationPanel();
        generatorModelPanel = new GeneratorModelPanel();

        addTab(TAB_TITLES[0], null, modelTrainerAndMazeGenerationPanel, TAB_TIPS[0]);
        addTab(TAB_TITLES[1], null, generatorModelPanel, TAB_TIPS[1]);
    }

    public ModelTrainerAndMazeGenerationPanel getModelTrainerAndMazeGenerationPanel() {
        return modelTrainerAndMazeGenerationPanel;
    }

    public GeneratorModelPanel getGeneratorModelPanel() {
        return generatorModelPanel;
    }
}

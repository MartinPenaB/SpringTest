package com.portfolio.japnet.javafx.src.application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;

public class GameOfLifeController extends GeneralController<GameOfLife> {

	Timeline timeline = new Timeline();
	boolean animationIsRunning = false;
	char[][] snapshot;

	@FXML
	public void initialize() {
		setInputLimit(3, iterationsTextField);
		setInputLimit(18, ruleTextField);
		ruleTextField.setPromptText(GameOfLife.DEFAULT_DEAD_RULE + GameOfLife.DEFAULT_ALIVE_RULE);
	}

	@Override
	void setLanguage() {
		iterationsLabel.setText(translate("Iterations") + ":");
		runButton.setText(translate("Start"));
		stopButton.setText(translate("Stop"));
		stage.setTitle(translate(project.getTitle()));
		backButton.setText(translate("Back"));
		resetButton.setText(translate("Reset"));
		clearButton.setText(translate("Clear"));
		ruleLabel.setText(translate("Rule") + ":");
		inputAlertHeaderText = translate("Invalid input")+" ("+translate("Iterations").toLowerCase()+")";
		inputAlertContentText = translate("Please enter a positive integer.");
		randomButton.setText(translate("Random"));
		multicolorRadioButton.setText(translate("Multicolor"));
	}

	void continueAnimation() {
		timeline.play();
		runButton.setDisable(true);
	}

	void startAnimation() {
		try {
			int total = Integer.parseInt(iterationsTextField.getText());
			if (total < 0)
				throw new NumberFormatException();

			runButton.setDisable(true);
			timeline.getKeyFrames().clear();
			animationIsRunning = true;
			snapshot = project.getGridState(true, null);

			for (int i = 0; i <= total; i++) {
				int exce = i;
				KeyFrame keyFrame = new KeyFrame(Duration.millis(GameOfLife.ANIMATION_DELAY_MS * i), event -> {
					project.modifyStates(false, project.getGridState(false, ruleTextField.getText()));
					project.updateColors();
					infoLabel.setText("Exce: " + exce);
					if (exce == total) {
						runButton.setDisable(false);
						animationIsRunning = false;
					}
				});
				timeline.getKeyFrames().add(keyFrame);
			}

			timeline.play();
		} catch (NumberFormatException e) {
			Project.showAlert(inputAlertHeaderText, inputAlertContentText);
		}
	}

	@Override
	public void initiateAction(ActionEvent event) {

		if (animationIsRunning)
			continueAnimation();
		else
			startAnimation();

	}

	@FXML
	void reset() {
		animationIsRunning = false;
		timeline.stop();
		infoLabel.setText("Exce: 0");
		runButton.setDisable(false);
		if (snapshot != null) {
			project.modifyStates(false, snapshot);
			project.updateColors();
		}

	}

	@FXML
	void changeColor() {
		project.updateColors();
	}

	@FXML
	void clear() {
		project.clearGrid();
	}

	@FXML
	void stop() {
		timeline.pause();
		runButton.setDisable(false);
	}

	@FXML
	void randomize() {
		project.modifyStates(true, null);
		project.updateColors();
	}

	@FXML
	void multicolor() {
		project.altColor = !project.altColor;
		project.updateColors();
	}

	@FXML
	void interactable() {
		project.interactable = !project.interactable;
	}

	@Override
	void setAdditionalData() {
		setLanguage();
		project.generateGrid();
	}

}

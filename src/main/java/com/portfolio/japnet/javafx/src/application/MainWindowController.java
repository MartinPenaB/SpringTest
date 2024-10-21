package com.portfolio.japnet.javafx.src.application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindowController extends GeneralController<Project> {

	String helpHeaderText;
	String helpContentText;

	@FXML
	public void initialize() {
		addTranslations();
		setAdditionalData();
	}

	/**
	 * Navigates to a project window.
	 *
	 * @param event The ActionEvent triggered by the button click.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public void initiateAction(ActionEvent event) throws IOException {
		openProject(getProjectOrNewClient(), event);
	}

	void openProject(Project instance, ActionEvent event) throws IOException {
		Project project = instance;

		if (project == null) {
			Project.showAlert(inputAlertHeaderText, inputAlertContentText);
			return;
		}

		Stage stage = isTM(project) ? getTMStage(project) : getCurrStage(event);
		Scene scene = project.scene;

		if (scene != null) {
			project.controller.setLanguage();
			stage.setScene(scene);
		} else
			project.createApp(stage);

		stage.show();
	}

	Project getProjectOrNewClient() {
		Project project = projectComboBox.getValue();
		return project instanceof TMClient ? new TMClient() : project;
	}

	Stage getCurrStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	Stage getTMStage(Project project) {
		return project.controller != null ? project.controller.stage : getNewStage();
	}

	Stage getNewStage() {
		return new Stage() {
			{
				setResizable(false);
				getIcons().add(new Image("/com/portfolio/japnet/javafx/src/javaicon.png"));
			}
		};
	}

	boolean isTM(Project project) {
		return project instanceof TMClient || project instanceof TMServer || project instanceof TuringMachine;
	}

	/**
	 * Opens the help window.
	 *
	 * @param event The ActionEvent triggered by the button click.
	 * @throws IOException If an I/O error occurs.
	 */
	@FXML
	public void openHelp() {
		new Alert(AlertType.INFORMATION) {
			{
				setHeaderText(helpHeaderText);
				setContentText(helpContentText);
				show();
			}
		};
	}

	@FXML
	public void quitApplication() {
		((Stage) mainAnchorPane.getScene().getWindow()).close();
	}

	@Override
	void setLanguage() {
		projectComboBox.setPromptText(translate("Select a project"));
		languageMenuBar.getMenus().getFirst().setText(translate("Language"));
		languageMenuBar.getMenus().getFirst().getItems().forEach(item -> item.setText(translate(item.getText())));
		closeButton.setText(translate("Close"));
		helpButton.setText(translate("Help"));
		helpHeaderText = translate("Help");
		helpContentText = translate(
				"Select one of the projects using the drop down list and click OK to confirm selection.");
		inputAlertHeaderText = translate("No project selected");
		inputAlertContentText = translate("Please select a project.");
	}

	@Override
	void setAdditionalData() {
		setLanguage();
	}

}

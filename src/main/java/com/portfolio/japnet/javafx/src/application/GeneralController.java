package com.portfolio.japnet.javafx.src.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class GeneralController<T> {

	@FXML
	protected ComboBox<Project> projectComboBox;
	@FXML
	protected AnchorPane mainAnchorPane;
	@FXML
	protected AnchorPane headerAnchorPane;
	@FXML
	protected AnchorPane backgroundAnchorPane;
	@FXML
	protected AnchorPane contentAnchorPane;
	@FXML
	protected AnchorPane footerAnchorPane;
	@FXML
	protected TextField iterationsTextField;
	@FXML
	protected TextField ruleTextField;
	@FXML
	protected TextField tapeTextField;
	@FXML
	protected TextField portTextField;
	@FXML
	protected TextField userTextField;
	@FXML
	protected Label userLabel;
	@FXML
	protected Label iterationsLabel;
	@FXML
	protected Label infoLabel;
	@FXML
	protected Label ruleLabel;
	@FXML
	protected Label tapeLabel;
	@FXML
	protected Label portLabel;
	@FXML
	protected RadioButton multicolorRadioButton;
	@FXML
	protected RadioButton manualRadioButton;
	@FXML
	protected Button randomButton;
	@FXML
	protected Button broadcastButton;
	@FXML
	protected Button connectButton;
	@FXML
	protected Button sendButton;
	@FXML
	protected Button runButton;
	@FXML
	protected Button closeButton;
	@FXML
	protected Button helpButton;
	@FXML
	protected Button backButton;
	@FXML
	protected Button stopButton;
	@FXML
	protected Button clearButton;
	@FXML
	protected Button resetButton;
	@FXML
	protected Button receiveButton;
	@FXML
	protected Button setDataButton;
	@FXML
	protected MenuBar languageMenuBar;
	@FXML
	protected MenuItem english;
	@FXML
	protected MenuItem spanish;
	@FXML
	protected MenuItem french;
	@FXML
	protected MenuItem portuguese;
	@FXML
	protected ColorPicker colorPicker;

	String inputAlertHeaderText;
	String inputAlertContentText;

	T project;
	Stage stage;

	Map<String, Map<String, String>> dictionary = new HashMap<>();
	static String language = "english";

	void init(T project, Stage stage) {
		this.project = project;
		this.stage = stage;
		addTranslations();
		setAdditionalData();
	}

	void addTranslations() {

		addTranslation("Rule", "Regla", "Regle", "Regra");
		addTranslation("Select a project", "Seleccione un proyecto", "Sélectionnez un projet", "Selecione um projeto");
		addTranslation("Select one of the projects using the drop down list and click OK to confirm selection.",
				"Seleccione uno de los proyectos utilizando la lista desplegable y haga clic en OK para confirmar la selección.",
				"Sélectionnez l'un des projets à l'aide de la liste déroulante et cliquez sur OK pour confirmer la sélection.",
				"Selecione um dos projetos usando a lista suspensa e clique em OK para confirmar a seleção.");
		addTranslation("French", "Francés", "Français", "Francês");
		addTranslation("Portuguese", "Portugués", "Portugais", "Português");
		addTranslation("Spanish", "Español", "Espagnol", "Espanhol");
		addTranslation("English", "Inglés", "Anglais", "Inglês");
		addTranslation("Random", "Aleatorio", "Aléatoire", "Aleatório");
		addTranslation("Back", "Atrás", "Retour", "Voltar");
		addTranslation("Stop", "Parar", "Arrêter", "Parar");
		addTranslation("Reset", "Resetear", "Redémarrer", "Reiniciar");
		addTranslation("Clear", "Borrar", "Effacer", "Limpar");
		addTranslation("Iterations", "Iteraciones", "Itérations", "Iterações");
		addTranslation("Close", "Cerrar", "Fermer", "Fechar");
		addTranslation("Help", "Ayuda", "Aide", "Ajuda");
		addTranslation("Model", "Modelo", "Modèle", "Modelo");
		addTranslation("Set", "Establecer", "Ensemble", "Conjunto");
		addTranslation("Start", "Iniciar", "Commencer", "Começar");
		addTranslation("Language", "Idioma", "Langue", "Língua");
		addTranslation("Cellular Automata", "Autómata Celular", "Automate cellulaire", "Autômato celular");
		addTranslation("Game of Life", "Juego de la Vida", "Jeu de la vie", "Jogo da Vida");
		addTranslation("Invalid input", "Entrada inválida", "Entrée invalide", "Entrada inválida");
		addTranslation("Please enter a binary number (8 digits).", "Por favor ingrese un número binario (8 dígitos).",
				"Veuillez entrer un nombre binaire (8 chiffres).", "Por favor insira um número binário (8 dígitos).");
		addTranslation("Please enter a positive integer.", "Por favor ingrese un entero positivo.",
				"Veuillez entrer un entier positif.", "Por favor insira um inteiro positivo.");
		addTranslation("No project selected", "Ningún proyecto seleccionado", "Aucun projet sélectionné",
				"Nenhum projeto selecionado");
		addTranslation("Please select a project.", "Por favor seleccione un proyecto.",
				"Veuillez sélectionner un projet.", "Por favor selecione um projeto.");
		addTranslation("Multicolor", "Multicolor", "Multicolore", "Multicolor");

	}

	void addTranslation(String english, String spanish, String french, String portuguese) {
		Map<String, String> translations = new HashMap<>();
		translations.put("spanish", spanish);
		translations.put("french", french);
		translations.put("portuguese", portuguese);
		dictionary.put(english, translations);
	}

	@FXML
	public void switchLanguage(ActionEvent event) {
		language = ((MenuItem) event.getSource()).getId();
		setLanguage();
	}

	String translate(String str) {
		String key = findKey(str);
		return language.equals("english") ? key : dictionary.get(key).get(language);
	}

	String findKey(String str) {
		for (Map.Entry<String, Map<String, String>> entry : dictionary.entrySet())
			if (entry.getValue().containsValue(str))
				return entry.getKey();
		return str;
	}

	void setInputLimit(int inputLimit, TextField textField) {
		textField.setTextFormatter(
				new TextFormatter<>(change -> change.getControlNewText().length() > inputLimit ? null : change));
	}

	void setIntegerConstraint(TextField textField) {
		textField.textProperty()
				.addListener((obs, oldVal, newVal) -> textField.setText(newVal.replaceAll("[^\\d]", "")));
	}

	/**
	 * Navigates to the main window.
	 *
	 * @param event The ActionEvent triggered by the button click.
	 */
	@FXML
	public void goToMain() {
		stage.setTitle("[JAP - Computer Sciene]");
		stage.setScene(JAP.mainScene);
	}

	@FXML
	public abstract void initiateAction(ActionEvent event) throws IOException;

	abstract void setLanguage();

	abstract void setAdditionalData();

}

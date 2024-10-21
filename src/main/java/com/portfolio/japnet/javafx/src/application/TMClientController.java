package com.portfolio.japnet.javafx.src.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TMClientController extends GeneralController<TMClient> {

	@FXML
	TextArea clientTextArea;
	Socket socket;
	String host = "localhost";
	int port;
	String username;
	String data = "";
	TuringMachine tm;

	@FXML
	public void initialize() {
		setIntegerConstraint(portTextField);
		setInputLimit(5, portTextField);
		clientTextArea.setEditable(false);
	}

	@Override
	public void initiateAction(ActionEvent event) throws IOException {
		try {
			username = userTextField.getText();
			if (username.equals("")) {
				Project.showAlert("Invalid username", "Username must be entered");
				return;
			}
			port = Integer.parseInt(portTextField.getText());
			socket = new Socket(host, port);
			clientTextArea.appendText("Connection made\n");
			project.sendData(username, socket);
			connectButton.setDisable(true);
		} catch (IllegalArgumentException e) {
			Project.showAlert("Invalid port", "Please enter a valid port number (0000 - 65535)");
		} catch (IOException e) {
			clientTextArea.appendText("Could not connect to server\n");
		}
	}

	@FXML
	void receive() throws IOException {
		if (socket != null && !socket.isClosed()) {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			if (in.ready()) {
				data = in.readLine();
				clientTextArea.appendText("Client received from server: " + data + "\n");
				ruleTextField.setText(data);
			}
		} else
			clientTextArea.appendText("Failed to receive\n");
	}

	@FXML
	void send() throws IOException {
		if (socket != null && !socket.isClosed()) {
			project.sendData("#" + data, socket);
			clientTextArea.appendText("Client sent to server: " + data + "\n");
		} else
			clientTextArea.appendText("Failed to send\n");
	}

	@FXML
	void end() throws IOException {
		if (socket != null) {
			project.sendData(ClientHandler.DONE, socket);
			socket.close();
			connectButton.setDisable(false);
			clientTextArea.appendText("Client disconnected\n");
		}
	}

	@FXML
	void set() {
		data = ruleTextField.getText();
		clientTextArea.appendText("local data set to: " + data + "\n");
	}

	@FXML
	void run() throws IOException {
		if (tm == null)
			tm = new TuringMachine();
		if (project.isValidModel(data)) {
			if (tm.controller == null)
				tm.model = data;
			else
				tm.controller.ruleTextField.setText(data);
			new MainWindowController().openProject(tm, null);
		}
	}

	@Override
	void setLanguage() {

	}

	@Override
	void setAdditionalData() {
		setLanguage();
		stage.setOnCloseRequest(event -> {
			if (connectButton.isDisabled())
				event.consume();
		});
	}

}

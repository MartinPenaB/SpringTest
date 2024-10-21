package com.portfolio.japnet.javafx.src.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;

public class TMServerController extends GeneralController<TMServer> {

	@FXML
	ComboBox<ClientHandler> clients;
	@FXML
	TextArea serverTextArea;
	ServerSocket serverSocket;
	int port;
	String data = "";

	@FXML
	public void initialize() {
		setIntegerConstraint(portTextField);
		setInputLimit(5, portTextField);
		serverTextArea.setEditable(false);
		clients.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(ClientHandler item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null || empty ? clients.getPromptText() : item.username);
			}
		});
	}

	@Override
	public void initiateAction(ActionEvent event) throws IOException {

		try {
			port = Integer.parseInt(portTextField.getText());
			serverSocket = new ServerSocket(port);
		} catch (IllegalArgumentException e) {
			Project.showAlert("Invalid port", "Please enter a valid port number (0000 - 65535)");
			return;
		}

		serverTextArea.appendText("Server waiting...\n");

		new Thread(() -> {
			try {
				while (true) {
					Socket clientSocket = serverSocket.accept();
					String username = getUsername(clientSocket);
					serverTextArea.appendText("Client " + username + " connected.\n");
					ClientHandler clientHandler = new ClientHandler(clientSocket, username, clients, serverTextArea);
					clientHandler.start();
					clients.getItems().add(clientHandler);
				}
			} catch (IOException e) {
				serverTextArea.appendText("Server closed.\n");
			}
		}).start();

		connectButton.setDisable(true);

	}

	String getUsername(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		return in.readLine();
	}

	@FXML
	void receive() {
		if (clients.getValue() != null) {
			if (!clients.getValue().data.isEmpty()) {
				data = clients.getValue().data.substring(1);
				serverTextArea.appendText("Server received from client[" + clients.getValue() + "]: " + data + "\n");
			}
		} else
			serverTextArea.appendText("Failed to receive\n");
	}

	@FXML
	void send() throws IOException {
		if (clients.getValue() != null) {
			project.sendData(data, clients.getValue().socket);
			serverTextArea.appendText("Server sent to client[" + clients.getValue() + "]: " + data + "\n");
		} else
			serverTextArea.appendText("Failed to send\n");
	}

	@FXML
	void end() throws IOException {
		if (!clients.getItems().isEmpty())
			Project.showAlert("Cannot stop server", "All clients must disconnect before server can stop");
		else if (serverSocket != null) {
			serverSocket.close();
			connectButton.setDisable(false);
		}
	}

	@FXML
	void broadcast() throws IOException {
		if (!clients.getItems().isEmpty()) {
			for (ClientHandler client : clients.getItems())
				project.sendData(data, client.socket);
			serverTextArea.appendText("Data[" + data + "] successfully broadcasted\n");
		} else
			serverTextArea.appendText("No clients connected\n");
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

class ClientHandler extends Thread {

	static final String DONE = ".";
	Socket socket;
	String username;
	@FXML
	ComboBox<ClientHandler> clients;
	@FXML
	TextArea serverTextArea;
	String data = "";

	ClientHandler(Socket socket, String username, ComboBox<ClientHandler> clients, TextArea serverTextArea) {
		this.socket = socket;
		this.username = username;
		this.clients = clients;
		this.serverTextArea = serverTextArea;
	}

	@Override
	public void run() {

		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			do {
				data = in.readLine();
			} while (!data.equals(DONE));

			Platform.runLater(() -> {
				clients.getItems().remove(this);
				serverTextArea.appendText("Client " + username + " disconnected\n");
			});

		} catch (IOException e) {
		}

	}

	@Override
	public String toString() {
		return username;
	}

}

package com.portfolio.japnet.javafx.src.application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public abstract class Project {

	Scene scene;
	GeneralController<Project> controller;

	boolean altColor = false;
	boolean interactable = false;

	static final Color DEFAULT_COLOR = Color.WHITESMOKE;

	static final int DEFAULT_GRID_WIDTH = 69;
	static final int DEFAULT_GRID_HEIGHT = 35;
	static final double DEFAULT_CELL_SIZE = 10;

	int gridWidth = DEFAULT_GRID_WIDTH;
	int gridHeight = DEFAULT_GRID_HEIGHT;
	double cellSize = DEFAULT_CELL_SIZE;

	GridPane grid;

	void generateGrid() {
		grid = new GridPane();
		initializeGrid('0');
		addGridToScene(scene);
	}

	void addGridToScene(Scene scene) {
		grid.setAlignment(Pos.CENTER);
		grid.setGridLinesVisible(true);
		((BorderPane) scene.getRoot()).setCenter(grid);
	}

	void initializeGrid(char defaultState) {
		for (int row = 0; row < gridHeight; row++)
			for (int col = 0; col < gridWidth; col++)
				addCell(row, col, defaultState);
	}

	void addCell(int row, int col, char state) {
		Rectangle cell = new Rectangle(cellSize, cellSize, state == '0' ? DEFAULT_COLOR : Color.BLACK);
		cell.setStroke(Color.DARKGREY);
		GridPane.setConstraints(cell, col, row, 1, 1, HPos.CENTER, VPos.CENTER);

		cell.setOnMouseClicked(event -> {
			if (interactable && event.getButton() == MouseButton.PRIMARY) {
				cell.setFill(cell.getFill().equals(DEFAULT_COLOR) ? Color.BLACK : DEFAULT_COLOR);
				updateColors();
			}
		});

		grid.add(cell, col, row);
	}

	char getCellState(int row, int col) {
		return getCellColor(row, col).equals(DEFAULT_COLOR) ? '0' : '1';
	}

	Color getCellColor(int row, int col) {
		try {
			return (Color) getCell(row, col).getFill();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	void updateColors() {
		for (int row = 0; row < gridHeight; row++)
			for (int cell = 0; cell < gridWidth; cell++)
				if (!getCellColor(row, cell).equals(DEFAULT_COLOR))
					getCell(row, cell).setFill(getFillColor(row, cell));
	}

	/**
	 * Gets the color that will be used to fill a cell in the "on" state.
	 *
	 * @param row The row index of the cell.
	 * @param col The column index of the cell.
	 * @return The color to be used for filling the specified cell in the "on"
	 *         state.
	 * 
	 */
	abstract Color getFillColor(int row, int col);

	Rectangle getCell(int row, int col) {
		if (row > -1 && row < gridHeight && col > -1 && col < gridWidth)
			return (Rectangle) grid.getChildren().get(row * gridWidth + col);
		throw new IndexOutOfBoundsException("Invalid row or column index in getCell");
	}

	/**
	 * Toggles a cell in the grid based on its state (0 or 1).
	 *
	 * @param row   The row of the cell.
	 * @param col   The column of the cell.
	 * @param state The state of the cell ('0' or '1').
	 */
	void toggleCell(int row, int col, char state) {
		getCell(row, col).setFill(state == '0' ? DEFAULT_COLOR : getFillColor(row, col));
	}

	void clearGrid() {
		for (int row = 0; row < gridHeight; row++)
			for (int col = 0; col < gridWidth; col++)
				toggleCell(row, col, '0');
	}

	/**
	 * Displays an alert dialog with the given header and content text.
	 *
	 * @param headerText  The header text for the alert.
	 * @param contentText The content text for the alert.
	 */
	static void showAlert(String headerText, String contentText) {
		new Alert(AlertType.WARNING) {
			{
				setHeaderText(headerText);

				getDialogPane().setContent(new TextArea(contentText) {
					{
						setEditable(false);
						setWrapText(true);
					}
				});

				show();
			}
		};
	}

	void createApp(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxml()));
		scene = new Scene(loader.load());
		controller = loader.getController();
		controller.init(this, stage);
		addStyle("application.css");
		stage.setScene(scene);
	}

	void addStyle(String cssFile) {
		String css = getClass().getResource(cssFile).toExternalForm();
		scene.getStylesheets().add(css);
	}

	abstract String getFxml();

	abstract String getTitle();

	void sendData(String data, Socket socket) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(data);
	}
	
	boolean isValidModel(String model) {
		String regex = "^[01][^\s][01][^\s][01]( [01][^\s][01][^\s][01])*$";
		if (!model.matches(regex)) {
			String explenation = "TM must be a series of tuples separated by spaces in the form \"abcde\" where:\n"
					+ "'a' = current state (0 or 1).\n" + "'b' = character to be read.\n"
					+ "'c' = next state (0 or 1).\n" + "'d' = character to be written.\n"
					+ "'e' = position shift (0 for left, 1 for right).";
			Project.showAlert("Invalid TM" + " (" + model + ")", explenation);
			return false;
		}
		return true;
	}

}

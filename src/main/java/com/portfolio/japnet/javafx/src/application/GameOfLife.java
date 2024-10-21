package com.portfolio.japnet.javafx.src.application;

import java.util.Random;

import javafx.scene.paint.Color;

public class GameOfLife extends Project {

	Random rand = new Random();

	static final String DEFAULT_DEAD_RULE = "000100000";
	static final String DEFAULT_ALIVE_RULE = "001100000";

	static final float ANIMATION_DELAY_MS = 100f;

	@Override
	String getFxml() {
		return "GL.fxml";
	}

	@Override
	String getTitle() {
		return "Game of Life";
	}

	@Override
	public String toString() {
		return "[A22] GL - Game of Life";
	}

	int getCellValue(int row, int col) {
		Color color = getCellColor(row, col);
		return color != null && !color.equals(DEFAULT_COLOR) ? 1 : 0;
	}

	int getRowNeighbors(int row, int col, boolean containsSelf) {
		return getCellValue(row, col - 1) + getCellValue(row, containsSelf ? -1 : col) + getCellValue(row, col + 1);
	}

	int getTotalNeighbors(int row, int col) {
		return getRowNeighbors(row - 1, col, false) + getRowNeighbors(row, col, true)
				+ getRowNeighbors(row + 1, col, false);
	}

	char getCellFate(int row, int col, String rules) {
		Color state = getCellColor(row, col);
		int neighbors = getTotalNeighbors(row, col);
		String deadRule = DEFAULT_DEAD_RULE;
		String aliveRule = DEFAULT_ALIVE_RULE;
		if (rules.matches("^[01]{18}$")) {
			deadRule = rules.substring(0, 9);
			aliveRule = rules.substring(9);
		} else
			controller.ruleTextField.clear();
		return state.equals(DEFAULT_COLOR) ? deadRule.charAt(neighbors) : aliveRule.charAt(neighbors);
	}

	/**
	 * Retrieves the grid state as a 2D char array based on the current flag and the
	 * provided rules.
	 * 
	 * @param current a boolean indicating whether to get the current grid state or
	 *                the next
	 * @param rules   a String containing the rules for determining the next state
	 * @return a 2D char array representation of the grid state at a given time
	 */
	char[][] getGridState(boolean current, String rules) {
		char[][] snapshot = new char[gridHeight][gridWidth];
		for (int row = 0; row < gridHeight; row++)
			for (int col = 0; col < gridWidth; col++)
				if (current)
					snapshot[row][col] = getCellState(row, col);
				else
					snapshot[row][col] = getCellFate(row, col, rules);
		return snapshot;
	}

	/**
	 * Modifies the states of the cells based on the random flag and the provided
	 * snapshot.
	 * 
	 * @param random   a boolean indicating whether to set states at random
	 * @param snapshot a 2D char array used to update grid states accordingly
	 */
	void modifyStates(boolean random, char[][] snapshot) {
		for (int row = 0; row < gridHeight; row++)
			for (int col = 0; col < gridWidth; col++)
				if (random)
					toggleCell(row, col, rand.nextBoolean() ? '1' : '0');
				else
					toggleCell(row, col, snapshot[row][col]);
	}

	@Override
	Color getFillColor(int row, int col) {
		Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.LIGHTBLUE,
				Color.LIGHTSALMON, Color.VIOLET };
		return altColor ? colors[getTotalNeighbors(row, col)] : controller.colorPicker.getValue();
	}

}

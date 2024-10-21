package com.portfolio.japnet.javafx.src.application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Represents a cellular automaton project.
 */
public class CellularAutomata extends Project {

	/** Delay between adding cells in milliseconds */
	static final float ANIMATION_DELAY_MS = 10f;

	{
		gridWidth = 203;
		gridHeight = 100;
		cellSize = 3;
	}

	/**
	 * Evolves the cellular automaton based on the given model.
	 *
	 * @param rule The model of the cellular automaton.
	 */
	void evolve(String rule) {

		String generation = pad("1");
		Timeline timeline = new Timeline();

		for (int gen = 0; gen < gridHeight; gen++) {

			String currGen = generation;
			int genIdx = gen;

			KeyFrame keyFrame = new KeyFrame(Duration.millis(ANIMATION_DELAY_MS * gen), event -> {
				for (int cell = 0; cell < gridWidth; cell++)
					toggleCell(genIdx, cell, currGen.charAt(cell));
			});

			timeline.getKeyFrames().add(keyFrame);
			generation = pad(nextGen(generation, rule));

		}

		timeline.play();

	}

	String reverseString(String str) {
		StringBuilder rev = new StringBuilder();
		for (int i = 0; i < str.length(); i++)
			rev.insert(0, str.charAt(i));
		return rev.toString();
	}

	char checkCells(int start, String currentGen, String model) {
		return reverseString(model).charAt(toDec(currentGen.substring(start, start + 3)));
	}

	/**
	 * Generates the next generation of the cellular automaton based on the current
	 * generation.
	 *
	 * @param rule       the model of the cellular automata
	 * @param currentGen The current generation of the cellular automaton.
	 * @return The next generation of the cellular automaton.
	 */
	String nextGen(String currentGen, String rule) {
		StringBuilder nextGen = new StringBuilder();
		for (int i = 0; i < currentGen.length() - 2; i++)
			nextGen.append(checkCells(i, currentGen, rule));
		return nextGen.toString();
	}

	/**
	 * Pads the model with '0' characters to match the gridWidth.
	 *
	 * @param states The states to be padded.
	 * @return The padded states.
	 */
	String pad(String states) {
		StringBuilder padded = new StringBuilder(states);
		while (padded.length() != gridWidth)
			if (padded.length() % 2 == 0)
				padded.insert(0, '0');
			else
				padded.append('0');
		return padded.toString();
	}

	/**
	 * Converts a binary model to its decimal equivalent.
	 *
	 * @param model The binary model to be converted.
	 * @return The decimal equivalent of the model.
	 */
	int toDec(String model) {
		int dec = 0;
		for (int pow = model.length() - 1, digit = 0; pow >= 0; pow--, digit++)
			dec += model.charAt(digit) == '1' ? Math.pow(2, pow) : 0;
		return dec;
	}

	@Override
	String getFxml() {
		return "CA.fxml";
	}

	@Override
	String getTitle() {
		return "Cellular Automata";
	}

	@Override
	public String toString() {
		return "[A12] CA - Cellular Automata";
	}

	@Override
	Color getFillColor(int row, int col) {
		return altColor ? Color.RED : Color.BLACK;
	}

}

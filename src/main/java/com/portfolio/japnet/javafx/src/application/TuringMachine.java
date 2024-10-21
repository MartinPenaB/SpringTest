package com.portfolio.japnet.javafx.src.application;

import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

public class TuringMachine extends Project {

	String model = "00000 01000 10010 11000";

	char[] tape;
	int headPosition;
	char currentState;

	void executeTransition(String rule) throws IndexOutOfBoundsException {
		char currChar = tape[headPosition];
		if (rule.charAt(0) == currentState && rule.charAt(1) == currChar) {
			tape[headPosition] = rule.charAt(3);
			if (rule.charAt(4) == '0')
				headPosition--;
			else
				headPosition++;
			currentState = rule.charAt(2);
		}
	}

	void displayTape(TextArea textArea) {
		for (int i = 0; i < tape.length; i++)
			if (i == headPosition)
				textArea.appendText("[" + tape[i] + "] ");
			else
				textArea.appendText(tape[i] + " ");
		textArea.appendText("\n");
	}

	@Override
	Color getFillColor(int row, int col) {
		return null;
	}

	@Override
	String getFxml() {
		return "TM.fxml";
	}

	@Override
	String getTitle() {
		return "Turing Machine";
	}

	@Override
	public String toString() {
		return "[A32] TM - Turing Machine";
	}

}

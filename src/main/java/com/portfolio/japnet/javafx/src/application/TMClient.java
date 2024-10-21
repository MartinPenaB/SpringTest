package com.portfolio.japnet.javafx.src.application;

import javafx.scene.paint.Color;

public class TMClient extends Project {

	@Override
	Color getFillColor(int row, int col) {
		return null;
	}

	@Override
	String getFxml() {
		return "TMC.fxml";
	}

	@Override
	String getTitle() {
		return "Turing Machine Client";
	}

	@Override
	public String toString() {
		return "[A32] TM - Client";
	}

}

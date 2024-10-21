package com.portfolio.japnet.javafx.src.application;

import javafx.scene.paint.Color;

public class TMServer extends Project {

	@Override
	Color getFillColor(int row, int col) {
		return null;
	}

	@Override
	String getFxml() {
		return "TMS.fxml";
	}

	@Override
	String getTitle() {
		return "Turing Machine Server";
	}

	@Override
	public String toString() {
		return "[A32] TM - Server";
	}

}

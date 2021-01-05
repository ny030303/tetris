package net.gondr.views;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import net.gondr.tetris1.App;
import net.gondr.tetris1.Game;

public class MainController {
	@FXML
	private Canvas gameCanvas;
	
	@FXML
	private Canvas nextBlockCanvas;
	
	@FXML
	private Label scoreText;
	
	@FXML
	public Button gameBtn;
	
	public boolean isReset = true;
	
	@FXML
	public void initialize() {
		App.app.controller = this;
		System.out.println("메인 레이아웃 초기화 완료");
		App.app.game = new Game(gameCanvas, nextBlockCanvas);
		// App.app.game.createSideBoard(nextBlockCanvas);
		
		App.app.game.getScore(scoreText);
		App.app.game.getButton(gameBtn);
//		App.app.game.clearBoard();
	}
	
	@FXML
	public void clickBtnEvent() {
		if(isReset) {
			App.app.game.clearBoard();
			App.app.game.scoreReset();
			scoreText.setText("0점");
			// score 리셋
			// 속도 리셋
			// 버튼 text 바꾸기
		} else {
			
		}
	}
	
	public void changeReset() {
		if(isReset) {
			isReset = false;
		} else {
			isReset = true;
		}
		
	}
	
	public void setGameBtnText(String text) {
		gameBtn.setText(text);
	}
	
	
}

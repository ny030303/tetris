package net.gondr.tetris1;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import net.gondr.domain.Block;
import net.gondr.domain.Player;

public class Game {
	private GraphicsContext gc;
	public Block[][] board; // 게임판 2차원 배열

	private double width; // 게임판의 너비와 높이
	private double height;

	private AnimationTimer mainLoop; // 게임 루프
	private long before; // 이전 프레임의 시간

	private Player player;
	private double blockDownTime = 0;

	private Label labelText;
	private Button sButton;

	private int score = 0;

	private double sideWidth; // 옆 캔버스의 너비와 높이
	private double sideHeight;

	public final int MAX_SIDE_BLOCK = 6;
	public Block[][] sideBoard; // 옆 캔버스의 2차원 배열
	private GraphicsContext sideGc;

	public Game(Canvas canvas, Canvas nextBlockCanvas) {
		width = canvas.getWidth();
		height = canvas.getHeight();

		double size = (width - 4) / 10;

		board = new Block[20][10];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				board[i][j] = new Block(j * size + 2, i * size + 2, size);
			}
		}
		this.gc = canvas.getGraphicsContext2D();

		mainLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update((now - before) / 1000000000d); // 0이 9개 끝에다가 d 붙여
				before = now;
				render();
				sideRender();
			}
		};
		before = System.nanoTime();
		
		createSideBoard(nextBlockCanvas);

		player = new Player(board, sideBoard);
		mainLoop.start();

	}

	public void createSideBoard(Canvas canvas) {
		sideWidth = canvas.getWidth();
		sideHeight = canvas.getHeight();

		double size = (sideWidth - 4) / MAX_SIDE_BLOCK;

		sideBoard = new Block[MAX_SIDE_BLOCK][MAX_SIDE_BLOCK];
		for (int i = 0; i < MAX_SIDE_BLOCK; i++) {
			for (int j = 0; j < MAX_SIDE_BLOCK; j++) {
				sideBoard[i][j] = new Block(j * size + 2, i * size + 2, size);
			}
		}
		sideGc = canvas.getGraphicsContext2D();

//		mainLoop = new AnimationTimer() {
//			@Override
//			public void handle(long now) {
//				update( (now - before) / 1000000000d);  //0이 9개 끝에다가 d 붙여
//				before = now;
//				render();
//			}
//		};
//		before = System.nanoTime();
//		mainLoop.start();
//		
//		
	}
	
	public void clearViewBoard() {
		for (int i = 0; i < MAX_SIDE_BLOCK; i++) {
			for (int j = 0; j < MAX_SIDE_BLOCK; j++) {
				sideBoard[i][j].setData(false, Color.WHITE);
			}
		}
	}

	public void getScore(Label scoreText) {
		labelText = scoreText;
	}

	public void setScore(int score) {
		labelText.setText(score + "점");
	}

	// 매초 실행되는 루프 매서드
	public void update(double delta) {
		blockDownTime += delta;
		double timeChecker = 0.5;
		if (score >= 9) {
			timeChecker = 0.1;
		} else if (score >= 8) {
			timeChecker = 0.2;
		} else if (score >= 7) {
			timeChecker = 0.35;
		} else if (score >= 4) {
			timeChecker = 0.35;
		} else if (score >= 2) {
			timeChecker = 0.45;
		}
		if (blockDownTime >= timeChecker) {
			player.down(); // 한칸 내리기
			blockDownTime = 0;
		}
	}

	// 한 줄이 가득 찼는지를 검사하는 매서드
	public void checkLineStatus() {
		for (int i = 19; i >= 0; i--) {
			boolean clear = true; // 해당 줄이 꽉찼다고 가정하고
			// 라인이 꽉찼는지를 검사하는 부분
			for (int j = 0; j < 10; j++) {
				if (!board[i][j].getFill()) {
					clear = false;
					break;
				}
			}

			// 만약 라인이 꽉 찼다면 수행할 부분
			if (clear) {
				score++;
				setScore(score);
				for (int j = 0; j < 10; j++) {
					board[i][j].setData(false, Color.WHITE);
				}

				for (int k = i - 1; k >= 0; k--) {
					for (int j = 0; j < 10; j++) {
						board[k + 1][j].copyData(board[k][j]);
					}
				}

				for (int j = 0; j < 10; j++) {
					board[0][j].setData(false, Color.WHITE);
				}

				// 중요한 것!
				i++;
			}

		}
	}

	// 보드안 블럭 다 지우기
	public void clearBoard() {
		player.clearBlockCount();
		for (int i = 19; i >= 0; i--) {
			for (int j = 0; j < 10; j++) {
				board[i][j].setData(false, Color.WHITE);
			}
		}
	}

	// 매 프레임마다 화면을 그려주는 매서드
	public void render() {
		gc.clearRect(0, 0, width, height);
		gc.setStroke(Color.rgb(0, 0, 0));
		gc.setLineWidth(2);
		gc.strokeRect(0, 0, width, height);

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				board[i][j].render(gc);
			}
		}

	}

	// 매 프레임마다 옆 캔버스 화면을 그려주는 매서드
	public void sideRender() {
		sideGc.clearRect(0, 0, width, height);
//		sideGc.setStroke(Color.rgb(0, 0, 0));
//		sideGc.setLineWidth(2);
//		sideGc.strokeRect(0, 0, width, height);

		for (int i = 0; i < MAX_SIDE_BLOCK; i++) {
			for (int j = 0; j < MAX_SIDE_BLOCK; j++) {
				sideBoard[i][j].render(sideGc);
			}
		}

	}

	// 키보드 이벤트를 처리해주는 매서드
	public void keyHandler(KeyEvent e) {
		player.keyHandler(e);
	}

	public void getButton(Button startBtn) {
		sButton = startBtn;
	}

	public void scoreReset() {
		score = 0;
	}
}

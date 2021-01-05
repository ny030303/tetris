package net.gondr.tetris1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.gondr.views.MainController;

public class App extends Application 
{
	public static App app;
	
	public Game game = null;
	public MainController controller;

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		app = this;
		System.out.println(this);
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/net/gondr/views/Main.fxml"));
			AnchorPane ap = (AnchorPane) loader.load();
			
			Scene scene = new Scene(ap);
			
			scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
				if(game != null) {
					game.keyHandler(e);
				}
			});
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("프로그램 로딩중 오류 발생");
		}		
	}
    
	public static void main(String[] args) {
		launch(args);
	}
}

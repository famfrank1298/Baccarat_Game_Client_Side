import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClientGUI extends Application {
	
	boolean ready = false;
	BaccaratInfo info;
	Client client;
	Text winningField = new Text("Total Winnings: ");
	TextField ip_address;
	TextField port_num;
	BorderPane pane;
	
	ListView<BaccaratInfo> listView;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Client GUI");
		
		Text welcome_title = new Text("Welcome to Baccarat!");
		
		listView = new ListView<BaccaratInfo>();
		
		ip_address = new TextField();
		ip_address.setPromptText("Enter IP Address");
		ip_address.setFocusTraversable(false);
		
		port_num = new TextField();
		port_num.setPromptText("Enter Port Number");
		port_num.setFocusTraversable(false);
		
		Button connect = new Button("Connect");
		connect.setOnAction(e-> {client = new Client(data -> {
			Platform.runLater(()->{listView.getItems().add(data); results(primaryStage, data);});},
				ip_address.getText(), Integer.parseInt(port_num.getText()));
					client.start(); gamble(primaryStage);
			});
		
		VBox welcome_vbox = new VBox(welcome_title, ip_address, port_num, connect);
		welcome_vbox.setAlignment(Pos.CENTER);
		welcome_vbox.setPadding(new Insets(5, 10, 5, 10));
		
		Scene welcome_scene = new Scene(welcome_vbox, 300, 300);
		primaryStage.setScene(welcome_scene);
		primaryStage.show();
	}
	
	public Scene gamble(Stage primaryStage) {
		
		TextField bid = new TextField();
		bid.setPromptText("Enter bidding amount... and press ENTER");
		bid.setDisable(true);
		
		Text who = new Text();
		
		Button play = new Button("Play!");
		play.setDisable(true);
		
		// game
		play.setOnAction(data -> {info = new BaccaratInfo(Integer.parseInt(bid.getText()), who.getText());
			client.send(info);
		});

		bid.setOnAction(e -> 
			{ if (validInput(bid.getText()) && ready == true) {
				play.setDisable(false);
			}});
		
		Text biddingOn = new Text("Click who you would like to place your bet on");
		
		Button player = new Button("Player");
		Button banker = new Button("Banker");
		Button draw = new Button("Draw");
		
		player.setOnAction(e-> {who.setText("Player"); ready = true; bid.setDisable(false);
			player.setStyle("-fx-background-color: blue"); banker.setStyle("-fx-background-color: f#"); 
			draw.setStyle("-fx-background-color: f#");
		});
		
		banker.setOnAction(e-> {who.setText("Banker"); ready = true; bid.setDisable(false);
			player.setStyle("-fx-background-color: f#"); banker.setStyle("-fx-background-color: blue"); 
			draw.setStyle("-fx-background-color: f#");
		});
		
		draw.setOnAction(e-> {who.setText("Draw"); ready = true; bid.setDisable(false);
			player.setStyle("-fx-background-color: f#"); banker.setStyle("-fx-background-color: f#"); 
			draw.setStyle("-fx-background-color: blue");
		});
		
		HBox options = new HBox(player, banker, draw);
		
		VBox vbox = new VBox(biddingOn, options, bid, play);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(5, 10, 5, 10));
		
		Scene infoScene = new Scene(vbox, 300, 300);
		primaryStage.setScene(infoScene);
		primaryStage.show();
		return infoScene;
		
		
	}

	boolean validInput(String bet) {
		for (int i = 0; i < bet.length(); ++i) {
			if (bet.charAt(i) == 46) {
				continue;
			}
			if (bet.charAt(i) < 48 || bet.charAt(i) > 57) {
				return false;
			}
		}
		return true;
	}
	
	public void results(Stage primaryStage, BaccaratInfo data) {
		/*Once it receives the results of the game, your client program will display what was
		dealt for the Player and Banker’s hands and pause for a few seconds.
		• It will then end the game if there is a “natural” win and post a message and update
		the current winnings field or inform that user that there is no natural. Pause for a
		few seconds.*/
		
		primaryStage.setTitle("Part One");
		
		Text player = new Text("Player's Cards");
		Text cardsP = new Text("");
		
		for(int i = 0; i < 2; i++) {
			cardsP.setText(cardsP.getText() + "\n" + data.players.get(i).getSuite() + " " +  data.players.get(i).getValue() + "\n");
		}
		
		
		Text banker = new Text("Banker's Cards");
		Text cardsB = new Text("");
		
		for(int i = 0; i < 2; i++) {
			cardsB.setText(cardsB.getText() + "\n" + data.bankers.get(i).getSuite() + " " +  data.bankers.get(i).getValue() + "\n");
		}
		
		Text amountGambled = new Text("Betted: " + data.getBidAmount());
		Text winnings = new Text("Current Winning: " + 0.0);
		VBox money = new VBox(amountGambled, winnings);
		money.setAlignment(Pos.CENTER_LEFT);
		
		TextField results = new TextField("Results...");
		results.setFocusTraversable(false);
		results.setDisable(true);
		HBox info = new HBox(money, results);
		info.setPadding(new Insets(20, 20, 20 , 20));
		
		VBox playerSide = new VBox(player, cardsP);
		VBox bankerSide = new VBox(banker, cardsB);
		HBox both = new HBox(playerSide, bankerSide);
		both.setAlignment(Pos.CENTER);
		both.setPadding(new Insets(5, 10, 5, 10));
		
		Text title = new Text("GAMBLING!");
		BorderPane bpane = new BorderPane();
		bpane.autosize();
		bpane.setTop(title);
		bpane.setAlignment(title, Pos.CENTER);
		
		bpane.setCenter(both);
		bpane.setAlignment(both, Pos.CENTER);
		
		bpane.setBottom(info);
		bpane.setAlignment(info, Pos.BOTTOM_CENTER);
				
		if(data.getNatural()) {
			System.out.println("In Natural!!");
			winningField.setText(winningField.getText() + data.getWinning());
			results.setText("Natural Win\n" +
					"Player Total: " + data.getPValue() + "\n"
					+ "Banker Total: " + data.getBValue() + "\n"
					+ data.getWinner() + " wins" + "\n");
		}
	
		
		Scene result = new Scene(bpane, 500, 500);
		primaryStage.setScene(result);
		primaryStage.show();
		
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(event -> { 
			if(data.bExtraCard || data.pExtraCard) {
				results2(primaryStage, data);
				System.out.println("2");
			} else {
				finale(primaryStage, data);
				System.out.println("3");
			};});
		pause.play();
		
	}
	
	public void results2(Stage primaryStage, BaccaratInfo data) {
		/*• It will then display whether the Player got another card and display it or not. Then
		pause again.
		• It will then display whether the Banker got another card and display it or not. Then
		pause again*/
		
		primaryStage.setTitle("Part Two");
		
		Text player = new Text("Player's Cards");
		Text cardsP = new Text("");
		
		for(int i = 0; i < data.playerSize(); i++) {
			cardsP.setText(cardsP.getText() + "\n" + data.players.get(i).getSuite() + " " +  data.players.get(i).getValue() + "\n");
		}
		
		
		Text banker = new Text("Banker's Cards");
		Text cardsB = new Text("");
		
		for(int i = 0; i < data.bankerSize(); i++) {
			cardsB.setText(cardsB.getText() + "\n" + data.bankers.get(i).getSuite() + " " +  data.bankers.get(i).getValue() + "\n");
		}
		
		Text amountGambled = new Text("Betted: " + data.getBidAmount());
		Text winnings = new Text("Current Winning: " + 0/*data.getWinning()*/);
		VBox money = new VBox(amountGambled, winnings);
		money.setAlignment(Pos.CENTER_LEFT);
		
		TextField results = new TextField("Results...");
		results.setFocusTraversable(false);
		results.setDisable(true);
		HBox info = new HBox(money, results);
		info.setPadding(new Insets(20, 20, 20 , 20));
		
		VBox playerSide = new VBox(player, cardsP);
		VBox bankerSide = new VBox(banker, cardsB);
		HBox both = new HBox(playerSide, bankerSide);
		both.setAlignment(Pos.CENTER);
		both.setPadding(new Insets(5, 10, 5, 10));
		
		Text title = new Text("GAMBLING!");
		BorderPane bpane = new BorderPane();
		bpane.autosize();
		bpane.setTop(title);
		bpane.setAlignment(title, Pos.CENTER);
		
		bpane.setCenter(both);
		bpane.setAlignment(both, Pos.CENTER);
		
		bpane.setBottom(info);
		bpane.setAlignment(info, Pos.BOTTOM_CENTER);
	
		
		Scene scene_2 = new Scene(bpane, 500, 500);
		primaryStage.setScene(scene_2);
		primaryStage.show();
		
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(event -> { 
			finale(primaryStage, data);
			});
		pause.play();
	}
	
	public void finale(Stage primaryStage, BaccaratInfo data) {
		/*• It will then report the results of the game and update the current winnings field.*/
		primaryStage.setTitle("Part Three");
		
		Text player = new Text("Player's Cards");
		Text cardsP = new Text("");
		
		for(int i = 0; i < data.playerSize(); i++) {
			cardsP.setText(cardsP.getText() + "\n" + data.players.get(i).getSuite() + " " +  data.players.get(i).getValue() + "\n");
		}
		
		
		Text banker = new Text("Banker's Cards");
		Text cardsB = new Text("");
		
		for(int i = 0; i < data.bankerSize(); i++) {
			cardsB.setText(cardsB.getText() + "\n" + data.bankers.get(i).getSuite() + " " +  data.bankers.get(i).getValue() + "\n");
		}
		
	
		/*Text naturalMsg = new Text();
		
		if(data.getNatural()) {
			naturalMsg.setText("Natural Win");
			winningField.setText(winningField.getText() + data.getWinning());
		} else {
			naturalMsg.setText("Not a Natural Win");
		}*/
		
		Text amountGambled = new Text("Betted: " + data.getBidAmount());
		Text winnings = new Text("Current Winning: " + data.getWinning());
		VBox money = new VBox(amountGambled, winnings);
		money.setAlignment(Pos.CENTER_LEFT);
		
		Button newGame = new Button("New Game");
		Button exit = new Button("Exit");
		
		newGame.setOnAction(e-> {System.out.println("Entered New Game!!");/*data.again();*/ gamble(primaryStage);});
		
		exit.setOnAction(e-> Platform.exit());
		
		HBox buttons = new HBox(newGame, exit);
		buttons.setAlignment(Pos.CENTER_RIGHT);
		
		TextField results = new TextField(
				"Player Total: " + data.getPValue() + "\n"
				+ "Banker Total: " + data.getBValue() + "\n"
				+ data.getWinner() + " wins" + "\n");
		
		if(data.getBetOn() == data.getWinner()) {
			results.setText(results.getText() + " Congrats, you bet " + data.getBetOn() + "! You won your bet!");
		} else {
			results.setText(results.getText() + " Sorry, you bet " + data.getBetOn() + "! You lost your bet!");
		}
		
		results.setFocusTraversable(false);
		// results.setDisable(true);
		HBox info = new HBox(money, results, buttons);
		info.setPadding(new Insets(20, 20, 20 , 20));
		
		VBox playerSide = new VBox(player, cardsP);
		VBox bankerSide = new VBox(banker, cardsB);
		HBox both = new HBox(playerSide, bankerSide);
		both.setAlignment(Pos.CENTER);
		both.setPadding(new Insets(5, 10, 5, 10));
		
		Text title = new Text("GAMBLING!");
		BorderPane bpane = new BorderPane();
		bpane.autosize();
		bpane.setTop(title);
		bpane.setAlignment(title, Pos.CENTER);
		
		bpane.setCenter(both);
		bpane.setAlignment(both, Pos.CENTER);
		
		bpane.setBottom(info);
		bpane.setAlignment(info, Pos.BOTTOM_CENTER);
	
		Scene scene_3 = new Scene(bpane, 500, 500);
		primaryStage.setScene(scene_3);
		primaryStage.show();
	}
	
	
	
}

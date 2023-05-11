package application;
	
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.Label;
import application.Graph;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class Main extends Application {
    private Circle selectedCircle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Graph graf = new Graph();
        Pane pane = new Pane();
        int mode[] = {1}; // Mode odpowiada za decydowanie czy teraz rysujemy wierzchołki(true) czy krawędzie(false)

        ToggleButton button1 = new ToggleButton("Wierzchołki");
        ToggleButton button2 = new ToggleButton("Krawędzie");
        ToggleButton button3 = new ToggleButton("Dijkstra");

        button1.setSelected(true);
        button2.setSelected(false);
        button3.setSelected(false);

        button1.setLayoutX(20);
        button1.setLayoutY(20);

        button2.setLayoutX(120);
        button2.setLayoutY(20);
        
        button3.setLayoutX(220);
        button3.setLayoutY(20);

        pane.getChildren().addAll(button1, button2, button3);
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Wpisz odległość");
        dialog.setHeaderText("Podaj odległość:");

        pane.setOnMouseClicked(event -> {
            if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                double x = event.getX();
                double y = event.getY();

                if (mode[0]==1) {
                    Circle circle = new Circle(x, y, 20, Color.RED);
                    pane.getChildren().add(circle);
                    graf.addNode(x, y);
                    graf.showNodes();
                    
                    
                    circle.setOnMouseClicked(circleEvent -> {
                        if (circleEvent.getButton() == javafx.scene.input.MouseButton.PRIMARY && mode[0] == 2) {
                            if (selectedCircle != null && selectedCircle != circle) {
                                // Rysowanie linii między wybranymi kółkami
                                Line line = new Line(
                                        selectedCircle.getCenterX(), selectedCircle.getCenterY(),
                                        circle.getCenterX(), circle.getCenterY()
                                );
                                line.setStrokeWidth(10);
                                pane.getChildren().add(line);
                                line.toBack();
                                Optional<String> result = dialog.showAndWait();
                                if (result.isPresent()) {
                                    String input = result.get();
                                    try {
                                        int number = Integer.parseInt(input);
                                        graf.addEdge(graf.findIndex(selectedCircle.getCenterX(), selectedCircle.getCenterY()), graf.findIndex(circle.getCenterX(), circle.getCenterY()), number);
                                        graf.showEdges();
                                        Label label = new Label(Integer.toString(number));
                                        label.setFont(new Font(20));
                                        label.setTextFill(Color.MAGENTA);
                                        label.setLayoutX((line.getStartX() + line.getEndX()) / 2 + 20);
                                        label.setLayoutY((line.getStartY() + line.getEndY()) / 2 - 30);
                                        pane.getChildren().add(label);
                                        
                                    } catch (NumberFormatException e) {
                                        System.out.println("Niepoprawny format liczby");
                                    }
                                }
                                

                                // Przywracanie koloru poprzedniego kółka
                                selectedCircle.setFill(Color.RED);
                                selectedCircle = null;
                            } else if(selectedCircle != null && selectedCircle != circle){
                            	selectedCircle.setFill(Color.RED);
                                selectedCircle = null;
                            } else {
                                // Zmiana koloru kółka
                                circle.setFill(Color.BLUE);
                                selectedCircle = circle;
                            }
                        } else if (circleEvent.getButton() == javafx.scene.input.MouseButton.PRIMARY && mode[0] == 3){
                            graf.Dijkstra(graf.findIndex(circle.getCenterX(), circle.getCenterY()));
                        }
                    });
                } 
            }
        });

        pane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
            	
            	if(mode[0]==1) {
            		Circle circleToRemove = null;
                    for (javafx.scene.Node node : pane.getChildren()) {
                        if (node instanceof Circle && node.contains(event.getX(), event.getY())) {
                            circleToRemove = (Circle) node;
                            break;
                        }
                    }
                    if (circleToRemove != null) {
                        graf.removeNode(graf.findIndex(circleToRemove.getCenterX(), circleToRemove.getCenterY()));
                        graf.showNodes();
                        pane.getChildren().remove(circleToRemove);
                    }
            	} else if (mode[0] == 2){
            		if (event.getTarget() instanceof Line) {
            			
            			// Usuwanie linii
                        Line line = (Line) event.getTarget();
                        pane.getChildren().remove(line);
                        
                        // Usuwanie etykiety
                        Optional<Node> labelToRemove = pane.getChildren().stream()
                                .filter(node -> node instanceof Label)
                                .filter(node -> node.getBoundsInParent().intersects(line.getBoundsInParent()))
                                .findFirst();
                        
                        labelToRemove.ifPresent(pane.getChildren()::remove);
            			
                        graf.removeEdge(graf.findIndex(line.getStartX(), line.getStartY()), graf.findIndex(line.getEndX(), line.getEndY()));
                        graf.showEdges();
                    }
            	}
            }
        });

        button1.setOnAction(event -> {
            button2.setSelected(false);
            button3.setSelected(false);
            mode[0] = 1;
        });

        button2.setOnAction(event -> {
            button1.setSelected(false);
            button3.setSelected(false);
            mode[0] = 2;
        });
        
        button3.setOnAction(event -> {
            button1.setSelected(false);
            button2.setSelected(false);
            mode[0] = 3;
        });

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Kreator grafów");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

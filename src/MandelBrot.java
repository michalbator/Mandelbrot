import javafx.scene.paint.Color;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import java.lang.Math;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import java.util.Scanner;
import javafx.scene.layout.HBox;

public class MandelBrot extends Application{
	public void start(Stage primaryStage){
		primaryStage.setTitle("Mandelbrot Generator");
		int max = 100;
		Fractal mandelBrot = new Fractal(max);
		StackPane root = new StackPane();
		ImageView imageView = new ImageView();
		WritableImage fractalImage = mandelBrot.draw();
		imageView.setImage(fractalImage);
		Label settingsLabel = new Label("Ustawienia: ");
		Button zoomOut = new Button("Zoom Out");
		TextField iterationInputTextField = new TextField(Integer.toString(mandelBrot.getMaxIterations()));
		TextField zoomInTextField = new TextField(Double.toString(mandelBrot.getZoom()));
		Button refresh = new Button("Odswiez");

		refresh.setOnAction(new EventHandler <ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				int newIterationInt = getIterationInt(iterationInputTextField);
				mandelBrot.setMaxIterations(newIterationInt);
				mandelBrot.setPoint(1);
				WritableImage fractalImage = mandelBrot.draw();
				imageView.setImage(fractalImage);
			}
		}
		);

		zoomOut.setOnAction(new EventHandler <ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				int newIterationInt = getIterationInt(iterationInputTextField);
				mandelBrot.setMaxIterations(newIterationInt);
				double zoom = 1/getZoomDouble(zoomInTextField);
				mandelBrot.setPoint(zoom);
				WritableImage fractalImage = mandelBrot.draw();
				imageView.setImage(fractalImage);
			}
		}
		);

		root.setOnMouseClicked(
		new EventHandler<MouseEvent>(){
			@Override
   			public void handle(MouseEvent event){
				int newIterationInt = getIterationInt(iterationInputTextField);
				mandelBrot.setMaxIterations(newIterationInt);
				double zoom = getZoomDouble(zoomInTextField);
				mandelBrot.setZoom(zoom);
   				double x = event.getSceneX();
   				double y = event.getSceneY();
   				mandelBrot.setPoint(x, y, zoom);
   				WritableImage fractalImage = mandelBrot.draw();
				imageView.setImage(fractalImage);
   			}
   		}
		);

		VBox vbox = new VBox();
		HBox hbox = new HBox();
		hbox.getChildren().addAll(zoomOut, iterationInputTextField, zoomInTextField, refresh);
		vbox.getChildren().addAll(imageView, settingsLabel, hbox);
		root.getChildren().addAll(vbox);
		Scene scene = new Scene(root, mandelBrot.getResX(), (mandelBrot.getResX()*mandelBrot.getRatioRes())+45);
		primaryStage.setScene(scene);
		primaryStage.show();
        
	}
	public static int getIterationInt(TextField textField){
		String newIteration = textField.getText();
		int newIterationInt = Integer.parseInt(newIteration);
		return newIterationInt;
	}
	public static double getZoomDouble(TextField textField){
		String zoomInString = textField.getText();
		double zoom = Double.parseDouble(zoomInString);
		return zoom;
	}
	public static void main(String args[]){
		Application.launch(args);
	}
}

class Complex{

	private double re, im;

	Complex(){
		this.re = 0;
		this.im = 0;
	}

	Complex(double x, double y){
		this.re = x;
		this.im = y;
	}

	Complex(Complex n){
		this.re = n.re;
		this.im = n.im;
	}

	Complex add(Complex n){
		this.re += n.re;
		this.im += n.im;
		return this;
	}

	Complex nextY(double n){
		this.im -= n;
		return this;
	}

	Complex nextX(double n){
		this.re += n;
		return this;
	}

	Complex pow(){
		double x = ((this.re*this.re) - (this.im*this.im));
        double y  = 2*this.re*this.im;
        this.re = x;
        this.im = y;
        return this;
	}
	Complex mul(Complex n){
		double x = (re*n.re) - (this.im*n.im);
		double y = (re*n.im) + (n.re*this.im);
		this.re = x;
		this.im = y;
		return this;
	}

	double abs(){
		double x = this.re*this.re + this.im*this.im;
		return x;
	}

	double getRe(){
		return this.re;
	}

	double getIm(){
		return this.im;
	}

	void setRe(double x){
		this.re = x;
	}

	void setIm(double x){
		this.im = x;
	}

	String toStr(){
  	 	String ch = new String();
    	if(this.im >= 0){
    		ch = "+";
		}
		String res = Double.toString(this.re) + ch + Double.toString(this.im) + "i";
		return res;
	}
}

class Fractal{
	private Complex leftUp, rightDown; //punkty wyznaczajace obszar do wyswietlenia
	private double step; //liczba wyznaczajaca roznice miedzy 1 pikselem przelozona na plaszczyzne zespolona
	private int maxIterations;
	private double zoom;
	private final double ratioRes = 2.0/3.0;
	private double resX;

	Fractal(){
		this.leftUp = new Complex(-2.0, 1.0);
		this.rightDown = new Complex(1.0, -1.0);
		this.zoom = 2.0;
		this.resX = 800;
	}

	Fractal(int maxIterations){
		this.leftUp = new Complex(-2.0, 1.0);
		this.rightDown = new Complex(1.0, -1.0);
		this.maxIterations = maxIterations;
		this.zoom = 2.0;
		this.resX = 800;
	}

	double getResX(){
		return this.resX;
	}

	void setResX(double resX){
		this.resX = resX;
	}

	double getRatioRes(){
		return this.ratioRes;
	}

	double getZoom(){
		return this.zoom;
	}

	void setZoom(double zoom){
		this.zoom = zoom;
	}

	int getMaxIterations(){
		return this.maxIterations;
	}

	void setMaxIterations(int x){
		this.maxIterations = x;
	}

	void setPoint(double x, double y, double zoom){
		// przelicza rogi obszaru do wyswietlenia na podstawie wspolrzednych myszki i podanego zoomu
		double ratioX = x/this.resX;
		double ratioY = y/(this.resX*this.ratioRes);
		double width = -(this.leftUp.getRe() - this.rightDown.getRe()); // szerokosc obszaru
		double midX = width*ratioX; // x-wspolrzedna srodka obszaru
		midX += this.leftUp.getRe();
		double height = this.leftUp.getIm() - this.rightDown.getIm(); // wysokosc obszaru
		double midY = height*ratioY; // y-wspolrzedna srodka obszaru
		midY += this.rightDown.getIm();
		width = width/zoom/2; // polowa nowej szerokosci obszaru
		height = height/zoom/2; // polowa nowej wysokosci obszaru
		// ustawienie nowych wspolrzednych rogow obszaru
		this.leftUp.setRe(midX - width);
		this.leftUp.setIm(midY + height);
		this.rightDown.setRe(midX + width);
		this.rightDown.setIm(midY - height);
	}

	void setPoint(double zoom){
		setPoint(this.resX/2, this.resX*ratioRes/2, zoom);
	}

	void setPoint(double x, double y){
		setPoint(x, y, this.zoom);
	}
	
	double calcStep(){
		double width = -(this.leftUp.getRe() - this.rightDown.getRe());
		double step = width/this.resX;
		this.step = step;
		System.out.println(step);
		return step;
	}

	Color calcColor(Complex point){
		int iteration = 0;
		Complex temp = new Complex();
		int max = this.maxIterations;
		while (temp.abs() < 4 && iteration < max){
            temp.mul(temp).add(point);
            iteration++;
        }
		if(iteration == max){
			return Color.BLACK;
		}
		else{
			return Color.hsb(iteration/256f, iteration/(iteration+8f), 1);
		}
	}

	WritableImage draw(){
		double resY = this.resX * this.ratioRes;
		WritableImage fractal = new WritableImage((int)this.resX, (int)resY);
		PixelWriter image = fractal.getPixelWriter();
		Complex point = new Complex(this.leftUp);
		this.step = this.calcStep();
		int i = 0;
		for(int x = 0; x < (int)this.resX; x++){
			for(int y = 0; y < (int)resY; y++){
				Color color = calcColor(point);
				image.setColor(x, (int)resY-y-1, color);
				point.nextY(this.step);
			}
			point.nextX(this.step);
			point.setIm(this.leftUp.getIm());
		}
		return fractal;
	}
}

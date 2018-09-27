package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.stream.Stream;

public class ImageDB extends Application {
    double imageWidth = 400;
    double imageHeight = 200;
    StackPane stackPileImages;
    StackPane stackPaneViewA;
    StackPane stackPaneViewB;

    public static final int BORDURE = 20;
    boolean imagePaire = true;
    Font font12 = Font.font("System", FontWeight.NORMAL, 12);
    private double ratioCourant = 1.0;


    // On ne peut utiliser le setPreserveRatio() si on veut détecter
    // le moment où le curseur entre dans l'image (ImageView).  En mode
    // preserveRatio(), le ImageView détecte l'entrée du curseur à l'extérieur
    // de l'image

    @Override
    public void start(Stage root) {

        Image imgRef = this.createImage(Color.CORAL);
        imageWidth = imgRef.getWidth();
        imageHeight = imgRef.getHeight();
        double imageRatio = imageWidth/imageHeight;
        Image imageA = this.createImage(Color.CORNFLOWERBLUE);
        Image imageB = this.createImage(Color.CORAL);
        ImageView imageViewA = new ImageView(imageA);
        ImageView imageViewB = new ImageView(imageB);
        imageViewA.setOnMouseClicked(event -> {
            System.out.println("click!");
        });
        //imageViewA.setPreserveRatio(true);
        //imageViewB.setPreserveRatio(true);
        //imageViewA.setStyle("-fx-border-color: green;  -fx-border-insets: 4 4 4 4;");
        //imageViewB.setStyle("-fx-border-color: yellow;  -fx-border-insets: 4 4 4 4;");

        Canvas canvasA = new Canvas(imageWidth, imageHeight);
        Canvas canvasB = new Canvas(imageWidth, imageHeight);

        stackPaneViewA = new StackPane(imageViewA, canvasA);
        stackPaneViewB = new StackPane(imageViewB, canvasB);

        imageViewA.fitWidthProperty().bind(stackPaneViewA.prefWidthProperty());
        imageViewA.fitHeightProperty().bind(stackPaneViewA.prefHeightProperty());

        imageViewB.fitWidthProperty().bind(stackPaneViewB.prefWidthProperty());
        imageViewB.fitHeightProperty().bind(stackPaneViewB.prefHeightProperty());

        // La dimension du canvas doit suivre celle de l'ImageView
        canvasA.widthProperty().bind(imageViewA.fitWidthProperty());
        canvasA.heightProperty().bind(imageViewA.fitHeightProperty());
        canvasB.widthProperty().bind(imageViewB.fitWidthProperty());
        canvasB.heightProperty().bind(imageViewB.fitHeightProperty());

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                System.out.println(imageViewA.getBoundsInLocal());
                System.out.println(imageViewA.getBoundsInParent());
                System.out.println(imageViewA.getLayoutBounds());
                System.out.println("-----------");

                GraphicsContext g2d;
                if (imagePaire) {
                    Bounds boundsInParent = imageViewA.getBoundsInParent();
                    g2d = canvasA.getGraphicsContext2D();
                    g2d.setFont(font12);
                    g2d.clearRect(0, 0, canvasA.getWidth(), canvasA.getHeight());
                    /*
                    g2d.strokeLine(boundsInParent.getMinX(),
                            boundsInParent.getMinY(),
                            boundsInParent.getMaxX(),
                            boundsInParent.getMaxY());*/
                    g2d.strokeLine(0, 0, imageWidth, imageHeight);//canvasA.getWidth(), canvasA.getHeight());
                    g2d.strokeText(canvasA.getLayoutBounds().toString(), 10, 50);
                    g2d.strokeText(imageViewA.getLayoutBounds().toString(), 10, 75);
                } else {
                    Bounds boundsInParent = imageViewB.getBoundsInParent();
                    g2d = canvasB.getGraphicsContext2D();
                    g2d.setFont(font12);
                    g2d.clearRect(0, 0, canvasB.getWidth(), canvasB.getHeight());
                    /*
                    g2d.strokeLine(boundsInParent.getMaxX(),
                            boundsInParent.getMinY(),
                            boundsInParent.getMinX(),
                            boundsInParent.getMaxY());*/
                    g2d.strokeLine(0, 0, imageWidth, imageHeight);//canvasB.getWidth(), canvasB.getHeight());

                    g2d.strokeText(canvasB.getLayoutBounds().toString(), 10, 50);
                    g2d.strokeText(imageViewB.getLayoutBounds().toString(), 10, 75);
                }
            }
        };

        stackPaneViewA.setStyle("-fx-border-color: red;-fx-border-insets: 4 4 4 4;");
        stackPaneViewA.setOpacity(1.0);
        stackPaneViewB.setStyle("-fx-border-color: green;-fx-border-insets: 4 4 4 4;");
        stackPaneViewB.setOpacity(1.0);

        stackPileImages = new StackPane(stackPaneViewA, stackPaneViewB);
        stackPileImages.setMinSize(10, 10);
        stackPileImages.setMaxSize(1000, 1000);
        stackPileImages.setStyle("-fx-border-color: red; -fx-border-insets: 4 4 4 4;");
        stackPileImages.setMinSize(2.0,2.0);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(stackPileImages);
        scrollPane.setMinViewportHeight(5.0);
        scrollPane.setMinViewportWidth(5.0);
        scrollPane.setMinSize(5.0,5.0);

        stackPileImages.widthProperty().addListener(changeListener);
        stackPileImages.heightProperty().addListener(changeListener);


        // Structure
        // scrollPane
        //    -------->stackPileImages
        //                      ----------->stackPaneViewA
        //                                      ------->imageViewA
        //                                      ------->canvasA
        //                      ----------->stackPaneViewB
        //                                      ------->imageViewB
        //                                      ------->canvasB
        //
        //
        // imageViewX.fitWidthProperty().bind(stackPaneViewX.prefWidthProperty())
        // canvasX.widthProperty().bind(imageViewX.fitWidthProperty());
        //
        // on sélectionne l'image visible avec stackPaneViewX.toFront()

        stackPaneViewA.toFront();

        this.center(scrollPane.getViewportBounds(), stackPileImages);
        scrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            // Mode dimension fixe
            if (newValue.getWidth() > 400 && newValue.getHeight() > 600) {
                stackPaneViewA.setScaleX(1.0);
                stackPaneViewB.setScaleX(1.0);
                stackPaneViewA.setScaleY(1.0);
                stackPaneViewB.setScaleY(1.0);
                double hImageView = 1.5*imageHeight;
                double wImageView = 1.5*imageWidth;
                stackPaneViewA.setPrefWidth(wImageView);            // A chaque fois
                stackPaneViewA.setPrefHeight(hImageView);
                stackPaneViewB.setPrefWidth(wImageView);  // A chaque fois
                stackPaneViewB.setPrefHeight(hImageView);
                this.center(newValue, stackPileImages);
            } else {
                // FIT
                double wDisponible = newValue.getWidth() - BORDURE;
                double hDisponible = newValue.getHeight() - BORDURE;
                ratioCourant = wDisponible / hDisponible;
                double hImageView;
                double wImageView;
                if (ratioCourant > imageRatio) {
                    // on décide de la hauteur, et on ajuste la largeur
                    hImageView = hDisponible;
                    wImageView = hDisponible * imageRatio;
                } else {
                    // on décide de la largeur, on ajuste la hauteur
                    wImageView = wDisponible;
                    hImageView = wDisponible * 1. / imageRatio;
                }

                double ratio = wImageView/imageWidth;
                stackPileImages.setScaleX(ratio);
                stackPileImages.setScaleY(ratio);
                stackPaneViewA.setScaleX(1.0);
                stackPaneViewB.setScaleX(1.0);
                stackPaneViewA.setScaleY(1.0);
                stackPaneViewB.setScaleY(1.0);
                /*stackPaneViewA.setScaleX(ratio);
                stackPaneViewB.setScaleX(ratio);
                stackPaneViewA.setScaleY(ratio);
                stackPaneViewB.setScaleY(ratio);

                stackPaneViewA.setPrefWidth(wImageView);            // A chaque fois
                stackPaneViewA.setPrefHeight(hImageView);
                stackPaneViewB.setPrefWidth(wImageView);  // A chaque fois
                stackPaneViewB.setPrefHeight(hImageView);
*/
                //stackPileImages.setScaleX(ratio);
                //stackPileImages.setScaleY(ratio);
                stackPileImages.setTranslateX((newValue.getWidth() - imageWidth*ratio - BORDURE)/2);
                stackPileImages.setTranslateY((newValue.getHeight() - imageHeight*ratio-BORDURE)/2);
                /*stackPileImages.setTranslateX((newValue.getWidth()-wImageView-BORDURE)/2);
                stackPileImages.setTranslateY((newValue.getHeight()-hImageView-BORDURE)/2);
                */
            }

            if (imagePaire) {
                stackPaneViewA.toFront();
            } else {
                stackPaneViewB.toFront();
            }
            imagePaire = !imagePaire;
        });



        StackPane stackPaneGlobal = new StackPane();
        stackPaneGlobal.setStyle("-fx-border-color: green;  -fx-border-insets: 18 18 18 18;");
        EtiquetteFlottante etiquette = new EtiquetteFlottante();
        // Do not grab mouse attention
        etiquette.getGroup().setMouseTransparent(true);
        Stream.of(stackPaneViewA, stackPaneViewB/*imageViewA, imageViewB, canvasA, canvasB*/).forEach(stackPane -> {
            stackPane.setOnMouseEntered(event -> {
                System.out.println("Enter imageView!"+stackPane);
                etiquette.getGroup().setVisible(true);
            });
            stackPane.setOnMouseMoved(event -> {
                etiquette.getLabel().setText(String.format("Local[%f:%f]\nScene[%f:%f]\nStack[%f:%f]\nImageViewTX[%f:%f:%f]\nImageViewSX[%f:%f:%f]",
                        event.getX(), event.getY(),
                        event.getSceneX(), event.getSceneY(),
                        stackPileImages.getTranslateX(), stackPileImages.getTranslateY(),
                        stackPileImages.getTranslateX(), stackPaneViewA.getTranslateX(), imageViewA.getScaleX(),
                        stackPileImages.getScaleX(), stackPaneViewA.getScaleX(), imageViewA.getScaleX()));
                double x = event.getSceneX();
                double y = event.getScreenY();
                Point2D ptLocal = stackPaneGlobal.screenToLocal(event.getScreenX(), event.getScreenY());
                Point2D pts = etiquette.getGroup().sceneToLocal(x,y);
                etiquette.getGroup().relocate(ptLocal.getX()+5, ptLocal.getY()- etiquette.getGroup().getLayoutBounds().getHeight()-5);//x, y - etiquette.getLabel().getHeight() - 25);
            });
            stackPane.setOnMouseExited(event -> {
                System.out.println("Exit imageView!"+stackPane);
                etiquette.getGroup().setVisible(false);
            });
        });

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        addStackPane(hbox);
        border.setCenter(scrollPane);
        stackPaneGlobal.getChildren().addAll(border, etiquette.getGroup());
        root.setScene(new Scene(stackPaneGlobal));
        root.setMaximized(true);
        root.show();
    }

    private void center(Bounds viewPortBounds, StackPane centeredNode) {

        double width = viewPortBounds.getWidth();
        double height = viewPortBounds.getHeight();

        if (width > (imageWidth*centeredNode.getScaleX() + BORDURE)) {
            centeredNode.setTranslateX((width - imageWidth*centeredNode.getScaleX() - BORDURE) / 2);
        } else {
            centeredNode.setTranslateX(0);
        }
        if (height > (imageHeight*centeredNode.getScaleX() + BORDURE)) {
            centeredNode.setTranslateY((height - imageHeight*centeredNode.getScaleY() - BORDURE) / 2);
        } else {
            centeredNode.setTranslateY(0);
        }
    }

    private Image createImage(Color color) {
        return new Image(getClass().getResourceAsStream("/x420.png"));

        //return new Rectangle(ImageDB.imageWidth, ImageDB.imageHeight, color).snapshot(null, null);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }



    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonCurrent = new Button("Current");
        buttonCurrent.setPrefSize(100, 20);

        Button buttonProjected = new Button("Projected");
        buttonProjected.setPrefSize(100, 20);
        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;
    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Data");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Hyperlink options[] = new Hyperlink[]{
                new Hyperlink("Sales"),
                new Hyperlink("Marketing"),
                new Hyperlink("Distribution"),
                new Hyperlink("Costs")};

        for (int i = 0; i < 4; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }

        return vbox;
    }

    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                        new Stop(0, Color.web("#4977A3")),
                        new Stop(0.5, Color.web("#B0C6DA")),
                        new Stop(1, Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }

}
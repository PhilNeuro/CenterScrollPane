package sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EtiquetteFlottante {
    Font theFont2 = Font.font("System", FontWeight.NORMAL, 12);
    private final Label label;
    private final Group group;

    public EtiquetteFlottante() {
        // Ajouter un petit label pour tester comme il faut
        label = new Label("Texte mobile");
        label.setTextFill(Color.BLACK);
        label.setFont(theFont2);
        label.setVisible(true);
        label.setOpacity(1.0);
        label.setScaleX(1.0);
        label.setScaleY(1.0);
        label.setWrapText(true);
        StackPane stackLabel = new StackPane(label);
        stackLabel.setPadding(new Insets(5, 5, 5, 5));
        stackLabel.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, new CornerRadii(3), Insets.EMPTY)));
        stackLabel.setVisible(true);
        group = new Group(stackLabel);
        group.setManaged(false);
        group.setVisible(true);
    }

    public Label getLabel() {
        return label;
    }

    public Group getGroup() {
        return group;
    }
}

/**
 *
 */
package ar.utn.thegrid.cpm;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**Engloba el comportamiento deseado
 * de los drag and drops...
 *
 * @author eayzenberg
 */
public class CargadorListenersDragAndDrop {

	Integer prevLblCordX,prevLblCordY,prevMouseCordX,prevMouseCordY;
	private int diffX;
	private int diffY;

	public CargadorListenersDragAndDrop(Nodo nodo, AnchorPane lienzo) {

		VBox contenedor = nodo.getContenedor();
		contenedor.setOnDragDetected(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
//				System.out.println("DragDetected");
				prevLblCordX= (int) contenedor.getLayoutX();
			    prevLblCordY= (int) contenedor.getLayoutY();
			    prevMouseCordX= (int) event.getX();
			    prevMouseCordY= (int) event.getY();
			}
		});

		contenedor.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent m) {
				if (prevLblCordX == null) return;
//				System.out.println("DragEntered");
				diffX= (int) (m.getX()- prevMouseCordX);
			    diffY= (int) (m.getY()-prevMouseCordY );
			    int x = (int) (diffX+contenedor.getLayoutX()-lienzo.getLayoutX());
			    int y = (int) (diffY+contenedor.getLayoutY()-lienzo.getLayoutY());
			    if (y > 0 && x > 0 && y < lienzo.getHeight() && x < lienzo.getWidth())
			    {
			    	contenedor.setLayoutX(x);
			    	contenedor.setLayoutY(y);
			    }
			}
		});


	}
}

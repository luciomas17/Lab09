/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {

	Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCalcolaConfini(ActionEvent event) {
		this.txtResult.clear();
		
		try {
			int year = Integer.parseInt(this.txtAnno.getText().trim());
			
			if(year < 1816 || year > 2016) {
				this.txtResult.appendText("Errore: range anno non valido. Inserire un anno compreso tra il 1816 e il 2016 inclusi.");
				return;
			}
			
			model.createGraph(year);
			
			Set<Country> vertexSet = model.getVertexSet();		
			for(Country c : vertexSet)
				this.txtResult.appendText(String.format("Stato: %s, Confinanti: %d\n", c.getStateName(), model.findDegree(c)));
			
			this.txtResult.appendText("\nComponenti connesse: " + model.getNumberOfConnectedComponents());
			
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Errore: input anno non valido.");
			e.printStackTrace();
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
}

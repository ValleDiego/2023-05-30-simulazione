package it.polito.tdp.gosales;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import it.polito.tdp.gosales.model.Arco;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.Retailers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnalizzaComponente;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<String> cmbNazione;

    @FXML
    private ComboBox<?> cmbProdotto;

    @FXML
    private ComboBox<Retailers> cmbRivenditore;

    @FXML
    private TextArea txtArchi;

    @FXML
    private TextField txtN;

    @FXML
    private TextField txtNProdotti;

    @FXML
    private TextField txtQ;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextArea txtVertici;

    @FXML
    void doAnalizzaComponente(ActionEvent event) {
    	String msg = "";
    	Retailers r = this.cmbRivenditore.getValue();
    	if(r == null) {
    		this.txtResult.setText("Prima devi creare un grafo, oppure scegliere una componente connessa\n");
    		return;
    	}
    	msg = this.model.doAnalizzaComponente(r);
    	txtResult.appendText("\n"+msg);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtVertici.clear();
    	txtArchi.clear();
    	
    	String nazione = this.cmbNazione.getValue();
    	if(nazione == null) {
    		this.txtResult.setText("Seleziona una nazioen\n");
    		return;
    	}
    	
    	Integer anno = this.cmbAnno.getValue();
    	if(anno == null) {
    		this.txtResult.setText("Seleziona un anno\n");
    		return;
    	}
    	
    	int nMin = 0;
    	try {
    		nMin = Integer.parseInt(this.txtNProdotti.getText());
    	}catch (NumberFormatException e ) {
    		this.txtResult.setText("Inserisci un numero\n");
    		return;
    	}
    	if (nMin < 0) {
    		this.txtResult.setText("Inserisci un numero non negativo\n");
    	}
    	
    	String msg = this.model.creaGrafo(nazione, anno, nMin);
      	txtResult.appendText(msg);
      	 //DIEGOOOO!! SE VUOI L'ORDINAMENTO, SERVE LA LIST
      	Map<Integer, Retailers> retailersMap = new HashMap<Integer, Retailers>();
      	retailersMap.putAll(this.model.getVertici());
      	for(Retailers v : retailersMap.values()) {
      		txtVertici.appendText(v.toString()+"\n");
      	}
      	
      	List<Arco> archi = new ArrayList<Arco>();
      	 archi.addAll(this.model.getArchi());
      	 for(Arco a : archi) {
      		 txtArchi.appendText(a.getPeso()+" - "+retailersMap.get(a.getV1())+"<-->"+ retailersMap.get(a.getV2())+"\n"   );
      	 }
      	 this.cmbRivenditore.setDisable(false);
      	 this.btnAnalizzaComponente.setDisable(false);
      	this.cmbRivenditore.getItems().clear();
      	this.cmbRivenditore.getItems().addAll(retailersMap.values());
      	 
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btnAnalizzaComponente != null : "fx:id=\"btnAnalizzaComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProdotto != null : "fx:id=\"cmbProdotto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbRivenditore != null : "fx:id=\"cmbRivenditore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtArchi != null : "fx:id=\"txtArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNProdotti != null : "fx:id=\"txtNProdotti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtQ != null : "fx:id=\"txtQ\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtVertici != null : "fx:id=\"txtVertici\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	for(int i=2015; i<=2018; i++) {
    		this.cmbAnno.getItems().add(i);
    	}
    	
    	this.cmbNazione.getItems().addAll(this.model.getNazioni());
    }

}

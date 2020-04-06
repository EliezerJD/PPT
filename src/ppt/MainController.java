/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppt;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author manolo
 */
public class MainController implements Initializable {
    @FXML
    private ImageView piedra;
    @FXML
    private ImageView papel;
    @FXML
    private ImageView tijera;
    @FXML
    private Label esperar;
    Socket socket;
    String seleccion;
    Semaphore semaforoTiro = new Semaphore(0);
    String player = null;
    @FXML
    private Rectangle tapar;
    @FXML
    private Label ganador;
    @FXML
    private Label empate;
    @FXML
    private Label perdedor;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            socket = IO.socket("http://localhost:3000", opts);
            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        socket.on("obtenerPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(player == null){
                    player=(String)args[0];
                } 
            }
        });
        socket.emit("conectarse", "");
    }    

    @FXML
    private void piedra(MouseEvent event) {
        tapar.setVisible(true);
        seleccion = "piedra";
        tirar();
    }

    @FXML
    private void papel(MouseEvent event) {
        tapar.setVisible(true);
        seleccion = "papel";
        tirar();
    }

    @FXML
    private void tijera(MouseEvent event) {
        tapar.setVisible(true);
        seleccion = "tijera";
        tirar();
    }
    
    public void tirar(){
        HiloTirar ht = new HiloTirar(seleccion, semaforoTiro, socket, player, tapar, esperar, ganador, perdedor, empate);
        ht.start();
    }
    
}

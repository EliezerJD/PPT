/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppt;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author manolo
 */
public class HiloTirar extends Thread {

    Socket socket;
    String seleccion;
    Semaphore semaforoTiro;
    String player;
    Label esperar;
    Rectangle tapar;
    String ganador;
    Label ganadorL;
    Label perdedor;
    Label empate;

    HiloTirar(String seleccion, Semaphore semaforoTiro, Socket socket, String player, Rectangle tapar, Label esperar, Label ganadorL, Label perdedor, Label empate) {
        this.player = player; this.seleccion = seleccion; this.semaforoTiro = semaforoTiro; this.socket = socket;
        this.esperar = esperar; this.tapar = tapar; this.ganadorL = ganadorL; this.perdedor = perdedor; this.empate = empate;
    }
    @Override
    public void run() {
        socket.on(player+"esperar", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                esperar.setVisible(true);     
            }
        });
        socket.on("ambostiraron", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                esperar.setVisible(false);
                
            }
        });
        socket.on("ganador", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ganador = (String)args[0];
                tapar.setFill(Color.WHITE);
                semaforoTiro.release();
            }
        });
        socket.emit("tiro"+player, seleccion);
         
        try {
            semaforoTiro.acquire();
            if(ganador.equals(player)){
                ganadorL.setVisible(true);
            }else if(ganador.equals("empate")){
                empate.setVisible(true);
            }else{
                perdedor.setVisible(true);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloTirar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
        
    
}

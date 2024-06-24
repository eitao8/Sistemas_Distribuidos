/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Servidor;

/**
 *
 * @author Eitão
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
    public static void main(String[] args) {
        try {
            QuadraImpl quadra = new QuadraImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("QuadraService", quadra);
            System.out.println("Servidor Iniciado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

/**
 *
 * @author Eitão
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceQuadra extends Remote {
    boolean reservarHorario(String nome, String horario) throws RemoteException;
    boolean liberarHorario(String horario) throws RemoteException;
    List<String> listarHorarios() throws RemoteException;
    List<String> listarReservas() throws RemoteException;
}



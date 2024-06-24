/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

/**
 *
 * @author Eitão
 */
import Controle_Concorrencia.ControleConcorrencia;
import Controle_Concorrencia.Operacao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class QuadraImpl extends UnicastRemoteObject implements InterfaceQuadra {
    private final Map<String, String> reservas;
    private final ControleConcorrencia controleConcorrencia;
    private final Map<String, List<String>> filaDeEspera; 

    protected QuadraImpl() throws RemoteException {
        reservas = new HashMap<>();
        controleConcorrencia = new ControleConcorrencia();
        filaDeEspera = new HashMap<>(); 
        
        for (int i = 8; i <= 22; i++) {     // Horarios
            reservas.put(i + ":00", null);
        }
    }

    @Override
    public boolean reservarHorario(String nome, String horario) throws RemoteException {
        ArrayList<Operacao> operacoes = new ArrayList<>();
        operacoes.add(new Operacao("w", "reservar_" + nome));

        Thread thread = Thread.currentThread();
        controleConcorrencia._2PL(operacoes, thread);

        synchronized (this) {
            if (reservas.get(horario) == null) {
                reservas.put(horario, nome);
                return true;
            } else {
       
                filaDeEspera.computeIfAbsent(horario, k -> new ArrayList<>()).add(nome);
                System.out.println(nome + " foi adicionado a fila de espera para " + horario);
                return false;
            }
        }
    }

    @Override
    public boolean liberarHorario(String horario) throws RemoteException {
        ArrayList<Operacao> operacoes = new ArrayList<>();
        operacoes.add(new Operacao("w", "liberar_" + horario));

        Thread thread = Thread.currentThread();
        controleConcorrencia._2PL(operacoes, thread);

        synchronized (this) {
            if (reservas.get(horario) != null) {
                reservas.put(horario, null);
                System.out.println("Horario " + horario + " liberado.");

                // Verifica a fila de espera e processa a próxima reserva
                if (filaDeEspera.containsKey(horario) && !filaDeEspera.get(horario).isEmpty()) {
                    String proximoNaFila = filaDeEspera.get(horario).remove(0);
                    reservas.put(horario, proximoNaFila);
                    System.out.println(proximoNaFila + " foi movido da fila de espera para o horario " + horario);
                }

                return true;
            }
            return false;
        }
    }

    @Override
    public List<String> listarHorarios() throws RemoteException {
        return new ArrayList<>(reservas.keySet());
    }

    @Override
    public List<String> listarReservas() throws RemoteException {
        List<String> listaReservas = new ArrayList<>();
        synchronized (this) {
            for (Map.Entry<String, String> entry : reservas.entrySet()) {
                if (entry.getValue() != null) {
                    listaReservas.add(entry.getKey() + " - " + entry.getValue());
                }
            }
        }
        return listaReservas;
    }
}
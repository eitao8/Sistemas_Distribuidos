/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

/**
 *
 * @author Eitão
 */
import Servidor.InterfaceQuadra;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente implements InterfaceCliente {
    private InterfaceQuadra quadraService;

    public Cliente() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            quadraService = (InterfaceQuadra) registry.lookup("QuadraService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Reservar Horario");
            System.out.println("2. Liberar Horario");
            System.out.println("3. Listar Horarios");
            System.out.println("4. Listar Reservas");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opcao: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite seu nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Digite o horario (ex: 10:00): ");
                    String horario = scanner.nextLine();
                {
                    try {
                        if (quadraService.reservarHorario(nome, horario)) {
                            System.out.println("\nHorario reservado");
                        } else {
                            System.out.println("\nFalha ao reservar horario");
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;

                case 2:
                    System.out.print("Digite o horario (ex: 10:00): ");
                    horario = scanner.nextLine();
                {
                    try {
                        if (quadraService.liberarHorario(horario)) {
                            System.out.println("\nHorario liberado");
                        } else {
                            System.out.println("\nFalha ao liberar horario");
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;

                case 3:
                    List<String> horarios = null;
                try {
                    horarios = quadraService.listarHorarios();
                } catch (RemoteException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                    System.out.println("Horarios disponiveis:");
                    for (String h : horarios) {
                        System.out.println(h);
                    }
                    break;

                case 4:
                    List<String> reservas = null;
                try {
                    reservas = quadraService.listarReservas();
                } catch (RemoteException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                    System.out.println("Reservas:");
                    for (String r : reservas) {
                        System.out.println(r);
                    }
                    break;

                case 5:
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.exibirMenu();
    }
}
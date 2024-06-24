/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controle_Concorrencia;

/**
 *
 * @author Eitão
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ControleConcorrencia {

    private boolean leitura = true;
    private boolean escrita = true;

    public static boolean liberouTrancas = true;
    public static List<tranca> trancas = new ArrayList<>();
    public static List<Operacao> fila = new ArrayList<>();  

    public synchronized void liberarTrancas(ArrayList<Operacao> operacoes, Thread thrd) {
        liberouTrancas = false; // durante a liberacao das trancas não admite consulta ao método de conflito

        for (Operacao operacao : operacoes) {
            System.out.println(thrd.getName() + ": " + operacao.tipo + "u" + operacao.transacao);
            delTranca(operacao);
        }

        System.out.println(thrd.getName() + ": liberou trancas.");
        notifyAll();
        liberouTrancas = true;
        leitura = true; // Permite acesso à região crítica (conflito())
    }

    public synchronized void delTranca(Operacao operacao) {
        String tipo = operacao.tipo;
        String transacao = operacao.transacao;

        if (trancas.size() > 0) {
            for (int i = 0; i < trancas.size(); i++) {
                if (trancas.get(i).tipo.equals(tipo) && trancas.get(i).transacao.equals(transacao)) {
                    trancas.remove(i);
                }
            }
        }
        leitura = false; // Não permite acesso à região crítica durante a liberação das trancas
    }

    public synchronized boolean enfileirar(Operacao operacao, Thread thrd) {
        boolean abort = false;
        boolean timeout = false;

        System.out.println(thrd.getName() + ": " + operacao.tipo + operacao.transacao + ": fila");
        fila.add(new Operacao(operacao.tipo, operacao.transacao)); // coloca na fila

        while (liberouTrancas == false && !timeout) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        liberouTrancas = false;
        System.out.println(thrd.getName() + ": " + operacao.tipo + operacao.transacao + ": saiu da fila");
        if (timeout) {
            abort = true;
        }

        return abort;
    }

    public void _2PL(ArrayList<Operacao> operacoes, Thread thrd) {
        boolean abort = false;

        for (int i = 0; i < operacoes.size() && !abort; i++) {
            Operacao operacao = operacoes.get(i);
            abort = false;

            while (conflito(operacao, thrd.getName()) && !abort) {
                abort = enfileirar(operacao, thrd);
            }

            if (!abort) {
                trancas.add(new tranca(operacao));
                System.out.println(thrd.getName() + ": " + operacao.tipo + "_2PL_" + operacao.transacao);
                leitura = true;
                dorme();
            } else {
                System.err.println(thrd.getName() + ": " + operacao.tipo + operacao.transacao + ": abort");
            }
        }

        liberarTrancas(operacoes, thrd);
    }

    public synchronized boolean conflito(Operacao operacao, String thread) {
        boolean result = false;

        while (!leitura && liberouTrancas == false) {
            try {
                System.out.println(thread + ": WAIT");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
        }

        leitura = false;
        System.out.print(thread + ": Trancas: [" + trancas.size() + "] ");
        Iterator<tranca> it = trancas.iterator();

        String trancasStr = "";
        while (it.hasNext()) {
            tranca t = it.next();
            trancasStr += thread + "-[" + t.tipo + "l" + t.transacao + "] ";

            if (!t.transacao.equals(operacao.transacao) && (
                    (t.tipo.equals("w") && operacao.tipo.equals("w")) || // operações conflitantes
                    (t.tipo.equals("r") && operacao.tipo.equals("w")) ||
                    (t.tipo.equals("w") && operacao.tipo.equals("r")))) {
                result = true;
            }
        }

        System.out.println(trancasStr);
        leitura = true;
        notifyAll();

        return result;
    }

    public void dorme() {
        try {
            Thread.sleep((int) (Math.random() * 1000));
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }
    }
}
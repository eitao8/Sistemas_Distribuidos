/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controle_Concorrencia;

/**
 *
 * @author Eitão
 */

public class tranca {
    public String tipo="-1";
    public String transacao="-1";
    
    public tranca(Operacao operacao){
	this.tipo=operacao.tipo;
	this.transacao=operacao.transacao;
    }
    
    @Override
    public String toString(){
	return tipo+" "+transacao;
    }
}
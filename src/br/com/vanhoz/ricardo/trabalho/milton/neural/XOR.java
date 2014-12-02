/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.vanhoz.ricardo.trabalho.milton.neural;

import br.com.vanhoz.ricardo.trabalho.milton.neural.train.TreinadorRede;
import br.com.vanhoz.ricardo.trabalho.milton.tela.Logger;
import java.util.Scanner;
import javax.swing.JTextArea;

/**
 *
 * @author ricardo
 */
public class XOR {
    
    public static void main(String[] args) {
//        treinar();
//        treinarComPesos();
        fixo();
    }
    
    private static void fixo() {
        Rede rede = new Rede(3, 2,2,1);
        double[][] pesos = new double[][] {{-7.5256036409553415,-7.557545402792849,3.081200804436247},{5.974910388138393,5.9803572056690495,-9.151538288894855},{-14.456424396448655,-14.474283218809298,7.188879340480953}};
        rede.loadPesos(pesos);
        
        double e1,e2;
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("E1: ");
        e1 = sc.nextDouble();
        System.out.print("E2: ");
        e2 = sc.nextDouble();
        
        sc.close();
        
        
        rede.setValoresEntradas(0, new EntradaSimples(e1));
        rede.setValoresEntradas(1, new EntradaSimples(e2));
        
        Linha saida = rede.getResult();
        System.out.println("Saida: "+saida.getNeuronios().get(0).saida());
    }

    private static void treinarComPesos() {
        TreinadorRede treinador = new TreinadorRede(new G(null), 3, new double[][] {{5.73150244857338,5.737171491990096,-8.88076331445625},{-7.369534181248351,-7.407432430894199,3.0835896732780177},{-13.99931795110484,-13.649139385275273,6.846598144074543}}, 2,2,1);
        double[][] pesos = treinador.train(new double[][] {{0,0,0},{0,1,1},{1,0,1},{1,1,0}});
        imprimirPesos(pesos);
    }

    private static void treinar() {
        TreinadorRede treinador = new TreinadorRede(new G(null), 3, 2,2,1);
        double[][] pesos = treinador.train(new double[][] {{0,0,0},{0,1,1},{1,0,1},{1,1,0}});
        imprimirPesos(pesos);
    }
    
    private static void imprimirPesos(double[][] pesos) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i=0;i<pesos.length;i++) {
            if (i>0)
                sb.append(",");

            sb.append("{");
            for (int j=0;j<pesos[i].length;j++) {
                if (j>0)
                    sb.append(",");
                
                sb.append(pesos[i][j]);
            }
            sb.append("}");
        }
        sb.append("}");
        
        System.out.println(sb.toString());
    }
    
    private static class G extends Logger {
        public G(JTextArea textArea) {
            super(null);
        }

        @Override
        public void log(String msg) {
            System.out.println(msg);
        }
    }
    
}

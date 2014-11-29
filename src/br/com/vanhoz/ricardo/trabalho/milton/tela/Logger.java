/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.vanhoz.ricardo.trabalho.milton.tela;

import javax.swing.JTextArea;

/**
 *
 * @author ricardo
 */
public class Logger {
    
    private JTextArea textArea;

    public Logger(JTextArea textArea) {
        this.textArea = textArea;
    }
    
    public void log(String msg) {
        String e = textArea.getText();
        e += "\n"+msg;
        textArea.setText(e);
    }
    
}

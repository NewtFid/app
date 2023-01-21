package com.company;

import Connect.Client;
import Connect.Weather;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimpleGUI extends JFrame {
    private JButton button = new JButton("Вывести");
    private JTextField input = new JTextField("Введите город", 32);
    private JLabel label1 = new JLabel("В каком городе вы находитесь?");

    private Client client = new Client();
    public SimpleGUI(){
        super("Погода");
        this.setBounds(100, 100, 500, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(6,4, 2, 2));
        container.add(label1);
        container.add(input);
        button.addActionListener(new ButtonEventListener());
        container.add(button);
    }

    class ButtonEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String request = input.getText();
           Weather a = client.getWeather(request);
           String message ="";
           message += "Город: " + request + "\n";
           message += "Темпреатура: " + a.temp +"\n";
           message += "Ощущается: " + a.feels +"\n";
           message += "Максимальная: " + a.temp_max +"\n";
           message += "Минимальная: " + a.temp_min +"\n";
           message += "Давление: " + a.pressure +"\n";
            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);

        }
    }

}

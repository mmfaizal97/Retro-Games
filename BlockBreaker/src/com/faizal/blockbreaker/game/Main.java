package com.faizal.blockbreaker.game;

import com.faizal.blockbreaker.gameplay.Gameplay;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame obj = new JFrame();
        Gameplay gamePlay = new Gameplay();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Block Breaker");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
        obj.setLocationRelativeTo(null);
        obj.setVisible(true);
    }
}

package Controlador;

import Modelo.Celda;
import Modelo.Laberinto;
import Vista.LaberintoGui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class LaberintoControlador {
    private Laberinto laberinto;
    private LaberintoGui vista;
    Vector<String> tiempo= new Vector<>();

    public LaberintoControlador(Laberinto laberinto, LaberintoGui vista) {
        this.laberinto = laberinto;
        this.vista = vista;
       

        vista.addGenerarLaberintoListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.generarLaberinto();
            }
        });

        vista.addRecursivoListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolverLaberinto("Recursivo Simple");
            }
        });

        vista.addCacheListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolverLaberinto("Método Caché");
            }
        });

        vista.addBFSListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolverLaberinto("BFS");
            }
        });

        vista.addDFSListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolverLaberinto("DFS");
            }
        });
    }

    private void resolverLaberinto(String metodo) {
        try {
            int inicioX = vista.getInicioX();
            int inicioY = vista.getInicioY();
            int finX = vista.getFinX();
            int finY = vista.getFinY();
            boolean[][] grid = obtenerGridDesdeBotones();

            laberinto.setInicio(new Celda(inicioX, inicioY));
            laberinto.setFin(new Celda(finX, finY));

            List<Celda> solucion = null;
            long inicioTiempo = System.nanoTime();

            switch (metodo) {
                case "Recursivo Simple":
                    solucion = laberinto.findPathRecursive(grid);
                    break;
                case "Método Caché":
                    solucion = laberinto.findPathWithCache(grid);
                    break;
                case "BFS":
                    solucion = laberinto.findPathBFS(grid);
                    break;
                case "DFS":
                    solucion = laberinto.findPathDFS(grid);
                    break;
            }

            DecimalFormat df = new DecimalFormat("#.##########");
            String tiempoTotal = df.format((System.nanoTime() - inicioTiempo) / 1_000_000_000.0);

            if (solucion != null) {
                
                for (Celda celda : solucion) {
                    if (vista.isDelayEnabled()) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                                    
                                    
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        vista.colorearCelda(celda.getRow(), celda.getCol(), Color.GREEN);
                    }
                    
                            
                    
                }
                
                vista.mostrarResultado("Ruta: " + laberinto.path);
            } else {
                vista.mostrarResultado("No se encontró solución en " + metodo);
            }

            // Acumulación de tiempos
        
            tiempo.add(metodo + ": " + tiempoTotal + " s \n");
            System.err.println(tiempoTotal);
            // vista.mostrarTiempo(tiempoTotal.toString());
            vista.mostrarTiemposAnterior(tiempo.toString());

        } catch (NumberFormatException e) {
            vista.mostrarResultado("Por favor, ingrese números válidos para las coordenadas.");
        }
    }

    private boolean[][] obtenerGridDesdeBotones() {
        JButton[][] botones = vista.getBotones();
        int filas = botones.length;
        int columnas = botones[0].length;
        boolean[][] grid = new boolean[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                grid[i][j] = (botones[i][j].getBackground() == Color.WHITE);
            }
        }
        return grid;
    }
}

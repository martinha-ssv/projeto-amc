package com.example.projetoamc2;
import javax.swing.*;
import java.awt.*;

public class Graph_Experiment extends JPanel{



        private Graph graph;

        public Graph_Experiment(Graph graph) {
            this.graph = graph;
            // Set preferred size or other properties
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Define logic to calculate positions of nodes
            // For simplicity, let's assume nodes are arranged in a circle
            int radius = 150; // Radius of the circle
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int nodeSize = 30; // Diameter of each node

            int numNodes = graph.getDim();

            // Draw nodes
            for (Integer i = 0; i<numNodes; i++) {
                int x = (int) (centerX + radius * Math.cos(2 * Math.PI * i / numNodes));
                int y = (int) (centerY + radius * Math.sin(2 * Math.PI * i / numNodes));
                g.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);
                g.drawString(i.toString(), x, y);
                i++;
            }

            // Draw edges
            for (int i = 0; i<numNodes; i++) {
                int fromX = (int) (centerX + radius * Math.cos(2 * Math.PI * i / numNodes));
                int fromY = (int) (centerY + radius * Math.sin(2 * Math.PI * i / numNodes));

                for (int toNode : graph.offspring(i)) {
                    int toX = (int) (centerX + radius * Math.cos(2 * Math.PI * toNode / numNodes));
                    int toY = (int) (centerY + radius * Math.sin(2 * Math.PI * toNode / numNodes));

                    g.drawLine(fromX, fromY, toX, toY);
                }
            }
        }


}

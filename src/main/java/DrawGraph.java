import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DrawGraph {

    public static void drawGraph(String filename) {
        Graph graph = new SingleGraph("DrawGraph");

        graph.setStrict(false);
        graph.setAutoCreate(true);

        // Create nodes and edges from file
        Scanner scanner;
        try {
            scanner = new Scanner(new File(String.format("src/main/resources/%s", filename)));
            scanner.useDelimiter(",");
            while (scanner.hasNextLine()) {
                String line[] = scanner.nextLine().split(",");
                String u = line[0];
                String v = line[1];
                graph.addEdge(u + v, u, v);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Set the color of nodes based on cluster memberships file
        try {
            scanner = new Scanner(new File("src/main/resources/clusters.dat"));
            int id = 1;
            while (scanner.hasNextLine()) {
                String cluster = scanner.nextLine();
                graph.getNode(Integer.toString(id)).addAttribute("ui.class", "cluster" + cluster);
                id++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        graph.addAttribute("ui.stylesheet", "url('src/main/java/stylesheet.css')");
        graph.display();
    }

    public static void main(String args[]) {
        DrawGraph.drawGraph("example1.dat");
    }
}
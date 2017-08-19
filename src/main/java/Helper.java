import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Helper {

    public static int ChordsByName(ArrayList<Point> points, String name, int i) {
        int max = points.toArray().length;
        int j = 0;
        Point vertex;
        boolean notFound = true;
        while (notFound && (j < max)) {
            vertex = points.get(j);
            if (vertex.name.equals(name)) notFound = false;
            j++;
        }
        if (!notFound)
            if (i == 0) {
                vertex = points.get(j - 1);
                i = vertex.getX();
            } else {
                vertex = points.get(j - 1);
                i = vertex.getY();
            }
        return i;
    }


    public static ArrayList<Point> SortPoints(ArrayList<Point> points) {
        int i = 0, x, y;
        int max = points.toArray().length;
        while (i < max - 2) {
            Point vertex = points.get(i);
            Point secondVertex = points.get(i + 1);
            Point thirdVertex = points.get(i + 2);
            if (!vertex.nearbyPoints.contains(secondVertex.name)) {
                for (int j = i + 3; j < max; j++) {
                    Point point = points.get(j);
                    if (vertex.nearbyPoints.contains(point.name)) {
                        x = secondVertex.getX();
                        y = secondVertex.getY();
                        secondVertex.changeCoords(point.getX(), point.getY());
                        point.changeCoords(x, y);
                    }
                }
            }
            if (!vertex.nearbyPoints.contains(thirdVertex.name)) {
                for (int j = i + 3; j < max; j++) {
                    Point point = points.get(j);
                    if (vertex.nearbyPoints.contains(point.name)) {
                        x = thirdVertex.getX();
                        y = thirdVertex.getY();
                        thirdVertex.changeCoords(point.getX(), point.getY());
                        point.changeCoords(x, y);
                    }
                }
            }
            i += 2;
        }
        return points;

    }

    protected interface readFiles{
        public static ArrayList<Point> read()
        {
            ArrayList<Point> all = new ArrayList<Point>();  return all;
        };
    }

    public static class jsonData implements readFiles {
        public static ArrayList<Point> read(String path) {
            ArrayList<Point> all = new ArrayList<Point>();
            try {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new FileReader(new File(path)));
                JsonElement jelement = new JsonParser().parse(reader);
                JsonObject jobject = jelement.getAsJsonObject();
                JsonArray nodes = jobject.getAsJsonArray("ver");
                int max = nodes.size();
                int h=80/(max/2);
                int m=1,l=0;
                JsonObject node = nodes.get(0).getAsJsonObject();
                Point vertex = new Point(node.get("name").toString().replaceAll("\"",""), 50, max * 40);
                JsonArray arc = node.getAsJsonArray("links");
                vertex.addNearbyPints(arc);
                all.add(vertex);
                for (int i = 1; i < max / 2 + 1 ; i++) {
                    if (i % 2 > 0) {
                        node = nodes.get(i).getAsJsonObject();
                        vertex = new Point(node.get("name").toString().replaceAll("\"",""), 50 + m * 80, max * 40 - m * (80-m*h));
                        arc = node.getAsJsonArray("links");
                        vertex.addNearbyPints(arc);
                        all.add(vertex);
                    } else {
                        node = nodes.get(i).getAsJsonObject();
                        vertex = new Point(node.get("name").toString().replaceAll("\"",""), 50 + m * 80, max * 40 + m * (80-m*h));
                        arc = node.getAsJsonArray("links");
                        vertex.addNearbyPints(arc);
                        all.add(vertex);m++;
                    }
                }
                for (int i = max / 2 + 1; i < max; i++) {
                    if ((i-max/2 +1) % 2 > 0) {
                        node = nodes.get(i).getAsJsonObject();
                        vertex = new Point(node.get("name").toString().replaceAll("\"",""), 50 + (m+l) * 80, max * 40 - (m-1) * (80 - (m-1)*h) + l*(80-(m-l+2)*h));
                        arc = node.getAsJsonArray("links");
                        vertex.addNearbyPints(arc);
                        all.add(vertex);
                    } else {
                        node = nodes.get(i).getAsJsonObject();
                        vertex = new Point(node.get("name").toString().replaceAll("\"",""), 50 + (m+l) * 80, max * 40 + (m-1) * (80 - (m-1)*h) - l*(80-(m-l+2)*h));
                        arc = node.getAsJsonArray("links");
                        vertex.addNearbyPints(arc);
                        all.add(vertex);l++;
                    }
                }
                for (int i=0;i<nodes.size(); i++) {
                    node = nodes.get(i).getAsJsonObject();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            ;
            return SortPoints(all);
        }
    }

    public static class txtData implements readFiles {
        public static ArrayList<Point> read(String path) {
            ArrayList<Point> all = new ArrayList<Point>();
            try {
                FileReader in = new FileReader(new File(path));
                BufferedReader input = new BufferedReader(in);
                String[] nodes = input.readLine().split(",");
                String[] arcs = input.readLine().split(";");
                int max = nodes.length;
                int h=80/(max/2);
                Point vertex = new Point(nodes[0], 50, max * 40);
                vertex.addNearbyPints(arcs);
                int m=1, l=0;
                all.add(vertex);
                for (int i = 1; i < max / 2 ; i++) {
                    if (i % 2 > 0) {
                        vertex = new Point(nodes[i], 50 + m * 80, max * 40 - m * (80-m*h));
                        vertex.addNearbyPints(arcs);
                        all.add(vertex);
                    } else {
                        vertex = new Point(nodes[i], 50 + m * 80, max * 40 + m * (80-m*h));
                        vertex.addNearbyPints(arcs);
                        all.add(vertex); m++;
                    }
                }
                for (int i = max / 2   ; i < max; i++) {
                    if ((i-max/2 +1) % 2 > 0) {
                        vertex = new Point(nodes[i], 50 + (m+l)*80, max * 40 - (m-1) * (80 - (m-1)*h) + l*(80-(m-l+2)*h)   );
                        vertex.addNearbyPints(arcs);
                        all.add(vertex);
                    } else {
                        vertex = new Point(nodes[i], 50 + (m+l)*80, max * 40 + (m-1) * (80 - (m-1)*h) - l*(80-(m-l+2)*h)  );
                        vertex.addNearbyPints(arcs);
                        all.add(vertex); //m++;
                        l++;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            ;
            return SortPoints(all);
        }
    }

}

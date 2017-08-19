import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by SBT-Baksheev-SI on 18.08.2017.
 */
public class Point {
    protected int x;
    protected int y;
    String name;
    ArrayList<String> nearbyPoints;
    public int getX ()
    {
        return this.x;
    }
    public int getY ()
    {
        return this.y;
    }
    public void changeCoords(int x, int y){
        this.x=x; this.y=y;
    }
    public Point(String name, int x, int y){
        this.x=x; this.y=y; this.name=name;
        this.nearbyPoints= new ArrayList<String>();
    }
    public void addNearbyPints(String [] arcs){
        for (int i=0; i<arcs.length; i++) {
            String[] arr = arcs[i].split(",");
            if (arr[1]!=null && ((arr[1].equals(this.name)))) {
                this.nearbyPoints.add(arr[0]);
            }
            if (arr[1]!=null &&  (arr[0].equals(this.name))) {
                this.nearbyPoints.add(arr[1]);
            }
        }
    }
    public void addNearbyPints(JsonArray arcs){
        for (int i=0; i<arcs.size(); i++) {
                this.nearbyPoints.add(arcs.get(i).toString().replaceAll("\"", ""));
        }
    }



}

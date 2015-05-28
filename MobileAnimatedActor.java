import processing.core.PImage;
import processing.core.PApplet;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Math.abs;

public abstract class MobileAnimatedActor
   extends AnimatedActor
{
   private List<Point> path;
   private List<Point> all;
   
   public MobileAnimatedActor(String name, Point position, int rate,
      int animation_rate, List<PImage> imgs)
   {
      super(name, position, rate, animation_rate, imgs);
      
   }

   
   public List<Point> findPath()
   {
      List<Point> new_path = new LinkedList<>();
      
      if (path != null)
      {
         for (int i = 0; i < path.size() - 1; i++)
         {
            new_path.add(path.get(i));
         }
      }
      
      return new_path;
   }
   
   
   protected Point nextPosition(Point start, Point goal, 
      WorldModel world)
   {
      List<Point> closed_set = new ArrayList<Point>();
      OrderedList<Node> open_set = new OrderedList<>();
      Node [][] came_from = new Node[world.getNumRows()]
         [world.getNumCols()];
      
      Node start_node = new Node(start);
      start_node.setG(0);
      start_node.setF(this.heuristic(start, goal));
      
      open_set.insert(start_node, start_node.f_score());

      while (open_set.size() > 0)
      {
         Node current = open_set.item();
 
         if (current.x == goal.x && current.y == goal.y)//adjacent(current, goal))
         {
            path = reconstructPath(came_from, goal, start_node, world);
            return path.get(path.size() - 2);
         }
         
         open_set.remove(current);
         closed_set.add(current);
         
         for (Node neighbor : neighborNodes(world, current, goal))
         {
            if (closedContains(closed_set, neighbor))
            {
               continue;
            }
            
            int tentative_g_score = current.g_score() + 
               dist_between(current, neighbor, world);
            
            if ((!open_set.contains(neighbor)) || 
                (tentative_g_score < neighbor.g_score()))
            {
               came_from[neighbor.y][neighbor.x] = current;
               neighbor.setG(tentative_g_score);
               neighbor.setF(heuristic(neighbor, goal));
               
               if (!open_set.contains(neighbor))
               {
                  open_set.insert(neighbor, neighbor.f_score());
               }
            }
         }
      }
       
      return null;
   }
   
   
   private static int heuristic(Point start, Point goal)
   {
      return abs(goal.x - start.x) + abs(goal.y - start.y);
   }
   
   //fix
   private static int dist_between(Point start, Point end, WorldModel world)
   {
      int cost = 0;
      
      if (world.isOccupied(end))
      {
         // costs may be added
      }
      return abs(end.x - start.x) + abs(end.y - start.y) + cost;
   }
   
   
   private List<Point> reconstructPath(Point [][] came_from, Point current,
      Point start, WorldModel world)
   {
      List<Point> total_path = new ArrayList<>();
      total_path.add(current);

      while (current != start)
      {
         current = came_from[current.y][current.x];
         total_path.add(current);
      }
      
      return total_path;
   }

   
   private static boolean closedContains(List<Point> points, Point pt)
   {
      for (Point point : points)
      {
         if (point.x == pt.x && point.y == pt.y)
         {
            return true;
         }
      }
      
      return false;
   }

   
   private static List<Node> neighborNodes(WorldModel world, Point current,
      Point goal)
   {
      List<Node> neighbors = new ArrayList<>();
      
      Point p = new Point(current.x, current.y - 1);
      if (world.withinBounds(p) && (!world.isOccupied(p) || 
         (p.x == goal.x && p.y == goal.y)))
      {
         neighbors.add(new Node(new Point(current.x, current.y - 1)));
      }
      
      Point p2 = new Point(current.x, current.y + 1);
      if (world.withinBounds(p2) && (!world.isOccupied(p2) || 
         (p2.x == goal.x && p2.y == goal.y)))
      {
         neighbors.add(new Node(new Point(current.x, current.y + 1)));
      }
      
      Point p3 = new Point(current.x - 1, current.y);
      if (world.withinBounds(p3) && (!world.isOccupied(p3) || 
         (p3.x == goal.x && p3.y == goal.y)))
      {
         neighbors.add(new Node(new Point(current.x - 1, current.y)));
      }
      
      Point p4 = new Point(current.x + 1, current.y);
      if (world.withinBounds(p4) && (!world.isOccupied(p4) || 
         (p4.x == goal.x && p4.y == goal.y)))
      {
         neighbors.add(new Node(new Point(current.x + 1, current.y)));
      }
      
      return neighbors;
   }

   
   protected static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && abs(p1.y - p2.y) == 1) ||
         (p1.y == p2.y && abs(p1.x - p2.x) == 1);
   }

   protected abstract boolean canPassThrough(WorldModel world, Point new_pt);
}

public class Node
   extends Point
{
   private int g_score;
   private int f_score;
   
   public Node(Point pt)//, int g_score, int f_score)
   {
      super(pt.x, pt.y);
      //this.g_score = g_score;
      //this.f_score = f_score;
   }
   
   public int g_score()
   {
      return this.g_score;
   }
   
   public int f_score()
   {
      return this.f_score;
   }
   
   public void setG(int new_g)
   {
      this.g_score = new_g;
   }
   
   public void setF(int h_score)
   {
      this.f_score = this.g_score + h_score;
   }
}
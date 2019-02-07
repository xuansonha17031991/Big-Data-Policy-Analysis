/*    */ package utils;
/*    */ 
/*    */ import org.apache.commons.lang.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang.builder.HashCodeBuilder;
/*    */ 
/*    */ 
/*    */ public class Pair<T, U>
/*    */ {
/*    */   public T first;
/*    */   public U second;
/*    */   
/*    */   public Pair(T t, U u)
/*    */   {
/* 14 */     this.first = t;
/* 15 */     this.second = u;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 21 */     if (!(obj instanceof Pair))
/*    */     {
/* 23 */       return false;
/*    */     }
/* 25 */     if (this == obj)
/*    */     {
/* 27 */       return true;
/*    */     }
/* 29 */     Pair<?, ?> rhs = (Pair)obj;
/*    */     
/*    */ 
/* 32 */     return new EqualsBuilder().append(this.first, rhs.first).append(this.second, rhs.second).isEquals();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 39 */     return new HashCodeBuilder(3, 7).append(this.first).append(this.second).toHashCode();
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 45 */     return "(" + this.first + ", " + this.second + ")";
/*    */   }
/*    */   
/*    */   public static <V, W> Pair<V, W> make(V v, W w)
/*    */   {
/* 50 */     return new Pair(v, w);
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/Pair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
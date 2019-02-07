/*    */ package utils;
/*    */ 
/*    */ import org.apache.commons.lang.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang.builder.HashCodeBuilder;
/*    */ 
/*    */ public class Triple<T, U, Z>
/*    */ {
/*    */   public T first;
/*    */   public U second;
/*    */   public Z third;
/*    */   
/*    */   public Triple(T t, U u, Z z)
/*    */   {
/* 14 */     this.first = t;
/* 15 */     this.second = u;
/* 16 */     this.third = z;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 22 */     if (!(obj instanceof Triple))
/*    */     {
/* 24 */       return false;
/*    */     }
/* 26 */     if (this == obj)
/*    */     {
/* 28 */       return true;
/*    */     }
/* 30 */     Triple<?, ?, ?> rhs = (Triple)obj;
/*    */     
/*    */ 
/* 33 */     return new EqualsBuilder().append(this.first, rhs.first).append(this.second, rhs.second).append(this.third, rhs.third).isEquals();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 40 */     return new HashCodeBuilder(3, 7).append(this.first).append(this.second).append(this.third).toHashCode();
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 46 */     return "(" + this.first + ", " + this.second + "," + this.third + ")";
/*    */   }
/*    */   
/*    */   public static <V, W, K> Triple<V, W, K> make(V v, W w, K k)
/*    */   {
/* 51 */     return new Triple(v, w, k);
/*    */   }
/*    */ }


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/utils/Triple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
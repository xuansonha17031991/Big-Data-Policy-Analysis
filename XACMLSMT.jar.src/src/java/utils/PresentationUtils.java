package utils;

import com.microsoft.z3.ApplyResult;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Goal;
import com.microsoft.z3.Status;
import com.microsoft.z3.Tactic;

public abstract class PresentationUtils {

	public static ApplyResult applyTactic(Context ctx, Tactic t, Goal g) {
		// System.out.println("\nGoal: " + g);
	
		ApplyResult res = t.apply(g);
		// System.out.println("Application result: " + res);
	
		Status q = Status.UNKNOWN;
		for (Goal sg : res.getSubgoals())
			if (sg.isDecidedSat())
				q = Status.SATISFIABLE;
			else if (sg.isDecidedUnsat())
				q = Status.UNSATISFIABLE;
	
		// switch (q) {
		// case UNKNOWN:
		// System.out.println("Tactic result: Undecided");
		// break;
		// case SATISFIABLE:
		// System.out.println("Tactic result: SAT");
		// break;
		// case UNSATISFIABLE:
		// System.out.println("Tactic result: UNSAT");
		// break;
		// }
	
		return res;
	}

	public static String normalUniform(Expr expr) {
		return expr.simplify().toString();
//		return expr.toString();
	}

	public static String uniform(Context ctx, Expr expr) {
		Goal g4 = ctx.mkGoal(true, false, false);
		g4.add((BoolExpr) expr.simplify());
		// Tactic tactic = ctx.andThen(ctx.mkTactic("ctx-solver-simplify"),
		// ctx.mkTactic("ctx-simplify"));
		// Tactic tactic = ctx.andThen(ctx.mkTactic("ctx-simplify"),
		// ctx.mkTactic("ctx-solver-simplify"));
		Tactic tactic = ctx.mkTactic("ctx-solver-simplify");
		// Tactic tactic = ctx.mkTactic("ctx-simplify");
		ApplyResult ar = applyTactic(ctx, tactic, g4);
		Goal[] subgoals = ar.getSubgoals();
		// System.err.println(subgoals.length);
		return subgoals[0].toString();
		// .replace("and", "\\\\WEDGE").replace("or", "\\\\VEE").replace("not",
		// "\\\\NEG");
	}
}

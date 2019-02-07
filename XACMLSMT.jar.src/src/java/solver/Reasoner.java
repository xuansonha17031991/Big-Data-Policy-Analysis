package solver;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import query.QueryRunner;
import smtfunctions.SMTFunctionFactory;
import translator.XACMLPQNormalizer;
import utils.Pair;

import com.microsoft.z3.ApplyResult;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.FuncInterp;
import com.microsoft.z3.Goal;
import com.microsoft.z3.Model;
import com.microsoft.z3.Params;
import com.microsoft.z3.SetSort;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Status;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Tactic;
import com.microsoft.z3.Z3Exception;
import com.microsoft.z3.enumerations.Z3_sort_kind;

public class Reasoner {
	static Solver s;
	ApplyResult appResult;
	private static final long KILOBYTE = 1024L;

	public Reasoner() {
	}

	public Reasoner(Context ctx) {
		try {
			s = ctx.mkSolver();
		} catch (Z3Exception zex) {
			System.err
					.println("Z3Exception in motExampleEncoding_AttributeHiding "
							+ zex.getMessage());
		}
	}

	public static long convertToBigger(int choice, long bytes) {
		long result = 0L;
		switch (choice) {
		case 1:
			result = bytes / 1048576L;
			break;
		case 2:
			result = bytes / 1024L;
		}
		return result;
	}

	ApplyResult applyTactic(Context ctx, Tactic t, Goal g) {
		System.out.println("\nGoal: " + g);

		ApplyResult res = t.apply(g);
		System.out.println("Application result: " + res);

		Status q = Status.UNKNOWN;
		for (Goal sg : res.getSubgoals())
			if (sg.isDecidedSat())
				q = Status.SATISFIABLE;
			else if (sg.isDecidedUnsat())
				q = Status.UNSATISFIABLE;

		switch (q) {
		case UNKNOWN:
			System.out.println("Tactic result: Undecided");
			break;
		case SATISFIABLE:
			System.out.println("Tactic result: SAT");
			break;
		case UNSATISFIABLE:
			System.out.println("Tactic result: UNSAT");
			break;
		}

		return res;
	}

	private Map<String, Object> invokeSolver(BoolExpr formula, Context ctx) {
		try {

			s.reset();
			Params pms = ctx.mkParams();

			Runtime runtime = Runtime.getRuntime();
			runtime.gc();
			long memoryBegin = runtime.totalMemory();
			long begin = System.currentTimeMillis();
			if (formula != null)
				s.add(new BoolExpr[] { formula });

			// System.err.println(s.toString());
			Status status = s.check();
			long end = System.currentTimeMillis();

			if (QueryRunner.z3output) {
				System.out
						.println("*********** Statistics from Z3 ************* \n"
								+ s.getStatistics().toString());
			}

			long memoryEnd = runtime.freeMemory();

			DescriptiveStatistics ds = new DescriptiveStatistics();
			Map<String, Object> result = new HashMap();
			ds.addValue(end - begin);
			result.put("time", ds);
			result.put("status", status);
			result.put("memory", Long.valueOf(memoryBegin - memoryEnd));

			return result;
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in invokeSolver of SMTSolver "
					+ zex.getMessage() + zex.getStackTrace());
		}
		return null;
	}

	public Map<String, Object> solveSMT(BoolExpr formula, boolean getModels,
			XACMLPQNormalizer pl, int threshold, PrintStream out) {
		try {
			if (getModels) {
				return getModel(pl, formula, threshold, out);
			}
			Map<String, Object> res = invokeSolver(formula, pl.getContext());
			Status status = (Status) res.get("status");
			System.out.println(status.toString());
			if ((status == Status.UNSATISFIABLE) && (QueryRunner.proofs)) {
				Expr proof = s.getProof();
				out.println(proof.toString());
			}

			HashMap<String, Object> result = new HashMap();
			DescriptiveStatistics descTime = new DescriptiveStatistics();
			DescriptiveStatistics descMemory = new DescriptiveStatistics();
			descTime.addValue(((DescriptiveStatistics) res.get("time"))
					.getElement(0));
			descMemory.addValue(((Long) res.get("memory")).longValue());
			result.put("time", descTime);
			result.put("memory", descMemory);
			result.put("status", status);
			return result;
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in solve of SMTSolver "
					+ zex.getMessage() + zex.getStackTrace().toString());
			zex.printStackTrace(out);
		} catch (Exception ex) {
			System.err.println("Exception in solveSMT " + ex.getMessage());
		}
		return null;
	}

	private Pair<Vector<BoolExpr>, Vector<BoolExpr>> generateDiffModelCons(
			XACMLPQNormalizer pl, FuncDecl[] decls, Context ctx, Model m,
			PrintStream out) {
		try {
			Vector<BoolExpr> notEquals = new Vector();
			Vector<BoolExpr> equals = new Vector();
			if (QueryRunner.debug)
				System.out.println("Enumerating new model with negation");
			for (int i = 0; i < decls.length; i++) {
				if (QueryRunner.debug) {
					System.out.println("Z3 Declaration : "
							+ decls[i].toString());
				}
				FuncDecl userDefined = SMTFunctionFactory
						.getUserDefinedFunctions().get(
								decls[i].getName().toString());
				if ((userDefined != null)
						&& (decls[i].getRange().getSortKind() != Z3_sort_kind.Z3_BOOL_SORT)) {
					notEquals.add(ctx.mkFalse());
					if (QueryRunner.debug) {
						out.println("Non-boolean User defined function found "
								+ userDefined.getName());
					}

				} else if (decls[i].getRange().getSortKind() == Z3_sort_kind.Z3_ARRAY_SORT) {

					FuncInterp fi = m.getFuncInterp(decls[i]);
					if (decls[i].getArity() <= 0) {

						Vector<BoolExpr> notOrs = new Vector();
						for (int j = 0; j < fi.getEntries().length; j++) {
							if (Boolean.valueOf(
									fi.getEntries()[j].getValue().toString())
									.booleanValue()) {
								Expr setVar = null;
								Pair<Expr, String> enumVar = pl.getTranslator()
										.getListOfEnumeratedVars()
										.get(decls[i].getName().toString());
								Pair<Expr, Sort> otherVar = pl.getTranslator()
										.getListOfOtherVars()
										.get(decls[i].getName().toString());
								if (enumVar != null) {
									setVar = enumVar.first;
								} else if (otherVar != null)
									setVar = otherVar.first;
								if (setVar != null)
									notOrs.add(ctx.mkNot(ctx.mkSetMembership(
											fi.getEntries()[j].getArgs()[0],
											(ArrayExpr) setVar)));
							}
						}
						if (notOrs.size() > 1) {
							BoolExpr neq = ctx.mkOr(notOrs
									.toArray(new BoolExpr[notOrs.size()]));
							notEquals.add(neq);
						} else if (notOrs.size() == 1) {
							notEquals.add(notOrs.get(0));
						}

					}

				} else if (decls[i].getArity() == 0) {
					if (decls[i].getRange().getSortKind() == Z3_sort_kind.Z3_INT_SORT) {
						BoolExpr eq = ctx.mkEq(decls[i].apply(new Expr[0]),
								m.getConstInterp(decls[i]));
						equals.add(eq);
						if (QueryRunner.debug)
							System.out.println("EQ = " + eq);
					}
					BoolExpr neq = ctx.mkNot(ctx.mkEq(
							decls[i].apply(new Expr[0]),
							m.getConstInterp(decls[i])));
					notEquals.add(neq);
				}
			}

			if ((notEquals.size() > 0) && (QueryRunner.debug))
				System.out.println("Not equals --> "
						+ notEquals.lastElement().toString());
			return new Pair(equals, notEquals);
		} catch (Z3Exception zex) {
			System.err
					.println("ZException in generateDiffModelCons of SMTSolver..."
							+ zex.getMessage());
		} catch (Exception ex) {
			System.err.println("Exception in generateDiffModelCons ..."
					+ ex.getMessage());
		}
		return null;
	}

	private Pair<DescriptiveStatistics, DescriptiveStatistics> getMoreModels(
			XACMLPQNormalizer pl, BoolExpr originalFormula,
			FuncDecl[] firstDecls, Context ctx, Model firstModel,
			int threshold, PrintStream out) {
		DescriptiveStatistics descTime = new DescriptiveStatistics();
		DescriptiveStatistics descMemory = new DescriptiveStatistics();
		Status statusTemp = Status.SATISFIABLE;
		int modelNum = 2;

		Model m = firstModel;
		FuncDecl[] decls = firstDecls;
		BoolExpr finalFormula = originalFormula;
		boolean equalsNotAdded = true;
		while ((statusTemp == Status.SATISFIABLE) && (modelNum <= threshold)) {

			Pair formRebuilt = generateDiffModelCons(pl, decls, ctx, m, out);
			Vector<BoolExpr> equals = (Vector) formRebuilt.first;
			Vector<BoolExpr> notEquals = (Vector) formRebuilt.second;

			if ((equals.size() > 0) && (equalsNotAdded)) {
				finalFormula = ctx
						.mkAnd(new BoolExpr[] {
								finalFormula,
								ctx.mkAnd(equals.toArray(new BoolExpr[equals
										.size()])) });
				equalsNotAdded = false;
			}

			finalFormula = notEquals.size() > 0 ? ctx
					.mkAnd(new BoolExpr[] {
							finalFormula,
							ctx.mkOr(notEquals.toArray(new BoolExpr[notEquals
									.size()])) }) : finalFormula;

			Map<String, Object> res = invokeSolver(finalFormula, ctx);
			descTime.addValue(((DescriptiveStatistics) res.get("time"))
					.getElement(0));
			descMemory.addValue(((Long) res.get("memory")).longValue());
			statusTemp = (Status) res.get("status");
			if (statusTemp == Status.SATISFIABLE) {
				m = s.getModel();
				decls = m.getDecls();
				getFunctionConstantInterpretations(m, decls, modelNum, pl, out);
				modelNum++;
			}
		}
		return new Pair(descTime, descMemory);
	}

	private Map<String, Object> getModel(XACMLPQNormalizer pl,
			BoolExpr formula, int threshold, PrintStream out) {
		try {
			DescriptiveStatistics descTime = new DescriptiveStatistics();
			DescriptiveStatistics descMemory = new DescriptiveStatistics();
			Context ctx = pl.getContext();

			Map<String, Object> res = invokeSolver(formula, ctx);
			descTime.addValue(((DescriptiveStatistics) res.get("time"))
					.getElement(0));
			descMemory.addValue(((Long) res.get("memory")).longValue());
			Status status = (Status) res.get("status");

			System.out.println(status.toString());
			if (out != System.out) {
				out.println(status.toString());
			}
			if (status == Status.SATISFIABLE) {
				Model m = s.getModel();
				FuncDecl[] decls = m.getDecls();
				getFunctionConstantInterpretations(m, m.getDecls(), 1, pl, out);
				Pair<DescriptiveStatistics, DescriptiveStatistics> moreModelResults = null;
				if (threshold > 1) {
					moreModelResults = getMoreModels(pl, formula, decls, ctx,
							m, threshold, out);
					for (int j = 0; j < moreModelResults.first.getValues().length; j++)
						descTime.addValue(moreModelResults.first.getElement(j));
					for (int j = 0; j < moreModelResults.second.getValues().length; j++) {
						descMemory.addValue(moreModelResults.second
								.getElement(j));
					}
				}
			}

			Map<String, Object> result = new HashMap();
			result.put("time", descTime);
			result.put("memory", descMemory);
			result.put("status", status);
			return result;
		} catch (Z3Exception zex) {
			System.err.println("ZException in allModels of getModel ..."
					+ zex.getMessage());
			zex.printStackTrace();
		} catch (Exception ex) {
			System.err.println("Exception in getModels " + ex.getMessage());
		}
		return null;
	}

	private void getFunctionConstantInterpretations(Model m,
			FuncDecl[] declarations, int modelNum, XACMLPQNormalizer pl,
			PrintStream out) {
		try {
			if ((QueryRunner.models) && (QueryRunner.debug)) {
				out.println("<----- MODEL " + modelNum + " ---->");
				out.println(m.toString());
			}
			Map<String, String> variables = pl.getPolicyVariables();
			out.println("<-----  MODEL " + modelNum
					+ " with Function Interpretations .... ");
			for (int i = 0; i < declarations.length; i++) {

				if ((declarations[i].getArity() == 0)
						&& (declarations[i].getRange().getSortKind() != Z3_sort_kind.Z3_ARRAY_SORT)) {
					Expr fi = m.getConstInterp(declarations[i]);
					out.println(declarations[i].toString()
							+ " --(Interpreted as)--> " + fi);
					out.println("That is : " + declarations[i].getName()
							+ " = " + fi);
				} else if ((declarations[i].isFuncDecl())
						|| (declarations[i].getArity() > 0)) {
					FuncInterp fi = m.getFuncInterp(declarations[i]);

					if (variables.containsKey(declarations[i].getName()
							.toString().trim())) {
						out.println(declarations[i].toString()
								+ " --(Interpreted as)--> " + fi);
					}
					Vector<String> values = new Vector();
					for (int j = 0; j < fi.getEntries().length; j++) {
						if (Boolean.valueOf(
								fi.getEntries()[j].getValue().toString())
								.booleanValue()) {
							values.addElement(fi.getEntries()[j].getArgs()[0]
									.toString());
						}
					}
					if (variables.containsKey(declarations[i].getName().toString().trim())) {
						out.print("That is : " + declarations[i].getName() + " = ");
						out.println(values);
					}
				}
			}
			out.println("------>");
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in Runner " + zex.getMessage());
		}
	}

	private void testgetFuncDeclarations(Expr[] exs, BoolExpr[] cons,
			Context ctx) {
		try {
			BoolExpr formula = ctx.mkAnd(new BoolExpr[] { ctx.mkNot(cons[0]),
					cons[1], cons[2], cons[3], cons[4], cons[5] });
			Solver solver = ctx.mkSolver();
			solver.add(new BoolExpr[] { formula });

			if (solver.check() == Status.SATISFIABLE) {
				System.out.println("Satisfiable!");
				Model m = solver.getModel();
				System.out.println("FuncDecl " + m.getFuncDecls().length);
				System.out.println("ConstDecl " + m.getConstDecls().length);

				FuncInterp fiRes = m.getFuncInterp(exs[0].getFuncDecl());
				System.out.println("HERE we are " + fiRes.toString());
				FuncInterp fiSub = m.getFuncInterp(exs[1].getFuncDecl());
				System.out.println("HERE we are " + fiSub.toString());
				for (int i = 0; i < fiSub.getEntries().length; i++) {
					FuncInterp.Entry e = fiSub.getEntries()[i];
					System.out.println("ZZ" + e.getValue().toString());
				}

				System.out.println("MODEL ");
				System.out.println(solver.getModel().toString());
			} else {
				System.out.println("Unsatisfiable!");
			}
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception in Runner " + zex.getMessage());
		}
	}

	private void testInterpretations() {
		HashMap<String, String> cfg = new HashMap();

		cfg.put("model", "true");
		cfg.put("proof", "true");
		try {
			Context ctx = new Context(cfg);

			EnumSort rSort = ctx.mkEnumSort(ctx.mkSymbol("res"),
					new Symbol[] { ctx.mkSymbol("res1") });
			SetSort rSet = ctx.mkSetSort(rSort);
			Expr rID = ctx.mkConst("rID", rSet);
			BoolExpr c1 = ctx.mkSetMembership(rSort.getConsts()[0],
					(ArrayExpr) rID);

			Solver s = ctx.mkSolver();
			s.add(new BoolExpr[] { c1 });
			Status status = s.check();
			Model m = s.getModel();

			System.out.println(status);
			System.out.println("Model = " + m);
			System.out.println("Expression : " + rID.toString() + " Sort : "
					+ rID.getSort().toString());
			FuncInterp fi = m.getFuncInterp(rID.getFuncDecl());
			System.out.println("fi=" + fi);
		} catch (Z3Exception zex) {
			System.err.println("Z3Exception " + zex.getMessage());
		}
	}

	public static void main(String[] args) {
		Reasoner s = new Reasoner();
		s.testInterpretations();
	}
}

/*
 * Location: /Users/okielabackend/Downloads/XACMLSMT.jar!/solver/Reasoner.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */
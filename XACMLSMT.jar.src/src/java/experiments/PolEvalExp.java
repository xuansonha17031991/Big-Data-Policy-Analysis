
package experiments;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import policy.SMTPolicy;
import policy.SMTPolicyElement.Decision;
import policy.SMTRule;
import query.QueryRunner;

import com.microsoft.z3.ApplyResult;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Goal;
import com.microsoft.z3.Status;
import com.microsoft.z3.Tactic;

public class PolEvalExp {
	private static final String POLICY_GEN_FOLDER = "E:\\Workspace\\Thesis\\Research\\alfa-project\\alfa-project\\src-gen\\";
	private static final String HEALTH_CARE_POLICY_SET = POLICY_GEN_FOLDER + "health_care.HealthCarePsPatientRecord.xml";
	
	private static final String CONTINUE_A = "E:\\Workspace\\Thesis\\Research\\DEMO\\data\\continue-a-xacml3.xml";
	Map<String, String[]> query2Policies;

	private void solve(QueryRunner qr) {
		for (Map.Entry<String, String[]> qpol : this.query2Policies.entrySet()) {
			long timeBeginLoad = System.currentTimeMillis();
			String domain = "(  ( user-id = Doctor-Patient \\VEE user-id = Nurse-Patient \\VEE user-id = Doctor-Emer-Agreement \\VEE user-id = Doctor-Non-Patient \\VEE user-id = Nurse-Non-Patient ) \\WEDGE  ( role = Doctor \\VEE role = Nurse \\VEE role = External ) \\WEDGE  ( party-type = Pharmacy \\VEE party-type = Insurance ) \\WEDGE  ( resource-type = Patient.Record \\VEE resource-type = Patient.Record.Billing \\VEE resource-type = Patient.Record.Medicines ) \\WEDGE  ( patient:management-info:status = NotAvailableDoctorNurse \\VEE patient:management-info:status = AvailableDoctorNurse ) \\WEDGE patient:management-info:relative-passport-id = Patient-Relative \\WEDGE  ( patient:management-info:conscious-status = Conscious \\VEE patient:management-info:conscious-status = Unconscious ) \\WEDGE patient:record:department = Heart \\WEDGE patient:record:assigned-doctor-id = Doctor-Patient \\WEDGE patient:record:assigned-nurse-id = Doctor-Nurse \\WEDGE patient:emgergency-agreement:doctor = Doctor-Emer-Agreement \\WEDGE patient:emgergency-agreement:relative-passport-id = Patient-Relative \\WEDGE action-type = Read \\WEDGE location = Heart )";
			SMTPolicy[] policyFormulas = qr.loadPolicies(qpol.getValue(), true, QueryRunner.xacmlVersion, domain);

			SMTRule smtRule = policyFormulas[0].getFinalElement();
			Context ctx = qr.getLoader().getContext();
			System.err.println(Decision.Permit + " " + uniform(ctx, smtRule.getDSStr(Decision.Permit, true)));
			System.err.println(Decision.Deny + " " + uniform(ctx, smtRule.getDSStr(Decision.Deny, true)));
			System.err.println(Decision.Indet + " " + uniform(ctx, smtRule.getDSStr(Decision.Indet, true)));
			System.err.println(Decision.Na + " " + uniform(ctx, smtRule.getDSStr(Decision.Na, true)));

			long timeEndLoad = System.currentTimeMillis();
			System.out.println("LOADING FINISHED !!");
			long timeBeginSolve = System.currentTimeMillis();
			Map<String, Object> result = qr.executeQuery(policyFormulas, qpol.getKey(), System.out);
			long timeEndSolve = System.currentTimeMillis();
			System.out.println("LOAD TIME : " + (timeEndLoad - timeBeginLoad) + "  SOLVE TIME : "
					+ (timeEndSolve - timeBeginSolve));

			System.out.println("Time : " + ((DescriptiveStatistics) result.get("time")).getSum() + " Memory :"
					+ Experiments.convertToBigger(1, ((DescriptiveStatistics) result.get("memory")).getSum()) + " Status :"
					+ ((Status) result.get("status")).toString());

			System.out.println("---------------------------------------------------");
		}
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

	private String uniform(Context ctx, Expr expr) {
		// return expr.simplify().toString();
		Goal g4 = ctx.mkGoal(true, false, false);
		g4.add((BoolExpr) expr);
		// Tactic tactic = ctx.andThen(ctx.mkTactic("ctx-solver-simplify"),
		// ctx.mkTactic("ctx-simplify"));
		// Tactic tactic = ctx.andThen(ctx.mkTactic("ctx-simplify"),
		// ctx.mkTactic("ctx-solver-simplify"));
		Tactic tactic = ctx.mkTactic("ctx-solver-simplify");
		// Tactic tactic = ctx.mkTactic("ctx-simplify");
		ApplyResult ar = applyTactic(ctx, tactic, g4);
		Goal[] subgoals = ar.getSubgoals();
		System.err.println(subgoals.length);
		return ar.getSubgoals()[0].toString();
		// .replace("and", "\\\\WEDGE").replace("or", "\\\\VEE").replace("not",
		// "\\\\NEG");
	}

	public void experimentPolEval() {
		QueryRunner qr = new QueryRunner();

		QueryRunner.verbose = true;
		QueryRunner.z3output = true;
		QueryRunner.debug = false;
		QueryRunner.models = true;
		QueryRunner.threshold = 1;
		QueryRunner.printVarDomains = true;
		QueryRunner.xacmlVersion = 3;
		this.query2Policies = new HashMap();

		// this.query2Policies.put("( P_1 \\VEE D_1 \\VEE IN_1 \\VEE NA_1 )", new
		// String[] {
		// //
		// "/Users/okielabackend/Downloads/alfa-project/alfa-project/src-gen/health_care.NormalAccessDoctor.xml"
		// "/Users/okielabackend/Documents/AccessControl/DEMO/data/health-care.xml"
		// // "/Users/okielabackend/Documents/AccessControl/DEMO/data/KMarket.xml"
		// });

		this.query2Policies
				.put(
		// Experiments.querySoDContinue
//				"( D_1 )"
		// "( P_1 \\WEDGE \\NEG ( D_1 \\VEE IN_1 \\VEE NA_1 ) \\WEDGE ( user-id = Doctor-Patient \\WEDGE role = Doctor \\WEDGE resource-type = Patient.Record \\WEDGE patient:management-info:status = AvailableNurseDoctor \\WEDGE patient:record:department = Heart \\WEDGE patient:record:assigned-doctor-id = Doctor-Patient \\WEDGE action-type = Read ) )"
						// "( D_1 \\WEDGE ( user-id = Doctor-Patient \\WEDGE role = Doctor \\WEDGE party-type = \\EMPTYSET \\WEDGE resource-type = Patient.Record \\WEDGE patient:management-info:status = AvailableNurseDoctor \\WEDGE patient:management-info:relative-passport-id = \\EMPTYSET \\WEDGE patient:management-info:conscious-status = \\EMPTYSET \\WEDGE patient:record:department = Heart \\WEDGE patient:record:assigned-doctor-id = Doctor-Patient \\WEDGE patient:record:assigned-nurse-id = \\EMPTYSET \\WEDGE patient:emgergency-agreement:doctor = \\EMPTYSET \\WEDGE patient:emgergency-agreement:relative-passport-id = \\EMPTYSET \\WEDGE patient:emgergency-agreement:start-time = -1 \\WEDGE patient:emgergency-agreement:end-time = -1 \\WEDGE action-type = Read \\WEDGE location = \\EMPTYSET \\WEDGE current-time = -1 ) )"
				// "( D_1 \\WEDGE ( ( \\REST user-id ( D_1 \\WEDGE ( user-id = Doctor-Patient ) ) ) \\WEDGE ( \\REST role ( D_1 \\WEDGE ( role = Doctor ) ) ) \\WEDGE ( \\REST resource-type ( D_1 \\WEDGE ( resource-type = Patient.Record ) ) ) \\WEDGE ( \\REST patient:management-info:status ( D_1 \\WEDGE ( patient:management-info:status = AvailableNurseDoctor ) ) ) \\WEDGE ( \\REST patient:record:department ( D_1 \\WEDGE ( patient:record:department = Heart ) ) ) \\WEDGE ( \\REST patient:record:assigned-doctor-id ( D_1 \\WEDGE ( patient:record:assigned-doctor-id = Doctor-Patient ) ) ) \\WEDGE ( \\REST action-type ( D_1 \\WEDGE ( action-type = Read ) ) ) ) )"
				"(\\REPLC ((role = External) \\WEDGE (\\NEG (role = Doctor \\WEDGE role = Nurse)) ) (\\REPLC (party-type = Insurance) P_1.SHORT))"
						// "(\\REPLC (patient:management-info:status = AvailableNurseDoctor \\WEDGE (\\NEG (patient:management-info:status = NotAvailableNurseDoctor))) P_1.SHORT)"
				// "(D_1.SHORT \\WEDGE role = External \\WEDGE (\\NEG (role = Doctor \\WEDGE role = Nurse)) \\WEDGE party-type = Insurance)"
				// "(D_1 \\IMPLIES P_1)"
				// "( P_1 \\WEDGE ( role = External \\WEDGE party-type = Insurance \\WEDGE action-type = Read \\WEDGE resource-type = Patient.Record ))"
				// "(D_1 \\WEDGE patient:record:assigned-doctor-id = Doctor-Emer-Agreement \\WEDGE user-id = Doctor )"
				// " ((\\REST role (D_1 \\WEDGE (role = Doctor))) " + "\\WEDGE "
						// "(\\REST action-type (P_1 \\WEDGE (action-type = Read)))"
						// "\\WEDGE ( (\\WEDGE (\\VEE (\\WEDGE (select resource-type Patient.Rec\\VEEd) (select action-type Read)))) (\\NEG false) \\VEE ( \\VEE ( \\VEE ( \\VEE ( POL_1.P \\WEDGE ( POL_1.NA POL_2.P ) ) \\WEDGE ( \\WEDGE ( POL_1.NA POL_2.NA ) POL_3.P ) ) \\WEDGE ( \\WEDGE ( \\WEDGE ( POL_1.NA POL_2.NA ) POL_3.NA ) POL_4.P ) ) \\WEDGE ( \\WEDGE ( \\WEDGE ( \\WEDGE ( POL_1.NA POL_2.NA ) POL_3.NA ) POL_4.NA ) POL_5.P ) ) )"
						// "( D_1 \\WEDGE ( user-id = Doctor-Emer-Agreement \\WEDGE role = Doctor \\WEDGE resource-type = Patient.Record \\WEDGE patient:management-info:status = NotAvailableNurseDoctor \\WEDGE patient:management-info:relative-passport-id = Patient-Relative \\WEDGE patient:management-info:conscious-status = Unconscious \\WEDGE patient:record:department = Heart \\WEDGE patient:emgergency-agreement:doctor = Doctor-Emer-Agreement \\WEDGE patient:emgergency-agreement:relative-passport-id = Patient-Relative \\WEDGE patient:emgergency-agreement:start-time = 1.523392594117E12 \\WEDGE patient:emgergency-agreement:end-time = 1.523406994117E12 \\WEDGE action-type = Read \\WEDGE location = Heart \\WEDGE current-time = 1.523399794117E12 ) )"
						// "( P_1 \\WEDGE ( user-id = Doctor-Patient \\WEDGE role = Doctor \\WEDGE party-type = \\EMPTYSET \\WEDGE resource-type = Patient.Record \\WEDGE patient:management-info:status = AvailableNurseDoctor \\WEDGE patient:management-info:relative-passport-id = \\EMPTYSET \\WEDGE patient:management-info:conscious-status = \\EMPTYSET \\WEDGE patient:record:department = Heart \\WEDGE patient:record:assigned-doctor-id = Doctor-Patient \\WEDGE patient:record:assigned-nurse-id = \\EMPTYSET \\WEDGE patient:emgergency-agreement:doctor = \\EMPTYSET \\WEDGE patient:emgergency-agreement:relative-passport-id = \\EMPTYSET \\WEDGE patient:emgergency-agreement:start-time = \\EMPTYSET \\WEDGE patient:emgergency-agreement:end-time = \\EMPTYSET \\WEDGE action-type = Read \\WEDGE location = \\EMPTYSET \\WEDGE current-time = \\EMPTYSET ) )"
						// "( D_1 \\WEDGE ( user-id = Doctor-Patient \\WEDGE role = Doctor \\WEDGE party-type = \\EMPTYSET \\WEDGE resource-type = Patient.Record \\WEDGE patient:management-info:status = AvailableNurseDoctor \\WEDGE patient:management-info:relative-passport-id = \\EMPTYSET \\WEDGE patient:management-info:conscious-status = \\EMPTYSET \\WEDGE patient:record:department = Heart \\WEDGE patient:record:assigned-doctor-id = Doctor-Patient \\WEDGE patient:record:assigned-nurse-id = \\EMPTYSET \\WEDGE patient:emgergency-agreement:doctor = \\EMPTYSET \\WEDGE patient:emgergency-agreement:relative-passport-id = \\EMPTYSET \\WEDGE action-type = Read \\WEDGE location = \\EMPTYSET ) )"
						// "( \\NEG ( ( \\REST party-type ( D_1 ) ) \\IMPLIES " +
						// "( P_1 ) ) \\WEDGE party-type" + " = \\EMPTYSET )"
						// "( ( P_1 \\IMPLIES ( \\NEG P_2 ) ) \\VEE ( D_1 \\IMPLIES ( \\NEG D_2 ) ) \\VEE ( IN_1 \\IMPLIES ( \\NEG IN_2 ) ) \\VEE ( ( \\NEG ( P_1 \\VEE ( D_1 \\VEE IN_1 ) ) ) \\IMPLIES ( P_2 \\VEE ( D_2 \\VEE IN_2 ) ) ) )"
						, new String[] {
						// "/Users/okielabackend/Downloads/alfa-project/alfa-project/src-gen/health_care.NormalAccessDoctor.xml"
						// "/Users/okielabackend/Documents/AccessControl/DEMO/data/health-care.xml",
						// "/Users/okielabackend/Documents/AccessControl/DEMO/data/health-care2.xml"
				"policies\\health-care.xml"
				// CONTINUE_A
				// CONTINUE_A
						// "E:\\Workspace\\Thesis\\Research\\DEMO\\data\\KMarket.xml"
						// "/Users/okielabackend/Documents/AccessControl/DEMO/data/KMarket.xml"
						});

		solve(qr);
		System.exit(0);
	}

	public static void main(String[] args) throws IOException {
		PolEvalExp qt = new PolEvalExp();
		qt.experimentPolEval();
	}
}
	

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/experiments/PolEvalExp.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */
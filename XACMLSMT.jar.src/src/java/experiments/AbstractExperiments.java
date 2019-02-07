package experiments;

import query.QueryRunner;

public abstract class AbstractExperiments {

	//protected static final String DOMAIN = "(  ( user-id = Doctor-Patient \\VEE user-id = Nurse-Patient \\VEE user-id = Doctor-Emer-Agreement \\VEE user-id = Doctor-Non-Patient \\VEE user-id = Nurse-Non-Patient ) \\WEDGE  ( role = Doctor \\VEE role = Nurse \\VEE role = External ) \\WEDGE  ( position = SeniorConsultant \\VEE position = Resident \\VEE position = NurseUnitManager ) \\WEDGE  ( party-type = Pharmacy \\VEE party-type = Insurance ) \\WEDGE resource-type = Patient.Record \\WEDGE  ( resource-path = Patient.Record.Billing \\VEE resource-path = Patient.Record.Medicines \\VEE resource-path = Patient.Record.HealthDiagnostic \\VEE resource-path = Patient.Record.AcquiredImmunodeficiencySyndromeWithDermatomycosis ) \\WEDGE  ( patient:management-info:status = NotAvailableDoctorNurse \\VEE patient:management-info:status = AvailableDoctorNurse ) \\WEDGE  ( patient:management-info:own-emergency-policies = Yes \\VEE patient:management-info:own-emergency-policies = No ) \\WEDGE patient:management-info:relative-passport-id = Patient-Relative \\WEDGE  ( patient:management-info:conscious-status = Conscious \\VEE patient:management-info:conscious-status = Unconscious ) \\WEDGE patient:record:department = Heart \\WEDGE patient:record:assigned-doctor-id = Doctor-Patient \\WEDGE patient:record:assigned-nurse-id = Doctor-Nurse \\WEDGE patient:emgergency-agreement:medical-staff = Doctor-Emer-Agreement \\WEDGE patient:emgergency-agreement:relative-passport-id = Patient-Relative \\WEDGE action-type = Read \\WEDGE location = Heart )";
	protected static final String DOMAIN =	"((http://kmarket.com/id/totalAmount = 500 \\VEE http://kmarket.com/id/totalAmount = 100 \\VEE http://kmarket.com/id/totalAmount = 1000) \\WEDGE (((http://kmarket.com/id/amount = 50 \\VEE http://kmarket.com/id/amount = 5 \\VEE http://kmarket.com/id/amount = 10) \\WEDGE (((http://kmarket.com/id/role = kmarket-silver \\VEE http://kmarket.com/id/role = kmarket-blue \\VEE http://kmarket.com/id/role = kmarket-gold) \\WEDGE (((urn:oasis:names:tc:xacml:1.0:resource:resource-id = Drink \\VEE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Medicine \\VEE urn:oasis:names:tc:xacml:1.0:resource:resource-id = Liquor) )))))))";
	public QueryRunner getQueryRunner() {
		QueryRunner qr = new QueryRunner();
	
		QueryRunner.verbose = false;
		QueryRunner.z3output = true;
		QueryRunner.debug = false;
		QueryRunner.models = true;
		QueryRunner.threshold = 1;
		QueryRunner.printVarDomains = true;
		QueryRunner.xacmlVersion = 3;
	
		return qr;
	}

}

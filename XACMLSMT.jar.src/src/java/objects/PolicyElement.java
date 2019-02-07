package objects;

public abstract class PolicyElement {
	protected Target target = null;
	protected String description = null;
	protected String id;
	protected CombiningAlg comAlg;
	protected Policy parent = null;

	public static enum CombiningAlg {
		PermitOverrides, OrderedPermitOverrides, DenyOverrides, FirstApplicable, OnlyOneApplicable, DenyUnlessPermit, PermitUnlessDeny;
		private CombiningAlg() {
		}
	}

	public static enum Effect {
		Permit, Deny, NotApplicable, Indeterminate;

		private Effect() {
		}
	}

	public String getId() {
		return this.id;
	}

	public void setId(String Id) {
		this.id = Id;
	}

	public void setTarget(Target t) {
		this.target = t;
	}

	public Target getTarget() {
		return this.target;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public boolean isApplicable() {
		return true;
	}

	public void setParent(PolicyElement p) {
		this.parent = ((Policy) p);
	}

	public Policy getParent() {
		return this.parent;
	}
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/objects/PolicyElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
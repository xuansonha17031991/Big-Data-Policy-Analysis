package combiningalg;

import policy.SMTPolicyElement;
import policy.SMTPolicyElement.PolicyElementType;

public interface ISMTCombAlg
{
	public SMTPolicyElement executeAlgAllPairs(PolicyElementType type, boolean withDetails);
}


/* Location:              /Users/okielabackend/Downloads/XACMLSMT.jar!/combiningalg/ISMTCombAlg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
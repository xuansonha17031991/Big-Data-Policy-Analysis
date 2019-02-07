package smtfunctions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import objects.Expression;
import utils.Pair;

import com.microsoft.z3.Context;
import com.microsoft.z3.EnumSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.SetSort;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Z3Exception;

import functions.FunctionFactory;

public abstract class SMTFunction extends Expression {
	Sort[] parameterSorts;
	Sort returnSort;
	List<Z3FuncExpr> SMTparameters;
	Context context;
	int type;

	protected static Pair<Sort, Sort> findSort(String typeName, Map<String, Pair<SetSort, EnumSort>> listOfSorts, Context con) {
		try {
			FunctionFactory.DataTypes typeNameEnum = FunctionFactory.typeNames.get(typeName);
			// Two groups EnumType and SingleType

			switch (typeNameEnum) {
			case xinteger:
				return new Pair(con.getIntSort(), null);
			case xdouble:
				return new Pair(con.getRealSort(), null);
			case xboolean:
				return new Pair(con.getBoolSort(), null);
			case xtime:
				return new Pair(con.getIntSort(), null);
			}

			// If the type is SetSort, we also add the EnumSort with related values.
			for (Map.Entry<String, Pair<SetSort, EnumSort>> entry : listOfSorts.entrySet()) {
				String entryType = entry.getKey().substring(entry.getKey().indexOf("#") + 1, entry.getKey().length());
				if (entryType.equals(typeName + "_sort"))
					return new Pair(((Pair) entry.getValue()).first, ((Pair) entry.getValue()).second);
			}
		} catch (Z3Exception zex) {
			System.err.println("Exception in findSorts of SMTFunction" + zex.getMessage());
		}
		return null;
	}

	public static Sort findParameterSort(String functionName, Map<String, Pair<SetSort, EnumSort>> listOfSorts, Context con) {
		try {
			FunctionFactory.FunctionTypes funcName = FunctionFactory.functionNames.get(functionName);
			if (funcName != null)
				switch (funcName) {
				case OneAndOnly:
				case LessOrEqual:
				case LessThan:
				case GreaterOrEqual:
				case GreaterThan:
				case Equal:
				case Match:
				case Bag:
				case AtLeastOneMemberOf:
				case TimeInRange:
				case Add:
				case Subtract:
				case IsIn:
					// The first substring is the type, e.g. string-one-and-only
					return findSort(functionName.substring(0, functionName.indexOf("-")), listOfSorts, con).first;
				case Not:
				case And:
				case Or:
					return con.getBoolSort();
				}
		} catch (Z3Exception zex) {
			System.err.println("Exception in findParameterSort of SMTFunction " + zex.getMessage());
		}
		return null;
	}

	protected void setSorts() {
		try {
			this.parameterSorts = new Sort[this.SMTparameters.size()];
			int i = 0;
			for (Iterator<Z3FuncExpr> it = this.SMTparameters.iterator(); it.hasNext();) {
				this.parameterSorts[i] = it.next().getPrincipalExpr().getSort();
				i++;
			}
		} catch (Z3Exception zex) {
			int i;
			Iterator<Z3FuncExpr> it;
			System.err.println("Exception in setSorts SMTFunction" + zex.getMessage());
		}
	}

	protected List<Expr> getCardExprFromParams() {
		List<Expr> cardExprs = new ArrayList();
		for (int i = 0; i < this.SMTparameters.size(); i++) {
			for (int j = 0; (this.SMTparameters.get(i).getCardExpr() != null) && (j < this.SMTparameters.get(i).getCardExpr().size()); j++) {
				boolean contained = false;
				for (int t = 0; t < cardExprs.size(); t++) {
					if (cardExprs.get(t).getSExpr().equals(this.SMTparameters.get(i).getCardExpr().get(j))) {
						contained = true;
						break;
					}
				}
				if (!contained)
					cardExprs.add(this.SMTparameters.get(i).getCardExpr().get(j));
			}
		}
		return cardExprs.size() > 0 ? cardExprs : null;
	}

	protected List<Expr> getAdditionalExprFromParams() {
		List<Expr> addExprs = new ArrayList();
		for (int i = 0; i < this.SMTparameters.size(); i++) {
			for (int j = 0; (this.SMTparameters.get(i).getAdditionalExpr() != null) && (j < this.SMTparameters.get(i).getAdditionalExpr().size()); j++) {
				boolean contained = false;
				for (int t = 0; t < addExprs.size(); t++) {
					if (addExprs.get(t).getSExpr().equals(this.SMTparameters.get(i).getAdditionalExpr().get(j))) {
						contained = true;
						break;
					}
				}
				if (!contained)
					addExprs.add(this.SMTparameters.get(i).getAdditionalExpr().get(j));
			}
		}
		return addExprs.size() > 0 ? addExprs : null;
	}

	public List<Z3FuncExpr> getSMTParameters() {
		return this.SMTparameters;
	}

	@Override
	public List<Expression> getParameters() {
		return this.parameters;
	}

	public Sort getReturnSort() {
		return this.returnSort;
	}

	public Sort[] getParameterSorts() {
		return this.parameterSorts;
	}

	public abstract Z3FuncExpr generateExpr();

	public static class Z3FuncExpr {
		Expr principal = null;
		List<Expr> additional = null;
		List<Expr> cardinality = null;
		int addExConnectType;

		public Z3FuncExpr(Expr p, List<Expr> add, List<Expr> card) {
			this.principal = p;
			if (add != null)
				this.additional = add;
			if (card != null) {
				this.cardinality = card;
			}
		}

		public void setAddConnectType(int type) {
			this.addExConnectType = type;
		}

		public Expr getPrincipalExpr() {
			return this.principal;
		}

		public List<Expr> getCardExpr() {
			return this.cardinality;
		}

		public List<Expr> getAdditionalExpr() {
			return this.additional;
		}
	}
}

/*
 * Location:
 * /Users/okielabackend/Downloads/XACMLSMT.jar!/smtfunctions/SMTFunction.class
 * Java compiler version: 8 (52.0) JD-Core Version: 0.7.1
 */
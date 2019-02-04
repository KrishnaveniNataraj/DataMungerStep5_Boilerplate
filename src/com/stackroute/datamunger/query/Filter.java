package com.stackroute.datamunger.query;

import java.util.Locale;

//This class contains methods to evaluate expressions
public class Filter {

	/*
	 * The evaluateExpression() method of this class is responsible for evaluating
	 * the expressions mentioned in the query. It has to be noted that the process
	 * of evaluating expressions will be different for different data types. there
	 * are 6 operators that can exist within a query i.e. >=,<=,<,>,!=,= This method
	 * should be able to evaluate all of them. Note: while evaluating string
	 * expressions, please handle uppercase and lowercase
	 * 
	 */

	public boolean evaluateExpressions(final String propertyValue, final String condition, final String value,
			final String dataType) {
		boolean bool = false;

		switch (dataType) {

		case "java.lang.String":
			switch (condition) {
			case "=":
				bool = equalToString(value, propertyValue);
				break;
			case "!=":
				bool = notEqualToString(value, propertyValue);
				break;
			default:
				bool = false;
				break;
			}
			break;
		case "java.lang.Integer":
			switch (condition) {
			case "=":
				bool = equalToInt(value, propertyValue);
				break;
			case "!=":
				bool = notEqualToInt(value, propertyValue);
				break;
			case ">":
				bool = greaterThanInt(value, propertyValue);
				break;
			case ">=":
				bool = greaterThanEqualToInt(value, propertyValue);
				break;
			case "<":
				bool = lessThanInt(value, propertyValue);
				break;
			case "<=":
				bool = lessThanEqualToInt(value, propertyValue);
				break;
			default:
				bool = false;
				break;
			}
			break;

		case "java.lang.Double":
			switch (condition) {
			case "=":
				bool = equalToDouble(value, propertyValue);
				break;
			case "!=":
				bool = notEqualToDouble(value, propertyValue);
				break;
			case ">":
				bool = greaterThanDouble(value, propertyValue);
				break;
			case ">=":
				bool = greaterThanEqualToDouble(value, propertyValue);
				break;
			case "<":
				bool = lessThanDouble(value, propertyValue);
				break;
			case "<=":
				bool = lessThanEqualToDouble(value, propertyValue);
				break;
			default:
				bool = false;
				break;
			}
			break;

		case "java.util.Date":
			switch (condition) {
			case "=":
				bool = equalToDate(value, propertyValue);
				break;
			case "!=":
				bool = notEqualToDate(value, propertyValue);
				break;
			case ">":
				bool = greaterThanDate(value, propertyValue);
				break;
			case ">=":
				bool = greaterThanEqualToDate(value, propertyValue);
				break;
			case "<":
				bool = lessThanDate(value, propertyValue);
				break;
			case "<=":
				bool = lessThanEqualToDate(value, propertyValue);
				break;
			default:
				bool = false;
				break;
			}
			break;

		case "java.lang.Object":
			switch (condition) {
			case "=":
				bool = equalToObject(value, propertyValue);
				break;
			case "!=":
				bool = notEqualToObject(value, propertyValue);
				break;
			default:
				bool = false;
				break;
			}
			break;
		default:
			bool = false;
			break;
		}

		return bool;
	}

	// Method containing implementation of equalTo operator
	private boolean equalToString(final String value, final String propertyValue) {
		boolean result = false;
		String val = value.toLowerCase(Locale.ENGLISH);
		if (val.equals(propertyValue)) {
			result = true;
		}
		return result;
	}

	private boolean equalToInt(final String value, final String propertyValue) {
		boolean result = false;
		final int a = Integer.parseInt(value);
		final int b = Integer.parseInt(propertyValue);
		if (a == b) {
			result = true;
		}
		return result;
	}

	private boolean equalToDouble(final String value, final String propertyValue) {
		boolean result = false;
		final Double a = Double.parseDouble(value);
		final Double b = Double.parseDouble(propertyValue);
		if (a == b) {
			result = true;
		}
		return result;
	}

	private boolean equalToObject(final String value, final String propertyValue) {
		boolean result = false;
		if (value.equals(propertyValue)) {
			result = true;
		}
		return result;
	}

	private boolean equalToDate(final String value, final String propertyValue) {
		boolean result = false;
		if (value.compareTo(propertyValue) == 0) {
			result = true;
		}
		return result;
	}

	// Method containing implementation of notEqualTo operator

	private boolean notEqualToString(final String value, final String propertyValue) {
		String val = value.toLowerCase(Locale.ENGLISH);
		boolean result = false;
		if (!val.equals(propertyValue)) {
			result = true;
		}
		return result;
	}

	private boolean notEqualToInt(final String value, final String propertyValue) {
		boolean result = false;
		final int a = Integer.parseInt(value);
		final int b = Integer.parseInt(propertyValue);
		if (a != b) {
			result = true;
		}
		return result;
	}

	private boolean notEqualToDouble(final String value, final String propertyValue) {
		boolean result = false;
		final Double a = Double.parseDouble(value);
		final Double b = Double.parseDouble(propertyValue);
		if (a != b) {
			result = true;
		}
		return result;
	}

	private boolean notEqualToObject(final String value, final String propertyValue) {
		boolean result = false;
		if (!value.equals(propertyValue)) {
			result = true;
		}
		return result;
	}

	private boolean notEqualToDate(final String value, final String propertyValue) {
		boolean result = false;
		if (value.compareTo(propertyValue) != 0) {
			result = true;
		}
		return result;
	}

	// Method containing implementation of greaterThan operator

	private boolean greaterThanInt(final String value, final String propertyValue) {
		boolean result = false;
		final int a = Integer.parseInt(value);
		final int b = Integer.parseInt(propertyValue);
		if (a > b) {
			result = true;
		}
		return result;
	}

	private boolean greaterThanDouble(final String value, final String propertyValue) {
		boolean result = false;
		final Double a = Double.parseDouble(value);
		final Double b = Double.parseDouble(propertyValue);
		if (a > b) {
			result = true;
		}
		return result;
	}

	private boolean greaterThanDate(final String value, final String propertyValue) {
		boolean result = false;
		if (value.compareTo(propertyValue) > 0) {
			result = true;
		}
		return result;
	}

	// Method containing implementation of greaterThanOrEqualTo operator

	private boolean greaterThanEqualToInt(final String value, final String propertyValue) {
		boolean result = false;
		final int a = Integer.parseInt(value);
		final int b = Integer.parseInt(propertyValue);
		if (a >= b) {
			result = true;
		}
		return result;
	}

	private boolean greaterThanEqualToDouble(final String value, final String propertyValue) {
		boolean result = false;
		final Double a = Double.parseDouble(value);
		final Double b = Double.parseDouble(propertyValue);
		if (a >= b) {
			result = true;
		}
		return result;
	}

	private boolean greaterThanEqualToDate(final String value, final String propertyValue) {
		boolean result = false;
		if (value.compareTo(propertyValue) >= 0) {
			result = true;
		}
		return result;
	}

	// Method containing implementation of lessThan operator

	private boolean lessThanInt(final String value, final String propertyValue) {
		boolean result = false;
		final int a = Integer.parseInt(value);
		final int b = Integer.parseInt(propertyValue);
		if (a < b) {
			result = true;
		}
		return result;
	}

	private boolean lessThanDouble(final String value, final String propertyValue) {
		boolean result = false;
		final Double a = Double.parseDouble(value);
		final Double b = Double.parseDouble(propertyValue);
		if (a < b) {
			result = true;
		}
		return result;
	}

	private boolean lessThanDate(final String value, final String propertyValue) {
		boolean result = false;
		if (value.compareTo(propertyValue) < 0) {
			result = true;
		}
		return result;
	}

	// Method containing implementation of lessThanOrEqualTo operator

	private boolean lessThanEqualToInt(final String value, final String propertyValue) {
		boolean result = false;
		final int a = Integer.parseInt(value);
		final int b = Integer.parseInt(propertyValue);
		if (a <= b) {
			result = true;
		}
		return result;
	}

	private boolean lessThanEqualToDouble(final String value, final String propertyValue) {
		boolean result = false;
		final Double a = Double.parseDouble(value);
		final Double b = Double.parseDouble(propertyValue);
		if (a <= b) {
			result = true;
		}
		return result;
	}

	private boolean lessThanEqualToDate(final String value, final String propertyValue) {
		boolean result = false;
		if (value.compareTo(propertyValue) <= 0) {
			result = true;
		}
		return result;
	}

}

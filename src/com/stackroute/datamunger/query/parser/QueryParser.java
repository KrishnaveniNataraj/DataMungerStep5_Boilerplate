package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		QueryParameter queryParameter = new QueryParameter();
		queryParameter.setQueryString(queryString);
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));		
		return queryParameter;
	}

	private String[] getSplitStrings(String queryString) {
		return queryString.toLowerCase().split(" ");
	}
	/*
	 * extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String getFileName(String queryString) {
		String fileName = "";
		String[] queriesStr = getSplitStrings(queryString);
		for (int i = 0; i < queriesStr.length; i++) {
			if ("from".equals(queriesStr[i])) {
				fileName = queriesStr[i + 1];
			}
		}
		return fileName;
	}
	
	public String getBaseQuery(String queryString) {
		String fileName = getFileName(queryString);
		return queryString.substring(0, queryString.indexOf(fileName)) + fileName;
	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {
		int index = queryString.indexOf(" order by ");
		if (index == -1)
			return null;
		String orderByClause = queryString.substring(index + 10).trim();
		return Arrays.asList(orderByClause.split(","));
	}

	/*
	 * extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	public List<String> getGroupByFields(String queryString) {
		int index = queryString.indexOf(" group by ");
		if (index == -1)
			return null;
		String[] strs = queryString.substring(index + 10).trim().split(" ");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			if ("order".equals(strs[i])) {
				break;
			}
			sb.append(strs[i].trim() + "#");
		}
		return Arrays.asList(sb.toString().split("#"));
	}

	/*
	 * extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	public List<String> getFields(String queryString) {
		return Arrays.asList(queryString.split("\\s*\\bselect\\b\\s*|\\s*\\bfrom\\b\\s*")[1].split("\\s*,\\s*"));
	}

	/*
	 * extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 * 
	 */
	public static void main(String[] args) {
		String queryString="select city,team1,team2,winner,toss_decision from data/ipl.csv where toss_decision = bat or toss_decision != bat";
		QueryParser parser=new QueryParser();
		System.out.println(parser.getRestrictions(queryString));
	}
	
	public List<Restriction> getRestrictions(String queryString) {
		List<Restriction> restrictions = new ArrayList<>();
		int conditionIndex = queryString.indexOf(" where ");
		if (conditionIndex == -1) {
			return null;
		}
		String conditionPartQuery = queryString.substring(conditionIndex + 7).split("\\border\\b|\\bgroup\\b")[0];
		String[] queriesPart = conditionPartQuery.split("\\s*\\band\\b\\s*|\\s*\\bor\\b\\s*");
		for (int i = 0; i < queriesPart.length; i++) {
			Boolean matchFound=Boolean.FALSE;
			Pattern pattern =null;
			Matcher matcher =null;
			if(!matchFound) {
			pattern = Pattern.compile("<=|>=|!=");
			matcher = pattern.matcher(queriesPart[i]);			
				if(matcher.find()) {
					restrictions.add(
						new Restriction(
							queriesPart[i].substring(0, matcher.start()).trim(),
							queriesPart[i].substring(matcher.start() + 2).replaceAll("'", "").trim(), 
							matcher.group()));
					matchFound=Boolean.TRUE;
				}
			}
			if(!matchFound) {
				pattern = Pattern.compile("<|>|=");
				matcher = pattern.matcher(queriesPart[i]);
				if(matcher.find()) {
					restrictions.add(
						new Restriction(
							queriesPart[i].substring(0, matcher.start()).trim(),
							queriesPart[i].substring(matcher.start() + 1).replaceAll("'", "").trim(), 
							matcher.group()));
					matchFound=Boolean.TRUE;
				}
			}
		}
		return restrictions;
	}

	/*
	 * extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * the query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	public List<String> getLogicalOperators(String queryString) {
		List<String> logicalOperators = new ArrayList<>();
		int conditionIndex = queryString.indexOf(" where ");
		if (conditionIndex == -1) {
			return null;
		}
		String conditionPartQuery = queryString.substring(conditionIndex + 7).replaceAll("'", " ");
		Pattern pattern = Pattern.compile("\\band\\b|\\bor\\b", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(conditionPartQuery);
		while (matcher.find()) {
			logicalOperators.add(matcher.group());
		}
		return logicalOperators;
	}

	/*
	 * extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied
	 * 
	 * Please note that more than one aggregate function can be present in a query
	 * 
	 * 
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		List<AggregateFunction> aggregateFunctions = new ArrayList<>();
		String fieldStr = queryString.split("\\bselect\\b|\\bfrom\\b")[1];
		String[] fields = fieldStr.split(",");
		for (int j = 0; j < fields.length; j++) {
			String field = fields[j].trim();
			if (field.startsWith("sum(") || field.startsWith("count(") || field.startsWith("min(")
					|| field.startsWith("max(") || field.startsWith("avg")) {
				aggregateFunctions
						.add(new AggregateFunction(field.substring(field.indexOf("(") + 1, field.indexOf(")")),
								field.substring(0, field.indexOf("("))));
			}
		}

		if (aggregateFunctions.isEmpty())
			return null;
		return aggregateFunctions;
	}

	
	
	
	

}

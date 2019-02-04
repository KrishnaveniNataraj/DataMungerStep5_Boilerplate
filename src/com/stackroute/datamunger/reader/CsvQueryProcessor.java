package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.stackroute.datamunger.query.DataSet;
import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Filter;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.query.Row;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

public class CsvQueryProcessor implements QueryProcessingEngine {
	/*
	 * This method will take QueryParameter object as a parameter which contains the
	 * parsed query and will process and populate the ResultSet
	 */
	public DataSet getResultSet(QueryParameter queryParameter) {

		BufferedReader br = null;
		String line=null;
		Header header=null;
		DataSet dataSet = new DataSet();

		/*
		 * initialize BufferedReader to read from the file which is mentioned in
		 * QueryParameter. Consider Handling Exception related to file reading.
		 */
		try {
			br = new BufferedReader(new FileReader(queryParameter.getFileName()));

			/*
			 * read the first line which contains the header. Please note that the headers
			 * can contain spaces in between them. For eg: city, winner
			 */
			
			
			/*
			 * read the next line which contains the first row of data. We are reading this
			 * line so that we can determine the data types of all the fields. Please note
			 * that ipl.csv file contains null value in the last column. If you do not
			 * consider this while splitting, this might cause exceptions later
			 */
			
			/*
			 * populate the header Map object from the header array. header map is having
			 * data type <String,Integer> to contain the header and it's index.
			 */

			/*
			 * We have read the first line of text already and kept it in an array. Now, we
			 * can populate the RowDataTypeDefinition Map object. RowDataTypeDefinition map
			 * is having data type <Integer,String> to contain the index of the field and
			 * it's data type. To find the dataType by the field value, we will use
			 * getDataType() method of DataTypeDefinitions class
			 */

			Boolean headerRow=Boolean.TRUE;
			Map<String, Integer> headerMap= new HashMap<>();
			Map<Integer, String> dataTypeDefinitionsMap = new HashMap<>();
			br.mark(1);
			while((line=br.readLine())!=null) {
				if(headerRow) {
					String[] headers=line.trim().split("\\s*,\\s*");
					for(int i=0;i<headers.length;i++) {
						headerMap.put(headers[i], i);
					}
					header= new Header(headerMap);
					headerRow=Boolean.FALSE;
				}else {
					String[] data=line.trim().split("\\s*,\\s*", headerMap.size());
					for(int i=0;i<data.length;i++) {
						dataTypeDefinitionsMap.put(i, DataTypeDefinitions.getDataType(data[i]).toString());
					}
					break;
				}
			}//while			

			
			/*
			 * once we have the header and dataTypeDefinitions maps populated, we can start
			 * reading from the first line. We will read one line at a time, then check
			 * whether the field values satisfy the conditions mentioned in the query,if
			 * yes, then we will add it to the resultSet. Otherwise, we will continue to
			 * read the next line. We will continue this till we have read till the last
			 * line of the CSV file.
			 */

			/* reset the buffered reader so that it can start reading from the first line */
			br.reset();

			/*
			 * skip the first line as it is already read earlier which contained the header
			 */

			/* read one line at a time from the CSV file till we have any lines left */				

			/*
			 * once we have read one line, we will split it into a String Array. This array
			 * will continue all the fields of the row. Please note that fields might
			 * contain spaces in between. Also, few fields might be empty.
			 */

			/*
			 * if there are where condition(s) in the query, test the row fields against
			 * those conditions to check whether the selected row satifies the conditions
			 */

			/*
			 * from QueryParameter object, read one condition at a time and evaluate the
			 * same. For evaluating the conditions, we will use evaluateExpressions() method
			 * of Filter class. Please note that evaluation of expression will be done
			 * differently based on the data type of the field. In case the query is having
			 * multiple conditions, you need to evaluate the overall expression i.e. if we
			 * have OR operator between two conditions, then the row will be selected if any
			 * of the condition is satisfied. However, in case of AND operator, the row will
			 * be selected only if both of them are satisfied.
			 */

			/*
			 * check for multiple conditions in where clause for eg: where salary>20000 and
			 * city=Bangalore for eg: where salary>20000 or city=Bangalore and dept!=Sales
			 */
			boolean condition[] = null;
			String logicalOperators[] = null;
			List<String> logicalOperatorslist = null;
			String[] headerStr=null;
			// logical operators
			logicalOperatorslist = queryParameter.getLogicalOperators();
			if (logicalOperatorslist != null) {
				logicalOperators = new String[logicalOperatorslist.size()];
				int m = 0;
				for (final String string : logicalOperatorslist) {
					logicalOperators[m] = string;
					m++;
				}
			}
			
			int linenumber = 1;
			long rownum = 1;			
			while ((line = br.readLine()) != null) {				

				if (linenumber == 1) {
					headerStr = line.trim().split("\\s*,\\s*");
					linenumber++;
					continue;
				}

				Filter filter = null;
				int index = 0;
				String data[] = null;
				final StringBuffer dataBfr = new StringBuffer();
				String[] dataRowDataTypeStr=null;
				boolean finalcondition = false;
				final Row rowMapObj = new Row();
				line = line.replaceAll(",,", ", ,");
				dataBfr.append(line);
				
				data = dataBfr.toString().trim().split(",", headerStr.length);
				dataRowDataTypeStr=line.trim().split("\\s*,\\s*",headerStr.length);

				// restrictions
				if (queryParameter.getRestrictions() != null) {
					condition = new boolean[queryParameter.getRestrictions().size()];
					Iterator<Restriction> restrictionIter = null;
					filter = new Filter();
					restrictionIter = queryParameter.getRestrictions().iterator();
					int n = 0;
					while (restrictionIter.hasNext()) {
						final Iterator<Map.Entry<String, Integer>> headerIter = sortByValue(header.getHeaders()).entrySet()
								.iterator();
						final Restriction rest = restrictionIter.next();
						while (headerIter.hasNext()) {
							final Map.Entry<String, Integer> myEntry = headerIter.next();
							if (myEntry.getKey().equals(rest.getPropertyName())) {
								index = myEntry.getValue();
								break;
							}
						}
						condition[n] = filter.evaluateExpressions(rest.getPropertyValue(), rest.getCondition(),
								data[index], DataTypeDefinitions.getDataType(dataRowDataTypeStr[index]).toString());
						n++;
					}
				}

				@SuppressWarnings("unused")
				boolean stepout=false;
				// final condition
				if (condition != null && condition.length > 1) {
					int counter = 0;
					for (int i = 0; i < logicalOperators.length; i++) {
						for (int j = 0; j < condition.length - 1; j++) {
							if (logicalOperators[i].equals("and")) {
								finalcondition = condition[counter] & condition[counter + 1];
								break;
							} else if (logicalOperators[i].equals("or")) {
								finalcondition = condition[counter] | condition[counter + 1];
								if(finalcondition) {
									stepout=true;
								}
								break;
							}
						}
						if(stepout=true) {
							break;
						}
						counter++;
					}
				} else if (condition != null && condition.length == 1) {
					finalcondition = condition[0];
				} else {
					finalcondition = true;
				}

				// add data to object
				if (!finalcondition) {
					continue;
				} else {
					String[] field = null;
					List<String> fields = null;
					int l = 0;

					// get fields
					fields = queryParameter.getFields();
					field = new String[fields.size()];
					l = 0;
					for (final String string : fields) {
						field[l] = string;
						l++;
					}

					// get all columns
					if (field[0].equals("*")) {
						for (int i = 0; i < headerStr.length; i++) {
							rowMapObj.put(headerStr[i], data[i]);
						}

					}

					// get selected columns
					else {
						l = 0;
						for (int i = 0; i < headerStr.length; i++) {
							for (int j = 0; j < field.length; j++) {
								if (headerStr[i].equals(field[j])) {
									rowMapObj.put(headerStr[i], data[i]);
									l++;
								}
							}
						}
					}

					dataSet.put(rownum++, rowMapObj);
				}
			}	


			/*
			 * if the overall condition expression evaluates to true, then we need to check
			 * if all columns are to be selected(select *) or few columns are to be
			 * selected(select col1,col2). In either of the cases, we will have to populate
			 * the row map object. Row Map object is having type <String,String> to contain
			 * field Index and field value for the selected fields. Once the row object is
			 * populated, add it to DataSet Map Object. DataSet Map object is having type
			 * <Long,Row> to hold the rowId (to be manually generated by incrementing a Long
			 * variable) and it's corresponding Row Object.
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		/* return dataset object */
		return dataSet;
	}
	
	
	/**
	 * Method for sorting hashmap by values
	 * 
	 * @param unsortMap
	 * @return
	 */
	private static Map<String, Integer> sortByValue(final Map<String, Integer> unsortMap) {

		// 1. Convert Map to List of Map
		final List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try switch the o1 o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(final Map.Entry<String, Integer> o1,final Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// 3. Loop the sorted list and put it into a new insertion order Map
		// LinkedHashMap
		final Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (final Map.Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		/*
		 * //classic iterator example for (Iterator<Map.Entry<String, Integer>> it =
		 * list.iterator(); it.hasNext(); ) { Map.Entry<String, Integer> entry =
		 * it.next(); sortedMap.put(entry.getKey(), entry.getValue()); }
		 */

		return sortedMap;
	}

}

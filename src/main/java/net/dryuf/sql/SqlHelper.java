/*
 * Dryuf framework
 *
 * ----------------------------------------------------------------------------------
 *
 * Copyright (C) 2000-2015 Zbyněk Vyškovský
 *
 * ----------------------------------------------------------------------------------
 *
 * LICENSE:
 *
 * This file is part of Dryuf
 *
 * Dryuf is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dryuf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dryuf; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * @author	2000-2015 Zbyněk Vyškovský
 * @link	mailto:kvr@matfyz.cz
 * @link	http://kvr.matfyz.cz/software/java/dryuf/
 * @link	http://github.com/dryuf/
 * @license	http://www.gnu.org/licenses/lgpl.txt GNU Lesser General Public License v3
 */

package net.dryuf.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;


/**
 * Helper class for SQL queries
 */
public class SqlHelper extends java.lang.Object
{
	/**
	 * Sets autocommit on existing connection
	 *
	 * @param dataSource
	 * 	SQL data source
	 */
	public static Connection	getDataSourceConnection(DataSource dataSource)
	{
		try {
			return dataSource.getConnection();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets autocommit on existing connection
	 *
	 * @param connection
	 * 	SQL connection
	 * @param autoCommit
	 * 	the new status of auto commit
	 */
	public static void		setAutoCommit(Connection connection, boolean autoCommit)
	{
		try {
			connection.setAutoCommit(autoCommit);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Closes connection.
	 *
	 * @param connection
	 * 	SQL connection
	 */
	public static void		closeConnection(Connection connection)
	{
		try {
			connection.close();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Commits connection.
	 *
	 * @param connection
	 * 	SQL connection
	 */
	public static void		commitConnection(Connection connection)
	{
		try {
			connection.commit();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Rolls back connection.
	 *
	 * @param connection
	 * 	SQL connection
	 */
	public static void		rollbackConnection(Connection connection)
	{
		try {
			connection.rollback();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Caches a statement provided as string+
	 *
	 * @return
	 * 	the prepared statement
	 */
	public static PreparedStatement	cacheString(Connection connection, String name, String statement)
	{
		try {
			return connection.prepareStatement(statement);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Queries field for specified table and filter+
	 * Exactly one row is expected
	 *
	 * @param connection
	 * 	the database connection to use
	 * @param field
	 * 	the required field
	 * @param table
	 * 	table name
	 * @param filter
	 * 	filter column name
	 * @param filterValue
	 * 	the value for filtered column
	 *
	 * @return
	 * 	field value
	 *
	 * @note
	 * 	Throws an exception if not exactly one row is found
	 */
	public static Object		queryOneField(Connection connection, String field, String table, String filter, Object filterValue)
	{
		try {
			String ststr = "SELECT "+field+" FROM "+table+" WHERE "+filter+" = ?";
			PreparedStatement st = connection.prepareStatement(ststr);
			st.setObject(1, filterValue);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				if (rs.next()) {
					throw new NonUniqueResultException("more than one row found for statement "+ststr);
				}
				return rs.getObject(1);
			}
			else {
				throw new NoResultException("no row found for statement "+ststr);
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Queries at most one row+
	 * Null is returned in case now row is found
	 *
	 * @param connection
	 * 	the database connection to use
	 * @param statement
	 * 	statement with ? binds
	 * @param binds
	 * 	bind variables for the statement
	 *
	 * @return null
	 * 	if no row is found
	 * @return result
	 * 	as an associative array of the retrieved columns
	 *
	 * @note
	 * 	Throws an exception if more than one row is found
	 */
	public static Map<String, Object> queryRow(Connection connection, String statement, Object[] binds)
	{
		try {
			PreparedStatement st = bindParams(connection.prepareStatement(statement), binds);
			ResultSet rs = st.executeQuery();
			Map<String, Object> rowMap;
			if ((rowMap = fetchMap(rs)) != null) {
				if (rs.next()) {
					throw new NonUniqueResultException("more than one row found for statement "+statement);
				}
				return rowMap;
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Queries exactly one row
	 *
	 * @param connection
	 * 	the database connection to use
	 * @param statement
	 * 	statement with ? binds
	 * @param binds
	 * 	bind variables for the statement
	 *
	 * @return result
	 * 	as an associative array of the retrieved columns
	 *
	 * @note
	 * 	Throws an exception if not exactly one row is found
	 */
	public static Map<String, Object> queryOneRow(Connection connection, String statement, Object[] binds)
	{
		Map<String, Object> row;
		if ((row = queryRow(connection, statement, binds)) == null)
			throw new NoResultException("no row found for statement "+statement);
		return row;
	}

	/**
	 * Queries a single column
	 *
	 * @return null
	 * 	either if the column is null or now row is returned at all
	 * @return
	 * 	the value of the first column
	 *
	 * @throw net.dryuf.sql.SqlTooManyRowsException
	 * 	if more than one row is returned
	 */
	public static Object		queryColumn(Connection connection, String statement, Object[] binds)
	{
		try {
			PreparedStatement st = connection.prepareStatement(statement);
			bindParams(st, binds);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				if (rs.next()) {
					throw new NonUniqueResultException("more than one row found for statement "+statement);
				}
				return rs.getObject(1);
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Queries a single column
	 *
	 * @return
	 * 	the value of the first column
	 *
	 * @throw net.dryuf.sql.SqlNoDataException
	 * 	if no row is returned
	 * @throw net.dryuf.sql.SqlTooManyRowsException
	 * 	if more than one row is returned
	 */
	public static Object		queryOneColumn(Connection connection, String statement, Object[] binds)
	{
		try {
			PreparedStatement st = bindParams(connection.prepareStatement(statement), binds);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				if (rs.next()) {
					throw new NonUniqueResultException("more than one row found for statement "+statement);
				}
				return rs.getObject(1);
			}
			else {
				throw new NoResultException("no row found for statement "+statement);
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * query rows and returns them as array of associative arrays
	 *
	 * @return
	 * 	the array of rows as associative arrays (even if empty)
	 */
	public static List<Map<String, Object>> queryRows(Connection connection, String statement, Object[] binds)
	{
		try {
			PreparedStatement st = bindParams(connection.prepareStatement(statement), binds);
			ResultSet rs = st.executeQuery();
			List<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();
			Map<String, Object> rowMap;
			while ((rowMap = fetchMap(rs)) != null) {
				rows.add(rowMap);
			}
			return rows;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs an non-result statement
	 *
	 * @return
	 * 	the number of rows modified
	 */
	public static long		updateAny(Connection connection, String statement, Object[] binds)
	{
		try {
			return bindParams(connection.prepareStatement(statement), binds).executeUpdate();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs an non-result statement, expecting exactly one match
	 *
	 * @return 1
	 *
	 * @note
	 * 	throws an exception if less or more than one row is updated
	 */
	public static long		updateOneRow(Connection connection, String statement, Object[] binds)
	{
		long count = updateAny(connection, statement, binds);
		if (count != 1)
			throw new NonUniqueResultException("updated "+count+" rows, expected one");
		return 1;
	}

	/**
	 * runs a prepared query with specified bindings
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the result set
	 */
	public static ResultSet		executeResult(PreparedStatement statement, Object[] binds)
	{
		try {
			ResultSet rs = bindParams(statement, binds).executeQuery();
			return rs;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a prepared query with specified bindings
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the list of queried rows as array of hashes
	 */
	public static List<Map<String, Object>> executeRows(PreparedStatement statement, Object[] binds)
	{
		try {
			ResultSet rs = bindParams(statement, binds).executeQuery();
			List<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();
			Map<String, Object> rowMap;
			if ((rowMap = fetchMap(rs)) != null) {
				rows.add(rowMap);
			}
			return rows;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a prepared query with specified bindings
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the list of queried row as hash or null if no row is found
	 *
	 * @note
	 * 	Throws an exception when more than one row is found
	 */
	public static Map<String, Object> executeRow(PreparedStatement statement, Object[] binds)
	{
		try {
			ResultSet rs = bindParams(statement, binds).executeQuery();
			Map<String, Object> rowMap;
			if ((rowMap = fetchMap(rs)) == null)
				return null;
			if (rs.next())
				throw new NonUniqueResultException("query returned more than a single row");
			return rowMap;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a prepared query with specified bindings
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the list of queried row as hash
	 *
	 * @note
	 * 	Throws an exception when not exactly one row is found
	 */
	public static Map<String, Object> executeOneRow(PreparedStatement statement, Object[] binds)
	{
		try {
			ResultSet rs = executeResult(statement, binds);
			Map<String, Object> rowMap;
			if ((rowMap = fetchMap(rs)) == null)
				throw new NoResultException();
			if (rs.next())
				throw new NonUniqueResultException();
			return rowMap;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a prepared query with specified bindings and returns the first column
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return null
	 * 	if no row is found
	 * @return
	 * 	the list of queried row as hash or null if no row is found
	 *
	 * @note
	 * 	Throws an exception when more than one row is returned
	 */
	public static Object		executeColumn(PreparedStatement statement, Object[] binds)
	{
		try {
			ResultSet rs = executeResult(statement, binds);
			if (!rs.next())
				return null;
			if (rs.next())
				throw new NonUniqueResultException();
			return rs.getObject(1);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a prepared query with specified bindings and returns the first column
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the list of queried row as hash or null if no row is found
	 *
	 * @note
	 * 	Throws an exception when not exactly one row is returned
	 */
	public static Object		executeOneColumn(PreparedStatement statement, Object[] binds)
	{
		try {
			ResultSet rs = executeResult(statement, binds);
			if (!rs.next())
				throw new NoResultException();
			if (rs.next())
				throw new NonUniqueResultException();
			return rs.getObject(1);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a prepared update-type with specified bindings and returns the
	 * number of rows affected
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the number of rows affected
	 */
	public static long		executeUpdateAny(PreparedStatement statement, Object[] binds)
	{
		try {
			return bindParams(statement, binds).executeUpdate();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a prepared update-type with specified bindings and checks exactly one row was updated
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the number of rows updated, i.e. 1
	 *
	 * @note
	 * 	throws an exception when not exactly one row was updated
	 */
	public static long		executeUpdateOne(PreparedStatement statement, Object[] binds)
	{
		long c;
		if ((c = executeUpdateAny(statement, binds)) != 1) {
			if (c == 0)
				throw new NoResultException("query did not udpate exactly one row");
			else
				throw new NonUniqueResultException("query did not udpate exactly one row");
		}
		return c;
	}

	/**
	 * runs a prepared insert with specific bindings and returns the last insert
	 *
	 * @param statement
	 * 	the prepared statement
	 * @param binds
	 * 	bind variables for this query
	 *
	 * @return
	 * 	the newly generated serial id
	 */
	public static long		executeInsertSerial(PreparedStatement statement, Object[] binds)
	{
		try {
			int c = bindParams(statement, binds).executeUpdate();
			if (c != 1) {
				if (c == 0)
					throw new NoResultException("query did not udpate exactly one row");
				else
					throw new NonUniqueResultException("query did not udpate exactly one row");
			}
			return getInsertId(statement);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a dynamic insert statement
	 *
	 * @param connection
	 * 	the connection
	 * @param table
	 * 	table name
	 * @param values
	 * 	hash list of column: value
	 *
	 * @return
	 * 	the number of affected columns
	 */
	public static long		runInsert(Connection connection, String table, Map<String, Object> values)
	{
		try {
			StringBuilder sb = new StringBuilder("INSERT INTO ").append(table).append(" (");
			String c = ""; String v = "";
			for (String key: values.keySet()) {
				sb.append(key).append(", ");
			}
			sb.replace(sb.length()-2, sb.length(), ") VALUES (");
			Object[] binds = values.values().toArray(new Object[values.size()]);
			for (Object b: binds) {
				sb.append("?, ");
			}
			sb.replace(sb.length()-2, sb.length(), ")");
			PreparedStatement st = connection.prepareStatement(sb.toString());
			bindParams(st, binds);
			return st.executeUpdate();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a dynamic insert statement, returning the insert id
	 *
	 * @param connection
	 * 	the connection
	 * @param table
	 * 	table name
	 * @param values
	 * 	hash list of column, value
	 *
	 * @return
	 * 	the serial inserted id
	 */
	public static long		runInsertSerial(Connection connection, String table, Map<String, Object> values)
	{
		try {
			StringBuilder sb = new StringBuilder("INSERT INTO ").append(table).append(" (");
			String c = ""; String v = "";
			for (String key: values.keySet()) {
				sb.append(key).append(", ");
			}
			sb.replace(sb.length()-2, sb.length(), ") VALUES (");
			Object[] binds = values.values().toArray(new Object[values.size()]);
			for (Object b: binds) {
				sb.append("?, ");
			}
			sb.replace(sb.length()-2, sb.length(), ")");
			return executeInsertSerial(connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS), values.values().toArray(new Object[values.size()]));
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a dynamic update statement, returning the number of rows affected
	 *
	 * @param connection
	 * 	the connection
	 * @param table
	 * 	table name
	 * @param values
	 * 	map of column to value
	 * @param key
	 * 	map of column to value
	 *
	 * @return
	 * 	the number of rows affected
	 */
	public static long		runUpdate(Connection connection, String table, Map<String, Object> values, Map<String, Object> key)
	{
		try {
			List<Object> binds = new LinkedList<Object>();
			StringBuilder sb = new StringBuilder("UPDATE ").append(table).append(" SET ");
			for (Map.Entry<String, Object> entry: values.entrySet()) {
				sb.append(entry.getKey()).append(" = ?, ");
				binds.add(entry.getValue());
			}
			sb.replace(sb.length()-2, sb.length(), " WHERE ");
			for (Map.Entry<String, Object> entry: key.entrySet()) {
				sb.append(entry.getKey()).append(" = ? AND ");
				binds.add(entry.getValue());
			}
			sb.replace(sb.length()-5, sb.length(), "");
			PreparedStatement st = connection.prepareStatement(sb.toString());
			bindParams(st, binds.toArray());
			return st.executeUpdate();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Runs a dynamic update statement or inserts the record if now row was affected.
	 *
	 * @param connection
	 * 	the connection
	 * @param table
	 * 	table name
	 * @param values
	 * 	map of column to value
	 * @param key
	 * 	map of column to value
	 *
	 * @return
	 * 	the number of rows affected
	 */
	public static long		runUpdateInsert(Connection connection, String table, Map<String, Object> values, Map<String, Object> key)
	{
		try {
			long affected;
			if ((affected = runUpdate(connection, table, values, key)) != 0)
				return affected;
			List<Object> binds = new LinkedList<Object>();
			StringBuilder sb = new StringBuilder("INSERT INTO ").append(table).append(" (");
			for (Map.Entry<String, Object> entry: values.entrySet()) {
				sb.append(entry.getKey()).append(", ");
				binds.add(entry.getValue());
			}
			for (Map.Entry<String, Object> entry: key.entrySet()) {
				sb.append(entry.getKey()).append(", ");
				binds.add(entry.getValue());
			}
			sb.replace(sb.length()-2, sb.length(), ") VALUES (");
			for (Object bind: binds) {
				sb.append("?, ");
			}
			sb.replace(sb.length()-2, sb.length(), ")");
			return executeUpdateAny(connection.prepareStatement(sb.toString()), binds.toArray());
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a dynamic delete statement, returning the number of rows affected
	 *
	 * @param connection
	 * 	the connection
	 * @param table
	 * 	table name
	 * @param key
	 * 	hash list of column to value
	 *
	 * @return
	 * 	the number of rows affected
	 */
	public static long		runDelete(Connection connection, String table, Map<String, Object> key)
	{
		try {
			StringBuilder sb = new StringBuilder("DELETE FROM ").append(table).append(" WHERE ");
			for (Map.Entry<String, Object> entry: key.entrySet()) {
				sb.append(entry.getKey()).append(" = ? AND ");
			}
			sb.replace(sb.length()-2, sb.length(), "");
			return executeUpdateAny(connection.prepareStatement(sb.toString()), key.values().toArray(new Object[key.size()]));
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a dynamic select statement, returning list of rows as associative arrays
	 *
	 * @param connection
	 * 	the connection
	 * @param columns
	 * 	the required columns
	 * @param table
	 * 	table name
	 * @param filter
	 * 	list of column to value pairs
	 *
	 * @return
	 * 	the list of rows as associative arrays
	 */
	public static List<Map<String, Object>> runQuery(Connection connection, String[] columns, String table, Map<String, Object> filter)
	{
		try {
			StringBuilder sb = new StringBuilder("SELECT ");
			for (String column: columns) {
				sb.append(column).append(", ");
			}
			sb.replace(sb.length()-2, sb.length(), " FROM ").append(table).append(" ");
			for (Map.Entry<String, Object> entry: filter.entrySet()) {
				sb.append(entry.getKey()).append(" = ? AND ");
			}
			sb.replace(sb.length()-5, sb.length(), "");
			PreparedStatement st = connection.prepareStatement(sb.toString());
			return executeRows(st, filter.values().toArray(new Object[filter.size()]));
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * runs a dynamic select statement, returning the value of the requested field
	 *
	 * @param connection
	 * 	the connection
	 * @param field
	 * 	the required field
	 * @param table
	 * 	table name
	 * @param filter_name
	 * 	name of column to be filtered
	 * @param filter_value
	 * 	value of column to be filtered
	 *
	 * @return null
	 * 	if no row was found
	 * @return field value
	 * 	if the entry was found
	 *
	 * @note
	 * 	Throws an exception if more than one row is found
	 */
	public static Object		runField(Connection connection, String field, String table, String filter_name, Object filter_value)
	{
		try {
			String s = "SELECT "+field+" FROM "+table+" WHERE "+filter_name+" = ?";
			PreparedStatement st = connection.prepareStatement(s);
			st.setObject(1, filter_value);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				Object value = rs.getObject(1);
				if (rs.next())
					throw new NonUniqueResultException("query returned more rows");
				return value;
			}
			return null;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Reads the last insert id from the statement.
	 *
	 * @param statement
	 * 	the statement
	 *
	 * @return
	 * 	the last insert id
	 *
	 * @note
	 * 	Throws an exception in case retrieval failed
	 */
	public static long		getInsertId(PreparedStatement statement)
	{
		try {
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			}
			throw new SQLException("failed to retrieve last insert value.");
		}
		catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Fetches single row from result set into map.
	 *
	 * @param rs
	 * 	result set
	 *
	 * @return null
	 * 	if no row was found
	 * @return row map
	 * 	if row was fetched
	 */
	public static Map<String, Object> fetchMap(ResultSet rs)
	{
		try {
			if (!rs.next())
				return null;
			return buildMap(rs);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Binds parameters to statement.
	 *
	 * @param statement
	 * 	statement to bind
	 * @param binds
	 * 	binds to be bound
	 *
	 * @return
	 *	original statement
	 */
	protected static PreparedStatement bindParams(PreparedStatement statement, Object[] binds)
	{
		try {
			for (int i = 0; i < binds.length; i++)
				statement.setObject(i+1, binds[i]);
			return statement;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Builds map from current row.
	 *
	 * @param rs
	 * 	result set
	 *
	 * @return
	 *	map of current row
	 */
	protected static Map<String, Object> buildMap(ResultSet rs)
	{
		try {
			Map<String, Object> rowHash = new HashMap<String, Object>();
			ResultSetMetaData rsm = rs.getMetaData();
			for (int i = 0, c = rsm.getColumnCount(); i < c; ++i) {
				rowHash.put(rsm.getColumnName(i+1), rs.getObject(i+1));
			}
			return rowHash;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

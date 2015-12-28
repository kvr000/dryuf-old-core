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

package net.dryuf.sql.test;

import net.dryuf.tenv.AppTenvObject;
import net.dryuf.util.MapUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import net.dryuf.sql.SqlHelper;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class SqlInsertSerialTest extends AppTenvObject
{
	@Test
	public void			testExecuteSerial() throws SQLException
	{
		Connection connection = this.dataSource.getConnection();
		connection.setAutoCommit(false);
		try {
			SqlHelper.updateAny(connection, "DELETE FROM DryufStIncTable WHERE name LIKE ?", new Object[]{ SqlInsertSerialTest.class.getName()+".testExecuteSerial-%"});
			PreparedStatement st_insertIncTable = connection.prepareStatement("INSERT INTO DryufStIncTable (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			long id0 = SqlHelper.executeInsertSerial(st_insertIncTable, new Object[]{ SqlInsertSerialTest.class.getName()+".testExecuteSerial-name0" });
			long id1 = SqlHelper.executeInsertSerial(st_insertIncTable, new Object[]{ SqlInsertSerialTest.class.getName()+".testExecuteSerial-name1" });
			Assert.assertTrue("id0 != id1", id1 != id0);
			connection.commit();
		}
		finally {
			connection.close();
		}
	}

	@Test
	public void			testRunSerial()
	{
		Connection connection = SqlHelper.getDataSourceConnection(this.dataSource);
		SqlHelper.setAutoCommit(connection, false);
		try {
			SqlHelper.updateAny(connection, "DELETE FROM DryufStIncTable WHERE name LIKE ?", new Object[]{SqlInsertSerialTest.class.getName()+".testRunSerial-%"});
			long id0 = SqlHelper.runInsertSerial(connection, "DryufStIncTable", MapUtil.createLinkedHashMap("name", SqlInsertSerialTest.class.getName()+".testRunSerial-name0"));
			long id1 = SqlHelper.runInsertSerial(connection, "DryufStIncTable", MapUtil.createLinkedHashMap("name", SqlInsertSerialTest.class.getName()+".testRunSerial-name1"));
			Assert.assertTrue("id0 != id1", id1 != id0);
			SqlHelper.commitConnection(connection);
		}
		finally {
			SqlHelper.closeConnection(connection);
		}
	}

	@Inject
	@Named("javax.sql.DataSource-dryuf")
	protected DataSource		dataSource;
}

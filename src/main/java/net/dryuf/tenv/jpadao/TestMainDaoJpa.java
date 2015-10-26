package net.dryuf.tenv.jpadao;

import net.dryuf.tenv.TestMain;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class TestMainDaoJpa extends net.dryuf.dao.DryufDaoContext<TestMain, Long> implements net.dryuf.tenv.dao.TestMainDao
{

	public				TestMainDaoJpa()
	{
		super(TestMain.class);
	}

}

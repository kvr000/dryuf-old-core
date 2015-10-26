package net.dryuf.tenv.jpadao;

import net.dryuf.tenv.TestChild;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class TestChildDaoJpa extends net.dryuf.dao.DryufDaoContext<TestChild, net.dryuf.tenv.TestChild.Pk> implements net.dryuf.tenv.dao.TestChildDao
{

	public				TestChildDaoJpa()
	{
		super(TestChild.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<TestChild>		listByCompos(Long compos)
	{
		return (List<TestChild>)entityManager.createQuery("FROM TestChild WHERE pk.testId = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(Long compos)
	{
		return entityManager.createQuery("DELETE FROM TestChild obj WHERE obj.pk.testId = ?1").setParameter(1, compos).executeUpdate();
	}

}

package net.dryuf.security.jpadao;

import net.dryuf.security.AppDomainDef;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AppDomainDefDaoJpa extends net.dryuf.dao.DryufDaoContext<AppDomainDef, String> implements net.dryuf.security.dao.AppDomainDefDao
{

	public				AppDomainDefDaoJpa()
	{
		super(AppDomainDef.class);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public AppDomainDef		loadByAlias(String domainAlias)
	{
		@SuppressWarnings("unchecked")
		List<AppDomainDef> result = entityManager.createQuery("SELECT dd FROM DomainDef dd WHERE dd.domain IN (SELECT da.domain FROM DomainAlias da WHERE da.domainAlias = ?1)").setParameter(1, domainAlias).getResultList();
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

}

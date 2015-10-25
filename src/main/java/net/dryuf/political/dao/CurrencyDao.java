package net.dryuf.political.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.political.Currency;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface CurrencyDao extends net.dryuf.dao.DynamicDao<Currency, String>
{
	public Currency			refresh(Currency obj);
	public Currency			loadByPk(String pk);
	public List<Currency>		listAll();
	public void			insert(Currency obj);
	public void			insertTxNew(Currency obj);
	public Currency			update(Currency obj);
	public void			remove(Currency obj);
	public boolean			removeByPk(String pk);

	public String			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<Currency> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<Currency> holder);
	public Currency			createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<Currency>	retrieveDynamic(EntityHolder<?> composition, String pk);
	public Currency			updateDynamic(EntityHolder<Currency> roleObject, String pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, String pk);
	public long			listDynamic(List<EntityHolder<Currency>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}

package tech.lihx.demo.core.mybatis.mybatis.transaction;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.transaction.TransactionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.HeuristicCompletionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import tech.lihx.demo.core.mybatis.mybatis.rw.IDataSourceService;

@SuppressWarnings( { "serial", "unchecked" } )
public class BestEffortMultiDataSourceTransactionManager extends AbstractPlatformTransactionManager implements
		InitializingBean {

	private IDataSourceService dataSourceService;

	protected List<AbstractPlatformTransactionManager> transactionManagers;


	public BestEffortMultiDataSourceTransactionManager() {
	}


	@Override
	protected Object doGetTransaction() throws TransactionException {
		return new ArrayList<DefaultTransactionStatus>();
	}


	@Override
	protected void doBegin( Object o, TransactionDefinition transactionDefinition ) throws TransactionException {
		List<TransactionStatus> statusList = (List<TransactionStatus>) o;
		for ( AbstractPlatformTransactionManager transactionManager : transactionManagers ) {
			statusList.add(transactionManager.getTransaction(transactionDefinition));
		}
	}


	@Override
	protected void doCommit( DefaultTransactionStatus defaultTransactionStatus ) throws TransactionException {
		MultipleCauseException ex = null;
		List<TransactionStatus> statusList = (List<TransactionStatus>) defaultTransactionStatus.getTransaction();
		for ( int i = transactionManagers.size() - 1 ; i >= 0 ; i-- ) {
			AbstractPlatformTransactionManager transactionManager = transactionManagers.get(i);
			TransactionStatus status = statusList.get(i);
			try {
				transactionManager.commit(status);
			} catch ( TransactionException e ) {
				if ( ex == null ) {
					ex = new MultipleCauseException();
				}
				ex.add(e);
			}
		}
		if ( ex != null && !ex.getCauses().isEmpty() ) { throw new HeuristicCompletionException(
				HeuristicCompletionException.STATE_UNKNOWN, ex); }

	}


	@Override
	protected void doRollback( DefaultTransactionStatus defaultTransactionStatus ) throws TransactionException {
		MultipleCauseException ex = null;
		List<TransactionStatus> statusList = (List<TransactionStatus>) defaultTransactionStatus.getTransaction();
		for ( int i = transactionManagers.size() - 1 ; i >= 0 ; i-- ) {
			AbstractPlatformTransactionManager transactionManager = transactionManagers.get(i);
			TransactionStatus status = statusList.get(i);
			try {
				transactionManager.rollback(status);
			} catch ( TransactionException e ) {
				if ( ex == null ) {
					ex = new MultipleCauseException();
				}
				ex.add(e);
			}
		}
		if ( ex != null && !ex.getCauses().isEmpty() ) { throw new UnexpectedRollbackException(
				"one or more error on rolling back the transaction", ex); }
	}


	public IDataSourceService getDataSourceService() {
		return dataSourceService;
	}


	public void setDataSourceService( IDataSourceService dataSourceService ) {
		this.dataSourceService = dataSourceService;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if ( dataSourceService == null ) { throw new IllegalArgumentException("'dataSourceService' is required."); }
		transactionManagers = new ArrayList<AbstractPlatformTransactionManager>();
		for ( DataSource dataSource : dataSourceService.getDataSources().values() ) {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
			transactionManager.setDefaultTimeout(getDefaultTimeout());
			transactionManager.setTransactionSynchronization(getTransactionSynchronization());
			transactionManagers.add(transactionManager);
		}
		setTransactionSynchronization(SYNCHRONIZATION_NEVER);
	}
}

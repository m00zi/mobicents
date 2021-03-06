package org.mobicents.slee.resource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.UnrecognizedActivityHandleException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class SleeEndpointEndActivityNotTransactedExecutor {
					
	private final SleeContainer sleeContainer;
	
	private final SleeEndpointImpl sleeEndpoint;
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	public SleeEndpointEndActivityNotTransactedExecutor(SleeContainer sleeContainer,SleeEndpointImpl sleeEndpoint) {
		this.sleeContainer = sleeContainer;
		this.sleeEndpoint = sleeEndpoint;
	}
	
	void execute(final ActivityHandle handle) throws UnrecognizedActivityHandleException {
		
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		
		final Runnable runnable = new Runnable() {
			
			public void run() {
				boolean rollback = true;
				try {
					txManager.begin();
					sleeEndpoint._endActivity(handle);	            	
			        rollback = false;            	
				} catch (NotSupportedException e) {
					throw new SLEEException(e.getMessage(),e);
				} catch (SystemException e) {
					throw new SLEEException(e.getMessage(),e);
				}
				finally {		        		
					try {
						if (rollback) {
							txManager.rollback();
						}
						else {
							txManager.commit();
						}
					} catch (Throwable e) {
						throw new SLEEException(e.getMessage(),e);
					}
		        }	
			}
		};
		
		Transaction tx = null;
		try {
			tx = txManager.getTransaction();
			if (tx != null) {
				txManager.suspend();
			}
			runnable.run();
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		finally {
			if (tx != null) {
				try {
					txManager.resume(tx);
				}
				catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
			}
		}
		/*
		if (runInThisThread) {
			runnable.run();
		}
		else {
			try {
				executorService.submit(runnable).get();
			} catch (ExecutionException e) {
				final Throwable realException = e.getCause();
				if (realException instanceof UnrecognizedActivityHandleException) {
					throw (UnrecognizedActivityHandleException) realException;
				}
				else if (realException instanceof SLEEException) {
					throw (SLEEException) realException;
				} 
				else {
					throw new SLEEException(e.getMessage(),e);
				}
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}*/
	}
	
}

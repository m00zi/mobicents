package javax.megaco.association;

import javax.megaco.AssociationEvent;
import javax.megaco.ErrorCode;
import javax.megaco.InvalidArgumentException;
import javax.megaco.MethodInvocationException;
import javax.megaco.ReturnStatus;

public class CreateAssocResp extends AssociationEvent {

	protected ReturnStatus eventStatus = null;
	protected ErrorCode errorCode = null;

	public CreateAssocResp(Object source, int assocHandle)
			throws InvalidArgumentException {
		super(source, assocHandle);

	}

	@Override
	public final int getAssocOperIdentifier() {
		return AssocEventType.M_CREATE_ASSOC_RESP;
	}

	/**
	 * This method returns whether the execution of create association request
	 * was success or not.
	 * 
	 * @return Returns an integer value that identifies whether the delete
	 *         association event issued earlier could be performed successfuly
	 *         or not. The values are field constants defined in class
	 *         ReturnStatus. If the returnStatus is not set, then this method
	 *         would return value 0.
	 */
	public final int getEventStatus() {
		return eventStatus == null ? 0 : eventStatus.getReturnStatus();
	}

	/**
	 * This method sets the status of whether the execution of the create
	 * association request was success or not.
	 * 
	 * @param returnStatus
	 *            - The return status of the processing of the create
	 *            association event. The static object corresponding to the
	 *            return status which are one of the derived classes of the
	 *            ReturnStatus shall be set.
	 * @throws javax.megaco.InvalidArgumentException
	 *             This exception is raised if the reference of Return Status
	 *             passed to this method is NULL.
	 */
	public final void setEventStatus(ReturnStatus returnStatus)
			throws javax.megaco.InvalidArgumentException {
		if (returnStatus == null)
			throw new InvalidArgumentException("Event status can not be null.");

		this.eventStatus = returnStatus;
	}

	/**
	 * This method returns the error code qualifying why the create association
	 * event could not be processed successfuly.
	 * 
	 * @return Returns an integer value that identifies the error code
	 *         specifying why the execution of the create association event
	 *         could not be successful. The possible values are field constants
	 *         defined for the class ErrorCode. If the error code is not set,
	 *         then this method would return value 0.
	 * @throws MethodInvocationException
	 * @throws MethodInvocationException
	 *             - This exception would be raised if the return status is set
	 *             to M_SUCCESS, the error code is not set and hence should not
	 *             invoke this method.
	 */
	public final int getErrorCode() throws MethodInvocationException {
		if (getEventStatus() == ReturnStatus.M_SUCCESS) {
			throw new MethodInvocationException(
					"Event status is success, error code is not premited.");
		}
		return errorCode == null ? 0 : errorCode.getErrorCode();
	}

	/**
	 * This method sets the error code specifying why the delete association
	 * event could not be executed successfuly.
	 * 
	 * @param errorCode
	 *            The error code corresponding to why the create association
	 *            event could not be executed successfuly.
	 * @throws javax.megaco.InvalidArgumentException
	 *             This exception would be raised in following conditions <br>
	 *             1. If the return status is not set to M_FAILURE, the error
	 *             code should not be set. <br>
	 *             2. If the error code object passed to this method is set to
	 *             NULL.
	 */
	public final void setErrorCode(ErrorCode errorCode)
			throws javax.megaco.InvalidArgumentException {
		if (errorCode == null)
			throw new InvalidArgumentException("Error code can not be null.");
		if (getEventStatus() != ReturnStatus.M_FAILURE) {
			throw new InvalidArgumentException(
					"Event status is not failure, error code is not premited.");
		}
		this.errorCode = errorCode;
	}

}